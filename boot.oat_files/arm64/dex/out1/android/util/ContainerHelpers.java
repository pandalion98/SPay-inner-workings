package android.util;

class ContainerHelpers {
    ContainerHelpers() {
    }

    static int binarySearch(int[] array, int size, int value) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int i = (lo + hi) >>> 1;
            int midVal = array[i];
            if (midVal < value) {
                lo = i + 1;
            } else if (midVal <= value) {
                return i;
            } else {
                hi = i - 1;
            }
        }
        return lo ^ -1;
    }

    static int binarySearch(long[] array, int size, long value) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int i = (lo + hi) >>> 1;
            long midVal = array[i];
            if (midVal < value) {
                lo = i + 1;
            } else if (midVal <= value) {
                return i;
            } else {
                hi = i - 1;
            }
        }
        return lo ^ -1;
    }
}
