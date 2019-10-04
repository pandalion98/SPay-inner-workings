/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.asn1.x500.style;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class BCStrictStyle
extends BCStyle {
    public static final X500NameStyle INSTANCE = new BCStrictStyle();

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean areEqual(X500Name x500Name, X500Name x500Name2) {
        RDN[] arrrDN;
        RDN[] arrrDN2 = x500Name.getRDNs();
        if (arrrDN2.length == (arrrDN = x500Name2.getRDNs()).length) {
            int n2 = 0;
            do {
                if (n2 == arrrDN2.length) {
                    return true;
                }
                if (!this.rdnAreEqual(arrrDN2[n2], arrrDN[n2])) break;
                ++n2;
            } while (true);
        }
        return false;
    }
}

