package no.ntnu.fullstack.backend.user.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
	@NonNull
	@NotBlank
	private UUID id;
	@NonNull
	@Email
	private String email;
	@NonNull
	@NotBlank
	private String name;
}
