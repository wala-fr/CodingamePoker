package com.codingame.model.object;

import java.util.ArrayList;
import java.util.List;
import com.codingame.model.object.enumeration.Rank;
import com.codingame.model.object.enumeration.Suit;

public class Constant {
  
  public static final List<Card> ALL_CARDS = new ArrayList<>();
  
  static {
    for (Rank rank : Rank.values()) {
      for (Suit suit : Suit.values()) {
        Card card = new Card(rank ,suit);
        ALL_CARDS.add(card);
      }
    }
  }
}
