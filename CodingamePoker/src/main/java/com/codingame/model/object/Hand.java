package com.codingame.model.object;

import java.util.ArrayList;
import java.util.List;

public class Hand {

  private Card[] hand = new Card[2];

  public void reset() {
    for (int i = 0; i < hand.length; i++) {
      hand[i] = null;
    }
  }

  public void deal(Card card) {
    hand[calculateCardIndex()] = card;
  }
  
  public int calculateCardIndex() {
    return hand[0] == null ? 0 : 1;
  }
  
  public FiveCardHand calculateBestFiveCardhand(List<Card> commonCards) {
    FiveCardHand best = new FiveCardHand(commonCards);
    int bestScore = best.getValue();
    List<Card> tmp = new ArrayList<>(5);

    // use only one player's card
    for (int playerCardIdx = 0; playerCardIdx < 2; playerCardIdx++) {
      for (int commonCardIdx = 0; commonCardIdx < 5; commonCardIdx++) {
        tmp.clear();
        tmp.add(hand[playerCardIdx]);
        for (int i = 0; i < 5; i++) {
          if (i != commonCardIdx) {
            tmp.add(commonCards.get(i));
          }
        }
        FiveCardHand tmpFiveCardHand = new FiveCardHand(tmp);
        int score = tmpFiveCardHand.getValue();
        if (score > bestScore) {
          bestScore = score;
          best = tmpFiveCardHand;
        }
      }
    }
    // use only two player's card
    for (int commonCardIdx1 = 0; commonCardIdx1 < 5; commonCardIdx1++) {
      for (int commonCardIdx2 = commonCardIdx1 + 1; commonCardIdx2 < 5; commonCardIdx2++) {
        tmp.clear();
        tmp.add(hand[0]);
        tmp.add(hand[1]);
        for (int i = 0; i < 5; i++) {
          if (i != commonCardIdx1 && i != commonCardIdx2) {
            tmp.add(commonCards.get(i));
          }
        }
        FiveCardHand tmpFiveCardHand = new FiveCardHand(tmp);
        int score = tmpFiveCardHand.getValue();
        if (score > bestScore) {
          bestScore = score;
          best = tmpFiveCardHand;
        }
      }
    }
    return best;
  }
  
  public Card[] getCards() {
    return hand;
  }

  public Card getCard(int idx) {
    return hand[idx];
  }
  
  @Override
  public String toString() {
    if (hand[0]==null) {
      return "[, ]";
    }
    return "[" + hand[0] + ", " + hand[1] + "]";
  }

}
