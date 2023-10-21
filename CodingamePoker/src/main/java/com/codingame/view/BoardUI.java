package com.codingame.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.RefereeParameter;
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
import com.codingame.model.utils.skeval.WinPercentUtils;
import com.codingame.view.object.Game;
import com.codingame.view.object.Point;
import com.codingame.view.parameter.Color;
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
  private Text percentWinTime;

  private Text pot;
  private Text tie;

  private PlayerUI[] playerUIS;

  private Group button;
  private Group actionButton;

  private Group levelGroup;
  private Group potGroup;
  private Group tieGroup;


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

      if (RefereeParameter.CALCULATE_WIN_PERCENT) {
        tieGroup = game.getGraphics().createGroup();
        tie = game.createText();
        createTie(tie, ViewConstant.TIE_X, ViewConstant.TIE_Y);
        tie.setTextAlign(TextAlign.CENTER);
        updateTie();
      }

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
      button.setZIndex(ViewConstant.Z_INDEX_BUTTON);

      actionButton = game.getGraphics().createGroup();
      createButton(actionButton, "A", "ACTION", ViewConstant.ACTION_BUTTON_FILL_COLOR,
          ViewConstant.Z_INDEX_ACTION_BUTTON);

      initPercentWintime();

      updateButtons(board);
    }
    deckUI.reset();
  }

  private void initPercentWintime() {
    if (RefereeParameter.SHOW_WIN_PERCENT_TIME) {
      percentWinTime = game.createText();
      percentWinTime.setX(ViewConstant.WIDTH - 300)
        .setY(20)
        .setZIndex(ViewConstant.Z_INDEX_BOARD)
        .setFontSize((int) (1.2 * ViewConstant.LABEL_FONT_SIZE))
        .setFontWeight(FontWeight.BOLD)
        .setFontFamily(ViewConstant.FONT)
        .setTextAlign(TextAlign.RIGHT)
        .setMaxWidth(200)
        .setFillColor(ViewConstant.LABEL_TEXT_COLOR);
    }
  }

  private void updatePercentWintime() {
    if (RefereeParameter.SHOW_WIN_PERCENT_TIME) {
      percentWinTime.setText(WinPercentUtils.getTotalTime() + " ms");
      game.getTooltips().setTooltipText(percentWinTime, "Time to calculate winning probability");
    }
  }

  private void createButton(Group button, String text, String toolTipText, int fillcolor,
      int zIndex) {
    Circle buttonCircle = game.getGraphics()
      .createCircle()
      .setFillColor(fillcolor)
      .setLineWidth(8)
      .setLineColor(ViewConstant.BUTTON_WRITE_COLOR)
      .setRadius(ViewConstant.BUTTON_RADIUS);
    button.add(buttonCircle);
    Text buttonText = game.getGraphics()
      .createText(text)
      .setX(-16)
      .setY(-25)
      .setFontSize(40)
      .setFontWeight(FontWeight.BOLDER)
      .setFontFamily(ViewConstant.FONT)
      .setTextAlign(TextAlign.RIGHT)
      .setFillColor(ViewConstant.BUTTON_WRITE_COLOR);
    game.getTooltips().setTooltipText(button, toolTipText);
    button.add(buttonText);
    button.setZIndex(zIndex);
  }

  private void createLabelAndText(Text text, int x, int y, String label) {
    Text labeltext = game.createText();
    ViewUtils.createTextRectangle(labeltext, x, y, ViewConstant.LABEL_WIDTH, Color.RED, game,
        levelGroup);
    labeltext.setText(label);
    ViewUtils.createTextRectangle(text, x + ViewConstant.LABEL_WIDTH, y,
        ViewConstant.LABEL_TEXT_WIDTH, Color.BLACK, game, levelGroup);
  }

  private void createPot(Text text, int x, int y) {
    Text labeltext = game.createText();
    ViewUtils.createTextRectangle(labeltext, x, y, ViewConstant.POT_LABEL_WIDTH,  Color.RED, game,
        potGroup);
    labeltext.setText("POT");
    ViewUtils.createTextRectangle(text, x + ViewConstant.POT_LABEL_WIDTH, y, ViewConstant.POT_WIDTH,
        Color.BLACK, game, potGroup);
  }

  private void createTie(Text text, int x, int y) {
    Text labeltext = game.createText();
    ViewUtils.createTextRectangle(labeltext, x, y, ViewConstant.TIE_LABEL_WIDTH,  Color.BLUE, game,
        tieGroup);
    labeltext.setText("TIE");
    ViewUtils.createTextRectangle(text, x + ViewConstant.TIE_LABEL_WIDTH, y, ViewConstant.TIE_WIDTH,
        Color.BLACK, game, tieGroup);
  }

  private void updateTie() {
    int split = (int) Math.round(WinPercentUtils.getSplitPercent());
    if (!RefereeParameter.CALCULATE_WIN_PERCENT || !game.getBoard().isCalculateWinChance() || split == 0) {
      tieGroup.setVisible(false);
    } else {
      tieGroup.setVisible(true);
      tie.setText(ViewUtils.addSpaceBefore(split +"%", 4));
    }
  }

  public void update() {
    Board board = game.getBoard();

    updatePercentWintime();
    ViewUtils.updateText(game, level, Integer.toString(board.getLevel()));
    ViewUtils.updateText(game, blinds, "$ " + board.getSmallBlind() + " / " + board.getBigBlind());
    ViewUtils.updateText(game, gameNb, Integer.toString(board.getHandNb()));
    updateTie();

    for (int id = 0; id < playerUIS.length; id++) {
      PlayerUI playerUI = playerUIS[id];
      playerUI.setEliminated(game);
    }

    deckUI.reset();

    updateButtons(board);

    deckUI.deal();

    // TODO
    for (int id = 0; id < playerUIS.length; id++) {
      PlayerUI playerUI = playerUIS[id];
      PlayerModel player = board.getPlayer(id);
      // TODO put if in method
      if (!playerUI.isFolded() && player.isFolded()) {
        deckUI.foldPlayerId(id);
      }
      playerUI.update(game);
    }
    game.commitWorldState();

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
    ViewUtils.updateText(game, pot, "$ " + board.getPot(),
        potOver.substring(0, potOver.length() - 1));

    game.commitWorldState();
  }

  public void resetHand() {
    deckUI.reset();
  }

  private void updateButtons(Board board) {
    if (!game.isDeal()) {
      return;
    }
    Point point = ViewUtils.getPlayerUICoordinates(board, board.getDealerId()).getButton();
    point.setPosition(button);
    point = board.getNextPlayerId() == -1 ? ViewUtils.getNoActionButtonPosition()
        : ViewUtils.getPlayerUICoordinates(board, board.getNextPlayerId()).getActionButton();
    point.setPosition(actionButton);
    game.commitEntityState(0.1, button, actionButton);
  }

  private void highlightWinningCards() {
    Board board = game.getBoard();
    if (board.isOver()) {
      List<Integer> bestPlayers = board.findWinner();
      AssertUtils.test(!bestPlayers.isEmpty());
      logger.debug("{}", board.toPlayerStatesString());
      logger.debug("calculatNotFoldedPlayerNb {}", board.calculatNotFoldedPlayerNb());
      if (!bestPlayers.isEmpty()
          && board.getPlayer(bestPlayers.get(0)).getBestPossibleHand() != null) {
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

        // highlight players cards
        for (PlayerModel player : board.getPlayers()) {
          if (!player.isFolded()) {
            for (Card card : player.getHand().getCards()) {
              deckUI.highlightCard(card, winCards.contains(card));
            }
          }
        }

        // highlight board cards
        for (Card card : board.getBoardCards()) {
          deckUI.highlightCard(card, winCards.contains(card));
        }
        game.commitWorldState(0.2); // so the last action appears before the hand values
      }
    }
  }
}
