package com.example.demo.data.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class LoginDTORQ {
    @NotNull
    private String email;

    @NotNull
    @Length
    private String password;

    public  String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public  String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
