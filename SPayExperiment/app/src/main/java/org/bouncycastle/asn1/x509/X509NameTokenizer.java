/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package org.bouncycastle.asn1.x509;

public class X509NameTokenizer {
    private StringBuffer buf = new StringBuffer();
    private int index;
    private char separator;
    private String value;

    public X509NameTokenizer(String string) {
        this(string, ',');
    }

    public X509NameTokenizer(String string, char c2) {
        this.value = string;
        this.index = -1;
        this.separator = c2;
    }

    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }

    /*
     * Enabled aggressive block sorting
     */
    public String nextToken() {
        int n2;
        if (this.index == this.value.length()) {
            return null;
        }
        int n3 = 1 + this.index;
        this.buf.setLength(0);
        boolean bl = false;
        boolean bl2 = false;
        for (n2 = n3; n2 != this.value.length(); ++n2) {
            char c2;
            boolean bl3;
            block6 : {
                block5 : {
                    block4 : {
                        c2 = this.value.charAt(n2);
                        if (c2 != '\"') break block4;
                        if (bl2) break block5;
                        bl3 = !bl;
                        break block6;
                    }
                    if (bl2 || bl) {
                        this.buf.append(c2);
                        bl2 = false;
                        continue;
                    }
                    if (c2 == '\\') {
                        this.buf.append(c2);
                        bl2 = true;
                        continue;
                    }
                    if (c2 == this.separator) break;
                    this.buf.append(c2);
                    continue;
                }
                bl3 = bl;
            }
            this.buf.append(c2);
            bl = bl3;
            bl2 = false;
        }
        this.index = n2;
        return this.buf.toString();
    }
}

