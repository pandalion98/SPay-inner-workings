/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.tls.DTLSRecordLayer;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.bouncycastle.crypto.tls.TlsFatalAlert;

public class DTLSTransport
implements DatagramTransport {
    private final DTLSRecordLayer recordLayer;

    DTLSTransport(DTLSRecordLayer dTLSRecordLayer) {
        this.recordLayer = dTLSRecordLayer;
    }

    @Override
    public void close() {
        this.recordLayer.close();
    }

    @Override
    public int getReceiveLimit() {
        return this.recordLayer.getReceiveLimit();
    }

    @Override
    public int getSendLimit() {
        return this.recordLayer.getSendLimit();
    }

    @Override
    public int receive(byte[] arrby, int n2, int n3, int n4) {
        try {
            int n5 = this.recordLayer.receive(arrby, n2, n3, n4);
            return n5;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            this.recordLayer.fail(tlsFatalAlert.getAlertDescription());
            throw tlsFatalAlert;
        }
        catch (IOException iOException) {
            this.recordLayer.fail((short)80);
            throw iOException;
        }
        catch (RuntimeException runtimeException) {
            this.recordLayer.fail((short)80);
            throw new TlsFatalAlert(80, runtimeException);
        }
    }

    @Override
    public void send(byte[] arrby, int n2, int n3) {
        try {
            this.recordLayer.send(arrby, n2, n3);
            return;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            this.recordLayer.fail(tlsFatalAlert.getAlertDescription());
            throw tlsFatalAlert;
        }
        catch (IOException iOException) {
            this.recordLayer.fail((short)80);
            throw iOException;
        }
        catch (RuntimeException runtimeException) {
            this.recordLayer.fail((short)80);
            throw new TlsFatalAlert(80, runtimeException);
        }
    }
}

