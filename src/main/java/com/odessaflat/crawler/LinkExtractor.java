package com.odessaflat.crawler;


import com.odessaflat.utils.BaseResolver;
import com.odessaflat.utils.DataTag;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LinkExtractor {

  private Logger logger = LogManager.getLogger();
  private final Pattern pattern = Pattern.compile("<a[^>]+>");
  @Autowired
  private DataTag dataTag;
  @Autowired
  private BaseResolver baseResolver;

  public Collection<URL> getUrls(URL url, String content) {
    logger.traceEntry("Content length:{}, url:{}", content.length(), url.toString());
    Collection<URL> urlCollection = new ArrayList<>();
    URL baseUrl = baseResolver.getBaseUrl(content, url);
    logger.trace("Url relative:{}", baseUrl);
    Matcher matcher = pattern.matcher(content);
    while (matcher.find()) {
      Optional<String> href = dataTag.getAttribute(matcher.group(), "href");
      try {
        if (href.isPresent()) {
          String clearedHref = href.get()
              .replace("&amp;", "&")
              .replaceFirst("/$", "");
          URL urlFound = new URL(baseUrl, clearedHref);
          logger.trace("Href:{} -> Url:{}", href.get(), urlFound);
          urlCollection.add(urlFound);
        }
      } catch (MalformedURLException ex) {
        logger.catching(Level.INFO, ex);
      }
    }

    logger.traceExit("Urls found:{}", urlCollection.size());
    return urlCollection;
  }
}
