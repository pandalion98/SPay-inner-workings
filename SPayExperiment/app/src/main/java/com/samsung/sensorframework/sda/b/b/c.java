/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.net.NetworkInfo
 *  android.net.wifi.WifiInfo
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.b;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import com.samsung.sensorframework.sda.b.a;

public class c
extends a {
    private boolean IB;
    private boolean IC;
    private boolean IE;
    private int IF;
    private int IG;
    private String IH;
    private String II;
    private String ssid;

    public c(long l2, com.samsung.sensorframework.sda.a.c c2) {
        super(l2, c2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void a(NetworkInfo networkInfo) {
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
        this.IG = networkInfo.isRoaming() ? 0 : 1;
        switch (networkInfo.getType()) {
            default: {
                this.IF = 3;
                return;
            }
            case 0: {
                this.IF = 1;
                return;
            }
            case 1: 
        }
        this.IF = 2;
    }

    public void a(WifiInfo wifiInfo) {
        if (wifiInfo != null) {
            this.ssid = wifiInfo.getSSID();
            this.II = wifiInfo.getMacAddress();
            return;
        }
        this.ssid = null;
    }

    public String gW() {
        switch (this.IF) {
            default: {
                return "UNKNOWN";
            }
            case 0: {
                return "NO_CONNECTION";
            }
            case 1: {
                return "MOBILE_CONNECTION";
            }
            case 2: {
                return "WIFI_CONNECTION";
            }
            case 3: 
        }
        return "OTHER_CONNECTION";
    }

    @Override
    public int getSensorType() {
        return 5011;
    }

    public boolean isConnected() {
        return this.IC;
    }

    public void setIpAddress(String string) {
        this.IH = string;
    }

    public String toString() {
        return "ConnectionStateData{ isConnected: " + this.isConnected() + " Network type " + this.gW() + " }";
    }
}

