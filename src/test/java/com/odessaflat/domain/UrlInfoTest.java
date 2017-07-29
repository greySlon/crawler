package com.odessaflat.domain;

//import static junit.framework.TestCase.assertFalse;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.net.URL;

public class UrlInfoTest {

  private UrlInfo urlInfo;

  @Test
  public void isOuterLink1() throws Exception {
    urlInfo = new UrlInfo(new URL("http://child.com"), null);
    assertThat(urlInfo.isOuterLink()).isFalse();
  }

  @Test
  public void isOuterLink2() throws Exception {
    urlInfo = new UrlInfo(new URL("http://child.com"), new URL("http://parent.com"));
    assertThat(urlInfo.isOuterLink()).isTrue();

  }

  @Test
  public void isOuterLink3() throws Exception {
    urlInfo = new UrlInfo(new URL("http://domain.com"), new URL("http://domain.com"));
    assertThat(urlInfo.isOuterLink()).isFalse();
  }

}