package com.samsung.android.cocktailbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class CocktailProviderInfo implements Parcelable {
    private static final String COCKTAIL_CATEGORY = "category";
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL = 2;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_EXPRESS_ME = 64;
    public static final int COCKTAIL_CATEGORY_FEEDS = 256;
    public static final int COCKTAIL_CATEGORY_HOME_SCREEN = 8;
    public static final int COCKTAIL_CATEGORY_INVALID = -1;
    public static final int COCKTAIL_CATEGORY_LOCK_SCREEN = 16;
    public static final int COCKTAIL_CATEGORY_NIGHT_MODE = 128;
    public static final int COCKTAIL_CATEGORY_NORMAL = 1;
    public static final int COCKTAIL_CATEGORY_QUICK_TOOL = 4;
    public static final int COCKTAIL_CATEGORY_TABLE_MODE = 32;
    public static final int COCKTAIL_CATEGORY_WHISPER = 512;
    private static final String COCKTAIL_COCKTAIL_AFFINITY = "cocktailAffinity";
    private static final String COCKTAIL_COCKTAIL_WIDTH = "cocktailWidth";
    private static final String COCKTAIL_CONFIGURE = "configure";
    private static final String COCKTAIL_CSC_PREVIEW_IMAGE = "cscPreviewImage";
    private static final String COCKTAIL_DESCRIPTION = "description";
    private static final String COCKTAIL_ICON = "icon";
    private static final String COCKTAIL_LABEL = "label";
    private static final String COCKTAIL_LAUNCH_ON_CLICK = "launchOnClick";
    private static final String COCKTAIL_PERMIT_VISIBILITY_CHANGED = "permitVisibilityChanged";
    private static final String COCKTAIL_PREVIEW_IMAGE = "previewImage";
    private static final String COCKTAIL_PRIVATE_MODE = "privateMode";
    private static final String COCKTAIL_PULL_TO_REFRESH = "pullToRefresh";
    private static final String COCKTAIL_UPDATE_TIME = "updatePeriodMillis";
    private static final String COCKTAIL_WHISPER = "whisper";
    public static final Creator<CocktailProviderInfo> CREATOR = new Creator<CocktailProviderInfo>() {
        public CocktailProviderInfo createFromParcel(Parcel in) {
            return new CocktailProviderInfo(in);
        }

        public CocktailProviderInfo[] newArray(int size) {
            return new CocktailProviderInfo[size];
        }
    };
    private static final String TAG = "CocktailProviderInfo";
    private static final int VAL_DEFAULT_COCKTAIL_WIDTH = 160;
    private static final String XMLVAL_CONTEXTUAL = "contextual";
    private static final String XMLVAL_FEEDS = "feeds";
    private static final String XMLVAL_HOME_SCREEN = "homescreen";
    private static final String XMLVAL_LOCK_SCREEN = "lockscreen";
    private static final String XMLVAL_NIGHT_MODE = "nightmode";
    private static final String XMLVAL_NORMAL = "normal";
    private static final String XMLVAL_QUICK_TOOL = "quicktool";
    private static final String XMLVAL_TABLE_MODE = "tablemode";
    private static final String XMLVAL_WHISPER = "whisper";
    public int category;
    public int cocktailAffinity;
    public int cocktailWidth;
    public ComponentName configure;
    public boolean cscPreviewImage;
    public int description;
    public int icon;
    public int label;
    public String launchOnClick;
    public boolean permitVisibilityChanged;
    public int previewImage;
    public String privateMode;
    public ComponentName provider;
    public boolean pullToRefresh;
    public int updatePeriodMillis;
    public String whisper;

    public static com.samsung.android.cocktailbar.CocktailProviderInfo create(android.content.Context r14, android.content.pm.ResolveInfo r15, android.content.ComponentName r16, android.content.res.XmlResourceParser r17, int r18, int r19) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:26:? in {14, 16, 18, 21, 22, 23, 24, 25, 27, 28} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r4 = r14.getPackageManager();
        r12 = android.os.Binder.clearCallingIdentity();
        r3 = r16.getPackageName();	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        r6 = r15.activityInfo;	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        r6 = r6.applicationInfo;	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        r6 = r6.uid;	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        r6 = android.os.UserHandle.getUserId(r6);	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        r5 = r4.getResourcesForApplicationAsUser(r3, r6);	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        android.os.Binder.restoreCallingIdentity(r12);
        r2 = new com.samsung.android.cocktailbar.CocktailProviderInfo;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r3 = r14;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r6 = r16;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r7 = r17;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r8 = r15;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r9 = r19;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r2.<init>(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r0 = r18;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r3 = enforceValidCategory(r0, r2);	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        if (r3 == 0) goto L_0x0037;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
    L_0x0032:
        r3 = r2.category;	 Catch:{ NotFoundException -> 0x004b, IllegalArgumentException -> 0x0055 }
        r6 = -1;
        if (r3 != r6) goto L_0x0038;
    L_0x0037:
        r2 = 0;
    L_0x0038:
        return r2;
    L_0x0039:
        r10 = move-exception;
        r3 = "CocktailProviderInfo";	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        r6 = "failed to load find package";	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        android.util.Log.e(r3, r6, r10);	 Catch:{ NameNotFoundException -> 0x0039, all -> 0x0046 }
        r2 = 0;
        android.os.Binder.restoreCallingIdentity(r12);
        goto L_0x0038;
    L_0x0046:
        r3 = move-exception;
        android.os.Binder.restoreCallingIdentity(r12);
        throw r3;
    L_0x004b:
        r10 = move-exception;
        r3 = "CocktailProviderInfo";
        r6 = "XML resources failed";
        android.util.Log.e(r3, r6);
    L_0x0053:
        r2 = 0;
        goto L_0x0038;
    L_0x0055:
        r10 = move-exception;
        r3 = "CocktailProviderInfo";
        r6 = "IllegalArgumentException";
        android.util.Log.e(r3, r6);
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.cocktailbar.CocktailProviderInfo.create(android.content.Context, android.content.pm.ResolveInfo, android.content.ComponentName, android.content.res.XmlResourceParser, int, int):com.samsung.android.cocktailbar.CocktailProviderInfo");
    }

    public CocktailProviderInfo() {
        this.permitVisibilityChanged = false;
    }

    private CocktailProviderInfo(Context context, PackageManager pkgMgr, Resources res, ComponentName provider, XmlResourceParser xml, ResolveInfo info, int version) throws NotFoundException, IllegalArgumentException {
        this.permitVisibilityChanged = false;
        this.provider = provider;
        this.icon = xml.getAttributeResourceValue(null, COCKTAIL_ICON, 0);
        this.label = xml.getAttributeResourceValue(null, COCKTAIL_LABEL, 0);
        this.description = xml.getAttributeResourceValue(null, COCKTAIL_DESCRIPTION, 0);
        String category = loadXmlString(xml, res, COCKTAIL_CATEGORY, "normal");
        if (TextUtils.isEmpty(category)) {
            this.category = 1;
        } else {
            SimpleStringSplitter categorySplitter = new SimpleStringSplitter('|');
            categorySplitter.setString(category);
            while (categorySplitter.hasNext()) {
                String c = categorySplitter.next().trim();
                int categoryId = getCategoryId(c);
                boolean isBreak = false;
                switch (categoryId) {
                    case -1:
                        Log.e(TAG, "Provider: " + provider + " specified an invalid catetory of " + c);
                        this.category = -1;
                        return;
                    case 4:
                    case 32:
                    case 128:
                        this.category = categoryId;
                        isBreak = true;
                        continue;
                    case 8:
                    case 16:
                    case 256:
                        this.category |= categoryId | 1;
                        continue;
                    default:
                        this.category |= categoryId;
                        continue;
                }
                if (isBreak) {
                }
            }
        }
        if (version > 1) {
            this.cocktailWidth = loadXmlDimension(xml, res, COCKTAIL_COCKTAIL_WIDTH, 160);
            this.cocktailAffinity = loadXmlInt(xml, res, COCKTAIL_COCKTAIL_AFFINITY, 0);
            this.launchOnClick = loadXmlString(xml, res, COCKTAIL_LAUNCH_ON_CLICK, null);
            if (this.cocktailAffinity < 0) {
                this.cocktailAffinity = 0;
            }
        } else {
            this.cocktailWidth = 160;
        }
        this.privateMode = loadXmlString(xml, res, COCKTAIL_PRIVATE_MODE, null);
        this.previewImage = xml.getAttributeResourceValue(null, COCKTAIL_PREVIEW_IMAGE, 0);
        this.updatePeriodMillis = loadXmlInt(xml, res, COCKTAIL_UPDATE_TIME, 0);
        this.permitVisibilityChanged = loadXmlBoolean(xml, res, COCKTAIL_PERMIT_VISIBILITY_CHANGED, false);
        this.pullToRefresh = loadXmlBoolean(xml, res, COCKTAIL_PULL_TO_REFRESH, false);
        String configureClassName = loadXmlString(xml, res, COCKTAIL_CONFIGURE, null);
        if (configureClassName != null) {
            this.configure = new ComponentName(provider.getPackageName(), configureClassName);
        }
        this.cscPreviewImage = loadXmlBoolean(xml, res, COCKTAIL_CSC_PREVIEW_IMAGE, false);
        if (this.category == 512) {
            this.whisper = loadXmlString(xml, res, "whisper", null);
        }
    }

    private static boolean enforceValidCategory(int categoryIds, CocktailProviderInfo pInfo) {
        if (categoryIds == 0) {
            Log.i(TAG, "enforceValidCategory: there is no category filters");
            return true;
        } else if (pInfo.privateMode != null) {
            return false;
        } else {
            if ((pInfo.category & categoryIds) == 0) {
                return false;
            }
            return true;
        }
    }

    public static int getCategoryIds(ArrayList<String> list) {
        int ids = 0;
        if (list == null || list.size() == 0) {
            return 0;
        }
        Iterator i$ = list.iterator();
        while (i$.hasNext()) {
            ids |= getCategoryId((String) i$.next());
        }
        return ids;
    }

    private static int getCategoryId(String category) {
        if ("normal".equals(category)) {
            return 1;
        }
        if (XMLVAL_CONTEXTUAL.equals(category)) {
            return 2;
        }
        if (XMLVAL_HOME_SCREEN.equals(category)) {
            return 8;
        }
        if (XMLVAL_FEEDS.equals(category)) {
            return 256;
        }
        if ("whisper".equals(category)) {
            return 512;
        }
        if (XMLVAL_QUICK_TOOL.equals(category)) {
            return 4;
        }
        if (XMLVAL_TABLE_MODE.equals(category)) {
            return 32;
        }
        if (XMLVAL_NIGHT_MODE.equals(category)) {
            return 128;
        }
        if (XMLVAL_LOCK_SCREEN.equals(category)) {
            return 16;
        }
        return -1;
    }

    private CocktailProviderInfo(Parcel in) {
        String readString;
        boolean z;
        ComponentName componentName;
        String str = null;
        boolean z2 = true;
        this.permitVisibilityChanged = false;
        this.provider = in.readInt() != 0 ? new ComponentName(in) : null;
        this.updatePeriodMillis = in.readInt();
        this.label = in.readInt();
        this.description = in.readInt();
        this.icon = in.readInt();
        this.previewImage = in.readInt();
        this.category = in.readInt();
        this.cocktailWidth = in.readInt();
        this.cocktailAffinity = in.readInt();
        if (in.readInt() != 0) {
            readString = in.readString();
        } else {
            readString = null;
        }
        this.privateMode = readString;
        if (in.readByte() == (byte) 1) {
            z = true;
        } else {
            z = false;
        }
        this.permitVisibilityChanged = z;
        if (in.readByte() == (byte) 1) {
            z = true;
        } else {
            z = false;
        }
        this.pullToRefresh = z;
        if (in.readInt() != 0) {
            componentName = new ComponentName(in);
        } else {
            componentName = null;
        }
        this.configure = componentName;
        if (in.readInt() != 0) {
            str = in.readString();
        }
        this.launchOnClick = str;
        if (in.readByte() != (byte) 1) {
            z2 = false;
        }
        this.cscPreviewImage = z2;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (this.provider != null) {
            out.writeInt(1);
            this.provider.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.updatePeriodMillis);
        out.writeInt(this.label);
        out.writeInt(this.description);
        out.writeInt(this.icon);
        out.writeInt(this.previewImage);
        out.writeInt(this.category);
        out.writeInt(this.cocktailWidth);
        out.writeInt(this.cocktailAffinity);
        if (this.privateMode != null) {
            out.writeInt(1);
            out.writeString(this.privateMode);
        } else {
            out.writeInt(0);
        }
        if (this.permitVisibilityChanged) {
            out.writeByte((byte) 1);
        } else {
            out.writeByte((byte) 0);
        }
        if (this.pullToRefresh) {
            out.writeByte((byte) 1);
        } else {
            out.writeByte((byte) 0);
        }
        if (this.configure != null) {
            out.writeInt(1);
            this.configure.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        if (this.launchOnClick != null) {
            out.writeInt(1);
            out.writeString(this.launchOnClick);
        } else {
            out.writeInt(0);
        }
        if (this.cscPreviewImage) {
            out.writeByte((byte) 1);
        } else {
            out.writeByte((byte) 0);
        }
    }

    private int loadXmlInt(XmlResourceParser parser, Resources pkgRes, String attrName, int defaultValue) {
        int refId = parser.getAttributeResourceValue(null, attrName, 0);
        if (refId == 0) {
            return parser.getAttributeIntValue(null, attrName, defaultValue);
        }
        try {
            return pkgRes.getInteger(refId);
        } catch (NotFoundException e) {
            return defaultValue;
        }
    }

    private String loadXmlString(XmlResourceParser parser, Resources pkgRes, String attrName, String defaultValue) {
        int refId = parser.getAttributeResourceValue(null, attrName, 0);
        if (refId != 0) {
            try {
                return pkgRes.getString(refId);
            } catch (NotFoundException e) {
                return defaultValue;
            }
        }
        String value = parser.getAttributeValue(null, attrName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private boolean loadXmlBoolean(XmlResourceParser parser, Resources pkgRes, String attrName, boolean defaultValue) {
        int refId = parser.getAttributeResourceValue(null, attrName, 0);
        if (refId == 0) {
            return parser.getAttributeBooleanValue(null, attrName, defaultValue);
        }
        try {
            return pkgRes.getBoolean(refId);
        } catch (NotFoundException e) {
            return defaultValue;
        }
    }

    private int loadXmlDimension(XmlResourceParser parser, Resources pkgRes, String attrName, int defaultValue) {
        int refId = parser.getAttributeResourceValue(null, attrName, 0);
        if (refId == 0) {
            return parser.getAttributeIntValue(null, attrName, defaultValue);
        }
        try {
            return pkgRes.getDimensionPixelSize(refId);
        } catch (NotFoundException e) {
            return defaultValue;
        }
    }

    public int describeContents() {
        return 0;
    }
}
