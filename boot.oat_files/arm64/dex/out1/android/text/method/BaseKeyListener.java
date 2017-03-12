package android.text.method;

import android.text.Editable;
import android.text.Layout;
import android.text.NoCopySpan.Concrete;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.TextKeyListener.Capitalize;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public abstract class BaseKeyListener extends MetaKeyKeyListener implements KeyListener {
    static final Object OLD_SEL_START = new Concrete();

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$text$method$TextKeyListener$Capitalize = new int[Capitalize.values().length];

        static {
            try {
                $SwitchMap$android$text$method$TextKeyListener$Capitalize[Capitalize.CHARACTERS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$text$method$TextKeyListener$Capitalize[Capitalize.WORDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$text$method$TextKeyListener$Capitalize[Capitalize.SENTENCES.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public boolean backspace(View view, Editable content, int keyCode, KeyEvent event) {
        return backspaceOrForwardDelete(view, content, keyCode, event, false);
    }

    public boolean forwardDelete(View view, Editable content, int keyCode, KeyEvent event) {
        return backspaceOrForwardDelete(view, content, keyCode, event, true);
    }

    private boolean backspaceOrForwardDelete(View view, Editable content, int keyCode, KeyEvent event, boolean isForwardDelete) {
        if (!KeyEvent.metaStateHasNoModifiers(event.getMetaState() & -28916)) {
            return false;
        }
        if (deleteSelection(view, content)) {
            return true;
        }
        boolean isCtrlActive;
        boolean isAltActive;
        if ((event.getMetaState() & 4096) != 0) {
            isCtrlActive = true;
        } else {
            isCtrlActive = false;
        }
        boolean isShiftActive;
        if (MetaKeyKeyListener.getMetaState(content, 1, event) == 1) {
            isShiftActive = true;
        } else {
            isShiftActive = false;
        }
        if (MetaKeyKeyListener.getMetaState(content, 2, event) == 1) {
            isAltActive = true;
        } else {
            isAltActive = false;
        }
        if (isCtrlActive) {
            if (isAltActive || isShiftActive) {
                return false;
            }
            return deleteUntilWordBoundary(view, content, isForwardDelete);
        } else if (isAltActive && deleteLine(view, content)) {
            return true;
        } else {
            int end;
            int start = Selection.getSelectionEnd(content);
            if (isForwardDelete) {
                end = TextUtils.getOffsetAfter(content, start);
            } else {
                end = TextUtils.getOffsetBefore(content, start);
            }
            if (start == end) {
                return false;
            }
            content.delete(Math.min(start, end), Math.max(start, end));
            return true;
        }
    }

    private boolean deleteUntilWordBoundary(View view, Editable content, boolean isForwardDelete) {
        int currentCursorOffset = Selection.getSelectionStart(content);
        if (currentCursorOffset != Selection.getSelectionEnd(content)) {
            return false;
        }
        if (!isForwardDelete && currentCursorOffset == 0) {
            return false;
        }
        if (isForwardDelete && currentCursorOffset == content.length()) {
            return false;
        }
        int deleteFrom;
        int deleteTo;
        WordIterator wordIterator = null;
        if (view instanceof TextView) {
            wordIterator = ((TextView) view).getWordIterator();
        }
        if (wordIterator == null) {
            wordIterator = new WordIterator();
        }
        if (isForwardDelete) {
            deleteFrom = currentCursorOffset;
            wordIterator.setCharSequence(content, deleteFrom, content.length());
            deleteTo = wordIterator.following(currentCursorOffset);
            if (deleteTo == -1) {
                deleteTo = content.length();
            }
        } else {
            deleteTo = currentCursorOffset;
            wordIterator.setCharSequence(content, 0, deleteTo);
            deleteFrom = wordIterator.preceding(currentCursorOffset);
            if (deleteFrom == -1) {
                deleteFrom = 0;
            }
        }
        content.delete(deleteFrom, deleteTo);
        return true;
    }

    private boolean deleteSelection(View view, Editable content) {
        int selectionStart = Selection.getSelectionStart(content);
        int selectionEnd = Selection.getSelectionEnd(content);
        if (selectionEnd < selectionStart) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }
        if (selectionStart == selectionEnd) {
            return false;
        }
        content.delete(selectionStart, selectionEnd);
        return true;
    }

    private boolean deleteLine(View view, Editable content) {
        if (view instanceof TextView) {
            Layout layout = ((TextView) view).getLayout();
            if (layout != null) {
                int line = layout.getLineForOffset(Selection.getSelectionStart(content));
                int start = layout.getLineStart(line);
                int end = layout.getLineEnd(line);
                if (end != start) {
                    content.delete(start, end);
                    return true;
                }
            }
        }
        return false;
    }

    static int makeTextContentType(Capitalize caps, boolean autoText) {
        int contentType = 1;
        switch (AnonymousClass1.$SwitchMap$android$text$method$TextKeyListener$Capitalize[caps.ordinal()]) {
            case 1:
                contentType = 1 | 4096;
                break;
            case 2:
                contentType = 1 | 8192;
                break;
            case 3:
                contentType = 1 | 16384;
                break;
        }
        if (autoText) {
            return contentType | 32768;
        }
        return contentType;
    }

    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        boolean handled;
        switch (keyCode) {
            case 67:
                handled = backspace(view, content, keyCode, event);
                break;
            case 112:
                handled = forwardDelete(view, content, keyCode, event);
                break;
            default:
                handled = false;
                break;
        }
        if (handled) {
            MetaKeyKeyListener.adjustMetaAfterKeypress((Spannable) content);
        }
        return super.onKeyDown(view, content, keyCode, event);
    }

    public boolean onKeyOther(View view, Editable content, KeyEvent event) {
        if (event.getAction() != 2 || event.getKeyCode() != 0) {
            return false;
        }
        int selectionStart = Selection.getSelectionStart(content);
        int selectionEnd = Selection.getSelectionEnd(content);
        if (selectionEnd < selectionStart) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }
        if (selectionStart < 0 || selectionEnd < 0) {
            selectionEnd = 0;
            selectionStart = 0;
            Selection.setSelection(content, 0);
        }
        CharSequence text = event.getCharacters();
        if (text == null) {
            return false;
        }
        content.replace(selectionStart, selectionEnd, text);
        return true;
    }
}
