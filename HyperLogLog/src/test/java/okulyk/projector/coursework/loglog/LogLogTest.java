package okulyk.projector.coursework.loglog;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Ints;
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
        String dataSetShortName = "reddit_trump";
        Path path = Paths.get("src/test/resources/in/reddit_trump.csv");

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

        dumpStatistic(dataSetShortName, requestsCount, hashSet);
        dumpUniqueValues(dataSetShortName, hashSet);
    }

    @Test
    public void ipDataset() throws IOException {
        String dataSetShortName = "ip";
        Path path = Paths.get("src/test/resources/in/Dataset-Unicauca-Version2-87Atts.csv");

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

        dumpStatistic(dataSetShortName, requestsCount, hashSet);
        dumpUniqueValues(dataSetShortName, hashSet);
    }

    @Test
    public void portDataset() throws IOException {
        String dataSetShortName = "port";
        Path path = Paths.get("src/test/resources/in/Dataset-Unicauca-Version2-87Atts.csv");

        HashSet<String> hashSet = new HashSet<>();
        int requestsCount = 0;

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] columns = line.split(",");
            String ip = columns[2];

            byte[] bytes = ip.getBytes();
            logLogs.forEach((key, value) -> value.add(bytes));
            hashSet.add(ip);
            requestsCount++;
        }

        dumpStatistic(dataSetShortName, requestsCount, hashSet);
        dumpUniqueValues(dataSetShortName, hashSet);
    }

    @Test
    public void sequentialUniqueHundredThousandInts() throws IOException {
        String dataSetShortName = "sequentialUniqueHundredThousandInts";

        HashSet<Integer> hashSet = new HashSet<>();
        int requestsCount = 50_000_000;

        for (int i = 0; i < requestsCount; i++) {
            byte[] bytes = Ints.toByteArray(i);
            logLogs.forEach((key, value) -> value.add(bytes));
            hashSet.add(i);

            if (i % 10000 == 0) {
                System.out.println(i);
            }
        }

        dumpStatistic(dataSetShortName, requestsCount, hashSet);
        dumpUniqueValues(dataSetShortName, hashSet);
    }

    @Test
    public void randomHundredThousandInts() throws IOException {
        String dataSetShortName = "randomHundredThousandInts";
        Random random = new Random();


        HashSet<Integer> hashSet = new HashSet<>();
        int requestsCount = 50_000_000;

        for (int i = 0; i < requestsCount; i++) {
            int randomInt = random.nextInt();
            byte[] bytes = Ints.toByteArray(randomInt);
            logLogs.forEach((key, value) -> value.add(bytes));
            hashSet.add(randomInt);

            if (i % 10000 == 0) {
                System.out.println(i);
            }
        }

        dumpStatistic(dataSetShortName, requestsCount, hashSet);
        dumpUniqueValues(dataSetShortName, hashSet);
    }

    @Test
    public void sequentialUniqueTenThousandRepeatedInts() throws IOException {
        String dataSetShortName = "sequentialUniqueTenThousandRepeatedInts";

        HashSet<Integer> hashSet = new HashSet<>();
        int requestsCount = 10_000;
        int iterationsCount = 1000;

        for (int j = 0; j < iterationsCount; j++) {
            for (int i = 0; i < requestsCount; i++) {
                byte[] bytes = Ints.toByteArray(i);
                logLogs.forEach((key, value) -> value.add(bytes));
                hashSet.add(i);
            }
            System.out.println(j);
        }

        int finalRequestsCount = requestsCount * iterationsCount;

        dumpStatistic(dataSetShortName, finalRequestsCount, hashSet);
        dumpUniqueValues(dataSetShortName, hashSet);
    }


    private void dumpStatistic(String dataSetShortName, int requestsCount, Set set) throws IOException {
        dumpGeneralStatistic(dataSetShortName, requestsCount, set);
        dumpBuckets(dataSetShortName);
    }

    private void dumpBuckets(String dataSetShortName) {
        logLogs.forEach((key, value) ->
        {
            try {
                Path bucketsFile = Paths.get(String.format("src/test/resources/out/buckets/%s/%s_%s_%d_%s_%d.buckets",dataSetShortName, dataSetShortName, key.hashName, key.hashSize, key.algorithmName, key.bitsToTake));

                String bucketsToFile = Arrays.stream(value.getMaxRankForBucket())
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining("\n"));

                Files.write(bucketsFile, bucketsToFile.getBytes());
                System.out.println("Buckets stored to file " + bucketsFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void dumpGeneralStatistic(String dataSetShortName, int requestsCount, Set set) throws IOException {
        int actualCardinality = set.size();

        String setMemory = "undef";
        try {
            setMemory = SizeOf.humanReadable(SizeOf.deepSizeOf(set));
        } catch (Exception e) {
            System.out.println(e);
        }

        Path summaryFile = Paths.get(String.format("src/test/resources/out/generalStats/%s.csv", dataSetShortName));

        String header = "dataSetShortName,hashName,hashSize,algorithmName,bitsToTake,requestsCount,actualCardinality,calculatedCardinality,diff,diffInPercentage,setMemory,logLogMemory";
        String finalSetMemory = setMemory;
        String results = logLogs.entrySet().stream()
                .map(entry -> {
                    int calculatedCardinality = entry.getValue().getCardinality();
                    int diff = calculatedCardinality - actualCardinality;
                    double diffInPercentage = (double) diff / actualCardinality * 100;
                    String logLogMemory = "undef";
                    try {
                        logLogMemory = SizeOf.humanReadable(SizeOf.deepSizeOf(entry.getValue().getMaxRankForBucket()));
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    return String.format("%s,%s,%d,%s,%d,%d,%d,%d,%d,%.2f,%s,%s",
                            dataSetShortName,
                            entry.getKey().hashName,
                            entry.getKey().hashSize,
                            entry.getKey().algorithmName,
                            entry.getKey().bitsToTake,
                            requestsCount,
                            actualCardinality,
                            calculatedCardinality,
                            diff,
                            diffInPercentage,
                            finalSetMemory,
                            logLogMemory);
                })
                .collect(Collectors.joining("\n"));


        System.out.println();
        System.out.println(results);
        Files.write(summaryFile, (header + "\n" + results) .getBytes());
        System.out.println("Stats stored to file " + summaryFile);
    }

    private void dumpUniqueValues(String dataSetShortName, Set<?> set) throws IOException {
        Path distinctDatasetFile = Paths.get(String.format("src/test/resources/out/distinct/%s-distinct.in", dataSetShortName));

        String distinctDatasetString = set.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

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
