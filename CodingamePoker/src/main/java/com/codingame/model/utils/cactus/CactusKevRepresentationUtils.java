package com.codingame.model.utils.cactus;

public class CactusKevRepresentationUtils {

  private static final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41};

  public static int calculateCactusKevCardRepresentation(int rank, int suit) {
    // Cactus Kev Card Representation
    // +--------+--------+--------+--------+
    // |xxxbbbbb|bbbbbbbb|cdhsrrrr|xxpppppp|
    // +--------+--------+--------+--------+
    // p = prime number of rank (two = 2, three = 3, four = 5, five = 7,..., ace = 41)
    // r = rank of card (two = 0, three = 1, four = 2, five = 3,..., ace = 12)
    // cdhs = suit of card
    // b = bit turned on depending on rank of card
    return primes[rank] | (rank << 8) | (1 << (suit + 12)) | (1 << (16 + rank));
  }
}
