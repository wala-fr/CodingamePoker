package com.codingame.model.object;

import com.codingame.model.utils.MessageUtils;

public class ActionInfo {

  private int playerId;
  private Action action;
  private String error;

  private ActionInfo() {}

  // public ActionInfo(int playerId, Action action) {
  // this.playerId = playerId;
  // this.action = action;
  // }

  public int getPlayerId() {
    return playerId;
  }

  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public void setError(String s) {
    if (s == null) {
      return;
    }
    error = s;
  }

  public boolean hasError() {
    return error != null;
  }

  public String getError() {
    return error;
  }

  public static ActionInfo create(int playerId, String str) {
    ActionInfo info = new ActionInfo();
    info.playerId = playerId;
    try {
      info.action = Action.create(str);
    } catch (Exception e) {
      info.action = Action.FOLD;
      info.error = MessageUtils.format("wrong.action.format", playerId, str);
    }
    return info;
  }


}
