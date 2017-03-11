package com.americanexpress.mobilepayments.hceclient.utils.json;

import org.bouncycastle.math.ec.ECCurve;

public class Yytoken {
    public static final int TYPE_COLON = 6;
    public static final int TYPE_COMMA = 5;
    public static final int TYPE_EOF = -1;
    public static final int TYPE_LEFT_BRACE = 1;
    public static final int TYPE_LEFT_SQUARE = 3;
    public static final int TYPE_RIGHT_BRACE = 2;
    public static final int TYPE_RIGHT_SQUARE = 4;
    public static final int TYPE_VALUE = 0;
    public int type;
    public Object value;

    public Yytoken(int i, Object obj) {
        this.type = 0;
        this.value = null;
        this.type = i;
        this.value = obj;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.type) {
            case TYPE_EOF /*-1*/:
                stringBuffer.append("END OF FILE");
                break;
            case ECCurve.COORD_AFFINE /*0*/:
                stringBuffer.append("VALUE(").append(this.value).append(")");
                break;
            case TYPE_LEFT_BRACE /*1*/:
                stringBuffer.append("LEFT BRACE({)");
                break;
            case TYPE_RIGHT_BRACE /*2*/:
                stringBuffer.append("RIGHT BRACE(})");
                break;
            case TYPE_LEFT_SQUARE /*3*/:
                stringBuffer.append("LEFT SQUARE([)");
                break;
            case TYPE_RIGHT_SQUARE /*4*/:
                stringBuffer.append("RIGHT SQUARE(])");
                break;
            case TYPE_COMMA /*5*/:
                stringBuffer.append("COMMA(,)");
                break;
            case TYPE_COLON /*6*/:
                stringBuffer.append("COLON(:)");
                break;
        }
        return stringBuffer.toString();
    }
}
