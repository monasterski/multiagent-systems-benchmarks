package pl.edu.agh.benchmarks.capacity.agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleAgent extends Agent {
    protected void setup() {
        // inicjalizacja agenta
        log.info("[CREATE] agent name: {}", getAID().getName());
    }
    protected void takeDown() { //opcjonalnie
        // operacje wykonywane bezpośrednio przed usunięciem agenta
        log.info("[DELETE] agent name: {}", getAID().getName());
    }
}
