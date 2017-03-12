package com.samsung.android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.util.secutil.Log;
import com.android.internal.R;
import com.samsung.android.feature.FloatingFeature;
import com.sec.android.app.CscFeature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public final class ExecutableInfo implements Parcelable {
    private static final String CLASSNAME_PREFIX_FOR_SEC_PRODUCT_FEATURE = "SecProductFeature_";
    public static final Creator<ExecutableInfo> CREATOR = new Creator<ExecutableInfo>() {
        public ExecutableInfo createFromParcel(Parcel in) {
            return new ExecutableInfo(in);
        }

        public ExecutableInfo[] newArray(int size) {
            return new ExecutableInfo[size];
        }
    };
    private static final String CSC_FEATURE_PREFIX = "CscFeature_";
    private static final boolean DEBUG;
    private static final String LOG_TAG = "ExecutableInfo";
    private static final String MD_LABEL_EXECUTABLE = "com.samsung.android.support.executable";
    private static final int ORDER_INIT_VALUE = -9996;
    private static final int ORDER_INVALID_FORMAT = -9998;
    private static final int ORDER_NOT_ALLOWED = -9997;
    private static final int ORDER_OUT_OF_RANGE = -9999;
    private static final String PACKAGE_PREFIX_FOR_SEC_PRODUCT_FEATURE = "com.sec.android.app.";
    private static final String SEC_FLOATING_FEATURE_PREFIX = "SEC_FLOATING_FEATURE_";
    private static final String SEC_PRODUCT_FEATURE_PREFIX = "SEC_PRODUCT_FEATURE_";
    private static final String XML_ELEMENT_COMMAND = "command";
    private static final String XML_ELEMENT_ENABLED = "enabled";
    private static final String XML_ELEMENT_EXECUTABLE = "executable";
    private static final String XML_ELEMENT_EXTRA_ATTR = "extras-attr";
    private static final String XML_ELEMENT_EXTRA_ATTR_CATEGORY = "category";
    private static final String XML_ELEMENT_EXTRA_ATTR_COMPONENTNAME = "componentName";
    private static final String XML_ELEMENT_EXTRA_ATTR_EXTRAS = "extras";
    private static final String XML_ELEMENT_EXTRA_ATTR_FEATURE = "feature";
    private static final String XML_ELEMENT_EXTRA_ATTR_INTETNACTION = "action";
    private static final String XML_ELEMENT_EXTRA_ATTR_LAUCHMODE = "launchMode";
    private static final String XML_ELEMENT_EXTRA_ATTR_PACKAGENAME = "packageName";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE = "type";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY = "activity";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY_FOR_RESULT = "activityForResult";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_BROADCAST = "broadcast";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_SERVICE = "service";
    private static final String XML_ELEMENT_ICON = "icon";
    private static final String XML_ELEMENT_LABEL = "label";
    private static final String XML_ELEMENT_LAUCHMODE_CLEARTOP = "clearTop";
    private static final String XML_ELEMENT_LAUCHMODE_NEWTASK = "newTask";
    private static final String XML_ELEMENT_LAUCHMODE_SINGLETOP = "singleTop";
    private static final String XML_ELEMENT_SMALL_ICON = "smallIcon";
    String mAction;
    String mActivityLaunchMode;
    Bundle mBundle;
    String mCategory;
    String mComponentName;
    boolean mEnabled;
    List<String> mFeatureNames;
    List<String> mFeatureValues;
    int mIconId;
    int mLabelId;
    int mLaunchType;
    String mPackageName;
    int mSmallIconId;
    String mUid;

    static {
        boolean z;
        if (Debug.isProductShip() == 0) {
            z = true;
        } else {
            z = false;
        }
        DEBUG = z;
    }

    public ExecutableInfo() {
        this.mUid = null;
        this.mEnabled = false;
        this.mBundle = new Bundle();
        this.mFeatureNames = new ArrayList();
        this.mFeatureValues = new ArrayList();
    }

    ExecutableInfo(Parcel in) {
        this();
        this.mUid = in.readString();
        this.mEnabled = in.readInt() != 0;
        this.mLabelId = in.readInt();
        this.mIconId = in.readInt();
        this.mSmallIconId = in.readInt();
        this.mLaunchType = in.readInt();
        this.mCategory = in.readString();
        this.mAction = in.readString();
        this.mPackageName = in.readString();
        in.readStringList(this.mFeatureNames);
        in.readStringList(this.mFeatureValues);
        this.mBundle = in.readBundle();
        this.mComponentName = in.readString();
        this.mActivityLaunchMode = in.readString();
    }

    private void setId(String applicaitonPackageName) {
        Builder builder = new Builder();
        String id = getAction() + getPackageName() + getComponentName() + getLaunchType() + getBundleString();
        builder.scheme(XML_ELEMENT_EXECUTABLE).authority(applicaitonPackageName);
        builder.appendPath(String.valueOf(((long) id.hashCode()) & 4294967295L));
        this.mUid = builder.toString();
    }

    public String getId() {
        return this.mUid;
    }

    public List<String> getCategories() {
        if (this.mCategory == null || TextUtils.isEmpty(this.mCategory)) {
            return new ArrayList();
        }
        return Arrays.asList(this.mCategory.split("\\|"));
    }

    public String getAction() {
        return this.mAction;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public int getSmallIconId() {
        return this.mSmallIconId;
    }

    public int getLabelId() {
        return this.mLabelId;
    }

    public int getLaunchType() {
        return this.mLaunchType;
    }

    public Bundle getExtras() {
        return this.mBundle;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getComponentName() {
        return this.mComponentName;
    }

    public int getActivityLaunchMode() {
        int flags = 0;
        if (this.mActivityLaunchMode == null || this.mActivityLaunchMode.length() == 0) {
            return 0;
        }
        String[] modes = this.mActivityLaunchMode.split("\\|");
        for (String mode : modes) {
            if (XML_ELEMENT_LAUCHMODE_NEWTASK.equals(mode)) {
                flags |= 268435456;
            } else if (XML_ELEMENT_LAUCHMODE_SINGLETOP.equals(mode)) {
                flags |= 536870912;
            }
            if (XML_ELEMENT_LAUCHMODE_CLEARTOP.equals(mode)) {
                flags |= 67108864;
            }
        }
        return flags;
    }

    private static ExecutableInfo getActivityMetaData(Context context, AttributeSet attr, ComponentName cName) {
        ExecutableInfo result = new ExecutableInfo();
        Context activityContext = createActivityContext(context, cName);
        if (activityContext == null) {
            return null;
        }
        TypedArray a = activityContext.obtainStyledAttributes(attr, R.styleable.command);
        result.mEnabled = a.getBoolean(2, true);
        result.mLabelId = a.getResourceId(0, 0);
        result.mIconId = a.getResourceId(1, 0);
        result.mSmallIconId = a.getResourceId(3, 0);
        a.recycle();
        return result;
    }

    private void addExtraAttribute(Context activityContext, AttributeSet attr) {
        TypedArray ta = activityContext.obtainStyledAttributes(attr, R.styleable.extrasCommand);
        String name = ta.getString(0);
        String key = ta.getString(2);
        String value = ta.getString(1);
        if (XML_ELEMENT_EXTRA_ATTR_LAUCHMODE.equals(name)) {
            this.mActivityLaunchMode = value;
        } else if (XML_ELEMENT_EXTRA_ATTR_TYPE.equals(name)) {
            if (XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY.equals(value)) {
                this.mLaunchType = 0;
            } else if (XML_ELEMENT_EXTRA_ATTR_TYPE_SERVICE.equals(value)) {
                this.mLaunchType = 1;
            } else if (XML_ELEMENT_EXTRA_ATTR_TYPE_BROADCAST.equals(value)) {
                this.mLaunchType = 2;
            } else if (XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY_FOR_RESULT.equals(value)) {
                this.mLaunchType = 3;
            } else {
                this.mLaunchType = 0;
            }
        } else if (XML_ELEMENT_EXTRA_ATTR_CATEGORY.equals(name)) {
            this.mCategory = value;
        } else if (XML_ELEMENT_EXTRA_ATTR_INTETNACTION.equals(name)) {
            this.mAction = value;
        } else if ("packageName".equals(name)) {
            this.mPackageName = value;
        } else if (XML_ELEMENT_EXTRA_ATTR_COMPONENTNAME.equals(name)) {
            this.mComponentName = value;
        } else if (XML_ELEMENT_EXTRA_ATTR_FEATURE.equals(name)) {
            this.mFeatureNames.add(key);
            this.mFeatureValues.add(value);
        } else if (!(!XML_ELEMENT_EXTRA_ATTR_EXTRAS.equals(name) || TextUtils.isEmpty(key) || TextUtils.isEmpty(value))) {
            this.mBundle.putString(key, value);
        }
        ta.recycle();
    }

    private static void examineOrderInCategory(ExecutableInfo info, boolean isSamsungApps) {
        String resultStr = "";
        int order = ORDER_INIT_VALUE;
        if (!isSamsungApps) {
            order = ORDER_NOT_ALLOWED;
        }
        if (!info.getCategories().isEmpty()) {
            for (String str : info.mCategory.split("\\|")) {
                String[] strSplit = str.split("@");
                switch (strSplit.length) {
                    case 1:
                        resultStr = resultStr + str + "|";
                        break;
                    case 2:
                        if (order != ORDER_NOT_ALLOWED) {
                            try {
                                order = Integer.parseInt(strSplit[0]);
                                if (order < -1000 || order > 1000) {
                                    order = ORDER_OUT_OF_RANGE;
                                }
                            } catch (NumberFormatException nfe) {
                                order = ORDER_INVALID_FORMAT;
                                if (DEBUG) {
                                    Log.d(LOG_TAG, "Invalid order");
                                    nfe.printStackTrace();
                                }
                                if (ORDER_INVALID_FORMAT != ORDER_OUT_OF_RANGE && ORDER_INVALID_FORMAT != ORDER_NOT_ALLOWED && ORDER_INVALID_FORMAT != ORDER_INVALID_FORMAT) {
                                    resultStr = resultStr + str + "|";
                                    break;
                                } else {
                                    resultStr = resultStr + strSplit[1] + "|";
                                    break;
                                }
                            } catch (Throwable th) {
                                if (ORDER_INVALID_FORMAT == ORDER_OUT_OF_RANGE || ORDER_INVALID_FORMAT == ORDER_NOT_ALLOWED || ORDER_INVALID_FORMAT == ORDER_INVALID_FORMAT) {
                                    resultStr = resultStr + strSplit[1] + "|";
                                } else {
                                    resultStr = resultStr + str + "|";
                                }
                            }
                        }
                        if (order != ORDER_OUT_OF_RANGE && order != ORDER_NOT_ALLOWED && order != ORDER_INVALID_FORMAT) {
                            resultStr = resultStr + str + "|";
                            break;
                        } else {
                            resultStr = resultStr + strSplit[1] + "|";
                            break;
                        }
                    default:
                        resultStr = resultStr + str + "|";
                        if (!DEBUG) {
                            break;
                        }
                        Log.d(LOG_TAG, "Invalid category format for category order");
                        break;
                }
            }
            info.mCategory = resultStr.substring(0, resultStr.length() - 1);
        }
    }

    private static Context createActivityContext(Context context, ComponentName componentName) {
        Context theirContext = null;
        try {
            theirContext = context.createPackageContext(componentName.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.e(LOG_TAG, "Package not found " + componentName.getPackageName());
        } catch (SecurityException e2) {
            Log.e(LOG_TAG, "Can't make context for " + componentName.getPackageName(), e2);
        }
        return theirContext;
    }

    public static List<ExecutableInfo> scanExecutableInfos(Context context) {
        if (DEBUG) {
            Log.d(LOG_TAG, "scan ExecutableInfo start");
        }
        String ACTION_EXECUTABLE = MD_LABEL_EXECUTABLE;
        PackageManager pm = context.getPackageManager();
        List<ExecutableInfo> newExecutableInfoList = new ArrayList();
        List<ResolveInfo> activityExecutableList = pm.queryIntentActivities(new Intent(MD_LABEL_EXECUTABLE), DisplayMetrics.DENSITY_XXXHIGH);
        List<ResolveInfo> serviceExecutableList = pm.queryIntentServices(new Intent(MD_LABEL_EXECUTABLE), DisplayMetrics.DENSITY_XXXHIGH);
        List<ResolveInfo> receiverExecutableList = pm.queryBroadcastReceivers(new Intent(MD_LABEL_EXECUTABLE), DisplayMetrics.DENSITY_XXXHIGH);
        for (List<ResolveInfo> list : new List[]{activityExecutableList, serviceExecutableList, receiverExecutableList}) {
            if (DEBUG) {
                Log.d(LOG_TAG, "list size = " + list.size());
            }
            for (ResolveInfo info : list) {
                PackageItemInfo itemInfo = null;
                ApplicationInfo appInfo = null;
                boolean isDisabled = true;
                boolean isComponentDisabled = true;
                if (info.activityInfo != null) {
                    itemInfo = info.activityInfo;
                    appInfo = info.activityInfo.applicationInfo;
                    isDisabled = !info.activityInfo.applicationInfo.enabled;
                    isComponentDisabled = !info.activityInfo.enabled;
                } else if (info.serviceInfo != null) {
                    itemInfo = info.serviceInfo;
                    appInfo = info.serviceInfo.applicationInfo;
                    isDisabled = !info.serviceInfo.applicationInfo.enabled;
                    isComponentDisabled = !info.serviceInfo.enabled;
                }
                if (!isDisabled && !isComponentDisabled) {
                    ComponentName cName = new ComponentName(itemInfo.packageName, itemInfo.name);
                    try {
                        XmlResourceParser xml = appInfo.loadXmlMetaData(context.getPackageManager(), MD_LABEL_EXECUTABLE);
                        if (xml != null) {
                            ExecutableInfo lastExecutableInfo = null;
                            boolean startedExecutable = false;
                            boolean startedCommand = false;
                            for (int tagType = xml.next(); tagType != 1; tagType = xml.next()) {
                                String elementName = xml.getName();
                                if (tagType == 2) {
                                    if (XML_ELEMENT_EXECUTABLE.equals(elementName)) {
                                        startedExecutable = true;
                                    }
                                    if ("command".equals(elementName)) {
                                        if (startedExecutable) {
                                            startedCommand = true;
                                            lastExecutableInfo = getActivityMetaData(context, Xml.asAttributeSet(xml), cName);
                                        } else {
                                            throw new XmlPullParserException("executable element wasn't started");
                                        }
                                    }
                                    if (!XML_ELEMENT_EXTRA_ATTR.equals(elementName)) {
                                        continue;
                                    } else if (startedExecutable && r30) {
                                        AttributeSet attr = Xml.asAttributeSet(xml);
                                        if (lastExecutableInfo != null) {
                                            lastExecutableInfo.addExtraAttribute(context, attr);
                                        }
                                    } else {
                                        throw new XmlPullParserException("executable or command element wasn't started");
                                    }
                                } else if (tagType == 3) {
                                    if (XML_ELEMENT_EXECUTABLE.equals(elementName)) {
                                        startedExecutable = false;
                                    }
                                    if ("command".equals(elementName)) {
                                        startedCommand = false;
                                        if (checkValidate(lastExecutableInfo)) {
                                            examineOrderInCategory(lastExecutableInfo, WhiteListForCategoryOrder.getInstance().isAllowedToUseOrder(context, appInfo.packageName));
                                            lastExecutableInfo.setId(appInfo.packageName);
                                            boolean bDuplicatedID = false;
                                            for (ExecutableInfo checkInfo : newExecutableInfoList) {
                                                if (checkInfo.getId() == lastExecutableInfo.getId()) {
                                                    bDuplicatedID = true;
                                                }
                                            }
                                            if (!bDuplicatedID) {
                                                newExecutableInfoList.add(lastExecutableInfo);
                                            }
                                        }
                                        lastExecutableInfo = null;
                                    }
                                }
                            }
                            continue;
                        } else {
                            continue;
                        }
                    } catch (IllegalArgumentException e) {
                        Log.w(LOG_TAG, "Invalid attribute in metadata for " + cName.flattenToShortString() + ": " + e.getMessage());
                    } catch (XmlPullParserException e2) {
                        Log.w(LOG_TAG, "Reading ExecutableInfo metadata for " + cName.flattenToShortString(), e2);
                    } catch (IOException e3) {
                        Log.w(LOG_TAG, "Reading ExecutableInfo metadata for " + cName.flattenToShortString(), e3);
                    } catch (Exception e4) {
                        Log.w(LOG_TAG, "Unknown Exception while Reading ExecutableInfo metadata", e4);
                    }
                } else if (DEBUG) {
                    Log.d(LOG_TAG, "skip disable component: " + isDisabled + ", " + isComponentDisabled);
                }
            }
        }
        if (DEBUG) {
            Log.d(LOG_TAG, "scan ExecutableInfo end: " + newExecutableInfoList.size());
        }
        return newExecutableInfoList;
    }

    private static boolean checkValidate(ExecutableInfo info) {
        if (info == null) {
            if (!DEBUG) {
                return false;
            }
            Log.d(LOG_TAG, "Invalid ExecutableInfo");
            return false;
        } else if (info.mEnabled) {
            if (info.getLaunchType() == 2 || !(info.getPackageName() == null || info.getComponentName() == null)) {
                if (info.getLabelId() != 0 && info.getIconId() != 0) {
                    for (int i = 0; i < info.mFeatureNames.size(); i++) {
                        String featureName = (String) info.mFeatureNames.get(i);
                        String featureValue = (String) info.mFeatureValues.get(i);
                        if (featureName == null || featureName.length() <= 0 || featureValue == null || featureValue.length() <= 0) {
                            if (featureName == null || featureName.length() <= 0 || (featureValue != null && (featureValue == null || featureValue.length() > 0))) {
                                if (featureValue != null && featureValue.length() > 0 && (featureName == null || (featureName != null && featureName.length() <= 0))) {
                                    if (!DEBUG) {
                                        return false;
                                    }
                                    Log.d(LOG_TAG, "No feature name is provided for the value " + featureValue + " " + info.toString());
                                    return false;
                                }
                            } else if (!DEBUG) {
                                return false;
                            } else {
                                Log.d(LOG_TAG, "No value for " + featureName + " " + info.toString());
                                return false;
                            }
                        } else if (featureName.startsWith(CSC_FEATURE_PREFIX)) {
                            str = CscFeature.getInstance().getString(featureName);
                            if (featureValue.startsWith("!")) {
                                if (str.equalsIgnoreCase(featureValue.substring(1))) {
                                    return false;
                                }
                            } else if (!str.equalsIgnoreCase(featureValue)) {
                                if (!DEBUG) {
                                    return false;
                                }
                                Log.d(LOG_TAG, featureName + " is not [" + featureValue + "] " + info.toString());
                                return false;
                            }
                        } else if (featureName.startsWith(SEC_FLOATING_FEATURE_PREFIX)) {
                            str = FloatingFeature.getInstance().getString(featureName);
                            if (featureValue.startsWith("!")) {
                                if (str.equalsIgnoreCase(featureValue.substring(1))) {
                                    return false;
                                }
                            } else if (!str.equalsIgnoreCase(featureValue)) {
                                if (!DEBUG) {
                                    return false;
                                }
                                Log.d(LOG_TAG, featureName + " is not [" + featureValue + "] " + info.toString());
                                return false;
                            }
                        } else if (featureName.startsWith(SEC_PRODUCT_FEATURE_PREFIX)) {
                            return false;
                        } else {
                            str = SystemProperties.get(featureName);
                            if (featureValue.startsWith("!")) {
                                if (str.equalsIgnoreCase(featureValue.substring(1))) {
                                    return false;
                                }
                            } else if (!str.equalsIgnoreCase(featureValue)) {
                                if (!DEBUG) {
                                    return false;
                                }
                                Log.d(LOG_TAG, featureName + " is not [" + featureValue + "] " + info.toString());
                                return false;
                            }
                        }
                    }
                    return true;
                } else if (!DEBUG) {
                    return false;
                } else {
                    Log.d(LOG_TAG, "Invalid label or icon = " + info.toString());
                    return false;
                }
            } else if (!DEBUG) {
                return false;
            } else {
                Log.d(LOG_TAG, "Invalid packageName or componentName = " + info.toString());
                return false;
            }
        } else if (!DEBUG) {
            return false;
        } else {
            Log.d(LOG_TAG, "disabled executableInfo " + info.toString());
            return false;
        }
    }

    private String getBundleString() {
        String result = "";
        if (this.mBundle.isEmpty()) {
            return result;
        }
        List<String> keyList = new ArrayList(this.mBundle.keySet());
        Collections.sort(keyList);
        for (String key : keyList) {
            result = result + "{" + key + "=" + this.mBundle.get(key) + "}";
        }
        return result;
    }

    public String toString() {
        String retString = "ExecutableInfo{enabled=" + this.mEnabled + ", id=" + this.mUid + ", labelId=" + this.mLabelId + ", iconIId=" + this.mIconId + ", smallIconIId=" + this.mSmallIconId + ", type=" + this.mLaunchType + ", category=" + this.mCategory + ", action='" + this.mAction + DateFormat.QUOTE + ", packageName='" + this.mPackageName + DateFormat.QUOTE + ", componentName='" + this.mComponentName + DateFormat.QUOTE + ", launchMode='" + this.mActivityLaunchMode + DateFormat.QUOTE;
        for (int i = 0; i < this.mFeatureNames.size(); i++) {
            retString = retString + ", featureName ='" + ((String) this.mFeatureNames.get(i)) + DateFormat.QUOTE + ", featureValue = '" + ((String) this.mFeatureValues.get(i)) + DateFormat.QUOTE;
        }
        return (retString + ", mBundle ='" + getBundleString() + DateFormat.QUOTE) + '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUid);
        dest.writeInt(this.mEnabled ? 1 : 0);
        dest.writeInt(this.mLabelId);
        dest.writeInt(this.mIconId);
        dest.writeInt(this.mSmallIconId);
        dest.writeInt(this.mLaunchType);
        dest.writeString(this.mCategory);
        dest.writeString(this.mAction);
        dest.writeString(this.mPackageName);
        dest.writeStringList(this.mFeatureNames);
        dest.writeStringList(this.mFeatureValues);
        dest.writeBundle(this.mBundle);
        dest.writeString(this.mComponentName);
        dest.writeString(this.mActivityLaunchMode);
    }
}
