package com.codingame.model.utils;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.ActionType;
import com.codingame.model.variable.Parameter;

public class ActionUtils {

  private static final Logger logger = LoggerFactory.getLogger(ActionUtils.class);

  public static void doAction(Board board, ActionInfo action) {
    logger.info("action {}", action);
    board.assertStacks();
    board.deal();
    calculatePossibleBet(board, action);
    assertActionInfo(board, action);
    board.doAction(action);
  }

  public static List<Action> calculatePossibleActions(Board board) {
    List<Action> possibleActions = new ArrayList<>();

    int playerId = board.getNextPlayerId();
    PlayerModel player = board.getPlayer(playerId);
    int stack = player.getStack();
    int callAmount = board.calculateCallAmount(player);
    if (callAmount > 0 && callAmount < stack) {
      possibleActions.add(Action.CALL);
    }
    AssertUtils.test(!board.calculateNoMoreCanDoAction());
    if (board.getLastRoundRaisePlayerId() != playerId) {
      possibleActions.add(Action.ALL_IN);
      int minRaise = board.isFirstBet() ? board.getBigBlind() : board.getLastRoundRaise();
      int minAmount = minRaise + board.getLastTotalRoundBet() - player.getRoundBetAmount();
      if (minAmount < stack) {
        possibleActions.add(Action.create(ActionType.BET, minAmount));
      }
    }
    if (board.isCheckPossible()) {
      possibleActions.add(Action.CHECK);
    }
    possibleActions.add(Action.FOLD);
    return possibleActions;
  }

  public static void assertActionInfo(Board board, ActionInfo info) {
    if (Parameter.ACTIVATE_ASSERTION) {
      ActionInfo copy = info.copy();
      logger.info("assertActionInfo {}", copy);
      calculatePossibleBet(board, copy);
      logger.info("assertActionInfo {}", copy);
      AssertUtils.test(info.getAction().equals(copy.getAction()));
    }
  }

  public static void calculatePossibleBet(Board board, ActionInfo info) {
    Action bet = info.getAction();
    ActionType type = bet.getType();

    if (type == ActionType.TIMEOUT || type == ActionType.FOLD) {
      return;
    }

    String errorStr = null;
    int playerId = board.getNextPlayerId();
    PlayerModel player = board.getPlayer(playerId);
    int stack = player.getStack();
    boolean levelError = false;
    int callAmount = board.calculateCallAmount(player);

    Action newBet = bet;
    AssertUtils.test(!board.calculateNoMoreCanDoAction());
    if (type == ActionType.CALL) {
      if (board.isCheckPossible()) {
        errorStr = MessageUtils.format("wrong.action.call.is.check", playerId);
        newBet = Action.CHECK;
      } else if (callAmount >= stack) {
        errorStr = MessageUtils.format("wrong.action.is.allin", playerId, stack, callAmount, bet);
        newBet = Action.ALL_IN;
      }
    } else if (type == ActionType.ALL_IN) {
      logger.info("LastRoundRaisePlayerId : {} - playerId : {} {}",
          board.getLastRoundRaisePlayerId(), playerId, board.getLastRoundRaise());
      if (board.getLastRoundRaisePlayerId() == playerId) {
        if (callAmount < stack) {
          newBet = Action.CALL;
          errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId, newBet);
        }
      }
    } else if (type == ActionType.BET) {
      if (bet.getAmount() <= 0) {
        errorStr = MessageUtils.format("wrong.action.bet.wrong.amount", playerId, bet.getAmount());
        levelError = true;
        newBet = Action.FOLD;
      } else if (bet.getAmount() <= callAmount) {
        newBet = callAmount < stack ? Action.CALL : Action.ALL_IN;
        errorStr = MessageUtils.format("wrong.action.raise.amount.less.than.call", playerId,
            bet.getAmount(), callAmount, newBet);
      } else if (board.getLastRoundRaisePlayerId() == playerId) {
        newBet = callAmount < stack ? Action.CALL : Action.ALL_IN;
        errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId, newBet);
      } else if (bet.getAmount() >= stack) {
        errorStr =
            MessageUtils.format("wrong.action.is.allin", playerId, stack, bet.getAmount(), bet);
        newBet = Action.ALL_IN;
      } else {
        levelError = true;
        int newTotalAmount = player.getRoundBetAmount() + bet.getAmount();
        int raise = newTotalAmount - board.getLastTotalRoundBet();
        int minRaise = board.isFirstBet() ? board.getBigBlind() : board.getLastRoundRaise();
        logger.debug("minRaise : {} - LastTotalRoundBet : {}", minRaise,
            board.getLastTotalRoundBet());
        if (raise < minRaise) {
          int newBetAmount = minRaise + board.getLastTotalRoundBet() - player.getRoundBetAmount();
          if (newBetAmount >= player.getStack()) {
            newBet = Action.ALL_IN;
          } else {
            newBet = Action.create(ActionType.BET, newBetAmount);
          }
          if (board.isFirstBet()) {
            errorStr =
                MessageUtils.format("wrong.action.raise.at.least.one.bigblind", playerId, newBet);
          } else {
            errorStr = MessageUtils.format("wrong.action.raise.at.least.twice", playerId,
                board.getLastRoundRaise(), raise, newBet);
          }
        }
      }
    } else {
      AssertUtils.test(type == ActionType.CHECK);
      if (!board.isCheckPossible()) {
        newBet = Action.FOLD;
        errorStr = MessageUtils.format("wrong.action.check", playerId);
        levelError = true;
      }
    }
    info.setError(errorStr, levelError);
    info.setAction(newBet);
  }
}
