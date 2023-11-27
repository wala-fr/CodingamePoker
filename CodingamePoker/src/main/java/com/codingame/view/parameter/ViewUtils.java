package com.codingame.view.parameter;

import com.codingame.gameengine.module.entities.BlendableEntity.BlendMode;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Text.FontWeight;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.model.object.Card;
import com.codingame.model.object.DealPosition;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.AssertUtils;
import com.codingame.view.PlayerUICoordinates;
import com.codingame.view.object.Game;
import com.codingame.view.object.Point;
import com.codingame.view.object.SizePoint;
import com.codingame.view.object.TextPoint;

public class ViewUtils {

  public static String getBackGroundUrl() {
    return ViewConstant.ASSET_URL + "background.jpg";
  }

  public static String getButtonUrl() {
    return ViewConstant.ASSET_URL + "button.jpg";
  }

  public static String getCardBackUrl() {
    return ViewConstant.ASSET_URL + "card_back.png";
  }

  public static String getCardUrl(Card card, boolean debug) {
    return (debug ? ViewConstant.DEBUG_CARD_URL : ViewConstant.CARD_URL) + card.toString() + ".png";
  }

  public static Point getCardPosition(Game graphics, DealPosition dealPosition) {
    AssertUtils.test(!dealPosition.isBurned());
    if (dealPosition.isBoard()) {
      return getBoardCardPosition(dealPosition.getIndex());
    }
    return getPlayerCardPosition(graphics, dealPosition.getId(), dealPosition.getIndex());
  }

  public static Point getPlayerCardPosition(Game graphics, int playerId, int index) {
    return getPlayerUICoordinates(graphics, playerId).getCard(index).getPoint();
  }

  // private static Point getBurnedCardPosition(int index) {
  // return new Point(ViewConstant.BURNED_CARD_X, ViewConstant.BURNED_CARD_Y);
  // }

  private static Point getBoardCardPosition(int index) {
    return getCardPosition(ViewConstant.BOARD_CARD_X, ViewConstant.BOARD_CARD_Y, index).getPoint();
  }

  public static Point getDeckCardPosition(int dealer) {
    return new Point(ViewConstant.DECK_CARD_X, ViewConstant.DECK_CARD_Y);
  }

  public static Point getDiscardCardPosition(int dealer, int index) {
    int deltaX = (index - 1) * ViewConstant.DISCARD_CARD_DELTA_X;
    int deltaY = (index - 1) * ViewConstant.DISCARD_CARD_DELTA_Y;
    return new Point(ViewConstant.DISCARD_CARD_X + deltaX, ViewConstant.DISCARD_CARD_Y + deltaY);
  }

  public static void clearText(Game graphics, Text text) {
    updateText(graphics, text, "");
  }

  public static void updateText(Game graphics, Text text, int n) {
    updateText(graphics, text, Integer.toString(n));
  }

  public static void updateText(Game graphics, Text text, String str) {
    updateText(graphics, text, str, null);
  }

  public static void updateText(Game graphics, Text text, String str, String strMouseHover) {
    if (str == null) {
      str = "";
    }
    String old = text.getText();
    if (!str.equals(old)) {
      text.setText(str);
    }
    graphics.getTooltips().setTooltipText(text, strMouseHover != null ? strMouseHover : str);
  }

  public static PlayerUICoordinates getPlayerUICoordinates(Game graphics, int id) {
    return getPlayerUICoordinates(graphics.getBoard(), id);
  }

  public static Point getNoActionButtonPosition() {
    return new Point(ViewConstant.NO_ACTION_X, ViewConstant.NO_ACTION_Y);
  }

  public static PlayerUICoordinates getPlayerUICoordinates(Board board, int id) {
    int playerNb = board.getPlayerNb();
    int index = id;
    if (playerNb == 3) {
      if (id == 2) {
        index = 3;
      }
    } else if (playerNb == 2) {
      if (id == 0) {
        index = 3;
      } else {
        index = 1;
      }
    }
    return ViewConstant.COORDINATES[index];
  }

