package android.graphics;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PersonaInfo;
import android.content.res.AssetManager;
import android.graphics.FontListParser.Alias;
import android.graphics.FontListParser.Config;
import android.graphics.FontListParser.Family;
import android.graphics.FontListParser.Font;
import android.net.ProxyInfo;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

public class Typeface {
    public static final int BOLD = 1;
    public static final int BOLD_ITALIC = 3;
    private static final boolean DEBUG = false;
    public static final Typeface DEFAULT = create(null, 0);
    public static final Typeface DEFAULT_BOLD = create(null, 1);
    private static final String DROIDSANS = "DroidSans.ttf";
    private static final String DROIDSANS_BOLD = "DroidSans-Bold.ttf";
    private static final boolean FLIP_ALL_APPS = true;
    static final String FONTS_CONFIG = "fonts.xml";
    private static final String FONTS_FOLDER = "/system/fonts/";
    private static String FlipFontPath = ProxyInfo.LOCAL_EXCL_LIST;
    private static final String[] FontsLikeDefault = new String[]{"sans-serif-thin", "sans-serif-light", "sans-serif", "sans-serif-medium", "sans-serif-black", "sec-roboto-light", "roboto-num3L", "roboto-num3R", "samsung-sans-thin", "samsung-sans-light"};
    public static final int ITALIC = 2;
    public static final Typeface MONOSPACE = create("monospace", 0);
    private static final int MONOSPACE_INDEX = 3;
    public static final int NORMAL = 0;
    private static final String OWNER_SANS_LOC_PATH = "/data/data/com.android.settings/app_fonts/sans.loc";
    private static final int SANS_INDEX = 1;
    public static final Typeface SANS_SERIF = create("sans-serif", 0);
    public static final Typeface SERIF = create("serif", 0);
    private static final int SERIF_INDEX = 2;
    private static String TAG = "Typeface";
    private static final String TAG_MONOTYPE = "Monotype";
    private static final String USER_SANS_LOC_POST = "/com.android.settings/app_fonts/sans.loc";
    private static final String USER_SANS_LOC_PRE = "/data/user/";
    public static boolean isFlipFontUsed = false;
    static Typeface sDefaultTypeface;
    static Typeface[] sDefaults = new Typeface[]{DEFAULT, DEFAULT_BOLD, create(null, 2), create(null, 3)};
    static FontFamily[] sFallbackFonts;
    static Map<String, Typeface> sSystemFontMap;
    private static final LongSparseArray<SparseArray<Typeface>> sTypefaceCache = new LongSparseArray(3);
    public boolean isLikeDefault = false;
    private int mStyle = 0;
    public long native_instance;

    private static native long nativeCreateFromArray(long[] jArr);

    private static native long nativeCreateFromTypeface(long j, int i);

    private static native long nativeCreateWeightAlias(long j, int i);

    private static native int nativeGetStyle(long j);

    private static native void nativeSetDefault(long j);

    private static native void nativeUnref(long j);

    static {
        init();
    }

    private static void setDefault(Typeface t) {
        sDefaultTypeface = t;
        nativeSetDefault(t.native_instance);
    }

    public int getStyle() {
        return this.mStyle;
    }

    public final boolean isBold() {
        return (this.mStyle & 1) != 0;
    }

    public final boolean isItalic() {
        return (this.mStyle & 2) != 0;
    }

    public static Typeface create(String familyName, int style) {
        if (sSystemFontMap != null) {
            return create((Typeface) sSystemFontMap.get(familyName), style);
        }
        return null;
    }

    public static Typeface create(Typeface family, int style) {
        Typeface typeface;
        long ni = 0;
        if (style < 0 || style > 3) {
            style = 0;
        }
        if (family != null) {
            if (family.mStyle == style) {
                return family;
            }
            if (isFlipFontUsed && family.isLikeDefault) {
                ni = 0;
            } else {
                ni = family.native_instance;
            }
        }
        SparseArray<Typeface> styles = (SparseArray) sTypefaceCache.get(ni);
        if (styles != null) {
            typeface = (Typeface) styles.get(style);
            if (typeface != null) {
                return typeface;
            }
        }
        typeface = new Typeface(nativeCreateFromTypeface(ni, style));
        if (!(typeface == null || family == null)) {
            typeface.isLikeDefault = family.isLikeDefault;
        }
        if (styles == null) {
            styles = new SparseArray(4);
            sTypefaceCache.put(ni, styles);
        }
        styles.put(style, typeface);
        return typeface;
    }

