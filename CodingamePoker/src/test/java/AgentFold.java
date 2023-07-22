import java.util.Random;
import java.util.Scanner;

public class AgentFold {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int smallBlind = in.nextInt();
    int bigBlind = in.nextInt(); 
    int handNbByLevel = in.nextInt(); 
    int levelBlindMultiplicator = in.nextInt(); 
                                                
    int buyIn = in.nextInt(); 
    int firstBigBlindId = in.nextInt(); 
    int playerNb = in.nextInt(); 
    int playerId = in.nextInt(); 
    
    while (true) {
      int round = in.nextInt(); 
      int handNb = in.nextInt(); 
      log("round", round, handNb);

      for (int i = 0; i < playerNb; i++) {
        int stack = in.nextInt(); 
        log(stack);
        int bet = in.nextInt(); 
        log(bet);
      }

      String boardCards = in.next(); 
      log(boardCards);

      String playerCards = in.next(); 
      log(playerCards);

      int actionNb = in.nextInt(); 
      for (int i = 0; i < actionNb; i++) {
        int actionRound = in.nextInt(); 
        int actionHandNb = in.nextInt(); 
        int actionPlayerId = in.nextInt(); 
        String action = in.next(); 
        String actionBoardCards = in.next(); 
                                             
        log("action", actionRound, actionHandNb, actionPlayerId, action, actionBoardCards);
      }
      int showDownNb = in.nextInt(); 
      for (int i = 0; i < showDownNb; i++) {
        int showDownHandNb = in.nextInt(); 
        String showDownBoardCards = in.next(); 
        String showDownCards = in.next(); 
        log("showDown", showDownHandNb, showDownBoardCards, showDownCards);
      }
//      int possibleActionNb = in.nextInt(); 
//      for (int i = 0; i < possibleActionNb; i++) {
//        String possibleAction = in.next();
//        log("possibleAction", possibleAction);
//      }

      System.out.println("FOLD;MESSAGE");
    }
  }
  
  private static void log(Object... obj) {
    String str = "";
    for (Object o : obj) {
      str += o + " ";
    }
    System.err.println(str);
  }
}
