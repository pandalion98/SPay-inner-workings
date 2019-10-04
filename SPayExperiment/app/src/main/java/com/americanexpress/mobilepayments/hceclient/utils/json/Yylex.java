/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Boolean
 *  java.lang.Character
 *  java.lang.Double
 *  java.lang.Error
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 */
package com.americanexpress.mobilepayments.hceclient.utils.json;

import com.americanexpress.mobilepayments.hceclient.utils.json.ParseException;
import com.americanexpress.mobilepayments.hceclient.utils.json.Yytoken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
    private static final int ZZ_UNKNOWN_ERROR;
    private StringBuffer sb = new StringBuffer();
    private int yychar;
    private int yycolumn;
    private int yyline;
    private boolean zzAtBOL = true;
    private boolean zzAtEOF;
    private char[] zzBuffer = new char[16384];
    private int zzCurrentPos;
    private int zzEndRead;
    private int zzLexicalState = 0;
    private int zzMarkedPos;
    private Reader zzReader;
    private int zzStartRead;
    private int zzState;

    static {
        ZZ_LEXSTATE = new int[]{0, 0, 1, 1};
        ZZ_CMAP = Yylex.zzUnpackCMap(ZZ_CMAP_PACKED);
        ZZ_ACTION = Yylex.zzUnpackAction();
        ZZ_ROWMAP = Yylex.zzUnpackRowMap();
        ZZ_TRANS = new int[]{2, 2, 3, 4, 2, 2, 2, 5, 2, 6, 2, 2, 7, 8, 2, 9, 2, 2, 2, 2, 2, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, 19, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, 32, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 34, 35, -1, -1, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, 39, -1, 39, -1, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 41, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 42, -1, 42, -1, 42, -1, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, 43, -1, 43, -1, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, -1, 44, -1, 44, -1, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1};
        ZZ_ERROR_MSG = new String[]{"Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large"};
        ZZ_ATTRIBUTE = Yylex.zzUnpackAttribute();
    }

    Yylex(InputStream inputStream) {
        this((Reader)new InputStreamReader(inputStream));
    }

    Yylex(Reader reader) {
        this.zzReader = reader;
    }

    private boolean zzRefill() {
        int n2;
        if (this.zzStartRead > 0) {
            System.arraycopy((Object)this.zzBuffer, (int)this.zzStartRead, (Object)this.zzBuffer, (int)0, (int)(this.zzEndRead - this.zzStartRead));
            this.zzEndRead -= this.zzStartRead;
            this.zzCurrentPos -= this.zzStartRead;
            this.zzMarkedPos -= this.zzStartRead;
            this.zzStartRead = 0;
        }
        if (this.zzCurrentPos >= this.zzBuffer.length) {
            char[] arrc = new char[2 * this.zzCurrentPos];
            System.arraycopy((Object)this.zzBuffer, (int)0, (Object)arrc, (int)0, (int)this.zzBuffer.length);
            this.zzBuffer = arrc;
        }
        if ((n2 = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead)) > 0) {
            this.zzEndRead = n2 + this.zzEndRead;
            return false;
        }
        if (n2 == 0) {
            int n3 = this.zzReader.read();
            if (n3 == -1) {
                return true;
            }
            char[] arrc = this.zzBuffer;
            int n4 = this.zzEndRead;
            this.zzEndRead = n4 + 1;
            arrc[n4] = (char)n3;
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void zzScanError(int n2) {
        String string;
        try {
            string = ZZ_ERROR_MSG[n2];
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            string = ZZ_ERROR_MSG[0];
            throw new Error(string);
        }
        do {
            throw new Error(string);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int zzUnpackAction(String string, int n2, int[] arrn) {
        int n3 = 0;
        int n4 = string.length();
        int n5 = n2;
        block0 : while (n3 < n4) {
            int n6 = n3 + 1;
            int n7 = string.charAt(n3);
            int n8 = n6 + 1;
            char c2 = string.charAt(n6);
            do {
                int n9 = n5 + 1;
                arrn[n5] = c2;
                if (--n7 <= 0) {
                    n5 = n9;
                    n3 = n8;
                    continue block0;
                }
                n5 = n9;
            } while (true);
            break;
        }
        return n5;
    }

    private static int[] zzUnpackAction() {
        int[] arrn = new int[45];
        Yylex.zzUnpackAction(ZZ_ACTION_PACKED_0, 0, arrn);
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int zzUnpackAttribute(String string, int n2, int[] arrn) {
        int n3 = 0;
        int n4 = string.length();
        int n5 = n2;
        block0 : while (n3 < n4) {
            int n6 = n3 + 1;
            int n7 = string.charAt(n3);
            int n8 = n6 + 1;
            char c2 = string.charAt(n6);
            do {
                int n9 = n5 + 1;
                arrn[n5] = c2;
                if (--n7 <= 0) {
                    n5 = n9;
                    n3 = n8;
                    continue block0;
                }
                n5 = n9;
            } while (true);
            break;
        }
        return n5;
    }

    private static int[] zzUnpackAttribute() {
        int[] arrn = new int[45];
        Yylex.zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, 0, arrn);
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static char[] zzUnpackCMap(String string) {
        int n2 = 0;
        char[] arrc = new char[65536];
        int n3 = 0;
        block0 : while (n3 < 90) {
            int n4 = n3 + 1;
            int n5 = string.charAt(n3);
            int n6 = n4 + 1;
            char c2 = string.charAt(n4);
            do {
                int n7 = n2 + 1;
                arrc[n2] = c2;
                if (--n5 <= 0) {
                    n2 = n7;
                    n3 = n6;
                    continue block0;
                }
                n2 = n7;
            } while (true);
            break;
        }
        return arrc;
    }

    private static int zzUnpackRowMap(String string, int n2, int[] arrn) {
        int n3 = 0;
        int n4 = string.length();
        while (n3 < n4) {
            int n5 = n3 + 1;
            int n6 = string.charAt(n3) << 16;
            int n7 = n2 + 1;
            int n8 = n5 + 1;
            arrn[n2] = n6 | string.charAt(n5);
            n2 = n7;
            n3 = n8;
        }
        return n2;
    }

    private static int[] zzUnpackRowMap() {
        int[] arrn = new int[45];
        Yylex.zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, 0, arrn);
        return arrn;
    }

    int getPosition() {
        return this.yychar;
    }

    public final void yybegin(int n2) {
        this.zzLexicalState = n2;
    }

    public final char yycharat(int n2) {
        return this.zzBuffer[n2 + this.zzStartRead];
    }

    public final void yyclose() {
        this.zzAtEOF = true;
        this.zzEndRead = this.zzStartRead;
        if (this.zzReader != null) {
            this.zzReader.close();
        }
    }

    public final int yylength() {
        return this.zzMarkedPos - this.zzStartRead;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Yytoken yylex() {
        int n2 = this.zzEndRead;
        char[] arrc = this.zzBuffer;
        char[] arrc2 = ZZ_CMAP;
        int[] arrn = ZZ_TRANS;
        int[] arrn2 = ZZ_ROWMAP;
        int[] arrn3 = ZZ_ATTRIBUTE;
        block28 : do {
            int n3 = this.zzMarkedPos;
            this.yychar += n3 - this.zzStartRead;
            int n4 = -1;
            this.zzStartRead = n3;
            this.zzCurrentPos = n3;
            this.zzState = ZZ_LEXSTATE[this.zzLexicalState];
            int n5 = n3;
            do {
                int n6;
                int n7;
                block39 : {
                    block35 : {
                        int n8;
                        block38 : {
                            block37 : {
                                int n9;
                                block36 : {
                                    block33 : {
                                        int n10;
                                        block31 : {
                                            int n11;
                                            block34 : {
                                                block32 : {
                                                    block30 : {
                                                        if (n5 >= n2) break block30;
                                                        n7 = n5 + 1;
                                                        n8 = arrc[n5];
                                                        break block31;
                                                    }
                                                    if (!this.zzAtEOF) break block32;
                                                    n8 = -1;
                                                    break block33;
                                                }
                                                this.zzCurrentPos = n5;
                                                this.zzMarkedPos = n3;
                                                boolean bl = this.zzRefill();
                                                n11 = this.zzCurrentPos;
                                                n3 = this.zzMarkedPos;
                                                arrc = this.zzBuffer;
                                                n2 = this.zzEndRead;
                                                if (!bl) break block34;
                                                n8 = -1;
                                                break block33;
                                            }
                                            n7 = n11 + 1;
                                            n8 = arrc[n11];
                                        }
                                        if ((n10 = arrn[arrn2[this.zzState] + arrc2[n8]]) == -1) break block33;
                                        this.zzState = n10;
                                        int n12 = arrn3[this.zzState];
                                        if ((n12 & 1) != 1) break block35;
                                        n9 = this.zzState;
                                        if ((n12 & 8) != 8) break block36;
                                        n4 = n9;
                                        n3 = n7;
                                    }
                                    this.zzMarkedPos = n3;
                                    if (n4 >= 0) break block37;
                                    break block38;
                                }
                                n6 = n9;
                                n3 = n7;
                                break block39;
                            }
                            n4 = ZZ_ACTION[n4];
                        }
                        switch (n4) {
                            case 3: 
                            case 25: 
                            case 26: 
                            case 27: 
                            case 28: 
                            case 29: 
                            case 30: 
                            case 31: 
                            case 32: 
                            case 33: 
                            case 34: 
                            case 35: 
                            case 36: 
                            case 37: 
                            case 38: 
                            case 39: 
                            case 40: 
                            case 41: 
                            case 42: 
                            case 43: 
                            case 44: 
                            case 45: 
                            case 46: 
                            case 47: 
                            case 48: {
                                continue block28;
                            }
                            default: {
                                if (n8 != -1 || this.zzStartRead != this.zzCurrentPos) break;
                                this.zzAtEOF = true;
                                return null;
                            }
                            case 11: {
                                this.sb.append(this.yytext());
                                continue block28;
                            }
                            case 4: {
                                this.sb = null;
                                this.sb = new StringBuffer();
                                this.yybegin(2);
                                continue block28;
                            }
                            case 16: {
                                this.sb.append('\b');
                                continue block28;
                            }
                            case 6: {
                                return new Yytoken(2, null);
                            }
                            case 23: {
                                return new Yytoken(0, (Object)Boolean.valueOf((String)this.yytext()));
                            }
                            case 22: {
                                return new Yytoken(0, null);
                            }
                            case 13: {
                                this.yybegin(0);
                                return new Yytoken(0, this.sb.toString());
                            }
                            case 12: {
                                this.sb.append('\\');
                                continue block28;
                            }
                            case 21: {
                                return new Yytoken(0, (Object)Double.valueOf((String)this.yytext()));
                            }
                            case 1: {
                                throw new ParseException(this.yychar, 0, (Object)new Character(this.yycharat(0)));
                            }
                            case 8: {
                                return new Yytoken(4, null);
                            }
                            case 19: {
                                this.sb.append('\r');
                                continue block28;
                            }
                            case 15: {
                                this.sb.append('/');
                                continue block28;
                            }
                            case 10: {
                                return new Yytoken(6, null);
                            }
                            case 14: {
                                this.sb.append('\"');
                                continue block28;
                            }
                            case 5: {
                                return new Yytoken(1, null);
                            }
                            case 17: {
                                this.sb.append('\f');
                                continue block28;
                            }
                            case 24: {
                                try {
                                    int n13 = Integer.parseInt((String)this.yytext().substring(2), (int)16);
                                    this.sb.append((char)n13);
                                    continue block28;
                                }
                                catch (Exception exception) {
                                    throw new ParseException(this.yychar, 2, (Object)exception);
                                }
                            }
                            case 20: {
                                this.sb.append('\t');
                                continue block28;
                            }
                            case 7: {
                                return new Yytoken(3, null);
                            }
                            case 2: {
                                return new Yytoken(0, (Object)Long.valueOf((String)this.yytext()));
                            }
                            case 18: {
                                this.sb.append('\n');
                                continue block28;
                            }
                            case 9: {
                                return new Yytoken(5, null);
                            }
                        }
                        this.zzScanError(1);
                        continue block28;
                    }
                    n6 = n4;
                }
                n4 = n6;
                n5 = n7;
            } while (true);
            break;
        } while (true);
    }

    public void yypushback(int n2) {
        if (n2 > this.yylength()) {
            this.zzScanError(2);
        }
        this.zzMarkedPos -= n2;
    }

    public final void yyreset(Reader reader) {
        this.zzReader = reader;
        this.zzAtBOL = true;
        this.zzAtEOF = false;
        this.zzStartRead = 0;
        this.zzEndRead = 0;
        this.zzMarkedPos = 0;
        this.zzCurrentPos = 0;
        this.yycolumn = 0;
        this.yychar = 0;
        this.yyline = 0;
        this.zzLexicalState = 0;
    }

    public final int yystate() {
        return this.zzLexicalState;
    }

    public final String yytext() {
        return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
    }
}

