package com.fredgar.pe.business.components;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class ErrorPropertiesBuilder {
  private final HttpServletRequest request;
  private final String serverId;

  public ErrorPropertiesBuilder(HttpServletRequest request, @Value("${server.id}") String serverId) {
    this.request = request;
    this.serverId = serverId;
  }


  public Map<String, Object> buildErrorProperties() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("timestamp", LocalDateTime.now().toString());
    properties.put("serverId", serverId);
    properties.put("requestId", UUID.randomUUID().toString());
    properties.put("path", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
    properties.put("method", request.getMethod());

    return properties;
  }
}
