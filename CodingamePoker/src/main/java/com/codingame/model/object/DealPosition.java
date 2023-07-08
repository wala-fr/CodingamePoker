package com.codingame.model.object;

public class DealPosition {

  private static final int BURNED = 4;
  private static final int BOARD = 5;

  private int index;
  private int id;


  private DealPosition(int id, int index) {
    super();
    this.index = index;
    this.id = id;
  }

  public static DealPosition createBurnedCardPosition(int index) {
    return new DealPosition(BURNED, index);
  }

  public static DealPosition createBoardCardPosition(int index) {
    return new DealPosition(BOARD, index);
  }

  public static DealPosition createPlayerCardPosition(int id, int index) {
    return new DealPosition(id, index);
  }

  public boolean isBurned() {
    return id == BURNED;
  }

  public boolean isBoard() {
    return id == BOARD;
  }

  public int getIndex() {
    return index;
  }


  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "DealPosition [id=" + id + ", index=" + index + "]";
  }


}
