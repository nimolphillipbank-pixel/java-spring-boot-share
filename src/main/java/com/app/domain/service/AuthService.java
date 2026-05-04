package com.app.domain.service;

import com.app.domain.model.AuthResponse;

public interface AuthService {

  AuthResponse login(String email, String password);
  AuthResponse login(String refreshToken);

}
