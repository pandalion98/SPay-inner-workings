package android.os;

import android.os.Parcelable.Creator;

public class BatteryProperties implements Parcelable {
    public static final Creator<BatteryProperties> CREATOR = new Creator<BatteryProperties>() {
        public BatteryProperties createFromParcel(Parcel p) {
            return new BatteryProperties(p);
        }

        public BatteryProperties[] newArray(int size) {
            return new BatteryProperties[size];
        }
    };
    public int batteryChargeCounter;
    public int batteryChargeType;
    public int batteryCurrentAvg;
    public int batteryCurrentNow;
    public int batteryHealth;
    public boolean batteryHighVoltageCharger;
    public int batteryLevel;
    public int batteryOnline;
    public boolean batteryPowerSharingOnline;
    public boolean batteryPresent;
    public boolean batterySWSelfDischarging;
    public int batterySecEvent;
    public int batteryStatus;
    public String batteryTechnology;
    public int batteryTemperature;
    public int batteryVoltage;
    public boolean chargerAcOnline;
    public boolean chargerPogoOnline;
    public boolean chargerUsbOnline;
    public boolean chargerWirelessOnline;
    public int maxChargingCurrent;

    public void set(BatteryProperties other) {
        this.chargerAcOnline = other.chargerAcOnline;
        this.chargerUsbOnline = other.chargerUsbOnline;
        this.chargerWirelessOnline = other.chargerWirelessOnline;
        this.maxChargingCurrent = other.maxChargingCurrent;
        this.batteryStatus = other.batteryStatus;
        this.batteryHealth = other.batteryHealth;
        this.batteryPresent = other.batteryPresent;
        this.batteryLevel = other.batteryLevel;
        this.batteryVoltage = other.batteryVoltage;
        this.batteryTemperature = other.batteryTemperature;
        this.batteryTechnology = other.batteryTechnology;
        this.batteryOnline = other.batteryOnline;
        this.batteryCurrentAvg = other.batteryCurrentAvg;
        this.batteryChargeType = other.batteryChargeType;
        this.chargerPogoOnline = other.chargerPogoOnline;
        this.batteryHighVoltageCharger = other.batteryHighVoltageCharger;
        this.batterySWSelfDischarging = other.batterySWSelfDischarging;
        this.batterySecEvent = other.batterySecEvent;
    }

    private BatteryProperties(Parcel p) {
        boolean z;
        boolean z2 = true;
        this.chargerAcOnline = p.readInt() == 1;
        if (p.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.chargerUsbOnline = z;
        if (p.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.chargerWirelessOnline = z;
        this.maxChargingCurrent = p.readInt();
        this.batteryStatus = p.readInt();
        this.batteryHealth = p.readInt();
        if (p.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.batteryPresent = z;
        this.batteryLevel = p.readInt();
        this.batteryVoltage = p.readInt();
        this.batteryCurrentNow = p.readInt();
        this.batteryChargeCounter = p.readInt();
        this.batteryTemperature = p.readInt();
        this.batteryTechnology = p.readString();
        this.batteryOnline = p.readInt();
        this.batteryCurrentAvg = p.readInt();
        this.batteryChargeType = p.readInt();
        if (p.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.batteryPowerSharingOnline = z;
        if (p.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.chargerPogoOnline = z;
        if (p.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.batteryHighVoltageCharger = z;
        if (p.readInt() != 1) {
            z2 = false;
        }
        this.batterySWSelfDischarging = z2;
        this.batterySecEvent = p.readInt();
    }

    public void writeToParcel(Parcel p, int flags) {
        int i;
        int i2 = 1;
        p.writeInt(this.chargerAcOnline ? 1 : 0);
        if (this.chargerUsbOnline) {
            i = 1;
        } else {
            i = 0;
        }
        p.writeInt(i);
        if (this.chargerWirelessOnline) {
            i = 1;
        } else {
            i = 0;
        }
        p.writeInt(i);
        p.writeInt(this.maxChargingCurrent);
        p.writeInt(this.batteryStatus);
        p.writeInt(this.batteryHealth);
        if (this.batteryPresent) {
            i = 1;
        } else {
            i = 0;
        }
        p.writeInt(i);
        p.writeInt(this.batteryLevel);
        p.writeInt(this.batteryVoltage);
        p.writeInt(this.batteryCurrentNow);
        p.writeInt(this.batteryChargeCounter);
        p.writeInt(this.batteryTemperature);
        p.writeString(this.batteryTechnology);
        p.writeInt(this.batteryOnline);
        p.writeInt(this.batteryCurrentAvg);
        p.writeInt(this.batteryChargeType);
        if (this.batteryPowerSharingOnline) {
            i = 1;
        } else {
            i = 0;
        }
        p.writeInt(i);
        if (this.chargerPogoOnline) {
            i = 1;
        } else {
            i = 0;
        }
        p.writeInt(i);
        if (this.batteryHighVoltageCharger) {
            i = 1;
        } else {
            i = 0;
        }
        p.writeInt(i);
        if (!this.batterySWSelfDischarging) {
            i2 = 0;
        }
        p.writeInt(i2);
        p.writeInt(this.batterySecEvent);
    }

    public int describeContents() {
        return 0;
    }
}
