package com.codingame.game;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.input.InputSender;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.ActionType;
import com.codingame.model.utils.ActionUtils;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.MessageUtils;
import com.codingame.model.utils.RandomUtils;
import com.codingame.view.Viewer;
import com.codingame.view.data.PokerModule;
import com.codingame.view.object.Game;
import com.codingame.view.object.Phase;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {

  private static final Logger logger = LoggerFactory.getLogger(Referee.class);

  @Inject
  private MultiplayerGameManager<Player> gameManager;
  @Inject
  private GraphicEntityModule graphics;
  @Inject
  private EndScreenModule endScreenModule;
  @Inject
  private Viewer viewer;
  
  @Inject 
  private PokerModule pokerModule;

  @Inject
  private Game game;

  @Inject
  private InputSender inputSender;

  private Board board;
  private int playerEliminatedNb;

  @Override
  public void init() {
    ViewConstant.init(graphics);

    RandomUtils.init(gameManager.getRandom());

    int playerNb = gameManager.getPlayerCount();

    int firstBigBlindId = RandomUtils.nextInt(playerNb);
    board = new Board(playerNb, firstBigBlindId);
    // so the button is rightly placed at the begining
    board.resetHand();
    board.initDeck();
    board.initBlind();
    board.calculateNextPlayer();
    
    game.setBoard(board);

    gameManager.setMaxTurns(RefereeParameter.MAX_TURN);
    gameManager.setFirstTurnMaxTime(RefereeParameter.FIRST_ROUND_TIME_OUT);
    gameManager.setTurnMaxTime(RefereeParameter.TIME_OUT);
    gameManager.setFrameDuration(ViewConstant.FRAME_DURATION);
    
    gameManager.registerModule(pokerModule);
    
    viewer.init();

    playerEliminatedNb = -1;
  }

  @Override
  public void gameTurn(int turn) {
    logger.info("########################### {}", turn);
    viewer.resetTurn(turn);
    initBoard(turn);
    logger.info("{}", board.toPlayerStatesString());

    logger.info("nextPlayerId {}", board.getNextPlayerId());

    int playerId = board.getNextPlayerId();
    board.deal();
    if (playerId == -1) {
      logger.info("all players are all-in");
      inputSender.updateRoundInfo(board, null, turn);
    } else {
      Player currentPlayer = gameManager.getPlayer(playerId);
      String nickName = currentPlayer.getNicknameToken();
      AssertUtils.test(playerId == currentPlayer.getIndex());
      inputSender.sendInputs(board, turn, currentPlayer);

      ActionInfo actionInfo;
      try {
        currentPlayer.execute();
        String[] outputs = currentPlayer.getOutputs().get(0).split(";", -1);
        if (outputs.length > 1) {
          String message = outputs[1];
          currentPlayer.setMessage(message);
        }
        logger.info("outputs {}", Arrays.toString(outputs));
        outputs[0] = outputs[0].toUpperCase().trim();
        actionInfo = ActionInfo.create(playerId, outputs[0]);

      } catch (TimeoutException e) {
        logger.info("TimeoutException {}", playerId);
        gameManager
          .addToGameSummary(GameManager.formatErrorMessage(nickName + " did not output in time!"));
        currentPlayer.deactivate(board, "timeout");

        actionInfo = ActionInfo.create(playerId, ActionType.TIMEOUT);
        String errorStr = MessageUtils.format("wrong.action.timeout", playerId);
        actionInfo.setError(errorStr, true);
      }

      inputSender.updateRoundInfo(board, actionInfo, turn);
      ActionUtils.doAction(board, actionInfo);
      if (actionInfo.hasError()) {
        String message = nickName + " :" + actionInfo.getError();
        if (actionInfo.isLevelError()) {
          message = GameManager.formatErrorMessage(message);
        }
        gameManager.addToGameSummary(message);
        logger.info("actionInfo {}", actionInfo.getError());
      }
    }

    game.setPhase(Phase.ACTION);
    viewer.update();

    board.endTurn();
    inputSender.updateShowDownInfo(board);
    board.endTurnView();

    game.setPhase(Phase.END);
    viewer.update();

    eliminatePlayers();
    addWinnings(turn);

    if (board.isGameOver()) {
      logger.info("GameOver {}", board.getPot());
      board.calculateFinalScores();
      gameManager.endGame();
      return;
    }
    if (game.isMaxRound()) {
      board.cancelCurrentHand();
      board.calculateFinalScores();
      viewer.update();
    }
  }
  
  private void addWinnings(int turn) {
    if (board.isOver()) {
      for (int i = 0; i < board.getPlayerNb(); i++) {
        PlayerModel playerModel = board.getPlayer(i);
        if (playerModel.isEliminated()) {
          continue;
        }
        int winAmount = playerModel.getWinAmount();
        if (ViewUtils.isShowWinAmount(winAmount, board)) {
          Player player = gameManager.getPlayer(i);
          player.addTooltip(game, "wins " + winAmount);
        }
      }
    }
  }
  
  private void initBoard(int turn) {
    if (board.isOver() || turn == 1) {
      game.setPhase(Phase.INIT_DECK);
      if (turn > 1) {
        board.resetHand();
        board.initDeck();
      }
      viewer.update();
      if (turn > 1) {
        board.initBlind();
        board.calculateNextPlayer();
      }
      viewer.update();

      game.setPhase(Phase.DEAL);
      board.dealFirst();
      viewer.update();

    } else {
      game.setPhase(Phase.DEAL);
      board.calculateNextPlayer();
      viewer.update();
    }

  }

  private void eliminatePlayers() {
    int nb = 0;
    do {
      int nextRank = playerEliminatedNb + 1;
      logger.debug("playerEliminatedNb nextRank {}", nextRank);
      nb = 0;
      for (int i = 0; i < board.getPlayerNb(); i++) {
        PlayerModel playerModel = board.getPlayer(i);
        logger.debug("{} eliminationRank {}", i, playerModel.getEliminationRank());
        if (playerModel.getEliminationRank() == nextRank) {
          logger.debug("{} is eliminated {}", i, playerModel.getScore());
          Player player = gameManager.getPlayer(i);
          player.setScore(playerModel.getScore());
          if (player.isActive()) {
            player.deactivate(board, "has no more chips");
          }
          nb++;
        }
      }
      playerEliminatedNb += nb;
    } while (nb > 0);
  }



  @Override
  public void onEnd() {
    int playerNb = board.getPlayerNb();

    int[] scores = new int[playerNb];
    String[] text = new String[playerNb];

    for (int i = 0; i < playerNb; i++) {
      PlayerModel player = board.getPlayer(i);
      int score = player.getScore();
      scores[i] = score;
      text[i] = player.isTimeout() ? "TIMEOUT" : ("" + (score > 0 ? scores[i] : 0));
      gameManager.getPlayer(i).setScore(score);
    }
    endScreenModule.setScores(scores, text);
  }
}
