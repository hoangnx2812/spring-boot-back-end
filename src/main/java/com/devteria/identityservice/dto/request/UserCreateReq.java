package com.devteria.identityservice.dto.request;

import com.devteria.identityservice.validator.DateOfBirthConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateReq {

    @Size(min = 4, message = "User name must be at least 4 characters")
    String username;

    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;

    @DateOfBirthConstraint(min = 18)
    @NotNull(message = "Date of birth is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;

    Set<String> roles;
}
