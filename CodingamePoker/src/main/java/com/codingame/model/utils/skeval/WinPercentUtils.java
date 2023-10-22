package com.codingame.model.utils.skeval;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.RefereeParameter;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.CardUtils;

public class WinPercentUtils {

  private static final Logger logger = LoggerFactory.getLogger(WinPercentUtils.class);

  private static final int[][] playerCards = new int[4][];

  private static final int[] boardCards = new int[7];
  private static final boolean[] usedCards = new boolean[52];
  private static long totalTime;
  private static final double[] winPercents = new double[4];
  private static final double[] splitPercents = new double[4];

  private static final boolean[] folded = new boolean[4];
  private static final int[] winBoardNb = new int[4];
  private static final int[] splitBoardNb = new int[4];

  private static int totalBoardNb;
  private static int splitTotalBoardNb;
  private static double splitPercent;
  private static int notFoldedNb;

  public static void proceed(Board board, int turn) {
    logger.error("{} {}", turn, !board.isCalculateWinChance());
    if (RefereeParameter.CALCULATE_WIN_PERCENT && !board.isCalculateWinChance()
        && !board.isOver()) {
      // recalculate only when card are dealt or players fold
      long timeStart = System.currentTimeMillis();
      init(board);
      board.setCalculateWinChance(true);
      calculateWinPercents(board);
      long timeEnd = System.currentTimeMillis();
      long deltaTime = timeEnd - timeStart;
      totalTime += deltaTime;
      logger.info("{} {}", Arrays.toString(winBoardNb), totalBoardNb);
      logger.info("calculatePercent SK time {} ms total {} ms", deltaTime, totalTime);
    }
  }

  private static void init(Board board) {
    if (playerCards[0] == null) {
      for (int i = 0; i < board.getPlayerNb(); i++) {
        playerCards[i] = new int[2];
      }
    }
    for (int i = 0; i < usedCards.length; i++) {
      usedCards[i] = false;
    }
    totalBoardNb = 0;
    splitTotalBoardNb = 0;
    notFoldedNb = 0;
    for (int i = 0; i < board.getPlayerNb(); i++) {
      winBoardNb[i] = 0;
      splitBoardNb[i] = 0;

      PlayerModel player = board.getPlayer(i);
      folded[i] = player.isFolded();
      winPercents[i] = 0;
      if (!player.isFolded()) {
        notFoldedNb++;
        for (int cardIndex = 0; cardIndex < 2; cardIndex++) {
          Card card = player.getHand().getCard(cardIndex);
          int tmp = CardUtils.calculateCardSK(card);
          playerCards[i][cardIndex] = tmp;
          usedCards[tmp] = true;
        }
      }
    }
    int cardNb = 0;
    for (Card card : board.getBoardCards()) {
      int tmp = CardUtils.calculateCardSK(card);
      boardCards[cardNb++] = tmp;
      usedCards[tmp] = true;
    }
  }

  private static void calculateWinPercents(Board board) {
    if (notFoldedNb == 1) {
      for (int i = 0; i < board.getPlayerNb(); i++) {
        if (!folded[i]) {
          winBoardNb[i] = 1;
        }
      }
      totalBoardNb = 1;
    } else {
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
    }
 
    // logger.error(Arrays.toString(winBoardNb));
    for (int i = 0; i < board.getPlayerNb(); i++) {
      winPercents[i] = 100.0 * winBoardNb[i] / totalBoardNb;
      splitPercents[i] = 100.0 * splitBoardNb[i] / totalBoardNb;
    }
    splitPercent = 100.0 * splitTotalBoardNb / totalBoardNb;
    // logger.error("{} {}", Arrays.toString(winBoardNb), totalBoardNb);
    // logger.error(Arrays.toString(winPercents));
  }

