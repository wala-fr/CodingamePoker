package com.codingame.view.object;

public enum Phase {
  INIT_DECK(0.1),DEAL(0.5), ACTION(0.9), END(1);

  private double endTime;

  private Phase(double endTime) {
    this.endTime = endTime;
  }

//  public double getEndTime() {
//    return endTime;
//  }



}
