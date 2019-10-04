/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util.io.pem;

public class PemHeader {
    private String name;
    private String value;

    public PemHeader(String string, String string2) {
        this.name = string;
        this.value = string2;
    }

    private int getHashCode(String string) {
        if (string == null) {
            return 1;
        }
        return string.hashCode();
    }

    private boolean isEqual(String string, String string2) {
        if (string == string2) {
            return true;
        }
        if (string == null || string2 == null) {
            return false;
        }
        return string.equals((Object)string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        PemHeader pemHeader;
        return object instanceof PemHeader && ((pemHeader = (PemHeader)object) == this || this.isEqual(this.name, pemHeader.name) && this.isEqual(this.value, pemHeader.value));
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public int hashCode() {
        return this.getHashCode(this.name) + 31 * this.getHashCode(this.value);
    }
}

