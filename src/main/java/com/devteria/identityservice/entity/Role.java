package com.devteria.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "role")
public class Role {

    @Id
    @Column(name = "name")
    String name;

    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    String description;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Permission> permissions;

}
