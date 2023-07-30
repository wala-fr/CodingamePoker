package com.codingame.model.object.board;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.Card;
import com.codingame.model.object.DealPosition;
import com.codingame.model.object.Deck;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.enumeration.ActionType;
import com.codingame.model.object.enumeration.BoardStatus;
import com.codingame.model.object.enumeration.Position;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.variable.Parameter;

public class Board {

  private static final Logger logger = LoggerFactory.getLogger(Board.class);

  private Deck deck = new Deck();

  private int firstBigBlindId;

  private int handNb;
  private int level;

  private int smallBlind;
  private int bigBlind;

  private int pot;
  // private int totalBetAmount;
  private int lastRoundRaise;
  private int lastRoundRaisePlayerId;
  private int raiseNb;

  // lastBet (ignore all-ins that aren't big enough to re-open the bets)
  private int lastTotalRoundBet;

  private List<Card> boardCards = new ArrayList<>();
  private List<Card> burnedCards = new ArrayList<>();
  private List<ActionInfo> actions = new ArrayList<>();

  // for the graphics
  private List<DealPosition> dealPositions = new ArrayList<>();

  private List<PlayerModel> players;
  private int playerNb;
  private int dealerId;
  private int sbId;
  private int bbId;

  private int lastPlayerId;
  private int nextPlayerId;
  private boolean over;
  // TODO suppress
  private int assertStacks;

  private WinningCalculator winningCalculator;

  private boolean calculateWinChance;

  public Board(int playerNb, int bbId) {
    players = new ArrayList<>(playerNb);
    this.playerNb = playerNb;
    for (int i = 0; i < playerNb; i++) {
      players.add(new PlayerModel(i, playerNb));
    }
    smallBlind = Parameter.SMALL_BLINB;
    bigBlind = Parameter.BIG_BLINB;
    level = 1;

    over = true;

    handNb = 0; // will immediatly be increased

    this.bbId = bbId;
    firstBigBlindId = bbId;

    winningCalculator = new WinningCalculator(this);

    initAssertStacks();
  }

  public void resetHand() {
    resetHand(false);
  }

  public void resetHand(boolean end) {
    deck.reset();
    boardCards.clear();
    players.forEach(p -> p.reset());
    burnedCards.clear();
    actions.clear();
    over = false;
    pot = 0;

    dealPositions.clear();
    resetRound();
    if (!end) {
      handNb++;
      increaseLevel();
      initPositions();
    }
  }

  private void resetRound() {
    players.forEach(p -> p.resetRound());
    lastRoundRaise = 0;
    lastTotalRoundBet = 0;
    lastRoundRaisePlayerId = -1;
    raiseNb = 0;
    calculateWinChance = false;
  }

  public void initDeck() {
    deck.reset();
    deck.shuffle();
    deck.cut();
  }

  public void assertStacks() {
    int sum = pot;
    for (PlayerModel player : players) {
      sum += player.getStack();
    }
    AssertUtils.test(sum == assertStacks, sum, assertStacks, pot);
  }

  public void initAssertStacks() {
    assertStacks = 0;
    for (PlayerModel player : players) {
      assertStacks += player.getStack();
    }
  }

  public void initPositions() {
    calculateNextBigBlindId();
    int startIndex = bbId;
    sbId = -1;
    dealerId = -1;
    int nb = 0;
    for (int i = 0; i < playerNb; i++) {
      int playerIndex = (startIndex - 1 - i) % playerNb;
      if (playerIndex < 0) {
        playerIndex += playerNb;
      }
      logger.debug("playerIndex {}", playerIndex);

      PlayerModel player = players.get(playerIndex);
      if (!player.isFolded()) {
        nb++;
        if (sbId == -1) {
          sbId = playerIndex;
        } else if (dealerId == -1) {
          dealerId = playerIndex;
        }
      }
    }
    // if only two player the smallBlind is the dealer
    if (nb == 2) {
      dealerId = sbId;
    }
    lastPlayerId = -1;
    nextPlayerId = bbId;
    lastTotalRoundBet = bigBlind;
    lastRoundRaise = smallBlind;
    lastRoundRaisePlayerId = -1;
    raiseNb = 1;
    logger.debug("dealerId {}", dealerId);
  }


