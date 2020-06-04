package rtt.agent;

import lombok.extern.slf4j.Slf4j;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static rtt.agent.Society.*;


/* To exchange messages, agents have to exist in an artificial society. That is, agents have to create communities
 * containing groups and take roles within to interact with others. Doing so, agents get agent addresses which could be
 * used to send them messages. Here, two agents take a role and ping the other one.
 */
@Slf4j

public class SenderAgent extends Agent {
    public static int group = 0;
    String GROUP = null;
    long rttTime;

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
            pause(10);
        }

        long start = System.nanoTime(); // początkowy czas w milisekundach.

        sendMessage(receiver, new StringMessage(getName()));
        Message m = waitNextMessage();

        rttTime = System.nanoTime() - start; // czas wykonania programu w milisekundach.

    }

    @Override
    protected void end() {

        log.info("[TIME] " + rttTime / 1000 + "ms");
        pause(100);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter("times.csv", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(rttTime);
        writer.close();
    }
}