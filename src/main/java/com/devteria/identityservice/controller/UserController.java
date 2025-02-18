package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.UserCreateReq;
import com.devteria.identityservice.dto.request.UserUpdateReq;
import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class UserController {

    UserService userService;

    @GetMapping("/get-all")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody UserCreateReq userCreateRequest) {
        userService.createUser(userCreateRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .message("User created successfully")
                .data(userCreateRequest)
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody UserUpdateReq userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
    }
}
