/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.DatagramPacket
 *  java.net.DatagramSocket
 */
package org.bouncycastle.crypto.tls;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.bouncycastle.crypto.tls.TlsFatalAlert;

public class UDPTransport
implements DatagramTransport {
    protected static final int MAX_IP_OVERHEAD = 84;
    protected static final int MIN_IP_OVERHEAD = 20;
    protected static final int UDP_OVERHEAD = 8;
    protected final int receiveLimit;
    protected final int sendLimit;
    protected final DatagramSocket socket;

    public UDPTransport(DatagramSocket datagramSocket, int n2) {
        if (!datagramSocket.isBound() || !datagramSocket.isConnected()) {
            throw new IllegalArgumentException("'socket' must be bound and connected");
        }
        this.socket = datagramSocket;
        this.receiveLimit = -8 + (n2 - 20);
        this.sendLimit = -8 + (n2 - 84);
    }

    @Override
    public void close() {
        this.socket.close();
    }

    @Override
    public int getReceiveLimit() {
        return this.receiveLimit;
    }

    @Override
    public int getSendLimit() {
        return this.sendLimit;
    }

    @Override
    public int receive(byte[] arrby, int n2, int n3, int n4) {
        this.socket.setSoTimeout(n4);
        DatagramPacket datagramPacket = new DatagramPacket(arrby, n2, n3);
        this.socket.receive(datagramPacket);
        return datagramPacket.getLength();
    }

    @Override
    public void send(byte[] arrby, int n2, int n3) {
        if (n3 > this.getSendLimit()) {
            throw new TlsFatalAlert(80);
        }
        DatagramPacket datagramPacket = new DatagramPacket(arrby, n2, n3);
        this.socket.send(datagramPacket);
    }
}

