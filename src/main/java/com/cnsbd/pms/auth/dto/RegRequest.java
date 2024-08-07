package com.cnsbd.pms.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegRequest {
    @NotEmpty(message = "username is required")
    private String username;

    @NotEmpty(message = "password is required")
    private String password;

    @NotEmpty(message = "email is required")
    @Email(message = "email is not valid")
    private String email;

    @NotEmpty(message = "full name is required")
    private String fullName;
}
