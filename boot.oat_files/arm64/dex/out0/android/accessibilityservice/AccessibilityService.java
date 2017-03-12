package android.accessibilityservice;

import android.accessibilityservice.IAccessibilityServiceClient.Stub;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import java.util.List;

public abstract class AccessibilityService extends Service {
    public static final int GESTURE_SWIPE_DOWN = 2;
    public static final int GESTURE_SWIPE_DOWN_AND_LEFT = 15;
    public static final int GESTURE_SWIPE_DOWN_AND_RIGHT = 16;
    public static final int GESTURE_SWIPE_DOWN_AND_UP = 8;
    public static final int GESTURE_SWIPE_LEFT = 3;
    public static final int GESTURE_SWIPE_LEFT_AND_DOWN = 10;
    public static final int GESTURE_SWIPE_LEFT_AND_RIGHT = 5;
    public static final int GESTURE_SWIPE_LEFT_AND_UP = 9;
    public static final int GESTURE_SWIPE_RIGHT = 4;
    public static final int GESTURE_SWIPE_RIGHT_AND_DOWN = 12;
    public static final int GESTURE_SWIPE_RIGHT_AND_LEFT = 6;
    public static final int GESTURE_SWIPE_RIGHT_AND_UP = 11;
    public static final int GESTURE_SWIPE_UP = 1;
    public static final int GESTURE_SWIPE_UP_AND_DOWN = 7;
    public static final int GESTURE_SWIPE_UP_AND_LEFT = 13;
    public static final int GESTURE_SWIPE_UP_AND_RIGHT = 14;
    public static final int GLOBAL_ACTION_BACK = 1;
    public static final int GLOBAL_ACTION_HOME = 2;
    public static final int GLOBAL_ACTION_NOTIFICATIONS = 4;
    public static final int GLOBAL_ACTION_POWER_DIALOG = 6;
    public static final int GLOBAL_ACTION_QUICK_SETTINGS = 5;
    public static final int GLOBAL_ACTION_RECENTS = 3;
    private static final String LOG_TAG = "AccessibilityService";
    public static final String SERVICE_INTERFACE = "android.accessibilityservice.AccessibilityService";
    public static final String SERVICE_META_DATA = "android.accessibilityservice";
    public static final int THREE_FINGER_DOUBLE_TAP = 36;
    public static final int THREE_FINGER_DOUBLE_TAP_AND_HOLD = 38;
    public static final int THREE_FINGER_GESTURE_SWIPE_DOWN = 26;
    public static final int THREE_FINGER_GESTURE_SWIPE_DOWN_AND_UP = 32;
    public static final int THREE_FINGER_GESTURE_SWIPE_LEFT = 27;
    public static final int THREE_FINGER_GESTURE_SWIPE_LEFT_AND_RIGHT = 29;
    public static final int THREE_FINGER_GESTURE_SWIPE_RIGHT = 28;
    public static final int THREE_FINGER_GESTURE_SWIPE_RIGHT_AND_LEFT = 30;
    public static final int THREE_FINGER_GESTURE_SWIPE_UP = 25;
    public static final int THREE_FINGER_GESTURE_SWIPE_UP_AND_DOWN = 31;
    public static final int THREE_FINGER_TAP = 34;
    public static final int THREE_FINGER_TAP_AND_HOLD = 41;
    public static final int THREE_FINGER_TRIPLE_TAP = 40;
    public static final int TWO_FINGER_DOUBLE_TAP = 35;
    public static final int TWO_FINGER_DOUBLE_TAP_AND_HOLD = 37;
    public static final int TWO_FINGER_GESTURE_SWIPE_DOWN = 18;
    public static final int TWO_FINGER_GESTURE_SWIPE_DOWN_AND_UP = 24;
    public static final int TWO_FINGER_GESTURE_SWIPE_LEFT = 19;
    public static final int TWO_FINGER_GESTURE_SWIPE_LEFT_AND_RIGHT = 21;
    public static final int TWO_FINGER_GESTURE_SWIPE_RIGHT = 20;
    public static final int TWO_FINGER_GESTURE_SWIPE_RIGHT_AND_LEFT = 22;
    public static final int TWO_FINGER_GESTURE_SWIPE_UP = 17;
    public static final int TWO_FINGER_GESTURE_SWIPE_UP_AND_DOWN = 23;
    public static final int TWO_FINGER_TAP = 33;
    public static final int TWO_FINGER_TRIPLE_TAP = 39;
    private int mConnectionId;
    private AccessibilityServiceInfo mInfo;
    private WindowManager mWindowManager;
    private IBinder mWindowToken;

