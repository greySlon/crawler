package com.odessaflat.events;

public class Event {

  protected Object sender;
  protected final EventType eventType;

  public Event(Object sender, EventType eventType) {
    this.sender = sender;
    this.eventType = eventType;
  }

  public EventType getEventType() {
    return eventType;
  }
}
