package com.codingame.game;

import java.lang.reflect.Field;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Board;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.utils.ActionUtils;
import com.codingame.model.utils.RandomUtils;
import com.codingame.model.variable.Parameter;
import com.codingame.view.Viewer;
import com.codingame.view.object.Graphic;
import com.codingame.view.object.Phase;
import com.codingame.view.parameter.ViewConstant;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
  @Inject
  private MultiplayerGameManager<Player> gameManager;
  @Inject
  private GraphicEntityModule graphics;
  @Inject
  private TooltipModule tooltips;
  @Inject
  private ToggleModule toggleModule;
  @Inject
  private EndScreenModule endScreenModule;
  @Inject
  private Viewer viewer;

  @Inject
  private Graphic graphic;

  private Board board;
  private int playerEliminatedNb;

  @Override
  public void init() {
    ViewConstant.init(graphics);

    RandomUtils.init(gameManager.getRandom());

    int playerNb = Constant.PLAYER_NB;

    int dealerId = RandomUtils.nextInt(playerNb);
    board = new Board(playerNb, dealerId);

    // board.init(Constant.PLAYER_NB);
    gameManager.setMaxTurns(Constant.MAX_TURN);
    gameManager.setFirstTurnMaxTime(Constant.FIRST_ROUND_TIME_OUT);
    gameManager.setTurnMaxTime(Constant.TIME_OUT);
    gameManager.setFrameDuration(ViewConstant.FRAME_DURATION);

    // ActionUtils.initBoard(board);
    viewer.init(board);
    // viewer.resetTurn(turn);
    //
    // graphic.setPhase(Phase.INIT_DECK);
    // board.resetHand();
    // board.initDeck();
    // board.initBlind(); // viewer.update(board, -1);
    // viewer.update(board);

    playerEliminatedNb = -1;


  }

  @Override
  public void gameTurn(int turn) {
    System.err.println("TTTTTTTTTTTTTTTTTT " + turn);
    System.err.println(board.toPlayerStatesString());
    viewer.resetTurn(turn);
    initBoard();

    System.err.println("nextPlayerId " + board.getNextPlayerId());
    Player currentPlayer = gameManager.getPlayer(board.getNextPlayerId());
    String nickName = currentPlayer.getNicknameToken();
    int playerId = currentPlayer.getIndex();
    System.err.println("turn " + turn + " playerId " + playerId + " gameNb " + board.getGameNb()
        + " LastPlayerId " + board.getLastPlayerId() + " dealer " + board.getDealerId());
    System.err.println(board);
    try {
      sendInputs(turn, currentPlayer);
      currentPlayer.execute();
      System.err.println("currentPlayer.getOutputs() " + currentPlayer.getOutputs());

      String[] outputs = currentPlayer.getOutputs().get(0).split(";", -1);
      if (outputs.length > 1) {
        String message = outputs[1];
        currentPlayer.setMessage(message);
      }
      outputs[0] = outputs[0].toUpperCase().trim();
      ActionInfo actionInfo = ActionInfo.create(playerId, outputs[0]);
      ActionUtils.doAction(board, actionInfo);
      graphic.setPhase(Phase.ACTION);
      viewer.update(board);

      board.endTurn();
      graphic.setPhase(Phase.END);
      viewer.update(board);

      if (actionInfo.hasError()) {
        gameManager
          .addToGameSummary(GameManager.formatErrorMessage(nickName + " " + actionInfo.getError()));
        System.err.println("actionInfo " + actionInfo.getError());

      }
    } catch (TimeoutException e) {
      // TODO considered as fold ????
      gameManager
        .addToGameSummary(GameManager.formatErrorMessage(nickName + " did not output in time!"));
      currentPlayer.deactivate(nickName + " timeout.");
      currentPlayer.setScore(-1);
      gameManager.endGame();
      return;
    }
    eliminatePlayers();
    if (board.isGameOver()) {
      System.err.println("GameOver " + board.getPot());
      gameManager.endGame();
      return;
    }
    if (graphic.isEndRound() ) {
      board.cancelCurrentHand();
      board.calculateFinalScores();
      viewer.update(board);
    }

  }

  private void initBoard() {
    if (board.isOver()) {
      System.err.println("############################");
      graphic.setPhase(Phase.INIT_DECK);
      board.resetHand();
      board.initDeck();
      viewer.update(board);

      board.initBlind();
      board.calculateNextPlayer();
      viewer.update(board);

      // viewer.update(board);

      graphic.setPhase(Phase.DEAL);
      board.dealFirst();
      viewer.update(board);



      // if (board.getGameNb() > 1) {
      // // AVOID IF
      // ActionUtils.nextDealer(board);
      // }
    } else {
      graphic.setPhase(Phase.DEAL);
      board.calculateNextPlayer();
      viewer.update(board);
    }

  }

  private void eliminatePlayers() {
    // int playerEliminatedNb = board.getPlayerEliminatedNb();
    // while (playerEliminatedNb != board.getPlayerEliminatedNb()) {
    // System.err.println(playerEliminatedNb + " " + board.getPlayerEliminatedNb());
    int nb = 0;
    do {
      int nextRank = playerEliminatedNb + 1;
      System.err.println("playerEliminatedNb " + nextRank);
      nb = 0;
      for (int i = 0; i < board.getPlayerNb(); i++) {
        PlayerModel playerModel = board.getPlayer(i);
        System.err.println(i + " getEliminationRank " + playerModel.getEliminationRank());

        if (playerModel.getEliminationRank() == nextRank) {
          System.err.println(i + " is eliminated " + playerModel.getScore());
          Player player = gameManager.getPlayer(i);
          player.setScore(playerModel.getScore());
          String nickName = gameManager.getPlayer(i).getNicknameToken();
          player.deactivate(nickName + " has no more chips.");
          nb++;
        }
      }
      playerEliminatedNb += nb;
    } while (nb > 0);
  }

  // private void findWinner() {
  // // there's a winner
  // for (int i = 0; i < board.getPlayerNb(); i++) {
  // PlayerModel player = board.getPlayer(i);
  // int eliminationRank = player.getEliminationRank();
  // if (eliminationRank == -1) {
  // player.setEliminationRank(lastEliminationRank++);
  // Player playerCG = gameManager.getPlayer(i);
  // playerCG.setScore(player.getStack());
  // }
  // }
  // gameManager.endGame();
  // }

  public void sendInputs(int turn, Player currentPlayer) {
    int id = currentPlayer.getIndex();
    int playerNb = board.getPlayerNb();
    PlayerModel playerModel = board.getPlayer(id);
    if (turn <= board.getPlayerNb()) {
      currentPlayer.sendInputLine(Parameter.SMALL_BLINB);
      currentPlayer.sendInputLine(Parameter.BIG_BLINB);
      currentPlayer.sendInputLine(Parameter.HAND_NB_BY_LEVEL);
      currentPlayer.sendInputLine(Parameter.LEVEL_BLIND_MULTIPLICATOR);
      currentPlayer.sendInputLine(playerNb);
      currentPlayer.sendInputLine(id);
    }
    for (int i = 0; i < playerNb; i++) {
      currentPlayer.sendInputLine(board.getPlayer(i).getStack());
    }
    currentPlayer.sendInputLine(board.getPot());
    int boardCardNb = board.getBoardCards().size();
    currentPlayer.sendInputLine(boardCardNb);
    for (int i = 0; i < boardCardNb; i++) {
      currentPlayer.sendInputLine(board.getBoardCards().get(i));
    }
    for (int i = 0; i < 2; i++) {
      currentPlayer.sendInputLine(playerModel.getHand().getCard(i));
    }
    int actionNb = playerNb;
    int index = id + 1;
    for (int i = 0; i < actionNb; i++) {
      index++;
      index %= playerNb;
      Action lastAction = board.getPlayer(index).getLastAction();
      currentPlayer.sendInputLine(lastAction == null ? "" : lastAction.toString());
    }
  }

  @Override
  public void onEnd() {
    int[] scores = new int[4];
    String[] text = new String[4];

    for (int i = 0; i < scores.length; i++) {
      scores[i] = board.getPlayer(i).getScore();
      text[i] = scores[i] > 0 ? "" + scores[i] : "";
      System.err.println(i + " " + scores[i]);
    }
    // int winId = scores[0] > scores[1] ? 0 : 1;
    // String nickName = gameManager.getPlayer(winId).getNicknameToken();
    // gameManager.addToGameSummary(GameManager.formatSuccessMessage(nickName + " won"));
    // gameManager.addTooltip(gameManager.getPlayer(winId), nickName + " won");
    // text[1 - winId] = "Lost";
    // text[winId] = "Won";
    endScreenModule.setScores(scores, text);
  }
}
