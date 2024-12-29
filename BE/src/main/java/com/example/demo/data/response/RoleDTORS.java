package com.example.demo.data.response;

public class RoleDTORS {
    private String code;

    private String name;

    public RoleDTORS(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public RoleDTORS() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
