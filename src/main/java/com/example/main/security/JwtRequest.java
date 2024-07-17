package com.example.main.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    private String employeeId;
    private String employeePassword;
    private boolean rememberMe;
}
