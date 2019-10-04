/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.payprovider;

import com.samsung.android.spayfw.b.c;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class j {
    private static final HashMap<String, Timer> pf = new HashMap();
    private String cardType;

    public j(String string) {
        this.cardType = string;
    }

    public void cancel() {
        c.d("UnloadTimer", "cancel : cardType = " + this.cardType + "; timerObj = " + pf.get((Object)this.cardType));
        if (pf.containsKey((Object)this.cardType)) {
            ((Timer)pf.get((Object)this.cardType)).cancel();
        }
    }

    public void schedule(TimerTask timerTask, long l2) {
        c.d("UnloadTimer", "schedule : delay = " + l2 + "; cardType = " + this.cardType);
        this.cancel();
        pf.put((Object)this.cardType, (Object)new Timer());
        ((Timer)pf.get((Object)this.cardType)).schedule(timerTask, l2);
        c.d("UnloadTimer", "schedule : timerObj = " + pf.get((Object)this.cardType));
    }
}

