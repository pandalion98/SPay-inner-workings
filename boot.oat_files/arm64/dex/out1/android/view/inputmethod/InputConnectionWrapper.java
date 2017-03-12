package android.view.inputmethod;

import android.os.Bundle;
import android.view.KeyEvent;

public class InputConnectionWrapper implements InputConnection {
    final boolean mMutable;
    private InputConnection mTarget;

    public InputConnectionWrapper(InputConnection target, boolean mutable) {
        this.mMutable = mutable;
        this.mTarget = target;
    }

    public void setTarget(InputConnection target) {
        if (this.mTarget == null || this.mMutable) {
            this.mTarget = target;
            return;
        }
        throw new SecurityException("not mutable");
    }

    public CharSequence getTextBeforeCursor(int n, int flags) {
        return this.mTarget.getTextBeforeCursor(n, flags);
    }

    public CharSequence getTextAfterCursor(int n, int flags) {
        return this.mTarget.getTextAfterCursor(n, flags);
    }

    public CharSequence getSelectedText(int flags) {
        return this.mTarget.getSelectedText(flags);
    }

    public int getCursorCapsMode(int reqModes) {
        return this.mTarget.getCursorCapsMode(reqModes);
    }

    public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
        return this.mTarget.getExtractedText(request, flags);
    }

    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        return this.mTarget.deleteSurroundingText(beforeLength, afterLength);
    }

    public boolean setComposingText(CharSequence text, int newCursorPosition) {
        return this.mTarget.setComposingText(text, newCursorPosition);
    }

    public boolean setComposingRegion(int start, int end) {
        return this.mTarget.setComposingRegion(start, end);
    }

    public boolean finishComposingText() {
        return this.mTarget.finishComposingText();
    }

    public boolean commitText(CharSequence text, int newCursorPosition) {
        return this.mTarget.commitText(text, newCursorPosition);
    }

    public boolean commitCompletion(CompletionInfo text) {
        return this.mTarget.commitCompletion(text);
    }

    public boolean commitCorrection(CorrectionInfo correctionInfo) {
        return this.mTarget.commitCorrection(correctionInfo);
    }

    public boolean setSelection(int start, int end) {
        return this.mTarget.setSelection(start, end);
    }

    public boolean performEditorAction(int editorAction) {
        return this.mTarget.performEditorAction(editorAction);
    }

    public boolean performContextMenuAction(int id) {
        return this.mTarget.performContextMenuAction(id);
    }

    public boolean beginBatchEdit() {
        return this.mTarget.beginBatchEdit();
    }

    public boolean endBatchEdit() {
        return this.mTarget.endBatchEdit();
    }

    public boolean sendKeyEvent(KeyEvent event) {
        return this.mTarget.sendKeyEvent(event);
    }

    public boolean clearMetaKeyStates(int states) {
        return this.mTarget.clearMetaKeyStates(states);
    }

    public boolean reportFullscreenMode(boolean enabled) {
        return this.mTarget.reportFullscreenMode(enabled);
    }

    public boolean performPrivateCommand(String action, Bundle data) {
        return this.mTarget.performPrivateCommand(action, data);
    }

    public boolean requestCursorUpdates(int cursorUpdateMode) {
        return this.mTarget.requestCursorUpdates(cursorUpdateMode);
    }
}
