package pl.edu.agh.benchmarks.capacity;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.core.Runtime;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
public class AgentManager {

    public static BigInteger totalNumberOfAgents = BigInteger.ONE;

    public static void main(String... args){
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");
        ContainerController containerController = runtime.createMainContainer(profile);

//        for(BigInteger i = BigInteger.ONE; i.compareTo(BigInteger.valueOf(6)) < 0; i = i.add(BigInteger.ONE)){
        for(BigInteger i = BigInteger.ONE; ; i = i.add(BigInteger.ONE)){
            AgentController agentController;
            try {
                agentController = containerController
                        .createNewAgent("SimpleAgent"+i, "pl.edu.agh.benchmarks.capacity.agent.SimpleAgent", null);
                agentController.start();
                totalNumberOfAgents = totalNumberOfAgents.add(BigInteger.ONE);
                log.info("Total number of agents: {}", totalNumberOfAgents);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }
}
