package rtt.agent;

import lombok.extern.slf4j.Slf4j;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import rtt.BenchmarkSettings;
import rtt.RttDataStructure;

import java.io.IOException;

import static rtt.agent.Society.*;


/* To exchange messages, agents have to exist in an artificial society. That is, agents have to create communities
 * containing groups and take roles within to interact with others. Doing so, agents get agent addresses which could be
 * used to send them messages. Here, two agents take a role and ping the other one.
 */
@Slf4j

public class SenderAgent extends Agent {
    public static int group = 0;
    String GROUP = null;
    private double rtt = 0;
    private int numberOfTrips = 0;
    private int t = 1;

    @Override
    protected void activate() {
        GROUP = String.valueOf(group++);
        createGroup(COMMUNITY, GROUP);
        requestRole(COMMUNITY, GROUP, ROLE_SENDER);
        pause(100);
    }

    @Override
    protected void live() {

        AgentAddress receiver = null;
        while (receiver == null) {
            // This way, I wait for another coming into play
            receiver = getAgentWithRole(COMMUNITY, GROUP, ROLE_RECEIVER);
            pause(1);
        }

        while (numberOfTrips++ < BenchmarkSettings.NUMBER_OF_MESSAGES) {

            long start = System.nanoTime(); // początkowy czas w nanosekundach.

            sendMessage(receiver, new StringMessage(getName()));
            Message m = waitNextMessage();

            long currRtt = System.nanoTime() - start; // czas wykonania w nanosekundach.
            rtt += (currRtt - rtt) / t;
            t++;
        }
    }

    @Override
    protected void end() {
        pause(100);
        System.out.println("END");
        RttDataStructure ds = RttDataStructure.getInstance();
        try {
            ds.saveRtt(rtt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}