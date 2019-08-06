package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.HashFunction;

public class SimpleLogLog extends LogLogBase {

    public SimpleLogLog(HashFunction hashFunction, int countOfFirstBitsToTake) {
        super(hashFunction, countOfFirstBitsToTake);
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
