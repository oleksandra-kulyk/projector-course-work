package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import okulyk.projector.coursework.loglog.LogLog;

import static okulyk.projector.coursework.loglog.BitUtils.findLeftmostOnePositionStartingFromK;
import static okulyk.projector.coursework.loglog.BitUtils.takeFirstKBitsAsInt;

public abstract class LogLogBase implements LogLog {

    private final HashFunction hashFunction;
    private final int countOfFirstBitsToTake;
    protected final int[] maxRankForBucket;
    protected final int bucketsCount;

    public LogLogBase(HashFunction hashFunction, int countOfFirstBitsToTake) {
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

}
