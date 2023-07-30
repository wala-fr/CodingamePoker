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

  public String toInputLine() {
    String str = String.format("%d %d %s %s", turn, handNb, InputUtils.toInputLine(action),
        InputUtils.toInputLineBoardCards(boardCard));
    return str;
  }

  @Override
  public String toString() {
    return "RoundInfo [turn=" + turn + ", handNb=" + handNb + ", action=" + action + ", boardCard="
        + boardCard + "]";
  }

}
