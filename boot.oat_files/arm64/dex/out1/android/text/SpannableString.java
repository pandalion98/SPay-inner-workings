package android.text;

public class SpannableString extends SpannableStringInternal implements CharSequence, GetChars, Spannable {
    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ int getSpanEnd(Object x0) {
        return super.getSpanEnd(x0);
    }

    public /* bridge */ /* synthetic */ int getSpanFlags(Object x0) {
        return super.getSpanFlags(x0);
    }

    public /* bridge */ /* synthetic */ int getSpanStart(Object x0) {
        return super.getSpanStart(x0);
    }

    public /* bridge */ /* synthetic */ Object[] getSpans(int x0, int x1, Class x2) {
        return super.getSpans(x0, x1, x2);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ int nextSpanTransition(int x0, int x1, Class x2) {
        return super.nextSpanTransition(x0, x1, x2);
    }

    public SpannableString(CharSequence source) {
        super(source, 0, source.length());
    }

    private SpannableString(CharSequence source, int start, int end) {
        super(source, start, end);
    }

    public static SpannableString valueOf(CharSequence source) {
        if (source instanceof SpannableString) {
            return (SpannableString) source;
        }
        return new SpannableString(source);
    }

    public void setSpan(Object what, int start, int end, int flags) {
        super.setSpan(what, start, end, flags);
    }

    public void removeSpan(Object what) {
        super.removeSpan(what);
    }

    public final CharSequence subSequence(int start, int end) {
        return new SpannableString(this, start, end);
    }
}
