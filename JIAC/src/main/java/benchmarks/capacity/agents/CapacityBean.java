package benchmarks.capacity.agents;

import de.dailab.jiactng.agentcore.AbstractAgentBean;

public class CapacityBean extends AbstractAgentBean {
    @Override
    public void doStart() throws Exception {
        super.doStart();
        log.info("[STARTING] AGENT_NAME=" + this.thisAgent.getAgentName() + "");
    }

    @Override
    public void execute() {
        log.info("[HEARTBEAT] AGENT_NAME=" + this.thisAgent.getAgentName());
    }

    @Override
    public void doStop() throws Exception {
        log.info("[STOPPING] AGENT_NAME=" + this.thisAgent.getAgentName());
        super.doStop();
    }
}
