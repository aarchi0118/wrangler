package io.cdap.wrangler.api;

import java.util.Map;

/**
 * Dummy Arguments implementation for testing purposes.
 */
public class Arguments {
  private final Map<String, Object> tokens;

  public Arguments(Map<String, Object> tokens) {
    this.tokens = tokens;
  }

  public Object value(String key) {
    return tokens.get(key);
  }
}
