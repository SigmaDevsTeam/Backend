package com.sigmadevs.testtask.security.repository;

import com.sigmadevs.testtask.app.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(@NotBlank String username);

    Optional<User> findByEmail(@NotBlank String email);

    boolean existsUserByUsername(@NotBlank String username);

    void deleteByUsername(@NotBlank String username);

    void deleteByEmail(@NotBlank String email);
}
