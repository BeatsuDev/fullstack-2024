package no.ntnu.fullstack.backend.security;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import no.ntnu.fullstack.backend.security.DTO.UserLogin;

@Controller
@RequestMapping(value = "/user/session")
public class AuthenticationController {
  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserDetailsService userDetailsService;

  @GetMapping
  @ResponseBody
  public String getUser(Authentication authentication) {
    var user = (UserDetails) authentication.getPrincipal();
    return user.getUsername();
  }

  @PostMapping
  public ResponseEntity<String> login(@RequestBody UserLogin login) {
    var user = userDetailsService.loadUserByUsername(login.getEmail());
    if (encoder.matches(login.getPassword(), user.getPassword())) {
      byte[] encoded = Base64.getEncoder()
          .encode(String.format("%s:%s", login.getEmail(), login.getPassword()).getBytes());
      return ResponseEntity.ok().header("Set-Cookie", String.format("Authorization=Basic %s", new String(encoded)))
          .build();
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PutMapping
  @ResponseBody
  public String refreshToken() {
    return ":)";
  }
}
