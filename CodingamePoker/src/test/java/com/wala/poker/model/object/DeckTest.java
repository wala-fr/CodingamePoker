package com.wala.poker.model.object;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Deck;

public class DeckTest {

  private static final Logger logger = LoggerFactory.getLogger(DeckTest.class);

  @Test
  public void test() {
    Deck deck = new Deck();
    deck.reset();
    logger.info("{}", deck);
    deck.cut();
    logger.info("{}", deck);

  }

}
