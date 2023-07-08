package com.wala.poker.model.simulation;

import static org.junit.Assert.*;
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
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Board;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.enumeration.HandType;
import com.codingame.model.utils.ActionUtils;
import com.codingame.model.utils.CardUtils;
import com.wala.poker.model.TestUtils;

public class SimulationInput {

  private static final Logger logger = LoggerFactory.getLogger(SimulationInput.class);

  private String name;
  private Path path;

  private List<Card> boardCards;
  private int playerNb;
  private int dealerId;

  private List<SimulationPlayer> players = new ArrayList<>();
  private List<ActionInput> actions = new ArrayList<>();

  private Board board;

  private List<String> inputs;
  private int index = 0;

  public SimulationInput(Path path) {
    try {
      this.path = path;
      inputs = Files.readAllLines(path);
      index = 0;
      name = nextLine();
      logger.info("#### test {} {}", name, path);
      boardCards = CardUtils.calculateHandFromString(nextLine());
      playerNb = nextNumLast();
      for (int i = 0; i < playerNb; i++) {
        SimulationPlayer player = new SimulationPlayer();
        player.setCards(nextLine());
        player.setStartStack(nextLine());
        player.setEndStack(nextLine());
        players.add(player);
      }
      dealerId = nextNumLast();
      while (hasNext()) {
        String actionStr = nextLine();
        actions.add(new ActionInput(actionStr));
      }
      createBoard();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private int nextNumLast() {
    String str = nextLine();
    String[] tmp = str.split(" ");
    String num = tmp[tmp.length - 1];
    return Integer.parseInt(num);
  }

  private boolean hasNext() {
    for (int i = index; i < inputs.size(); i++) {
      String s = inputs.get(i).trim();
      if (!s.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private String nextLine() {
    String str;
    do {
      str = inputs.get(index++).trim();
    } while (str.isEmpty());
    return str;
  }

  public void executeActions() {
    List<Card> cards = createDeckCards();
    logger.debug("cards {}", cards);
    ActionTestUtils.initBoardTest(board, cards);
    logger.debug("deck {}", board.getDeck());
    logger.debug("dealerId {}", board.getDealerId());
    logger.debug("nextPlayerId {}", board.getNextPlayerId());
    logger.debug("{}", board.toPlayerStatesString());

    for (ActionInput actionInput : actions) {
      logger.debug("ACTION:::: {}", actionInput);
      assertFalse(board.isOver());
      assertEquals(actionInput.getPlayerId(), board.getNextPlayerId());
      ActionInfo actionInfo = actionInput.getActionInfo();
      ActionUtils.doAction(board, actionInfo);
      assertEquals(actionInput.getRealAction(), actionInfo.getAction());

      if (actionInfo.hasError()) {
        logger.debug("{}", actionInfo.getError());
        logger.debug("REAL ACTION:::: {}", actionInfo.getAction());
      }
      logger.debug("{}", board.toPlayerStatesString());
      ActionTestUtils.endTurn(board);
    }
    assertTrue(board.isOver());
    logger.debug("{}", board.toPlayerStatesString());
    for (PlayerModel player : board.getPlayers()) {
      assertEquals(players.get(player.getId()).getEndStack(), player.getStack());
    }
  }

  private void createBoard() {
    logger.debug("playerNb {}", playerNb);

    board = new Board(playerNb, dealerId);
    board.getPlayers().forEach(p -> p.setStack(players.get(p.getId()).getStartStack()));

  }

  private List<Card> createDeckCards() {
    List<List<Card>> playerCards = new ArrayList<>();
    players.forEach(p -> playerCards.add(p.getCards()));
    List<Card> cards = TestUtils.createDeckCards(dealerId, playerNb, boardCards, playerCards);
    return cards;
  }
}
