package com.codingame.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.model.object.Board;
import com.codingame.model.object.Card;
import com.codingame.model.object.DealPosition;
import com.codingame.view.object.Graphic;
import com.codingame.view.object.Phase;
import com.codingame.view.object.Point;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeckUI {

  @Inject
  private Graphic graphics;

  private Sprite[] cards;

  private int nextCardIndex;
  private int discardCardIndex;

  private Map<Card, Integer> cardToIndex = new HashMap<>();
  // private int gameNb = -1;

  private void init(Graphic graphics) {
    if (cards == null) {
      cards = new Sprite[52];
      for (int i = 0; i < cards.length; i++) {
        cards[i] = graphics.getGraphics()
          .createSprite()
          .setImage(ViewUtils.getCardBackUrl())
          .setX(0)
          .setY(0)
          .setBaseWidth(ViewConstant.CARD_WIDTH)
          .setBaseHeight(ViewConstant.CARD_HEIGHT);
      }
      // highlight = new Sprite[13];
      // for (int i = 0; i < highlight.length; i++) {
      // highlight[i] = graphics.getGraphics()
      // .createSprite()
      // .setImage(ViewUtils.getCardHighlightUrl())
      // .setX(0)
      // .setY(0)
      // .setBaseWidth(ViewConstant.CARD_WIDTH)
      // .setBaseHeight(ViewConstant.CARD_HEIGHT)
      // .setVisible(false);
      // }
    }
  }

  public void reset(Graphic graphic) {
    init(graphic);
    Board board = graphic.getBoard();
    if (board.getDealPositions().isEmpty()) {
      cardToIndex.clear();
      nextCardIndex = 0;
      discardCardIndex = 0;
      for (int i = 0; i < cards.length; i++) {
        resetCard(board, i);
      }
      // TODO
      graphic.commitWorldState(graphic.isFirstRound() ? 0 : Phase.INIT_DECK.getEndTime());
      System.err.println("getTime " + graphic.getTime());
    }
  }

  private void resetCard(Board board, int i) {
    Sprite card = cards[i];
    card.setImage(ViewUtils.getCardBackUrl());
    Point position = ViewUtils.getDeckCardPosition(board.getDealerId(), i);
    position.setPosition(card);
    card.setZIndex(i);
    card.setAlpha(1);
    card.setTint(0xFFFFFF);
    graphics.getTooltips().setTooltipText(card, "");
  }

  public void foldPlayerId(Board board, int playerId) {
    if (!graphics.isAction()) {
      return;
    }
    List<DealPosition> dealPositions = board.getDealPositions();
    int i = 0;
    for (DealPosition dealPosition : dealPositions) {
      if (dealPosition.getId() == playerId) {
        Point position = ViewUtils.getDiscardCardPosition(board.getDealerId(), discardCardIndex);
        move(position, false, null, i, discardCardIndex);
        graphics.setTime(1);
        graphics.commitWorldState(0);

        discardCardIndex++;
      }
      i++;
    }
  }

  public void deal(Graphic graphics) {
    Board board = graphics.getBoard();
    List<DealPosition> dealPositions = board.getDealPositions();

    // TODO 0.8 beacuase first player can fold
    int cardDealNb = dealPositions.size() - nextCardIndex;
    double timeIncrement = (graphics.getPhase().getEndTime() - graphics.getTime()) / cardDealNb;

    for (; nextCardIndex < dealPositions.size(); nextCardIndex++) {
      DealPosition dealPosition = dealPositions.get(nextCardIndex);
      Point position = ViewUtils.getCardPosition(graphics, dealPosition);
      // System.err.println(dealPosition + " " + position);
      Card card = board.getCard(dealPosition);
      move(position, !dealPosition.isBurned(), card, nextCardIndex, nextCardIndex);
      graphics.commitWorldState(0.1);

      // graphics.incrementTime(timeIncrement);
      System.err
        .println("Time " + graphics.getTime() + " " + graphics.getPhase() + " " + dealPosition);
      // graphics.getGraphics().commitEntityState(graphics.getTime(), cards[nextCardIndex]);
      // graphics.commitWorldState(timeIncrement);
    }
  }

  private void move(Point position, boolean visible, Card card, int index, int zIndex) {
    // move sprite to position

    Sprite sprite = cards[index];
    // if (!visible) {
    // sprite.setImage(ViewUtils.getCardBackUrl());
    // graphics.commitEntityState(sprite);
    // }
    sprite.setZIndex(52 - index);
    position.setPosition(sprite);
    if (visible) {
      graphics.getTooltips().setTooltipText(sprite, card.getLabel());
      cardToIndex.put(card, index);
    } else {
      cardToIndex.remove(card);
      graphics.getTooltips().setTooltipText(sprite, "");
    }
    // TODO test change ???
    if (visible) {
      sprite.setImage(ViewUtils.getCardUrl(card));
    } else {
      sprite.setImage(ViewUtils.getCardBackUrl());
    }
  }

  public void highlightCard(Graphic graphics, Card card, boolean win) {
    int index = cardToIndex.get(card);
    Sprite sprite = cards[index];
//    sprite.setImage(ViewUtils.getCardHighlightUrl(card, win));
    sprite.setTint(win ? 0x82fc4c : 0xfa6f6f, Curve.LINEAR);
  }


}
