//package com.codingame.model.utils.skeval;
//
//import java.util.Arrays;
//import com.codingame.model.utils.AssertUtils;
//
//public class SKPokerEvalConstant2 {
//
//  public static final int SPADE = 0;
//  public static final int HEART = 1;
//  public static final int DIAMOND = 8;
//  public static final int CLUB = 57;
//
//  public static final int INDEX_SPADE = 0;
//  public static final int INDEX_HEART = 1;
//  public static final int INDEX_DIAMOND = 2;
//  public static final int INDEX_CLUB = 3;
//
//  public static final int NOT_A_SUIT = -1;
//
//  public static final int TWO_FIVE = 0;
//  public static final int THREE_FIVE = 1;
//  public static final int FOUR_FIVE = 5;
//  public static final int FIVE_FIVE = 22;
//  public static final int SIX_FIVE = 94;
//  public static final int SEVEN_FIVE = 312;
//  public static final int EIGHT_FIVE = 992;
//  public static final int NINE_FIVE = 2422;
//  public static final int TEN_FIVE = 5624;
//  public static final int JACK_FIVE = 12522;
//  public static final int QUEEN_FIVE = 19998;
//  public static final int KING_FIVE = 43258;
//  public static final int ACE_FIVE = 79415;
//
//  public static final int TWO_FLUSH = 1;
//  public static final int THREE_FLUSH = TWO_FLUSH << 1;
//  public static final int FOUR_FLUSH = THREE_FLUSH << 1;
//  public static final int FIVE_FLUSH = FOUR_FLUSH << 1;
//  public static final int SIX_FLUSH = FIVE_FLUSH << 1;
//  public static final int SEVEN_FLUSH = SIX_FLUSH << 1;
//  public static final int EIGHT_FLUSH = SEVEN_FLUSH << 1;
//  public static final int NINE_FLUSH = EIGHT_FLUSH << 1;
//  public static final int TEN_FLUSH = NINE_FLUSH << 1;
//  public static final int JACK_FLUSH = TEN_FLUSH << 1;
//  public static final int QUEEN_FLUSH = JACK_FLUSH << 1;
//  public static final int KING_FLUSH = QUEEN_FLUSH << 1;
//  public static final int ACE_FLUSH = KING_FLUSH << 1;
//
//  public static final int TWO = 0;
//  public static final int THREE = 1;
//  public static final int FOUR = 5;
//  public static final int FIVE = 22;
//  public static final int SIX = 98;
//  public static final int SEVEN = 453;
//  public static final int EIGHT = 2031;
//  public static final int NINE = 8698;
//  public static final int TEN = 22854;
//  public static final int JACK = 83661;
//  public static final int QUEEN = 262349;
//  public static final int KING = 636345;
//  public static final int ACE = 1479181;
//
//  public static final int RANK_OFFSET_SHIFT = 9;
//  public static final int RANK_HASH_MOD = ((1 << RANK_OFFSET_SHIFT) - 1);
//
//  // public static final int MAX_FLUSH_CHECK_SUM = (7 * CLUB);
//
//  // Bit masks
//  public static final int FLUSH_BIT_SHIFT = 23;
//  public static final int FACE_BIT_MASK = ((1 << FLUSH_BIT_SHIFT) - 1);
//
//  public static final int[] card =
//      {ACE + (SPADE << FLUSH_BIT_SHIFT), ACE + (HEART << FLUSH_BIT_SHIFT),
//          ACE + (DIAMOND << FLUSH_BIT_SHIFT), ACE + (CLUB << FLUSH_BIT_SHIFT),
//
//          KING + (SPADE << FLUSH_BIT_SHIFT), KING + (HEART << FLUSH_BIT_SHIFT),
//          KING + (DIAMOND << FLUSH_BIT_SHIFT), KING + (CLUB << FLUSH_BIT_SHIFT),
//
//          QUEEN + (SPADE << FLUSH_BIT_SHIFT), QUEEN + (HEART << FLUSH_BIT_SHIFT),
//          QUEEN + (DIAMOND << FLUSH_BIT_SHIFT), QUEEN + (CLUB << FLUSH_BIT_SHIFT),
//
//          JACK + (SPADE << FLUSH_BIT_SHIFT), JACK + (HEART << FLUSH_BIT_SHIFT),
//          JACK + (DIAMOND << FLUSH_BIT_SHIFT), JACK + (CLUB << FLUSH_BIT_SHIFT),
//
//          TEN + (SPADE << FLUSH_BIT_SHIFT), TEN + (HEART << FLUSH_BIT_SHIFT),
//          TEN + (DIAMOND << FLUSH_BIT_SHIFT), TEN + (CLUB << FLUSH_BIT_SHIFT),
//
//          NINE + (SPADE << FLUSH_BIT_SHIFT), NINE + (HEART << FLUSH_BIT_SHIFT),
//          NINE + (DIAMOND << FLUSH_BIT_SHIFT), NINE + (CLUB << FLUSH_BIT_SHIFT),
//
//          EIGHT + (SPADE << FLUSH_BIT_SHIFT), EIGHT + (HEART << FLUSH_BIT_SHIFT),
//          EIGHT + (DIAMOND << FLUSH_BIT_SHIFT), EIGHT + (CLUB << FLUSH_BIT_SHIFT),
//
//          SEVEN + (SPADE << FLUSH_BIT_SHIFT), SEVEN + (HEART << FLUSH_BIT_SHIFT),
//          SEVEN + (DIAMOND << FLUSH_BIT_SHIFT), SEVEN + (CLUB << FLUSH_BIT_SHIFT),
//
//          SIX + (SPADE << FLUSH_BIT_SHIFT), SIX + (HEART << FLUSH_BIT_SHIFT),
//          SIX + (DIAMOND << FLUSH_BIT_SHIFT), SIX + (CLUB << FLUSH_BIT_SHIFT),
//
//          FIVE + (SPADE << FLUSH_BIT_SHIFT), FIVE + (HEART << FLUSH_BIT_SHIFT),
//          FIVE + (DIAMOND << FLUSH_BIT_SHIFT), FIVE + (CLUB << FLUSH_BIT_SHIFT),
//
//          FOUR + (SPADE << FLUSH_BIT_SHIFT), FOUR + (HEART << FLUSH_BIT_SHIFT),
//          FOUR + (DIAMOND << FLUSH_BIT_SHIFT), FOUR + (CLUB << FLUSH_BIT_SHIFT),
//
//          THREE + (SPADE << FLUSH_BIT_SHIFT), THREE + (HEART << FLUSH_BIT_SHIFT),
//          THREE + (DIAMOND << FLUSH_BIT_SHIFT), THREE + (CLUB << FLUSH_BIT_SHIFT),
//
//          TWO + (SPADE << FLUSH_BIT_SHIFT), TWO + (HEART << FLUSH_BIT_SHIFT),
//          TWO + (DIAMOND << FLUSH_BIT_SHIFT), TWO + (CLUB << FLUSH_BIT_SHIFT)};
//
//  private static final int[] tmp =
//      {1479181, 636345, 262349, 83661, 22854, 8698, 2031, 453, 98, 22, 5, 1, 0};
//  private static final int[] tmp2 = {SPADE << FLUSH_BIT_SHIFT, HEART << FLUSH_BIT_SHIFT,
//      DIAMOND << FLUSH_BIT_SHIFT, CLUB << FLUSH_BIT_SHIFT};
//
//  public static final int[] card2 = new int[52];
//  public static final int[] suit2 = new int[52];
//  public static final int[][] suit_kronecker2 = new int[4][];
//
//
//  public static void main(String[] args) {
//    System.out.println(Arrays.toString(card2));
//    System.out.println(Arrays.toString(suit));
//    System.out.println(suit.length);
//
//  }
//
//  public static final int[] suit = {INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB,
//      INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND,
//      INDEX_CLUB, INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE, INDEX_HEART,
//      INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE,
//      INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB,
//      INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND,
//      INDEX_CLUB, INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE, INDEX_HEART,
//      INDEX_DIAMOND, INDEX_CLUB, INDEX_SPADE, INDEX_HEART, INDEX_DIAMOND, INDEX_CLUB};
//
//  public static final int[][] suit_kronecker = {
//      {ACE_FLUSH, 0, 0, 0, KING_FLUSH, 0, 0, 0, QUEEN_FLUSH, 0, 0, 0, JACK_FLUSH, 0, 0, 0,
//          TEN_FLUSH, 0, 0, 0, NINE_FLUSH, 0, 0, 0, EIGHT_FLUSH, 0, 0, 0, SEVEN_FLUSH, 0, 0, 0,
//          SIX_FLUSH, 0, 0, 0, FIVE_FLUSH, 0, 0, 0, FOUR_FLUSH, 0, 0, 0, THREE_FLUSH, 0, 0, 0,
//          TWO_FLUSH, 0, 0, 0},
//      {0, ACE_FLUSH, 0, 0, 0, KING_FLUSH, 0, 0, 0, QUEEN_FLUSH, 0, 0, 0, JACK_FLUSH, 0, 0, 0,
//          TEN_FLUSH, 0, 0, 0, NINE_FLUSH, 0, 0, 0, EIGHT_FLUSH, 0, 0, 0, SEVEN_FLUSH, 0, 0, 0,
//          SIX_FLUSH, 0, 0, 0, FIVE_FLUSH, 0, 0, 0, FOUR_FLUSH, 0, 0, 0, THREE_FLUSH, 0, 0, 0,
//          TWO_FLUSH, 0, 0},
//      {0, 0, ACE_FLUSH, 0, 0, 0, KING_FLUSH, 0, 0, 0, QUEEN_FLUSH, 0, 0, 0, JACK_FLUSH, 0, 0, 0,
//          TEN_FLUSH, 0, 0, 0, NINE_FLUSH, 0, 0, 0, EIGHT_FLUSH, 0, 0, 0, SEVEN_FLUSH, 0, 0, 0,
//          SIX_FLUSH, 0, 0, 0, FIVE_FLUSH, 0, 0, 0, FOUR_FLUSH, 0, 0, 0, THREE_FLUSH, 0, 0, 0,
//          TWO_FLUSH, 0},
//      {0, 0, 0, ACE_FLUSH, 0, 0, 0, KING_FLUSH, 0, 0, 0, QUEEN_FLUSH, 0, 0, 0, JACK_FLUSH, 0, 0, 0,
//          TEN_FLUSH, 0, 0, 0, NINE_FLUSH, 0, 0, 0, EIGHT_FLUSH, 0, 0, 0, SEVEN_FLUSH, 0, 0, 0,
//          SIX_FLUSH, 0, 0, 0, FIVE_FLUSH, 0, 0, 0, FOUR_FLUSH, 0, 0, 0, THREE_FLUSH, 0, 0, 0,
//          TWO_FLUSH}};
//  
//  static {
//    int index = 0;
//    for (int r = 0; r < 13; r++) {
//      int suit = tmp[r];
//      for (int s = 0; s < 4; s++) {
//        card2[index]=suit+tmp2[s];
//        suit2[index] = index % 4;
//        AssertUtils.test(card2[index] == card[index]);
//        AssertUtils.test(suit2[index] == SKPokerEvalConstant2.suit[index]);
//
//        index++;
//      }
//    }
//    for (int s = 0; s < 4; s++) {
//      suit_kronecker2[s] = new int[52];
//      for (int i = 0; i < 52; i++) {
//        if (i % 4 == s) {
//          suit_kronecker2[s][i] = 1 << (12 - (i / 4));
//        }
//        AssertUtils.test(suit_kronecker2[s][i] == suit_kronecker[s][i], suit_kronecker2[s][i], suit_kronecker[s][i], s, i);
//      }
//    }
//  }
//
//}
