package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.os.Vibrator;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Calendar;

public class CaAlarmManager implements IUtilManager {
    private static volatile CaAlarmManager instance;
    private Vibrator mVibrator;

    public static CaAlarmManager getInstance() {
        if (instance == null) {
            synchronized (CaAlarmManager.class) {
                if (instance == null) {
                    instance = new CaAlarmManager();
                }
            }
        }
        return instance;
    }

    public final void initializeManager(Context context) {
        if (context == null) {
            CaLogger.error("Context is null");
        } else {
            this.mVibrator = (Vibrator) context.getSystemService("vibrator");
        }
    }

    public final void terminateManager() {
        this.mVibrator = null;
    }

    public final void vibrateAlarm(boolean enable) {
        if (this.mVibrator == null) {
            CaLogger.error("mVibrator is null");
        } else if (enable) {
            CaLogger.debug("vibrate alarm");
            this.mVibrator.vibrate(2000);
        } else {
            CaLogger.debug("vibrate Cancel");
            this.mVibrator.cancel();
        }
    }

    public final int getCurrentHour() {
        return Calendar.getInstance().get(11);
    }

    public final int getCurrentMinute() {
        return Calendar.getInstance().get(12);
    }

    public final int getCurrentSecond() {
        return Calendar.getInstance().get(13);
    }
}
