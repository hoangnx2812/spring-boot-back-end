package com.devteria.identityservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "invalidated_token")
public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime;
}
