package no.ntnu.fullstack.backend.question;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import net.minidev.json.JSONObject;
import no.ntnu.fullstack.backend.StartupSeed;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
import no.ntnu.fullstack.backend.revision.RevisionRepository;
import no.ntnu.fullstack.backend.revision.model.Revision;
import no.ntnu.fullstack.backend.security.UserDetailsImpl;
import no.ntnu.fullstack.backend.user.UserRepository;
import no.ntnu.fullstack.backend.user.model.User;
import org.junit.jupiter.api.AfterEach;
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
public class QuestionControllerIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private StartupSeed startupSeed;
  @Autowired private UserRepository userRepository;
  @Autowired private RevisionRepository revisionRepository;
  @Autowired private QuizRepository quizRepository;
  @MockBean private UserDetailsImpl userDetails;
  private UUID quizId;

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

    Revision revision = new Revision();
    revision.setCategories(Collections.emptyList());
    revision.setCreatedAt(new Date());
    revision.setCreator(user);
    revision.setDescription("description");
    revision.setDifficulty(1);
    revision.setQuestions(Collections.emptyList());
    revision.setTitle("title");
    revision.setQuiz(quiz);
    revisionRepository.saveAndFlush(revision);

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
  public void addQuestion_InsertsCreated() throws Exception {
    JSONObject question = new JSONObject();
    question.put("question", "What is the capital of Norway?");
    question.put("answer", "Oslo");
    question.put("options", new String[] {"Oslo", "Stockholm", "Copenhagen", "Helsinki"});
    question.put("quizId", quizId.toString());

    mockMvc
        .perform(
            post("/question")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(question.toJSONString()))
        .andExpect(status().isCreated());

    quizRepository
        .findWithFirstRevision(quizId)
        .ifPresentOrElse(
            q -> {
              assert q.getLatestRevision().getQuestions().size() == 1;
              assert q.getLatestRevision()
                  .getQuestions()
                  .get(0)
                  .getQuestion()
                  .equals("What is the capital of Norway?");
              assert q.getLatestRevision().getQuestions().get(0).getOptions().size() == 4;
            },
            () -> {
              throw new RuntimeException("Quiz not found");
            });
  }

  @Test
  public void updateQuestion_UpdatesQuestion() throws Exception {
    JSONObject question = new JSONObject();
    question.put("question", "What is the capital of Norway?");
    question.put("answer", "Oslo");
    question.put("options", new String[] {"Oslo", "Stockholm", "Copenhagen", "Helsinki"});
    question.put("quizId", quizId.toString());

    mockMvc
        .perform(
            post("/question")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(question.toJSONString()))
        .andExpect(status().isCreated());

    UUID questionId =
        quizRepository
            .findWithFirstRevision(quizId)
            .map(q -> q.getLatestRevision().getQuestions().get(0).getQuestionId())
            .orElseThrow();

    JSONObject updatedQuestion = new JSONObject();
    updatedQuestion.put("question", "What is the capital of Sweden?");
    updatedQuestion.put("answer", "Stockholm");
    updatedQuestion.put("options", new String[] {"Stockholm", "Copenhagen", "Helsinki"});
    updatedQuestion.put("quizId", quizId.toString());

    mockMvc
        .perform(
            put("/question/" + questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedQuestion.toJSONString()))
        .andExpect(status().isOk());

    quizRepository
        .findWithFirstRevision(quizId)
        .ifPresentOrElse(
            q -> {
              assert q.getLatestRevision().getQuestions().size() == 1;
              assert q.getLatestRevision()
                  .getQuestions()
                  .get(0)
                  .getQuestion()
                  .equals("What is the capital of Sweden?");
              assert q.getLatestRevision().getQuestions().get(0).getOptions().size() == 3;
            },
            () -> {
              throw new RuntimeException("Quiz not found");
            });
  }

  @Test
  public void createQuestion_AnswerNotInOptions_ReturnsBadRequest() throws Exception {
    JSONObject question = new JSONObject();
    question.put("question", "What is the capital of Norway?");
    question.put("answer", "Helsinki");
    question.put("options", new String[] {"Oslo", "Stockholm", "Copenhagen"});
    question.put("quizId", quizId.toString());

    mockMvc
        .perform(
            post("/question")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(question.toJSONString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void deleteQuestion_RemovesQuestion() throws Exception {
    JSONObject question = new JSONObject();
    question.put("question", "What is the capital of Norway?");
    question.put("answer", "Oslo");
    question.put("options", new String[] {"Oslo", "Stockholm", "Copenhagen", "Helsinki"});
    question.put("quizId", quizId.toString());

    mockMvc
        .perform(
            post("/question")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(question.toJSONString()))
        .andExpect(status().isCreated());

    UUID questionId =
        quizRepository
            .findWithFirstRevision(quizId)
            .map(q -> q.getLatestRevision().getQuestions().get(0).getQuestionId())
            .orElseThrow();

    mockMvc.perform(delete("/question/" + questionId)).andExpect(status().isNoContent());

    quizRepository
        .findWithFirstRevision(quizId)
        .ifPresentOrElse(
            q -> {
              assert q.getLatestRevision().getQuestions().isEmpty();
            },
            () -> {
              throw new RuntimeException("Quiz not found");
            });
  }
}
