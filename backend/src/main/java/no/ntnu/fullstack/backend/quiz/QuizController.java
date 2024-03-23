package no.ntnu.fullstack.backend.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/quiz")
@RequiredArgsConstructor
public class QuizController {
  private final QuizService quizService;
}
