package no.ntnu.fullstack.backend.revision;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import no.ntnu.fullstack.backend.StartupSeed;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
import no.ntnu.fullstack.backend.security.UserDetailsImpl;
import no.ntnu.fullstack.backend.user.UserRepository;
import no.ntnu.fullstack.backend.user.model.User;
import org.json.JSONArray;
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
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RevisionControllerIntegrationTest {
  @Autowired private StartupSeed startupSeed;
  @Autowired private MockMvc mockMvc;
  @Autowired private RevisionRepository revisionRepository;
  @Autowired private QuizRepository quizRepository;
  @Autowired private UserRepository userRepository;
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

    Authentication auth = new TestingAuthenticationToken(user, "password", Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @AfterEach
  public void tearDown() {
    revisionRepository.deleteAll();
    quizRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testRevisionControllerHappyPath() throws Exception {
    JSONObject quizCreate = new JSONObject();
    quizCreate.put("title", "Example Quiz");
    quizCreate.put("description", "An example quiz.");
    quizCreate.put("category", "[]");
    quizCreate.put("difficulty", 1);

    AtomicReference<UUID> quizId = new AtomicReference<>();
    mockMvc
        .perform(
            post("/quiz").contentType(MediaType.APPLICATION_JSON).content(quizCreate.toString()))
        .andDo(
            result -> {
              JSONObject json = new JSONObject(result.getResponse().getContentAsString());
              quizId.set(UUID.fromString(json.getString("id")));
            });

    JSONObject quizUpdate = new JSONObject();
    quizUpdate.put("title", "Updated Quiz");
    quizUpdate.put("description", "An updated quiz.");
    quizUpdate.put("category", "[]");
    quizUpdate.put("difficulty", 2);

    mockMvc
        .perform(
            put("/quiz/" + quizId.get())
                .contentType(MediaType.APPLICATION_JSON)
                .content(quizUpdate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Quiz"))
        .andExpect(jsonPath("$.description").value("An updated quiz."));

    AtomicReference<UUID> revisionId = new AtomicReference<>();
    mockMvc
        .perform(get("/quiz/" + quizId.get() + "/revision"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              JSONArray json = new JSONArray(result.getResponse().getContentAsString());
              assert json.length() == 2;
              revisionId.set(UUID.fromString(json.getJSONObject(1).getString("revisionId")));
            });

    mockMvc
        .perform(put("/quiz/" + quizId.get() + "/revision/" + revisionId.get()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Example Quiz"))
        .andExpect(jsonPath("$.description").value("An example quiz."));

    mockMvc
        .perform(get("/quiz/" + quizId.get() + "/revision"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              JSONArray json = new JSONArray(result.getResponse().getContentAsString());
              assert json.length() == 3;
              assert json.getJSONObject(0)
                  .getJSONObject("quiz")
                  .getString("title")
                  .equals("Example Quiz");
              assert json.getJSONObject(1)
                  .getJSONObject("quiz")
                  .getString("title")
                  .equals("Updated Quiz");
              revisionId.set(UUID.fromString(json.getJSONObject(1).getString("revisionId")));
              assert json.getJSONObject(2)
                  .getJSONObject("quiz")
                  .getString("title")
                  .equals("Example Quiz");
            });

    mockMvc
        .perform(get("/quiz/" + quizId.get() + "/revision/" + revisionId.get()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Quiz"))
        .andExpect(jsonPath("$.description").value("An updated quiz."));
  }
}
