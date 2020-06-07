package rtt;

import java.io.FileWriter;
import java.io.IOException;

public class RttDataStructure {
    private static final RttDataStructure instance = new RttDataStructure();
    private final FileWriter fw;

    private RttDataStructure() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(BenchmarkSettings.BENCHMARK_OUT_FILE_NAME, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fw = fw;
    }

    public static RttDataStructure getInstance() {
        return instance;
    }

    public void saveRtt(double rtt) throws IOException {
        String record = "MADKIT;" + BenchmarkSettings.NUMBER_OF_AGENT_PAIRS + ";" + String.valueOf(rtt).replace('.', ',') + "\n";

        synchronized (fw) {
            fw.append(record);
        }
    }

    public void close() throws IOException {
        fw.flush();
        fw.close();
    }
}
