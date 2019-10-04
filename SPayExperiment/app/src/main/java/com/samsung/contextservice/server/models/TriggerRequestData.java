/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.server.models.DeviceInfo;
import com.samsung.contextservice.server.models.UserInfo;
import com.samsung.contextservice.server.models.WalletInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class TriggerRequestData {
    private DeviceInfo device;
    private ArrayList<Poi> pois;
    private UserInfo user;
    private WalletInfo wallet;

    public DeviceInfo getDevice() {
        return this.device;
    }

    public ArrayList<Poi> getPois() {
        return this.pois;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public WalletInfo getWallet() {
        return this.wallet;
    }

    public void setDevice(DeviceInfo deviceInfo) {
        this.device = deviceInfo;
    }

    public void setPois(ArrayList<Poi> arrayList) {
        this.pois = arrayList;
    }

    public void setUser(UserInfo userInfo) {
        this.user = userInfo;
    }

    public void setWallet(WalletInfo walletInfo) {
        this.wallet = walletInfo;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[pois:");
        if (this.pois != null) {
            Iterator iterator = this.pois.iterator();
            while (iterator.hasNext()) {
                stringBuilder.append(((Poi)iterator.next()).toString());
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

