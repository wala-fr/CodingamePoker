package com.codingame.model.object.enumeration;

public enum HandType {
	HIGH_CARD,
	PAIR, 
	TWO_PAIR,
	THREE_OF_A_KIND("TRIP"),
	STRAIGHT,
	FLUSH,
	FULL_HOUSE("FULL"),
	FOUR_OF_A_KIND("QUAD"),
	STRAIGHT_FLUSH("STR FLUSH");
  
  private String shortName;
  
  private HandType() {
  }
  
  private HandType(String shortName) {
    this.shortName = shortName;
  }

  public String getLabel() {
    return toString().toLowerCase().replaceAll("_", " ");
  }

  public String getShortName() {
    return shortName == null ? getLabel().toUpperCase() : shortName;
  }
  
  
}
