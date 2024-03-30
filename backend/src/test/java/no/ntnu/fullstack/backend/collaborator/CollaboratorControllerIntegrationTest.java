package no.ntnu.fullstack.backend.collaborator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.UUID;
import net.minidev.json.JSONObject;
import no.ntnu.fullstack.backend.StartupSeed;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
import no.ntnu.fullstack.backend.quiz.repository.RevisionRepository;
import no.ntnu.fullstack.backend.security.UserDetailsImpl;
import no.ntnu.fullstack.backend.user.UserRepository;
import no.ntnu.fullstack.backend.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CollaboratorControllerIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private StartupSeed startupSeed;
  @Autowired private UserRepository userRepository;
  @Autowired private QuizRepository quizRepository;
  @Autowired private RevisionRepository revisionRepository;
  private UUID quizId;
  private UserDetailsImpl userDetails;

  @BeforeEach
  public void setup() {
    startupSeed.onApplicationEvent(null);

    User user = new User();
    user.setEmail("user@example");
    user.setPassword("password");
    user.setName("user");
    userRepository.save(user);
    userDetails = new UserDetailsImpl(user);

    Quiz quiz = new Quiz();
    quiz.setCreator(user);
    quiz = quizRepository.saveAndFlush(quiz);
    quizId = quiz.getId();

    Authentication auth = new TestingAuthenticationToken(user, "password", Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @AfterEach
  public void cleanUp() {
    revisionRepository.deleteAll();
    quizRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void addCollaborator_thenReturnCollaborators() throws Exception {
    User collaborator = new User();
    collaborator.setEmail("collaborator@example");
    collaborator.setPassword("password");
    collaborator.setName("collaborator");
    userRepository.save(collaborator);

    JSONObject collaboratorJson = new JSONObject();
    collaboratorJson.put("email", collaborator.getEmail());
    mockMvc
        .perform(
            post("/quiz/" + quizId + "/collaborator")
                .contentType("application/json")
                .content(collaboratorJson.toJSONString()))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              if (!result.getResponse().getContentAsString().contains(collaborator.getEmail())) {
                throw new AssertionError("Response does not contain collaborator email");
              }
            });
  }

  @Test
  public void getCollaborators_thenReturnCollaborators() throws Exception {
    User collaborator = new User();
    collaborator.setEmail("collaborator@example");
    collaborator.setPassword("password");
    collaborator.setName("collaborator");
    userRepository.save(collaborator);

    JSONObject collaboratorJson = new JSONObject();
    collaboratorJson.put("email", collaborator.getEmail());
    mockMvc
        .perform(
            post("/quiz/" + quizId + "/collaborator")
                .contentType("application/json")
                .content(collaboratorJson.toJSONString()))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/quiz/" + quizId + "/collaborator"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              if (!result.getResponse().getContentAsString().contains(collaborator.getEmail())) {
                throw new AssertionError("Response does not contain collaborator email");
              }
            });
  }

  @Test
  public void removeCollaborator_thenReturnCollaborators() throws Exception {
    User collaborator = new User();
    collaborator.setEmail("collaborator@example");
    collaborator.setPassword("password");
    collaborator.setName("collaborator");
    userRepository.save(collaborator);

    JSONObject collaboratorJson = new JSONObject();
    collaboratorJson.put("email", collaborator.getEmail());
    mockMvc
        .perform(
            post("/quiz/" + quizId + "/collaborator")
                .contentType("application/json")
                .content(collaboratorJson.toJSONString()))
        .andExpect(status().isOk());

    mockMvc
        .perform(delete("/quiz/" + quizId + "/collaborator/" + collaborator.getId()))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              if (result.getResponse().getContentAsString().contains(collaborator.getEmail())) {
                throw new AssertionError("Response contains collaborator email");
              }
            });
  }
}
