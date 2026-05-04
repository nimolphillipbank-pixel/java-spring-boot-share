package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class AuthByEmailRequest {
  @Email
  @NotNull
  @JsonProperty("email")
  @Schema(description = "Login email (username)", defaultValue = "admin@example.com", example = "admin@example.com")
  private String email;

  @NotNull
  @JsonProperty("password")
  @Schema(description = "Login password", defaultValue = "123456", example = "123456")
  private String password;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
