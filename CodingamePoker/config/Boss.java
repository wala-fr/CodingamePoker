import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 **/
class Player {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int smallBlind = in.nextInt();
    int bigBlind = in.nextInt(); // initial values of the blinds
    int handNbByLevel = in.nextInt(); // number of hand to play before next level
    int levelBlindMultiplicator = in.nextInt(); // coefficient that multiplies the blind when the
                                                // level changes
    int buyIn = in.nextInt(); // initial stack
    int firstBigBlindId = in.nextInt(); // id of the first big blind player
    int playerNb = in.nextInt(); // number of player (2 to 4)
    int playerId = in.nextInt(); // your id

    // game loop
    while (true) {
      int round = in.nextInt(); // referee round(starts at 1-game will end when it reaches 600)
      int handNb = in.nextInt(); // hand number(from deal to show down-starts at 0)
      int pot = in.nextInt(); // number of chips in the pot
      for (int i = 0; i < playerNb; i++) {
        int stack = in.nextInt(); // number of chips of the player stack
      }
      String boardCards = in.next(); // common board cards(for example : AD_QH_2S_X_X)
      String playerCards = in.next(); // your cards(for example : TC_JH)
      int actionNb = in.nextInt(); // number of actions since your last turn
      for (int i = 0; i < actionNb; i++) {
        int actionRound = in.nextInt(); // round of the action
        int actionHandNb = in.nextInt(); // hand number of the action
        int actionPlayerId = in.nextInt(); // player id of the action
        String action = in.next(); // action(for example:BET 200, FOLD, NONE...)
        String actionBoardCards = in.next(); // board card when the action is done (for example :
                                             // AD_QH_2S_4H_X)
      }
      int showDownNb = in.nextInt(); // number of end of hand since your last turn
      for (int i = 0; i < showDownNb; i++) {
        int showDownHandNb = in.nextInt(); // hand number
        String showDownCards = in.next(); // cards shown at the end of the hand (for example :
                                          // AD_QH_X_X_X_X_8S_8H)
      }

      // Write an action using System.out.println()
      // To debug: System.err.println("Debug messages...");

      System.out.println("CALL;MESSAGE");
    }
  }
}
