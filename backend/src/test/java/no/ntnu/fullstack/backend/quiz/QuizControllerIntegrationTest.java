package no.ntnu.fullstack.backend.quiz;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import no.ntnu.fullstack.backend.category.component.Startup;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
import no.ntnu.fullstack.backend.quiz.repository.RevisionRepository;
import no.ntnu.fullstack.backend.security.UserDetailsImpl;
import no.ntnu.fullstack.backend.security.UserDetailsServiceImpl;
import no.ntnu.fullstack.backend.user.UserRepository;
import no.ntnu.fullstack.backend.user.model.User;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerIntegrationTest {

  @Autowired RevisionRepository revisionRepository;
  @Autowired private MockMvc mockMvc;
  @MockBean private UserDetailsServiceImpl userDetailsService;
  @Autowired private Startup startup;
  @Autowired private UserRepository userRepository;
  @Autowired private QuizRepository quizRepository;
  @MockBean private UserDetailsImpl userDetails;

  @BeforeEach
  public void setup() {
    revisionRepository.deleteAll();
    quizRepository.deleteAll();
    userRepository.deleteAll();
    startup.onApplicationEvent(null);

    User user = new User();
    user.setEmail("user@example");
    user.setPassword("password");
    user.setName("user");
    userRepository.save(user);
    userDetails = new UserDetailsImpl(user);

    Authentication auth = new TestingAuthenticationToken(user, "password", Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);

    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
  }

  @Test
  public void createQuiz_ValidRequest_ReturnsQuiz() throws Exception {
    JSONObject quizCreate = new JSONObject();
    quizCreate.put("title", "Example Quiz");
    quizCreate.put("description", "An example quiz.");
    quizCreate.put("category", "[]");
    quizCreate.put("difficulty", 1);

    mockMvc
        .perform(
            post("/quiz").contentType(MediaType.APPLICATION_JSON).content(quizCreate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Example Quiz"))
        .andExpect(jsonPath("$.description").value("An example quiz."));
  }

  @Test
  public void createQuiz_0Difficulty_ReturnsBadRequest() throws Exception {
    JSONObject quizCreate = new JSONObject();
    quizCreate.put("title", "Example Quiz");
    quizCreate.put("description", "An example quiz.");
    quizCreate.put("category", "[]");
    quizCreate.put("difficulty", 0);

    mockMvc
        .perform(
            post("/quiz").contentType(MediaType.APPLICATION_JSON).content(quizCreate.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void createQuiz_EmptyTitle_ReturnsBadRequest() throws Exception {
    JSONObject quizCreate = new JSONObject();
    quizCreate.put("title", "");
    quizCreate.put("description", "An example quiz.");
    quizCreate.put("category", "[]");
    quizCreate.put("difficulty", 0);

    mockMvc
        .perform(
            post("/quiz").contentType(MediaType.APPLICATION_JSON).content(quizCreate.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void fetchQuiz_ValidRequest_ReturnsQuiz() throws Exception {
    mockMvc.perform(get("/quiz")).andExpect(status().isOk());
  }

  @Test
  public void updateQuiz_UpdatesExistingQuiz_ReturnsUpdatedQuiz() throws Exception {
    JSONObject quizCreate = new JSONObject();
    quizCreate.put("title", "Example Quiz");
    quizCreate.put("description", "An example quiz.");
    quizCreate.put("category", "[]");
    quizCreate.put("difficulty", 3);

    mockMvc
        .perform(
            post("/quiz").contentType(MediaType.APPLICATION_JSON).content(quizCreate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Example Quiz"))
        .andExpect(jsonPath("$.description").value("An example quiz."));

    var quizId = quizRepository.findWithFirstRevision().get(0).getQuiz().getId();
    JSONObject quizUpdate = new JSONObject();
    quizUpdate.put("title", "Updated Quiz");
    quizUpdate.put("description", "An updated quiz.");
    quizUpdate.put("category", "[]");
    quizUpdate.put("difficulty", 1);

    mockMvc
        .perform(
            put("/quiz/" + quizId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(quizUpdate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Quiz"))
        .andExpect(jsonPath("$.description").value("An updated quiz."));

    quizRepository
        .findWithFirstRevision(quizId)
        .ifPresent(
            quiz -> {
              assert quiz.getLatestRevision().getTitle().equals("Updated Quiz");
              assert quiz.getLatestRevision().getDescription().equals("An updated quiz.");
              assert quiz.getLatestRevision().getDifficulty() == 1;
              assert quiz.getLatestRevision().getCategories().isEmpty();
            });
  }
}
