package pl.edu.agh.benchmarks.throughput.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.benchmarks.throughput.BenchmarkSettings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class SenderAgent extends Agent {

    private AID receiverAgentAID;
    private int sentMessages = 0;

    protected void setup() {

        log.info("[CREATE] agent name: {}", getAID().getName());
        receiverAgentAID = new AID("ReceiverAgent", AID.ISLOCALNAME);

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                // Send PING
                ACLMessage request = new ACLMessage( ACLMessage.REQUEST );
                String message = String.join("", Collections.nCopies(BenchmarkSettings.MESSAGE_SIZE_IN_KB * 1024, "a"));
                request.setContent(message);
                request.addReceiver(receiverAgentAID);
                send(request);
                log.info("Agent {} sent ping to {}", "SenderAgent", "ReceiverAgent");

                // RECEIVE PONG
                MessageTemplate mt =
                        MessageTemplate.and(
                                MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
                                MessageTemplate.MatchSender(receiverAgentAID)
                        );
                ACLMessage msg= blockingReceive(mt);
                log.info("Agent {} received pong from {}", getAID().getName(), msg.getSender().getName());
                sentMessages++;
            }
        });
    }

    private void saveBenchmarkResult() throws IOException {
        String benchmarkResult = "JADE;" + BenchmarkSettings.MESSAGE_SIZE_IN_KB + ";" + (sentMessages * BenchmarkSettings.MESSAGE_SIZE_IN_KB) + "\n";
        log.info(benchmarkResult);
        FileWriter fw = new FileWriter(BenchmarkSettings.BENCHMARK_OUT_FILE_NAME, true);
        fw.append(benchmarkResult);

        fw.flush();
        fw.close();
    }

    protected void takeDown() {
        log.info("[DELETE] agent name: {}", getAID().getName());
        try {
            saveBenchmarkResult();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}

