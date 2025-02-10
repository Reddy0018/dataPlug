package com.dp.dataPlug.Model;

import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String scope;

    // Getters and Setters
    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public int getExpiresIn() {
        return expires_in;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "TokenService{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
