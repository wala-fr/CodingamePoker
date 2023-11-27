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
import com.codingame.view.object.Frame;
import com.codingame.view.object.Game;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.codingame.win_percent.skeval.WinPercentUtils;
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
  private boolean calculateNextPlayerId;

  @Override
  public void init() {
    ViewConstant.init(graphics);

    RandomUtils.init(gameManager.getRandom());

    int playerNb = gameManager.getPlayerCount();

    int firstBigBlindId = RandomUtils.nextInt(playerNb);
    board = new Board(playerNb, firstBigBlindId);
    // so the button is rightly placed at the beginning
    board.resetHand();
    board.initDeck();
    board.initDemoBoard();
    board.initBlind();
    board.calculateNextPlayer();

    game.setBoard(board);

    gameManager.setMaxTurns(RefereeParameter.MAX_REFEREE_TURN);
    gameManager.setFirstTurnMaxTime(RefereeParameter.FIRST_ROUND_TIME_OUT);
    gameManager.setTurnMaxTime(RefereeParameter.TIME_OUT);
    gameManager.setFrameDuration(ViewConstant.FRAME_DURATION);

    gameManager.registerModule(pokerModule);

    viewer.init();

    playerEliminatedNb = -1;
  }

  @Override
  public void gameTurn(int t) {
    logger.info("########################### {} {}", t, game.getTurn());
    game.resetTime();
    game.resetFrame();
    if (board.calculatePlayerWinnings()) {
      // FRAME WINNING
      doBoardOver();
      return;
    }
    initBoard(t == 1);
    if (game.isFrame(Frame.DEAL_PLAYER)) {
      // FRAME DEAL PLAYER CARD
      WinPercentUtils.proceed(board);
      viewer.update();
      calculateNextPlayerId = false;
      return;
    }

    int cardNb = board.getBoardCards().size();
    board.endTurn();
    if (cardNb != board.getBoardCards().size()) {
      // FRAME DEAL ALL MISSING BOARD CARDS
      if (!inputSender.isHandSent(board.getHandNb())) {
        logger.info("all players are directly all-in. No player's action");
        game.incrementTurn();
        inputSender.updateRoundInfo(board, null, game.getTurn());
      }
      game.setFrame(Frame.DEAL_BOARD);

      WinPercentUtils.proceed(board);
      viewer.update();
      return;
    }

    if (board.calculatePlayerWinnings()) {
      // FRAME WINNING
      doBoardOver();
      return;
    }
    if (game.isMaxRound()) {
      // FRAME CANCEL MAX ROUND
      doCancelLastHand();
      return;
    }
    if (board.deal()) {
      board.calculateNextPlayer();
      game.setFrame(Frame.DEAL_BOARD);
      WinPercentUtils.proceed(board);
      viewer.update();
      calculateNextPlayerId = false;
      return;
    }
    game.setFrame(Frame.ACTION);
    if (calculateNextPlayerId) {
      board.calculateNextPlayer();
//      viewer.update();
    }
    calculateNextPlayerId = true;

    int playerId = board.getNextPlayerId();
    logger.info("{}", board.toPlayerStatesString());
    logger.info("nextPlayerId {}", board.getNextPlayerId());

    Player currentPlayer = gameManager.getPlayer(playerId);
    String nickName = currentPlayer.getNicknameToken();
    AssertUtils.test(playerId == currentPlayer.getIndex());
    
    game.incrementTurn();
    inputSender.sendInputs(board, game.getTurn(), currentPlayer);

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

    inputSender.updateRoundInfo(board, actionInfo, game.getTurn());
    ActionUtils.doAction(board, actionInfo);
    if (actionInfo.hasError()) {
      String message = nickName + " :" + actionInfo.getError();
      if (actionInfo.isLevelError()) {
        message = GameManager.formatErrorMessage(message);
      }
      gameManager.addToGameSummary(message);
      logger.info("actionInfo {}", actionInfo.getError());
    }
    WinPercentUtils.proceed(board);

    viewer.update();

  }

  private void doBoardOver() {
    game.setFrame(Frame.END_HAND);
    inputSender.updateShowDownInfo(board);

    board.endTurnView();
    viewer.update();

    eliminatePlayers();
    addWinnings();

    if (board.isGameOver()) {
      logger.info("GameOver {}", board.getPot());
      board.calculateFinalScores();
      gameManager.endGame();
    }
  }

  private void doCancelLastHand() {
    game.setFrame(Frame.LAST_END_CANCELLED);
    board.cancelCurrentHand();
    board.calculateFinalScores();
    viewer.update();
    gameManager.endGame();
  }

  private void addWinnings() {
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

  private void initBoard(boolean first) {
    if (board.isOver() || first) {
      game.setFrame(Frame.DEAL_PLAYER);
      if (!first) {
        board.resetHand();
        board.initDeck();
        board.initDemoBoard();
      }
      if (!first) {
        board.initBlind();
        board.calculateNextPlayer();
      }
      viewer.update();

      board.dealFirst();
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
