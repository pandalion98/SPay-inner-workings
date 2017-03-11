package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.Poi;
import java.util.ArrayList;
import java.util.Iterator;

public class TriggerRequestData {
    private DeviceInfo device;
    private ArrayList<Poi> pois;
    private UserInfo user;
    private WalletInfo wallet;

    public ArrayList<Poi> getPois() {
        return this.pois;
    }

    public void setPois(ArrayList<Poi> arrayList) {
        this.pois = arrayList;
    }

    public DeviceInfo getDevice() {
        return this.device;
    }

    public void setDevice(DeviceInfo deviceInfo) {
        this.device = deviceInfo;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public void setUser(UserInfo userInfo) {
        this.user = userInfo;
    }

    public WalletInfo getWallet() {
        return this.wallet;
    }

    public void setWallet(WalletInfo walletInfo) {
        this.wallet = walletInfo;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[pois:");
        if (this.pois != null) {
            Iterator it = this.pois.iterator();
            while (it.hasNext()) {
                stringBuilder.append(((Poi) it.next()).toString());
                stringBuilder.append(",");
            }
        } else {
            stringBuilder.append("null");
        }
        stringBuilder.append(", device:");
        if (this.device == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.device.toString());
        }
        stringBuilder.append(", user:");
        if (this.user == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.user.toString());
        }
        stringBuilder.append(", wallet:");
        if (this.wallet == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.wallet.toString());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
