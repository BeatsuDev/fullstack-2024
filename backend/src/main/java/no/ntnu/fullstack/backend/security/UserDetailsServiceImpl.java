package no.ntnu.fullstack.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.user.UserService;
import no.ntnu.fullstack.backend.user.model.User;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    return new UserDetailsImpl(user);
  }
}
