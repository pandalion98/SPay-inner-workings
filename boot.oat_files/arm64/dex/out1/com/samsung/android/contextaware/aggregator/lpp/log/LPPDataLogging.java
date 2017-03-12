package com.samsung.android.contextaware.aggregator.lpp.log;

import android.os.Binder;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class LPPDataLogging extends DataLogging {
    String FileType = "log";
    String TAG = "LPPDataLogging";

    LPPDataLogging(String fileName) {
        this.strLogFileName = fileName;
    }

    public void LogInit(String folderName) {
        super.LogInit();
        Log.d(this.TAG, "LogInit : " + this.saveFilePath + folderName);
        Calendar curTime = Calendar.getInstance();
        long curTimeYear = (long) curTime.get(1);
        long curTimeMonth = (long) (curTime.get(2) + 1);
        long curTimeDay = (long) curTime.get(5);
        long curTimeHour = (long) curTime.get(11);
        long curTimeMinute = (long) curTime.get(12);
        long curTimeSecond = (long) curTime.get(13);
        this.saveFilePath += String.format(folderName + "_%04d%02d%02d_%02d%02d%02d/", new Object[]{Long.valueOf(curTimeYear), Long.valueOf(curTimeMonth), Long.valueOf(curTimeDay), Long.valueOf(curTimeHour), Long.valueOf(curTimeMinute), Long.valueOf(curTimeSecond)});
        try {
            File dir = new File(this.saveFilePath);
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(this.TAG, "Unable to create dir:" + this.saveFilePath);
            }
        } catch (Exception e) {
            Log.e(this.TAG, "error in dir" + e.toString());
        }
    }

    public void addLogStream(String log) {
        this.m_LoggingData.add(log);
    }

    public void createFileToLog() {
        long ident = Binder.clearCallingIdentity();
        try {
            Log.d(this.TAG, "[DataLogging] strLogFileName:" + this.strLogFileName);
            this.m_fileOutStream = new FileOutputStream(new File(this.saveFilePath + this.strLogFileName + "." + this.FileType));
        } catch (IOException e) {
            Log.e(this.TAG, "error in createFileToLog:" + e.toString());
        }
        Binder.restoreCallingIdentity(ident);
    }

    public void setFileType(String type) {
        this.FileType = type;
    }
}
