package com.example.demo.data.response;

import java.util.List;

public class UserDTORS {
    private String email;
    private List<RoleDTORS> roles;
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTORS> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTORS> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
