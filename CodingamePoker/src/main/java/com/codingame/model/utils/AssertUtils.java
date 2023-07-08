package com.codingame.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.variable.Parameter;

public class AssertUtils {

  private static final Logger logger = LoggerFactory.getLogger(AssertUtils.class);

  public static void test(boolean b, Object... o) {
    if (Parameter.ACTIVATE_ASSERTION) {
      if (!b) {
        String str = toString(o);
        logger.error(str);
        throw new IllegalStateException(str);
      }
    }
  }
  
  private static String toString(Object... p) {
    StringBuilder str = new StringBuilder();
    int i = 0;
    for (Object o : p) {
      str.append(o == null ? "null" : o.toString());
      if (++i < p.length) {
        str.append(' ');
      }
    }
    return str.toString();
  }
}
