package com.devteria.identityservice.dto.response;


import com.devteria.identityservice.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String dateOfBirth;
    Set<Role> roles;
}
