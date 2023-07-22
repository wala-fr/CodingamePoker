package com.codingame.model.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.codingame.model.utils.CardUtils;
import com.codingame.model.utils.RandomUtils;

public class Deck {

  private List<Card> cards = new ArrayList<Card>();
  
  private int index;

  public Deck() {}

  public void reset() {
    cards.clear();
    cards.addAll(CardUtils.ALL_CARDS);
    index = 0;
  }

  public void shuffle() {
    Collections.shuffle(cards, RandomUtils.RANDOM);
  }

  public Card dealCard() {
    return cards.get(index++);
  }

  public void cut() {
    int random = RandomUtils.nextInt(cards.size());
    for (int i = 0; i < random; i++) {
      cards.add(cards.remove(0));
    }
  }

  // for JUnit tests
  public void initCards(List<Card> cards) {
    this.cards.clear();
    this.cards.addAll(cards);
  }

  public List<Card> getCards() {
    return cards;
  }
  
  public Card getCard(int index) {
    return cards.get(index);
  }

  @Override
  public String toString() {
    return "Deck [cards=" + cards + "]";
  }



}
