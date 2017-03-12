package android.hardware.camera2.utils;

import android.hardware.camera2.CameraAccessException;

public class CameraRuntimeException extends RuntimeException {
    private Throwable mCause;
    private String mMessage;
    private final int mReason;

    public final int getReason() {
        return this.mReason;
    }

    public CameraRuntimeException(int problem) {
        this.mReason = problem;
    }

    public CameraRuntimeException(int problem, String message) {
        super(message);
        this.mReason = problem;
        this.mMessage = message;
    }

    public CameraRuntimeException(int problem, String message, Throwable cause) {
        super(message, cause);
        this.mReason = problem;
        this.mMessage = message;
        this.mCause = cause;
    }

    public CameraRuntimeException(int problem, Throwable cause) {
        super(cause);
        this.mReason = problem;
        this.mCause = cause;
    }

    public CameraAccessException asChecked() {
        CameraAccessException e;
        if (this.mMessage != null && this.mCause != null) {
            e = new CameraAccessException(this.mReason, this.mMessage, this.mCause);
        } else if (this.mMessage != null) {
            e = new CameraAccessException(this.mReason, this.mMessage);
        } else if (this.mCause != null) {
            e = new CameraAccessException(this.mReason, this.mCause);
        } else {
            e = new CameraAccessException(this.mReason);
        }
        e.setStackTrace(getStackTrace());
        return e;
    }
}
