package com.wala.poker.model.simulation;

import java.util.List;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.ActionType;

public class ActionTestUtils {
  
  public static void initBoardTest(Board board, List<Card> cards) {
    board.getDeck().initCards(cards);
    board.dealFirst();
    board.initBlind();
    board.calculateNextPlayer();
  }

  public static void endTurn(Board board) {
    board.endTurn();
    board.calculateNextPlayer();
  }
}
