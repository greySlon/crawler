package com.odessaflat.events;

public class CrawlerStopedEvent extends Event {

  public CrawlerStopedEvent(Object sender) {
    super(sender, EventType.STOPPED);
  }

  @Override
  public String toString() {
    return "CrawlerStopedEvent{" +
        "eventType=" + eventType +
        '}';
  }
}
