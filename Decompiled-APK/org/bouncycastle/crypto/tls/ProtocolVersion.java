package org.bouncycastle.crypto.tls;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.util.Strings;
import org.bouncycastle.x509.ExtendedPKIXParameters;

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
        SSLv3 = new ProtocolVersion(McTACommands.MOP_MC_TA_CMD_CASD_BASE, "SSL 3.0");
        TLSv10 = new ProtocolVersion(McTACommands.MOP_MC_TA_CMD_CASD_GET_UID, "TLS 1.0");
        TLSv11 = new ProtocolVersion(McTACommands.MOP_MC_TA_CMD_CASD_WRITE_KEY, "TLS 1.1");
        TLSv12 = new ProtocolVersion(McTACommands.MOP_MC_TA_CMD_CASD_VERIFY_KEY, "TLS 1.2");
        DTLSv10 = new ProtocolVersion(65279, "DTLS 1.0");
        DTLSv12 = new ProtocolVersion(65277, "DTLS 1.2");
    }

    private ProtocolVersion(int i, String str) {
        this.version = HCEClientConstants.HIGHEST_ATC_DEC_VALUE & i;
        this.name = str;
    }

    public static ProtocolVersion get(int i, int i2) {
        switch (i) {
            case F2m.PPB /*3*/:
                switch (i2) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        return SSLv3;
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        return TLSv10;
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        return TLSv11;
                    case F2m.PPB /*3*/:
                        return TLSv12;
                    default:
                        return getUnknownVersion(i, i2, "TLS");
                }
            case 254:
                switch (i2) {
                    case 253:
                        return DTLSv12;
                    case 254:
                        throw new TlsFatalAlert((short) 47);
                    case GF2Field.MASK /*255*/:
                        return DTLSv10;
                    default:
                        return getUnknownVersion(i, i2, "DTLS");
                }
            default:
                throw new TlsFatalAlert((short) 47);
        }
    }

    private static ProtocolVersion getUnknownVersion(int i, int i2, String str) {
        TlsUtils.checkUint8(i);
        TlsUtils.checkUint8(i2);
        int i3 = (i << 8) | i2;
        return new ProtocolVersion(i3, str + " 0x" + Strings.toUpperCase(Integer.toHexString(PKIFailureInfo.notAuthorized | i3).substring(1)));
    }

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof ProtocolVersion) && equals((ProtocolVersion) obj));
    }

    public boolean equals(ProtocolVersion protocolVersion) {
        return protocolVersion != null && this.version == protocolVersion.version;
    }

    public ProtocolVersion getEquivalentTLSVersion() {
        return !isDTLS() ? this : this == DTLSv10 ? TLSv11 : TLSv12;
    }

    public int getFullVersion() {
        return this.version;
    }

    public int getMajorVersion() {
        return this.version >> 8;
    }

    public int getMinorVersion() {
        return this.version & GF2Field.MASK;
    }

    public int hashCode() {
        return this.version;
    }

    public boolean isDTLS() {
        return getMajorVersion() == 254;
    }

    public boolean isEqualOrEarlierVersionOf(ProtocolVersion protocolVersion) {
        boolean z = true;
        if (getMajorVersion() != protocolVersion.getMajorVersion()) {
            return false;
        }
        int minorVersion = protocolVersion.getMinorVersion() - getMinorVersion();
        if (isDTLS()) {
            if (minorVersion > 0) {
                z = false;
            }
        } else if (minorVersion < 0) {
            z = false;
        }
        return z;
    }

    public boolean isLaterVersionOf(ProtocolVersion protocolVersion) {
        boolean z = true;
        if (getMajorVersion() != protocolVersion.getMajorVersion()) {
            return false;
        }
        int minorVersion = protocolVersion.getMinorVersion() - getMinorVersion();
        if (isDTLS()) {
            if (minorVersion <= 0) {
                z = false;
            }
        } else if (minorVersion >= 0) {
            z = false;
        }
        return z;
    }

    public boolean isSSL() {
        return this == SSLv3;
    }

    public boolean isTLS() {
        return getMajorVersion() == 3;
    }

    public String toString() {
        return this.name;
    }
}
