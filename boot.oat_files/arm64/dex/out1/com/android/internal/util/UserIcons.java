package com.android.internal.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.Slog;
import android.util.Xml;
import android.util.XmlMoreAtomicFile;
import com.android.internal.R;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class UserIcons {
    private static final String ATTR_VERSION = "version";
    private static final String LOG_USERICON_TAG = "UserIcon";
    private static final String TAG_FOTA = "fota";
    private static final int[] USER_DRAWABLES = new int[]{R.drawable.avatar_default_1, R.drawable.avatar_default_2, R.drawable.avatar_default_3, R.drawable.avatar_default_4, R.drawable.avatar_default_5};
    private static final int[] USER_ICON_COLORS = new int[]{R.color.user_icon_1, R.color.user_icon_2, R.color.user_icon_3};
    static String mFotaVersion = null;

    public static Bitmap convertToBitmap(Drawable icon) {
        if (icon == null) {
            return null;
        }
        int width = icon.getIntrinsicWidth();
        int height = icon.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.setBounds(0, 0, width, height);
        icon.draw(canvas);
        return bitmap;
    }

    public static Drawable getDefaultUserIconForBMode(int userId, boolean light, boolean isPrimary) {
        int colorResId;
        Drawable icon;
        Slog.d(LOG_USERICON_TAG, "getDefaultUserIconForBMode , userId: " + userId + " isPrimary : " + isPrimary);
        if (light) {
        }
        if (userId != DeviceRootKeyServiceManager.ERR_SERVICE_ERROR) {
            colorResId = USER_ICON_COLORS[userId % USER_ICON_COLORS.length];
        } else {
            colorResId = R.color.user_icon_4;
        }
        if (isPrimary) {
            icon = Resources.getSystem().getDrawable(R.drawable.mum_default_p);
        } else {
            icon = Resources.getSystem().getDrawable(R.drawable.mum_default_b);
        }
        icon.setColorFilter(Resources.getSystem().getColor(colorResId), Mode.SCREEN);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        return icon;
    }

    public static Drawable getDefaultUserIcon(int userId, boolean light) {
        if (mFotaVersion == null) {
            mFotaVersion = readFotaLocked();
        }
        if (mFotaVersion == null) {
            mFotaVersion = "LL";
        }
        Slog.d(LOG_USERICON_TAG, "getDefaultUserIcon , userId: " + userId + " mFotaVersion : " + mFotaVersion);
        Drawable icon;
        if (!mFotaVersion.equals("KK")) {
            int colorResId;
            if (light) {
            }
            if (userId != DeviceRootKeyServiceManager.ERR_SERVICE_ERROR) {
                colorResId = USER_ICON_COLORS[userId % USER_ICON_COLORS.length];
            } else {
                colorResId = R.color.user_icon_4;
            }
            icon = Resources.getSystem().getDrawable(R.drawable.mum_default, null);
            icon.setColorFilter(Resources.getSystem().getColor(colorResId, null), Mode.SCREEN);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            return icon;
        } else if (userId != DeviceRootKeyServiceManager.ERR_SERVICE_ERROR) {
            icon = Resources.getSystem().getDrawable(USER_DRAWABLES[userId % USER_DRAWABLES.length]);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            return icon;
        } else {
            icon = Resources.getSystem().getDrawable(R.drawable.avatar_default_5);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            return icon;
        }
    }

    private static String readFotaLocked() {
        FileInputStream fis = null;
        XmlMoreAtomicFile fotaFile = new XmlMoreAtomicFile(new File("/data/system/users", "fota.xml"));
        String mFotaVersion = null;
        try {
            int type;
            fis = fotaFile.openRead();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, null);
            do {
                type = parser.next();
                if (type == 2) {
                    break;
                }
            } while (type != 1);
            if (type != 2) {
                fotaFile.processDamagedFile();
                String str = "LL";
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
                return null;
            }
            if (parser.getName().equals("fota")) {
                mFotaVersion = parser.getAttributeValue(null, ATTR_VERSION);
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e2) {
                }
            }
            return mFotaVersion;
        } catch (IOException ioe) {
            fotaFile.processDamagedFile();
            ioe.printStackTrace();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e3) {
                }
            }
            return null;
        } catch (XmlPullParserException pe) {
            fotaFile.processDamagedFile();
            pe.printStackTrace();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e4) {
                }
            }
            return null;
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e5) {
                }
            }
            return null;
        }
    }
}