  public static SizePoint getCardPosition(int x, int y, int cardNb) {
    return new SizePoint(x + (ViewConstant.CARD_WIDTH + ViewConstant.CARD_DELTA) * cardNb, y,
        ViewConstant.CARD_WIDTH, ViewConstant.CARD_HEIGHT);
  }

  public static void createTextRectangle(Text text, TextPoint point, Color color, Game graphic,
      Group group) {
    createTextRectangle(text, point.getX(), point.getY(), point.getWidth(), color, graphic, group);
  }

  // public static void createTextRectangleNoBorder(Text text, TextPoint point, boolean label,
  // Game graphic, Group group) {
  // createTextRectangle(text, point.getX(), point.getY(), point.getWidth(), label, graphic, group,
  // false);
  // }

  // public static void createTextRectangle(Text text, int x, int y, int width, boolean label,
  // Game graphic, Group group) {
  // createTextRectangle(text, x, y, width, label, graphic, group, true);
  // }

  public static void createTextRectangle(Text text, int x, int y, int width, Color color,
      Game graphic, Group group) {
    text.setX(x + 10)
      .setY(y + 5)
      .setZIndex(ViewConstant.Z_INDEX_BOARD)
      .setFontSize(ViewConstant.LABEL_FONT_SIZE)
      .setFontWeight(FontWeight.BOLD)
      .setFontFamily(ViewConstant.FONT)
      .setTextAlign(TextAlign.RIGHT)
      .setMaxWidth(width - ViewConstant.DELTA_RECTANGLE_TEXT_WIDTH)
      .setFillColor(ViewConstant.LABEL_TEXT_COLOR);
    group.add(text);
    Rectangle rectangle = graphic.getGraphics()
      .createRectangle()
      .setX(x)
      .setY(y)
      .setZIndex(ViewConstant.Z_INDEX_BOARD - 1)
      .setWidth(width)
      .setHeight(ViewConstant.LABEL_HEIGHT)
      .setLineWidth(ViewConstant.LABEL_FRAME_WIDTH)
      .setLineColor(ViewConstant.LABEL_TEXT_COLOR)
      .setFillColor(color == Color.RED ? ViewConstant.LABEL_COLOR
          : color == Color.BLUE ? ViewConstant.TIE_COLOR
              : ViewConstant.LABEL_TEXT_BACK_GROUND_COLOR);
    group.add(rectangle);
  }

  public static void copy(Text source, Text destination) {
    destination.setX(source.getX())
      .setY(source.getY())
      .setZIndex(source.getZIndex())
      .setFontSize(source.getFontSize())
      .setFontWeight(source.getFontWeight())
      .setFontFamily(source.getFontFamily())
      .setTextAlign(source.getTextAlign())
      .setMaxWidth(source.getMaxWidth())
      .setFillColor(source.getFillColor());
  }

  public static void increaseZIndex(Entity e) {
    e.setZIndex(e.getZIndex() + 1);
  }

  public static boolean isShowWinAmount(int winAmount, Board board) {
    return winAmount >= (board.getPlayerNotEliminatedNb() - 1) * ViewConstant.SHOW_WIN_AMOUNT_COEFF
        * board.getBigBlind() || (board.isIncreaseLevel() && winAmount > 0);
  }

  public static String addSpaceBefore(String s, int length) {
    while (s.length() < length) {
      s = " " + s;
    }
    return s;
  }

  public static Sprite createCard(Game game) {
    Sprite ret = game.getGraphics()
      .createSprite()
      .setImage(ViewUtils.getCardBackUrl())
      .setX(0)
      .setY(0)
      .setBaseWidth(ViewConstant.CARD_WIDTH)
      .setBaseHeight(ViewConstant.CARD_HEIGHT);
    return ret;
  }

  public static void hide(Sprite card, Game game) {
    // card.setAlpha(0, Curve.IMMEDIATE);
    if (card != null) {
      card.setZIndex(ViewConstant.Z_INDEX_INVISIBLE);
      game.commitEntityState(card);
    }
  }

  public static String roundTwoDecimal(double value) {
    return String.format("%.2f", value);
  }

  public static int round(double value) {
    return (int) Math.round(value);
  }
}
