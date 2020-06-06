package benchmarks.rtt.agents;

import benchmarks.rtt.MASSettings;
import benchmarks.rtt.dto.Message;
import de.dailab.jiactng.agentcore.AbstractAgentBean;
import de.dailab.jiactng.agentcore.comm.ICommunicationBean;
import de.dailab.jiactng.agentcore.comm.message.IJiacMessage;
import de.dailab.jiactng.agentcore.comm.message.JiacMessage;
import de.dailab.jiactng.agentcore.knowledge.IFact;
import de.dailab.jiactng.agentcore.ontology.IActionDescription;
import de.dailab.jiactng.agentcore.action.Action;
import org.sercho.masp.space.event.SpaceEvent;
import org.sercho.masp.space.event.SpaceObserver;
import org.sercho.masp.space.event.WriteCallEvent;

import java.io.Serializable;

public class RttReceiverBean extends AbstractAgentBean {
    private IActionDescription sendAction;

    @Override
    public void doStart() throws Exception {
        super.doStart();
        log.info("[STARTING] AGENT_NAME=" + this.thisAgent.getAgentName() + "");

        IActionDescription description = new Action(ICommunicationBean.ACTION_SEND);
        sendAction = memory.read(description);
        if (sendAction == null) {
            sendAction = thisAgent.searchAction(description);
        }
        if (sendAction == null) {
            throw new RuntimeException("Send action not found");
        }

        memory.attach(new MessageObserver(), new JiacMessage());
    }

    @Override
    public void doStop() throws Exception {
        log.info("[STOPPING] AGENT_NAME=" + this.thisAgent.getAgentName());
        super.doStop();
    }

    private class MessageObserver implements SpaceObserver<IFact> {
        public void notify(SpaceEvent<? extends IFact> event) {
            if (event instanceof WriteCallEvent<?>) {
                WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;

                IJiacMessage rec = memory.remove(wce.getObject());

                IJiacMessage msg = new JiacMessage(new Message(MASSettings.ACK));

                invoke(sendAction, new Serializable[] { msg, rec.getSender() });
            }
        }
    }
}
