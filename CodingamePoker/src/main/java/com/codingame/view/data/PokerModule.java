package com.codingame.view.data;

import com.codingame.gameengine.core.Module;
import com.codingame.view.object.Game;
import com.google.inject.Inject;

public class PokerModule implements Module {

  @Inject
  private Game game;

  @Override
  public final void onGameInit() {
    sendGlobalData();
    sendFrameData();
  }

  @Override
  public final void onAfterGameTurn() {
    sendFrameData();
  }

  @Override
  public final void onAfterOnEnd() {
    sendFrameData();
  }

  private void sendFrameData() {
    game.getGameManager().setViewData("poker", game.getFrameViewData().serialize());
  }

  private void sendGlobalData() {
    game.getGameManager().setViewGlobalData("poker", game.getGlobalViewData().serialize());
  }



}
