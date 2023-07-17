package com.codingame.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.model.object.Card;
import com.codingame.model.object.DealPosition;
import com.codingame.model.object.board.Board;
import com.codingame.view.object.Game;
import com.codingame.view.object.Phase;
import com.codingame.view.object.Point;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeckUI {

  private static final Logger logger = LoggerFactory.getLogger(DeckUI.class);

  @Inject
  private Game game;

  private Sprite[] cards;

  private int nextCardIndex;
  private int discardCardIndex;

  private Map<Card, Integer> cardToIndex = new HashMap<>();
  
  private int zIndex;
  
  private void init() {
    if (cards == null) {
      cards = new Sprite[52];
      for (int i = 0; i < cards.length; i++) {
        cards[i] = game.getGraphics()
          .createSprite()
          .setImage(ViewUtils.getCardBackUrl())
          .setX(0)
          .setY(0)
          .setBaseWidth(ViewConstant.CARD_WIDTH)
          .setBaseHeight(ViewConstant.CARD_HEIGHT);
      }
    }
  }

  public void reset() {
    init();
    Board board = game.getBoard();
    if (board.getDealPositions().isEmpty()) {
      zIndex = ViewConstant.Z_INDEX_CARD;
      cardToIndex.clear();
      nextCardIndex = 0;
      discardCardIndex = 0;
      for (int i = cards.length - 1; i >= 0; i--) {
        resetCard(i);
      }
      // TODO
      game.commitWorldState(game.isFirstRound() ? 0 : Phase.INIT_DECK.getEndTime());
    }
  }

  private void resetCard(int i) {
    Board board = game.getBoard();
    Sprite card = cards[i];
    card.setImage(ViewUtils.getCardBackUrl());
    Point position = ViewUtils.getDeckCardPosition(board.getDealerId(), i);
    position.setPosition(card);
    setZIndex(card);
    card.setAlpha(1);
    card.setTint(0xFFFFFF, Curve.IMMEDIATE);
    game.getTooltips().setTooltipText(card, "");
  }
  
  private void setZIndex(Sprite card) {
    card.setZIndex(zIndex);
    zIndex++;
  }

  public void foldPlayerId(int playerId) {
    if (!game.isAction()) {
      return;
    }
    Board board = game.getBoard();

    List<DealPosition> dealPositions = board.getDealPositions();
    int i = 0;
    for (DealPosition dealPosition : dealPositions) {
      if (dealPosition.getId() == playerId) {
        Point position = calculateDiscardCardPosition();
        move(position, false, null, i);
      }
      i++;
    }
    game.setTime(1);
    game.commitWorldState(0);
  }

  public void deal() {
    Board board = game.getBoard();
    List<DealPosition> dealPositions = board.getDealPositions();

    for (; nextCardIndex < dealPositions.size(); nextCardIndex++) {
      DealPosition dealPosition = dealPositions.get(nextCardIndex);
      Card card = board.getCard(dealPosition);
      Point position;
      if (dealPosition.isBurned()) {
        position = calculateDiscardCardPosition();
      } else {
        position = ViewUtils.getCardPosition(game, dealPosition);
      }
      move(position, !dealPosition.isBurned(), card, nextCardIndex);
      game.commitEntityState(cards[nextCardIndex], 0.1);
    }
  }
  
  private Point calculateDiscardCardPosition() {
    discardCardIndex++;
    Board board = game.getBoard();
    return ViewUtils.getDiscardCardPosition(board.getDealerId(), discardCardIndex);
  }

  private void move(Point position, boolean visible, Card card, int index) {
    Sprite sprite = cards[index];
    if (visible) {
      sprite.setImage(ViewUtils.getCardUrl(card));
    } else {
      sprite.setImage(ViewUtils.getCardBackUrl());
    }
    setZIndex(sprite);
    
    if (visible) {
      game.getTooltips().setTooltipText(sprite, card.getLabel());
      cardToIndex.put(card, index);
    } else {
      cardToIndex.remove(card);
      game.getTooltips().setTooltipText(sprite, "");
    }
    
    game.commitEntityState(sprite);

    position.setPosition(sprite);

  }

  public void highlightCard(Card card, boolean win) {
    int index = cardToIndex.get(card);
    Sprite sprite = cards[index];
    sprite.setTint(win ? 0x82fc4c : 0xfa6f6f, Curve.LINEAR);
  }

}
