package android.inputmethodservice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ExtractEditLayout extends LinearLayout {
    Button mExtractActionButton;
    private InputMethodService mIME;

    public ExtractEditLayout(Context context) {
        super(context);
    }

    public ExtractEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mExtractActionButton = (Button) findViewById(16909275);
    }

    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this.mIME != null && visibility == 8) {
            this.mIME.updateFullscreenMode();
        }
    }

    public void setIME(InputMethodService ime) {
        this.mIME = ime;
    }
}
