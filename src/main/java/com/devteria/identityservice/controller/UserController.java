package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.UserCreateReq;
import com.devteria.identityservice.dto.request.UserUpdateReq;
import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class UserController {

    UserService userService;

    @GetMapping("/get-all")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .data(userService.getAll())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<User> createUser(@Valid @RequestBody UserCreateReq userCreateRequest) {
        return ApiResponse.<User>builder()
                .code(200).message("User created successfully")
                .data(userService.createUser(userCreateRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("User deleted successfully")
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@Valid @RequestBody UserUpdateReq userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User updated successfully").
                data(userService.updateUser(userUpdateRequest))
                .build();
    }

    @GetMapping("/find-by-id/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .data(userService.findByIdResponse(id))
                .build();
    }

    @GetMapping("/get-info")
    public ApiResponse<UserResponse> getInfo() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .data(userService.getInfo())
                .build();
    }


}
