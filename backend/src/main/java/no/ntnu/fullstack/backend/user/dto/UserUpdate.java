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
  @NotNull private UUID id;
  @Email private String email;
  @NotBlank private String name;
  private String password;
}
