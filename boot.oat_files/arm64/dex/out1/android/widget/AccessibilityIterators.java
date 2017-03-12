package android.widget;

import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.view.AccessibilityIterators.AbstractTextSegmentIterator;

final class AccessibilityIterators {

    static class LineTextSegmentIterator extends AbstractTextSegmentIterator {
        protected static final int DIRECTION_END = 1;
        protected static final int DIRECTION_START = -1;
        private static LineTextSegmentIterator sLineInstance;
        protected Layout mLayout;

        LineTextSegmentIterator() {
        }

        public static LineTextSegmentIterator getInstance() {
            if (sLineInstance == null) {
                sLineInstance = new LineTextSegmentIterator();
            }
            return sLineInstance;
        }

        public void initialize(Spannable text, Layout layout) {
            this.mText = text.toString();
            this.mLayout = layout;
        }

        public int[] following(int offset) {
            if (this.mText.length() <= 0 || offset >= this.mText.length()) {
                return null;
            }
            int nextLine;
            if (offset < 0) {
                nextLine = this.mLayout.getLineForOffset(0);
            } else {
                int currentLine = this.mLayout.getLineForOffset(offset);
                if (getLineEdgeIndex(currentLine, -1) == offset) {
                    nextLine = currentLine;
                } else {
                    nextLine = currentLine + 1;
                }
            }
            if (nextLine < this.mLayout.getLineCount()) {
                return getRange(getLineEdgeIndex(nextLine, -1), getLineEdgeIndex(nextLine, 1) + 1);
            }
            return null;
        }

        public int[] preceding(int offset) {
            if (this.mText.length() <= 0 || offset <= 0) {
                return null;
            }
            int previousLine;
            if (offset > this.mText.length()) {
                previousLine = this.mLayout.getLineForOffset(this.mText.length());
            } else {
                int currentLine = this.mLayout.getLineForOffset(offset);
                if (getLineEdgeIndex(currentLine, 1) + 1 == offset) {
                    previousLine = currentLine;
                } else {
                    previousLine = currentLine - 1;
                }
            }
            if (previousLine >= 0) {
                return getRange(getLineEdgeIndex(previousLine, -1), getLineEdgeIndex(previousLine, 1) + 1);
            }
            return null;
        }

        protected int getLineEdgeIndex(int lineNumber, int direction) {
            if (direction * this.mLayout.getParagraphDirection(lineNumber) < 0) {
                return this.mLayout.getLineStart(lineNumber);
            }
            return this.mLayout.getLineEnd(lineNumber) - 1;
        }
    }

    static class PageTextSegmentIterator extends LineTextSegmentIterator {
        private static PageTextSegmentIterator sPageInstance;
        private final Rect mTempRect = new Rect();
        private TextView mView;

        PageTextSegmentIterator() {
        }

        public static PageTextSegmentIterator getInstance() {
            if (sPageInstance == null) {
                sPageInstance = new PageTextSegmentIterator();
            }
            return sPageInstance;
        }

        public void initialize(TextView view) {
            super.initialize((Spannable) view.getIterableTextForAccessibility(), view.getLayout());
            this.mView = view;
        }

        public int[] following(int offset) {
            if (this.mText.length() <= 0 || offset >= this.mText.length() || !this.mView.getGlobalVisibleRect(this.mTempRect)) {
                return null;
            }
            int start = Math.max(0, offset);
            int nextPageStartY = this.mLayout.getLineTop(this.mLayout.getLineForOffset(start)) + ((this.mTempRect.height() - this.mView.getTotalPaddingTop()) - this.mView.getTotalPaddingBottom());
            int currentPageEndLine = nextPageStartY < this.mLayout.getLineTop(this.mLayout.getLineCount() + -1) ? this.mLayout.getLineForVertical(nextPageStartY) - 1 : this.mLayout.getLineCount() - 1;
            if (currentPageEndLine >= 0) {
                return getRange(start, getLineEdgeIndex(currentPageEndLine, 1) + 1);
            }
            return null;
        }

        public int[] preceding(int offset) {
            if (this.mText.length() <= 0 || offset <= 0 || !this.mView.getGlobalVisibleRect(this.mTempRect)) {
                return null;
            }
            int end = Math.min(this.mText.length(), offset);
            int previousPageEndY = this.mLayout.getLineTop(this.mLayout.getLineForOffset(end)) - ((this.mTempRect.height() - this.mView.getTotalPaddingTop()) - this.mView.getTotalPaddingBottom());
            return getRange(getLineEdgeIndex(previousPageEndY > 0 ? this.mLayout.getLineForVertical(previousPageEndY) + 1 : 0, -1), end);
        }
    }

    AccessibilityIterators() {
    }
}
