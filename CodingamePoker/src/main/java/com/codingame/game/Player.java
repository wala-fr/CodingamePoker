package com.codingame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Card;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.CardUtils;

public class Player extends AbstractMultiplayerPlayer {

  private static final Logger logger = LoggerFactory.getLogger(Player.class);

  private String message = "";

  @Override
  public int getExpectedOutputLines() {
    return 1;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void sendInputLine2(String line) {
    logger.debug(line);
    sendInputLine(line);
  }

  public void sendInputLine(int line) {
    sendInputLine2(Integer.toString(line));
  }


}
