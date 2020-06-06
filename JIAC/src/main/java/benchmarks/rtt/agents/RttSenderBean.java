package benchmarks.rtt.agents;

import benchmarks.rtt.MASSettings;
import benchmarks.rtt.RttDataStructure;
import benchmarks.rtt.dto.Message;
import benchmarks.rtt.BenchmarkSettings;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.action.Action;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.IMessageBoxAddress;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.ontology.AgentDescription;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.ontology.IAgentDescription;
import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import java.io.Serializable;
import java.util.List;

public class RttSenderBean extends AbstractAgentBean {
    private IActionDescription sendAction;
    private boolean hasStarted = false;
    private double rtt = 0;
    private int t = 1;
    private long prevTime = -1;
    private String id;
    private int numberOfMessages;

    @Override
    public void doStart() throws Exception {
        super.doStart();
        String agentName = this.thisAgent.getAgentName();

        id = agentName.split(MASSettings.SENDER_AGENT)[1];
        numberOfMessages = BenchmarkSettings.NUMBER_OF_MESSAGES;

        log.info("[STARTING] AGENT_NAME=" + agentName + "");

        IActionDescription description = new Action(ICommunicationBean.ACTION_SEND);
        sendAction = memory.read(description);

        if (sendAction == null) {
            sendAction = thisAgent.searchAction(description);
        }

        if (sendAction == null) {
            throw new RuntimeException("Send action not found");
        }

        memory.attach(new MessageObserver(), new JiacMessage(new Message(MASSettings.ACK)));
    }

    @Override
    public void execute() {
        if (!hasStarted) {
            List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

            for (IAgentDescription agent : agentDescriptions) {
                if (agent.getName().equals(MASSettings.RECEIVER_AGENT + id)) {
                    JiacMessage msg = new JiacMessage(new Message(MASSettings.INIT));

                    IMessageBoxAddress receiver = agent.getMessageBoxAddress();
                    invoke(sendAction, new Serializable[]{ msg, receiver });
                }
            }

            hasStarted = true;
        }
    }

    @Override
    public void doStop() throws Exception {
        log.info("[STOPPING] AGENT_NAME=" + this.thisAgent.getAgentName());

        RttDataStructure ds = RttDataStructure.getInstance();
        ds.saveRtt(rtt);

        super.doStop();
    }

    private class MessageObserver implements SpaceObserver<IFact> {
        public void notify(SpaceEvent<? extends IFact> event) {
            if (event instanceof WriteCallEvent<?>) {
                if (prevTime != -1) {
                    long currentTime = System.nanoTime();
                    long currRtt = currentTime - prevTime;

                    rtt += (currRtt - rtt) / t;
                    t++;
                }

                if (numberOfMessages > 0) {
                    WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;

                    IJiacMessage rec = memory.remove(wce.getObject());

                    IJiacMessage msg = new JiacMessage(new Message(MASSettings.MSG));

                    invoke(sendAction, new Serializable[]{msg, rec.getSender()});

                    prevTime = System.nanoTime();
                    numberOfMessages--;
                }
            }
        }
    }
}
