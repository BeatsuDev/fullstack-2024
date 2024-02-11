package no.ntnu.fullstack.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import no.ntnu.fullstack.backend.security.DTO.UserLogin;

@Controller
@RequestMapping(value = "/user/session")
public class AuthenticationController {
  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JWTService jwtService;

  @GetMapping
  @ResponseBody
  public String getUser(Authentication authentication) {
    var user = (UserDetails) authentication.getPrincipal();
    return user.getUsername();
  }

  @PostMapping
  @ResponseBody
  public void login(HttpServletResponse response, @RequestBody UserLogin login)
      throws IOException {
    UserDetails user;
    try {
      user = userDetailsService.loadUserByUsername(login.getEmail());
    } catch (UsernameNotFoundException e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    if (encoder.matches(login.getPassword(), user.getPassword())) {
      Cookie jwtCookie = jwtService.generateTokenCookie(user);
      response.addCookie(jwtCookie);
    } else {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  @PutMapping
  @ResponseBody
  public String refreshToken() {
    return ":)";
  }
}
