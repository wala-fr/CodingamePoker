package com.codingame.view;

import com.codingame.game.Constant;
import com.codingame.game.Player;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.codingame.model.object.Board;
import com.codingame.view.object.Game;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Viewer {

  @Inject
  private Game graphic;
  @Inject
  private BoardUI boardUI;

  public void init() {
    int viewerWidth = graphic.getGraphics().getWorld().getWidth();
    int viewerHeight = graphic.getGraphics().getWorld().getHeight();
    graphic.getGraphics()
      .createSprite()
      .setImage(ViewUtils.getBackGroundUrl())
      .setX(0)
      .setY(0)
      .setBaseWidth(viewerWidth)
      .setBaseHeight(viewerHeight)
      .setZIndex(-1000);
    graphic.commitWorldState();
    // graphics.getGraphics()
    // .createRectangle()
    // .setWidth(viewerWidth)
    // .setHeight(viewerHeight)
    // .setFillColor(ViewConstant.BACK_GROUND_COLOR);
    boardUI.init();
  }

  public void resetTurn(int turn) {
    graphic.resetTime();
    graphic.setTurn(turn);
  }

  public void resetHAnd() {
    boardUI.resetHand();
  }

  public void update() {
    boardUI.update();
    if (graphic.isEnd()) {
      System.err.println(graphic.getBoard().toPlayerStatesString());
    }
  }


}
