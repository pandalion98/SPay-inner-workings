package egis.finger.host;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FingerUtil {
    private static final String FEATURE_FILE = "Enroll_Data";
    private static final String FEATURE_FILE_BAK = "Enroll_Data_Bak";
    private static final String FEATURE_FILE_DEFAULT_ID = "Egistec_Company_Internal_Default_Data_1_id";
    private static final String FEATURE_FILE_DEFAULT_VAULE = "Egistec_Company_Internal_Default_Data_1_value";
    private static final String FEATURE_FILE_NEW = "Enroll_Data_New";
    protected static final String TAG = "FpCsaClientLib_FingerUtil";
    public static int[] fileDBMap;
    protected static Handler mApHandler;
    private static Context mContext;
    public byte[] mFeature = new byte[1024];
    public int mFeatureSize = 0;
    private FileDB mFileDB = null;

    public FingerUtil(Handler handle, Context context) {
        mApHandler = handle;
        mContext = context;
        this.mFileDB = new FileDB();
        if (!refreshList()) {
            Log.e(TAG, "onCreate load data from data fail, retry to use bak file");
            if (!recoveryDB()) {
                Log.e(TAG, "onCreate recoveryDB fail");
                if (!reInitDB()) {
                    Log.e(TAG, "reInitDB fail");
                    return;
                }
                return;
            }
            Log.d(TAG, "Try to refresh");
            if (!refreshList()) {
                Log.e(TAG, "onCreate still can not load data from DB");
            }
        }
    }

    public static void postFpResultStatus(int status) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("postFpResultStatus status = ");
        sb.append(status);
        Log.d(str, sb.toString());
        mApHandler.obtainMessage(1000, status, -1).sendToTarget();
    }

    public boolean enrollToDB(String userID) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("enrollToDB userID=");
        sb.append(userID);
        Log.d(str, sb.toString());
        if (this.mFileDB == null) {
            Log.e(TAG, "enrollToDB FileDB==null");
            FPNativeBase.lastErrCode = 2030;
            return false;
        } else if (this.mFeature == null) {
            Log.e(TAG, "mFeature == null");
            FPNativeBase.lastErrCode = 2034;
            return false;
        } else {
            if (!this.mFileDB.add(userID, (byte[]) this.mFeature.clone())) {
                Log.e(TAG, "enrollToDB(): mFileDB.add() fail");
                postFpResultStatus(1002);
                return false;
            } else if (saveFeatureToFile()) {
                return true;
            } else {
                Log.e(TAG, "enrollToDB(): saveFeatureToFile() fail");
                postFpResultStatus(1002);
                return false;
            }
        }
    }

    private boolean saveFeatureToFile() {
        Log.d(TAG, "svaeFeatureToFile");
        if (saveFeatureToFile(FEATURE_FILE_NEW)) {
            if (replaceDB()) {
                Log.d(TAG, "svaeFeatureToFile complete");
                return true;
            }
            Log.e(TAG, "svaeFeatureToFile replaceDB fail");
        }
        Log.e(TAG, "saveFeatureToFile fail, retry");
        if (saveFeatureToFile(FEATURE_FILE_NEW)) {
            if (replaceDB()) {
                Log.d(TAG, "svaeFeatureToFile complete");
                return true;
            }
            Log.e(TAG, "svaeFeatureToFile retry then replaceDB fail");
        }
        Log.e(TAG, "saveFeatureToFile still fail");
        return false;
    }

    private boolean saveFeatureToFile(String fileName) {
        checkFileLen(FEATURE_FILE);
        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(fileName, 0);
            if (!this.mFileDB.save(fos)) {
                Log.e(TAG, "saveFeature() mFileDB.Save() fail");
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder("saveFeature() fos.close() exception=");
                        sb.append(e.getMessage());
                        Log.e(str, sb.toString());
                    }
                }
                Log.d(TAG, "svaeFeatureToFile end");
                return false;
            }
            Log.d(TAG, "svaeFeatureToFile success");
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e2) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder("saveFeature() fos.close() exception=");
                    sb2.append(e2.getMessage());
                    Log.e(str2, sb2.toString());
                }
            }
            Log.d(TAG, "svaeFeatureToFile end");
            return true;
        } catch (FileNotFoundException e3) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder("saveFeature() exception=");
            sb3.append(e3.getMessage());
            Log.e(str3, sb3.toString());
            FPNativeBase.lastErrCode = 2044;
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e4) {
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder("saveFeature() fos.close() exception=");
                    sb4.append(e4.getMessage());
                    Log.e(str4, sb4.toString());
                }
            }
            Log.d(TAG, "svaeFeatureToFile end");
            return false;
        } catch (Throwable th) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e5) {
                    StringBuilder sb5 = new StringBuilder("saveFeature() fos.close() exception=");
                    sb5.append(e5.getMessage());
                    Log.e(TAG, sb5.toString());
                }
            }
            Log.d(TAG, "svaeFeatureToFile end");
            throw th;
        }
    }

    private int checkFileLen(String fileName) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("checkFileLen fileName = ");
        sb.append(fileName);
        Log.d(str, sb.toString());
        int len = 0;
        FileInputStream fis = null;
        BufferedInputStream ois = null;
        try {
            FileInputStream fis2 = mContext.openFileInput(fileName);
            if (fis2 != null) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder("checkDBLen fislen = ");
                sb2.append(fis2.available());
                Log.d(str2, sb2.toString());
                ois = new BufferedInputStream(fis2);
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder("checkDBLen oislen = ");
                sb3.append(ois.available());
                Log.d(str3, sb3.toString());
                len = ois.available();
            }
            if (fis2 != null) {
                try {
                    fis2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                ois.close();
            }
        } catch (FileNotFoundException e2) {
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder("checkDBLen(): FileNotFoundException:");
            sb4.append(e2.getMessage());
            Log.e(str4, sb4.toString());
            e2.printStackTrace();
            if (fis != null) {
                fis.close();
            }
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e3) {
            String str5 = TAG;
            StringBuilder sb5 = new StringBuilder("checkDBLen(): IOException:");
            sb5.append(e3.getMessage());
            Log.e(str5, sb5.toString());
            e3.printStackTrace();
            if (fis != null) {
                fis.close();
            }
            if (ois != null) {
                ois.close();
            }
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                    throw th;
                }
            }
            if (ois != null) {
                ois.close();
            }
            throw th;
        }
        String str6 = TAG;
        StringBuilder sb6 = new StringBuilder("checkFileLen len = ");
        sb6.append(len);
        Log.d(str6, sb6.toString());
        return len;
    }

    private boolean replaceDB() {
        Log.d(TAG, "replaceDB");
        if (!checkFileState(FEATURE_FILE_NEW)) {
            Log.e(TAG, "replaceDB checkFileState fail Enroll_Data_New");
            return false;
        }
        StringBuilder sb = new StringBuilder(String.valueOf(getDBPath()));
        sb.append("/");
        sb.append(FEATURE_FILE_NEW);
        File file_new = new File(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(mContext.getFilesDir());
        sb2.append("/");
        sb2.append(FEATURE_FILE);
        File file = new File(sb2.toString());
        StringBuilder sb3 = new StringBuilder(String.valueOf(getDBPath()));
        sb3.append("/");
        sb3.append(FEATURE_FILE_BAK);
        File file_bak = new File(sb3.toString());
        if (file_bak.exists() && !file_bak.delete()) {
            String str = TAG;
            StringBuilder sb4 = new StringBuilder("replaceDB delete file fail ");
            sb4.append(file_bak.getPath());
            Log.e(str, sb4.toString());
            FPNativeBase.lastErrCode = 2044;
        }
        if (!file.renameTo(file_bak)) {
            String str2 = TAG;
            StringBuilder sb5 = new StringBuilder("replaceDB rename file fail ");
            sb5.append(file.getPath());
            sb5.append(" to ");
            sb5.append(file_bak.getPath());
            Log.e(str2, sb5.toString());
        }
        if (file.exists() && !file.delete()) {
            String str3 = TAG;
            StringBuilder sb6 = new StringBuilder("replaceDB delete file fail ");
            sb6.append(file.getPath());
            Log.e(str3, sb6.toString());
            FPNativeBase.lastErrCode = 2044;
        }
        if (file_new.renameTo(file)) {
            Log.d(TAG, "replaceDB complete");
            return true;
        }
        String str4 = TAG;
        StringBuilder sb7 = new StringBuilder("replaceDB rename file fail ");
        sb7.append(file_new.getPath());
        sb7.append(" to ");
        sb7.append(file.getPath());
        Log.e(str4, sb7.toString());
        FPNativeBase.lastErrCode = 2051;
        return false;
    }

    private boolean checkFileState(String fileName) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("checkFileState fileName = ");
        sb.append(fileName);
        Log.d(str, sb.toString());
        if (checkFileLen(fileName) == 0) {
            Log.e(TAG, "   checkDBLen len == 0");
            FPNativeBase.lastErrCode = 2039;
            return false;
        } else if (getDBPath() == null) {
            Log.e(TAG, "checkDBState path == null");
            FPNativeBase.lastErrCode = 2040;
            return false;
        } else {
            String dataRead = dataRead(FEATURE_FILE_DEFAULT_ID);
            String value = dataRead;
            if (dataRead == null) {
                Log.e(TAG, "checkDBState dataRead error");
                return false;
            } else if (value.equals(FEATURE_FILE_DEFAULT_VAULE)) {
                Log.d(TAG, "checkDBState complete");
                return true;
            } else {
                Log.e(TAG, "checkDBState fail");
                FPNativeBase.lastErrCode = 2041;
                return false;
            }
        }
    }

    private String getDBPath() {
        if (mContext.getFilesDir() != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("getDBPath()=");
            sb.append(mContext.getFilesDir().getAbsolutePath());
            Log.d(str, sb.toString());
        } else {
            Log.e(TAG, "getDBPath()=null");
        }
        if (mContext.getFilesDir() != null) {
            return mContext.getFilesDir().getAbsolutePath();
        }
        return null;
    }

    public String dataRead(String id) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("dataRead(): start, id=");
        sb.append(id);
        Log.d(str, sb.toString());
        if (this.mFileDB == null) {
            Log.e(TAG, "dataRead(): FileDB is null");
            FPNativeBase.lastErrCode = 2030;
            return null;
        }
        int idx = -1;
        int i = 0;
        while (true) {
            try {
                if (i >= this.mFileDB.size()) {
                    break;
                } else if (this.mFileDB.readID(i).compareTo(id) == 0) {
                    idx = i;
                    break;
                } else {
                    i++;
                }
            } catch (Exception e) {
                Log.d(TAG, "search data from db file error");
                return null;
            }
        }
        if (idx != -1) {
            String data = new String(this.mFileDB.readFeature(idx), 0, this.mFileDB.readFeature(idx).length);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("data = ");
            sb2.append(data);
            Log.d(str2, sb2.toString());
            return data;
        }
        Log.w(TAG, "data not found");
        FPNativeBase.lastErrCode = 2031;
        return null;
    }

    public boolean checkId(String id) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("checkId(): start, id=");
        sb.append(id);
        Log.d(str, sb.toString());
        if (this.mFileDB == null) {
            Log.e(TAG, "checkId(): FileDB is null");
            FPNativeBase.lastErrCode = 2030;
            return false;
        }
        int idx = -1;
        int i = 0;
        while (true) {
            try {
                if (i >= this.mFileDB.size()) {
                    break;
                }
                String temp = this.mFileDB.readID(i);
                int idLen = temp.indexOf(59);
                if (idLen != -1) {
                    if (temp.substring(0, idLen).compareTo(id.substring(0, id.indexOf(";"))) == 0) {
                        idx = i;
                        break;
                    }
                }
                i++;
            } catch (Exception e) {
                Log.d(TAG, "checkId search data from db file error");
                return false;
            }
        }
        if (idx != -1) {
            return true;
        }
        Log.w(TAG, "data not found");
        FPNativeBase.lastErrCode = 2031;
        return false;
    }

    /* Debug info: failed to restart local var, previous not found, register: 11 */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x028e, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02d4, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x030d, code lost:
        if (reInitDB() == false) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x033d, code lost:
        if (reInitDB() == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x035c, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x0430, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:210:0x0469, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:221:0x0495, code lost:
        if (reInitDB() == false) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x04c0, code lost:
        if (reInitDB() == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x04df, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:271:0x059c, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:282:0x05d5, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:293:0x0601, code lost:
        if (reInitDB() == false) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:304:0x062c, code lost:
        if (reInitDB() == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00d5, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:312:0x064b, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00d7, code lost:
        android.util.Log.e(TAG, " Set default data fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00de, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:341:0x06fe, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:352:0x0737, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:363:0x0763, code lost:
        if (reInitDB() == false) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:374:0x078e, code lost:
        if (reInitDB() == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:382:0x07ad, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:408:0x0861, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:421:0x08a7, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:435:0x08e0, code lost:
        if (reInitDB() == false) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:448:0x0910, code lost:
        if (reInitDB() == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:456:0x092f, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0122, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0124, code lost:
        android.util.Log.e(TAG, "refreshList reInitDB fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x012b, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0161, code lost:
        if (reInitDB() == false) goto L_0x0163;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0163, code lost:
        android.util.Log.e(TAG, "refreshList reInitDB fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0197, code lost:
        if (reInitDB() == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0199, code lost:
        android.util.Log.e(TAG, "refreshList reInitDB fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01bc, code lost:
        if (reInitDB() == false) goto L_0x0124;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean refreshList() {
        /*
            r11 = this;
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList"
            android.util.Log.d(r0, r1)
            boolean r0 = r11.replaceNewDB()
            r1 = 0
            if (r0 != 0) goto L_0x0016
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r2 = "refreshList replaceNewDB fail"
            android.util.Log.e(r0, r2)
            return r1
        L_0x0016:
            r0 = 0
            r2 = r0
            r3 = 2044(0x7fc, float:2.864E-42)
            r4 = 1
            r5 = 2052(0x804, float:2.875E-42)
            android.content.Context r6 = mContext     // Catch:{ FileNotFoundException -> 0x0371, IOException -> 0x01da }
            java.lang.String r7 = "Enroll_Data"
            java.io.FileInputStream r6 = r6.openFileInput(r7)     // Catch:{ FileNotFoundException -> 0x0371, IOException -> 0x01da }
            r2 = r6
            if (r2 == 0) goto L_0x0037
            egis.finger.host.FileDB r6 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0371, IOException -> 0x01da }
            r6.Load(r2)     // Catch:{ FileNotFoundException -> 0x0371, IOException -> 0x01da }
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r6, r7)     // Catch:{ FileNotFoundException -> 0x0371, IOException -> 0x01da }
            r2.close()     // Catch:{ FileNotFoundException -> 0x0371, IOException -> 0x01da }
        L_0x0037:
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList load DB complete"
            android.util.Log.d(r6, r7)     // Catch:{ FileNotFoundException -> 0x0371, IOException -> 0x01da }
            if (r2 == 0) goto L_0x0048
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0048
        L_0x0044:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0048:
            java.lang.String r6 = "Enroll_Data"
            boolean r6 = r11.checkFileState(r6)
            if (r6 != 0) goto L_0x01c1
            java.io.File r6 = new java.io.File
            java.lang.String r7 = "Enroll_Data"
            r6.<init>(r7)
            boolean r7 = r6.exists()
            if (r7 == 0) goto L_0x007a
            boolean r7 = r6.delete()
            if (r7 == 0) goto L_0x007a
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "refreshList delete file fail "
            r8.<init>(r9)
            java.lang.String r9 = r6.getPath()
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            android.util.Log.e(r7, r8)
        L_0x007a:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r7, r8)
            boolean r7 = r11.recoveryDB()
            if (r7 != 0) goto L_0x00df
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r7 = java.lang.String.valueOf(r0)
            r5.<init>(r7)
            java.lang.String r7 = "/"
            r5.append(r7)
            java.lang.String r7 = "Enroll_Data"
            r5.append(r7)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x00c1
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x00c1
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList delete file error"
            android.util.Log.e(r5, r7)
        L_0x00c1:
            egis.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "DB is cleared"
            android.util.Log.e(r5, r7)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r7 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r7)
            if (r5 != 0) goto L_0x01c1
        L_0x00d7:
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = " Set default data fail"
            android.util.Log.e(r4, r5)
            return r1
        L_0x00df:
            android.content.Context r7 = mContext     // Catch:{ FileNotFoundException -> 0x016c, IOException -> 0x012f }
            java.lang.String r8 = "Enroll_Data"
            java.io.FileInputStream r7 = r7.openFileInput(r8)     // Catch:{ FileNotFoundException -> 0x016c, IOException -> 0x012f }
            r0 = r7
            if (r0 == 0) goto L_0x00fb
            egis.finger.host.FileDB r7 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x016c, IOException -> 0x012f }
            r7.Load(r0)     // Catch:{ FileNotFoundException -> 0x016c, IOException -> 0x012f }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r7, r8)     // Catch:{ FileNotFoundException -> 0x016c, IOException -> 0x012f }
            r0.close()     // Catch:{ FileNotFoundException -> 0x016c, IOException -> 0x012f }
        L_0x00fb:
            if (r0 == 0) goto L_0x010f
            r0.close()     // Catch:{ IOException -> 0x0101 }
            goto L_0x010f
        L_0x0101:
            r3 = move-exception
            r3.printStackTrace()
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList retry close DB fail"
            android.util.Log.e(r4, r7)
        L_0x010c:
            egis.finger.host.FPNativeBase.lastErrCode = r5
            return r1
        L_0x010f:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x01c1
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x01c1
        L_0x0124:
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r4 = "refreshList reInitDB fail"
            android.util.Log.e(r3, r4)
        L_0x012b:
            return r1
        L_0x012c:
            r3 = move-exception
            goto L_0x01a2
        L_0x012f:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x012c }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r7)     // Catch:{ all -> 0x012c }
            egis.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x012c }
            if (r0 == 0) goto L_0x014e
            r0.close()     // Catch:{ IOException -> 0x0142 }
            goto L_0x014e
        L_0x0142:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0146:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry close DB fail"
            android.util.Log.e(r7, r8)
            goto L_0x010c
        L_0x014e:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x016b
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x016b
        L_0x0163:
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refreshList reInitDB fail"
            android.util.Log.e(r4, r5)
            goto L_0x012b
        L_0x016b:
            return r1
        L_0x016c:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x012c }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x012c }
            egis.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x012c }
            if (r0 == 0) goto L_0x0184
            r0.close()     // Catch:{ IOException -> 0x017f }
            goto L_0x0184
        L_0x017f:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0146
        L_0x0184:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x01a1
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x01a1
        L_0x0199:
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refreshList reInitDB fail"
            android.util.Log.e(r3, r5)
            goto L_0x012b
        L_0x01a1:
            return r1
        L_0x01a2:
            if (r0 == 0) goto L_0x01a9
            r0.close()     // Catch:{ IOException -> 0x0101 }
        L_0x01a9:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x01c0
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x01c0
            goto L_0x0124
        L_0x01c0:
            throw r3
        L_0x01c1:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x01cf
        L_0x01c7:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile complete"
            android.util.Log.e(r0, r1)
            return r4
        L_0x01cf:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            return r4
        L_0x01d7:
            r6 = move-exception
            goto L_0x07c9
        L_0x01da:
            r6 = move-exception
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d7 }
            java.lang.String r9 = "refreshList(): IOException:"
            r8.<init>(r9)     // Catch:{ all -> 0x01d7 }
            java.lang.String r9 = r6.getMessage()     // Catch:{ all -> 0x01d7 }
            r8.append(r9)     // Catch:{ all -> 0x01d7 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x01d7 }
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x01d7 }
            r6.printStackTrace()     // Catch:{ all -> 0x01d7 }
            egis.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x01d7 }
            if (r2 == 0) goto L_0x0201
            r2.close()     // Catch:{ IOException -> 0x01fd }
            goto L_0x0201
        L_0x01fd:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0201:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x0361
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x0233
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x0233
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "refreshList delete file fail "
            r9.<init>(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x0233:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x0292
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r0)
            r5.<init>(r8)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x027a
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x027a
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x027a:
            egis.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x0361
            goto L_0x00d7
        L_0x0292:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x0312, IOException -> 0x02da }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x0312, IOException -> 0x02da }
            r0 = r8
            if (r0 == 0) goto L_0x02ae
            egis.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0312, IOException -> 0x02da }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x0312, IOException -> 0x02da }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x0312, IOException -> 0x02da }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0312, IOException -> 0x02da }
        L_0x02ae:
            if (r0 == 0) goto L_0x02c1
            r0.close()     // Catch:{ IOException -> 0x02b4 }
            goto L_0x02c1
        L_0x02b4:
            r3 = move-exception
            r3.printStackTrace()
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry close DB fail"
            android.util.Log.e(r4, r8)
            goto L_0x010c
        L_0x02c1:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0361
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0361
            goto L_0x0124
        L_0x02d8:
            r3 = move-exception
            goto L_0x0342
        L_0x02da:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x02d8 }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r8)     // Catch:{ all -> 0x02d8 }
            egis.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x02d8 }
            if (r0 == 0) goto L_0x02fa
            r0.close()     // Catch:{ IOException -> 0x02ed }
            goto L_0x02fa
        L_0x02ed:
            r4 = move-exception
            r4.printStackTrace()
        L_0x02f1:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry close DB fail"
            android.util.Log.e(r8, r9)
            goto L_0x010c
        L_0x02fa:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0311
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0311
            goto L_0x0163
        L_0x0311:
            return r1
        L_0x0312:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x02d8 }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry to load DB fail"
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x02d8 }
            egis.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x02d8 }
            if (r0 == 0) goto L_0x032a
            r0.close()     // Catch:{ IOException -> 0x0325 }
            goto L_0x032a
        L_0x0325:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x02f1
        L_0x032a:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0341
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0341
            goto L_0x0199
        L_0x0341:
            return r1
        L_0x0342:
            if (r0 == 0) goto L_0x0349
            r0.close()     // Catch:{ IOException -> 0x02b4 }
        L_0x0349:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0360
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0360
            goto L_0x0124
        L_0x0360:
            throw r3
        L_0x0361:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x0369
            goto L_0x01c7
        L_0x0369:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r3)
            return r1
        L_0x0371:
            r6 = move-exception
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d7 }
            java.lang.String r9 = "refreshList(): FileNotFoundException:"
            r8.<init>(r9)     // Catch:{ all -> 0x01d7 }
            java.lang.String r9 = r6.getMessage()     // Catch:{ all -> 0x01d7 }
            r8.append(r9)     // Catch:{ all -> 0x01d7 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x01d7 }
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x01d7 }
            r6.printStackTrace()     // Catch:{ all -> 0x01d7 }
            boolean r7 = r11.recoveryDB()     // Catch:{ all -> 0x01d7 }
            if (r7 == 0) goto L_0x04f4
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = " refreshList() recoveryDB  complete"
            android.util.Log.d(r7, r8)     // Catch:{ all -> 0x01d7 }
            if (r2 == 0) goto L_0x03a3
            r2.close()     // Catch:{ IOException -> 0x039f }
            goto L_0x03a3
        L_0x039f:
            r7 = move-exception
            r7.printStackTrace()
        L_0x03a3:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x04e4
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x03d5
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x03d5
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "refreshList delete file fail "
            r9.<init>(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x03d5:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x0434
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r0)
            r5.<init>(r8)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x041c
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x041c
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x041c:
            egis.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x04e4
            goto L_0x00d7
        L_0x0434:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x049a, IOException -> 0x046f }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x049a, IOException -> 0x046f }
            r0 = r8
            if (r0 == 0) goto L_0x0450
            egis.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x049a, IOException -> 0x046f }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x049a, IOException -> 0x046f }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x049a, IOException -> 0x046f }
            r0.close()     // Catch:{ FileNotFoundException -> 0x049a, IOException -> 0x046f }
        L_0x0450:
            if (r0 == 0) goto L_0x0456
            r0.close()     // Catch:{ IOException -> 0x02b4 }
        L_0x0456:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x04e4
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x04e4
            goto L_0x0124
        L_0x046d:
            r3 = move-exception
            goto L_0x04c5
        L_0x046f:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x046d }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r8)     // Catch:{ all -> 0x046d }
            egis.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x046d }
            if (r0 == 0) goto L_0x0482
            r0.close()     // Catch:{ IOException -> 0x02ed }
        L_0x0482:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0499
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0499
            goto L_0x0163
        L_0x0499:
            return r1
        L_0x049a:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x046d }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry to load DB fail"
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x046d }
            egis.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x046d }
            if (r0 == 0) goto L_0x04ad
            r0.close()     // Catch:{ IOException -> 0x0325 }
        L_0x04ad:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x04c4
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x04c4
            goto L_0x0199
        L_0x04c4:
            return r1
        L_0x04c5:
            if (r0 == 0) goto L_0x04cc
            r0.close()     // Catch:{ IOException -> 0x02b4 }
        L_0x04cc:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x04e3
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x04e3
            goto L_0x0124
        L_0x04e3:
            throw r3
        L_0x04e4:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x04ec
            goto L_0x01c7
        L_0x04ec:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            return r4
        L_0x04f4:
            java.lang.String r7 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r7 = r11.dataSet(r7, r8)     // Catch:{ all -> 0x01d7 }
            if (r7 == 0) goto L_0x0660
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList init default data complete"
            android.util.Log.d(r7, r8)     // Catch:{ all -> 0x01d7 }
            if (r2 == 0) goto L_0x050f
            r2.close()     // Catch:{ IOException -> 0x050b }
            goto L_0x050f
        L_0x050b:
            r7 = move-exception
            r7.printStackTrace()
        L_0x050f:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x0650
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x0541
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x0541
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "refreshList delete file fail "
            r9.<init>(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x0541:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x05a0
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r0)
            r5.<init>(r8)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x0588
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x0588
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x0588:
            egis.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x0650
            goto L_0x00d7
        L_0x05a0:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x0606, IOException -> 0x05db }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x0606, IOException -> 0x05db }
            r0 = r8
            if (r0 == 0) goto L_0x05bc
            egis.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0606, IOException -> 0x05db }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x0606, IOException -> 0x05db }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x0606, IOException -> 0x05db }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0606, IOException -> 0x05db }
        L_0x05bc:
            if (r0 == 0) goto L_0x05c2
            r0.close()     // Catch:{ IOException -> 0x02b4 }
        L_0x05c2:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0650
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0650
            goto L_0x0124
        L_0x05d9:
            r3 = move-exception
            goto L_0x0631
        L_0x05db:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x05d9 }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r8)     // Catch:{ all -> 0x05d9 }
            egis.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x05d9 }
            if (r0 == 0) goto L_0x05ee
            r0.close()     // Catch:{ IOException -> 0x02ed }
        L_0x05ee:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0605
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0605
            goto L_0x0163
        L_0x0605:
            return r1
        L_0x0606:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x05d9 }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry to load DB fail"
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x05d9 }
            egis.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x05d9 }
            if (r0 == 0) goto L_0x0619
            r0.close()     // Catch:{ IOException -> 0x0325 }
        L_0x0619:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0630
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0630
            goto L_0x0199
        L_0x0630:
            return r1
        L_0x0631:
            if (r0 == 0) goto L_0x0638
            r0.close()     // Catch:{ IOException -> 0x02b4 }
        L_0x0638:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x064f
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x064f
            goto L_0x0124
        L_0x064f:
            throw r3
        L_0x0650:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x0658
            goto L_0x01c7
        L_0x0658:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            return r4
        L_0x0660:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList file not found, fail to recovertDB and dataSet"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x01d7 }
            if (r2 == 0) goto L_0x0671
            r2.close()     // Catch:{ IOException -> 0x066d }
            goto L_0x0671
        L_0x066d:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0671:
            java.lang.String r6 = "Enroll_Data"
            boolean r6 = r11.checkFileState(r6)
            if (r6 != 0) goto L_0x07b2
            java.io.File r6 = new java.io.File
            java.lang.String r7 = "Enroll_Data"
            r6.<init>(r7)
            boolean r7 = r6.exists()
            if (r7 == 0) goto L_0x06a3
            boolean r7 = r6.delete()
            if (r7 == 0) goto L_0x06a3
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "refreshList delete file fail "
            r8.<init>(r9)
            java.lang.String r9 = r6.getPath()
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            android.util.Log.e(r7, r8)
        L_0x06a3:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r7, r8)
            boolean r7 = r11.recoveryDB()
            if (r7 != 0) goto L_0x0702
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r7 = java.lang.String.valueOf(r0)
            r5.<init>(r7)
            java.lang.String r7 = "/"
            r5.append(r7)
            java.lang.String r7 = "Enroll_Data"
            r5.append(r7)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x06ea
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x06ea
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList delete file error"
            android.util.Log.e(r5, r7)
        L_0x06ea:
            egis.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "DB is cleared"
            android.util.Log.e(r5, r7)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r7 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r7)
            if (r5 != 0) goto L_0x07b2
            goto L_0x00d7
        L_0x0702:
            android.content.Context r7 = mContext     // Catch:{ FileNotFoundException -> 0x0768, IOException -> 0x073d }
            java.lang.String r8 = "Enroll_Data"
            java.io.FileInputStream r7 = r7.openFileInput(r8)     // Catch:{ FileNotFoundException -> 0x0768, IOException -> 0x073d }
            r0 = r7
            if (r0 == 0) goto L_0x071e
            egis.finger.host.FileDB r7 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0768, IOException -> 0x073d }
            r7.Load(r0)     // Catch:{ FileNotFoundException -> 0x0768, IOException -> 0x073d }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r7, r8)     // Catch:{ FileNotFoundException -> 0x0768, IOException -> 0x073d }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0768, IOException -> 0x073d }
        L_0x071e:
            if (r0 == 0) goto L_0x0724
            r0.close()     // Catch:{ IOException -> 0x0101 }
        L_0x0724:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x07b2
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x07b2
            goto L_0x0124
        L_0x073b:
            r3 = move-exception
            goto L_0x0793
        L_0x073d:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x073b }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r7)     // Catch:{ all -> 0x073b }
            egis.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x073b }
            if (r0 == 0) goto L_0x0750
            r0.close()     // Catch:{ IOException -> 0x0142 }
        L_0x0750:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0767
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0767
            goto L_0x0163
        L_0x0767:
            return r1
        L_0x0768:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x073b }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x073b }
            egis.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x073b }
            if (r0 == 0) goto L_0x077b
            r0.close()     // Catch:{ IOException -> 0x017f }
        L_0x077b:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0792
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0792
            goto L_0x0199
        L_0x0792:
            return r1
        L_0x0793:
            if (r0 == 0) goto L_0x079a
            r0.close()     // Catch:{ IOException -> 0x0101 }
        L_0x079a:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x07b1
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x07b1
            goto L_0x0124
        L_0x07b1:
            throw r3
        L_0x07b2:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x07ba
            goto L_0x01c7
        L_0x07ba:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList fail"
            android.util.Log.e(r0, r3)
            return r1
        L_0x07c9:
            if (r2 == 0) goto L_0x07d4
            r2.close()     // Catch:{ IOException -> 0x07d0 }
            goto L_0x07d4
        L_0x07d0:
            r7 = move-exception
            r7.printStackTrace()
        L_0x07d4:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x0934
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x0806
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x0806
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "refreshList delete file fail "
            r9.<init>(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x0806:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x0865
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r8 = java.lang.String.valueOf(r0)
            r5.<init>(r8)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x084d
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x084d
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x084d:
            egis.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x0934
            goto L_0x00d7
        L_0x0865:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x08e5, IOException -> 0x08ad }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x08e5, IOException -> 0x08ad }
            r0 = r8
            if (r0 == 0) goto L_0x0881
            egis.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x08e5, IOException -> 0x08ad }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x08e5, IOException -> 0x08ad }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x08e5, IOException -> 0x08ad }
            r0.close()     // Catch:{ FileNotFoundException -> 0x08e5, IOException -> 0x08ad }
        L_0x0881:
            if (r0 == 0) goto L_0x0894
            r0.close()     // Catch:{ IOException -> 0x0887 }
            goto L_0x0894
        L_0x0887:
            r3 = move-exception
            r3.printStackTrace()
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r6 = "refreshList retry close DB fail"
            android.util.Log.e(r4, r6)
            goto L_0x010c
        L_0x0894:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0934
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0934
            goto L_0x0124
        L_0x08ab:
            r3 = move-exception
            goto L_0x0915
        L_0x08ad:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x08ab }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r6 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r6)     // Catch:{ all -> 0x08ab }
            egis.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x08ab }
            if (r0 == 0) goto L_0x08cd
            r0.close()     // Catch:{ IOException -> 0x08c0 }
            goto L_0x08cd
        L_0x08c0:
            r4 = move-exception
            r4.printStackTrace()
        L_0x08c4:
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry close DB fail"
            android.util.Log.e(r6, r8)
            goto L_0x010c
        L_0x08cd:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x08e4
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x08e4
            goto L_0x0163
        L_0x08e4:
            return r1
        L_0x08e5:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x08ab }
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r6, r8)     // Catch:{ all -> 0x08ab }
            egis.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x08ab }
            if (r0 == 0) goto L_0x08fd
            r0.close()     // Catch:{ IOException -> 0x08f8 }
            goto L_0x08fd
        L_0x08f8:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x08c4
        L_0x08fd:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0914
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0914
            goto L_0x0199
        L_0x0914:
            return r1
        L_0x0915:
            if (r0 == 0) goto L_0x091c
            r0.close()     // Catch:{ IOException -> 0x0887 }
        L_0x091c:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0933
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0933
            goto L_0x0124
        L_0x0933:
            throw r3
        L_0x0934:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x093c
            goto L_0x01c7
        L_0x093c:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: egis.finger.host.FingerUtil.refreshList():boolean");
    }

    private boolean recoveryDB() {
        Log.d(TAG, "recoveryDB");
        StringBuilder sb = new StringBuilder(String.valueOf(getDBPath()));
        sb.append("/");
        sb.append(FEATURE_FILE);
        File file = new File(sb.toString());
        StringBuilder sb2 = new StringBuilder(String.valueOf(getDBPath()));
        sb2.append("/");
        sb2.append(FEATURE_FILE_BAK);
        File file_bak = new File(sb2.toString());
        if (!file_bak.exists()) {
            Log.e(TAG, "Backup DB file does not exist");
            FPNativeBase.lastErrCode = 2048;
            return false;
        } else if (file_bak.renameTo(file)) {
            Log.d(TAG, "recoveryDB complete");
            return true;
        } else {
            String str = TAG;
            StringBuilder sb3 = new StringBuilder("rename file faile ");
            sb3.append(file.getPath());
            sb3.append(" to ");
            sb3.append(file_bak.getPath());
            Log.e(str, sb3.toString());
            FPNativeBase.lastErrCode = 2051;
            return false;
        }
    }

    private boolean reInitDB() {
        Log.d(TAG, "reInitDB");
        StringBuilder sb = new StringBuilder(String.valueOf(getDBPath()));
        sb.append("/");
        sb.append(FEATURE_FILE_NEW);
        File file_new = new File(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(mContext.getFilesDir());
        sb2.append("/");
        sb2.append(FEATURE_FILE);
        File file = new File(sb2.toString());
        StringBuilder sb3 = new StringBuilder(String.valueOf(getDBPath()));
        sb3.append("/");
        sb3.append(FEATURE_FILE_BAK);
        File file_bak = new File(sb3.toString());
        file_new.delete();
        file.delete();
        file_bak.delete();
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE)) {
            Log.d(TAG, " onCreate init default data complete");
            return true;
        }
        Log.d(TAG, "reInitDB fail");
        return false;
    }

    private boolean replaceNewDB() {
        Log.d(TAG, "replaceNewDB");
        StringBuilder sb = new StringBuilder(String.valueOf(getDBPath()));
        sb.append("/");
        sb.append(FEATURE_FILE_NEW);
        File file_new = new File(sb.toString());
        if (file_new.exists()) {
            Log.e(TAG, "replaceNewDB DB new file exists, replace current DB");
            StringBuilder sb2 = new StringBuilder(String.valueOf(getDBPath()));
            sb2.append("/");
            sb2.append(FEATURE_FILE);
            File file = new File(sb2.toString());
            if (file.exists() && !file.delete()) {
                String str = TAG;
                StringBuilder sb3 = new StringBuilder("replaceNewDB delete file fail ");
                sb3.append(file.getPath());
                Log.e(str, sb3.toString());
            }
            if (!file_new.renameTo(file)) {
                String str2 = TAG;
                StringBuilder sb4 = new StringBuilder("replaceNewDB rename fail ");
                sb4.append(file_new.getPath());
                sb4.append(" to ");
                sb4.append(file.getPath());
                Log.e(str2, sb4.toString());
                FPNativeBase.lastErrCode = 2051;
                return false;
            }
            Log.d(TAG, "replaceNewDB complete");
            return true;
        }
        Log.d(TAG, "replaceNewDB new file does not exist");
        return true;
    }

    public boolean dataSet(String id, String value) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("dataSet id=");
        sb.append(id);
        sb.append("value=");
        sb.append(value);
        Log.d(str, sb.toString());
        if (!this.mFileDB.add(id, value)) {
            Log.e(TAG, "dataSet(): mFileDB.add() fail");
            return false;
        } else if (saveFeatureToFile()) {
            return true;
        } else {
            Log.e(TAG, "dataSet(): saveFeatureToFile() fail");
            return false;
        }
    }

    private boolean saveFeatureToBakFile() {
        Log.d(TAG, "svaeFeatureToBakFile");
        if (saveFeatureToFile(FEATURE_FILE_BAK)) {
            Log.d(TAG, "svaeFeatureToBakFile complete");
            return true;
        }
        Log.e(TAG, "saveFeatureToBakFile fail, retry");
        if (saveFeatureToFile(FEATURE_FILE_BAK)) {
            Log.d(TAG, "svaeFeatureToBakFile complete");
            return true;
        }
        Log.e(TAG, "saveFeatureToBakFile still fail");
        return false;
    }

    public byte[][] getAllFeature(String IdentifyUserId) {
        int size = 0;
        for (int i = 0; i < this.mFileDB.size(); i++) {
            String fid = this.mFileDB.readID(i);
            if (fid.startsWith("*") && fid.substring(1, fid.indexOf(";")).equals(IdentifyUserId)) {
                size++;
            }
        }
        int index = 0;
        byte[][] allFeature = new byte[size][];
        fileDBMap = new int[size];
        for (int i2 = 0; i2 < this.mFileDB.size(); i2++) {
            String fid2 = this.mFileDB.readID(i2);
            if (fid2.startsWith("*") && fid2.substring(1, fid2.indexOf(";")).equals(IdentifyUserId)) {
                String str = TAG;
                StringBuilder sb = new StringBuilder("getAllFeature() fid=");
                sb.append(fid2);
                Log.d(str, sb.toString());
                allFeature[index] = new byte[this.mFileDB.readFeature(i2).length];
                allFeature[index] = this.mFileDB.readFeature(i2);
                fileDBMap[index] = i2;
                index++;
            }
        }
        return allFeature;
    }

    public String getMatchedID(int matchIdx) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("+++++ getMatchedID matchIdx = ");
        sb.append(matchIdx);
        sb.append(" +++++");
        Log.d(str, sb.toString());
        String data = this.mFileDB.readID(matchIdx).substring(1);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder("data = ");
        sb2.append(data);
        Log.d(str2, sb2.toString());
        return data;
    }

    public String getEnrollListFromDB(String userID) {
        String fid;
        String str = TAG;
        StringBuilder sb = new StringBuilder("getEnrollListFromDB=");
        sb.append(userID);
        Log.d(str, sb.toString());
        String enrollList = null;
        for (int i = 0; i < this.mFileDB.size(); i++) {
            String fid2 = this.mFileDB.readID(i);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("fid=");
            sb2.append(fid2);
            Log.d(str2, sb2.toString());
            if (!userID.equals(new String("*"))) {
                if (fid2.startsWith("*")) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder("in startsWith() fid=");
                    sb3.append(fid2);
                    Log.d(str3, sb3.toString());
                    String dbUserId = fid2.substring(0, fid2.indexOf(";"));
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder("dbUserId=");
                    sb4.append(dbUserId);
                    Log.d(str4, sb4.toString());
                    if (userID.equals(dbUserId)) {
                        fid = fid2.substring(fid2.length() - 2);
                    }
                }
            } else {
                String str5 = TAG;
                StringBuilder sb5 = new StringBuilder("userID=");
                sb5.append(userID);
                Log.d(str5, sb5.toString());
                if (!fid2.startsWith("*")) {
                } else {
                    fid = fid2.substring(1);
                }
            }
            if (enrollList == null) {
                enrollList = fid;
            } else {
                StringBuilder sb6 = new StringBuilder(String.valueOf(enrollList));
                sb6.append(";");
                sb6.append(fid);
                enrollList = sb6.toString();
            }
            Log.d(TAG, "-------------------------");
        }
        String str6 = TAG;
        StringBuilder sb7 = new StringBuilder("enrollList=");
        sb7.append(enrollList);
        Log.d(str6, sb7.toString());
        return enrollList;
    }

    public String[] getUserIdList() {
        Log.d(TAG, "+++++ getUserIdList+++++");
        List<String> enrollList = new ArrayList<>();
        for (int i = 0; i < this.mFileDB.size(); i++) {
            String fid = this.mFileDB.readID(i);
            if (fid.startsWith("*") && fid.indexOf(";") != -1) {
                String userName = fid.substring(1, fid.indexOf(";"));
                if (!enrollList.contains(userName)) {
                    enrollList.add(userName);
                }
            }
        }
        if (enrollList.size() == 0) {
            Log.d(TAG, "no user fingers in the fileDB");
            return null;
        }
        String[] s = (String[]) enrollList.toArray(new String[enrollList.size()]);
        String str = TAG;
        StringBuilder sb = new StringBuilder("s = ");
        sb.append(s);
        Log.d(str, sb.toString());
        return s;
    }

    public boolean deleteFromDB(String userID) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("+++++ deleteFromDB  userID = ");
        sb.append(userID);
        sb.append(" +++++");
        Log.d(str, sb.toString());
        if (!this.mFileDB.delete(userID)) {
            Log.e(TAG, "deleteFromDB(): mFileDB.delete() fail");
            return false;
        } else if (saveFeatureToFile()) {
            return true;
        } else {
            Log.e(TAG, "deleteFromDB(): saveFeature() fail");
            return false;
        }
    }

    public boolean dataDelete(String id) {
        if (!this.mFileDB.delete(id)) {
            Log.e(TAG, "dataDelete(): mFileDB.delete() fail");
            return false;
        } else if (saveFeatureToFile()) {
            return true;
        } else {
            Log.e(TAG, "dataDelete(): saveFeatureToFile() fail");
            return false;
        }
    }
}
