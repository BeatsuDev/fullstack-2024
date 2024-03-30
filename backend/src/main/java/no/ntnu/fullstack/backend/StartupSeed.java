package no.ntnu.fullstack.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.category.CategoryService;
import no.ntnu.fullstack.backend.category.model.Category;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.RevisionService;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.user.UserRepository;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupSeed {

  private final CategoryService categoryService;
  private final UserRepository userRepository;
  private final Environment environment;
  private final PasswordEncoder encoder;
  private final QuizService quizService;
  private final RevisionService revisionService;
  private final Logger logger = Logger.getLogger(StartupSeed.class.getName());

  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (categoryService.getAllCategories().isEmpty()) {
      createCategories();
    }

    if (List.of(environment.getActiveProfiles()).contains("dev")
        && userRepository.findAll().isEmpty()) {
      User user = createUser();
      createExampleQuiz(user);
    }
  }

  private void createCategories() {
    logger.info("Inserting default categories");

    List<Category> categories =
        List.of(
            Category.builder().name("Science").color("#00FF00").build(),
            Category.builder().name("History").color("#0000FF").build(),
            Category.builder().name("Geography").color("#FFFF00").build(),
            Category.builder().name("Literature").color("#FF00FF").build(),
            Category.builder().name("Art").color("#00FFFF").build(),
            Category.builder().name("Music").color("#000000").build(),
            Category.builder().name("Sports").color("#FFFFFF").build());

    categories.forEach(categoryService::saveCategory);
  }

  private User createUser() {
    logger.info("Creating example user");
    User user = new User();
    user.setEmail("test@example.org");
    user.setName("Test User");
    user.setPassword(encoder.encode("password"));
    return userRepository.saveAndFlush(user);
  }

  private void createExampleQuiz(User user) {
    logger.info("Creating example quiz");
    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    Quiz quiz = new Quiz();
    quiz.setCreator(user);

    Revision revision = new Revision();
    revision.setTitle("Påskenøtter");
    revision.setDescription("En quiz med påskenøtter");
    revision.setCreator(user);
    revision.setCategories(List.of(categoryService.getAllCategories().get(0)));
    revision.setDifficulty(1);
    QuizWithRevision newQuiz = quizService.createQuiz(quiz, revision);

    exampleQuestions()
        .forEach(
            question -> {
              try {
                revisionService.createQuestion(newQuiz.getQuiz().getId(), question);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });
  }

  private List<Question> exampleQuestions() {
    List<Question> questions = new ArrayList<>();

    questions.add(
        Question.builder()
            .questionId(UUID.randomUUID())
            .question("Hva er hovedstaden i Norge?")
            .sequenceNumber(0)
            .answer("Oslo")
            .options(
                Stream.of("Oslo", "Bergen", "Trondheim", "Kristiansand")
                    .map(QuestionOption::new)
                    .toList())
            .build());
    questions.add(
        Question.builder()
            .questionId(UUID.randomUUID())
            .question("Hva er 2 + 2?")
            .sequenceNumber(1)
            .answer("4")
            .options(List.of())
            .build());
    questions.add(
        Question.builder()
            .questionId(UUID.randomUUID())
            .question("Har du det bra?")
            .sequenceNumber(2)
            .answer("True")
            .options(Stream.of("True", "False").map(QuestionOption::new).toList())
            .build());
    return questions;
  }
}
