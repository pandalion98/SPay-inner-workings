package android.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Pair;
import android.util.Slog;
import android.view.View;
import java.util.ArrayList;

public class ActivityOptions {
    public static final int ANIM_CLIP_REVEAL = 11;
    public static final int ANIM_CUSTOM = 1;
    public static final int ANIM_CUSTOM_IN_PLACE = 10;
    public static final int ANIM_CUSTOM_SCALE_UP = 1001;
    public static final int ANIM_DEFAULT = 6;
    public static final int ANIM_LAUNCH_TASK_BEHIND = 7;
    public static final int ANIM_NONE = 0;
    public static final int ANIM_SCALE_UP = 2;
    public static final int ANIM_SCENE_TRANSITION = 5;
    public static final int ANIM_THUMBNAIL_ASPECT_SCALE_DOWN = 9;
    public static final int ANIM_THUMBNAIL_ASPECT_SCALE_UP = 8;
    public static final int ANIM_THUMBNAIL_SCALE_DOWN = 4;
    public static final int ANIM_THUMBNAIL_SCALE_UP = 3;
    public static final int ANIM_TRANSLATE = 1000;
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";
    public static final String KEY_ANIM_ENTER_RES_ID = "android:activity.animEnterRes";
    public static final String KEY_ANIM_EXIT_RES_ID = "android:activity.animExitRes";
    public static final String KEY_ANIM_HEIGHT = "android:activity.animHeight";
    public static final String KEY_ANIM_IN_PLACE_RES_ID = "android:activity.animInPlaceRes";
    public static final String KEY_ANIM_START_LISTENER = "android:activity.animStartListener";
    public static final String KEY_ANIM_START_X = "android:activity.animStartX";
    public static final String KEY_ANIM_START_Y = "android:activity.animStartY";
    public static final String KEY_ANIM_THUMBNAIL = "android:activity.animThumbnail";
    public static final String KEY_ANIM_TYPE = "android:activity.animType";
    public static final String KEY_ANIM_WIDTH = "android:activity.animWidth";
    private static final String KEY_EXIT_COORDINATOR_INDEX = "android:activity.exitCoordinatorIndex";
    public static final String KEY_PACKAGE_NAME = "android:activity.packageName";
    private static final String KEY_RESULT_CODE = "android:activity.resultCode";
    private static final String KEY_RESULT_DATA = "android:activity.resultData";
    private static final String KEY_TRANSITION_COMPLETE_LISTENER = "android:activity.transitionCompleteListener";
    private static final String KEY_TRANSITION_IS_RETURNING = "android:activity.transitionIsReturning";
    private static final String KEY_TRANSITION_SHARED_ELEMENTS = "android:activity.sharedElementNames";
    public static final String KEY_TRANSIT_STARTED_LISTENER = "android:activity.transitStartedListener";
    private static final String KEY_USAGE_TIME_REPORT = "android:activity.usageTimeReport";
    private static final String TAG = "ActivityOptions";
    private IRemoteCallback mAnimationStartedListener;
    private int mAnimationType = 0;
    private int mCustomEnterResId;
    private int mCustomExitResId;
    private int mCustomInPlaceResId;
    private int mExitCoordinatorIndex;
    private int mHeight;
    private boolean mIsReturning;
    private String mPackageName;
    private int mResultCode;
    private Intent mResultData;
    private ArrayList<String> mSharedElementNames;
    private int mStartX;
    private int mStartY;
    private Bitmap mThumbnail;
    private ResultReceiver mTransitionReceiver;
    private IRemoteCallback mTransitionStartedListener;
    private PendingIntent mUsageTimeReport;
    private int mWidth;

    public interface OnAnimationStartedListener {
        void onAnimationStarted();
    }

    public interface OnTransitionStartedListener {
        void onTransitionStarted();
    }

    public static ActivityOptions makeCustomAnimation(Context context, int enterResId, int exitResId) {
        return makeCustomAnimation(context, enterResId, exitResId, null, null);
    }

