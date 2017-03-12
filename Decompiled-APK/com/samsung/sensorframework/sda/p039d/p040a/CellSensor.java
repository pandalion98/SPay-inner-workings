package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.CellData;
import com.samsung.sensorframework.sda.p036c.p037a.CellProcessor;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.d.a.g */
public class CellSensor extends AbstractPullSensor {
    private static CellSensor JE;
    private static final String[] Jz;
    private static final Object lock;
    private ArrayList<HashMap<String, Object>> Ik;
    private CellData JF;

    protected /* synthetic */ SensorData hl() {
        return ht();
    }

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_COARSE_LOCATION"};
    }

    public static CellSensor aS(Context context) {
        if (JE == null) {
            synchronized (lock) {
                if (JE == null) {
                    JE = new CellSensor(context);
                }
            }
        }
        return JE;
    }

    private CellSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        JE = null;
    }

    protected String he() {
        return "CellSensor";
    }

    public int getSensorType() {
        return 5037;
    }

    protected String[] hb() {
        return Jz;
    }

    protected CellData ht() {
        return this.JF;
    }

    protected void hm() {
        this.JF = ((CellProcessor) hi()).m1543a(this.Jn, this.Ik, this.Id.gS());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean hc() {
        /*
        r10 = this;
        r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0057 }
        r2.<init>();	 Catch:{ Exception -> 0x0057 }
        r10.Ik = r2;	 Catch:{ Exception -> 0x0057 }
        r2 = r10.HR;	 Catch:{ Exception -> 0x0057 }
        r3 = "phone";
        r2 = r2.getSystemService(r3);	 Catch:{ Exception -> 0x0057 }
        r2 = (android.telephony.TelephonyManager) r2;	 Catch:{ Exception -> 0x0057 }
        r2 = r2.getAllCellInfo();	 Catch:{ Exception -> 0x0057 }
        if (r2 == 0) goto L_0x00d3;
    L_0x0017:
        r9 = r2.iterator();	 Catch:{ Exception -> 0x0057 }
    L_0x001b:
        r2 = r9.hasNext();	 Catch:{ Exception -> 0x0057 }
        if (r2 == 0) goto L_0x00d3;
    L_0x0021:
        r2 = r9.next();	 Catch:{ Exception -> 0x0057 }
        r0 = r2;
        r0 = (android.telephony.CellInfo) r0;	 Catch:{ Exception -> 0x0057 }
        r8 = r0;
        r2 = 0;
        r3 = r8 instanceof android.telephony.CellInfoGsm;	 Catch:{ Exception -> 0x0057 }
        if (r3 == 0) goto L_0x0060;
    L_0x002e:
        r0 = r8;
        r0 = (android.telephony.CellInfoGsm) r0;	 Catch:{ Exception -> 0x0057 }
        r2 = r0;
        r3 = r2.getCellIdentity();	 Catch:{ Exception -> 0x0057 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0057 }
        r4 = r2.getCellSignalStrength();	 Catch:{ Exception -> 0x0057 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0057 }
        r5 = r2.isRegistered();	 Catch:{ Exception -> 0x0057 }
        r6 = r2.getTimeStamp();	 Catch:{ Exception -> 0x0057 }
        r2 = r10;
        r2 = r2.m1591a(r3, r4, r5, r6);	 Catch:{ Exception -> 0x0057 }
    L_0x004f:
        if (r8 == 0) goto L_0x001b;
    L_0x0051:
        r3 = r10.Ik;	 Catch:{ Exception -> 0x0057 }
        r3.add(r2);	 Catch:{ Exception -> 0x0057 }
        goto L_0x001b;
    L_0x0057:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00d7 }
        r10.ho();
    L_0x005e:
        r2 = 1;
        return r2;
    L_0x0060:
        r3 = r8 instanceof android.telephony.CellInfoCdma;	 Catch:{ Exception -> 0x0057 }
        if (r3 == 0) goto L_0x0086;
    L_0x0064:
        r0 = r8;
        r0 = (android.telephony.CellInfoCdma) r0;	 Catch:{ Exception -> 0x0057 }
        r2 = r0;
        r3 = r2.getCellIdentity();	 Catch:{ Exception -> 0x0057 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0057 }
        r4 = r2.getCellSignalStrength();	 Catch:{ Exception -> 0x0057 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0057 }
        r5 = r2.isRegistered();	 Catch:{ Exception -> 0x0057 }
        r6 = r2.getTimeStamp();	 Catch:{ Exception -> 0x0057 }
        r2 = r10;
        r2 = r2.m1591a(r3, r4, r5, r6);	 Catch:{ Exception -> 0x0057 }
        goto L_0x004f;
    L_0x0086:
        r3 = r8 instanceof android.telephony.CellInfoLte;	 Catch:{ Exception -> 0x0057 }
        if (r3 == 0) goto L_0x00ac;
    L_0x008a:
        r0 = r8;
        r0 = (android.telephony.CellInfoLte) r0;	 Catch:{ Exception -> 0x0057 }
        r2 = r0;
        r3 = r2.getCellIdentity();	 Catch:{ Exception -> 0x0057 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0057 }
        r4 = r2.getCellSignalStrength();	 Catch:{ Exception -> 0x0057 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0057 }
        r5 = r2.isRegistered();	 Catch:{ Exception -> 0x0057 }
        r6 = r2.getTimeStamp();	 Catch:{ Exception -> 0x0057 }
        r2 = r10;
        r2 = r2.m1591a(r3, r4, r5, r6);	 Catch:{ Exception -> 0x0057 }
        goto L_0x004f;
    L_0x00ac:
        r3 = r8 instanceof android.telephony.CellInfoWcdma;	 Catch:{ Exception -> 0x0057 }
        if (r3 == 0) goto L_0x004f;
    L_0x00b0:
        r0 = r8;
        r0 = (android.telephony.CellInfoWcdma) r0;	 Catch:{ Exception -> 0x0057 }
        r2 = r0;
        r3 = r2.getCellIdentity();	 Catch:{ Exception -> 0x0057 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0057 }
        r4 = r2.getCellSignalStrength();	 Catch:{ Exception -> 0x0057 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0057 }
        r5 = r2.isRegistered();	 Catch:{ Exception -> 0x0057 }
        r6 = r2.getTimeStamp();	 Catch:{ Exception -> 0x0057 }
        r2 = r10;
        r2 = r2.m1591a(r3, r4, r5, r6);	 Catch:{ Exception -> 0x0057 }
        goto L_0x004f;
    L_0x00d3:
        r10.ho();
        goto L_0x005e;
    L_0x00d7:
        r2 = move-exception;
        r10.ho();
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.sensorframework.sda.d.a.g.hc():boolean");
    }

    protected HashMap<String, Object> m1591a(String str, String str2, boolean z, long j) {
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("CellIdentity", str);
        hashMap.put("CellSignalStrength", str2);
        hashMap.put("isRegistered", Boolean.valueOf(z));
        hashMap.put("cellTimestamp", Long.valueOf(j));
        return hashMap;
    }

    protected void hd() {
    }
}
