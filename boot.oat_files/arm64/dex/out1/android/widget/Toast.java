package android.widget;

import android.app.INotificationManager;
import android.app.ITransientNotification.Stub;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.sec.enterprise.content.SecContentProviderURI;
import android.text.Html;
import android.text.Spanned;
import android.util.GateConfig;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.R;
import com.samsung.android.smartface.SmartFaceManager;

public class Toast {
    static final boolean DEBUG;
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_LONG_DOUBLE = 1000;
    public static final int LENGTH_SHORT = 0;
    static final String TAG = "Toast";
    static final boolean localLOGV;
    private static INotificationManager sService;
    final Context mContext;
    int mDuration;
    View mNextView;
    final TN mTN = new TN();

    private static class TN extends Stub {
        int mGravity;
        final Handler mHandler = new Handler();
        final Runnable mHide = new Runnable() {
            public void run() {
                TN.this.handleHide();
                TN.this.mNextView = null;
            }
        };
        float mHorizontalMargin;
        View mNextView;
        private final LayoutParams mParams = new LayoutParams();
        final Runnable mShow = new Runnable() {
            public void run() {
                TN.this.handleShow();
            }
        };
        float mVerticalMargin;
        View mView;
        WindowManager mWM;
        int mX;
        int mY;

        TN() {
            LayoutParams params = this.mParams;
            params.height = -2;
            params.width = -2;
            params.format = -3;
            params.windowAnimations = R.style.Animation_Toast;
            params.type = LayoutParams.TYPE_TOAST;
            params.setTitle(Toast.TAG);
            params.flags = 262280;
        }

        public void show() {
            if (Toast.localLOGV) {
                Log.v(Toast.TAG, "SHOW: " + this);
            }
            this.mHandler.post(this.mShow);
        }

        public void hide() {
            if (Toast.localLOGV) {
                Log.v(Toast.TAG, "HIDE: " + this);
            }
            this.mHandler.post(this.mHide);
        }

        public void handleShow() {
            if (Toast.localLOGV) {
                Log.v(Toast.TAG, "HANDLE SHOW: " + this + " mView=" + this.mView + " mNextView=" + this.mNextView);
            }
            if (this.mView != this.mNextView) {
                handleHide();
                this.mView = this.mNextView;
                Context context = this.mView.getContext().getApplicationContext();
                String packageName = this.mView.getContext().getOpPackageName();
                if (context == null) {
                    context = this.mView.getContext();
                }
                this.mWM = (WindowManager) context.getSystemService("window");
                int gravity = Gravity.getAbsoluteGravity(this.mGravity, this.mView.getContext().getResources().getConfiguration().getLayoutDirection());
                this.mParams.gravity = gravity;
                if ((gravity & 7) == 7) {
                    this.mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & 112) == 112) {
                    this.mParams.verticalWeight = 1.0f;
                }
                this.mParams.x = this.mX;
                this.mParams.y = this.mY;
                this.mParams.verticalMargin = this.mVerticalMargin;
                this.mParams.horizontalMargin = this.mHorizontalMargin;
                this.mParams.packageName = packageName;
                if (Toast.localLOGV) {
                    Log.v(Toast.TAG, "HANDLE SHOW packageName: " + packageName);
                }
                if (this.mView.getParent() != null) {
                    if (Toast.localLOGV) {
                        Log.v(Toast.TAG, "REMOVE! " + this.mView + " in " + this);
                    }
                    this.mWM.removeView(this.mView);
                }
                if (Toast.localLOGV) {
                    Log.v(Toast.TAG, "ADD! " + this.mView + " in " + this);
                }
                this.mWM.addView(this.mView, this.mParams);
                trySendAccessibilityEvent();
            }
        }

        private void trySendAccessibilityEvent() {
            AccessibilityManager accessibilityManager = AccessibilityManager.getInstance(this.mView.getContext());
            if (accessibilityManager.isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(64);
                event.setClassName(getClass().getName());
                event.setPackageName(this.mView.getContext().getPackageName());
                this.mView.dispatchPopulateAccessibilityEvent(event);
                accessibilityManager.sendAccessibilityEvent(event);
            }
        }

