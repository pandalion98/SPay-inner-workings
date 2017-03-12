package android.database.sqlite;

import android.net.ProxyInfo;

public class SQLiteFullException extends SQLiteException {
    public static final int errCode = 13;

    public SQLiteFullException(String error) {
        super(error + addErrCode(error));
    }

    private static String addErrCode(String errMessage) {
        if (errMessage == null || errMessage.indexOf("(code ") <= 0) {
            return " (code 13)";
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }
}
