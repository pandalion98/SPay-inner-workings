/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package com.americanexpress.mobilepayments.hceclient.utils.json;

public class Yytoken {
    public static final int TYPE_COLON = 6;
    public static final int TYPE_COMMA = 5;
    public static final int TYPE_EOF = -1;
    public static final int TYPE_LEFT_BRACE = 1;
    public static final int TYPE_LEFT_SQUARE = 3;
    public static final int TYPE_RIGHT_BRACE = 2;
    public static final int TYPE_RIGHT_SQUARE = 4;
    public static final int TYPE_VALUE;
    public int type = 0;
    public Object value = null;

    public Yytoken(int n2, Object object) {
        this.type = n2;
        this.value = object;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.type) {
            case 0: {
                stringBuffer.append("VALUE(").append(this.value).append(")");
                return stringBuffer.toString();
            }
            case 1: {
                stringBuffer.append("LEFT BRACE({)");
                return stringBuffer.toString();
            }
            case 2: {
                stringBuffer.append("RIGHT BRACE(})");
                return stringBuffer.toString();
            }
            case 3: {
                stringBuffer.append("LEFT SQUARE([)");
                return stringBuffer.toString();
            }
            case 4: {
                stringBuffer.append("RIGHT SQUARE(])");
                return stringBuffer.toString();
            }
            case 5: {
                stringBuffer.append("COMMA(,)");
                return stringBuffer.toString();
            }
            case 6: {
                stringBuffer.append("COLON(:)");
                return stringBuffer.toString();
            }
            case -1: {
                stringBuffer.append("END OF FILE");
                break;
            }
        }
        return stringBuffer.toString();
    }
}

