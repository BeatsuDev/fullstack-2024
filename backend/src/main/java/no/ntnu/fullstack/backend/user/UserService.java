package no.ntnu.fullstack.backend.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.user.model.User;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public Optional<User> getUserById(UUID id) {
    return userRepository.findById(id);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User getUserByEmailOrThrow(String email) throws UsernameNotFoundException {
    return getUserByEmail(email).orElseThrow(() -> {
      throw new UsernameNotFoundException(email);
    });
  }

  public Optional<User> createUser(User user) {
    if (user == null || user.getId() != null) {
      return Optional.empty();
    }
    return Optional.of(userRepository.saveAndFlush(user));
  }

  public Optional<User> updateUser(User user) {
    if (user == null || user.getId() == null) {
      return Optional.empty();
    }
    return Optional.of(userRepository.saveAndFlush(user));
  }
}
