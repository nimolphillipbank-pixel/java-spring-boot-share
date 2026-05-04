package com.app.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.app.common.exception.UnAuthorized;
import com.app.common.property.CredentialProperty;
import com.app.domain.component.AuthComponent;
import com.app.domain.component.RequestInfoComponent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

public class ApiRequestFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(ApiRequestFilter.class);
  private static final String SPRING_ERROR_PATH = "/error";
  private static final List<String> PUBLIC_ENDPOINT_PREFIXES = List.of(
      "/api/v1/user",
      "/api/v1/auth",
      "/api/v1/hello");
  private static final String CLIENT_ID_HEADER = "X-Client-Id";
  private static final String SECRET_KEY_HEADER = "X-Secret-Key";

  private final AuthComponent authComponent;
  private final CredentialProperty credentialProperty;
  private final RequestInfoComponent requestInfo;
  private final Environment environment;
  private final HandlerExceptionResolver handlerExceptionResolver;

  public ApiRequestFilter(AuthComponent authComponent, CredentialProperty credentialProperty,
      RequestInfoComponent requestInfo, Environment environment,
      HandlerExceptionResolver handlerExceptionResolver) {
    this.authComponent = authComponent;
    this.credentialProperty = credentialProperty;
    this.requestInfo = requestInfo;
    this.environment = environment;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return !request.getRequestURI().startsWith("/api/");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    this.requestInfo.setRequestId(UUID.randomUUID().toString());
    this.requestInfo.setPath(request.getRequestURI());
    this.requestInfo.setHttpMethod(request.getMethod());

    StringBuilder accessLogBuilder = new StringBuilder();
    accessLogBuilder.append("AUDIT ")
        .append("RequestId: ")
        .append(this.requestInfo.getRequestId())
        .append(", Path: ")
        .append(this.requestInfo.getPath())
        .append(", Method: ")
        .append(this.requestInfo.getHttpMethod());

    try {
      authenticateRequest(request, accessLogBuilder);
      logger.info(accessLogBuilder.toString());
      filterChain.doFilter(request, response);
    } catch (Exception ex) {
      logger.info(accessLogBuilder.toString(), ex);
      this.handlerExceptionResolver.resolveException(request, response, null, ex);
    }
  }

  private void authenticateRequest(HttpServletRequest request, StringBuilder accessLogBuilder) {
    if (this.requestInfo.getPath().startsWith(SPRING_ERROR_PATH)) {
      return;
    }

    authorizeClient(request, accessLogBuilder);
    if (PUBLIC_ENDPOINT_PREFIXES.stream().anyMatch(this.requestInfo.getPath()::startsWith)
        || this.environment.matchesProfiles("local")) {
      return;
    }

    String bearerToken = request.getHeader("Authorization");
    if (bearerToken == null || bearerToken.isBlank()) {
      throw new UnAuthorized("Authorization header is empty");
    }
    if (!bearerToken.startsWith("Bearer ")) {
      throw new UnAuthorized("Authorization supports Bearer format");
    }

    DecodedJWT decodedJWT = this.authComponent.decodeJwt(bearerToken.substring("Bearer ".length()).trim());
    this.requestInfo.setUserId(decodedJWT.getSubject());
    this.requestInfo.setUserEmail(decodedJWT.getClaim(AuthComponent.ClaimUserEmailKey).asString());
    this.requestInfo.setRefreshToken(decodedJWT.getClaim(AuthComponent.ClaimRefreshKey).asBoolean());

    accessLogBuilder.append(", UserId: ")
        .append(this.requestInfo.getUserId());
  }

  private void authorizeClient(HttpServletRequest request, StringBuilder accessLogBuilder) {
    String clientId = request.getHeader(CLIENT_ID_HEADER);
    if (clientId == null || clientId.isBlank()) {
      throw new UnAuthorized(CLIENT_ID_HEADER + " header is empty");
    }

    String secretKey = request.getHeader(SECRET_KEY_HEADER);
    if (secretKey == null || secretKey.isBlank()) {
      throw new UnAuthorized(SECRET_KEY_HEADER + " header is empty");
    }

    if (!clientId.equals(this.credentialProperty.getClientId())
        || !secretKey.equals(this.credentialProperty.getSecretKey())) {
      throw new UnAuthorized("Client credentials are invalid");
    }

    this.requestInfo.setClientId(clientId);
    accessLogBuilder.append(", ClientId: ")
        .append(clientId);
  }
}
