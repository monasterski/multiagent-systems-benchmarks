package throughput.agent;

import lombok.extern.slf4j.Slf4j;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static throughput.agent.Society.*;


/* To exchange messages, agents have to exist in an artificial society. That is, agents have to create communities
 * containing groups and take roles within to interact with others. Doing so, agents get agent addresses which could be
 * used to send them messages. Here, two agents take a role and ping the other one.
 */
@Slf4j

public class SenderAgent extends Agent {
    double time_ns;
    String string = null;
    private int sizeOfInt = 4;

    @Override
    protected void activate() {
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

        long start = System.nanoTime(); // początkowy czas w nanosekundach.

        string = "000000000000000000000000000000000000000000000";
        sendMessage(receiver, new StringMessage(string));
        Message m = waitNextMessage();

        time_ns = System.nanoTime() - start; // czas wykonania programu w nanosekundach.

    }

    @Override
    protected void end() {
        double time_s = time_ns / 1000 / 1000 / 1000;
        log.info("[TIME] " + time_s + " s");
        int sizeOfMString = string.getBytes().length;
        log.info("[STRING SIZE] " + sizeOfMString + " B");

        double throughput = sizeOfMString / time_s;

        log.info("[THROUGHPUT] " + throughput + " B/s");

        pause(100);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter("timeThroughput.csv", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(time_ns);
        writer.close();
    }
}