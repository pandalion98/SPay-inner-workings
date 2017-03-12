package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.Uid;

public class BluetoothPowerCalculator extends PowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "BluetoothPowerCalculator";
    private final double mIdleMa;
    private final double mRxMa;
    private final double mTxMa;

    public BluetoothPowerCalculator(PowerProfile profile) {
        this.mIdleMa = profile.getAveragePower(PowerProfile.POWER_BLUETOOTH_CONTROLLER_IDLE);
        this.mRxMa = profile.getAveragePower(PowerProfile.POWER_BLUETOOTH_CONTROLLER_RX);
        this.mTxMa = profile.getAveragePower(PowerProfile.POWER_BLUETOOTH_CONTROLLER_TX);
    }

    public void calculateApp(BatterySipper app, Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
    }

    public void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        long idleTimeMs = stats.getBluetoothControllerActivity(0, statsType);
        long txTimeMs = stats.getBluetoothControllerActivity(2, statsType);
        long rxTimeMs = stats.getBluetoothControllerActivity(1, statsType);
        long totalTimeMs = (idleTimeMs + txTimeMs) + rxTimeMs;
        double powerMah = ((double) stats.getBluetoothControllerActivity(3, statsType)) / 3600000.0d;
        if (powerMah == 0.0d) {
            powerMah = (((((double) idleTimeMs) * this.mIdleMa) + (((double) rxTimeMs) * this.mRxMa)) + (((double) txTimeMs) * this.mTxMa)) / 3600000.0d;
        }
        app.usagePowerMah = powerMah;
        app.usageTimeMs = totalTimeMs;
    }
}
