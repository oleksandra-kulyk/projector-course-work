package okulyk.projector.coursework.loglog;

import com.google.common.primitives.Ints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static okulyk.projector.coursework.loglog.BitUtils.takeFirstKBitsAsInt;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TakeFirstKBitsAsIntTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "00000000000000000000000000000000", 3, "000" },
                { "00001000", 3, "000" },
                { "00100000000000000000000000000000", 4, "0010" },
                { "00000101000001010000010100000101", 17, "00000101000001010" },
        });
    }

    private String inputAsBinaryString;
    private int k;
    private String expectedIntAsBinaryString;

    public TakeFirstKBitsAsIntTest(String inputAsBinaryString, int k, String expectedIntAsBinaryString) {
        this.inputAsBinaryString = inputAsBinaryString;
        this.k = k;
        this.expectedIntAsBinaryString = expectedIntAsBinaryString;
    }

    @Test
    public void takeFirstKBitsAsIntTest() {
        int inputAsInt = Integer.parseInt(inputAsBinaryString,2 );
        byte[] valueInBytes = Ints.toByteArray(inputAsInt);

        int value = takeFirstKBitsAsInt(k, valueInBytes);

        assertEquals(Integer.parseInt(expectedIntAsBinaryString,2 ), value);
    }
}