package no.ntnu.fullstack.backend.user;

import java.util.Optional;
import java.util.UUID;

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

  public User getUserByIdOrThrow(UUID id) throws UserNotFoundException {
    return getUserById(id).orElseThrow(UserNotFoundException::new);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User getUserByEmailOrThrow(String email) throws UserNotFoundException {
    return getUserByEmail(email).orElseThrow(UserNotFoundException::new);
  }

  public Optional<User> createUser(User user) { // TODO: Validate email
    if (user == null || user.getId() != null) {
      return Optional.empty();
    }
    return Optional.of(userRepository.saveAndFlush(user));
  }

  public Optional<User> updateUser(User user) { // TODO: Validate email
    if (user == null || user.getId() == null) {
      return Optional.empty();
    }
    return Optional.of(userRepository.saveAndFlush(user));
  }

  public void deleteUser(UUID id) throws UserNotFoundException {
    boolean deleteResult = userRepository.existsById(id);
    if (!deleteResult) {
      throw new UserNotFoundException();
    }

    userRepository.deleteById(id);
  }
}
