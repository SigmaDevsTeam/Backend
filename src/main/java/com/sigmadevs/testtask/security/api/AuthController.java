package com.sigmadevs.testtask.security.api;

import com.sigmadevs.testtask.security.dto.UserGetDto;
import com.sigmadevs.testtask.security.dto.UserLoginDto;
import com.sigmadevs.testtask.security.dto.UserRegistrationDto;
import com.sigmadevs.testtask.security.mapper.UserMapper;
import com.sigmadevs.testtask.security.service.AuthService;
import com.sigmadevs.testtask.security.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.sql.Time;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return authService.login(userLoginDto);
    }

    @PostMapping(value = "/registration",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registration(@RequestPart(required = false) MultipartFile image,
                                                @RequestPart(value = "user") @Valid  UserRegistrationDto userRegistrationDto) {

        return authService.registration(userRegistrationDto, image);
    }

    @GetMapping("/currentAccount")
    public ResponseEntity<UserGetDto> getCurrentAccount(Principal principal) {
        return ResponseEntity.ok(userMapper.userToUserGetDto(userService.findByUsername(principal.getName())));
    }

//    @PostMapping("/getCookie")
//    public ResponseEntity<String> getCookie(HttpServletResponse response,@RequestBody String jwt) {
//        response.addCookie(authService.createCookie(jwt));
//        return ResponseEntity.ok("Success");
//    }
}


