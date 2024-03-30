package no.ntnu.fullstack.backend.feedback;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.UUID;
import net.minidev.json.JSONObject;
import no.ntnu.fullstack.backend.category.component.Startup;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
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
public class FeedbackControllerIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private Startup startup;
  @Autowired private UserRepository userRepository;
  @Autowired private QuizRepository quizRepository;
  @Autowired private FeedbackRepository feedbackRepository;
  private UUID quizId;
  private UserDetailsImpl userDetails;

  @BeforeEach
  public void setup() {
    startup.onApplicationEvent(null);

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
    feedbackRepository.deleteAll();
    quizRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void addFeedback_GetFeedbacksOnQuiz() throws Exception {
    JSONObject feedback = new JSONObject();
    feedback.put("quizId", quizId.toString());
    feedback.put("feedback", "good job");

    mockMvc
        .perform(
            post("/quiz/" + quizId + "/feedback")
                .contentType("application/json")
                .content(feedback.toJSONString()))
        .andExpect(
            result -> {
              String content = result.getResponse().getContentAsString();
              assert content.contains("good job");
              assert content.contains("user");
            })
        .andExpect(status().isCreated());

    mockMvc
        .perform(get("/quiz/" + quizId + "/feedback"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              String content = result.getResponse().getContentAsString();
              assert content.contains("good job");
              assert content.contains("user");
            });
  }
}
