package com.samsung.android.sdk.look;

import android.content.Context;
import com.android.internal.R;

public class SlookResourceManager {
    public static final int AIR_BUTTON_WARN_NO_CONTACT_HISTORY = 0;
    public static final int AIR_BUTTON_WARN_NO_IMAGES = 1;
    public static final int CLIPED_TEXT_MAX_COUNT = 5;
    public static final int DRAWABLE_AUDIO = 2;
    public static final int DRAWABLE_CONTACT = 1;
    public static final int FREQUENT_CONTACT_MAX_COUNT = 2;
    public static final int RECENT_MEDIA_MAX_COUNT = 3;
    public static final int RECENT_SNOTE_MAX_COUNT = 4;

    public static int getDrawableId(int id) {
        switch (id) {
            case 1:
                return R.drawable.airbutton_default_contact;
            case 2:
                return R.drawable.airbutton_default_audio;
            default:
                throw new IllegalArgumentException("id(" + id + ") was wrong.");
        }
    }

    public static int getInt(int id) {
        switch (id) {
            case 2:
            case 3:
            case 4:
            case 5:
                return 15;
            default:
                throw new IllegalArgumentException("id(" + id + ") was wrong.");
        }
    }

    public static CharSequence getText(Context context, int id) {
        switch (id) {
            case 0:
                return context.getResources().getText(R.string.air_button_warn_no_contact_history);
            case 1:
                return context.getResources().getText(R.string.air_button_warn_no_images);
            default:
                return null;
        }
    }
}
