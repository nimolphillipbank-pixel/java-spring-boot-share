package com.app.controllers;

import com.app.common.property.CredentialProperty;
import com.app.domain.component.AuthComponent;
import com.app.domain.component.RequestInfoComponent;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.DispatcherType;
import java.util.EnumSet;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerExceptionResolver;

public class AppBean {

  @Configuration
  public static class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(
        CredentialProperty credentialProperty,
        @Value("${app.swagger.server-url:http://localhost:8080}") String swaggerServerUrl) {
      return new OpenAPI()
          .info(new Info()
              .title("Spring boot best practice API")
              .description("Spring boot best practice API document")
              .version("0.0.1"))
          .addServersItem(new Server().url(swaggerServerUrl).description("Default server"))
          .components(new Components()
              .addSecuritySchemes("bearerAuth", new SecurityScheme()
                  .type(SecurityScheme.Type.HTTP)
                  .scheme("bearer")
                  .bearerFormat("JWT")
                  .description("Paste JWT token from /api/v1/auth/email-login response"))
              .addSecuritySchemes("xClientId", new SecurityScheme()
                  .type(SecurityScheme.Type.APIKEY)
                  .in(SecurityScheme.In.HEADER)
                  .name("X-Client-Id")
                  .description("Default: " + credentialProperty.getClientId()))
              .addSecuritySchemes("xSecretKey", new SecurityScheme()
                  .type(SecurityScheme.Type.APIKEY)
                  .in(SecurityScheme.In.HEADER)
                  .name("X-Secret-Key")
                  .description("Default: " + credentialProperty.getSecretKey())))
          .addSecurityItem(new SecurityRequirement().addList("xClientId").addList("xSecretKey"));
    }

    @Bean
    public FilterRegistrationBean<ApiRequestFilter> apiRequestFilter(
        AuthComponent authComponent,
        CredentialProperty credentialProperty,
        RequestInfoComponent requestInfo,
        Environment environment,
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
      FilterRegistrationBean<ApiRequestFilter> registration = new FilterRegistrationBean<>();
      registration.setFilter(
          new ApiRequestFilter(authComponent, credentialProperty, requestInfo, environment,
              handlerExceptionResolver));
      registration.setUrlPatterns(java.util.List.of("/api/*"));
      registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST));
      registration.setOrder(1);
      return registration;
    }
  }
}
