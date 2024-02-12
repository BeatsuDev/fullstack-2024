package no.ntnu.fullstack.backend.user.dto;

import java.util.UUID;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdate {
  @NonNull
  private UUID id;
  @NonNull
  private String email;
  @NonNull
  private String name;
  @NonNull
  private String password;
}
