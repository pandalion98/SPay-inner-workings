package com.android.internal.net;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.IpPrefix;
import android.net.LinkAddress;
import android.net.Network;
import android.net.RouteInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.R;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class VpnConfig implements Parcelable {
    public static final Creator<VpnConfig> CREATOR = new Creator<VpnConfig>() {
        public VpnConfig createFromParcel(Parcel in) {
            boolean z;
            boolean z2 = true;
            VpnConfig config = new VpnConfig();
            config.user = in.readString();
            config.interfaze = in.readString();
            config.session = in.readString();
            config.mtu = in.readInt();
            in.readTypedList(config.addresses, LinkAddress.CREATOR);
            in.readTypedList(config.routes, RouteInfo.CREATOR);
            config.dnsServers = in.createStringArrayList();
            config.searchDomains = in.createStringArrayList();
            config.allowedApplications = in.createStringArrayList();
            config.disallowedApplications = in.createStringArrayList();
            config.configureIntent = (PendingIntent) in.readParcelable(null);
            config.startTime = in.readLong();
            config.legacy = in.readInt() != 0;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.blocking = z;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.allowBypass = z;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.allowIPv4 = z;
            if (in.readInt() == 0) {
                z2 = false;
            }
            config.allowIPv6 = z2;
            config.underlyingNetworks = (Network[]) in.createTypedArray(Network.CREATOR);
            return config;
        }

        public VpnConfig[] newArray(int size) {
            return new VpnConfig[size];
        }
    };
    public static final String DIALOGS_PACKAGE = "com.android.vpndialogs";
    private static final int KNOXVPN_CONTAINER_ENABLED = 2;
    private static int KNOXVPN_FEATURE = -1;
    private static final int KNOXVPN_MDM_ENABLED = 1;
    public static final String LEGACY_VPN = "[Legacy VPN]";
    private static final boolean REMOVE = false;
    public static final String SERVICE_INTERFACE = "android.net.VpnService";
    public static final String TAG = "VpnConfig";
    private static ConcurrentHashMap<Integer, ArrayList<VpnConfig>> mConfigByUserMap = new ConcurrentHashMap();
    private static ArrayList<VpnConfig> mConfigsReceived = new ArrayList();
    public List<LinkAddress> addresses = new ArrayList();
    public boolean allowBypass;
    public boolean allowIPv4;
    public boolean allowIPv6;
    public List<String> allowedApplications;
    public boolean blocking;
    public PendingIntent configureIntent;
    public List<String> disallowedApplications;
    public List<String> dnsServers;
    public String interfaze;
    public boolean legacy;
    public int mtu = -1;
    public List<RouteInfo> routes = new ArrayList();
    public List<String> searchDomains;
    public String session;
    public long startTime = -1;
    public Network[] underlyingNetworks;
    public String user;

    private static int getKnoxVpnFeature() {
        KNOXVPN_FEATURE = 2;
        return KNOXVPN_FEATURE;
    }

    public static Intent getIntentForConfirmation() {
        Intent intent = new Intent();
        ComponentName componentName = ComponentName.unflattenFromString(Resources.getSystem().getString(R.string.config_customVpnConfirmDialogComponent));
        intent.setClassName(componentName.getPackageName(), componentName.getClassName());
        return intent;
    }

    public static PendingIntent getIntentForStatusPanel(Context context) {
        Intent intent = new Intent();
        intent.setClassName(DIALOGS_PACKAGE, "com.android.vpndialogs.ManageDialog");
        intent.addFlags(1350565888);
        return PendingIntent.getActivityAsUser(context, 0, intent, 0, null, UserHandle.CURRENT);
    }

    public static CharSequence getVpnLabel(Context context, String packageName) throws NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(SERVICE_INTERFACE);
        intent.setPackage(packageName);
        List<ResolveInfo> services = pm.queryIntentServices(intent, 0);
        if (services == null || services.size() != 1) {
            return pm.getApplicationInfo(packageName, 0).loadLabel(pm);
        }
        return ((ResolveInfo) services.get(0)).loadLabel(pm);
    }

    public static PendingIntent getIntentForStatusPanelAsUser(Context context, int user) {
        if (getKnoxVpnFeature() < 1) {
            Log.d(TAG, "Knox VPN is not supported Knox VPN feature: " + getKnoxVpnFeature());
            return null;
        }
        Intent intent = new Intent();
        intent.setClassName(DIALOGS_PACKAGE, "com.android.vpndialogs.ManageDialog");
        intent.addFlags(1350565888);
        return PendingIntent.getActivityAsUser(context, 0, intent, 0, null, new UserHandle(user));
    }

    public static PendingIntent getIntentForStatusPanelEnterpriseVpn(Context context, VpnConfig config, boolean configOption) {
        if (getKnoxVpnFeature() < 1) {
            Log.d(TAG, "Knox VPN is not supported Knox VPN feature: " + getKnoxVpnFeature());
            return null;
        }
        Intent intent = new Intent();
        if (configOption) {
            if (config != null) {
                Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : Adding iterator for profile : " + config.session);
            }
            mConfigsReceived.add(config);
        } else {
            String configSession = config.session;
            Iterator iterator = mConfigsReceived.iterator();
            while (iterator.hasNext()) {
                if (((VpnConfig) iterator.next()).session.equals(configSession)) {
                    Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : Removing iterator for profile : " + configSession);
                    iterator.remove();
                    break;
                }
            }
            Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : config size =  " + mConfigsReceived.size());
            if (mConfigsReceived.size() == 0) {
                Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : Returning null");
                return null;
            }
        }
        intent.setClassName(DIALOGS_PACKAGE, "com.android.vpndialogs.EnterpriseVpnDialog");
        intent.putParcelableArrayListExtra("config", mConfigsReceived);
        intent.addFlags(1350565888);
        return PendingIntent.getActivityAsUser(context, 0, intent, config == null ? 536870912 : 268435456, null, UserHandle.CURRENT);
    }

    public static PendingIntent getIntentForStatusPanelRefresh(Context context) {
        if (getKnoxVpnFeature() < 1) {
            Log.d(TAG, "Knox VPN is not supported Knox VPN feature: " + getKnoxVpnFeature());
            return null;
        }
        Intent intent = new Intent();
        intent.setClassName(DIALOGS_PACKAGE, "com.android.vpndialogs.EnterpriseVpnDialog");
        intent.putParcelableArrayListExtra("config", mConfigsReceived);
        intent.addFlags(1350565888);
        return PendingIntent.getActivityAsUser(context, 0, intent, 268435456, null, UserHandle.CURRENT);
    }

    public static PendingIntent getIntentForStatusPanelEnterpriseVpnAsUser(Context context, VpnConfig config, boolean configOption, int domain) {
        if (getKnoxVpnFeature() < 1) {
            Log.d(TAG, "Knox VPN is not supported Knox VPN feature: " + getKnoxVpnFeature());
            return null;
        }
        Intent intent = new Intent();
        ArrayList<VpnConfig> receivedConfig = (ArrayList) mConfigByUserMap.get(Integer.valueOf(domain));
        if (config == null) {
            return null;
        }
        if (receivedConfig == null) {
            receivedConfig = new ArrayList();
        }
        if (configOption) {
            if (config != null) {
                Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : Adding iterator for profile : " + config.session);
            }
            boolean isFoundProfile = false;
            for (int i = 0; i < receivedConfig.size(); i++) {
                if (((VpnConfig) receivedConfig.get(i)).session.equals(config.session)) {
                    isFoundProfile = true;
                    break;
                }
            }
            if (!isFoundProfile) {
                receivedConfig.add(config);
                mConfigByUserMap.put(Integer.valueOf(domain), receivedConfig);
            }
        } else {
            String configSession = config.session;
            Iterator iterator = receivedConfig.iterator();
            while (iterator.hasNext()) {
                if (((VpnConfig) iterator.next()).session.equals(configSession)) {
                    Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : Removing iterator for profile : " + configSession);
                    iterator.remove();
                    break;
                }
            }
            Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : config size =  " + mConfigsReceived.size());
            if (receivedConfig.size() == 0) {
                Log.d(TAG, "getIntentForStatusPanelEnterpriseVpn : Returning null");
                return null;
            }
        }
        intent.setClassName(DIALOGS_PACKAGE, "com.android.vpndialogs.EnterpriseVpnDialog");
        intent.putParcelableArrayListExtra("config", receivedConfig);
        intent.addFlags(1350565888);
        return PendingIntent.getActivityAsUser(context, 0, intent, 268435456, null, new UserHandle(domain));
    }

    public static PendingIntent getIntentForStatusPanelRefreshAsUser(Context context, int domain) {
        if (getKnoxVpnFeature() < 1) {
            Log.d(TAG, "Knox VPN is not supported Knox VPN feature: " + getKnoxVpnFeature());
            return null;
        }
        Intent intent = new Intent();
        intent.setClassName(DIALOGS_PACKAGE, "com.android.vpndialogs.EnterpriseVpnDialog");
        intent.putParcelableArrayListExtra("config", (ArrayList) mConfigByUserMap.get(Integer.valueOf(domain)));
        intent.addFlags(1350565888);
        return PendingIntent.getActivityAsUser(context, 0, intent, 268435456, null, new UserHandle(domain));
    }

    public void updateAllowedFamilies(InetAddress address) {
        if (address instanceof Inet4Address) {
            this.allowIPv4 = true;
        } else {
            this.allowIPv6 = true;
        }
    }

    public void addLegacyRoutes(String routesStr) {
        if (!routesStr.trim().equals("")) {
            for (String route : routesStr.trim().split(" ")) {
                RouteInfo info = new RouteInfo(new IpPrefix(route), null);
                this.routes.add(info);
                updateAllowedFamilies(info.getDestination().getAddress());
            }
        }
    }

    public void addLegacyAddresses(String addressesStr) {
        if (!addressesStr.trim().equals("")) {
            for (String address : addressesStr.trim().split(" ")) {
                LinkAddress addr = new LinkAddress(address);
                this.addresses.add(addr);
                updateAllowedFamilies(addr.getAddress());
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        int i2 = 1;
        out.writeString(this.user);
        out.writeString(this.interfaze);
        out.writeString(this.session);
        out.writeInt(this.mtu);
        out.writeTypedList(this.addresses);
        out.writeTypedList(this.routes);
        out.writeStringList(this.dnsServers);
        out.writeStringList(this.searchDomains);
        out.writeStringList(this.allowedApplications);
        out.writeStringList(this.disallowedApplications);
        out.writeParcelable(this.configureIntent, flags);
        out.writeLong(this.startTime);
        out.writeInt(this.legacy ? 1 : 0);
        if (this.blocking) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeInt(i);
        if (this.allowBypass) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeInt(i);
        if (this.allowIPv4) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeInt(i);
        if (!this.allowIPv6) {
            i2 = 0;
        }
        out.writeInt(i2);
        out.writeTypedArray(this.underlyingNetworks, flags);
    }
}
