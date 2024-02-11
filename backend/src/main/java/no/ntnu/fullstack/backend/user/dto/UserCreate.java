package no.ntnu.fullstack.backend.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreate {
  @NonNull
  private String name;
  @NonNull
  private String email;
  @NonNull
  private String password;
}
