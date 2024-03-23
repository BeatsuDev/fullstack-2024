package no.ntnu.fullstack.backend.quiz;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.user.model.User;

@Service
@RequiredArgsConstructor
public class QuizService {
  private final QuizRepository quizRepository;
}
