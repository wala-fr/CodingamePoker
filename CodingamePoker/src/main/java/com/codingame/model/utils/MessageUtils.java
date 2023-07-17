package com.codingame.model.utils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageUtils {

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("message");

  public static String format(String key, Object... params) {
    try {
      return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
    } catch (MissingResourceException e) {
      throw new IllegalStateException(e);
    }
  }
}
