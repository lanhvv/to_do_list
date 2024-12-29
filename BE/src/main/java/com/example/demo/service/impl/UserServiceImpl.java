package com.example.demo.service.impl;

import com.example.demo.config.exception.policy.BusinessLogicException;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.data.mapper.ToDoUserMapper;
import com.example.demo.data.request.LoginDTORQ;
import com.example.demo.data.request.RegisterDTORQ;
import com.example.demo.data.response.UserDTORS;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;
import com.example.demo.utils.MessageUtil;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ToDoUserMapper toDoUserMapper;

    public UserServiceImpl(
            BCryptPasswordEncoder passwordEncoder,
            UserRepo userRepo,
            RoleRepo roleRepo,
            JwtTokenProvider jwtTokenProvider,
            ToDoUserMapper toDoUserMapper
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtTokenProvider = jwtTokenProvider;
        this.toDoUserMapper = toDoUserMapper;
    }

    @Transactional
    @Override
    public void register(RegisterDTORQ userDTORQ) {
        User user = this.userRepo.findUserByEmail(userDTORQ.getEmail());
        Set<Role> roles = userDTORQ.getRole().stream()
                .map(role -> {
                    Role roleDB = this.roleRepo.findRoleByCode(role);
                    if (ObjectUtils.isEmpty(roleDB)) {
                        throw new BusinessLogicException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageUtil.ROLE_NOT_EXITS, HttpStatus.BAD_REQUEST, role);
                    }
                    return roleDB;
                }).collect(Collectors.toSet());


        if (ObjectUtils.isNotEmpty(user)) {
            this.logger.debug(user.toString());
            throw new BusinessLogicException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageUtil.EMAIL_EXITS, HttpStatus.BAD_REQUEST);
        }

        User userRegister = new User(
                userDTORQ.getEmail(),
                this.passwordEncoder.encode(userDTORQ.getPassword()),
                roles
            );

        this.logger.debug(userRegister.toString());

        this.userRepo.save(userRegister);

        this.logger.info("Register successful");
    }

    @Transactional
    @Override
    public UserDTORS login(LoginDTORQ loginDTORQ) {
        User user = this.userRepo.findUserByEmail(loginDTORQ.getEmail());
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessLogicException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageUtil.USER_LOGIN_NOT_EXITS, HttpStatus.BAD_REQUEST);
        }
        if (!this.passwordEncoder.matches(loginDTORQ.getPassword(), user.getPassword())) {
            throw new BusinessLogicException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageUtil.PASSWORD_INVALID, HttpStatus.BAD_REQUEST);
        }

        this.logger.debug(user.toString());

        UserDTORS userDTORS = this.toDoUserMapper.UserToUserDTORS(user);
        userDTORS.setToken(this.jwtTokenProvider.generateToken(user.getEmail()));
        // Đăng nhập thành công là return token để để authorization
        this.logger.info("Login successful");
        return userDTORS;
    }
}
