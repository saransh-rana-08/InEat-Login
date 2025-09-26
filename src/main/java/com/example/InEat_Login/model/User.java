package com.example.InEat_Login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Will be hashed

    private String universityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // --- Constructors, Getters, and Setters ---

    public User() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUniversityId() { return universityId; }
    public Role getRole() { return role; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setUniversityId(String universityId) { this.universityId = universityId; }
    public void setRole(Role role) { this.role = role; }
}