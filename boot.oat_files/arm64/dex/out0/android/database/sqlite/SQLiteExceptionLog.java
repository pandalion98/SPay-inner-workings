package android.database.sqlite;

import android.net.ProxyInfo;
import android.os.Environment;
import android.os.StatFs;

public class SQLiteExceptionLog {
    int errCode;
    String errCodeString;
    String errMessage;

    SQLiteExceptionLog(int code, String message) {
        this.errCode = parseCode(code, message);
        this.errMessage = message;
    }

    public int parseCode(int code, String message) {
        if (message != null) {
            int startIndex = message.indexOf("code ");
            if (startIndex > 0) {
                startIndex += 5;
                int endIndex = startIndex;
                while (endIndex < message.length() && message.charAt(endIndex) != ')') {
                    endIndex++;
                }
                try {
                    code = Integer.parseInt(message.substring(startIndex, endIndex));
                } catch (NumberFormatException e) {
                    return code;
                }
            }
        }
        return code;
    }

    public void setErrString(String errs) {
        this.errCodeString = errs;
    }

    public int getMajorCode() {
        if (this.errCode < 0) {
            return this.errCode;
        }
        return this.errCode & 255;
    }

    public int getMinorCode() {
        if (this.errCode < 0) {
            return this.errCode;
        }
        return (this.errCode >> 8) & 255;
    }

    private boolean needMemoryInfo() {
        return this.errCode == 7 || this.errCode == 3082 || this.errCode == 1550;
    }

    private boolean needMountInfo() {
        return this.errCode == 0 || this.errCode == 5 || this.errCode == 6922 || getMajorCode() == 8 || getMajorCode() == 14;
    }

    private boolean needStorageInfo() {
        return this.errCode == 13 || this.errCode == 4874 || this.errCode == 2318;
    }

    public String makeSQLiteExceptionLog(String Causedby) {
        String l = "\n#################################################################\n";
        if (this.errCode != -1) {
            l = l + "Error Code : " + this.errCode + " (" + this.errCodeString + ")\n";
            if (!(this.errMessage.equals(ProxyInfo.LOCAL_EXCL_LIST) && Causedby.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
                if (Causedby.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                    Causedby = this.errMessage;
                } else if (!this.errMessage.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                    Causedby = Causedby + "\n\t(" + this.errMessage + ")";
                }
                l = l + "Caused By : " + Causedby + "\n";
            }
        }
        return l + "#################################################################";
    }

    public long getTotalInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public long getAvailableInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public String FormatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }
        return Long.toString(size) + suffix;
    }
}
