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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
    @Value("front-end-url")
    private String cors;

    public ResponseEntity<String> login(UserLoginDto userLoginDto) {
        String login = userLoginDto.getLogin();
        User user;
        user = userService.findByEmailOptional(login).orElse(null);
        if(user == null) {
            user = userService.findByUsername(login);
        }
        if (user == null) {
            throw new RuntimeException("User not found");
        }
try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),userLoginDto.getPassword()));
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    } catch (AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
    }
        return ResponseEntity.ok(jwtUtil.generateToken(user));
    }

    public ResponseEntity<String> registration(UserRegistrationDto userRegistrationDto, MultipartFile image) {
        User user = userService.save(userMapper.userRegistrationDtoToUser(userRegistrationDto), image);
        return ResponseEntity.ok(jwtUtil.generateToken(user));
    }

//    private void setCookie(User user) {
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes())
//                .getResponse();
//        Cookie cookie = createCookie(jwtUtil.generateToken(user));
//        response.addCookie(cookie);
//    }

//    public Cookie createCookie(String jwt) {
//        Cookie cookie = new Cookie("accessToken", jwt);
//        cookie.setMaxAge(24_192_000);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
////        cookie.setDomain("taupe-raindrop-f35bc9.netlify.app");
//        return cookie;
//    }
}
