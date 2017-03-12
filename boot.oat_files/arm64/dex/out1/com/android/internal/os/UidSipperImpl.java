package com.android.internal.os;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.android.internal.os.UidSipper.BatterySipStat;
import java.util.ArrayList;

public class UidSipperImpl {
    public static final Creator<UidSipperImpl> CREATOR = new Creator<UidSipperImpl>() {
        public UidSipperImpl createFromParcel(Parcel in) {
            return new UidSipperImpl(in);
        }

        public UidSipperImpl[] newArray(int arg0) {
            return null;
        }
    };
    private final String TAG;
    private ArrayList<PowerConsumingPackages> consumerPackages;
    private double finalPower;
    private boolean isConsumerListIncluded;
    private final ArrayList<UidSipper> uidSippers;

    public static final class PowerConsumingPackages {
        private int count;
        private String[] packages;
        private String time;

        private PowerConsumingPackages(String time, String names) {
            this.time = time;
            String[] packageNames = names.split(";");
            this.count = packageNames.length;
            if (this.count != 0 && packageNames[0].isEmpty()) {
                this.count = 0;
            }
            if (this.count != 0) {
                this.packages = packageNames;
            }
        }

        private PowerConsumingPackages(String time, int count, String[] names) {
            this.time = time;
            this.count = count;
            this.packages = names;
        }

        public String getTime() {
            return this.time;
        }

        public int getPackageCount() {
            return this.count;
        }

        public String[] getPackageNames() {
            return this.packages;
        }
    }

    public UidSipperImpl() {
        this.TAG = "BatteryStatsDumper";
        this.uidSippers = new ArrayList();
    }

    public UidSipperImpl(Parcel in) {
        this();
        Log.i("BatteryStatsDumper", "Creating UidSipperImpl class from parcel");
        this.finalPower = in.readDouble();
        int uidNum = in.readInt();
        Log.i("BatteryStatsDumper", "Total number of uids = " + uidNum);
        for (int i = 0; i < uidNum; i++) {
            int j;
            String name = in.readString();
            Log.i("BatteryStatsDumper", "Creating UidSipper class for " + name);
            UidSipper uidSipper = new UidSipper(name);
            uidSipper.setTotalPower(in.readDouble());
            if (in.readByte() == (byte) 1) {
                uidSipper.makeUserLaunch();
            }
            if (in.readByte() == (byte) 1) {
                uidSipper.makeNetworkUser();
            }
            int bsNum = in.readInt();
            Log.i("BatteryStatsDumper", "Total number of graph points for this uid is " + bsNum);
            for (j = 0; j < bsNum; j++) {
                uidSipper.addBatterySipStat(in.readString(), in.readByte() == (byte) 1, in.readDouble(), 0.0d);
            }
            int consumerNum = in.readInt();
            if (consumerNum != 0) {
                initializeConsumerList();
                for (j = 0; j < consumerNum; j++) {
                    String time = in.readString();
                    int count = in.readInt();
                    String[] packages = null;
                    if (count != 0) {
                        packages = new String[count];
                        for (int k = 0; k < count; k++) {
                            packages[k] = in.readString();
                        }
                    }
                    addConsumerPackages(time, count, packages);
                }
            }
            this.uidSippers.add(uidSipper);
        }
    }

    public void setFinalPower(double power) {
        this.finalPower = power;
    }

    public double getFinalPower() {
        return this.finalPower;
    }

    public void writeToParcel(Parcel out) {
        out.writeDouble(this.finalPower);
        int uidNum = this.uidSippers.size();
        out.writeInt(uidNum);
        for (int i = 0; i < uidNum; i++) {
            int j;
            UidSipper uSipper = (UidSipper) this.uidSippers.get(i);
            out.writeString(uSipper.name);
            out.writeDouble(uSipper.getTotalPower());
            out.writeByte(uSipper.isUserLaunched() ? (byte) 1 : (byte) 0);
            out.writeByte(uSipper.isUsingNetwork() ? (byte) 1 : (byte) 0);
            int bsNum = uSipper.batterySipStats.size();
            out.writeInt(bsNum);
            for (j = 0; j < bsNum; j++) {
                BatterySipStat bSipStat = (BatterySipStat) uSipper.batterySipStats.get(j);
                out.writeString(bSipStat.getTime());
                out.writeByte(bSipStat.isLcdOn() ? (byte) 1 : (byte) 0);
                out.writeDouble(bSipStat.getPower());
            }
            int consumerNum = this.isConsumerListIncluded ? this.consumerPackages.size() : 0;
            out.writeInt(consumerNum);
            for (j = 0; j < consumerNum; j++) {
                PowerConsumingPackages consumer = (PowerConsumingPackages) this.consumerPackages.get(j);
                out.writeString(consumer.getTime());
                int count = consumer.getPackageCount();
                out.writeInt(count);
                if (count != 0) {
                    String[] packages = consumer.getPackageNames();
                    for (int k = 0; k < count; k++) {
                        out.writeString(packages[k]);
                    }
                }
            }
        }
    }

    public ArrayList<UidSipper> getUsageList() {
        return this.uidSippers;
    }

    public void addUidSipper(UidSipper uidSipper) {
        this.uidSippers.add(uidSipper);
    }

    public boolean isConsumerListIncluded() {
        return this.isConsumerListIncluded;
    }

    public ArrayList<PowerConsumingPackages> getPowerConsumingPackageList() {
        return this.consumerPackages;
    }

    public void initializeConsumerList() {
        this.isConsumerListIncluded = true;
        this.consumerPackages = new ArrayList();
    }

    void addConsumerPackages(String time, String names) {
        this.consumerPackages.add(new PowerConsumingPackages(time, names));
    }

    void addConsumerPackages(String time, int count, String[] names) {
        this.consumerPackages.add(new PowerConsumingPackages(time, count, names));
    }
}
