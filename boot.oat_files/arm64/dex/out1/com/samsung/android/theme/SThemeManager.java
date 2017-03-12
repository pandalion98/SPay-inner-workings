package com.samsung.android.theme;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.provider.Settings$System;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.HashMap;
import java.util.Map.Entry;

public class SThemeManager {
    public static final String ACTION_FESTIVAL_EFFECT_CHANGED = "android.intent.action.FESTIVAL_EFFECT_CHANGED";
    public static final String ACTION_THEME_CHANGED = "android.intent.action.STHEME_CHANGED";
    private static final String CSC_FILE_THEME_APP_LIST = "/system/csc/theme_app_list.xml";
    public static final String CURRENT_FESTIVAL_EFFECT_PACKAGE = "current_festival_effect_package";
    public static final String CURRENT_FESTIVAL_WALLPAPER_CLASS = "current_festival_wallpaper_class";
    public static final String CURRENT_FESTIVAL_WALLPAPER_PACKAGE = "current_festival_wallpaper_package";
    public static final String CURRENT_THEME_PACKAGE = "current_sec_theme_package";
    private static final boolean DBG;
    private static final String FESTIVAL_EFFECT_STR = "festival";
    private static final String TAG = "SThemeManager";
    private static final String TAG_APP_LIST = "ThemeAppList";
    private static final String TAG_ATTR_CLASSNAME = "className";
    private static final String TAG_ATTR_ICONID = "iconId";
    private static final String TAG_THEME_APP = "ThemeApp";
    private static final String THEME_STR = "theme";
    public static final int TYPE_FESTIVAL_EFFECT = 1;
    public static final int TYPE_THEME = 0;
    private static HashMap<String, String> sPackageIconMap = new HashMap();
    private Context mContext;
    private boolean mPackageIconLoaded;
    private String mPackageName;
    private int mType;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public SThemeManager(Context context) {
        this(context, 0);
    }

    public SThemeManager(Context context, int type) {
        this.mPackageIconLoaded = false;
        this.mContext = context;
        this.mType = type;
        resetTheme();
    }

    public String getVersionFromFeature(int type) {
        String v = SmartFaceManager.PAGE_MIDDLE;
        String typeString = "";
        String feature = FloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_COMMON_CONFIG_CHANGEABLE_UI", SmartFaceManager.PAGE_MIDDLE);
        if (type == 0) {
            typeString = "theme";
        } else if (type == 1) {
            typeString = FESTIVAL_EFFECT_STR;
        }
        if (feature == null || feature.isEmpty() || typeString.isEmpty() || !feature.contains(typeString)) {
            return v;
        }
        int index = feature.indexOf(typeString);
        if (index <= -1 || (typeString.length() + index) + 1 >= feature.length()) {
            return v;
        }
        return String.valueOf(feature.charAt((typeString.length() + index) + 1));
    }

    public void resetTheme() {
        getCurrentResourcePackage();
        if (this.mPackageName == null || this.mPackageName.isEmpty()) {
            this.mPackageName = this.mContext.getPackageName();
        }
    }

