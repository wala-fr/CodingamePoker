package com.codingame.game;

public class RefereeParameter {
  
  public static final int FIRST_ROUND_TIME_OUT = 1000;
  public static final int TIME_OUT = 50;

  public static final int MAX_REFEREE_TURN = 10000; // NO FRAME LIMIT
  public static final int MAX_TURN = 600;

  public static final boolean POSSIBLE_ACTION_INPUT = true;

  public static final boolean SHOW_FOLDED_CARDS = true;

  public static final boolean CALCULATE_WIN_PERCENT = true;
  public static final boolean CALCULATE_SPLIT_PERCENT = CALCULATE_WIN_PERCENT && true;

  public static final boolean ASSERT_WIN_PERCENT = CALCULATE_WIN_PERCENT && false;


  public static final String WORD_DELIMITER = "_";
  public static final String NONE = "NONE";
  
}
