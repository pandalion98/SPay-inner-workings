package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;

public class SContextActivityNotificationExAttribute extends SContextAttribute {
    private static final int ACTIVITY_STATUS_MAX = 5;
    private static final String TAG = "SContextActivityNotificationExAttribute";
    private int[] mActivityFilter = new int[]{4};
    private int mDuration = 30;

    SContextActivityNotificationExAttribute() {
        setAttribute();
    }

    public SContextActivityNotificationExAttribute(int[] activityFilter, int duration) {
        this.mActivityFilter = activityFilter;
        this.mDuration = duration;
        setAttribute();
    }

    boolean checkAttribute() {
        ArrayList<Integer> list = new ArrayList();
        int i = 0;
        while (i < this.mActivityFilter.length) {
            if ((this.mActivityFilter[i] < 0 || this.mActivityFilter[i] > 5) && this.mActivityFilter[i] != 30) {
                Log.e(TAG, "The activity status is wrong.");
                return false;
            }
            list.add(Integer.valueOf(this.mActivityFilter[i]));
            for (int j = 0; j < i; j++) {
                if (list.get(i) == list.get(j)) {
                    Log.e(TAG, "This activity status cannot have duplicated status.");
                    return false;
                }
            }
            i++;
        }
        if (this.mDuration >= 0) {
            return true;
        }
        Log.e(TAG, "The duration is wrong.");
        return false;
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putIntArray("activity_filter", this.mActivityFilter);
        attribute.putInt("duration", this.mDuration);
        super.setAttribute(30, attribute);
    }
}
