package okulyk.projector.coursework.loglog.impl;

import com.google.common.hash.HashFunction;

import java.util.Arrays;

public class SuperLogLog extends LogLogBase {

    public SuperLogLog(HashFunction hashFunction, int countOfFirstBitsToTake) {
        super(hashFunction, countOfFirstBitsToTake);
    }

    @Override
    public int getCardinality() {
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
}
