package android.hardware.camera2.legacy;

import android.util.AndroidException;

public class LegacyExceptionUtils {
    private static final String TAG = "LegacyExceptionUtils";

    public static class BufferQueueAbandonedException extends AndroidException {
        public BufferQueueAbandonedException(String name) {
            super(name);
        }

        public BufferQueueAbandonedException(String name, Throwable cause) {
            super(name, cause);
        }

        public BufferQueueAbandonedException(Exception cause) {
            super(cause);
        }
    }

    public static int throwOnError(int errorFlag) throws BufferQueueAbandonedException {
        switch (errorFlag) {
            case -22:
                throw new BufferQueueAbandonedException();
            case 0:
                return 0;
            default:
                if (errorFlag >= 0) {
                    return errorFlag;
                }
                throw new UnsupportedOperationException("Unknown error " + errorFlag);
        }
    }

    private LegacyExceptionUtils() {
        throw new AssertionError();
    }
}
