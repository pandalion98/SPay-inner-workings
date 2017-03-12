package android.view.inputmethod;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewRootImpl;
import com.android.internal.R;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import java.util.Locale;

public class BaseInputConnection implements InputConnection {
    static final Object COMPOSING = new ComposingText();
    private static final boolean DEBUG = false;
    private static final String TAG = "BaseInputConnection";
    private Object[] mDefaultComposingSpans;
    final boolean mDummyMode;
    Editable mEditable;
    protected final InputMethodManager mIMM;
    KeyCharacterMap mKeyCharacterMap;
    final View mTargetView;

    BaseInputConnection(InputMethodManager mgr, boolean fullEditor) {
        this.mIMM = mgr;
        this.mTargetView = null;
        this.mDummyMode = !fullEditor;
    }

    public BaseInputConnection(View targetView, boolean fullEditor) {
        this.mIMM = (InputMethodManager) targetView.getContext().getSystemService("input_method");
        this.mTargetView = targetView;
        this.mDummyMode = !fullEditor;
    }

    public static final void removeComposingSpans(Spannable text) {
        text.removeSpan(COMPOSING);
        Object[] sps = text.getSpans(0, text.length(), Object.class);
        if (sps != null) {
            for (int i = sps.length - 1; i >= 0; i--) {
                Object o = sps[i];
                if ((text.getSpanFlags(o) & 256) != 0) {
                    text.removeSpan(o);
                }
            }
        }
    }

    public static void setComposingSpans(Spannable text) {
        setComposingSpans(text, 0, text.length());
    }

    public static void setComposingSpans(Spannable text, int start, int end) {
        Object[] sps = text.getSpans(start, end, Object.class);
        if (sps != null) {
            for (int i = sps.length - 1; i >= 0; i--) {
                Object o = sps[i];
                if (o == COMPOSING) {
                    text.removeSpan(o);
                } else {
                    int fl = text.getSpanFlags(o);
                    if ((fl & 307) != R.styleable.Theme_panelMenuIsCompact) {
                        text.setSpan(o, text.getSpanStart(o), text.getSpanEnd(o), ((fl & -52) | 256) | 33);
                    }
                }
            }
        }
        text.setSpan(COMPOSING, start, end, R.styleable.Theme_panelMenuIsCompact);
    }

    public static int getComposingSpanStart(Spannable text) {
        return text.getSpanStart(COMPOSING);
    }

    public static int getComposingSpanEnd(Spannable text) {
        return text.getSpanEnd(COMPOSING);
    }

    public Editable getEditable() {
        if (this.mEditable == null) {
            this.mEditable = Factory.getInstance().newEditable("");
            Selection.setSelection(this.mEditable, 0);
        }
        return this.mEditable;
    }

    public boolean beginBatchEdit() {
        return false;
    }

    public boolean endBatchEdit() {
        return false;
    }

    protected void reportFinish() {
    }

    public boolean clearMetaKeyStates(int states) {
        Editable content = getEditable();
        if (content == null) {
            return false;
        }
        MetaKeyKeyListener.clearMetaKeyState(content, states);
        return true;
    }

    public boolean commitCompletion(CompletionInfo text) {
        return false;
    }

    public boolean commitCorrection(CorrectionInfo correctionInfo) {
        return false;
    }

