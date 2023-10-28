package com.codingame.win_percent.skeval;

public class SKPokerEvalConstant {

  public static final int NOT_A_SUIT = -1;

  public static final int RANK_OFFSET_SHIFT = 9;
  public static final int RANK_HASH_MOD = ((1 << RANK_OFFSET_SHIFT) - 1);

  public static final int FLUSH_BIT_SHIFT = 23;
  public static final int FACE_BIT_MASK = ((1 << FLUSH_BIT_SHIFT) - 1);

  private static final int[] card_values =
      {1479181, 636345, 262349, 83661, 22854, 8698, 2031, 453, 98, 22, 5, 1, 0}; // ACE DOWN TO TWO
  private static final int[] suit_values = {0, 1, 8, 57};

  public static final int[] CARDS = new int[52];
  public static final int[] SUITS = new int[52];
  public static final int[][] SUIT_KRONECKER = new int[4][];

  static {
    int index = 0;
    for (int r = 0; r < 13; r++) {
      for (int s = 0; s < 4; s++) {
        CARDS[index] = card_values[r] + (suit_values[s] << FLUSH_BIT_SHIFT);
        SUITS[index] = index % 4;
        index++;
      }
    }
    for (int s = 0; s < 4; s++) {
      SUIT_KRONECKER[s] = new int[52];
      for (int i = 0; i < 52; i++) {
        if (i % 4 == s) {
          SUIT_KRONECKER[s][i] = 1 << (12 - (i / 4));
        }
      }
    }
  }
}
