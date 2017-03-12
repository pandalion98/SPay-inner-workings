package com.android.internal.widget;

import com.android.internal.R;

public class Smileys {
    public static int ANGEL = 11;
    public static int COOL = 7;
    public static int CRYING = 13;
    public static int EMBARRASSED = 10;
    public static int FOOT_IN_MOUTH = 9;
    public static int HAPPY = 0;
    public static int KISSING = 5;
    public static int LAUGHING = 15;
    public static int LIPS_ARE_SEALED = 14;
    public static int MONEY_MOUTH = 8;
    public static int SAD = 1;
    public static int SURPRISED = 4;
    public static int TONGUE_STICKING_OUT = 3;
    public static int UNDECIDED = 12;
    public static int WINKING = 2;
    public static int WTF = 16;
    public static int YELLING = 6;
    private static final int[] sIconIds = new int[]{R.drawable.emo_im_happy, R.drawable.emo_im_sad, R.drawable.emo_im_winking, R.drawable.emo_im_tongue_sticking_out, R.drawable.emo_im_surprised, R.drawable.emo_im_kissing, R.drawable.emo_im_yelling, R.drawable.emo_im_cool, R.drawable.emo_im_money_mouth, R.drawable.emo_im_foot_in_mouth, R.drawable.emo_im_embarrassed, R.drawable.emo_im_angel, R.drawable.emo_im_undecided, R.drawable.emo_im_crying, R.drawable.emo_im_lips_are_sealed, R.drawable.emo_im_laughing, R.drawable.emo_im_wtf};

    public static int getSmileyResource(int which) {
        return sIconIds[which];
    }
}
