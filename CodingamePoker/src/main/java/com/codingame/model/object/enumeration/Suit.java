package com.codingame.model.object.enumeration;

public enum Suit {
  CLUBS("C"),
  DIAMONDS("D"),
  HEARTS("H"),
  SPADES("S");

  private String name;

  private Suit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
  
}
