package com.wala.poker.model.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.HandType;
import com.codingame.model.utils.CardUtils;
import com.wala.poker.model.TestUtils;

public class BoardInput {

  private static final Logger logger = LoggerFactory.getLogger(BoardInput.class);

  private String name;
  private Path path;

  private List<Card> boardCards;
  private int playerNb;
  private List<Integer> winners;
  private List<List<Card>> playerCards = new ArrayList<>();
  private List<HandType> playerHandtypes = new ArrayList<>();
  private Board board;

  public BoardInput(Path path) {
    try {
      this.path = path;
      List<String> inputs = Files.readAllLines(path);
      int index = 0;
      name = inputs.get(index++);
      logger.info("#### test {} {}", name, path);
      boardCards = CardUtils.calculateHandFromString(inputs.get(index++));
      playerNb = Integer.parseInt(inputs.get(index++));
      for (int i = 0; i < playerNb; i++) {
        playerCards.add(CardUtils.calculateHandFromString(inputs.get(index++)));
        playerHandtypes.add(HandType.valueOf(inputs.get(index++)));
      }
      winners = Arrays.stream(inputs.get(index++).split(" ")).map(i -> Integer.parseInt(i))
          .collect(Collectors.toList());
      createBoard();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public void doAssertions() {
    logger.debug("{} {}", name, board);
    board.initPlayerBestHands();
    List<Integer> playerIds = board.findWinner();
    for (PlayerModel player : board.getPlayers()) {
      logger.info("{}, bestHand :{}", player.getId(), player.getBestPossibleHand());
      assertEquals(playerHandtypes.get(player.getId()), player.getBestPossibleHand().getHandType());
    }
    logger.info("winners {}", winners);
    assertEquals(winners.size(), playerIds.size());
    for (Integer playerId : playerIds) {
      assertTrue(winners.contains(playerId));
    }
  }

  private void createBoard() {
    int dealerId = playerNb - 1;
    List<Card> cards = TestUtils.createDeckCards(dealerId, playerNb, boardCards, playerCards);
    logger.debug("cards {}", cards);
    logger.debug("playerNb {}", playerNb);
    int bbId = playerNb == 2 ? 0 : 1;
    board = new Board(playerNb, bbId);
    board.resetHand(false);
    logger.debug("dealerId {}", board.getDealerId());
    logger.debug("bbId {}", board.getBbId());
    logger.debug("sbId {}", board.getSbId());

    board.getDeck().initCards(cards);

//    // so the small blind is player 0
//    board.setDealerId(dealerId);
    board.dealFirst();
    board.dealBoardCards();
    board.dealBoardCards();
    board.dealBoardCards();
  }

}