    public static Typeface defaultFromStyle(int style) {
        if (style < 0 || style > 3) {
            style = 0;
        }
        return sDefaults[style];
    }

    public static Typeface createFromAsset(AssetManager mgr, String path) {
        if (sFallbackFonts != null) {
            if (new FontFamily().addFontFromAsset(mgr, path)) {
                return createFromFamiliesWithDefault(new FontFamily[]{new FontFamily()});
            }
        }
        throw new RuntimeException("Font asset not found " + path);
    }

    public static Typeface createFromFile(File path) {
        return createFromFile(path.getAbsolutePath());
    }

    public static Typeface createFromFile(String path) {
        if (sFallbackFonts != null) {
            if (new FontFamily().addFont(path)) {
                return createFromFamiliesWithDefault(new FontFamily[]{new FontFamily()});
            }
        }
        throw new RuntimeException("Font not found " + path);
    }

    public static Typeface createFromFamilies(FontFamily[] families) {
        long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        return new Typeface(nativeCreateFromArray(ptrArray));
    }

    public static Typeface createFromFamiliesWithDefault(FontFamily[] families) {
        int i;
        long[] ptrArray = new long[(families.length + sFallbackFonts.length)];
        for (i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        for (i = 0; i < sFallbackFonts.length; i++) {
            ptrArray[families.length + i] = sFallbackFonts[i].mNativePtr;
        }
        return new Typeface(nativeCreateFromArray(ptrArray));
    }

    private Typeface(long ni) {
        if (ni == 0) {
            throw new RuntimeException("native typeface cannot be made");
        }
        this.native_instance = ni;
        this.mStyle = nativeGetStyle(ni);
    }

    private static FontFamily makeFamilyFromParsed(Family family) {
        FontFamily fontFamily = new FontFamily(family.lang, family.variant);
        for (Font font : family.fonts) {
            fontFamily.addFontWeightStyle(font.fontName, font.weight, font.isItalic);
        }
        return fontFamily;
    }

    private static void init() {
        File configFilename = new File(getSystemFontConfigLocation(), FONTS_CONFIG);
        try {
            int i;
            Family f;
            Config fontConfig = FontListParser.parse(new FileInputStream(configFilename));
            List<FontFamily> familyList = new ArrayList();
            for (i = 0; i < fontConfig.families.size(); i++) {
                f = (Family) fontConfig.families.get(i);
                if (i == 0 || f.name == null) {
                    familyList.add(makeFamilyFromParsed(f));
                }
            }
            sFallbackFonts = (FontFamily[]) familyList.toArray(new FontFamily[familyList.size()]);
            setDefault(createFromFamilies(sFallbackFonts));
            Map<String, Typeface> systemFonts = new HashMap();
            for (i = 0; i < fontConfig.families.size(); i++) {
                f = (Family) fontConfig.families.get(i);
                if (f.name != null) {
                    Typeface typeface;
                    if (i == 0) {
                        typeface = sDefaultTypeface;
                    } else {
                        typeface = createFromFamiliesWithDefault(new FontFamily[]{makeFamilyFromParsed(f)});
                    }
                    for (Object equals : FontsLikeDefault) {
                        if (f.name.equals(equals)) {
                            typeface.isLikeDefault = true;
                            break;
                        }
                    }
                    systemFonts.put(f.name, typeface);
                }
            }
            for (Alias alias : fontConfig.aliases) {
                Typeface base = (Typeface) systemFonts.get(alias.toName);
                Typeface newFace = base;
                int weight = alias.weight;
                if (weight != 400) {
                    Typeface typeface2 = new Typeface(nativeCreateWeightAlias(base.native_instance, weight));
                    for (Object equals2 : FontsLikeDefault) {
                        if (alias.name.equals(equals2)) {
                            typeface2.isLikeDefault = true;
                            break;
                        }
                    }
                }
                systemFonts.put(alias.name, newFace);
            }
            sSystemFontMap = systemFonts;
        } catch (RuntimeException e) {
            Log.w(TAG, "Didn't create default family (most likely, non-Minikin build)", e);
        } catch (FileNotFoundException e2) {
            Log.e(TAG, "Error opening " + configFilename);
        } catch (IOException e3) {
            Log.e(TAG, "Error reading " + configFilename);
        } catch (XmlPullParserException e4) {
            Log.e(TAG, "XML parse exception for " + configFilename);
        }
    }

    private static File getSystemFontConfigLocation() {
        return new File("/system/etc/");
    }

    protected void finalize() throws Throwable {
        try {
            nativeUnref(this.native_instance);
        } finally {
            super.finalize();
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Typeface typeface = (Typeface) o;
        if (this.mStyle == typeface.mStyle && this.native_instance == typeface.native_instance) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((int) (this.native_instance ^ (this.native_instance >>> 32))) + 527) * 31) + this.mStyle;
    }

