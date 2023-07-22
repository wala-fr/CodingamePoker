
import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setSeed(-7006392080589544000l);
        
        gameRunner.addAgent(AgentCall.class, "wala", "https://github.com/wala-fr/CodingamePoker/blob/main/CodingamePoker/src/main/resources/view/assets/wala.png");
        gameRunner.addAgent(AgentCall.class);
        gameRunner.addAgent(AgentCall.class);
        gameRunner.addAgent(AgentFold.class);

        gameRunner.start();
    }
}
