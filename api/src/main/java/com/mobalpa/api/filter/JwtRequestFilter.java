package com.mobalpa.api.filter;

import com.mobalpa.api.util.JwtUtil;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

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

        logger.info(authorizationHeader);

        if (requestApiKey != null && requestApiKey.equals(apiKey)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "apiKeyUser", null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwt);
                logger.info("Extracted email from JWT: " + email);
            } catch (Exception e) {
                logger.error("Error extracting email from JWT", e);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims;
                try {
                    claims = jwtUtil.extractAllClaims(jwt);
                    if (!jwtUtil.validateToken(jwt, email)) {
                        logger.warn("Invalid or expired JWT token");
                    } else {
                        Object rolesObj = claims.get("role");
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                        if (rolesObj instanceof List<?>) {
                            for (Object role : (List<?>) rolesObj) {
                                if (role instanceof String) {
                                    authorities.add(new SimpleGrantedAuthority((String) role));
                                }
                            }
                        }

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities);
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