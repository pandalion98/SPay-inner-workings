package com.samsung.android.mdnie;

import android.os.RemoteException;
import android.util.Slog;

public final class MdnieManager {
    public static final int MDNIE_CONTENT_MODE_BROWSER = 8;
    public static final int MDNIE_CONTENT_MODE_CAMERA = 4;
    public static final int MDNIE_CONTENT_MODE_EBOOK = 9;
    public static final int MDNIE_CONTENT_MODE_EMAIL = 10;
    public static final int MDNIE_CONTENT_MODE_GALLERY = 6;
    public static final int MDNIE_CONTENT_MODE_HMT16 = 12;
    public static final int MDNIE_CONTENT_MODE_HMT8 = 11;
    public static final int MDNIE_CONTENT_MODE_NAVI = 5;
    public static final int MDNIE_CONTENT_MODE_UI = 0;
    public static final int MDNIE_CONTENT_MODE_VIDEO = 1;
    public static final int MDNIE_CONTENT_MODE_VT = 7;
    public static final int MDNIE_SCREEN_ADAPTIVE_MODE = 4;
    public static final int MDNIE_SCREEN_ADOBERGB_MODE = 2;
    public static final int MDNIE_SCREEN_AMOLED_MODE = 1;
    public static final int MDNIE_SCREEN_AMOLED_S_CURVE_MODE = 0;
    public static final int MDNIE_SCREEN_SRGB_MODE = 3;
    public static final int MDNIE_VISION_COLOR_BLIND = 3;
    public static final int MDNIE_VISION_GREY_SCALE = 4;
    public static final int MDNIE_VISION_LOCAL_CE = 5;
    public static final int MDNIE_VISION_LOCAL_CE_TEXT = 6;
    public static final int MDNIE_VISION_NEGATIVE = 1;
    public static final int MDNIE_VISION_NORMAL = 0;
    public static final int MDNIE_VISION_SCREEN_CURTAIN = 2;
    private static int RETURN_ERROR = -1;
    private static final String TAG = "MdnieManager";
    final IMdnieManager mService;

    public MdnieManager(IMdnieManager service) {
        if (service == null) {
            Slog.i(TAG, "In Constructor Stub-Service(IMdnieManager) is null");
        }
        this.mService = service;
    }

    public int getScreenMode() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getScreenMode();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public int getContentMode() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getContentMode();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public boolean setScreenMode(int mode) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setScreenMode(mode);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean setContentMode(int mode) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setContentMode(mode);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean setAmoledACL(int mode) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setAmoledACL(mode);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean setmDNIeColorBlind(boolean enable, int[] result) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setmDNIeColorBlind(enable, result);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean setmDNIeNegative(boolean enable) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setmDNIeNegative(enable);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean setmDNIeScreenCurtain(boolean enable) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setmDNIeScreenCurtain(enable);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean setmDNIeEmergencyMode(boolean enable) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setmDNIeEmergencyMode(enable);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean setmDNIeAccessibilityMode(int mode, boolean enable) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.setmDNIeAccessibilityMode(mode, enable);
            } catch (RemoteException e) {
            }
        }
        return z;
    }
}
