package no.ntnu.fullstack.backend.question;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import no.ntnu.fullstack.backend.question.repository.QuestionRepository;
import no.ntnu.fullstack.backend.quiz.QuizService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
  private final QuestionRepository questionRepository;
  private final QuizService quizService;

  public Optional<Question> getQuestion(UUID id) {
    return questionRepository.findById(id);
  }

  public boolean checkIfQuestionExists(UUID id) {
    return questionRepository.existsById(id);
  }

  private Question saveQuestion(Question question) {
    return questionRepository.save(question);
  }

  public Question makeQuestionCopy(Question question) {
    Question copy = new Question();
    copy.setQuestion(question.getQuestion());
    copy.setAnswer(question.getAnswer());

    copy.setOptions(new ArrayList<>());
    for (QuestionOption option : question.getOptions()) {
      QuestionOption optionCopy = new QuestionOption();
      optionCopy.setOption(option.getOption());
      copy.getOptions().add(optionCopy);
    }

    return copy;
  }
}
