package com.codingame.input;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codingame.game.Player;
import com.codingame.game.RefereeParameter;
import com.codingame.model.object.Action;
import com.codingame.model.object.ActionInfo;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.utils.ActionUtils;
import com.codingame.model.variable.Parameter;
import com.google.inject.Singleton;

@Singleton
public class InputSender {

  private static final Logger logger = LoggerFactory.getLogger(InputSender.class);

  private final RoundInfo[] roundInfos = new RoundInfo[RefereeParameter.MAX_TURN + 1];
  private final int[] lastSendRounds = new int[4]; // first turn is 1
  private final ShowDownInfo[] handInfos = new ShowDownInfo[RefereeParameter.MAX_TURN + 1];
  private final int[] lastSendHandNbs = new int[4]; // first handNb is 1
  private final boolean[] sendGameInput = new boolean[4]; // if the 4 3 first player fold then the third player won't play in fourth (automatically win)

  public void updateRoundInfo(Board board, ActionInfo actionInfo, int turn) {
    RoundInfo roundInfo = new RoundInfo(board, actionInfo, turn);
    roundInfos[turn] = roundInfo;
    logger.debug("updateRoundInfo {} {}", turn, roundInfo);
  }
  
  public void updateShowDownInfo(Board board) {
    int handNb = board.getHandNb();
    if (handInfos[handNb] == null) {
      // create at the start of the hand so if a player time out we still have his cards (not yet eliminated)
      // eve if they will not be shown (since he's folded)
      ShowDownInfo handInfo = new ShowDownInfo(board);
      handInfos[handNb] = handInfo;
    }
    if (board.isOver()) {
      handInfos[handNb].update(board);
      logger.debug("updateHandInfo {}", handNb);
    }
  }

  public void sendInputs(Board board, int turn, Player player) {
    int id = player.getIndex();
    int playerNb = board.getPlayerNb();
    PlayerModel playerModel = board.getPlayer(id);
    if (!sendGameInput[id]) {
      sendGameInput[id] = true;
      
      player.sendInputLine(Parameter.SMALL_BLINB);
      player.sendInputLine(Parameter.BIG_BLINB);
      player.sendInputLine(Parameter.HAND_NB_BY_LEVEL);
      player.sendInputLine(Parameter.LEVEL_BLIND_MULTIPLIER);
      player.sendInputLine(Parameter.TOTAL_BUY_IN / playerNb);
      player.sendInputLine(board.getFirstBigBlindId());

      player.sendInputLine(playerNb);
      player.sendInputLine(id);
    }
    player.sendInputLine(turn);
    player.sendInputLine(board.getHandNb());

//    player.sendInputLine(board.getPot());
    for (int i = 0; i < playerNb; i++) {
      player.sendInputLine(board.getPlayer(i).getStack());
      player.sendInputLine(board.getPlayer(i).getTotalBetAmount());
    }
    
    player.sendBoardCards(board.getBoardCards());
    player.sendInputLine(playerModel.getHand().getCards());

    sendRoundInfoInput(board, turn, player);
    sendHandInfoInput(board, player);
    sendPossibleActions(board, player);
  }
  

  private void sendRoundInfoInput(Board board, int turn, Player player) {
    int lastSendRound = lastSendRounds[player.getIndex()];
    lastSendRound++;
    int nb = turn - lastSendRound;
    player.sendInputLine(nb);
    for (int i = lastSendRound; i < turn; i++) {
      logger.debug("id {} referee round {}", player.getIndex(), i +" / " + turn);
      roundInfos[i].sendInput(player);
    }
    lastSendRounds[player.getIndex()] = turn - 1;
  }
  
  private void sendHandInfoInput(Board board, Player player) {
    int lastSendHand = lastSendHandNbs[player.getIndex()];
    lastSendHand++;
    int handNb = board.getHandNb();
    int nb = handNb - lastSendHand;
    player.sendInputLine(nb);
    for (int i = lastSendHand; i < handNb; i++) {
      handInfos[i].sendInput(player);
    }
    lastSendHandNbs[player.getIndex()] = handNb - 1;
  }
  
  private void sendPossibleActions(Board board, Player player) {
    if (RefereeParameter.POSSIBLE_ACTION_INPUT) {
      List<Action> possibleActions = ActionUtils.calculatePossibleActions(board);
      player.sendInputLine(possibleActions.size());
      possibleActions.forEach(a ->  player.sendInputLine(a));
    }
  }
}
