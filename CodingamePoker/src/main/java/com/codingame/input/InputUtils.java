package com.codingame.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.codingame.game.RefereeParameter;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Card;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.CardUtils;

public class InputUtils {

  public static String toInputLineBoardCards(List<Card> cards) {
    List<Card> tmp = new ArrayList<>(cards);
    while (tmp.size() < 5) {
      tmp.add(CardUtils.BURNED);
    }
   return toInputLine(tmp);
  }
  
  public static String toInputLine(List<Card> cards) {
    return toInputLine(cards.stream());
  }
  
  public static String toInputLine(Card[] cards) {
    for (Card card : cards) {
      AssertUtils.test(card != null);
    }
   return toInputLine(Arrays.stream(cards));
  }
  
  public static String toInputLine(Stream<Card> cards) {
    return cards.map(c -> c.toString()).collect(Collectors.joining(RefereeParameter.WORD_DELIMITER));
  }
  
  
  public static String toInputLine(ActionInfo action) {
    if (action == null) {
      return  String.format("%d %s", -1, RefereeParameter.NONE);
    } else {
      return  String.format("%d %s", action.getPlayerId(), action.getAction().toInputString());
    }
  }
  
}
