package android.view.inputmethod;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.sec.clipboard.IClipboardService;
import android.telephony.SubscriptionManager;
import android.text.style.SuggestionSpan;
import android.util.Log;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventSender;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewRootImpl;
import android.widget.EditText;
import android.widget.TextView;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputConnectionWrapper;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodClient;
import com.android.internal.view.IInputMethodClient.Stub;
import com.android.internal.view.IInputMethodManager;
import com.android.internal.view.IInputMethodSession;
import com.android.internal.view.InputBindResult;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class InputMethodManager {
    public static final int CONTROL_START_INITIAL = 256;
    public static final int CONTROL_WINDOW_FIRST = 4;
    public static final int CONTROL_WINDOW_IS_TEXT_EDITOR = 2;
    public static final int CONTROL_WINDOW_IS_WRITING_BUDDY_SHOWN = 65536;
    public static final int CONTROL_WINDOW_VIEW_HAS_FOCUS = 1;
    static final boolean DEBUG = false;
    static final boolean DEBUG_SIMPLE_LOG;
    static final boolean DEBUG_TRACE_HIDEIME = false;
    static final boolean DEBUG_TRACE_SHOWIME = false;
    public static final int DISPATCH_HANDLED = 1;
    public static final int DISPATCH_IN_PROGRESS = -1;
    public static final int DISPATCH_NOT_HANDLED = 0;
    public static final int HIDE_IMPLICIT_ONLY = 1;
    public static final int HIDE_NOT_ALWAYS = 2;
    static final long INPUT_METHOD_NOT_RESPONDING_TIMEOUT = 2500;
    static final int MSG_BIND = 2;
    static final int MSG_DUMP = 1;
    static final int MSG_FLUSH_INPUT_EVENT = 7;
    static final int MSG_HIDE_MOBILE_KEYBOARD = 101;
    static final int MSG_SEND_INPUT_EVENT = 5;
    static final int MSG_SET_ACTIVE = 4;
    static final int MSG_SET_USER_ACTION_NOTIFICATION_SEQUENCE_NUMBER = 9;
    static final int MSG_SHOW_MOBILE_KEYBOARD = 100;
    static final int MSG_TIMEOUT_INPUT_EVENT = 6;
    static final int MSG_UNBIND = 3;
    private static final int NOT_AN_ACTION_NOTIFICATION_SEQUENCE_NUMBER = -1;
    static final String PENDING_EVENT_COUNTER = "aq:imm";
    public static final int PRIVATE_FLAG_CHECK_FOCUS_FORCED = 4;
    private static final int REQUEST_UPDATE_CURSOR_ANCHOR_INFO_NONE = 0;
    public static final int RESULT_HIDDEN = 3;
    public static final int RESULT_SHOWN = 2;
    public static final int RESULT_UNCHANGED_HIDDEN = 1;
    public static final int RESULT_UNCHANGED_SHOWN = 0;
    public static final int SHOW_FORCED = 2;
    public static final int SHOW_IMPLICIT = 1;
    public static final int SHOW_IM_PICKER_MODE_AUTO = 0;
    public static final int SHOW_IM_PICKER_MODE_EXCLUDE_AUXILIARY_SUBTYPES = 2;
    public static final int SHOW_IM_PICKER_MODE_INCLUDE_AUXILIARY_SUBTYPES = 1;
    static final String TAG = "InputMethodManager";
    static InputMethodManager sInstance;
    private final int STATE_MOBILE_KEYBOARD_HIDE = 2;
    private final int STATE_MOBILE_KEYBOARD_NONE = 0;
    private final int STATE_MOBILE_KEYBOARD_SHOW = 1;
    boolean mActive = false;
    int mBindSequence = -1;
    final Stub mClient = new Stub() {
        protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
            CountDownLatch latch = new CountDownLatch(1);
            SomeArgs sargs = SomeArgs.obtain();
            sargs.arg1 = fd;
            sargs.arg2 = fout;
            sargs.arg3 = args;
            sargs.arg4 = latch;
            InputMethodManager.this.mH.sendMessage(InputMethodManager.this.mH.obtainMessage(1, sargs));
            try {
                if (!latch.await(5, TimeUnit.SECONDS)) {
                    fout.println("Timeout waiting for dump");
                }
            } catch (InterruptedException e) {
                fout.println("Interrupted waiting for dump");
            }
        }

        public void setUsingInputMethod(boolean state) {
        }

        public void onBindMethod(InputBindResult res) {
            InputMethodManager.this.mH.sendMessage(InputMethodManager.this.mH.obtainMessage(2, res));
        }

        public void onUnbindMethod(int sequence) {
            InputMethodManager.this.mH.sendMessage(InputMethodManager.this.mH.obtainMessage(3, sequence, 0));
        }

        public void setActive(boolean active) {
            int i;
            H h = InputMethodManager.this.mH;
            H h2 = InputMethodManager.this.mH;
            if (active) {
                i = 1;
            } else {
                i = 0;
            }
            h.sendMessage(h2.obtainMessage(4, i, 0));
        }

        public void setUserActionNotificationSequenceNumber(int sequenceNumber) {
            InputMethodManager.this.mH.sendMessage(InputMethodManager.this.mH.obtainMessage(9, sequenceNumber, 0));
        }
    };
    CompletionInfo[] mCompletions;
    InputChannel mCurChannel;
    String mCurId;
    IInputMethodSession mCurMethod;
    View mCurRootView;
    ImeInputEventSender mCurSender;
    EditorInfo mCurrentTextBoxAttribute;
    private CursorAnchorInfo mCursorAnchorInfo = null;
    int mCursorCandEnd;
    int mCursorCandStart;
    Rect mCursorRect = new Rect();
    int mCursorSelEnd;
    int mCursorSelStart;
    final InputConnection mDummyInputConnection = new BaseInputConnection(this, false);
    boolean mFullscreenMode;
    final H mH;
    boolean mHasBeenInactive = true;
    final IInputContext mIInputContext;
    private boolean mIsCheckedFocusInView;
    private int mLastSentUserActionNotificationSequenceNumber = -1;
    final Looper mMainLooper;
    private int mMobileKeyboardState = 0;
    View mNextServedView;
    private int mNextUserActionNotificationSequenceNumber = -1;
    final Pool<PendingEvent> mPendingEventPool = new SimplePool(20);
    final SparseArray<PendingEvent> mPendingEvents = new SparseArray(20);
    private int mRequestUpdateCursorAnchorInfoMonitorMode = 0;
    boolean mServedConnecting;
    ControlledInputConnectionWrapper mServedContext;
    InputConnection mServedInputConnection;
    ControlledInputConnectionWrapper mServedInputConnectionWrapper;
    View mServedView;
    final IInputMethodManager mService;
    EditorInfo mTba;
    Rect mTmpCursorRect = new Rect();
    private IClipboardService sService = null;

    public interface FinishedInputEventCallback {
        void onFinishedInputEvent(Object obj, boolean z);
    }

    private static class ControlledInputConnectionWrapper extends IInputConnectionWrapper {
        private boolean mActive = true;
        private final InputMethodManager mParentInputMethodManager;

        public ControlledInputConnectionWrapper(Looper mainLooper, InputConnection conn, InputMethodManager inputMethodManager) {
            super(mainLooper, conn);
            this.mParentInputMethodManager = inputMethodManager;
        }

        public boolean isActive() {
            return this.mParentInputMethodManager.mActive && this.mActive;
        }

        void deactivate() {
            this.mActive = false;
        }
    }

    class H extends Handler {
        H(Looper looper) {
            super(looper, null, true);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r13) {
            /*
            r12 = this;
            r0 = 1;
            r10 = 0;
            r6 = 0;
            r7 = r13.what;
            switch(r7) {
                case 1: goto L_0x0009;
                case 2: goto L_0x004e;
                case 3: goto L_0x00cb;
                case 4: goto L_0x0112;
                case 5: goto L_0x0195;
                case 6: goto L_0x01a0;
                case 7: goto L_0x01a9;
                case 9: goto L_0x01b2;
                case 100: goto L_0x01c4;
                case 101: goto L_0x01d6;
                default: goto L_0x0008;
            };
        L_0x0008:
            return;
        L_0x0009:
            r1 = r13.obj;
            r1 = (com.android.internal.os.SomeArgs) r1;
            r9 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RuntimeException -> 0x002f }
            r6 = r1.arg1;	 Catch:{ RuntimeException -> 0x002f }
            r6 = (java.io.FileDescriptor) r6;	 Catch:{ RuntimeException -> 0x002f }
            r7 = r1.arg2;	 Catch:{ RuntimeException -> 0x002f }
            r7 = (java.io.PrintWriter) r7;	 Catch:{ RuntimeException -> 0x002f }
            r8 = r1.arg3;	 Catch:{ RuntimeException -> 0x002f }
            r8 = (java.lang.String[]) r8;	 Catch:{ RuntimeException -> 0x002f }
            r8 = (java.lang.String[]) r8;	 Catch:{ RuntimeException -> 0x002f }
            r9.doDump(r6, r7, r8);	 Catch:{ RuntimeException -> 0x002f }
        L_0x0020:
            r7 = r1.arg4;
            monitor-enter(r7);
            r6 = r1.arg4;	 Catch:{ all -> 0x004b }
            r6 = (java.util.concurrent.CountDownLatch) r6;	 Catch:{ all -> 0x004b }
            r6.countDown();	 Catch:{ all -> 0x004b }
            monitor-exit(r7);	 Catch:{ all -> 0x004b }
            r1.recycle();
            goto L_0x0008;
        L_0x002f:
            r2 = move-exception;
            r6 = r1.arg2;
            r6 = (java.io.PrintWriter) r6;
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r8 = "Exception: ";
            r7 = r7.append(r8);
            r7 = r7.append(r2);
            r7 = r7.toString();
            r6.println(r7);
            goto L_0x0020;
        L_0x004b:
            r6 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x004b }
            throw r6;
        L_0x004e:
            r3 = r13.obj;
            r3 = (com.android.internal.view.InputBindResult) r3;
            r7 = android.view.inputmethod.InputMethodManager.this;
            r7 = r7.mH;
            monitor-enter(r7);
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r8 = r8.mBindSequence;	 Catch:{ all -> 0x00a1 }
            if (r8 < 0) goto L_0x0065;
        L_0x005d:
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r8 = r8.mBindSequence;	 Catch:{ all -> 0x00a1 }
            r9 = r3.sequence;	 Catch:{ all -> 0x00a1 }
            if (r8 == r9) goto L_0x00a4;
        L_0x0065:
            r6 = "InputMethodManager";
            r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a1 }
            r8.<init>();	 Catch:{ all -> 0x00a1 }
            r9 = "Ignoring onBind: cur seq=";
            r8 = r8.append(r9);	 Catch:{ all -> 0x00a1 }
            r9 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r9 = r9.mBindSequence;	 Catch:{ all -> 0x00a1 }
            r8 = r8.append(r9);	 Catch:{ all -> 0x00a1 }
            r9 = ", given seq=";
            r8 = r8.append(r9);	 Catch:{ all -> 0x00a1 }
            r9 = r3.sequence;	 Catch:{ all -> 0x00a1 }
            r8 = r8.append(r9);	 Catch:{ all -> 0x00a1 }
            r8 = r8.toString();	 Catch:{ all -> 0x00a1 }
            android.util.Log.w(r6, r8);	 Catch:{ all -> 0x00a1 }
            r6 = r3.channel;	 Catch:{ all -> 0x00a1 }
            if (r6 == 0) goto L_0x009e;
        L_0x0091:
            r6 = r3.channel;	 Catch:{ all -> 0x00a1 }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r8 = r8.mCurChannel;	 Catch:{ all -> 0x00a1 }
            if (r6 == r8) goto L_0x009e;
        L_0x0099:
            r6 = r3.channel;	 Catch:{ all -> 0x00a1 }
            r6.dispose();	 Catch:{ all -> 0x00a1 }
        L_0x009e:
            monitor-exit(r7);	 Catch:{ all -> 0x00a1 }
            goto L_0x0008;
        L_0x00a1:
            r6 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x00a1 }
            throw r6;
        L_0x00a4:
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r9 = 0;
            r8.mRequestUpdateCursorAnchorInfoMonitorMode = r9;	 Catch:{ all -> 0x00a1 }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r9 = r3.channel;	 Catch:{ all -> 0x00a1 }
            r8.setInputChannelLocked(r9);	 Catch:{ all -> 0x00a1 }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r9 = r3.method;	 Catch:{ all -> 0x00a1 }
            r8.mCurMethod = r9;	 Catch:{ all -> 0x00a1 }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r9 = r3.id;	 Catch:{ all -> 0x00a1 }
            r8.mCurId = r9;	 Catch:{ all -> 0x00a1 }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x00a1 }
            r9 = r3.sequence;	 Catch:{ all -> 0x00a1 }
            r8.mBindSequence = r9;	 Catch:{ all -> 0x00a1 }
            monitor-exit(r7);	 Catch:{ all -> 0x00a1 }
            r7 = android.view.inputmethod.InputMethodManager.this;
            r7.startInputInner(r10, r6, r6, r6);
            goto L_0x0008;
        L_0x00cb:
            r4 = r13.arg1;
            r5 = 0;
            r7 = android.view.inputmethod.InputMethodManager.this;
            r7 = r7.mH;
            monitor-enter(r7);
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x010f }
            r8 = r8.mBindSequence;	 Catch:{ all -> 0x010f }
            if (r8 != r4) goto L_0x00fa;
        L_0x00d9:
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x010f }
            r8.clearBindingLocked();	 Catch:{ all -> 0x010f }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x010f }
            r8 = r8.mServedView;	 Catch:{ all -> 0x010f }
            if (r8 == 0) goto L_0x00f3;
        L_0x00e4:
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x010f }
            r8 = r8.mServedView;	 Catch:{ all -> 0x010f }
            r8 = r8.isFocused();	 Catch:{ all -> 0x010f }
            if (r8 == 0) goto L_0x00f3;
        L_0x00ee:
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x010f }
            r9 = 1;
            r8.mServedConnecting = r9;	 Catch:{ all -> 0x010f }
        L_0x00f3:
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x010f }
            r8 = r8.mActive;	 Catch:{ all -> 0x010f }
            if (r8 == 0) goto L_0x00fa;
        L_0x00f9:
            r5 = 1;
        L_0x00fa:
            monitor-exit(r7);	 Catch:{ all -> 0x010f }
            if (r5 == 0) goto L_0x0008;
        L_0x00fd:
            r7 = android.view.inputmethod.InputMethodManager.DEBUG_SIMPLE_LOG;
            if (r7 == 0) goto L_0x0108;
        L_0x0101:
            r7 = "InputMethodManager";
            r8 = "MSG_UNBIND startInputInner is called with null IBinder ";
            android.util.Log.w(r7, r8);
        L_0x0108:
            r7 = android.view.inputmethod.InputMethodManager.this;
            r7.startInputInner(r10, r6, r6, r6);
            goto L_0x0008;
        L_0x010f:
            r6 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x010f }
            throw r6;
        L_0x0112:
            r7 = r13.arg1;
            if (r7 == 0) goto L_0x0193;
        L_0x0116:
            r6 = android.view.inputmethod.InputMethodManager.DEBUG_SIMPLE_LOG;
            if (r6 == 0) goto L_0x0140;
        L_0x011a:
            r6 = "InputMethodManager";
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r8 = "handleMessage: MSG_SET_ACTIVE ";
            r7 = r7.append(r8);
            r7 = r7.append(r0);
            r8 = ", was ";
            r7 = r7.append(r8);
            r8 = android.view.inputmethod.InputMethodManager.this;
            r8 = r8.mActive;
            r7 = r7.append(r8);
            r7 = r7.toString();
            android.util.Log.i(r6, r7);
        L_0x0140:
            r6 = android.view.inputmethod.InputMethodManager.this;
            r7 = r6.mH;
            monitor-enter(r7);
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r6.mActive = r0;	 Catch:{ all -> 0x0190 }
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r8 = 0;
            r6.mFullscreenMode = r8;	 Catch:{ all -> 0x0190 }
            if (r0 != 0) goto L_0x015c;
        L_0x0150:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r8 = 1;
            r6.mHasBeenInactive = r8;	 Catch:{ all -> 0x0190 }
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x01e8 }
            r6 = r6.mIInputContext;	 Catch:{ RemoteException -> 0x01e8 }
            r6.finishComposingText();	 Catch:{ RemoteException -> 0x01e8 }
        L_0x015c:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r6 = r6.mServedView;	 Catch:{ all -> 0x0190 }
            if (r6 == 0) goto L_0x018d;
        L_0x0162:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r6 = r6.mServedView;	 Catch:{ all -> 0x0190 }
            r6 = r6.hasWindowFocus();	 Catch:{ all -> 0x0190 }
            if (r6 == 0) goto L_0x018d;
        L_0x016c:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r8 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r8 = r8.mHasBeenInactive;	 Catch:{ all -> 0x0190 }
            r9 = 0;
            r6 = r6.checkFocusNoStartInput(r8, r9);	 Catch:{ all -> 0x0190 }
            if (r6 == 0) goto L_0x018d;
        L_0x0179:
            r6 = android.view.inputmethod.InputMethodManager.DEBUG_SIMPLE_LOG;	 Catch:{ all -> 0x0190 }
            if (r6 == 0) goto L_0x0184;
        L_0x017d:
            r6 = "InputMethodManager";
            r8 = "MSG_SET_ACTIVE startInputInner is called with null IBinder ";
            android.util.Log.w(r6, r8);	 Catch:{ all -> 0x0190 }
        L_0x0184:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x0190 }
            r8 = 0;
            r9 = 0;
            r10 = 0;
            r11 = 0;
            r6.startInputInner(r8, r9, r10, r11);	 Catch:{ all -> 0x0190 }
        L_0x018d:
            monitor-exit(r7);	 Catch:{ all -> 0x0190 }
            goto L_0x0008;
        L_0x0190:
            r6 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0190 }
            throw r6;
        L_0x0193:
            r0 = r6;
            goto L_0x0116;
        L_0x0195:
            r7 = android.view.inputmethod.InputMethodManager.this;
            r6 = r13.obj;
            r6 = (android.view.inputmethod.InputMethodManager.PendingEvent) r6;
            r7.sendInputEventAndReportResultOnMainLooper(r6);
            goto L_0x0008;
        L_0x01a0:
            r7 = android.view.inputmethod.InputMethodManager.this;
            r8 = r13.arg1;
            r7.finishedInputEvent(r8, r6, r0);
            goto L_0x0008;
        L_0x01a9:
            r7 = android.view.inputmethod.InputMethodManager.this;
            r8 = r13.arg1;
            r7.finishedInputEvent(r8, r6, r6);
            goto L_0x0008;
        L_0x01b2:
            r6 = android.view.inputmethod.InputMethodManager.this;
            r7 = r6.mH;
            monitor-enter(r7);
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ all -> 0x01c1 }
            r8 = r13.arg1;	 Catch:{ all -> 0x01c1 }
            r6.mNextUserActionNotificationSequenceNumber = r8;	 Catch:{ all -> 0x01c1 }
            monitor-exit(r7);	 Catch:{ all -> 0x01c1 }
            goto L_0x0008;
        L_0x01c1:
            r6 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x01c1 }
            throw r6;
        L_0x01c4:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x01d3 }
            r6 = r6.mService;	 Catch:{ RemoteException -> 0x01d3 }
            r7 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x01d3 }
            r7 = r7.mClient;	 Catch:{ RemoteException -> 0x01d3 }
            r8 = 0;
            r9 = 0;
            r6.showSoftInput(r7, r8, r9);	 Catch:{ RemoteException -> 0x01d3 }
            goto L_0x0008;
        L_0x01d3:
            r6 = move-exception;
            goto L_0x0008;
        L_0x01d6:
            r6 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x01e5 }
            r6 = r6.mService;	 Catch:{ RemoteException -> 0x01e5 }
            r7 = android.view.inputmethod.InputMethodManager.this;	 Catch:{ RemoteException -> 0x01e5 }
            r7 = r7.mClient;	 Catch:{ RemoteException -> 0x01e5 }
            r8 = 0;
            r9 = 0;
            r6.hideSoftInput(r7, r8, r9);	 Catch:{ RemoteException -> 0x01e5 }
            goto L_0x0008;
        L_0x01e5:
            r6 = move-exception;
            goto L_0x0008;
        L_0x01e8:
            r6 = move-exception;
            goto L_0x015c;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.H.handleMessage(android.os.Message):void");
        }
    }

    private final class ImeInputEventSender extends InputEventSender {
        public ImeInputEventSender(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        public void onInputEventFinished(int seq, boolean handled) {
            InputMethodManager.this.finishedInputEvent(seq, handled, false);
        }
    }

    private final class PendingEvent implements Runnable {
        public FinishedInputEventCallback mCallback;
        public InputEvent mEvent;
        public boolean mHandled;
        public Handler mHandler;
        public String mInputMethodId;
        public Object mToken;

        private PendingEvent() {
        }

        public void recycle() {
            this.mEvent = null;
            this.mToken = null;
            this.mInputMethodId = null;
            this.mCallback = null;
            this.mHandler = null;
            this.mHandled = false;
        }

        public void run() {
            this.mCallback.onFinishedInputEvent(this.mToken, this.mHandled);
            synchronized (InputMethodManager.this.mH) {
                InputMethodManager.this.recyclePendingEventLocked(this);
            }
        }
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DEBUG_SIMPLE_LOG = z;
    }

    InputMethodManager(IInputMethodManager service, Looper looper) {
        this.mService = service;
        this.mMainLooper = looper;
        this.mH = new H(looper);
        this.mIInputContext = new ControlledInputConnectionWrapper(looper, this.mDummyInputConnection, this);
    }

    InputConnection getServedInputConnection() {
        return this.mServedInputConnection;
    }

    View getServedView() {
        return this.mServedView;
    }

    public void setWACOMPen(boolean bPen) {
        try {
            if (this.mService != null) {
                this.mService.setWACOMPen(bPen);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeFullInputMethod(boolean fullmode) {
        try {
            if (this.mService != null) {
                this.mService.changeFullInputMethod(fullmode);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getWACOMPen() {
        try {
            if (this.mService != null) {
                return this.mService.getWACOMPen();
            }
            return false;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputMethodManager getInstance() {
        InputMethodManager inputMethodManager;
        synchronized (InputMethodManager.class) {
            if (sInstance == null) {
                sInstance = new InputMethodManager(IInputMethodManager.Stub.asInterface(ServiceManager.getService("input_method")), Looper.getMainLooper());
            }
            inputMethodManager = sInstance;
        }
        return inputMethodManager;
    }

    public static InputMethodManager peekInstance() {
        return sInstance;
    }

    public IInputMethodClient getClient() {
        return this.mClient;
    }

    public IInputContext getInputContext() {
        return this.mIInputContext;
    }

    public List<InputMethodInfo> getInputMethodList() {
        try {
            return this.mService.getInputMethodList();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InputMethodInfo> getEnabledInputMethodList() {
        try {
            return this.mService.getEnabledInputMethodList();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(InputMethodInfo imi, boolean allowsImplicitlySelectedSubtypes) {
        try {
            return this.mService.getEnabledInputMethodSubtypeList(imi == null ? null : imi.getId(), allowsImplicitlySelectedSubtypes);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void showStatusIcon(IBinder imeToken, String packageName, int iconId) {
        try {
            this.mService.updateStatusIcon(imeToken, packageName, iconId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void hideStatusIcon(IBinder imeToken) {
        try {
            this.mService.updateStatusIcon(imeToken, null, 0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setImeWindowStatus(IBinder imeToken, int vis, int backDisposition) {
        try {
            this.mService.setImeWindowStatus(imeToken, vis, backDisposition);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFullscreenMode(boolean fullScreen) {
        this.mFullscreenMode = fullScreen;
    }

    public void registerSuggestionSpansForNotification(SuggestionSpan[] spans) {
        try {
            this.mService.registerSuggestionSpansForNotification(spans);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifySuggestionPicked(SuggestionSpan span, String originalString, int index) {
        try {
            this.mService.notifySuggestionPicked(span, originalString, index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFullscreenMode() {
        return this.mFullscreenMode;
    }

    public boolean isActive(View view) {
        boolean z;
        checkFocus();
        synchronized (this.mH) {
            z = (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) && this.mCurrentTextBoxAttribute != null;
        }
        return z;
    }

    public boolean isActive() {
        boolean z;
        checkFocus();
        synchronized (this.mH) {
            z = (this.mServedView == null || this.mCurrentTextBoxAttribute == null) ? false : true;
        }
        return z;
    }

    public boolean isAcceptingText() {
        checkFocus();
        return this.mServedInputConnection != null;
    }

    void clearBindingLocked() {
        clearConnectionLocked();
        setInputChannelLocked(null);
        this.mBindSequence = -1;
        this.mCurId = null;
        this.mCurMethod = null;
    }

    void setInputChannelLocked(InputChannel channel) {
        if (this.mCurChannel != channel) {
            if (this.mCurSender != null) {
                flushPendingEventsLocked();
                this.mCurSender.dispose();
                this.mCurSender = null;
            }
            if (this.mCurChannel != null) {
                this.mCurChannel.dispose();
            }
            this.mCurChannel = channel;
        }
    }

    void clearConnectionLocked() {
        this.mCurrentTextBoxAttribute = null;
        this.mServedInputConnection = null;
        if (this.mServedInputConnectionWrapper != null) {
            this.mServedInputConnectionWrapper.deactivate();
            this.mServedInputConnectionWrapper = null;
        }
    }

    void finishInputLocked() {
        this.mNextServedView = null;
        if (this.mServedView != null) {
            if (this.mCurrentTextBoxAttribute != null) {
                try {
                    this.mService.finishInput(this.mClient);
                } catch (RemoteException e) {
                }
            }
            notifyInputConnectionFinished();
            this.mServedView = null;
            this.mCompletions = null;
            this.mServedConnecting = false;
            clearConnectionLocked();
        }
    }

    private void notifyInputConnectionFinished() {
        if (this.mServedView != null && this.mServedInputConnection != null) {
            ViewRootImpl viewRootImpl = this.mServedView.getViewRootImpl();
            if (viewRootImpl != null) {
                viewRootImpl.dispatchFinishInputConnection(this.mServedInputConnection);
            }
        }
    }

    public void reportFinishInputConnection(InputConnection ic) {
        if (this.mServedInputConnection != ic) {
            ic.finishComposingText();
            if (ic instanceof BaseInputConnection) {
                ((BaseInputConnection) ic).reportFinish();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void displayCompletions(android.view.View r4, android.view.inputmethod.CompletionInfo[] r5) {
        /*
        r3 = this;
        r3.checkFocus();
        r1 = r3.mH;
        monitor-enter(r1);
        r0 = r3.mServedView;	 Catch:{ all -> 0x0027 }
        if (r0 == r4) goto L_0x0018;
    L_0x000a:
        r0 = r3.mServedView;	 Catch:{ all -> 0x0027 }
        if (r0 == 0) goto L_0x0016;
    L_0x000e:
        r0 = r3.mServedView;	 Catch:{ all -> 0x0027 }
        r0 = r0.checkInputConnectionProxy(r4);	 Catch:{ all -> 0x0027 }
        if (r0 != 0) goto L_0x0018;
    L_0x0016:
        monitor-exit(r1);	 Catch:{ all -> 0x0027 }
    L_0x0017:
        return;
    L_0x0018:
        r3.mCompletions = r5;	 Catch:{ all -> 0x0027 }
        r0 = r3.mCurMethod;	 Catch:{ all -> 0x0027 }
        if (r0 == 0) goto L_0x0025;
    L_0x001e:
        r0 = r3.mCurMethod;	 Catch:{ RemoteException -> 0x002a }
        r2 = r3.mCompletions;	 Catch:{ RemoteException -> 0x002a }
        r0.displayCompletions(r2);	 Catch:{ RemoteException -> 0x002a }
    L_0x0025:
        monitor-exit(r1);	 Catch:{ all -> 0x0027 }
        goto L_0x0017;
    L_0x0027:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0027 }
        throw r0;
    L_0x002a:
        r0 = move-exception;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.displayCompletions(android.view.View, android.view.inputmethod.CompletionInfo[]):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateExtractedText(android.view.View r3, int r4, android.view.inputmethod.ExtractedText r5) {
        /*
        r2 = this;
        r2.checkFocus();
        r1 = r2.mH;
        monitor-enter(r1);
        r0 = r2.mServedView;	 Catch:{ all -> 0x0023 }
        if (r0 == r3) goto L_0x0018;
    L_0x000a:
        r0 = r2.mServedView;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x0016;
    L_0x000e:
        r0 = r2.mServedView;	 Catch:{ all -> 0x0023 }
        r0 = r0.checkInputConnectionProxy(r3);	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x0018;
    L_0x0016:
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
    L_0x0017:
        return;
    L_0x0018:
        r0 = r2.mCurMethod;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x0021;
    L_0x001c:
        r0 = r2.mCurMethod;	 Catch:{ RemoteException -> 0x0026 }
        r0.updateExtractedText(r4, r5);	 Catch:{ RemoteException -> 0x0026 }
    L_0x0021:
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        goto L_0x0017;
    L_0x0023:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        throw r0;
    L_0x0026:
        r0 = move-exception;
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateExtractedText(android.view.View, int, android.view.inputmethod.ExtractedText):void");
    }

    public boolean showSoftInput(View view, int flags) {
        if (DEBUG_SIMPLE_LOG) {
            Log.i(TAG, "showSoftInput(View,I)");
        }
        return showSoftInput(view, flags, null);
    }

    public boolean showSoftInput(View view, int flags, ResultReceiver resultReceiver) {
        boolean z = false;
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) {
                try {
                    if (DEBUG_SIMPLE_LOG) {
                        Log.w(TAG, "showSoftInput(V,I,ResultReceiver)");
                    }
                    if (view != null) {
                        if (view.isScaleWindow()) {
                            flags |= 4;
                        }
                        z = this.mService.showSoftInput(this.mClient, flags, resultReceiver);
                    }
                } catch (RemoteException e) {
                }
            }
        }
        return z;
    }

    public void showSoftInputUnchecked(int flags, ResultReceiver resultReceiver) {
        checkFocus();
        try {
            if (DEBUG_SIMPLE_LOG) {
                Log.w(TAG, "showSoftInputUnchecked(I,ResultReceiver)");
            }
            this.mService.showSoftInput(this.mClient, flags, resultReceiver);
        } catch (RemoteException e) {
        }
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags) {
        return hideSoftInputFromWindow(windowToken, flags, null);
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags, ResultReceiver resultReceiver) {
        boolean z = false;
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == null || this.mServedView.getWindowToken() != windowToken) {
            } else {
                if (this.mMobileKeyboardState != 0) {
                    if (this.mServedView.getContext() == null || this.mServedView.getContext().getResources() == null || this.mServedView.getContext().getResources().getConfiguration().mobileKeyboardCovered != 1 || !this.mServedView.isEnabled()) {
                        this.mMobileKeyboardState = 0;
                    } else {
                        this.mMobileKeyboardState = 2;
                        if (this.mServedView.hasWindowFocus()) {
                            dismissClipboard();
                        }
                    }
                }
                try {
                    if (DEBUG_SIMPLE_LOG) {
                        Log.i(TAG, "hideSoftInputFromWindow(IBinder,I,ResultReceiver");
                    }
                    z = this.mService.hideSoftInput(this.mClient, flags, resultReceiver);
                } catch (RemoteException e) {
                }
            }
        }
        return z;
    }

    public boolean minimizeSoftInput(IBinder windowToken, int height) {
        boolean z = false;
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == null || this.mServedView.getWindowToken() != windowToken) {
            } else if (this.mServedView.getContext().getResources().getConfiguration().mobileKeyboardCovered == 1) {
            } else {
                try {
                    z = this.mService.minimizeSoftInput(this.mClient, height);
                } catch (RemoteException e) {
                }
            }
        }
        return z;
    }

    public void undoMinimizeSoftInput() {
        synchronized (this.mH) {
            try {
                this.mService.undoMinimizeSoftInput();
            } catch (RemoteException e) {
            }
        }
    }

    public boolean forceHideSoftInput() {
        if (DEBUG_SIMPLE_LOG) {
            Log.i(TAG, "forceHideSoftInput");
        }
        return forceHideSoftInput(null);
    }

    public boolean forceHideSoftInput(ResultReceiver resultReceiver) {
        boolean hideSoftInput;
        synchronized (this.mH) {
            try {
                if (DEBUG_SIMPLE_LOG) {
                    Log.i(TAG, "forceHideSoftInput(ResultReceiver)");
                }
                hideSoftInput = this.mService.hideSoftInput(this.mClient, 0, resultReceiver);
            } catch (RemoteException e) {
                hideSoftInput = false;
            }
        }
        return hideSoftInput;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void toggleSoftInputFromWindow(android.os.IBinder r4, int r5, int r6) {
        /*
        r3 = this;
        r1 = r3.mH;
        monitor-enter(r1);
        r0 = r3.mServedView;	 Catch:{ all -> 0x0028 }
        if (r0 == 0) goto L_0x000f;
    L_0x0007:
        r0 = r3.mServedView;	 Catch:{ all -> 0x0028 }
        r0 = r0.getWindowToken();	 Catch:{ all -> 0x0028 }
        if (r0 == r4) goto L_0x0011;
    L_0x000f:
        monitor-exit(r1);	 Catch:{ all -> 0x0028 }
    L_0x0010:
        return;
    L_0x0011:
        r0 = r3.mCurMethod;	 Catch:{ all -> 0x0028 }
        if (r0 == 0) goto L_0x0026;
    L_0x0015:
        r0 = DEBUG_SIMPLE_LOG;	 Catch:{ RemoteException -> 0x002b }
        if (r0 == 0) goto L_0x0021;
    L_0x0019:
        r0 = "InputMethodManager";
        r2 = "toggleSoftInputFromWindow(IBinder, I,I)";
        android.util.Log.i(r0, r2);	 Catch:{ RemoteException -> 0x002b }
    L_0x0021:
        r0 = r3.mCurMethod;	 Catch:{ RemoteException -> 0x002b }
        r0.toggleSoftInput(r5, r6);	 Catch:{ RemoteException -> 0x002b }
    L_0x0026:
        monitor-exit(r1);	 Catch:{ all -> 0x0028 }
        goto L_0x0010;
    L_0x0028:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0028 }
        throw r0;
    L_0x002b:
        r0 = move-exception;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.toggleSoftInputFromWindow(android.os.IBinder, int, int):void");
    }

    public void toggleSoftInput(int showFlags, int hideFlags) {
        if (this.mCurMethod != null) {
            try {
                if (DEBUG_SIMPLE_LOG) {
                    Log.i(TAG, "toggleSoftInput(I,I)");
                }
                this.mCurMethod.toggleSoftInput(showFlags, hideFlags);
            } catch (RemoteException e) {
            }
        }
    }

    public void showSideSyncSoftInput(int showFlags) {
        if (this.mCurMethod != null) {
            try {
                if (DEBUG_SIMPLE_LOG) {
                    Log.i(TAG, "showSideSyncSoftInput(I)");
                }
                this.mCurMethod.showSideSyncSoftInput(showFlags);
            } catch (RemoteException e) {
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void restartInput(android.view.View r4) {
        /*
        r3 = this;
        r2 = 0;
        r3.checkFocus();
        r1 = r3.mH;
        monitor-enter(r1);
        r0 = r3.mServedView;	 Catch:{ all -> 0x002e }
        if (r0 == r4) goto L_0x0019;
    L_0x000b:
        r0 = r3.mServedView;	 Catch:{ all -> 0x002e }
        if (r0 == 0) goto L_0x0017;
    L_0x000f:
        r0 = r3.mServedView;	 Catch:{ all -> 0x002e }
        r0 = r0.checkInputConnectionProxy(r4);	 Catch:{ all -> 0x002e }
        if (r0 != 0) goto L_0x0019;
    L_0x0017:
        monitor-exit(r1);	 Catch:{ all -> 0x002e }
    L_0x0018:
        return;
    L_0x0019:
        r0 = 1;
        r3.mServedConnecting = r0;	 Catch:{ all -> 0x002e }
        monitor-exit(r1);	 Catch:{ all -> 0x002e }
        r0 = DEBUG_SIMPLE_LOG;
        if (r0 == 0) goto L_0x0029;
    L_0x0021:
        r0 = "InputMethodManager";
        r1 = "restartInput(View)";
        android.util.Log.i(r0, r1);
    L_0x0029:
        r0 = 0;
        r3.startInputInner(r0, r2, r2, r2);
        goto L_0x0018;
    L_0x002e:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002e }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.restartInput(android.view.View):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean startInputInner(android.os.IBinder r16, int r17, int r18, int r19) {
        /*
        r15 = this;
        r2 = r15.mH;
        monitor-enter(r2);
        r13 = r15.mServedView;	 Catch:{ all -> 0x0048 }
        r1 = DEBUG_SIMPLE_LOG;	 Catch:{ all -> 0x0048 }
        if (r1 == 0) goto L_0x0021;
    L_0x0009:
        r1 = "InputMethodManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0048 }
        r3.<init>();	 Catch:{ all -> 0x0048 }
        r4 = "Starting input: view=";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0048 }
        r3 = r3.append(r13);	 Catch:{ all -> 0x0048 }
        r3 = r3.toString();	 Catch:{ all -> 0x0048 }
        android.util.Log.v(r1, r3);	 Catch:{ all -> 0x0048 }
    L_0x0021:
        if (r13 != 0) goto L_0x0031;
    L_0x0023:
        r1 = DEBUG_SIMPLE_LOG;	 Catch:{ all -> 0x0048 }
        if (r1 == 0) goto L_0x002e;
    L_0x0027:
        r1 = "InputMethodManager";
        r3 = "ABORT input: no served view!";
        android.util.Log.v(r1, r3);	 Catch:{ all -> 0x0048 }
    L_0x002e:
        r1 = 0;
        monitor-exit(r2);	 Catch:{ all -> 0x0048 }
    L_0x0030:
        return r1;
    L_0x0031:
        monitor-exit(r2);	 Catch:{ all -> 0x0048 }
        r12 = r13.getHandler();
        if (r12 != 0) goto L_0x004b;
    L_0x0038:
        r1 = DEBUG_SIMPLE_LOG;
        if (r1 == 0) goto L_0x0043;
    L_0x003c:
        r1 = "InputMethodManager";
        r2 = "ABORT input: no handler for view! Close current input.";
        android.util.Log.v(r1, r2);
    L_0x0043:
        r15.closeCurrentInput();
        r1 = 0;
        goto L_0x0030;
    L_0x0048:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0048 }
        throw r1;
    L_0x004b:
        r1 = r12.getLooper();
        r2 = android.os.Looper.myLooper();
        if (r1 == r2) goto L_0x006a;
    L_0x0055:
        r1 = DEBUG_SIMPLE_LOG;
        if (r1 == 0) goto L_0x0060;
    L_0x0059:
        r1 = "InputMethodManager";
        r2 = "Starting input: reschedule to view thread";
        android.util.Log.v(r1, r2);
    L_0x0060:
        r1 = new android.view.inputmethod.InputMethodManager$2;
        r1.<init>();
        r12.post(r1);
        r1 = 0;
        goto L_0x0030;
    L_0x006a:
        r7 = new android.view.inputmethod.EditorInfo;
        r7.<init>();
        r1 = r13.getContext();
        r1 = r1.getOpPackageName();
        r7.packageName = r1;
        r1 = r13.getId();
        r7.fieldId = r1;
        r10 = r13.onCreateInputConnection(r7);
        r1 = DEBUG_SIMPLE_LOG;
        if (r1 == 0) goto L_0x009f;
    L_0x0087:
        r1 = "InputMethodManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Starting input: tba=";
        r2 = r2.append(r3);
        r2 = r2.append(r7);
        r2 = r2.toString();
        android.util.Log.v(r1, r2);
    L_0x009f:
        r14 = r15.mH;
        monitor-enter(r14);
        r1 = r15.mServedView;	 Catch:{ all -> 0x00ad }
        if (r1 != r13) goto L_0x00aa;
    L_0x00a6:
        r1 = r15.mServedConnecting;	 Catch:{ all -> 0x00ad }
        if (r1 != 0) goto L_0x00b0;
    L_0x00aa:
        r1 = 0;
        monitor-exit(r14);	 Catch:{ all -> 0x00ad }
        goto L_0x0030;
    L_0x00ad:
        r1 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x00ad }
        throw r1;
    L_0x00b0:
        r1 = r15.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x00ad }
        if (r1 != 0) goto L_0x00ba;
    L_0x00b4:
        r0 = r17;
        r0 = r0 | 256;
        r17 = r0;
    L_0x00ba:
        r15.mCurrentTextBoxAttribute = r7;	 Catch:{ all -> 0x00ad }
        r1 = 0;
        r15.mServedConnecting = r1;	 Catch:{ all -> 0x00ad }
        r15.notifyInputConnectionFinished();	 Catch:{ all -> 0x00ad }
        r15.mServedInputConnection = r10;	 Catch:{ all -> 0x00ad }
        if (r10 == 0) goto L_0x013f;
    L_0x00c6:
        r1 = r7.initialSelStart;	 Catch:{ all -> 0x00ad }
        r15.mCursorSelStart = r1;	 Catch:{ all -> 0x00ad }
        r1 = r7.initialSelEnd;	 Catch:{ all -> 0x00ad }
        r15.mCursorSelEnd = r1;	 Catch:{ all -> 0x00ad }
        r1 = -1;
        r15.mCursorCandStart = r1;	 Catch:{ all -> 0x00ad }
        r1 = -1;
        r15.mCursorCandEnd = r1;	 Catch:{ all -> 0x00ad }
        r1 = r15.mCursorRect;	 Catch:{ all -> 0x00ad }
        r1.setEmpty();	 Catch:{ all -> 0x00ad }
        r1 = 0;
        r15.mCursorAnchorInfo = r1;	 Catch:{ all -> 0x00ad }
        r8 = new android.view.inputmethod.InputMethodManager$ControlledInputConnectionWrapper;	 Catch:{ all -> 0x00ad }
        r1 = r12.getLooper();	 Catch:{ all -> 0x00ad }
        r8.<init>(r1, r10, r15);	 Catch:{ all -> 0x00ad }
    L_0x00e5:
        r1 = r15.mServedInputConnectionWrapper;	 Catch:{ all -> 0x00ad }
        if (r1 == 0) goto L_0x00ee;
    L_0x00e9:
        r1 = r15.mServedInputConnectionWrapper;	 Catch:{ all -> 0x00ad }
        r1.deactivate();	 Catch:{ all -> 0x00ad }
    L_0x00ee:
        r15.mServedInputConnectionWrapper = r8;	 Catch:{ all -> 0x00ad }
        if (r16 == 0) goto L_0x0141;
    L_0x00f2:
        r1 = DEBUG_SIMPLE_LOG;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 == 0) goto L_0x00fd;
    L_0x00f6:
        r1 = "InputMethodManager";
        r2 = "[IMM] startInputInner - mService.windowGainedFocus";
        android.util.Log.i(r1, r2);	 Catch:{ RemoteException -> 0x0171 }
    L_0x00fd:
        r1 = r15.mService;	 Catch:{ RemoteException -> 0x0171 }
        r2 = r15.mClient;	 Catch:{ RemoteException -> 0x0171 }
        r3 = r16;
        r4 = r17;
        r5 = r18;
        r6 = r19;
        r11 = r1.windowGainedFocus(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x0171 }
        r15.mTba = r7;	 Catch:{ RemoteException -> 0x0171 }
        r15.mServedContext = r8;	 Catch:{ RemoteException -> 0x0171 }
    L_0x0111:
        if (r11 == 0) goto L_0x012c;
    L_0x0113:
        r1 = r11.id;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 == 0) goto L_0x015a;
    L_0x0117:
        r1 = r11.channel;	 Catch:{ RemoteException -> 0x0171 }
        r15.setInputChannelLocked(r1);	 Catch:{ RemoteException -> 0x0171 }
        r1 = r11.sequence;	 Catch:{ RemoteException -> 0x0171 }
        r15.mBindSequence = r1;	 Catch:{ RemoteException -> 0x0171 }
        r1 = r11.method;	 Catch:{ RemoteException -> 0x0171 }
        r15.mCurMethod = r1;	 Catch:{ RemoteException -> 0x0171 }
        r1 = r11.id;	 Catch:{ RemoteException -> 0x0171 }
        r15.mCurId = r1;	 Catch:{ RemoteException -> 0x0171 }
        r1 = r11.userActionNotificationSequenceNumber;	 Catch:{ RemoteException -> 0x0171 }
        r15.mNextUserActionNotificationSequenceNumber = r1;	 Catch:{ RemoteException -> 0x0171 }
    L_0x012c:
        r1 = r15.mCurMethod;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 == 0) goto L_0x013b;
    L_0x0130:
        r1 = r15.mCompletions;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 == 0) goto L_0x013b;
    L_0x0134:
        r1 = r15.mCurMethod;	 Catch:{ RemoteException -> 0x018d }
        r2 = r15.mCompletions;	 Catch:{ RemoteException -> 0x018d }
        r1.displayCompletions(r2);	 Catch:{ RemoteException -> 0x018d }
    L_0x013b:
        monitor-exit(r14);	 Catch:{ all -> 0x00ad }
        r1 = 1;
        goto L_0x0030;
    L_0x013f:
        r8 = 0;
        goto L_0x00e5;
    L_0x0141:
        r1 = DEBUG_SIMPLE_LOG;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 == 0) goto L_0x014c;
    L_0x0145:
        r1 = "InputMethodManager";
        r2 = "[IMM] startInputInner - mService.startInput. windowGainingFocus is null case";
        android.util.Log.i(r1, r2);	 Catch:{ RemoteException -> 0x0171 }
    L_0x014c:
        r15.undoMinimizeSoftInput();	 Catch:{ RemoteException -> 0x0171 }
        r1 = r15.mService;	 Catch:{ RemoteException -> 0x0171 }
        r2 = r15.mClient;	 Catch:{ RemoteException -> 0x0171 }
        r0 = r17;
        r11 = r1.startInput(r2, r8, r7, r0);	 Catch:{ RemoteException -> 0x0171 }
        goto L_0x0111;
    L_0x015a:
        r1 = r11.channel;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 == 0) goto L_0x0169;
    L_0x015e:
        r1 = r11.channel;	 Catch:{ RemoteException -> 0x0171 }
        r2 = r15.mCurChannel;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 == r2) goto L_0x0169;
    L_0x0164:
        r1 = r11.channel;	 Catch:{ RemoteException -> 0x0171 }
        r1.dispose();	 Catch:{ RemoteException -> 0x0171 }
    L_0x0169:
        r1 = r15.mCurMethod;	 Catch:{ RemoteException -> 0x0171 }
        if (r1 != 0) goto L_0x012c;
    L_0x016d:
        r1 = 1;
        monitor-exit(r14);	 Catch:{ all -> 0x00ad }
        goto L_0x0030;
    L_0x0171:
        r9 = move-exception;
        r1 = "InputMethodManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ad }
        r2.<init>();	 Catch:{ all -> 0x00ad }
        r3 = "IME died: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x00ad }
        r3 = r15.mCurId;	 Catch:{ all -> 0x00ad }
        r2 = r2.append(r3);	 Catch:{ all -> 0x00ad }
        r2 = r2.toString();	 Catch:{ all -> 0x00ad }
        android.util.Log.w(r1, r2, r9);	 Catch:{ all -> 0x00ad }
        goto L_0x013b;
    L_0x018d:
        r1 = move-exception;
        goto L_0x013b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.startInputInner(android.os.IBinder, int, int, int):boolean");
    }

    public void windowDismissed(IBinder appWindowToken) {
        checkFocus();
        synchronized (this.mH) {
            if (!(this.mServedView == null || appWindowToken == null || this.mServedView.getWindowToken() != appWindowToken)) {
                if (DEBUG_SIMPLE_LOG) {
                    Log.i(TAG, "windowDismissed");
                }
                finishInputLocked();
            }
        }
    }

    public void focusIn(View view) {
        synchronized (this.mH) {
            focusInLocked(view);
            if (this.mIsCheckedFocusInView && this.mNextServedView != null && (this.mNextServedView instanceof EditText)) {
                EditText editText = this.mNextServedView;
                if (editText.getInputType() != 0 && editText.isFocused() && editText.isCursorVisible() && editText.getContext() != null && editText.getContext().getResources() != null && editText.getContext().getResources().getConfiguration().keyboard == 3 && "KOREA".equals(SystemProperties.get("ro.csc.country_code")) && editText.getContext().getResources().getConfiguration().orientation == 1) {
                    this.mH.sendEmptyMessage(100);
                }
            }
            this.mMobileKeyboardState = 0;
            if (this.mNextServedView != null && (this.mNextServedView instanceof TextView)) {
                TextView textView = this.mNextServedView;
                if (textView.getInputType() != 0 && textView.isFocused() && textView.isCursorVisible() && textView.getContext() != null && textView.getContext().getResources() != null && textView.getContext().getResources().getConfiguration().mobileKeyboardCovered == 1) {
                    this.mMobileKeyboardState = 1;
                    this.mH.sendEmptyMessage(100);
                }
            }
        }
    }

    void focusInLocked(View view) {
        if (DEBUG_SIMPLE_LOG) {
            Log.v(TAG, "focusIn: " + view);
        }
        if (this.mCurRootView != view.getRootView()) {
            this.mIsCheckedFocusInView = false;
            return;
        }
        this.mIsCheckedFocusInView = true;
        this.mNextServedView = view;
        scheduleCheckFocusLocked(view);
    }

    public void focusOut(View view) {
        synchronized (this.mH) {
            if (DEBUG_SIMPLE_LOG) {
                Log.v(TAG, "focusOut: " + view + " mServedView=" + this.mServedView + " winFocus=" + view.hasWindowFocus());
            }
            if (this.mServedView != view) {
            }
            if (this.mMobileKeyboardState == 2) {
                this.mH.removeMessages(100);
                this.mH.sendEmptyMessage(101);
            }
            this.mMobileKeyboardState = 0;
        }
    }

    public void onViewDetachedFromWindow(View view) {
        synchronized (this.mH) {
            if (this.mServedView == view && view.hasWindowFocus()) {
                scheduleCheckFocusLocked(view);
            }
        }
    }

    static void scheduleCheckFocusLocked(View view) {
        ViewRootImpl viewRootImpl = view.getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.dispatchCheckFocus();
        }
    }

    public void checkFocus() {
        if (checkFocusNoStartInput(false, true)) {
            startInputInner(null, 0, 0, 0);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkFocusNoStartInput(boolean r9, boolean r10) {
        /*
        r8 = this;
        r3 = 1;
        r2 = 0;
        r4 = r8.mServedView;
        r5 = r8.mNextServedView;
        if (r4 != r5) goto L_0x000b;
    L_0x0008:
        if (r9 != 0) goto L_0x000b;
    L_0x000a:
        return r2;
    L_0x000b:
        r0 = 0;
        r4 = r8.mH;
        monitor-enter(r4);
        r5 = r8.mServedView;	 Catch:{ all -> 0x0019 }
        r6 = r8.mNextServedView;	 Catch:{ all -> 0x0019 }
        if (r5 != r6) goto L_0x001c;
    L_0x0015:
        if (r9 != 0) goto L_0x001c;
    L_0x0017:
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        goto L_0x000a;
    L_0x0019:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        throw r2;
    L_0x001c:
        r5 = DEBUG_SIMPLE_LOG;	 Catch:{ all -> 0x0019 }
        if (r5 == 0) goto L_0x0072;
    L_0x0020:
        r1 = "<none>";
        r5 = r8.mServedView;	 Catch:{ all -> 0x0019 }
        if (r5 == 0) goto L_0x0038;
    L_0x0026:
        r5 = r8.mServedView;	 Catch:{ all -> 0x0019 }
        r5 = r5.getContext();	 Catch:{ all -> 0x0019 }
        if (r5 == 0) goto L_0x0038;
    L_0x002e:
        r5 = r8.mServedView;	 Catch:{ all -> 0x0019 }
        r5 = r5.getContext();	 Catch:{ all -> 0x0019 }
        r1 = r5.getPackageName();	 Catch:{ all -> 0x0019 }
    L_0x0038:
        r5 = "InputMethodManager";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0019 }
        r6.<init>();	 Catch:{ all -> 0x0019 }
        r7 = "checkFocus: view=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x0019 }
        r7 = r8.mServedView;	 Catch:{ all -> 0x0019 }
        r6 = r6.append(r7);	 Catch:{ all -> 0x0019 }
        r7 = " next=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x0019 }
        r7 = r8.mNextServedView;	 Catch:{ all -> 0x0019 }
        r6 = r6.append(r7);	 Catch:{ all -> 0x0019 }
        r7 = " forceNewFocus=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x0019 }
        r6 = r6.append(r9);	 Catch:{ all -> 0x0019 }
        r7 = " package=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x0019 }
        r6 = r6.append(r1);	 Catch:{ all -> 0x0019 }
        r6 = r6.toString();	 Catch:{ all -> 0x0019 }
        android.util.Log.v(r5, r6);	 Catch:{ all -> 0x0019 }
    L_0x0072:
        r5 = r8.mNextServedView;	 Catch:{ all -> 0x0019 }
        if (r5 != 0) goto L_0x007e;
    L_0x0076:
        r8.finishInputLocked();	 Catch:{ all -> 0x0019 }
        r8.closeCurrentInput();	 Catch:{ all -> 0x0019 }
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        goto L_0x000a;
    L_0x007e:
        r0 = r8.mServedInputConnection;	 Catch:{ all -> 0x0019 }
        r2 = r8.mNextServedView;	 Catch:{ all -> 0x0019 }
        r8.mServedView = r2;	 Catch:{ all -> 0x0019 }
        r2 = 0;
        r8.mCurrentTextBoxAttribute = r2;	 Catch:{ all -> 0x0019 }
        r2 = 0;
        r8.mCompletions = r2;	 Catch:{ all -> 0x0019 }
        r2 = 1;
        r8.mServedConnecting = r2;	 Catch:{ all -> 0x0019 }
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        if (r10 == 0) goto L_0x009d;
    L_0x0090:
        if (r0 == 0) goto L_0x009d;
    L_0x0092:
        r2 = r8.mMainLooper;
        r2 = r2.isCurrentThread();
        if (r2 == 0) goto L_0x00a0;
    L_0x009a:
        r0.finishComposingText();
    L_0x009d:
        r2 = r3;
        goto L_0x000a;
    L_0x00a0:
        r2 = "InputMethodManager";
        r4 = "[IMM] checkFocusNoStartInput - ic.finishComposingText() must be called on the main looper";
        android.util.Log.w(r2, r4);
        goto L_0x009d;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.checkFocusNoStartInput(boolean, boolean):boolean");
    }

    void closeCurrentInput() {
        try {
            this.mService.hideSoftInput(this.mClient, 2, null);
        } catch (RemoteException e) {
        }
    }

    public void onPostWindowFocus(View rootView, View focusedView, int softInputMode, boolean first, int windowFlags) {
        boolean forceNewFocus = false;
        synchronized (this.mH) {
            View view;
            if (DEBUG_SIMPLE_LOG) {
                Log.v(TAG, "onWindowFocus: " + focusedView + " softInputMode=" + softInputMode + " first=" + first + " flags=#" + Integer.toHexString(windowFlags));
            }
            if (this.mHasBeenInactive) {
                if (DEBUG_SIMPLE_LOG) {
                    Log.v(TAG, "Has been inactive!  Starting fresh");
                }
                this.mHasBeenInactive = false;
                forceNewFocus = true;
            }
            if (focusedView != null) {
                view = focusedView;
            } else {
                view = rootView;
            }
            focusInLocked(view);
        }
        int controlFlags = 0;
        if (focusedView != null) {
            controlFlags = 0 | 1;
            if (focusedView.onCheckIsTextEditor()) {
                controlFlags |= 2;
            }
        }
        if (first) {
            controlFlags |= 4;
        }
        if (checkFocusNoStartInput(forceNewFocus, true)) {
            if (DEBUG_SIMPLE_LOG) {
                Log.i(TAG, "onWindowFocus startInputInner will call soon");
            }
            if (startInputInner(rootView.getWindowToken(), controlFlags, softInputMode, windowFlags)) {
                return;
            }
        } else if (this.mServedView == this.mNextServedView && !forceNewFocus && this.mServedView != null && "com.kakao.talk".equals(this.mServedView.getContext().getPackageName())) {
            Log.v(TAG, "Kakao Window Focus and Lagging check ");
            this.mServedConnecting = true;
            if (DEBUG_SIMPLE_LOG) {
                Log.i(TAG, "startInputInner(null, 0, 0, 0) for KAKAO");
            }
            startInputInner(null, 0, 0, 0);
        }
        synchronized (this.mH) {
            try {
                if (DEBUG_SIMPLE_LOG) {
                    Log.v(TAG, "Reporting focus gain, without startInput");
                }
                this.mService.windowGainedFocus(this.mClient, rootView.getWindowToken(), controlFlags, softInputMode, windowFlags, null, null);
            } catch (RemoteException e) {
            }
        }
    }

    public void onPreWindowFocus(View rootView, boolean hasWindowFocus) {
        synchronized (this.mH) {
            if (rootView == null) {
                this.mCurRootView = null;
            }
            if (hasWindowFocus) {
                this.mCurRootView = rootView;
            } else if (rootView == this.mCurRootView) {
                this.mCurRootView = null;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateSelection(android.view.View r10, int r11, int r12, int r13, int r14) {
        /*
        r9 = this;
        r9.checkFocus();
        r8 = r9.mH;
        monitor-enter(r8);
        r0 = r9.mServedView;	 Catch:{ all -> 0x0047 }
        if (r0 == r10) goto L_0x0016;
    L_0x000a:
        r0 = r9.mServedView;	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x001e;
    L_0x000e:
        r0 = r9.mServedView;	 Catch:{ all -> 0x0047 }
        r0 = r0.checkInputConnectionProxy(r10);	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x001e;
    L_0x0016:
        r0 = r9.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x001e;
    L_0x001a:
        r0 = r9.mCurMethod;	 Catch:{ all -> 0x0047 }
        if (r0 != 0) goto L_0x0020;
    L_0x001e:
        monitor-exit(r8);	 Catch:{ all -> 0x0047 }
    L_0x001f:
        return;
    L_0x0020:
        r0 = r9.mCursorSelStart;	 Catch:{ all -> 0x0047 }
        if (r0 != r11) goto L_0x0030;
    L_0x0024:
        r0 = r9.mCursorSelEnd;	 Catch:{ all -> 0x0047 }
        if (r0 != r12) goto L_0x0030;
    L_0x0028:
        r0 = r9.mCursorCandStart;	 Catch:{ all -> 0x0047 }
        if (r0 != r13) goto L_0x0030;
    L_0x002c:
        r0 = r9.mCursorCandEnd;	 Catch:{ all -> 0x0047 }
        if (r0 == r14) goto L_0x0045;
    L_0x0030:
        r1 = r9.mCursorSelStart;	 Catch:{ RemoteException -> 0x004a }
        r2 = r9.mCursorSelEnd;	 Catch:{ RemoteException -> 0x004a }
        r9.mCursorSelStart = r11;	 Catch:{ RemoteException -> 0x004a }
        r9.mCursorSelEnd = r12;	 Catch:{ RemoteException -> 0x004a }
        r9.mCursorCandStart = r13;	 Catch:{ RemoteException -> 0x004a }
        r9.mCursorCandEnd = r14;	 Catch:{ RemoteException -> 0x004a }
        r0 = r9.mCurMethod;	 Catch:{ RemoteException -> 0x004a }
        r3 = r11;
        r4 = r12;
        r5 = r13;
        r6 = r14;
        r0.updateSelection(r1, r2, r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x004a }
    L_0x0045:
        monitor-exit(r8);	 Catch:{ all -> 0x0047 }
        goto L_0x001f;
    L_0x0047:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0047 }
        throw r0;
    L_0x004a:
        r7 = move-exception;
        r0 = "InputMethodManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0047 }
        r3.<init>();	 Catch:{ all -> 0x0047 }
        r4 = "IME died: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0047 }
        r4 = r9.mCurId;	 Catch:{ all -> 0x0047 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0047 }
        r3 = r3.toString();	 Catch:{ all -> 0x0047 }
        android.util.Log.w(r0, r3, r7);	 Catch:{ all -> 0x0047 }
        goto L_0x0045;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateSelection(android.view.View, int, int, int, int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void viewClicked(android.view.View r7) {
        /*
        r6 = this;
        r2 = r6.mServedView;
        r3 = r6.mNextServedView;
        if (r2 == r3) goto L_0x0027;
    L_0x0006:
        r1 = 1;
    L_0x0007:
        r6.checkFocus();
        r3 = r6.mH;
        monitor-enter(r3);
        r2 = r6.mServedView;	 Catch:{ all -> 0x0030 }
        if (r2 == r7) goto L_0x001d;
    L_0x0011:
        r2 = r6.mServedView;	 Catch:{ all -> 0x0030 }
        if (r2 == 0) goto L_0x0025;
    L_0x0015:
        r2 = r6.mServedView;	 Catch:{ all -> 0x0030 }
        r2 = r2.checkInputConnectionProxy(r7);	 Catch:{ all -> 0x0030 }
        if (r2 == 0) goto L_0x0025;
    L_0x001d:
        r2 = r6.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x0030 }
        if (r2 == 0) goto L_0x0025;
    L_0x0021:
        r2 = r6.mCurMethod;	 Catch:{ all -> 0x0030 }
        if (r2 != 0) goto L_0x0029;
    L_0x0025:
        monitor-exit(r3);	 Catch:{ all -> 0x0030 }
    L_0x0026:
        return;
    L_0x0027:
        r1 = 0;
        goto L_0x0007;
    L_0x0029:
        r2 = r6.mCurMethod;	 Catch:{ RemoteException -> 0x0033 }
        r2.viewClicked(r1);	 Catch:{ RemoteException -> 0x0033 }
    L_0x002e:
        monitor-exit(r3);	 Catch:{ all -> 0x0030 }
        goto L_0x0026;
    L_0x0030:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0030 }
        throw r2;
    L_0x0033:
        r0 = move-exception;
        r2 = "InputMethodManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0030 }
        r4.<init>();	 Catch:{ all -> 0x0030 }
        r5 = "IME died: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0030 }
        r5 = r6.mCurId;	 Catch:{ all -> 0x0030 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0030 }
        r4 = r4.toString();	 Catch:{ all -> 0x0030 }
        android.util.Log.w(r2, r4, r0);	 Catch:{ all -> 0x0030 }
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.viewClicked(android.view.View):void");
    }

    @Deprecated
    public boolean isWatchingCursor(View view) {
        return false;
    }

    public boolean isCursorAnchorInfoEnabled() {
        boolean z = false;
        synchronized (this.mH) {
            boolean isImmediate;
            if ((this.mRequestUpdateCursorAnchorInfoMonitorMode & 1) != 0) {
                isImmediate = true;
            } else {
                isImmediate = false;
            }
            boolean isMonitoring;
            if ((this.mRequestUpdateCursorAnchorInfoMonitorMode & 2) != 0) {
                isMonitoring = true;
            } else {
                isMonitoring = false;
            }
            if (isImmediate || isMonitoring) {
                z = true;
            }
        }
        return z;
    }

    public void setUpdateCursorAnchorInfoMode(int flags) {
        synchronized (this.mH) {
            this.mRequestUpdateCursorAnchorInfoMonitorMode = flags;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @java.lang.Deprecated
    public void updateCursor(android.view.View r6, int r7, int r8, int r9, int r10) {
        /*
        r5 = this;
        r5.checkFocus();
        r2 = r5.mH;
        monitor-enter(r2);
        r1 = r5.mServedView;	 Catch:{ all -> 0x003f }
        if (r1 == r6) goto L_0x0016;
    L_0x000a:
        r1 = r5.mServedView;	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x001e;
    L_0x000e:
        r1 = r5.mServedView;	 Catch:{ all -> 0x003f }
        r1 = r1.checkInputConnectionProxy(r6);	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x001e;
    L_0x0016:
        r1 = r5.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x001e;
    L_0x001a:
        r1 = r5.mCurMethod;	 Catch:{ all -> 0x003f }
        if (r1 != 0) goto L_0x0020;
    L_0x001e:
        monitor-exit(r2);	 Catch:{ all -> 0x003f }
    L_0x001f:
        return;
    L_0x0020:
        r1 = r5.mTmpCursorRect;	 Catch:{ all -> 0x003f }
        r1.set(r7, r8, r9, r10);	 Catch:{ all -> 0x003f }
        r1 = r5.mCursorRect;	 Catch:{ all -> 0x003f }
        r3 = r5.mTmpCursorRect;	 Catch:{ all -> 0x003f }
        r1 = r1.equals(r3);	 Catch:{ all -> 0x003f }
        if (r1 != 0) goto L_0x003d;
    L_0x002f:
        r1 = r5.mCurMethod;	 Catch:{ RemoteException -> 0x0042 }
        r3 = r5.mTmpCursorRect;	 Catch:{ RemoteException -> 0x0042 }
        r1.updateCursor(r3);	 Catch:{ RemoteException -> 0x0042 }
        r1 = r5.mCursorRect;	 Catch:{ RemoteException -> 0x0042 }
        r3 = r5.mTmpCursorRect;	 Catch:{ RemoteException -> 0x0042 }
        r1.set(r3);	 Catch:{ RemoteException -> 0x0042 }
    L_0x003d:
        monitor-exit(r2);	 Catch:{ all -> 0x003f }
        goto L_0x001f;
    L_0x003f:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x003f }
        throw r1;
    L_0x0042:
        r0 = move-exception;
        r1 = "InputMethodManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x003f }
        r3.<init>();	 Catch:{ all -> 0x003f }
        r4 = "IME died: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x003f }
        r4 = r5.mCurId;	 Catch:{ all -> 0x003f }
        r3 = r3.append(r4);	 Catch:{ all -> 0x003f }
        r3 = r3.toString();	 Catch:{ all -> 0x003f }
        android.util.Log.w(r1, r3, r0);	 Catch:{ all -> 0x003f }
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateCursor(android.view.View, int, int, int, int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCursorAnchorInfo(android.view.View r7, android.view.inputmethod.CursorAnchorInfo r8) {
        /*
        r6 = this;
        if (r7 == 0) goto L_0x0004;
    L_0x0002:
        if (r8 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r6.checkFocus();
        r3 = r6.mH;
        monitor-enter(r3);
        r2 = r6.mServedView;	 Catch:{ all -> 0x0025 }
        if (r2 == r7) goto L_0x001b;
    L_0x000f:
        r2 = r6.mServedView;	 Catch:{ all -> 0x0025 }
        if (r2 == 0) goto L_0x0023;
    L_0x0013:
        r2 = r6.mServedView;	 Catch:{ all -> 0x0025 }
        r2 = r2.checkInputConnectionProxy(r7);	 Catch:{ all -> 0x0025 }
        if (r2 == 0) goto L_0x0023;
    L_0x001b:
        r2 = r6.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x0025 }
        if (r2 == 0) goto L_0x0023;
    L_0x001f:
        r2 = r6.mCurMethod;	 Catch:{ all -> 0x0025 }
        if (r2 != 0) goto L_0x0028;
    L_0x0023:
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        goto L_0x0004;
    L_0x0025:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        throw r2;
    L_0x0028:
        r2 = r6.mRequestUpdateCursorAnchorInfoMonitorMode;	 Catch:{ all -> 0x0025 }
        r2 = r2 & 1;
        if (r2 == 0) goto L_0x003b;
    L_0x002e:
        r1 = 1;
    L_0x002f:
        if (r1 != 0) goto L_0x003d;
    L_0x0031:
        r2 = r6.mCursorAnchorInfo;	 Catch:{ all -> 0x0025 }
        r2 = java.util.Objects.equals(r2, r8);	 Catch:{ all -> 0x0025 }
        if (r2 == 0) goto L_0x003d;
    L_0x0039:
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        goto L_0x0004;
    L_0x003b:
        r1 = 0;
        goto L_0x002f;
    L_0x003d:
        r2 = r6.mCurMethod;	 Catch:{ RemoteException -> 0x004c }
        r2.updateCursorAnchorInfo(r8);	 Catch:{ RemoteException -> 0x004c }
        r6.mCursorAnchorInfo = r8;	 Catch:{ RemoteException -> 0x004c }
        r2 = r6.mRequestUpdateCursorAnchorInfoMonitorMode;	 Catch:{ RemoteException -> 0x004c }
        r2 = r2 & -2;
        r6.mRequestUpdateCursorAnchorInfoMonitorMode = r2;	 Catch:{ RemoteException -> 0x004c }
    L_0x004a:
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        goto L_0x0004;
    L_0x004c:
        r0 = move-exception;
        r2 = "InputMethodManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0025 }
        r4.<init>();	 Catch:{ all -> 0x0025 }
        r5 = "IME died: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0025 }
        r5 = r6.mCurId;	 Catch:{ all -> 0x0025 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0025 }
        r4 = r4.toString();	 Catch:{ all -> 0x0025 }
        android.util.Log.w(r2, r4, r0);	 Catch:{ all -> 0x0025 }
        goto L_0x004a;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.updateCursorAnchorInfo(android.view.View, android.view.inputmethod.CursorAnchorInfo):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendAppPrivateCommand(android.view.View r6, java.lang.String r7, android.os.Bundle r8) {
        /*
        r5 = this;
        r5.checkFocus();
        r2 = r5.mH;
        monitor-enter(r2);
        r1 = r5.mServedView;	 Catch:{ all -> 0x0027 }
        if (r1 == r6) goto L_0x0016;
    L_0x000a:
        r1 = r5.mServedView;	 Catch:{ all -> 0x0027 }
        if (r1 == 0) goto L_0x001e;
    L_0x000e:
        r1 = r5.mServedView;	 Catch:{ all -> 0x0027 }
        r1 = r1.checkInputConnectionProxy(r6);	 Catch:{ all -> 0x0027 }
        if (r1 == 0) goto L_0x001e;
    L_0x0016:
        r1 = r5.mCurrentTextBoxAttribute;	 Catch:{ all -> 0x0027 }
        if (r1 == 0) goto L_0x001e;
    L_0x001a:
        r1 = r5.mCurMethod;	 Catch:{ all -> 0x0027 }
        if (r1 != 0) goto L_0x0020;
    L_0x001e:
        monitor-exit(r2);	 Catch:{ all -> 0x0027 }
    L_0x001f:
        return;
    L_0x0020:
        r1 = r5.mCurMethod;	 Catch:{ RemoteException -> 0x002a }
        r1.appPrivateCommand(r7, r8);	 Catch:{ RemoteException -> 0x002a }
    L_0x0025:
        monitor-exit(r2);	 Catch:{ all -> 0x0027 }
        goto L_0x001f;
    L_0x0027:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0027 }
        throw r1;
    L_0x002a:
        r0 = move-exception;
        r1 = "InputMethodManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0027 }
        r3.<init>();	 Catch:{ all -> 0x0027 }
        r4 = "IME died: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0027 }
        r4 = r5.mCurId;	 Catch:{ all -> 0x0027 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0027 }
        r3 = r3.toString();	 Catch:{ all -> 0x0027 }
        android.util.Log.w(r1, r3, r0);	 Catch:{ all -> 0x0027 }
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.sendAppPrivateCommand(android.view.View, java.lang.String, android.os.Bundle):void");
    }

    public void sendAppPrivateCommand(String action, Bundle data) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mCurMethod == null) {
                return;
            }
            try {
                this.mCurMethod.appPrivateCommand(action, data);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
    }

    public void setInputMethod(IBinder token, String id) {
        try {
            this.mService.setInputMethod(token, id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInputMethodAndSubtype(IBinder token, String id, InputMethodSubtype subtype) {
        try {
            this.mService.setInputMethodAndSubtype(token, id, subtype);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void hideSoftInputFromInputMethod(IBinder token, int flags) {
        try {
            if (DEBUG_SIMPLE_LOG) {
                Log.i(TAG, "hideSoftInputFromInputMethod(IBinder,I)");
            }
            this.mService.hideMySoftInput(token, flags);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void showSoftInputFromInputMethod(IBinder token, int flags) {
        try {
            if (DEBUG_SIMPLE_LOG) {
                Log.i(TAG, "showSoftInputFromInputMethod(IBinder,I)");
            }
            this.mService.showMySoftInput(token, flags);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int dispatchInputEvent(InputEvent event, Object token, FinishedInputEventCallback callback, Handler handler) {
        synchronized (this.mH) {
            if (this.mCurMethod != null) {
                PendingEvent p;
                int sendInputEventOnMainLooperLocked;
                Message msg;
                if (event instanceof KeyEvent) {
                    KeyEvent keyEvent = (KeyEvent) event;
                    if (keyEvent.getSource() == 1073741824) {
                        int actionFromEvent = keyEvent.getAction();
                        int keycodeFromEvent = keyEvent.getKeyCode();
                        if ((actionFromEvent == 0 || actionFromEvent == 1) && ((keycodeFromEvent == 19 || keycodeFromEvent == 20 || keycodeFromEvent == 21 || keycodeFromEvent == 22 || keycodeFromEvent == 23 || keycodeFromEvent == 61) && keyEvent.getRepeatCount() == 0)) {
                            p = obtainPendingEventLocked(event, token, this.mCurId, callback, handler);
                            if (this.mMainLooper.isCurrentThread()) {
                                sendInputEventOnMainLooperLocked = sendInputEventOnMainLooperLocked(p);
                                return sendInputEventOnMainLooperLocked;
                            }
                            msg = this.mH.obtainMessage(5, p);
                            msg.setAsynchronous(true);
                            this.mH.sendMessage(msg);
                            return 1;
                        }
                    }
                    if (keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 63 && keyEvent.getRepeatCount() == 0) {
                        showInputMethodPickerLocked();
                        return 1;
                    }
                }
                p = obtainPendingEventLocked(event, token, this.mCurId, callback, handler);
                if (this.mMainLooper.isCurrentThread()) {
                    sendInputEventOnMainLooperLocked = sendInputEventOnMainLooperLocked(p);
                    return sendInputEventOnMainLooperLocked;
                }
                msg = this.mH.obtainMessage(5, p);
                msg.setAsynchronous(true);
                this.mH.sendMessage(msg);
                return -1;
            }
            return 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void sendInputEventAndReportResultOnMainLooper(android.view.inputmethod.InputMethodManager.PendingEvent r5) {
        /*
        r4 = this;
        r0 = 1;
        r3 = r4.mH;
        monitor-enter(r3);
        r1 = r4.sendInputEventOnMainLooperLocked(r5);	 Catch:{ all -> 0x0016 }
        r2 = -1;
        if (r1 != r2) goto L_0x000d;
    L_0x000b:
        monitor-exit(r3);	 Catch:{ all -> 0x0016 }
    L_0x000c:
        return;
    L_0x000d:
        if (r1 != r0) goto L_0x0014;
    L_0x000f:
        monitor-exit(r3);	 Catch:{ all -> 0x0016 }
        r4.invokeFinishedInputEventCallback(r5, r0);
        goto L_0x000c;
    L_0x0014:
        r0 = 0;
        goto L_0x000f;
    L_0x0016:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0016 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.sendInputEventAndReportResultOnMainLooper(android.view.inputmethod.InputMethodManager$PendingEvent):void");
    }

    int sendInputEventOnMainLooperLocked(PendingEvent p) {
        if (this.mCurChannel != null) {
            if (this.mCurSender == null) {
                this.mCurSender = new ImeInputEventSender(this.mCurChannel, this.mH.getLooper());
            }
            InputEvent event = p.mEvent;
            int seq = event.getSequenceNumber();
            if (this.mCurSender.sendInputEvent(seq, event)) {
                this.mPendingEvents.put(seq, p);
                Trace.traceCounter(4, PENDING_EVENT_COUNTER, this.mPendingEvents.size());
                Message msg = this.mH.obtainMessage(6, p);
                msg.setAsynchronous(true);
                this.mH.sendMessageDelayed(msg, INPUT_METHOD_NOT_RESPONDING_TIMEOUT);
                return -1;
            }
            Log.w(TAG, "Unable to send input event to IME: " + this.mCurId + " dropping: " + event);
        }
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void finishedInputEvent(int r8, boolean r9, boolean r10) {
        /*
        r7 = this;
        r3 = r7.mH;
        monitor-enter(r3);
        r2 = r7.mPendingEvents;	 Catch:{ all -> 0x004f }
        r0 = r2.indexOfKey(r8);	 Catch:{ all -> 0x004f }
        if (r0 >= 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r3);	 Catch:{ all -> 0x004f }
    L_0x000c:
        return;
    L_0x000d:
        r2 = r7.mPendingEvents;	 Catch:{ all -> 0x004f }
        r1 = r2.valueAt(r0);	 Catch:{ all -> 0x004f }
        r1 = (android.view.inputmethod.InputMethodManager.PendingEvent) r1;	 Catch:{ all -> 0x004f }
        r2 = r7.mPendingEvents;	 Catch:{ all -> 0x004f }
        r2.removeAt(r0);	 Catch:{ all -> 0x004f }
        r4 = 4;
        r2 = "aq:imm";
        r6 = r7.mPendingEvents;	 Catch:{ all -> 0x004f }
        r6 = r6.size();	 Catch:{ all -> 0x004f }
        android.os.Trace.traceCounter(r4, r2, r6);	 Catch:{ all -> 0x004f }
        if (r10 == 0) goto L_0x0048;
    L_0x0029:
        r2 = "InputMethodManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004f }
        r4.<init>();	 Catch:{ all -> 0x004f }
        r5 = "Timeout waiting for IME to handle input event after 2500 ms: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x004f }
        r5 = r1.mInputMethodId;	 Catch:{ all -> 0x004f }
        r4 = r4.append(r5);	 Catch:{ all -> 0x004f }
        r4 = r4.toString();	 Catch:{ all -> 0x004f }
        android.util.Log.w(r2, r4);	 Catch:{ all -> 0x004f }
    L_0x0043:
        monitor-exit(r3);	 Catch:{ all -> 0x004f }
        r7.invokeFinishedInputEventCallback(r1, r9);
        goto L_0x000c;
    L_0x0048:
        r2 = r7.mH;	 Catch:{ all -> 0x004f }
        r4 = 6;
        r2.removeMessages(r4, r1);	 Catch:{ all -> 0x004f }
        goto L_0x0043;
    L_0x004f:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x004f }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.finishedInputEvent(int, boolean, boolean):void");
    }

    void invokeFinishedInputEventCallback(PendingEvent p, boolean handled) {
        p.mHandled = handled;
        if (p.mHandler.getLooper().isCurrentThread()) {
            p.run();
            return;
        }
        Message msg = Message.obtain(p.mHandler, p);
        msg.setAsynchronous(true);
        msg.sendToTarget();
    }

    private void flushPendingEventsLocked() {
        this.mH.removeMessages(7);
        int count = this.mPendingEvents.size();
        for (int i = 0; i < count; i++) {
            Message msg = this.mH.obtainMessage(7, this.mPendingEvents.keyAt(i), 0);
            msg.setAsynchronous(true);
            msg.sendToTarget();
        }
    }

    private PendingEvent obtainPendingEventLocked(InputEvent event, Object token, String inputMethodId, FinishedInputEventCallback callback, Handler handler) {
        PendingEvent p = (PendingEvent) this.mPendingEventPool.acquire();
        if (p == null) {
            p = new PendingEvent();
        }
        p.mEvent = event;
        p.mToken = token;
        p.mInputMethodId = inputMethodId;
        p.mCallback = callback;
        p.mHandler = handler;
        return p;
    }

    private void recyclePendingEventLocked(PendingEvent p) {
        p.recycle();
        this.mPendingEventPool.release(p);
    }

    public void showInputMethodPicker() {
        synchronized (this.mH) {
            showInputMethodPickerLocked();
        }
    }

    public void showInputMethodPicker(boolean showAuxiliarySubtypes) {
        synchronized (this.mH) {
            try {
                this.mService.showInputMethodPickerFromClient(this.mClient, showAuxiliarySubtypes ? 1 : 2);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
    }

    public boolean isInputMethodShown() {
        boolean shown = false;
        try {
            shown = this.mService.isInputMethodShown();
        } catch (RemoteException e) {
        }
        return shown;
    }

    public void dismissAndShowAgainInputMethodPicker() {
        synchronized (this.mH) {
            dismissAndShowAgainInputMethodPickerLocked();
        }
    }

    private void dismissAndShowAgainInputMethodPickerLocked() {
        try {
            this.mService.dismissAndShowAgainInputMethodPicker();
        } catch (RemoteException e) {
            Log.w(TAG, "Unable dismiss And Show Again InputMethodPicker");
        }
    }

    private void showInputMethodPickerLocked() {
        try {
            this.mService.showInputMethodPickerFromClient(this.mClient, 0);
        } catch (RemoteException e) {
            Log.w(TAG, "IME died: " + this.mCurId, e);
        }
    }

    public void showInputMethodAndSubtypeEnabler(String imiId) {
        synchronized (this.mH) {
            try {
                this.mService.showInputMethodAndSubtypeEnablerFromClient(this.mClient, imiId);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
    }

    public InputMethodSubtype getCurrentInputMethodSubtype() {
        InputMethodSubtype currentInputMethodSubtype;
        synchronized (this.mH) {
            try {
                currentInputMethodSubtype = this.mService.getCurrentInputMethodSubtype();
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
                currentInputMethodSubtype = null;
            }
        }
        return currentInputMethodSubtype;
    }

    public boolean setCurrentInputMethodSubtype(InputMethodSubtype subtype) {
        boolean currentInputMethodSubtype;
        synchronized (this.mH) {
            try {
                currentInputMethodSubtype = this.mService.setCurrentInputMethodSubtype(subtype);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
                currentInputMethodSubtype = false;
            }
        }
        return currentInputMethodSubtype;
    }

    public void notifyUserAction() {
        synchronized (this.mH) {
            if (this.mLastSentUserActionNotificationSequenceNumber == this.mNextUserActionNotificationSequenceNumber) {
                return;
            }
            try {
                this.mService.notifyUserAction(this.mNextUserActionNotificationSequenceNumber);
                this.mLastSentUserActionNotificationSequenceNumber = this.mNextUserActionNotificationSequenceNumber;
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
    }

    public Map<InputMethodInfo, List<InputMethodSubtype>> getShortcutInputMethodsAndSubtypes() {
        HashMap<InputMethodInfo, List<InputMethodSubtype>> ret;
        synchronized (this.mH) {
            ret = new HashMap();
            try {
                List<Object> info = this.mService.getShortcutInputMethodsAndSubtypes();
                ArrayList<InputMethodSubtype> subtypes = null;
                if (info != null && !info.isEmpty()) {
                    int N = info.size();
                    for (int i = 0; i < N; i++) {
                        Object o = info.get(i);
                        if (o instanceof InputMethodInfo) {
                            if (ret.containsKey(o)) {
                                Log.e(TAG, "IMI list already contains the same InputMethod.");
                                break;
                            }
                            subtypes = new ArrayList();
                            ret.put((InputMethodInfo) o, subtypes);
                        } else if (subtypes != null && (o instanceof InputMethodSubtype)) {
                            subtypes.add((InputMethodSubtype) o);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
        return ret;
    }

    public int getInputMethodWindowVisibleHeight() {
        int inputMethodWindowVisibleHeight;
        synchronized (this.mH) {
            try {
                inputMethodWindowVisibleHeight = this.mService.getInputMethodWindowVisibleHeight();
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
                inputMethodWindowVisibleHeight = 0;
            }
        }
        return inputMethodWindowVisibleHeight;
    }

    public boolean switchToLastInputMethod(IBinder imeToken) {
        boolean switchToLastInputMethod;
        synchronized (this.mH) {
            try {
                switchToLastInputMethod = this.mService.switchToLastInputMethod(imeToken);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
                switchToLastInputMethod = false;
            }
        }
        return switchToLastInputMethod;
    }

    public boolean switchToNextInputMethod(IBinder imeToken, boolean onlyCurrentIme) {
        boolean switchToNextInputMethod;
        synchronized (this.mH) {
            try {
                switchToNextInputMethod = this.mService.switchToNextInputMethod(imeToken, onlyCurrentIme);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
                switchToNextInputMethod = false;
            }
        }
        return switchToNextInputMethod;
    }

    public boolean shouldOfferSwitchingToNextInputMethod(IBinder imeToken) {
        boolean shouldOfferSwitchingToNextInputMethod;
        synchronized (this.mH) {
            try {
                shouldOfferSwitchingToNextInputMethod = this.mService.shouldOfferSwitchingToNextInputMethod(imeToken);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
                shouldOfferSwitchingToNextInputMethod = false;
            }
        }
        return shouldOfferSwitchingToNextInputMethod;
    }

    public void setAdditionalInputMethodSubtypes(String imiId, InputMethodSubtype[] subtypes) {
        synchronized (this.mH) {
            try {
                this.mService.setAdditionalInputMethodSubtypes(imiId, subtypes);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
    }

    public InputMethodSubtype getLastInputMethodSubtype() {
        InputMethodSubtype lastInputMethodSubtype;
        synchronized (this.mH) {
            try {
                lastInputMethodSubtype = this.mService.getLastInputMethodSubtype();
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
                lastInputMethodSubtype = null;
            }
        }
        return lastInputMethodSubtype;
    }

    public boolean isCurrentInputMethodAsSamsungKeyboard() {
        boolean z = false;
        synchronized (this.mH) {
            try {
                z = this.mService.isCurrentInputMethodAsSamsungKeyboard();
            } catch (RemoteException e) {
            } catch (SecurityException e2) {
            }
        }
        return z;
    }

    void doDump(FileDescriptor fd, PrintWriter fout, String[] args) {
        Printer p = new PrintWriterPrinter(fout);
        p.println("Input method client state for " + this + ":");
        p.println("  mService=" + this.mService);
        p.println("  mMainLooper=" + this.mMainLooper);
        p.println("  mIInputContext=" + this.mIInputContext);
        p.println("  mActive=" + this.mActive + " mHasBeenInactive=" + this.mHasBeenInactive + " mBindSequence=" + this.mBindSequence + " mCurId=" + this.mCurId);
        p.println("  mCurMethod=" + this.mCurMethod);
        p.println("  mCurRootView=" + this.mCurRootView);
        p.println("  mServedView=" + this.mServedView);
        p.println("  mNextServedView=" + this.mNextServedView);
        p.println("  mServedConnecting=" + this.mServedConnecting);
        if (this.mCurrentTextBoxAttribute != null) {
            p.println("  mCurrentTextBoxAttribute:");
            this.mCurrentTextBoxAttribute.dump(p, "    ");
        } else {
            p.println("  mCurrentTextBoxAttribute: null");
        }
        p.println("  mServedInputConnection=" + this.mServedInputConnection);
        p.println("  mCompletions=" + this.mCompletions);
        p.println("  mCursorRect=" + this.mCursorRect);
        p.println("  mCursorSelStart=" + this.mCursorSelStart + " mCursorSelEnd=" + this.mCursorSelEnd + " mCursorCandStart=" + this.mCursorCandStart + " mCursorCandEnd=" + this.mCursorCandEnd);
        p.println("  mNextUserActionNotificationSequenceNumber=" + this.mNextUserActionNotificationSequenceNumber + " mLastSentUserActionNotificationSequenceNumber=" + this.mLastSentUserActionNotificationSequenceNumber);
    }

    public void setBrightnessWithKeyboard(int direction) {
        try {
            this.mService.setScreenBrightness(direction);
        } catch (RemoteException e) {
        }
    }

    public int isAccessoryKeyboardState() {
        try {
            return this.mService.isAccessoryKeyboard();
        } catch (RemoteException e) {
            return 0;
        }
    }

    private String getStackTrace() {
        return getStackTrace(SubscriptionManager.MAX_SUBSCRIPTION_ID_VALUE);
    }

    private String getStackTrace(int limit) {
        StringBuilder sb = new StringBuilder();
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            StackTraceElement[] frames = e.getStackTrace();
            int j = 1;
            while (j < frames.length && j < limit + 1) {
                sb.append(frames[j].toString() + "\n");
                j++;
            }
            return sb.toString();
        }
    }

    public int getCurrentFocusDisplayID() {
        try {
            return this.mService.getCurrentFocusDisplayID();
        } catch (RemoteException e) {
            return 0;
        }
    }

    private void dismissClipboard() {
        if (this.sService == null) {
            this.sService = IClipboardService.Stub.asInterface(ServiceManager.getService("clipboardEx"));
        }
        try {
            if (this.sService != null && this.sService.IsShowUIClipboardData()) {
                if (DEBUG_SIMPLE_LOG) {
                    Log.d(TAG, "dismissClipboard");
                }
                this.sService.dismissUIDataDialog();
            }
        } catch (RemoteException e) {
        }
    }
}
