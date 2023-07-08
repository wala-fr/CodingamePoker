package com.codingame.model.object.enumeration;

public enum Rank {
  
  TWO("2", 2),
  THREE("3", 3),
  FOUR("4", 4),
  FIVE("5", 5),
  SIX("6", 6),
  SEVEN("7", 7),
  EIGHT("8", 8),
  NINE("9", 9),
  TEN("T", 10),
  JACK("J", 11),
  QUEEN("Q", 12),
  KING("K", 13),
  ACE("A", 14);
  
  private String name;
  private int index;

  private Rank(String name, int index) {
    this.name = name;
    this.index = index;
  }

  public String getName() {
    return name;
  }

  public int getIndex() {
    return index;
  }
}
