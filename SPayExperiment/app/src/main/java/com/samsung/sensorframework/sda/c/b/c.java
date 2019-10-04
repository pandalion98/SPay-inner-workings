/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.NetworkInfo
 *  android.net.wifi.WifiInfo
 *  java.lang.Object
 *  java.lang.String
 *  java.net.InetAddress
 *  java.net.NetworkInterface
 *  java.util.Enumeration
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import com.samsung.sensorframework.sda.c.a;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class c
extends a {
    public c(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    private String a(Enumeration<NetworkInterface> enumeration) {
        while (enumeration.hasMoreElements()) {
            Enumeration enumeration2 = ((NetworkInterface)enumeration.nextElement()).getInetAddresses();
            while (enumeration2.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress)enumeration2.nextElement();
                if (inetAddress.isLoopbackAddress()) continue;
                return inetAddress.getHostAddress().toString();
            }
        }
        return "None";
    }

    public com.samsung.sensorframework.sda.b.b.c a(long l2, com.samsung.sensorframework.sda.a.c c2, NetworkInfo networkInfo, WifiInfo wifiInfo, Enumeration<NetworkInterface> enumeration) {
        com.samsung.sensorframework.sda.b.b.c c3 = new com.samsung.sensorframework.sda.b.b.c(l2, c2);
        if (this.Je) {
            c3.a(networkInfo);
            c3.a(wifiInfo);
            c3.setIpAddress(this.a(enumeration));
        }
        return c3;
    }
}

