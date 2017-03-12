package com.samsung.android.contextaware.aggregator.lpp.log;

import android.os.Environment;
import android.os.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataLogging {
    String TAG = "DATA_LOGGING";
    protected String fileName = null;
    List<String> m_LoggingData = new ArrayList();
    protected FileOutputStream m_fileOutStream;
    private String saveFileLoc = null;
    protected String saveFilePath = null;
    protected String strLogFileName = null;

    public void LogInit() {
        this.m_LoggingData.clear();
        this.saveFileLoc = Environment.getDataDirectory().getAbsolutePath();
        this.saveFilePath = this.saveFileLoc + File.separator + "01_DataLogging" + File.separator;
        FileUtils.setPermissions(Environment.getExternalStorageDirectory().getPath(), 509, -1, -1);
    }

    public void flushLogToFile() {
    }

    public void createFileToLog() {
    }
}
