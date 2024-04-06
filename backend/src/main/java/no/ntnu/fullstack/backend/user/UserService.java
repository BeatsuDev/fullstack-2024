package no.ntnu.fullstack.backend.user;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.user.exception.EmailAlreadyTakenException;
import no.ntnu.fullstack.backend.user.exception.InvalidPasswordException;
import no.ntnu.fullstack.backend.user.exception.UserNotFoundException;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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

  public User saveUser(User user) throws EmailAlreadyTakenException, InvalidPasswordException {
    try {
      return userRepository.saveAndFlush(user);
    } catch (DataIntegrityViolationException e) {
      throw new EmailAlreadyTakenException();
    }
  }

  public User createUser(User user) throws EmailAlreadyTakenException, InvalidPasswordException {
    return saveUser(user);
  }

  public User updateUser(User user)
      throws UserNotFoundException, EmailAlreadyTakenException, InvalidPasswordException {
    if (user.getPassword() == null) {
      User existingUser =
          userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
      user.setPassword(existingUser.getPassword());
    }
    return saveUser(user);
  }

  public void deleteUser(UUID id) throws UserNotFoundException {
    boolean deleteResult = userRepository.existsById(id);
    if (!deleteResult) {
      throw new UserNotFoundException();
    }

    userRepository.deleteById(id);
  }
}
