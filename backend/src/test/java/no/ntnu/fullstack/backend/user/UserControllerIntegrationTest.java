package no.ntnu.fullstack.backend.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.fullstack.backend.StartupSeed;
import no.ntnu.fullstack.backend.user.model.User;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
  @Autowired private StartupSeed startupSeed;
  @Autowired private UserRepository userRepository;
  @Autowired private UserService userService;
  @Autowired private MockMvc mockMvc;
  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  public void testCreateUser_ValidRequest_Returns201() throws Exception {
    JSONObject json = new JSONObject();
    json.put("email", "test@example.org");
    json.put("name", "test");
    json.put("password", "password");

    mockMvc
        .perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
        .andExpect(status().isCreated());

    Optional<User> userOption = userRepository.findByEmail("test@example.org");
    assert userOption.isPresent();

    User user = userOption.get();
    assert user.getEmail().equals("test@example.org");
    assert user.getName().equals("test");
    assert passwordEncoder.matches("password", user.getPassword());
  }

  @Test
  public void testCreateUser_InvalidPassword_Returns400() throws Exception {
    JSONObject json = new JSONObject();
    json.put("email", "test@example.org");
    json.put("name", "test");
    json.put("password", "short");

    mockMvc
        .perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateUser_NoPassword_AltersUser() throws Exception {
    User user = new User();
    user.setEmail("test@test.no");
    user.setName("test");
    user.setPassword(passwordEncoder.encode("password"));
    user = userService.createUser(user);
    authenticateUser(user);

    JSONObject json = new JSONObject();
    json.put("id", user.getId());
    json.put("email", "test2@test.no");
    json.put("name", "test2");

    mockMvc
        .perform(put("/user").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
        .andExpect(status().isOk());

    User updatedUser = userService.getUserById(user.getId()).orElseThrow();
    assert updatedUser.getEmail().equals("test2@test.no");
    assert updatedUser.getName().equals("test2");
    assert passwordEncoder.matches("password", updatedUser.getPassword());
  }

  @Test
  void updateUser_withPassword_AltersPassword() throws Exception {
    User user = new User();
    user.setEmail("test@test.no");
    user.setName("test");
    user.setPassword(passwordEncoder.encode("password"));
    user = userService.createUser(user);
    authenticateUser(user);

    JSONObject json = new JSONObject();
    json.put("id", user.getId());
    json.put("email", "test2@test.no");
    json.put("name", "test2");
    json.put("password", "password2");

    mockMvc
        .perform(put("/user").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
        .andExpect(status().isOk());

    User updatedUser = userService.getUserById(user.getId()).orElseThrow();
    assert updatedUser.getEmail().equals("test2@test.no");
    assert updatedUser.getName().equals("test2");
    assert passwordEncoder.matches("password2", updatedUser.getPassword());
  }

  @Test
  void updateUser_invalidPassword_Returns400() throws Exception {
    User user = new User();
    user.setEmail("test@test.no");
    user.setName("test");
    user.setPassword(passwordEncoder.encode("password"));
    user = userService.createUser(user);
    authenticateUser(user);

    JSONObject json = new JSONObject();
    json.put("id", user.getId());
    json.put("email", "test2@test.no");
    json.put("name", "test2");
    json.put("password", "toshort");

    mockMvc
        .perform(put("/user").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getUser_ValidRequest_ReturnsUser() throws Exception {
    User user = new User();
    user.setEmail("test@test.no");
    user.setName("test");
    user.setPassword(passwordEncoder.encode("password"));
    user = userService.createUser(user);
    authenticateUser(user);

    mockMvc
        .perform(get("/user/" + user.getId().toString()))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              JSONObject json = new JSONObject(result.getResponse().getContentAsString());
              assert json.getString("email").equals("test@test.no");
              assert json.getString("name").equals("test");
            });
  }

  @Test
  void getUser_Unauthenticated_Returns401() throws Exception {
    mockMvc.perform(post("/user/" + UUID.randomUUID())).andExpect(status().isUnauthorized());
  }

  private void authenticateUser(User user) {
    Authentication auth = new TestingAuthenticationToken(user, "password", Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @BeforeEach
  public void setup() {
    startupSeed.onApplicationEvent(null);
  }

  @AfterEach
  public void tearDown() {
    userRepository.deleteAll();
  }
}
