package com.odessaflat.filters;

import static com.odessaflat.events.EventType.URL_FOUND;

import com.odessaflat.events.Event;
import com.odessaflat.events.UrlFoundEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FilterOuterLink extends Filter {

  private Logger logger = LogManager.getLogger();

  @Override
  public boolean test(Event event) {
    logger.traceEntry(event.toString());
    if (event.getEventType() == URL_FOUND) {
      return logger.traceExit(((UrlFoundEvent) event).getUrlInfo().isOuterLink());
    }
    return logger.traceExit(false);
  }
}
