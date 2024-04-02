package no.ntnu.fullstack.backend.competition.event;

public final class Finish implements Event {
  @Override
  public String toMessage() {
    return "FINISH";
  }
}
