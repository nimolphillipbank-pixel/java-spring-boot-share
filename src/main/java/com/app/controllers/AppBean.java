package com.app.controllers;

import com.app.common.property.CredentialProperty;
import com.app.domain.component.AuthComponent;
import com.app.domain.component.RequestInfoComponent;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.servlet.DispatcherType;
import java.util.EnumSet;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

public class AppBean {

  @Configuration
  public static class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
      return new OpenAPI().info(new Info()
          .title("Spring boot best practice API")
          .description("Spring boot best practice API document")
          .version("0.0.1"));
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
