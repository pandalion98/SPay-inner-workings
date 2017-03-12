package android.hardware.input;

import android.hardware.display.DisplayViewport;
import android.view.InputEvent;
import java.util.ArrayList;

public abstract class InputManagerInternal {
    public abstract boolean injectInputEvent(InputEvent inputEvent, int i, int i2);

    public abstract void setDisplayViewports(DisplayViewport displayViewport, DisplayViewport displayViewport2);

    public abstract void setDisplayViewports(ArrayList<DisplayViewport> arrayList, DisplayViewport displayViewport);

    public abstract void setInteractive(boolean z);

    public abstract void setMouseControlType(int i);

    public abstract void setMouseCursorVisibility(boolean z);

    public abstract void setSubInteractive(boolean z);

    public abstract void setTspLpmMode(int i, boolean z);
}
