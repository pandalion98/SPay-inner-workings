package org.bouncycastle.crypto.tls;

import com.samsung.android.spayfw.appinterface.PaymentFramework;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class AlertLevel {
    public static final short fatal = (short) 2;
    public static final short warning = (short) 1;

    public static String getName(short s) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "warning";
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return "fatal";
            default:
                return PaymentFramework.CARD_TYPE_UNKNOWN;
        }
    }

    public static String getText(short s) {
        return getName(s) + "(" + s + ")";
    }
}
