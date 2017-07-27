package com.odessaflat.crawler;

import com.odessaflat.domain.UrlInfo;
import com.odessaflat.events.CrawlerStopedEvent;
import com.odessaflat.events.Event;
import com.odessaflat.events.UrlFoundEvent;
import com.odessaflat.events.UrlProcessedEvent;
import com.odessaflat.exceptions.ContentLoaderException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class Crawler {

  private Logger logger = LogManager.getLogger();
  private BlockingQueue<Event> blockingQueue;
  private HashMap<URL, UrlInfo> urlInfoMap;
  private String host;
  @Autowired
  private LinkExtractor linkExtractor;

  public Crawler() {
    urlInfoMap = new HashMap<>();
    blockingQueue = new ArrayBlockingQueue<Event>(200);
  }

  public void startCrawl(URL url) throws Exception {
    logger.traceEntry("start URL{}", url);
    urlInfoMap.put(url, new UrlInfo(url));
    host = url.getHost();

    while (true) {
      List<UrlInfo> urlInfoCollection = getUrlInfoToProcessed();
      if (urlInfoCollection.isEmpty()) {
        blockingQueue.add(new CrawlerStopedEvent(this));
        logger.debug("CrawlerStopedEvent has published");
        break;
      }
      for (UrlInfo urlInfo : urlInfoCollection) {
        try {
          URL itemUrl = urlInfo.getUrl();
          linkExtractor.getUrls(itemUrl, urlInfo.getContent())
              .forEach(link -> storeLinkData(link, itemUrl));
          urlInfo.setProcessed(true);
          blockingQueue.add(new UrlProcessedEvent(this, urlInfo));
          logger.debug("URL processed:{}", itemUrl);
        } catch (ContentLoaderException ex) {
          logger.catching(Level.INFO, ex);
        }
      }
    }
  }

  private void storeLinkData(URL link, URL parent) {
    if (urlInfoMap.containsKey(link)) {
      UrlInfo tuple = urlInfoMap.get(link);
      tuple.addParent(parent);
    } else {
      urlInfoMap.put(link, new UrlInfo(link, parent));
      logger.debug("New URL found:{}", link);
      blockingQueue.add(new UrlFoundEvent(this, link));
    }
  }

  private List<UrlInfo> getUrlInfoToProcessed() {
    List<UrlInfo> urlInfoCollection = new ArrayList<>();
    for (Entry<URL, UrlInfo> entry : urlInfoMap.entrySet()) {
      UrlInfo urlInfo = entry.getValue();
      if (!urlInfo.isProcessed() && host.equals(urlInfo.getUrl().getHost())) {
        urlInfoCollection.add(urlInfo);
      }
    }
    logger.debug("Collection size to be processed:{}", urlInfoCollection.size());
    return urlInfoCollection;
  }

  public BlockingQueue<Event> getBlockingQueue() {
    return blockingQueue;
  }
}
