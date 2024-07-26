package com.mobalpa.api.filter;

import com.mobalpa.api.model.User;
import com.mobalpa.api.util.JwtUtil;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String requestApiKey = request.getHeader("X-API-KEY");

        String email = null;
        String jwt = null;

        if (requestApiKey != null && requestApiKey.equals(apiKey)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "apiKeyUser", null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                logger.error("Error extracting email from JWT", e);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims;
                User user = new User();

                try {
                    claims = jwtUtil.extractAllClaims(jwt);
                    if (!jwtUtil.validateToken(jwt, claims.get("email", String.class))) {
                        logger.warn("Invalid or expired JWT token");
                    } else {
                        user.setEmail(claims.get("email", String.class));
                        user.setFirstname(claims.get("firstname", String.class));
                        user.setLastname(claims.get("lastname", String.class));
                        user.setUuid(UUID.fromString(claims.get("id", String.class)));

                        Instant createdTimeInstant = Instant.ofEpochMilli(Long.parseLong(claims.get("createdTime", String.class)));
                        LocalDateTime createdAt = LocalDateTime.ofInstant(createdTimeInstant, ZoneId.systemDefault());
                        user.setCreatedAt(createdAt);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    logger.error("Error validating JWT token", e);
                }
            }
        }
        chain.doFilter(request, response);
    }
}