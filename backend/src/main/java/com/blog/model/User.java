package com.blog.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
    private boolean enabled = true;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}