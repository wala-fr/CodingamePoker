package com.codingame.model.object.enumeration;

public enum HandType {
	HIGH_CARD,
	PAIR, 
	TWO_PAIR,
	THREE_OF_A_KIND,
	STRAIGHT,
	FLUSH,
	FULL_HOUSE,
	FOUR_OF_A_KIND,
	STRAIGHT_FLUSH;
  
  public String getLabel() {
    return toString().toLowerCase().replaceAll("_", " ");
  }
}
