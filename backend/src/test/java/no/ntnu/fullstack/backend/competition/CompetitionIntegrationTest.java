package no.ntnu.fullstack.backend.competition;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import no.ntnu.fullstack.backend.StartupSeed;
import no.ntnu.fullstack.backend.attempt.repository.QuestionAttemptRepository;
import no.ntnu.fullstack.backend.attempt.repository.QuizAttemptRepository;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.image.exception.ImageNotFound;
import no.ntnu.fullstack.backend.question.exception.NoCorrectOptionException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import no.ntnu.fullstack.backend.question.repository.QuestionRepository;
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
public class CompetitionIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private StartupSeed startupSeed;
  @Autowired private UserRepository userRepository;
  @Autowired private QuizRepository quizRepository;
  @Autowired private QuestionRepository questionRepository;
  @Autowired private CompetitionRepository competitionRepository;
  @Autowired private QuestionAttemptRepository questionAttemptRepository;
  @Autowired private QuizAttemptRepository quizAttemptRepository;
  @Autowired private RevisionRepository revisionRepository;
  @Autowired private RevisionService revisionService;
  @Autowired private QuizService quizService;

  private UUID quizId;

  @BeforeEach
  public void setup()
      throws QuizNotFoundException,
          NotCollaboratorException,
          NoCorrectOptionException,
          ImageNotFound {
    try {
      startupSeed.onApplicationEvent(null);

      User user = new User();
      user.setEmail("user@example");
      user.setPassword("password");
      user.setName("user");
      userRepository.save(user);

      Authentication auth =
          new TestingAuthenticationToken(user, "password", Collections.emptyList());
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

      List<Question> questions = new ArrayList<>();
      Question question =
          Question.builder()
              .question("Hva er 1 + 1?")
              .answer("2")
              .options(Stream.of("1", "2", "3", "4").map(QuestionOption::new).toList())
              .build();
      questions.add(revisionService.createQuestion(quizId, question, null));

      question =
          Question.builder()
              .question("Hva er 2 + 2?")
              .answer("4")
              .options(Stream.of("3", "4", "5", "6").map(QuestionOption::new).toList())
              .build();
      questions.add(revisionService.createQuestion(quizId, question, null));
      questions.forEach(
          q -> {
            try {
              revisionService.createQuestion(quizId, q, null);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          });
    } catch (Exception e) {
    }
  }

  @AfterEach
  public void cleanUp() {
    competitionRepository.deleteAll();
    questionAttemptRepository.deleteAll();
    quizAttemptRepository.deleteAll();
    revisionRepository.deleteAll();
    questionRepository.deleteAll();
    quizRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testCreateCompetition() throws Exception {
    mockMvc.perform(post("/quiz/" + quizId + "/competition")).andExpect(status().isCreated());
  }

  @Test
  void testJoinCompetition() throws Exception {
    AtomicInteger joinCode = new AtomicInteger();
    mockMvc
        .perform(post("/quiz/" + quizId + "/competition"))
        .andExpect(status().isCreated())
        .andDo(
            r -> {
              JSONObject jsonObject = new JSONObject(r.getResponse().getContentAsString());
              joinCode.set(Integer.parseInt(jsonObject.get("joinCode").toString()));
            });

    mockMvc.perform(post("/competition/" + joinCode)).andExpect(status().isOk());
  }

  @Test
  void testGetCompetition() throws Exception {
    AtomicInteger joinCode = new AtomicInteger();
    mockMvc
        .perform(post("/quiz/" + quizId + "/competition"))
        .andExpect(status().isCreated())
        .andDo(
            r -> {
              JSONObject jsonObject = new JSONObject(r.getResponse().getContentAsString());
              joinCode.set(Integer.parseInt(jsonObject.get("joinCode").toString()));
            });

    mockMvc.perform(post("/competition/" + joinCode)).andExpect(status().isOk());
    mockMvc.perform(get("/competition/" + joinCode)).andExpect(status().isOk());
  }

  @Test
  public void testStartCompetition() throws Exception {
    AtomicInteger joinCode = new AtomicInteger();
    mockMvc
        .perform(post("/quiz/" + quizId + "/competition"))
        .andExpect(status().isCreated())
        .andDo(
            r -> {
              JSONObject jsonObject = new JSONObject(r.getResponse().getContentAsString());
              joinCode.set(Integer.parseInt(jsonObject.get("joinCode").toString()));
            });

    mockMvc.perform(post("/competition/" + joinCode)).andExpect(status().isOk());
    mockMvc.perform(put("/competition/" + joinCode)).andExpect(status().isOk());
  }
}
