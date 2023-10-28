package com.codingame.model.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    boolean raiseCap = board.isRaiseCap();
    
    if (callAmount > 0) {
      if (callAmount < stack) {
        possibleActions.add(Action.CALL);
      } else if (!raiseCap) {
        possibleActions.add(Action.ALL_IN);
      }
    }
    AssertUtils.test(!board.calculateNoMoreCanDoAction());
    if (board.getLastRoundRaisePlayerId() != playerId && !raiseCap) {
      if (!possibleActions.contains(Action.ALL_IN)) {
        possibleActions.add(Action.ALL_IN);
      }
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
    assertPossibleActions(board, possibleActions);
    return possibleActions;
  }

  private static void assertActionInfo(Board board, ActionInfo info) {
    if (Parameter.ACTIVATE_ASSERTION) {
      ActionInfo copy = info.copy();
      logger.info("assertActionInfo {}", copy);
      calculatePossibleBet(board, copy);
      logger.info("assertActionInfo {}", copy);
      Action action = info.getAction();
      AssertUtils.test(action.equals(copy.getAction()));
      
      if (action.getType() == ActionType.TIMEOUT) {
        return;
      }
      List<Action> possibleActions = calculatePossibleActions(board);
      if (action.isBet()) {
        Optional<Action> opt = possibleActions.stream().filter(a -> a.isBet()).findFirst();
        AssertUtils.test(opt.isPresent());
        AssertUtils.test(opt.get().getAmount() <= action.getAmount(), action, opt.get());
      } else {
        AssertUtils.test(possibleActions.contains(action), action, possibleActions);
      }
    }
  }
  
  private static void assertPossibleActions(Board board, List<Action> possibleActions) {
    if (Parameter.ACTIVATE_ASSERTION) {

      for (Action action : possibleActions) {
        ActionInfo info = ActionInfo.create(0, action);
        calculatePossibleBet(board, info);
        AssertUtils.test(!info.hasError(), action);
      }
      Action[] actions = {Action.FOLD, Action.CALL, Action.ALL_IN, Action.CHECK}; 
      for (Action action : actions) {
        if (possibleActions.contains(action)) {
          continue;
        }
        ActionInfo info = ActionInfo.create(0, action);
        calculatePossibleBet(board, info);
        AssertUtils.test(info.hasError(), action);
      }
      AssertUtils.test(possibleActions.contains(Action.ALL_IN) || possibleActions.contains(Action.CALL));

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
    boolean raiseCap = board.isRaiseCap();
    
    // in case the new action is a call
    Action callAction = callAmount < stack ? Action.CALL : Action.ALL_IN;
        
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
      if (raiseCap) {
        newBet = callAction;
        errorStr = MessageUtils.format("wrong.action.can.not.raise.cap", playerId, newBet, Parameter.RAISE_CAP);
      }
    } else if (type == ActionType.BET) {
      if (bet.getAmount() <= 0) {
        errorStr = MessageUtils.format("wrong.action.bet.wrong.amount", playerId, bet.getAmount());
        levelError = true;
        newBet = Action.FOLD;
      } else if (bet.getAmount() <= callAmount) {
        newBet = callAction;
        errorStr = MessageUtils.format("wrong.action.raise.amount.less.than.call", playerId,
            bet.getAmount(), callAmount, newBet);
      } else if (board.getLastRoundRaisePlayerId() == playerId) {
        newBet = callAction;
        errorStr = MessageUtils.format("wrong.action.can.not.raise", playerId, newBet);
      } else if (raiseCap) {
        newBet = callAction;
        errorStr = MessageUtils.format("wrong.action.can.not.raise.cap", playerId, newBet, Parameter.RAISE_CAP);
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
