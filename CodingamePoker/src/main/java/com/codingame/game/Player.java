package com.codingame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Card;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.CardUtils;

public class Player extends AbstractMultiplayerPlayer {
  
  private static final Logger logger = LoggerFactory.getLogger(Player.class);

  private String message = "";

  @Override
  public int getExpectedOutputLines() {
    return 1;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

   public void sendInputLine2(String line) {
   logger.debug(line);
   sendInputLine(line);
   }

  public void sendInputLine(int line) {
    sendInputLine2(Integer.toString(line));
  }
  
  public void sendInputLine(Card line) {
    sendInputLine2(line.toString());
  }
  
  public void sendBoardCards(List<Card> cards) {
    List<Card> tmp = new ArrayList<>(cards);
    while (tmp.size() < 5) {
      tmp.add(CardUtils.BURNED);
    }
    sendInputLine(tmp);
  }
  
  public void sendInputLine(List<Card> cards) {
//    if (line.isEmpty()) {
//      sendInputLine(Constant.NONE);
//    }
    AssertUtils.test(!cards.isEmpty());
    sendInputLine(cards.stream());
  }
  
  public void sendInputLine(Card[] cards) {
    for (Card card : cards) {
      AssertUtils.test(card != null);
    }
    sendInputLine(Arrays.stream(cards));
  }
  
  public void sendInputLine(Stream<Card> cards) {
    sendInputLine2(cards.map(c -> c.toString()).collect(Collectors.joining(RefereeParameter.WORD_DELIMITER)));
  }
  
  
  public void sendInputLine(ActionInfo action) {
    if (action == null) {
      sendInputLine(-1);
      sendInputLine(RefereeParameter.NONE);
    } else {
      sendInputLine(action.getPlayerId());
      sendInputLine(action.getAction());
    }
  }
  
  public void sendInputLine(Action action) {
    sendInputLine2(action.toInputString());
  }
}