  private void calculateNextBigBlindId() {
    if (handNb <= 1) {
      return;
    } else {
      do {
        bbId++;
        bbId %= playerNb;
      } while (players.get(bbId).getStack() == 0);
    }
  }


  public void initBlind() {
    for (int i = 0; i < players.size(); i++) {
      PlayerModel player = players.get(i);
      if (!player.isFolded()) {
        int bet = player.getId() == bbId ? bigBlind : smallBlind;
        // If the player's stack is larger than the small blind but smaller than the big blind,
        // they will be considered all-in in any position other than the small blind, assuming
        // they fold for their option.
        if (player.getStack() < (player.getId() == sbId ? smallBlind : bigBlind)) {
          bet = player.getStack();
        }
        betChips(player, bet);
      }
    }
  }

  public void increaseLevel() {
    if (handNb % Parameter.HAND_NB_BY_LEVEL == 0) {
      level++;
      smallBlind *= Parameter.LEVEL_BLIND_MULTIPLIER;
      bigBlind *= Parameter.LEVEL_BLIND_MULTIPLIER;
    }
  }

  public void calculateNextPlayer() {
    int idx = nextPlayerId + 1;
    nextPlayerId = -1;
    for (int i = 0; i < playerNb; i++) {
      int playerIndex = idx % playerNb;
      PlayerModel player = players.get(playerIndex);
      if (!player.isFolded() && !player.isAllIn()) {
        nextPlayerId = playerIndex;
        break;
      }
      idx++;
    }
    logger.info("calculateNextPlayer {}", nextPlayerId);

    // AssertUtils.test(nextPlayerId >= 0, nextPlayerId);
    // throw new IllegalStateException();
  }

  public void betChips(PlayerModel player, int value) {
    // AssertUtils.test(value <= player.getStack());
    if (player.getStack() < value) {
      // ALL IN
      value = player.getStack();
    }
    player.bet(value);
    int raise = player.getRoundBetAmount() - lastTotalRoundBet;
    if ((isFirstBet() && raise >= bigBlind) || (!isFirstBet() && raise >= lastRoundRaise)) {
      // in case one all-in is not enough to be considered as as a raise
      lastRoundRaise = raise;
      lastRoundRaisePlayerId = player.getId();
      lastTotalRoundBet += raise;
      raiseNb++;
    }
    logger.info("lastRoundRaisePlayerId {} : ${}", lastRoundRaisePlayerId, lastRoundRaise);
    pot += value;
  }

  public void dealFirst() {
    dealPlayerCard();
    dealPlayerCard();
  }

  private void dealPlayerCard() {
    int idx = dealerId + 1;
    for (int i = 0; i < players.size(); i++) {
      int playerIndex = idx % players.size();
      PlayerModel player = players.get(playerIndex);
      if (!player.isFolded()) {
        dealPositions.add(DealPosition.createPlayerCardPosition(playerIndex,
            player.getHand().calculateCardIndex()));
        player.dealCard(deck);
      }
      idx++;
    }
  }

  public void burn() {
    dealPositions.add(DealPosition.createBurnedCardPosition(burnedCards.size()));
    burnedCards.add(deck.dealCard());
  }

  private boolean isOnlyOneNotFolded() {
    return calculatNotFoldedPlayerNb() == 1;
  }

