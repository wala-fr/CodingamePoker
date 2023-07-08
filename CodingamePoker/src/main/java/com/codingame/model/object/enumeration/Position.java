package com.codingame.model.object.enumeration;

public enum Position {
  DEALER("BTN", "Button"), SMALL_BLIND("SB", "Small Blind"), BIG_BLIND("BB",
      "Big Blind"), UTG("UTG", "Under The Gun"), ELIMINATED("", "");

  private String smallLabel;
  private String label;

  private Position(String smallLabel, String label) {
    this.smallLabel = smallLabel;
    this.label = label;
  }

  public String getSmallLabel() {
    return smallLabel;
  }

  public String getLabel() {
    return label;
  }

  public String toString() {
    return getLabel();
  }

}
