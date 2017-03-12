package com.samsung.sensorframework;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sdi.p046c.SensingController;
import java.util.List;

/* renamed from: com.samsung.sensorframework.b */
public class SFManager {
    private static SFManager HN;
    private static final Object Ho;
    private SFContextInterface HO;
    private Context context;

    /* renamed from: com.samsung.sensorframework.b.1 */
    class SFManager extends Thread {
        final /* synthetic */ SFManager HP;

        SFManager(SFManager sFManager) {
            this.HP = sFManager;
        }

        public void run() {
            Log.m285d("SFManager", "startSFManager()");
            if (this.HP.context == null) {
                Log.m285d("SFManager", "startSFManager() context is null");
            } else if (SensingController.br(this.HP.context) != null) {
                SensingController.br(this.HP.context).hQ();
            } else {
                Log.m286e("SFManager", "SensingController.getInstance() returned null");
            }
        }
    }

    static {
        Ho = new Object();
    }

    public static SFManager aM(Context context) {
        if (HN == null) {
            synchronized (Ho) {
                if (HN == null) {
                    HN = new SFManager(context);
                }
            }
        }
        return HN;
    }

    private SFManager(Context context) {
        Log.m285d("SFManager", "SFManager created");
        this.context = context;
    }

    public void m1504a(SFContextInterface sFContextInterface) {
        this.HO = sFContextInterface;
    }

    public void gM() {
        new SFManager(this).start();
    }

    public void gN() {
        Log.m285d("SFManager", "stopSFManager()");
        if (SensingController.br(this.context) != null) {
            SensingController.br(this.context).hR();
        } else {
            Log.m286e("SFManager", "stopSFManager() SensingController.getInstance() returned null");
        }
    }

    public List<String> m1505b(double d, double d2, int i) {
        Log.m285d("SFManager", "queryPoICache()");
        if (this.HO != null) {
            return this.HO.m1485b(d, d2, i);
        }
        Log.m285d("SFManager", "queryPoICache() sfContextInterface is null");
        return null;
    }

    public void m1506i(List<String> list) {
        Log.m285d("SFManager", "onNearbyPoIs()");
        if (this.HO != null) {
            this.HO.m1486i(list);
        } else {
            Log.m285d("SFManager", "onNearbyPoIs() sfContextInterface is null");
        }
    }

    public void m1507j(List<String> list) {
        Log.m285d("SFManager", "onExitPoIs()");
        if (this.HO != null) {
            this.HO.m1487j(list);
        } else {
            Log.m285d("SFManager", "onExitPoIs() sfContextInterface is null");
        }
    }

    public String gG() {
        Log.m285d("SFManager", "queryContextSensingPolicy()");
        if (this.HO != null) {
            return this.HO.gG();
        }
        Log.m285d("SFManager", "queryContextSensingPolicy() sfContextInterface is null");
        return null;
    }

    public SensorData aa(int i) {
        Log.m285d("SFManager", "getLastSensorData() sensorId: " + i);
        if (SensingController.br(this.context) != null) {
            return SensingController.br(this.context).aa(i);
        }
        return null;
    }
}
