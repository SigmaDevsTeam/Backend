package com.sigmadevs.testtask.security.api;

import com.sigmadevs.testtask.security.dto.UserGetDto;
import com.sigmadevs.testtask.security.dto.UserLoginDto;
import com.sigmadevs.testtask.security.dto.UserRegistrationDto;
import com.sigmadevs.testtask.security.mapper.UserMapper;
import com.sigmadevs.testtask.security.service.AuthService;
import com.sigmadevs.testtask.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<UserGetDto> login(@RequestBody UserLoginDto userLoginDto) {
        return authService.login(userLoginDto);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserGetDto> registration(@RequestBody UserRegistrationDto userRegistrationDto) {
        return authService.registration(userRegistrationDto);
    }

    @GetMapping("/currentAccount")
    public ResponseEntity<UserGetDto> getCurrentAccount(Principal principal) {
        return ResponseEntity.ok(userMapper.userToUserGetDto(userService.findByUsername(principal.getName())));
    }
    //test
    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
    //test
    @GetMapping("/profile")
    public String getProfile(Principal principal) {
        return principal.getName();
    }
}