  public void endTurn() {
    if (nextPlayerId != -1) {
      PlayerModel player = players.get(nextPlayerId);
      lastPlayerId = nextPlayerId;
      player.setSpoken(true);
    }
    logger.info("isTurnOver {} {}", isTurnOver(), nextPlayerId);
    if (isOnlyOneNotFolded()) {
      // only one player still involved
      logger.info("Only one player still involved. All other are folded.");
      calculatePlayerWinnings();
    } else if (isTurnOver()) {
      if (players.stream()
        .filter(p -> !p.isFolded())
        // .peek(p -> System.err.println(p.getId()+ " " + p.isAllIn()))
        .filter(p -> !p.isAllIn())
        .count() <= 1) {
        // no more bet possible due to all-ins
        logger.info("no more bet possible due to all-ins");
        dealAllBoardCards();
        calculatePlayerWinnings();
      } else {
        // System.err.println("isTurnOver");
        if (isAllBoardCardDealt()) {
          calculatePlayerWinnings();
        } else {
          dealBoardCards();
          lastPlayerId = -1;
          nextPlayerId = dealerId;
        }
      }
    }
  }

  public void dealAllBoardCards() {
    while (!isAllBoardCardDealt()) {
      dealBoardCards();
    }
  }

  public boolean isAllBoardCardDealt() {
    return getBoardCards().size() == 5;
  }

  public void dealBoardCards() {
    if (boardCards.isEmpty()) {
      dealFlop();
    } else {
      burn();
      dealOneBoardCard();
    }
    resetRound();
  }

  private void dealFlop() {
    burn();
    for (int i = 0; i < 3; i++) {
      dealOneBoardCard();
    }
  }

  private void dealOneBoardCard() {
    dealPositions.add(DealPosition.createBoardCardPosition(boardCards.size()));
    boardCards.add(deck.dealCard());
  }

  public void initPlayerBestHands() {
    if (calculatNotFoldedPlayerNb() > 1) {
      for (PlayerModel player : players) {
        player.calculateBestFiveCardhand(boardCards);
      }
    }
  }

  public List<Integer> findWinner() {
    return winningCalculator.findWinner();
  }

  public void doAction(ActionInfo actionInfo) {
    PlayerModel player = players.get(nextPlayerId);
    actions.add(actionInfo);
    Action action = actionInfo.getAction();
    ActionType type = action.getType();
    player.setLastAction(action);
    if (type == ActionType.FOLD) {
      fold(player);
    } else if (type == ActionType.CHECK) {
      check(player);
    } else if (type == ActionType.ALL_IN) {
      allIn(player);
    } else if (type == ActionType.CALL) {
      call(player);
    } else if (type == ActionType.BET) {
      bet(player, action.getAmount());
    } else if (type == ActionType.TIMEOUT) {
      timeout(player);
    } else {
      AssertUtils.test(type == ActionType.CHECK, type);
    }
  }

  public void fold(PlayerModel player) {
    player.setFolded(true);
    calculateWinChance = false;
  }

  public void check(PlayerModel player) {}

  public void allIn(PlayerModel player) {
    betChips(player, player.getStack());
  }

  public void call(PlayerModel player) {
    int call = calculateCallAmount(player);
    betChips(player, call);
  }

  public void bet(PlayerModel player, int amount) {
    betChips(player, amount);
  }

  public boolean isCheckPossible() {
    PlayerModel player = players.get(nextPlayerId);
    boolean checkPossible = calculateCallAmount(player) == 0;
    if (checkPossible) {
      AssertUtils.test((calculateBoardStatus() == BoardStatus.PRE_FLOP && nextPlayerId == bbId)
          || calculateRoundMaxChipInAction() == 0);
    }
    return checkPossible;
  }

  public boolean isBetPossible() {
    PlayerModel player = players.get(nextPlayerId);
    int canSpeakNb = (int) players.stream().filter(p -> p.canSpeak()).count();
    return !(canSpeakNb == 1 && calculateCallAmount(player) == 0);
  }

  public BoardStatus calculateBoardStatus() {
    return BoardStatus.getStatus(boardCards.size());
  }

  public boolean isPreFlop() {
    return calculateBoardStatus() == BoardStatus.PRE_FLOP;
  }

