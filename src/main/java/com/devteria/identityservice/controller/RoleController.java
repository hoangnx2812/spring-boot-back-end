package com.devteria.identityservice.controller;


import com.devteria.identityservice.dto.request.RoleReq;
import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.dto.response.RoleResponse;
import com.devteria.identityservice.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class RoleController {

    RoleService roleService;

    @GetMapping("/get-all")
    public ApiResponse<List<RoleResponse>> getAll() {
        return  ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .data(roleService.findAll())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<RoleResponse> create(@RequestBody RoleReq roleReq) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Role created successfully")
                .data(roleService.create(roleReq))
                .build();
    }

    @DeleteMapping("/delete/{name}")
    public ApiResponse<Void> delete(@PathVariable String name) {
        roleService.delete(name);
        return ApiResponse.<Void> builder()
                .code(200)
                .message("Role deleted successfully")
                .build();
    }
}
