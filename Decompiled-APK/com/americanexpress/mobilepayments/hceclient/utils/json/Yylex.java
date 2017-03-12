package com.americanexpress.mobilepayments.hceclient.utils.json;

import com.google.android.gms.location.places.Place;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPConstants;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.agreement.jpake.JPAKEParticipant;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;

class Yylex {
    public static final int STRING_BEGIN = 2;
    public static final int YYEOF = -1;
    public static final int YYINITIAL = 0;
    private static final int[] ZZ_ACTION;
    private static final String ZZ_ACTION_PACKED_0 = "\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0005\u0000\u0001\f\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018";
    private static final int[] ZZ_ATTRIBUTE;
    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\u0002\u0000\u0001\t\u0003\u0001\u0001\t\u0003\u0001\u0006\t\u0002\u0001\u0001\t\u0005\u0000\b\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\t\u0002\u0000\u0001\t";
    private static final int ZZ_BUFFERSIZE = 16384;
    private static final char[] ZZ_CMAP;
    private static final String ZZ_CMAP_PACKED = "\t\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\t\b\u0000\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\n\n\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005\u0001\u0001\u0014\u0000\u0001\u0017\u0001\b\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011\u0001\f\u0005\u0000\u0001\u0013\u0001\u0000\u0001\r\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016\uff82\u0000";
    private static final String[] ZZ_ERROR_MSG;
    private static final int[] ZZ_LEXSTATE;
    private static final int ZZ_NO_MATCH = 1;
    private static final int ZZ_PUSHBACK_2BIG = 2;
    private static final int[] ZZ_ROWMAP;
    private static final String ZZ_ROWMAP_PACKED_0 = "\u0000\u0000\u0000\u001b\u00006\u0000Q\u0000l\u0000\u0087\u00006\u0000\u00a2\u0000\u00bd\u0000\u00d8\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u00f3\u0000\u010e\u00006\u0000\u0129\u0000\u0144\u0000\u015f\u0000\u017a\u0000\u0195\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u01b0\u0000\u01cb\u0000\u01e6\u0000\u01e6\u0000\u0201\u0000\u021c\u0000\u0237\u0000\u0252\u00006\u00006\u0000\u026d\u0000\u0288\u00006";
    private static final int[] ZZ_TRANS;
    private static final int ZZ_UNKNOWN_ERROR = 0;
    private StringBuffer sb;
    private int yychar;
    private int yycolumn;
    private int yyline;
    private boolean zzAtBOL;
    private boolean zzAtEOF;
    private char[] zzBuffer;
    private int zzCurrentPos;
    private int zzEndRead;
    private int zzLexicalState;
    private int zzMarkedPos;
    private Reader zzReader;
    private int zzStartRead;
    private int zzState;

