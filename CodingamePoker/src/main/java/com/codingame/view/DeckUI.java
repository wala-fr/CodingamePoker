package com.codingame.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.toggle.ToggleModule;
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

  @Inject
  private ToggleModule toggleModule;

  private Sprite[] cards;
  private Sprite[] debugCards;

  private int nextCardIndex;
  private int discardCardIndex;

  private Map<Card, Integer> cardToIndex = new HashMap<>();

  private int zIndex;

  private void init() {
    if (cards == null) {
      cards = new Sprite[52];
      init(cards, false);
      debugCards = new Sprite[52];
      init(debugCards, true);
    }
  }

  private void init(Sprite[] cards, boolean debug) {
    for (int i = 0; i < cards.length; i++) {
      cards[i] = game.getGraphics()
        .createSprite()
        .setImage(ViewUtils.getCardBackUrl())
        .setX(0)
        .setY(0)
        .setBaseWidth(ViewConstant.CARD_WIDTH)
        .setBaseHeight(ViewConstant.CARD_HEIGHT);
      toggleModule.displayOnToggleState(cards[i], "debug", debug);
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
      zIndex = ViewConstant.Z_INDEX_CARD_DEAL;

      // TODO
      game.commitWorldState(game.isFirstRound() ? 0 : Phase.INIT_DECK.getEndTime());
    }
  }

  private void resetCard(int i) {
    resetCard(i, false);
    resetCard(i, true);
  }

  private void resetCard(int i, boolean debug) {
    Sprite card = getSpriteCard(i, debug);
    Board board = game.getBoard();
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
    game.setTime(ViewConstant.MAX_TIME);
    game.commitWorldState();
  }

  public void deal() {
    Board board = game.getBoard();
    List<DealPosition> dealPositions = board.getDealPositions();
    int cardNb = dealPositions.size() - nextCardIndex;
    // TO AVOID THAT THE LAST CARD BUG : WITH A TIME OF 1 IT MOVES FIRST
    double delta = (0.9 - game.getTime()) / cardNb;
    logger.debug("delta {} {}", delta, cardNb);
    if (delta > 0.1) {
      delta = 0.1;
    }
    logger.debug("delta {}", delta);
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
      game.incrementTime(delta);
      // commitCardState(cardNb, delta);
      logger.debug("time {}", game.getTime());
    }
  }

  // private void commitCardState(int i, double delta) {
  // game.commitEntityState(cards[i], delta);
  // game.commitEntityState(debugCards[i], delta);
  // }

  private Point calculateDiscardCardPosition() {
    discardCardIndex++;
    Board board = game.getBoard();
    return ViewUtils.getDiscardCardPosition(board.getDealerId(), discardCardIndex);
  }

  private void move(Point position, boolean visible, Card card, int index) {
    move(position, visible, card, index, false);
    move(position, visible, card, index, true);
  }

  private void move(Point position, boolean visible, Card card, int index, boolean debug) {
    Sprite sprite = getSpriteCard(index, debug);
    if (visible) {
      sprite.setImage(ViewUtils.getCardUrl(card, debug));
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
    
    // put before commit state if only visible in position ???
    position.setPosition(sprite);
  }


  public void highlightCard(Card card, boolean win) {
    int index = cardToIndex.get(card);
    tint(index, win, false);
    tint(index, win, true);
  }

  private void tint(int index, boolean win, boolean debug) {
    Sprite sprite = getSpriteCard(index, debug);
    sprite.setTint(win ? 0x82fc4c : 0xfa6f6f, Curve.LINEAR);
  }

  private Sprite getSpriteCard(int index, boolean debug) {
    Sprite sprite = (debug ? debugCards : cards)[index];
    return sprite;
  }

}
