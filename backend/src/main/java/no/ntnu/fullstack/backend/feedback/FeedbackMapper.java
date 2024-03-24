package no.ntnu.fullstack.backend.feedback;

import no.ntnu.fullstack.backend.feedback.dto.FeedbackCreate;
import no.ntnu.fullstack.backend.feedback.dto.FeedbackDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class FeedbackMapper {
    public abstract Feedback toFeedback(FeedbackCreate feedbackCreate);

    @Mapping(source = "user.name", target = "username")
    public abstract FeedbackDTO toFeedbackDTO(Feedback feedback);
}
