package com.samsung.sensorframework.sda.p033b.p035b;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.sensorframework.sda.b.b.c */
public class ConnectionStateData extends SensorData {
    private boolean IB;
    private boolean IC;
    private boolean IE;
    private int IF;
    private int IG;
    private String IH;
    private String II;
    private String ssid;

    public ConnectionStateData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void m1525a(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            this.IB = false;
            this.IC = false;
            this.IE = false;
            this.IG = 2;
            this.IF = 0;
            return;
        }
        this.IE = networkInfo.isAvailable();
        this.IB = networkInfo.isConnectedOrConnecting();
        this.IC = networkInfo.isConnected();
        if (networkInfo.isRoaming()) {
            this.IG = 0;
        } else {
            this.IG = 1;
        }
        switch (networkInfo.getType()) {
            case ECCurve.COORD_AFFINE /*0*/:
                this.IF = 1;
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                this.IF = 2;
            default:
                this.IF = 3;
        }
    }

    public void setIpAddress(String str) {
        this.IH = str;
    }

    public void m1526a(WifiInfo wifiInfo) {
        if (wifiInfo != null) {
            this.ssid = wifiInfo.getSSID();
            this.II = wifiInfo.getMacAddress();
            return;
        }
        this.ssid = null;
    }

    public boolean isConnected() {
        return this.IC;
    }

    public int getSensorType() {
        return 5011;
    }

    public String gW() {
        switch (this.IF) {
            case ECCurve.COORD_AFFINE /*0*/:
                return "NO_CONNECTION";
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "MOBILE_CONNECTION";
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return "WIFI_CONNECTION";
            case F2m.PPB /*3*/:
                return "OTHER_CONNECTION";
            default:
                return PaymentFramework.CARD_TYPE_UNKNOWN;
        }
    }

    public String toString() {
        return "ConnectionStateData{ isConnected: " + isConnected() + " Network type " + gW() + " }";
    }
}
