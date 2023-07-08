
import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setSeed(-6353511557071152000l);
        
        gameRunner.addAgent(AgentCall.class, "walawalawala", "https://static.codingame.com/servlet/fileservlet?id=90904183775363&format=viewer_avatar");
        gameRunner.addAgent(AgentCall.class);
        gameRunner.addAgent(AgentCall.class);
        gameRunner.addAgent(AgentFold.class);

        gameRunner.start();
    }
}
