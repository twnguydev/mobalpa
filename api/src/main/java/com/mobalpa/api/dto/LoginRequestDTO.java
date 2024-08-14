package com.mobalpa.api.dto;

import lombok.Data;

import com.mobalpa.api.model.User;

@Data
public class LoginRequestDTO {
  private final User user;
  private final String accessToken;

  public LoginRequestDTO(User user, String accessToken) {
      this.user = user;
      this.accessToken = accessToken;
  }
}
