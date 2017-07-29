package com.odessaflat.filters;

import static com.odessaflat.events.EventType.URL_PROCESSED;

import com.odessaflat.events.Event;
import com.odessaflat.events.UrlProcessedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FilterStatusNotFound extends Filter {

  private Logger logger = LogManager.getLogger();

  @Override
  public boolean test(Event event) {
    logger.traceEntry(event.toString());
    if (event.getEventType() == URL_PROCESSED) {
      return logger.traceExit(((UrlProcessedEvent) event).getUrlInfo().getHttpStatusCode() == 404);
    }
    return logger.traceExit(false);
  }
}