  private static void calculatePreFlopWinPercents(Board board) {
    int playerNb = board.getPlayerNb();
    for (int i0 = 0; i0 < 52; i0++) {
      if (usedCards[i0]) {
        continue;
      }
      boardCards[0] = i0;
      for (int i1 = i0 + 1; i1 < 52; i1++) {
        if (usedCards[i1]) {
          continue;
        }
        boardCards[1] = i1;
        for (int i2 = i1 + 1; i2 < 52; i2++) {
          if (usedCards[i2]) {
            continue;
          }
          boardCards[2] = i2;
          for (int i3 = i2 + 1; i3 < 52; i3++) {
            if (usedCards[i3]) {
              continue;
            }
            boardCards[3] = i3;
            for (int i4 = i3 + 1; i4 < 52; i4++) {
              if (usedCards[i4]) {
                continue;
              }
              boardCards[4] = i4;
              calculateWinner(boardCards, playerNb);
            }
          }
        }
      }
    }
  }

  private static void calculateFlopWinPercents(Board board) {
    int playerNb = board.getPlayerNb();
    for (int i3 = 0; i3 < 52; i3++) {
      if (usedCards[i3]) {
        continue;
      }
      boardCards[3] = i3;
      for (int i4 = i3 + 1; i4 < 52; i4++) {
        if (usedCards[i4]) {
          continue;
        }
        boardCards[4] = i4;
        calculateWinner(boardCards, playerNb);
      }
    }
  }

  private static void calculateTurnWinPercents(Board board) {
    int playerNb = board.getPlayerNb();
    for (int i4 = 0; i4 < 52; i4++) {
      if (usedCards[i4]) {
        continue;
      }
      boardCards[4] = i4;
      calculateWinner(boardCards, playerNb);
    }
  }

  private static void calculateRiverWinPercents(Board board) {
    int playerNb = board.getPlayerNb();
    calculateWinner(boardCards, playerNb);
  }

  private static final int[] winnerIds = new int[4];

  private static void calculateWinner(int[] boardCards, int playerNb) {
    if (RefereeParameter.CALCULATE_SPLIT_PERCENT) {
      calculateWinnerSplit(boardCards, playerNb);
    } else {
      calculateWinnerNoSplit(boardCards, playerNb);
    }
  }

  private static void calculateWinnerSplit(int[] boardCards, int playerNb) {
    int winNb = 0;
    int bestScore = Integer.MIN_VALUE;
    for (int i = 0; i < playerNb; i++) {
      if (!folded[i]) {
        boardCards[5] = playerCards[i][0];
        boardCards[6] = playerCards[i][1];
        int score = SKPokerEvalUtils.calculateBestPossibleScore(boardCards);
        if (score > bestScore) {
          winnerIds[0] = i;
          bestScore = score;
          winNb = 1;
        } else if (score == bestScore) {
          winnerIds[winNb] = i;
          winNb++;
        }
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

  private static void calculateWinnerNoSplit(int[] boardCards, int playerNb) {
    int winNb = 0;
    int winId = -1;
    int bestScore = Integer.MIN_VALUE;
    for (int i = 0; i < playerNb; i++) {
      if (!folded[i]) {
        boardCards[5] = playerCards[i][0];
        boardCards[6] = playerCards[i][1];
        int score = SKPokerEvalUtils.calculateBestPossibleScore(boardCards);
        if (score > bestScore) {
          winId = i;
          bestScore = score;
          winNb = 1;
        } else if (score == bestScore) {
          winNb++;
        }
      }
    }
    totalBoardNb++;
    if (winNb == 1) {
      winBoardNb[winId]++;
    } else {
      splitTotalBoardNb++;
    }
  }
  
  public static boolean isSureWin(int id) {
    return winBoardNb[id] == totalBoardNb || splitBoardNb[id] == totalBoardNb;
  }
  
  public static boolean isSureLose(int id) {
    return winBoardNb[id] == 0 && splitBoardNb[id] == 0;
  }

  public static long getTotalTime() {
    return totalTime;
  }

  public static double getWinPercent(int id) {
    return winPercents[id];
  }

  public static double getSplitPercent(int id) {
    return splitPercents[id];
  }

  public static double getSplitPercent() {
    return splitPercent;
  }

}
