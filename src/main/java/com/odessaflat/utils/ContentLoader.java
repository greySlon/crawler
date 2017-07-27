package com.odessaflat.utils;

import com.odessaflat.domain.UrlInfo;
import com.odessaflat.exceptions.ContentLoaderException;
import com.odessaflat.exceptions.NotHtmlTypeException;
import com.odessaflat.exceptions.StatusNotOkException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Component
public class ContentLoader {

  private final Logger logger = LogManager.getLogger();

  public String load(UrlInfo urlInfo) throws ContentLoaderException {
    logger.traceEntry("Loading url:{}", urlInfo.getUrl());
    try {
      TrustManager[] trustAllCerts = new TrustManager[]{
          new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                String authType) {
            }
          }
      };

      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      HttpURLConnection conn = ((HttpURLConnection) urlInfo.getUrl().openConnection());

      int responseCode = conn.getResponseCode();
      urlInfo.setHttpStatusCode(responseCode);
      if (responseCode != 200) {
        throw new StatusNotOkException();
      }

      if (!checkContentType(conn, "html")) {
        throw new NotHtmlTypeException();
      }
      Charset charset = getCharset(conn);

      return getContent(conn.getInputStream(), charset);
    } catch (Exception ex) {
      logger.throwing(Level.INFO, ex);
      throw new ContentLoaderException("Error while loading" + urlInfo.getUrl(), ex);
    }
  }

  private String getContent(InputStream inputStream, Charset charset) throws IOException {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream, charset))) {
      String content = reader.lines()
          .collect(Collectors.joining());
      logger.traceExit("Content loaded:{} bytes", content.length());
      return content;
    }
  }

  private boolean checkContentType(HttpURLConnection conn, String type) {
    if (conn != null) {
      String contentType = conn.getContentType();
      if (contentType != null) {
        return contentType.contains(type);
      }
    }
    return false;
  }

  private Charset getCharset(HttpURLConnection conn) {
    if (conn != null) {
      String contentType = conn.getContentType();
      if (contentType != null && contentType.contains("1251")) {
        return Charset.forName("cp1251");
      }
    }
    return Charset.forName("utf-8");
  }

  public static void main(String[] args) throws Exception {
    ContentLoader loader = new ContentLoader();
    UrlInfo tuple = new UrlInfo(
        new URL("http://letsgoodessa.com/apartments_studio.html/&amp;show_all=yes"));
    int length = loader.load(tuple).length();
    System.out.println(length);
    tuple = new UrlInfo(
        new URL("http://letsgoodessa.com/apartments_studio.html/&show_all=yes"));
    length = loader.load(tuple).length();
    System.out.println(length);
  }
}