    public boolean commitText(CharSequence text, int newCursorPosition) {
        replaceText(text, newCursorPosition, false);
        this.mIMM.notifyUserAction();
        sendCurrentText();
        return true;
    }

    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        Editable content = getEditable();
        if (content == null) {
            return false;
        }
        beginBatchEdit();
        int a = Selection.getSelectionStart(content);
        int b = Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        int ca = getComposingSpanStart(content);
        int cb = getComposingSpanEnd(content);
        if (cb < ca) {
            tmp = ca;
            ca = cb;
            cb = tmp;
        }
        if (!(ca == -1 || cb == -1)) {
            if (ca < a) {
                a = ca;
            }
            if (cb > b) {
                b = cb;
            }
        }
        int deleted = 0;
        if (beforeLength > 0) {
            int start = a - beforeLength;
            if (start < 0) {
                start = 0;
            }
            content.delete(start, a);
            deleted = a - start;
        }
        if (afterLength > 0) {
            b -= deleted;
            int end = b + afterLength;
            if (end > content.length()) {
                end = content.length();
            }
            content.delete(b, end);
        }
        endBatchEdit();
        return true;
    }

    public boolean finishComposingText() {
        Editable content = getEditable();
        if (content != null) {
            beginBatchEdit();
            removeComposingSpans(content);
            sendCurrentText();
            endBatchEdit();
        }
        return true;
    }

    public int getCursorCapsMode(int reqModes) {
        if (this.mDummyMode) {
            return 0;
        }
        Editable content = getEditable();
        if (content == null) {
            return 0;
        }
        int a = Selection.getSelectionStart(content);
        int b = Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return TextUtils.getCapsMode(content, a, reqModes);
    }

    public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
        return null;
    }

    public CharSequence getTextBeforeCursor(int length, int flags) {
        Editable content = getEditable();
        if (content == null) {
            return null;
        }
        int a = Selection.getSelectionStart(content);
        int b = Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if (a <= 0) {
            return "";
        }
        if (length > a) {
            length = a;
        }
        if ((flags & 1) != 0) {
            return content.subSequence(a - length, a);
        }
        return TextUtils.substring(content, a - length, a);
    }

    public CharSequence getSelectedText(int flags) {
        Editable content = getEditable();
        if (content == null) {
            return null;
        }
        int a = Selection.getSelectionStart(content);
        int b = Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if (a == b) {
            return null;
        }
        if ((flags & 1) != 0) {
            return content.subSequence(a, b);
        }
        return TextUtils.substring(content, a, b);
    }

    public CharSequence getTextAfterCursor(int length, int flags) {
        Editable content = getEditable();
        if (content == null) {
            return null;
        }
        int a = Selection.getSelectionStart(content);
        int b = Selection.getSelectionEnd(content);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if (b < 0) {
            b = 0;
        }
        if (b + length > content.length()) {
            length = content.length() - b;
        }
        if ((flags & 1) != 0) {
            return content.subSequence(b, b + length);
        }
        return TextUtils.substring(content, b, b + length);
    }

    public boolean performEditorAction(int actionCode) {
        long eventTime = SystemClock.uptimeMillis();
        sendKeyEvent(new KeyEvent(eventTime, eventTime, 0, 66, 0, 0, -1, 0, 22));
        sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), eventTime, 1, 66, 0, 0, -1, 0, 22));
        return true;
    }

    public boolean performContextMenuAction(int id) {
        return false;
    }

    public boolean performPrivateCommand(String action, Bundle data) {
        return false;
    }

    public boolean requestCursorUpdates(int cursorUpdateMode) {
        return false;
    }

    public boolean setComposingText(CharSequence text, int newCursorPosition) {
        replaceText(text, newCursorPosition, true);
        this.mIMM.notifyUserAction();
        return true;
    }

    public boolean setComposingRegion(int start, int end) {
        Editable content = getEditable();
        if (content != null) {
            beginBatchEdit();
            removeComposingSpans(content);
            int a = start;
            int b = end;
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            int length = content.length();
            if (a < 0) {
                a = 0;
            }
            if (b < 0) {
                b = 0;
            }
            if (a > length) {
                a = length;
            }
            if (b > length) {
                b = length;
            }
            ensureDefaultComposingSpans();
            if (this.mDefaultComposingSpans != null) {
                for (Object span : this.mDefaultComposingSpans) {
                    content.setSpan(span, a, b, R.styleable.Theme_panelMenuIsCompact);
                }
            }
            content.setSpan(COMPOSING, a, b, R.styleable.Theme_panelMenuIsCompact);
            sendCurrentText();
            endBatchEdit();
        }
        return true;
    }

    public boolean setSelection(int start, int end) {
        CharSequence content = getEditable();
        if (content == null) {
            return false;
        }
        int len = content.length();
        if (start > len || end > len || start < 0 || end < 0) {
            return true;
        }
        if (start != end || MetaKeyKeyListener.getMetaState(content, 2048) == 0) {
            Selection.setSelection(content, start, end);
            return true;
        }
        Selection.extendSelection(content, start);
        return true;
    }

    public boolean sendKeyEvent(KeyEvent event) {
        synchronized (this.mIMM.mH) {
            ViewRootImpl viewRootImpl = this.mTargetView != null ? this.mTargetView.getViewRootImpl() : null;
            if (viewRootImpl == null && this.mIMM.mServedView != null) {
                viewRootImpl = this.mIMM.mServedView.getViewRootImpl();
            }
            if (viewRootImpl != null) {
                viewRootImpl.dispatchKeyFromIme(event);
            }
        }
        this.mIMM.notifyUserAction();
        return false;
    }

    public boolean reportFullscreenMode(boolean enabled) {
        this.mIMM.setFullscreenMode(enabled);
        return true;
    }

    private void sendCurrentText() {
        if (this.mDummyMode) {
            Editable content = getEditable();
            if (content != null) {
                int N = content.length();
                if (N != 0) {
                    if (N == 1) {
                        if (this.mKeyCharacterMap == null) {
                            this.mKeyCharacterMap = KeyCharacterMap.load(-1);
                        }
                        char[] chars = new char[1];
                        content.getChars(0, 1, chars, 0);
                        KeyEvent[] events = this.mKeyCharacterMap.getEvents(chars);
                        if (events != null) {
                            for (KeyEvent sendKeyEvent : events) {
                                sendKeyEvent(sendKeyEvent);
                            }
                            content.clear();
                            return;
                        }
                    }
                    sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), content.toString(), -1, 0));
                    content.clear();
                }
            }
        }
    }

    private void ensureDefaultComposingSpans() {
        if (this.mDefaultComposingSpans == null) {
            Context context;
            if (this.mTargetView != null) {
                context = this.mTargetView.getContext();
            } else if (this.mIMM.mServedView != null) {
                context = this.mIMM.mServedView.getContext();
            } else {
                context = null;
            }
            if (context != null) {
                TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{R.attr.candidatesTextStyleSpans});
                CharSequence style = ta.getText(0);
                ta.recycle();
                if (style != null && (style instanceof Spanned)) {
                    this.mDefaultComposingSpans = ((Spanned) style).getSpans(0, style.length(), Object.class);
                }
            }
        }
    }

    private void replaceText(CharSequence text, int newCursorPosition, boolean composing) {
        Editable content = getEditable();
        if (content != null && text != null) {
            int tmp;
            beginBatchEdit();
            int a = getComposingSpanStart(content);
            int b = getComposingSpanEnd(content);
            if (b < a) {
                tmp = a;
                a = b;
                b = tmp;
            }
            if (a == -1 || b == -1) {
                a = Selection.getSelectionStart(content);
                b = Selection.getSelectionEnd(content);
                if (a < 0) {
                    a = 0;
                }
                if (b < 0) {
                    b = 0;
                }
                if (b < a) {
                    tmp = a;
                    a = b;
                    b = tmp;
                }
            } else {
                removeComposingSpans(content);
            }
            if (composing) {
                Spannable sp;
                if (text instanceof Spannable) {
                    sp = (Spannable) text;
                } else {
                    sp = new SpannableStringBuilder(text);
                    text = sp;
                    ensureDefaultComposingSpans();
                    if (this.mDefaultComposingSpans != null) {
                        for (Object span : this.mDefaultComposingSpans) {
                            sp.setSpan(span, 0, sp.length(), R.styleable.Theme_panelMenuIsCompact);
                        }
                    }
                }
                setComposingSpans(sp);
            }
            if (newCursorPosition > 0) {
                newCursorPosition += b - 1;
            } else {
                newCursorPosition += a;
            }
            if (newCursorPosition < 0) {
                newCursorPosition = 0;
            }
            if (newCursorPosition > content.length()) {
                newCursorPosition = content.length();
            }
            Selection.setSelection(content, newCursorPosition);
            if (isAllBracketChars(text) && isRTLText(content.toString(), newCursorPosition)) {
                text = convertAllBrackets(text);
            }
            content.replace(a, b, text);
            endBatchEdit();
        }
    }

    private boolean isRTLText(String sCurStr, int newCursorPosition) {
        int nDirection = -1;
        int nLastEnterPos = -1;
        if (sCurStr != null && sCurStr.length() > 0) {
            char ch;
            byte directionality;
            if (newCursorPosition != 0) {
                nLastEnterPos = sCurStr.lastIndexOf(10, newCursorPosition - 1);
            }
            if (nLastEnterPos < 0) {
                nLastEnterPos = 0;
            } else {
                nLastEnterPos++;
            }
            int nIndex = nLastEnterPos;
            while (nIndex < sCurStr.length()) {
                ch = sCurStr.charAt(nIndex);
                if (ch == '\n') {
                    break;
                }
                directionality = Character.getDirectionality(ch);
                if (directionality == (byte) 0 || directionality == (byte) 14 || directionality == (byte) 15) {
                    nDirection = 0;
                    break;
                } else if (directionality == (byte) 1 || directionality == (byte) 2 || directionality == ISensorHubCmdProtocol.TYPE_PUT_DOWN_MOTION_SERVICE || directionality == (byte) 17) {
                    nDirection = 1;
                    break;
                } else {
                    nIndex++;
                }
            }
            for (nIndex = newCursorPosition; nIndex < sCurStr.length(); nIndex++) {
                ch = sCurStr.charAt(nIndex);
                if (ch == '\n') {
                    break;
                }
                directionality = Character.getDirectionality(ch);
                if (directionality == (byte) 0 || directionality == (byte) 14 || directionality == (byte) 15 || directionality == (byte) 1 || directionality == (byte) 2 || directionality == ISensorHubCmdProtocol.TYPE_PUT_DOWN_MOTION_SERVICE || directionality == (byte) 17 || directionality == (byte) 3) {
                    nDirection = -1;
                    break;
                }
            }
            if (nDirection == -1) {
                nIndex = newCursorPosition - 1;
                while (nIndex >= 0) {
                    ch = sCurStr.charAt(nIndex);
                    if (ch == '\n') {
                        break;
                    }
                    directionality = Character.getDirectionality(ch);
                    if (directionality == (byte) 0 || directionality == (byte) 14 || directionality == (byte) 15) {
                        nDirection = 0;
                        break;
                    } else if (directionality == (byte) 1 || directionality == (byte) 2 || directionality == ISensorHubCmdProtocol.TYPE_PUT_DOWN_MOTION_SERVICE || directionality == (byte) 17) {
                        nDirection = 1;
                        break;
                    } else {
                        nIndex--;
                    }
                }
                if (nDirection == -1) {
                    nIndex = newCursorPosition;
                    while (nIndex < sCurStr.length()) {
                        ch = sCurStr.charAt(nIndex);
                        if (ch == '\n') {
                            break;
                        }
                        directionality = Character.getDirectionality(ch);
                        if (directionality == (byte) 0 || directionality == (byte) 14 || directionality == (byte) 15) {
                            nDirection = 0;
                            break;
                        } else if (directionality == (byte) 1 || directionality == (byte) 2 || directionality == ISensorHubCmdProtocol.TYPE_PUT_DOWN_MOTION_SERVICE || directionality == (byte) 17) {
                            nDirection = 1;
                            break;
                        } else {
                            nIndex++;
                        }
                    }
                }
                if (nDirection == -1 && this.mTargetView != null && this.mTargetView.getLayoutDirection() == 1 && isRtlLanguage()) {
                    nDirection = 1;
                }
            }
        } else if (isRtlLanguage() && ((sCurStr == null || sCurStr.length() == 0) && this.mTargetView != null && this.mTargetView.getLayoutDirection() == 1)) {
            return true;
        }
        if (nDirection == 1) {
            return true;
        }
        return false;
    }

    private boolean isAllBracketChars(CharSequence text) {
        String BRACKET = "<>{}[]()«»《》";
        String sText = text.toString();
        int nIndex = 0;
        while (sText != null && nIndex < sText.length()) {
            if (!"<>{}[]()«»《》".contains(Character.valueOf(sText.charAt(nIndex)).toString())) {
                return false;
            }
            nIndex++;
        }
        return true;
    }

    private String convertAllBrackets(CharSequence text) {
        String sText = text.toString();
        int nIndex = 0;
        while (sText != null && nIndex < sText.length()) {
            sText = sText.substring(0, nIndex) + convertBracket(Character.valueOf(sText.charAt(nIndex)).toString()) + sText.substring(nIndex + 1);
            nIndex++;
        }
        return sText;
    }

    private String convertBracket(CharSequence text) {
        String sText = text.toString();
        if ("{".equals(sText)) {
            return "}";
        }
        if ("}".equals(sText)) {
            return "{";
        }
        if ("[".equals(sText)) {
            return "]";
        }
        if ("]".equals(sText)) {
            return "[";
        }
        if ("<".equals(sText)) {
            return ">";
        }
        if (">".equals(sText)) {
            return "<";
        }
        if ("(".equals(sText)) {
            return ")";
        }
        if (")".equals(sText)) {
            return "(";
        }
        if (String.valueOf('«').equals(sText)) {
            return String.valueOf('»');
        }
        if (String.valueOf('»').equals(sText)) {
            return String.valueOf('«');
        }
        if (String.valueOf('《').equals(sText)) {
            return String.valueOf('》');
        }
        if (String.valueOf('》').equals(sText)) {
            return String.valueOf('《');
        }
        return sText;
    }

    private boolean isRtlLanguage() {
        String curLanguage = Locale.getDefault().getLanguage();
        if ("ar".equals(curLanguage) || "fa".equals(curLanguage) || "ur".equals(curLanguage) || "iw".equals(curLanguage)) {
            return true;
        }
        return false;
    }
}
