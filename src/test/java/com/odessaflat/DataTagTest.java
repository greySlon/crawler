package com.odessaflat;

import static org.assertj.core.api.Assertions.assertThat;

import com.odessaflat.utils.DataTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DataTagTest {

  @Autowired
  private DataTag dataTag;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void getAttribute1() throws Exception {
    Optional<String> href = dataTag.getAttribute("<a href=\"/page.asp\" >", "href");
    assertThat(href.get()).isEqualTo("/page.asp");
  }

  @Test
  public void getAttribute2() throws Exception {
    Optional<String> href = dataTag.getAttribute("<a class=\"cls\" href = \"/page.asp\">", "href");
    assertThat(href.get()).isEqualTo("/page.asp");
  }

  @Test
  public void getAttribute3() throws Exception {
    Optional<String> href = dataTag.getAttribute("<a class=\"cls\" >", "href");
    assertThat(href.orElse(null)).isEqualTo(null);
  }
}