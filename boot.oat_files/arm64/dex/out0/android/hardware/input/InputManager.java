package android.hardware.input;

import android.content.Context;
import android.graphics.Color;
import android.hardware.input.IInputDevicesChangedListener.Stub;
import android.media.AudioAttributes;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Vibrator;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputDevice;
import android.view.InputEvent;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
import java.util.List;

public final class InputManager {
    public static final String ACTION_QUERY_KEYBOARD_LAYOUTS = "android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS";
    private static final boolean DEBUG = false;
    public static final int DEFAULT_POINTER_SPEED = 0;
    public static final int FLAG_MONITOR_KEY_FILTER = 1;
    public static final int FLAG_MONITOR_MOTION_FILTER = 2;
    public static final int FLAG_MONITOR_NO_FILTER = 0;
    public static final int FLAG_MONITOR_SOURCE_CLASS_POINTER_FILTER = 4;
    public static final int FLAG_MONITOR_TOOL_TYPE_FINGER_FILTER = 16;
    public static final int FLAG_MONITOR_TOOL_TYPE_STYLUS_FILTER = 8;
    public static final int INJECT_INPUT_EVENT_MODE_ASYNC = 0;
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = 2;
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = 1;
    public static final int MAX_POINTER_SPEED = 7;
    public static final String META_DATA_KEYBOARD_LAYOUTS = "android.hardware.input.metadata.KEYBOARD_LAYOUTS";
    public static final int MIN_POINTER_SPEED = -7;
    private static final int MSG_DEVICE_ADDED = 1;
    private static final int MSG_DEVICE_CHANGED = 3;
    private static final int MSG_DEVICE_REMOVED = 2;
    public static final int SWITCH_STATE_OFF = 0;
    public static final int SWITCH_STATE_ON = 1;
    public static final int SWITCH_STATE_UNKNOWN = -1;
    private static final String TAG = "InputManager";
    private static InputManager sInstance;
    private final IInputManager mIm;
    private final ArrayList<InputDeviceListenerDelegate> mInputDeviceListeners = new ArrayList();
    private SparseArray<InputDevice> mInputDevices;
    private InputDevicesChangedListener mInputDevicesChangedListener;
    private final Object mInputDevicesLock = new Object();
    private List<OnTabletModeChangedListenerDelegate> mOnTabletModeChangedListeners;
    private TabletModeChangedListener mTabletModeChangedListener;
    private final Object mTabletModeLock = new Object();

    public interface InputDeviceListener {
        void onInputDeviceAdded(int i);

        void onInputDeviceChanged(int i);

        void onInputDeviceRemoved(int i);
    }

    private static final class InputDeviceListenerDelegate extends Handler {
        public final InputDeviceListener mListener;

        public InputDeviceListenerDelegate(InputDeviceListener listener, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper());
            this.mListener = listener;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onInputDeviceAdded(msg.arg1);
                    return;
                case 2:
                    this.mListener.onInputDeviceRemoved(msg.arg1);
                    return;
                case 3:
                    this.mListener.onInputDeviceChanged(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    private final class InputDeviceVibrator extends Vibrator {
        private final int mDeviceId;
        private final Binder mToken = new Binder();

        public InputDeviceVibrator(int deviceId) {
            this.mDeviceId = deviceId;
        }

        public boolean hasVibrator() {
            return true;
        }

        public void vibrate(int uid, String opPkg, long milliseconds, AudioAttributes attributes) {
            vibrate(new long[]{0, milliseconds}, -1);
        }

        public void vibrate(int uid, String opPkg, long[] pattern, int repeat, AudioAttributes attributes) {
            if (repeat >= pattern.length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            try {
                InputManager.this.mIm.vibrate(this.mDeviceId, pattern, repeat, this.mToken);
            } catch (RemoteException ex) {
                Log.w(InputManager.TAG, "Failed to vibrate.", ex);
            }
        }

        public void cancel() {
            try {
                InputManager.this.mIm.cancelVibrate(this.mDeviceId, this.mToken);
            } catch (RemoteException ex) {
                Log.w(InputManager.TAG, "Failed to cancel vibration.", ex);
            }
        }
    }

    private final class InputDevicesChangedListener extends Stub {
        private InputDevicesChangedListener() {
        }

        public void onInputDevicesChanged(int[] deviceIdAndGeneration) throws RemoteException {
            InputManager.this.onInputDevicesChanged(deviceIdAndGeneration);
        }
    }

    public interface OnTabletModeChangedListener {
        void onTabletModeChanged(long j, boolean z);
    }

    private static final class OnTabletModeChangedListenerDelegate extends Handler {
        private static final int MSG_TABLET_MODE_CHANGED = 0;
        public final OnTabletModeChangedListener mListener;

        public OnTabletModeChangedListenerDelegate(OnTabletModeChangedListener listener, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper());
            this.mListener = listener;
        }

        public void sendTabletModeChanged(long whenNanos, boolean inTabletMode) {
            SomeArgs args = SomeArgs.obtain();
            args.argi1 = (int) (-1 & whenNanos);
            args.argi2 = (int) (whenNanos >> 32);
            args.arg1 = Boolean.valueOf(inTabletMode);
            obtainMessage(0, args).sendToTarget();
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SomeArgs args = msg.obj;
                    this.mListener.onTabletModeChanged((((long) args.argi1) & 4294967295L) | (((long) args.argi2) << 32), ((Boolean) args.arg1).booleanValue());
                    return;
                default:
                    return;
            }
        }
    }

