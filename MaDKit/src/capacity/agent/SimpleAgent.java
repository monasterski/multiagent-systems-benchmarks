package capacity.agent;

import lombok.extern.slf4j.Slf4j;
import madkit.kernel.Agent;

@Slf4j
public class SimpleAgent extends Agent {

    @Override
    protected void activate() {
        // inicjalizacja agenta
        if (hashCode() % 100 == 0)
            log.info("[CREATE] agent name: {}", getName());
    }


    @Override
    protected void live() {
        while (true)
            pause(1000);
    }
}