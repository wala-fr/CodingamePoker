package com.codingame.model.object;

import java.util.Objects;
import com.codingame.game.RefereeParameter;
import com.codingame.model.object.enumeration.ActionType;

public class Action {

  public static final Action FOLD = new Action(ActionType.FOLD);
  public static final Action CALL = new Action(ActionType.CALL);
  public static final Action ALL_IN = new Action(ActionType.ALL_IN);
  public static final Action CHECK = new Action(ActionType.CHECK);
  public static final Action TIMEOUT = new Action(ActionType.TIMEOUT);

  private ActionType type;
  private int amount;

  private Action(ActionType type) {
    this.type = type;
  }

  private Action(ActionType type, int amount) {
    this.type = type;
    this.amount = amount;
  }

  public static Action create(ActionType type) {
    if (type == ActionType.FOLD) {
      return FOLD;
    }
    if (type == ActionType.CHECK) {
      return CHECK;
    }
    if (type == ActionType.CALL) {
      return CALL;
    }
    if (type == ActionType.TIMEOUT) {
      return TIMEOUT;
    }
    return ALL_IN;
  }

  public static Action create(String str) throws InvalidMoveException {
    if (str.startsWith("BET_")) {
      str = str.replaceAll("BET_", "BET ");
    }
    String[] tmp = str.split(" ", -1);
    ActionType type = ActionType.fromString(tmp[0]);
    int amount = 0;
    if (tmp.length == 0 && tmp.length > 2) {
      throw new InvalidMoveException(str);
    }
    if (tmp.length > 1) {
      amount = Integer.parseInt(tmp[1]);
    } else if (type == ActionType.BET) {
      throw new InvalidMoveException(str);
    }
    return create(type, amount);
  }

  public static Action create(ActionType type, int amount) {
    if (type == ActionType.BET) {
      return new Action(ActionType.BET, amount);
    }
    return create(type);
  }

  public ActionType getType() {
    return type;
  }

  public int getAmount() {
    return amount;
  }

  @Override
  public int hashCode() {
    return type == ActionType.BET ? Objects.hash(amount, type) : Objects.hash(type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Action other = (Action) obj;
    return amount == other.amount && type == other.type;
  }

  @Override
  public String toString() {
    return type + (amount > 0 ? " " + amount : "");
  }

  public String toInputString() {
    String ret = type.toString();
    if (type == ActionType.BET) {
      ret += RefereeParameter.WORD_DELIMITER + amount;
    }
    return ret;
  }


}
