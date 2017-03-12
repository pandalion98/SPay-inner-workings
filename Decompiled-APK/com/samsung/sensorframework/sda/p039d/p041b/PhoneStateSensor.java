package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p036c.p038b.PhoneStateProcessor;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.sensorframework.sda.d.b.h */
public class PhoneStateSensor extends AbstractPushSensor {
    private static final String[] Jz;
    private static PhoneStateSensor KB;
    private static final Object lock;
    private PhoneStateListener KA;
    private TelephonyManager Kz;

    /* renamed from: com.samsung.sensorframework.sda.d.b.h.1 */
    class PhoneStateSensor extends PhoneStateListener {
        final /* synthetic */ PhoneStateSensor KD;

        PhoneStateSensor(PhoneStateSensor phoneStateSensor) {
            this.KD = phoneStateSensor;
        }

        public void onCallStateChanged(int i, String str) {
            String str2 = "N/A";
            int i2 = 0;
            switch (i) {
                case ECCurve.COORD_AFFINE /*0*/:
                    i2 = 54401;
                    str2 = "CALL_STATE_IDLE";
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i2 = 54403;
                    str2 = "CALL_STATE_RINGING";
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    i2 = 54402;
                    str2 = "CALL_STATE_OFFHOOK";
                    break;
            }
            this.KD.m1626c(i2, str2, str);
        }

        public void onCellLocationChanged(CellLocation cellLocation) {
            if (cellLocation != null) {
                this.KD.m1626c(5441, cellLocation.toString(), null);
            }
        }

        public void onDataActivity(int i) {
            this.KD.m1626c(5442, PhoneStateSensor.as(i), null);
        }

        public void onDataConnectionStateChanged(int i) {
            this.KD.m1626c(5443, PhoneStateSensor.au(i), null);
        }

        public void onDataConnectionStateChanged(int i, int i2) {
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            if (serviceState != null) {
                this.KD.m1626c(5444, PhoneStateSensor.ar(serviceState.getState()) + " " + serviceState.toString(), null);
            }
        }
    }

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.PROCESS_OUTGOING_CALLS", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION"};
    }

    public static PhoneStateSensor bh(Context context) {
        if (KB == null) {
            synchronized (lock) {
                if (KB == null) {
                    KB = new PhoneStateSensor(context);
                }
            }
        }
        return KB;
    }

    private PhoneStateSensor(Context context) {
        super(context);
        this.Kz = (TelephonyManager) context.getSystemService("phone");
        this.KA = new PhoneStateSensor(this);
    }

    public void gY() {
        super.gY();
        KB = null;
    }

    private void m1626c(int i, String str, String str2) {
        if (this.Ji) {
            PhoneStateProcessor phoneStateProcessor = (PhoneStateProcessor) hi();
            if (phoneStateProcessor != null) {
                m1613a(phoneStateProcessor.m1567a(System.currentTimeMillis(), this.Id.gS(), i, str, str2));
            }
        }
    }

    protected void m1627a(Context context, Intent intent) {
        m1626c(54404, "CALL_STATE_OUTGOING ", intent.getStringExtra("android.intent.extra.PHONE_NUMBER"));
    }

    protected IntentFilter[] hC() {
        return new IntentFilter[]{new IntentFilter("android.intent.action.NEW_OUTGOING_CALL")};
    }

    public static String ar(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return "STATE_IN_SERVICE";
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "STATE_OUT_OF_SERVICE";
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return "STATE_EMERGENCY_ONLY";
            case F2m.PPB /*3*/:
                return "STATE_POWER_OFF";
            default:
                return BuildConfig.FLAVOR;
        }
    }

    public static String as(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return "DATA_ACTIVITY_NONE";
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "DATA_ACTIVITY_IN";
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return "DATA_ACTIVITY_OUT";
            case F2m.PPB /*3*/:
                return "DATA_ACTIVITY_INOUT";
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return "DATA_ACTIVITY_DORMANT";
            default:
                return BuildConfig.FLAVOR;
        }
    }

    public static String au(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return "DATA_DISCONNECTED";
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "DATA_CONNECTING";
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return "DATA_CONNECTED";
            case F2m.PPB /*3*/:
                return "DATA_SUSPENDED";
            default:
                return BuildConfig.FLAVOR;
        }
    }

    protected String[] hb() {
        return Jz;
    }

    protected String he() {
        return "PhoneStateSensor";
    }

    public int getSensorType() {
        return 5006;
    }

    protected boolean hc() {
        this.Kz.listen(this.KA, 241);
        return true;
    }

    protected void hd() {
        this.Kz.listen(this.KA, 0);
    }
}