    static {
        ZZ_LEXSTATE = new int[]{YYINITIAL, YYINITIAL, ZZ_NO_MATCH, ZZ_NO_MATCH};
        ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);
        ZZ_ACTION = zzUnpackAction();
        ZZ_ROWMAP = zzUnpackRowMap();
        ZZ_TRANS = new int[]{ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, 3, 4, ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, 5, ZZ_PUSHBACK_2BIG, 6, ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, 7, 8, ZZ_PUSHBACK_2BIG, 9, ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, ZZ_PUSHBACK_2BIG, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 4, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 4, 19, 20, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 20, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 5, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 21, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 22, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 23, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 16, 16, 16, 16, 16, 16, 16, 16, YYEOF, YYEOF, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 24, 25, 26, 27, 28, 29, 30, 31, 32, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 33, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 34, 35, YYEOF, YYEOF, 34, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 36, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 37, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 38, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 39, YYEOF, 39, YYEOF, 39, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 39, 39, YYEOF, YYEOF, YYEOF, YYEOF, 39, 39, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 33, YYEOF, 20, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 20, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 35, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 38, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 40, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 41, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 42, YYEOF, 42, YYEOF, 42, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 42, 42, YYEOF, YYEOF, YYEOF, YYEOF, 42, 42, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 43, YYEOF, 43, YYEOF, 43, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 43, 43, YYEOF, YYEOF, YYEOF, YYEOF, 43, 43, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 44, YYEOF, 44, YYEOF, 44, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, 44, 44, YYEOF, YYEOF, YYEOF, YYEOF, 44, 44, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF, YYEOF};
        ZZ_ERROR_MSG = new String[]{"Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large"};
        ZZ_ATTRIBUTE = zzUnpackAttribute();
    }

    private static int[] zzUnpackAction() {
        int[] iArr = new int[45];
        zzUnpackAction(ZZ_ACTION_PACKED_0, YYINITIAL, iArr);
        return iArr;
    }

    private static int zzUnpackAction(String str, int i, int[] iArr) {
        int i2 = YYINITIAL;
        int length = str.length();
        int i3 = i;
        while (i2 < length) {
            int i4 = i2 + ZZ_NO_MATCH;
            i2 = str.charAt(i2);
            int i5 = i4 + ZZ_NO_MATCH;
            char charAt = str.charAt(i4);
            while (true) {
                i4 = i3 + ZZ_NO_MATCH;
                iArr[i3] = charAt;
                i2 += YYEOF;
                if (i2 <= 0) {
                    break;
                }
                i3 = i4;
            }
            i3 = i4;
            i2 = i5;
        }
        return i3;
    }

    private static int[] zzUnpackRowMap() {
        int[] iArr = new int[45];
        zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, YYINITIAL, iArr);
        return iArr;
    }

    private static int zzUnpackRowMap(String str, int i, int[] iArr) {
        int i2 = YYINITIAL;
        int length = str.length();
        while (i2 < length) {
            int i3 = i2 + ZZ_NO_MATCH;
            int charAt = str.charAt(i2) << 16;
            i2 = i + ZZ_NO_MATCH;
            int i4 = i3 + ZZ_NO_MATCH;
            iArr[i] = str.charAt(i3) | charAt;
            i = i2;
            i2 = i4;
        }
        return i;
    }

    private static int[] zzUnpackAttribute() {
        int[] iArr = new int[45];
        zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, YYINITIAL, iArr);
        return iArr;
    }

    private static int zzUnpackAttribute(String str, int i, int[] iArr) {
        int i2 = YYINITIAL;
        int length = str.length();
        int i3 = i;
        while (i2 < length) {
            int i4 = i2 + ZZ_NO_MATCH;
            i2 = str.charAt(i2);
            int i5 = i4 + ZZ_NO_MATCH;
            char charAt = str.charAt(i4);
            while (true) {
                i4 = i3 + ZZ_NO_MATCH;
                iArr[i3] = charAt;
                i2 += YYEOF;
                if (i2 <= 0) {
                    break;
                }
                i3 = i4;
            }
            i3 = i4;
            i2 = i5;
        }
        return i3;
    }

    int getPosition() {
        return this.yychar;
    }

    Yylex(Reader reader) {
        this.zzLexicalState = YYINITIAL;
        this.zzBuffer = new char[ZZ_BUFFERSIZE];
        this.zzAtBOL = true;
        this.sb = new StringBuffer();
        this.zzReader = reader;
    }

    Yylex(InputStream inputStream) {
        this(new InputStreamReader(inputStream));
    }

    private static char[] zzUnpackCMap(String str) {
        int i = YYINITIAL;
        char[] cArr = new char[PKIFailureInfo.notAuthorized];
        int i2 = YYINITIAL;
        while (i2 < 90) {
            int i3 = i2 + ZZ_NO_MATCH;
            i2 = str.charAt(i2);
            int i4 = i3 + ZZ_NO_MATCH;
            char charAt = str.charAt(i3);
            while (true) {
                i3 = i + ZZ_NO_MATCH;
                cArr[i] = charAt;
                i2 += YYEOF;
                if (i2 <= 0) {
                    break;
                }
                i = i3;
            }
            i = i3;
            i2 = i4;
        }
        return cArr;
    }

    private boolean zzRefill() {
        if (this.zzStartRead > 0) {
            System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, YYINITIAL, this.zzEndRead - this.zzStartRead);
            this.zzEndRead -= this.zzStartRead;
            this.zzCurrentPos -= this.zzStartRead;
            this.zzMarkedPos -= this.zzStartRead;
            this.zzStartRead = YYINITIAL;
        }
        if (this.zzCurrentPos >= this.zzBuffer.length) {
            Object obj = new char[(this.zzCurrentPos * ZZ_PUSHBACK_2BIG)];
            System.arraycopy(this.zzBuffer, YYINITIAL, obj, YYINITIAL, this.zzBuffer.length);
            this.zzBuffer = obj;
        }
        int read = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
        if (read > 0) {
            this.zzEndRead += read;
            return false;
        } else if (read != 0) {
            return true;
        } else {
            read = this.zzReader.read();
            if (read == YYEOF) {
                return true;
            }
            char[] cArr = this.zzBuffer;
            int i = this.zzEndRead;
            this.zzEndRead = i + ZZ_NO_MATCH;
            cArr[i] = (char) read;
            return false;
        }
    }

    public final void yyclose() {
        this.zzAtEOF = true;
        this.zzEndRead = this.zzStartRead;
        if (this.zzReader != null) {
            this.zzReader.close();
        }
    }

    public final void yyreset(Reader reader) {
        this.zzReader = reader;
        this.zzAtBOL = true;
        this.zzAtEOF = false;
        this.zzStartRead = YYINITIAL;
        this.zzEndRead = YYINITIAL;
        this.zzMarkedPos = YYINITIAL;
        this.zzCurrentPos = YYINITIAL;
        this.yycolumn = YYINITIAL;
        this.yychar = YYINITIAL;
        this.yyline = YYINITIAL;
        this.zzLexicalState = YYINITIAL;
    }

    public final int yystate() {
        return this.zzLexicalState;
    }

    public final void yybegin(int i) {
        this.zzLexicalState = i;
    }

    public final String yytext() {
        return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
    }

    public final char yycharat(int i) {
        return this.zzBuffer[this.zzStartRead + i];
    }

    public final int yylength() {
        return this.zzMarkedPos - this.zzStartRead;
    }

    private void zzScanError(int i) {
        String str;
        try {
            str = ZZ_ERROR_MSG[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            str = ZZ_ERROR_MSG[YYINITIAL];
        }
        throw new Error(str);
    }

    public void yypushback(int i) {
        if (i > yylength()) {
            zzScanError(ZZ_PUSHBACK_2BIG);
        }
        this.zzMarkedPos -= i;
    }

    public Yytoken yylex() {
        int i = this.zzEndRead;
        char[] cArr = this.zzBuffer;
        char[] cArr2 = ZZ_CMAP;
        int[] iArr = ZZ_TRANS;
        int[] iArr2 = ZZ_ROWMAP;
        int[] iArr3 = ZZ_ATTRIBUTE;
        while (true) {
            int i2 = this.zzMarkedPos;
            this.yychar += i2 - this.zzStartRead;
            int i3 = YYEOF;
            this.zzStartRead = i2;
            this.zzCurrentPos = i2;
            this.zzState = ZZ_LEXSTATE[this.zzLexicalState];
            int i4 = i2;
            while (true) {
                int i5;
                if (i4 < i) {
                    i5 = i4 + ZZ_NO_MATCH;
                    i4 = cArr[i4];
                } else {
                    if (this.zzAtEOF) {
                        i4 = YYEOF;
                    } else {
                        this.zzCurrentPos = i4;
                        this.zzMarkedPos = i2;
                        boolean zzRefill = zzRefill();
                        i4 = this.zzCurrentPos;
                        i2 = this.zzMarkedPos;
                        cArr = this.zzBuffer;
                        i = this.zzEndRead;
                        if (zzRefill) {
                            i4 = YYEOF;
                        } else {
                            i5 = i4 + ZZ_NO_MATCH;
                            i4 = cArr[i4];
                        }
                    }
                    this.zzMarkedPos = i2;
                    if (i3 >= 0) {
                        i3 = ZZ_ACTION[i3];
                    }
                    switch (i3) {
                        case ZZ_NO_MATCH /*1*/:
                            throw new ParseException(this.yychar, YYINITIAL, new Character(yycharat(YYINITIAL)));
                        case ZZ_PUSHBACK_2BIG /*2*/:
                            return new Yytoken(YYINITIAL, Long.valueOf(yytext()));
                        case F2m.PPB /*3*/:
                        case NamedCurve.secp521r1 /*25*/:
                        case NamedCurve.brainpoolP256r1 /*26*/:
                        case NamedCurve.brainpoolP384r1 /*27*/:
                        case NamedCurve.brainpoolP512r1 /*28*/:
                        case DSRPConstants.APP_CRYPTO_GENERATION_CDOL_PART_LAST_BYTE /*29*/:
                        case JPAKEParticipant.STATE_ROUND_2_CREATED /*30*/:
                        case Place.TYPE_ELECTRICIAN /*31*/:
                        case X509KeyUsage.keyEncipherment /*32*/:
                        case EACTags.CARDHOLDER_CERTIFICATE /*33*/:
                        case Place.TYPE_ESTABLISHMENT /*34*/:
                        case ExtensionType.session_ticket /*35*/:
                        case EACTags.APPLICATION_EXPIRATION_DATE /*36*/:
                        case EACTags.APPLICATION_EFFECTIVE_DATE /*37*/:
                        case Place.TYPE_FOOD /*38*/:
                        case Place.TYPE_FUNERAL_HOME /*39*/:
                        case JPAKEParticipant.STATE_ROUND_2_VALIDATED /*40*/:
                        case EACTags.INTERCHANGE_PROFILE /*41*/:
                        case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                        case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                        case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                        case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
                        case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
                        case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                        case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
                            break;
                        case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                            this.sb = null;
                            this.sb = new StringBuffer();
                            yybegin(ZZ_PUSHBACK_2BIG);
                            break;
                        case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                            return new Yytoken(ZZ_NO_MATCH, null);
                        case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                            return new Yytoken(ZZ_PUSHBACK_2BIG, null);
                        case ECCurve.COORD_SKEWED /*7*/:
                            return new Yytoken(3, null);
                        case X509KeyUsage.keyAgreement /*8*/:
                            return new Yytoken(4, null);
                        case NamedCurve.sect283k1 /*9*/:
                            return new Yytoken(5, null);
                        case NamedCurve.sect283r1 /*10*/:
                            return new Yytoken(6, null);
                        case CertStatus.UNREVOKED /*11*/:
                            this.sb.append(yytext());
                            break;
                        case CertStatus.UNDETERMINED /*12*/:
                            this.sb.append('\\');
                            break;
                        case NamedCurve.sect571k1 /*13*/:
                            yybegin(YYINITIAL);
                            return new Yytoken(YYINITIAL, this.sb.toString());
                        case NamedCurve.sect571r1 /*14*/:
                            this.sb.append('\"');
                            break;
                        case NamedCurve.secp160k1 /*15*/:
                            this.sb.append('/');
                            break;
                        case X509KeyUsage.dataEncipherment /*16*/:
                            this.sb.append('\b');
                            break;
                        case NamedCurve.secp160r2 /*17*/:
                            this.sb.append('\f');
                            break;
                        case NamedCurve.secp192k1 /*18*/:
                            this.sb.append('\n');
                            break;
                        case NamedCurve.secp192r1 /*19*/:
                            this.sb.append('\r');
                            break;
                        case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                            this.sb.append('\t');
                            break;
                        case NamedCurve.secp224r1 /*21*/:
                            return new Yytoken(YYINITIAL, Double.valueOf(yytext()));
                        case NamedCurve.secp256k1 /*22*/:
                            return new Yytoken(YYINITIAL, null);
                        case NamedCurve.secp256r1 /*23*/:
                            return new Yytoken(YYINITIAL, Boolean.valueOf(yytext()));
                        case NamedCurve.secp384r1 /*24*/:
                            try {
                                this.sb.append((char) Integer.parseInt(yytext().substring(ZZ_PUSHBACK_2BIG), 16));
                                break;
                            } catch (Exception e) {
                                throw new ParseException(this.yychar, ZZ_PUSHBACK_2BIG, e);
                            }
                        default:
                            if (i4 == YYEOF || this.zzStartRead != this.zzCurrentPos) {
                                zzScanError(ZZ_NO_MATCH);
                                break;
                            }
                            this.zzAtEOF = true;
                            return null;
                    }
                }
                int i6 = iArr[iArr2[this.zzState] + cArr2[i4]];
                if (i6 != YYEOF) {
                    this.zzState = i6;
                    i6 = iArr3[this.zzState];
                    if ((i6 & ZZ_NO_MATCH) == ZZ_NO_MATCH) {
                        i2 = this.zzState;
                        if ((i6 & 8) == 8) {
                            i3 = i2;
                            i2 = i5;
                        } else {
                            i4 = i2;
                            i2 = i5;
                        }
                    } else {
                        i4 = i3;
                    }
                    i3 = i4;
                    i4 = i5;
                }
                this.zzMarkedPos = i2;
                if (i3 >= 0) {
                    i3 = ZZ_ACTION[i3];
                }
                switch (i3) {
                    case ZZ_NO_MATCH /*1*/:
                        throw new ParseException(this.yychar, YYINITIAL, new Character(yycharat(YYINITIAL)));
                    case ZZ_PUSHBACK_2BIG /*2*/:
                        return new Yytoken(YYINITIAL, Long.valueOf(yytext()));
                    case F2m.PPB /*3*/:
                    case NamedCurve.secp521r1 /*25*/:
                    case NamedCurve.brainpoolP256r1 /*26*/:
                    case NamedCurve.brainpoolP384r1 /*27*/:
                    case NamedCurve.brainpoolP512r1 /*28*/:
                    case DSRPConstants.APP_CRYPTO_GENERATION_CDOL_PART_LAST_BYTE /*29*/:
                    case JPAKEParticipant.STATE_ROUND_2_CREATED /*30*/:
                    case Place.TYPE_ELECTRICIAN /*31*/:
                    case X509KeyUsage.keyEncipherment /*32*/:
                    case EACTags.CARDHOLDER_CERTIFICATE /*33*/:
                    case Place.TYPE_ESTABLISHMENT /*34*/:
                    case ExtensionType.session_ticket /*35*/:
                    case EACTags.APPLICATION_EXPIRATION_DATE /*36*/:
                    case EACTags.APPLICATION_EFFECTIVE_DATE /*37*/:
                    case Place.TYPE_FOOD /*38*/:
                    case Place.TYPE_FUNERAL_HOME /*39*/:
                    case JPAKEParticipant.STATE_ROUND_2_VALIDATED /*40*/:
                    case EACTags.INTERCHANGE_PROFILE /*41*/:
                    case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                    case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                    case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                    case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
                    case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
                    case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                    case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
                        break;
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        this.sb = null;
                        this.sb = new StringBuffer();
                        yybegin(ZZ_PUSHBACK_2BIG);
                        break;
                    case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                        return new Yytoken(ZZ_NO_MATCH, null);
                    case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                        return new Yytoken(ZZ_PUSHBACK_2BIG, null);
                    case ECCurve.COORD_SKEWED /*7*/:
                        return new Yytoken(3, null);
                    case X509KeyUsage.keyAgreement /*8*/:
                        return new Yytoken(4, null);
                    case NamedCurve.sect283k1 /*9*/:
                        return new Yytoken(5, null);
                    case NamedCurve.sect283r1 /*10*/:
                        return new Yytoken(6, null);
                    case CertStatus.UNREVOKED /*11*/:
                        this.sb.append(yytext());
                        break;
                    case CertStatus.UNDETERMINED /*12*/:
                        this.sb.append('\\');
                        break;
                    case NamedCurve.sect571k1 /*13*/:
                        yybegin(YYINITIAL);
                        return new Yytoken(YYINITIAL, this.sb.toString());
                    case NamedCurve.sect571r1 /*14*/:
                        this.sb.append('\"');
                        break;
                    case NamedCurve.secp160k1 /*15*/:
                        this.sb.append('/');
                        break;
                    case X509KeyUsage.dataEncipherment /*16*/:
                        this.sb.append('\b');
                        break;
                    case NamedCurve.secp160r2 /*17*/:
                        this.sb.append('\f');
                        break;
                    case NamedCurve.secp192k1 /*18*/:
                        this.sb.append('\n');
                        break;
                    case NamedCurve.secp192r1 /*19*/:
                        this.sb.append('\r');
                        break;
                    case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                        this.sb.append('\t');
                        break;
                    case NamedCurve.secp224r1 /*21*/:
                        return new Yytoken(YYINITIAL, Double.valueOf(yytext()));
                    case NamedCurve.secp256k1 /*22*/:
                        return new Yytoken(YYINITIAL, null);
                    case NamedCurve.secp256r1 /*23*/:
                        return new Yytoken(YYINITIAL, Boolean.valueOf(yytext()));
                    case NamedCurve.secp384r1 /*24*/:
                        this.sb.append((char) Integer.parseInt(yytext().substring(ZZ_PUSHBACK_2BIG), 16));
                        break;
                    default:
                        if (i4 == YYEOF) {
                            break;
                        }
                        zzScanError(ZZ_NO_MATCH);
                        break;
                }
            }
        }
    }
}
