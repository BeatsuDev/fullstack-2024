package no.ntnu.fullstack.backend.security;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JWTService {

  private Duration tokenAge = Duration.ofMinutes(5);
  private String cookieName = "Authentication";
  private SecretKey key = Jwts.SIG.HS512.key().build();
  private JwtParser parser = Jwts.parser().verifyWith(key).build();

  private final UserDetailsService userDetailsService;

  public Optional<String> getJwt(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, cookieName);
    if (cookie != null) {
      return Optional.of(cookie.getValue());
    } else {
      return Optional.empty();
    }
  }

  public ResponseCookie generateTokenCookie(UserDetails userDatails) {
    String jwt = generateTokenFromUsername(userDatails.getUsername());
    return ResponseCookie.from(cookieName, jwt)
        .path("/").maxAge(tokenAge).httpOnly(true).build();
  }

  public Optional<UserDetails> resolveJWT(String jwt) {
    String subject = null;
    try {
      subject = parser.parseSignedClaims(jwt).getPayload().getSubject();
    } catch (MalformedJwtException e) { // TODO: use a logger
      System.out.println("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      System.out.println("JWT token is expired:" + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.out.println("JWT token is unsupported:" + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("JWT claims string is empty:" + e.getMessage());
    }

    if (subject == null) {
      return Optional.empty();
    }

    UserDetails user = null;
    try {
      user = userDetailsService.loadUserByUsername(subject);
    } catch (UsernameNotFoundException e) {
    }

    return Optional.of(user);
  }

  public ResponseCookie getCleanCookie() {
    return ResponseCookie.from(cookieName, null).path("/").build();
  }

  public Optional<UserDetails> resolveJWT(HttpServletRequest request) {
    return getJwt(request).flatMap(this::resolveJWT);
  }

  public String generateTokenFromUsername(String username) {
    var now = new Date();
    return Jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + tokenAge.get(ChronoUnit.SECONDS) * 1000))
        .signWith(key)
        .compact();
  }
}
