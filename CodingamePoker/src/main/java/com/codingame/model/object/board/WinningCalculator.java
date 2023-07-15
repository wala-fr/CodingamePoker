package com.codingame.model.object.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.PlayerModel;

public class WinningCalculator {

  private static final Logger logger = LoggerFactory.getLogger(WinningCalculator.class);

  private int playerNb;
  private int[] winnings;
  private int[] bets;
  private boolean[] folded;

  private Board board;

  public WinningCalculator(Board board) {
    this.playerNb = board.getPlayerNb();
    winnings = new int[playerNb];
    bets = new int[playerNb];
    folded = new boolean[playerNb];
    this.board = board;
  }

  public static void main(String[] args) {
    System.out.println(WinningCalculator.class.getName());
  }

  public int[] calculateWinnings() {
    for (int i = 0; i < playerNb; i++) {
      winnings[i] = 0;
      PlayerModel player = board.getPlayer(i);
      bets[i] = player.getTotalBetAmount();
      folded[i] = player.isFolded();
    }
    board.initPlayerBestHands();

    logger.debug(board.toPlayerStatesString());
    resolveSidePot();
    logger.info("winnings {}", Arrays.toString(winnings));

    return winnings;
  }

  private void resolveSidePot() {
    logger.debug("bets {}", Arrays.toString(bets));
    logger.debug("folded {}", Arrays.toString(folded));
    IntSummaryStatistics summary =
        IntStream.range(0, playerNb).filter(i -> !folded[i]).map(i -> bets[i]).summaryStatistics();
    int minBet = summary.getMin();
    boolean allIn = minBet != summary.getMax();
    if (!allIn) {
      applyWinnings(minBet);
      return;
    }
    applyWinnings(minBet);
    logger.debug("winnings {}", Arrays.toString(winnings));
    resolveSidePot();
  }

  private void applyWinnings(int minBet) {
    List<Integer> winnerIds = findWinner(p -> folded[p.getId()]);
    logger.debug("winnerIds {}", winnerIds);
    int potSplitTotal = 0;
    for (int i = 0; i < playerNb; i++) {
      int tmp = Math.min(bets[i], minBet);
      potSplitTotal += tmp;
      bets[i] -= tmp;
      if (bets[i] == 0) {
        folded[i] = true;
      }
    }
    int winnerNb = winnerIds.size();
    int potSplit = potSplitTotal / winnerNb;
    int remaining = potSplitTotal % winnerNb;
    for (Integer playerId : winnerIds) {
      winnings[playerId] += potSplit;
    }
    // Odd chips go to first player in game order
    int oddChipWinningPlayerId = board.getDealerId() + 1;
    while (true) {
      oddChipWinningPlayerId %= playerNb;
      if (winnerIds.contains(oddChipWinningPlayerId)) {
        break;
      }
      oddChipWinningPlayerId++;
    }
    winnings[oddChipWinningPlayerId] += remaining;
  }

  public List<Integer> findWinner() {
    return findWinner(p -> p.isFolded());
  }

  private List<Integer> findWinner(Predicate<PlayerModel> fold) {
    int bestScore = Integer.MIN_VALUE;
    List<Integer> bestPlayers = new ArrayList<>();
    for (PlayerModel player : board.getPlayers()) {
      if (!fold.test(player)) {
        int score = player.getBestPossibleHandValue();
        if (score > bestScore) {
          bestScore = score;
          bestPlayers.clear();
          bestPlayers.add(player.getId());
        } else if (score == bestScore) {
          bestPlayers.add(player.getId());
        }
      }
    }
    return bestPlayers;
  }
}
