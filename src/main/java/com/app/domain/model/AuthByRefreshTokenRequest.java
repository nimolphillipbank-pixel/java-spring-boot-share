package com.app.domain.model;

import jakarta.validation.constraints.NotNull;

public class AuthByRefreshTokenRequest {
  @NotNull
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

}
