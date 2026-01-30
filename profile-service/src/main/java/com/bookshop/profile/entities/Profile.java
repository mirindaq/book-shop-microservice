package com.bookshop.profile.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;


    @Column(nullable = false, unique = true)
    String userId;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String email;

    @Column(length = 100)
    String firstName;

    @Column(length = 100)
    String lastName;

    @Column(length = 20)
    String phone;

    @Column(length = 500)
    String avatarUrl;

    @Column(nullable = false, updatable = false)
    Instant createdAt;

    Instant updatedAt;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
