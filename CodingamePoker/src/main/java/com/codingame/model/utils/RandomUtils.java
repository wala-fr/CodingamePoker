package com.codingame.model.utils;

import java.util.Random;

public class RandomUtils {

  public static Random RANDOM = new Random(0);
  
  public static void init(Random rand) {
    RANDOM = rand;
  }
  
  public static int nextInt(int n) {
    return RANDOM.nextInt(n);
  }
  
} 
