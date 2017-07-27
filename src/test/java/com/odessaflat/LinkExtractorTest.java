package com.odessaflat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.odessaflat.crawler.LinkExtractor;
import com.odessaflat.utils.BaseResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.Collection;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LinkExtractorTest {

  private URL url;
  private String content;
  @Autowired
  private LinkExtractor linkExtractor;
  @MockBean
  private BaseResolver baseResolver;

  @Before
  public void setUp() throws Exception {
    url = new URL("http://domain.com");
    when(baseResolver.getBaseUrl(anyString(), any(URL.class))).thenReturn(url);
  }

  @Test
  public void getUrls1() throws Exception {
    content = "<a href=\"file.html\">link</a>some text <a href=\"/segment1/resourse\">link</a>";
    Collection<URL> urls = linkExtractor.getUrls(url, content);
    assertEquals(2, urls.size());
  }

  @Test
  public void getUrls2() throws Exception {
    content = "<a href=\"\">link</a>some text <a href=\"\">link</a>";
    Collection<URL> urls = linkExtractor.getUrls(url, content);
    assertEquals(0, urls.size());
  }

}