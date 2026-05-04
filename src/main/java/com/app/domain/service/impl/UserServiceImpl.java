package com.app.domain.service.impl;

import com.app.common.exception.Conflict;
import com.app.common.exception.InternalServerError;
import com.app.common.exception.UnAuthorized;
import com.app.domain.component.BCryptPasswordEncryptionComponent;
import com.app.domain.model.UserRequest;
import com.app.domain.model.UserResponse;
import com.app.domain.service.UserService;
import com.app.infrastrucuture.entity.User;
import com.app.infrastrucuture.persistent.UserPersistentRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserPersistentRepository userRepository;
  private final BCryptPasswordEncryptionComponent encryptionComponent;

  public UserServiceImpl(
      UserPersistentRepository userRepository,
      BCryptPasswordEncryptionComponent encryptionComponent) {

    this.userRepository = userRepository;
    this.encryptionComponent = encryptionComponent;
  }

  @Override
  public User getUserById(String id) {
    return this.userRepository.findById(id);
  }

  @Override
  public User getAuthenticatedUser(String email, String rawPw) {
    User user = getUserByEmail(email);
    if (user == null) {
      throw new UnAuthorized();
    }

    if (!this.encryptionComponent.matchedPassword(user.getPassword(), rawPw)) {
      throw new UnAuthorized();
    }
    return user;
  }

  @Override
  public UserResponse generateUser(UserRequest request) {
    String encPw = this.encryptionComponent.encodePassword(request.getPassword());
    User user = request.convert(this.userRepository.newId(), encPw);
    try {
      user = this.userRepository.insert(user);
    } catch (Conflict ex) {
      throw new Conflict(ex);
    } catch (Exception ex) {
      throw new InternalServerError(ex);
    }
    return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
  }

  private User getUserByEmail(String email) {
    User user;

    try {
      user = this.userRepository.findByEmail(email);
    } catch (Exception ex) {
      throw new InternalServerError(ex);
    }

    return user;
  }
}