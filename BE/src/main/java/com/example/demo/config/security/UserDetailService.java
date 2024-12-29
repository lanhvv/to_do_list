package com.example.demo.config.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    // cung cấp cho authentication để biết cách lấy dữ liệu
    // có username, passowrd, role thì có thể compare rồi
    // lưu ý quan trọng spring security cung cấp cho ta đẩy đủ mọi thứ để phát triển Basic Authen rất đầy đủ và tuyệt vời
    // Nhưng thằng JWT là 1 bên thứ 3 mình dùng để tăng tính bảo mật token -> thế thôi đừng nhầm lẫn
    // Bạn đang nhầm thằng này chỉ dùng cho JWT thôi đấy con chó
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userDB = this.userRepo.findUserByEmail(email);
        if (!ObjectUtils.isEmpty(userDB)) {
            return new org.springframework.security.core.userdetails.User(
                    userDB.getEmail(),
                    userDB.getPassword(),
                    true, true, true, true, // trạng thái User false thì có vấn đề là không dùng được nữa
                    userDB.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toSet()) // lấy ra danh sách Role mà User đang sỡ hữu để check
            );
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
