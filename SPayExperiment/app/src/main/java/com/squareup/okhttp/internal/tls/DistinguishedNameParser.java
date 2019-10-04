/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  javax.security.auth.x500.X500Principal
 */
package com.squareup.okhttp.internal.tls;

import javax.security.auth.x500.X500Principal;

final class DistinguishedNameParser {
    private int beg;
    private char[] chars;
    private int cur;
    private final String dn;
    private int end;
    private final int length;
    private int pos;

    public DistinguishedNameParser(X500Principal x500Principal) {
        this.dn = x500Principal.getName("RFC2253");
        this.length = this.dn.length();
    }

    private String escapedAV() {
        this.beg = this.pos;
        this.end = this.pos;
        block5 : do {
            if (this.pos >= this.length) {
                return new String(this.chars, this.beg, this.end - this.beg);
            }
            switch (this.chars[this.pos]) {
                default: {
                    char[] arrc = this.chars;
                    int n2 = this.end;
                    this.end = n2 + 1;
                    arrc[n2] = this.chars[this.pos];
                    this.pos = 1 + this.pos;
                    continue block5;
                }
                case '+': 
                case ',': 
                case ';': {
                    return new String(this.chars, this.beg, this.end - this.beg);
                }
                case '\\': {
                    char[] arrc = this.chars;
                    int n3 = this.end;
                    this.end = n3 + 1;
                    arrc[n3] = this.getEscaped();
                    this.pos = 1 + this.pos;
                    continue block5;
                }
                case ' ': 
            }
            this.cur = this.end;
            this.pos = 1 + this.pos;
            char[] arrc = this.chars;
            int n4 = this.end;
            this.end = n4 + 1;
            arrc[n4] = 32;
            while (this.pos < this.length && this.chars[this.pos] == ' ') {
                char[] arrc2 = this.chars;
                int n5 = this.end;
                this.end = n5 + 1;
                arrc2[n5] = 32;
                this.pos = 1 + this.pos;
            }
            if (this.pos == this.length || this.chars[this.pos] == ',' || this.chars[this.pos] == '+' || this.chars[this.pos] == ';') break;
        } while (true);
        return new String(this.chars, this.beg, this.cur - this.beg);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getByte(int n2) {
        int n3;
        char c2;
        int n4;
        if (n2 + 1 >= this.length) {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        char c3 = this.chars[n2];
        if (c3 >= '0' && c3 <= '9') {
            n4 = c3 - 48;
        } else if (c3 >= 'a' && c3 <= 'f') {
            n4 = c3 - 87;
        } else {
            if (c3 < 'A') throw new IllegalStateException("Malformed DN: " + this.dn);
            if (c3 > 'F') throw new IllegalStateException("Malformed DN: " + this.dn);
            n4 = c3 - 55;
        }
        if ((c2 = this.chars[n2 + 1]) >= '0' && c2 <= '9') {
            n3 = c2 - 48;
            return n3 + (n4 << 4);
        }
        if (c2 >= 'a' && c2 <= 'f') {
            n3 = c2 - 87;
            return n3 + (n4 << 4);
        }
        if (c2 < 'A') throw new IllegalStateException("Malformed DN: " + this.dn);
        if (c2 > 'F') throw new IllegalStateException("Malformed DN: " + this.dn);
        n3 = c2 - 55;
        return n3 + (n4 << 4);
    }

    private char getEscaped() {
        this.pos = 1 + this.pos;
        if (this.pos == this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        switch (this.chars[this.pos]) {
            default: {
                return this.getUTF8();
            }
            case ' ': 
            case '\"': 
            case '#': 
            case '%': 
            case '*': 
            case '+': 
            case ',': 
            case ';': 
            case '<': 
            case '=': 
            case '>': 
            case '\\': 
            case '_': 
        }
        return this.chars[this.pos];
    }

    /*
     * Enabled aggressive block sorting
     */
    private char getUTF8() {
        int n2;
        int n3;
        int n4 = this.getByte(this.pos);
        this.pos = 1 + this.pos;
        if (n4 < 128) {
            return (char)n4;
        }
        if (n4 >= 192 && n4 <= 247) {
            if (n4 <= 223) {
                n2 = 1;
                n3 = n4 & 31;
            } else if (n4 <= 239) {
                n2 = 2;
                n3 = n4 & 15;
            } else {
                n2 = 3;
                n3 = n4 & 7;
            }
        } else {
            return '?';
        }
        int n5 = n3;
        int n6 = 0;
        while (n6 < n2) {
            this.pos = 1 + this.pos;
            if (this.pos == this.length || this.chars[this.pos] != '\\') {
                return '?';
            }
            this.pos = 1 + this.pos;
            int n7 = this.getByte(this.pos);
            this.pos = 1 + this.pos;
            if ((n7 & 192) != 128) {
                return '?';
            }
            n5 = (n5 << 6) + (n7 & 63);
            ++n6;
        }
        return (char)n5;
    }

    /*
     * Enabled aggressive block sorting
     */
    private String hexAV() {
        int n2;
        if (4 + this.pos >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        this.beg = this.pos;
        this.pos = 1 + this.pos;
        do {
            block9 : {
                block8 : {
                    block7 : {
                        if (this.pos != this.length && this.chars[this.pos] != '+' && this.chars[this.pos] != ',' && this.chars[this.pos] != ';') break block7;
                        this.end = this.pos;
                        break block8;
                    }
                    if (this.chars[this.pos] == ' ') {
                        this.end = this.pos;
                        this.pos = 1 + this.pos;
                        while (this.pos < this.length && this.chars[this.pos] == ' ') {
                            this.pos = 1 + this.pos;
                        }
                    }
                    break block9;
                }
                if ((n2 = this.end - this.beg) >= 5 && (n2 & 1) != 0) break;
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
            if (this.chars[this.pos] >= 'A' && this.chars[this.pos] <= 'F') {
                char[] arrc = this.chars;
                int n3 = this.pos;
                arrc[n3] = (char)(32 + arrc[n3]);
            }
            this.pos = 1 + this.pos;
        } while (true);
        byte[] arrby = new byte[n2 / 2];
        int n4 = 0;
        int n5 = 1 + this.beg;
        while (n4 < arrby.length) {
            arrby[n4] = (byte)this.getByte(n5);
            n5 += 2;
            ++n4;
        }
        return new String(this.chars, this.beg, n2);
    }

    private String nextAT() {
        while (this.pos < this.length && this.chars[this.pos] == ' ') {
            this.pos = 1 + this.pos;
        }
        if (this.pos == this.length) {
            return null;
        }
        this.beg = this.pos;
        this.pos = 1 + this.pos;
        while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] != ' ') {
            this.pos = 1 + this.pos;
        }
        if (this.pos >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        this.end = this.pos;
        if (this.chars[this.pos] == ' ') {
            while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] == ' ') {
                this.pos = 1 + this.pos;
            }
            if (this.chars[this.pos] != '=' || this.pos == this.length) {
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
        }
        this.pos = 1 + this.pos;
        while (this.pos < this.length && this.chars[this.pos] == ' ') {
            this.pos = 1 + this.pos;
        }
        if (!(this.end - this.beg <= 4 || this.chars[3 + this.beg] != '.' || this.chars[this.beg] != 'O' && this.chars[this.beg] != 'o' || this.chars[1 + this.beg] != 'I' && this.chars[1 + this.beg] != 'i' || this.chars[2 + this.beg] != 'D' && this.chars[2 + this.beg] != 'd')) {
            this.beg = 4 + this.beg;
        }
        return new String(this.chars, this.beg, this.end - this.beg);
    }

    /*
     * Enabled aggressive block sorting
     */
    private String quotedAV() {
        this.end = this.beg = (this.pos = 1 + this.pos);
        do {
            if (this.pos == this.length) {
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
            if (this.chars[this.pos] == '\"') {
                this.pos = 1 + this.pos;
                while (this.pos < this.length && this.chars[this.pos] == ' ') {
                    this.pos = 1 + this.pos;
                }
                return new String(this.chars, this.beg, this.end - this.beg);
            }
            this.chars[this.end] = this.chars[this.pos] == '\\' ? this.getEscaped() : this.chars[this.pos];
            this.pos = 1 + this.pos;
            this.end = 1 + this.end;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public String findMostSpecific(String string) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        String string2 = this.nextAT();
        if (string2 == null) {
            return null;
        }
        do {
            String string3 = "";
            if (this.pos == this.length) {
                return null;
            }
            switch (this.chars[this.pos]) {
                default: {
                    string3 = this.escapedAV();
                    break;
                }
                case '\"': {
                    string3 = this.quotedAV();
                    break;
                }
                case '#': {
                    string3 = this.hexAV();
                }
                case '+': 
                case ',': 
                case ';': 
            }
            if (string.equalsIgnoreCase(string2)) return string3;
            if (this.pos >= this.length) {
                return null;
            }
            if (this.chars[this.pos] != ',' && this.chars[this.pos] != ';' && this.chars[this.pos] != '+') {
                throw new IllegalStateException("Malformed DN: " + this.dn);
            }
            this.pos = 1 + this.pos;
        } while ((string2 = this.nextAT()) != null);
        throw new IllegalStateException("Malformed DN: " + this.dn);
    }
}

