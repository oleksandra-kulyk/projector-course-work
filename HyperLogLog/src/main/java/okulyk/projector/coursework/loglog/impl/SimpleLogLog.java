package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import okulyk.projector.coursework.loglog.LogLog;

import java.util.Arrays;

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
        for (int i = 0; i < bytes.length; i++) {
            byte aByte = bytes[i];
            for (int j = 7; j >= 0; j--) {
                if (i * 8 + (7 - j) >= k) { //skipFirstKBits
                    int bit = aByte & (1 << j);
                    if (bit > 0) {
                        return position;
                    }
                    position++;
                }
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
        return hyperLogLog();
    }

    private int superLogLog() {
        Arrays.sort(maxRankForBucket);
        int sum = 0;
        double countToTake = bucketsCount * 0.7;
        for (int i = 0; i < countToTake; i++) { //skip max 30%
            sum += maxRankForBucket[i];
        }
        double average = sum / countToTake;

        double alphaConstant = 0.79402;
        return (int) (alphaConstant * bucketsCount * Math.pow(2, average));
    }

    private int hyperLogLog() {
        double harmonicSum = 0;
        for (int i = 0; i < bucketsCount; i++) {
            harmonicSum += 1 / Math.pow(2, maxRankForBucket[i]);
        }

        double alphaConstant = 0.7213 / (1 + 1.079/bucketsCount);
        return (int) ((alphaConstant * bucketsCount * bucketsCount) / harmonicSum);
    }

}
