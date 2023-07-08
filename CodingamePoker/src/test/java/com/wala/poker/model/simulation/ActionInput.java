package com.wala.poker.model.simulation;

import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.InvalidMoveException;
import com.codingame.model.object.enumeration.ActionType;

public class ActionInput {

  private ActionInfo actionInfo;
  private int playerId;
  private Action realAction;

  public ActionInput(String str) {
    String[] tmp = str.split(":");
    playerId = Integer.parseInt(tmp[0]);
    actionInfo = ActionInfo.create(playerId, tmp[1]);
    if (tmp.length > 2) {
      try {
        realAction = Action.create(tmp[2]);
      } catch (InvalidMoveException e) {
        throw new IllegalStateException();
      }
    }
  }

  public ActionInfo getActionInfo() {
    return actionInfo;
  }
  
  public Action getRealAction() {
    return realAction == null ? actionInfo.getAction() : realAction;
  }

  public int getPlayerId() {
    return playerId;
  }

  @Override
  public String toString() {
    return playerId + " " + actionInfo.getAction();
  }

  
}
