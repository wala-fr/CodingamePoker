package com.wala.poker.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import com.codingame.model.object.Card;
import com.codingame.model.utils.CardUtils;
import com.codingame.model.variable.Parameter;
import com.wala.poker.model.variable.TestParameter;

public class TestUtils {

  public static void initParameter() {
    Parameter.BIG_BLINB = TestParameter.BIG_BLINB;
    Parameter.SMALL_BLINB = TestParameter.SMALL_BLINB;
  }

  public static List<Card> createDeckCards(int dealerId, int playerNb, List<Card> boardCards,
      List<List<Card>> playerCards) {
    List<Card> cards = new ArrayList<>();
    for (int cardIdx = 0; cardIdx < 2; cardIdx++) {
      for (int i = 0; i < playerNb; i++) {
        int playerIdx = dealerId + 1 + i;
        playerIdx %= playerNb;
        Card c = playerCards.get(playerIdx).get(cardIdx);
        if (c != CardUtils.BURNED) {
          cards.add(c);
        }
      }
    }
    Card burnedCard = CardUtils.BURNED;
    cards.add(burnedCard);
    for (int i = 0; i < 5; i++) {
      if (i > 2) {
        cards.add(burnedCard);
      }
      cards.add(boardCards.get(i));
    }
    testValidDeck(cards);

    return cards;
  }

  // TO TEST THAT THE BOARD HAS NO DUPLICATE CARDS
  public static void testValidDeck(List<Card> cards) {
    Set<Card> set = new HashSet<>();
    for (Card card : cards) {
      if (card != CardUtils.BURNED && !set.add(card)) {
        throw new IllegalStateException();
      }
    }
  }

  public static int calculateLastInt(Path p) {
    String s = p.toString().replace(".txt", "");
    int i = s.length();
    while (i > 0 && Character.isDigit(s.charAt(i - 1))) {
      i--;
    }
    if (i == s.length()) {
      return 0;
    }
    return Integer.parseInt(s.substring(i));
  }

  public static Stream<Arguments> extractFiles(String pathStr) throws IOException {
    List<Arguments> arguments = new ArrayList<>();
    Path root = Paths.get(pathStr);
    Comparator<Path> comparator = Comparator.<Path, String>comparing(p -> p.getParent().toString())
        .thenComparingInt(p -> TestUtils.calculateLastInt(p));
    Files.walk(root).filter(p -> p.getFileName().toString().endsWith(".txt")).sorted(comparator)
        .forEach(p -> arguments.add(Arguments.of(p)));
    return arguments.stream();
  }
}
