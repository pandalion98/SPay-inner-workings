package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.os.Looper;
import java.io.Serializable;

public class LppConfig implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean GPSAlways = true;
    public int GPSKeepOn_Timer = 15;
    public int GPSRequest_APDR = 100;
    private int GPSRequest_By = 0;
    public int GPSRequest_Timer = 45;
    private String LogConfig = "[LPPTest Configuration]\r\n";
    private boolean[] LogFlags = new boolean[]{false, false, false, false};
    private String LogFolderName = "LPPTest";
    private boolean flag_log = false;
    public transient Looper looper;
    private Context mcontext;

    public LppConfig(boolean Log_Sensor, boolean Log_PDRLOC, boolean Log_Result, boolean Log_ResultKML, int APDR_Request, boolean GPS_Always, String Log_FolderN) {
        this.LogFlags[0] = Log_Sensor;
        this.LogFlags[1] = Log_PDRLOC;
        this.LogFlags[2] = Log_Result;
        this.LogFlags[3] = Log_ResultKML;
        this.GPSAlways = GPS_Always;
        this.LogFolderName = Log_FolderN;
    }

    public LppConfig(boolean Log_Sensor, boolean Log_PDRLOC, boolean Log_Result, boolean Log_ResultKML, int gPSRequest_By, int gPSRequest_APDR, int gPSRequest_Timer, int gPSKeepOn_Timer, String strConfig, String strFolderName) {
        this.LogFlags[0] = Log_Sensor;
        this.LogFlags[1] = Log_PDRLOC;
        this.LogFlags[2] = Log_Result;
        this.LogFlags[3] = Log_ResultKML;
        this.GPSRequest_By = gPSRequest_By;
        this.GPSRequest_APDR = gPSRequest_APDR;
        this.GPSRequest_Timer = gPSRequest_Timer;
        this.GPSKeepOn_Timer = gPSKeepOn_Timer;
        this.LogConfig = strConfig;
        this.LogFolderName = strFolderName;
    }

    public LppConfig(Context context, int gPSRequest_APDR, int gPSRequest_Timer, int gPSKeepOn_Timer) {
        this.GPSRequest_APDR = gPSRequest_APDR;
        this.GPSRequest_Timer = gPSRequest_Timer;
        this.GPSKeepOn_Timer = gPSKeepOn_Timer;
        this.mcontext = context;
    }

    public LppConfig(LppConfig mlppconfig) {
        boolean[] logflags = mlppconfig.getLogFlags();
        for (int inx = 0; inx < 4; inx++) {
            this.LogFlags[inx] = logflags[inx];
        }
        this.GPSRequest_APDR = mlppconfig.GPSRequest_APDR;
        this.GPSRequest_Timer = mlppconfig.GPSRequest_Timer;
        this.GPSKeepOn_Timer = mlppconfig.GPSKeepOn_Timer;
        this.LogConfig = mlppconfig.getConfigStr();
        this.LogFolderName = mlppconfig.getLogFolderNameStr();
        this.mcontext = mlppconfig.getContext();
    }

    public void setLogParameter(boolean Log_Sensor, boolean Log_PDRLOC, boolean Log_Result, boolean Log_ResultKML, String strConfig, String strFolderName) {
        this.flag_log = true;
        this.LogFlags[0] = Log_Sensor;
        this.LogFlags[1] = Log_PDRLOC;
        this.LogFlags[2] = Log_Result;
        this.LogFlags[3] = Log_ResultKML;
        this.LogConfig = strConfig;
        this.LogFolderName = strFolderName;
    }

    public boolean[] getLogFlags() {
        return this.LogFlags;
    }

    public String getConfigStr() {
        return this.LogConfig;
    }

    public String getLogFolderNameStr() {
        return this.LogFolderName;
    }

    public boolean getLogCommand() {
        return this.flag_log;
    }

    public void setContext(Context context) {
        this.mcontext = context;
    }

    public Context getContext() {
        return this.mcontext;
    }
}
