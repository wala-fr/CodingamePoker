package com.codingame.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.model.object.board.Board;
import com.codingame.view.object.Game;

public class Player extends AbstractMultiplayerPlayer {

  private static final Logger logger = LoggerFactory.getLogger(Player.class);

  private String message = "";

  @Override
  public int getExpectedOutputLines() {
    return 1;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void sendInputLine2(String line) {
    logger.debug(line);
    sendInputLine(line);
  }

  public void sendInputLine(int line) {
    sendInputLine2(Integer.toString(line));
  }

  public void deactivate(Board board, String msg) {
    deactivate(formatTooltipMessage(board, msg));
  }
  
  public void addTooltip(Game game, String msg) {
    game.getGameManager().addTooltip(this, formatTooltipMessage(game.getBoard(), msg));
  }
  
  private String formatTooltipMessage(Board board, String msg) {
    return board.getHandNb() + " : " + getNicknameToken() + " " + msg + ".";
  }

}
