package pl.edu.agh.benchmarks.rtt.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.benchmarks.rtt.BenchmarkSettings;

@Slf4j
public class ReceiverAgent extends Agent {

    private int numberOfTrips = 0;
    private int pairNr;
    private AID senderAgentAID;

    protected void setup() {

        log.info("[CREATE] agent name: {}", getAID().getName());
        pairNr = (int) getArguments()[0];
        senderAgentAID = new AID("SenderAgent"+pairNr, AID.ISLOCALNAME);

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                MessageTemplate mt =
                        MessageTemplate.and(
                                MessageTemplate.MatchPerformative( ACLMessage.REQUEST ),
                                MessageTemplate.MatchSender(senderAgentAID)
                        );
                ACLMessage msg= blockingReceive(mt);

                //log.info("Agent {} received ping from {}", getAID().getName(), msg.getSender().getName());

                ACLMessage reply = new ACLMessage( ACLMessage.INFORM );
                reply.setContent( "Pong" );
                reply.addReceiver( msg.getSender() );
                send(reply);
                numberOfTrips++;
                if(numberOfTrips == BenchmarkSettings.NUMBER_OF_MESSAGES){
                    doDelete();
                }
            }
        });
    }

    protected void takeDown() {
        log.info("[DELETE] agent name: {}", getAID().getName());
    }
}
