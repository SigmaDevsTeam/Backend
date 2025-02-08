package com.sigmadevs.testtask.security.api;

import com.sigmadevs.testtask.security.dto.UserGetDto;
import com.sigmadevs.testtask.security.dto.UserUpdateDto;
import com.sigmadevs.testtask.security.mapper.UserMapper;
import com.sigmadevs.testtask.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(Principal principal) {
        if(userService.deleteByUsername(principal.getName())){
            return ResponseEntity.ok("Account deleted successfully");
        }else {
            return ResponseEntity.badRequest().body("Account could not be deleted");
        }
    }

    @PatchMapping(value = "/updateImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserGetDto> updateImage(Principal principal, @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(userService.updateImage(image,principal.getName()));
    }


}
