package com.codingame.model.utils.cactus;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.RefereeParameter;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.CardUtils;
import com.codingame.model.utils.cactus.CactusKevUtils;

public class WinPercentKevUtils {

  private static final Logger logger = LoggerFactory.getLogger(WinPercentKevUtils.class);

  private static final int[][] playerCards = new int[4][];

  private static final int[] boardCards = new int[7];
  private static long usedCards;
  private static long totalTime;
  private static final double[] winPercents = new double[4];
  private static final boolean[] folded = new boolean[4];
  public static final int[] winBoardNb = new int[4];
  public static int totalBoardNb;

  public static void proceed(Board board) {
    if (RefereeParameter.CALCULATE_WIN_PERCENT && !board.isCalculateWinChance() && !board.isOver()) {
      // recalculate only when card are dealt or players fold
      long timeStart = System.currentTimeMillis();
      init(board);
      board.setCalculateWinChance(true);
      calculateWinPercents(board);
      long timeEnd = System.currentTimeMillis();
      long deltaTime = timeEnd - timeStart;
      totalTime += deltaTime;
      logger.info("{} {}", Arrays.toString(winBoardNb), totalBoardNb);
      logger.error("calculatePercent time {} ms total {} ms", deltaTime, totalTime);
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
          playerCards[i][cardIndex] = CactusKevUtils.calculateCactusKevCardRepresentation(card);
          usedCards = usedCards | (1l << CardUtils.calculateCardKev(card));
        }
      }
    }
//    AssertUtils.test(notFoldedNb > 1, board.getHandNb(), notFoldedNb, board.isOver());
    int cardNb = 0;
    for (Card card : board.getBoardCards()) {
      boardCards[cardNb++] = CactusKevUtils.calculateCactusKevCardRepresentation(card);
      usedCards = usedCards | (1l << CardUtils.calculateCardKev(card));
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
        boardCards[0] = CactusKevUtils.calculateCactusKevCardRepresentation(i0);
        for (int i1 = i0 + 1; i1 < 52; i1++) {
          if ((usedCards & (1l << i1)) == 0) {
            boardCards[1] = CactusKevUtils.calculateCactusKevCardRepresentation(i1);
            for (int i2 = i1 + 1; i2 < 52; i2++) {
              if ((usedCards & (1l << i2)) == 0) {
                boardCards[2] = CactusKevUtils.calculateCactusKevCardRepresentation(i2);
                for (int i3 = i2 + 1; i3 < 52; i3++) {
                  if ((usedCards & (1l << i3)) == 0) {
                    boardCards[3] = CactusKevUtils.calculateCactusKevCardRepresentation(i3);
                    for (int i4 = i3 + 1; i4 < 52; i4++) {
                      if ((usedCards & (1l << i4)) == 0) {
                        boardCards[4] = CactusKevUtils.calculateCactusKevCardRepresentation(i4);
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
        boardCards[3] = CactusKevUtils.calculateCactusKevCardRepresentation(i3);
        for (int i4 = i3 + 1; i4 < 52; i4++) {
          if ((usedCards & (1l << i4)) == 0) {
            boardCards[4] = CactusKevUtils.calculateCactusKevCardRepresentation(i4);
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
        boardCards[4] = CactusKevUtils.calculateCactusKevCardRepresentation(i4);
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
    int boardScore = CactusKevUtils.calculateScore(boardCards);
    for (int i = 0; i < playerNb; i++) {
      if (!folded[i]) {
//        int score = CactusKevUtils.calculateBestPossibleScore(boardCards, playerCards[i], boardScore);
        
        boardCards[5] = playerCards[i][0];
        boardCards[6] = playerCards[i][1];

        int score = CactusKevUtils.calculateBestPossibleScore(boardCards);

        if (score > bestScore) {
          winnerId = i;
          bestScore = score;
          winNb = 1;
//          if (winnerId >= 0) {
//            // multiple winner not counted in winner
//            return -2;
//          } else {
//            winnerId = i;
//          }
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
