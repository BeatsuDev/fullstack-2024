package no.ntnu.fullstack.backend.competition.event;

public sealed interface Event permits Proceed, Finish, Join {
  String toMessage();
}
