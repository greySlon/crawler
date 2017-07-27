package com.odessaflat.events;

import com.odessaflat.domain.UrlInfo;

public class UrlProcessedEvent extends Event {

  private UrlInfo urlInfo;
  private String content;

  public UrlProcessedEvent(Object sender, UrlInfo urlInfo) {
    super(sender);
    this.urlInfo = urlInfo;
  }

  @Override
  public String toString() {
    return "UrlProcessedEvent{"
        + "sender=" + sender
        + ", url=" + urlInfo.getUrl()
        + '}';
  }
}
