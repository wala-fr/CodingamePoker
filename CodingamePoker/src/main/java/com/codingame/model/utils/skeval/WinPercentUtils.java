package com.codingame.model.utils.skeval;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.RefereeParameter;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.CardUtils;
import com.codingame.model.utils.cactus.WinPercentKevUtils;

public class WinPercentUtils {

  private static final Logger logger = LoggerFactory.getLogger(WinPercentUtils.class);

  private static final int[][] playerCards = new int[4][];

  private static final int[] boardCards = new int[7];
  private static long usedCards;
  private static long totalTime;
  private static final double[] winPercents = new double[4];
  private static final boolean[] folded = new boolean[4];
  private static final int[] winBoardNb = new int[4];
  private static int totalBoardNb;

  public static void proceed(Board board, int turn) {
    logger.error("{} {}", turn, !board.isCalculateWinChance());
    if (RefereeParameter.CALCULATE_WIN_PERCENT && !board.isCalculateWinChance() && !board.isOver()) {
      // recalculate only when card are dealt or players fold
      long timeStart = System.currentTimeMillis();
      init(board);
      board.setCalculateWinChance(true);
      calculateWinPercents(board);
      long timeEnd = System.currentTimeMillis();
      long deltaTime = timeEnd - timeStart;
      totalTime += deltaTime;
      logger.error("{} {}", Arrays.toString(winBoardNb), totalBoardNb);
      logger.info("calculatePercent SK time {} ms total {} ms", deltaTime, totalTime);
      
      board.setCalculateWinChance(false);
      WinPercentKevUtils.proceed(board);
      for (int i = 0; i < 4; i++) {
        AssertUtils.test(winBoardNb[i] == WinPercentKevUtils.winBoardNb[i], i, winBoardNb[i], WinPercentKevUtils.winBoardNb[i]);
        AssertUtils.test(getWinPercent(i) == WinPercentKevUtils.getWinPercent(i));

      }
      AssertUtils.test(totalBoardNb == WinPercentKevUtils.totalBoardNb);
    }
  }

  private static void init(Board board) {
    if (playerCards[0] == null) {
      for (int i = 0; i < board.getPlayerNb(); i++) {
        playerCards[i] = new int[2];
      }
    }
    usedCards = 0;
    totalBoardNb = 0;
    int notFoldedNb = 0;
    for (int i = 0; i < board.getPlayerNb(); i++) {
      winBoardNb[i] = 0;
      PlayerModel player = board.getPlayer(i);
      folded[i] = player.isFolded();
      winPercents[i] = 0;
      if (!player.isFolded()) {
        notFoldedNb++;
        for (int cardIndex = 0; cardIndex < 2; cardIndex++) {
          Card card = player.getHand().getCard(cardIndex);
          int tmp = CardUtils.calculateCardSK(card);
          playerCards[i][cardIndex] = tmp;
          usedCards = usedCards | (1l << tmp);
        }
      }
    }
    AssertUtils.test(notFoldedNb > 1);
    int cardNb = 0;
    for (Card card : board.getBoardCards()) {
      int tmp = CardUtils.calculateCardSK(card);
      boardCards[cardNb++] = tmp;
      usedCards = usedCards | (1l << tmp);
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
//    logger.error(Arrays.toString(winBoardNb));
    for (int i = 0; i < board.getPlayerNb(); i++) {
      winPercents[i] = 100.0 * winBoardNb[i] / totalBoardNb;
    }
//    logger.error("{} {}", Arrays.toString(winBoardNb), totalBoardNb);
//    logger.error(Arrays.toString(winPercents));
  }

  private static void calculatePreFlopWinPercents(Board board) {
    for (int i0 = 0; i0 < 52; i0++) {
      if ((usedCards & (1l << i0)) == 0) {
        boardCards[0] = i0;
        for (int i1 = i0 + 1; i1 < 52; i1++) {
          if ((usedCards & (1l << i1)) == 0) {
            boardCards[1] = i1;
            for (int i2 = i1 + 1; i2 < 52; i2++) {
              if ((usedCards & (1l << i2)) == 0) {
                boardCards[2] = i2;
                for (int i3 = i2 + 1; i3 < 52; i3++) {
                  if ((usedCards & (1l << i3)) == 0) {
                    boardCards[3] = i3;
                    for (int i4 = i3 + 1; i4 < 52; i4++) {
                      if ((usedCards & (1l << i4)) == 0) {
                        boardCards[4] = i4;
                        int winId = calculateWinner(boardCards, board);
                        totalBoardNb++;
                        if (winId >= 0) {
                          winBoardNb[winId]++;
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private static void calculateFlopWinPercents(Board board) {
    for (int i3 = 0; i3 < 52; i3++) {
      if ((usedCards & (1l << i3)) == 0) {
        boardCards[3] = i3;
        for (int i4 = i3 + 1; i4 < 52; i4++) {
          if ((usedCards & (1l << i4)) == 0) {
            boardCards[4] = i4;
            int winId = calculateWinner(boardCards, board);
            totalBoardNb++;
            if (winId >= 0) {
              winBoardNb[winId]++;
            }
          }
        }
      }
    }
  }

  private static void calculateTurnWinPercents(Board board) {
    for (int i4 = 0; i4 < 52; i4++) {
      if ((usedCards & (1l << i4)) == 0) {
        boardCards[4] = i4;
        int winId = calculateWinner(boardCards, board);
        totalBoardNb++;
        if (winId >= 0) {
          winBoardNb[winId]++;
        }
      }
    }
  }

  private static void calculateRiverWinPercents(Board board) {
    int winId = calculateWinner(boardCards, board);
    totalBoardNb++;
    if (winId >= 0) {
      winBoardNb[winId]++;
    }
  }

  private static int calculateWinner(int[] boardCards, Board board) {
    int playerNb = board.getPlayerNb();
    int winnerId = -1;
    int winNb = 0;
    int bestScore = Integer.MIN_VALUE;
    for (int i = 0; i < playerNb; i++) {
      if (!folded[i]) {
        boardCards[5] = playerCards[i][0];
        boardCards[6] = playerCards[i][1];
        int score = SKPokerEvalUtils.calculateBestPossibleScore(boardCards);

        if (score > bestScore) {
          winnerId = i;
          bestScore = score;
          winNb = 1;
        } else if (score == bestScore) {
          winNb++;
        }
      }
    }
    return winNb == 1 ? winnerId : -2;
  }

  public static long getTotalTime() {
    return totalTime;
  }

  public static double getWinPercent(int id) {
    return winPercents[id];
  }
  
  

}
