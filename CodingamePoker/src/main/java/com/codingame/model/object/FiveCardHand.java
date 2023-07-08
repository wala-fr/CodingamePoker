package com.codingame.model.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codingame.model.object.enumeration.HandType;
import com.codingame.model.object.enumeration.Rank;
import com.codingame.model.object.enumeration.Suit;

public class FiveCardHand {

  private List<Card> cards;
  private HandType handType;
  private int value;
  private Map<Rank, Integer> identicals = new HashMap<>();
  private static final int[] POWS = new int[6];
  static {
    int pow = 1;
    for (int i = 0; i < POWS.length; i++) {
      POWS[i] = pow;
      pow *= 20;
    }
  }

  public FiveCardHand(List<Card> cards) {
    if (cards.size() != 5) {
      throw new IllegalStateException("hand without 5 cards " + cards);
    }
    this.cards = new ArrayList<>();
    this.cards.addAll(cards);
    evaluate();
  }

  private void evaluate() {
    Collections.sort(this.cards);
    calculateIdenticalNb();
    if (isStraightFlush()) {
      handType = HandType.STRAIGHT_FLUSH;
    } else if (isFourOfAKind()) {
      handType = HandType.FOUR_OF_A_KIND;
    } else if (isFullHouse()) {
      handType = HandType.FULL_HOUSE;
    } else if (isFlush()) {
      handType = HandType.FLUSH;
    } else if (isStraight()) {
      handType = HandType.STRAIGHT;
    } else if (isThreeOfAKind()) {
      handType = HandType.THREE_OF_A_KIND;
    } else if (isTwoPair()) {
      handType = HandType.TWO_PAIR;
    } else if (isPair()) {
      handType = HandType.PAIR;
    } else {
      handType = HandType.HIGH_CARD;
    }
    if (handType == HandType.STRAIGHT_FLUSH || handType == HandType.STRAIGHT) {
      if (cards.get(4).getRank() == Rank.ACE && cards.get(3).getRank() == Rank.FIVE) {
        cards.add(0, cards.remove(cards.size() - 1));
      }
    } else {
      cards.sort(Comparator.<Card>comparingInt(c -> identicals.getOrDefault(c.getRank(), 1))
        .thenComparingInt(c -> c.getRank().getIndex()));
    }
    value = 0;
    for (int i = 0; i < cards.size(); i++) {
      value += POWS[i] * cards.get(i).getRank().getIndex();
    }
    value += POWS[5] * handType.ordinal();
  }

  private boolean isStraightFlush() {
    return isFlush() && isStraight();
  }

  private boolean isFourOfAKind() {
    return isSomeOfAKind(4);
  }

  private boolean isThreeOfAKind() {
    return isSomeOfAKind(3);
  }

  private boolean isPair() {
    return isSomeOfAKind(2);
  }

  private boolean isTwoPair() {
    return identicals.size() == 2
        && identicals.entrySet().stream().allMatch(e -> e.getValue() == 2);
  }

  private boolean isFullHouse() {
    return identicals.size() == 2
        && identicals.entrySet().stream().anyMatch(e -> e.getValue() == 3);
  }

  private boolean isSomeOfAKind(int n) {
    return identicals.size() == 1
        && identicals.entrySet().stream().anyMatch(e -> e.getValue() == n);
  }

  private void calculateIdenticalNb() {
    Rank lastIndex = cards.get(0).getRank();
    int nb = 1;
    for (int i = 1; i < cards.size(); i++) {
      Rank nextIndex = cards.get(i).getRank();
      if (nextIndex == lastIndex) {
        nb++;
      } else {
        if (nb > 1) {
          identicals.put(lastIndex, nb);
        }
        nb = 1;
      }
      lastIndex = nextIndex;
    }
    if (nb > 1) {
      identicals.put(lastIndex, nb);
    }
  }

  private boolean isFlush() {
    Suit suit = cards.get(0).getSuit();
    for (int i = 1; i < cards.size(); i++) {
      if (suit != cards.get(i).getSuit()) {
        return false;
      }
    }
    return true;
  }

  private boolean isStraight() {
    int lastIndex = cards.get(0).getRank().getIndex();
    for (int i = 1; i < cards.size(); i++) {
      int nextIndex = cards.get(i).getRank().getIndex();
      if (nextIndex != lastIndex + 1) {
        if (i == 4 && lastIndex == Rank.FIVE.getIndex() && nextIndex == Rank.ACE.getIndex()) {
          return true;
        }
        return false;
      }
      lastIndex = nextIndex;
    }
    return true;
  }

  public HandType getHandType() {
    return handType;
  }



  public int getValue() {
    return value;
  }

  public List<Card> getCards() {
    return cards;
  }

  @Override
  public String toString() {
    return "FullHand [cards=" + cards + ", handType=" + handType + ", value=" + value
        + ", identicals=" + identicals + "]";
  }


}
