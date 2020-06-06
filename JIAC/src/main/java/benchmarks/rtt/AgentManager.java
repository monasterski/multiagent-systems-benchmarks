package benchmarks.rtt;

import de.dailab.jiactng.agentcore.IAgent;
import de.dailab.jiactng.agentcore.IAgentNode;
import de.dailab.jiactng.agentcore.SimpleAgentNode;
import de.dailab.jiactng.agentcore.lifecycle.LifecycleException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AgentManager {
    private static final ClassPathXmlApplicationContext CTX =
            new ClassPathXmlApplicationContext(MASSettings.CONFIG_LOCATION);

    public static void main(String[] args) throws Exception {
        SimpleAgentNode agentNode = (SimpleAgentNode) CTX.getBean(MASSettings.NODE_BEAN);

        for (int i = 1; i <= BenchmarkSettings.NUMBER_OF_AGENT_PAIRS; i++) {
            createNewAgent(agentNode, i);
        }

        System.out.println("Test started, going to sleep...");
        Thread.sleep(BenchmarkSettings.SLEEP_LENGTH_IN_SECONDS * 1000);

        System.out.println("Test ended, terminating...");
        agentNode.shutdown();

        RttDataStructure ds = RttDataStructure.getInstance();
        ds.close();
    }

    private static void createNewAgent(IAgentNode agentNode, int agentNo) throws LifecycleException {
        IAgent recAgent = (IAgent) CTX.getBean(MASSettings.RECEIVER_AGENT);
        IAgent sendAgent = (IAgent) CTX.getBean(MASSettings.SENDER_AGENT);

        recAgent.setAgentName(MASSettings.RECEIVER_AGENT + agentNo);
        sendAgent.setAgentName(MASSettings.SENDER_AGENT + agentNo);

        agentNode.addAgent(recAgent);
        agentNode.addAgent(sendAgent);

        recAgent.init();
        sendAgent.init();

        recAgent.start();
        sendAgent.start();
    }
}
