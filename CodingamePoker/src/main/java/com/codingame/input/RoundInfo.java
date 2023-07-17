package com.codingame.input;

import java.util.ArrayList;
import java.util.List;
import com.codingame.game.Player;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Card;
import com.codingame.model.object.board.Board;

public class RoundInfo {
  
  private int turn;
  private int handNb;
  private ActionInfo action;
  private List<Card> boardCard;
  
  
  public RoundInfo(Board board, ActionInfo action, int turn) {
    this.action = action;
    this.turn = turn;
    handNb = board.getHandNb();
    boardCard = new ArrayList<>(board.getBoardCards());
  }

  public void sendInput(Player player) {
    player.sendInputLine(turn);
    player.sendInputLine(handNb);
    player.sendInputLine(action);
    player.sendBoardCards(boardCard);
  }

}
