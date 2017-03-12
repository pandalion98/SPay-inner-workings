package com.samsung.android.contextaware.aggregator.lpp.log;

import android.location.Location;
import com.samsung.android.contextaware.aggregator.lpp.ILppDataProvider;
import com.samsung.android.contextaware.aggregator.lpp.LppConfig;
import java.util.ArrayList;

public class LppLogManager {
    static boolean FlagLog_Monitor = true;
    public static final char LogTypeAPDR = '\u0004';
    public static final char LogTypeAPDRLM = '\u0001';
    public static final char LogTypeGPSNmea = '\b';
    public static final char LogTypeGPSStatus = '\u0007';
    public static final char LogTypeLPPResult = '\u0002';
    public static final char LogTypeLocM = '\u0005';
    public static final char LogTypeMonitor = '\u0006';
    public static final char LogTypeSensor = '\u0000';
    public static final char LogTypemLPPKML_ = '\u0003';
    boolean FlagLog_APDRLM = true;
    boolean FlagLog_LPPRes = true;
    boolean FlagLog_Sensor = true;
    String LogFolderName = "test";
    LPPDataLogging mAPDRLM_Logging = new LPPDataLogging("APDR_LM");
    LPPDataLogging mConfig_Logging = new LPPDataLogging("Configuration");
    LPPDataLogging mGPNMEA_Logging = new LPPDataLogging("GPS_NMEA");
    LPPDataLogging mGPSSta_Logging = new LPPDataLogging("GPSStatus");
    KMLGenerator mKMLGen = new KMLGenerator(this.LogFolderName);
    KMLGenerator mKMLGenRT = new KMLGenerator(this.LogFolderName);
    LPPDataLogging mLPPKMLRLogging = new LPPDataLogging("LPPResultKMLRT");
    LPPDataLogging mLPPKML_Logging = new LPPDataLogging("LPPResultKML");
    LPPDataLogging mLPPMon_Logging = new LPPDataLogging("LPPMonitor");
    LPPDataLogging mLPPResuLogging = new LPPDataLogging("LPPResultTest");
    ILppDataProvider mLppDataProviderLnr = null;
    LPPDataLogging mSensor_Logging = new LPPDataLogging("SensorLog");
    String strFromAPDR = "\t0 0 0 0 0";
    String strFromLocM = "\t0 0 0 0";

    public void init(LppConfig config) {
    }

    public void start() {
    }

    public void stop() {
    }

    protected void setLogName(String str) {
        this.LogFolderName = str;
    }

    public void LogData(int apdrLm, String logData) {
        switch (apdrLm) {
            case 6:
                if (this.mLppDataProviderLnr != null) {
                    this.mLppDataProviderLnr.lppStatus("\n" + System.currentTimeMillis() + "\t" + logData);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void LogAPDRLM(int Logindex) {
        this.mAPDRLM_Logging.addLogStream(System.nanoTime() + "\t" + Logindex + this.strFromAPDR + this.strFromLocM + "\n");
    }

    public void AddCoordinate(ArrayList<Location> arrayList) {
    }

    public void AddRTCoordinate(Location loc) {
    }

    public void AddGPSCoordinate(ArrayList<Location> arrayList) {
    }

    public void setILppDataProviderListener(ILppDataProvider lnr) {
        this.mLppDataProviderLnr = lnr;
    }
}
