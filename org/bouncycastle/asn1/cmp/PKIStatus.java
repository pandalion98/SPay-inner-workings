package org.bouncycastle.asn1.cmp;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;

public class PKIStatus extends ASN1Object {
    public static final int GRANTED = 0;
    public static final int GRANTED_WITH_MODS = 1;
    public static final int KEY_UPDATE_WARNING = 6;
    public static final int REJECTION = 2;
    public static final int REVOCATION_NOTIFICATION = 5;
    public static final int REVOCATION_WARNING = 4;
    public static final int WAITING = 3;
    public static final PKIStatus granted;
    public static final PKIStatus grantedWithMods;
    public static final PKIStatus keyUpdateWaiting;
    public static final PKIStatus rejection;
    public static final PKIStatus revocationNotification;
    public static final PKIStatus revocationWarning;
    public static final PKIStatus waiting;
    private ASN1Integer value;

    static {
        granted = new PKIStatus((int) GRANTED);
        grantedWithMods = new PKIStatus((int) GRANTED_WITH_MODS);
        rejection = new PKIStatus((int) REJECTION);
        waiting = new PKIStatus((int) WAITING);
        revocationWarning = new PKIStatus((int) REVOCATION_WARNING);
        revocationNotification = new PKIStatus((int) REVOCATION_NOTIFICATION);
        keyUpdateWaiting = new PKIStatus((int) KEY_UPDATE_WARNING);
    }

    private PKIStatus(int i) {
        this(new ASN1Integer((long) i));
    }

    private PKIStatus(ASN1Integer aSN1Integer) {
        this.value = aSN1Integer;
    }

    public static PKIStatus getInstance(Object obj) {
        return obj instanceof PKIStatus ? (PKIStatus) obj : obj != null ? new PKIStatus(ASN1Integer.getInstance(obj)) : null;
    }

    public BigInteger getValue() {
        return this.value.getValue();
    }

    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }
}
