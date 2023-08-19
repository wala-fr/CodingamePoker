package com.codingame.model.object;

import java.util.List;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.ActionType;
import com.codingame.model.object.enumeration.PlayerStatus;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.MessageUtils;
import com.codingame.model.variable.Parameter;

public class PlayerModel {

  private int id;
  private int stack;
  private int totalBetAmount;
  private int roundBetAmount;
  private int lastRoundBetAmount;

  private int winAmount;

  private Hand hand = new Hand();
  private boolean folded;
  private boolean spoken;
  private boolean elimated;
  private boolean timeout;
  private boolean allIn;

  private Action lastAction;

  private int eliminationRank = -1;
  private int score = 0;

  private FiveCardHand bestPossibleHand;

  public PlayerModel(int id, int playerNb) {
    this.id = id;
    stack = Parameter.TOTAL_BUY_IN / playerNb;
  }

  public void reset() {
    hand.reset();
    // TODO useless
    elimated = stack == 0;
    bestPossibleHand = null;
    allIn = false;
    resetEndTurn();
  }

  public void resetEndTurn() {
    folded = stack == 0;
    totalBetAmount = 0;
    winAmount = 0;
    // roundBetAmount = 0;
    resetRound();
  }

  public void resetRound() {
    roundBetAmount = 0;
    lastRoundBetAmount = 0;
    // roundLastRaise = 0;
    spoken = false;
    lastAction = null;
  }

  public void resetEndTurnView() {
    totalBetAmount = 0;
    roundBetAmount = 0;
  }

  public void addToStack(int delta) {
    stack += delta;
  }

  public void bet(int delta) {
    stack -= delta;
    totalBetAmount += delta;
    lastRoundBetAmount = roundBetAmount;
    roundBetAmount += delta;
    if (stack == 0) {
      allIn = true;
    }
  }

  public void cancelCurrentHand() {
    stack += totalBetAmount;
  }

  public void dealCard(Deck deck) {
    hand.deal(deck.dealCard());
  }

  public void calculateBestFiveCardhand(List<Card> commoneCards) {
    if (commoneCards.size() == 5 && !isFolded()) {
      bestPossibleHand = hand.calculateBestFiveCardhand(commoneCards);
    } else {
      bestPossibleHand = null;
    }
  }

  public boolean isAllIn() {
    return allIn;
  }

  public void setAllIn(boolean allIn) {
    this.allIn = allIn;
  }

  public boolean isEliminated() {
    return elimated;
  }

  public PlayerStatus getStatus() {
    if (isEliminated()) {
      return PlayerStatus.ELIMINATED;
    }
    if (isAllIn()) {
      return PlayerStatus.ALL_IN;
    }
    if (isFolded()) {
      return PlayerStatus.FOLDED;
    }
    return PlayerStatus.ACTIVE;
  }

  public int getId() {
    return id;
  }

  public int getStack() {
    return stack;
  }

  // public int getTotalStack() {
  // return stack + totalBetAmount;
  // }
  public void setStack(int stack) {
    this.stack = stack;
  }

  public int getTotalBetAmount() {
    return totalBetAmount;
  }

  public int getRoundBetAmount() {
    return roundBetAmount;
  }

  public FiveCardHand getBestPossibleHand() {
    return bestPossibleHand;
  }

  public int getBestPossibleHandValue() {
    return bestPossibleHand == null ? Integer.MIN_VALUE + 1 : bestPossibleHand.getValue();
  }

  public boolean isFolded() {
    return folded;
  }

  public boolean canStillDoAction() {
    return !folded && !isAllIn();
  }

  public void setFolded(boolean folded) {
    this.folded = folded;
  }

  public boolean isSpoken() {
    return spoken;
  }

  public void setSpoken(boolean spoken) {
    this.spoken = spoken;
  }

  public boolean isCheck() {
    return lastAction != null && lastAction.getType() == ActionType.CHECK;
  }

  public Action getLastAction() {
    return lastAction;
  }

  public void setLastAction(Action lastAction) {
    this.lastAction = lastAction;
  }

  public Hand getHand() {
    return hand;
  }

  public int getEliminationRank() {
    return eliminationRank;
  }

  public void setEliminationRank(int eliminationRank) {
    this.eliminationRank = eliminationRank;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void setElimated(boolean elimated) {
    this.elimated = elimated;
  }

  public void setTimeout(boolean timeout) {
    this.timeout = timeout;
  }

  public boolean isTimeout() {
    return timeout;
  }
  
  public int getWinAmount() {
    return winAmount;
  }

  public void setWinAmount(int amount) {
    winAmount = amount;
  }

  public String getMessage(Board board) {
    if (timeout) {
      return MessageUtils.format("player.message.timeout");
    }
    if (isEliminated()) {
      return MessageUtils.format("player.message.eliminated");
    }
    if (isFolded()) {
      return MessageUtils.format("player.message.fold");
    }
    if (isAllIn()) {
      if (roundBetAmount > 0) {
        return MessageUtils.format("player.message.allin2", roundBetAmount);
      }
      return MessageUtils.format("player.message.allin");
    }
    if (board.getLastRoundRaisePlayerId() == id) {
      if (board.getRaiseNb() == 1) {
        return MessageUtils.format("player.message.bet", board.getLastTotalRoundBet());
      }
      return MessageUtils.format("player.message.raise", board.getLastTotalRoundBet());
    }
    if (roundBetAmount < board.getLastTotalRoundBet()) {
      return MessageUtils.format("player.message.tocall",
          board.getLastTotalRoundBet() - roundBetAmount);
    } else if (lastAction != null) {
      if (lastAction.getType() == ActionType.CHECK) {
        return MessageUtils.format("player.message.check");
      } else if (lastAction.getType() == ActionType.CALL) {
        if (totalBetAmount > 0) {
          // no end reset for view done
          AssertUtils.test(roundBetAmount > lastRoundBetAmount, roundBetAmount, lastRoundBetAmount);
        }
        return MessageUtils.format("player.message.call", roundBetAmount - lastRoundBetAmount);
      } else {
        // BET and FOLD have been dealt with already
        AssertUtils.test(false);
      }
    }
    return "";

  }

  @Override
  public String toString() {
    if (isEliminated()) {
      return "[" + id + ": ELIMINATED]";
    }
    return "[" + id + " : stack=" + stack + ", inAction=" + totalBetAmount + ", hand=" + hand
        + (folded ? " FOLDED" : "") + "]";
  }


}
