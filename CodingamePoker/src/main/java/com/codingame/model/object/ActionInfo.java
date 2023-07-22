package com.codingame.model.object;

import com.codingame.model.object.enumeration.ActionType;
import com.codingame.model.utils.MessageUtils;

public class ActionInfo {

  private int playerId;
  private Action action;
  private String error;
  private boolean levelError;

  private ActionInfo() {}

  public int getPlayerId() {
    return playerId;
  }

  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public void setError(String s, boolean levelError) {
    if (s == null) {
      return;
    }
    error = s;
    this.levelError = levelError;
  }

  public boolean hasError() {
    return error != null;
  }

  public String getError() {
    return error;
  }

  public boolean isLevelError() {
    return levelError;
  }



  @Override
  public String toString() {
    return "ActionInfo [playerId=" + playerId + ", action=" + action + ", error=" + error
        + ", levelError=" + levelError + "]";
  }

  public static ActionInfo create(int playerId, ActionType type) {
    return create(playerId, type.toString());
  }

  public static ActionInfo create(int playerId, String str) {
    ActionInfo info = new ActionInfo();
    info.playerId = playerId;
    try {
      info.action = Action.create(str);
    } catch (Exception e) {
      info.action = Action.FOLD;
      info.error = MessageUtils.format("wrong.action.format", playerId, str);
      info.levelError = true;
    }
    return info;
  }


}
