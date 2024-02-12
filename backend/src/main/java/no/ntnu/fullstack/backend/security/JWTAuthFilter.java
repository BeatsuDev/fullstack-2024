package no.ntnu.fullstack.backend.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.user.UserService;
import no.ntnu.fullstack.backend.user.model.User;

@Component
@RequiredArgsConstructor
class JWTAuthFilter extends OncePerRequestFilter {
  private final JWTService jwtService;
  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getRequestURI().endsWith("/user/session")
        && (request.getMethod().equals("DELETE") || request.getMethod().equals("POST"))) {
      filterChain.doFilter(request, response);
      return;
    }

    jwtService.resolveJWT(request)
        .flatMap((userDetails) -> {
          return userService.getUserByEmail(userDetails.getUsername())
              .map((user) -> {
                return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    userDetails.getAuthorities());
              });
        }).ifPresent((auth) -> {
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
          response.addCookie(jwtService.generateTokenCookie((User) auth.getPrincipal()));
        });

    filterChain.doFilter(request, response);
  }
}
