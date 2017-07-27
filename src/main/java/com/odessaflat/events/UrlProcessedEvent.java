package com.odessaflat.events;

import com.odessaflat.domain.UrlInfo;

public class UrlProcessedEvent extends Event {

  private UrlInfo urlInfo;
  private String content;

  public UrlProcessedEvent(Object sender, UrlInfo urlInfo) {
    super(sender, EventType.URL_PROCESSED);
    this.urlInfo = urlInfo;
  }

  public UrlInfo getUrlInfo() {
    return urlInfo;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "UrlProcessedEvent{"
        + "sender=" + sender
        + ", url=" + urlInfo.getUrl()
        + '}';
  }
}