  public int calculateCallAmount(PlayerModel player) {
    if (isPreFlop()) {
      return calculateMaxChipInActionOrBigBlind() - player.getTotalBetAmount();
    } else {
      return calculateMaxChipInAction() - player.getTotalBetAmount();
    }
  }

  public int calculateMaxChipInActionOrBigBlind() {
    // in case bigBlind player can not pay the blinds
    return Math.max(bigBlind, calculateMaxChipInAction());
  }

  public int calculateMaxChipInAction() {
    int ret = players.stream().mapToInt(p -> p.getTotalBetAmount()).max().getAsInt();
    return ret;
  }

  public int calculateRoundMaxChipInAction() {
    int ret = players.stream().mapToInt(p -> p.getRoundBetAmount()).max().getAsInt();
    return ret;
  }

  public boolean isTurnOver() {
    int maxAmount = calculateMaxChipInAction();
    for (PlayerModel player : players) {
      if (!player.isFolded() && !player.isAllIn()) {
        if (!player.isSpoken()) {
          if (players.stream().noneMatch(p -> p.canSpeak() && p.getId() != player.getId())) {
            // case bb hasn't spoken yet but all other players are folded or all-in
            // and bb can't make a raise (all bets are less than the bb value)
            if (player.getRoundBetAmount() < calculateMaxChipInAction()) {
              return false;
            }
          } else {
            return false;
          }
        }
        if (player.getTotalBetAmount() < maxAmount) {
          return false;
        }
      }
    }
    return true;
  }

  private void calculatePlayerWinnings() {
    int[] winnings = winningCalculator.calculateWinnings();
    for (int i = 0; i < winnings.length; i++) {
      PlayerModel player = players.get(i);
      player.addToStack(winnings[i]);
    }
    calculateEliminationRanks();

    over = true;
  }

  /*
   * to shown winning on view end frame of showdown end
   */
  public void endTurnView() {
    if (over) {
      players.forEach(p -> p.resetEndTurnView());
      pot = 0;
    }
  }

  private void calculateEliminationRanks() {
    int nextRank = getNextRank();
    boolean change = true;
    while (change) {
      change = false;
      List<PlayerModel> eliminated = new ArrayList<>();
      int minStack = Integer.MAX_VALUE;
      for (PlayerModel player : players) {
        if (player.getEliminationRank() == -1 && player.getStack() == 0) {
          int stack = player.getTotalBetAmount();
          if (stack < minStack) {
            minStack = stack;
            eliminated.clear();
            eliminated.add(player);
          } else if (stack == minStack) {
            eliminated.add(player);
          }
        }
      }
      for (PlayerModel player : eliminated) {
        player.setEliminationRank(nextRank);
        player.setScore(nextRank - playerNb);
        change = true;
      }
      nextRank += eliminated.size();
    }
  }

  private void timeout(PlayerModel player) {
    assertStacks -= player.getStack();
    player.setStack(0);
    player.setTimeout(true);
    fold(player);
  }

  private int getNextRank() {
    return ((int) players.stream()
      .mapToInt(p -> p.getEliminationRank())
      .filter(p -> p >= 0)
      .count());
  }

  public boolean isFirstBet() {
    return lastRoundRaisePlayerId == -1;
  }

  public int calculatNotFoldedPlayerNb() {
    return (int) players.stream().filter(p -> !p.isFolded()).count();
  }

  public void cancelCurrentHand() {
    if (!over) {
      for (PlayerModel player : players) {
        player.cancelCurrentHand();
      }
      resetHand(false);
    }
  }

  public void calculateFinalScores() {
    for (PlayerModel player : players) {
      if (player.getStack() > 0) {
        player.setScore(player.getStack());
      }
    }
  }

  public int getNextPlayerId() {
    return nextPlayerId;
  }

  public int getLastPlayerId() {
    return lastPlayerId;
  }

  public Deck getDeck() {
    return deck;
  }

  public int getDealerId() {
    return dealerId;
  }

