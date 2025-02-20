package com.devteria.identityservice.service;


import com.devteria.identityservice.dto.request.UserCreateReq;
import com.devteria.identityservice.dto.request.UserUpdateReq;
import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.mapper.UserMapper;
import com.devteria.identityservice.repository.RoleRepository;
import com.devteria.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponse)
                .toList();
    }

    public User createUser(UserCreateReq userCreateRequest) {
        if (userRepository.existsByUsername(userCreateRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_NAME_EXISTS);
        }
        User user = userMapper.addUser(userCreateRequest);
        var roles = roleRepository.findAllById(userCreateRequest.getRoles());
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserResponse updateUser(UserUpdateReq userUpdateRequest) {
        var roles = roleRepository.findAllById(userUpdateRequest.getRoles());
        User user = findById(userUpdateRequest.getId());
        userMapper.updateUser(user, userUpdateRequest);
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return userMapper.userToUserResponse(user);
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    //Nếu kết quả trả về thỏa mã điều kiện thì hàm này mới đc thực thi,
    //=> Quyền truy cập phụ thuộc kết quả trả về
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse findByIdResponse(String id) {
        return userMapper.userToUserResponse(findById(id));
    }


    public UserResponse getInfo() {
        var context = SecurityContextHolder.getContext();
        String userName = context.getAuthentication().getName(); // lấy theo sub(sub sẽ lưu theo dữ liệu unique từ user)  từ token
        return userMapper.userToUserResponse(userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

}
