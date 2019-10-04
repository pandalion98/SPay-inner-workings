/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.util.encoders;

import org.bouncycastle.util.encoders.Base64Encoder;

public class UrlBase64Encoder
extends Base64Encoder {
    public UrlBase64Encoder() {
        this.encodingTable[-2 + this.encodingTable.length] = 45;
        this.encodingTable[-1 + this.encodingTable.length] = 95;
        this.padding = (byte)46;
        this.initialiseDecodingTable();
    }
}

