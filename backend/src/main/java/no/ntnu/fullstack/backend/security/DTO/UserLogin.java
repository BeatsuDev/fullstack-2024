package no.ntnu.fullstack.backend.security.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class UserLogin {
  @NonNull
  private String email;

  @NonNull
  private String password;
}