    public enum TSP_CMDTYPE {
        TSP_CMDTYPE_EMPTY(0),
        TSP_CMDTYPE_SPAY(1);
        
        private int mvalue;

        private TSP_CMDTYPE(int value) {
            this.mvalue = value;
        }

        public int getvalue() {
            return this.mvalue;
        }
    }

    private final class TabletModeChangedListener extends ITabletModeChangedListener.Stub {
        private TabletModeChangedListener() {
        }

        public void onTabletModeChanged(long whenNanos, boolean inTabletMode) {
            InputManager.this.onTabletModeChanged(whenNanos, inTabletMode);
        }
    }

    private InputManager(IInputManager im) {
        this.mIm = im;
    }

    public static InputManager getInstance() {
        InputManager inputManager;
        synchronized (InputManager.class) {
            if (sInstance == null) {
                sInstance = new InputManager(IInputManager.Stub.asInterface(ServiceManager.getService("input")));
            }
            inputManager = sInstance;
        }
        return inputManager;
    }

    public InputDevice getInputDevice(int id) {
        InputDevice inputDevice;
        synchronized (this.mInputDevicesLock) {
            populateInputDevicesLocked();
            int index = this.mInputDevices.indexOfKey(id);
            if (index < 0) {
                inputDevice = null;
            } else {
                inputDevice = (InputDevice) this.mInputDevices.valueAt(index);
                if (inputDevice == null) {
                    try {
                        inputDevice = this.mIm.getInputDevice(id);
                        if (inputDevice != null) {
                            this.mInputDevices.setValueAt(index, inputDevice);
                        }
                    } catch (RemoteException ex) {
                        throw new RuntimeException("Could not get input device information.", ex);
                    }
                }
            }
        }
        return inputDevice;
    }

    public InputDevice getInputDeviceByDescriptor(String descriptor) {
        if (descriptor == null) {
            throw new IllegalArgumentException("descriptor must not be null.");
        }
        InputDevice inputDevice;
        synchronized (this.mInputDevicesLock) {
            populateInputDevicesLocked();
            int numDevices = this.mInputDevices.size();
            for (int i = 0; i < numDevices; i++) {
                inputDevice = (InputDevice) this.mInputDevices.valueAt(i);
                if (inputDevice == null) {
                    try {
                        inputDevice = this.mIm.getInputDevice(this.mInputDevices.keyAt(i));
                    } catch (RemoteException e) {
                    }
                    if (inputDevice == null) {
                        continue;
                    } else {
                        this.mInputDevices.setValueAt(i, inputDevice);
                    }
                }
                if (descriptor.equals(inputDevice.getDescriptor())) {
                    break;
                }
            }
            inputDevice = null;
        }
        return inputDevice;
    }

    public int[] getInputDeviceIds() {
        int[] ids;
        synchronized (this.mInputDevicesLock) {
            populateInputDevicesLocked();
            int count = this.mInputDevices.size();
            ids = new int[count];
            for (int i = 0; i < count; i++) {
                ids[i] = this.mInputDevices.keyAt(i);
            }
        }
        return ids;
    }

