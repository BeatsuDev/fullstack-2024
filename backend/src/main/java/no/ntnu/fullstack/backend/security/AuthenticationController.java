package no.ntnu.fullstack.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import io.jsonwebtoken.io.IOException;
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
  public ResponseEntity<String> login(HttpServletResponse response, @RequestBody UserLogin login)
      throws AuthenticationException {
    UserDetails user = userDetailsService.loadUserByUsername(login.getEmail());
    if (encoder.matches(login.getPassword(), user.getPassword())) {
      ResponseCookie jwtCookie = jwtService.generateTokenCookie(user);
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
          .build();
    } else {
      throw new BadCredentialsException(login.getEmail());
    }
  }

  @PutMapping
  @ResponseBody
  public String refreshToken() {
    return ":)";
  }
}
