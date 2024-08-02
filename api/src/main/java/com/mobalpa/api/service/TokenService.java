package com.mobalpa.api.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private final Map<String, String> tokenMap = new HashMap<>();

    public String getTokenByEmail(String email) {
        return tokenMap.get(email);
    }

    public void saveToken(String email, String token) {
        tokenMap.put(email, token);
    }
}