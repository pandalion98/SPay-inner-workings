/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.TlsUtils;

public class NewSessionTicket {
    protected byte[] ticket;
    protected long ticketLifetimeHint;

    public NewSessionTicket(long l2, byte[] arrby) {
        this.ticketLifetimeHint = l2;
        this.ticket = arrby;
    }

    public static NewSessionTicket parse(InputStream inputStream) {
        return new NewSessionTicket(TlsUtils.readUint32(inputStream), TlsUtils.readOpaque16(inputStream));
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint32(this.ticketLifetimeHint, outputStream);
        TlsUtils.writeOpaque16(this.ticket, outputStream);
    }

    public byte[] getTicket() {
        return this.ticket;
    }

    public long getTicketLifetimeHint() {
        return this.ticketLifetimeHint;
    }
}

