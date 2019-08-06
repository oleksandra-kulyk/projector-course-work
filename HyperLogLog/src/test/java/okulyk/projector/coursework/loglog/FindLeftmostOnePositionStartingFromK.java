package okulyk.projector.coursework.loglog;

import com.google.common.primitives.Ints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static okulyk.projector.coursework.loglog.BitUtils.findLeftmostOnePositionStartingFromK;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FindLeftmostOnePositionStartingFromK {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "00000000000000000000000000000000", 3, 0 },
                { "00001000000000000000000000000000", 0, 5 },
                { "00001000000000000000000000000000", 3, 2 },
                { "00000000000000000000000000001000", 24 + 3, 2 },
                { "00000000000000000000000000001000", 3, 24 + 2 },
        });
    }

    private String inputAsBinaryString;
    private int k;
    private int expectedPosition;

    public FindLeftmostOnePositionStartingFromK(String inputAsBinaryString, int k, int expectedPosition) {
        this.inputAsBinaryString = inputAsBinaryString;
        this.k = k;
        this.expectedPosition = expectedPosition;
    }

    @Test
    public void findFirstOneTailingPosition() {
        int inputAsInt = Integer.parseInt(inputAsBinaryString,2 );
        byte[] valueInBytes = Ints.toByteArray(inputAsInt);

        int position = findLeftmostOnePositionStartingFromK(k, valueInBytes);

        assertEquals(expectedPosition, position);
    }
}