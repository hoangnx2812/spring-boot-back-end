package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.AuthenticationReq;
import com.devteria.identityservice.dto.request.IntrospectReq;
import com.devteria.identityservice.dto.request.LogoutReq;
import com.devteria.identityservice.dto.request.RefreshTokenReq;
import com.devteria.identityservice.dto.response.AuthenticationResponse;
import com.devteria.identityservice.dto.response.IntrospectResponse;
import com.devteria.identityservice.entity.InvalidatedToken;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.repository.InvalidatedTokenRepository;
import com.devteria.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String jwtSecret;

    @NonFinal
    @Value("${jwt.valid-duration}")
    Long validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    Long refreshDuration;

    public AuthenticationResponse authenticate(AuthenticationReq authenticationReq) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(authenticationReq.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));
        boolean authenticated = passwordEncoder.matches(authenticationReq.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .build();

    }

    // Kiểm tra xem token còn hiệu lực không
    public IntrospectResponse introspect(IntrospectReq introspectReq) throws JOSEException, ParseException {
        boolean isValid = true;
        try {
            verifyToken(introspectReq.getToken(), false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutReq logoutReq) throws ParseException, JOSEException {
        try {
            SignedJWT signToken = verifyToken(logoutReq.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            log.info(e.getMessage());
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenReq request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(), true);
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        var userName = signToken.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        generateToken(user);
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .build();
    }


    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes()); // tạo verifier
        SignedJWT signedJWT = SignedJWT.parse(token); // parse token
        Date expirationTime = isRefresh
                ? Date.from(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(refreshDuration, ChronoUnit.SECONDS))
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier); // kiểm tra token
        if (!verified || expirationTime.before(new Date()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512); // tạo header, chứa thuật toán
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder() // tạo claims, nội dung
                .subject(user.getUsername())
                .issuer("hoangnx2812") // được phát hành từ ai
                .issueTime(new Date()) // thời gian phát hành
                .expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli())) // thời gian hết hạn
                .jwtID(UUID.randomUUID().toString()) // id của token
                .claim("scope", buildScope(user)) // thêm claim
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // tạo payload thông qua dữ liệu của claims
        JWSObject jwsObject = new JWSObject(jwsHeader, payload); // tạo token
        try {
            jwsObject.sign(new MACSigner(jwtSecret.getBytes())); // ký token
            return jwsObject.serialize(); // trả về token
        } catch (JOSEException e) {
            log.error("Error signing token", e);
            throw new IllegalArgumentException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
            });
        }
        return joiner.toString();
    }
}