    public static String getFontNameFlipFont(Context ctx, int typefaceIndex) {
        String[] parts = getFullFlipFont(ctx, typefaceIndex).split("#");
        if (parts.length >= 2) {
            return parts[1];
        }
        if (parts[0].endsWith(PersonaInfo.PERSONA_TYPE_DEFAULT)) {
            return PersonaInfo.PERSONA_TYPE_DEFAULT;
        }
        return null;
    }

    public static String getFontPathFlipFont(Context ctx, int typefaceIndex) {
        return getFullFlipFont(ctx, typefaceIndex).split("#")[0];
    }

    private static String getFullFlipFont(Context ctx, int typefaceIndex) {
        FileInputStream fis;
        FileInputStream fis2;
        String string;
        Throwable th;
        IOException e;
        String systemFont = PersonaInfo.PERSONA_TYPE_DEFAULT;
        switch (typefaceIndex) {
            case 1:
                String sans_path = ProxyInfo.LOCAL_EXCL_LIST;
                int currentUser = UserHandle.getCallingUserId();
                if (ctx != null && UserManager.supportsMultipleUsers() && currentUser == 0) {
                    try {
                        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
                        currentUser = ActivityManager.getCurrentUser();
                    } catch (Exception e2) {
                    }
                }
                BufferedReader br;
                BufferedReader bufferedReader;
                if (currentUser == 0) {
                    sans_path = OWNER_SANS_LOC_PATH;
                    systemFont = SystemProperties.get("persist.sys.flipfontpath", "empty");
                    if (systemFont.equals("empty")) {
                        fis = null;
                        try {
                            fis2 = new FileInputStream(new File(sans_path));
                            try {
                                br = new BufferedReader(new InputStreamReader(fis2));
                                try {
                                    string = br.readLine();
                                    fis2.close();
                                    br.close();
                                    if (fis2 != null) {
                                        try {
                                            fis2.close();
                                        } catch (IOException e3) {
                                            bufferedReader = br;
                                            fis = fis2;
                                        }
                                    }
                                    bufferedReader = br;
                                    fis = fis2;
                                } catch (FileNotFoundException e4) {
                                    bufferedReader = br;
                                    fis = fis2;
                                    try {
                                        string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                        if (fis != null) {
                                            try {
                                                fis.close();
                                            } catch (IOException e5) {
                                            }
                                        }
                                        systemFont = string;
                                        if (systemFont == null) {
                                            return systemFont;
                                        }
                                        return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        if (fis != null) {
                                            try {
                                                fis.close();
                                            } catch (IOException e6) {
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (IOException e7) {
                                    e = e7;
                                    bufferedReader = br;
                                    fis = fis2;
                                    string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    e.printStackTrace();
                                    if (fis != null) {
                                        try {
                                            fis.close();
                                        } catch (IOException e8) {
                                        }
                                    }
                                    systemFont = string;
                                    if (systemFont == null) {
                                        return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    }
                                    return systemFont;
                                } catch (Throwable th3) {
                                    th = th3;
                                    bufferedReader = br;
                                    fis = fis2;
                                    if (fis != null) {
                                        fis.close();
                                    }
                                    throw th;
                                }
                            } catch (FileNotFoundException e9) {
                                fis = fis2;
                                string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                if (fis != null) {
                                    fis.close();
                                }
                                systemFont = string;
                                if (systemFont == null) {
                                    return systemFont;
                                }
                                return PersonaInfo.PERSONA_TYPE_DEFAULT;
                            } catch (IOException e10) {
                                e = e10;
                                fis = fis2;
                                string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                e.printStackTrace();
                                if (fis != null) {
                                    fis.close();
                                }
                                systemFont = string;
                                if (systemFont == null) {
                                    return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                }
                                return systemFont;
                            } catch (Throwable th4) {
                                th = th4;
                                fis = fis2;
                                if (fis != null) {
                                    fis.close();
                                }
                                throw th;
                            }
                        } catch (FileNotFoundException e11) {
                            string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                            if (fis != null) {
                                fis.close();
                            }
                            systemFont = string;
                            if (systemFont == null) {
                                return systemFont;
                            }
                            return PersonaInfo.PERSONA_TYPE_DEFAULT;
                        } catch (IOException e12) {
                            e = e12;
                            string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                            e.printStackTrace();
                            if (fis != null) {
                                fis.close();
                            }
                            systemFont = string;
                            if (systemFont == null) {
                                return PersonaInfo.PERSONA_TYPE_DEFAULT;
                            }
                            return systemFont;
                        }
                        systemFont = string;
                    }
                } else if (currentUser >= 100) {
                    sans_path = OWNER_SANS_LOC_PATH;
                    systemFont = SystemProperties.get("persist.sys.flipfontpath", "empty");
                    if (systemFont.equals("empty")) {
                        fis = null;
                        try {
                            fis2 = new FileInputStream(new File(sans_path));
                            try {
                                br = new BufferedReader(new InputStreamReader(fis2));
                                try {
                                    string = br.readLine();
                                    fis2.close();
                                    br.close();
                                    if (fis2 != null) {
                                        try {
                                            fis2.close();
                                        } catch (IOException e13) {
                                            bufferedReader = br;
                                            fis = fis2;
                                        }
                                    }
                                    bufferedReader = br;
                                    fis = fis2;
                                } catch (FileNotFoundException e14) {
                                    bufferedReader = br;
                                    fis = fis2;
                                    try {
                                        string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                        if (fis != null) {
                                            try {
                                                fis.close();
                                            } catch (IOException e15) {
                                            }
                                        }
                                        systemFont = string;
                                        if (systemFont == null) {
                                            return systemFont;
                                        }
                                        return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    } catch (Throwable th5) {
                                        th = th5;
                                        if (fis != null) {
                                            try {
                                                fis.close();
                                            } catch (IOException e16) {
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (IOException e17) {
                                    e = e17;
                                    bufferedReader = br;
                                    fis = fis2;
                                    string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    e.printStackTrace();
                                    if (fis != null) {
                                        try {
                                            fis.close();
                                        } catch (IOException e18) {
                                        }
                                    }
                                    systemFont = string;
                                    if (systemFont == null) {
                                        return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    }
                                    return systemFont;
                                } catch (Throwable th6) {
                                    th = th6;
                                    bufferedReader = br;
                                    fis = fis2;
                                    if (fis != null) {
                                        fis.close();
                                    }
                                    throw th;
                                }
                            } catch (FileNotFoundException e19) {
                                fis = fis2;
                                string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                if (fis != null) {
                                    fis.close();
                                }
                                systemFont = string;
                                if (systemFont == null) {
                                    return systemFont;
                                }
                                return PersonaInfo.PERSONA_TYPE_DEFAULT;
                            } catch (IOException e20) {
                                e = e20;
                                fis = fis2;
                                string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                e.printStackTrace();
                                if (fis != null) {
                                    fis.close();
                                }
                                systemFont = string;
                                if (systemFont == null) {
                                    return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                }
                                return systemFont;
                            } catch (Throwable th7) {
                                th = th7;
                                fis = fis2;
                                if (fis != null) {
                                    fis.close();
                                }
                                throw th;
                            }
                        } catch (FileNotFoundException e21) {
                            string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                            if (fis != null) {
                                fis.close();
                            }
                            systemFont = string;
                            if (systemFont == null) {
                                return systemFont;
                            }
                            return PersonaInfo.PERSONA_TYPE_DEFAULT;
                        } catch (IOException e22) {
                            e = e22;
                            string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                            e.printStackTrace();
                            if (fis != null) {
                                fis.close();
                            }
                            systemFont = string;
                            if (systemFont == null) {
                                return PersonaInfo.PERSONA_TYPE_DEFAULT;
                            }
                            return systemFont;
                        }
                        systemFont = string;
                    }
                } else {
                    sans_path = USER_SANS_LOC_PRE + currentUser + USER_SANS_LOC_POST;
                    systemFont = "empty";
                    if (systemFont.equals("empty")) {
                        fis = null;
                        try {
                            fis2 = new FileInputStream(new File(sans_path));
                            try {
                                br = new BufferedReader(new InputStreamReader(fis2));
                                try {
                                    string = br.readLine();
                                    fis2.close();
                                    br.close();
                                    if (fis2 != null) {
                                        try {
                                            fis2.close();
                                        } catch (IOException e23) {
                                            bufferedReader = br;
                                            fis = fis2;
                                        }
                                    }
                                    bufferedReader = br;
                                    fis = fis2;
                                } catch (FileNotFoundException e24) {
                                    bufferedReader = br;
                                    fis = fis2;
                                    try {
                                        string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                        if (fis != null) {
                                            try {
                                                fis.close();
                                            } catch (IOException e25) {
                                            }
                                        }
                                        systemFont = string;
                                        if (systemFont == null) {
                                            return systemFont;
                                        }
                                        return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    } catch (Throwable th8) {
                                        th = th8;
                                        if (fis != null) {
                                            try {
                                                fis.close();
                                            } catch (IOException e26) {
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (IOException e27) {
                                    e = e27;
                                    bufferedReader = br;
                                    fis = fis2;
                                    string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    e.printStackTrace();
                                    if (fis != null) {
                                        try {
                                            fis.close();
                                        } catch (IOException e28) {
                                        }
                                    }
                                    systemFont = string;
                                    if (systemFont == null) {
                                        return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                    }
                                    return systemFont;
                                } catch (Throwable th9) {
                                    th = th9;
                                    bufferedReader = br;
                                    fis = fis2;
                                    if (fis != null) {
                                        fis.close();
                                    }
                                    throw th;
                                }
                            } catch (FileNotFoundException e29) {
                                fis = fis2;
                                string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                if (fis != null) {
                                    fis.close();
                                }
                                systemFont = string;
                                if (systemFont == null) {
                                    return systemFont;
                                }
                                return PersonaInfo.PERSONA_TYPE_DEFAULT;
                            } catch (IOException e30) {
                                e = e30;
                                fis = fis2;
                                string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                                e.printStackTrace();
                                if (fis != null) {
                                    fis.close();
                                }
                                systemFont = string;
                                if (systemFont == null) {
                                    return PersonaInfo.PERSONA_TYPE_DEFAULT;
                                }
                                return systemFont;
                            } catch (Throwable th10) {
                                th = th10;
                                fis = fis2;
                                if (fis != null) {
                                    fis.close();
                                }
                                throw th;
                            }
                        } catch (FileNotFoundException e31) {
                            string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                            if (fis != null) {
                                fis.close();
                            }
                            systemFont = string;
                            if (systemFont == null) {
                                return systemFont;
                            }
                            return PersonaInfo.PERSONA_TYPE_DEFAULT;
                        } catch (IOException e32) {
                            e = e32;
                            string = PersonaInfo.PERSONA_TYPE_DEFAULT;
                            e.printStackTrace();
                            if (fis != null) {
                                fis.close();
                            }
                            systemFont = string;
                            if (systemFont == null) {
                                return PersonaInfo.PERSONA_TYPE_DEFAULT;
                            }
                            return systemFont;
                        }
                        systemFont = string;
                    }
                }
        }
        if (systemFont == null) {
            return PersonaInfo.PERSONA_TYPE_DEFAULT;
        }
        return systemFont;
    }

    private static void SetFlipFonts(Context ctx) {
        String strFontPathBold = ProxyInfo.LOCAL_EXCL_LIST;
        String strFontPath = getFontPathFlipFont(ctx, 1);
        if (strFontPath.endsWith(PersonaInfo.PERSONA_TYPE_DEFAULT)) {
            isFlipFontUsed = false;
        } else {
            isFlipFontUsed = true;
            strFontPathBold = strFontPath + "/" + DROIDSANS_BOLD;
            strFontPath = strFontPath + "/" + DROIDSANS;
        }
        if (!strFontPath.equals(FlipFontPath)) {
            FlipFontPath = strFontPath;
            long iNative = DEFAULT.native_instance;
            if (!isFlipFontUsed || strFontPath.isEmpty()) {
                nativeSetDefault(sDefaultTypeface.native_instance);
                DEFAULT.native_instance = nativeCreateFromTypeface(0, 0);
            } else {
                try {
                    DEFAULT.native_instance = createFromFile(strFontPath).native_instance;
                } catch (RuntimeException e) {
                    DEFAULT.native_instance = create((String) null, 0).native_instance;
                }
                if (DEFAULT.native_instance == 0) {
                    DEFAULT.native_instance = create((String) null, 0).native_instance;
                }
            }
            DEFAULT.mStyle = nativeGetStyle(DEFAULT.native_instance);
            iNative = DEFAULT_BOLD.native_instance;
            if (!isFlipFontUsed || strFontPathBold.isEmpty()) {
                DEFAULT_BOLD.native_instance = nativeCreateFromTypeface(0, 1);
            } else {
                try {
                    DEFAULT_BOLD.native_instance = createFromFile(strFontPathBold).native_instance;
                } catch (RuntimeException e2) {
                    DEFAULT_BOLD.native_instance = create((String) null, 1).native_instance;
                }
                if (DEFAULT_BOLD.native_instance == 0) {
                    DEFAULT_BOLD.native_instance = create((String) null, 1).native_instance;
                }
            }
            DEFAULT_BOLD.mStyle = nativeGetStyle(DEFAULT_BOLD.native_instance);
            iNative = sDefaults[0].native_instance;
            sDefaults[0].native_instance = nativeCreateFromTypeface(DEFAULT.native_instance, 0);
            sDefaults[0].mStyle = nativeGetStyle(sDefaults[0].native_instance);
            iNative = sDefaults[1].native_instance;
            sDefaults[1].native_instance = nativeCreateFromTypeface(DEFAULT_BOLD.native_instance, 1);
            sDefaults[1].mStyle = nativeGetStyle(sDefaults[1].native_instance);
            iNative = sDefaults[2].native_instance;
            sDefaults[2].native_instance = nativeCreateFromTypeface(DEFAULT.native_instance, 2);
            sDefaults[2].mStyle = nativeGetStyle(sDefaults[2].native_instance);
            iNative = sDefaults[3].native_instance;
            sDefaults[3].native_instance = nativeCreateFromTypeface(DEFAULT_BOLD.native_instance, 3);
            sDefaults[3].mStyle = nativeGetStyle(sDefaults[3].native_instance);
            if (isFlipFontUsed) {
                nativeSetDefault(sDefaultTypeface.native_instance);
            }
        }
    }

    public static void SetAppTypeFace(Context ctx, String sAppName) {
        SetFlipFonts(ctx);
    }
}
