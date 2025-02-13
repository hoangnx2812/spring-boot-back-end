package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.UserCreateReq;
import com.devteria.identityservice.dto.request.UserUpdateReq;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
        log.info("All users fetched successfully");
        return userService.getAll();
    }

    @PostMapping("/create")
    public void createUser(@RequestBody UserCreateReq userCreateRequest) {
        userService.createUser(userCreateRequest);
        log.info("User created successfully");
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        log.info("User deleted successfully");
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody UserUpdateReq userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
        log.info("User updated successfully");
    }
}
