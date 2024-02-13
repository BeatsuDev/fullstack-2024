package no.ntnu.fullstack.backend.security.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLogin {
  @NonNull
  @NotBlank
  private String email;

  @NonNull
  @NotBlank
  private String password;
}
