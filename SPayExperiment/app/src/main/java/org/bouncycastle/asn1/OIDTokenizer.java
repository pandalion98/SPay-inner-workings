/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

public class OIDTokenizer {
    private int index;
    private String oid;

    public OIDTokenizer(String string) {
        this.oid = string;
        this.index = 0;
    }

    public boolean hasMoreTokens() {
        return this.index != -1;
    }

    public String nextToken() {
        if (this.index == -1) {
            return null;
        }
        int n2 = this.oid.indexOf(46, this.index);
        if (n2 == -1) {
            String string = this.oid.substring(this.index);
            this.index = -1;
            return string;
        }
        String string = this.oid.substring(this.index, n2);
        this.index = n2 + 1;
        return string;
    }
}

