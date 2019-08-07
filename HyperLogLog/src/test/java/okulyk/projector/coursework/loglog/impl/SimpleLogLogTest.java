package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.Hashing;
import com.google.common.primitives.Ints;
import okulyk.projector.coursework.loglog.hash.GuavaHashWrapper;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;

public class SimpleLogLogTest {
    private SimpleLogLog simpleLogLog = new SimpleLogLog(new GuavaHashWrapper(Hashing.murmur3_128()), 5);
    private final Random random = new Random();

    @Test
    public void randomInt() {
        int cardinality = 10_000_000;

        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 0; i < cardinality; i++) {
            int value = random.nextInt();
            hashSet.add(value);
            simpleLogLog.add(Ints.toByteArray(value));
        }

        System.out.println(String.format("calculated cardinality for random in is %d real %d", simpleLogLog.getCardinality(), hashSet.size()));
    }

    @Test
    public void sequentialInt() {
        int cardinality = 100_000_000;
        for (int i = 0; i < cardinality; i++) {
            simpleLogLog.add(Ints.toByteArray(i));
        }

        System.out.println("sequential int:" + simpleLogLog.getCardinality());
    }
}