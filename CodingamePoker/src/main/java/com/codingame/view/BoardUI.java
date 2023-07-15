package com.codingame.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Text.FontWeight;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.model.object.Card;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.AssertUtils;
import com.codingame.view.object.Game;
import com.codingame.view.object.Point;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BoardUI {
  
  private static final Logger logger = LoggerFactory.getLogger(BoardUI.class);

  @Inject
  private Game game;
  @Inject
  private DeckUI deckUI;

  private Text level;
  private Text gameNb;
  private Text blinds;

  private Text pot;


  private PlayerUI[] playerUIS;
  private Group button;
  private Group levelGroup;
  private Group potGroup;

  public void init() {
    Board board = game.getBoard();
    if (playerUIS == null) {
      playerUIS = new PlayerUI[board.getPlayerNb()];
      for (int i = 0; i < playerUIS.length; ++i) {
        playerUIS[i] = new PlayerUI(game.getGameManager().getPlayer(i), game);
      }

      levelGroup = game.getGraphics().createGroup();
      blinds = game.createText();
      createLabelAndText(blinds, ViewConstant.BLIND_X, ViewConstant.BLIND_Y, "BLINDS");

      level = game.createText();
      createLabelAndText(level, ViewConstant.LEVEL_X, ViewConstant.LEVEL_Y, "LEVEL");

      gameNb = game.createText();
      createLabelAndText(gameNb, ViewConstant.GAME_NB_X, ViewConstant.GAME_NB_Y, "HAND");

      potGroup = game.getGraphics().createGroup();
      pot = game.createText();
      createPot(pot, ViewConstant.POT_X, ViewConstant.POT_Y);
      pot.setTextAlign(TextAlign.CENTER);

      button = game.getGraphics().createGroup();
      Circle buttonCircle = game.getGraphics()
        .createCircle()
        .setFillColor(ViewConstant.BUTTON_FILL_COLOR)
        .setLineWidth(8)
        .setLineColor(ViewConstant.BUTTON_WRITE_COLOR)
        .setRadius(ViewConstant.BUTTON_RADIUS);
      button.add(buttonCircle);
      Text buttonText = game.getGraphics()
        .createText("D")
        .setX(-16)
        .setY(-25)
        .setFontSize(40)
        .setFontWeight(FontWeight.BOLDER)
        .setFontFamily(ViewConstant.FONT)
        .setTextAlign(TextAlign.RIGHT)
        .setFillColor(ViewConstant.BUTTON_WRITE_COLOR);
      game.getTooltips().setTooltipText(button, "DEALER");
      button.add(buttonText);
      updateButton(board);
    }
  }

  private void createLabelAndText(Text text, int x, int y, String label) {
    Text labeltext = game.createText();
    ViewUtils.createTextRectangle(labeltext, x, y, ViewConstant.LABEL_WIDTH, true, game,
        levelGroup);
    labeltext.setText(label);
    ViewUtils.createTextRectangle(text, x + ViewConstant.LABEL_WIDTH, y,
        ViewConstant.LABEL_TEXT_WIDTH, false, game, levelGroup);
  }

  private void createPot(Text text, int x, int y) {
    Text labeltext = game.createText();
    ViewUtils.createTextRectangle(labeltext, x, y, ViewConstant.POT_LABEL_WIDTH, true, game,
        potGroup);
    labeltext.setText("POT");
    ViewUtils.createTextRectangle(text, x + ViewConstant.POT_LABEL_WIDTH, y, ViewConstant.POT_WIDTH,
        false, game, potGroup);
  }


  public void update() {
    Board board = game.getBoard();

    ViewUtils.updateText(game, level, Integer.toString(board.getLevel()));
    ViewUtils.updateText(game, blinds,
        "$ " + board.getSmallBlind() + " / " + board.getBigBlind());
    ViewUtils.updateText(game, gameNb, Integer.toString(board.getGameNb()));
    for (int id = 0; id < playerUIS.length; id++) {
      PlayerUI playerUI = playerUIS[id];
      playerUI.setEliminated(game);
    }
    updateButton(board);

    deckUI.reset(game);

    deckUI.deal(game);

    // TODO
    for (int id = 0; id < playerUIS.length; id++) {
      PlayerUI playerUI = playerUIS[id];
      PlayerModel player = board.getPlayer(id);
      // TODO put if in method
      if (!playerUI.isFolded() && player.isFolded()) {
        deckUI.foldPlayerId(board, id);
      }
      playerUI.update(game);
    }
    if (game.isEnd()) {
      highlightWinningCards();
    }

    String potOver = "";
    for (int id = 0; id < playerUIS.length; id++) {
      PlayerUI playerUI = playerUIS[id];
      PlayerModel player = board.getPlayer(id);
      if (!playerUI.isEliminated()) {
        potOver += player.getId() + " : $ " + player.getTotalBetAmount() + "\n";
      }
    }
    // System.err.println(potOver);
    ViewUtils.updateText(game, pot, "$ " + board.getPot(),
        potOver.substring(0, potOver.length() - 1));

    game.commitWorldState();

  }

  public void resetHand() {
    deckUI.reset(game);

  }

  private void updateButton(Board board) {
    Point point = ViewUtils.getPlayerUICoordinates(board, board.getDealerId()).getButton();
    button.setX(point.getX()).setY(point.getY());
  }

  private void highlightWinningCards() {
    Board board = game.getBoard();
    if (board.isOver()) {
      List<Integer> bestPlayers = board.findWinner();
      AssertUtils.test(!bestPlayers.isEmpty());
      logger.debug("{}", board.toPlayerStatesString());
      logger.debug("calculatNotFoldedPlayerNb {}", board.calculatNotFoldedPlayerNb());
      if (!bestPlayers.isEmpty() && board.getPlayer(bestPlayers.get(0)).getBestPossibleHand() != null) {
        Set<Card> winCards = new HashSet<>();
        for (PlayerModel player : board.getPlayers()) {
          boolean win = bestPlayers.contains(player.getId());
          if (win) {
            FiveCardHand bestHand = player.getBestPossibleHand();
            for (Card card : bestHand.getCards()) {
              winCards.add(card);
            }
          }
          playerUIS[player.getId()].setWinOrLose(game, win);
        }
        for (PlayerModel player : board.getPlayers()) {
          if (!player.isEliminated()) {
            for (Card card : player.getHand().getCards()) {
              deckUI.highlightCard(game, card, winCards.contains(card));
            }
          }
        }
        for (Card card : board.getBoardCards()) {
          deckUI.highlightCard(game, card, winCards.contains(card));
        }
        game.commitWorldState();
      }
    }
  }
}
