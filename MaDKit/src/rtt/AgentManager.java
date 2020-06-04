package rtt;

import lombok.extern.slf4j.Slf4j;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import rtt.agent.ReceiverAgent;
import rtt.agent.SenderAgent;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Thread.sleep;

@Slf4j
public class AgentManager {

    public static void main(String... args) {

        for (int i = 1; i <= 100; i++) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new FileWriter("times.csv", true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.println(i);
            writer.close();

            try {
                Madkit m = new Madkit("--launchAgents", ReceiverAgent.class.getName() + ",false," + i + ";" +
                        SenderAgent.class.getName() + ",false," + i);
                sleep(1000);
                m.doAction(KernelAction.EXIT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}