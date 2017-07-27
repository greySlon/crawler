package com.odessaflat.events;

import java.net.URL;

public class UrlFoundEvent extends Event {

  private URL url;

  public UrlFoundEvent(Object sender, URL url) {
    super(sender);
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
