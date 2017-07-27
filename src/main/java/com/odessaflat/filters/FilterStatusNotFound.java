package com.odessaflat.filters;

import static com.odessaflat.events.EventType.URL_PROCESSED;

import com.odessaflat.events.Event;
import com.odessaflat.events.UrlProcessedEvent;

public class FilterStatusNotFound extends Filter {

  @Override
  public boolean test(Event event) {
    if (event.getEventType() != URL_PROCESSED) {
      return true;
    }
    if (((UrlProcessedEvent) event).getUrlInfo().getHttpStatusCode() == 404) {
      return true;
    }
    return false;
  }
}
