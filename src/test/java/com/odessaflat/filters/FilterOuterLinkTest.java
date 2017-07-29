package com.odessaflat.filters;

import static org.assertj.core.api.Assertions.assertThat;

import com.odessaflat.domain.UrlInfo;
import com.odessaflat.events.UrlFoundEvent;
import com.odessaflat.events.UrlProcessedEvent;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;

public class FilterOuterLinkTest {

  private FilterOuterLink filterOuterLink;

  @Before
  public void setUp() throws Exception {
    filterOuterLink = new FilterOuterLink();

  }

  @Test
  public void test1() throws Exception {
    UrlInfo parentUrlInfo = new UrlInfo(new URL("http://child.com"), new URL("http://parent.com"));
    UrlFoundEvent foundEvent = new UrlFoundEvent(null, parentUrlInfo);
    assertThat(filterOuterLink.test(foundEvent)).isTrue();
  }

  @Test
  public void test2() throws Exception {
    UrlInfo parentUrlInfo = new UrlInfo(new URL("http://domain.com"), new URL("http://domain.com"));
    UrlFoundEvent foundEvent = new UrlFoundEvent(null, parentUrlInfo);
    assertThat(filterOuterLink.test(foundEvent)).isFalse();
  }

  @Test
  public void test3() throws Exception {
    UrlInfo parentUrlInfo = new UrlInfo(new URL("http://child.com"), new URL("http://parent.com"));
    UrlProcessedEvent processedEvent = new UrlProcessedEvent(null, parentUrlInfo);
    assertThat(filterOuterLink.test(processedEvent)).isFalse();
  }

  @Test
  public void test4() throws Exception {
    UrlInfo parentUrlInfo = new UrlInfo(new URL("http://domain.com"), new URL("http://domain.com"));
    UrlProcessedEvent processedEvent = new UrlProcessedEvent(null, parentUrlInfo);
    assertThat(filterOuterLink.test(processedEvent)).isFalse();
  }

  @Test
  public void test5() throws Exception {
    UrlInfo parentUrlInfo = new UrlInfo(new URL("http://domain.com"), null);
    UrlProcessedEvent processedEvent = new UrlProcessedEvent(null, parentUrlInfo);
    assertThat(filterOuterLink.test(processedEvent)).isFalse();
  }
}