/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public class HeartbeatMessageType {
    public static final short heartbeat_request = 1;
    public static final short heartbeat_response = 2;

    public static boolean isValid(short s2) {
        return s2 >= 1 && s2 <= 2;
    }
}

