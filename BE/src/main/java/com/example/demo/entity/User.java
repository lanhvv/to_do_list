package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "user")
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user", // xác định bảng trung gian
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, // tham chiếu cột của bảng đó đến bảng User
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")} // tham chiếu của cột đó đến bảng Role
    )
    private Set<Role> roles;

    public User(String email, String password, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
