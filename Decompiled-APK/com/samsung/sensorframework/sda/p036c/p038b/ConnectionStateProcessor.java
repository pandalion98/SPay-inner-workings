package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.ConnectionStateData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/* renamed from: com.samsung.sensorframework.sda.c.b.c */
public class ConnectionStateProcessor extends AbstractProcessor {
    public ConnectionStateProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public ConnectionStateData m1564a(long j, SensorConfig sensorConfig, NetworkInfo networkInfo, WifiInfo wifiInfo, Enumeration<NetworkInterface> enumeration) {
        ConnectionStateData connectionStateData = new ConnectionStateData(j, sensorConfig);
        if (this.Je) {
            connectionStateData.m1525a(networkInfo);
            connectionStateData.m1526a(wifiInfo);
            connectionStateData.setIpAddress(m1563a(enumeration));
        }
        return connectionStateData;
    }

    private String m1563a(Enumeration<NetworkInterface> enumeration) {
        while (enumeration.hasMoreElements()) {
            Enumeration inetAddresses = ((NetworkInterface) enumeration.nextElement()).getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    return inetAddress.getHostAddress().toString();
                }
            }
        }
        return PaymentNetworkProvider.AUTHTYPE_NONE;
    }
}
