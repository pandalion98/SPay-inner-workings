package com.samsung.android.contextaware.utilbundle.logger;

import android.os.Environment;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CaFileLogger {
    private static final int LOGTYPE_GPSPOS = 1;
    private static final String LOG_FILE_DIR_NAME = ("log" + File.separator + "cae");
    private static volatile CaFileLogger instance;
    private final Map<String, DataOutputStream> mDataOutputStream = new HashMap();
    private final Map<String, File> mFile = new HashMap();

    public static CaFileLogger getInstance() {
        if (instance == null) {
            synchronized (CaFileLogger.class) {
                if (instance == null) {
                    instance = new CaFileLogger();
                }
            }
        }
        return instance;
    }

    public final boolean startLogging(String type) {
        if (this.mFile.containsKey(type) || this.mDataOutputStream.containsKey(type)) {
            CaLogger.error("This file is created already.");
            return false;
        }
        try {
            File logDir = getLogStorageDir();
            if (logDir == null) {
                CaLogger.error("Log directory is null");
                return false;
            }
            File logFile = getLogFile(type, logDir);
            if (logFile == null) {
                CaLogger.error("Log file is null");
                return false;
            }
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(logFile));
            this.mFile.put(type, logFile);
            this.mDataOutputStream.put(type, dataOutputStream);
            return true;
        } catch (FileNotFoundException fnfe) {
            CaLogger.exception(fnfe);
            return false;
        } catch (IOException ioe) {
            CaLogger.exception(ioe);
            return false;
        } catch (Exception e) {
            CaLogger.exception(e);
            return false;
        }
    }

    public final boolean stopLogging(String fileName) {
        if (!this.mFile.containsKey(fileName) || !this.mDataOutputStream.containsKey(fileName)) {
            return false;
        }
        try {
            ((DataOutputStream) this.mDataOutputStream.get(fileName)).close();
            this.mFile.remove(fileName);
            this.mDataOutputStream.remove(fileName);
            return true;
        } catch (IOException ioe) {
            CaLogger.exception(ioe);
            return false;
        }
    }

    public final void logging(String fileName, String text) {
        if (this.mFile.containsKey(fileName) && this.mDataOutputStream.containsKey(fileName)) {
            try {
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeBytes(text + System.getProperty("line.separator"));
                return;
            } catch (IOException ioe) {
                CaLogger.exception(ioe);
                return;
            }
        }
        CaLogger.error("This file dose not exist.");
    }

    public final void loggingForKML(String fileName, long sysTime, double[] location, float[] additionalInfo, long timeStamp) {
        if (!this.mFile.containsKey(fileName) || !this.mDataOutputStream.containsKey(fileName)) {
            CaLogger.error("This file dose not exist.");
        } else if (location != null && location.length > 3 && additionalInfo != null && additionalInfo.length > 3) {
            try {
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeInt(1);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeLong(sysTime);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeFloat(additionalInfo[0]);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeDouble(location[0]);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeDouble(location[1]);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeDouble(location[2]);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeFloat(additionalInfo[1]);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeFloat(additionalInfo[2]);
                ((DataOutputStream) this.mDataOutputStream.get(fileName)).writeLong(timeStamp);
            } catch (IOException ioe) {
                CaLogger.exception(ioe);
            }
        }
    }

    private File getLogStorageDir() {
        File logDir = new File(Environment.getDataDirectory().getAbsolutePath() + File.separator + LOG_FILE_DIR_NAME + File.separator);
        if (logDir == null) {
            try {
                CaLogger.error("External storage directory is null");
                return null;
            } catch (Exception e) {
                CaLogger.error(e.toString());
                return logDir;
            }
        } else if (logDir.exists() && !logDir.isDirectory()) {
            CaLogger.error(logDir.getAbsolutePath() + " already exists and is not a directory");
            return null;
        } else if (logDir.exists() || logDir.mkdir()) {
            return logDir;
        } else {
            CaLogger.error("Unable to create directory: " + logDir.getAbsolutePath());
            return null;
        }
    }

    private File getLogFile(String type, File logDir) throws IOException {
        Calendar curTime = Calendar.getInstance();
        long curTimeYear = (long) curTime.get(1);
        long curTimeMonth = (long) (curTime.get(2) + 1);
        long curTimeDay = (long) curTime.get(5);
        long curTimeHour = (long) curTime.get(11);
        long curTimeMinute = (long) curTime.get(12);
        long curTimeSecond = (long) curTime.get(13);
        String fileName = String.format("%04dY%02dM%02dD%02dH%02dM%02dS_" + type + ".log", new Object[]{Long.valueOf(curTimeYear), Long.valueOf(curTimeMonth), Long.valueOf(curTimeDay), Long.valueOf(curTimeHour), Long.valueOf(curTimeMinute), Long.valueOf(curTimeSecond)});
        CaLogger.info("logDir = " + logDir.toString());
        CaLogger.info("fileName = " + fileName);
        File file = new File(logDir, fileName);
        if (file.createNewFile()) {
            return file;
        }
        CaLogger.error("createNewFile() error");
        return null;
    }
}
