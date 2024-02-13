package no.ntnu.fullstack.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreate {
  @NonNull
  @NotBlank
  private String name;
  @NonNull
  @Email
  private String email;
  @NonNull
  @NotBlank
  private String password;
}
