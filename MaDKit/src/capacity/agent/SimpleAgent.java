package capacity.agent;

import lombok.extern.slf4j.Slf4j;
import madkit.kernel.Agent;

@Slf4j
public class SimpleAgent extends Agent {

    @Override
    protected void activate() {
        // inicjalizacja agenta
//        log.info("[CREATE] agent name: {}", getName());
    }


    @Override
    protected void live() {
        while (true) ;
    }

    @Override
    protected void end() {
        // operacje wykonywane bezpośrednio przed usunięciem agenta
        log.info("[DELETE] agent name: {}", getName());
    }


}