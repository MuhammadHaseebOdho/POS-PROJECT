package com.user.payload;

import lombok.Data;

@Data
public class AuthorizationRequest {
    private String email;
    private String password;
}
