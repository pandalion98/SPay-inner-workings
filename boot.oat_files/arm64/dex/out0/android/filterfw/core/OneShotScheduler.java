package android.filterfw.core;

import android.util.Log;
import java.util.HashMap;

public class OneShotScheduler extends RoundRobinScheduler {
    private static final String TAG = "OneShotScheduler";
    private final boolean mLogVerbose = Log.isLoggable(TAG, 2);
    private HashMap<String, Integer> scheduled = new HashMap();

    public OneShotScheduler(FilterGraph graph) {
        super(graph);
    }

    public void reset() {
        super.reset();
        this.scheduled.clear();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.filterfw.core.Filter scheduleNextNode() {
        /*
        r5 = this;
        r2 = 0;
        r1 = 0;
    L_0x0002:
        r0 = super.scheduleNextNode();
        if (r0 != 0) goto L_0x0015;
    L_0x0008:
        r3 = r5.mLogVerbose;
        if (r3 == 0) goto L_0x0013;
    L_0x000c:
        r3 = "OneShotScheduler";
        r4 = "No filters available to run.";
        android.util.Log.v(r3, r4);
    L_0x0013:
        r0 = r2;
    L_0x0014:
        return r0;
    L_0x0015:
        r3 = r5.scheduled;
        r4 = r0.getName();
        r3 = r3.containsKey(r4);
        if (r3 != 0) goto L_0x0064;
    L_0x0021:
        r2 = r0.getNumberOfConnectedInputs();
        if (r2 != 0) goto L_0x0035;
    L_0x0027:
        r2 = r5.scheduled;
        r3 = r0.getName();
        r4 = 1;
        r4 = java.lang.Integer.valueOf(r4);
        r2.put(r3, r4);
    L_0x0035:
        r2 = r5.mLogVerbose;
        if (r2 == 0) goto L_0x0014;
    L_0x0039:
        r2 = "OneShotScheduler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Scheduling filter \"";
        r3 = r3.append(r4);
        r4 = r0.getName();
        r3 = r3.append(r4);
        r4 = "\" of type ";
        r3 = r3.append(r4);
        r4 = r0.getFilterClassName();
        r3 = r3.append(r4);
        r3 = r3.toString();
        android.util.Log.v(r2, r3);
        goto L_0x0014;
    L_0x0064:
        if (r1 != r0) goto L_0x0073;
    L_0x0066:
        r3 = r5.mLogVerbose;
        if (r3 == 0) goto L_0x0071;
    L_0x006a:
        r3 = "OneShotScheduler";
        r4 = "One pass through graph completed.";
        android.util.Log.v(r3, r4);
    L_0x0071:
        r0 = r2;
        goto L_0x0014;
    L_0x0073:
        if (r1 != 0) goto L_0x0002;
    L_0x0075:
        r1 = r0;
        goto L_0x0002;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.filterfw.core.OneShotScheduler.scheduleNextNode():android.filterfw.core.Filter");
    }
}
