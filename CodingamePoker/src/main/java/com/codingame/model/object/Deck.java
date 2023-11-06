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
  
  public void cheat() {
    // FOR DEMO :)
    int i = 0;
    cards.set(i++, CardUtils.fromString("KD"));
    cards.set(i++, CardUtils.fromString("QH"));
    cards.set(i++, CardUtils.fromString("AS"));
    cards.set(i++, CardUtils.fromString("JC"));
    
    cards.set(i++, CardUtils.fromString("KS"));
    cards.set(i++, CardUtils.fromString("QS"));
    cards.set(i++, CardUtils.fromString("AH"));
    cards.set(i++, CardUtils.fromString("JH"));

    cards.set(i++, CardUtils.fromString("2H"));
    
    cards.set(i++, CardUtils.fromString("AC"));
    cards.set(i++, CardUtils.fromString("AD"));
    cards.set(i++, CardUtils.fromString("KC"));

    cards.set(i++, CardUtils.fromString("2C"));

    cards.set(i++, CardUtils.fromString("QC"));

    cards.set(i++, CardUtils.fromString("2S"));

    cards.set(i++, CardUtils.fromString("JS"));

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
