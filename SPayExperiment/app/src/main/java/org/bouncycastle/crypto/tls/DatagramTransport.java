/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public interface DatagramTransport {
    public void close();

    public int getReceiveLimit();

    public int getSendLimit();

    public int receive(byte[] var1, int var2, int var3, int var4);

    public void send(byte[] var1, int var2, int var3);
}

