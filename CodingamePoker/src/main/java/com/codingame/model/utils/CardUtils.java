package com.codingame.model.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.codingame.model.object.Card;
import com.codingame.model.object.Constant;

public class CardUtils {

  private static final Map<String, Card> MAP_FROM_STRING = new HashMap<>();
  
  // for JUnit test
  public static final Card BURNED = new Card(null, null);
  public static final String BURNED_STR = "X";

  static {
    for (Card card : Constant.ALL_CARDS) {
      MAP_FROM_STRING.put(card.toString(), card);
    }
  }

  public static Card fromString(String s) {
    if (BURNED_STR.equals(s)) {
      return BURNED;
    }
    Card ret = MAP_FROM_STRING.get(s);
    if (ret == null) {
      throw new IllegalStateException("no card for " + s);
    }
    return ret;
  }

  public static List<Card> calculateHandFromString(String str) {
    return Arrays.stream(str.split(" ")).map(s -> fromString(s)).collect(Collectors.toList());
  }
}
