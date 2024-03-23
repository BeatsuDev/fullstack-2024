package no.ntnu.fullstack.backend.quiz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {
  private final QuizRepository quizRepository;
  private final RevisionRepository revisionRepository;

  @Transactional
  public Quiz createQuiz(Quiz quiz, Revision revision) {
    Quiz createdQuiz = quizRepository.saveAndFlush(quiz);
    revision.setQuiz(createdQuiz);
    revisionRepository.saveAndFlush(revision);
    return createdQuiz;
  }
}