    public CharSequence getItemText(String textId) {
        CharSequence text = null;
        Resources r = getResources();
        if (r != null) {
            try {
                int resId = r.getIdentifier(textId, "string", this.mPackageName);
                if (resId == 0) {
                    return null;
                }
                text = r.getText(resId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return text;
    }

    public Drawable getItemDrawable(String drawableId) {
        Drawable drawable = null;
        Resources r = getResources();
        if (r != null) {
            try {
                int resId = r.getIdentifier(drawableId, "drawable", this.mPackageName);
                if (resId == 0) {
                    return null;
                }
                drawable = r.getDrawable(resId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return drawable;
    }

    public Bitmap getItemBitmap(String bitmapId) {
        Bitmap bitmap = null;
        Resources r = getResources();
        if (r != null) {
            try {
                int resId = r.getIdentifier(bitmapId, "drawable", this.mPackageName);
                if (resId != 0) {
                    bitmap = BitmapFactory.decodeResource(r, resId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public int getItemColor(String colorId) {
        int i = 0;
        Resources r = getResources();
        if (r != null) {
            try {
                int resId = r.getIdentifier(colorId, SubscriptionManager.COLOR, this.mPackageName);
                if (resId != 0) {
                    i = r.getColor(resId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public Drawable getPackageIcon(String packageName) {
        if (!this.mPackageIconLoaded) {
            loadThemeAppList();
        }
        String iconId = (String) sPackageIconMap.get(packageName);
        if (DBG) {
            Log.d(TAG, "getPackageIcon " + packageName + ", iconId " + iconId);
        }
        if (iconId != null) {
            return getItemDrawable(iconId);
        }
        return null;
    }

    public Bitmap getPackageIconBitmap(String packageName) {
        if (!this.mPackageIconLoaded) {
            loadThemeAppList();
        }
        String iconId = (String) sPackageIconMap.get(packageName);
        if (DBG) {
            Log.d(TAG, "getPackageIconBitmap " + packageName + ", iconId " + iconId);
        }
        if (iconId != null) {
            return getItemBitmap(iconId);
        }
        return null;
    }

    public Bitmap getPackageIconBitmapStartsWith(String packageName) {
        if (!this.mPackageIconLoaded) {
            loadThemeAppList();
        }
        String iconId = null;
        for (Entry<String, String> entry : sPackageIconMap.entrySet()) {
            if (((String) entry.getKey()).startsWith(packageName)) {
                iconId = (String) entry.getValue();
                break;
            }
        }
        if (DBG) {
            Log.d(TAG, "getPackageIconBitmapStartsWith " + packageName + ", iconId " + iconId);
        }
        if (iconId != null) {
            return getItemBitmap(iconId);
        }
        return null;
    }

    public XmlResourceParser getItemXml(String xmlId) {
        XmlResourceParser xmlResourceParser = null;
        Resources r = getResources();
        if (r != null) {
            try {
                int resId = r.getIdentifier(xmlId, "xml", this.mPackageName);
                if (resId != 0) {
                    xmlResourceParser = r.getXml(resId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return xmlResourceParser;
    }

    public Context getCurrentContext() {
        Context c = null;
        if (this.mPackageName.isEmpty()) {
            return this.mContext;
        }
        try {
            return this.mContext.createPackageContext(this.mPackageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return c;
        }
    }

    private void getCurrentResourcePackage() {
        if (this.mType == 0) {
            this.mPackageName = Settings$System.getString(this.mContext.getContentResolver(), CURRENT_THEME_PACKAGE);
        } else if (this.mType == 1) {
            this.mPackageName = Settings$System.getString(this.mContext.getContentResolver(), CURRENT_FESTIVAL_EFFECT_PACKAGE);
        }
    }

    private Resources getResources() {
        Resources r = null;
        if (this.mPackageName.isEmpty()) {
            return this.mContext.getResources();
        }
        try {
            return this.mContext.getPackageManager().getResourcesForApplication(this.mPackageName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return r;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadThemeAppList() {
        /*
        r22 = this;
        r14 = 0;
        r10 = 0;
        r6 = 0;
        r17 = "SThemeManager";
        r18 = "Theme app list path: /system/csc/theme_app_list.xml";
        android.util.Log.d(r17, r18);
        r5 = new java.io.File;	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r17 = "/system/csc/theme_app_list.xml";
        r0 = r17;
        r5.<init>(r0);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r17 = r5.exists();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        if (r17 == 0) goto L_0x004b;
    L_0x0019:
        r18 = r5.length();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r20 = 0;
        r17 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
        if (r17 <= 0) goto L_0x004b;
    L_0x0023:
        r7 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x008a }
        r17 = new java.io.InputStreamReader;	 Catch:{ FileNotFoundException -> 0x008a }
        r18 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x008a }
        r19 = "/system/csc/theme_app_list.xml";
        r18.<init>(r19);	 Catch:{ FileNotFoundException -> 0x008a }
        r17.<init>(r18);	 Catch:{ FileNotFoundException -> 0x008a }
        r0 = r17;
        r7.<init>(r0);	 Catch:{ FileNotFoundException -> 0x008a }
        r6 = r7;
    L_0x0037:
        if (r6 == 0) goto L_0x004b;
    L_0x0039:
        r10 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ XmlPullParserException -> 0x00b9, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r17 = 1;
        r0 = r17;
        r10.setNamespaceAware(r0);	 Catch:{ XmlPullParserException -> 0x00b9, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r14 = r10.newPullParser();	 Catch:{ XmlPullParserException -> 0x00b9, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r14.setInput(r6);	 Catch:{ XmlPullParserException -> 0x00b9, IOException -> 0x0145, NotFoundException -> 0x0170 }
    L_0x004b:
        if (r14 == 0) goto L_0x0084;
    L_0x004d:
        r17 = "ThemeAppList";
        r0 = r17;
        com.android.internal.util.XmlUtils.beginDocument(r14, r0);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r8 = r14.getDepth();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
    L_0x0058:
        r16 = r14.next();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r17 = 3;
        r0 = r16;
        r1 = r17;
        if (r0 != r1) goto L_0x006c;
    L_0x0064:
        r17 = r14.getDepth();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r0 = r17;
        if (r0 <= r8) goto L_0x007c;
    L_0x006c:
        r17 = 1;
        r0 = r16;
        r1 = r17;
        if (r0 == r1) goto L_0x007c;
    L_0x0074:
        r17 = 1;
        r0 = r16;
        r1 = r17;
        if (r0 != r1) goto L_0x00bf;
    L_0x007c:
        r17 = 1;
        r0 = r17;
        r1 = r22;
        r1.mPackageIconLoaded = r0;	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
    L_0x0084:
        if (r6 == 0) goto L_0x0089;
    L_0x0086:
        r6.close();	 Catch:{ Exception -> 0x013f }
    L_0x0089:
        return;
    L_0x008a:
        r9 = move-exception;
        r6 = 0;
        r9.printStackTrace();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        goto L_0x0037;
    L_0x0090:
        r9 = move-exception;
        r17 = "SThemeManager";
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x019b }
        r18.<init>();	 Catch:{ all -> 0x019b }
        r19 = "Exception during parsing theme app list";
        r18 = r18.append(r19);	 Catch:{ all -> 0x019b }
        r0 = r18;
        r18 = r0.append(r9);	 Catch:{ all -> 0x019b }
        r18 = r18.toString();	 Catch:{ all -> 0x019b }
        android.util.Log.e(r17, r18);	 Catch:{ all -> 0x019b }
        r9.printStackTrace();	 Catch:{ all -> 0x019b }
        if (r6 == 0) goto L_0x0089;
    L_0x00b0:
        r6.close();	 Catch:{ Exception -> 0x00b4 }
        goto L_0x0089;
    L_0x00b4:
        r9 = move-exception;
        r9.printStackTrace();
        goto L_0x0089;
    L_0x00b9:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r14 = 0;
        goto L_0x004b;
    L_0x00bf:
        r17 = 2;
        r0 = r16;
        r1 = r17;
        if (r0 != r1) goto L_0x0058;
    L_0x00c7:
        r4 = 0;
        r12 = 0;
        r13 = r14.getName();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        if (r13 == 0) goto L_0x0058;
    L_0x00cf:
        r15 = r14.getAttributeCount();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r11 = 0;
    L_0x00d4:
        if (r11 >= r15) goto L_0x012b;
    L_0x00d6:
        r2 = r14.getAttributeName(r11);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r3 = r14.getAttributeValue(r11);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r17 = DBG;	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        if (r17 == 0) goto L_0x010e;
    L_0x00e2:
        r17 = "SThemeManager";
        r18 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r18.<init>();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r19 = "Parsing... [";
        r18 = r18.append(r19);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r0 = r18;
        r18 = r0.append(r2);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r19 = "][";
        r18 = r18.append(r19);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r0 = r18;
        r18 = r0.append(r3);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r19 = "]";
        r18 = r18.append(r19);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r18 = r18.toString();	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        android.util.Log.d(r17, r18);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
    L_0x010e:
        if (r2 == 0) goto L_0x011b;
    L_0x0110:
        r17 = "className";
        r0 = r17;
        r17 = r2.equals(r0);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        if (r17 == 0) goto L_0x011b;
    L_0x011a:
        r4 = r3;
    L_0x011b:
        if (r2 == 0) goto L_0x0128;
    L_0x011d:
        r17 = "iconId";
        r0 = r17;
        r17 = r2.equals(r0);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        if (r17 == 0) goto L_0x0128;
    L_0x0127:
        r12 = r3;
    L_0x0128:
        r11 = r11 + 1;
        goto L_0x00d4;
    L_0x012b:
        r17 = "ThemeApp";
        r0 = r17;
        r17 = r13.equals(r0);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        if (r17 == 0) goto L_0x0058;
    L_0x0135:
        r17 = sPackageIconMap;	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r0 = r17;
        r0.put(r4, r12);	 Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x0145, NotFoundException -> 0x0170 }
        r4 = 0;
        goto L_0x0058;
    L_0x013f:
        r9 = move-exception;
        r9.printStackTrace();
        goto L_0x0089;
    L_0x0145:
        r9 = move-exception;
        r17 = "SThemeManager";
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x019b }
        r18.<init>();	 Catch:{ all -> 0x019b }
        r19 = "Exception during parsing theme app list";
        r18 = r18.append(r19);	 Catch:{ all -> 0x019b }
        r0 = r18;
        r18 = r0.append(r9);	 Catch:{ all -> 0x019b }
        r18 = r18.toString();	 Catch:{ all -> 0x019b }
        android.util.Log.e(r17, r18);	 Catch:{ all -> 0x019b }
        r9.printStackTrace();	 Catch:{ all -> 0x019b }
        if (r6 == 0) goto L_0x0089;
    L_0x0165:
        r6.close();	 Catch:{ Exception -> 0x016a }
        goto L_0x0089;
    L_0x016a:
        r9 = move-exception;
        r9.printStackTrace();
        goto L_0x0089;
    L_0x0170:
        r9 = move-exception;
        r17 = "SThemeManager";
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x019b }
        r18.<init>();	 Catch:{ all -> 0x019b }
        r19 = "Exception during parsing theme app list";
        r18 = r18.append(r19);	 Catch:{ all -> 0x019b }
        r0 = r18;
        r18 = r0.append(r9);	 Catch:{ all -> 0x019b }
        r18 = r18.toString();	 Catch:{ all -> 0x019b }
        android.util.Log.e(r17, r18);	 Catch:{ all -> 0x019b }
        r9.printStackTrace();	 Catch:{ all -> 0x019b }
        if (r6 == 0) goto L_0x0089;
    L_0x0190:
        r6.close();	 Catch:{ Exception -> 0x0195 }
        goto L_0x0089;
    L_0x0195:
        r9 = move-exception;
        r9.printStackTrace();
        goto L_0x0089;
    L_0x019b:
        r17 = move-exception;
        if (r6 == 0) goto L_0x01a1;
    L_0x019e:
        r6.close();	 Catch:{ Exception -> 0x01a2 }
    L_0x01a1:
        throw r17;
    L_0x01a2:
        r9 = move-exception;
        r9.printStackTrace();
        goto L_0x01a1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.theme.SThemeManager.loadThemeAppList():void");
    }
}
