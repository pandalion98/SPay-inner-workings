package android.graphics;

@Deprecated
public class PixelXorXfermode extends Xfermode {
    private static native long nativeCreate(int i);

    public PixelXorXfermode(int opColor) {
        this.native_instance = nativeCreate(opColor);
    }
}
