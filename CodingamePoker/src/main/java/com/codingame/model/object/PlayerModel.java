package com.codingame.model.object;

import java.util.ArrayList;
import java.util.List;
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

  private int roundLastRaise;

  private Hand hand = new Hand();
  private boolean folded;
  private boolean spoken;
  private boolean elimated;

  private Action lastAction;

  private int eliminationRank = -1;
  private int score = 0;


  private FiveCardHand bestPossibleHand;

  public PlayerModel(int id) {
    this.id = id;
    stack = Parameter.BUY_IN;
  }

  public void reset() {
    hand.reset();
    elimated = stack == 0;
    resetEndTurn();
  }
  
  public void resetEndTurn() {
    folded = stack == 0;
    totalBetAmount = 0;
    // roundBetAmount = 0;
    resetRound();
  }

  public void resetRound() {
    roundBetAmount = 0;
    lastRoundBetAmount = 0;
    roundLastRaise = 0;
    spoken = false;
    lastAction = null;
  }

  public void addToStack(int delta) {
    stack += delta;
  }

  public void bet(int delta) {
    stack -= delta;
    totalBetAmount += delta;
    lastRoundBetAmount = roundBetAmount;
    roundBetAmount += delta;
  }

  public void cancelCurrentHand() {
    stack += totalBetAmount;
  }

  public void dealCard(Deck deck) {
    hand.deal(deck.dealCard());
  }

  public void calculateBestFiveCardhand(List<Card> commoneCards) {
    bestPossibleHand = hand.calculateBestFiveCardhand(commoneCards);
  }

  public boolean isAllIn() {
    return stack == 0 && totalBetAmount > 0;
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
  
//  public int getTotalStack() {
//    return stack + totalBetAmount;
//  }
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

  public boolean isFolded() {
    return folded;
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

  public int getRoundLastRaise() {
    return roundLastRaise;
  }

  public void setRoundLastRaise(int roundLastRaiseAmount) {
    this.roundLastRaise = roundLastRaiseAmount;
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

  public String getMessage(Board board) {
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
      // AssertUtils.test(board.getLastTotalRoundBet() == roundBetAmount, this,
      // board.getLastTotalRoundBet(), roundBetAmount);
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
      } else {
        AssertUtils.test(lastAction.getType() == ActionType.CALL, lastAction.getType());
        return MessageUtils.format("player.message.call", roundBetAmount - lastRoundBetAmount);
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
