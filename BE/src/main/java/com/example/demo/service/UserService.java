package com.example.demo.service;

import com.example.demo.data.request.LoginDTORQ;
import com.example.demo.data.request.RegisterDTORQ;
import com.example.demo.data.response.UserDTORS;

public interface UserService {
    void register(RegisterDTORQ userDTORQ);
    UserDTORS login(LoginDTORQ loginDTORQ);
}
