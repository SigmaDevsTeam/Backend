package com.sigmadevs.testtask.security.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDto {

    @Email(message = "Please provide a valid email address.")
    @NotBlank(message = "User should contains an E-mail!")
    private String email;

    @NotBlank(message = "User should contains a username!")
    @Size(min = 8, message = "Username should have at least 8 characters!")
    private String username;

    @NotBlank(message = "User should contains a password!")
    @Size(min = 8, message = "Password should have at least 8 characters!")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;
}
