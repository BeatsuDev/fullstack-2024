package no.ntnu.fullstack.backend.question;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.exception.NoCorrectOptionException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import no.ntnu.fullstack.backend.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
  private final QuestionRepository questionRepository;

  public Optional<Question> getQuestion(UUID id) {
    return questionRepository.findById(id);
  }

  public Optional<Question> getLatestQuestionByQuestionId(UUID questionId) {
    return questionRepository.findLatestByQuestionId(questionId);
  }

  public void validateQuestion(Question question) throws NoCorrectOptionException {
    if (!question.getOptions().isEmpty()
        && question.getOptions().stream()
            .map(QuestionOption::getOption)
            .noneMatch(o -> o.equals(question.getAnswer()))) {
      throw new NoCorrectOptionException();
    }
  }
}
