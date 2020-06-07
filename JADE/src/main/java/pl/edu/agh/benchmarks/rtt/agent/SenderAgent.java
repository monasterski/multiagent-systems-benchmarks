package pl.edu.agh.benchmarks.rtt.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.benchmarks.rtt.BenchmarkSettings;
import pl.edu.agh.benchmarks.rtt.RttDataStructure;

import java.io.IOException;

@Slf4j
public class SenderAgent extends Agent {

    private double rtt = 0;
    private int t = 1;

    private int pairNr;
    private AID receiverAgentAID;

    private int numberOfTrips = 0;

    protected void setup() {

        log.info("[CREATE] agent name: {}", getAID().getName());

        pairNr = (int) getArguments()[0];
        receiverAgentAID = new AID("ReceiverAgent"+pairNr, AID.ISLOCALNAME);

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                // Send PING
                ACLMessage request = new ACLMessage( ACLMessage.REQUEST );
                request.setContent( "Ping" );
                request.addReceiver(receiverAgentAID);
                send(request);
                long sendingPingTime = System.nanoTime();
                //log.info("Agent {} sent ping to {}", "SenderAgent"+pairNr, "ReceiverAgent"+pairNr);

                // RECEIVE PONG
                MessageTemplate mt =
                        MessageTemplate.and(
                                MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
                                MessageTemplate.MatchSender(receiverAgentAID)
                        );
                ACLMessage msg= blockingReceive(mt);
                //log.info("Agent {} received pong from {}", getAID().getName(), msg.getSender().getName());

                long receivingPongTime = System.nanoTime();
                long currRtt = receivingPongTime - sendingPingTime;

                rtt += (currRtt - rtt) / t;
                t++;

                numberOfTrips++;
                if(numberOfTrips == BenchmarkSettings.NUMBER_OF_MESSAGES){
                    doDelete();
                }
            }
        });
    }

    protected void takeDown() {
        log.info("[DELETE] agent name: {}", getAID().getName());
        RttDataStructure ds = RttDataStructure.getInstance();
        try {
            ds.saveRtt(rtt);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

