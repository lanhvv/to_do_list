package com.example.demo.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Lấy jwt từ request
        String jwt = tokenProvider.getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // Lấy id user từ chuỗi jwt
            String email = tokenProvider.getUserIdFromJWT(jwt);
            // Lấy thông tin người dùng từ id
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            if(userDetails != null) {
                // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                // method authenticated chỉ sử dụng khi login để validate username, password nhé còn method hiện tại để thực hiện authorization
                // tránh nhầm lẫn
                // sau khi login chỉ cần lấy username để lấy được User và Authorities thôi
                // tránh nhầm lẫn <điều quan trọng nhắc 2 lần vì sau khi học lại do không ôn lại quên>
                // khác với Security Basic nhé
            }
        }
        filterChain.doFilter(request, response);
    }

}

