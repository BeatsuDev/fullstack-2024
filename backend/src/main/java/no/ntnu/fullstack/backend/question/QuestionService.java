package no.ntnu.fullstack.backend.question;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.exception.NoCorrectOptionException;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import no.ntnu.fullstack.backend.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
  private final QuestionRepository questionRepository;

  public Question getQuestion(UUID id) throws QuestionNotFoundException {
    return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
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
