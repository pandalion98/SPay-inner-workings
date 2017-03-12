package android.media;

import android.graphics.ImageFormat;
import android.media.Image.Plane;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageReader implements AutoCloseable {
    private static final int ACQUIRE_MAX_IMAGES = 2;
    private static final int ACQUIRE_NO_BUFS = 1;
    private static final int ACQUIRE_SUCCESS = 0;
    private int mEstimatedNativeAllocBytes;
    private final int mFormat;
    private final int mHeight;
    private OnImageAvailableListener mListener;
    private ListenerHandler mListenerHandler;
    private final Object mListenerLock = new Object();
    private final int mMaxImages;
    private long mNativeContext;
    private final int mNumPlanes;
    private final Surface mSurface;
    private final int mWidth;

    private final class ListenerHandler extends Handler {
        public ListenerHandler(Looper looper) {
            super(looper, null, true);
        }

        public void handleMessage(Message msg) {
            synchronized (ImageReader.this.mListenerLock) {
                OnImageAvailableListener listener = ImageReader.this.mListener;
            }
            if (listener != null) {
                listener.onImageAvailable(ImageReader.this);
            }
        }
    }

    public interface OnImageAvailableListener {
        void onImageAvailable(ImageReader imageReader);
    }

    private class SurfaceImage extends Image {
        private int mFormat = 0;
        private AtomicBoolean mIsDetached = new AtomicBoolean(false);
        private long mNativeBuffer;
        private SurfacePlane[] mPlanes;
        private long mTimestamp;

        private class SurfacePlane extends Plane {
            private ByteBuffer mBuffer;
            private final int mIndex;
            private final int mPixelStride;
            private final int mRowStride;

            private SurfacePlane(int index, int rowStride, int pixelStride) {
                this.mIndex = index;
                this.mRowStride = rowStride;
                this.mPixelStride = pixelStride;
            }

            public ByteBuffer getBuffer() {
                SurfaceImage.this.throwISEIfImageIsInvalid();
                if (this.mBuffer != null) {
                    return this.mBuffer;
                }
                this.mBuffer = SurfaceImage.this.nativeImageGetBuffer(this.mIndex, ImageReader.this.mFormat);
                return this.mBuffer.order(ByteOrder.nativeOrder());
            }

            public int getPixelStride() {
                SurfaceImage.this.throwISEIfImageIsInvalid();
                return this.mPixelStride;
            }

            public int getRowStride() {
                SurfaceImage.this.throwISEIfImageIsInvalid();
                return this.mRowStride;
            }

            private void clearBuffer() {
                if (this.mBuffer != null) {
                    if (this.mBuffer.isDirect()) {
                        NioUtils.freeDirectBuffer(this.mBuffer);
                    }
                    this.mBuffer = null;
                }
            }
        }

        private native synchronized SurfacePlane nativeCreatePlane(int i, int i2);

        private native synchronized int nativeGetFormat(int i);

        private native synchronized int nativeGetHeight(int i);

        private native synchronized int nativeGetWidth(int i);

        private native synchronized ByteBuffer nativeImageGetBuffer(int i, int i2);

        public SurfaceImage(int format) {
            this.mFormat = format;
        }

        public void close() {
            if (this.mIsImageValid) {
                ImageReader.this.releaseImage(this);
            }
        }

        public ImageReader getReader() {
            return ImageReader.this;
        }

        public int getFormat() {
            throwISEIfImageIsInvalid();
            int readerFormat = ImageReader.this.getImageFormat();
            if (readerFormat != 34) {
                readerFormat = nativeGetFormat(readerFormat);
            }
            this.mFormat = readerFormat;
            return this.mFormat;
        }

        public int getWidth() {
            throwISEIfImageIsInvalid();
            switch (getFormat()) {
                case 256:
                case 257:
                    return ImageReader.this.getWidth();
                default:
                    return nativeGetWidth(this.mFormat);
            }
        }

        public int getHeight() {
            throwISEIfImageIsInvalid();
            switch (getFormat()) {
                case 256:
                case 257:
                    return ImageReader.this.getHeight();
                default:
                    return nativeGetHeight(this.mFormat);
            }
        }

        public long getTimestamp() {
            throwISEIfImageIsInvalid();
            return this.mTimestamp;
        }

        public void setTimestamp(long timestampNs) {
            throwISEIfImageIsInvalid();
            this.mTimestamp = timestampNs;
        }

        public Plane[] getPlanes() {
            throwISEIfImageIsInvalid();
            return (Plane[]) this.mPlanes.clone();
        }

        protected final void finalize() throws Throwable {
            try {
                close();
            } finally {
                super.finalize();
            }
        }

        boolean isAttachable() {
            throwISEIfImageIsInvalid();
            return this.mIsDetached.get();
        }

        ImageReader getOwner() {
            throwISEIfImageIsInvalid();
            return ImageReader.this;
        }

        long getNativeContext() {
            throwISEIfImageIsInvalid();
            return this.mNativeBuffer;
        }

        private void setDetached(boolean detached) {
            throwISEIfImageIsInvalid();
            this.mIsDetached.getAndSet(detached);
        }

        private void clearSurfacePlanes() {
            if (this.mIsImageValid) {
                for (int i = 0; i < this.mPlanes.length; i++) {
                    if (this.mPlanes[i] != null) {
                        this.mPlanes[i].clearBuffer();
                        this.mPlanes[i] = null;
                    }
                }
            }
        }

        private void createSurfacePlanes() {
            this.mPlanes = new SurfacePlane[ImageReader.this.mNumPlanes];
            for (int i = 0; i < ImageReader.this.mNumPlanes; i++) {
                this.mPlanes[i] = nativeCreatePlane(i, ImageReader.this.mFormat);
            }
        }
    }

    private static native void nativeClassInit();

    private native synchronized void nativeClose();

    private native synchronized int nativeDetachImage(Image image);

    private native synchronized Surface nativeGetSurface();

    private native synchronized int nativeImageSetup(Image image);

    private native synchronized void nativeInit(Object obj, int i, int i2, int i3, int i4);

    private native synchronized void nativeReleaseImage(Image image);

    public static ImageReader newInstance(int width, int height, int format, int maxImages) {
        return new ImageReader(width, height, format, maxImages);
    }

    protected ImageReader(int width, int height, int format, int maxImages) {
        this.mWidth = width;
        this.mHeight = height;
        this.mFormat = format;
        this.mMaxImages = maxImages;
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("The image dimensions must be positive");
        } else if (this.mMaxImages < 1) {
            throw new IllegalArgumentException("Maximum outstanding image count must be at least 1");
        } else if (format == 17) {
            throw new IllegalArgumentException("NV21 format is not supported");
        } else {
            this.mNumPlanes = getNumPlanesFromFormat();
            nativeInit(new WeakReference(this), width, height, format, maxImages);
            this.mSurface = nativeGetSurface();
            this.mEstimatedNativeAllocBytes = ImageUtils.getEstimatedNativeAllocBytes(width, height, format, maxImages);
            VMRuntime.getRuntime().registerNativeAllocation(this.mEstimatedNativeAllocBytes);
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getImageFormat() {
        return this.mFormat;
    }

    public int getMaxImages() {
        return this.mMaxImages;
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public Image acquireLatestImage() {
        Image image = acquireNextImage();
        if (image == null) {
            return null;
        }
        while (true) {
            Image next = acquireNextImageNoThrowISE();
            if (next == null) {
                break;
            }
            try {
                image.close();
                image = next;
            } catch (Throwable th) {
                if (image != null) {
                    image.close();
                }
            }
        }
        Image result = image;
        image = null;
        if (image == null) {
            return result;
        }
        image.close();
        return result;
    }

    public Image acquireNextImageNoThrowISE() {
        SurfaceImage si = new SurfaceImage(this.mFormat);
        return acquireNextSurfaceImage(si) == 0 ? si : null;
    }

    private int acquireNextSurfaceImage(SurfaceImage si) {
        int status = nativeImageSetup(si);
        switch (status) {
            case 0:
                si.createSurfacePlanes();
                si.mIsImageValid = true;
                break;
            case 1:
            case 2:
                break;
            default:
                throw new AssertionError("Unknown nativeImageSetup return code " + status);
        }
        return status;
    }

    public Image acquireNextImage() {
        SurfaceImage si = new SurfaceImage(this.mFormat);
        int status = acquireNextSurfaceImage(si);
        switch (status) {
            case 0:
                return si;
            case 1:
                return null;
            case 2:
                throw new IllegalStateException(String.format("maxImages (%d) has already been acquired, call #close before acquiring more.", new Object[]{Integer.valueOf(this.mMaxImages)}));
            default:
                throw new AssertionError("Unknown nativeImageSetup return code " + status);
        }
    }

    private void releaseImage(Image i) {
        if (i instanceof SurfaceImage) {
            SurfaceImage si = (SurfaceImage) i;
            if (si.getReader() != this) {
                throw new IllegalArgumentException("This image was not produced by this ImageReader");
            }
            si.clearSurfacePlanes();
            nativeReleaseImage(i);
            si.mIsImageValid = false;
            return;
        }
        throw new IllegalArgumentException("This image was not produced by an ImageReader");
    }

    public void setOnImageAvailableListener(OnImageAvailableListener listener, Handler handler) {
        synchronized (this.mListenerLock) {
            if (listener != null) {
                Looper looper = handler != null ? handler.getLooper() : Looper.myLooper();
                if (looper == null) {
                    throw new IllegalArgumentException("handler is null but the current thread is not a looper");
                }
                if (this.mListenerHandler == null || this.mListenerHandler.getLooper() != looper) {
                    this.mListenerHandler = new ListenerHandler(looper);
                }
                this.mListener = listener;
            } else {
                this.mListener = null;
                this.mListenerHandler = null;
            }
        }
    }

    public void close() {
        setOnImageAvailableListener(null, null);
        if (this.mSurface != null) {
            this.mSurface.release();
        }
        nativeClose();
        if (this.mEstimatedNativeAllocBytes > 0) {
            VMRuntime.getRuntime().registerNativeFree(this.mEstimatedNativeAllocBytes);
            this.mEstimatedNativeAllocBytes = 0;
        }
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    void detachImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("input image must not be null");
        } else if (isImageOwnedbyMe(image)) {
            SurfaceImage si = (SurfaceImage) image;
            si.throwISEIfImageIsInvalid();
            if (si.isAttachable()) {
                throw new IllegalStateException("Image was already detached from this ImageReader");
            }
            nativeDetachImage(image);
            si.setDetached(true);
        } else {
            throw new IllegalArgumentException("Trying to detach an image that is not owned by this ImageReader");
        }
    }

    private int getNumPlanesFromFormat() {
        switch (this.mFormat) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 20:
            case 32:
            case 37:
            case 256:
            case 257:
            case ImageFormat.Y8 /*538982489*/:
            case ImageFormat.Y16 /*540422489*/:
            case ImageFormat.DEPTH16 /*1144402265*/:
                return 1;
            case 16:
                return 2;
            case 17:
            case 35:
            case ImageFormat.YV12 /*842094169*/:
                return 3;
            case 34:
                return 0;
            default:
                throw new UnsupportedOperationException(String.format("Invalid format specified %d", new Object[]{Integer.valueOf(this.mFormat)}));
        }
    }

    private boolean isImageOwnedbyMe(Image image) {
        if ((image instanceof SurfaceImage) && ((SurfaceImage) image).getReader() == this) {
            return true;
        }
        return false;
    }

    private static void postEventFromNative(Object selfRef) {
        ImageReader ir = (ImageReader) ((WeakReference) selfRef).get();
        if (ir != null) {
            Handler handler;
            synchronized (ir.mListenerLock) {
                handler = ir.mListenerHandler;
            }
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }
        }
    }

    static {
        System.loadLibrary("media_jni");
        nativeClassInit();
    }
}
