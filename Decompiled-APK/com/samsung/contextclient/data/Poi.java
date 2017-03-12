package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.Gson;

public class Poi extends JsonWraper implements Parcelable {
    public static final transient Creator<Poi> CREATOR;
    public static final transient String PURPOSE_DEAL = "DEALS";
    public static final transient String PURPOSE_PROMOTION = "PROMO";
    public static final transient double RADIUS_LARGE = 321869.0d;
    public static final transient double RADIUS_MEDIUM = 80467.0d;
    public static final transient double RADIUS_SMALL = 500.0d;
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

    /* renamed from: com.samsung.contextclient.data.Poi.1 */
    static class C06071 implements Creator<Poi> {
        C06071() {
        }

        public Poi createFromParcel(Parcel parcel) {
            return new Poi(parcel);
        }

        public Poi[] newArray(int i) {
            return new Poi[i];
        }
    }

    protected Poi(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.attributes = (PoiAttribute) parcel.readParcelable(PoiAttribute.class.getClassLoader());
        this.sensorData = (SensorData) parcel.readParcelable(SensorData.class.getClassLoader());
        this.confidence = parcel.readFloat();
        this.location = (Location) parcel.readParcelable(Location.class.getClassLoader());
        this.wifiSignature = (WifiSignature) parcel.readParcelable(WifiSignature.class.getClassLoader());
        this.trigger = (PoiTrigger) parcel.readParcelable(PoiTrigger.class.getClassLoader());
        this.proximityInfo = (ProximityInfo) parcel.readParcelable(ProximityInfo.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeParcelable(this.attributes, i);
        parcel.writeParcelable(this.sensorData, i);
        parcel.writeFloat(this.confidence);
        parcel.writeParcelable(this.location, i);
        parcel.writeParcelable(this.wifiSignature, i);
        parcel.writeParcelable(this.trigger, i);
        parcel.writeParcelable(this.proximityInfo, i);
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06071();
    }

    public static Poi toPoiFromJson(String str) {
        return (Poi) new Gson().fromJson(str, Poi.class);
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public void setConfidence(float f) {
        this.confidence = f;
    }

    public float getConfidence() {
        return this.confidence;
    }

    public void setWifiSignature(WifiSignature wifiSignature) {
        this.wifiSignature = wifiSignature;
    }

    public WifiSignature getWifiSignature() {
        return this.wifiSignature;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setTrigger(PoiTrigger poiTrigger) {
        this.trigger = poiTrigger;
    }

    public PoiTrigger getTrigger() {
        return this.trigger;
    }

    public void setSensorData(SensorData sensorData) {
        this.sensorData = sensorData;
    }

    public SensorData getSensorData() {
        return this.sensorData;
    }

    public PoiAttribute getAttributes() {
        return this.attributes;
    }

    public void setAttributes(PoiAttribute poiAttribute) {
        this.attributes = poiAttribute;
    }

    public ProximityInfo getProximityInfo() {
        return this.proximityInfo;
    }

    public void setProximityInfo(ProximityInfo proximityInfo) {
        this.proximityInfo = proximityInfo;
    }

    public String toString() {
        return toJson();
    }
}
