package com.odessaflat.events;

import java.net.URL;

public class UrlFoundEvent extends Event {

  private URL url;

  public UrlFoundEvent(Object sender, URL url) {
    super(sender, EventType.URL_FOUND);
    this.url = url;
  }

  @Override
  public String toString() {
    return "UrlFoundEvent{"
        + "sender=" + sender
        + ", url=" + url
        + '}';
  }
}
