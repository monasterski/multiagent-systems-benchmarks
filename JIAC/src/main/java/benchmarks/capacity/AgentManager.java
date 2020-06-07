package benchmarks.capacity;

import de.dailab.jiactng.agentcore.IAgent;
import de.dailab.jiactng.agentcore.IAgentNode;
import de.dailab.jiactng.agentcore.SimpleAgentNode;
import de.dailab.jiactng.agentcore.lifecycle.LifecycleException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileWriter;
import java.io.IOException;

public class AgentManager {
    private static final ClassPathXmlApplicationContext CTX =
            new ClassPathXmlApplicationContext(MASSettings.CONFIG_LOCATION);

    public static void main(String[] args) throws Exception {
        SimpleAgentNode agentNode = (SimpleAgentNode) CTX.getBean(MASSettings.NODE_BEAN);

        int nrOfAgents = Integer.parseInt(args[0]);

        for (int i = 1; i <= nrOfAgents; i++) {
            createNewAgent(agentNode, i);
        }

//        System.out.println("Created all agents, sleeping...");
//        Thread.sleep(BenchmarkSettings.SLEEP_LENGTH_IN_SECONDS * 1000);
//        System.out.println("Sleep over, shutting down...");
//
//        saveBenchmarkResult();
//
//        agentNode.shutdown();
//        System.out.println("Node shut down");
    }

    private static void createNewAgent(IAgentNode agentNode, int agentNo) throws LifecycleException {
        IAgent agent = (IAgent) CTX.getBean(MASSettings.AGENT_BEAN);
        agent.setAgentName(MASSettings.AGENT_BEAN + agentNo);

        agentNode.addAgent(agent);
        agent.init();
        agent.start();
    }

    private static void saveBenchmarkResult() throws IOException {
        double memUsage = getMemoryUsage();
        String benchmarkResult = "JIAC," + BenchmarkSettings.TOTAL_NUMBER_OF_AGENTS + "," + memUsage + "\n";

        FileWriter fw = new FileWriter(BenchmarkSettings.BENCHMARK_OUT_FILE_NAME, true);
        fw.append(benchmarkResult);

        fw.flush();
        fw.close();
    }

    private static double getMemoryUsage() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
    }
}
