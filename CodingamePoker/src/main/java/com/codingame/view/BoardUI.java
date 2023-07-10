package com.codingame.view;

import java.util.HashSet;
import java.util.Set;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Text.FontWeight;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.model.object.Board;
import com.codingame.model.object.Card;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.object.PlayerModel;
import com.codingame.view.object.Game;
import com.codingame.view.object.Point;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BoardUI {

  @Inject
  private Game graphics;
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
    Board board = graphics.getBoard();
    if (playerUIS == null) {
      playerUIS = new PlayerUI[board.getPlayerNb()];
      for (int i = 0; i < playerUIS.length; ++i) {
        playerUIS[i] = new PlayerUI(graphics.getGameManager().getPlayer(i), graphics);
      }

      levelGroup = graphics.getGraphics().createGroup();
      blinds = graphics.createText();
      createLabelAndText(blinds, ViewConstant.BLIND_X, ViewConstant.BLIND_Y, "BLINDS");

      level = graphics.createText();
      createLabelAndText(level, ViewConstant.LEVEL_X, ViewConstant.LEVEL_Y, "LEVEL");

      gameNb = graphics.createText();
      createLabelAndText(gameNb, ViewConstant.GAME_NB_X, ViewConstant.GAME_NB_Y, "HAND");

      potGroup = graphics.getGraphics().createGroup();
      pot = graphics.createText();
      createPot(pot, ViewConstant.POT_X, ViewConstant.POT_Y);
      pot.setTextAlign(TextAlign.CENTER);

      button = graphics.getGraphics().createGroup();
      Circle buttonCircle = graphics.getGraphics()
        .createCircle()
        .setFillColor(ViewConstant.BUTTON_FILL_COLOR)
        .setLineWidth(8)
        .setLineColor(ViewConstant.BUTTON_WRITE_COLOR)
        .setRadius(ViewConstant.BUTTON_RADIUS);
      button.add(buttonCircle);
      Text buttonText = graphics.getGraphics()
        .createText("D")
        .setX(-16)
        .setY(-25)
        .setFontSize(40)
        .setFontWeight(FontWeight.BOLDER)
        .setFontFamily(ViewConstant.FONT)
        .setTextAlign(TextAlign.RIGHT)
        .setFillColor(ViewConstant.BUTTON_WRITE_COLOR);
      graphics.getTooltips().setTooltipText(button, "DEALER");
      button.add(buttonText);
      updateButton(board);
    }
  }

  private void createLabelAndText(Text text, int x, int y, String label) {
    Text labeltext = graphics.createText();
    ViewUtils.createTextRectangle(labeltext, x, y, ViewConstant.LABEL_WIDTH, true, graphics,
        levelGroup);
    labeltext.setText(label);
    ViewUtils.createTextRectangle(text, x + ViewConstant.LABEL_WIDTH, y,
        ViewConstant.LABEL_TEXT_WIDTH, false, graphics, levelGroup);
  }

  private void createPot(Text text, int x, int y) {
    Text labeltext = graphics.createText();
    ViewUtils.createTextRectangle(labeltext, x, y, ViewConstant.POT_LABEL_WIDTH, true, graphics,
        potGroup);
    labeltext.setText("POT");
    ViewUtils.createTextRectangle(text, x + ViewConstant.POT_LABEL_WIDTH, y, ViewConstant.POT_WIDTH,
        false, graphics, potGroup);
  }


  public void update() {
    Board board = graphics.getBoard();

    ViewUtils.updateText(graphics, level, Integer.toString(board.getLevel()));
    ViewUtils.updateText(graphics, blinds,
        "$ " + board.getSmallBlind() + " / " + board.getBigBlind());
    ViewUtils.updateText(graphics, gameNb, Integer.toString(board.getGameNb()));
    for (int id = 0; id < playerUIS.length; id++) {
      PlayerUI playerUI = playerUIS[id];
      playerUI.setEliminated(graphics);
    }
    updateButton(board);

    deckUI.reset(graphics);

    deckUI.deal(graphics);

    // TODO
    // graphics.setEndTime();;
    for (int id = 0; id < playerUIS.length; id++) {
      PlayerUI playerUI = playerUIS[id];
      PlayerModel player = board.getPlayer(id);
      // TODO put if in method
      if (!playerUI.isFolded() && player.isFolded()) {
        deckUI.foldPlayerId(board, id);
      }
      playerUI.update(graphics);
    }
    if (graphics.isEnd()) {
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
    ViewUtils.updateText(graphics, pot, "$ " + board.getPot(),
        potOver.substring(0, potOver.length() - 1));

    graphics.commitWorldState();

  }

  public void resetHand() {
    deckUI.reset(graphics);

  }

  private void updateButton(Board board) {
    Point point = ViewUtils.getPlayerUICoordinates(board, board.getDealerId()).getButton();
    button.setX(point.getX()).setY(point.getY());
  }

  private void highlightWinningCards() {
    Board board = graphics.getBoard();
    if (!board.getBestPlayers().isEmpty()) {
      // for (PlayerUI player : playerUIS) {
      // player.setLose(graphics);
      // }
      Set<Card> winCards = new HashSet<>();
      // deckUI.lowlightCards(graphics);
      for (PlayerModel player : board.getPlayers()) {
        boolean win = board.getBestPlayers().contains(player.getId());
        if (win) {
          FiveCardHand bestHand = player.getBestPossibleHand();
          for (Card card : bestHand.getCards()) {
            winCards.add(card);
            // deckUI.highlightCard(graphics, card, true);
          }
        }
        playerUIS[player.getId()].setWinOrLose(graphics, win);

      }
      for (PlayerModel player : board.getPlayers()) {
        if (!player.isEliminated()) {
          for (Card card : player.getHand().getCards()) {
            deckUI.highlightCard(graphics, card, winCards.contains(card));
          }
        }
      }
      for (Card card : board.getBoardCards()) {
        deckUI.highlightCard(graphics, card, winCards.contains(card));
      }
      graphics.commitWorldState();
    }
  }
}
