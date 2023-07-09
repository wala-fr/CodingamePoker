package com.codingame.view.parameter;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Text.FontWeight;
import com.codingame.view.PlayerUICoordinates;
import com.codingame.view.object.Point;

public class ViewConstant {

  public static final int RECTANGLE_COLOR = 0xFFFF00;

  public static final int BOARD_COLOR = 0x000000;

  public static final int[] PAWN_COLORS = new int[] {0xFF0000, 0x0000FF};

  public static final int AVATAR_COLOR = 0xd1d1e0;

  public static final int AVATAR_COLOR_ELIMINATED = 0x292828;
  public static final int AVATAR_NAME_COLOR_ELIMINATED = 0xFFFFFF;

  public static final int AVATAR_COLOR_LOSE = 0xff0400;
  public static final int AVATAR_COLOR_WIN = 0x23fc05;


  public static final int AVATAR_COLOR_CURRENT = 0xfcc743;

  public static final int BACK_GROUND_COLOR = 0x008000;
  // public static final int WRITE_COLOR = 0x133337;

  public static final int Y_NICKNAME = 0;
  public static final int Y_AVATAR = 100;
  public static final int Y_LAST_TILE_TITLE = 360;
  public static final int Y_LAST_TILE = Y_LAST_TILE_TITLE + 120;
  public static final int Y_MESSAGE = 580;
  // public static final int FONT_LAST_TILE = 60;
  // public static final int FONT_TEXT = 45;
  // public static final int FONT_MESSAGE = 35;

  public static final int TILE_GAP = 20;
  public static final int PAWN_GAP = 2;
  public static final int PAWN_LINE_WIDTH = 0;


  public static final int FRAME_DURATION = 800;
  public static final double PAWN_TIME = 0.5;

  ///////////////////////////
  //
  // public static PlayerUICoordinates COORDINATE_UP;
  //
  // public static PlayerUICoordinates COORDINATE_DOWN;
  //
  // public static PlayerUICoordinates COORDINATE_LEFT;
  //
  // public static PlayerUICoordinates COORDINATE_RIGHT;

  public static PlayerUICoordinates[] COORDINATES;

  public static final int DEFAULT_FONT_SIZE = 30;

  private static final double h = 2.0;
  public static final int CARD_WIDTH = (int) (64 * h);
  public static final int CARD_HEIGHT = (int) (89 * h);
  public static final int CARD_DELTA = (int) (5 * h);

  public static final int AVATAR_WIDTH = CARD_HEIGHT;
  // public static final int AVATAR_FONTSIZE = 200;
  public static final int AVATAR_NAME_HEIGHT = 75;
  // public static final int AVATAR_NAME_WIDTH = 200;
  // public static final int AVATAR_NAME_SIZE_MAX = 8;



  public static final String FONT = "Verdana";
  public static final int WRITE_COLOR = 0x133337;

  public static final int BUTTON_FILL_COLOR = 0xFF0000;
  public static final int BUTTON_WRITE_COLOR = 0xFFFFFF;
  public static final int BUTTON_RADIUS = 30;

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

  public static int BLIND_X;
  public static int BLIND_Y;

  public static int LEVEL_X;
  public static int LEVEL_Y;

  public static int GAME_NB_X;
  public static int GAME_NB_Y;

  public static int POT_X;
  public static int POT_Y;

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

    BOARD_CARD_Y = middleY - CARD_HEIGHT / 2;
    BOARD_CARD_X = middleX - (5 * CARD_WIDTH) / 2 + 2 * CARD_DELTA;

    DECK_CARD_Y = BOARD_CARD_Y + CARD_HEIGHT + CARD_DELTA;
    DECK_CARD_X = middleX + CARD_WIDTH / 2;

    BURNED_CARD_Y = DECK_CARD_Y;
    BURNED_CARD_X = DECK_CARD_X - CARD_WIDTH - CARD_DELTA;

    DISCARD_CARD_Y = BURNED_CARD_Y;
    DISCARD_CARD_X = BURNED_CARD_X;

    LEVEL_X = DELTA_BOARD_SIDE;
    LEVEL_Y = DELTA_BOARD_SIDE;

    BLIND_X = LEVEL_X;
    BLIND_Y = LEVEL_Y + LABEL_HEIGHT;

    GAME_NB_X = LEVEL_X;
    GAME_NB_Y = BLIND_Y + LABEL_HEIGHT;

    POT_X = middleX - POT_LABEL_WIDTH;//(POT_LABEL_WIDTH +  POT_WIDTH) / 2;
    POT_Y = BOARD_CARD_Y - 100;

//    int sideWidth = 2 * CARD_WIDTH + CARD_DELTA;
    int sideHeight = CARD_HEIGHT + CARD_DELTA + 3 * LABEL_HEIGHT;

//    int topHeight = CARD_HEIGHT + AVATAR_NAME_HEIGHT;
    int topWidth = AVATAR_WIDTH + 2 * CARD_WIDTH + 3 * CARD_DELTA;

    TOP_X = middleX - topWidth / 2;
    SIDE_Y = middleY - sideHeight / 2;

//    PlayerUICoordinates COORDINATE_UP = new PlayerUICoordinates(topX, DELTA_BOARD_SIDE, 0);
//    PlayerUICoordinates COORDINATE_RIGHT =
//        new PlayerUICoordinates(0, sideY, 1);
//    PlayerUICoordinates COORDINATE_LEFT = new PlayerUICoordinates(0, sideY, 3);
//    PlayerUICoordinates COORDINATE_DOWN =
//        new PlayerUICoordinates(topX, HEIGHT - topHeight - DELTA_BOARD_SIDE, 2);

    COORDINATES = new PlayerUICoordinates[4];
    for (int i = 0; i < COORDINATES.length; i++) {
      COORDINATES[i] = new PlayerUICoordinates(i);
    }
//    for (PlayerUICoordinates coord : COORDINATES) {
//      System.err.println(coord);
//    }
  }

  // public static final String ASSET_URL = "src/main/resources/view/assets/";

  public static final String ASSET_URL = "";

  public static final String CARD_URL = ASSET_URL + "cards4/";

  public static final int TEXT_DEFAULT_WIDTH = 300;
  public static final int NAME_WIDTH = 170;

  public static final int LABEL_WIDTH = 160;
  public static final int LABEL_TEXT_WIDTH = 250;

  public static final int LABEL_COLOR = 0xcd1624;
  public static final int LABEL_TEXT_COLOR = 0xFFFFFF;

  public static final int LABEL_FONT_SIZE = ViewConstant.DEFAULT_FONT_SIZE;
  public static final FontWeight LABEL_FONT_WEIGHT = FontWeight.NORMAL;

  public static final double LABEL_FRAME_WIDTH = 5;

  public static final int LABEL_HEIGHT = 50;

  public static final int LABEL_TEXT_BACK_GROUND_COLOR = 0x000000;

  public static final int POSITION_WIDTH = 100;

  public static final int ACTION_WIDTH = 300;

  public static final int STACK_WIDTH = ACTION_WIDTH - POSITION_WIDTH;

//  public static final int POT_FILL_COLOR = 0xf8fa5e;
//  public static final int POT_TEXT_COLOR = 0x000000;

  public static final int POT_LABEL_WIDTH = 100;
  public static final int POT_WIDTH = 200;

  public static final int DELTA_RECTANGLE_TEXT_WIDTH = 10;


}
