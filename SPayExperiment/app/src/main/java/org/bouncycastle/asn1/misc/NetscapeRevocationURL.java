/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.asn1.misc;

import org.bouncycastle.asn1.DERIA5String;

public class NetscapeRevocationURL
extends DERIA5String {
    public NetscapeRevocationURL(DERIA5String dERIA5String) {
        super(dERIA5String.getString());
    }

    @Override
    public String toString() {
        return "NetscapeRevocationURL: " + this.getString();
    }
}

