package com.odessaflat.crawler;

import static com.odessaflat.events.EventType.STOPPED;

import com.odessaflat.domain.UrlInfo;
import com.odessaflat.events.CrawlerStopedEvent;
import com.odessaflat.events.Event;
import com.odessaflat.events.UrlFoundEvent;
import com.odessaflat.events.UrlProcessedEvent;
import com.odessaflat.exceptions.ContentLoaderException;
import com.odessaflat.filters.Filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
  private HashMap<String, UrlInfo> urlInfoMap;
  private String host;
  private Filter filter;
  @Autowired
  private LinkExtractor linkExtractor;

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public Crawler() {
    urlInfoMap = new HashMap<>();
    blockingQueue = new ArrayBlockingQueue<Event>(200);
  }

  public void startCrawl(URL url) {
    logger.traceEntry("start URL{}", url);
    host = url.getHost();
    storeLinkData(url, url);
//    urlInfoMap.put(url.toString(), new UrlInfo(url));

    while (true) {
      List<UrlInfo> urlInfoCollection = getUrlInfoToProcessed();
      if (urlInfoCollection.isEmpty()) {
        sendMessage(new CrawlerStopedEvent(this));
        logger.debug("CrawlerStopedEvent has published");
        break;
      }
      for (UrlInfo urlInfo : urlInfoCollection) {
        try {
          URL itemUrl = urlInfo.getUrl();
          linkExtractor.getUrls(itemUrl, urlInfo.getContent())
              .forEach(link -> storeLinkData(link, itemUrl));
          urlInfo.setProcessed(true);
          sendMessage(new UrlProcessedEvent(this, urlInfo));
          logger.debug("URL processed:{}", itemUrl);
        } catch (ContentLoaderException ex) {
          logger.catching(Level.INFO, ex);
        }
      }
    }
  }

  private void sendMessage(Event event) {
    if (event.getEventType() == STOPPED/* || filter.test(event)*/) {
      blockingQueue.add(event);
    }
    if (filter != null) {
      if (filter.test(event)) {
        blockingQueue.add(event);
      }
    } else {
      blockingQueue.add(event);
    }
  }

  private void storeLinkData(URL link, URL parent) {
    String key = link.toString().toLowerCase();
    if (urlInfoMap.containsKey(key)) {
      UrlInfo urlInfo = urlInfoMap.get(key);
      urlInfo.addParent(parent);
      sendMessage(new UrlFoundEvent(this, urlInfo));
    } else {
      UrlInfo urlInfo = new UrlInfo(link, parent);
      urlInfoMap.put(key, urlInfo);
      logger.debug("New URL found:{}", key);
      sendMessage(new UrlFoundEvent(this, urlInfo));
    }
  }

  private List<UrlInfo> getUrlInfoToProcessed() {
    List<UrlInfo> urlInfoCollection = new ArrayList<>();
    for (Entry<String, UrlInfo> entry : urlInfoMap.entrySet()) {
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
