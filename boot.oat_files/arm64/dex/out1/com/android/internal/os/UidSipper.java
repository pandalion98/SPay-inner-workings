package com.android.internal.os;

import java.util.ArrayList;

public final class UidSipper {
    public final ArrayList<BatterySipStat> batterySipStats;
    private boolean isUsingNetwork;
    public final String name;
    private double totalPower;
    private boolean userLaunch;

    public static final class BatterySipStat {
        boolean lcdOn;
        double power;
        String time;
        double totalPower;

        private BatterySipStat(String time, boolean lcdOn, double power, double totalPower) {
            this.time = time;
            this.lcdOn = lcdOn;
            this.power = power;
            this.totalPower = totalPower;
        }

        public String getTime() {
            return this.time;
        }

        public boolean isLcdOn() {
            return this.lcdOn;
        }

        public double getPower() {
            return this.power;
        }

        public double getTotalPower() {
            return this.totalPower;
        }
    }

    UidSipper(String name) {
        this.name = name;
        this.batterySipStats = new ArrayList();
    }

    UidSipper(String name, String time, double power, double totalPower) {
        this(name);
        addBatterySipStat(time, false, power, totalPower);
    }

    void setTotalPower(double totalpower) {
        this.totalPower = totalpower;
    }

    void makeUserLaunch() {
        this.userLaunch = true;
    }

    void makeNetworkUser() {
        this.isUsingNetwork = true;
    }

    public double getTotalPower() {
        return this.totalPower;
    }

    public boolean isUserLaunched() {
        return this.userLaunch;
    }

    public boolean isUsingNetwork() {
        return this.isUsingNetwork;
    }

    void addBatterySipStat(long time, boolean lcdOn, double power, double totalPower) {
        this.batterySipStats.add(new BatterySipStat(String.valueOf(time), lcdOn, power, totalPower));
    }

    void addBatterySipStat(String time, boolean lcdOn, double power, double totalPower) {
        this.batterySipStats.add(new BatterySipStat(time, lcdOn, power, totalPower));
    }
}
