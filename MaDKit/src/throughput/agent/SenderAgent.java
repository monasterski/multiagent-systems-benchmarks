package throughput.agent;

import lombok.extern.slf4j.Slf4j;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import throughput.BenchmarkSettings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import static throughput.agent.Society.*;


/* To exchange messages, agents have to exist in an artificial society. That is, agents have to create communities
 * containing groups and take roles within to interact with others. Doing so, agents get agent addresses which could be
 * used to send them messages. Here, two agents take a role and ping the other one.
 */
@Slf4j

public class SenderAgent extends Agent {
    private int sentMessages = -1;

    @Override
    protected void activate() {
        createGroup(COMMUNITY, GROUP);
        requestRole(COMMUNITY, GROUP, ROLE_SENDER);
    }

    @Override
    protected void live() {

        AgentAddress receiver = null;
        while (receiver == null) {
            // This way, I wait for another coming into play
            receiver = getAgentWithRole(COMMUNITY, GROUP, ROLE_RECEIVER);
            pause(1);
        }

        long start = System.nanoTime(); // początkowy czas w nanosekundach.

        String message = String.join("", Collections.nCopies(BenchmarkSettings.MESSAGE_SIZE_IN_KB * 1024, "a"));

        while (true) {
            sendMessage(receiver, new StringMessage(message));
            sentMessages++;
            Message m = waitNextMessage();
        }
    }

    @Override
    protected void end() {
        try {
            saveBenchmarkResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBenchmarkResult() throws IOException {
        String benchmarkResult = "MADKIT," + BenchmarkSettings.MESSAGE_SIZE_IN_KB + "," + (sentMessages * BenchmarkSettings.MESSAGE_SIZE_IN_KB) + "\n";

        FileWriter fw = new FileWriter(BenchmarkSettings.BENCHMARK_OUT_FILE_NAME, true);
        fw.append(benchmarkResult);

        fw.flush();
        fw.close();
    }

}