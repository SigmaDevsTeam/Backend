package com.sigmadevs.testtask.security.api;

import com.sigmadevs.testtask.security.mapper.UserMapper;
import com.sigmadevs.testtask.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PatchMapping("/update")
//    public ResponseEntity<UserGetDto> updateAccount(Principal principal, @RequestBody UserUpdateDto userUpdateDto) {
//        return userMapper.userToUserGetDto(userService.update(userUpdateDto));
//    }


}
