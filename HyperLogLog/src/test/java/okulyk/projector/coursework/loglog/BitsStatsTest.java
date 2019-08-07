package okulyk.projector.coursework.loglog;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BitsStatsTest {

    @Test
    public void ipDataset() throws IOException {
        Path path = Paths.get("src/test/resources/Dataset-Unicauca-Version2-87Atts.csv");

        HashSet<String> hashSet = new HashSet<>();

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] columns = line.split(",");
            String ip = columns[1];
            hashSet.add(ip);
        }

        HashMap<Integer, Integer> noHash12BitDistribution = new HashMap<>();
        for (String ip : hashSet) {
            byte[] bytes = ip.getBytes();

            int leftmostOnePositionStartingFromK = BitUtils.findLeftmostOnePositionStartingFromK(0, bytes);
            incrementValueInBitsDistributionMap(noHash12BitDistribution, leftmostOnePositionStartingFromK);
        }

        dumpBitsDistribution("ip", "murmur3", 128, noHash12BitDistribution);
    }

    private void incrementValueInBitsDistributionMap(HashMap<Integer, Integer> noHashBitDistribution, int leftmostOnePositionStartingFromK) {
        Integer existingValue = noHashBitDistribution.get(leftmostOnePositionStartingFromK);
        if (existingValue == null) {
            noHashBitDistribution.put(leftmostOnePositionStartingFromK, 1);
        } else {
            existingValue++;
            noHashBitDistribution.put(leftmostOnePositionStartingFromK, existingValue);
        }
    }

    private void dumpBitsDistribution(String dataSetShortName, String hashName, int hashSize, Map<Integer, Integer> distributionMap) throws IOException {
        Path distributionFile = Paths.get(String.format("src/test/resources/out/%s_%s_%d-bits.distribution", dataSetShortName, hashName, hashSize));

        String distributionToWrite = distributionMap.entrySet()
                .stream()
                .map(entry -> String.format("%d,%d", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));

        Files.write(distributionFile, distributionToWrite.getBytes());
        System.out.println("Distribution stored to file " + distributionFile);
    }
}
