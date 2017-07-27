package com.odessaflat;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.odessaflat")
public class Config {

  @Bean
  public PropertyPlaceholderConfigurer getPPC() {
    PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
    propertyPlaceholderConfigurer
        .setLocation(new ClassPathResource("application.properties"));
    return propertyPlaceholderConfigurer;
  }
}
