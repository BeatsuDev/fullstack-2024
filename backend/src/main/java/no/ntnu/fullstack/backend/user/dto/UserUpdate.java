package no.ntnu.fullstack.backend.user.dto;

import java.util.UUID;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdate {
  @NonNull
  @NotNull
  private UUID id;
  @NonNull
  @Email
  private String email;
  @NonNull
  @NotBlank
  private String name;
  @NonNull
  @NotBlank
  private String password;
}
