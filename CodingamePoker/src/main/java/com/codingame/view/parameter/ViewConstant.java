package com.codingame.view.parameter;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Text.FontWeight;
import com.codingame.view.PlayerUICoordinates;

public class ViewConstant {

  public static final int AVATAR_COLOR = 0xd1d1e0;
  public static final int AVATAR_ACTIVE_COLOR = 0x5a756f;

  public static final int FRAME_DURATION = 800;

  public static PlayerUICoordinates[] COORDINATES;

  public static final int DEFAULT_FONT_SIZE = 30;

  private static final double h = 2.0;
  public static final int CARD_WIDTH = (int) (64 * h);
  public static final int CARD_HEIGHT = (int) (89 * h);
  public static final int CARD_DELTA = (int) (5 * h);

  public static final int AVATAR_WIDTH = CARD_HEIGHT;
  public static final int AVATAR_NAME_HEIGHT = 75;

  public static final String FONT = "Verdana";
  public static final int WRITE_COLOR = 0x133337;

  public static final int BUTTON_FILL_COLOR = 0xFF0000;
  public static final int BUTTON_WRITE_COLOR = 0xFFFFFF;
  public static final int BUTTON_RADIUS = 30;

  public static final int ACTION_BUTTON_FILL_COLOR = 0x354264;

  public static final int ACTION_HEIGHT = 0;
  public static final int ACTION_FONT_SIZE = DEFAULT_FONT_SIZE;

  public static final int STACK_HEIGHT = 50;
  public static final int STACK_FONT_SIZE = DEFAULT_FONT_SIZE;

  public static int BOARD_CARD_X;
  public static int BOARD_CARD_Y;

  public static int BURNED_CARD_X;
  public static int BURNED_CARD_Y;

  public static int DECK_CARD_X;
  public static int DECK_CARD_Y;

  public static int DISCARD_CARD_X;
  public static int DISCARD_CARD_Y;

  public static final int DISCARD_CARD_DELTA_X = 11;
  public static final int DISCARD_CARD_DELTA_Y = 8;

  public static int BLIND_X;
  public static int BLIND_Y;

  public static int LEVEL_X;
  public static int LEVEL_Y;

  public static int GAME_NB_X;
  public static int GAME_NB_Y;
  
  public static int TURN_NB_X;
  public static int TURN_NB_Y;

  public static int POT_X;
  public static int POT_Y;
  
  public static int TIE_X;
  public static int TIE_Y;

  public static int NO_ACTION_X;
  public static int NO_ACTION_Y;

  public static int HEIGHT;
  public static int WIDTH;

  public static final int DELTA_BOARD_SIDE = 10;

  public static int TOP_X;
  public static int SIDE_Y;


  public static void init(GraphicEntityModule graphics) {
    HEIGHT = graphics.getWorld().getHeight();
    WIDTH = graphics.getWorld().getWidth();
    int middleX = WIDTH / 2;
    int middleY = HEIGHT / 2;

    BOARD_CARD_Y = middleY - CARD_HEIGHT / 2 - 30;
    BOARD_CARD_X = middleX - (5 * CARD_WIDTH + 2 * CARD_DELTA) / 2;

    DECK_CARD_Y = BOARD_CARD_Y + CARD_HEIGHT + CARD_DELTA;
    DECK_CARD_X = BOARD_CARD_X + 1 * (CARD_WIDTH + CARD_DELTA);

    BURNED_CARD_Y = DECK_CARD_Y;
    BURNED_CARD_X = BOARD_CARD_X + 3 * (CARD_WIDTH + CARD_DELTA);

    DISCARD_CARD_Y = BURNED_CARD_Y;
    DISCARD_CARD_X = BURNED_CARD_X;
    
    int tmpY = DELTA_BOARD_SIDE;

    LEVEL_X = DELTA_BOARD_SIDE;
    LEVEL_Y = tmpY;

    tmpY += LABEL_HEIGHT;
    BLIND_X = LEVEL_X;
    BLIND_Y = tmpY;
    
    tmpY += LABEL_HEIGHT;
    GAME_NB_X = LEVEL_X;
    GAME_NB_Y = tmpY;
    
    tmpY += LABEL_HEIGHT;
    TURN_NB_X = LEVEL_X;
    TURN_NB_Y = tmpY;

    POT_X = middleX - POT_LABEL_WIDTH;// (POT_LABEL_WIDTH + POT_WIDTH) / 2;
    POT_Y = BOARD_CARD_Y - 110;
    
    TIE_X = POT_X + (ViewConstant.POT_LABEL_WIDTH + ViewConstant.POT_WIDTH + 50);
    TIE_Y = POT_Y;
    
    NO_ACTION_X = POT_X - (ViewConstant.BUTTON_RADIUS + 50);
    NO_ACTION_Y = POT_Y + (ViewConstant.BUTTON_RADIUS - 5);

    // int sideWidth = 2 * CARD_WIDTH + CARD_DELTA;
    int sideHeight = CARD_HEIGHT + CARD_DELTA + 3 * LABEL_HEIGHT;

    // int topHeight = CARD_HEIGHT + AVATAR_NAME_HEIGHT;
    int topWidth = AVATAR_WIDTH + 2 * CARD_WIDTH + 3 * CARD_DELTA;

    TOP_X = middleX - topWidth / 2 - 80;
    SIDE_Y = middleY - sideHeight / 2;

    COORDINATES = new PlayerUICoordinates[4];
    for (int i = 0; i < COORDINATES.length; i++) {
      COORDINATES[i] = new PlayerUICoordinates(i);
    }
  }

