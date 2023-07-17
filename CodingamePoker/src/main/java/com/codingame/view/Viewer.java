package com.codingame.view;

import com.codingame.view.object.Game;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Viewer {

  @Inject
  private Game game;
  @Inject
  private BoardUI boardUI;

  public void init() {
    game.getGraphics()
      .createSprite()
      .setImage(ViewUtils.getBackGroundUrl())
      .setX(0)
      .setY(0)
      .setBaseWidth(ViewConstant.WIDTH)
      .setBaseHeight(ViewConstant.HEIGHT)
      .setZIndex(ViewConstant.Z_INDEX_BACK);
    game.commitWorldState();
    // graphics.getGraphics()
    // .createRectangle()
    // .setWidth(viewerWidth)
    // .setHeight(viewerHeight)
    // .setFillColor(ViewConstant.BACK_GROUND_COLOR);
    boardUI.init();
  }

  public void resetTurn(int turn) {
    game.resetTime();
    game.setTurn(turn);
  }

  public void resetHAnd() {
    boardUI.resetHand();
  }

  public void update() {
    boardUI.update();
  }


}
