package android.media;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

/* compiled from: ClosedCaptionRenderer */
class MutableBackgroundColorSpan extends CharacterStyle implements UpdateAppearance {
    private int mColor;

    public MutableBackgroundColorSpan(int color) {
        this.mColor = color;
    }

    public void setBackgroundColor(int color) {
        this.mColor = color;
    }

    public int getBackgroundColor() {
        return this.mColor;
    }

    public void updateDrawState(TextPaint ds) {
        ds.bgColor = this.mColor;
    }
}
