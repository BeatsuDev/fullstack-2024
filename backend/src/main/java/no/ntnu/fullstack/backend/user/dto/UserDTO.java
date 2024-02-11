package no.ntnu.fullstack.backend.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	@NonNull
	private UUID id;
	@NonNull
	private String email;
	@NonNull
	private String name;
}
