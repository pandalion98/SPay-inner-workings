package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Strings;

public class ServerName {
    protected Object name;
    protected short nameType;

    public ServerName(short s, Object obj) {
        if (isCorrectType(s, obj)) {
            this.nameType = s;
            this.name = obj;
            return;
        }
        throw new IllegalArgumentException("'name' is not an instance of the correct type");
    }

    protected static boolean isCorrectType(short s, Object obj) {
        switch (s) {
            case ECCurve.COORD_AFFINE /*0*/:
                return obj instanceof String;
            default:
                throw new IllegalArgumentException("'name' is an unsupported value");
        }
    }

    public static ServerName parse(InputStream inputStream) {
        short readUint8 = TlsUtils.readUint8(inputStream);
        switch (readUint8) {
            case ECCurve.COORD_AFFINE /*0*/:
                byte[] readOpaque16 = TlsUtils.readOpaque16(inputStream);
                if (readOpaque16.length >= 1) {
                    return new ServerName(readUint8, Strings.fromUTF8ByteArray(readOpaque16));
                }
                throw new TlsFatalAlert((short) 50);
            default:
                throw new TlsFatalAlert((short) 50);
        }
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.nameType, outputStream);
        switch (this.nameType) {
            case ECCurve.COORD_AFFINE /*0*/:
                byte[] toUTF8ByteArray = Strings.toUTF8ByteArray((String) this.name);
                if (toUTF8ByteArray.length < 1) {
                    throw new TlsFatalAlert((short) 80);
                }
                TlsUtils.writeOpaque16(toUTF8ByteArray, outputStream);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public String getHostName() {
        if (isCorrectType((short) 0, this.name)) {
            return (String) this.name;
        }
        throw new IllegalStateException("'name' is not a HostName string");
    }

    public Object getName() {
        return this.name;
    }

    public short getNameType() {
        return this.nameType;
    }
}
