package android.database.sqlite;

import android.net.ProxyInfo;

public class SQLiteDiskIOException extends SQLiteException {
    public static final int errCode = 10;
    public static final String[][] errString;
    public static final String postfix = " operation.";
    public static final String prefix = "Disk I/O error occurred during ";

    static {
        r0 = new String[28][];
        r0[0] = new String[]{"SQLITE_IOERR", "Disk I/O error occurred."};
        r0[1] = new String[]{"SQLITE_IOERR_READ", "'read'"};
        r0[2] = new String[]{"SQLITE_IOERR_SHORT_READ", "'short read'"};
        r0[3] = new String[]{"SQLITE_IOERR_WRITE", "'write'"};
        r0[4] = new String[]{"SQLITE_IOERR_FSYNC", "'fsync'"};
        r0[5] = new String[]{"SQLITE_IOERR_DIR_FSYNC", "'dir fsync'"};
        r0[6] = new String[]{"SQLITE_IOERR_TRUNCATE", "'truncate'"};
        r0[7] = new String[]{"SQLITE_IOERR_FSTAT", "Failed to get database file information with system call stat(). Please confirm whether database file has been removed."};
        r0[8] = new String[]{"SQLITE_IOERR_UNLOCK", "'unlock'"};
        r0[9] = new String[]{"SQLITE_IOERR_RDLOCK", "Disk I/O error occurred because of holding incompatible lock."};
        r0[10] = new String[]{"SQLITE_IOERR_DELETE", "'delete'"};
        r0[11] = new String[]{"SQLITE_IOERR_BLOCKED", "Disk I/O operation is blocked."};
        r0[12] = new String[]{"SQLITE_IOERR_NOMEM", "There is no enough heap memory for I/O operation."};
        r0[13] = new String[]{"SQLITE_IOERR_ACCESS", "Disk I/O operation access is deined."};
        r0[14] = new String[]{"SQLITE_IOERR_CHECKRESERVEDLOCK", "A RESERVED lock held on file by other process."};
        r0[15] = new String[]{"SQLITE_IOERR_LOCK", "'lock'"};
        r0[16] = new String[]{"SQLITE_IOERR_CLOSE", "'close'"};
        r0[17] = new String[]{"SQLITE_IOERR_DIR_CLOSE", "'dir close'"};
        r0[18] = new String[]{"SQLITE_IOERR_SHMOPEN", "'share memory open (ftrucate)'"};
        r0[19] = new String[]{"SQLITE_IOERR_SHMSIZE", "No available space in disk."};
        r0[20] = new String[]{"SQLITE_IOERR_SHMLOCK", "'shared memory lock'"};
        r0[21] = new String[]{"SQLITE_IOERR_SHMMAP", "'shared memory mmap'"};
        r0[22] = new String[]{"SQLITE_IOERR_SEEK", "'seek'"};
        r0[23] = new String[]{"SQLITE_IOERR_DELETE_NOENT", "Can not delete path or file."};
        r0[24] = new String[]{"SQLITE_IOERR_MMAP", "'mmap'"};
        r0[25] = new String[]{"SQLITE_IOERR_GETTEMPPATH", "'get temporary path'"};
        r0[26] = new String[]{"SQLITE_IOERR_CONVPATH", "'converted path'"};
        r0[27] = new String[]{"SQLITE_IOERR_LOCK_EBADF", "I/O error happened due to bad file descriptor. There is possibility the partition changed to read-only."};
        errString = r0;
    }

    public SQLiteDiskIOException(String error) {
        super(error + addErrCode(error));
    }

    private static String addErrCode(String errMessage) {
        if (errMessage == null || errMessage.indexOf("(code ") <= 0) {
            return " (code 10)";
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }

    public static String makeCausedBy(int minorCode) {
        String caused = ProxyInfo.LOCAL_EXCL_LIST;
        if (errString[minorCode][1].charAt(0) == '\'') {
            return prefix + errString[minorCode][1] + postfix;
        }
        return errString[minorCode][1];
    }
}