  public static final String ASSET_URL = "";

  public static final String CARD_URL = ASSET_URL + "cards4/";
  public static final String DEBUG_CARD_URL = ASSET_URL + "cards3/";

  public static final int TEXT_DEFAULT_WIDTH = 300;
  public static final int NAME_WIDTH = 170;

  public static final int LABEL_WIDTH = 160;
  public static final int LABEL_TEXT_WIDTH = 280;

  public static final int LABEL_COLOR = 0xcd1624;
  public static final int LABEL_TEXT_COLOR = 0xFFFFFF;

  public static final int LABEL_FONT_SIZE = ViewConstant.DEFAULT_FONT_SIZE;
  public static final FontWeight LABEL_FONT_WEIGHT = FontWeight.NORMAL;

  public static final double LABEL_FRAME_WIDTH = 5;

  public static final int LABEL_HEIGHT = 50;

  public static final int LABEL_TEXT_BACK_GROUND_COLOR = 0x000000;

  public static final int POSITION_WIDTH = 100;

  public static final int ACTION_WIDTH = 380;

  public static final int STACK_WIDTH = ACTION_WIDTH - POSITION_WIDTH;

  // public static final int POT_FILL_COLOR = 0xf8fa5e;
  // public static final int POT_TEXT_COLOR = 0x000000;

  public static final int POT_LABEL_WIDTH = 100;
  public static final int POT_WIDTH = 200;
  
  public static final int TIE_LABEL_WIDTH = 90;
  public static final int TIE_WIDTH = 130;

  public static final int DELTA_RECTANGLE_TEXT_WIDTH = 10;

  public static final int Z_INDEX_BACK = -100;
  public static final int Z_INDEX_BOARD = 0;
  public static final int Z_INDEX_CARD = 100;
  public static final int Z_INDEX_CARD_DEAL = Z_INDEX_CARD + 100;
  // so the button goes over the deck
  public static final int Z_INDEX_BUTTON = Z_INDEX_CARD_DEAL;
  public static final int Z_INDEX_ACTION_BUTTON = Z_INDEX_CARD + 1000;
  
  // if value to -101 there's a bug as if the zIndex change slowly ????
  public static final int Z_INDEX_INVISIBLE = -10000;

  public static final int WIN_COLOR = 0x46ff33;
  public static final int LOSS_COLOR = 0xfe1304;

  public static final int SHOW_WIN_AMOUNT_COEFF = 2;
  
  public static final double MAX_TIME = 0.9;

  public static final int CARD_MIN_NB = 4 * 2 + 5 + 3 + 1;
  public static final int TIE_COLOR = 0x4486ea;
  
  public static final int FOLD_COLOR = 0x9c8136;

//  public static final int SHOW_WIN_AMOUNT_CHIPS = 500;

}
