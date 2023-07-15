package com.codingame.model.utils;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.ActionType;

public class ActionUtils {

  private static final Logger logger = LoggerFactory.getLogger(ActionUtils.class);

  // public static void initBoard(Board board) {
  // board.resetHand();
  // board.initDeck();
  // board.dealFirst();
  // board.initBlind();
  // }



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
    int callAmount = board.calculateCallAmount(player);
    if (type == ActionType.CALL) {
      if (player.getTotalBetAmount() == board.calculateMaxChipInAction()) {
        errorStr = MessageUtils.format("wrong.action.call.is.check", playerId, stack, callAmount);
        bet = Action.CHECK;
      } else if (callAmount >= stack) {
        errorStr = MessageUtils.format("wrong.action.call.is.allin", playerId, stack, callAmount);
        bet = Action.ALL_IN;
      }
    } else if (type == ActionType.ALL_IN) {
      logger.info("LastRoundRaisePlayerId : {} - playerId : {} {}",
          board.getLastRoundRaisePlayerId(), playerId, board.getLastRoundRaise());

      if (board.getLastRoundRaisePlayerId() == playerId) {
        errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId);
        bet = Action.CALL;
      }
    } else if (type == ActionType.BET) {
      // int raiseTotalAmount = board.calculateRaiseTotalAmount(player, bet.getAmount());
      // System.err.println(board.getLastRoundRaisePlayerId() +" "+ player.getId());
      if (board.getLastRoundRaisePlayerId() == playerId) {
        errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId);
        bet = Action.CALL;
      } else if (bet.getAmount() >= stack) {
        errorStr = MessageUtils.format("wrong.action.raise.is.allin", playerId, stack,
            bet.getAmount(), bet.getAmount());
        bet = Action.ALL_IN;
        // } else if (player.getTotalBetAmount() + bet.getAmount() == board.getMaxChipInAction()) {
        // bet = Action.CALL;
        // errorStr = MessageUtils.format("wrong.action.raise.is.allin2", playerId);
        // } else if (bet.getAmount() < callAmount) {
        // errorStr = MessageUtils.format("wrong.action.raise.amount", playerId);
        // bet = Action.CALL;
      } else {
        if (bet.getAmount() <= callAmount) {
          errorStr = MessageUtils.format("wrong.action.raise.amount.less.than.call", playerId,
              bet.getAmount(), callAmount);
          bet = Action.CALL;
        } else {
          int newTotalAmount = player.getRoundBetAmount() + bet.getAmount();
          int raise = newTotalAmount - board.getLastTotalRoundBet();
          int minRaise = board.isFirstBet() ? board.getBigBlind() : board.getLastRoundRaise();
          logger.error("minRaise : {} - LastTotalRoundBet : {}", minRaise,
              board.getLastTotalRoundBet());
          if (raise < minRaise) {
            int newBetAmount = minRaise + board.getLastTotalRoundBet() - player.getRoundBetAmount();
            if (newBetAmount >= player.getStack()) {
              bet = Action.ALL_IN;
            } else {
              bet = Action.create(ActionType.BET, newBetAmount);
            }
            if (board.isFirstBet()) {
              errorStr =
                  MessageUtils.format("wrong.action.raise.at.least.one.bigblind", playerId, bet);
            } else {
              errorStr = MessageUtils.format("wrong.action.raise.at.least.twice", playerId,
                  board.getLastRoundRaise(), raise, bet);
            }
          }
          // String message = "wrong.action.raise.at.least.one.bigblind";
          // if (board.getBigBlind() >= stack) {
          // errorStr += "2";
          // bet = Action.ALL_IN;
          // } else {
          // bet = Action.create(ActionType.BET, board.getBigBlind());
          // }
          // bet = Action.CHECK;
          //
          // else if (raise < 2 * board.getLastRoundRaise()) {
          // String message = "wrong.action.raise.at.least.twice";
          // if (callAmount >= stack) {
          // errorStr += "2";
          // bet = Action.ALL_IN;
          // } else {
          // bet = Action.CALL;
          // }
          // errorStr = MessageUtils.format(message, playerId, board.getLastRoundRaise(), raise);
          // }
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

  // public static void nextDealer(Board board) {
  // board.calculateNextDealerId();
  // }

  // public static void endBoard(Board board) {
  // if (board.isTurnOver()) {
  // board.calculatePlayerWinnings();
  // }
  // }
}