    public static ActivityOptions makeCustomAnimation(Context context, int enterResId, int exitResId, Handler handler, OnAnimationStartedListener listener) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = context.getPackageName();
        opts.mAnimationType = 1;
        opts.mCustomEnterResId = enterResId;
        opts.mCustomExitResId = exitResId;
        opts.setOnAnimationStartedListener(handler, listener);
        return opts;
    }

    public static ActivityOptions makeCustomInPlaceAnimation(Context context, int animId) {
        if (animId == 0) {
            throw new RuntimeException("You must specify a valid animation.");
        }
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = context.getPackageName();
        opts.mAnimationType = 10;
        opts.mCustomInPlaceResId = animId;
        return opts;
    }

    private void setOnAnimationStartedListener(Handler handler, OnAnimationStartedListener listener) {
        if (listener != null) {
            final Handler h = handler;
            final OnAnimationStartedListener finalListener = listener;
            this.mAnimationStartedListener = new Stub() {
                public void sendResult(Bundle data) throws RemoteException {
                    h.post(new Runnable() {
                        public void run() {
                            finalListener.onAnimationStarted();
                        }
                    });
                }
            };
        }
    }

    private void setOnTransitionStartedListener(Handler handler, OnTransitionStartedListener listener) {
        if (listener != null) {
            final Handler h = handler;
            final OnTransitionStartedListener finalListener = listener;
            this.mTransitionStartedListener = new Stub() {
                public void sendResult(Bundle data) throws RemoteException {
                    h.post(new Runnable() {
                        public void run() {
                            finalListener.onTransitionStarted();
                        }
                    });
                }
            };
        }
    }

    public static ActivityOptions makeScaleUpAnimation(View source, int startX, int startY, int width, int height) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = 2;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = width;
        opts.mHeight = height;
        return opts;
    }

    public static ActivityOptions makeCustomScaleUpAnimation(View source, int width, int height) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = 1001;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0];
        opts.mStartY = pts[1];
        opts.mWidth = width;
        opts.mHeight = height;
        return opts;
    }

    public static ActivityOptions makeClipRevealAnimation(View source, int startX, int startY, int width, int height) {
        ActivityOptions opts = new ActivityOptions();
        opts.mAnimationType = 11;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = width;
        opts.mHeight = height;
        return opts;
    }

    public static ActivityOptions makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY) {
        return makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY, null);
    }

    public static ActivityOptions makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY, OnAnimationStartedListener listener) {
        return makeThumbnailAnimation(source, thumbnail, startX, startY, listener, null, true);
    }

    public static ActivityOptions makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY, OnAnimationStartedListener listener, OnTransitionStartedListener transitListener) {
        return makeThumbnailAnimation(source, thumbnail, startX, startY, listener, transitListener, true);
    }

    public static ActivityOptions makeThumbnailScaleDownAnimation(View source, Bitmap thumbnail, int startX, int startY, OnAnimationStartedListener listener) {
        return makeThumbnailAnimation(source, thumbnail, startX, startY, listener, null, false);
    }

    public static ActivityOptions makeThumbnailScaleDownAnimation(View source, Bitmap thumbnail, int startX, int startY, OnAnimationStartedListener listener, OnTransitionStartedListener transitListener) {
        return makeThumbnailAnimation(source, thumbnail, startX, startY, listener, transitListener, false);
    }

    private static ActivityOptions makeThumbnailAnimation(View source, Bitmap thumbnail, int startX, int startY, OnAnimationStartedListener listener, OnTransitionStartedListener transitListener, boolean scaleUp) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = scaleUp ? 3 : 4;
        opts.mThumbnail = thumbnail;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.setOnAnimationStartedListener(source.getHandler(), listener);
        opts.setOnTransitionStartedListener(source.getHandler(), transitListener);
        return opts;
    }

    public static ActivityOptions makeThumbnailAspectScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, Handler handler, OnAnimationStartedListener listener) {
        return makeAspectScaledThumbnailAnimation(source, thumbnail, startX, startY, targetWidth, targetHeight, handler, listener, null, true);
    }

    public static ActivityOptions makeThumbnailAspectScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, Handler handler, OnAnimationStartedListener listener, OnTransitionStartedListener transitListener) {
        return makeAspectScaledThumbnailAnimation(source, thumbnail, startX, startY, targetWidth, targetHeight, handler, listener, transitListener, true);
    }

    public static ActivityOptions makeThumbnailAspectScaleDownAnimation(View source, Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, Handler handler, OnAnimationStartedListener listener) {
        return makeAspectScaledThumbnailAnimation(source, thumbnail, startX, startY, targetWidth, targetHeight, handler, listener, null, false);
    }

    public static ActivityOptions makeThumbnailAspectScaleDownAnimation(View source, Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, Handler handler, OnAnimationStartedListener listener, OnTransitionStartedListener transitListener) {
        return makeAspectScaledThumbnailAnimation(source, thumbnail, startX, startY, targetWidth, targetHeight, handler, listener, transitListener, false);
    }

    private static ActivityOptions makeAspectScaledThumbnailAnimation(View source, Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, Handler handler, OnAnimationStartedListener listener, OnTransitionStartedListener transitListener, boolean scaleUp) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = scaleUp ? 8 : 9;
        opts.mThumbnail = thumbnail;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = targetWidth;
        opts.mHeight = targetHeight;
        opts.setOnAnimationStartedListener(handler, listener);
        opts.setOnTransitionStartedListener(handler, transitListener);
        return opts;
    }

    public static ActivityOptions makeSceneTransitionAnimation(Activity activity, View sharedElement, String sharedElementName) {
        return makeSceneTransitionAnimation(activity, Pair.create(sharedElement, sharedElementName));
    }

    public static ActivityOptions makeSceneTransitionAnimation(Activity activity, Pair<View, String>... sharedElements) {
        ActivityOptions opts = new ActivityOptions();
        if (activity.getWindow().hasFeature(13)) {
            opts.mAnimationType = 5;
            ArrayList<String> names = new ArrayList();
            ArrayList<View> views = new ArrayList();
            if (sharedElements != null) {
                for (Pair<View, String> sharedElement : sharedElements) {
                    String sharedElementName = sharedElement.second;
                    if (sharedElementName == null) {
                        throw new IllegalArgumentException("Shared element name must not be null");
                    }
                    names.add(sharedElementName);
                    if (sharedElement.first == null) {
                        throw new IllegalArgumentException("Shared element must not be null");
                    }
                    views.add(sharedElement.first);
                }
            }
            ExitTransitionCoordinator exit = new ExitTransitionCoordinator(activity, names, names, views, false);
            opts.mTransitionReceiver = exit;
            opts.mSharedElementNames = names;
            opts.mIsReturning = false;
            opts.mExitCoordinatorIndex = activity.mActivityTransitionState.addExitTransitionCoordinator(exit);
        } else {
            opts.mAnimationType = 6;
        }
        return opts;
    }

    public static ActivityOptions makeSceneTransitionAnimation(Activity activity, ExitTransitionCoordinator exitCoordinator, ArrayList<String> sharedElementNames, int resultCode, Intent resultData) {
        ActivityOptions opts = new ActivityOptions();
        opts.mAnimationType = 5;
        opts.mSharedElementNames = sharedElementNames;
        opts.mTransitionReceiver = exitCoordinator;
        opts.mIsReturning = true;
        opts.mResultCode = resultCode;
        opts.mResultData = resultData;
        opts.mExitCoordinatorIndex = activity.mActivityTransitionState.addExitTransitionCoordinator(exitCoordinator);
        return opts;
    }

    public static ActivityOptions makeTaskLaunchBehind() {
        ActivityOptions opts = new ActivityOptions();
        opts.mAnimationType = 7;
        return opts;
    }

    public static ActivityOptions makeBasic() {
        return new ActivityOptions();
    }

    public boolean getLaunchTaskBehind() {
        return this.mAnimationType == 7;
    }

    private ActivityOptions() {
    }

    public ActivityOptions(Bundle opts) {
        this.mPackageName = opts.getString(KEY_PACKAGE_NAME);
        try {
            this.mUsageTimeReport = (PendingIntent) opts.getParcelable(KEY_USAGE_TIME_REPORT);
        } catch (RuntimeException e) {
            Slog.w(TAG, e);
        }
        this.mAnimationType = opts.getInt(KEY_ANIM_TYPE);
        if (opts.getBoolean("launchFromEdge", false)) {
            this.mAnimationType = 1000;
        }
        switch (this.mAnimationType) {
            case 1:
                this.mCustomEnterResId = opts.getInt(KEY_ANIM_ENTER_RES_ID, 0);
                this.mCustomExitResId = opts.getInt(KEY_ANIM_EXIT_RES_ID, 0);
                this.mAnimationStartedListener = Stub.asInterface(opts.getBinder(KEY_ANIM_START_LISTENER));
                return;
            case 2:
            case 11:
            case 1001:
                this.mStartX = opts.getInt(KEY_ANIM_START_X, 0);
                this.mStartY = opts.getInt(KEY_ANIM_START_Y, 0);
                this.mWidth = opts.getInt(KEY_ANIM_WIDTH, 0);
                this.mHeight = opts.getInt(KEY_ANIM_HEIGHT, 0);
                return;
            case 3:
            case 4:
            case 8:
            case 9:
                this.mThumbnail = (Bitmap) opts.getParcelable(KEY_ANIM_THUMBNAIL);
                this.mStartX = opts.getInt(KEY_ANIM_START_X, 0);
                this.mStartY = opts.getInt(KEY_ANIM_START_Y, 0);
                this.mWidth = opts.getInt(KEY_ANIM_WIDTH, 0);
                this.mHeight = opts.getInt(KEY_ANIM_HEIGHT, 0);
                this.mAnimationStartedListener = Stub.asInterface(opts.getBinder(KEY_ANIM_START_LISTENER));
                this.mTransitionStartedListener = Stub.asInterface(opts.getBinder(KEY_TRANSIT_STARTED_LISTENER));
                return;
            case 5:
                this.mTransitionReceiver = (ResultReceiver) opts.getParcelable(KEY_TRANSITION_COMPLETE_LISTENER);
                this.mIsReturning = opts.getBoolean(KEY_TRANSITION_IS_RETURNING, false);
                this.mSharedElementNames = opts.getStringArrayList(KEY_TRANSITION_SHARED_ELEMENTS);
                this.mResultData = (Intent) opts.getParcelable(KEY_RESULT_DATA);
                this.mResultCode = opts.getInt(KEY_RESULT_CODE);
                this.mExitCoordinatorIndex = opts.getInt(KEY_EXIT_COORDINATOR_INDEX);
                return;
            case 10:
                this.mCustomInPlaceResId = opts.getInt(KEY_ANIM_IN_PLACE_RES_ID, 0);
                return;
            default:
                return;
        }
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getAnimationType() {
        return this.mAnimationType;
    }

    public int getCustomEnterResId() {
        return this.mCustomEnterResId;
    }

    public int getCustomExitResId() {
        return this.mCustomExitResId;
    }

    public int getCustomInPlaceResId() {
        return this.mCustomInPlaceResId;
    }

    public Bitmap getThumbnail() {
        return this.mThumbnail;
    }

    public int getStartX() {
        return this.mStartX;
    }

    public int getStartY() {
        return this.mStartY;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public IRemoteCallback getOnAnimationStartListener() {
        return this.mAnimationStartedListener;
    }

    public IRemoteCallback getOnTransitionStartedListener() {
        return this.mTransitionStartedListener;
    }

    public int getExitCoordinatorKey() {
        return this.mExitCoordinatorIndex;
    }

    public void abort() {
        if (this.mAnimationStartedListener != null) {
            try {
                this.mAnimationStartedListener.sendResult(null);
            } catch (RemoteException e) {
            }
        }
    }

    public boolean isReturning() {
        return this.mIsReturning;
    }

    public ArrayList<String> getSharedElementNames() {
        return this.mSharedElementNames;
    }

    public ResultReceiver getResultReceiver() {
        return this.mTransitionReceiver;
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public Intent getResultData() {
        return this.mResultData;
    }

    public PendingIntent getUsageTimeReport() {
        return this.mUsageTimeReport;
    }

    public static void abort(Bundle options) {
        if (options != null) {
            new ActivityOptions(options).abort();
        }
    }

    public void update(ActivityOptions otherOptions) {
        if (otherOptions.mPackageName != null) {
            this.mPackageName = otherOptions.mPackageName;
        }
        this.mUsageTimeReport = otherOptions.mUsageTimeReport;
        this.mTransitionReceiver = null;
        this.mSharedElementNames = null;
        this.mIsReturning = false;
        this.mResultData = null;
        this.mResultCode = 0;
        this.mExitCoordinatorIndex = 0;
        this.mAnimationType = otherOptions.mAnimationType;
        if (this.mTransitionStartedListener != null) {
            try {
                this.mTransitionStartedListener.sendResult(null);
            } catch (RemoteException e) {
            }
        }
        this.mTransitionStartedListener = null;
        switch (otherOptions.mAnimationType) {
            case 1:
                this.mCustomEnterResId = otherOptions.mCustomEnterResId;
                this.mCustomExitResId = otherOptions.mCustomExitResId;
                this.mThumbnail = null;
                if (this.mAnimationStartedListener != null) {
                    try {
                        this.mAnimationStartedListener.sendResult(null);
                    } catch (RemoteException e2) {
                    }
                }
                this.mAnimationStartedListener = otherOptions.mAnimationStartedListener;
                return;
            case 2:
            case 1001:
                this.mStartX = otherOptions.mStartX;
                this.mStartY = otherOptions.mStartY;
                this.mWidth = otherOptions.mWidth;
                this.mHeight = otherOptions.mHeight;
                if (this.mAnimationStartedListener != null) {
                    try {
                        this.mAnimationStartedListener.sendResult(null);
                    } catch (RemoteException e3) {
                    }
                }
                this.mAnimationStartedListener = null;
                return;
            case 3:
            case 4:
            case 8:
            case 9:
                this.mThumbnail = otherOptions.mThumbnail;
                this.mStartX = otherOptions.mStartX;
                this.mStartY = otherOptions.mStartY;
                this.mWidth = otherOptions.mWidth;
                this.mHeight = otherOptions.mHeight;
                if (this.mAnimationStartedListener != null) {
                    try {
                        this.mAnimationStartedListener.sendResult(null);
                    } catch (RemoteException e4) {
                    }
                }
                this.mAnimationStartedListener = otherOptions.mAnimationStartedListener;
                this.mTransitionStartedListener = otherOptions.mTransitionStartedListener;
                return;
            case 5:
                this.mTransitionReceiver = otherOptions.mTransitionReceiver;
                this.mSharedElementNames = otherOptions.mSharedElementNames;
                this.mIsReturning = otherOptions.mIsReturning;
                this.mThumbnail = null;
                this.mAnimationStartedListener = null;
                this.mResultData = otherOptions.mResultData;
                this.mResultCode = otherOptions.mResultCode;
                this.mExitCoordinatorIndex = otherOptions.mExitCoordinatorIndex;
                return;
            case 10:
                this.mCustomInPlaceResId = otherOptions.mCustomInPlaceResId;
                return;
            default:
                return;
        }
    }

    public Bundle toBundle() {
        IBinder iBinder = null;
        if (this.mAnimationType == 6) {
            return null;
        }
        Bundle b = new Bundle();
        if (this.mPackageName != null) {
            b.putString(KEY_PACKAGE_NAME, this.mPackageName);
        }
        b.putInt(KEY_ANIM_TYPE, this.mAnimationType);
        if (this.mUsageTimeReport != null) {
            b.putParcelable(KEY_USAGE_TIME_REPORT, this.mUsageTimeReport);
        }
        String str;
        switch (this.mAnimationType) {
            case 1:
                b.putInt(KEY_ANIM_ENTER_RES_ID, this.mCustomEnterResId);
                b.putInt(KEY_ANIM_EXIT_RES_ID, this.mCustomExitResId);
                str = KEY_ANIM_START_LISTENER;
                if (this.mAnimationStartedListener != null) {
                    iBinder = this.mAnimationStartedListener.asBinder();
                }
                b.putBinder(str, iBinder);
                break;
            case 2:
            case 11:
            case 1001:
                b.putInt(KEY_ANIM_START_X, this.mStartX);
                b.putInt(KEY_ANIM_START_Y, this.mStartY);
                b.putInt(KEY_ANIM_WIDTH, this.mWidth);
                b.putInt(KEY_ANIM_HEIGHT, this.mHeight);
                break;
            case 3:
            case 4:
            case 8:
            case 9:
                IBinder asBinder;
                b.putParcelable(KEY_ANIM_THUMBNAIL, this.mThumbnail);
                b.putInt(KEY_ANIM_START_X, this.mStartX);
                b.putInt(KEY_ANIM_START_Y, this.mStartY);
                b.putInt(KEY_ANIM_WIDTH, this.mWidth);
                b.putInt(KEY_ANIM_HEIGHT, this.mHeight);
                String str2 = KEY_ANIM_START_LISTENER;
                if (this.mAnimationStartedListener != null) {
                    asBinder = this.mAnimationStartedListener.asBinder();
                } else {
                    asBinder = null;
                }
                b.putBinder(str2, asBinder);
                str = KEY_TRANSIT_STARTED_LISTENER;
                if (this.mTransitionStartedListener != null) {
                    iBinder = this.mTransitionStartedListener.asBinder();
                }
                b.putBinder(str, iBinder);
                break;
            case 5:
                if (this.mTransitionReceiver != null) {
                    b.putParcelable(KEY_TRANSITION_COMPLETE_LISTENER, this.mTransitionReceiver);
                }
                b.putBoolean(KEY_TRANSITION_IS_RETURNING, this.mIsReturning);
                b.putStringArrayList(KEY_TRANSITION_SHARED_ELEMENTS, this.mSharedElementNames);
                b.putParcelable(KEY_RESULT_DATA, this.mResultData);
                b.putInt(KEY_RESULT_CODE, this.mResultCode);
                b.putInt(KEY_EXIT_COORDINATOR_INDEX, this.mExitCoordinatorIndex);
                break;
            case 10:
                b.putInt(KEY_ANIM_IN_PLACE_RES_ID, this.mCustomInPlaceResId);
                break;
        }
        return b;
    }

    public void requestUsageTimeReport(PendingIntent receiver) {
        this.mUsageTimeReport = receiver;
    }

    public ActivityOptions forTargetActivity() {
        if (this.mAnimationType != 5) {
            return null;
        }
        ActivityOptions result = new ActivityOptions();
        result.update(this);
        return result;
    }
}
