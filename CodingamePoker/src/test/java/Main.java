
import java.util.Random;
import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
  public static void main(String[] args) {
    MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
    long seed = -3994686588343231815l;
    seed = new Random().nextLong();
    gameRunner.setSeed(seed);
    System.err.println("seed=" + seed);
    gameRunner.addAgent(AgentCall.class, "wala",
        "https://raw.githubusercontent.com/wala-fr/CodingamePoker/main/CodingamePoker/src/main/resources/view/assets/demo/wala.png");
    gameRunner.addAgent(AgentCall.class);
    gameRunner.addAgent(AgentCall.class);
    gameRunner.addAgent(AgentCall.class);

    gameRunner.start();
  }
}
