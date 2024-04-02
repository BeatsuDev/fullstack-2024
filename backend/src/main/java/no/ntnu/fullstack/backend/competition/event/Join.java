package no.ntnu.fullstack.backend.competition.event;

public final class Join implements Event {
  @Override
  public String toMessage() {
    return "JOIN";
  }
}
