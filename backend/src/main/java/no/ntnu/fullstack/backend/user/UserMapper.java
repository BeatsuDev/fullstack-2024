package no.ntnu.fullstack.backend.user;

import java.util.List;
import no.ntnu.fullstack.backend.user.dto.UserCreate;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.dto.UserUpdate;
import no.ntnu.fullstack.backend.user.exception.InvalidPasswordException;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
public abstract class UserMapper {
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Named("encodePassword")
  public String encodePassword(String password) throws InvalidPasswordException {
    if (password == null) {
      return null;
    }
    if (password.length() < 8) {
      throw new InvalidPasswordException();
    }
    return passwordEncoder.encode(password);
  }

  @Mappings({
    @Mapping(target = "password", qualifiedByName = "encodePassword"),
    @Mapping(target = "id", ignore = true)
  })
  public abstract User fromUserCreate(UserCreate user) throws InvalidPasswordException;

  @Mappings({
    @Mapping(target = "password", qualifiedByName = "encodePassword"),
  })
  public abstract User fromUserUpdate(UserUpdate user) throws InvalidPasswordException;

  public abstract UserDTO toUserDTO(User user);

  public abstract List<UserDTO> toUserDTOList(List<User> users);
}
