package com.codingame.model.object.enumeration;

public enum BoardStatus {
  PRE_FLOP, FLOP, TURN, RIVER;
  
  public static BoardStatus getStatus(int boardCardNb) {
    if (boardCardNb == 0) {
      return PRE_FLOP;
    }
    if (boardCardNb == 3) {
      return FLOP;
    }
    if (boardCardNb == 4) {
      return TURN;
    }
    if (boardCardNb == 5) {
      return RIVER;
    }
    throw new IllegalStateException();
  }
}
