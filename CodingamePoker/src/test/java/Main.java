
import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setSeed(-7006392080589544000l);
        
        gameRunner.addAgent(AgentCall.class, "wala", "https://raw.githubusercontent.com/wala-fr/CodingamePoker/main/CodingamePoker/src/main/resources/view/assets/demo/wala.png");
        gameRunner.addAgent(AgentCall.class);
//        gameRunner.addAgent(AgentCall.class);
//        gameRunner.addAgent(AgentFold.class);

        gameRunner.start();
    }
}
