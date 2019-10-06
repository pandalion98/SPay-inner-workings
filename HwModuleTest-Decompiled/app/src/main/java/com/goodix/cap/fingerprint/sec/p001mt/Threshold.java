package com.goodix.cap.fingerprint.sec.p001mt;

import com.goodix.cap.fingerprint.Constants.MilanA;
import com.goodix.cap.fingerprint.Constants.MilanB;
import com.goodix.cap.fingerprint.Constants.MilanC;
import com.goodix.cap.fingerprint.Constants.MilanE;
import com.goodix.cap.fingerprint.Constants.MilanF;
import com.goodix.cap.fingerprint.Constants.MilanFN;
import com.goodix.cap.fingerprint.Constants.MilanG;
import com.goodix.cap.fingerprint.Constants.MilanH;
import com.goodix.cap.fingerprint.Constants.MilanHU;
import com.goodix.cap.fingerprint.Constants.MilanJ;
import com.goodix.cap.fingerprint.Constants.MilanK;
import com.goodix.cap.fingerprint.Constants.MilanL;
import com.goodix.cap.fingerprint.Constants.Oswego;

/* renamed from: com.goodix.cap.fingerprint.sec.mt.Threshold */
public class Threshold {
    public float allTiltAngle;
    public int averagePixelDiff;
    public short avgDiffVal;
    public int badBointNum;
    public int badLine;
    public int badPixelNum;
    public float blockTiltAngleMax;
    public int chipId;
    public int hbdAvgMax;
    public int hbdAvgMin;
    public int hbdElectricityMax;
    public int hbdElectricityMin;
    public int imageQuality;
    public int inCircle;
    public int localBadPixelNum;
    public int localBigBadPixel;
    public int localSmallBadPixel;
    public short localWorst;
    public int osdTouchedMax;
    public int osdTouchedMin;
    public int osdUntouched;
    public int overSaturated;
    public int saturated;
    public int sensorMaxPerformance;
    public int sensorMaxSnr;
    public int sensorMinPerformance;
    public int sensorMinSnr;
    public int sensorNoise;
    public float sensorStableFactor;
    public int singular;
    public String spiFwVersion;
    public long totalTime;
    public int underSaturated;
    public int validArea;

    public Threshold() {
        this.badBointNum = 0;
        this.totalTime = 0;
        this.imageQuality = 0;
        this.validArea = 0;
        this.badPixelNum = 0;
        this.localBadPixelNum = 0;
        this.allTiltAngle = 0.0f;
        this.blockTiltAngleMax = 0.0f;
        this.localWorst = 0;
        this.singular = 0;
        this.inCircle = 0;
        this.osdUntouched = 0;
        this.osdTouchedMin = 0;
        this.osdTouchedMax = 0;
        this.hbdAvgMax = 0;
        this.hbdAvgMin = 0;
        this.hbdElectricityMin = 0;
        this.hbdElectricityMax = 0;
        this.localSmallBadPixel = 0;
        this.localBigBadPixel = 0;
        this.averagePixelDiff = 0;
        this.badLine = 0;
        this.saturated = 0;
        this.underSaturated = 0;
        this.overSaturated = 0;
        this.sensorMinPerformance = 0;
        this.sensorMaxPerformance = 0;
        this.sensorMinSnr = 0;
        this.sensorMaxSnr = 0;
        this.sensorNoise = 0;
        this.sensorStableFactor = 0.0f;
        this.imageQuality = 15;
        this.validArea = 65;
        this.totalTime = 400;
    }

