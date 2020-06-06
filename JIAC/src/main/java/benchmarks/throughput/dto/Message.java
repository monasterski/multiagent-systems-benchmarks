package benchmarks.throughput.dto;

import de.dailab.jiactng.agentcore.knowledge.IFact;

public class Message implements IFact {
    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