        public void handleHide() {
            if (Toast.localLOGV) {
                Log.v(Toast.TAG, "HANDLE HIDE: " + this + " mView=" + this.mView);
            }
            if (this.mView != null) {
                if (this.mView.getParent() != null) {
                    if (Toast.localLOGV) {
                        Log.v(Toast.TAG, "REMOVE! " + this.mView + " in " + this);
                    }
                    this.mWM.removeView(this.mView);
                }
                this.mView = null;
            }
        }

        public void setShowForAllUsers() {
            LayoutParams layoutParams = this.mParams;
            layoutParams.privateFlags |= 16;
        }

        public void setIgnoreMultiWindowLayout() {
            LayoutParams layoutParams = this.mParams;
            layoutParams.multiWindowFlags |= 8;
        }
    }

    static {
        boolean z;
        boolean z2 = false;
        if (Debug.isProductShip() == 1) {
            z = false;
        } else {
            z = true;
        }
        localLOGV = z;
        if (Debug.isProductShip() != 1) {
            z2 = true;
        }
        DEBUG = z2;
    }

    public Toast(Context context) {
        this.mContext = context;
        this.mTN.mY = context.getResources().getDimensionPixelSize(R.dimen.toast_y_offset);
        this.mTN.mGravity = context.getResources().getInteger(R.integer.config_toastDefaultGravity);
        Uri uri = Uri.parse(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE2_URI);
        Cursor cursorState = this.mContext.getContentResolver().query(uri, null, SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITYENABLEDSTATE_METHOD, null, null);
        if (cursorState != null) {
            Cursor cursorGravity = this.mContext.getContentResolver().query(uri, null, SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITY_METHOD, null, null);
            Cursor cursorX = this.mContext.getContentResolver().query(uri, null, SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITYXOFFSET_METHOD, null, null);
            Cursor cursorY = this.mContext.getContentResolver().query(uri, null, SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITYYOFFSET_METHOD, null, null);
            try {
                cursorState.moveToFirst();
                if (cursorState.getString(cursorState.getColumnIndex(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITYENABLEDSTATE_METHOD)).equals(SmartFaceManager.TRUE)) {
                    Log.d(TAG, "Knox Customization: Using custom gravity");
                    cursorGravity.moveToFirst();
                    int gravity = cursorGravity.getInt(cursorGravity.getColumnIndex(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITY_METHOD));
                    if (gravity != 0) {
                        this.mTN.mGravity = gravity;
                    }
                    cursorX.moveToFirst();
                    int x = cursorX.getInt(cursorX.getColumnIndex(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITYXOFFSET_METHOD));
                    if (x != 0) {
                        this.mTN.mX = x;
                    }
                    cursorY.moveToFirst();
                    int y = cursorY.getInt(cursorY.getColumnIndex(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTGRAVITYYOFFSET_METHOD));
                    if (y != 0) {
                        this.mTN.mY = y;
                    }
                }
                cursorState.close();
                cursorGravity.close();
                cursorX.close();
                cursorY.close();
            } catch (Throwable th) {
                cursorState.close();
                cursorGravity.close();
                cursorX.close();
                cursorY.close();
            }
        }
    }

    public void show() {
        Cursor cursorState = this.mContext.getContentResolver().query(Uri.parse(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE2_URI), null, SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTENABLEDSTATE_METHOD, null, null);
        if (cursorState != null) {
            try {
                cursorState.moveToFirst();
                if (cursorState.getString(cursorState.getColumnIndex(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTENABLEDSTATE_METHOD)).equals(SmartFaceManager.TRUE)) {
                    cursorState.close();
                } else {
                    Log.d(TAG, "Knox Customization: Not showing toast");
                    cursorState.close();
                    return;
                }
            } finally {
                cursorState.close();
            }
        }
        if (checkShowingCondition()) {
            if (DEBUG) {
                Log.d(TAG, "showing allowed");
            }
            if (!checkGameHomeWhiteList()) {
                if (this.mNextView == null) {
                    throw new RuntimeException("setView must have been called");
                }
                TextView tv;
                cursorState = this.mContext.getContentResolver().query(Uri.parse(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE2_URI), null, SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTSHOWPACKAGENAMESTATE_METHOD, null, null);
                if (cursorState != null) {
                    String appName;
                    TextView textView;
                    try {
                        cursorState.moveToFirst();
                        if (cursorState.getString(cursorState.getColumnIndex(SecContentProviderURI.KNOXCUSTOMMANAGERSERVICE_TOASTSHOWPACKAGENAMESTATE_METHOD)).equals(SmartFaceManager.TRUE) && this.mNextView != null) {
                            appName = this.mContext.getPackageManager().getApplicationLabel(this.mContext.getApplicationInfo()).toString();
                            tv = (TextView) this.mNextView.findViewById(R.id.message);
                            if (!(tv == null || appName == null || tv.getText().toString().startsWith(appName))) {
                                String oldText = Html.toHtml((Spanned) tv.getText());
                                int idx1 = oldText.indexOf(62);
                                String str = oldText;
                                textView = tv;
                                textView.setText(Html.fromHtml(appName + ": " + str.substring(idx1 + 1, oldText.lastIndexOf(60))));
                            }
                        }
                    } catch (Exception e) {
                        textView = tv;
                        textView.setText(appName + ": " + tv.getText().toString());
                    } catch (Throwable th) {
                        cursorState.close();
                    }
                    cursorState.close();
                }
                INotificationManager service = getService();
                String pkg = this.mContext.getOpPackageName();
                TN tn = this.mTN;
                tn.mNextView = this.mNextView;
                try {
                    service.enqueueToast(pkg, tn, this.mDuration);
                } catch (RemoteException e2) {
                }
                if (GateConfig.isGateEnabled() && this.mNextView != null) {
                    tv = (TextView) this.mNextView.findViewById(R.id.message);
                    if (tv != null) {
                        Log.i("GATE", "<GATE-M>Toast:" + tv.getText() + "</GATE-M>");
                    }
                }
            }
        } else if (DEBUG) {
            Log.d(TAG, "showing not allowed");
        }
    }

    public void cancel() {
        this.mTN.hide();
        try {
            getService().cancelToast(this.mContext.getPackageName(), this.mTN);
        } catch (RemoteException e) {
        }
    }

    public void setView(View view) {
        View v = view;
        v.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == 0 || event.getAction() == 4) {
                    Toast.this.mTN.hide();
                    try {
                        Toast.getService().cancelToast(Toast.this.mContext.getPackageName(), Toast.this.mTN);
                    } catch (RemoteException e) {
                    }
                }
                return true;
            }
        });
        this.mNextView = v;
    }

    public View getView() {
        return this.mNextView;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        this.mTN.mHorizontalMargin = horizontalMargin;
        this.mTN.mVerticalMargin = verticalMargin;
    }

    public float getHorizontalMargin() {
        return this.mTN.mHorizontalMargin;
    }

    public float getVerticalMargin() {
        return this.mTN.mVerticalMargin;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        this.mTN.mGravity = gravity;
        this.mTN.mX = xOffset;
        this.mTN.mY = yOffset;
    }

    public int getGravity() {
        return this.mTN.mGravity;
    }

    public int getXOffset() {
        return this.mTN.mX;
    }

    public int getYOffset() {
        return this.mTN.mY;
    }

    public LayoutParams getWindowParams() {
        return this.mTN.mParams;
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        View v;
        boolean isDeviceDefault = true;
        final Toast result = new Toast(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService("layout_inflater");
        TypedValue outValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true) || outValue.data == 0) {
            isDeviceDefault = false;
        }
        if (isDeviceDefault) {
            v = inflate.inflate((int) R.layout.tw_transient_notification, null);
        } else {
            v = inflate.inflate((int) R.layout.transient_notification, null);
        }
        v.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == 0 || event.getAction() == 4) {
                    result.cancel();
                }
                return true;
            }
        });
        ((TextView) v.findViewById(R.id.message)).setText(text);
        result.mNextView = v;
        result.mDuration = duration;
        return result;
    }

    public static Toast twMakeText(Context context, CharSequence text, int duration) {
        final Toast result = new Toast(context);
        View v = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate((int) R.layout.tw_transient_notification_actionbar, null);
        v.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == 0 || event.getAction() == 4) {
                    result.cancel();
                }
                return true;
            }
        });
        ((TextView) v.findViewById(R.id.message)).setText(text);
        result.mNextView = v;
        result.mDuration = duration;
        return result;
    }

    public static Toast makeText(Context context, int resId, int duration) throws NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public void setText(int resId) {
        setText(this.mContext.getText(resId));
    }

    public void setText(CharSequence s) {
        if (this.mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) this.mNextView.findViewById(R.id.message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    private boolean checkShowingCondition() {
        if (checkMirrorLinkEnabled()) {
            return checkWhiteList();
        }
        return true;
    }

    private boolean checkMirrorLinkEnabled() {
        boolean mirrorLinkOn = false;
        if (SystemProperties.get("net.mirrorlink.on").equals(SmartFaceManager.PAGE_BOTTOM)) {
            mirrorLinkOn = true;
        }
        if (DEBUG) {
            Log.d(TAG, " checkMirrorLinkEnabled returns : " + mirrorLinkOn);
        }
        return mirrorLinkOn;
    }

    private boolean checkWhiteList() {
        String BASE_PATH_PKGNAME = "pkgname";
        Uri CONTENT_URI_PKGNAMES = Uri.parse(SecContentProviderURI.CONTENT + "com.samsung.mirrorlink.acms.pkgnames" + "/" + BASE_PATH_PKGNAME);
        int res = this.mContext.checkCallingOrSelfPermission("com.mirrorlink.android.service.ACCESS_PERMISSION");
        Log.d(TAG, "Check Access Permission  : res = " + res);
        if (res != 0) {
            return false;
        }
        String currentPackageName = this.mContext.getPackageName();
        Cursor cursor = this.mContext.getContentResolver().query(CONTENT_URI_PKGNAMES, new String[]{BASE_PATH_PKGNAME}, BASE_PATH_PKGNAME + "=?", new String[]{currentPackageName}, null);
        if (cursor == null) {
            Log.d(TAG, "Cursor is null");
            return false;
        }
        boolean z;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.d(TAG, "currentPackageName =  " + currentPackageName);
            Log.d(TAG, "db column packagename =" + cursor.getString(cursor.getColumnIndex(BASE_PATH_PKGNAME)));
            z = true;
        } else {
            z = false;
        }
        cursor.close();
        return z;
    }

    private boolean checkGameHomeWhiteList() {
        if (Settings$System.getInt(this.mContext.getContentResolver(), "game_no_interruption", 0) <= 0) {
            return false;
        }
        String whitelist = Settings$System.getString(this.mContext.getContentResolver(), "game_no_interruption_white_list");
        if (whitelist == null) {
            Log.d(TAG, "gameNoInterruption is on, but whitelist is null.");
            return false;
        } else if (whitelist.contains(this.mContext.getPackageName())) {
            Log.d(TAG, "GameNoInterruption mode. Show game toast. " + whitelist);
            return false;
        } else {
            Log.d(TAG, "GameNoInterruption mode. Block toast " + whitelist);
            return true;
        }
    }

    public void setShowForAllUsers() {
        this.mTN.setShowForAllUsers();
    }

    public void setIgnoreMultiWindowLayout() {
        this.mTN.setIgnoreMultiWindowLayout();
    }

    private static INotificationManager getService() {
        if (sService != null) {
            return sService;
        }
        sService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        return sService;
    }
}
