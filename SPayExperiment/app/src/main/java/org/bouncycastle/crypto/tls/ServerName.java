/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Strings;

public class ServerName {
    protected Object name;
    protected short nameType;

    public ServerName(short s2, Object object) {
        if (!ServerName.isCorrectType(s2, object)) {
            throw new IllegalArgumentException("'name' is not an instance of the correct type");
        }
        this.nameType = s2;
        this.name = object;
    }

    protected static boolean isCorrectType(short s2, Object object) {
        switch (s2) {
            default: {
                throw new IllegalArgumentException("'name' is an unsupported value");
            }
            case 0: 
        }
        return object instanceof String;
    }

    public static ServerName parse(InputStream inputStream) {
        short s2 = TlsUtils.readUint8(inputStream);
        switch (s2) {
            default: {
                throw new TlsFatalAlert(50);
            }
            case 0: 
        }
        byte[] arrby = TlsUtils.readOpaque16(inputStream);
        if (arrby.length < 1) {
            throw new TlsFatalAlert(50);
        }
        return new ServerName(s2, Strings.fromUTF8ByteArray((byte[])arrby));
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.nameType, outputStream);
        switch (this.nameType) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 0: 
        }
        byte[] arrby = Strings.toUTF8ByteArray((String)((String)this.name));
        if (arrby.length < 1) {
            throw new TlsFatalAlert(80);
        }
        TlsUtils.writeOpaque16(arrby, outputStream);
    }

    public String getHostName() {
        if (!ServerName.isCorrectType((short)0, this.name)) {
            throw new IllegalStateException("'name' is not a HostName string");
        }
        return (String)this.name;
    }

    public Object getName() {
        return this.name;
    }

    public short getNameType() {
        return this.nameType;
    }
}

