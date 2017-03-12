package com.android.internal.os;

import android.os.IBinder;
import android.os.WorkSource;

public abstract class SelfHealingServiceInternal {
    public abstract void wakeLockAcquired(IBinder iBinder, int i, String str, String str2, int i2, int i3, WorkSource workSource);

    public abstract void wakeLockChanged(IBinder iBinder, int i, WorkSource workSource);

    public abstract void wakeLockReleased(IBinder iBinder, int i, String str, String str2, int i2, int i3, WorkSource workSource);
}
