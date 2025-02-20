package com.devteria.identityservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "permission")
public class Permission {
    @Id
    @Column(name = "name")
    String name;

    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    String description;
}
