package pl.edu.agh.benchmarks.throughput;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pl.edu.agh.benchmarks.throughput.BenchmarkSettings;
import pl.edu.agh.benchmarks.rtt.RttDataStructure;

public class AgentManagerThroughput {

    public static void main(String... args) throws Exception{
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "false");
        ContainerController containerController = runtime.createMainContainer(profile);

        // SENDER
        AgentController senderAgentController = containerController
                .createNewAgent("SenderAgent", "pl.edu.agh.benchmarks.throughput.agent.SenderAgent", null);
        senderAgentController.start();
        //RECEIVER
        AgentController receiverAgentController = containerController
                .createNewAgent("ReceiverAgent", "pl.edu.agh.benchmarks.throughput.agent.ReceiverAgent", null);
        receiverAgentController.start();

        // TEST
        System.out.println("Test started, going to sleep...");
        Thread.sleep(BenchmarkSettings.TEST_LENGTH_IN_SECONDS * 1000);

        // END OF TEST
        System.out.println("Test ended, terminating...");
        senderAgentController.kill();
        receiverAgentController.kill();
        containerController.kill();
    }
}
