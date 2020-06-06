package pl.edu.agh.benchmarks.rtt;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgentManagerRTT {

    public static void main(String... args) throws Exception{
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "false");
        ContainerController containerController = runtime.createMainContainer(profile);

        for (int i = 1; i <= BenchmarkSettings.NUMBER_OF_AGENT_PAIRS; i++) {
            createNewAgentPair(i, containerController);
        }

        System.out.println("Test started, going to sleep...");
        Thread.sleep(BenchmarkSettings.SLEEP_LENGTH_IN_SECONDS * 1000);

        System.out.println("Test ended, terminating...");
        containerController.kill();

        RttDataStructure ds = RttDataStructure.getInstance();
        ds.close();
    }

    private static void createNewAgentPair(int agentNr, ContainerController containerController) throws StaleProxyException {
        Object[] args = new Object[1];
        args[0] = agentNr;

        AgentController senderAgentController = containerController
                .createNewAgent("SenderAgent"+agentNr, "pl.edu.agh.benchmarks.rtt.agent.SenderAgent", args);
        senderAgentController.start();
        AgentController receiverAgentController = containerController
                .createNewAgent("ReceiverAgent"+agentNr, "pl.edu.agh.benchmarks.rtt.agent.ReceiverAgent", args);
        receiverAgentController.start();
    }
}
