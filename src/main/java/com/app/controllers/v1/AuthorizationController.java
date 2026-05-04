package com.app.controllers.v1;

import com.app.domain.component.RequestInfoComponent;
import com.app.domain.model.AuthorizationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/authorization")
public class AuthorizationController {
  private final RequestInfoComponent requestInfo;

  public AuthorizationController(RequestInfoComponent requestInfo) {
    this.requestInfo = requestInfo;
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public AuthorizationResponse authorize() {
    return new AuthorizationResponse(
        this.requestInfo.getClientId() != null && !this.requestInfo.getClientId().isBlank(),
        this.requestInfo.getClientId(),
        this.requestInfo.getUserId(),
        this.requestInfo.getUserEmail(),
        this.requestInfo.isRefreshToken(),
        this.requestInfo.getRequestId(),
        this.requestInfo.getPath(),
        this.requestInfo.getHttpMethod());
  }
}