    public void registerInputDeviceListener(InputDeviceListener listener, Handler handler) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mInputDevicesLock) {
            if (findInputDeviceListenerLocked(listener) < 0) {
                this.mInputDeviceListeners.add(new InputDeviceListenerDelegate(listener, handler));
            }
        }
    }

    public void unregisterInputDeviceListener(InputDeviceListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mInputDevicesLock) {
            int index = findInputDeviceListenerLocked(listener);
            if (index >= 0) {
                ((InputDeviceListenerDelegate) this.mInputDeviceListeners.get(index)).removeCallbacksAndMessages(null);
                this.mInputDeviceListeners.remove(index);
            }
        }
    }

    private int findInputDeviceListenerLocked(InputDeviceListener listener) {
        int numListeners = this.mInputDeviceListeners.size();
        for (int i = 0; i < numListeners; i++) {
            if (((InputDeviceListenerDelegate) this.mInputDeviceListeners.get(i)).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    public int isInTabletMode() {
        try {
            return this.mIm.isInTabletMode();
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not get tablet mode state", ex);
            return -1;
        }
    }

    public void registerOnTabletModeChangedListener(OnTabletModeChangedListener listener, Handler handler) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mTabletModeLock) {
            if (this.mOnTabletModeChangedListeners == null) {
                initializeTabletModeListenerLocked();
            }
            if (findOnTabletModeChangedListenerLocked(listener) < 0) {
                this.mOnTabletModeChangedListeners.add(new OnTabletModeChangedListenerDelegate(listener, handler));
            }
        }
    }

    public void unregisterOnTabletModeChangedListener(OnTabletModeChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mTabletModeLock) {
            int idx = findOnTabletModeChangedListenerLocked(listener);
            if (idx >= 0) {
                ((OnTabletModeChangedListenerDelegate) this.mOnTabletModeChangedListeners.remove(idx)).removeCallbacksAndMessages(null);
            }
        }
    }

    private void initializeTabletModeListenerLocked() {
        TabletModeChangedListener listener = new TabletModeChangedListener();
        try {
            this.mIm.registerTabletModeChangedListener(listener);
            this.mTabletModeChangedListener = listener;
            this.mOnTabletModeChangedListeners = new ArrayList();
        } catch (RemoteException ex) {
            throw new RuntimeException("Could not register tablet mode changed listener", ex);
        }
    }

    private int findOnTabletModeChangedListenerLocked(OnTabletModeChangedListener listener) {
        int N = this.mOnTabletModeChangedListeners.size();
        for (int i = 0; i < N; i++) {
            if (((OnTabletModeChangedListenerDelegate) this.mOnTabletModeChangedListeners.get(i)).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    public KeyboardLayout[] getKeyboardLayouts() {
        try {
            return this.mIm.getKeyboardLayouts();
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not get list of keyboard layout informations.", ex);
            return new KeyboardLayout[0];
        }
    }

    public KeyboardLayout getKeyboardLayout(String keyboardLayoutDescriptor) {
        if (keyboardLayoutDescriptor == null) {
            throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        }
        try {
            return this.mIm.getKeyboardLayout(keyboardLayoutDescriptor);
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not get keyboard layout information.", ex);
            return null;
        }
    }

    public String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier) {
        try {
            return this.mIm.getCurrentKeyboardLayoutForInputDevice(identifier);
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not get current keyboard layout for input device.", ex);
            return null;
        }
    }

    public void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier must not be null");
        } else if (keyboardLayoutDescriptor == null) {
            throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        } else {
            try {
                this.mIm.setCurrentKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
            } catch (RemoteException ex) {
                Log.w(TAG, "Could not set current keyboard layout for input device.", ex);
            }
        }
    }

    public String[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("inputDeviceDescriptor must not be null");
        }
        try {
            return this.mIm.getKeyboardLayoutsForInputDevice(identifier);
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not get keyboard layouts for input device.", ex);
            return (String[]) ArrayUtils.emptyArray(String.class);
        }
    }

    public void addKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) {
        if (identifier == null) {
            throw new IllegalArgumentException("inputDeviceDescriptor must not be null");
        } else if (keyboardLayoutDescriptor == null) {
            throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        } else {
            try {
                this.mIm.addKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
            } catch (RemoteException ex) {
                Log.w(TAG, "Could not add keyboard layout for input device.", ex);
            }
        }
    }

    public void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) {
        if (identifier == null) {
            throw new IllegalArgumentException("inputDeviceDescriptor must not be null");
        } else if (keyboardLayoutDescriptor == null) {
            throw new IllegalArgumentException("keyboardLayoutDescriptor must not be null");
        } else {
            try {
                this.mIm.removeKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
            } catch (RemoteException ex) {
                Log.w(TAG, "Could not remove keyboard layout for input device.", ex);
            }
        }
    }

    public TouchCalibration getTouchCalibration(String inputDeviceDescriptor, int surfaceRotation) {
        try {
            return this.mIm.getTouchCalibrationForInputDevice(inputDeviceDescriptor, surfaceRotation);
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not get calibration matrix for input device.", ex);
            return TouchCalibration.IDENTITY;
        }
    }

    public void setTouchCalibration(String inputDeviceDescriptor, int surfaceRotation, TouchCalibration calibration) {
        try {
            this.mIm.setTouchCalibrationForInputDevice(inputDeviceDescriptor, surfaceRotation, calibration);
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not set calibration matrix for input device.", ex);
        }
    }

    public int getPointerSpeed(Context context) {
        int speed = 0;
        try {
            speed = System.getInt(context.getContentResolver(), "pointer_speed");
        } catch (SettingNotFoundException e) {
        }
        return speed;
    }

    public void setPointerSpeed(Context context, int speed) {
        if (speed < -7 || speed > 7) {
            throw new IllegalArgumentException("speed out of range");
        }
        System.putInt(context.getContentResolver(), "pointer_speed", speed);
    }

    public void tryPointerSpeed(int speed) {
        if (speed < -7 || speed > 7) {
            throw new IllegalArgumentException("speed out of range");
        }
        try {
            this.mIm.tryPointerSpeed(speed);
        } catch (RemoteException ex) {
            Log.w(TAG, "Could not set temporary pointer speed.", ex);
        }
    }

    public boolean[] deviceHasKeys(int[] keyCodes) {
        return deviceHasKeys(-1, keyCodes);
    }

    public boolean[] deviceHasKeys(int id, int[] keyCodes) {
        boolean[] ret = new boolean[keyCodes.length];
        try {
            this.mIm.hasKeys(id, Color.YELLOW, keyCodes, ret);
        } catch (RemoteException e) {
        }
        return ret;
    }

    public boolean injectInputEvent(InputEvent event, int mode) {
        if (event == null) {
            throw new IllegalArgumentException("event must not be null");
        } else if (mode == 0 || mode == 2 || mode == 1) {
            try {
                return this.mIm.injectInputEvent(event, mode);
            } catch (RemoteException e) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("mode is invalid");
        }
    }

    public void setStartedShutdown(boolean isStarted) {
        try {
            this.mIm.setStartedShutdown(isStarted);
        } catch (RemoteException e) {
        }
    }

    public void setFlipCoverTouchEnabled(boolean enabled) {
        try {
            this.mIm.setFlipCoverTouchEnabled(enabled);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not set setFlipCovertouchEnabled()");
        }
    }

    public void coverEventFinished() {
        try {
            this.mIm.coverEventFinished();
        } catch (RemoteException e) {
            Log.w(TAG, "Could not call coverEventFinished()");
        }
    }

    public int setLedState(boolean on) {
        try {
            return this.mIm.setLedState(on);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int getScanCodeState(int deviceId, int sourceMask, int scanCode) {
        try {
            return this.mIm.getScanCodeState(deviceId, sourceMask, scanCode);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public InputChannel monitorInput(String inputChannelName) {
        try {
            return this.mIm.monitorInput(inputChannelName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setCoverVerify(int verify) {
        try {
            this.mIm.setCoverVerify(verify);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not call setCoverVerify()");
        }
    }

    public int getInboundQueueLength() {
        try {
            return this.mIm.getInboundQueueLength();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int addOrRemoveVirtualGamePadDevice(boolean add) {
        try {
            return this.mIm.addOrRemoveVirtualGamePadDevice(add);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not call addOrRemoveVirtualGamePadDevice()");
            return -1;
        }
    }

    public boolean setEnableTSP(TSP_CMDTYPE cmdtype, boolean enable) {
        try {
            return this.mIm.setEnableTSP(cmdtype.getvalue(), enable);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not call setEnableTSPforSpayGesture()");
            return false;
        }
    }

    private void populateInputDevicesLocked() {
        if (this.mInputDevicesChangedListener == null) {
            InputDevicesChangedListener listener = new InputDevicesChangedListener();
            try {
                this.mIm.registerInputDevicesChangedListener(listener);
                this.mInputDevicesChangedListener = listener;
            } catch (RemoteException ex) {
                throw new RuntimeException("Could not get register input device changed listener", ex);
            }
        }
        if (this.mInputDevices == null) {
            try {
                int[] ids = this.mIm.getInputDeviceIds();
                this.mInputDevices = new SparseArray();
                for (int put : ids) {
                    this.mInputDevices.put(put, null);
                }
            } catch (RemoteException ex2) {
                throw new RuntimeException("Could not get input device ids.", ex2);
            }
        }
    }

    private void onInputDevicesChanged(int[] deviceIdAndGeneration) {
        synchronized (this.mInputDevicesLock) {
            int i = this.mInputDevices.size();
            while (true) {
                i--;
                if (i <= 0) {
                    break;
                }
                int deviceId = this.mInputDevices.keyAt(i);
                if (!containsDeviceId(deviceIdAndGeneration, deviceId)) {
                    this.mInputDevices.removeAt(i);
                    sendMessageToInputDeviceListenersLocked(2, deviceId);
                }
            }
            i = 0;
            while (i < deviceIdAndGeneration.length) {
                deviceId = deviceIdAndGeneration[i];
                int index = this.mInputDevices.indexOfKey(deviceId);
                if (index >= 0) {
                    InputDevice device = (InputDevice) this.mInputDevices.valueAt(index);
                    if (!(device == null || device.getGeneration() == deviceIdAndGeneration[i + 1])) {
                        this.mInputDevices.setValueAt(index, null);
                        sendMessageToInputDeviceListenersLocked(3, deviceId);
                    }
                } else {
                    this.mInputDevices.put(deviceId, null);
                    sendMessageToInputDeviceListenersLocked(1, deviceId);
                }
                i += 2;
            }
        }
    }

    private void sendMessageToInputDeviceListenersLocked(int what, int deviceId) {
        int numListeners = this.mInputDeviceListeners.size();
        for (int i = 0; i < numListeners; i++) {
            InputDeviceListenerDelegate listener = (InputDeviceListenerDelegate) this.mInputDeviceListeners.get(i);
            listener.sendMessage(listener.obtainMessage(what, deviceId, 0));
        }
    }

    private static boolean containsDeviceId(int[] deviceIdAndGeneration, int deviceId) {
        for (int i = 0; i < deviceIdAndGeneration.length; i += 2) {
            if (deviceIdAndGeneration[i] == deviceId) {
                return true;
            }
        }
        return false;
    }

    private void onTabletModeChanged(long whenNanos, boolean inTabletMode) {
        synchronized (this.mTabletModeLock) {
            int N = this.mOnTabletModeChangedListeners.size();
            for (int i = 0; i < N; i++) {
                ((OnTabletModeChangedListenerDelegate) this.mOnTabletModeChangedListeners.get(i)).sendTabletModeChanged(whenNanos, inTabletMode);
            }
        }
    }

    public Vibrator getInputDeviceVibrator(int deviceId) {
        return new InputDeviceVibrator(deviceId);
    }

    public void enablePatialScreen(boolean enable) {
        try {
            this.mIm.enablePatialScreen(enable);
        } catch (RemoteException e) {
        }
    }

    public int getSmartHallFlipState() {
        try {
            return this.mIm.getSmartHallFlipState();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setWakeKeyDynamically(String packagename, boolean isPut, String keycodes) {
        try {
            this.mIm.setWakeKeyDynamically(packagename, isPut, keycodes);
        } catch (RemoteException e) {
        }
    }

    public void setMonitorChannelFilter(InputChannel inputChannel, int monitorFilter) {
        try {
            this.mIm.setMonitorChannelFilter(inputChannel, monitorFilter);
        } catch (RemoteException e) {
        }
    }
}
