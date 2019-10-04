/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.Thread
 */
package com.samsung.sensorframework.sda.e;

import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.b;
import com.samsung.sensorframework.sda.d.b.j;
import com.samsung.sensorframework.sda.e.a;

public class c
extends a
implements b {
    public c(com.samsung.sensorframework.sda.d.b b2) {
        super(b2);
    }

    @Override
    public void a(com.samsung.sensorframework.sda.b.a a2) {
        super.c(a2);
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
            block17 : {
                block16 : {
                    block15 : {
                        if (this.state != 6123) break block16;
                        try {
                            if (!this.KM.hf()) {
                                ((j)this.KM).a(this);
                                continue;
                            }
                            var10_9 = var8_8 = this.KN;
                            // MONITORENTER : var10_9
                            if (this.state != 6123) break block15;
                        }
                        catch (SDAException var2_2) {}
                        this.KN.wait();
                    }
                    // MONITOREXIT : var10_9
                    continue;
                    ** GOTO lbl-1000
                }
                if (!this.KM.hf()) break block17;
                try {
                    ((j)this.KM).b(this);
                }
                catch (SDAException var1_10) {}
                ** GOTO lbl-1000
                catch (SecurityException var1_12) {}
lbl-1000: // 2 sources:
                {
                    var1_11.printStackTrace();
                }
            }
            this.KM.gY();
            com.samsung.android.spayfw.b.c.d(this.he(), "Stopped PushSensorTask.");
            return;
            catch (SecurityException var2_3) {}
lbl-1000: // 2 sources:
            {
                var2_1.printStackTrace();
                try {
                    var4_5 = var2_1 instanceof SecurityException != false ? 300000L : (var2_1 instanceof SDAException != false && (var6_6 = ((SDAException)var2_1).getErrorCode()) == 8000 ? 300000L : 30000L);
                }
                catch (Exception var3_4) {
                    var3_4.printStackTrace();
                }
                Thread.sleep((long)var4_5);
                continue;
            }
            catch (InterruptedException var7_7) {
                continue;
            }
            break;
        } while (true);
    }
}

