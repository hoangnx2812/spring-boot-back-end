package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.UserCreateReq;
import com.devteria.identityservice.dto.request.UserUpdateReq;
import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class UserController {

    UserService userService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .data(userService.getAll())
                .build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody UserCreateReq userCreateRequest) {
        userService.createUser(userCreateRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .message("User created successfully")
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .message("User deleted successfully")
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateUser(@Valid @RequestBody UserUpdateReq userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .message("User updated successfully")
                .build());
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<?>> findById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .data(userService.findByIdResponse(id))
                .build());
    }

    @GetMapping("/get-info")
    public ResponseEntity<ApiResponse<?>> getInfo() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .data(userService.getInfo())
                .build());
    }


}
