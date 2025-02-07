package com.sigmadevs.testtask.security.service;

import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.security.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.exception.UsernameAlreadyExistsException;
import com.sigmadevs.testtask.security.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public User save(@NotNull User newUser) {
        if (userRepository.existsUserByUsername(newUser.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (newUser.getPassword() != null) {

            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        return userRepository.save(newUser);
    }
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
            newUser.setImage(serverProperties.getAddress()+"/avatars/"+randomNumber+".jpg");
        }

        if (newUser.getPassword() != null) {

            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        return userRepository.save(newUser);
    }

    public boolean delete(@NotNull Long id) {
        try{
            userRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    public boolean deleteByUsername(@NotNull String username) {
        try{
            userRepository.deleteByUsername(username);
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    public boolean deleteByEmail(@NotNull String email) {
        try{
            userRepository.deleteByEmail(email);
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    public User update(@NotNull User newUser) {
        return userRepository.save(newUser);
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
