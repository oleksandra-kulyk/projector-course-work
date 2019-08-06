package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.HashFunction;

public class HyperLogLog extends LogLogBase {
    public HyperLogLog(HashFunction hashFunction, int countOfFirstBitsToTake) {
        super(hashFunction, countOfFirstBitsToTake);
    }

    @Override
    public int getCardinality() {
        double harmonicSum = 0;
        for (int i = 0; i < bucketsCount; i++) {
            harmonicSum += 1 / Math.pow(2, maxRankForBucket[i]);
        }

        double alphaConstant = 0.7213 / (1 + 1.079/bucketsCount);
        return (int) ((alphaConstant * bucketsCount * bucketsCount) / harmonicSum);
    }
}
