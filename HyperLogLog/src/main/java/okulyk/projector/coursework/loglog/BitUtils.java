package okulyk.projector.coursework.loglog;

public class BitUtils {

    public static int findLeftmostOnePositionStartingFromK(int k, byte[] bytes) {
        int position = 1;
        for (int i = 0; i < bytes.length; i++) {
            byte aByte = bytes[i];
            for (int j = 7; j >= 0; j--) {
                if (i * 8 + (7 - j) >= k) { //skipFirstKBits
                    int bit = aByte & (1 << j);
                    if (bit > 0) {
                        return position;
                    }
                    position++;
                }
            }
        }
        return 0;
    }

    public static int takeFirstKBitsAsInt(int k, byte[] bytes) {
        int result = 0;
        for (int i = 0; i < Math.min(k, bytes.length); i++) {
            byte aByte = bytes[i / 8];
            byte bitNumber = (byte) (7 - (i % 8));
            int bit = aByte & (1 << bitNumber);
            if (bit > 0) {
                result += 1 << (k - i - 1);
            }
        }
        return result;
    }
}
