package com.wala.poker.model.object;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Card;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.object.enumeration.HandType;
import com.codingame.model.utils.CardUtils;

public class FiveCardHandTest {

  private static final Logger logger = LoggerFactory.getLogger(FiveCardHandTest.class);

  @Test
  public void testHandType() {
    FiveCardHand hand = createHand("2D 4D 3D 5D 6D");
    assertEquals(HandType.STRAIGHT_FLUSH, hand.getHandType());
    
    hand = createHand("2D 4D 3D 5D AD");
    assertEquals(HandType.STRAIGHT_FLUSH, hand.getHandType());
    
    hand = createHand("2D 4D 3D 5S 6D");
    assertEquals(HandType.STRAIGHT, hand.getHandType());
    
    hand = createHand("2D 4D 3D QD 6D");
    assertEquals(HandType.FLUSH, hand.getHandType());
    
    hand = createHand("2D 2S 3D 2C 2H");
    assertEquals(HandType.FOUR_OF_A_KIND, hand.getHandType());
    
    hand = createHand("2D QS 3D 2C 2H");
    assertEquals(HandType.THREE_OF_A_KIND, hand.getHandType());
    
    hand = createHand("2D 3S 3D 2C 2H");
    assertEquals(HandType.FULL_HOUSE, hand.getHandType());
    
    hand = createHand("2D 3S 3D JC 2H");
    assertEquals(HandType.TWO_PAIR, hand.getHandType());
    
    hand = createHand("2D 3S 3D JC 5H");
    assertEquals(HandType.PAIR, hand.getHandType());
    
    hand = createHand("2D KS 3D JC 5H");
    assertEquals(HandType.HIGH_CARD, hand.getHandType());
  }
  
  private FiveCardHand createHand(String cards) {
    List<Card> tmp = new ArrayList<>();
    for (String card : cards.split(" ")) {
      tmp.add(CardUtils.fromString(card));
    }
    FiveCardHand ret = new FiveCardHand(tmp);
    logger.info("{} {}", ret, ret.getLabel());
    return ret;
  }

}
