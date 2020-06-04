package throughput;

import lombok.extern.slf4j.Slf4j;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import throughput.agent.ReceiverAgent;
import throughput.agent.SenderAgent;

import static java.lang.Thread.sleep;

@Slf4j
public class AgentManager {

    public static void main(String... args) {

        try {
            Madkit m = new Madkit("--launchAgents", ReceiverAgent.class.getName() + ",false,1;" +
                    SenderAgent.class.getName() + ",false,1");
            sleep(1000);
            m.doAction(KernelAction.EXIT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}