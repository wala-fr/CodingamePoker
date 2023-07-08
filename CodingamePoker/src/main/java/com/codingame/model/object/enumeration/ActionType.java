package com.codingame.model.object.enumeration;

public enum ActionType {
  FOLD, CHECK, ALL_IN, BET, CALL;
  
  public static ActionType fromString(String str) {
    str = str.toUpperCase().trim();
    if ("ALL-IN".equals(str)) {
      return ALL_IN;
    }
    return ActionType.valueOf(str);
  }
}
