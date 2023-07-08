package com.codingame.model.object.enumeration;

public enum PlayerStatus {
  FOLDED, ELIMINATED, ALL_IN, ACTIVE;
  
  public String toString() {
    return super.toString().replaceAll("_", " ");
  }
}
