package com.codingame.game;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.model.object.Card;

public class Player extends AbstractMultiplayerPlayer {
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

  // public void sendInputLine2(String line) {
  // System.err.println(line);
  // sendInputLine(line);
  // }

  public void sendInputLine(int line) {
    sendInputLine(Integer.toString(line));
  }
  
  public void sendInputLine(Card line) {
    sendInputLine(line.toString());
  }
}
