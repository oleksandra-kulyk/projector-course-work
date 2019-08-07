package okulyk.projector.coursework.loglog.impl;

import okulyk.projector.coursework.loglog.HashWrapper;
import okulyk.projector.coursework.loglog.LogLog;

import static okulyk.projector.coursework.loglog.BitUtils.findLeftmostOnePositionStartingFromK;
import static okulyk.projector.coursework.loglog.BitUtils.takeFirstKBitsAsInt;

public abstract class LogLogBase implements LogLog {

    private final HashWrapper hashWrapper;
    private final int countOfFirstBitsToTake;
    protected final int[] maxRankForBucket;
    protected final int bucketsCount;

    public LogLogBase(HashWrapper hashWrapper, int countOfFirstBitsToTake) {
        this.hashWrapper = hashWrapper;
        this.countOfFirstBitsToTake = countOfFirstBitsToTake;
        bucketsCount = 1 << countOfFirstBitsToTake;
        maxRankForBucket = new int[bucketsCount];
    }

    public void add(byte[] input) {
        byte[] hashAsBytes = hashWrapper.getHash(input);
        int rank = findLeftmostOnePositionStartingFromK(countOfFirstBitsToTake, hashAsBytes);
        int bucket = takeFirstKBitsAsInt(countOfFirstBitsToTake, hashAsBytes);

        if (maxRankForBucket[bucket] < rank) {
            maxRankForBucket[bucket] = rank;
        }
    }

    public int[] getMaxRankForBucket() {
        return maxRankForBucket;
    }
}
