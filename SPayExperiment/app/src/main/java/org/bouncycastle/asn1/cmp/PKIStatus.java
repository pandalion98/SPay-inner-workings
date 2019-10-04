/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.cmp;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;

public class PKIStatus
extends ASN1Object {
    public static final int GRANTED = 0;
    public static final int GRANTED_WITH_MODS = 1;
    public static final int KEY_UPDATE_WARNING = 6;
    public static final int REJECTION = 2;
    public static final int REVOCATION_NOTIFICATION = 5;
    public static final int REVOCATION_WARNING = 4;
    public static final int WAITING = 3;
    public static final PKIStatus granted = new PKIStatus(0);
    public static final PKIStatus grantedWithMods = new PKIStatus(1);
    public static final PKIStatus keyUpdateWaiting;
    public static final PKIStatus rejection;
    public static final PKIStatus revocationNotification;
    public static final PKIStatus revocationWarning;
    public static final PKIStatus waiting;
    private ASN1Integer value;

    static {
        rejection = new PKIStatus(2);
        waiting = new PKIStatus(3);
        revocationWarning = new PKIStatus(4);
        revocationNotification = new PKIStatus(5);
        keyUpdateWaiting = new PKIStatus(6);
    }

    private PKIStatus(int n2) {
        this(new ASN1Integer(n2));
    }

    private PKIStatus(ASN1Integer aSN1Integer) {
        this.value = aSN1Integer;
    }

    public static PKIStatus getInstance(Object object) {
        if (object instanceof PKIStatus) {
            return (PKIStatus)object;
        }
        if (object != null) {
            return new PKIStatus(ASN1Integer.getInstance(object));
        }
        return null;
    }

    public BigInteger getValue() {
        return this.value.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }
}

