package com.squareup.okhttp.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface Network {
    public static final Network DEFAULT;

    /* renamed from: com.squareup.okhttp.internal.Network.1 */
    static class C06441 implements Network {
        C06441() {
        }

        public InetAddress[] resolveInetAddresses(String str) {
            if (str != null) {
                return InetAddress.getAllByName(str);
            }
            throw new UnknownHostException("host == null");
        }
    }

    InetAddress[] resolveInetAddresses(String str);

    static {
        DEFAULT = new C06441();
    }
}
