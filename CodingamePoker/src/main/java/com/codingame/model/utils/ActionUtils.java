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

public class ActionUtils {

  private static final Logger logger = LoggerFactory.getLogger(ActionUtils.class);

  public static void doAction(Board board, ActionInfo action) {
    logger.info("action {}", action);
    board.assertStacks();
    calculatePossibleBet(board, action);
    board.doAction(action);
  }

  public static List<Action> calculatePossibleActions(Board board) {
    List<Action> possibleActions = new ArrayList<>();

    int playerId = board.getNextPlayerId();
    PlayerModel player = board.getPlayer(playerId);
    int stack = player.getStack();
    int callAmount = board.calculateCallAmount(player);
    if (player.getTotalBetAmount() != board.calculateMaxChipInAction() && callAmount < stack) {
      possibleActions.add(Action.CALL);
    }
    if (board.getLastRoundRaisePlayerId() != playerId && board.isBetPossible()) {
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
    if (!board.isBetPossible()) {
      // particular case when player can only make a bad fold
      if (type == ActionType.CALL || type == ActionType.ALL_IN || type == ActionType.BET) {
        errorStr = MessageUtils.format("wrong.action.bet.is.impossible", playerId);
        newBet = Action.CHECK;
      }
    } else {
      if (type == ActionType.CALL) {
        if (player.getTotalBetAmount() == board.calculateMaxChipInAction()) {
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
          errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId);
          newBet = Action.CALL;
        }
      } else if (type == ActionType.BET) {
        if (board.getLastRoundRaisePlayerId() == playerId) {
          errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId);
          newBet = Action.CALL;
        } else if (bet.getAmount() >= stack) {
          errorStr =
              MessageUtils.format("wrong.action.is.allin", playerId, stack, bet.getAmount(), bet);
          newBet = Action.ALL_IN;
        } else {
          levelError = true;
          if (bet.getAmount() <= callAmount) {
            errorStr = MessageUtils.format("wrong.action.raise.amount.less.than.call", playerId,
                bet.getAmount(), callAmount);
            newBet = Action.CALL;
          } else {
            int newTotalAmount = player.getRoundBetAmount() + bet.getAmount();
            int raise = newTotalAmount - board.getLastTotalRoundBet();
            int minRaise = board.isFirstBet() ? board.getBigBlind() : board.getLastRoundRaise();
            logger.debug("minRaise : {} - LastTotalRoundBet : {}", minRaise,
                board.getLastTotalRoundBet());
            if (raise < minRaise) {
              int newBetAmount =
                  minRaise + board.getLastTotalRoundBet() - player.getRoundBetAmount();
              if (newBetAmount >= player.getStack()) {
                newBet = Action.ALL_IN;
              } else {
                newBet = Action.create(ActionType.BET, newBetAmount);
              }
              if (board.isFirstBet()) {
                errorStr = MessageUtils.format("wrong.action.raise.at.least.one.bigblind", playerId,
                    newBet);
              } else {
                errorStr = MessageUtils.format("wrong.action.raise.at.least.twice", playerId,
                    board.getLastRoundRaise(), raise, newBet);
              }
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
    }
    info.setError(errorStr, levelError);
    info.setAction(newBet);
  }
}
