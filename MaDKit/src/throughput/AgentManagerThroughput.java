package throughput;

import lombok.extern.slf4j.Slf4j;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import throughput.agent.ReceiverAgent;
import throughput.agent.SenderAgent;

@Slf4j
public class AgentManagerThroughput {

    public static void main(String... args) {

        try {
            Madkit m = new Madkit("--launchAgents", ReceiverAgent.class.getName() + ",false,1;" +
                    SenderAgent.class.getName() + ",false,1");

            System.out.println("Test started, going to sleep...");
            Thread.sleep(BenchmarkSettings.TEST_LENGTH_IN_SECONDS * 1000);

            System.out.println("Test ended, terminating...");
            m.doAction(KernelAction.EXIT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}