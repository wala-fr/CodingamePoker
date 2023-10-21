package com.codingame.model.utils.skeval.table;

import com.codingame.model.utils.skeval.table.hash.RankHash1;
import com.codingame.model.utils.skeval.table.hash.RankHash2;
import com.codingame.model.utils.skeval.table.hash.RankHash3;
import com.codingame.model.utils.skeval.table.hash.RankHash4;
import com.codingame.model.utils.skeval.table.hash.RankHash5;
import com.codingame.model.utils.skeval.table.hash.RankHash6;

public class RankHash {
  public static final short[] TABLE = new short[42077];
  private static final short[][] tables = {RankHash1.table,RankHash2.table,RankHash3.table,RankHash4.table,RankHash5.table,RankHash6.table};

  static {
    int index = 0;
    for (int i = 0; i < tables.length; i++) {
      short[] t = tables[i];
      System.arraycopy(t, 0, TABLE, index, t.length);
      index += t.length;
    }
  }
  
}
