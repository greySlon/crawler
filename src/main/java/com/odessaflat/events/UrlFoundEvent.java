package com.odessaflat.events;

import com.odessaflat.domain.UrlInfo;

public class UrlFoundEvent extends Event {

  private UrlInfo urlInfo;

  public UrlFoundEvent(Object sender, UrlInfo urlInfo) {
    super(sender, EventType.URL_FOUND);
    this.urlInfo = urlInfo;
  }

  public UrlInfo getUrlInfo() {
    return urlInfo;
  }

  @Override
  public String toString() {
    return "UrlFoundEvent{" +
        "eventType=" + eventType +
        ", urlInfo=" + urlInfo +
        '}';
  }
}
