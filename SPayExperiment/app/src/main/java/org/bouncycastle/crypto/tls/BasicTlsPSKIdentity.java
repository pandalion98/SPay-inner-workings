/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsPSKIdentity;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class BasicTlsPSKIdentity
implements TlsPSKIdentity {
    protected byte[] identity;
    protected byte[] psk;

    public BasicTlsPSKIdentity(String string, byte[] arrby) {
        this.identity = Strings.toUTF8ByteArray((String)string);
        this.psk = Arrays.clone((byte[])arrby);
    }

    public BasicTlsPSKIdentity(byte[] arrby, byte[] arrby2) {
        this.identity = Arrays.clone((byte[])arrby);
        this.psk = Arrays.clone((byte[])arrby2);
    }

    @Override
    public byte[] getPSK() {
        return this.psk;
    }

    @Override
    public byte[] getPSKIdentity() {
        return this.identity;
    }

    @Override
    public void notifyIdentityHint(byte[] arrby) {
    }

    @Override
    public void skipIdentityHint() {
    }
}

