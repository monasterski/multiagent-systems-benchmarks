package pl.edu.agh.benchmarks.rtt.agent;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SenderAgent extends Agent {
    protected void setup() {
        // inicjalizacja agenta
        log.info("[CREATE] pl.edu.agh.benchmarks.rtt.agent name: {}", getAID().getName());
    }
    protected void takeDown() { //opcjonalnie
        // operacje wykonywane bezpośrednio przed usunięciem agenta
        log.info("[DELETE] pl.edu.agh.benchmarks.rtt.agent name: {}", getAID().getName());
    }
}
