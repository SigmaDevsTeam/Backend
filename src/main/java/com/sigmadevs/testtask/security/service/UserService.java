package com.sigmadevs.testtask.security.service;

import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.security.dto.UserGetDto;
import com.sigmadevs.testtask.app.exception.UserNotFoundException;
import com.sigmadevs.testtask.app.exception.UsernameAlreadyExistsException;
import com.sigmadevs.testtask.security.mapper.UserMapper;
import com.sigmadevs.testtask.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final ServerProperties serverProperties;
    private final UserMapper userMapper;

    //get

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(@NotNull Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id, "id"));
    }

    public User findByUsername(@NotNull String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username, "username"));
    }

    public User findByEmail(@NotNull String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email, "email"));
    }

    public Optional<User> findByIdOptional(@NotNull Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsernameOptional(@NotNull String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    //manipulations
//    @Transactional
//    public User save(@NotNull User newUser) {
//        if (userRepository.existsUserByUsername(newUser.getUsername())) {
//            throw new UsernameAlreadyExistsException("Username already exists");
//        }
//        if (newUser.getPassword() != null) {
//
//            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        }
//        return userRepository.save(newUser);
//    }

    @Transactional
    public User save(@NotNull User newUser, MultipartFile image) {
        if (userRepository.existsUserByUsername(newUser.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if(image != null) {
            String avatar = imageService.uploadImage("avatars", image);
            newUser.setImage(avatar);
        }else {
            Random random = new Random();
            int randomNumber = random.nextInt(10) + 1;
            newUser.setImage(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/avatars/"+randomNumber+".jpg");
        }

        if (newUser.getPassword() != null) {

            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        return userRepository.save(newUser);
    }
    @Transactional
    public boolean delete(@NotNull Long id) {

        try{
            userRepository.deleteById(id);
            deleteCookie();
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteByUsername(@NotNull String username) {
        try{
            userRepository.deleteByUsername(username);
            deleteCookie();
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteByEmail(@NotNull String email) {
        try{
            userRepository.deleteByEmail(email);
            deleteCookie();
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    private static void deleteCookie() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getResponse();
        response.setHeader("Set-Cookie", "accessToken=unauthorized;expires=Thu, 01 Jan 1970 00:00:01 GMT;");
    }
    @Transactional
    public UserGetDto updateImage(@NotNull MultipartFile image, Principal principal) {
        User user = findByUsername(principal.getName());
        String newUrl = imageService.updateImage(user.getImage(),image);
        user.setImage(newUrl);
        return userMapper.userToUserGetDto(userRepository.save(user));
    }
    @Transactional
    public UserGetDto updatePassword(Principal principal, String password) {
        User user = findByUsername(principal.getName());
        if (password!=null && !password.isBlank()){
            user.setPassword(passwordEncoder.encode(password));
        }
        return userMapper.userToUserGetDto(userRepository.save(user));
    }
//    public User update(@NotNull UserUpdateDto userUpdateDto) {
//
//        return userRepository.save(userUpdateDto);
//    }

    //system

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }
}
