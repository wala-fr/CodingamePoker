package com.codingame.view;

import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.model.object.Card;
import com.codingame.model.object.DealPosition;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.AssertUtils;
import com.codingame.view.object.Game;
import com.codingame.view.object.Point;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;

public class CardSprite {

  private Sprite card;
  private Sprite debugCard;
  private Sprite hiddenCard;
  private Sprite showCard;

  public CardSprite(Game game) {
    card = ViewUtils.createCard(game);
    debugCard = ViewUtils.createCard(game);
    hiddenCard = ViewUtils.createCard(game);
  }

  public void reset(Game game, int zIndex) {
    Board board = game.getBoard();
    Point position = ViewUtils.getDeckCardPosition(board.getDealerId());
    setPosition(position);
    setZIndex(zIndex);
    hiddenCard.setVisible(true);

    card.setTint(0xFFFFFF, Curve.IMMEDIATE);
    debugCard.setTint(0xFFFFFF, Curve.IMMEDIATE);

    removeShowCard(game);
    // setTooltipText("", game);
  }

  public void setZIndex(int zIndex) {
    card.setZIndex(zIndex);
    debugCard.setZIndex(zIndex + 1);
    if (showCard != null) {
      showCard.setZIndex(zIndex + 2);
    }
    hiddenCard.setZIndex(zIndex + 3);
  }

  private void removeShowCard(Game game) {
    if (showCard != null) {
      revealOpponentCard(game);
      showCard = null;
    }
  }

  public void revealOpponentCard(Game game) {
    ViewUtils.hide(showCard, game);
  }

  public void setShowCard(Sprite card, Game game) {
    showCard = card;

    AssertUtils.test(showCard.getX() == card.getX());
    AssertUtils.test(showCard.getY() == card.getY());

    // TODO remove and add assert showCard at deck position
    // showCard.setX(card.getX(), Curve.IMMEDIATE);
    // showCard.setY(card.getY(), Curve.IMMEDIATE);
    game.commitEntityState(showCard);
  }

  public void tint(boolean win, Game game) {
    int color = win ? 0x82fc4c : 0xfa6f6f;
    AssertUtils.test(!hiddenCard.isVisible());
    revealOpponentCard(game);
    tint(color);
  }

  private void tint(int color) {
    card.setTint(color, Curve.LINEAR);
    debugCard.setTint(color, Curve.LINEAR);
  }

  public void tintFoldedCard() {
    int color = ViewConstant.FOLD_COLOR;
    tint(color);
    showCard.setTint(color);
    hiddenCard.setTint(color); // useless (hidden card is always hidden for folded card)
  }

  public void showFoldedCard(Game game) {
    // hiddenCard isn't useful for the card that allows to show the folded cards
    hiddenCard.setVisible(false);
    setZIndex(ViewConstant.Z_INDEX_CARD - 10);
  }

  public void hide(Game game) {
    ViewUtils.hide(card, game);
    ViewUtils.hide(debugCard, game);
    ViewUtils.hide(hiddenCard, game);
    ViewUtils.hide(showCard, game);
  }

  public void move(Point position, DealPosition dealPosition, Card cardModel, int zIndex,
      double delta, Game game) {
    setCard(cardModel, game);

    boolean visible = dealPosition != null && !dealPosition.isBurned();
    hiddenCard.setVisible(!visible);
    // if (visible) {
    // setTooltipText(cardModel.getLabel(), game);
    // } else {
    // setTooltipText("", game);
    // }

    setZIndex(zIndex);
    // put before commit state if only visible in position ???
    commitEntityState(delta / 2, game);

    setPosition(position);
    commitEntityState(delta / 2, game);
  }

  public void setCard(Card cardModel, Game game) {
    if (cardModel != null) {
      card.setImage(ViewUtils.getCardUrl(cardModel, false));
      debugCard.setImage(ViewUtils.getCardUrl(cardModel, true));
      game.commitEntityState(card, debugCard);
    }
  }

  public void setTooltipText(String str, Game game) {
    game.getTooltips().setTooltipText(card, str);
    game.getTooltips().setTooltipText(debugCard, str);
  }

  public void setPosition(Point position) {
    position.setPosition(card);
    position.setPosition(debugCard);
    position.setPosition(hiddenCard);
    if (showCard != null) {
      position.setPosition(showCard);
    }
  }

  public void commitEntityState(double t, Game game) {
    game.commitEntityState(t, card, debugCard, hiddenCard);
    if (showCard != null) {
      game.commitEntityState(showCard);
    }
  }

  public int getCardId() {
    return card.getId();
  }

  public int getDebugCardId() {
    return debugCard.getId();
  }

  public int getShowCardId() {
    return showCard.getId();
  }

}
