package android.hardware.camera2.utils;

public final class HashCodeHelpers {
    public static int hashCode(int... array) {
        if (array == null) {
            return 0;
        }
        int h = 1;
        for (int x : array) {
            h = ((h << 5) - h) ^ x;
        }
        return h;
    }

    public static int hashCode(float... array) {
        if (array == null) {
            return 0;
        }
        int h = 1;
        for (float f : array) {
            h = ((h << 5) - h) ^ Float.floatToIntBits(f);
        }
        return h;
    }

    public static <T> int hashCodeGeneric(T... array) {
        if (array == null) {
            return 0;
        }
        int h = 1;
        for (T o : array) {
            h = ((h << 5) - h) ^ (o == null ? 0 : o.hashCode());
        }
        return h;
    }
}
