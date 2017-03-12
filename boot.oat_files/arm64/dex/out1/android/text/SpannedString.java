package android.text;

public final class SpannedString extends SpannableStringInternal implements CharSequence, GetChars, Spanned {
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

    public SpannedString(CharSequence source) {
        super(source, 0, source.length());
    }

    private SpannedString(CharSequence source, int start, int end) {
        super(source, start, end);
    }

    public CharSequence subSequence(int start, int end) {
        return new SpannedString(this, start, end);
    }

    public static SpannedString valueOf(CharSequence source) {
        if (source instanceof SpannedString) {
            return (SpannedString) source;
        }
        return new SpannedString(source);
    }
}
