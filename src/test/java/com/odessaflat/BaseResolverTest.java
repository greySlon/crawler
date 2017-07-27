package com.odessaflat;

import static org.junit.Assert.assertEquals;

import com.odessaflat.utils.BaseResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseResolverTest {

  @Autowired
  private BaseResolver baseResolver;
  private URL url;

  @Before
  public void setUp() throws Exception {
    url = new URL("http://domain.com/segment1/segment2");
  }

  @Test
  public void getBaseUrl1() throws Exception {
    String content = "some text<base href=\"http://another.com\">";
    assertEquals("http://another.com", baseResolver.getBaseUrl(content, url).toString());
  }

  @Test
  public void getBaseUrl2() throws Exception {
    String content = "some text<base href=\"./folder2\">";
    assertEquals("http://domain.com/segment1/folder2",
        baseResolver.getBaseUrl(content, url).toString());
  }

  @Test
  public void getBaseUrl3() throws Exception {
    String content = "some text";
    assertEquals("http://domain.com/segment1/segment2",
        baseResolver.getBaseUrl(content, url).toString());
  }

  @Test
  public void getBaseUrl4() throws Exception {
    String content = "some text<base href=\"/folder2\">";
    assertEquals("http://domain.com/folder2", baseResolver.getBaseUrl(content, url).toString());
  }

  @Test
  public void getBaseUrl5() throws Exception {
    String content = "some text<base href=\"folder2\">";
    assertEquals("http://domain.com/segment1/folder2",
        baseResolver.getBaseUrl(content, url).toString());
  }

}