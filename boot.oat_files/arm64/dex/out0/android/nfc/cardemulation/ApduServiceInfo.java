package android.nfc.cardemulation;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public final class ApduServiceInfo implements Parcelable {
    public static final Creator<ApduServiceInfo> CREATOR = new Creator<ApduServiceInfo>() {
        public ApduServiceInfo createFromParcel(Parcel source) {
            ResolveInfo info = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(source);
            String description = source.readString();
            boolean onHost = source.readInt() != 0;
            ArrayList<AidGroup> staticAidGroups = new ArrayList();
            if (source.readInt() > 0) {
                source.readTypedList(staticAidGroups, AidGroup.CREATOR);
            }
            ArrayList<AidGroup> dynamicAidGroups = new ArrayList();
            if (source.readInt() > 0) {
                source.readTypedList(dynamicAidGroups, AidGroup.CREATOR);
            }
            boolean requiresUnlock = source.readInt() != 0;
            int bannerResource = source.readInt();
            int uid = source.readInt();
            String settingsActivityName = source.readString();
            SecureElementInfo seInfo = (SecureElementInfo) SecureElementInfo.CREATOR.createFromParcel(source);
            String bannerFileName = source.readString();
            boolean isSelected = source.readInt() != 0;
            if ("DCM".equals(ApduServiceInfo.SALES_CODE) || "KDI".equals(ApduServiceInfo.SALES_CODE)) {
                return new ApduServiceInfo(info, onHost, description, staticAidGroups, dynamicAidGroups, requiresUnlock, bannerResource, uid, settingsActivityName, isSelected);
            }
            return new ApduServiceInfo(info, onHost, description, staticAidGroups, dynamicAidGroups, requiresUnlock, bannerResource, bannerFileName, uid, settingsActivityName, seInfo, isSelected);
        }

        public ApduServiceInfo[] newArray(int size) {
            return new ApduServiceInfo[size];
        }
    };
    static final String CSC_SALES_CODE = SystemProperties.get("ro.csc.sales_code");
    static final String OMC_SALES_CODE = SystemProperties.get("persist.omc.sales_code");
    static final String SALES_CODE;
    static final String SECURE_ELEMENT_ESE = "eSE";
    public static final int SECURE_ELEMENT_ROUTE_ESE = 1;
    public static final int SECURE_ELEMENT_ROUTE_UICC = 2;
    static final String SECURE_ELEMENT_UICC = "UICC";
    static final String TAG = "ApduServiceInfo";
    String mBannerResourceFileName = null;
    final int mBannerResourceId;
    final String mDescription;
    final HashMap<String, AidGroup> mDynamicAidGroups;
    final boolean mOnHost;
    private boolean mOtherServiceSelectionState;
    final boolean mRequiresDeviceUnlock;
    final SecureElementInfo mSeInfo;
    final ResolveInfo mService;
    final String mSettingsActivityName;
    final HashMap<String, AidGroup> mStaticAidGroups;
    final int mUid;

    public static class SecureElementInfo implements Parcelable {
        public static final Creator<SecureElementInfo> CREATOR = new Creator<SecureElementInfo>() {
            public SecureElementInfo createFromParcel(Parcel source) {
                return new SecureElementInfo(source.readInt());
            }

            public SecureElementInfo[] newArray(int size) {
                return new SecureElementInfo[size];
            }
        };
        final int seId;

        public SecureElementInfo(int seId) {
            this.seId = seId;
        }

        public int getSeId() {
            return this.seId;
        }

        public String toString() {
            return new StringBuilder("seId: " + this.seId).toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.seId);
        }
    }

    static {
        String str;
        if (OMC_SALES_CODE == null || OMC_SALES_CODE.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            str = CSC_SALES_CODE;
        } else {
            str = OMC_SALES_CODE;
        }
        SALES_CODE = str;
    }

    public ApduServiceInfo(ResolveInfo info, boolean onHost, String description, ArrayList<AidGroup> staticAidGroups, ArrayList<AidGroup> dynamicAidGroups, boolean requiresUnlock, int bannerResource, String bannerFileName, int uid, String settingsActivityName, SecureElementInfo seInfo, boolean isSelected) {
        this.mService = info;
        this.mDescription = description;
        this.mStaticAidGroups = new HashMap();
        this.mDynamicAidGroups = new HashMap();
        this.mOnHost = onHost;
        this.mRequiresDeviceUnlock = requiresUnlock;
        Iterator i$ = staticAidGroups.iterator();
        while (i$.hasNext()) {
            AidGroup aidGroup = (AidGroup) i$.next();
            this.mStaticAidGroups.put(aidGroup.category, aidGroup);
        }
        i$ = dynamicAidGroups.iterator();
        while (i$.hasNext()) {
            aidGroup = (AidGroup) i$.next();
            this.mDynamicAidGroups.put(aidGroup.category, aidGroup);
        }
        this.mBannerResourceId = bannerResource;
        this.mBannerResourceFileName = bannerFileName;
        this.mUid = uid;
        this.mSettingsActivityName = settingsActivityName;
        this.mSeInfo = seInfo;
        this.mOtherServiceSelectionState = isSelected;
    }

    public ApduServiceInfo(ResolveInfo info, boolean onHost, String description, ArrayList<AidGroup> staticAidGroups, ArrayList<AidGroup> dynamicAidGroups, boolean requiresUnlock, int bannerResource, int uid, String settingsActivityName, boolean isSelected) {
        this.mService = info;
        this.mDescription = description;
        this.mStaticAidGroups = new HashMap();
        this.mDynamicAidGroups = new HashMap();
        this.mOnHost = onHost;
        this.mRequiresDeviceUnlock = requiresUnlock;
        Iterator i$ = staticAidGroups.iterator();
        while (i$.hasNext()) {
            AidGroup aidGroup = (AidGroup) i$.next();
            this.mStaticAidGroups.put(aidGroup.category, aidGroup);
        }
        i$ = dynamicAidGroups.iterator();
        while (i$.hasNext()) {
            aidGroup = (AidGroup) i$.next();
            this.mDynamicAidGroups.put(aidGroup.category, aidGroup);
        }
        this.mBannerResourceId = bannerResource;
        this.mUid = uid;
        this.mSettingsActivityName = settingsActivityName;
        this.mOtherServiceSelectionState = isSelected;
        this.mSeInfo = null;
    }

    private ResolveInfo buildResolveInfo(String packageName, String className, String description) {
        ResolveInfo ri = new ResolveInfo();
        ServiceInfo si = new ServiceInfo();
        ApplicationInfo ai = new ApplicationInfo();
        ai.packageName = packageName;
        ai.enabled = true;
        si.applicationInfo = ai;
        si.enabled = true;
        si.packageName = packageName;
        si.name = className;
        si.exported = true;
        si.nonLocalizedLabel = description;
        ri.nonLocalizedLabel = description;
        ri.serviceInfo = si;
        return ri;
    }

    public ApduServiceInfo(String description, ArrayList<AidGroup> staticAidGroups, String bannerFileName, int uid, String seName, String packageName, String className, boolean isSelected) {
        int i = 2;
        this.mService = buildResolveInfo(packageName, className, description);
        this.mDescription = description;
        this.mStaticAidGroups = new HashMap();
        this.mDynamicAidGroups = new HashMap();
        this.mOnHost = false;
        this.mRequiresDeviceUnlock = false;
        Iterator i$ = staticAidGroups.iterator();
        while (i$.hasNext()) {
            AidGroup aidGroup = (AidGroup) i$.next();
            this.mStaticAidGroups.put(aidGroup.category, aidGroup);
        }
        this.mBannerResourceId = -1;
        this.mBannerResourceFileName = bannerFileName;
        this.mUid = uid;
        this.mSettingsActivityName = null;
        if (seName != null) {
            if (seName.equals(SECURE_ELEMENT_ESE)) {
                i = 1;
            }
            this.mSeInfo = new SecureElementInfo(i);
        } else {
            this.mSeInfo = new SecureElementInfo(2);
        }
        this.mOtherServiceSelectionState = isSelected;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ApduServiceInfo(android.content.pm.PackageManager r25, android.content.pm.ResolveInfo r26, boolean r27, int r28) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r24 = this;
        r24.<init>();
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.mBannerResourceFileName = r0;
        r0 = r26;
        r0 = r0.serviceInfo;
        r19 = r0;
        r14 = 0;
        r10 = 0;
        if (r27 == 0) goto L_0x0052;
    L_0x0015:
        r21 = "android.nfc.cardemulation.host_apdu_service";
        r0 = r19;
        r1 = r25;
        r2 = r21;
        r14 = r0.loadXmlMetaData(r1, r2);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r14 != 0) goto L_0x0092;
    L_0x0023:
        r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = "No android.nfc.cardemulation.host_apdu_service meta-data";
        r21.<init>(r22);	 Catch:{ NameNotFoundException -> 0x002b }
        throw r21;	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x002b:
        r8 = move-exception;
        r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ all -> 0x004b }
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004b }
        r22.<init>();	 Catch:{ all -> 0x004b }
        r23 = "Unable to create context for: ";
        r22 = r22.append(r23);	 Catch:{ all -> 0x004b }
        r0 = r19;
        r0 = r0.packageName;	 Catch:{ all -> 0x004b }
        r23 = r0;
        r22 = r22.append(r23);	 Catch:{ all -> 0x004b }
        r22 = r22.toString();	 Catch:{ all -> 0x004b }
        r21.<init>(r22);	 Catch:{ all -> 0x004b }
        throw r21;	 Catch:{ all -> 0x004b }
    L_0x004b:
        r21 = move-exception;
        if (r14 == 0) goto L_0x0051;
    L_0x004e:
        r14.close();
    L_0x0051:
        throw r21;
    L_0x0052:
        r21 = "android.nfc.cardemulation.off_host_apdu_service";
        r0 = r19;
        r1 = r25;
        r2 = r21;
        r14 = r0.loadXmlMetaData(r1, r2);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r14 != 0) goto L_0x0068;
    L_0x0060:
        r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = "No android.nfc.cardemulation.off_host_apdu_service meta-data";
        r21.<init>(r22);	 Catch:{ NameNotFoundException -> 0x002b }
        throw r21;	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x0068:
        r21 = "com.gsma.nfc.services";
        r0 = r19;
        r1 = r25;
        r2 = r21;
        r10 = r0.loadXmlMetaData(r1, r2);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r10 != 0) goto L_0x0092;
    L_0x0076:
        r21 = "ApduServiceInfo";
        r22 = "No com.gsma.nfc.services meta-data";
        android.util.Log.d(r21, r22);	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = "android.nfc.cardemulation.se_extensions";
        r0 = r19;
        r1 = r25;
        r2 = r21;
        r10 = r0.loadXmlMetaData(r1, r2);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r10 != 0) goto L_0x0092;
    L_0x008b:
        r21 = "ApduServiceInfo";
        r22 = "No android.nfc.cardemulation.se_extensions meta-data";
        android.util.Log.d(r21, r22);	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x0092:
        r9 = r14.getEventType();	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x0096:
        r21 = 2;
        r0 = r21;
        if (r9 == r0) goto L_0x00a7;
    L_0x009c:
        r21 = 1;
        r0 = r21;
        if (r9 == r0) goto L_0x00a7;
    L_0x00a2:
        r9 = r14.next();	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x0096;
    L_0x00a7:
        r20 = r14.getName();	 Catch:{ NameNotFoundException -> 0x002b }
        if (r27 == 0) goto L_0x00c2;
    L_0x00ad:
        r21 = "host-apdu-service";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 != 0) goto L_0x00c2;
    L_0x00ba:
        r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = "Meta-data does not start with <host-apdu-service> tag";
        r21.<init>(r22);	 Catch:{ NameNotFoundException -> 0x002b }
        throw r21;	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x00c2:
        if (r27 != 0) goto L_0x00d9;
    L_0x00c4:
        r21 = "offhost-apdu-service";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 != 0) goto L_0x00d9;
    L_0x00d1:
        r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = "Meta-data does not start with <offhost-apdu-service> tag";
        r21.<init>(r22);	 Catch:{ NameNotFoundException -> 0x002b }
        throw r21;	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x00d9:
        r0 = r19;
        r0 = r0.applicationInfo;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r25;
        r1 = r21;
        r15 = r0.getResourcesForApplication(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        r5 = android.util.Xml.asAttributeSet(r14);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r27 == 0) goto L_0x0206;
    L_0x00ed:
        r21 = com.android.internal.R.styleable.HostApduService;	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r16 = r15.obtainAttributes(r5, r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r26;
        r1 = r24;
        r1.mService = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 0;
        r0 = r16;
        r1 = r21;
        r21 = r0.getString(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mDescription = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 2;
        r22 = 0;
        r0 = r16;
        r1 = r21;
        r2 = r22;
        r21 = r0.getBoolean(r1, r2);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mRequiresDeviceUnlock = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 3;
        r22 = -1;
        r0 = r16;
        r1 = r21;
        r2 = r22;
        r21 = r0.getResourceId(r1, r2);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mBannerResourceId = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 1;
        r0 = r16;
        r1 = r21;
        r21 = r0.getString(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mSettingsActivityName = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r16.recycle();	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x0146:
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.mBannerResourceFileName = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = new java.util.HashMap;	 Catch:{ NameNotFoundException -> 0x002b }
        r21.<init>();	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mStaticAidGroups = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = new java.util.HashMap;	 Catch:{ NameNotFoundException -> 0x002b }
        r21.<init>();	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mDynamicAidGroups = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r27;
        r1 = r24;
        r1.mOnHost = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r7 = r14.getDepth();	 Catch:{ NameNotFoundException -> 0x002b }
        r6 = 0;
    L_0x016f:
        r9 = r14.next();	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 3;
        r0 = r21;
        if (r9 != r0) goto L_0x0181;
    L_0x0179:
        r21 = r14.getDepth();	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        if (r0 <= r7) goto L_0x0373;
    L_0x0181:
        r21 = 1;
        r0 = r21;
        if (r9 == r0) goto L_0x0373;
    L_0x0187:
        r20 = r14.getName();	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 2;
        r0 = r21;
        if (r9 != r0) goto L_0x025b;
    L_0x0191:
        r21 = "aid-group";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 == 0) goto L_0x025b;
    L_0x019d:
        if (r6 != 0) goto L_0x025b;
    L_0x019f:
        r21 = com.android.internal.R.styleable.AidGroup;	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r11 = r15.obtainAttributes(r5, r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 1;
        r0 = r21;
        r12 = r11.getString(r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 0;
        r0 = r21;
        r13 = r11.getString(r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = "payment";
        r0 = r21;
        r21 = r0.equals(r12);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 != 0) goto L_0x01c5;
    L_0x01c2:
        r12 = "other";
    L_0x01c5:
        r0 = r24;
        r0 = r0.mStaticAidGroups;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r21;
        r6 = r0.get(r12);	 Catch:{ NameNotFoundException -> 0x002b }
        r6 = (android.nfc.cardemulation.AidGroup) r6;	 Catch:{ NameNotFoundException -> 0x002b }
        if (r6 == 0) goto L_0x0255;
    L_0x01d5:
        r21 = "other";
        r0 = r21;
        r21 = r0.equals(r12);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 != 0) goto L_0x0201;
    L_0x01e0:
        r21 = "ApduServiceInfo";
        r22 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x002b }
        r22.<init>();	 Catch:{ NameNotFoundException -> 0x002b }
        r23 = "Not allowing multiple aid-groups in the ";
        r22 = r22.append(r23);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r22;
        r22 = r0.append(r12);	 Catch:{ NameNotFoundException -> 0x002b }
        r23 = " category";
        r22 = r22.append(r23);	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = r22.toString();	 Catch:{ NameNotFoundException -> 0x002b }
        android.util.Log.e(r21, r22);	 Catch:{ NameNotFoundException -> 0x002b }
        r6 = 0;
    L_0x0201:
        r11.recycle();	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x016f;
    L_0x0206:
        r21 = com.android.internal.R.styleable.OffHostApduService;	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r16 = r15.obtainAttributes(r5, r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r26;
        r1 = r24;
        r1.mService = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 0;
        r0 = r16;
        r1 = r21;
        r21 = r0.getString(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mDescription = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.mRequiresDeviceUnlock = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 2;
        r22 = -1;
        r0 = r16;
        r1 = r21;
        r2 = r22;
        r21 = r0.getResourceId(r1, r2);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mBannerResourceId = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 1;
        r0 = r16;
        r1 = r21;
        r21 = r0.getString(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r1 = r24;
        r1.mSettingsActivityName = r0;	 Catch:{ NameNotFoundException -> 0x002b }
        r16.recycle();	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x0146;
    L_0x0255:
        r6 = new android.nfc.cardemulation.AidGroup;	 Catch:{ NameNotFoundException -> 0x002b }
        r6.<init>(r12, r13);	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x0201;
    L_0x025b:
        r21 = 3;
        r0 = r21;
        if (r9 != r0) goto L_0x02a5;
    L_0x0261:
        r21 = "aid-group";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 == 0) goto L_0x02a5;
    L_0x026d:
        if (r6 == 0) goto L_0x02a5;
    L_0x026f:
        r0 = r6.aids;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r21 = r21.size();	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 <= 0) goto L_0x029d;
    L_0x0279:
        r0 = r24;
        r0 = r0.mStaticAidGroups;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r6.category;	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = r0;
        r21 = r21.containsKey(r22);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 != 0) goto L_0x029a;
    L_0x0289:
        r0 = r24;
        r0 = r0.mStaticAidGroups;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r6.category;	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = r0;
        r0 = r21;
        r1 = r22;
        r0.put(r1, r6);	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x029a:
        r6 = 0;
        goto L_0x016f;
    L_0x029d:
        r21 = "ApduServiceInfo";
        r22 = "Not adding <aid-group> with empty or invalid AIDs";
        android.util.Log.e(r21, r22);	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x029a;
    L_0x02a5:
        r21 = 2;
        r0 = r21;
        if (r9 != r0) goto L_0x0308;
    L_0x02ab:
        r21 = "aid-filter";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 == 0) goto L_0x0308;
    L_0x02b7:
        if (r6 == 0) goto L_0x0308;
    L_0x02b9:
        r21 = com.android.internal.R.styleable.AidFilter;	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r3 = r15.obtainAttributes(r5, r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 0;
        r0 = r21;
        r21 = r3.getString(r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r4 = r21.toUpperCase();	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = android.nfc.cardemulation.CardEmulation.isValidAid(r4);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 == 0) goto L_0x02ed;
    L_0x02d3:
        r0 = r6.aids;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r21;
        r21 = r0.contains(r4);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 != 0) goto L_0x02ed;
    L_0x02df:
        r0 = r6.aids;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r21;
        r0.add(r4);	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x02e8:
        r3.recycle();	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x016f;
    L_0x02ed:
        r21 = "ApduServiceInfo";
        r22 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x002b }
        r22.<init>();	 Catch:{ NameNotFoundException -> 0x002b }
        r23 = "Ignoring invalid or duplicate aid: ";
        r22 = r22.append(r23);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r22;
        r22 = r0.append(r4);	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = r22.toString();	 Catch:{ NameNotFoundException -> 0x002b }
        android.util.Log.e(r21, r22);	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x02e8;
    L_0x0308:
        r21 = 2;
        r0 = r21;
        if (r9 != r0) goto L_0x016f;
    L_0x030e:
        r21 = "aid-prefix-filter";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 == 0) goto L_0x016f;
    L_0x031a:
        if (r6 == 0) goto L_0x016f;
    L_0x031c:
        r21 = com.android.internal.R.styleable.AidFilter;	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r21;
        r3 = r15.obtainAttributes(r5, r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = 0;
        r0 = r21;
        r21 = r3.getString(r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r4 = r21.toUpperCase();	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = "*";
        r0 = r21;
        r4 = r4.concat(r0);	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = android.nfc.cardemulation.CardEmulation.isValidAid(r4);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 == 0) goto L_0x0358;
    L_0x033e:
        r0 = r6.aids;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r21;
        r21 = r0.contains(r4);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r21 != 0) goto L_0x0358;
    L_0x034a:
        r0 = r6.aids;	 Catch:{ NameNotFoundException -> 0x002b }
        r21 = r0;
        r0 = r21;
        r0.add(r4);	 Catch:{ NameNotFoundException -> 0x002b }
    L_0x0353:
        r3.recycle();	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x016f;
    L_0x0358:
        r21 = "ApduServiceInfo";
        r22 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x002b }
        r22.<init>();	 Catch:{ NameNotFoundException -> 0x002b }
        r23 = "Ignoring invalid or duplicate aid: ";
        r22 = r22.append(r23);	 Catch:{ NameNotFoundException -> 0x002b }
        r0 = r22;
        r22 = r0.append(r4);	 Catch:{ NameNotFoundException -> 0x002b }
        r22 = r22.toString();	 Catch:{ NameNotFoundException -> 0x002b }
        android.util.Log.e(r21, r22);	 Catch:{ NameNotFoundException -> 0x002b }
        goto L_0x0353;
    L_0x0373:
        if (r14 == 0) goto L_0x0378;
    L_0x0375:
        r14.close();
    L_0x0378:
        r0 = r19;
        r0 = r0.applicationInfo;
        r21 = r0;
        r0 = r21;
        r0 = r0.uid;
        r21 = r0;
        r0 = r21;
        r1 = r24;
        r1.mUid = r0;
        r21 = 1;
        r0 = r21;
        r1 = r24;
        r1.mOtherServiceSelectionState = r0;
        r18 = 0;
        r17 = 0;
        if (r10 == 0) goto L_0x0459;
    L_0x0398:
        r9 = r10.getEventType();	 Catch:{ all -> 0x03de }
        r7 = r10.getDepth();	 Catch:{ all -> 0x03de }
    L_0x03a0:
        r21 = 2;
        r0 = r21;
        if (r9 == r0) goto L_0x03b1;
    L_0x03a6:
        r21 = 1;
        r0 = r21;
        if (r9 == r0) goto L_0x03b1;
    L_0x03ac:
        r9 = r10.next();	 Catch:{ all -> 0x03de }
        goto L_0x03a0;
    L_0x03b1:
        r20 = r10.getName();	 Catch:{ all -> 0x03de }
        r21 = "extensions";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ all -> 0x03de }
        if (r21 != 0) goto L_0x03e3;
    L_0x03c1:
        r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ all -> 0x03de }
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03de }
        r22.<init>();	 Catch:{ all -> 0x03de }
        r23 = "Meta-data does not start with <extensions> tag ";
        r22 = r22.append(r23);	 Catch:{ all -> 0x03de }
        r0 = r22;
        r1 = r20;
        r22 = r0.append(r1);	 Catch:{ all -> 0x03de }
        r22 = r22.toString();	 Catch:{ all -> 0x03de }
        r21.<init>(r22);	 Catch:{ all -> 0x03de }
        throw r21;	 Catch:{ all -> 0x03de }
    L_0x03de:
        r21 = move-exception;
        r10.close();
        throw r21;
    L_0x03e3:
        r9 = r10.next();	 Catch:{ all -> 0x03de }
        r21 = 3;
        r0 = r21;
        if (r9 != r0) goto L_0x03f5;
    L_0x03ed:
        r21 = r10.getDepth();	 Catch:{ all -> 0x03de }
        r0 = r21;
        if (r0 <= r7) goto L_0x0456;
    L_0x03f5:
        r21 = 1;
        r0 = r21;
        if (r9 == r0) goto L_0x0456;
    L_0x03fb:
        r20 = r10.getName();	 Catch:{ all -> 0x03de }
        r21 = 2;
        r0 = r21;
        if (r9 != r0) goto L_0x03e3;
    L_0x0405:
        r21 = "se-id";
        r0 = r21;
        r1 = r20;
        r21 = r0.equals(r1);	 Catch:{ all -> 0x03de }
        if (r21 == 0) goto L_0x03e3;
    L_0x0412:
        r21 = 0;
        r22 = "name";
        r0 = r21;
        r1 = r22;
        r18 = r10.getAttributeValue(r0, r1);	 Catch:{ all -> 0x03de }
        if (r18 == 0) goto L_0x0439;
    L_0x0421:
        r21 = "eSE";
        r0 = r18;
        r1 = r21;
        r21 = r0.equalsIgnoreCase(r1);	 Catch:{ all -> 0x03de }
        if (r21 != 0) goto L_0x03e3;
    L_0x042d:
        r21 = "UICC";
        r0 = r18;
        r1 = r21;
        r21 = r0.equalsIgnoreCase(r1);	 Catch:{ all -> 0x03de }
        if (r21 != 0) goto L_0x03e3;
    L_0x0439:
        r21 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ all -> 0x03de }
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03de }
        r22.<init>();	 Catch:{ all -> 0x03de }
        r23 = "Unsupported se name: ";
        r22 = r22.append(r23);	 Catch:{ all -> 0x03de }
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);	 Catch:{ all -> 0x03de }
        r22 = r22.toString();	 Catch:{ all -> 0x03de }
        r21.<init>(r22);	 Catch:{ all -> 0x03de }
        throw r21;	 Catch:{ all -> 0x03de }
    L_0x0456:
        r10.close();
    L_0x0459:
        if (r27 == 0) goto L_0x047c;
    L_0x045b:
        r17 = 0;
    L_0x045d:
        r21 = new android.nfc.cardemulation.ApduServiceInfo$SecureElementInfo;
        r0 = r21;
        r1 = r17;
        r0.<init>(r1);
        r0 = r21;
        r1 = r24;
        r1.mSeInfo = r0;
        r21 = "ApduServiceInfo";
        r0 = r24;
        r0 = r0.mSeInfo;
        r22 = r0;
        r22 = r22.toString();
        android.util.Log.d(r21, r22);
        return;
    L_0x047c:
        if (r28 == 0) goto L_0x0481;
    L_0x047e:
        r17 = r28;
        goto L_0x045d;
    L_0x0481:
        if (r18 == 0) goto L_0x0495;
    L_0x0483:
        r21 = "eSE";
        r0 = r18;
        r1 = r21;
        r21 = r0.equalsIgnoreCase(r1);
        if (r21 == 0) goto L_0x0492;
    L_0x048f:
        r17 = 1;
    L_0x0491:
        goto L_0x045d;
    L_0x0492:
        r17 = 2;
        goto L_0x0491;
    L_0x0495:
        r17 = 2;
        goto L_0x045d;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.cardemulation.ApduServiceInfo.<init>(android.content.pm.PackageManager, android.content.pm.ResolveInfo, boolean, int):void");
    }

    public ComponentName getComponent() {
        if (this.mService == null) {
            Log.e(TAG, "info is null");
            return null;
        } else if (this.mService.serviceInfo != null) {
            return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
        } else {
            Log.e(TAG, "serviceInfo is null");
            return null;
        }
    }

    public List<String> getAids() {
        ArrayList<String> aids = new ArrayList();
        Iterator i$ = getAidGroups().iterator();
        while (i$.hasNext()) {
            aids.addAll(((AidGroup) i$.next()).aids);
        }
        return aids;
    }

    public List<String> getPrefixAids() {
        ArrayList<String> prefixAids = new ArrayList();
        Iterator it = getAidGroups().iterator();
        while (it.hasNext()) {
            for (String aid : ((AidGroup) it.next()).aids) {
                if (aid.endsWith("*")) {
                    prefixAids.add(aid);
                }
            }
        }
        return prefixAids;
    }

    public AidGroup getDynamicAidGroupForCategory(String category) {
        return (AidGroup) this.mDynamicAidGroups.get(category);
    }

    public boolean removeDynamicAidGroupForCategory(String category) {
        return this.mDynamicAidGroups.remove(category) != null;
    }

    public ArrayList<AidGroup> getAidGroups() {
        ArrayList<AidGroup> groups = new ArrayList();
        for (Entry<String, AidGroup> entry : this.mDynamicAidGroups.entrySet()) {
            groups.add(entry.getValue());
        }
        for (Entry<String, AidGroup> entry2 : this.mStaticAidGroups.entrySet()) {
            if (!this.mDynamicAidGroups.containsKey(entry2.getKey())) {
                groups.add(entry2.getValue());
            }
        }
        return groups;
    }

    public String getCategoryForAid(String aid) {
        Iterator i$ = getAidGroups().iterator();
        while (i$.hasNext()) {
            AidGroup group = (AidGroup) i$.next();
            if (group.aids.contains(aid.toUpperCase())) {
                return group.category;
            }
        }
        return null;
    }

    public boolean hasCategory(String category) {
        return this.mStaticAidGroups.containsKey(category) || this.mDynamicAidGroups.containsKey(category);
    }

    public boolean isOnHost() {
        return this.mOnHost;
    }

    public boolean requiresUnlock() {
        return this.mRequiresDeviceUnlock;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public int getUid() {
        return this.mUid;
    }

    public void setOrReplaceDynamicAidGroup(AidGroup aidGroup) {
        this.mDynamicAidGroups.put(aidGroup.getCategory(), aidGroup);
    }

    public CharSequence loadLabel(PackageManager pm) {
        if (this.mService != null) {
            return this.mService.loadLabel(pm);
        }
        Log.e(TAG, "label is null");
        return null;
    }

    public CharSequence loadAppLabel(PackageManager pm) {
        try {
            return pm.getApplicationLabel(pm.getApplicationInfo(this.mService.resolvePackageName, 128));
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public Drawable loadIcon(PackageManager pm) {
        if (this.mService != null) {
            return this.mService.loadIcon(pm);
        }
        Log.e(TAG, "icon is null");
        return null;
    }

    public Drawable loadBanner(PackageManager pm) {
        try {
            if (this.mBannerResourceId != -1) {
                return pm.getResourcesForApplication(this.mService.serviceInfo.packageName).getDrawable(this.mBannerResourceId);
            }
            Log.d(TAG, "use drawable banner");
            return Drawable.createFromPath("/data/data/com.android.nfc/files/" + this.mBannerResourceFileName);
        } catch (NotFoundException e) {
            Log.e(TAG, "Could not load banner.");
            return null;
        } catch (NameNotFoundException e2) {
            Log.e(TAG, "Could not load banner.");
            return null;
        } catch (OutOfMemoryError e3) {
            Log.e(TAG, "Could not load banner.");
            return null;
        }
    }

    public String getSettingsActivityName() {
        return this.mSettingsActivityName;
    }

    public String toString() {
        StringBuilder out = new StringBuilder("ApduService: ");
        out.append(getComponent());
        out.append(", description: " + this.mDescription);
        out.append(", Static AID Groups: ");
        for (AidGroup aidGroup : this.mStaticAidGroups.values()) {
            out.append(aidGroup.toString());
        }
        out.append(", Dynamic AID Groups: ");
        for (AidGroup aidGroup2 : this.mDynamicAidGroups.values()) {
            out.append(aidGroup2.toString());
        }
        return out.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ApduServiceInfo) {
            return ((ApduServiceInfo) o).getComponent().equals(getComponent());
        }
        return false;
    }

    public int hashCode() {
        return getComponent().hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        this.mService.writeToParcel(dest, flags);
        dest.writeString(this.mDescription);
        dest.writeInt(this.mOnHost ? 1 : 0);
        dest.writeInt(this.mStaticAidGroups.size());
        if (this.mStaticAidGroups.size() > 0) {
            dest.writeTypedList(new ArrayList(this.mStaticAidGroups.values()));
        }
        dest.writeInt(this.mDynamicAidGroups.size());
        if (this.mDynamicAidGroups.size() > 0) {
            dest.writeTypedList(new ArrayList(this.mDynamicAidGroups.values()));
        }
        if (this.mRequiresDeviceUnlock) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.mBannerResourceId);
        dest.writeInt(this.mUid);
        dest.writeString(this.mSettingsActivityName);
        if (this.mSeInfo != null) {
            this.mSeInfo.writeToParcel(dest, flags);
        }
        dest.writeString(this.mBannerResourceFileName);
        if (!this.mOtherServiceSelectionState) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("    " + getComponent() + " (Description: " + getDescription() + ")");
        pw.println("    Static AID groups:");
        for (AidGroup group : this.mStaticAidGroups.values()) {
            pw.println("        Category: " + group.category);
            for (String aid : group.aids) {
                pw.println("            AID: " + aid);
            }
        }
        pw.println("    Dynamic AID groups:");
        for (AidGroup group2 : this.mDynamicAidGroups.values()) {
            pw.println("        Category: " + group2.category);
            for (String aid2 : group2.aids) {
                pw.println("            AID: " + aid2);
            }
        }
        pw.println("    Settings Activity: " + this.mSettingsActivityName);
    }

    public void setOtherServiceState(boolean selected) {
        this.mOtherServiceSelectionState = selected;
    }

    public boolean isSelectedOtherService() {
        return this.mOtherServiceSelectionState;
    }

    public SecureElementInfo getSEInfo() {
        return this.mSeInfo;
    }
}
