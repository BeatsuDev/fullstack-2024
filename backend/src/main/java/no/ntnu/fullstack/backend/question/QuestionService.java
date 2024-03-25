package no.ntnu.fullstack.backend.question;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

  public Question makeQuestionCopy(Question question) {
    Question copy = new Question();
    copy.setQuestion(question.getQuestion());
    copy.setAnswer(question.getAnswer());

    copy.setOptions(new ArrayList<>());
    for (QuestionOption option : question.getOptions()) {
      QuestionOption optionCopy = new QuestionOption();
      optionCopy.setOption(option.getOption());
      optionCopy.setQuestion(copy);
      copy.getOptions().add(optionCopy);
    }

    return copy;
  }
}
