package com.codingame.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.Player;
import com.codingame.model.object.Card;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.CardUtils;

public class ShowDownInfo {

  private static final Logger logger = LoggerFactory.getLogger(ShowDownInfo.class);

  private int handNb;
  private List<Card> playerCards;
  private List<Card> boardCard;

  private boolean[] playerShow;
  private boolean[] playerEliminated;

  public ShowDownInfo(Board board) {
    this.handNb = board.getHandNb();
    playerCards = new ArrayList<>();
    playerEliminated = new boolean[board.getPlayerNb()];

    for (int i = 0; i < board.getPlayerNb(); i++) {
      PlayerModel player = board.getPlayer(i);
      playerEliminated[i] = player.isEliminated();
      for (Card card : player.getHand().getCards()) {
        playerCards.add(card);
      }
    }
  }

  public void update(Board board) {
    playerShow = new boolean[board.getPlayerNb()];
    int notFoldedPlayerNb = board.calculatNotFoldedPlayerNb();
    AssertUtils.test(notFoldedPlayerNb > 0);
    // if all players but one have folded => no cards is shown
    boolean showNone = notFoldedPlayerNb == 1;
    for (int i = 0; i < board.getPlayerNb(); i++) {
      PlayerModel player = board.getPlayer(i);
      logger.debug("{} ShowDownInfo {} isFolded {}", i, handNb, player.isFolded());
      playerShow[i] = !player.isFolded() && !showNone;
    }
    boardCard = new ArrayList<>(board.getBoardCards());
  }

  public String toInputLine(Player player) {
    logger.debug("sendInput {} playerShow {}", handNb, Arrays.toString(playerShow));
    List<Card> showDownCards = new ArrayList<>();
    for (int playerId = 0; playerId < playerShow.length; playerId++) {
      boolean show = playerShow[playerId] || playerId == player.getIndex();
      for (int cardNb = 0; cardNb < 2; cardNb++) {
        if (show) {
          showDownCards.add(playerCards.get(2 * playerId + cardNb));
          AssertUtils.test(!playerEliminated[playerId]);
        } else if (playerEliminated[playerId]) {
          showDownCards.add(CardUtils.ELIMINATED);
        } else {
          showDownCards.add(CardUtils.BURNED);
        }
      }
    }
    String str = String.format("%d %s %s", handNb, InputUtils.toInputLineBoardCards(boardCard),
        InputUtils.toInputLine(showDownCards));
    return str;
  }
}
