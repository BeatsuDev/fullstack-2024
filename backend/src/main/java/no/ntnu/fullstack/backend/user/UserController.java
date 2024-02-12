package no.ntnu.fullstack.backend.user;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.security.JWTService;
import no.ntnu.fullstack.backend.user.dto.UserCreate;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.dto.UserUpdate;
import no.ntnu.fullstack.backend.user.model.User;

@Controller
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
  private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  private final UserService userService;
  private final JWTService jwtService;

  @PostMapping
  @ResponseBody
  public UserDTO registerUser(@RequestBody UserCreate userCreate) {
    User user = userMapper.fromUserCreate(userCreate);
    Optional<User> result = userService.createUser(user);

    return userMapper.toUserDTO(result.orElseThrow(() -> new RuntimeException("Could not create user.")));
  }

  @PutMapping
  @ResponseBody
  @PreAuthorize("isAuthenticated() && #userUpdate.id == authentication.principal.id")
  public UserDTO updateUser(HttpServletResponse response, @RequestBody UserUpdate userUpdate) {
    User user = userMapper.fromUserUpdate(userUpdate);
    Optional<User> result = userService.updateUser(user);

    result.ifPresent((_user) -> {
      Cookie jwtCookie = jwtService.generateTokenCookie(_user);
      response.reset();
      response.addCookie(jwtCookie);
    });

    return userMapper.toUserDTO(
        result.orElseThrow(() -> new RuntimeException("Could not update user.")));
  }
}
