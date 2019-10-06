package com.sec.android.app.hwmoduletest.modules;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.view.View;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;

public class ModulePower extends ModuleObject {
    public static final int AUTO_BRIGHTNESS_MODE_OFF = 0;
    public static final int AUTO_BRIGHTNESS_MODE_ON = 1;
    public static final int BUTTON_KEY_LIGHT_ALWAYS_ON = -1;
    public static final int BUTTON_KEY_LIGHT_OFF = 0;
    public static final int BUTTON_KEY_LIGHT_ON_1500 = 1500;
    public static final int BUTTON_KEY_LIGHT_ON_3000 = 3000;
    public static final int BUTTON_KEY_LIGHT_ON_6000 = 6000;
    private static ModulePower mInstance = null;
    private WakeLock mPartialWakeLock = null;
    private WakeLock mWakeLock = null;

    private ModulePower(Context context) {
        super(context, "ModulePower");
        LtUtil.log_i(this.CLASS_NAME, "ModulePower", "Create ModulePower");
    }

    public static ModulePower instance(Context context) {
        if (mInstance == null) {
            mInstance = new ModulePower(context);
        }
        return mInstance;
    }

    public void sleep() {
        LtUtil.log_i(this.CLASS_NAME, "sleep", "Power Sleep");
        doWakeLock(false);
        doPartialWakeLock(false);
        ((PowerManager) getSystemService("power")).goToSleep(SystemClock.uptimeMillis());
    }

