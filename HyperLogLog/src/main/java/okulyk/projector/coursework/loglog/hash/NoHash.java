package okulyk.projector.coursework.loglog.hash;

import okulyk.projector.coursework.loglog.HashWrapper;

public class NoHash implements HashWrapper {
    @Override
    public byte[] getHash(byte[] value) {
        return value;
    }
}
