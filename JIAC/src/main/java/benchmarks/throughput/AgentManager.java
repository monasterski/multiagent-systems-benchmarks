package benchmarks.throughput;

import de.dailab.jiactng.agentcore.SimpleAgentNode;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AgentManager {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(MASSettings.CONFIG_LOCATION);
        SimpleAgentNode agentNode = (SimpleAgentNode) ctx.getBean(MASSettings.NODE_BEAN);

        System.out.println("Test started, going to sleep...");
        Thread.sleep(BenchmarkSettings.TEST_LENGTH_IN_SECONDS * 1000);

        System.out.println("Test ended, terminating...");
        agentNode.shutdown();
    }
}
