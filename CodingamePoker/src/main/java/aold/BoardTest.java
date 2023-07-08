//package aold;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Stream;
//import org.junit.Test;
//import org.mockito.internal.util.reflection.FieldSetter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.wala.poker.model.object.enumeration.HandType;
//import com.wala.poker.model.utils.CardUtils;
//
//public class BoardTest {
//
//  private static final Logger logger = LoggerFactory.getLogger(BoardTest.class);
//
////  @Test
////  public void test1() {
////    doAssertion("8D 7C 7D 6C KS 9D 5C 3S 2D", 0, HandType.HIGH_CARD, HandType.HIGH_CARD);
////    doAssertion("7C 8D 7D 6C 9D 5C KS 3S 2D", 0, HandType.HIGH_CARD, HandType.HIGH_CARD);
////  }
////
////  @Test
////  public void test2() {
////    doAssertion("AD 7C AC 6D 9D 8C KS 3S 2D", 0, HandType.HIGH_CARD, HandType.HIGH_CARD);
////    doAssertion("QC 6D QD 7C 4D 8C KS TS 2D", 1, HandType.HIGH_CARD, HandType.HIGH_CARD);
////
////    doAssertion("6C 6D QD 7C 4D 8C KS TS 2D", 0, HandType.PAIR, HandType.HIGH_CARD);
////
////  }
////
////  @Test
////  public void test3() {
////    doAssertion("2D 3D AC AD 9D 6S KD 4D 2S", 0, HandType.FLUSH, HandType.PAIR);
////    doAssertion("2C JD 3C 4C 2S 9C 6C KC 4D", 1, HandType.PAIR, HandType.FLUSH);
////
////    doAssertion("2D 6D 5D AD 9D 6S KD 4D 2S", 1, HandType.FLUSH, HandType.FLUSH);
////
////  }
////
////  @Test
////  public void test4() {
////    doAssertion("2D 3D 7D KD 9D 6S AD 4D 5D", 0, HandType.STRAIGHT_FLUSH, HandType.FLUSH);
////    doAssertion("3H 7D 2H 3D 9D 6S AD 4S 5D", 0, HandType.STRAIGHT, HandType.STRAIGHT);
////
////    doAssertion("3H 7D 4H 5S 9D 6S AD 4S 5D", 0, HandType.STRAIGHT, HandType.TWO_PAIR);
////
////  }
////
////  @Test
////  public void test5() {
////    doAssertion("5H 5S 6D 5D 9C 6C AH 6H 5C", 1, HandType.FULL_HOUSE, HandType.FULL_HOUSE);
////    doAssertion("6D 5D JH KS 9C 6C AH 6H 5C", 0, HandType.FULL_HOUSE, HandType.HIGH_CARD);
////  }
////
////  @Test
////  public void test6() {
////    doAssertion("5H 5S 6D 6H 2C AC AH 2H 9C", 1, HandType.TWO_PAIR, HandType.TWO_PAIR);
////    doAssertion("9H TS 2D AH 2C AC 6H TH 9C", 1, HandType.TWO_PAIR, HandType.TWO_PAIR);
////  }
////
////  @Test
////  public void test7() {
////    doAssertion("5H 2D 6D QH 5C 5S AH 5D 9C", 0, HandType.FOUR_OF_A_KIND, HandType.PAIR);
////    doAssertion("5H 2D 6D QH 5C 5S AH 5D QC", 0, HandType.FOUR_OF_A_KIND, HandType.TWO_PAIR);
////    doAssertion("5H 2D QD QH 5C 5S AH 5D QC", 0, HandType.FOUR_OF_A_KIND, HandType.FULL_HOUSE);
////
////    doAssertion("6D TH 5H 2D 5C 5S AH QD 9C", 1, HandType.PAIR, HandType.THREE_OF_A_KIND);
////    doAssertion("6D 6H 5H 2D 5C 5S AH QD 9C", 1, HandType.TWO_PAIR, HandType.THREE_OF_A_KIND);
////
////  }
////
////  @Test
////  public void testDraw() {
////    doAssertion("5H 2D 8D 5D 5C 5S AS QD 9C", -1, HandType.THREE_OF_A_KIND,
////        HandType.THREE_OF_A_KIND);
////    doAssertion("QC 2D 6H QH TS 9C KC 4C 7D", -1, HandType.HIGH_CARD, HandType.HIGH_CARD);
////
////    doAssertion("8D 7C 7D 6C KS 9D 5C 3S 2D", 0, HandType.HIGH_CARD, HandType.HIGH_CARD);
////    doAssertion("7C 8D 7D 6C 9D 5C KS 3S 2D", 0, HandType.HIGH_CARD, HandType.HIGH_CARD);
////  }
//
////  private void doAssertion(String cards, int winner, HandType... handTypes) {
////    Board board = createBoard(cards);
////    logger.info("{}", board);
////    List<Player> players = board.findWinner();
////    for (Player player : players) {
////      logger.info("winner {}, bestHand :{}", player.getId(), player.getBestPossibleHand());
////      assertEquals(handTypes[player.getId()], player.getBestPossibleHand().getHandType());
////
////    }
////    if (winner != -1) {
////      Player player = players.get(0);
////      assertEquals(winner, player.getId());
////    } else {
////      assertEquals(2, players.size());
////    }
////
////  }
//
//  @Test
//  public void test() throws IOException {
//    Path root = Paths.get("src\\test\\resources\\bestHand");
//    Files.walk(root).filter(p -> p.getFileName().toString().endsWith(".txt"))
//        .forEach(p -> testFile(p));;
//  }
//
//  private void testFile(Path path) {
//    BoardInput input = new BoardInput(path);
//    input.doAssertions();
//  }
//
////  private Board createBoard(String s) {
////    List<Card> cards = createDeckCards(s);
////    logger.debug("cards {}", cards);
////
////    int playerNb = (cards.size() - 8) / 2;
////    logger.debug("playerNb {}", playerNb);
////
////    Board ret = new Board(playerNb);
////    ret.getDeck().initCards(cards);
////
////    // so the small blind is player 0
////    ret.setDealerId(playerNb - 1);
////    ret.dealFirst();
////    ret.dealFlop();
////    ret.dealOneBoardCard();
////    ret.dealRiver();
////    return ret;
////  }
//
////  private List<Card> createDeckCards(String s) {
////    String[] cardStrs = s.split(" ");
////    int playerNb = (cardStrs.length - 5) / 2;
////    List<Card> cards = new ArrayList<>();
////    for (int i = 0; i < playerNb; i++) {
////      cards.add(CardUtils.fromString(cardStrs[2 * i]));
////    }
////    for (int i = 0; i < playerNb; i++) {
////      cards.add(CardUtils.fromString(cardStrs[2 * i + 1]));
////    }
////    Card burnedCard = CardUtils.fromString("2H");
////    cards.add(burnedCard);
////    for (int i = 0; i < 5; i++) {
////      if (i > 2) {
////        cards.add(burnedCard);
////      }
////      cards.add(CardUtils.fromString(cardStrs[2 * playerNb + i]));
////    }
////    return cards;
////  }
//  
//}
