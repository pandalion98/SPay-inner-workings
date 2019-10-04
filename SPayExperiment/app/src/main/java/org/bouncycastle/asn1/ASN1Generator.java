/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Object
 */
package org.bouncycastle.asn1;

import java.io.OutputStream;

public abstract class ASN1Generator {
    protected OutputStream _out;

    public ASN1Generator(OutputStream outputStream) {
        this._out = outputStream;
    }

    public abstract OutputStream getRawOutputStream();
}

