package com.codingame.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.model.object.Card;
import com.codingame.model.object.DealPosition;
import com.codingame.model.object.board.Board;
import com.codingame.view.data.GlobalViewData;
import com.codingame.view.object.Frame;
import com.codingame.view.object.Game;
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

  private CardSprite[] cards;

  // cards to show / hide opponents cards (fog of war in settings panel)
  private Sprite[] showCards;
  
  // to allows to show the cards even when they've been folded
  private CardSprite[] foldCards;

  private int nextCardIndex;
  private int discardCardIndex;

  private Map<Card, Integer> cardToIndex = new HashMap<>();

  private int zIndex;

  private void init() {
    if (cards == null) {
      GlobalViewData globalViewData = game.getGlobalViewData();
      cards = new CardSprite[ViewConstant.CARD_MIN_NB];
      for (int i = 0; i < cards.length; i++) {
        cards[i] = new CardSprite(game);
        globalViewData.addCard(cards[i]);
      }
      showCards = new Sprite[8];
      for (int i = 0; i < showCards.length; i++) {
        showCards[i] = ViewUtils.createCard(game);
        ViewUtils.getDeckCardPosition(0).setPosition(showCards[i]);
//        game.getTooltips().setTooltipText(showCards[i], "showCard " + i);
        ViewUtils.hide(showCards[i], game);
        globalViewData.addShowOpponentCard(showCards[i], i / 2);
      }
      foldCards = new CardSprite[8];
      for (int i = 0; i < foldCards.length; i++) {
        CardSprite foldCard = new CardSprite(game);
        foldCards[i] = foldCard;
        Sprite showCard = ViewUtils.createCard(game);
        foldCard.setShowCard(showCard, game);
        foldCard.setPosition(ViewUtils.getPlayerCardPosition(game, i / 2, i % 2));
        foldCard.hide(game);
        foldCard.tintFoldedCard();
        globalViewData.addFoldCard(foldCard);
        globalViewData.addShowOpponentCard(showCard, i / 2);
        globalViewData.addCard(foldCard);
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
        cards[i].reset(game, zIndex);
        incrementZIndex();
      }
      zIndex = ViewConstant.Z_INDEX_CARD_DEAL;
      for (int i = 0; i < foldCards.length; i++) {
        foldCards[i].hide(game);
      }
      // TODO
      game.commitWorldState(game.isFirstRound() ? 0 : 0.1);
    }
  }

  public void foldPlayerId(int playerId) {
    if (!game.isFrame(Frame.ACTION)) {
      return;
    }
    Board board = game.getBoard();

    // to keep showing the folded cards
    for (int j = 0; j < 2; j++) {
      foldCards[2 * playerId + j].showFoldedCard(game);;
    }
    game.commitWorldState();
    List<DealPosition> dealPositions = board.getDealPositions();
    int i = 0;
    for (DealPosition dealPosition : dealPositions) {
      if (dealPosition.getId() == playerId) {
        Point position = calculateDiscardCardPosition();
        move(position, null, null, i, 0);
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
    
    // add show / hide opponent card
    for (int i = nextCardIndex; i < dealPositions.size(); i++) {
      DealPosition dealPosition = dealPositions.get(i);
      if (dealPosition.isPlayer()) {
        int showCardIndex = 2 * dealPosition.getId() + dealPosition.getIndex();
        cards[i].setShowCard(showCards[showCardIndex], game);
        foldCards[showCardIndex].setCard(board.getCard(dealPosition), game);
      }
    }
    for (; nextCardIndex < dealPositions.size(); nextCardIndex++) {
      DealPosition dealPosition = dealPositions.get(nextCardIndex);
      Card card = board.getCard(dealPosition);
      Point position;
      if (dealPosition.isBurned()) {
        position = calculateDiscardCardPosition();
      } else {
        position = ViewUtils.getCardPosition(game, dealPosition);
      }
      move(position, dealPosition, card, nextCardIndex, delta);
      logger.debug("time {}", game.getTime());
    }
  }

  private Point calculateDiscardCardPosition() {
    discardCardIndex++;
    Board board = game.getBoard();
    return ViewUtils.getDiscardCardPosition(board.getDealerId(), discardCardIndex);
  }

  private void move(Point position, DealPosition dealPosition, Card card, int index, double delta) {
    CardSprite sprite = cards[index];
    sprite.move(position, dealPosition, card, zIndex, delta, game);
    if (dealPosition != null && !dealPosition.isBurned()) {
      cardToIndex.put(card, index);
    } else {
      cardToIndex.remove(card);
    }
    incrementZIndex();
  }

  private void incrementZIndex() {
    zIndex += 4;
  }

  public void highlightCard(Card card, boolean win) {
    int index = cardToIndex.get(card);
    cards[index].tint(win, game);
  }

  public void revealOpponentFoldCard() {
    for (int i = 0; i < foldCards.length; i++) {
      foldCards[i].revealOpponentCard(game);;
    }    
  }

}
