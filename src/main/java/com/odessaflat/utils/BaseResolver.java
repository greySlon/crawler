package com.odessaflat.utils;

import com.odessaflat.utils.DataTag;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BaseResolver {

  private Logger logger = LogManager.getLogger();
  private Pattern pattern = Pattern.compile("<base[^>]+>");
  @Autowired
  DataTag dataTag;

  public URL getBaseUrl(String content, URL url) {
    logger.traceEntry("Content length:{}, url:{}", content.length(), url.toString());
    try {
      Matcher matcher = pattern.matcher(content);
      if (matcher.find()) {
        String base = dataTag.getAttribute(matcher.group(), "href")
            .orElseThrow(
                () -> new MalformedURLException("There is no base tag in content on url: " + url));
        logger.trace("Base:{}", base);
        return logger.traceExit("BaseUrl:{}", new URL(url, base));
      }
    } catch (MalformedURLException ex) {
      logger.catching(Level.INFO, ex);
    }
    logger.traceExit("No base -> BaseUrl:{}", url);
    return url;
  }
}
