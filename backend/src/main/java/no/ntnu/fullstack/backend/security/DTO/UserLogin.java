package no.ntnu.fullstack.backend.security.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLogin {
  @NonNull
  private String email;

  @NonNull
  private String password;
}
