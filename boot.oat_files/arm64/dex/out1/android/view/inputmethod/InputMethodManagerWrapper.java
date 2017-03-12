package android.view.inputmethod;

import android.view.View;

public final class InputMethodManagerWrapper {
    private static InputMethodManagerWrapper sInstance;

    public static InputMethodManagerWrapper getInstance() {
        InputMethodManagerWrapper inputMethodManagerWrapper;
        synchronized (InputMethodManagerWrapper.class) {
            if (sInstance == null) {
                sInstance = new InputMethodManagerWrapper();
            }
            inputMethodManagerWrapper = sInstance;
        }
        return inputMethodManagerWrapper;
    }

    public View getServedView() {
        InputMethodManager imm = InputMethodManager.peekInstance();
        return imm == null ? null : imm.getServedView();
    }

    public InputConnection getServedInputConnection() {
        InputMethodManager imm = InputMethodManager.peekInstance();
        return imm == null ? null : imm.getServedInputConnection();
    }
}
