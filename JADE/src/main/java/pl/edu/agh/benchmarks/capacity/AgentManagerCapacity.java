package pl.edu.agh.benchmarks.capacity;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentManagerCapacity {

    private static int totalNumberOfAgents;

    public static void main(String... args){
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "false");
        ContainerController containerController = runtime.createMainContainer(profile);

        for(int i = 1; i<=BenchmarkSettings.TOTAL_NUMBER_OF_AGENTS ; i++){
            try {
                createNewAgent(i, containerController);
                totalNumberOfAgents++;
                log.info("Total number of agents: {}", totalNumberOfAgents);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createNewAgent(int agentNr, ContainerController containerController) throws StaleProxyException {
        AgentController agentController = containerController
                .createNewAgent("SimpleAgent"+agentNr, "pl.edu.agh.benchmarks.capacity.agent.SimpleAgent", null);
        agentController.start();
    }
}
