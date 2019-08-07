package okulyk.projector.coursework.loglog;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import net.sourceforge.sizeof.SizeOf;
import okulyk.projector.coursework.loglog.hash.GuavaHashWrapper;
import okulyk.projector.coursework.loglog.hash.NoHash;
import okulyk.projector.coursework.loglog.impl.HyperLogLog;
import okulyk.projector.coursework.loglog.impl.SimpleLogLog;
import okulyk.projector.coursework.loglog.impl.SuperLogLog;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LogLogTest {

    Map<AlgorithmMetadata, LogLog> logLogs = ImmutableMap.<AlgorithmMetadata, LogLog>builder()
            .put(new AlgorithmMetadata("SimpleLogLog", "noHash", 0, 8), new SimpleLogLog(new NoHash(), 8))
            .put(new AlgorithmMetadata("SimpleLogLog", "noHash", 0, 12), new SimpleLogLog(new NoHash(), 12))
            .put(new AlgorithmMetadata("SimpleLogLog", "noHash", 0, 16), new SimpleLogLog(new NoHash(), 16))

            .put(new AlgorithmMetadata("SimpleLogLog", "murmur3", 32, 8), new SimpleLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 8))
            .put(new AlgorithmMetadata("SimpleLogLog", "murmur3", 32, 12), new SimpleLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 12))
            .put(new AlgorithmMetadata("SimpleLogLog", "murmur3", 32, 16), new SimpleLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 16))

            .put(new AlgorithmMetadata("SimpleLogLog", "murmur3", 128, 8), new SimpleLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 8))
            .put(new AlgorithmMetadata("SimpleLogLog", "murmur3", 128, 12), new SimpleLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 12))
            .put(new AlgorithmMetadata("SimpleLogLog", "murmur3", 128, 16), new SimpleLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 16))
            
            
            .put(new AlgorithmMetadata("SuperLogLog", "noHash", 0, 8), new SuperLogLog(new NoHash(), 8))
            .put(new AlgorithmMetadata("SuperLogLog", "noHash", 0, 12), new SuperLogLog(new NoHash(), 12))
            .put(new AlgorithmMetadata("SuperLogLog", "noHash", 0, 16), new SuperLogLog(new NoHash(), 16))

            .put(new AlgorithmMetadata("SuperLogLog", "murmur3", 32, 8), new SuperLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 8))
            .put(new AlgorithmMetadata("SuperLogLog", "murmur3", 32, 12), new SuperLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 12))
            .put(new AlgorithmMetadata("SuperLogLog", "murmur3", 32, 16), new SuperLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 16))

            .put(new AlgorithmMetadata("SuperLogLog", "murmur3", 128, 8), new SuperLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 8))
            .put(new AlgorithmMetadata("SuperLogLog", "murmur3", 128, 12), new SuperLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 12))
            .put(new AlgorithmMetadata("SuperLogLog", "murmur3", 128, 16), new SuperLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 16))
            
            
            .put(new AlgorithmMetadata("HyperLogLog", "noHash", 0, 8), new HyperLogLog(new NoHash(), 8))
            .put(new AlgorithmMetadata("HyperLogLog", "noHash", 0, 12), new HyperLogLog(new NoHash(), 12))
            .put(new AlgorithmMetadata("HyperLogLog", "noHash", 0, 16), new HyperLogLog(new NoHash(), 16))

            .put(new AlgorithmMetadata("HyperLogLog", "murmur3", 32, 8), new HyperLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 8))
            .put(new AlgorithmMetadata("HyperLogLog", "murmur3", 32, 12), new HyperLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 12))
            .put(new AlgorithmMetadata("HyperLogLog", "murmur3", 32, 16), new HyperLogLog(new GuavaHashWrapper(Hashing.murmur3_32()), 16))

            .put(new AlgorithmMetadata("HyperLogLog", "murmur3", 128, 8), new HyperLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 8))
            .put(new AlgorithmMetadata("HyperLogLog", "murmur3", 128, 12), new HyperLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 12))
            .put(new AlgorithmMetadata("HyperLogLog", "murmur3", 128, 16), new HyperLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 16))
            
            .build();
    


    @Test
    public void realText() throws IOException {
        Path path = Paths.get("src/test/resources/reddit_trump.csv");

        HashSet<String> hashSet = new HashSet<>();
        int requestsCount = 0;

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String lineWithoutSymbols = line.replaceAll("\\P{Alnum}", "");
            String[] words = lineWithoutSymbols.split(" ");
            for (String word : words) {
                logLogs.forEach((key, value) -> value.add(word.getBytes()));
                hashSet.add(word);
                requestsCount++;
            }
        }

        int finalRequestsCount = requestsCount;
        logLogs.forEach((key, value) ->
        {
            try {
                dumpStatistic("reddit_trump", "reddit_trump", key.hashName, key.hashSize, key.algorithmName, key.bitsToTake,
                        finalRequestsCount, hashSet, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dumpUniqueValues("reddit_trump", hashSet);
    }

    @Test
    public void ipDataset() throws IOException {
        Path path = Paths.get("src/test/resources/Dataset-Unicauca-Version2-87Atts.csv");

        HashSet<String> hashSet = new HashSet<>();
        int requestsCount = 0;

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] columns = line.split(",");
            String ip = columns[1];

            byte[] bytes = ip.getBytes();
            logLogs.forEach((key, value) -> value.add(bytes));
            hashSet.add(ip);
            requestsCount++;
        }

        
        int finalRequestsCount = requestsCount;
        logLogs.forEach((key, value) ->
        {
            try {
                dumpStatistic("ip", "IP", key.hashName, key.hashSize, key.algorithmName, key.bitsToTake,
                        finalRequestsCount, hashSet, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        dumpUniqueValues("ip", hashSet);
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

        System.out.println();
        System.out.println(statsText);
        Files.write(summaryFile, statsText.getBytes());
        System.out.println("Stats stored to file " + summaryFile);

        Path bucketsFile = Paths.get(String.format("src/test/resources/out/%s_%s_%d_%s_%d.buckets", dataSetShortName, hashName, hashSize, algorithmName, bitsToTake));

        String bucketsToFile = Arrays.stream(logLog.getMaxRankForBucket())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("\n"));

        System.out.println(bucketsToFile);
        Files.write(bucketsFile, bucketsToFile.getBytes());
        System.out.println("Buckets stored to file " + bucketsFile);
    }

    private void dumpUniqueValues(String dataSetShortName, Set<?> set) throws IOException {
        Path distinctDatasetFile = Paths.get(String.format("src/test/resources/out/%s-distinct.in", dataSetShortName));

        String distinctDatasetString = set.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        System.out.println(distinctDatasetString);
        Files.write(distinctDatasetFile, distinctDatasetString.getBytes());
        System.out.println("Distinct dataset stored to file " + distinctDatasetFile);
    }
    
    private class AlgorithmMetadata {
        private final String algorithmName;
        private final String hashName;
        private final int hashSize;
        private final int bitsToTake;

        private AlgorithmMetadata(String algorithmName, String hashName, int hashSize, int bitsToTake) {
            this.algorithmName = algorithmName;
            this.hashName = hashName;
            this.hashSize = hashSize;
            this.bitsToTake = bitsToTake;
        }
    } 


}
