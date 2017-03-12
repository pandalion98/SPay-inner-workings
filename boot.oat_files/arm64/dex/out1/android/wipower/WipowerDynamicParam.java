package android.wipower;

import android.os.SystemProperties;
import android.util.Log;

public class WipowerDynamicParam {
    private static final float IREG_ADC_TO_mA_RATIO = 4.765625f;
    private static final String LOGTAG = "WipowerDynamicParam";
    private static final int LSB_MASK = 255;
    private static final int MSB_MASK = 65280;
    private static final byte OVP_BIT = Byte.MIN_VALUE;
    private static final short OVP_THRESHHOLD_VAL = (short) 21500;
    private static final float VREG_ADC_TO_mV_RATIO = 95.3125f;
    private static boolean sDebug = false;
    private byte mAlert = (byte) 0;
    private short mMaxRectVoltageDyn = (short) 0;
    private short mMinRectVoltageDyn = (short) 0;
    private byte mOptValidity = (byte) 0;
    private short mOutputCurrent = (short) 0;
    private short mOutputVoltage = (short) 0;
    private short mRectCurrent = (short) 0;
    private short mRectVoltage = (short) 0;
    private short mReserved1 = (short) 0;
    private byte mReserved2 = (byte) 0;
    private short mSetRectVoltageDyn = (short) 0;
    private byte mTemperature = (byte) 0;

    private static String toHex(int num) {
        return String.format("0x%8s", new Object[]{Integer.toHexString(num)}).replace(' ', '0');
    }

    void print() {
        sDebug = SystemProperties.getBoolean("persist.a4wp.logging", false);
        if (sDebug) {
            Log.v(LOGTAG, "mOptValidity " + toHex(this.mOptValidity) + "mRectVoltage " + toHex(this.mRectVoltage) + "mRectCurrent " + toHex(this.mRectCurrent) + "mOutputVoltage " + toHex(this.mOutputVoltage));
        }
        if (sDebug) {
            Log.v(LOGTAG, "mOutputCurrent " + toHex(this.mOutputCurrent) + "mTemperature " + toHex(this.mTemperature) + "mMinRectVoltageDyn " + toHex(this.mMinRectVoltageDyn) + "mMaxRectVoltageDyn " + toHex(this.mMaxRectVoltageDyn));
        }
        if (sDebug) {
            Log.v(LOGTAG, "mSetRectVoltageDyn " + toHex(this.mSetRectVoltageDyn) + "mAlert " + toHex(this.mAlert) + "mReserved1 " + toHex(this.mReserved1) + "mReserved2 " + toHex(this.mReserved2));
        }
    }

    public byte[] getValue() {
        byte[] res = new byte[20];
        print();
        res[0] = this.mOptValidity;
        res[1] = (byte) (this.mRectVoltage & 255);
        res[2] = (byte) ((this.mRectVoltage & 65280) >> 8);
        res[3] = (byte) (this.mRectCurrent & 255);
        res[4] = (byte) ((this.mRectCurrent & 65280) >> 8);
        res[5] = (byte) (this.mOutputVoltage & 255);
        res[6] = (byte) ((this.mOutputVoltage & 65280) >> 8);
        res[7] = (byte) (this.mOutputCurrent & 255);
        res[8] = (byte) ((this.mOutputCurrent & 65280) >> 8);
        res[9] = this.mTemperature;
        res[10] = (byte) (this.mMinRectVoltageDyn & 255);
        res[11] = (byte) ((this.mMinRectVoltageDyn & 65280) >> 8);
        res[12] = (byte) (this.mSetRectVoltageDyn & 255);
        res[13] = (byte) ((this.mSetRectVoltageDyn & 65280) >> 8);
        res[14] = (byte) (this.mMaxRectVoltageDyn & 255);
        res[15] = (byte) ((this.mMaxRectVoltageDyn & 65280) >> 8);
        res[16] = this.mAlert;
        if ((res[16] & -128) == -128 && this.mRectVoltage < OVP_THRESHHOLD_VAL) {
            res[16] = (byte) (res[16] & 127);
        }
        Log.i(LOGTAG, "mPruDynamicParam.getValue");
        return res;
    }

    public void resetValues() {
        this.mOptValidity = (byte) 0;
        this.mRectVoltage = (short) 0;
        this.mRectCurrent = (short) 0;
        this.mOutputVoltage = (short) 0;
        this.mOutputCurrent = (short) 0;
        this.mTemperature = (byte) 0;
        this.mMinRectVoltageDyn = (short) 0;
        this.mMaxRectVoltageDyn = (short) 0;
        this.mSetRectVoltageDyn = (short) 0;
        this.mAlert = (byte) 0;
        this.mReserved1 = (short) 0;
        this.mReserved2 = (byte) 0;
    }

    public static short toUnsigned(byte b) {
        return (short) (b & 255);
    }

    public static short VREG_ADC_TO_mV(short adc) {
        return (short) ((int) (((float) adc) * VREG_ADC_TO_mV_RATIO));
    }

    public static short IREG_ADC_TO_mA(short adc) {
        return (short) ((int) (((float) adc) * IREG_ADC_TO_mA_RATIO));
    }

    public void setValue(byte[] value) {
        resetValues();
        this.mOptValidity = value[0];
        this.mRectVoltage = toUnsigned(value[1]);
        this.mRectVoltage = (short) (this.mRectVoltage | ((short) (toUnsigned(value[2]) << 8)));
        this.mRectCurrent = toUnsigned(value[3]);
        this.mRectCurrent = (short) (this.mRectCurrent | ((short) (toUnsigned(value[4]) << 8)));
        this.mOutputVoltage = toUnsigned(value[5]);
        this.mOutputVoltage = (short) (this.mOutputVoltage | ((short) (toUnsigned(value[6]) << 8)));
        this.mOutputCurrent = toUnsigned(value[7]);
        this.mOutputCurrent = (short) (this.mOutputCurrent | ((short) (toUnsigned(value[8]) << 8)));
        this.mTemperature = value[9];
        this.mMinRectVoltageDyn = toUnsigned(value[10]);
        this.mMinRectVoltageDyn = (short) (this.mMinRectVoltageDyn | ((short) (toUnsigned(value[11]) << 8)));
        this.mSetRectVoltageDyn = toUnsigned(value[12]);
        this.mSetRectVoltageDyn = (short) (this.mSetRectVoltageDyn | ((short) (toUnsigned(value[13]) << 8)));
        this.mMaxRectVoltageDyn = toUnsigned(value[14]);
        this.mMaxRectVoltageDyn = (short) (this.mMaxRectVoltageDyn | ((short) (toUnsigned(value[15]) << 8)));
        this.mAlert = value[16];
        this.mReserved1 = toUnsigned(value[17]);
        this.mReserved1 = (short) (toUnsigned(value[18]) << 8);
        this.mReserved2 = value[19];
        Log.i(LOGTAG, "mPruDynamicParam.setAppValue");
        print();
    }
}
