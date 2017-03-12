package com.samsung.android.writingbuddy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings.System;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.samsung.android.writingbuddy.IWritingBuddyClient.Stub;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

public final class WritingBuddyImpl {
    public static final int BIND_MSG_REQUEST_IME_RECT = 2;
    public static final int BIND_MSG_REQUEST_TARGET_RECT = 1;
    public static final int BIND_MSG_RESULT_IME_CLOSED = 2;
    public static final int BIND_MSG_RESULT_IME_OPENED = 1;
    public static final int BIND_MSG_RESULT_WRITING_TYPE_IMAGE = 2;
    public static final int BIND_MSG_RESULT_WRITING_TYPE_TEXT = 1;
    private static final int CLIENT_SEQUENCE_MASK = 255;
    private static final int CLIENT_UNIQUE_ID_MASK = -256;
    public static final int CMD_ID_RECEIVE_ACTION_BUTTON = 2;
    public static final int CMD_ID_WATCH_ACTION = 1;
    private static final boolean DEBUG = "eng".equals(Build.TYPE);
    public static final int FLAG_HELP_MODE = 4;
    public static final int FLAG_IMAGE_WRITING = 1;
    public static final int FLAG_MATH_WRITING = 2;
    public static final int FLAG_MMS_MODE = 8;
    public static final int FLAG_START_DRAWING_MODE = 16;
    private static final String HELP_MODE_RESULT_CLOSED = "CLOSED";
    private static final String HELP_MODE_RESULT_HOVERED = "HOVERED";
    private static final String HELP_MODE_RESULT_HOVER_CANCELED = "HOVER_CANCELED";
    private static final String HELP_MODE_RESULT_OPENED = "OPENED";
    private static final String HELP_MODE_RESULT_TEXT_INSERTED = "TEXT_INSERTED";
    public static final String IME_CMD_CANCEL_CLOSE = "com.samsung.android.writingbuddy/CANCEL_CLOSE";
    public static final String IME_CMD_SEND_BINDER = "com.samsung.android.writingbuddy/SEND_BINDER";
    private static final String MMS_DATA_DELETE = "MMS_DATA_DELETE";
    private static final int MSG_CANCEL_WRITINGBUDDY_CUE = 7;
    private static final int MSG_EDITOR_ACTION_DOWN = 8;
    private static final int MSG_SERVICE_IMAGE_ADDED = 1;
    private static final int MSG_SERVICE_RESULT_RECEIVED = 4;
    private static final int MSG_SERVICE_TEXT_DELETED = 3;
    private static final int MSG_SERVICE_TEXT_INSERTED = 2;
    private static final int MSG_SERVICE_UPDATE_DIALOG = 9;
    private static final int MSG_SERVICE_UPDATE_POSITION = 5;
    private static final int MSG_SERVICE_UPDATE_POSITION_CHECK = 10;
    private static final int MSG_SHOW_WRITINGBUDDY_CUE = 6;
    public static final String PRIVATE_CMD_HELP_MODE = "HELP_MODE";
    public static final String PRIVATE_CMD_RECEIVE_ACTION_BUTTON = "RECEIVE_ACTION_BUTTON";
    public static final String PRIVATE_CMD_WACTH_ACTION = "WATCH_ACTION";
    public static final String RESULT_FIELD_DELIMITER = "//";
    public static final String RESULT_STRING_DELIMITER = "//";
    public static final String SERVICE_CB_CLIENT_CHANGED = "service_cb_client_changed";
    public static final String SERVICE_CB_CLOSED = "service_cb_closed";
    public static final String SERVICE_CB_DRAWING_MODE_SET = "service_cb_drawing_mode_set";
    public static final String SERVICE_CB_HELP_MODE_RESULT = "service_cb_help_mode_result";
    public static final String SERVICE_CB_INFLATE_DONE = "service_cb_inflate_done";
    public static final String SERVICE_CB_INIT_TEXT = "service_cb_init_text";
    public static final String SERVICE_CB_MATH_WRITING_RESULT = "service_cb_math_writing_result";
    public static final String SERVICE_CB_MMS_DATA_DELETE = "service_cb_mms_data_delete";
    public static final String SERVICE_CB_PERFORM_EDITOR_ACTION = "service_cb_perform_editor_action";
    public static final String SERVICE_CB_PRIVATE = "service_cb_private";
    public static final String SERVICE_CB_WRITING_DONE = "service_cb_writing_done";
    private static final int START_DELAY_TIME_MS = 150;
    private static final int STATE_EVENT_SERVICE_CALLBACK_CLOSED = 2;
    private static final int STATE_EVENT_SERVICE_CALLBACK_INFLATE_DONE = 1;
    private static final int STATE_EVENT_TYPE_MOTION = 1;
    private static final int STATE_EVENT_TYPE_SERVICE_CALLBACK = 2;
    private static final int STATE_RESET_COUNT = 3;
    private static final int STATE_STEP_STANDBY = 0;
    private static final int STATE_STEP_WRITING = 1;
    private static final String TAG = "WritingBuddyImpl";
    public static final int TEMPLATE_ALARM = 12;
    public static final int TEMPLATE_ALARM_AM = 13;
    public static final int TEMPLATE_ALARM_PM = 14;
    public static final int TEMPLATE_CALCULATOR = 20;
    public static final int TEMPLATE_DATEPICKER = 18;
    public static final int TEMPLATE_DATEPICKER_NO_YEAR = 19;
    public static final int TEMPLATE_DIALER = 21;
    public static final int TEMPLATE_EDITOR = 22;
    public static final int TEMPLATE_NONE = 0;
    public static final int TEMPLATE_TIME = 1;
    public static final int TEMPLATE_TIMEPICKER = 15;
    public static final int TEMPLATE_TIMEPICKER_AM = 16;
    public static final int TEMPLATE_TIMEPICKER_PM = 17;
    public static final int TEMPLATE_TIMER = 11;
    public static final int TYPE_BOARD_EDITOR = 1;
    public static final int TYPE_BOARD_NONE = 0;
    public static final int TYPE_BOARD_TEMPLATE = 2;
    public static final int TYPE_EDITOR_DATETIME = 3;
    public static final int TYPE_EDITOR_NONE = 0;
    public static final int TYPE_EDITOR_NUMBER = 1;
    public static final int TYPE_EDITOR_TEXT = 2;
    private View mAnchorView = null;
    private int mBoardTemplate;
    private int mBoardType;
    private boolean mCanShowAutoCompletePopup = true;
    private boolean mCanStartWritingBuddy = false;
    private int mEditCount = 0;
    private int mEditorType;
    private Handler mHandler;
    private boolean mIgnoreSizeChange = false;
    private OnImageWritingListener mImageWritingListener;
    private Rect mInitRect = null;
    private boolean mIsCursorBlinkDisabled = false;
    private boolean mIsHelpModeEnabled;
    private boolean mIsImageModePenDrawing = false;
    private boolean mIsImageWritingEnabled;
    private boolean mIsMathWritingEnabled;
    private boolean mIsMultiLineEditor = false;
    private boolean mIsPerformingAction = false;
    private boolean mIsPopupCueShowMSGCalled = false;
    private boolean mIsReceiveActionButtonEnabled;
    private boolean mIsWaitingHideSoftInput = false;
    private boolean mIsWatchActionEnabled;
    private MultiWindowStyle mMultiWindowStyle;
    private OnScrollChangedListener mOnScrollChangedListener = new OnScrollChangedListener() {
        public void onScrollChanged() {
            WritingBuddyImpl.this.notifyPositionChanged(11);
        }
    };
    private View mParentView;
    private PopupCue mPopupCue;
    private String mPrivateCMD;
    private OnPrivateCommandListener mPrivateCommandListener;
    private Rect mScrRectUpdated;
    IWritingBuddyClient mServiceCallback = new Stub() {
        public void onImageInserted(int clientId, Uri uri) throws RemoteException {
            Slog.d(WritingBuddyImpl.TAG, "mServiceCallback onImageAdded() : " + uri);
            Context context = WritingBuddyImpl.this.mParentView.getContext();
            if (uri != null && context != null) {
                try {
                    WritingBuddyImpl.this.mHandler.obtainMessage(1, Media.getBitmap(context.getContentResolver(), uri)).sendToTarget();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }

        public void onTextInserted(int clientId, int where, CharSequence text, int nextCursor) throws RemoteException {
            Slog.d(WritingBuddyImpl.TAG, "mServiceCallback onTextInserted()");
            WritingBuddyImpl.this.mHandler.obtainMessage(2, where, nextCursor, text).sendToTarget();
        }

        public void onTextDeleted(int clientId, int start, int end) throws RemoteException {
            Slog.d(WritingBuddyImpl.TAG, "mServiceCallback onTextDeleted()");
            WritingBuddyImpl.this.mHandler.obtainMessage(3, start, end).sendToTarget();
        }

        public void onResultReceived(int clientId, Bundle result) throws RemoteException {
            Slog.d(WritingBuddyImpl.TAG, "mServiceCallback onResultReceived() clientID : " + Integer.toHexString(clientId));
            WritingBuddyImpl.this.mHandler.obtainMessage(4, clientId, 0, result).sendToTarget();
        }

        public void onUpdateDialog(int clientId) throws RemoteException {
            Slog.d(WritingBuddyImpl.TAG, "mServiceCallback onUpdateDialog() clientID : " + Integer.toHexString(clientId));
            WritingBuddyImpl.this.mHandler.obtainMessage(9).sendToTarget();
        }
    };
    private int mShowCnt = 0;
    private int mState = 0;
    private int mStateResetCnt = 0;
    private OnTextUpdateListener mTextUpdateListener;
    private OnTextWritingListener mTextWritingListener;
    private int mViewID = 0;
    private IWritingBuddyManager mWBManager;
    private Rect mWBRect = null;
    private int mWindowMode = 0;
    private MotionEvent motionEvent = null;

    static class EventChecker {
        static int action = -1;
        static float x = -1.0f;
        static float y = -1.0f;

        EventChecker() {
        }

        public static boolean isDuplicated(MotionEvent event) {
            if (event == null) {
                action = -1;
                return false;
            }
            int newAction = event.getAction();
            float newX = event.getX();
            float newY = event.getY();
            if (newAction == action && newX == x && newY == y) {
                return true;
            }
            action = newAction;
            x = newX;
            y = newY;
            return false;
        }
    }

    public interface OnImageWritingListener {
        void onImageReceived(Bitmap bitmap);
    }

    public interface OnPrivateCommandListener {
        boolean onPrivateCommand(int i, CharSequence charSequence, Bundle bundle);
    }

    public interface OnTextUpdateListener {
        CharSequence onTextUpdated(CharSequence charSequence);
    }

    public interface OnTextWritingListener {
        void onTextReceived(CharSequence charSequence);
    }

    private static class WBHandler extends Handler {
        private final WeakReference<WritingBuddyImpl> mWritingBuddy;

        WBHandler(WritingBuddyImpl wb) {
            this.mWritingBuddy = new WeakReference(wb);
        }

        public void handleMessage(Message msg) {
            WritingBuddyImpl wb = (WritingBuddyImpl) this.mWritingBuddy.get();
            if (wb != null) {
                wb.handleMessage(msg);
            }
        }
    }

    public WritingBuddyImpl(View parentView) {
        initVariable();
        setParentView(parentView);
        if (parentView != null) {
            parentView.setWritingBuddy(this);
        }
        if (parentView instanceof EditText) {
            setBoardType(1);
        } else {
            setBoardType(2);
        }
    }

    private void initVariable() {
        this.mParentView = null;
        this.mWBManager = null;
        this.mPopupCue = null;
        this.mWBRect = new Rect();
        this.mIsImageWritingEnabled = false;
        this.mCanShowAutoCompletePopup = true;
        setBoardType(2);
        setEditorType(2);
        setBoardTemplate(0);
    }

    private boolean setupInRuntime() {
        boolean result = true;
        int mCurrentUserId = UserHandle.myUserId();
        String CURRENT_SERVICE_NAME = "";
        Slog.d(TAG, " setupInRuntime userId:" + mCurrentUserId);
        switch (mCurrentUserId) {
            case 10:
                CURRENT_SERVICE_NAME = "writingbuddymanagerservicerestricted0";
                break;
            case 11:
                CURRENT_SERVICE_NAME = "writingbuddymanagerservicerestricted1";
                break;
            case 12:
                CURRENT_SERVICE_NAME = "writingbuddymanagerservicerestricted2";
                break;
            case 13:
                CURRENT_SERVICE_NAME = "writingbuddymanagerservicerestricted3";
                break;
            case 14:
                CURRENT_SERVICE_NAME = "writingbuddymanagerservicerestricted4";
                break;
            case 15:
                CURRENT_SERVICE_NAME = "writingbuddymanagerservicerestricted5";
                break;
            case 100:
                CURRENT_SERVICE_NAME = "writingbuddymanagerserviceknox0";
                break;
            case 101:
                CURRENT_SERVICE_NAME = "writingbuddymanagerserviceknox1";
                break;
            case 102:
                CURRENT_SERVICE_NAME = "writingbuddymanagerserviceknox2";
                break;
            case 103:
                CURRENT_SERVICE_NAME = "writingbuddymanagerserviceknox3";
                break;
            case 104:
                CURRENT_SERVICE_NAME = "writingbuddymanagerserviceknox4";
                break;
            case 105:
                CURRENT_SERVICE_NAME = "writingbuddymanagerserviceknox5";
                break;
        }
        IBinder binder;
        try {
            CURRENT_SERVICE_NAME = "writingbuddymanagerservice";
            binder = ServiceManager.getService(CURRENT_SERVICE_NAME);
            this.mWBManager = IWritingBuddyManager.Stub.asInterface(binder);
            Slog.d(TAG, "setupInRuntime binder, binder:" + binder + ", CURRENT_SERVICE_NAME:" + CURRENT_SERVICE_NAME);
            if (this.mWBManager == null) {
                Slog.e(TAG, "Failed to get WritingBuddyService");
                result = false;
            }
        } catch (Exception e) {
            Slog.e(TAG, "Failed to get ActivityManager :: get default binder to avoid error, mWBManager:" + this.mWBManager);
            binder = ServiceManager.getService("writingbuddymanagerservice");
            if (this.mWBManager == null) {
                this.mWBManager = IWritingBuddyManager.Stub.asInterface(binder);
            }
        }
        getHandler();
        this.mEditCount = 0;
        return result;
    }

    public void setParentView(View view) {
        if (view == null) {
            Slog.d(TAG, "Reset parent View");
            this.mParentView = null;
            this.mAnchorView = null;
            this.mPopupCue = null;
            this.mWBManager = null;
            this.mHandler = null;
            return;
        }
        this.mParentView = view;
    }

    public void setBoardType(int type) {
        if (type == 1 || type == 2) {
            this.mBoardType = type;
            return;
        }
        throw new IllegalArgumentException("The board type should be one of TYPE_BOARD_EDITOR or TYPE_BOARD_TEMPLATE");
    }

    private int getModeFlag() {
        int flag = 0;
        if (this.mIsImageWritingEnabled) {
            flag = 0 | 1;
        }
        if (this.mIsMathWritingEnabled) {
            flag |= 2;
        }
        if (this.mIsHelpModeEnabled) {
            flag |= 4;
        }
        if (!(this.mParentView instanceof EditText)) {
            return flag;
        }
        Bundle b = ((EditText) this.mParentView).getInputExtras(true);
        if (b.getBoolean("isMmsMode", false)) {
            flag |= 8;
        }
        if (!b.getBoolean("isStartDrawingMode", false)) {
            return flag;
        }
        flag |= 16;
        b.putBoolean("isStartDrawingMode", false);
        return flag;
    }

    public void setImageWritingEnabled(boolean enabled) {
        this.mIsImageWritingEnabled = enabled;
    }

    public void setMathWritingEnabled(boolean enabled) {
        this.mIsMathWritingEnabled = enabled;
    }

    public void setPrivateCommnad(String cmd) {
        this.mPrivateCMD = cmd;
        if (PRIVATE_CMD_HELP_MODE.equals(this.mPrivateCMD)) {
            this.mIsHelpModeEnabled = true;
        } else if (PRIVATE_CMD_WACTH_ACTION.equals(this.mPrivateCMD)) {
            this.mIsWatchActionEnabled = true;
        } else if (PRIVATE_CMD_RECEIVE_ACTION_BUTTON.equals(this.mPrivateCMD)) {
            this.mIsReceiveActionButtonEnabled = true;
        } else {
            throw new IllegalArgumentException("The privateCommand should be  one of PRIVATE_CMD_HELP_MODE,PRIVATE_CMD_WACTH_ACTION or PRIVATE_CMD_RECEIVE_ACTION_BUTTON.");
        }
    }

    public void setAnchorView(View anchor) {
        this.mAnchorView = anchor;
    }

    public void setEditorType(int type) {
        if (type == 1 || type == 2) {
            this.mEditorType = type;
            return;
        }
        throw new IllegalArgumentException("You should set the Drawable, String, subDescription and Object in Param");
    }

    public int getEditorType() {
        return this.mEditorType;
    }

    public void setBoardTemplate(int template) {
        if (template == 0 || template == 1 || (template >= 11 && template <= 21)) {
            this.mBoardTemplate = template;
            return;
        }
        throw new IllegalArgumentException("Invalid Template Type");
    }

    public void notifyPositionChanged(int what) {
        Slog.d(TAG, "notifyPositionChanged code : " + what + " " + this.mState);
        if (this.mState != 0) {
            Rect wndRect;
            Rect scrRect;
            if (this.mBoardType == 1) {
                wndRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
                scrRect = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            } else {
                wndRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
                scrRect = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            }
            if (!this.mIgnoreSizeChange || this.mInitRect == null || (this.mInitRect.width() == wndRect.width() && this.mInitRect.height() == wndRect.height())) {
                if (DEBUG) {
                    Slog.d(TAG, "Update Position. wnd : " + wndRect + " scr : " + scrRect);
                }
                try {
                    if (this.mWBManager != null) {
                        this.mWBManager.updatePosition(this.mViewID, wndRect, scrRect);
                        if (this.mBoardType == 1) {
                            this.mScrRectUpdated = new Rect(scrRect);
                            getHandler().sendEmptyMessageDelayed(10, 300);
                        }
                    }
                } catch (RemoteException e) {
                    Slog.e(TAG, "Can not start WritingBuddy, RemoteException happened");
                }
            }
        }
    }

    private void notifyPositionCheck(int what) {
        Slog.d(TAG, "notifyPositionCheck code : " + what + " " + this.mState);
        if (this.mState != 0) {
            Rect wndRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            Rect scrRect = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            if (DEBUG) {
                Slog.d(TAG, "Update Position check. wnd : " + wndRect + " scr : " + scrRect);
            }
            if (!scrRect.equals(this.mScrRectUpdated)) {
                try {
                    if (this.mWBManager != null) {
                        this.mWBManager.updatePosition(this.mViewID, wndRect, scrRect);
                    }
                } catch (RemoteException e) {
                    Slog.e(TAG, "Can not start WritingBuddy, RemoteException happened");
                }
            }
        }
    }

    public void setOnTextWritingListener(OnTextWritingListener l) {
        this.mTextWritingListener = l;
    }

    public void setOnImageWritingListener(OnImageWritingListener l) {
        setImageWritingEnabled(l != null);
        this.mImageWritingListener = l;
    }

    public void setOnPrivateCommandListner(OnPrivateCommandListener l) {
        this.mPrivateCommandListener = l;
    }

    public void setOnTextUpdateListener(OnTextUpdateListener l) {
        this.mTextUpdateListener = l;
    }

    @Deprecated
    private void registerEventListener(View servedView) {
        servedView.setOnHoverListener(new OnHoverListener() {
            public boolean onHover(View v, MotionEvent event) {
                return WritingBuddyImpl.this.handleMotionEvent(v, event);
            }
        });
        servedView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return WritingBuddyImpl.this.handleMotionEvent(v, event);
            }
        });
    }

    public boolean isRunning() {
        return isWBRunning();
    }

    private boolean isWBRunning() {
        if (this.mState == 1 && this.mParentView != null && this.mParentView.isWritingBuddyEnabled() && this.mParentView.equals(this.mParentView.getCurrentWritingBuddyView())) {
            return true;
        }
        return false;
    }

    private Rect getTargetWBRect(View servedView, int type) {
        return this.mWBRect;
    }

    public Rect getTargetWBRect() {
        return this.mWBRect;
    }

    public Bitmap getBitmap() {
        return null;
    }

    public boolean getImageModePenDrawing() {
        return this.mIsImageModePenDrawing;
    }

    public void showPopup() {
        Slog.d(TAG, "showPopup");
        try {
            if (this.mWBManager != null) {
                this.mWBManager.showPopup(this.mViewID, 0);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Can not start showPopup, RemoteException happened");
        }
    }

    public Rect getExpectedTargetWBRect() {
        return getTargetWBRect(this.mParentView, this.mBoardType);
    }

    public void finish() {
        Slog.d(TAG, "Finish WritingBuddy");
        if (this.mIsPerformingAction && this.mIsImageWritingEnabled) {
            Slog.d(TAG, "Cancel finish.");
        } else {
            finish(true);
        }
    }

    public void finish(boolean immediate) {
        Slog.d(TAG, "Finish : " + immediate);
    }

    private void resetState() {
        if (this.mIsCursorBlinkDisabled && (this.mParentView instanceof EditText)) {
            ((EditText) this.mParentView).stopCursorBlink(false);
            this.mIsCursorBlinkDisabled = false;
        }
        unregisterPositionChangeListener();
        if (this.mParentView != null && this.mParentView.equals(this.mParentView.getCurrentWritingBuddyView())) {
            this.mParentView.reportCurrentWritingBuddyView(null);
            Slog.d(TAG, "Report current WB : " + null);
        }
        this.mCanShowAutoCompletePopup = true;
        this.mState = 0;
        this.mStateResetCnt = 0;
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return null;
    }

    public void onImageInserted(Bitmap b) {
        if (b != null && this.mIsImageWritingEnabled && this.mImageWritingListener != null && this.mParentView != null && this.mParentView.getWindowVisibility() == 0) {
            this.mImageWritingListener.onImageReceived(b);
        }
    }

    public void onTextInserted(int where, CharSequence text, int nextCursor) {
        Object obj = null;
        if (DEBUG) {
            Slog.d(TAG, "onTextInserted() : " + where + " " + (text != null ? Integer.valueOf(text.length()) : null) + " " + nextCursor);
        }
        if (this.mBoardType == 2) {
            if (this.mTextWritingListener != null && text != null) {
                this.mTextWritingListener.onTextReceived(text);
            }
        } else if ((this.mParentView instanceof EditText) && this.mState == 1) {
            CharSequence textBuffer = ((EditText) this.mParentView).getWBTextBuffer(false);
            if (DEBUG) {
                String str = TAG;
                StringBuilder append = new StringBuilder().append("onTextInserted() : ");
                if (textBuffer != null) {
                    obj = Integer.valueOf(textBuffer.length());
                }
                Slog.d(str, append.append(obj).toString());
            }
            if (textBuffer instanceof Editable) {
                Editable editable = (Editable) textBuffer;
                if (editable.length() < where) {
                    Slog.d(TAG, "onTextInserted() : where is out of bound editor length");
                    return;
                }
                if (editable.length() + text.length() < nextCursor) {
                    nextCursor = editable.length() + text.length();
                    Slog.d(TAG, "onTextInserted() : nextCursor position is more than total text length, set nextCursor to end of text");
                }
                editable.insert(where, text);
                Selection.setSelection(editable, nextCursor);
            }
            if (!(text == null || text.length() == 0)) {
                this.mEditCount++;
            }
            if (!TextUtils.isEmpty(text)) {
                sendHelpModeResult(HELP_MODE_RESULT_TEXT_INSERTED);
            }
        }
    }

    public void onTextDeleted(int start, int end) {
        if (DEBUG) {
            Slog.d(TAG, "onTextDeleted() : " + start + " " + end);
        }
        if (this.mParentView instanceof EditText) {
            CharSequence textBuffer = ((EditText) this.mParentView).getWBTextBuffer(false);
            if (DEBUG) {
                Slog.d(TAG, "onTextDeleted() : " + (textBuffer != null ? Integer.valueOf(textBuffer.length()) : null));
            }
            if (textBuffer instanceof Editable) {
                Editable editable = (Editable) textBuffer;
                if (textBuffer.length() >= end - start) {
                    if (textBuffer.length() < end) {
                        Slog.d(TAG, "onTextDeleted() : end is out of bound textBuffer length");
                        return;
                    }
                    editable.delete(start, end);
                } else {
                    return;
                }
            }
            this.mEditCount++;
        }
    }

    public void onResultReceived(Bundle result, int receivedClientID) {
        Slog.d(TAG, "onResultReceived " + result + " receivedClientID : " + Integer.toHexString(receivedClientID) + " current ClientID : " + Integer.toHexString(this.mViewID));
        if (result != null) {
            CharSequence cs;
            if (result.getInt(SERVICE_CB_CLIENT_CHANGED) > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_CLIENT_CHANGED ");
                if (this.mBoardType == 1 && (this.mParentView instanceof EditText)) {
                    if (this.mIsCursorBlinkDisabled) {
                        ((EditText) this.mParentView).stopCursorBlink(false);
                        this.mIsCursorBlinkDisabled = false;
                    }
                    if (this.mState == 1 && this.mEditCount > 0 && this.mParentView.isShown() && this.mParentView.getWindowVisibility() == 0) {
                        ((EditText) this.mParentView).applyWBTextBuffer(true);
                    }
                    ((EditText) this.mParentView).setWBTextBuffer(null);
                    this.mEditCount = 0;
                }
                if (this.mParentView != null && this.mParentView.equals(this.mParentView.getCurrentWritingBuddyView())) {
                    this.mParentView.reportCurrentWritingBuddyView(null);
                    Slog.d(TAG, "Report current WB : " + null);
                }
                unregisterPositionChangeListener();
                this.mState = 0;
            }
            if (result.getInt(SERVICE_CB_INFLATE_DONE) > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_INFLATE_DONE ");
                scheduleState(2, 1, null, receivedClientID);
            }
            int resultValue = result.getInt(SERVICE_CB_WRITING_DONE);
            if (resultValue > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_WRITING_DONE " + resultValue);
                this.mCanShowAutoCompletePopup = true;
                if (this.mParentView instanceof EditText) {
                    boolean isShown = this.mParentView.getVisibility() == 0;
                    if (isShown && (this.mParentView.getParent() instanceof View)) {
                        isShown = ((View) this.mParentView.getParent()).isShown();
                    }
                    if (this.mEditCount > 0 && isShown) {
                        ((EditText) this.mParentView).applyWBTextBuffer(resultValue == 1);
                    }
                    ((EditText) this.mParentView).setWBTextBuffer(null);
                    this.mEditCount = 0;
                }
            }
            int editorAction = result.getInt(SERVICE_CB_PERFORM_EDITOR_ACTION, -1);
            if (editorAction >= 0) {
                if (this.mParentView instanceof EditText) {
                    Slog.d(TAG, "onResultReceived SERVICE_CB_PERFORM_EDITOR_ACTION " + editorAction);
                    this.mIsPerformingAction = true;
                    ((EditText) this.mParentView).performWBEditorAction(editorAction);
                    this.mIsPerformingAction = false;
                }
                sendActionButtonResult(editorAction);
            }
            if (result.getInt(SERVICE_CB_CLOSED) > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_CLOSED ");
                CharSequence modifedText = result.getString(SERVICE_CB_INIT_TEXT);
                if (result.getInt(SERVICE_CB_CLOSED, -1) == 1) {
                    ((EditText) this.mParentView).setWBTextBuffer(modifedText);
                    ((EditText) this.mParentView).applyWBTextBuffer(true);
                }
                scheduleState(2, 2, null, receivedClientID);
                sendHelpModeResult(HELP_MODE_RESULT_CLOSED);
                sendWatchActionResult(HELP_MODE_RESULT_CLOSED, null);
            }
            if (result.getInt(SERVICE_CB_PRIVATE) > 0) {
                sendWatchActionResult(SERVICE_CB_PRIVATE, result);
            }
            if (this.mIsMathWritingEnabled && this.mTextWritingListener != null) {
                cs = result.getCharSequence(SERVICE_CB_MATH_WRITING_RESULT, null);
                if (cs != null) {
                    this.mTextWritingListener.onTextReceived(cs);
                    if (DEBUG) {
                        Slog.d(TAG, "onResultReceived SERVICE_CB_MATH_WRITING_RESULT : " + cs);
                    }
                }
            }
            if (this.mIsHelpModeEnabled && this.mTextWritingListener != null) {
                cs = result.getCharSequence(SERVICE_CB_HELP_MODE_RESULT, null);
                if (cs != null) {
                    this.mTextWritingListener.onTextReceived(cs);
                    if (DEBUG) {
                        Slog.d(TAG, "onResultReceived SERVICE_CB_HELP_MODE_RESULT : " + cs);
                    }
                }
            }
            if (result.getInt(SERVICE_CB_MMS_DATA_DELETE) > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_MMS_DATA_DELETE ");
                sendMMSDataDelete(MMS_DATA_DELETE);
            }
            int ImageMode = result.getInt(SERVICE_CB_DRAWING_MODE_SET);
            if (ImageMode > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_DRAWING_MODE_SET : " + ImageMode);
                this.mIsImageModePenDrawing = ImageMode == 1;
                if (this.mIsReceiveActionButtonEnabled && this.mPrivateCommandListener != null && this.mIsImageModePenDrawing) {
                    this.mPrivateCommandListener.onPrivateCommand(2, "DRAWING_MODE", null);
                }
            }
        }
    }

    public void onUpdateDialog() {
        Slog.d(TAG, "onUpdateDialog code : " + this.mState);
        if (this.mState != 0) {
            Rect wndRect;
            Rect scrRect;
            if (this.mBoardType == 1) {
                wndRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
                scrRect = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            } else {
                wndRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
                scrRect = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            }
            if (DEBUG) {
                Slog.d(TAG, "Update onUpdateDialog. wnd : " + wndRect + " scr : " + scrRect);
            }
            try {
                if (this.mWBManager != null) {
                    this.mWBManager.updateDialog(this.mViewID, wndRect, scrRect);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Can not start WritingBuddy, RemoteException happened");
            }
        }
    }

    private void sendHelpModeResult(CharSequence result) {
        if (this.mIsHelpModeEnabled && this.mTextWritingListener != null) {
            this.mTextWritingListener.onTextReceived(result);
        }
    }

    private void sendMMSDataDelete(CharSequence result) {
        if (this.mTextWritingListener != null) {
            this.mTextWritingListener.onTextReceived(result);
        }
    }

    private void sendWatchActionResult(CharSequence action, Bundle data) {
        if (this.mIsWatchActionEnabled && this.mPrivateCommandListener != null) {
            this.mPrivateCommandListener.onPrivateCommand(1, action, data);
        }
    }

    private void sendActionButtonResult(int action) {
        if (this.mIsReceiveActionButtonEnabled && this.mPrivateCommandListener != null) {
            String actionString;
            if (action == 4) {
                actionString = "ACTION_SEND";
            } else if (action == 3) {
                actionString = "ACTION_SEARCH";
            } else if (action == 2) {
                actionString = "ACTION_GO";
            } else if (action == 895) {
                actionString = "ACTION_IMAGE";
            } else {
                actionString = "ACTION_DONE";
            }
            this.mPrivateCommandListener.onPrivateCommand(2, actionString, null);
        }
    }

    private void resetPenPointerIcon() {
        try {
            PointerIcon.setHoveringSpenIcon(1, -1);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to change Pen Point to HOVERING_SPENICON_MORE");
        }
    }

    private void createPopupCue() {
        if (this.mPopupCue == null) {
            this.mPopupCue = new PopupCue(this.mParentView);
            this.mPopupCue.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == 0) {
                        if (WritingBuddyImpl.this.mPopupCue != null && WritingBuddyImpl.this.mPopupCue.isShowing()) {
                            WritingBuddyImpl.this.mPopupCue.switchCueButton(true);
                        }
                        if (v != null) {
                            v.playSoundEffect(0);
                        }
                        WritingBuddyImpl.this.getHandler().removeMessages(7);
                        InputMethodManager imm = InputMethodManager.peekInstance();
                        if (imm == null || !imm.isInputMethodShown()) {
                            Slog.d(WritingBuddyImpl.TAG, "Can not find IMM");
                            WritingBuddyImpl.this.showWritingBuddy();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    WritingBuddyImpl.this.dismissPopupCue(false);
                                }
                            }, 450);
                        } else {
                            WritingBuddyImpl.this.dismissPopupCue(false);
                            WritingBuddyImpl.this.mIsWaitingHideSoftInput = true;
                            imm.forceHideSoftInput();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    WritingBuddyImpl.this.mIsWaitingHideSoftInput = false;
                                    WritingBuddyImpl.this.showWritingBuddy();
                                }
                            }, 350);
                        }
                    } else if (action == 1 && WritingBuddyImpl.this.mBoardType == 1) {
                        WritingBuddyImpl.this.dismissPopupCue(false);
                    }
                    return false;
                }
            });
            this.mPopupCue.setOnHoverListener(new OnHoverListener() {
                public boolean onHover(View v, MotionEvent event) {
                    if (WritingBuddyImpl.this.getHandler().hasMessages(7)) {
                        WritingBuddyImpl.this.getHandler().removeMessages(7);
                    }
                    if (event.getAction() == 10 && WritingBuddyImpl.this.mState == 0) {
                        InputManager im = InputManager.getInstance();
                        if (!WritingBuddyImpl.this.pointInView(v, event.getX(), event.getY())) {
                            Slog.d(WritingBuddyImpl.TAG, "Close WritingBuddy cue : 1 " + WritingBuddyImpl.this.mIsPopupCueShowMSGCalled);
                            if (WritingBuddyImpl.this.mIsPopupCueShowMSGCalled) {
                                WritingBuddyImpl.this.mIsPopupCueShowMSGCalled = false;
                                WritingBuddyImpl.this.getHandler().removeMessages(7);
                            } else {
                                WritingBuddyImpl.this.getHandler().sendEmptyMessageDelayed(7, 150);
                            }
                        } else if (im != null && im.getScanCodeState(-1, WritingBuddyImpl.CLIENT_UNIQUE_ID_MASK, 320) == 0) {
                            Slog.d(WritingBuddyImpl.TAG, "Close WritingBuddy cue : 2");
                            WritingBuddyImpl.this.dismissPopupCue(true);
                            WritingBuddyImpl.this.sendHelpModeResult(WritingBuddyImpl.HELP_MODE_RESULT_HOVER_CANCELED);
                        } else if (WritingBuddyImpl.this.mPopupCue == null || !WritingBuddyImpl.this.mPopupCue.isAirButtonClicked()) {
                            Slog.d(WritingBuddyImpl.TAG, "Close WritingBuddy cue : 4");
                            WritingBuddyImpl.this.getHandler().sendEmptyMessageDelayed(7, 150);
                        } else {
                            Slog.d(WritingBuddyImpl.TAG, "Close WritingBuddy cue : 3");
                            WritingBuddyImpl.this.dismissPopupCue(true);
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void showWritingBuddyCue() {
        boolean isShown = false;
        getHandler().removeMessages(6);
        this.mIsPopupCueShowMSGCalled = false;
        if (this.mParentView == null) {
            Slog.d(TAG, "Caencel to show writingbuddy cue because mParentView is null");
            return;
        }
        if (this.mParentView.getVisibility() == 0) {
            isShown = true;
        }
        if (isShown && (this.mParentView.getParent() instanceof View)) {
            isShown = ((View) this.mParentView.getParent()).isShown();
        }
        if (isShown) {
            int cueType;
            if (this.mBoardType == 2) {
                Rect visualRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
                Rect viewRect = getRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView);
                if (visualRect.height() < viewRect.height() - 100 || visualRect.width() < viewRect.width() / 2) {
                    Slog.d(TAG, "Caencel to show writingbuddy cue. viewRect is smaller than wndRect");
                    Slog.d(TAG, "viewRect : " + viewRect.toShortString());
                    return;
                }
            }
            createPopupCue();
            if (this.mBoardType != 1) {
                cueType = 3;
            } else if (this.mIsMultiLineEditor) {
                cueType = 2;
            } else {
                cueType = 1;
            }
            this.mPopupCue.show(cueType, this.motionEvent);
            sendHelpModeResult(HELP_MODE_RESULT_HOVERED);
            return;
        }
        Slog.d(TAG, "Caencel to show writingbuddy cue.");
    }

    public boolean show() {
        return false;
    }

    private boolean showWritingBuddy() {
        boolean result = false;
        if (!setupInRuntime()) {
            return false;
        }
        IBinder wndToken = this.mParentView.getWindowToken();
        IBinder appToken = this.mParentView.getApplicationWindowToken();
        Rect wndRect = null;
        Rect scrRect = null;
        if (this.mBoardType == 1) {
            wndRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            scrRect = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
        } else if (this.mBoardType == 2) {
            View view;
            wndRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            if (this.mAnchorView != null) {
                view = this.mAnchorView;
            } else {
                view = this.mParentView;
            }
            scrRect = getVisibleRectOnScreen(view, true);
        }
        ExtractedText et;
        EditorInfo ei;
        Rect aniInitRect;
        int i;
        int i2;
        int flag;
        if (this.mBoardType == 1) {
            et = new ExtractedText();
            ei = new EditorInfo();
            EditorInfo upView_ei = new EditorInfo();
            if (this.mParentView instanceof EditText) {
                EditText parent = (EditText) this.mParentView;
                parent.extractText(new ExtractedTextRequest(), et);
                parent.extractEditorInfo(ei);
                ((EditText) this.mParentView).getWBTextBuffer(true);
                this.mEditCount = 0;
                int flagPrevNext = 0;
                View upView = this.mParentView.focusSearch(1);
                if (upView != null && (upView instanceof EditText) && upView.isWritingBuddyEnabled()) {
                    ((EditText) upView).extractEditorInfo(upView_ei);
                    if ((upView_ei.imeOptions & CLIENT_SEQUENCE_MASK) == 5 && !isPasswordInputType(upView)) {
                        ViewParent grandParent = upView.getParent();
                        if (grandParent == null || !(grandParent instanceof NumberPicker)) {
                            flagPrevNext = 0 | 1;
                        }
                    }
                }
                View downView = this.mParentView.focusSearch(2);
                if (downView != null && (downView instanceof EditText) && downView.isWritingBuddyEnabled() && (ei.imeOptions & CLIENT_SEQUENCE_MASK) == 5 && !isPasswordInputType(downView)) {
                    flagPrevNext |= 2;
                }
                if (ei.extras != null) {
                    ei.extras.putInt("flagPrevNext", flagPrevNext);
                }
            }
            if (TextUtils.isEmpty(ei.packageName)) {
                ei.packageName = this.mParentView.getContext().getPackageName();
            }
            if (this.mParentView.hasFocus() && ei.extras != null) {
                ei.extras.putBoolean("hasFocus", true);
            }
            if ((ei.inputType & 4080) == 16 && "com.sec.android.app.sbrowser".equals(ei.packageName) && !this.mParentView.hasFocus()) {
                this.mIgnoreSizeChange = true;
                this.mInitRect = new Rect(wndRect);
            } else {
                this.mIgnoreSizeChange = false;
            }
            if (this.mPopupCue == null || !this.mPopupCue.isShowing()) {
                aniInitRect = new Rect();
                aniInitRect.right = wndRect.width();
                aniInitRect.bottom = wndRect.height();
            } else {
                aniInitRect = this.mPopupCue.getRectInAnchor();
            }
            this.mViewID = ((this.mParentView.hashCode() & 4095) << 20) | ((this.mParentView.getId() & 4095) << 8);
            i = this.mViewID;
            i2 = this.mShowCnt + 1;
            this.mShowCnt = i2;
            this.mViewID = i | (i2 & CLIENT_SEQUENCE_MASK);
            flag = getModeFlag();
            if ((flag & 8) != 0) {
                dismissPopupCue(true);
            }
            try {
                this.mWBManager.show(this.mViewID, this.mServiceCallback.asBinder(), appToken, wndToken, wndRect, scrRect, aniInitRect, et, ei, flag);
                Slog.d(TAG, "startWritingBuddy " + this.mViewID + " " + this.mParentView);
                this.mParentView.reportCurrentWritingBuddyView(this.mParentView);
                Slog.d(TAG, "Report current WB : " + this.mParentView);
                registerPositionChangeListener();
                if (this.mParentView instanceof EditText) {
                    ((EditText) this.mParentView).hideCursorControllers();
                    ((TextView) this.mParentView).clearAllMultiSelection();
                }
                this.mCanShowAutoCompletePopup = false;
                this.mState = 1;
                result = true;
            } catch (RemoteException e) {
                Slog.e(TAG, "Can not start WritingBuddy, RemoteException happened " + e.toString());
                this.mWBManager = null;
                resetState();
                result = false;
            }
        } else if (this.mBoardType == 2) {
            et = new ExtractedText();
            ei = new EditorInfo();
            if (this.mEditorType == 1) {
                ei.inputType = 2;
            } else {
                ei.inputType = 1;
            }
            ei.imeOptions = 6;
            ei.packageName = this.mParentView.getContext().getPackageName();
            if (this.mTextUpdateListener != null) {
                et.text = this.mTextUpdateListener.onTextUpdated(et.text);
            }
            if (this.mPopupCue == null || !this.mPopupCue.isShowing()) {
                aniInitRect = new Rect();
                aniInitRect.right = wndRect.width();
                aniInitRect.bottom = wndRect.height();
            } else {
                aniInitRect = this.mPopupCue.getRectInAnchor();
            }
            this.mViewID = ((this.mParentView.hashCode() & 4095) << 20) | ((this.mParentView.getId() & 4095) << 8);
            i = this.mViewID;
            i2 = this.mShowCnt + 1;
            this.mShowCnt = i2;
            this.mViewID = i | (i2 & CLIENT_SEQUENCE_MASK);
            flag = getModeFlag();
            if (this.mParentView.hideCursorControllers(this.mParentView)) {
                Slog.d(TAG, "hideCursorControllers ");
            }
            try {
                this.mWBManager.showTemplate(this.mViewID, this.mServiceCallback.asBinder(), appToken, wndToken, wndRect, scrRect, aniInitRect, this.mBoardTemplate, et, ei, flag);
                Slog.d(TAG, "startWritingBuddy. " + this.mViewID + " " + this.mParentView);
                this.mParentView.reportCurrentWritingBuddyView(this.mParentView);
                Slog.d(TAG, "Report current WB : " + this.mParentView);
                this.mState = 1;
                result = true;
            } catch (RemoteException e2) {
                Slog.e(TAG, "Can not start WritingBuddy, RemoteException happened" + e2.toString());
                this.mWBManager = null;
                resetState();
                return false;
            }
        }
        getHandler().sendEmptyMessageDelayed(7, 1000);
        return result;
    }

    private boolean closeWritingBuddy(boolean immediate) {
        try {
            if (this.mWBManager != null) {
                this.mWBManager.dismiss(this.mViewID, immediate);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Can not close WritingBuddy, RemoteException happened");
        }
        return false;
    }

    private boolean canStartWritingBuddy(boolean refresh) {
        boolean result = this.mCanStartWritingBuddy;
        if (!refresh) {
            return result;
        }
        result = this.mParentView != null && this.mParentView.isWritingBuddyEnabled();
        if (result) {
            result = System.getInt(this.mParentView.getContext().getContentResolver(), "pen_writing_buddy", 0) == 1;
        }
        if (result && (this.mParentView instanceof EditText) && !(this.mParentView.isEnabled() && this.mParentView.isFocusable())) {
            result = false;
        }
        if (result && (this.mParentView instanceof EditText)) {
            LayoutParams lp = this.mParentView.getRootView().getLayoutParams();
            if (lp instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams wlp = (WindowManager.LayoutParams) lp;
                Slog.d(TAG, "canStartWritingBuddy : window type=" + wlp.type);
                if (wlp.type == 1000) {
                    result = false;
                }
            }
        }
        Context context = this.mParentView != null ? this.mParentView.getContext() : null;
        this.mWindowMode = getWindowMode();
        if (context != null) {
            this.mMultiWindowStyle = context.getAppMultiWindowStyle();
            if (context instanceof Activity) {
                if (result && (this.mWindowMode & 33554432) != 0 && (this.mWindowMode & 15) == 0 && (this.mWindowMode & 2048) != 0) {
                    result = false;
                }
            } else if ((context instanceof ContextWrapper) && this.mMultiWindowStyle != null && this.mMultiWindowStyle.getType() == 2 && this.mMultiWindowStyle.isEnabled(2048)) {
                result = false;
            }
            if (context.getResources().getConfiguration().mobileKeyboardCovered == 1) {
                result = false;
            }
        }
        if (result) {
            ViewParent p = this.mParentView.getParent();
            while (p != null && (p instanceof ViewGroup)) {
                if (((ViewGroup) p).isWritingBuddyEnabled()) {
                    result = false;
                    break;
                }
                p = p.getParent();
            }
        }
        if (result && (this.mParentView instanceof EditText)) {
            boolean z;
            float visibleRatio;
            EditorInfo ei = new EditorInfo();
            this.mParentView.extractEditorInfo(ei);
            int editorClass = ei.inputType & 15;
            int variation = ei.inputType & 4080;
            if (variation == 128 || variation == 144 || variation == 224 || (editorClass == 2 && variation == 16)) {
                result = false;
            }
            if (result && ei.extras != null && ei.extras.getBoolean("com.samsung.android/disableDirectPenInput", false)) {
                result = false;
            }
            if ((ei.inputType & 15) == 1) {
                int i = ei.inputType & 131072;
                ei.inputType = i;
                if (i > 0) {
                    z = true;
                    this.mIsMultiLineEditor = z;
                    if (result && !this.mIsMultiLineEditor && this.mParentView.getCurrentWritingBuddyView() == null) {
                        visibleRatio = ((float) getVisibleRectInWindow(this.mAnchorView == null ? this.mAnchorView : this.mParentView, false).height()) / ((float) getRectInWindow(this.mAnchorView == null ? this.mAnchorView : this.mParentView).height());
                        if (visibleRatio < 0.2f) {
                            result = false;
                            Slog.d(TAG, "View is scrolled." + visibleRatio);
                        }
                    }
                }
            }
            z = false;
            this.mIsMultiLineEditor = z;
            if (this.mAnchorView == null) {
            }
            if (this.mAnchorView == null) {
            }
            visibleRatio = ((float) getVisibleRectInWindow(this.mAnchorView == null ? this.mAnchorView : this.mParentView, false).height()) / ((float) getRectInWindow(this.mAnchorView == null ? this.mAnchorView : this.mParentView).height());
            if (visibleRatio < 0.2f) {
                result = false;
                Slog.d(TAG, "View is scrolled." + visibleRatio);
            }
        }
        this.mCanStartWritingBuddy = result;
        Slog.d(TAG, "canStartWritingBuddy() : " + result);
        return result;
    }

    private boolean canStartTemplateWritingBuddy(boolean refresh) {
        boolean result = this.mCanStartWritingBuddy;
        if (!refresh) {
            return result;
        }
        Context context;
        if (this.mParentView == null || !this.mParentView.isWritingBuddyEnabled()) {
            result = false;
        } else {
            result = true;
        }
        if (result) {
            if (System.getInt(this.mParentView.getContext().getContentResolver(), "pen_writing_buddy", 0) == 1) {
                result = true;
            } else {
                result = false;
            }
        }
        if (this.mParentView != null) {
            context = this.mParentView.getContext();
        } else {
            context = null;
        }
        this.mWindowMode = getWindowMode();
        if (context != null) {
            boolean isMobileKeyboard;
            this.mMultiWindowStyle = context.getAppMultiWindowStyle();
            if (context instanceof Activity) {
                if (result && (this.mWindowMode & 33554432) != 0 && (this.mWindowMode & 15) == 0 && (this.mWindowMode & 2048) != 0) {
                    result = false;
                }
            } else if ((context instanceof ContextWrapper) && this.mMultiWindowStyle != null && this.mMultiWindowStyle.getType() == 2 && this.mMultiWindowStyle.isEnabled(2048)) {
                result = false;
            }
            if (context.getResources().getConfiguration().mobileKeyboardCovered == 1) {
                isMobileKeyboard = true;
            } else {
                isMobileKeyboard = false;
            }
            if (isMobileKeyboard) {
                result = false;
            }
        }
        if (result) {
            ViewParent p = this.mParentView.getParent();
            while (p != null && (p instanceof ViewGroup)) {
                if (((ViewGroup) p).isWritingBuddyEnabled()) {
                    result = false;
                    break;
                }
                p = p.getParent();
            }
        }
        if (result) {
            Rect visibleRect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            Rect viewRect = getRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView);
            if (visibleRect.top != viewRect.top) {
                result = false;
                if (DEBUG) {
                    Slog.d(TAG, "VisibleRect : " + visibleRect.toShortString() + " ViewRect : " + viewRect.toShortString());
                }
                if (this.mPopupCue != null && this.mPopupCue.isShowing()) {
                    this.mPopupCue.dismiss(false);
                    this.mPopupCue = null;
                }
            }
        }
        this.mCanStartWritingBuddy = result;
        Slog.d(TAG, "canStartWritingBuddy() : " + result);
        return result;
    }

    public boolean canShowAutoCompletePopup() {
        if (this.mState == 1) {
            return this.mCanShowAutoCompletePopup;
        }
        return true;
    }

    private boolean dismissPopupCue(boolean animation) {
        Slog.d(TAG, "dismissPopupCue()");
        boolean ret = false;
        if (this.mHandler != null) {
            this.mHandler.removeMessages(6);
            this.mIsPopupCueShowMSGCalled = false;
        }
        if (this.mPopupCue != null) {
            if (this.mPopupCue.isShowing()) {
                ret = true;
            }
            this.mPopupCue.dismiss(animation);
        }
        return ret;
    }

    public boolean handleWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            if (this.mBoardType == 2 && this.mPopupCue != null && this.mPopupCue.isShowing()) {
                this.mPopupCue.dismiss(false);
                this.mPopupCue = null;
            }
            if (this.mBoardType == 1 && isWBRunning()) {
                Slog.d(TAG, "Update.");
                this.mHandler.sendEmptyMessage(5);
            }
        } else {
            if (this.mPopupCue != null && this.mPopupCue.isShowing()) {
                this.mPopupCue.dismiss(false);
                this.mPopupCue = null;
            }
            if (this.mIsPopupCueShowMSGCalled && this.mHandler != null) {
                this.mHandler.removeMessages(6);
                this.mIsPopupCueShowMSGCalled = false;
            }
            if (isWBRunning() && (this.mWindowMode & -16777216) != (getWindowMode() & -16777216)) {
                Slog.d(TAG, "Window mode changed.");
                finish(true);
            }
        }
        return false;
    }

    public boolean handleMotionEvent(View view, MotionEvent event) {
        if (event.getToolType(0) != 2 || this.mIsWaitingHideSoftInput) {
            return false;
        }
        if (event.getAction() == 9) {
            Slog.d(TAG, "handleMotionEvent ACTION_HOVER_ENTER");
            if (!setupInRuntime()) {
                startWritingBuddyService();
            }
        }
        return scheduleState(1, 0, event, 0);
    }

    private void startWritingBuddyService() {
        try {
            int mCurrentUserId = UserHandle.myUserId();
            Slog.w(TAG, "Starting writingbuddy service id : " + mCurrentUserId);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.samsung.android.writingbuddyservice", "com.samsung.android.writingbuddyservice.WritingBuddyServiceStarter"));
            Context context = this.mParentView != null ? this.mParentView.getContext() : null;
            if (context != null) {
                context.startServiceAsUser(intent, new UserHandle(mCurrentUserId));
            }
        } catch (Exception e) {
            Slog.w(TAG, "Starting writingbuddy service failed: " + e);
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                onImageInserted((Bitmap) msg.obj);
                return;
            case 2:
                onTextInserted(msg.arg1, msg.obj, msg.arg2);
                return;
            case 3:
                onTextDeleted(msg.arg1, msg.arg2);
                return;
            case 4:
                onResultReceived(new Bundle((Bundle) msg.obj), msg.arg1);
                return;
            case 5:
                notifyPositionChanged(0);
                return;
            case 6:
                showWritingBuddyCue();
                return;
            case 7:
                dismissPopupCue(true);
                sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                return;
            case 9:
                onUpdateDialog();
                return;
            case 10:
                notifyPositionCheck(0);
                return;
            default:
                return;
        }
    }

    private void unregisterPositionChangeListener() {
        if (this.mBoardType == 1) {
            if (this.mParentView instanceof EditText) {
                ((EditText) this.mParentView).setWBPositionListenerEnalbed(false);
            }
        } else if (this.mParentView != null) {
            ViewTreeObserver vto = this.mParentView.getViewTreeObserver();
            if (vto != null) {
                vto.removeOnScrollChangedListener(this.mOnScrollChangedListener);
            }
        }
    }

    private void registerPositionChangeListener() {
        if (this.mBoardType == 1) {
            if (this.mParentView instanceof EditText) {
                ((EditText) this.mParentView).setWBPositionListenerEnalbed(true);
            }
        } else if (this.mParentView != null) {
            ViewTreeObserver vto = this.mParentView.getViewTreeObserver();
            if (vto != null) {
                vto.removeOnScrollChangedListener(this.mOnScrollChangedListener);
                vto.addOnScrollChangedListener(this.mOnScrollChangedListener);
            }
        }
    }

    private boolean scheduleState(int eventType, int event, MotionEvent motionEvent, int arg1) {
        if (this.mBoardType == 1) {
            return scheduleStateForEditor(eventType, event, motionEvent, arg1);
        }
        return scheduleStateForTemplate(eventType, event, motionEvent, arg1);
    }

    private boolean scheduleStateForEditor(int eventType, int event, MotionEvent motionEvent, int arg1) {
        this.motionEvent = motionEvent;
        if (eventType == 2 && event == 2) {
            if (this.mViewID == arg1) {
                resetState();
            }
            Slog.d(TAG, "state : " + this.mState + "." + eventType + "." + event + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null) + ".  " + Integer.toHexString(this.mParentView != null ? this.mParentView.hashCode() : 0));
            return false;
        }
        boolean showLog = true;
        if (this.mState == 0) {
            if (eventType == 1) {
                int action = motionEvent.getAction();
                if (action == 9 && canStartWritingBuddy(true)) {
                    Slog.d(TAG, "ACTION_HOVER_ENTER");
                    if (getHandler().hasMessages(7)) {
                        Slog.d(TAG, "ACTION_HOVER_ENTER_1");
                        getHandler().removeMessages(7);
                    }
                    if (!getHandler().hasMessages(8)) {
                        Slog.d(TAG, "ACTION_HOVER_ENTER_2");
                        getHandler().sendEmptyMessageDelayed(6, 150);
                        this.mIsPopupCueShowMSGCalled = true;
                    }
                } else if (action == 10 && canStartWritingBuddy(false)) {
                    Slog.d(TAG, "ACTION_HOVER_EXIT");
                    InputManager im = InputManager.getInstance();
                    if (!pointInView(motionEvent.getX(), motionEvent.getY())) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_1");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    } else if (im == null || im.getScanCodeState(-1, CLIENT_UNIQUE_ID_MASK, 320) != 0) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_4");
                        getHandler().sendEmptyMessageDelayed(7, 40);
                    } else {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_2");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    }
                } else if (action == 0 && canStartWritingBuddy(false)) {
                    Slog.d(TAG, "ACTION_DOWN");
                    dismissPopupCue(false);
                } else if (action == 1 && canStartWritingBuddy(false)) {
                    Slog.d(TAG, "ACTION_UP");
                    getHandler().sendEmptyMessageDelayed(8, 30);
                }
                if (action == 7) {
                    showLog = false;
                }
            }
        } else if (this.mState == 1) {
            if (eventType == 1 && motionEvent.getAction() == 9) {
                int i = this.mStateResetCnt + 1;
                this.mStateResetCnt = i;
                if (i > 3) {
                    Slog.d(TAG, "Reset state");
                    this.mStateResetCnt = 0;
                    resetState();
                }
            }
            if (eventType == 2 && event == 1) {
                dismissPopupCue(false);
                resetPenPointerIcon();
                if (this.mParentView instanceof EditText) {
                    this.mParentView.requestFocus();
                }
                InputMethodManager imm = InputMethodManager.peekInstance();
                if (imm != null) {
                    imm.forceHideSoftInput();
                } else {
                    Slog.d(TAG, "Can not find IMM");
                }
                if (this.mParentView instanceof EditText) {
                    ((EditText) this.mParentView).stopCursorBlink(true);
                    this.mIsCursorBlinkDisabled = true;
                }
                sendHelpModeResult(HELP_MODE_RESULT_OPENED);
                sendWatchActionResult(HELP_MODE_RESULT_OPENED, null);
                getHandler().sendEmptyMessageDelayed(5, 150);
            }
        }
        if (showLog) {
            Slog.d(TAG, "state : " + this.mState + "." + eventType + "." + event + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null) + ".  " + Integer.toHexString(this.mParentView != null ? this.mParentView.hashCode() : 0));
        }
        return false;
    }

    private boolean scheduleStateForTemplate(int eventType, int event, MotionEvent motionEvent, int arg1) {
        if (EventChecker.isDuplicated(motionEvent)) {
            return false;
        }
        this.motionEvent = motionEvent;
        boolean showLog = true;
        if (eventType == 2 && event == 2) {
            dismissPopupCue(false);
            resetState();
            Slog.d(TAG, "state : " + this.mState + "." + eventType + ". " + event + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null));
            return false;
        }
        if (this.mState == 0) {
            if (eventType == 1) {
                int action = motionEvent.getAction();
                if (action == 9 && canStartTemplateWritingBuddy(true)) {
                    Slog.d(TAG, "ACTION_HOVER_ENTER");
                    if (!getHandler().hasMessages(8)) {
                        getHandler().sendEmptyMessageDelayed(6, 150);
                        this.mIsPopupCueShowMSGCalled = true;
                    }
                } else if (action == 7) {
                    Slog.d(TAG, "ACTION_HOVER_MOVE");
                    if (getHandler().hasMessages(7)) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_2");
                        getHandler().removeMessages(7);
                    }
                } else if (action == 10 && canStartTemplateWritingBuddy(false)) {
                    Slog.d(TAG, "ACTION_HOVER_EXIT");
                    InputManager im = InputManager.getInstance();
                    if (!pointInView(motionEvent.getX(), motionEvent.getY())) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_1");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    } else if (im != null && im.getScanCodeState(-1, CLIENT_UNIQUE_ID_MASK, 320) == 0) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_2");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    } else if (this.mPopupCue == null || !this.mPopupCue.isShowing()) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_4");
                        getHandler().sendEmptyMessageDelayed(7, 30);
                    } else {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_3");
                        getHandler().sendEmptyMessageDelayed(7, 30);
                    }
                } else if (action == 0 && canStartTemplateWritingBuddy(false)) {
                    Slog.d(TAG, "ACTION_DOWN");
                    dismissPopupCue(true);
                } else if (action == 1 && canStartTemplateWritingBuddy(false)) {
                    Slog.d(TAG, "ACTION_UP");
                    getHandler().sendEmptyMessageDelayed(8, 30);
                }
                if (action == 7) {
                    showLog = false;
                }
            }
        } else if (this.mState == 1) {
            if (eventType == 1) {
                if (motionEvent.getAction() == 9) {
                    int i = this.mStateResetCnt + 1;
                    this.mStateResetCnt = i;
                    if (i > 3) {
                        Slog.d(TAG, "Reset state");
                        this.mStateResetCnt = 0;
                        resetState();
                    }
                }
            } else if (eventType == 2 && event == 1) {
                InputMethodManager imm = InputMethodManager.peekInstance();
                if (imm != null) {
                    imm.forceHideSoftInput(new ResultReceiver(new Handler()) {
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            WritingBuddyImpl.this.getHandler().sendEmptyMessageDelayed(5, 150);
                        }
                    });
                } else {
                    Slog.d(TAG, "Can not find IMM");
                }
                dismissPopupCue(false);
                sendHelpModeResult(HELP_MODE_RESULT_OPENED);
                sendWatchActionResult(HELP_MODE_RESULT_OPENED, null);
                resetPenPointerIcon();
                getHandler().sendEmptyMessageDelayed(5, 200);
            }
        }
        if (showLog) {
            Slog.d(TAG, "state : " + this.mState + "." + eventType + ". " + event + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null));
        }
        return false;
    }

    private int getWindowMode() {
        Context context = this.mParentView != null ? this.mParentView.getContext() : null;
        if (context == null || !(context instanceof Activity)) {
            return 16777216;
        }
        return ((Activity) context).getWindowMode();
    }

    private Handler getHandler() {
        if (this.mHandler == null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                this.mHandler = new WBHandler(this);
            } else {
                Slog.e(TAG, "Attempting to create Handler from background thread.");
                this.mHandler = new WBHandler(this);
            }
        }
        return this.mHandler;
    }

    private int convertDPtoPX(float dp, DisplayMetrics displayMetrics) {
        DisplayMetrics dm = displayMetrics;
        if (dm == null) {
            dm = this.mParentView.getContext().getResources().getDisplayMetrics();
        }
        return (int) (TypedValue.applyDimension(1, dp, dm) + 0.5f);
    }

    private boolean pointInView(float localX, float localY) {
        return pointInView(this.mParentView, localX, localY);
    }

    private boolean pointInView(View v, float localX, float localY) {
        return localX >= 0.0f && localX < ((float) (v.getRight() - v.getLeft())) && localY >= 0.0f && localY < ((float) (v.getBottom() - v.getTop()));
    }

    private Rect getRectOnScreen(View view) {
        Rect r = new Rect(0, 0, 0, 0);
        if (view != null) {
            int[] locOnScr = new int[]{0, 0};
            view.getLocationOnScreen(locOnScr);
            r.set(locOnScr[0], locOnScr[1], locOnScr[0] + view.getWidth(), locOnScr[1] + view.getHeight());
        }
        return r;
    }

    private Rect getRectInWindow(View view) {
        Rect r = new Rect(0, 0, 0, 0);
        if (view != null) {
            int[] locInWindow = new int[]{0, 0};
            view.getLocationInWindow(locInWindow);
            r.set(locInWindow[0], locInWindow[1], locInWindow[0] + view.getWidth(), locInWindow[1] + view.getHeight());
        }
        return r;
    }

    private Rect getVisibleRectInWindow(View view, boolean chechkWidth) {
        int widthNormalizer = this.mParentView.getContext().getResources().getDisplayMetrics().widthPixels;
        Rect r = getRectInWindow(view);
        View v = view;
        ViewParent vp = view.getParent();
        int top = 0;
        int bottomDiff = 0;
        int left = 0;
        int rightDiff = 0;
        while (vp instanceof View) {
            View parent = (View) vp;
            int y = (int) v.getY();
            top += y;
            if (y < 0 && top < 0) {
                r.top += Math.abs(top);
                top = 0;
            }
            if (parent.getScrollY() > 0) {
                if (parent.getScrollY() > top) {
                    r.top += parent.getScrollY() - top;
                    top = 0;
                } else {
                    top -= parent.getScrollY();
                }
            }
            int bottomPosY = (((int) v.getY()) + v.getHeight()) - parent.getScrollY();
            if (bottomPosY + bottomDiff < parent.getHeight()) {
                bottomDiff = -(parent.getHeight() - (bottomPosY + bottomDiff));
            } else {
                r.bottom -= (bottomPosY + bottomDiff) - parent.getHeight();
                bottomDiff = 0;
            }
            if (chechkWidth) {
                int x = ((int) v.getX()) % widthNormalizer;
                left += x;
                if (x < 0 && left < 0) {
                    r.left += Math.abs(left);
                    left = 0;
                }
                int parentScrollX = parent.getScrollX() % widthNormalizer;
                if (parentScrollX > 0) {
                    if (parentScrollX > left) {
                        r.left += parentScrollX - left;
                        left = 0;
                    } else {
                        left -= parentScrollX;
                    }
                }
                int rightPosX = (v.getWidth() + x) - parentScrollX;
                if (rightPosX + rightDiff < parent.getWidth()) {
                    rightDiff = -(parent.getWidth() - (rightPosX + rightDiff));
                } else {
                    r.right -= (rightPosX + rightDiff) - parent.getWidth();
                    rightDiff = 0;
                }
            }
            v = parent;
            vp = parent.getParent();
        }
        if (DEBUG) {
            Slog.d(TAG, "getVisibleRectInWindow : " + r.toShortString());
        }
        return r;
    }

    private Rect getVisibleRectOnScreen(View view, boolean chechkWidth) {
        int widthNormalizer = this.mParentView.getContext().getResources().getDisplayMetrics().widthPixels;
        Rect r = getRectOnScreen(view);
        View v = view;
        ViewParent vp = view.getParent();
        int top = 0;
        int bottomDiff = 0;
        int left = 0;
        int rightDiff = 0;
        while (vp instanceof View) {
            View parent = (View) vp;
            int y = (int) v.getY();
            top += y;
            if (y < 0 && top < 0) {
                r.top += Math.abs(top);
                top = 0;
            }
            if (parent.getScrollY() > 0) {
                if (parent.getScrollY() > top) {
                    r.top += parent.getScrollY() - top;
                    top = 0;
                } else {
                    top -= parent.getScrollY();
                }
            }
            int bottomPosY = (((int) v.getY()) + v.getHeight()) - parent.getScrollY();
            if (bottomPosY + bottomDiff < parent.getHeight()) {
                bottomDiff = -(parent.getHeight() - (bottomPosY + bottomDiff));
            } else {
                r.bottom -= (bottomPosY + bottomDiff) - parent.getHeight();
                bottomDiff = 0;
            }
            if (chechkWidth) {
                int x = ((int) v.getX()) % widthNormalizer;
                left += x;
                if (x < 0 && left < 0) {
                    r.left += Math.abs(left);
                    left = 0;
                }
                int parentScrollX = parent.getScrollX() % widthNormalizer;
                if (parentScrollX > 0) {
                    if (parentScrollX > left) {
                        r.left += parentScrollX - left;
                        left = 0;
                    } else {
                        left -= parentScrollX;
                    }
                }
                int rightPosX = (v.getWidth() + x) - parentScrollX;
                if (rightPosX + rightDiff < parent.getWidth()) {
                    rightDiff = -(parent.getWidth() - (rightPosX + rightDiff));
                } else {
                    r.right -= (rightPosX + rightDiff) - parent.getWidth();
                    rightDiff = 0;
                }
            }
            v = parent;
            vp = parent.getParent();
        }
        if (DEBUG) {
            Slog.d(TAG, "getVisibleRectOnScreen : " + r.toShortString());
        }
        return r;
    }

    private boolean isPasswordInputType(View v) {
        if (v == null) {
            return false;
        }
        EditText et = (EditText) v;
        EditorInfo ei = new EditorInfo();
        et.extractEditorInfo(ei);
        if (ei.inputType == 128 || ei.inputType == 144 || ei.inputType == 224 || ((ei.inputType == 2 && ei.inputType == 16) || ei.inputType == 129 || ei.inputType == 145)) {
            return true;
        }
        return false;
    }
}
