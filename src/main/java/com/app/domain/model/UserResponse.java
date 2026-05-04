package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(@JsonProperty("id") String id, @JsonProperty("username") String username,
                           @JsonProperty("email") String email) {
  public UserResponse(String id, String username, String email) {
    this.id = id;
    this.username = username;
    this.email = email;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String username() {
    return username;
  }

  @Override
  public String email() {
    return email;
  }
}
