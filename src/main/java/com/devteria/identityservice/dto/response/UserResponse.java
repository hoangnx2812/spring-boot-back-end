package com.devteria.identityservice.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String username;
    String firstName;
    String lastName;
    String dateOfBirth;
}
