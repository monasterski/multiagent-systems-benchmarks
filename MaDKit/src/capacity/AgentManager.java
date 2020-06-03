package capacity;

import lombok.extern.slf4j.Slf4j;
import madkit.kernel.Madkit;

import java.math.BigInteger;
import java.util.logging.Level;

@Slf4j
public class AgentManager {

    public static BigInteger totalNumberOfAgents = BigInteger.ONE;

    public static void main(String... args) {

        for (BigInteger i = BigInteger.ONE; ; i = i.add(BigInteger.ONE)) {
            try {
                new Madkit(Madkit.LevelOption.madkitLogLevel.toString(), Level.OFF.toString(), "--launchAgents", "capacity.agent.SimpleAgent,false");
                totalNumberOfAgents = totalNumberOfAgents.add(BigInteger.ONE);
                if ((totalNumberOfAgents.mod(new BigInteger("100")).equals(BigInteger.ZERO))) {
                    log.info("Total number of agents: {}", totalNumberOfAgents);
                }
            } catch (Exception e) {
                log.info("Total number of agents: {}", totalNumberOfAgents);
                e.printStackTrace();
            }
        }
    }
}