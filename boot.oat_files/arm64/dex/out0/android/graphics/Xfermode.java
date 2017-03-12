package android.graphics;

public class Xfermode {
    long native_instance;

    private static native void finalizer(long j);

    protected void finalize() throws Throwable {
        try {
            finalizer(this.native_instance);
        } finally {
            super.finalize();
        }
    }
}
