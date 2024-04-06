package no.ntnu.fullstack.backend.user;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.security.JWTService;
import no.ntnu.fullstack.backend.user.dto.UserCreate;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.dto.UserUpdate;
import no.ntnu.fullstack.backend.user.exception.EmailAlreadyTakenException;
import no.ntnu.fullstack.backend.user.exception.InvalidPasswordException;
import no.ntnu.fullstack.backend.user.exception.UserNotFoundException;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/user")
public class UserController {
  private final UserService userService;
  private final JWTService jwtService;
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @PostMapping
  @ResponseBody
  public ResponseEntity<UserDTO> registerUser(
      @Valid @RequestBody UserCreate userCreate,
      HttpServletResponse response,
      @RequestParam(value = "anonymous", required = false) boolean isAnonymous)
      throws EmailAlreadyTakenException, InvalidPasswordException {

    User user = userMapper.fromUserCreate(userCreate);
    if (isAnonymous) {
      user = userService.createAnnonymousUser(user.getName());
    } else {
      user = userService.createUser(user);
    }

    response.addCookie(jwtService.generateTokenCookie(user));
    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserDTO(user));
  }

  @PutMapping
  @ResponseBody
  @PreAuthorize("isAuthenticated() && #userUpdate.id == authentication.principal.id")
  public UserDTO updateUser(HttpServletResponse response, @Valid @RequestBody UserUpdate userUpdate)
      throws UserNotFoundException, EmailAlreadyTakenException, InvalidPasswordException {
    User user = userMapper.fromUserUpdate(userUpdate);
    user = userService.updateUser(user);

    response.addCookie(jwtService.generateTokenCookie(user));
    return userMapper.toUserDTO(user);
  }

  @ResponseBody
  @GetMapping(value = "/{userId}")
  @PreAuthorize("isAuthenticated() && #userId == authentication.principal.id")
  public UserDTO getUser(@PathVariable UUID userId) throws UserNotFoundException {
    User user = userService.getUserByIdOrThrow(userId);
    return userMapper.toUserDTO(user);
  }

  @ResponseBody
  @DeleteMapping(value = "/{userId}")
  @PreAuthorize("isAuthenticated() && #userId == authentication.principal.id")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable UUID userId) throws UserNotFoundException {
    userService.deleteUser(userId);
  }
}
