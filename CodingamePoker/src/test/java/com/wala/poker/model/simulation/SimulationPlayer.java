package com.wala.poker.model.simulation;

import java.util.ArrayList;
import java.util.List;
import com.codingame.model.object.Card;
import com.codingame.model.utils.CardUtils;

public class SimulationPlayer {

  private List<Card> cards;
  private int startStack;
  private int endStack;

  public void setCards(String s) {
    cards = CardUtils.calculateHandFromString(s);
  }

  public void setStartStack(String s) {
    startStack = Integer.parseInt(s);
  }

  public void setEndStack(String s) {
    endStack = Integer.parseInt(s);
  }

  public List<Card> getCards() {
    return cards;
  }

  public int getStartStack() {
    return startStack;
  }

  public int getEndStack() {
    return endStack;
  }

}
