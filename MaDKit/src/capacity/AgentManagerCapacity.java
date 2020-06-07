package capacity;

import capacity.agent.SimpleAgent;
import lombok.extern.slf4j.Slf4j;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import static java.lang.Thread.sleep;

@Slf4j
public class AgentManagerCapacity {

    public static int totalNumberOfAgents = 1;

    public static void main(String... args) throws IOException, InterruptedException {
        Madkit md = null;
        try {
            md = new Madkit(Madkit.LevelOption.madkitLogLevel.toString(), Level.OFF.toString(), "--launchAgents", "capacity.agent.SimpleAgent,false");
        } catch (Exception e) {
            log.info("Total number of agents: {}", totalNumberOfAgents);
            e.printStackTrace();
        }
        for (int i = 1; i <= BenchmarkSettings.TOTAL_NUMBER_OF_AGENTS; i++) {
            md.doAction(KernelAction.LAUNCH_AGENT, SimpleAgent.class.getName());

            totalNumberOfAgents++;

//                if (totalNumberOfAgents%100==0) {
//                    log.info("Total number of agents: {}", totalNumberOfAgents);
//                }
            sleep(1);
        }

        System.out.println("Created all agents, sleeping...");
        Thread.sleep(BenchmarkSettings.SLEEP_LENGTH_IN_SECONDS * 1000);
        System.out.println("Sleep over, shutting down...");

        saveBenchmarkResult();

        md.doAction(KernelAction.EXIT);
        System.out.println("Node shut down");

    }

    private static void saveBenchmarkResult() throws IOException {
        double memUsage = getMemoryUsage();
        String benchmarkResult = "MADKIT," + BenchmarkSettings.TOTAL_NUMBER_OF_AGENTS + "," + memUsage + "\n";

        FileWriter fw = new FileWriter(BenchmarkSettings.BENCHMARK_OUT_FILE_NAME, true);
        fw.append(benchmarkResult);

        fw.flush();
        fw.close();
    }

    private static double getMemoryUsage() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
    }
}