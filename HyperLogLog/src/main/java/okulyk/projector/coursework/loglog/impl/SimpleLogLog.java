package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import okulyk.projector.coursework.loglog.LogLog;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class SimpleLogLog implements LogLog {

    private final HashFunction hashFunction;
    private final int countOfFirstBitsToTake;
    private final int[] maxRankForBucket;
    private final int bucketsCount;

    public SimpleLogLog(HashFunction hashFunction, int countOfFirstBitsToTake) {
        this.hashFunction = hashFunction;
        this.countOfFirstBitsToTake = countOfFirstBitsToTake;
        bucketsCount = 1 << countOfFirstBitsToTake;
        maxRankForBucket = new int[bucketsCount];
    }

    public void add(byte[] input) {
        HashCode hashCode = hashFunction.hashBytes(input);
        byte[] hashAsBytes = hashCode.asBytes();
        int rank = findLeftmostOnePositionStartingFromK(countOfFirstBitsToTake, hashAsBytes);
        int bucket = takeFirstKBitsAsInt(countOfFirstBitsToTake, hashAsBytes);

        if (maxRankForBucket[bucket] < rank) {
            maxRankForBucket[bucket] = rank;
        }
    }

    public int findLeftmostOnePositionStartingFromK(int k, byte[] bytes) {
        int position = 1;
        for (int i = bytes.length - 1; i >= 0; i--) {
            byte aByte = bytes[i];
            for (int j = 0; j < 8; j++) {
                if (((aByte >> j) & 1) == 1) {
                    return position;
                }
                position++;
            }
        }
        return 0;
    }

    public int takeFirstKBitsAsInt(int k, byte[] bytes) {
        int result = 0;
        for (int i = 0; i < k; i++) {
            byte aByte = bytes[i / 8];
            byte bitNumber = (byte) (7 - (i % 8));
            int bit = aByte & (1 << bitNumber);
            if (bit > 0) {
                result += 1 << (k - i - 1);
            }
        }
        return result;
    }

    public int getCardinality() {
        double average = calculateAverage();
        double alfaConstant = 0.79402;
        return (int) (alfaConstant * bucketsCount * Math.pow(2, average));
    }

    private double calculateAverage() {
        IntSummaryStatistics statistics = Arrays.stream(maxRankForBucket)
                .summaryStatistics();
        double average = statistics.getAverage();
        return average;
    }

}
