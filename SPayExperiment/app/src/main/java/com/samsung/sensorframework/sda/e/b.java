/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.Thread
 */
package com.samsung.sensorframework.sda.e;

import android.os.SystemClock;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.d.a.m;

public class b
extends a {
    public b(com.samsung.sensorframework.sda.d.b b2) {
        super(b2);
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected void a(Object var1_1, long var2_2) {
        block8 : {
            Log.d(this.he(), "waitElapsedRealtime() original sleep duration: " + var2_2);
            var4_3 = var2_2;
            block5 : while (var2_2 > 0L && this.state == 6123) {
                var6_5 = Math.min((long)15000L, (long)var2_2);
                var8_6 = SystemClock.elapsedRealtime();
                var1_1.wait(var6_5);
                var10_7 = var2_2 - (SystemClock.elapsedRealtime() - var8_6);
                var13_9 = (Long)this.KM.cj("POST_SENSE_SLEEP_LENGTH_MILLIS");
                ** if (var13_9 == var4_3) goto lbl-1000
lbl-1000: // 1 sources:
                {
                    Log.d(this.he(), "waitElapsedRealtime() Sleep duration config has changed, Current config: " + var4_3 + " Time remaining in current config: " + var10_7 + " Updated config: " + var13_9);
                    Log.d(this.he(), "waitElapsedRealtime() sleeping for: " + var13_9);
                    var15_10 = var13_9;
lbl16: // 3 sources:
                    do {
                        var2_2 = var13_9;
                        var4_3 = var15_10;
                        continue block5;
                        break;
                    } while (true);
                }
lbl-1000: // 1 sources:
                {
                    break block8;
                }
            }
            return;
            catch (SDAException var12_8) {
                var13_9 = var10_7;
                var15_10 = var4_3;
            }
            ** GOTO lbl16
            catch (SDAException var17_4) {
                var15_10 = var13_9;
            }
            ** GOTO lbl16
        }
        var13_9 = var10_7;
        var15_10 = var4_3;
        ** while (true)
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void run() {
        do {
            block12 : {
                if (this.state != 6123) {
                    this.KM.gY();
                    Log.d(this.he(), "Stopped PullSensorTask.");
                    return;
                }
                try {
                    Log.d(this.he(), "Pulling from: " + com.samsung.sensorframework.sda.d.c.ap(this.KM.getSensorType()));
                    this.c(((m)this.KM).hn());
                    var7_8 = (Long)this.KM.cj("POST_SENSE_SLEEP_LENGTH_MILLIS");
                    var11_10 = var9_9 = this.KN;
                    // MONITORENTER : var11_10
                    if (this.state != 6123) break block12;
                }
                catch (InterruptedException var6_7) {
                    continue;
                }
                catch (SDAException var1_2) {}
                this.a(this.KN, var7_8);
            }
            // MONITOREXIT : var11_10
            continue;
            ** GOTO lbl-1000
            catch (SecurityException var1_3) {}
lbl-1000: // 2 sources:
            {
                var1_1.printStackTrace();
                try {
                    var3_5 = var1_1 instanceof SecurityException != false ? 300000L : (var1_1 instanceof SDAException != false && (var5_6 = ((SDAException)var1_1).getErrorCode()) == 8000 ? 300000L : 300000L);
                }
                catch (Exception var2_4) {
                    var2_4.printStackTrace();
                }
                Thread.sleep((long)var3_5);
                continue;
            }
            break;
        } while (true);
    }
}

