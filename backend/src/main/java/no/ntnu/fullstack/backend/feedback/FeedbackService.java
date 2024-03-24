package no.ntnu.fullstack.backend.feedback;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackService {
  private final FeedbackRepository feedbackRepository;

  public Feedback createFeedback(Feedback feedback, Quiz quiz, User user) {
    feedback.setQuiz(quiz);
    feedback.setUser(user);
    return feedbackRepository.saveAndFlush(feedback);
  }

  public List<Feedback> getFeedbacksByQuizId(UUID quizId) {
    return feedbackRepository.findAllByQuizId(quizId);
  }
}
