package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class WifiP2pConfigList implements Parcelable {
    public static final Creator<WifiP2pConfigList> CREATOR = new Creator<WifiP2pConfigList>() {
        public WifiP2pConfigList createFromParcel(Parcel in) {
            WifiP2pConfigList deviceList = new WifiP2pConfigList();
            int deviceCount = in.readInt();
            for (int i = 0; i < deviceCount; i++) {
                deviceList.update((WifiP2pConfig) in.readParcelable(null));
            }
            return deviceList;
        }

        public WifiP2pConfigList[] newArray(int size) {
            return new WifiP2pConfigList[size];
        }
    };
    private int mAlllistCount;
    private Collection<WifiP2pConfig> mDevices;

    public WifiP2pConfigList() {
        this.mDevices = new ArrayList();
        this.mAlllistCount = 0;
    }

    public WifiP2pConfigList(WifiP2pConfigList source) {
        if (source != null) {
            this.mDevices = source.getConfigList();
            this.mAlllistCount = 0;
        }
    }

    public WifiP2pConfigList(ArrayList<WifiP2pConfig> devices) {
        this.mDevices = new ArrayList();
        this.mAlllistCount = 0;
        Iterator i$ = devices.iterator();
        while (i$.hasNext()) {
            this.mDevices.add((WifiP2pConfig) i$.next());
        }
    }

    public boolean clear() {
        if (this.mDevices.isEmpty()) {
            return false;
        }
        this.mDevices.clear();
        this.mAlllistCount = 0;
        return true;
    }

    public void update(WifiP2pConfig device) {
        if (device != null) {
            for (WifiP2pConfig d : this.mDevices) {
                if (d.equals(device)) {
                    d.deviceAddress = device.deviceAddress;
                    d.wps = device.wps;
                    d.groupOwnerIntent = device.groupOwnerIntent;
                    d.netId = device.netId;
                    return;
                }
            }
            this.mDevices.add(device);
            this.mAlllistCount++;
        }
    }

    public boolean remove(WifiP2pConfig device) {
        if (device == null) {
            return false;
        }
        return this.mDevices.remove(device);
    }

    public Collection<WifiP2pConfig> getConfigList() {
        return Collections.unmodifiableCollection(this.mDevices);
    }

    public boolean isEmpty() {
        if (this.mDevices.isEmpty()) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        for (WifiP2pConfig device : this.mDevices) {
            sbuf.append("\n").append(device);
        }
        return sbuf.toString();
    }

    public WifiP2pConfig getConfigIndex(int index) {
        int i = -1;
        if (isEmpty()) {
            return null;
        }
        for (WifiP2pConfig cc : getConfigList()) {
            i++;
            if (i == index) {
                return cc;
            }
        }
        return null;
    }

    public int getAllCount() {
        return this.mAlllistCount;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mDevices.size());
        for (WifiP2pConfig device : this.mDevices) {
            dest.writeParcelable(device, flags);
        }
    }
}
