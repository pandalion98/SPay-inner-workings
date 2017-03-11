package com.squareup.okhttp.internal.tls;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.google.android.gms.location.places.Place;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

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

    private String nextAT() {
        while (this.pos < this.length && this.chars[this.pos] == ' ') {
            this.pos++;
        }
        if (this.pos == this.length) {
            return null;
        }
        this.beg = this.pos;
        this.pos++;
        while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] != ' ') {
            this.pos++;
        }
        if (this.pos >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        this.end = this.pos;
        if (this.chars[this.pos] == ' ') {
            while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] == ' ') {
                this.pos++;
            }
            if (this.chars[this.pos] != '=' || this.pos == this.length) {
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
        }
        this.pos++;
        while (this.pos < this.length && this.chars[this.pos] == ' ') {
            this.pos++;
        }
        if (this.end - this.beg > 4 && this.chars[this.beg + 3] == '.' && ((this.chars[this.beg] == 'O' || this.chars[this.beg] == 'o') && ((this.chars[this.beg + 1] == PolynomialGF2mSmallM.RANDOM_IRREDUCIBLE_POLYNOMIAL || this.chars[this.beg + 1] == 'i') && (this.chars[this.beg + 2] == 'D' || this.chars[this.beg + 2] == 'd')))) {
            this.beg += 4;
        }
        return new String(this.chars, this.beg, this.end - this.beg);
    }

    private String quotedAV() {
        this.pos++;
        this.beg = this.pos;
        this.end = this.beg;
        while (this.pos != this.length) {
            if (this.chars[this.pos] == '\"') {
                this.pos++;
                while (this.pos < this.length && this.chars[this.pos] == ' ') {
                    this.pos++;
                }
                return new String(this.chars, this.beg, this.end - this.beg);
            }
            if (this.chars[this.pos] == '\\') {
                this.chars[this.end] = getEscaped();
            } else {
                this.chars[this.end] = this.chars[this.pos];
            }
            this.pos++;
            this.end++;
        }
        throw new IllegalStateException("Unexpected end of DN: " + this.dn);
    }

    private String hexAV() {
        if (this.pos + 4 >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        int i;
        this.beg = this.pos;
        this.pos++;
        while (this.pos != this.length && this.chars[this.pos] != '+' && this.chars[this.pos] != ',' && this.chars[this.pos] != ';') {
            int i2;
            if (this.chars[this.pos] == ' ') {
                this.end = this.pos;
                this.pos++;
                while (this.pos < this.length && this.chars[this.pos] == ' ') {
                    this.pos++;
                }
                i = this.end - this.beg;
                if (i >= 5 || (i & 1) == 0) {
                    throw new IllegalStateException("Unexpected end of DN: " + this.dn);
                }
                byte[] bArr = new byte[(i / 2)];
                int i3 = this.beg + 1;
                for (i2 = 0; i2 < bArr.length; i2++) {
                    bArr[i2] = (byte) getByte(i3);
                    i3 += 2;
                }
                return new String(this.chars, this.beg, i);
            }
            if (this.chars[this.pos] >= 'A' && this.chars[this.pos] <= 'F') {
                char[] cArr = this.chars;
                i2 = this.pos;
                cArr[i2] = (char) (cArr[i2] + 32);
            }
            this.pos++;
        }
        this.end = this.pos;
        i = this.end - this.beg;
        if (i >= 5) {
        }
        throw new IllegalStateException("Unexpected end of DN: " + this.dn);
    }

    private String escapedAV() {
        this.beg = this.pos;
        this.end = this.pos;
        while (this.pos < this.length) {
            char[] cArr;
            int i;
            switch (this.chars[this.pos]) {
                case X509KeyUsage.keyEncipherment /*32*/:
                    this.cur = this.end;
                    this.pos++;
                    cArr = this.chars;
                    i = this.end;
                    this.end = i + 1;
                    cArr[i] = ' ';
                    while (this.pos < this.length && this.chars[this.pos] == ' ') {
                        cArr = this.chars;
                        i = this.end;
                        this.end = i + 1;
                        cArr[i] = ' ';
                        this.pos++;
                    }
                    if (this.pos != this.length && this.chars[this.pos] != ',' && this.chars[this.pos] != '+' && this.chars[this.pos] != ';') {
                        break;
                    }
                    return new String(this.chars, this.beg, this.cur - this.beg);
                    break;
                case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                    return new String(this.chars, this.beg, this.end - this.beg);
                case EACTags.TAG_LIST /*92*/:
                    cArr = this.chars;
                    i = this.end;
                    this.end = i + 1;
                    cArr[i] = getEscaped();
                    this.pos++;
                    break;
                default:
                    cArr = this.chars;
                    i = this.end;
                    this.end = i + 1;
                    cArr[i] = this.chars[this.pos];
                    this.pos++;
                    break;
            }
        }
        return new String(this.chars, this.beg, this.end - this.beg);
    }

    private char getEscaped() {
        this.pos++;
        if (this.pos == this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        switch (this.chars[this.pos]) {
            case X509KeyUsage.keyEncipherment /*32*/:
            case Place.TYPE_ESTABLISHMENT /*34*/:
            case ExtensionType.session_ticket /*35*/:
            case EACTags.APPLICATION_EFFECTIVE_DATE /*37*/:
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
            case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_CBC_SHA256 /*62*/:
            case EACTags.TAG_LIST /*92*/:
            case Place.TYPE_VETERINARY_CARE /*95*/:
                return this.chars[this.pos];
            default:
                return getUTF8();
        }
    }

    private char getUTF8() {
        int i = getByte(this.pos);
        this.pos++;
        if (i < X509KeyUsage.digitalSignature) {
            return (char) i;
        }
        if (i < CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 || i > 247) {
            return '?';
        }
        int i2;
        if (i <= 223) {
            i2 = 1;
            i &= 31;
        } else if (i <= 239) {
            i2 = 2;
            i &= 15;
        } else {
            i2 = 3;
            i &= 7;
        }
        int i3 = i;
        for (i = 0; i < i2; i++) {
            this.pos++;
            if (this.pos == this.length || this.chars[this.pos] != '\\') {
                return '?';
            }
            this.pos++;
            int i4 = getByte(this.pos);
            this.pos++;
            if ((i4 & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) != X509KeyUsage.digitalSignature) {
                return '?';
            }
            i3 = (i3 << 6) + (i4 & 63);
        }
        return (char) i3;
    }

    private int getByte(int i) {
        if (i + 1 >= this.length) {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        int i2;
        int i3;
        char c = this.chars[i];
        if (c >= LLVARUtil.EMPTY_STRING && c <= '9') {
            i2 = c - 48;
        } else if (c >= 'a' && c <= 'f') {
            i2 = c - 87;
        } else if (c < 'A' || c > 'F') {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        } else {
            i2 = c - 55;
        }
        char c2 = this.chars[i + 1];
        if (c2 >= LLVARUtil.EMPTY_STRING && c2 <= '9') {
            i3 = c2 - 48;
        } else if (c2 >= 'a' && c2 <= 'f') {
            i3 = c2 - 87;
        } else if (c2 < 'A' || c2 > 'F') {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        } else {
            i3 = c2 - 55;
        }
        return (i2 << 4) + i3;
    }

    public String findMostSpecific(String str) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        String nextAT = nextAT();
        if (nextAT == null) {
            return null;
        }
        do {
            String str2 = BuildConfig.FLAVOR;
            if (this.pos == this.length) {
                return null;
            }
            switch (this.chars[this.pos]) {
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    str2 = quotedAV();
                    break;
                case ExtensionType.session_ticket /*35*/:
                    str2 = hexAV();
                    break;
                case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                    break;
                default:
                    str2 = escapedAV();
                    break;
            }
            if (str.equalsIgnoreCase(nextAT)) {
                return str2;
            }
            if (this.pos >= this.length) {
                return null;
            }
            if (this.chars[this.pos] == ',' || this.chars[this.pos] == ';' || this.chars[this.pos] == '+') {
                this.pos++;
                nextAT = nextAT();
            } else {
                throw new IllegalStateException("Malformed DN: " + this.dn);
            }
        } while (nextAT != null);
        throw new IllegalStateException("Malformed DN: " + this.dn);
    }
}
