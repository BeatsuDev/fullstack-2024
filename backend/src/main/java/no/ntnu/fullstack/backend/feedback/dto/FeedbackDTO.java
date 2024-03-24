package no.ntnu.fullstack.backend.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {

    private String id;
    private String feedback;
    private String username;
    private String createdAt;

}
