package android.media.tv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.net.Uri;
import android.os.FileObserver;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public final class TvInputInfo implements Parcelable {
    public static final Creator<TvInputInfo> CREATOR = new Creator<TvInputInfo>() {
        public TvInputInfo createFromParcel(Parcel in) {
            return new TvInputInfo(in);
        }

        public TvInputInfo[] newArray(int size) {
            return new TvInputInfo[size];
        }
    };
    private static final boolean DEBUG = false;
    private static final String DELIMITER_INFO_IN_ID = "/";
    public static final String EXTRA_INPUT_ID = "android.media.tv.extra.INPUT_ID";
    private static final int LENGTH_HDMI_DEVICE_ID = 2;
    private static final int LENGTH_HDMI_PHYSICAL_ADDRESS = 4;
    private static final String PREFIX_HARDWARE_DEVICE = "HW";
    private static final String PREFIX_HDMI_DEVICE = "HDMI";
    private static final String TAG = "TvInputInfo";
    public static final int TYPE_COMPONENT = 1004;
    public static final int TYPE_COMPOSITE = 1001;
    public static final int TYPE_DISPLAY_PORT = 1008;
    public static final int TYPE_DVI = 1006;
    public static final int TYPE_HDMI = 1007;
    public static final int TYPE_OTHER = 1000;
    public static final int TYPE_SCART = 1003;
    public static final int TYPE_SVIDEO = 1002;
    public static final int TYPE_TUNER = 0;
    public static final int TYPE_VGA = 1005;
    private static final String XML_START_TAG_NAME = "tv-input";
    private static final SparseIntArray sHardwareTypeToTvInputType = new SparseIntArray();
    private HdmiDeviceInfo mHdmiDeviceInfo;
    private Icon mIcon;
    private Uri mIconUri;
    private final String mId;
    private boolean mIsConnectedToHdmiSwitch;
    private final boolean mIsHardwareInput;
    private String mLabel;
    private int mLabelRes;
    private final String mParentId;
    private final ResolveInfo mService;
    private String mSettingsActivity;
    private String mSetupActivity;
    private final int mType;

    public static final class TvInputSettings {
        private static final String CUSTOM_NAME_SEPARATOR = ",";
        private static final String TV_INPUT_SEPARATOR = ":";

        private TvInputSettings() {
        }

        private static boolean isHidden(Context context, String inputId, int userId) {
            return getHiddenTvInputIds(context, userId).contains(inputId);
        }

        private static String getCustomLabel(Context context, String inputId, int userId) {
            return (String) getCustomLabels(context, userId).get(inputId);
        }

        public static Set<String> getHiddenTvInputIds(Context context, int userId) {
            String hiddenIdsString = Secure.getStringForUser(context.getContentResolver(), Secure.TV_INPUT_HIDDEN_INPUTS, userId);
            Set<String> set = new HashSet();
            if (!TextUtils.isEmpty(hiddenIdsString)) {
                for (String id : hiddenIdsString.split(TV_INPUT_SEPARATOR)) {
                    set.add(Uri.decode(id));
                }
            }
            return set;
        }

        public static Map<String, String> getCustomLabels(Context context, int userId) {
            String labelsString = Secure.getStringForUser(context.getContentResolver(), Secure.TV_INPUT_CUSTOM_LABELS, userId);
            Map<String, String> map = new HashMap();
            if (!TextUtils.isEmpty(labelsString)) {
                for (String pairString : labelsString.split(TV_INPUT_SEPARATOR)) {
                    String[] pair = pairString.split(CUSTOM_NAME_SEPARATOR);
                    map.put(Uri.decode(pair[0]), Uri.decode(pair[1]));
                }
            }
            return map;
        }

        public static void putHiddenTvInputs(Context context, Set<String> hiddenInputIds, int userId) {
            StringBuilder builder = new StringBuilder();
            boolean firstItem = true;
            for (String inputId : hiddenInputIds) {
                ensureValidField(inputId);
                if (firstItem) {
                    firstItem = false;
                } else {
                    builder.append(TV_INPUT_SEPARATOR);
                }
                builder.append(Uri.encode(inputId));
            }
            Secure.putStringForUser(context.getContentResolver(), Secure.TV_INPUT_HIDDEN_INPUTS, builder.toString(), userId);
        }

        public static void putCustomLabels(Context context, Map<String, String> customLabels, int userId) {
            StringBuilder builder = new StringBuilder();
            boolean firstItem = true;
            for (Entry<String, String> entry : customLabels.entrySet()) {
                ensureValidField((String) entry.getKey());
                ensureValidField((String) entry.getValue());
                if (firstItem) {
                    firstItem = false;
                } else {
                    builder.append(TV_INPUT_SEPARATOR);
                }
                builder.append(Uri.encode((String) entry.getKey()));
                builder.append(CUSTOM_NAME_SEPARATOR);
                builder.append(Uri.encode((String) entry.getValue()));
            }
            Secure.putStringForUser(context.getContentResolver(), Secure.TV_INPUT_CUSTOM_LABELS, builder.toString(), userId);
        }

        private static void ensureValidField(String value) {
            if (TextUtils.isEmpty(value)) {
                throw new IllegalArgumentException(value + " should not empty ");
            }
        }
    }

    static {
        sHardwareTypeToTvInputType.put(1, 1000);
        sHardwareTypeToTvInputType.put(2, 0);
        sHardwareTypeToTvInputType.put(3, 1001);
        sHardwareTypeToTvInputType.put(4, 1002);
        sHardwareTypeToTvInputType.put(5, 1003);
        sHardwareTypeToTvInputType.put(6, 1004);
        sHardwareTypeToTvInputType.put(7, 1005);
        sHardwareTypeToTvInputType.put(8, 1006);
        sHardwareTypeToTvInputType.put(9, 1007);
        sHardwareTypeToTvInputType.put(10, 1008);
    }

    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service) throws XmlPullParserException, IOException {
        return createTvInputInfo(context, service, generateInputIdForComponentName(new ComponentName(service.serviceInfo.packageName, service.serviceInfo.name)), null, 0, false, 0, null, null, null, false);
    }

    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, HdmiDeviceInfo hdmiDeviceInfo, String parentId, String label, Uri iconUri) throws XmlPullParserException, IOException {
        TvInputInfo input = createTvInputInfo(context, service, generateInputIdForHdmiDevice(new ComponentName(service.serviceInfo.packageName, service.serviceInfo.name), hdmiDeviceInfo), parentId, 1007, true, 0, label, null, iconUri, (hdmiDeviceInfo.getPhysicalAddress() & FileObserver.ALL_EVENTS) != 0);
        input.mHdmiDeviceInfo = hdmiDeviceInfo;
        return input;
    }

    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, HdmiDeviceInfo hdmiDeviceInfo, String parentId, int labelRes, Icon icon) throws XmlPullParserException, IOException {
        TvInputInfo input = createTvInputInfo(context, service, generateInputIdForHdmiDevice(new ComponentName(service.serviceInfo.packageName, service.serviceInfo.name), hdmiDeviceInfo), parentId, 1007, true, labelRes, null, icon, null, (hdmiDeviceInfo.getPhysicalAddress() & FileObserver.ALL_EVENTS) != 0);
        input.mHdmiDeviceInfo = hdmiDeviceInfo;
        return input;
    }

    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, TvInputHardwareInfo hardwareInfo, String label, Uri iconUri) throws XmlPullParserException, IOException {
        int inputType = sHardwareTypeToTvInputType.get(hardwareInfo.getType(), 0);
        return createTvInputInfo(context, service, generateInputIdForHardware(new ComponentName(service.serviceInfo.packageName, service.serviceInfo.name), hardwareInfo), null, inputType, true, 0, label, null, iconUri, false);
    }

    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, TvInputHardwareInfo hardwareInfo, int labelRes, Icon icon) throws XmlPullParserException, IOException {
        int inputType = sHardwareTypeToTvInputType.get(hardwareInfo.getType(), 0);
        return createTvInputInfo(context, service, generateInputIdForHardware(new ComponentName(service.serviceInfo.packageName, service.serviceInfo.name), hardwareInfo), null, inputType, true, labelRes, null, icon, null, false);
    }

    private static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, String id, String parentId, int inputType, boolean isHardwareInput, int labelRes, String label, Icon icon, Uri iconUri, boolean isConnectedToHdmiSwitch) throws XmlPullParserException, IOException {
        ServiceInfo si = service.serviceInfo;
        PackageManager pm = context.getPackageManager();
        XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(pm, TvInputService.SERVICE_META_DATA);
            if (parser == null) {
                throw new XmlPullParserException("No android.media.tv.input meta-data for " + si.name);
            }
            Resources res = pm.getResourcesForApplication(si.applicationInfo);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            int type;
            do {
                type = parser.next();
                if (type == 1) {
                    break;
                }
            } while (type != 2);
            if (XML_START_TAG_NAME.equals(parser.getName())) {
                TvInputInfo input = new TvInputInfo(service, id, parentId, inputType, isHardwareInput);
                TypedArray sa = res.obtainAttributes(attrs, R.styleable.TvInputService);
                input.mSetupActivity = sa.getString(1);
                if (inputType == 0 && TextUtils.isEmpty(input.mSetupActivity)) {
                    throw new XmlPullParserException("Setup activity not found in " + si.name);
                }
                input.mSettingsActivity = sa.getString(0);
                sa.recycle();
                input.mLabelRes = labelRes;
                input.mLabel = label;
                input.mIcon = icon;
                input.mIconUri = iconUri;
                input.mIsConnectedToHdmiSwitch = isConnectedToHdmiSwitch;
                if (parser != null) {
                    parser.close();
                }
                return input;
            }
            throw new XmlPullParserException("Meta-data does not start with tv-input-service tag in " + si.name);
        } catch (NameNotFoundException e) {
            throw new XmlPullParserException("Unable to create context for: " + si.packageName);
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private TvInputInfo(ResolveInfo service, String id, String parentId, int type, boolean isHardwareInput) {
        this.mService = service;
        this.mId = id;
        this.mParentId = parentId;
        this.mType = type;
        this.mIsHardwareInput = isHardwareInput;
    }

    public String getId() {
        return this.mId;
    }

    public String getParentId() {
        return this.mParentId;
    }

    public ServiceInfo getServiceInfo() {
        return this.mService.serviceInfo;
    }

    public ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public Intent createSetupIntent() {
        if (TextUtils.isEmpty(this.mSetupActivity)) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(this.mService.serviceInfo.packageName, this.mSetupActivity);
        intent.putExtra(EXTRA_INPUT_ID, getId());
        return intent;
    }

    public Intent createSettingsIntent() {
        if (TextUtils.isEmpty(this.mSettingsActivity)) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(this.mService.serviceInfo.packageName, this.mSettingsActivity);
        intent.putExtra(EXTRA_INPUT_ID, getId());
        return intent;
    }

    public int getType() {
        return this.mType;
    }

    public HdmiDeviceInfo getHdmiDeviceInfo() {
        if (this.mType == 1007) {
            return this.mHdmiDeviceInfo;
        }
        return null;
    }

    public boolean isPassthroughInput() {
        return this.mType != 0;
    }

    public boolean isHardwareInput() {
        return this.mIsHardwareInput;
    }

    public boolean isConnectedToHdmiSwitch() {
        return this.mIsConnectedToHdmiSwitch;
    }

    public boolean isHidden(Context context) {
        return TvInputSettings.isHidden(context, this.mId, UserHandle.myUserId());
    }

    public CharSequence loadLabel(Context context) {
        if (this.mLabelRes != 0) {
            return context.getPackageManager().getText(this.mService.serviceInfo.packageName, this.mLabelRes, null);
        }
        if (TextUtils.isEmpty(this.mLabel)) {
            return this.mService.loadLabel(context.getPackageManager());
        }
        return this.mLabel;
    }

    public CharSequence loadCustomLabel(Context context) {
        return TvInputSettings.getCustomLabel(context, this.mId, UserHandle.myUserId());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable loadIcon(android.content.Context r8) {
        /*
        r7 = this;
        r5 = 0;
        r4 = r7.mIcon;
        if (r4 == 0) goto L_0x000c;
    L_0x0005:
        r4 = r7.mIcon;
        r0 = r4.loadDrawable(r8);
    L_0x000b:
        return r0;
    L_0x000c:
        r4 = r7.mIconUri;
        if (r4 == 0) goto L_0x004a;
    L_0x0010:
        r4 = r8.getContentResolver();	 Catch:{ IOException -> 0x002f }
        r6 = r7.mIconUri;	 Catch:{ IOException -> 0x002f }
        r2 = r4.openInputStream(r6);	 Catch:{ IOException -> 0x002f }
        r4 = 0;
        r6 = 0;
        r0 = android.graphics.drawable.Drawable.createFromStream(r2, r6);	 Catch:{ Throwable -> 0x0064 }
        if (r0 == 0) goto L_0x0053;
    L_0x0022:
        if (r2 == 0) goto L_0x000b;
    L_0x0024:
        if (r5 == 0) goto L_0x004f;
    L_0x0026:
        r2.close();	 Catch:{ Throwable -> 0x002a }
        goto L_0x000b;
    L_0x002a:
        r3 = move-exception;
        r4.addSuppressed(r3);	 Catch:{ IOException -> 0x002f }
        goto L_0x000b;
    L_0x002f:
        r1 = move-exception;
        r4 = "TvInputInfo";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Loading the default icon due to a failure on loading ";
        r5 = r5.append(r6);
        r6 = r7.mIconUri;
        r5 = r5.append(r6);
        r5 = r5.toString();
        android.util.Log.w(r4, r5, r1);
    L_0x004a:
        r0 = r7.loadServiceIcon(r8);
        goto L_0x000b;
    L_0x004f:
        r2.close();	 Catch:{ IOException -> 0x002f }
        goto L_0x000b;
    L_0x0053:
        if (r2 == 0) goto L_0x004a;
    L_0x0055:
        if (r5 == 0) goto L_0x0060;
    L_0x0057:
        r2.close();	 Catch:{ Throwable -> 0x005b }
        goto L_0x004a;
    L_0x005b:
        r3 = move-exception;
        r4.addSuppressed(r3);	 Catch:{ IOException -> 0x002f }
        goto L_0x004a;
    L_0x0060:
        r2.close();	 Catch:{ IOException -> 0x002f }
        goto L_0x004a;
    L_0x0064:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x0066 }
    L_0x0066:
        r4 = move-exception;
        if (r2 == 0) goto L_0x006e;
    L_0x0069:
        if (r5 == 0) goto L_0x0074;
    L_0x006b:
        r2.close();	 Catch:{ Throwable -> 0x006f }
    L_0x006e:
        throw r4;	 Catch:{ IOException -> 0x002f }
    L_0x006f:
        r3 = move-exception;
        r5.addSuppressed(r3);	 Catch:{ IOException -> 0x002f }
        goto L_0x006e;
    L_0x0074:
        r2.close();	 Catch:{ IOException -> 0x002f }
        goto L_0x006e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputInfo.loadIcon(android.content.Context):android.graphics.drawable.Drawable");
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        return this.mId.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TvInputInfo)) {
            return false;
        }
        return this.mId.equals(((TvInputInfo) o).mId);
    }

    public String toString() {
        return "TvInputInfo{id=" + this.mId + ", pkg=" + this.mService.serviceInfo.packageName + ", service=" + this.mService.serviceInfo.name + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b = (byte) 1;
        dest.writeString(this.mId);
        dest.writeString(this.mParentId);
        this.mService.writeToParcel(dest, flags);
        dest.writeString(this.mSetupActivity);
        dest.writeString(this.mSettingsActivity);
        dest.writeInt(this.mType);
        dest.writeByte(this.mIsHardwareInput ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.mHdmiDeviceInfo, flags);
        dest.writeParcelable(this.mIcon, flags);
        dest.writeParcelable(this.mIconUri, flags);
        dest.writeInt(this.mLabelRes);
        dest.writeString(this.mLabel);
        if (!this.mIsConnectedToHdmiSwitch) {
            b = (byte) 0;
        }
        dest.writeByte(b);
    }

    private Drawable loadServiceIcon(Context context) {
        if (this.mService.serviceInfo.icon == 0 && this.mService.serviceInfo.applicationInfo.icon == 0) {
            return null;
        }
        return this.mService.serviceInfo.loadIcon(context.getPackageManager());
    }

    private static String generateInputIdForComponentName(ComponentName name) {
        return name.flattenToShortString();
    }

    private static String generateInputIdForHdmiDevice(ComponentName name, HdmiDeviceInfo deviceInfo) {
        return name.flattenToShortString() + String.format(Locale.ENGLISH, "/HDMI%04X%02X", new Object[]{Integer.valueOf(deviceInfo.getPhysicalAddress()), Integer.valueOf(deviceInfo.getId())});
    }

    private static String generateInputIdForHardware(ComponentName name, TvInputHardwareInfo hardwareInfo) {
        return name.flattenToShortString() + DELIMITER_INFO_IN_ID + PREFIX_HARDWARE_DEVICE + hardwareInfo.getDeviceId();
    }

    private TvInputInfo(Parcel in) {
        boolean z = true;
        this.mId = in.readString();
        this.mParentId = in.readString();
        this.mService = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(in);
        this.mSetupActivity = in.readString();
        this.mSettingsActivity = in.readString();
        this.mType = in.readInt();
        this.mIsHardwareInput = in.readByte() == (byte) 1;
        this.mHdmiDeviceInfo = (HdmiDeviceInfo) in.readParcelable(null);
        this.mIcon = (Icon) in.readParcelable(null);
        this.mIconUri = (Uri) in.readParcelable(null);
        this.mLabelRes = in.readInt();
        this.mLabel = in.readString();
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        this.mIsConnectedToHdmiSwitch = z;
    }
}
