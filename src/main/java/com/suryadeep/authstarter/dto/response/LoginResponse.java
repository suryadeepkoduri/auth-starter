package com.suryadeep.authstarter.dto.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginResponse {
    private String tokenType = "Bearer";
    private String accessToken;

    public LoginResponse(String token) {
        this.accessToken = token;
    }
}
