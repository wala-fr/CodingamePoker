package com.codingame.model.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.codingame.model.object.Card;
import com.codingame.model.object.enumeration.Rank;
import com.codingame.model.object.enumeration.Suit;

public class CardUtils {

  public static final List<Card> ALL_CARDS = new ArrayList<>();

  private static final Map<String, Card> MAP_FROM_STRING = new HashMap<>();

  public static final Card BURNED = new Card(null, null);
  public static final String BURNED_STR = "X";

  public static final Card ELIMINATED = new Card(null, Suit.DIAMONDS);
  public static final String ELIMINATED_STR = "E";

  static {
    for (Rank rank : Rank.values()) {
      for (Suit suit : Suit.values()) {
        Card card = new Card(rank, suit);
        ALL_CARDS.add(card);
      }
    }
    for (Card card : ALL_CARDS) {
      MAP_FROM_STRING.put(card.toString(), card);
    }
    MAP_FROM_STRING.put(BURNED_STR, BURNED);
    MAP_FROM_STRING.put(ELIMINATED_STR, ELIMINATED);
  }

  public static Card fromString(String s) {
//    if (BURNED_STR.equals(s)) {
//      return BURNED;
//    }
//    if (ELIMINATED_STR.equals(s)) {
//      return ELIMINATED;
//    }
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
