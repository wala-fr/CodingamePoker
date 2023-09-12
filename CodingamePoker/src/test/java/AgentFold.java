import java.util.Random;
import java.util.Scanner;

public class AgentFold {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int smallBlind = in.nextInt(); // initial small blind's value
    int bigBlind = in.nextInt(); // initial big blind's value
    int handNbByLevel = in.nextInt(); // number of hands to play to reach next level
    int levelBlindMultiplyCoeff = in.nextInt();
    int buyIn = in.nextInt(); // initial stack for each player
    int firstBigBlindId = in.nextInt(); // id of the first big blind player
    int playerNb = in.nextInt(); // number of players (2 to 4)
    int playerId = in.nextInt(); // your id

    // game loop
    while (true) {
        int round = in.nextInt(); // referee round (starts at 1-game ends when it reaches 600)
        int handNb = in.nextInt(); // hand number (starts at 1)
        for (int i = 0; i < playerNb; i++) {
            int stack = in.nextInt(); // number of chips in the player's stack
            int chipInPot = in.nextInt(); // number of player's chips in the pot
        }
        String boardCards = in.next(); // board cards (example : AD_QH_2S_X_X)
        String playerCards = in.next(); // your cards (example : TC_JH)
        int actionNb = in.nextInt(); // number of actions since your last turn
        for (int i = 0; i < actionNb; i++) {
            int actionRound = in.nextInt(); // round of the action
            int actionHandNb = in.nextInt(); // hand number of the action
            int actionPlayerId = in.nextInt(); // player id of the action
            String action = in.next(); // action (examples : BET_200, FOLD, NONE...)
            String actionBoardCards = in.next(); // board cards when the action is done (example : AD_QH_2S_4H_X)
        }
        int showDownNb = in.nextInt(); // number of hands that ended since your last turn
        for (int i = 0; i < showDownNb; i++) {
            int showDownHandNb = in.nextInt(); // hand number
            String showDownBoardCards = in.next(); // board cards at the showdown
            String showDownPlayerCards = in.next(); // players cards shown at the end of the hand (example : AD_QH_E_E_X_X_8S_8H player0's cards, followed by player1's cards ...)
        }
        int possibleActionNb = in.nextInt(); // number of actions you can do
        for (int i = 0; i < possibleActionNb; i++) {
            String possibleAction = in.next(); // your possible action (BET_240 means 240 is the minimum raise but you can bet more)
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        System.out.println("FOLD;MESSAGE");
    }}
  
  private static void log(Object... obj) {
    String str = "";
    for (Object o : obj) {
      str += o + " ";
    }
    System.err.println(str);
  }
}
