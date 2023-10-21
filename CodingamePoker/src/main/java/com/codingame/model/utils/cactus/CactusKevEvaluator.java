package com.codingame.model.utils.cactus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.utils.cactus.table.Flushes;
import com.codingame.model.utils.cactus.table.Products;
import com.codingame.model.utils.cactus.table.Unique;
import com.codingame.model.utils.cactus.table.Values;

public class CactusKevEvaluator {

  private static final Logger logger = LoggerFactory.getLogger(CactusKevEvaluator.class);

  private static final CactusKevEvaluator instance = new CactusKevEvaluator();

  static CactusKevEvaluator getInstance() {
    return instance;
  }

  int calculateHandRank(int[] hand) {
    return calculateHandRank(hand[0], hand[1], hand[2], hand[3], hand[4]);
  }

  int calculateHandRank(int c1, int c2, int c3, int c4, int c5) {
    int q = (c1 | c2 | c3 | c4 | c5) >> 16;
    // check for Flushes and Straight Flushes
    if ((c1 & c2 & c3 & c4 & c5 & 0xF000) != 0) {
      return Flushes.table[q];
    }
    // check for Straights and High Card hands
    int s = Unique.table[q];
    if (s != 0) {
      return s;
    }
    q = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
    return Values.table[getIndex(q)];
  }

  private int getIndex(int key) {
    int low = -1;
    int high = 4888;
    int pivot;
    while (high - low > 1) {
      pivot = (low + high) >>> 1;
      int prod = Products.table[pivot];
      if (prod > key) {
        high = pivot;
      } else if (prod < key) {
        low = pivot;
      } else {
        return pivot;
      }
    }
    return -1;
  }

}
