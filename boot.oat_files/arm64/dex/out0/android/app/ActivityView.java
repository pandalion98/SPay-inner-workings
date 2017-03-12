package android.app;

import android.app.IActivityContainerCallback.Stub;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.OperationCanceledException;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public class ActivityView extends ViewGroup {
    private static final boolean DEBUG = false;
    private static final int MSG_SET_SURFACE = 1;
    private static final String TAG = "ActivityView";
    private Activity mActivity;
    private ActivityContainerWrapper mActivityContainer;
    private ActivityViewCallback mActivityViewCallback;
    private Handler mHandler;
    private int mHeight;
    private int mLastVisibility;
    DisplayMetrics mMetrics;
    private Surface mSurface;
    private final TextureView mTextureView;
    private HandlerThread mThread;
    private int mWidth;

    private static class ActivityContainerCallback extends Stub {
        private final WeakReference<ActivityView> mActivityViewWeakReference;

        ActivityContainerCallback(ActivityView activityView) {
            this.mActivityViewWeakReference = new WeakReference(activityView);
        }

        public void setVisible(IBinder container, boolean visible) {
        }

        public void onAllActivitiesComplete(IBinder container) {
            final ActivityView activityView = (ActivityView) this.mActivityViewWeakReference.get();
            if (activityView != null) {
                ActivityViewCallback callback = activityView.mActivityViewCallback;
                if (callback != null) {
                    final WeakReference<ActivityViewCallback> callbackRef = new WeakReference(callback);
                    activityView.post(new Runnable() {
                        public void run() {
                            ActivityViewCallback callback = (ActivityViewCallback) callbackRef.get();
                            if (callback != null) {
                                callback.onAllActivitiesComplete(activityView);
                            }
                        }
                    });
                }
            }
        }
    }

    private static class ActivityContainerWrapper {
        private final CloseGuard mGuard = CloseGuard.get();
        private final IActivityContainer mIActivityContainer;
        boolean mOpened;

        ActivityContainerWrapper(IActivityContainer container) {
            this.mIActivityContainer = container;
            this.mOpened = true;
            this.mGuard.open("release");
        }

        void attachToDisplay(int displayId) {
            try {
                this.mIActivityContainer.attachToDisplay(displayId);
            } catch (RemoteException e) {
            }
        }

        void setSurface(Surface surface, int width, int height, int density) throws RemoteException {
            this.mIActivityContainer.setSurface(surface, width, height, density);
        }

        int startActivity(Intent intent) {
            try {
                return this.mIActivityContainer.startActivity(intent);
            } catch (RemoteException e) {
                throw new RuntimeException("ActivityView: Unable to startActivity. " + e);
            }
        }

        int startActivityIntentSender(IIntentSender intentSender) {
            try {
                return this.mIActivityContainer.startActivityIntentSender(intentSender);
            } catch (RemoteException e) {
                throw new RuntimeException("ActivityView: Unable to startActivity from IntentSender. " + e);
            }
        }

        int getDisplayId() {
            try {
                return this.mIActivityContainer.getDisplayId();
            } catch (RemoteException e) {
                return -1;
            }
        }

        boolean injectEvent(InputEvent event) {
            try {
                return this.mIActivityContainer.injectEvent(event);
            } catch (RemoteException e) {
                return false;
            }
        }

        void release() {
            synchronized (this.mGuard) {
                if (this.mOpened) {
                    try {
                        this.mIActivityContainer.release();
                        this.mGuard.close();
                    } catch (RemoteException e) {
                    }
                    this.mOpened = false;
                }
            }
        }

        protected void finalize() throws Throwable {
            try {
                if (this.mGuard != null) {
                    this.mGuard.warnIfOpen();
                    release();
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
            }
        }
    }

    public static abstract class ActivityViewCallback {
        public abstract void onAllActivitiesComplete(ActivityView activityView);

        public abstract void onSurfaceAvailable(ActivityView activityView);

        public abstract void onSurfaceDestroyed(ActivityView activityView);
    }

    private class ActivityViewSurfaceTextureListener implements SurfaceTextureListener {
        private ActivityViewSurfaceTextureListener() {
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            if (ActivityView.this.mActivityContainer != null) {
                ActivityView.this.mWidth = width;
                ActivityView.this.mHeight = height;
                ActivityView.this.attachToSurfaceWhenReady();
                if (ActivityView.this.mActivityViewCallback != null) {
                    ActivityView.this.mActivityViewCallback.onSurfaceAvailable(ActivityView.this);
                }
            }
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            if (ActivityView.this.mActivityContainer != null) {
            }
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            if (ActivityView.this.mActivityContainer != null) {
                ActivityView.this.mSurface.release();
                ActivityView.this.mSurface = null;
                try {
                    ActivityView.this.mActivityContainer.setSurface(null, ActivityView.this.mWidth, ActivityView.this.mHeight, ActivityView.this.mMetrics.densityDpi);
                    if (ActivityView.this.mActivityViewCallback != null) {
                        ActivityView.this.mActivityViewCallback.onSurfaceDestroyed(ActivityView.this);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException("ActivityView: Unable to set surface of ActivityContainer. " + e);
                }
            }
            return true;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    }

    public ActivityView(Context context) {
        this(context, null);
    }

    public ActivityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMetrics = new DisplayMetrics();
        this.mThread = new HandlerThread("ActivityViewThread");
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                this.mActivity = (Activity) context;
                break;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (this.mActivity == null) {
            throw new IllegalStateException("The ActivityView's Context is not an Activity.");
        }
        try {
            this.mActivityContainer = new ActivityContainerWrapper(ActivityManagerNative.getDefault().createVirtualActivityContainer(this.mActivity.getActivityToken(), new ActivityContainerCallback(this)));
            this.mThread.start();
            this.mHandler = new Handler(this.mThread.getLooper()) {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        try {
                            ActivityView.this.mActivityContainer.setSurface((Surface) msg.obj, msg.arg1, msg.arg2, ActivityView.this.mMetrics.densityDpi);
                        } catch (RemoteException e) {
                            throw new RuntimeException("ActivityView: Unable to set surface of ActivityContainer. " + e);
                        }
                    }
                }
            };
            this.mTextureView = new TextureView(context);
            this.mTextureView.setSurfaceTextureListener(new ActivityViewSurfaceTextureListener());
            addView(this.mTextureView);
            ((WindowManager) this.mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(this.mMetrics);
            this.mLastVisibility = getVisibility();
        } catch (RemoteException e) {
            throw new RuntimeException("ActivityView: Unable to create ActivityContainer. " + e);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mTextureView.layout(0, 0, r - l, b - t);
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this.mSurface != null && (visibility == 8 || this.mLastVisibility == 8)) {
            Message msg = Message.obtain(this.mHandler, 1);
            msg.obj = visibility == 8 ? null : this.mSurface;
            msg.arg1 = this.mWidth;
            msg.arg2 = this.mHeight;
            this.mHandler.sendMessage(msg);
        }
        this.mLastVisibility = visibility;
    }

    private boolean injectInputEvent(InputEvent event) {
        return this.mActivityContainer != null && this.mActivityContainer.injectEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return injectInputEvent(event) || super.onTouchEvent(event);
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.isFromSource(2) && injectInputEvent(event)) {
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    public void onAttachedToWindow() {
    }

    public void onDetachedFromWindow() {
    }

    public boolean isAttachedToDisplay() {
        return this.mSurface != null;
    }

    public void startActivity(Intent intent) {
        if (this.mActivityContainer == null) {
            throw new IllegalStateException("Attempt to call startActivity after release");
        } else if (this.mSurface == null) {
            throw new IllegalStateException("Surface not yet created.");
        } else if (this.mActivityContainer.startActivity(intent) == -6) {
            throw new OperationCanceledException();
        }
    }

    public void startActivity(IntentSender intentSender) {
        if (this.mActivityContainer == null) {
            throw new IllegalStateException("Attempt to call startActivity after release");
        } else if (this.mSurface == null) {
            throw new IllegalStateException("Surface not yet created.");
        } else {
            if (this.mActivityContainer.startActivityIntentSender(intentSender.getTarget()) == -6) {
                throw new OperationCanceledException();
            }
        }
    }

    public void startActivity(PendingIntent pendingIntent) {
        if (this.mActivityContainer == null) {
            throw new IllegalStateException("Attempt to call startActivity after release");
        } else if (this.mSurface == null) {
            throw new IllegalStateException("Surface not yet created.");
        } else {
            if (this.mActivityContainer.startActivityIntentSender(pendingIntent.getTarget()) == -6) {
                throw new OperationCanceledException();
            }
        }
    }

    public void release() {
        if (this.mActivityContainer == null) {
            Log.e(TAG, "Duplicate call to release");
            return;
        }
        this.mActivityContainer.release();
        this.mActivityContainer = null;
        if (this.mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
        this.mTextureView.setSurfaceTextureListener(null);
    }

    private void attachToSurfaceWhenReady() {
        SurfaceTexture surfaceTexture = this.mTextureView.getSurfaceTexture();
        if (surfaceTexture != null && this.mSurface == null) {
            this.mSurface = new Surface(surfaceTexture);
            try {
                this.mActivityContainer.setSurface(this.mSurface, this.mWidth, this.mHeight, this.mMetrics.densityDpi);
            } catch (RemoteException e) {
                this.mSurface.release();
                this.mSurface = null;
                throw new RuntimeException("ActivityView: Unable to create ActivityContainer. " + e);
            }
        }
    }

    public void setCallback(ActivityViewCallback callback) {
        this.mActivityViewCallback = callback;
        if (this.mSurface != null) {
            this.mActivityViewCallback.onSurfaceAvailable(this);
        }
    }
}
