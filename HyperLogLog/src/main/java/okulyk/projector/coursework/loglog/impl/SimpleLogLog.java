package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import okulyk.projector.coursework.loglog.LogLog;

import static okulyk.projector.coursework.loglog.BitUtils.findLeftmostOnePositionStartingFromK;
import static okulyk.projector.coursework.loglog.BitUtils.takeFirstKBitsAsInt;

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

    public int getCardinality() {
        double sum = 0;
        for (int i = 0; i < bucketsCount; i++) {
            sum += maxRankForBucket[i];
        }
        double average = sum / bucketsCount;

        double alphaConstant = 0.79402;
        return (int) (alphaConstant * bucketsCount * Math.pow(2, average));
    }

}
