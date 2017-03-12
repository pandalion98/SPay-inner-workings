package android.graphics;

public class Shader {
    private Matrix mLocalMatrix;
    private long native_instance;

    public enum TileMode {
        CLAMP(0),
        REPEAT(1),
        MIRROR(2);
        
        final int nativeInt;

        private TileMode(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    private static native void nativeDestructor(long j);

    private static native long nativeSetLocalMatrix(long j, long j2);

    protected void init(long ni) {
        this.native_instance = ni;
    }

    public boolean getLocalMatrix(Matrix localM) {
        if (this.mLocalMatrix == null) {
            return false;
        }
        localM.set(this.mLocalMatrix);
        if (this.mLocalMatrix.isIdentity()) {
            return false;
        }
        return true;
    }

    public void setLocalMatrix(Matrix localM) {
        this.mLocalMatrix = localM;
        this.native_instance = nativeSetLocalMatrix(this.native_instance, localM == null ? 0 : localM.native_instance);
    }

    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            nativeDestructor(this.native_instance);
        }
    }

    protected Shader copy() {
        Shader copy = new Shader();
        copyLocalMatrix(copy);
        return copy;
    }

    protected void copyLocalMatrix(Shader dest) {
        if (this.mLocalMatrix != null) {
            Matrix lm = new Matrix();
            getLocalMatrix(lm);
            dest.setLocalMatrix(lm);
            return;
        }
        dest.setLocalMatrix(null);
    }

    long getNativeInstance() {
        return this.native_instance;
    }
}
