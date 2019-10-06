package com.sec.android.app.hwmoduletest;

import android.graphics.Paint;

/* compiled from: TspPatternStyleX */
enum TOOLTYPE {
    TOOLTYPE_NONE("none", -16777216),
    TOOLTYPE_FINGER_TOUCH("finger touch", -65536),
    TOOLTYPE_FINGER_HOVER("finger hover", -16777216),
    TOOLTYPE_GLOVE_TOUCH("glove touch", -16776961);
    
    private static TOOLTYPE mType;
    private int mColor;
    private String mName;

    static {
        mType = TOOLTYPE_NONE;
    }

    private TOOLTYPE(String name, int color) {
        this.mName = name;
        this.mColor = color;
    }

    public static void setToolType(TOOLTYPE type) {
        mType = type;
    }

    public static void setToolType(TOOLTYPE type, Paint p) {
        mType = type;
        if (p != null) {
            p.setColor(mType.mColor);
        }
    }

    public static int getToolTypeColor() {
        return mType.mColor;
    }

    public static boolean getIsHoverToolType() {
        switch (mType) {
            case TOOLTYPE_FINGER_HOVER:
                return true;
            case TOOLTYPE_FINGER_TOUCH:
            case TOOLTYPE_GLOVE_TOUCH:
            case TOOLTYPE_NONE:
                return false;
            default:
                return false;
        }
    }

    public String toString() {
        return this.mName;
    }
}
