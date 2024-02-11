package no.ntnu.fullstack.backend.user;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.user.dto.UserCreate;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.model.User;

@Controller
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
  private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  private final UserRepository userRepository;

  @PostMapping
  @ResponseBody
  public UserDTO registerUser(@RequestBody UserCreate userCreate) {
    User user = userMapper.fromUserCreate(userCreate);
    user = userRepository.save(user);
    return userMapper.toUserDTO(user);
  }
}
