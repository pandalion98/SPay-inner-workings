package org.bouncycastle.asn1;

public class OIDTokenizer {
    private int index;
    private String oid;

    public OIDTokenizer(String str) {
        this.oid = str;
        this.index = 0;
    }

    public boolean hasMoreTokens() {
        return this.index != -1;
    }

    public String nextToken() {
        if (this.index == -1) {
            return null;
        }
        int indexOf = this.oid.indexOf(46, this.index);
        if (indexOf == -1) {
            String substring = this.oid.substring(this.index);
            this.index = -1;
            return substring;
        }
        substring = this.oid.substring(this.index, indexOf);
        this.index = indexOf + 1;
        return substring;
    }
}