    public Threshold(int chipType) {
        int i = chipType;
        this();
        switch (i) {
            case 0:
            case 3:
            case 6:
                this.spiFwVersion = Oswego.TEST_SPI_GFX16;
                this.badBointNum = 30;
                this.badPixelNum = 45;
                this.avgDiffVal = 800;
                this.allTiltAngle = 0.1793f;
                this.blockTiltAngleMax = 0.4788f;
                this.localSmallBadPixel = 12;
                this.localBigBadPixel = 5;
                this.averagePixelDiff = 1200;
                return;
            case 1:
            case 2:
            case 4:
            case 5:
                this.spiFwVersion = Oswego.TEST_SPI_GFX18;
                this.badBointNum = 30;
                this.badPixelNum = 45;
                this.avgDiffVal = 800;
                this.allTiltAngle = 0.1793f;
                this.blockTiltAngleMax = 0.4788f;
                this.localSmallBadPixel = 12;
                this.localBigBadPixel = 5;
                this.averagePixelDiff = 1200;
                return;
            case 7:
                this.chipId = MilanE.TEST_SPI_CHIP_ID;
                this.badBointNum = 30;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 8:
                this.chipId = MilanF.TEST_SPI_CHIP_ID;
                this.badBointNum = 35;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 9:
                this.chipId = MilanFN.TEST_SPI_CHIP_ID;
                this.badBointNum = 30;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 10:
                this.chipId = MilanK.TEST_SPI_CHIP_ID;
                this.badBointNum = 35;
                this.badPixelNum = 50;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                this.badLine = 0;
                this.saturated = 0;
                this.sensorMinPerformance = 5;
                this.sensorMaxPerformance = MilanK.TEST_SENSOR_MAX_PERFORMANCE;
                this.sensorMinSnr = 4;
                this.sensorMaxSnr = 100;
                this.sensorNoise = 36;
                this.sensorStableFactor = 0.3f;
                this.underSaturated = 250;
                this.overSaturated = 4000;
                return;
            case 11:
                this.chipId = MilanL.TEST_SPI_CHIP_ID;
                this.badBointNum = 35;
                this.badPixelNum = 65;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 12:
                this.chipId = MilanG.TEST_SPI_CHIP_ID;
                this.badBointNum = 35;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 13:
                this.chipId = MilanJ.TEST_SPI_CHIP_ID;
                this.badBointNum = 35;
                this.badLine = 0;
                this.badPixelNum = 50;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                this.saturated = 0;
                this.sensorMinPerformance = 19;
                this.sensorMaxPerformance = 128;
                this.sensorMinSnr = 4;
                this.sensorMaxSnr = 100;
                this.sensorNoise = 31;
                this.sensorStableFactor = 0.3f;
                this.underSaturated = 250;
                this.overSaturated = 4000;
                return;
            case 14:
                this.chipId = MilanH.TEST_SPI_CHIP_ID;
                this.badBointNum = 35;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 15:
                this.chipId = MilanHU.TEST_SPI_CHIP_ID;
                this.badBointNum = 35;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 16:
            case 17:
                if (i == 16) {
                    this.spiFwVersion = MilanA.TEST_SPI_FW_VERSION;
                } else {
                    this.spiFwVersion = MilanB.TEST_SPI_FW_VERSION;
                }
                this.badBointNum = 35;
                this.totalTime = 500;
                this.badPixelNum = 50;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                this.inCircle = 30;
                if (i == 16) {
                    this.osdUntouched = 200;
                    this.osdTouchedMin = 800;
                    this.osdTouchedMax = 2500;
                    this.hbdAvgMin = 500;
                    this.hbdAvgMax = 3500;
                    this.hbdElectricityMin = 10;
                    this.hbdElectricityMax = 200;
                    return;
                }
                return;
            case 18:
            case 19:
                this.spiFwVersion = MilanC.TEST_SPI_FW_VERSION;
                this.badBointNum = 35;
                this.totalTime = 500;
                this.badPixelNum = 50;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                this.inCircle = 30;
                if (i == 18) {
                    this.osdUntouched = 200;
                    this.osdTouchedMin = 800;
                    this.osdTouchedMax = 2500;
                    this.hbdAvgMin = 500;
                    this.hbdAvgMax = 3500;
                    this.hbdElectricityMin = 10;
                    this.hbdElectricityMax = 200;
                    return;
                }
                return;
            case 20:
                this.chipId = MilanE.TEST_SPI_CHIP_ID;
                this.badBointNum = 30;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 22:
                this.chipId = MilanFN.TEST_SPI_CHIP_ID;
                this.badBointNum = 30;
                this.badPixelNum = 45;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            case 23:
                this.chipId = MilanJ.TEST_SPI_CHIP_ID;
                this.badBointNum = 30;
                this.badPixelNum = 50;
                this.localBadPixelNum = 15;
                this.localWorst = 8;
                return;
            default:
                return;
        }
    }
}
