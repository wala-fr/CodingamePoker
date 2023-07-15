import java.util.Random;
import java.util.Scanner;

public class AgentCall {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int startSmallBlind = in.nextInt();
    int startBigBlind = in.nextInt(); // value of the blinds at the beginning
    int handlNbByLevel = in.nextInt();
    int levelBlindMultiplicator = in.nextInt();
    int playerNb = in.nextInt();
    int playerId = in.nextInt();

    // game loop
    while (true) {
        int round = in.nextInt();
        int pot = in.nextInt();
        for (int i = 0; i < playerNb; i++) {
            int stack = in.nextInt();
        }
        int boardCardNb = in.nextInt();
        for (int i = 0; i < boardCardNb; i++) {
            String boardCard = in.next();
        }
        for (int i = 0; i < 2; i++) {
            String playerCard = in.next();
        }
        int actionNb = in.nextInt();
        for (int i = 0; i < actionNb; i++) {
            int actionPlayerId = in.nextInt();
            String action = in.next();
            int amount = in.nextInt();
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        System.out.println("");
    }
}
}
