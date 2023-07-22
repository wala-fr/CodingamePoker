package com.codingame.view.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.Player;
import com.codingame.game.RefereeParameter;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.codingame.model.object.board.Board;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Game {

  private static final Logger logger = LoggerFactory.getLogger(Game.class);

  @Inject
  private MultiplayerGameManager<Player> gameManager;
  @Inject
  private GraphicEntityModule graphics;
  @Inject
  private TooltipModule tooltips;

  private Board board;

  private double time;

  private Phase phase;

  private int turn;

  public MultiplayerGameManager<Player> getGameManager() {
    return gameManager;
  }

  public GraphicEntityModule getGraphics() {
    return graphics;
  }

  public TooltipModule getTooltips() {
    return tooltips;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public double getTime() {
    return time;
  }

  public void resetTime() {
    time = 0;
  }

  private void incrementTime(double timeIncrement) {
    this.time += timeIncrement;
    if (time > 1) {
      logger.error("incrementTime error {}", timeIncrement);
      time = 1;
    }
  }

  public void commitWorldState(double timeIncrement) {
    incrementTime(timeIncrement);
    graphics.commitWorldState(time);
  }

  public void setTime(double time) {
    if (time < this.time) {
      return;
    }
    this.time = time;
  }

  // public void setEndTime() {
  // time = phase.getEndTime();
  // }

  public void commitWorldState() {
    graphics.commitWorldState(time);
  }

  public void commitEntityState(Entity<?>... entities) {
    graphics.commitEntityState(time, entities);

  }
  
  public void commitEntityState(Entity<?> entity, double timeIncrement) {
    incrementTime(timeIncrement);
    commitEntityState(entity);
  }

  public Text createText() {
    return graphics.createText();
  }

  public Group createGroup() {
    return graphics.createGroup();
  }

  public Phase getPhase() {
    return phase;
  }

  public boolean isDeal() {
    return phase == Phase.DEAL;
  }

  public boolean isEnd() {
    return phase == Phase.END;
  }

  public boolean isAction() {
    return phase == Phase.ACTION;
  }

  public void setPhase(Phase phase) {
    this.phase = phase;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }
  
  public boolean isFirstRound() {
    return turn == 1;
  }

  public boolean isMaxRound() {
    return turn == RefereeParameter.MAX_TURN;
  }

}
