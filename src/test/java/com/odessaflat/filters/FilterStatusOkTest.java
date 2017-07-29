package com.odessaflat.filters;

import static org.assertj.core.api.Assertions.assertThat;

import com.odessaflat.domain.UrlInfo;
import com.odessaflat.events.CrawlerStopedEvent;
import com.odessaflat.events.UrlFoundEvent;
import com.odessaflat.events.UrlProcessedEvent;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;


public class FilterStatusOkTest {

  private FilterStatusOk filterStatusOk;

  @Before
  public void setUp() {
    filterStatusOk = new FilterStatusOk();
  }

  @Test
  public void test1() throws Exception {
    UrlInfo urlInfo = new UrlInfo(new URL("http://domain.com"), null);
    UrlFoundEvent event = new UrlFoundEvent(null, urlInfo);
    event.getUrlInfo().setHttpStatusCode(200);
    assertThat(filterStatusOk.test(event)).isFalse();
  }

  @Test
  public void test2() throws Exception {
    UrlInfo urlInfo = new UrlInfo(new URL("http://domain.com"), null);
    UrlFoundEvent event = new UrlFoundEvent(null, urlInfo);
    event.getUrlInfo().setHttpStatusCode(404);
    assertThat(filterStatusOk.test(event)).isFalse();
  }

  @Test
  public void test3() throws Exception {
    UrlInfo urlInfo = new UrlInfo(new URL("http://domain.com"), null);
    UrlProcessedEvent event = new UrlProcessedEvent(null, urlInfo);
    event.getUrlInfo().setHttpStatusCode(200);
    assertThat(filterStatusOk.test(event)).isTrue();
  }

  @Test
  public void test4() throws Exception {
    UrlInfo urlInfo = new UrlInfo(new URL("http://domain.com"), null);
    UrlProcessedEvent event = new UrlProcessedEvent(null, urlInfo);
    event.getUrlInfo().setHttpStatusCode(404);
    assertThat(filterStatusOk.test(event)).isFalse();
  }
}