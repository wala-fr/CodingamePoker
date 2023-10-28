package com.codingame.win_percent.skeval.table;

import com.codingame.win_percent.skeval.table.offset.RankOffsets1;
import com.codingame.win_percent.skeval.table.offset.RankOffsets2;

public class RankOffsets {
  public static final int[] TABLE = new int[16384];
  private static final int[][] tables = {RankOffsets1.table,RankOffsets2.table};

  static {
    int index = 0;
    for (int i = 0; i < tables.length; i++) {
      int[] t = tables[i];
      System.arraycopy(t, 0, TABLE, index, t.length);
      index += t.length;
    }
  }
}
