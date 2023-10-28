package com.codingame.win_percent.naive_eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.RefereeParameter;
import com.codingame.model.object.Card;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.object.Hand;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.CardUtils;
import com.codingame.win_percent.skeval.SKPokerEvalUtils;
import com.codingame.win_percent.skeval.WinPercentUtils;

/**
 * only to test that there's no regression in the fast evaluation
 */
public class WinPercentNaiveUtils {

  private static final Logger logger = LoggerFactory.getLogger(WinPercentNaiveUtils.class);

  private static final List<Card> boardCards = new ArrayList<>(5);
  private static final Set<Card> usedCards = new HashSet<>(20);
  
  private static long totalTime;

  private static final int[] winBoardNb = new int[4];
  private static final int[] splitBoardNb = new int[4];

  private static int totalBoardNb;
  private static int splitTotalBoardNb;
  private static int notFoldedNb;
  
  private static final double[] winPercents = new double[4];
  private static final double[] splitPercents = new double[4];
  private static double splitPercent;

  public static void proceed(Board board) {
    if (RefereeParameter.ASSERT_WIN_PERCENT && !board.isOver()) {
      long timeStart = System.currentTimeMillis();
      init(board);
      calculateWinPercents(board);
      long timeEnd = System.currentTimeMillis();
      long deltaTime = timeEnd - timeStart;
      totalTime += deltaTime;
      logger.info("{} {}", Arrays.toString(winBoardNb), totalBoardNb);
      logger.error("calculatePercent naive time {} ms total {} ms", deltaTime, totalTime);
      
      if (notFoldedNb > 1) {
        AssertUtils.test(totalBoardNb == WinPercentUtils.getTotalBoardNb());
        AssertUtils.test(splitTotalBoardNb == WinPercentUtils.getSplitTotalBoardNb());

        for (int i = 0; i < 4; i++) {
          AssertUtils.test(winBoardNb[i] == WinPercentUtils.getWinBoardNb()[i]);
          AssertUtils.test(splitBoardNb[i] == WinPercentUtils.getSplitBoardNb()[i]);

        }
      }
      AssertUtils.test(splitPercent == WinPercentUtils.getSplitPercent());

      for (int i = 0; i < 4; i++) {
        AssertUtils.test(winPercents[i] == WinPercentUtils.getWinPercent(i));
        AssertUtils.test(splitPercents[i] == WinPercentUtils.getSplitPercent(i));

      }
 
    }
  }

  private static void init(Board board) {
    usedCards.clear();
    boardCards.clear();
  
    notFoldedNb = 0;
    totalBoardNb = 0;
    splitTotalBoardNb = 0;
    for (int i = 0; i < board.getPlayerNb(); i++) {
      winBoardNb[i] = 0;
      splitBoardNb[i] = 0;
      PlayerModel player = board.getPlayer(i);
      if (!player.isFolded()) {
        notFoldedNb++;
        for (int cardIndex = 0; cardIndex < 2; cardIndex++) {
          Card card = player.getHand().getCard(cardIndex);
          usedCards.add(card);
        }
      }
    }
    for (Card card : board.getBoardCards()) {
      boardCards.add(card);
      usedCards.add(card);
    }
    while (boardCards.size() < 5) {
      boardCards.add(null);
    }
  }

  private static void calculateWinPercents(Board board) {
    int cardNb = board.getBoardCards().size();
    if (cardNb == 0) {
      calculatePreFlopWinPercents(board);
    } else if (cardNb == 3) {
      calculateFlopWinPercents(board);
    } else if (cardNb == 4) {
      calculateTurnWinPercents(board);
    } else {
      calculateRiverWinPercents(board);
    }
    
    for (int i = 0; i < board.getPlayerNb(); i++) {
      winPercents[i] = 100.0 * winBoardNb[i] / totalBoardNb;
      splitPercents[i] = 100.0 * splitBoardNb[i] / totalBoardNb;
    }
    splitPercent = 100.0 * splitTotalBoardNb / totalBoardNb;
  }

  private static void calculatePreFlopWinPercents(Board board) {
    for (int i0 = 0; i0 < 52; i0++) {
      Card card0 = CardUtils.ALL_CARDS.get(i0);
      if (usedCards.contains(card0)) {
        continue;
      }
      boardCards.set(0, card0);
      for (int i1 = i0 + 1; i1 < 52; i1++) {
        Card card1 = CardUtils.ALL_CARDS.get(i1);
        if (usedCards.contains(card1)) {
          continue;
        }
        boardCards.set(1, card1);
        for (int i2 = i1 + 1; i2 < 52; i2++) {
          Card card2 = CardUtils.ALL_CARDS.get(i2);
          if (usedCards.contains(card2)) {
            continue;
          }
          boardCards.set(2, card2);
          for (int i3 = i2 + 1; i3 < 52; i3++) {
            Card card3 = CardUtils.ALL_CARDS.get(i3);
            if (usedCards.contains(card3)) {
              continue;
            }
            boardCards.set(3, card3);
            for (int i4 = i3 + 1; i4 < 52; i4++) {
              Card card4 = CardUtils.ALL_CARDS.get(i4);
              if (usedCards.contains(card4)) {
                continue;
              }
              boardCards.set(4, card4);
              calculateWinner(boardCards, board);
            }
          }
        }
      }
    }
  }

  private static void calculateFlopWinPercents(Board board) {
    int playerNb = board.getPlayerNb();
    for (int i3 = 0; i3 < 52; i3++) {
      Card card3 = CardUtils.ALL_CARDS.get(i3);
      if (usedCards.contains(card3)) {
        continue;
      }
      boardCards.set(3, card3);
      for (int i4 = i3 + 1; i4 < 52; i4++) {
        Card card4 = CardUtils.ALL_CARDS.get(i4);
        if (usedCards.contains(card4)) {
          continue;
        }
        boardCards.set(4, card4);
        calculateWinner(boardCards, board);
      }
    }
  }

  private static void calculateTurnWinPercents(Board board) {
    for (int i4 = 0; i4 < 52; i4++) {
      Card card4 = CardUtils.ALL_CARDS.get(i4);
      if (usedCards.contains(card4)) {
        continue;
      }
      boardCards.set(4, card4);
      calculateWinner(boardCards, board);
    }
  }

  private static void calculateRiverWinPercents(Board board) {
    calculateWinner(boardCards, board);
  }

  private static final int[] winnerIds = new int[4];

  private static void calculateWinner(List<Card> boardCards, Board board) {
    int winNb = 0;
    int bestScore = Integer.MIN_VALUE;
    for (int i = 0; i < board.getPlayerNb(); i++) {
      PlayerModel player = board.getPlayer(i);
      if (player.isFolded()) {
        continue;
      }
      Hand hand = player.getHand();
      FiveCardHand bestHand = hand.calculateBestFiveCardhand(boardCards);
      int score = bestHand.getValue();
      if (score > bestScore) {
        winnerIds[0] = i;
        bestScore = score;
        winNb = 1;
      } else if (score == bestScore) {
        winnerIds[winNb] = i;
        winNb++;
      }
    }
    totalBoardNb++;
    if (winNb == 1) {
      winBoardNb[winnerIds[0]]++;
    } else {
      splitTotalBoardNb++;
      for (int i = 0; i < winNb; i++) {
        splitBoardNb[winnerIds[i]]++;
      }
    }
  }


}
