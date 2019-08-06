package okulyk.projector.coursework.loglog;

public interface LogLog {
    void add(byte[] input);

    int getCardinality();

    int[] getMaxRankForBucket();
}
