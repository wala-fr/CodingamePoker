import java.util.Random;
import java.util.Scanner;

public class AgentAllIn {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int playerNb = in.nextInt();
    int id = in.nextInt();
    System.err.println(playerNb +" " + id);
    // game loop
    while (true) {
      for (int i = 0; i < playerNb; i++) {
        in.nextInt();
      }
      System.out.println("ALL-IN;abcdefghijklmnopqrstuvwxyz");
    }
  }
}
