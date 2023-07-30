package com.codingame.view;

import com.codingame.view.object.Point;
import com.codingame.view.object.SizePoint;
import com.codingame.view.object.TextPoint;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;

public class PlayerUICoordinates {

  private int positionId;
  private SizePoint avatar;
  private SizePoint avatarFrame;
  private TextPoint name;

  private SizePoint card1;
  private SizePoint card2;

  private TextPoint action;
  private TextPoint position;
  private TextPoint message;
  private TextPoint stack;

  private Point buttonTop;
  private Point buttonBottom;

  public PlayerUICoordinates(int id) {
    positionId = id;
    if (id == 0) {
      initTop();
    } else if (id == 2) {
      initBottom();
    } else if (id == 1) {
      initRight();
    } else {
      initLeft();
    }
  }

  private PlayerUICoordinates initTop() {
    int x = ViewConstant.TOP_X;
    int y = ViewConstant.DELTA_BOARD_SIDE;
    initAvatar(x, y);
    initButton(x, y);
    int xCard = calculateCardTopX();
    initCard(xCard, y);
    int xLabel = calculateLabelTopX();
    initLabels(xLabel, y);
    return this;
  }

  private int calculateCardTopX() {
    return ViewConstant.TOP_X + ViewConstant.AVATAR_WIDTH + ViewConstant.CARD_DELTA;
  }

  private int calculateLabelTopX() {
    return calculateCardTopX() + 2 * ViewConstant.CARD_WIDTH + 2 * ViewConstant.CARD_DELTA;
  }

  private PlayerUICoordinates initBottom() {
    int x = ViewConstant.TOP_X;
    int y = ViewConstant.HEIGHT - (ViewConstant.DELTA_BOARD_SIDE + calculateAvatarHeight());
    initAvatar(x, y);
    initButton(x, y);
    int xCard = calculateCardTopX();
    int yCard = ViewConstant.HEIGHT - (ViewConstant.DELTA_BOARD_SIDE + ViewConstant.CARD_HEIGHT);
    initCard(xCard, yCard);
    int xLabel = calculateLabelTopX();
    int yLabel =
        ViewConstant.HEIGHT - (ViewConstant.DELTA_BOARD_SIDE + 3 * ViewConstant.LABEL_HEIGHT);

    initLabels(xLabel, yLabel);
    return this;
  }

  private PlayerUICoordinates initLeft() {
    int x = ViewConstant.DELTA_BOARD_SIDE;
    int y = ViewConstant.SIDE_Y;
    initAvatar(x, y);
    initButton(x + ViewConstant.AVATAR_WIDTH, y);
    int yCard = calculateCardSideY();
    initCard(x, yCard);
    int yLabel = calculateLabelSideY();
    initLabels(x, yLabel);
    return this;
  }

  private PlayerUICoordinates initRight() {
    int xAvatar = ViewConstant.WIDTH - (ViewConstant.DELTA_BOARD_SIDE + ViewConstant.AVATAR_WIDTH);
    int y = ViewConstant.SIDE_Y;
    initAvatar(xAvatar, y);
    initButton(xAvatar, y);
    int yCard = calculateCardSideY();
    int xCard = ViewConstant.WIDTH
        - (2 * ViewConstant.CARD_WIDTH + ViewConstant.CARD_DELTA + ViewConstant.DELTA_BOARD_SIDE);
    initCard(xCard, yCard);
    int yLabel = calculateLabelSideY();
    int xLabel = ViewConstant.WIDTH - (ViewConstant.ACTION_WIDTH + ViewConstant.DELTA_BOARD_SIDE);
    initLabels(xLabel, yLabel);
    return this;
  }

  private int calculateCardSideY() {
    return ViewConstant.SIDE_Y + ViewConstant.AVATAR_WIDTH + 50 + ViewConstant.CARD_DELTA;
  }

  private int calculateLabelSideY() {
    return calculateCardSideY() + ViewConstant.CARD_HEIGHT + ViewConstant.CARD_DELTA;
  }

  private void initButton(int x, int y) {
    boolean left = positionId != 3;
    buttonTop = new Point(x + (30 + ViewConstant.BUTTON_RADIUS) * (left ? -1 : 1),
        y + (ViewConstant.BUTTON_RADIUS + 10));
    y += calculateAvatarHeight();
    buttonBottom = new Point(x + (30 + ViewConstant.BUTTON_RADIUS) * (left ? -1 : 1),
        y - (ViewConstant.BUTTON_RADIUS + 10));
  }


  private void initCard(int x, int y) {
    card1 = ViewUtils.getCardPosition(x, y, 0);
    card2 = ViewUtils.getCardPosition(x, y, 1);
  }

  private void initAvatar(int x, int y) {
    avatarFrame = new SizePoint(x, y, ViewConstant.AVATAR_WIDTH, calculateAvatarHeight());
    int delta = 20;
    int w = ViewConstant.AVATAR_WIDTH - 2 * delta;
    avatar = new SizePoint(x + delta, y + delta, w, w);
    name = new TextPoint(x + 10, y + delta + w + ViewConstant.CARD_DELTA, ViewConstant.NAME_WIDTH);
  }

  private int calculateAvatarHeight() {
    return ViewConstant.AVATAR_WIDTH + 40;
  }

  private void initLabels(int x, int y) {
    position = new TextPoint(x, y, ViewConstant.POSITION_WIDTH);
    stack = new TextPoint(x + ViewConstant.POSITION_WIDTH, y, ViewConstant.STACK_WIDTH);
    action = new TextPoint(x, y + ViewConstant.STACK_HEIGHT, ViewConstant.ACTION_WIDTH);
    message = new TextPoint(x, y + 2 * ViewConstant.STACK_HEIGHT, ViewConstant.ACTION_WIDTH);
  }

  public SizePoint getAvatar() {
    return avatar;
  }

  public SizePoint getAvatarFrame() {
    return avatarFrame;
  }

  public SizePoint getCard(int idx) {
    return idx == 0 ? card1 : card2;
  }

  public TextPoint getName() {
    return name;
  }

  public TextPoint getStack() {
    return stack;
  }

  public TextPoint getPosition() {
    return position;
  }

  public TextPoint getAction() {
    return action;
  }

  public TextPoint getMessage() {
    return message;
  }

  private boolean isButtonOnTop() {
    return positionId == 0;
  }

  public Point getButton() {
    return isButtonOnTop() ? buttonTop : buttonBottom;
  }

  public Point getActionButton() {
    return !isButtonOnTop() ? buttonTop : buttonBottom;
  }


  @Override
  public String toString() {
    return "PlayerUICoordinates [avatar=" + avatar.getPoint() + ", name=" + name.getPoint()
        + ", avatarFrame=" + avatarFrame.getPoint() + ", card1=" + card1.getPoint() + ", card2="
        + card2.getPoint() + ", action=" + action.getPoint() + ", position=" + position.getPoint()
        + ", stack=" + stack.getPoint() + "]";
  }

}
