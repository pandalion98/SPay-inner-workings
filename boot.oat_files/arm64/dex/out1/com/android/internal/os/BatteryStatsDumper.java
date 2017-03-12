package com.android.internal.os;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.BatteryStats.Counter;
import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;
import android.os.BatteryStats.Uid.Pkg;
import android.os.BatteryStats.Uid.Proc;
import android.os.BatteryStats.Uid.Sensor;
import android.os.BatteryStats.Uid.Wakelock;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.IBatteryStats.Stub;
import com.android.internal.telephony.PhoneConstants;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BatteryStatsDumper {
    private static final boolean DEBUG = false;
    private static final String TAG = "BatteryStatsDumper";
    public static BatteryStatsDBHelper db;
    static boolean mIsOnBattery;
    private static int sPrevBatteryLevel;
    public static boolean sScreenOn;
    private IBatteryStats mBatteryInfo;
    private Context mContext;
    private PowerProfile mPowerProfile;
    private BatteryStatsImpl mStats;
    private final int mStatsType = 0;
    private PackageManager pm;
    private SensorManager sensorManager;

    static class PowerObject {
        boolean isSystem;
        long networkUsage;
        String packageName;
        double powerUid;
        long usageTime;
        int wakeUpCount;

        private PowerObject(double power, String packageName, boolean isSystem, long networkUsage, long usageTime, int wakeUpCount) {
            this.powerUid = power;
            this.packageName = packageName;
            this.isSystem = isSystem;
            this.networkUsage = networkUsage;
            this.usageTime = usageTime;
            this.wakeUpCount = wakeUpCount;
        }
    }

    public BatteryStatsDumper(Context context) {
        this.mContext = context;
        this.pm = this.mContext.getPackageManager();
        this.mPowerProfile = new PowerProfile(this.mContext);
        this.mBatteryInfo = Stub.asInterface(ServiceManager.getService("batterystats"));
        this.sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        db = BatteryStatsDBHelper.getInstance(this.mContext);
        if (BatteryStatsDBHelper.sBatteryCapacity == 0.0d) {
            BatteryStatsDBHelper.sBatteryCapacity = this.mPowerProfile.getBatteryCapacity();
        }
    }

    private void load() {
        try {
            byte[] data = this.mBatteryInfo.getStatistics();
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(data, 0, data.length);
            parcel.setDataPosition(0);
            this.mStats = (BatteryStatsImpl) BatteryStatsImpl.CREATOR.createFromParcel(parcel);
            parcel.recycle();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private String mapKnownUIDs(int uid) {
        switch (uid) {
            case 0:
                return "ROOT";
            case 1000:
                return "ANDROID_SYSTEM";
            case 1001:
                return "PHONE";
            case 1002:
                return "com.android.bluetooth";
            case 1013:
                return "MEDIA";
            case 1027:
                return "com.android.nfc";
            default:
                return null;
        }
    }

    public void refreshStats(boolean isWriteReasonScreenChange, boolean screenOn) {
        Log.i(TAG, "In refreshStats isReason Screen ON/OFF: " + isWriteReasonScreenChange);
        load();
        mIsOnBattery = this.mStats.mOnBattery;
        if (this.sensorManager == null) {
            this.sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        }
        double computedPower = 0.0d;
        long mRawRealtime = SystemClock.elapsedRealtime() * 1000;
        double mobilePowerPerPacket = getMobilePowerPerPacket(mRawRealtime);
        double wifiPowerPerPacket = getWifiPowerPerPacket();
        long time = System.currentTimeMillis();
        ArrayList<PowerObject> powerEntries = new ArrayList();
        SparseArray<? extends Uid> uidStats = this.mStats.getUidStats();
        int NU = uidStats.size();
        for (int iu = 0; iu < NU; iu++) {
            String packageName;
            Uid u = (Uid) uidStats.valueAt(iu);
            double power = 0.0d;
            double highestDrain = 0.0d;
            String packageWithHighestDrain = null;
            long cpuTimeMs = 0;
            long cpuFgTimeMs = 0;
            long wakelockTime = 0;
            int wakeUpCount = 0;
            Map<String, ? extends Proc> processStats = u.getProcessStats();
            if (processStats != null && processStats.size() > 0) {
                for (Entry<String, ? extends Proc> ent : processStats.entrySet()) {
                    int cluster;
                    int speed;
                    Proc ps = (Proc) ent.getValue();
                    cpuFgTimeMs += ps.getForegroundTime(0);
                    long tmpCpuTime = ps.getUserTime(0) + ps.getSystemTime(0);
                    int totalTimeAtSpeeds = 0;
                    int numClusters = this.mPowerProfile.getNumCpuClusters();
                    for (cluster = 0; cluster < numClusters; cluster++) {
                        for (speed = 0; speed < this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster); speed++) {
                            totalTimeAtSpeeds = (int) (((long) totalTimeAtSpeeds) + u.getTimeAtCpuSpeed(cluster, speed, 0));
                        }
                    }
                    if (totalTimeAtSpeeds == 0) {
                        totalTimeAtSpeeds = 1;
                    }
                    double processPower = 0.0d;
                    for (cluster = 0; cluster < numClusters; cluster++) {
                        for (speed = 0; speed < this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster); speed++) {
                            processPower += (((double) tmpCpuTime) * (((double) u.getTimeAtCpuSpeed(cluster, speed, 0)) / ((double) totalTimeAtSpeeds))) * this.mPowerProfile.getAveragePowerForCpu(cluster, speed);
                        }
                    }
                    cpuTimeMs += tmpCpuTime;
                    if (processPower != 0.0d) {
                        power += processPower;
                        if (packageWithHighestDrain != null) {
                            if (!packageWithHighestDrain.startsWith(PhoneConstants.APN_TYPE_ALL)) {
                                if (highestDrain < processPower && !((String) ent.getKey()).startsWith(PhoneConstants.APN_TYPE_ALL)) {
                                    highestDrain = processPower;
                                    packageWithHighestDrain = (String) ent.getKey();
                                }
                            }
                        }
                        highestDrain = processPower;
                        packageWithHighestDrain = (String) ent.getKey();
                    }
                }
            }
            power /= 3600000.0d;
            if (cpuFgTimeMs > cpuTimeMs) {
                if (cpuFgTimeMs > 10000 + cpuTimeMs) {
                    Log.d(TAG, "WARNING! Cputime is more than 10 seconds behind Foreground time");
                }
                cpuTimeMs = cpuFgTimeMs;
            }
            long uidUsageTime = 0 + cpuTimeMs;
            for (Entry<String, ? extends Wakelock> wakelockEntry : u.getWakelockStats().entrySet()) {
                Timer timer = ((Wakelock) wakelockEntry.getValue()).getWakeTime(0);
                if (timer != null) {
                    wakelockTime += timer.getTotalTimeLocked(mRawRealtime, 0);
                }
            }
            wakelockTime /= 1000;
            power += (((double) wakelockTime) * this.mPowerProfile.getAveragePower(PowerProfile.POWER_CPU_AWAKE)) / 3600000.0d;
            uidUsageTime += wakelockTime;
            long mobileRx = u.getNetworkActivityPackets(0, 0);
            long mobileTx = u.getNetworkActivityPackets(1, 0);
            power += ((double) (mobileRx + mobileTx)) * mobilePowerPerPacket;
            uidUsageTime += u.getMobileRadioActiveTime(0);
            long wifiRx = u.getNetworkActivityPackets(2, 0);
            long wifiTx = u.getNetworkActivityPackets(3, 0);
            long wifiRunningTimeMs = u.getWifiRunningTime(mRawRealtime, 0) / 1000;
            long wifiScanTimeMs = u.getWifiScanTime(mRawRealtime, 0) / 1000;
            power = ((power + (((double) (wifiRx + wifiTx)) * wifiPowerPerPacket)) + ((((double) wifiRunningTimeMs) * this.mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_ON)) / 3600000.0d)) + ((((double) wifiScanTimeMs) * this.mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_SCAN)) / 3600000.0d);
            uidUsageTime = (uidUsageTime + wifiRunningTimeMs) + wifiScanTimeMs;
            for (int bin = 0; bin < 5; bin++) {
                long batchScanTimeMs = u.getWifiBatchedScanTime(bin, mRawRealtime, 0) / 1000;
                power += (((double) batchScanTimeMs) * this.mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_BATCHED_SCAN, bin)) / 3600000.0d;
                uidUsageTime += batchScanTimeMs;
            }
            SparseArray<? extends Sensor> sensorStats = u.getSensorStats();
            int NSE = sensorStats.size();
            for (int ise = 0; ise < NSE; ise++) {
                Sensor sensor = (Sensor) sensorStats.valueAt(ise);
                int sensorHandle = sensorStats.keyAt(ise);
                long sensorTime = sensor.getSensorTime().getTotalTimeLocked(mRawRealtime, 0) / 1000;
                double multiplier = 0.0d;
                switch (sensorHandle) {
                    case DeviceRootKeyServiceManager.ERR_SERVICE_ERROR /*-10000*/:
                        multiplier = this.mPowerProfile.getAveragePower(PowerProfile.POWER_GPS_ON);
                        if (multiplier >= 30.0d) {
                            break;
                        }
                        multiplier = 30.1d;
                        break;
                    default:
                        for (android.hardware.Sensor s : this.sensorManager.getSensorList(-1)) {
                            if (s.getHandle() == sensorHandle) {
                                multiplier = (double) s.getPower();
                                break;
                            }
                        }
                        break;
                }
                power += (((double) sensorTime) * multiplier) / 3600000.0d;
                uidUsageTime += sensorTime;
            }
            if (power < 0.0d) {
                Log.i(TAG, " calculated power is negative");
                power = 0.0d;
            }
            int thisUid = u.getUid();
            String[] packages = this.pm.getPackagesForUid(thisUid);
            if (packages == null || packages.length <= 0) {
                packageName = packageWithHighestDrain;
            } else {
                packageName = packages[0];
            }
            int userId = UserHandle.getUserId(thisUid);
            boolean isSystem = false;
            if (thisUid < 10000) {
                isSystem = true;
                String specialPackageName = mapKnownUIDs(thisUid);
                if (specialPackageName != null) {
                    packageName = specialPackageName;
                }
            }
            long totalNetworkUsage = ((wifiRx + wifiTx) + mobileRx) + mobileTx;
            ArrayMap<String, ? extends Pkg> packageStats = u.getPackageStats();
            double powerPerWakeUp = this.mPowerProfile.getAveragePower(PowerProfile.POWER_WAKEUP);
            for (int ipkg = packageStats.size() - 1; ipkg >= 0; ipkg--) {
                wakeUpCount = 0;
                ArrayMap<String, ? extends Counter> alarms = ((Pkg) packageStats.valueAt(ipkg)).getWakeupAlarmStats();
                for (int iwa = alarms.size() - 1; iwa >= 0; iwa--) {
                    wakeUpCount += ((Counter) alarms.valueAt(iwa)).getCountLocked(0);
                }
            }
            power += ((double) wakeUpCount) * powerPerWakeUp;
            computedPower += power;
            if (packageName != null && userId == 0) {
                powerEntries.add(new PowerObject(power, packageName, isSystem, totalNetworkUsage, uidUsageTime, wakeUpCount));
            }
        }
        computedPower += addScreenUsage(mRawRealtime);
        double totalPower = computedPower;
        double mMinDrainedPower = (((double) this.mStats.getLowDischargeAmountSinceCharge()) * this.mPowerProfile.getBatteryCapacity()) / 100.0d;
        if (mMinDrainedPower > computedPower) {
            totalPower = mMinDrainedPower;
        }
        int currentBatteryLevel = this.mStats.mCurrentBatteryLevel;
        int delta = sPrevBatteryLevel - currentBatteryLevel;
        Log.i(TAG, "Previous Battery Level: " + sPrevBatteryLevel + " Current Level: " + currentBatteryLevel + " Delta: " + delta);
        if (delta < 0) {
            delta = 0;
        }
        sPrevBatteryLevel = currentBatteryLevel;
        db.addStatsToDB(isWriteReasonScreenChange, screenOn, time, powerEntries, delta, currentBatteryLevel, totalPower, this.mStats.getHighDischargeAmountSinceCharge());
        Log.i(TAG, "Writing to database completed");
    }

    private double getMobilePowerPerPacket(long rawRealtime) {
        double MOBILE_POWER = this.mPowerProfile.getAveragePower(PowerProfile.POWER_RADIO_ACTIVE) / 3600.0d;
        long mobileData = this.mStats.getNetworkActivityPackets(0, 0) + this.mStats.getNetworkActivityPackets(1, 0);
        long radioDataUptimeMs = this.mStats.getMobileRadioActiveTime(rawRealtime, 0) / 1000;
        double mobilePps = (mobileData == 0 || radioDataUptimeMs == 0) ? 12.20703125d : ((double) mobileData) / ((double) radioDataUptimeMs);
        return (MOBILE_POWER / mobilePps) / 3600.0d;
    }

    private double getMobilePowerPerMs() {
        return this.mPowerProfile.getAveragePower(PowerProfile.POWER_RADIO_ACTIVE) / 3600000.0d;
    }

    private double getWifiPowerPerPacket() {
        return ((this.mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_ACTIVE) / 3600.0d) / 61.03515625d) / 3600.0d;
    }

    private double addScreenUsage(long rawRealtime) {
        double power = 0.0d + (((double) (this.mStats.getScreenOnTime(rawRealtime, 0) / 1000)) * this.mPowerProfile.getAveragePower(PowerProfile.POWER_SCREEN_ON));
        double screenFullPower = this.mPowerProfile.getAveragePower(PowerProfile.POWER_SCREEN_FULL);
        for (int i = 0; i < 5; i++) {
            power += ((((double) (((float) i) + 0.5f)) * screenFullPower) / 5.0d) * ((double) (this.mStats.getScreenBrightnessTime(i, rawRealtime, 0) / 1000));
        }
        return power / 3600000.0d;
    }

    public void updatePrevBatteryValue() {
        sPrevBatteryLevel = getBatteryLevel();
    }

    public int getBatteryLevel() {
        byte batteryLevel = getByteFromFile("/sys/class/power_supply/battery/capacity");
        if (batteryLevel < (byte) 0) {
            return 100;
        }
        return batteryLevel;
    }

    private byte getByteFromFile(String path) {
        byte ret;
        Throwable th;
        BufferedReader in = null;
        try {
            BufferedReader in2 = new BufferedReader(new FileReader(path), 8);
            try {
                String s = in2.readLine();
                if (s != null) {
                    s = s.trim();
                }
                in2.close();
                ret = Byte.valueOf(s).byteValue();
                if (in2 != null) {
                    try {
                        in2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        in = in2;
                    }
                }
                in = in2;
            } catch (IOException e2) {
                in = in2;
                ret = (byte) -1;
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                return ret;
            } catch (Throwable th2) {
                th = th2;
                in = in2;
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            ret = (byte) -1;
            if (in != null) {
                in.close();
            }
            return ret;
        } catch (Throwable th3) {
            th = th3;
            if (in != null) {
                in.close();
            }
            throw th;
        }
        return ret;
    }

    public void deleteTableForApp(String appName) {
        db.deleteTableForApp(appName);
    }

    public void modifyThresholdIfNecessary() {
        db.modifyThresholdIfNecessary();
    }

    public void modifyHealingCampThresholdIfNecessary() {
        db.modifyHealingCampThresholdIfNecessary();
    }

    public long computeSavedTimeAfterKillingPackage(String packageName) {
        try {
            int dischageStepsNum;
            long[] dischageSteps;
            int batteryLevel;
            synchronized (this.mStats) {
                dischageStepsNum = this.mStats.mDischargeStepTracker.mNumStepDurations;
                dischageSteps = Arrays.copyOf(this.mStats.mDischargeStepTracker.mStepDurations, dischageStepsNum);
                batteryLevel = this.mStats.mCurrentBatteryLevel;
            }
            Log.i(TAG, "computeSavedTimeAfterKillingPackage:: batteryLevel:" + batteryLevel + " dischageStepsNum: " + dischageStepsNum);
            if (dischageStepsNum <= 0) {
                return -1;
            }
            long total = 0;
            for (int i = 0; i < dischageStepsNum; i++) {
                total += dischageSteps[i] & 1099511627775L;
            }
            double consumptionRateTotal = (double) (((long) ((dischageStepsNum * 3600) * 1000)) / total);
            Log.i(TAG, "computeSavedTimeAfterKillingPackage:: consumptionRateTotal:" + consumptionRateTotal);
            if (consumptionRateTotal == 0.0d) {
                return -1;
            }
            double newConsumptionRateTotal;
            double consumptionRateOfPackage = db.getAverageLevelDropPerHour(packageName);
            if (consumptionRateTotal > consumptionRateOfPackage) {
                newConsumptionRateTotal = consumptionRateTotal - consumptionRateOfPackage;
            } else {
                newConsumptionRateTotal = consumptionRateOfPackage - consumptionRateTotal;
            }
            Log.i(TAG, "New battery consumption rate = " + newConsumptionRateTotal + " Previous consumptionRateOfPackage: " + consumptionRateOfPackage);
            long i2 = (long) ((((((double) batteryLevel) * consumptionRateOfPackage) * 3600.0d) * 1000.0d) / (consumptionRateTotal * newConsumptionRateTotal));
            Log.i(TAG, "computeSavedTimeAfterKillingPackage:: returning i:" + i2);
            return i2;
        } catch (Exception e) {
            Log.i(TAG, "computeSavedTimeAfterKillingPackage::  Exception");
            return 0;
        }
    }

    public int getBatteryTotalCapacity() {
        return (int) this.mPowerProfile.getBatteryCapacity();
    }

    public void handleTimeChange() {
        db.handleTimeChange();
    }

    public void dumpConsumingAppDetails(PrintWriter pw) {
        db.dumpConsumingAppDetails(pw);
    }

    public void deleteRecordsExceptLatest(int maxNumOfItems) {
        if (db != null) {
            db.deleteRecordsExceptLatest(maxNumOfItems);
        }
    }

    public void abusiveDetectionFromHCamp(List<String> list, String type) {
        if (db != null) {
            db.abusiveDetectionFromHCamp(list, type);
        }
    }

    public void abusiveDetectionFromHCamp(String packageName, String type, String metaData) {
        if (db != null) {
            db.abusiveDetectionFromHCamp(packageName, type, metaData);
        }
    }
}
