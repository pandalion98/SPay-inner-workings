/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Strings;

public final class ProtocolVersion {
    public static final ProtocolVersion DTLSv10;
    public static final ProtocolVersion DTLSv12;
    public static final ProtocolVersion SSLv3;
    public static final ProtocolVersion TLSv10;
    public static final ProtocolVersion TLSv11;
    public static final ProtocolVersion TLSv12;
    private String name;
    private int version;

    static {
        SSLv3 = new ProtocolVersion(768, "SSL 3.0");
        TLSv10 = new ProtocolVersion(769, "TLS 1.0");
        TLSv11 = new ProtocolVersion(770, "TLS 1.1");
        TLSv12 = new ProtocolVersion(771, "TLS 1.2");
        DTLSv10 = new ProtocolVersion(65279, "DTLS 1.0");
        DTLSv12 = new ProtocolVersion(65277, "DTLS 1.2");
    }

    private ProtocolVersion(int n2, String string) {
        this.version = 65535 & n2;
        this.name = string;
    }

    public static ProtocolVersion get(int n2, int n3) {
        switch (n2) {
            default: {
                throw new TlsFatalAlert(47);
            }
            case 3: {
                switch (n3) {
                    default: {
                        return ProtocolVersion.getUnknownVersion(n2, n3, "TLS");
                    }
                    case 0: {
                        return SSLv3;
                    }
                    case 1: {
                        return TLSv10;
                    }
                    case 2: {
                        return TLSv11;
                    }
                    case 3: 
                }
                return TLSv12;
            }
            case 254: 
        }
        switch (n3) {
            default: {
                return ProtocolVersion.getUnknownVersion(n2, n3, "DTLS");
            }
            case 255: {
                return DTLSv10;
            }
            case 254: {
                throw new TlsFatalAlert(47);
            }
            case 253: 
        }
        return DTLSv12;
    }

    private static ProtocolVersion getUnknownVersion(int n2, int n3, String string) {
        TlsUtils.checkUint8(n2);
        TlsUtils.checkUint8(n3);
        int n4 = n3 | n2 << 8;
        String string2 = Strings.toUpperCase((String)Integer.toHexString((int)(65536 | n4)).substring(1));
        return new ProtocolVersion(n4, string + " 0x" + string2);
    }

    public boolean equals(Object object) {
        return this == object || object instanceof ProtocolVersion && this.equals((ProtocolVersion)object);
    }

    public boolean equals(ProtocolVersion protocolVersion) {
        return protocolVersion != null && this.version == protocolVersion.version;
    }

    public ProtocolVersion getEquivalentTLSVersion() {
        if (!this.isDTLS()) {
            return this;
        }
        if (this == DTLSv10) {
            return TLSv11;
        }
        return TLSv12;
    }

    public int getFullVersion() {
        return this.version;
    }

    public int getMajorVersion() {
        return this.version >> 8;
    }

    public int getMinorVersion() {
        return 255 & this.version;
    }

    public int hashCode() {
        return this.version;
    }

    public boolean isDTLS() {
        return this.getMajorVersion() == 254;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isEqualOrEarlierVersionOf(ProtocolVersion protocolVersion) {
        boolean bl = true;
        if (this.getMajorVersion() != protocolVersion.getMajorVersion()) {
            return false;
        }
        int n2 = protocolVersion.getMinorVersion() - this.getMinorVersion();
        if (this.isDTLS()) {
            if (n2 > 0) return false;
            return bl;
        }
        if (n2 >= 0) return bl;
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isLaterVersionOf(ProtocolVersion protocolVersion) {
        boolean bl = true;
        if (this.getMajorVersion() != protocolVersion.getMajorVersion()) {
            return false;
        }
        int n2 = protocolVersion.getMinorVersion() - this.getMinorVersion();
        if (this.isDTLS()) {
            if (n2 <= 0) return false;
            return bl;
        }
        if (n2 < 0) return bl;
        return false;
    }

    public boolean isSSL() {
        return this == SSLv3;
    }

    public boolean isTLS() {
        return this.getMajorVersion() == 3;
    }

    public String toString() {
        return this.name;
    }
}