    public interface Callbacks {
        void init(int i, IBinder iBinder);

        void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        boolean onGesture(int i);

        void onInterrupt();

        boolean onKeyEvent(KeyEvent keyEvent);

        void onServiceConnected();
    }

    public static class IAccessibilityServiceClientWrapper extends Stub implements Callback {
        private static final int DO_CLEAR_ACCESSIBILITY_CACHE = 5;
        private static final int DO_INIT = 1;
        private static final int DO_ON_ACCESSIBILITY_EVENT = 3;
        private static final int DO_ON_GESTURE = 4;
        private static final int DO_ON_INTERRUPT = 2;
        private static final int DO_ON_KEY_EVENT = 6;
        private final Callbacks mCallback;
        private final HandlerCaller mCaller;
        private int mConnectionId;

        public IAccessibilityServiceClientWrapper(Context context, Looper looper, Callbacks callback) {
            this.mCallback = callback;
            this.mCaller = new HandlerCaller(context, looper, this, true);
        }

        public void init(IAccessibilityServiceConnection connection, int connectionId, IBinder windowToken) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageIOO(1, connectionId, connection, windowToken));
        }

        public void onInterrupt() {
            this.mCaller.sendMessage(this.mCaller.obtainMessage(2));
        }

        public void onAccessibilityEvent(AccessibilityEvent event) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageO(3, event));
        }

        public void onGesture(int gestureId) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(4, gestureId));
        }

        public void clearAccessibilityCache() {
            this.mCaller.sendMessage(this.mCaller.obtainMessage(5));
        }

        public void onKeyEvent(KeyEvent event, int sequence) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageIO(6, sequence, event));
        }

        public void executeMessage(Message message) {
            IAccessibilityServiceConnection connection;
            switch (message.what) {
                case 1:
                    this.mConnectionId = message.arg1;
                    SomeArgs args = message.obj;
                    connection = args.arg1;
                    IBinder windowToken = args.arg2;
                    args.recycle();
                    if (connection != null) {
                        AccessibilityInteractionClient.getInstance().addConnection(this.mConnectionId, connection);
                        this.mCallback.init(this.mConnectionId, windowToken);
                        this.mCallback.onServiceConnected();
                        return;
                    }
                    AccessibilityInteractionClient.getInstance().removeConnection(this.mConnectionId);
                    this.mConnectionId = -1;
                    AccessibilityInteractionClient.getInstance().clearCache();
                    this.mCallback.init(-1, null);
                    return;
                case 2:
                    this.mCallback.onInterrupt();
                    return;
                case 3:
                    AccessibilityEvent event = message.obj;
                    if (event != null) {
                        AccessibilityInteractionClient.getInstance().onAccessibilityEvent(event);
                        this.mCallback.onAccessibilityEvent(event);
                        try {
                            event.recycle();
                            return;
                        } catch (IllegalStateException e) {
                            return;
                        }
                    }
                    return;
                case 4:
                    this.mCallback.onGesture(message.arg1);
                    return;
                case 5:
                    AccessibilityInteractionClient.getInstance().clearCache();
                    return;
                case 6:
                    KeyEvent event2 = message.obj;
                    try {
                        connection = AccessibilityInteractionClient.getInstance().getConnection(this.mConnectionId);
                        if (connection != null) {
                            try {
                                connection.setOnKeyEventResult(this.mCallback.onKeyEvent(event2), message.arg1);
                            } catch (RemoteException e2) {
                            }
                        }
                        return;
                    } finally {
                        try {
                            event2.recycle();
                        } catch (IllegalStateException e3) {
                        }
                    }
                default:
                    Log.w(AccessibilityService.LOG_TAG, "Unknown message type " + message.what);
                    return;
            }
        }
    }

    public abstract void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);

    public abstract void onInterrupt();

    protected void onServiceConnected() {
    }

    protected boolean onGesture(int gestureId) {
        return false;
    }

    protected boolean onKeyEvent(KeyEvent event) {
        return false;
    }

    public List<AccessibilityWindowInfo> getWindows() {
        return AccessibilityInteractionClient.getInstance().getWindows(this.mConnectionId);
    }

    public AccessibilityNodeInfo getRootInActiveWindow() {
        return AccessibilityInteractionClient.getInstance().getRootInActiveWindow(this.mConnectionId);
    }

    public final boolean performGlobalAction(int action) {
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getInstance().getConnection(this.mConnectionId);
        if (connection != null) {
            try {
                return connection.performGlobalAction(action);
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while calling performGlobalAction", re);
            }
        }
        return false;
    }

    public AccessibilityNodeInfo findFocus(int focus) {
        return AccessibilityInteractionClient.getInstance().findFocus(this.mConnectionId, -2, AccessibilityNodeInfo.ROOT_NODE_ID, focus);
    }

    public final AccessibilityServiceInfo getServiceInfo() {
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getInstance().getConnection(this.mConnectionId);
        if (connection != null) {
            try {
                return connection.getServiceInfo();
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while getting AccessibilityServiceInfo", re);
            }
        }
        return null;
    }

    public final void setServiceInfo(AccessibilityServiceInfo info) {
        this.mInfo = info;
        sendServiceInfo();
    }

    private void sendServiceInfo() {
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getInstance().getConnection(this.mConnectionId);
        if (this.mInfo != null && connection != null) {
            try {
                connection.setServiceInfo(this.mInfo);
                this.mInfo = null;
                AccessibilityInteractionClient.getInstance().clearCache();
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while setting AccessibilityServiceInfo", re);
            }
        }
    }

    public Object getSystemService(String name) {
        if (getBaseContext() == null) {
            throw new IllegalStateException("System services not available to Activities before onCreate()");
        } else if (!Context.WINDOW_SERVICE.equals(name)) {
            return super.getSystemService(name);
        } else {
            if (this.mWindowManager == null) {
                this.mWindowManager = (WindowManager) getBaseContext().getSystemService(name);
            }
            return this.mWindowManager;
        }
    }

    public final IBinder onBind(Intent intent) {
        return new IAccessibilityServiceClientWrapper(this, getMainLooper(), new Callbacks() {
            public void onServiceConnected() {
                AccessibilityService.this.onServiceConnected();
            }

            public void onInterrupt() {
                AccessibilityService.this.onInterrupt();
            }

            public void onAccessibilityEvent(AccessibilityEvent event) {
                AccessibilityService.this.onAccessibilityEvent(event);
            }

            public void init(int connectionId, IBinder windowToken) {
                AccessibilityService.this.mConnectionId = connectionId;
                AccessibilityService.this.mWindowToken = windowToken;
                ((WindowManagerImpl) AccessibilityService.this.getSystemService(Context.WINDOW_SERVICE)).setDefaultToken(windowToken);
            }

            public boolean onGesture(int gestureId) {
                return AccessibilityService.this.onGesture(gestureId);
            }

            public boolean onKeyEvent(KeyEvent event) {
                return AccessibilityService.this.onKeyEvent(event);
            }
        });
    }
}
