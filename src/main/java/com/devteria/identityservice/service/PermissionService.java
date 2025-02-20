package com.devteria.identityservice.service;

import com.devteria.identityservice.entity.Permission;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;


    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission create(Permission permission) {
        Permission permissions1 = Permission.builder()
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
        return  permissionRepository.save(permissions1);
    }

    public void delete(String name) {
        permissionRepository.deleteById(name);
    }

    public Permission findByName(String name) {
        return permissionRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTS));
    }

}
