/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.samsung.contextclient.data.JsonWraper;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.PoiAttribute;
import com.samsung.contextclient.data.PoiTrigger;
import com.samsung.contextclient.data.ProximityInfo;
import com.samsung.contextclient.data.SensorData;
import com.samsung.contextclient.data.WifiSignature;

public class Poi
extends JsonWraper
implements Parcelable {
    public static final transient Parcelable.Creator<Poi> CREATOR = new Parcelable.Creator<Poi>(){

        public Poi createFromParcel(Parcel parcel) {
            return new Poi(parcel);
        }

        public Poi[] newArray(int n2) {
            return new Poi[n2];
        }
    };
    public static final transient String PURPOSE_DEAL = "DEALS";
    public static final transient String PURPOSE_PROMOTION = "PROMO";
    public static final transient double RADIUS_LARGE = 321869.0;
    public static final transient double RADIUS_MEDIUM = 80467.0;
    public static final transient double RADIUS_SMALL = 500.0;
    public static final transient int SOURCE_OTHER = 32;
    private PoiAttribute attributes;
    private float confidence;
    private String id;
    private Location location;
    private String name;
    private ProximityInfo proximityInfo;
    private SensorData sensorData;
    private PoiTrigger trigger;
    private WifiSignature wifiSignature;

    public Poi() {
    }

    protected Poi(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.attributes = (PoiAttribute)parcel.readParcelable(PoiAttribute.class.getClassLoader());
        this.sensorData = (SensorData)parcel.readParcelable(SensorData.class.getClassLoader());
        this.confidence = parcel.readFloat();
        this.location = (Location)parcel.readParcelable(Location.class.getClassLoader());
        this.wifiSignature = (WifiSignature)parcel.readParcelable(WifiSignature.class.getClassLoader());
        this.trigger = (PoiTrigger)parcel.readParcelable(PoiTrigger.class.getClassLoader());
        this.proximityInfo = (ProximityInfo)parcel.readParcelable(ProximityInfo.class.getClassLoader());
    }

    public static Poi toPoiFromJson(String string) {
        return (Poi)new Gson().fromJson(string, Poi.class);
    }

    public int describeContents() {
        return 0;
    }

    public PoiAttribute getAttributes() {
        return this.attributes;
    }

    public float getConfidence() {
        return this.confidence;
    }

    public String getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public ProximityInfo getProximityInfo() {
        return this.proximityInfo;
    }

    public SensorData getSensorData() {
        return this.sensorData;
    }

    public PoiTrigger getTrigger() {
        return this.trigger;
    }

    public WifiSignature getWifiSignature() {
        return this.wifiSignature;
    }

    public void setAttributes(PoiAttribute poiAttribute) {
        this.attributes = poiAttribute;
    }

    public void setConfidence(float f2) {
        this.confidence = f2;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setProximityInfo(ProximityInfo proximityInfo) {
        this.proximityInfo = proximityInfo;
    }

    public void setSensorData(SensorData sensorData) {
        this.sensorData = sensorData;
    }

    public void setTrigger(PoiTrigger poiTrigger) {
        this.trigger = poiTrigger;
    }

    public void setWifiSignature(WifiSignature wifiSignature) {
        this.wifiSignature = wifiSignature;
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeParcelable((Parcelable)this.attributes, n2);
        parcel.writeParcelable((Parcelable)this.sensorData, n2);
        parcel.writeFloat(this.confidence);
        parcel.writeParcelable((Parcelable)this.location, n2);
        parcel.writeParcelable((Parcelable)this.wifiSignature, n2);
        parcel.writeParcelable((Parcelable)this.trigger, n2);
        parcel.writeParcelable((Parcelable)this.proximityInfo, n2);
    }

}

