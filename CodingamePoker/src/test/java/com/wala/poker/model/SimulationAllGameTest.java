package com.wala.poker.model;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.scanner.Constant;
import com.codingame.game.RefereeParameter;
import com.codingame.gameengine.core.GameManager;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.ActionType;
import com.codingame.model.utils.ActionUtils;
import com.codingame.model.utils.RandomUtils;
import com.codingame.view.object.Game;
import com.codingame.view.object.Phase;

public class SimulationAllGameTest {

  private static final Logger logger = LoggerFactory.getLogger(ActionUtils.class);


  public static void main(String[] args) {
    while (true) {
      long seed = new Random().nextLong();
      logger.error("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% {}", seed);
      test(seed);
    }
  }

  public static void test(long seed) {
    RandomUtils.init(new Random(seed));
    int playerNb = RandomUtils.nextInt(3) + 2;
    int bbId = RandomUtils.nextInt(playerNb);
    Board board = new Board(playerNb, bbId);
    logger.error("######################## playerNb {} bbId {}", playerNb, bbId);

    int turn = 0;
    while (true) {
      turn++;
      initBoard(board);
      int id = board.getNextPlayerId();
      if (id != -1) {
        Action action = chooseRandom(board, id);
        if (action.getType() == ActionType.ALL_IN) {
          action = chooseRandom(board, id);
        }
        ActionInfo actionInfo = ActionInfo.create(id, action);
        logger.info("######################## {} {}", board.getHandNb(), actionInfo);
        logger.info("{}", board.toPlayerStatesString());
        ActionUtils.doAction(board, actionInfo);
        if (actionInfo.hasError()) {
          logger.info("actionInfo {}", actionInfo.getError());
        }
      }
      board.endTurn();
      board.endTurnView();
      logger.info("isGameOver {}", board.isGameOver());

      if (board.isGameOver()) {
        board.calculateFinalScores();
        break;
      }
      if (turn == RefereeParameter.MAX_TURN) {
        board.cancelCurrentHand();
        board.calculateFinalScores();
        break;
      }
    }
    for (PlayerModel player : board.getPlayers()) {
      logger.error("{} {} {} ######## {}", turn, board.getHandNb(), player.getId(),
          player.getScore());
    }
  }

  public static Action chooseRandom(Board board, int id) {
    int action = RandomUtils.nextInt(5);
    ActionType type = ActionType.values()[action];
    if (type == ActionType.BET) {
      int stack = board.getPlayer(id).getStack();
      int amount = RandomUtils.nextInt(stack + 10) - 1;
      return Action.create(type, amount);
    } else {
      return Action.create(type);
    }
  }

  private static void initBoard(Board board) {
    if (board.isOver()) {
      board.resetHand();
      board.initDeck();
      board.initBlind();
      board.calculateNextPlayer();
      board.dealFirst();
    } else {
      board.calculateNextPlayer();
    }

  }
}
