package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import jakarta.validation.constraints.NotNull;

public record AuthResponse(@JsonProperty("token_type") @NotNull String tokenType,
                           @JsonProperty("token") @NotNull String token,
                           @JsonProperty("refresh_token") @NotNull String refreshToken,
                           @JsonProperty("expired_at") Date expiresAt) {

  public AuthResponse(String tokenType, String token, String refreshToken, Date expiresAt) {
    this.tokenType = tokenType;
    this.token = token;
    this.refreshToken = refreshToken;
    this.expiresAt = expiresAt;
  }

  @Override
  public String tokenType() {
    return tokenType;
  }

  @Override
  public String token() {
    return token;
  }

  @Override
  public String refreshToken() {
    return refreshToken;
  }

  @Override
  public Date expiresAt() {
    return expiresAt;
  }
}
