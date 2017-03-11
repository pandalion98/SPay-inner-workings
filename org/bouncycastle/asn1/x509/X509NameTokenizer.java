package org.bouncycastle.asn1.x509;

public class X509NameTokenizer {
    private StringBuffer buf;
    private int index;
    private char separator;
    private String value;

    public X509NameTokenizer(String str) {
        this(str, ',');
    }

    public X509NameTokenizer(String str, char c) {
        this.buf = new StringBuffer();
        this.value = str;
        this.index = -1;
        this.separator = c;
    }

    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }

    public String nextToken() {
        if (this.index == this.value.length()) {
            return null;
        }
        int i = this.index + 1;
        this.buf.setLength(0);
        int i2 = 0;
        int i3 = i;
        i = 0;
        while (i3 != this.value.length()) {
            char charAt = this.value.charAt(i3);
            if (charAt == '\"') {
                i = i == 0 ? i2 == 0 ? 1 : 0 : i2;
                this.buf.append(charAt);
                i2 = i;
                i = 0;
            } else if (i != 0 || i2 != 0) {
                this.buf.append(charAt);
                i = 0;
            } else if (charAt == '\\') {
                this.buf.append(charAt);
                i = 1;
            } else if (charAt == this.separator) {
                break;
            } else {
                this.buf.append(charAt);
            }
            i3++;
        }
        this.index = i3;
        return this.buf.toString();
    }
}
