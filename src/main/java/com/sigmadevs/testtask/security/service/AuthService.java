package com.sigmadevs.testtask.security.service;

import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.security.config.JwtUtil;
import com.sigmadevs.testtask.security.dto.UserGetDto;
import com.sigmadevs.testtask.security.dto.UserLoginDto;
import com.sigmadevs.testtask.security.dto.UserRegistrationDto;
import com.sigmadevs.testtask.security.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ResponseEntity<UserGetDto> login(UserLoginDto userLoginDto) {
        String login = userLoginDto.getLogin();
        User user;
        user = userService.findByEmailOptional(login).orElse(null);
        if(user == null) {
            user = userService.findByUsername(login);
        }
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),userLoginDto.getPassword()));
        setCookie(user);
        return ResponseEntity.ok(userMapper.userToUserGetDto(user));
    }

    public ResponseEntity<UserGetDto> registration(UserRegistrationDto userRegistrationDto, MultipartFile image) {
        User user = userService.save(userMapper.userRegistrationDtoToUser(userRegistrationDto), image);
        setCookie(user);
        return ResponseEntity.ok(userMapper.userToUserGetDto(user));
    }


    private void setCookie(User user) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getResponse();
        Cookie cookie = createCookie(jwtUtil.generateToken(user));
        response.addCookie(cookie);
    }

    public Cookie createCookie(String jwt) {
        Cookie cookie = new Cookie("accessToken", jwt);
        cookie.setMaxAge(24_192_000);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setDomain(cors);
        return cookie;
    }
}
