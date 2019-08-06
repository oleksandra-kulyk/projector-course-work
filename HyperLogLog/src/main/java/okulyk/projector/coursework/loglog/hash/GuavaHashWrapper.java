package okulyk.projector.coursework.loglog.hash;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import okulyk.projector.coursework.loglog.HashWrapper;

public class GuavaHashWrapper implements HashWrapper {

    private final HashFunction hashFunction;

    public GuavaHashWrapper(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    public byte[] getHash(byte[] value) {
        HashCode hashCode = hashFunction.hashBytes(value);
        return hashCode.asBytes();
    }
}
