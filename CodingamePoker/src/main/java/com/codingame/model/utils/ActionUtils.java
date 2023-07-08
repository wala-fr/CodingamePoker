package com.codingame.model.utils;

import java.util.List;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Board;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.enumeration.ActionType;

public class ActionUtils {

//  public static void initBoard(Board board) {
//    board.resetHand();
//    board.initDeck();
//    board.dealFirst();
//    board.initBlind();
//  }



  public static void doAction(Board board, ActionInfo bet) {
    board.assertStacks();
    calculatePossibleBet(board, bet);
    board.doAction(bet);
  }



  public static void calculatePossibleBet(Board board, ActionInfo info) {
    String errorStr = null;
    int playerId = board.getNextPlayerId();
    PlayerModel player = board.getPlayer(playerId);
    int stack = player.getStack();
    Action bet = info.getAction();
    ActionType type = bet.getType();
    if (type == ActionType.CALL) {
      int callAmount = board.calculateCallAmount(player);
      if (player.getTotalBetAmount() == board.getMaxChipInAction()) {
        bet = Action.CHECK;
        errorStr = MessageUtils.format("wrong.action.call.is.check", playerId, stack, callAmount);
      } else if (callAmount >= stack) {
        bet = Action.ALL_IN;
        errorStr = MessageUtils.format("wrong.action.call.is.allin", playerId, stack, callAmount);
      }
    } else if (type == ActionType.BET) {
      // int raiseTotalAmount = board.calculateRaiseTotalAmount(player, bet.getAmount());
      // System.err.println(board.getLastRoundRaisePlayerId() +" "+ player.getId());
      if (board.getLastRoundRaisePlayerId() == player.getId()) {
        bet = Action.CALL;
        errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId, stack,
            bet.getAmount(), bet.getAmount());
      } else if (bet.getAmount() >= stack) {
        bet = Action.ALL_IN;
        errorStr = MessageUtils.format("wrong.action.raise.is.allin", playerId, stack,
            bet.getAmount(), bet.getAmount());
      } else if (player.getTotalBetAmount() + bet.getAmount() == board.getMaxChipInAction()) {
        bet = Action.CALL;
        errorStr = MessageUtils.format("wrong.action.raise.is.allin2", playerId);
      } else if (player.getTotalBetAmount() + bet.getAmount() < board.getMaxChipInAction()) {
        // wrong bet amount
        bet = Action.FOLD;
      } else {
        int newTotalAmount = player.getTotalBetAmount() + bet.getAmount();
        int raise = newTotalAmount - board.getLastTotalRoundBet();
        if (raise < board.getBigBlind()) {
          bet = Action.CALL;
          errorStr = MessageUtils.format("wrong.action.raise.at.least.one.bigblind", playerId);
        } else if (raise < 2 * board.getLastRoundRaise()) {
          bet = Action.CALL;
          errorStr = MessageUtils.format("wrong.action.raise.at.least.twice", playerId,
              board.getLastRoundRaise(), raise);
        }
      }
    } else if (type == ActionType.CHECK) {
      if (!board.isCheckPossible()) {
        bet = Action.FOLD;
        errorStr = MessageUtils.format("wrong.action.check", playerId);
      }
    }
    // ActionInfo ret = new ActionInfo(playerId, bet);
    info.setError(errorStr);
    info.setAction(bet);
    // return ret;

  }

//  public static void nextDealer(Board board) {
//    board.calculateNextDealerId();
//  }

  // public static void endBoard(Board board) {
  // if (board.isTurnOver()) {
  // board.calculatePlayerWinnings();
  // }
  // }
}
