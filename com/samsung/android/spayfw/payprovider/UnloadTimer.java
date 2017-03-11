package com.samsung.android.spayfw.payprovider;

import com.samsung.android.spayfw.p002b.Log;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.samsung.android.spayfw.payprovider.j */
public class UnloadTimer {
    private static final HashMap<String, Timer> pf;
    private String cardType;

    static {
        pf = new HashMap();
    }

    public void cancel() {
        Log.m285d("UnloadTimer", "cancel : cardType = " + this.cardType + "; timerObj = " + pf.get(this.cardType));
        if (pf.containsKey(this.cardType)) {
            ((Timer) pf.get(this.cardType)).cancel();
        }
    }

    public void schedule(TimerTask timerTask, long j) {
        Log.m285d("UnloadTimer", "schedule : delay = " + j + "; cardType = " + this.cardType);
        cancel();
        pf.put(this.cardType, new Timer());
        ((Timer) pf.get(this.cardType)).schedule(timerTask, j);
        Log.m285d("UnloadTimer", "schedule : timerObj = " + pf.get(this.cardType));
    }

    public UnloadTimer(String str) {
        this.cardType = str;
    }
}
