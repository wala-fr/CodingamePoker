package com.codingame.model.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import com.codingame.game.Player;
import com.codingame.model.object.enumeration.ActionType;
import com.codingame.model.object.enumeration.Position;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.variable.Parameter;

public class Board {

  private Deck deck = new Deck();

  private int handNb;
  private int level;

  private int smallBlind;
  private int bigBlind;

  private int pot;
//  private int totalBetAmount;
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
  private int[] winnings;
  private boolean over;
  // TODO suppress
  private int assertStacks;

  private List<Integer> bestPlayers = new ArrayList<>();


  // private int playerEliminatedNb;

  public Board(int playerNb, int bbId) {
    players = new ArrayList<>(playerNb);
    this.playerNb = playerNb;
    winnings = new int[playerNb];
    for (int i = 0; i < playerNb; i++) {
      players.add(new PlayerModel(i));
    }
    smallBlind = Parameter.SMALL_BLINB;
    bigBlind = Parameter.BIG_BLINB;
    level = 1;

    over = true;

    handNb = -1;

    this.bbId = bbId;
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
//    sbId = -1;
//    bbId = -1;
    pot = 0;

    dealPositions.clear();
    bestPlayers.clear();

    resetRound();
    if (!end) {
      handNb++;
      increaseLevel();
      initPositions();
    }
  }

