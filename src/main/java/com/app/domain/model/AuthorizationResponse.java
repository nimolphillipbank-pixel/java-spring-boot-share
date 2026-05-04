package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorizationResponse(
    @JsonProperty("authorized") boolean authorized,
    @JsonProperty("client_id") String clientId,
    @JsonProperty("user_id") String userId,
    @JsonProperty("user_email") String userEmail,
    @JsonProperty("refresh_token") boolean refreshToken,
    @JsonProperty("request_id") String requestId,
    @JsonProperty("path") String path,
    @JsonProperty("http_method") String httpMethod) {

  public AuthorizationResponse(boolean authorized, String clientId, String userId, String userEmail,
      boolean refreshToken, String requestId, String path, String httpMethod) {
    this.authorized = authorized;
    this.clientId = clientId;
    this.userId = userId;
    this.userEmail = userEmail;
    this.refreshToken = refreshToken;
    this.requestId = requestId;
    this.path = path;
    this.httpMethod = httpMethod;
  }
}