    public void doWakeLock(boolean wake) {
        StringBuilder sb = new StringBuilder();
        sb.append("wake=");
        sb.append(wake);
        LtUtil.log_i(this.CLASS_NAME, "doWakeLock", sb.toString());
        if (wake) {
            if (this.mWakeLock == null) {
                this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(805306394, this.CLASS_NAME);
                this.mWakeLock.acquire();
            }
        } else if (this.mWakeLock != null) {
            if (this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
    }

    public void doPartialWakeLock(boolean wake) {
        StringBuilder sb = new StringBuilder();
        sb.append("wake=");
        sb.append(wake);
        LtUtil.log_i(this.CLASS_NAME, "doPartialWakeLock", sb.toString());
        if (wake) {
            if (this.mPartialWakeLock == null) {
                PowerManager pm = (PowerManager) getSystemService("power");
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.CLASS_NAME);
                sb2.append("Partial");
                this.mPartialWakeLock = pm.newWakeLock(1, sb2.toString());
            }
            this.mPartialWakeLock.acquire();
        } else if (this.mPartialWakeLock != null) {
            if (this.mPartialWakeLock.isHeld()) {
                this.mPartialWakeLock.release();
            }
            this.mPartialWakeLock = null;
        }
    }

    public void sendAlarmManagerOnOff(boolean alarm) {
        String str = "android.intent.action.START_FACTORY_TEST";
        String str2 = "android.intent.action.STOP_FACTORY_TEST";
        if (!alarm) {
            LtUtil.log_i(this.CLASS_NAME, "sendAlarmOnOffIntent", "sendAlarmOnOffIntentandroid.intent.action.START_FACTORY_TEST");
            sendBroadcast(new Intent("android.intent.action.START_FACTORY_TEST"));
            return;
        }
        LtUtil.log_i(this.CLASS_NAME, "sendAlarmOnOffIntent", "sendAlarmOnOffIntentandroid.intent.action.STOP_FACTORY_TEST");
        sendBroadcast(new Intent("android.intent.action.STOP_FACTORY_TEST"));
    }

    public void setDisplayColor(View view, int color) {
        StringBuilder sb = new StringBuilder();
        sb.append("color=");
        sb.append(color);
        LtUtil.log_i(this.CLASS_NAME, "setDisplayColor", sb.toString());
        view.setBackgroundColor(color);
    }

    public void setBrightness(int brightness) {
        StringBuilder sb = new StringBuilder();
        sb.append("brightness=");
        sb.append(brightness);
        LtUtil.log_i(this.CLASS_NAME, "setBrightness", sb.toString());
        try {
            if (VERSION.SDK_INT >= 28) {
                DisplayManager mDisplayManager = (DisplayManager) getSystemService("display");
                if (mDisplayManager != null) {
                    mDisplayManager.getClass().getMethod("setTemporaryBrightness", new Class[]{Integer.TYPE}).invoke(mDisplayManager, new Object[]{Integer.valueOf(brightness)});
                }
                return;
            }
            PowerManager mPowerManager = (PowerManager) getSystemService("power");
            if (mPowerManager != null) {
                mPowerManager.getClass().getMethod("setBacklightBrightness", new Class[]{Integer.TYPE}).invoke(mPowerManager, new Object[]{Integer.valueOf(brightness)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getBrightness() {
        int brightness = System.getInt(getContentResolver(), "screen_brightness", 0);
        StringBuilder sb = new StringBuilder();
        sb.append("brightness=");
        sb.append(brightness);
        LtUtil.log_i(this.CLASS_NAME, "getBrightness", sb.toString());
        return brightness;
    }

    public void setScreenState(boolean b) {
        StringBuilder sb = new StringBuilder();
        sb.append("state=");
        sb.append(b);
        LtUtil.log_i(this.CLASS_NAME, "setScreenState", sb.toString());
        PowerManager pm = (PowerManager) getSystemService("power");
        if (b) {
            doPartialWakeLock(false);
            pm.userActivity(SystemClock.uptimeMillis(), false);
            return;
        }
        doPartialWakeLock(true);
        pm.goToSleep(SystemClock.uptimeMillis());
    }

    public void setScreenBrightnessMode(int mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("mode=");
        sb.append(mode);
        LtUtil.log_i(this.CLASS_NAME, "setScreenBrightnessMode", sb.toString());
        System.putInt(getContentResolver(), "screen_brightness_mode", mode);
    }

    public int getScreenBrightnessMode() {
        int mode = System.getInt(getContentResolver(), "screen_brightness_mode", -1);
        StringBuilder sb = new StringBuilder();
        sb.append("mode=");
        sb.append(mode);
        LtUtil.log_i(this.CLASS_NAME, "getScreenBrightnessMode", sb.toString());
        return mode;
    }

    public String readLux() {
        String lux = Kernel.read(Kernel.LIGHT_SENSOR_LUX);
        StringBuilder sb = new StringBuilder();
        sb.append("lux=");
        sb.append(lux);
        LtUtil.log_i(this.CLASS_NAME, "readLux", sb.toString());
        return lux;
    }

    public boolean isScreenOn() {
        return ((PowerManager) getSystemService("power")).isScreenOn();
    }

    public String readBatteryStatus() {
        return "";
    }

    public String readBatteryVoltage() {
        String voltage = Kernel.read(Kernel.BATTERY_VOLT);
        if (voltage == null) {
            return null;
        }
        String voltage2 = voltage.length() < 3 ? voltage.trim() : voltage.substring(0, 3);
        StringBuilder sb = new StringBuilder();
        sb.append("voltage=");
        sb.append(voltage2);
        LtUtil.log_i(this.CLASS_NAME, "readBatteryVoltage", sb.toString());
        return String.valueOf(Float.valueOf(voltage2).floatValue() / 100.0f);
    }

    public boolean resetFuelGaugeIC() {
        LtUtil.log_i(this.CLASS_NAME, "resetFuelGaugeIC", "Fuel Gauge IC reset");
        return Kernel.write(Kernel.FUEL_GAUGE_RESET, EgisFingerprint.MAJOR_VERSION);
    }

    public String readBatteryTemp() {
        String temp;
        int data;
        String temp2 = Kernel.read(Kernel.BATTERY_TEMP);
        StringBuilder sb = new StringBuilder();
        sb.append("sysfs temp=");
        sb.append(temp2);
        LtUtil.log_i(this.CLASS_NAME, "readBatteryTemp", sb.toString());
        if (temp2 != null) {
            try {
                if (temp2.length() >= 3) {
                    data = Integer.parseInt(temp2) / 10;
                } else {
                    data = Integer.parseInt(temp2.substring(0, 2));
                }
                temp = String.valueOf(data);
            } catch (Exception e) {
                e.printStackTrace();
                temp = "0";
            }
        } else {
            temp = "0";
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("return temp=");
        sb2.append(temp);
        LtUtil.log_i(this.CLASS_NAME, "readBatteryTemp", sb2.toString());
        return temp;
    }

    @Deprecated
    public String readBatteryTempRTRChip() {
        return null;
    }

    @Deprecated
    public String readBatteryTempRF2Chip() {
        return null;
    }

    public String readBatteryTempAdc() {
        String adc = Kernel.read(Kernel.BATTERY_TEMP_ADC);
        StringBuilder sb = new StringBuilder();
        sb.append("adc=");
        sb.append(adc);
        LtUtil.log_i(this.CLASS_NAME, "readBatteryTempAdc", sb.toString());
        return adc;
    }

    public String readApChipTemp() {
        String adc = Kernel.read(Kernel.APCHIP_TEMP_ADC);
        StringBuilder sb = new StringBuilder();
        sb.append("adc=");
        sb.append(adc);
        LtUtil.log_i(this.CLASS_NAME, "readApChipTemp", sb.toString());
        return adc;
    }

    @Deprecated
    public String readBatteryTempAdcRTRChip() {
        return null;
    }

    @Deprecated
    public String readBatteryTempAdcRF2Chip() {
        return null;
    }

    public String readBatteryAdcCal() {
        String adc_cal = Kernel.read(Kernel.BATTERY_VOLT_ADC_CAL);
        StringBuilder sb = new StringBuilder();
        sb.append("adc_cal=");
        sb.append(adc_cal);
        LtUtil.log_i(this.CLASS_NAME, "readBatteryAdcCal", sb.toString());
        return adc_cal;
    }

    public void writeBatteryAdcCal(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("value=");
        sb.append(value);
        LtUtil.log_i(this.CLASS_NAME, "writeBatteryAdcCal", sb.toString());
        Kernel.write(Kernel.BATTERY_VOLT_ADC_CAL, value);
    }

    public String readBatteryCal() {
        String cal = Kernel.read(Kernel.BATTERY_VOLT_ADC_CAL);
        StringBuilder sb = new StringBuilder();
        sb.append("cal=");
        sb.append(cal);
        LtUtil.log_i(this.CLASS_NAME, "readBatteryCal", sb.toString());
        return cal;
    }

    public void writeBatteryCal(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("value=");
        sb.append(value);
        LtUtil.log_i(this.CLASS_NAME, "writeBatteryCal", sb.toString());
        Kernel.write(Kernel.BATTERY_VOLT_ADC_CAL, value);
    }
}
