package no.ntnu.fullstack.backend.security;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
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
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.user.model.User;

@Service
@RequiredArgsConstructor
public class JWTService {

  private Duration tokenAge = Duration.ofMinutes(5);
  private String cookieName = "Authentication";

  @Value("${application.jwtsecret}")
  private String jwtSecret;
  private Optional<SecretKey> key = Optional.empty();
  private Optional<JwtParser> jwtParser = Optional.empty();

  private final UserDetailsService userDetailsService;

  private SecretKey getKey() {
    return key.orElseGet(this::createKey);
  }

  private SecretKey createKey() {
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    this.key = Optional.of(key);
    return key;
  }

  private JwtParser getJwtParser() {
    return jwtParser.orElseGet(this::createParser);
  }

  private JwtParser createParser() {
    JwtParser jwtParser = Jwts.parser().verifyWith(getKey()).build();
    this.jwtParser = Optional.of(jwtParser);
    return jwtParser;
  }

  public Optional<String> getJwt(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, cookieName);
    return Optional.ofNullable(cookie).map(Cookie::getValue);
  }

  public Cookie generateTokenCookie(User user) {
    String jwt = generateTokenFromUsername(user.getEmail());
    Cookie cookie = new Cookie(cookieName, jwt);
    cookie.setPath("/");
    cookie.setMaxAge((int) tokenAge.getSeconds());
    cookie.setHttpOnly(true);
    return cookie;
  }

  public Optional<UserDetails> resolveJWT(String jwt) {
    Optional<String> subject = Optional.empty();
    try {
      subject = Optional.ofNullable(getJwtParser().parseSignedClaims(jwt).getPayload().getSubject());
    } catch (MalformedJwtException e) { // TODO: use a logger
      System.out.println("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      System.out.println("JWT token is expired:" + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.out.println("JWT token is unsupported:" + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("JWT claims string is empty:" + e.getMessage());
    }

    try {
      return subject.map((s) -> userDetailsService.loadUserByUsername(s));
    } catch (UsernameNotFoundException e) {
      return Optional.empty();
    }
  }

  public Cookie getCleanCookie() {
    Cookie cookie = new Cookie(cookieName, null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    return cookie;
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
        .signWith(getKey())
        .compact();
  }
}
