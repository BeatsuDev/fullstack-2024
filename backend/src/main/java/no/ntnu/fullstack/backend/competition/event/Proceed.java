package no.ntnu.fullstack.backend.competition.event;

import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Proceed implements Event {
  private final UUID questionId;

  @Override
  public String toMessage() {
    return "PROCEED:" + questionId;
  }
}
