package com.google.android.gles_jni;

import javax.microedition.khronos.egl.EGLSurface;

public class EGLSurfaceImpl extends EGLSurface {
    long mEGLSurface;
    private long mNativePixelRef;

    public EGLSurfaceImpl() {
        this.mEGLSurface = 0;
        this.mNativePixelRef = 0;
    }

    public EGLSurfaceImpl(long surface) {
        this.mEGLSurface = surface;
        this.mNativePixelRef = 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.mEGLSurface != ((EGLSurfaceImpl) o).mEGLSurface) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((int) (this.mEGLSurface ^ (this.mEGLSurface >>> 32))) + 527;
    }
}
