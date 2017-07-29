package com.odessaflat.domain;

import com.odessaflat.exceptions.ContentLoaderException;
import com.odessaflat.utils.ContentLoader;

import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UrlInfo {

  private URL url;
  private HashSet<URL> parents;
  private boolean processed;
  private int httpStatusCode;
  private ContentLoader loader = new ContentLoader();

  public UrlInfo(URL url) {
    this(url, url);
  }

  public UrlInfo(URL url, URL parent) {
    this.parents = new HashSet<>();
    this.url = url;
    if (parent != null) {
      this.parents.add(parent);
    }
  }

  public boolean isOuterLink() {
    Optional<URL> parent = parents
        .stream()
        .findAny();
    if (parent.isPresent()) {
      return !parent.get().getHost().equals(url.getHost());
    }
    return false;
  }

  public Set<URL> getParents() {
    return parents;
  }

  public void addParent(URL parent) {
    parents.add(parent);
  }

  public String getContent() throws ContentLoaderException {
    return loader.load(this);
  }

  public boolean isProcessed() {
    return processed;
  }

  public void setProcessed(boolean processed) {
    this.processed = processed;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }


  public void setHttpStatusCode(int httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

  @Override
  public int hashCode() {
    return url.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    UrlInfo another = (UrlInfo) obj;
    return url.equals(another.getUrl());
  }

  @Override
  public String toString() {
    return "UrlInfo{"
        + "url=" + url
        + ", parents=" + parents
        + ", processed=" + processed
        + ", httpStatusCode=" + httpStatusCode
        + '}';
  }
}
