package com.mobalpa.api.filter;

import com.mobalpa.api.dto.UserDTO;
import com.mobalpa.api.mapper.UserMapper;
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
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                logger.error("Error extracting email from JWT", e);
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims;
            UserDTO userDTO = new UserDTO();

            try {
                claims = jwtUtil.extractAllClaims(jwt);
                if (!jwtUtil.validateToken(jwt, claims.get("email", String.class))) {
                    logger.warn("Invalid or expired JWT token");
                } else {
                    userDTO.setEmail(claims.get("email", String.class));
                    userDTO.setFirstname(claims.get("firstname", String.class));
                    userDTO.setLastname(claims.get("lastname", String.class));
                    userDTO.setUuid(UUID.fromString(claims.get("id", String.class))); // Extract UUID correctly

                    // Convert Date to LocalDateTime
                    Instant createdTimeInstant = Instant.ofEpochMilli(Long.parseLong(claims.get("createdTime", String.class)));
                    LocalDateTime createdAt = LocalDateTime.ofInstant(createdTimeInstant, ZoneId.systemDefault());
                    userDTO.setCreatedAt(createdAt.toLocalDate());
                }
            } catch (Exception e) {
                logger.error("Error validating JWT token", e);
            }

            User userModel = userMapper.userDTOToUser(userDTO);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userModel,
                    null, // You should use null for credentials as it's not needed here
                    userModel.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}