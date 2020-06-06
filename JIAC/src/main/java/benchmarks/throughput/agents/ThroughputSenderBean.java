package benchmarks.throughput.agents;

import benchmarks.throughput.BenchmarkSettings;
import benchmarks.throughput.MASSettings;
import benchmarks.throughput.dto.Message;
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

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ThroughputSenderBean extends AbstractAgentBean {
    private IActionDescription sendAction;
    private boolean hasStarted = false;
    private int sentMessages = -1;

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

        memory.attach(new MessageObserver(), new JiacMessage(new Message(MASSettings.ACK)));
    }

    @Override
    public void execute() {
        if (!hasStarted) {
            List<IAgentDescription> agentDescriptions = thisAgent.searchAllAgents(new AgentDescription());

            for (IAgentDescription agent : agentDescriptions) {
                if (agent.getName().equals(MASSettings.RECEIVER_AGENT)) {
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

        saveBenchmarkResult();

        super.doStop();
    }

    private void saveBenchmarkResult() throws IOException {
        String benchmarkResult = "JIAC," + BenchmarkSettings.MESSAGE_SIZE_IN_KB + "," + (sentMessages * BenchmarkSettings.MESSAGE_SIZE_IN_KB) + "\n";

        FileWriter fw = new FileWriter(BenchmarkSettings.BENCHMARK_OUT_FILE_NAME, true);
        fw.append(benchmarkResult);

        fw.flush();
        fw.close();
    }

    private class MessageObserver implements SpaceObserver<IFact> {
        public void notify(SpaceEvent<? extends IFact> event) {
            if (event instanceof WriteCallEvent<?>) {
                sentMessages++;

                WriteCallEvent<IJiacMessage> wce = (WriteCallEvent<IJiacMessage>) event;

                IJiacMessage rec = memory.remove(wce.getObject());

                String message = String.join("", Collections.nCopies(BenchmarkSettings.MESSAGE_SIZE_IN_KB * 1024, "a"));
                IJiacMessage msg = new JiacMessage(new Message(message));

                invoke(sendAction, new Serializable[] { msg, rec.getSender() });
            }
        }
    }
}
