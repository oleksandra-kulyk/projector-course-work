package okulyk.projector.coursework.loglog.impl;

import okulyk.projector.coursework.loglog.HashWrapper;

public class SimpleLogLog extends LogLogBase {

    public SimpleLogLog(HashWrapper hashWrapper, int countOfFirstBitsToTake) {
        super(hashWrapper, countOfFirstBitsToTake);
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
