package com.codingame.model.utils.cactus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Card;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.utils.CardUtils;

public class CactusKevUtils {

  private static final Logger logger = LoggerFactory.getLogger(CactusKevEvaluator.class);

  private static final CactusKevEvaluator evaluator = CactusKevEvaluator.getInstance();

  private static final int[] tmpHand = new int[5];

  private static final int[] CACTUS_KEV_VALUES = new int[52];

  static {
    for (int rank = 0; rank < 13; rank++) {
      for (int suit = 0; suit < 4; suit++) {
        int card = CardUtils.calculateCardKev(rank, suit);
        int value = CactusKevRepresentationUtils.calculateCactusKevCardRepresentation(rank, suit);
        CACTUS_KEV_VALUES[card] = value;
      }
    }
  }
  // public static int calculateCactusKevCardRepresentation(Rank rank, Suit suit) {
  // return CactusKevRepresentationUtils.calculateCactusKevCardRepresentation(rank.getIndex() - 2,
  // suit.ordinal());
  // }

  public static int getHandRank(FiveCardHand hand) {
    for (int i = 0; i < tmpHand.length; i++) {
      tmpHand[i] = calculateCactusKevCardRepresentation(hand.getCard(i));
    }
    int ret = calculateScore(tmpHand);
    // logger.debug("{} {}", hand, ret);
    return ret;
  }

  public static int calculateCactusKevCardRepresentation(Card card) {
    return calculateCactusKevCardRepresentation(CardUtils.calculateCardKev(card));
  }

  public static int calculateCactusKevCardRepresentation(int card) {
    return CACTUS_KEV_VALUES[card];
  }

  public static int calculateScore(int[] hand) {
    return -evaluator.calculateHandRank(hand); // 1 is the best
  }

  private static final int[][] perm7 = {{0, 1, 2, 3, 4}, {0, 1, 2, 3, 5}, {0, 1, 2, 3, 6},
      {0, 1, 2, 4, 5}, {0, 1, 2, 4, 6}, {0, 1, 2, 5, 6}, {0, 1, 3, 4, 5}, {0, 1, 3, 4, 6},
      {0, 1, 3, 5, 6}, {0, 1, 4, 5, 6}, {0, 2, 3, 4, 5}, {0, 2, 3, 4, 6}, {0, 2, 3, 5, 6},
      {0, 2, 4, 5, 6}, {0, 3, 4, 5, 6}, {1, 2, 3, 4, 5}, {1, 2, 3, 4, 6}, {1, 2, 3, 5, 6},
      {1, 2, 4, 5, 6}, {1, 3, 4, 5, 6}, {2, 3, 4, 5, 6}};

  public static int calculateBestPossibleScore(int[] cards) {
    int bestScore = Integer.MIN_VALUE;
    for (int i = 0; i < 21; i++) {
      for (int j = 0; j < 5; j++) {
        tmpHand[j] = cards[perm7[i][j]];
      }
      int score = calculateScore(tmpHand);
      if (score > bestScore) {
        bestScore = score;
      }
    }
    return bestScore;
  }

  public static int calculateBestPossibleScore(int[] commonCards, int[] hand, int bestScore) {
    // use only one player's card
    for (int playerCardIdx = 0; playerCardIdx < 2; playerCardIdx++) {
      tmpHand[0] = hand[playerCardIdx];
      for (int commonCardIdx = 0; commonCardIdx < 5; commonCardIdx++) {
        int index = 1;
        for (int i = 0; i < 5; i++) {
          if (i != commonCardIdx) {
            tmpHand[index++] = commonCards[i];
          }
        }
        int score = calculateScore(tmpHand);
        if (score > bestScore) {
          bestScore = score;
        }
      }
    }
    // use two player's card
    tmpHand[0] = hand[0];
    tmpHand[1] = hand[1];
    for (int commonCardIdx1 = 0; commonCardIdx1 < 5; commonCardIdx1++) {
      for (int commonCardIdx2 = commonCardIdx1 + 1; commonCardIdx2 < 5; commonCardIdx2++) {
        int index = 2;
        for (int i = 0; i < 5; i++) {
          if (i != commonCardIdx1 && i != commonCardIdx2) {
            tmpHand[index++] = commonCards[i];
          }
        }
        int score = calculateScore(tmpHand);
        if (score > bestScore) {
          bestScore = score;
        }
      }
    }
    return bestScore;
  }
  
}
