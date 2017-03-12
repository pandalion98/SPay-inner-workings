package android.app;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager.LayoutParams;

public class InternalPresentation extends Presentation {
    public InternalPresentation(Context outerContext, Display display) {
        this(outerContext, display, 0);
        Log.i("InternalPresentation", "Creating InternalPresentation 1");
    }

    public InternalPresentation(Context outerContext, Display display, int theme) {
        super(outerContext, display, theme);
        Log.i("InternalPresentation", "Creating InternalPresentation 2");
        setInternalPresentationFlag();
    }

    private void setInternalPresentationFlag() {
        Log.i("InternalPresentation", "setInternalPresentationFlag");
        if (this.mWindow != null) {
            LayoutParams lp = this.mWindow.getAttributes();
            lp.samsungFlags |= Integer.MIN_VALUE;
            this.mWindow.setAttributes(lp);
        }
    }
}