  private void resetRound() {
    players.forEach(p -> p.resetRound());
//    totalBetAmount = 0;
    lastRoundRaise = 0;
    lastTotalRoundBet = 0;
    lastRoundRaisePlayerId = -1;
    raiseNb = 0;
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

  private void initPositions() {
    initAssertStacks();
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
      System.err.println(playerIndex +" " + playerNb +" " + sbId);
      PlayerModel player = players.get(playerIndex);
      if (!player.isFolded()) {
        nb++;
        if (sbId == -1) {
          sbId = playerIndex;
        } else  if (dealerId == -1) {
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
  }


  private void calculateNextBigBlindId() {
    if (handNb <= 0) {
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
    // calculateNextPlayer();
  }

  public void increaseLevel() {
    if (handNb > 0 && handNb % Parameter.HAND_NB_BY_LEVEL == 0) {
      level++;
      smallBlind *= Parameter.LEVEL_BLIND_MULTIPLICATOR;
      bigBlind *= Parameter.LEVEL_BLIND_MULTIPLICATOR;
    }
  }

  public void calculateNextPlayer() {
    int idx = nextPlayerId + 1;
    nextPlayerId = -1;
    for (int i = 0; i < players.size(); i++) {
      int playerIndex = idx % players.size();
      PlayerModel player = players.get(playerIndex);
      if (!player.isFolded() && !player.isAllIn()) {
        nextPlayerId = playerIndex;
        break;
      }
      idx++;
    }
    // throw new IllegalStateException();
  }

  public void betChips(PlayerModel player, int value) {
    // AssertUtils.test(value <= player.getStack());
    System.err.println("BETCHIPS " + player.getId() + " " + value);
    if (player.getStack() < value) {
      // ALL IN
      value = player.getStack();
    }
    player.bet(value);
    int raise = player.getRoundBetAmount() - lastTotalRoundBet;
    player.setRoundLastRaise(raise);
    System.err.println("lastTotalRoundBet " + lastTotalRoundBet + " raise " + raise + " " + player.getRoundBetAmount() + " " + value +" lastRoundRaisePlayerId "+lastRoundRaisePlayerId);

    if (raise >= lastRoundRaise) {
      // in case one all-in is not enough to be considered as as a raise
      lastRoundRaise = raise;
      lastRoundRaisePlayerId = player.getId();
      lastTotalRoundBet += raise;
      raiseNb++;
    }
    System.err.println("raise " + raise + " " + player.getRoundBetAmount() + " " + value +" lastRoundRaisePlayerId "+lastRoundRaisePlayerId +" lastRoundRaise "+lastRoundRaise);

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

  private boolean isAllFolded() {
    return getInvolvedPlayerNb() == 1;
  }

  public void endTurn() {
    PlayerModel player = players.get(nextPlayerId);
    lastPlayerId = nextPlayerId;
    player.setSpoken(true);
    // calculateNextPlayer();
    if (isAllFolded()) {
      // only one player still involved
      // System.err.println("isAllFolded");
      calculatePlayerWinnings();
    } else if (isTurnOver()) {
      if (players.stream()
        .filter(p -> !p.isFolded())
        // .peek(p -> System.err.println(p.getId()+ " " + p.isAllIn()))
        .filter(p -> !p.isAllIn())
        .count() <= 1) {
        // no more bet possible due to all-ins
        // System.err.println("no more bet possible due to all-ins");
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
    // if (!over) {
    // }
    // System.err.println("nextPlayerId " + nextPlayerId);
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

  public List<Integer> findWinner() {
    if (isAllFolded()) {
      // all player have fold except one who wins
      return players.stream()
        .filter(p -> !p.isFolded())
        .map(p -> p.getId())
        .collect(Collectors.toList());
    }
    int bestScore = Integer.MIN_VALUE;
    for (PlayerModel player : players) {
      if (!player.isFolded()) {
        player.calculateBestFiveCardhand(boardCards);
        int score = player.getBestPossibleHand().getValue();
        if (score > bestScore) {
          bestScore = score;
          bestPlayers.clear();
          bestPlayers.add(player.getId());
        } else if (score == bestScore) {
          bestPlayers.add(player.getId());
        }
      }
    }
    return bestPlayers;
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
    }
  }

  public void fold(PlayerModel player) {
    player.setFolded(true);
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
    return getMaxChipInAction() == player.getTotalBetAmount();
  }

  // public boolean isCallPossible() {
  // Player player = players.get(nextPlayerId);
  // return player.getStack() > calculateCallAmount(player);
  // }

  // public boolean isRaisePossible(int amount) {
  // Player player = players.get(nextPlayerId);
  // return player.getStack() > calculateRaiseTotalAmount(player, amount);
  // }

  // public boolean calculateRaiseRealAmount(int amount) {
  // Player player = players.get(nextPlayerId);
  // int call = totalBetAmount - player.getRoundBetAmount();
  // int total = amount + call;
  //
  // // player.getRoundBetAmount() lastRaise + amount;
  // return player.getStack() > calculateRaiseTotalAmount(player, amount);
  // }

  public int calculateCallAmount(PlayerModel player) {
    return getMaxChipInAction() - player.getTotalBetAmount();
  }

  // public int calculateRaiseTotalAmount(Player player, int amount) {
  // return calculateCallAmount(player) + amount;
  // }


  public int getMaxChipInAction() {
    int ret = players.stream().mapToInt(p -> p.getTotalBetAmount()).max().getAsInt();
    // in case bigBlind player can not pay the blinds
    return Math.max(bigBlind, ret);
  }


  // public void doBet(int amount) {
  // Player player = players.get(nextPlayerId);
  // if (player.isFolded()) {
  // throw new IllegalStateException("FOLD PLAYER IS BETTING ??");
  // }
  // if (amount == 0) {
  // if (player.isSpoken()) {
  // // FOLD
  // player.setFolded(true);
  // } else {
  // // CHECK
  // }
  // } else {
  // int maxAmount = calculateMaxChipInAction();
  // int callAmount = maxAmount - player.getChipInAction();
  // if (amount < callAmount) {
  // // CALL
  // amount = callAmount;
  // }
  // bet(player, amount);
  // }
  // endBet();
  // }

  // public void endBet() {
  //
  // }

  public boolean isTurnOver() {
    int maxAmount = calculateMaxChipInAction();
    for (PlayerModel player : players) {
      if (!player.isFolded() && !player.isAllIn()) {
        if (!player.isSpoken()) {
          return false;
        }
        if (player.getTotalBetAmount() < maxAmount) {
          return false;
        }
      }
    }
    return true;
  }

  private int calculateMaxChipInAction() {
    return players.stream().mapToInt(p -> p.getTotalBetAmount()).max().getAsInt();
  }

  public void calculatePlayerWinnings() {
    for (int i = 0; i < winnings.length; i++) {
      winnings[i] = 0;
    }
    cutBiggestAllIn();
    resolveSidePot(0);
    resolveDeadMoney();
    for (int i = 0; i < winnings.length; i++) {
      players.get(i).addToStack(winnings[i]);
    }
    calculateEliminationRanks();

    players.forEach(p -> p.resetEndTurn());
    pot = 0;

    over = true;
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

  public void eliminatePlayer(int id) {
    PlayerModel player = getPlayer(id);
    fold(player);
    assertStacks -= player.getStack();
    player.setStack(0);
    int nextRank = getNextRank();
    player.setEliminationRank(nextRank);
  }

  private int getNextRank() {
    return ((int) players.stream()
      .mapToInt(p -> p.getEliminationRank())
      .filter(p -> p >= 0)
      .count());
  }

  private void cutBiggestAllIn() {
    // so the biggest all-in player get back the chip excess
    List<PlayerModel> sortedPlayers = players.stream()
      .filter(p -> !p.isFolded())
      .sorted(Comparator.<PlayerModel>comparingInt(p -> p.getTotalBetAmount()).reversed())
      .collect(Collectors.toList());
    if (sortedPlayers.size() > 1) {
      AssertUtils.test(sortedPlayers.size() >= 2);
      PlayerModel player1 = sortedPlayers.get(0);
      PlayerModel player2 = sortedPlayers.get(1);
      int delta = player1.getTotalBetAmount() - player2.getTotalBetAmount();
      AssertUtils.test(delta >= 0);
      if (delta > 0) {
        betChips(player1, -delta);
        if (player1.getId() == lastRoundRaisePlayerId) {
          lastRoundRaise -= delta;
          lastTotalRoundBet -= delta;
        }
      }
    }
  }

  private void resolveSidePot(int alreadyDoneBet) {
    int playerInvolvedNb = getInvolvedPlayerNb();
    IntSummaryStatistics summary = players.stream()
      .filter(p -> !p.isFolded())
      .mapToInt(p -> p.getTotalBetAmount())
      .summaryStatistics();
    int minBet = summary.getMin();
    boolean allIn = minBet != summary.getMax();
    int minimumBetAmountPerPlayer = minBet - alreadyDoneBet;
    alreadyDoneBet += minimumBetAmountPerPlayer;
    if (!allIn || playerInvolvedNb <= 2) {
      applyWinnings(minimumBetAmountPerPlayer);
      return;
    }
    applyWinnings(minimumBetAmountPerPlayer);
    players.stream()
      .filter(p -> !p.isFolded() && p.getTotalBetAmount() == minBet)
      .forEach(p -> p.setFolded(true));
    resolveSidePot(alreadyDoneBet);
  }

  private int getInvolvedPlayerNb() {
    return (int) players.stream().filter(p -> !p.isFolded()).count();
  }

  private void applyWinnings(int minBet) {
    List<Integer> winnerIds = findWinner();
    int playerInvolvedNb = getInvolvedPlayerNb();

    int potSplit = (minBet * playerInvolvedNb) / winnerIds.size();
    int remaining = (minBet * playerInvolvedNb) % winnerIds.size();

    for (Integer playerId : winnerIds) {
      winnings[playerId] += potSplit;
    }
    // Odd chips go to first player in game order
    int oddChipWinningPlayerId = dealerId + 1;
    while (true) {
      oddChipWinningPlayerId %= playerNb;
      if (winnerIds.contains(oddChipWinningPlayerId)) {
        break;
      }
      oddChipWinningPlayerId++;
    }
    winnings[oddChipWinningPlayerId] += remaining;
  }

  private void resolveDeadMoney() {
    int payout = 0;
    int winnerNb = 0;
    for (int i = 0; i < winnings.length; i++) {
      if (winnings[i] > 0) {
        payout += winnings[i];
        winnerNb++;
      }
    }
    int deadMoney = pot - payout;
    for (int i = 0; i < winnings.length; i++) {
      if (winnings[i] > 0) {
        winnings[i] += deadMoney / winnerNb;
      }
    }
    int remaining = deadMoney % winnerNb;
    int oddChipWinningPlayerId = dealerId + 1;
    while (true) {
      oddChipWinningPlayerId %= playerNb;
      if (winnings[oddChipWinningPlayerId] > 0) {
        break;
      }
      oddChipWinningPlayerId++;
    }
    winnings[oddChipWinningPlayerId] += remaining;
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

  public int getGameNb() {
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

  public List<Integer> getBestPlayers() {
    return bestPlayers;
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
      // ret.append("Player ");
      // ret.append(player.getId());
      // ret.append(' ');
      // ret.append(getPosition(id));
      // ret.append(' ');
      // ret.append(player.getStatus());
      // ret.append(' ');
      // ret.append(player.getTotalBetAmount());
      // ret.append(' ');
      // ret.append(player.getRoundBetAmount());
    }
    return ret.toString();
  }
}
