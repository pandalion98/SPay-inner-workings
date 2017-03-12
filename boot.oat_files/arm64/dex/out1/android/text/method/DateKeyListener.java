package android.text.method;

public class DateKeyListener extends NumberKeyListener {
    public static final char[] CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', '-', '.'};
    private static DateKeyListener sInstance;

    public int getInputType() {
        return 20;
    }

    protected char[] getAcceptedChars() {
        return CHARACTERS;
    }

    public static DateKeyListener getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new DateKeyListener();
        return sInstance;
    }
}
