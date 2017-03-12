package android.animation;

public class ArgbEvaluator implements TypeEvaluator {
    private static final ArgbEvaluator sInstance = new ArgbEvaluator();

    public static ArgbEvaluator getInstance() {
        return sInstance;
    }

    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = ((Integer) startValue).intValue();
        int startA = (startInt >> 24) & 255;
        int startR = (startInt >> 16) & 255;
        int startG = (startInt >> 8) & 255;
        int startB = startInt & 255;
        int endInt = ((Integer) endValue).intValue();
        return Integer.valueOf(((((((int) (((float) (((endInt >> 24) & 255) - startA)) * fraction)) + startA) << 24) | ((((int) (((float) (((endInt >> 16) & 255) - startR)) * fraction)) + startR) << 16)) | ((((int) (((float) (((endInt >> 8) & 255) - startG)) * fraction)) + startG) << 8)) | (((int) (((float) ((endInt & 255) - startB)) * fraction)) + startB));
    }
}
