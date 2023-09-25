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
    doAssert(HandType.STRAIGHT_FLUSH, "2D 4D 3D 5D 6D");
    
    doAssert(HandType.STRAIGHT_FLUSH, "2D 4D 3D 5D AD");
    
    doAssert(HandType.STRAIGHT, "2D 4D 3D 5S 6D");
    
    doAssert(HandType.FLUSH, "2D 4D 3D QD 6D");
    
    doAssert(HandType.FOUR_OF_A_KIND, "2D 2S 3D 2C 2H");
    
    doAssert(HandType.THREE_OF_A_KIND, "2D QS 3D 2C 2H");
    
    doAssert(HandType.FULL_HOUSE, "2D 3S 3D 2C 2H");
    
    doAssert(HandType.TWO_PAIR, "2D 3S 3D JC 2H");
    
    doAssert(HandType.PAIR, "2D 3S 3D JC 5H");
    
    doAssert(HandType.HIGH_CARD, "2D KS 3D JC 5H");
  }
  
  private void doAssert(HandType handType, String handStr) {
    FiveCardHand hand = createHand(handStr);
    System.out.println("###########");
    System.out.println(hand.getLabel());
    System.out.println(hand.getShortLabel());

    assertEquals(handType, hand.getHandType());
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
