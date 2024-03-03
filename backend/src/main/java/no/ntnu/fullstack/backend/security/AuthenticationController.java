package no.ntnu.fullstack.backend.security;

import java.io.IOException;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.security.DTO.UserLogin;
import no.ntnu.fullstack.backend.user.UserMapper;
import no.ntnu.fullstack.backend.user.UserNotFoundException;
import no.ntnu.fullstack.backend.user.UserService;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.model.User;

@Controller
@RequestMapping(value = "/user/session")
@RequiredArgsConstructor
public class AuthenticationController {
  private final PasswordEncoder encoder;
  private final UserService userService;
  private final JWTService jwtService;
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @GetMapping
  @ResponseBody
  public UserDTO getUser(Authentication authentication) {
    var user = (User) authentication.getPrincipal();
    return userMapper.toUserDTO(user);
  }

  @PostMapping
  @ResponseBody
  public UserDTO login(HttpServletResponse response, @Valid @RequestBody UserLogin login)
      throws IOException {
    User user;
    try {
      user = userService.getUserByEmailOrThrow(login.getEmail());
    } catch (UserNotFoundException e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      return null;
    }

    if (encoder.matches(login.getPassword(), user.getPassword())) {
      Cookie jwtCookie = jwtService.generateTokenCookie(user);
      response.reset();
      response.addCookie(jwtCookie);
    } else {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    return userMapper.toUserDTO(user);
  }

  @PutMapping
  @ResponseBody
  public void refreshToken() {
  }

  @DeleteMapping
  @ResponseBody
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void logout(HttpServletResponse response) {
    Cookie cleanCookie = jwtService.getCleanCookie();

    response.reset();
    response.addCookie(cleanCookie);
    return;
  }
}
