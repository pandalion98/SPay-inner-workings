package android.app.usage;

import android.util.LongSparseArray;

public class TimeSparseArray<E> extends LongSparseArray<E> {
    public TimeSparseArray(int initialCapacity) {
        super(initialCapacity);
    }

    public int closestIndexOnOrAfter(long time) {
        int size = size();
        int lo = 0;
        int hi = size - 1;
        int mid = -1;
        long key = -1;
        while (lo <= hi) {
            mid = lo + ((hi - lo) / 2);
            key = keyAt(mid);
            if (time > key) {
                lo = mid + 1;
            } else if (time >= key) {
                return mid;
            } else {
                hi = mid - 1;
            }
        }
        if (time < key) {
            return mid;
        }
        return (time <= key || lo >= size) ? -1 : lo;
    }

    public int closestIndexOnOrBefore(long time) {
        int index = closestIndexOnOrAfter(time);
        if (index < 0) {
            return size() - 1;
        }
        return keyAt(index) != time ? index - 1 : index;
    }
}
