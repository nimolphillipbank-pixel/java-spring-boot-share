package com.app.domain.service;

import com.app.domain.model.UserRequest;
import com.app.domain.model.UserResponse;
import com.app.infrastrucuture.entity.User;

public interface UserService {
  User getUserById(String id);
  User getAuthenticatedUser(String email, String rawPw);
  UserResponse generateUser(UserRequest request);
}
