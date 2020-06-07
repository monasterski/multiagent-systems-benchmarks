package rtt;

import lombok.extern.slf4j.Slf4j;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import rtt.agent.ReceiverAgent;
import rtt.agent.SenderAgent;

import static java.lang.Thread.sleep;

@Slf4j
public class AgentManagerRTT {

    public static void main(String... args) {

        try {
            Madkit m = new Madkit("--launchAgents", ReceiverAgent.class.getName() + ",false," + BenchmarkSettings.NUMBER_OF_AGENT_PAIRS + ";" +
                    SenderAgent.class.getName() + ",false," + BenchmarkSettings.NUMBER_OF_AGENT_PAIRS);

            System.out.println("Test started, going to sleep...");
            sleep(BenchmarkSettings.SLEEP_LENGTH_IN_SECONDS * 1000);

            System.out.println("Test ended, terminating...");
            m.doAction(KernelAction.EXIT);

            sleep(100);

            RttDataStructure ds = RttDataStructure.getInstance();
            ds.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}