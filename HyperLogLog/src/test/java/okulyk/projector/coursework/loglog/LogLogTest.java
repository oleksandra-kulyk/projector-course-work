package okulyk.projector.coursework.loglog;

import com.google.common.hash.Hashing;
import net.sourceforge.sizeof.SizeOf;
import okulyk.projector.coursework.loglog.impl.HyperLogLog;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LogLogTest {

    private Path path;
    private HyperLogLog hyperLogLog = new HyperLogLog(Hashing.murmur3_128(), 12);

    @Test
    public void realText() throws IOException {
        path = Paths.get("src/test/resources/reddit_trump.csv");

        HashSet<String> hashSet = new HashSet<>();

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String lineWithoutSymbols = line.replaceAll("\\P{Alnum}", "");
            String[] words = lineWithoutSymbols.split(" ");
            for (String word : words) {
                hyperLogLog.add(word.getBytes());
                hashSet.add(word);
            }
        }

        System.out.println(String.format("HashSet size: %s vs LogLog size: %s",
                SizeOf.humanReadable(SizeOf.deepSizeOf(hashSet)),
                SizeOf.humanReadable(SizeOf.deepSizeOf(hyperLogLog.getMaxRankForBucket()))));

        System.out.println(String.format("calculated cardinality for text is %d real %d", hyperLogLog.getCardinality(), hashSet.size()));
    }

    @Test
    public void ipDataset() throws IOException {
        path = Paths.get("src/test/resources/Dataset-Unicauca-Version2-87Atts.csv");

        HashSet<String> hashSet = new HashSet<>();
        int requestsCount = 0;

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] columns = line.split(",");
            String ip = columns[1];

            byte[] bytes = ip.getBytes();
            hyperLogLog.add(bytes);
            hashSet.add(ip);
            requestsCount++;
        }

        dumpStatistic("ip", "IP", "murmur3", 128, "hyperLogLog", 8,
                requestsCount, hashSet, hyperLogLog);
    }

    private void dumpStatistic(String dataSetShortName, String dataSetDescription, String hashName, int hashSize, String algorithmName, int bitsToTake,
                               int requestsCount, Set set, LogLog logLog) throws IOException {
        int actualCardinality = set.size();
        int calculatedCardinality = logLog.getCardinality();
        int diff = calculatedCardinality - actualCardinality;
        double diffInPercentage = (double) diff / actualCardinality * 100;

        String setMemory = SizeOf.humanReadable(SizeOf.deepSizeOf(set));
        String logLogMemory = SizeOf.humanReadable(SizeOf.deepSizeOf(logLog.getMaxRankForBucket()));

        Path summaryFile = Paths.get(String.format("src/test/resources/out/%s_%s_%d_%s_%d.summary", dataSetShortName, hashName, hashSize, algorithmName, bitsToTake));

        String statsText = String.format("Dataset: %s", dataSetDescription)
                + String.format("\nHash function: %s, hash size: %s, algorithm: %s, first bits count to take: %d\n", hashName, hashName, algorithmName, bitsToTake)
                + String.format("\nRequests count: %d, actual cardinality: %d, calculated cardinality: %d", requestsCount, actualCardinality, calculatedCardinality)
                + String.format("\nAbsolute mistake: %d, mistake in percentage: %.2f", diff, diffInPercentage)
                + String.format("\nMemory for set: %s, memory for LogLog: %s", setMemory, logLogMemory);

        Files.write(summaryFile, statsText.getBytes());
        System.out.println("Stats stored to file " + summaryFile);

        Path bucketsFile = Paths.get(String.format("src/test/resources/out/%s_%s_%d_%s_%d.buckets", dataSetShortName, hashName, hashSize, algorithmName, bitsToTake));

        String bucketsToFile = Arrays.stream(logLog.getMaxRankForBucket())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("\n"));

        Files.write(bucketsFile, bucketsToFile.getBytes());
        System.out.println("Buckets stored to file " + bucketsFile);
    }


}
