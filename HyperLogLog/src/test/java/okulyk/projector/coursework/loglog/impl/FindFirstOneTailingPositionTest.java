package okulyk.projector.coursework.loglog.impl;

import com.google.common.primitives.Ints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.google.common.hash.Hashing.murmur3_32;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FindFirstOneTailingPositionTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }, { 1, 1 }, { 8, 4 }, { 5, 1 }, { 6, 2 }
        });
    }

    private int input;
    private int expectedPosition;

    public FindFirstOneTailingPositionTest(int input, int expectedPosition) {
        this.input = input;
        this.expectedPosition = expectedPosition;
    }

    private SimpleLogLog simpleLogLog = new SimpleLogLog(murmur3_32(), 8);

    @Test
    public void findFirstOneTailingPosition() {
        byte[] valueInBytes = Ints.toByteArray(input);

        int position = simpleLogLog.findLeftmostOnePositionStartingFromK(valueInBytes);

        assertEquals(expectedPosition, position);
    }
}