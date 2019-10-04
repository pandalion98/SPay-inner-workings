/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util.encoders;

import java.io.OutputStream;

public interface Encoder {
    public int decode(String var1, OutputStream var2);

    public int decode(byte[] var1, int var2, int var3, OutputStream var4);

    public int encode(byte[] var1, int var2, int var3, OutputStream var4);
}

