/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.BufferedWriter
 *  java.io.Writer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Iterator
 *  java.util.List
 */
package org.bouncycastle.util.io.pem;

import java.io.BufferedWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemHeader;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;

public class PemWriter
extends BufferedWriter {
    private static final int LINE_LENGTH = 64;
    private char[] buf = new char[64];
    private final int nlLength;

    public PemWriter(Writer writer) {
        super(writer);
        String string = System.getProperty((String)"line.separator");
        if (string != null) {
            this.nlLength = string.length();
            return;
        }
        this.nlLength = 2;
    }

    private void writeEncoded(byte[] arrby) {
        byte[] arrby2 = Base64.encode(arrby);
        block0 : for (int i = 0; i < arrby2.length; i += this.buf.length) {
            int n = 0;
            do {
                if (n == this.buf.length || i + n >= arrby2.length) {
                    this.write(this.buf, 0, n);
                    this.newLine();
                    continue block0;
                }
                this.buf[n] = (char)arrby2[i + n];
                ++n;
            } while (true);
        }
    }

    private void writePostEncapsulationBoundary(String string) {
        this.write("-----END " + string + "-----");
        this.newLine();
    }

    private void writePreEncapsulationBoundary(String string) {
        this.write("-----BEGIN " + string + "-----");
        this.newLine();
    }

    public int getOutputSize(PemObject pemObject) {
        int n = 4 + (6 + 2 * (10 + pemObject.getType().length() + this.nlLength));
        if (!pemObject.getHeaders().isEmpty()) {
            Iterator iterator = pemObject.getHeaders().iterator();
            int n2 = n;
            while (iterator.hasNext()) {
                PemHeader pemHeader = (PemHeader)iterator.next();
                n2 += pemHeader.getName().length() + ": ".length() + pemHeader.getValue().length() + this.nlLength;
            }
            n = n2 + this.nlLength;
        }
        int n3 = 4 * ((2 + pemObject.getContent().length) / 3);
        return n + (n3 + (-1 + (n3 + 64)) / 64 * this.nlLength);
    }

    public void writeObject(PemObjectGenerator pemObjectGenerator) {
        PemObject pemObject = pemObjectGenerator.generate();
        this.writePreEncapsulationBoundary(pemObject.getType());
        if (!pemObject.getHeaders().isEmpty()) {
            for (PemHeader pemHeader : pemObject.getHeaders()) {
                this.write(pemHeader.getName());
                this.write(": ");
                this.write(pemHeader.getValue());
                this.newLine();
            }
            this.newLine();
        }
        this.writeEncoded(pemObject.getContent());
        this.writePostEncapsulationBoundary(pemObject.getType());
    }
}

