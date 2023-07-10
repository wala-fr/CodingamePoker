package com.codingame.view.parameter;

import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Text.FontWeight;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.model.object.Board;
import com.codingame.model.object.Card;
import com.codingame.model.object.DealPosition;
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

  public static String getCardUrl(Card card) {
    return ViewConstant.CARD_URL + card.toString() + ".png";
  }

  // public static String getCardHighlightUrl(Card card, boolean win) {
  // return ViewConstant.CARD_URL + card.toString() + (win ? "_G.png" : "_R.png");
  // }

  // public static String getCardHighlightUrl() {
  // return ViewConstant.ASSET_URL + "highlight_card.png";
  // }
  //
  //
  // public static String getCardHighlightUrl(Card card) {
  // return ViewConstant.CARD_URL + card.toString() + "_G.png";
  // }
  //
  // public static String getCardHighlightUrl(String card) {
  // return card.replace(".png", "_G.png");
  // }
  //
  // public static String getCardLowlightUrl(Card card) {
  // return ViewConstant.CARD_URL + card.toString() + "_R.png";
  // }
  //
  // public static String getCardLowlightUrl(String card) {
  // return card.replace(".png", "_R.png");
  // }

  public static Point getCardPosition(Game graphics, DealPosition dealPosition) {
    if (dealPosition.isBurned()) {
      return getBurnedCardPosition(dealPosition.getIndex());
    }
    if (dealPosition.isBoard()) {
      return getBoardCardPosition(dealPosition.getIndex());
    }
    return getPlayerCardPosition(graphics, dealPosition.getId(), dealPosition.getIndex());
  }

  private static Point getPlayerCardPosition(Game graphics, int playerId, int index) {
    return getPlayerUICoordinates(graphics, playerId).getCard(index).getPoint();
  }

  private static Point getBurnedCardPosition(int index) {
    return new Point(ViewConstant.BURNED_CARD_X, ViewConstant.BURNED_CARD_Y);
  }

  private static Point getBoardCardPosition(int index) {
    return getCardPosition(ViewConstant.BOARD_CARD_X, ViewConstant.BOARD_CARD_Y, index).getPoint();
  }

  public static Point getDeckCardPosition(int dealer, int index) {
    return new Point(ViewConstant.DECK_CARD_X, ViewConstant.DECK_CARD_Y);
  }

  public static Point getDiscardCardPosition(int dealer, int index) {
    return new Point(ViewConstant.DISCARD_CARD_X, ViewConstant.DISCARD_CARD_Y);
  }

  // public static TextPoint getBlindTextPoint(boolean label) {
  // return new TextPoint(getX(ViewConstant.BLIND_X, label), ViewConstant.BLIND_Y);
  // }
  //
  // private static int getX(int x, boolean label) {
  // return x + (label ? 0 : ViewConstant.LABEL_WIDTH);
  // }
  // public static TextPoint getLevelTextPoint(boolean label) {
  // return new TextPoint(getX(ViewConstant.LEVEL_X, label), ViewConstant.LEVEL_Y);
  // }
  //
  // public static TextPoint getGameNbTextPoint(boolean label) {
  // return new TextPoint(getX(ViewConstant.GAME_NB_X, label), ViewConstant.GAME_NB_Y);
  // }

//  public static TextPoint getPotTextPoint() {
//    return new TextPoint(ViewConstant.POT_X, ViewConstant.POT_Y,
//        ViewConstant.POT_WIDTH);
//  }

  public static void clearText(Game graphics, Text text) {
    updateText(graphics, text, "");
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
      graphics.getTooltips().setTooltipText(text, strMouseHover != null ? strMouseHover : str);
//      graphics.getGraphics().commitEntityState(graphics.getTime(), text);
    }
  }

  public static PlayerUICoordinates getPlayerUICoordinates(Game graphics, int id) {
    return getPlayerUICoordinates(graphics.getBoard(), id);
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

  public static void createTextRectangle(Text text, TextPoint point, boolean label, Game graphic,
      Group group) {
    createTextRectangle(text, point.getX(), point.getY(), point.getWidth(), label, graphic, group);
  }

  public static void createTextRectangle(Text text, int x, int y, int width, boolean label,
      Game graphic, Group group) {
    text.setX(x + 10)
      .setY(y + 5)
      .setFontSize(ViewConstant.LABEL_FONT_SIZE)
      .setFontWeight(label ? FontWeight.BOLD : FontWeight.BOLD)
      .setFontFamily(ViewConstant.FONT)
      .setTextAlign(TextAlign.RIGHT)
      .setMaxWidth(width - ViewConstant.DELTA_RECTANGLE_TEXT_WIDTH)
      .setFillColor(ViewConstant.LABEL_TEXT_COLOR);
    group.add(text);
    Rectangle rectangle = graphic.getGraphics()
      .createRectangle()
      .setX(x)
      .setY(y)
      .setZIndex(-10)
      .setWidth(width)
      .setHeight(ViewConstant.LABEL_HEIGHT)
      .setLineWidth(ViewConstant.LABEL_FRAME_WIDTH)
      .setLineColor(ViewConstant.LABEL_TEXT_COLOR)
      .setFillColor(label ? ViewConstant.LABEL_COLOR : ViewConstant.LABEL_TEXT_BACK_GROUND_COLOR);
    group.add(rectangle);
  }
}
