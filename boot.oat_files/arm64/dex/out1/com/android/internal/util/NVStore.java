package com.android.internal.util;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings$System;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.telephony.MultiSimManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class NVStore {
    private static final boolean DBG = false;
    private static final String DBKEY_MT_PWD = "mt_pwd";
    private static final String DBKEY_MT_STATE = "mt_state";
    private static final String DEFAULT_ICCID = "00000000";
    private static final String DEFAULT_IMSI = "0000";
    private static final String DEFAULT_PHPWD = "00000000";
    private static final String DEFAULT_RETURN_STR = "none";
    private static final String DEFAULT_SENDER_NAME = "Not Mentioned";
    private static final String DEFAULT_SMS_MSG = "Keep this message.";
    private static final String LOG_TAG = "NVStore";
    private static final String MT_FILE = "/system/mobiletracker.txt";
    private static final int OEM_FUNCTION_ID_RFS = 19;
    private static final int OEM_RFS_NV_MOBILE_TRACKER = 1;
    private static final int OEM_WRITE_EVENT_DONE = 2001;
    Context mContext;
    String mNvData;

    public enum datatype {
        All,
        PHPWD,
        PHLOCK_STATE,
        ENABLE_PHLOCK_FIRST
    }

    public NVStore(Context context) {
        this.mNvData = null;
        this.mContext = null;
        this.mContext = context;
        this.mNvData = ReaddataFromNv();
    }

    public NVStore() {
        this.mNvData = null;
        this.mContext = null;
        this.mNvData = ReaddataFromNv();
    }

    public String GetPhPWD() {
        if (this.mNvData == null) {
            return null;
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 1 || tokens[0].length() > 8 || tokens[0].length() < 8) {
            return null;
        }
        return tokens[0];
    }

    public boolean GetMTStatus() {
        if (this.mNvData == null) {
            return false;
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 2 || tokens[1].compareTo(SmartFaceManager.PAGE_BOTTOM) != 0) {
            return false;
        }
        return true;
    }

    public String GetSenderName() {
        if (this.mNvData == null) {
            return DEFAULT_SENDER_NAME;
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 3) {
            return DEFAULT_SENDER_NAME;
        }
        if (tokens[2].length() > 32) {
            return DEFAULT_SENDER_NAME;
        }
        return tokens[2];
    }

    public String GetRec1() {
        if (this.mNvData == null) {
            return "none";
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 4) {
            return "none";
        }
        return tokens[3];
    }

    public String GetRec2() {
        if (this.mNvData == null) {
            return "none";
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 5) {
            return "none";
        }
        return tokens[4];
    }

    public String GetRec3() {
        if (this.mNvData == null) {
            return "none";
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 6) {
            return "none";
        }
        return tokens[5];
    }

    public String GetRec4() {
        if (this.mNvData == null) {
            return "none";
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 7) {
            return "none";
        }
        return tokens[6];
    }

    public String GetRec5() {
        if (this.mNvData == null) {
            return "none";
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 8) {
            return "none";
        }
        return tokens[7];
    }

    public String GetSmsMsg() {
        if (this.mNvData == null) {
            return DEFAULT_SMS_MSG;
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 9) {
            return DEFAULT_SMS_MSG;
        }
        if (tokens[8].length() > 80) {
            return DEFAULT_SMS_MSG;
        }
        return tokens[8];
    }

    public String GetStoredIMSI() {
        if (this.mNvData == null) {
            return DEFAULT_IMSI;
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 10) {
            return DEFAULT_IMSI;
        }
        if (tokens[9].length() > 21) {
            return DEFAULT_IMSI;
        }
        return tokens[9];
    }

    public String GetStoredIMSI2() {
        if (MultiSimManager.getSimSlotCount() <= 1) {
            return DEFAULT_IMSI;
        }
        if (this.mNvData == null) {
            return DEFAULT_IMSI;
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 12) {
            return DEFAULT_IMSI;
        }
        if (tokens[11].length() > 21) {
            return DEFAULT_IMSI;
        }
        return tokens[11];
    }

    public boolean IsPhLockeEnabled() {
        if (this.mNvData == null) {
            return false;
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 11 || tokens[10].compareTo(SmartFaceManager.PAGE_BOTTOM) != 0) {
            return false;
        }
        return true;
    }

    public String GetStoredICCID() {
        if (this.mNvData == null) {
            return "00000000";
        }
        StringTokenizer str = new StringTokenizer(this.mNvData, ";");
        String[] tokens = new String[str.countTokens()];
        int i = 0;
        while (str.hasMoreTokens()) {
            tokens[i] = str.nextToken();
            i++;
        }
        if (i < 12) {
            return "00000000";
        }
        if (tokens[11].length() > 20) {
            return "00000000";
        }
        return tokens[11];
    }

    public String ReaddataFromNv() {
        Throwable th;
        FileInputStream fIn = null;
        InputStreamReader isr = null;
        try {
            FileInputStream fIn2 = new FileInputStream(Environment.getDataDirectory().getAbsolutePath() + MT_FILE);
            try {
                InputStreamReader isr2 = new InputStreamReader(fIn2);
                try {
                    char[] inputBuffer = new char[256];
                    isr2.read(inputBuffer);
                    String data = new String(inputBuffer);
                    if (isr2 != null) {
                        try {
                            isr2.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fIn2 != null) {
                        try {
                            fIn2.close();
                        } catch (IOException e2) {
                        }
                    }
                    isr = isr2;
                    fIn = fIn2;
                    return data;
                } catch (FileNotFoundException e3) {
                    isr = isr2;
                    fIn = fIn2;
                    if (isr != null) {
                        try {
                            isr.close();
                        } catch (IOException e4) {
                        }
                    }
                    if (fIn != null) {
                        try {
                            fIn.close();
                        } catch (IOException e5) {
                        }
                    }
                    return null;
                } catch (IOException e6) {
                    isr = isr2;
                    fIn = fIn2;
                    if (isr != null) {
                        try {
                            isr.close();
                        } catch (IOException e7) {
                        }
                    }
                    if (fIn != null) {
                        try {
                            fIn.close();
                        } catch (IOException e8) {
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    isr = isr2;
                    fIn = fIn2;
                    if (isr != null) {
                        try {
                            isr.close();
                        } catch (IOException e9) {
                        }
                    }
                    if (fIn != null) {
                        try {
                            fIn.close();
                        } catch (IOException e10) {
                        }
                    }
                    throw th;
                }
            } catch (FileNotFoundException e11) {
                fIn = fIn2;
                if (isr != null) {
                    isr.close();
                }
                if (fIn != null) {
                    fIn.close();
                }
                return null;
            } catch (IOException e12) {
                fIn = fIn2;
                if (isr != null) {
                    isr.close();
                }
                if (fIn != null) {
                    fIn.close();
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                fIn = fIn2;
                if (isr != null) {
                    isr.close();
                }
                if (fIn != null) {
                    fIn.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e13) {
            if (isr != null) {
                isr.close();
            }
            if (fIn != null) {
                fIn.close();
            }
            return null;
        } catch (IOException e14) {
            if (isr != null) {
                isr.close();
            }
            if (fIn != null) {
                fIn.close();
            }
            return null;
        } catch (Throwable th4) {
            th = th4;
            if (isr != null) {
                isr.close();
            }
            if (fIn != null) {
                fIn.close();
            }
            throw th;
        }
    }

    public void writedata(String data, datatype type) {
        Throwable th;
        String newdata = "";
        RandomAccessFile raf = null;
        this.mNvData = ReaddataFromNv();
        if (type == datatype.PHPWD) {
            newdata = newdata + data + ";" + (GetMTStatus() ? SmartFaceManager.PAGE_BOTTOM : SmartFaceManager.PAGE_MIDDLE) + ";" + GetSenderName() + ";" + GetRec1() + ";" + GetRec2() + ";" + GetRec3() + ";" + GetRec4() + ";" + GetRec5() + ";" + GetSmsMsg() + ";" + GetStoredIMSI() + ";" + IsPhLockeEnabled() + ";" + GetStoredIMSI2() + ";";
        } else if (type == datatype.PHLOCK_STATE) {
            newdata = newdata + GetPhPWD() + ";" + (GetMTStatus() ? SmartFaceManager.PAGE_BOTTOM : SmartFaceManager.PAGE_MIDDLE) + ";" + GetSenderName() + ";" + GetRec1() + ";" + GetRec2() + ";" + GetRec3() + ";" + GetRec4() + ";" + GetRec5() + ";" + GetSmsMsg() + ";" + GetStoredIMSI() + ";" + data + ";" + GetStoredIMSI2() + ";";
        } else if (type == datatype.ENABLE_PHLOCK_FIRST) {
            newdata = newdata + data + ";" + (GetMTStatus() ? SmartFaceManager.PAGE_BOTTOM : SmartFaceManager.PAGE_MIDDLE) + ";" + GetSenderName() + ";" + GetRec1() + ";" + GetRec2() + ";" + GetRec3() + ";" + GetRec4() + ";" + GetRec5() + ";" + GetSmsMsg() + ";" + GetStoredIMSI() + ";" + SmartFaceManager.PAGE_BOTTOM + ";" + GetStoredIMSI2() + ";";
        } else {
            newdata = newdata + data;
        }
        this.mNvData = newdata;
        if (this.mContext != null) {
            Settings$System.putString(this.mContext.getContentResolver(), DBKEY_MT_PWD, GetPhPWD());
            if (GetMTStatus()) {
                Settings$System.putInt(this.mContext.getContentResolver(), DBKEY_MT_STATE, 1);
            } else {
                Settings$System.putInt(this.mContext.getContentResolver(), DBKEY_MT_STATE, 0);
            }
        }
        try {
            RandomAccessFile raf2 = new RandomAccessFile(Environment.getDataDirectory().getAbsolutePath() + MT_FILE, "rw");
            try {
                raf2.write(newdata.getBytes("UTF-8"));
                if (raf2 != null) {
                    try {
                        raf2.close();
                        raf = raf2;
                        return;
                    } catch (IOException e) {
                        raf = raf2;
                        return;
                    }
                }
            } catch (FileNotFoundException e2) {
                raf = raf2;
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (UnsupportedEncodingException e4) {
                raf = raf2;
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e5) {
                    }
                }
            } catch (IOException e6) {
                raf = raf2;
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e7) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                raf = raf2;
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e8) {
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e9) {
            if (raf != null) {
                raf.close();
            }
        } catch (UnsupportedEncodingException e10) {
            if (raf != null) {
                raf.close();
            }
        } catch (IOException e11) {
            if (raf != null) {
                raf.close();
            }
        } catch (Throwable th3) {
            th = th3;
            if (raf != null) {
                raf.close();
            }
            throw th;
        }
    }
}
