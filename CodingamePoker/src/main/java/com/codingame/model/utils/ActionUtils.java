package com.codingame.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
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
    boolean levelError = false;
    
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
      if (board.getLastRoundRaisePlayerId() == playerId) {
        errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId);
        bet = Action.CALL;
      } else if (bet.getAmount() >= stack) {
        errorStr = MessageUtils.format("wrong.action.raise.is.allin", playerId, stack,
            bet.getAmount(), bet.getAmount());
        bet = Action.ALL_IN;
      } else {
        levelError = true;
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
        }
      }
    } else if (type == ActionType.CHECK) {
      if (!board.isCheckPossible()) {
        bet = Action.FOLD;
        errorStr = MessageUtils.format("wrong.action.check", playerId);
        levelError = true;
      }
    }
    // ActionInfo ret = new ActionInfo(playerId, bet);
    info.setError(errorStr, levelError);
    info.setAction(bet);
    // return ret;

  }
}
