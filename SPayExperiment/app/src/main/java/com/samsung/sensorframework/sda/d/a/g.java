/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.telephony.CellIdentityCdma
 *  android.telephony.CellIdentityGsm
 *  android.telephony.CellIdentityLte
 *  android.telephony.CellIdentityWcdma
 *  android.telephony.CellInfo
 *  android.telephony.CellInfoCdma
 *  android.telephony.CellInfoGsm
 *  android.telephony.CellInfoLte
 *  android.telephony.CellInfoWcdma
 *  android.telephony.CellSignalStrengthCdma
 *  android.telephony.CellSignalStrengthGsm
 *  android.telephony.CellSignalStrengthLte
 *  android.telephony.CellSignalStrengthWcdma
 *  android.telephony.TelephonyManager
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.i;
import com.samsung.sensorframework.sda.c.a.f;
import com.samsung.sensorframework.sda.d.a.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class g
extends b {
    private static g JE;
    private static final String[] Jz;
    private static final Object lock;
    private ArrayList<HashMap<String, Object>> Ik;
    private i JF;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_COARSE_LOCATION"};
    }

    private g(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static g aS(Context context) {
        if (JE == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (JE == null) {
                    JE = new g(context);
                }
            }
        }
        return JE;
    }

    protected HashMap<String, Object> a(String string, String string2, boolean bl, long l2) {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"CellIdentity", (Object)string);
        hashMap.put((Object)"CellSignalStrength", (Object)string2);
        hashMap.put((Object)"isRegistered", (Object)bl);
        hashMap.put((Object)"cellTimestamp", (Object)l2);
        return hashMap;
    }

    @Override
    public void gY() {
        super.gY();
        JE = null;
    }

    @Override
    public int getSensorType() {
        return 5037;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected boolean hc() {
        try {
            try {
                this.Ik = new ArrayList();
                List list = ((TelephonyManager)this.HR.getSystemService("phone")).getAllCellInfo();
                if (list == null) return true;
                {
                    for (CellInfo cellInfo : list) {
                        HashMap<String, Object> hashMap;
                        if (cellInfo instanceof CellInfoGsm) {
                            CellInfoGsm cellInfoGsm = (CellInfoGsm)cellInfo;
                            hashMap = this.a(cellInfoGsm.getCellIdentity().toString(), cellInfoGsm.getCellSignalStrength().toString(), cellInfoGsm.isRegistered(), cellInfoGsm.getTimeStamp());
                        } else if (cellInfo instanceof CellInfoCdma) {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma)cellInfo;
                            hashMap = this.a(cellInfoCdma.getCellIdentity().toString(), cellInfoCdma.getCellSignalStrength().toString(), cellInfoCdma.isRegistered(), cellInfoCdma.getTimeStamp());
                        } else if (cellInfo instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte)cellInfo;
                            hashMap = this.a(cellInfoLte.getCellIdentity().toString(), cellInfoLte.getCellSignalStrength().toString(), cellInfoLte.isRegistered(), cellInfoLte.getTimeStamp());
                        } else {
                            boolean bl = cellInfo instanceof CellInfoWcdma;
                            hashMap = null;
                            if (bl) {
                                HashMap<String, Object> hashMap2;
                                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma)cellInfo;
                                hashMap = hashMap2 = this.a(cellInfoWcdma.getCellIdentity().toString(), cellInfoWcdma.getCellSignalStrength().toString(), cellInfoWcdma.isRegistered(), cellInfoWcdma.getTimeStamp());
                            }
                        }
                        if (cellInfo == null) continue;
                        this.Ik.add(hashMap);
                    }
                    return true;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                this.ho();
                return true;
            }
        }
        finally {
            this.ho();
        }
    }

    @Override
    protected void hd() {
    }

    @Override
    protected String he() {
        return "CellSensor";
    }

    @Override
    protected /* synthetic */ a hl() {
        return this.ht();
    }

    @Override
    protected void hm() {
        this.JF = ((f)this.hi()).a(this.Jn, this.Ik, this.Id.gS());
    }

    protected i ht() {
        return this.JF;
    }
}

