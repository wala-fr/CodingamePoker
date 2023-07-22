package com.codingame.model.utils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageUtils {

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("message");

  public static String format(String key, Object... params) {
    try {
      String str = RESOURCE_BUNDLE.getString(key);
      AssertUtils.test(str.replaceAll("\\D", "").length() == params.length, key);
      return MessageFormat.format(str, params);
    } catch (MissingResourceException e) {
      throw new IllegalStateException(e);
    }
  }
}
