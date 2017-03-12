package com.samsung.android.contextaware.utilbundle;

import android.text.format.Time;

public class CaCurrentUtcTimeManager {
    private static volatile CaCurrentUtcTimeManager instance;

    public static CaCurrentUtcTimeManager getInstance() {
        if (instance == null) {
            synchronized (CaCurrentUtcTimeManager.class) {
                if (instance == null) {
                    instance = new CaCurrentUtcTimeManager();
                }
            }
        }
        return instance;
    }

    public final int[] getUtcTime() {
        Time time = new Time();
        time.set(System.currentTimeMillis());
        time.switchTimezone("GMT+00:00");
        return new int[]{time.hour, time.minute, time.second};
    }
}
