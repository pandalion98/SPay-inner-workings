package android.os;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.IVibratorService.Stub;
import android.os.Vibrator.MagnitudeTypes;
import android.util.Log;

public class SystemVibrator extends Vibrator {
    private static final String TAG = "Vibrator";
    private static final boolean TRACE_DEBUG = false;
    private static final boolean mIsAmericano = SystemProperties.get("ro.build.scafe").equals("americano");
    private final IVibratorService mService = Stub.asInterface(ServiceManager.getService(Context.VIBRATOR_SERVICE));
    private final Binder mToken = new Binder();

    public enum MagnitudeType {
        TouchMagnitude,
        NotificationMagnitude,
        CallMagnitude,
        MaxMagnitude
    }

    public SystemVibrator(Context context) {
        super(context);
    }

    public boolean hasVibrator() {
        boolean z = false;
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
        } else {
            try {
                z = this.mService.hasVibrator();
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean isEnableIntensity() {
        boolean z = false;
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
        } else {
            try {
                z = this.mService.isEnableIntensity();
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public void vibrate(int uid, String opPkg, long milliseconds, AudioAttributes attributes) {
        Log.v(TAG, "Called vibrate(int, String, long, AudioAttributes) API - PUID: " + uid + ", PackageName: " + opPkg);
        vibrate(uid, opPkg, milliseconds, attributes, -1);
    }

    public void vibrate(long milliseconds, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(long, MagnitudeTypes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", MagnitudeTypes: " + magnitudeType.toString());
            vibrate(Process.myUid(), this.mPackageName, milliseconds, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(long milliseconds, MagnitudeType magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(long, MagnitudeType) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", MagnitudeType: " + magnitudeType.toString());
            vibrate(Process.myUid(), this.mPackageName, milliseconds, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(int uid, String opPkg, long milliseconds, AudioAttributes attributes, int magnitude) {
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
            return;
        }
        try {
            Log.v(TAG, "vibrate - PUID: " + uid + ", PackageName: " + opPkg + ", ms: " + milliseconds + ", AudioAttr: " + attributes + ", mag: " + magnitude);
            this.mService.vibrateMagnitude(uid, opPkg, milliseconds, usageForAttributes(attributes), this.mToken, magnitude);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(int uid, String opPkg, long[] pattern, int repeat, AudioAttributes attributes) {
        Log.v(TAG, "Called vibrate(int, String, long[], int, AudioAttributes) API - PUID: " + uid + ", PackageName: " + opPkg + ", pattern");
        vibrate(uid, opPkg, pattern, repeat, attributes, -1);
    }

    public void vibrate(long[] pattern, int repeat, int magnitude) {
        Log.v(TAG, "Called vibrate(long[], int, int) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", pattern, mag: " + magnitude);
        vibrate(Process.myUid(), this.mPackageName, pattern, repeat, null, magnitude);
    }

    public void vibrate(long[] pattern, int repeat, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(long[], int, MagnitudeTypes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", pattern, MagnitudeTypes: " + magnitudeType.toString());
            vibrate(Process.myUid(), this.mPackageName, pattern, repeat, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(long milliseconds, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(long, AudioAttributes, MagnitudeTypes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", AudioAttributes: " + attributes + ", MagnitudeTypes: " + magnitudeType.toString());
            vibrate(Process.myUid(), this.mPackageName, milliseconds, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(long[] pattern, int repeat, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(long[], int, AudioAttributes, MagnitudeTypes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", AudioAttributes: " + attributes + ", pattern, MagnitudeTypes: " + magnitudeType.toString());
            vibrate(Process.myUid(), this.mPackageName, pattern, repeat, attributes, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(int type, AudioAttributes attributes, int magnitude) {
        try {
            Log.v(TAG, "vibrate - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", type: " + type + ", mag: " + magnitude + ", AudioAttr : " + attributes);
            this.mService.vibrateCommonPatternMagnitude(Process.myUid(), this.mPackageName, type, -1, this.mToken, magnitude, usageForAttributes(attributes));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(int type, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(int, AudioAttributes, MagnitudeTypes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", MagnitudeType: " + magnitudeType.toString());
            vibrate(type, attributes, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(int type, int repeat, AudioAttributes attributes, int magnitude) {
        try {
            Log.v(TAG, "vibrate - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", type: " + type + ", repeat: " + repeat + ", mag: " + magnitude + ", AudioAttr : " + attributes);
            this.mService.vibrateCommonPatternMagnitude(Process.myUid(), this.mPackageName, type, repeat, this.mToken, magnitude, usageForAttributes(attributes));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(int type, int repeat, AudioAttributes attributes, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(int, int, AudioAttributes, MagnitudeTypes) API - PUID: " + Process.myUid() + ", repeat: " + repeat + ", PackageName: " + this.mPackageName + ", MagnitudeType: " + magnitudeType.toString());
            vibrate(type, repeat, attributes, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(long[] pattern, int repeat, MagnitudeType magnitudeType) {
        try {
            Log.v(TAG, "Called vibrate(long[], int, MagnitudeType) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", pattern, MagnitudeType: " + magnitudeType.toString());
            vibrate(Process.myUid(), this.mPackageName, pattern, repeat, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrate(int uid, String opPkg, long[] pattern, int repeat, AudioAttributes attributes, int magnitude) {
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
        } else if (repeat < pattern.length) {
            try {
                Log.v(TAG, "vibrate - PUID: " + uid + ", PackageName: " + opPkg + ", pattern" + ", repeat: " + repeat + ", AudioAttr: " + attributes + ", mag: " + magnitude);
                this.mService.vibratePatternMagnitude(uid, opPkg, pattern, repeat, usageForAttributes(attributes), this.mToken, magnitude);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to vibrate.", e);
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private static int usageForAttributes(AudioAttributes attributes) {
        return attributes != null ? attributes.getUsage() : 0;
    }

    public void cancel() {
        if (this.mService != null) {
            try {
                Log.v(TAG, "Called cancel() API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName);
                this.mService.cancelVibrate(this.mToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to cancel vibration.", e);
            }
        }
    }

    public int getMaxMagnitude() {
        int i = 0;
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
        } else {
            try {
                i = this.mService.getMaxMagnitude();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to get the maximum magnitude.", e);
            }
        }
        return i;
    }

    public void setMagnitude(int magnitude) {
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
            return;
        }
        try {
            this.mService.setMagnitude(magnitude);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to set the magnitude.", e);
        }
    }

    public void resetMagnitude() {
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
            return;
        }
        try {
            this.mService.resetMagnitude();
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to reset the magnitude.", e);
        }
    }

    public void vibrateImmVibe(int type) {
        if (mIsAmericano) {
            Log.v(TAG, "Called legacy(int) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName);
            vibrate(50025, -1, null, MagnitudeTypes.TouchMagnitude);
            return;
        }
        Log.d(TAG, "You can only use this API in maintain release project.");
    }

    public void vibrateImmVibe(int type, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called legacy(int, MagnitudeTypes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", MagnitudeType: " + magnitudeType.toString());
            vibrate(50025, -1, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrateImmVibe(int type, MagnitudeType magnitudeType) {
        try {
            Log.v(TAG, "Called legacy(int, MagnitudeType) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", MagnitudeType: " + magnitudeType.toString());
            vibrate(50025, -1, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrateImmVibe(int type, int magnitude) {
        Log.v(TAG, "legacy (int, int) - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", type: " + type + ", mag: " + magnitude);
        vibrate(50025, -1, null, magnitude);
    }

    public void vibrateImmVibe(int type, int magnitude, AudioAttributes attributes) {
        Log.v(TAG, "legacy (int, int, AudioAttributes) - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", type: " + type + ", mag: " + magnitude + ", AudioAttr : " + attributes);
        vibrate(50025, -1, attributes, magnitude);
    }

    public void vibrateImmVibe(byte[] pattern) {
        if (mIsAmericano) {
            Log.v(TAG, "Called legacy([]) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName);
            vibrate(50025, -1, null, MagnitudeTypes.TouchMagnitude);
            return;
        }
        Log.d(TAG, "You can only use this API in maintain release project.");
    }

    public void vibrateImmVibe(byte[] pattern, MagnitudeTypes magnitudeType) {
        try {
            Log.v(TAG, "Called legacy([], MagnitudeTypes) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", MagnitudeType: " + magnitudeType.toString());
            vibrate(50025, -1, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrateImmVibe(byte[] pattern, MagnitudeType magnitudeType) {
        try {
            Log.v(TAG, "Called legacy([], MagnitudeType) API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName);
            vibrate(50025, -1, null, this.mService.getMagnitude(magnitudeType.toString()));
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    public void vibrateImmVibe(byte[] pattern, int magnitude) {
        Log.v(TAG, "legacy ([], int) - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", pattern, mag: " + magnitude);
        vibrate(50025, -1, null, magnitude);
    }

    public void vibrateImmVibe(byte[] pattern, int magnitude, AudioAttributes attributes) {
        Log.v(TAG, "legacy ([], int, AudioAttributes) - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", pattern, mag: " + magnitude + ", AudioAttr : " + attributes);
        vibrate(50025, -1, attributes, magnitude);
    }

    public void vibrateCall(int type) {
        if (mIsAmericano) {
            Log.v(TAG, "Called vibrateCall() API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", type: " + type);
            vibrate(50025, -1, null, MagnitudeTypes.CallMagnitude);
            return;
        }
        Log.d(TAG, "You can only use this API in maintain release project.");
    }

    public void vibrateNotification(int type) {
        if (mIsAmericano) {
            Log.v(TAG, "Called vibrateNotification() API - PUID: " + Process.myUid() + ", PackageName: " + this.mPackageName + ", type: " + type);
            vibrate(50025, -1, null, MagnitudeTypes.NotificationMagnitude);
            return;
        }
        Log.d(TAG, "You can only use this API in maintain release project.");
    }

    public void setVibratorFrequency(long offset, int value) {
        boolean isInserted = false;
        if (!FactoryTest.isFactoryBinary()) {
            Log.e(TAG, "It's not Factory Binary");
        }
        try {
            isInserted = this.mService.writeToFile(offset, value);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to vibrate.", e);
        }
        if (isInserted) {
            Log.v(TAG, "success to set freqeuncy value as " + value);
        } else {
            Log.e(TAG, "fail to set freqeuncy value");
        }
    }

    public void setVibratorFrequency(int value) {
        setVibratorFrequency(7340596, value);
    }

    public int getVibratorFrequency(long offset) {
        int value = -1;
        if (!FactoryTest.isFactoryBinary()) {
            Log.e(TAG, "It's not Factory Binary");
        }
        try {
            value = this.mService.readFromFile(offset);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to vibrate.", e);
        }
        if (value != -1) {
            Log.v(TAG, "success to get freqeuncy value as " + value);
        } else {
            Log.e(TAG, "fail to get freqeuncy value");
        }
        return value;
    }

    public int getVibratorFrequency() {
        return getVibratorFrequency(7340596);
    }
}
