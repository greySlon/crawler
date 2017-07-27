package com.odessaflat.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataTag {

  private final Logger logger = LogManager.getLogger();
  private Pattern removeSpacePattern = Pattern.compile("[\\s]*=[\\s]*");
  private Map<String, Pattern> patternMap = new HashMap<>();

  public Optional<String> getAttribute(String headTag, String attribute) {
    logger.traceEntry("Head tag:{}, attribute:{}", headTag, attribute);
    headTag = removeSpacePattern.matcher(headTag).replaceAll("=");

    if (headTag.contains(attribute)) {
      String attributeData = null;
      Matcher matcher = getPattern(attribute).matcher(headTag);
      if (matcher.find()) {
        attributeData = matcher.group().trim();
      }
      logger.traceExit("AttributeData:{}", attributeData);
      return Optional.ofNullable(attributeData);
    } else {
      logger.traceExit("AttributeData:empty");
      return Optional.empty();
    }
  }

  private Pattern getPattern(String attributeName) {
    if (patternMap.containsKey(attributeName)) {
      return patternMap.get(attributeName);
    } else {
      Pattern pattern = Pattern.compile("(?<=" + attributeName + "=\")[^\"]+");
      patternMap.put(attributeName, pattern);
      return pattern;
    }
  }
}
