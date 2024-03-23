package no.ntnu.fullstack.backend.quiz.repository;

import java.util.List;
import java.util.UUID;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.LatestQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {


    //TODO: Only return the latest revision for each quiz
    @Query("SELECT new no.ntnu.fullstack.backend.quiz.model.QuizWithLatestRevision(q, r) FROM Quiz q JOIN Revision r ON q.id = r.quiz.id")
    List<LatestQuiz> findWithFirstRevision();

}