  public int getSbId() {
    return sbId;
  }

  public int getBbId() {
    return bbId;
  }

  public List<PlayerModel> getPlayers() {
    return players;
  }

  public PlayerModel getPlayer(int id) {
    return players.get(id);
  }

  public List<Card> getBoardCards() {
    return boardCards;
  }

  public boolean isOver() {
    return over;
  }

  public boolean isGameOver() {
    return over && players.stream().filter(p -> p.getStack() != 0).count() == 1;
  }

  public int getLastRoundRaisePlayerId() {
    return lastRoundRaisePlayerId;
  }

  public int getLastRoundRaise() {
    return lastRoundRaise;
  }

  public int getLastTotalRoundBet() {
    return lastTotalRoundBet;
  }

  public int getRaiseNb() {
    return raiseNb;
  }

  public void setRaiseNb(int raiseNb) {
    this.raiseNb = raiseNb;
  }

  public int getSmallBlind() {
    return smallBlind;
  }

  public int getBigBlind() {
    return bigBlind;
  }

  public int getLevel() {
    return level;
  }

  public int getPot() {
    return pot;
  }

  public int getHandNb() {
    return handNb;
  }

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder("Board [pot=" + pot + ", board=" + boardCards);
    for (PlayerModel player : players) {
      ret.append('\n');
      ret.append(player);
    }
    ret.append(", burned=");
    ret.append(burnedCards);

    ret.append(", lastRoundRaise=");
    ret.append(lastRoundRaise);

    ret.append(", lastRoundRaisePlayerId=");
    ret.append(lastRoundRaisePlayerId);

    ret.append(", lastTotalRoundBet=");
    ret.append(lastTotalRoundBet);

    ret.append(']');
    return ret.toString();
  }

  public Position getPosition(int id) {
    if (players.get(id).isEliminated()) {
      return Position.ELIMINATED;
    }
    if (id == sbId) {
      return Position.SMALL_BLIND;
    }
    if (id == bbId) {
      return Position.BIG_BLIND;
    }
    if (id == dealerId) {
      return Position.DEALER;
    }
    return Position.UTG;
  }

  public int getPlayerNb() {
    return playerNb;
  }

  public List<DealPosition> getDealPositions() {
    return dealPositions;
  }

  public List<Card> getBurnedCards() {
    return burnedCards;
  }

  public List<ActionInfo> getActions() {
    return actions;
  }

  public int getFirstBigBlindId() {
    return firstBigBlindId;
  }

  public boolean isCalculateWinChance() {
    return calculateWinChance;
  }

  public void setCalculateWinChance(boolean calculateWinChance) {
    this.calculateWinChance = calculateWinChance;
  }

  public Card getCard(DealPosition dealPosition) {
    int index = dealPosition.getIndex();
    int id = dealPosition.getId();

    if (dealPosition.isBoard()) {
      return boardCards.get(index);
    }
    if (dealPosition.isBurned()) {
      return burnedCards.get(index);
    }
    return players.get(id).getHand().getCard(index);
  }

  public String toPlayerStatesString() {
    StringBuilder ret = new StringBuilder("\nBoard [pot=" + pot + ", board=" + boardCards);
    for (PlayerModel player : players) {
      ret.append('\n');
      int id = player.getId();
      ret.append(String.format(over ? "Player %d | %15s | Stack= %5$7s"
          : "Player %d | %15s | %8s | %10s | Stack= %7s | Total= %7s | Round=%7s | %10s | %15s | %20s | %s",
          id, getPosition(id), player.getHand(), player.getStatus(), player.getStack(),
          player.getTotalBetAmount(), player.getRoundBetAmount(),
          player.getLastAction() == null ? "" : player.getLastAction(),
          lastRoundRaisePlayerId == id ? "RAISE " + lastRoundRaise : "", player.getMessage(this),
          nextPlayerId == id ? "***" : ""));
    }
    return ret.toString();
  }
}
