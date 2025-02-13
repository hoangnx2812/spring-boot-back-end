package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.UserCreateReq;
import com.devteria.identityservice.dto.request.UserUpdateReq;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

   UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void createUser(UserCreateReq userCreateRequest) {
        User user = User.builder()
                .username(userCreateRequest.getUsername())
                .password(userCreateRequest.getPassword())
                .firstName(userCreateRequest.getFirstName())
                .lastName(userCreateRequest.getLastName())
                .dateOfBirth(userCreateRequest.getDateOfBirth())
                .build();
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void updateUser(UserUpdateReq userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.getId())
                .map(o -> {
                    o.setFirstName(userUpdateRequest.getFirstName());
                    o.setLastName(userUpdateRequest.getLastName());
                    o.setDateOfBirth(userUpdateRequest.getDob());
                    return o;
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.save(user);
    }


}
