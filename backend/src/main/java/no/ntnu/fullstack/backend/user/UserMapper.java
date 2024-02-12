package no.ntnu.fullstack.backend.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import no.ntnu.fullstack.backend.user.dto.UserCreate;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.dto.UserUpdate;
import no.ntnu.fullstack.backend.user.model.User;

@Mapper
public abstract class UserMapper {
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Named("encodePassword")
  public String encodePassword(String password) {
    if (password == null) {
      return null;
    }
    return passwordEncoder.encode(password);

  }

  @Mappings({
      @Mapping(target = "password", qualifiedByName = "encodePassword"),
      @Mapping(target = "id", ignore = true)
  })
  public abstract User fromUserCreate(UserCreate user);

  @Mappings({
      @Mapping(target = "password", qualifiedByName = "encodePassword"),
  })
  public abstract User fromUserUpdate(UserUpdate user);

  public abstract UserDTO toUserDTO(User user);
}
