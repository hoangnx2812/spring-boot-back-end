package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.entity.Permission;
import com.devteria.identityservice.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/permission")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class PermissionController {

    PermissionService permissionService;

    @GetMapping("/get-all")
    public ApiResponse<List<Permission>> getAllPermissions() {
        return  ApiResponse.<List<Permission>>builder()
                .code(200)
                .data(permissionService.findAll())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<Permission> createPermission(@RequestBody Permission permission) {
        return ApiResponse.<Permission>builder()
                .code(200)
                .message("Permission created successfully")
                .data(permissionService.create(permission))
                .build();
    }

    @DeleteMapping("/delete/{name}")
    public ApiResponse<Void> deletePermission(@PathVariable String name) {
        permissionService.delete(name);
        return ApiResponse.<Void> builder()
                .code(200)
                .message("Permission deleted successfully")
                .build();
    }

}
