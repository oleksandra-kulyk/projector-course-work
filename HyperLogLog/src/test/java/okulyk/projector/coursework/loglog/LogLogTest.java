package okulyk.projector.coursework.loglog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import com.google.common.hash.Hashing;
import okulyk.projector.coursework.loglog.impl.HyperLogLog;
import org.junit.Test;

public class LogLogTest {

    private final Path path = Paths.get("src/test/resources/reddit_trump.csv");
    private HyperLogLog hyperLogLog = new HyperLogLog(Hashing.murmur3_128(), 12);

    @Test
    public void realText() throws IOException {
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

        System.out.println(String.format("calculated cardinality for text is %d real %d", hyperLogLog.getCardinality(), hashSet.size()));
    }
}
