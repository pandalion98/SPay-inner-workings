package android.hardware.camera2.legacy;

import android.hardware.Camera.Size;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SizeAreaComparator implements Comparator<Size> {
    public int compare(Size size, Size size2) {
        Preconditions.checkNotNull(size, "size must not be null");
        Preconditions.checkNotNull(size2, "size2 must not be null");
        if (size.equals(size2)) {
            return 0;
        }
        long width = (long) size.width;
        long width2 = (long) size2.width;
        long area = width * ((long) size.height);
        long area2 = width2 * ((long) size2.height);
        if (area == area2) {
            if (width <= width2) {
                return -1;
            }
            return 1;
        } else if (area <= area2) {
            return -1;
        } else {
            return 1;
        }
    }

    public static Size findLargestByArea(List<Size> sizes) {
        Preconditions.checkNotNull(sizes, "sizes must not be null");
        return (Size) Collections.max(sizes, new SizeAreaComparator());
    }
}
