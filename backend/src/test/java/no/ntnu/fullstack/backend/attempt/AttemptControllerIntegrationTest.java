package no.ntnu.fullstack.backend.attempt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import no.ntnu.fullstack.backend.StartupSeed;
import no.ntnu.fullstack.backend.attempt.repository.QuestionAttemptRepository;
import no.ntnu.fullstack.backend.attempt.repository.QuizAttemptRepository;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.question.exception.NoCorrectOptionException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
import no.ntnu.fullstack.backend.revision.RevisionRepository;
import no.ntnu.fullstack.backend.revision.RevisionService;
import no.ntnu.fullstack.backend.revision.model.Revision;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AttemptControllerIntegrationTest {
  @Autowired MockMvc mockMvc;
  @Autowired StartupSeed startupSeed;

  @Autowired private RevisionRepository revisionRepository;
  @Autowired private QuizRepository quizRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private QuestionAttemptRepository questionAttemptRepository;
  @Autowired private QuizAttemptRepository quizAttemptRepository;
  @Autowired private QuizService quizService;
  @Autowired private RevisionService revisionService;

  private UUID quizId;
  private List<Question> questions;

  @BeforeEach
  public void setup()
      throws NotCollaboratorException, QuizNotFoundException, NoCorrectOptionException {
    startupSeed.onApplicationEvent(null);

    User user = new User();
    user.setEmail("user@example");
    user.setPassword("password");
    user.setName("user");
    userRepository.save(user);

    Authentication auth = new TestingAuthenticationToken(user, "password", Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);

    Revision revision = new Revision();
    revision.setCreator(user);
    revision.setTitle("Test");
    revision.setDescription("Test description");
    revision.setDifficulty(1);
    revision.setCategories(List.of());

    Quiz quiz = new Quiz();
    quiz.setCreator(user);
    QuizWithRevision quizWithRevision = quizService.createQuiz(quiz, revision);
    quizId = quizWithRevision.getQuiz().getId();

    questions = new ArrayList<>();
    Question question =
        Question.builder()
            .question("Hva er 1 + 1?")
            .answer("2")
            .options(Stream.of("1", "2", "3", "4").map(QuestionOption::new).toList())
            .build();
    questions.add(revisionService.createQuestion(quizId, question));

    question =
        Question.builder()
            .question("Hva er 2 + 2?")
            .answer("4")
            .options(Stream.of("3", "4", "5", "6").map(QuestionOption::new).toList())
            .build();
    questions.add(revisionService.createQuestion(quizId, question));
  }

  @AfterEach
  public void cleanUp() {
    questionAttemptRepository.deleteAll();
    quizAttemptRepository.deleteAll();
    revisionRepository.deleteAll();
    quizRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testAttemptControllerHappyPath() throws Exception {
    AtomicReference<UUID> attemptId = new AtomicReference<>(null);
    mockMvc
        .perform(post("/quiz/" + quizId + "/attempt"))
        .andExpect(status().isCreated())
        .andDo(
            result -> {
              JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
              attemptId.set(UUID.fromString(jsonObject.get("id").toString()));
            });

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("questionId", questions.get(0).getId().toString());
    jsonObject.put("answer", "2");
    mockMvc
        .perform(
            post("/quiz/" + quizId + "/attempt/" + attemptId.get(), jsonObject.toString())
                .contentType("application/json")
                .content(jsonObject.toString()))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              if (!result.getResponse().getContentAsString().contains("\"correct\":true")) {
                throw new AssertionError("The answer should be true");
              }
            });

    jsonObject = new JSONObject();
    jsonObject.put("questionId", questions.get(1).getId().toString());
    jsonObject.put("answer", "3");
    mockMvc
        .perform(
            post("/quiz/" + quizId + "/attempt/" + attemptId.get(), jsonObject.toString())
                .contentType("application/json")
                .content(jsonObject.toString()))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              if (!result.getResponse().getContentAsString().contains("\"correct\":false")) {
                throw new AssertionError("The answer should be false");
              }
            });

    mockMvc
        .perform(get("/quiz/" + quizId + "/attempt"))
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              JSONArray jsonArray = new JSONArray(result.getResponse().getContentAsString());
              if (jsonArray.length() != 1) {
                throw new AssertionError("There should be 1 quiz attempts");
              }
            });
  }
}
