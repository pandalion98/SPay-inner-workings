package android.text.style;

import android.text.TextPaint;
import android.view.View;

public abstract class HoverableSpan extends CharacterStyle implements UpdateAppearance {
    public abstract void onHoverEnter(View view);

    public abstract void onHoverExit(View view);

    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(true);
    }
}
