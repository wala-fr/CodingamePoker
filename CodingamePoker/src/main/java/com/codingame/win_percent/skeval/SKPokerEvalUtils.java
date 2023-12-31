package com.codingame.win_percent.skeval;

import static com.codingame.win_percent.skeval.SKPokerEvalConstant.CARDS;
import static com.codingame.win_percent.skeval.SKPokerEvalConstant.FACE_BIT_MASK;
import static com.codingame.win_percent.skeval.SKPokerEvalConstant.FLUSH_BIT_SHIFT;
import static com.codingame.win_percent.skeval.SKPokerEvalConstant.NOT_A_SUIT;
import static com.codingame.win_percent.skeval.SKPokerEvalConstant.RANK_HASH_MOD;
import static com.codingame.win_percent.skeval.SKPokerEvalConstant.RANK_OFFSET_SHIFT;
import static com.codingame.win_percent.skeval.SKPokerEvalConstant.SUIT_KRONECKER;
import com.codingame.win_percent.skeval.table.FlushCheck;
import com.codingame.win_percent.skeval.table.FlushRanks;
import com.codingame.win_percent.skeval.table.RankHash;
import com.codingame.win_percent.skeval.table.RankOffsets;

public class SKPokerEvalUtils {

  public static int calculateBestPossibleScore(final int[] cards) {
    int key = CARDS[cards[0]] + CARDS[cards[1]] + CARDS[cards[2]] + CARDS[cards[3]] + CARDS[cards[4]]
        + CARDS[cards[5]] + CARDS[cards[6]];

    int suit = FlushCheck.TABLE[key >>> FLUSH_BIT_SHIFT];
    if (NOT_A_SUIT != suit) {
      int[] s = SUIT_KRONECKER[suit];
      return FlushRanks.TABLE[s[cards[0]] | s[cards[1]] | s[cards[2]] | s[cards[3]] | s[cards[4]]
          | s[cards[5]] | s[cards[6]]];
    }
    int hash = FACE_BIT_MASK & (31 * key);
    return RankHash.TABLE[RankOffsets.TABLE[hash >>> RANK_OFFSET_SHIFT]
        + (hash & RANK_HASH_MOD)];
  }
  
}
