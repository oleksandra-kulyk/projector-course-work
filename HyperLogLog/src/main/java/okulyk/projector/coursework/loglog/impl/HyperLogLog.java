package okulyk.projector.coursework.loglog.impl;

import okulyk.projector.coursework.loglog.HashWrapper;

public class HyperLogLog extends LogLogBase {
    public HyperLogLog(HashWrapper hashWrapper, int countOfFirstBitsToTake) {
        super(hashWrapper, countOfFirstBitsToTake);
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
