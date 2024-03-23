package no.ntnu.fullstack.backend.quiz;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {
  private final QuizRepository quizRepository;
  private final RevisionRepository revisionRepository;

  public Quiz createQuiz(Quiz quiz, Revision revision) {
    Quiz createdQuiz = quizRepository.saveAndFlush(quiz);
    revisionRepository.saveAndFlush(revision);
    return createdQuiz;
  }
}
