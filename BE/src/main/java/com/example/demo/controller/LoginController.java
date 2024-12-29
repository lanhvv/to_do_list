package com.example.demo.controller;

import com.example.demo.data.request.LoginDTORQ;
import com.example.demo.data.request.RegisterDTORQ;
import com.example.demo.data.response.UserDTORS;
import com.example.demo.service.UserService;
import com.example.demo.utils.PathUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathUtil.ROOT)
public class LoginController {
    private final UserService userService;


    public LoginController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping(PathUtil.REGISTER)
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTORQ userDTORQ) {
        this.userService.register(userDTORQ);
        return  ResponseEntity.status(HttpStatus.OK).body(null); // method body có formal type để convert :))
    }

    @PostMapping(PathUtil.LOGIN)
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDTORQ loginDTORQ) {
        UserDTORS user = this.userService.login(loginDTORQ);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
