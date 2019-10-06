package egis.optical.finger.host;

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
    private FileDB mFileDB = null;
    public byte[] mTemplate = new byte[1024];
    public int mTemplateSize = 0;

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
        StringBuilder sb = new StringBuilder();
        sb.append("postFpResultStatus status = ");
        sb.append(status);
        Log.d(str, sb.toString());
        mApHandler.obtainMessage(1000, status, -1).sendToTarget();
    }

    public boolean enrollToDB(String userID) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("enrollToDB userID=");
        sb.append(userID);
        Log.d(str, sb.toString());
        if (this.mFileDB == null) {
            Log.e(TAG, "enrollToDB FileDB==null");
            FPNativeBase.lastErrCode = 2030;
            return false;
        } else if (this.mTemplate == null) {
            Log.e(TAG, "mFeature == null");
            FPNativeBase.lastErrCode = 2034;
            return false;
        } else {
            if (!this.mFileDB.add(userID, (byte[]) this.mTemplate.clone())) {
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
                        StringBuilder sb = new StringBuilder();
                        sb.append("saveFeature() fos.close() exception=");
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
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("saveFeature() fos.close() exception=");
                    sb2.append(e2.getMessage());
                    Log.e(str2, sb2.toString());
                }
            }
            Log.d(TAG, "svaeFeatureToFile end");
            return true;
        } catch (FileNotFoundException e3) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("saveFeature() exception=");
            sb3.append(e3.getMessage());
            Log.e(str3, sb3.toString());
            FPNativeBase.lastErrCode = 2044;
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e4) {
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("saveFeature() fos.close() exception=");
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
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("saveFeature() fos.close() exception=");
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
        StringBuilder sb = new StringBuilder();
        sb.append("checkFileLen fileName = ");
        sb.append(fileName);
        Log.d(str, sb.toString());
        int len = 0;
        FileInputStream fis = null;
        BufferedInputStream ois = null;
        try {
            FileInputStream fis2 = mContext.openFileInput(fileName);
            if (fis2 != null) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("checkDBLen fislen = ");
                sb2.append(fis2.available());
                Log.d(str2, sb2.toString());
                ois = new BufferedInputStream(fis2);
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("checkDBLen oislen = ");
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
            StringBuilder sb4 = new StringBuilder();
            sb4.append("checkDBLen(): FileNotFoundException:");
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
            StringBuilder sb5 = new StringBuilder();
            sb5.append("checkDBLen(): IOException:");
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
        StringBuilder sb6 = new StringBuilder();
        sb6.append("checkFileLen len = ");
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
        StringBuilder sb = new StringBuilder();
        sb.append(getDBPath());
        sb.append("/");
        sb.append(FEATURE_FILE_NEW);
        File file_new = new File(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(mContext.getFilesDir());
        sb2.append("/");
        sb2.append(FEATURE_FILE);
        File file = new File(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getDBPath());
        sb3.append("/");
        sb3.append(FEATURE_FILE_BAK);
        File file_bak = new File(sb3.toString());
        if (file_bak.exists() && !file_bak.delete()) {
            String str = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("replaceDB delete file fail ");
            sb4.append(file_bak.getPath());
            Log.e(str, sb4.toString());
            FPNativeBase.lastErrCode = 2044;
        }
        if (!file.renameTo(file_bak)) {
            String str2 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("replaceDB rename file fail ");
            sb5.append(file.getPath());
            sb5.append(" to ");
            sb5.append(file_bak.getPath());
            Log.e(str2, sb5.toString());
        }
        if (file.exists() && !file.delete()) {
            String str3 = TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("replaceDB delete file fail ");
            sb6.append(file.getPath());
            Log.e(str3, sb6.toString());
            FPNativeBase.lastErrCode = 2044;
        }
        if (file_new.renameTo(file)) {
            Log.d(TAG, "replaceDB complete");
            return true;
        }
        String str4 = TAG;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("replaceDB rename file fail ");
        sb7.append(file_new.getPath());
        sb7.append(" to ");
        sb7.append(file.getPath());
        Log.e(str4, sb7.toString());
        FPNativeBase.lastErrCode = 2051;
        return false;
    }

    private boolean checkFileState(String fileName) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkFileState fileName = ");
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
            StringBuilder sb = new StringBuilder();
            sb.append("getDBPath()=");
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
        StringBuilder sb = new StringBuilder();
        sb.append("dataRead(): start, id=");
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
            StringBuilder sb2 = new StringBuilder();
            sb2.append("data = ");
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
        StringBuilder sb = new StringBuilder();
        sb.append("checkId(): start, id=");
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
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x029d, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02e4, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x031e, code lost:
        if (reInitDB() == false) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x034f, code lost:
        if (reInitDB() == false) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x036d, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x0449, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:210:0x0483, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:221:0x04b0, code lost:
        if (reInitDB() == false) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x04dc, code lost:
        if (reInitDB() == false) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x04fa, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:271:0x05bc, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:282:0x05f6, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:293:0x0623, code lost:
        if (reInitDB() == false) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:304:0x064f, code lost:
        if (reInitDB() == false) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00da, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:312:0x066d, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00dc, code lost:
        android.util.Log.e(TAG, " Set default data fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00e3, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:341:0x0724, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:352:0x075e, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:363:0x078b, code lost:
        if (reInitDB() == false) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:374:0x07b7, code lost:
        if (reInitDB() == false) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:382:0x07d5, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:408:0x088e, code lost:
        if (dataSet(FEATURE_FILE_DEFAULT_ID, FEATURE_FILE_DEFAULT_VAULE) == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:421:0x08d5, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:435:0x090f, code lost:
        if (reInitDB() == false) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:448:0x0940, code lost:
        if (reInitDB() == false) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:456:0x095e, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0128, code lost:
        if (reInitDB() == false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x012a, code lost:
        android.util.Log.e(TAG, "refreshList reInitDB fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0131, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0168, code lost:
        if (reInitDB() == false) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x016a, code lost:
        android.util.Log.e(TAG, "refreshList reInitDB fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x019f, code lost:
        if (reInitDB() == false) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01a1, code lost:
        android.util.Log.e(TAG, "refreshList reInitDB fail");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01c3, code lost:
        if (reInitDB() == false) goto L_0x012a;
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
            android.content.Context r6 = mContext     // Catch:{ FileNotFoundException -> 0x0382, IOException -> 0x01e1 }
            java.lang.String r7 = "Enroll_Data"
            java.io.FileInputStream r6 = r6.openFileInput(r7)     // Catch:{ FileNotFoundException -> 0x0382, IOException -> 0x01e1 }
            r2 = r6
            if (r2 == 0) goto L_0x0037
            egis.optical.finger.host.FileDB r6 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0382, IOException -> 0x01e1 }
            r6.Load(r2)     // Catch:{ FileNotFoundException -> 0x0382, IOException -> 0x01e1 }
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r6, r7)     // Catch:{ FileNotFoundException -> 0x0382, IOException -> 0x01e1 }
            r2.close()     // Catch:{ FileNotFoundException -> 0x0382, IOException -> 0x01e1 }
        L_0x0037:
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList load DB complete"
            android.util.Log.d(r6, r7)     // Catch:{ FileNotFoundException -> 0x0382, IOException -> 0x01e1 }
            if (r2 == 0) goto L_0x004a
            r2.close()     // Catch:{ IOException -> 0x0045 }
            goto L_0x004a
        L_0x0045:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x004b
        L_0x004a:
        L_0x004b:
            java.lang.String r6 = "Enroll_Data"
            boolean r6 = r11.checkFileState(r6)
            if (r6 != 0) goto L_0x01c8
            java.io.File r6 = new java.io.File
            java.lang.String r7 = "Enroll_Data"
            r6.<init>(r7)
            boolean r7 = r6.exists()
            if (r7 == 0) goto L_0x0080
            boolean r7 = r6.delete()
            if (r7 == 0) goto L_0x0080
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "refreshList delete file fail "
            r8.append(r9)
            java.lang.String r9 = r6.getPath()
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            android.util.Log.e(r7, r8)
        L_0x0080:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r7, r8)
            boolean r7 = r11.recoveryDB()
            if (r7 != 0) goto L_0x00e6
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r0)
            java.lang.String r7 = "/"
            r5.append(r7)
            java.lang.String r7 = "Enroll_Data"
            r5.append(r7)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x00c6
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x00c6
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList delete file error"
            android.util.Log.e(r5, r7)
        L_0x00c6:
            egis.optical.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "DB is cleared"
            android.util.Log.e(r5, r7)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r7 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r7)
            if (r5 != 0) goto L_0x00e4
        L_0x00dc:
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = " Set default data fail"
            android.util.Log.e(r4, r5)
            return r1
        L_0x00e4:
            goto L_0x01c8
        L_0x00e6:
            android.content.Context r7 = mContext     // Catch:{ FileNotFoundException -> 0x0173, IOException -> 0x0135 }
            java.lang.String r8 = "Enroll_Data"
            java.io.FileInputStream r7 = r7.openFileInput(r8)     // Catch:{ FileNotFoundException -> 0x0173, IOException -> 0x0135 }
            r0 = r7
            if (r0 == 0) goto L_0x0101
            egis.optical.finger.host.FileDB r7 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0173, IOException -> 0x0135 }
            r7.Load(r0)     // Catch:{ FileNotFoundException -> 0x0173, IOException -> 0x0135 }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r7, r8)     // Catch:{ FileNotFoundException -> 0x0173, IOException -> 0x0135 }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0173, IOException -> 0x0135 }
        L_0x0101:
            if (r0 == 0) goto L_0x0115
            r0.close()     // Catch:{ IOException -> 0x0107 }
            goto L_0x0115
        L_0x0107:
            r3 = move-exception
            r3.printStackTrace()
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList retry close DB fail"
            android.util.Log.e(r4, r7)
        L_0x0112:
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5
            return r1
        L_0x0115:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x01c8
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x01c8
        L_0x012a:
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r4 = "refreshList reInitDB fail"
            android.util.Log.e(r3, r4)
        L_0x0131:
            return r1
        L_0x0132:
            r3 = move-exception
            goto L_0x01aa
        L_0x0135:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0132 }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r7)     // Catch:{ all -> 0x0132 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x0132 }
            if (r0 == 0) goto L_0x0155
            r0.close()     // Catch:{ IOException -> 0x0149 }
            goto L_0x0155
        L_0x0149:
            r4 = move-exception
            r4.printStackTrace()
        L_0x014d:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry close DB fail"
            android.util.Log.e(r7, r8)
            goto L_0x0112
        L_0x0155:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0172
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0172
        L_0x016a:
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refreshList reInitDB fail"
            android.util.Log.e(r4, r5)
            goto L_0x0131
        L_0x0172:
            return r1
        L_0x0173:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x0132 }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x0132 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x0132 }
            if (r0 == 0) goto L_0x018c
            r0.close()     // Catch:{ IOException -> 0x0187 }
            goto L_0x018c
        L_0x0187:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x014d
        L_0x018c:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x01a9
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x01a9
        L_0x01a1:
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refreshList reInitDB fail"
            android.util.Log.e(r3, r5)
            goto L_0x0131
        L_0x01a9:
            return r1
        L_0x01aa:
            if (r0 == 0) goto L_0x01b0
            r0.close()     // Catch:{ IOException -> 0x0107 }
        L_0x01b0:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x01c7
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x01c7
            goto L_0x012a
        L_0x01c7:
            throw r3
        L_0x01c8:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x01d6
        L_0x01ce:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile complete"
            android.util.Log.e(r0, r1)
            return r4
        L_0x01d6:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            return r4
        L_0x01de:
            r6 = move-exception
            goto L_0x07f2
        L_0x01e1:
            r6 = move-exception
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x01de }
            r8.<init>()     // Catch:{ all -> 0x01de }
            java.lang.String r9 = "refreshList(): IOException:"
            r8.append(r9)     // Catch:{ all -> 0x01de }
            java.lang.String r9 = r6.getMessage()     // Catch:{ all -> 0x01de }
            r8.append(r9)     // Catch:{ all -> 0x01de }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x01de }
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x01de }
            r6.printStackTrace()     // Catch:{ all -> 0x01de }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x01de }
            if (r2 == 0) goto L_0x020d
            r2.close()     // Catch:{ IOException -> 0x0208 }
            goto L_0x020d
        L_0x0208:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x020e
        L_0x020d:
        L_0x020e:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x0372
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x0243
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x0243
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "refreshList delete file fail "
            r9.append(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x0243:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x02a3
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r0)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x0289
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x0289
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x0289:
            egis.optical.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x02a1
            goto L_0x00dc
        L_0x02a1:
            goto L_0x0372
        L_0x02a3:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x0323, IOException -> 0x02ea }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x0323, IOException -> 0x02ea }
            r0 = r8
            if (r0 == 0) goto L_0x02be
            egis.optical.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0323, IOException -> 0x02ea }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x0323, IOException -> 0x02ea }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x0323, IOException -> 0x02ea }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0323, IOException -> 0x02ea }
        L_0x02be:
            if (r0 == 0) goto L_0x02d1
            r0.close()     // Catch:{ IOException -> 0x02c4 }
            goto L_0x02d1
        L_0x02c4:
            r3 = move-exception
            r3.printStackTrace()
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry close DB fail"
            android.util.Log.e(r4, r8)
            goto L_0x0112
        L_0x02d1:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0372
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0372
            goto L_0x012a
        L_0x02e8:
            r3 = move-exception
            goto L_0x0354
        L_0x02ea:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x02e8 }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r8)     // Catch:{ all -> 0x02e8 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x02e8 }
            if (r0 == 0) goto L_0x030b
            r0.close()     // Catch:{ IOException -> 0x02fe }
            goto L_0x030b
        L_0x02fe:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0302:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry close DB fail"
            android.util.Log.e(r8, r9)
            goto L_0x0112
        L_0x030b:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0322
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0322
            goto L_0x016a
        L_0x0322:
            return r1
        L_0x0323:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x02e8 }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry to load DB fail"
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x02e8 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x02e8 }
            if (r0 == 0) goto L_0x033c
            r0.close()     // Catch:{ IOException -> 0x0337 }
            goto L_0x033c
        L_0x0337:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0302
        L_0x033c:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0353
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0353
            goto L_0x01a1
        L_0x0353:
            return r1
        L_0x0354:
            if (r0 == 0) goto L_0x035a
            r0.close()     // Catch:{ IOException -> 0x02c4 }
        L_0x035a:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0371
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0371
            goto L_0x012a
        L_0x0371:
            throw r3
        L_0x0372:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x037a
            goto L_0x01ce
        L_0x037a:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r3)
            return r1
        L_0x0382:
            r6 = move-exception
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x01de }
            r8.<init>()     // Catch:{ all -> 0x01de }
            java.lang.String r9 = "refreshList(): FileNotFoundException:"
            r8.append(r9)     // Catch:{ all -> 0x01de }
            java.lang.String r9 = r6.getMessage()     // Catch:{ all -> 0x01de }
            r8.append(r9)     // Catch:{ all -> 0x01de }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x01de }
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x01de }
            r6.printStackTrace()     // Catch:{ all -> 0x01de }
            boolean r7 = r11.recoveryDB()     // Catch:{ all -> 0x01de }
            if (r7 == 0) goto L_0x050f
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = " refreshList() recoveryDB  complete"
            android.util.Log.d(r7, r8)     // Catch:{ all -> 0x01de }
            if (r2 == 0) goto L_0x03b9
            r2.close()     // Catch:{ IOException -> 0x03b4 }
            goto L_0x03b9
        L_0x03b4:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x03ba
        L_0x03b9:
        L_0x03ba:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x04ff
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x03ef
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x03ef
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "refreshList delete file fail "
            r9.append(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x03ef:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x044f
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r0)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x0435
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x0435
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x0435:
            egis.optical.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x044d
            goto L_0x00dc
        L_0x044d:
            goto L_0x04ff
        L_0x044f:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x04b5, IOException -> 0x0489 }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x04b5, IOException -> 0x0489 }
            r0 = r8
            if (r0 == 0) goto L_0x046a
            egis.optical.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x04b5, IOException -> 0x0489 }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x04b5, IOException -> 0x0489 }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x04b5, IOException -> 0x0489 }
            r0.close()     // Catch:{ FileNotFoundException -> 0x04b5, IOException -> 0x0489 }
        L_0x046a:
            if (r0 == 0) goto L_0x0470
            r0.close()     // Catch:{ IOException -> 0x02c4 }
        L_0x0470:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x04ff
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x04ff
            goto L_0x012a
        L_0x0487:
            r3 = move-exception
            goto L_0x04e1
        L_0x0489:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0487 }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r8)     // Catch:{ all -> 0x0487 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x0487 }
            if (r0 == 0) goto L_0x049d
            r0.close()     // Catch:{ IOException -> 0x02fe }
        L_0x049d:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x04b4
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x04b4
            goto L_0x016a
        L_0x04b4:
            return r1
        L_0x04b5:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x0487 }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry to load DB fail"
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x0487 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x0487 }
            if (r0 == 0) goto L_0x04c9
            r0.close()     // Catch:{ IOException -> 0x0337 }
        L_0x04c9:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x04e0
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x04e0
            goto L_0x01a1
        L_0x04e0:
            return r1
        L_0x04e1:
            if (r0 == 0) goto L_0x04e7
            r0.close()     // Catch:{ IOException -> 0x02c4 }
        L_0x04e7:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x04fe
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x04fe
            goto L_0x012a
        L_0x04fe:
            throw r3
        L_0x04ff:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x0507
            goto L_0x01ce
        L_0x0507:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            return r4
        L_0x050f:
            java.lang.String r7 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r7 = r11.dataSet(r7, r8)     // Catch:{ all -> 0x01de }
            if (r7 == 0) goto L_0x0682
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList init default data complete"
            android.util.Log.d(r7, r8)     // Catch:{ all -> 0x01de }
            if (r2 == 0) goto L_0x052c
            r2.close()     // Catch:{ IOException -> 0x0527 }
            goto L_0x052c
        L_0x0527:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x052d
        L_0x052c:
        L_0x052d:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x0672
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x0562
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x0562
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "refreshList delete file fail "
            r9.append(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x0562:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x05c2
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r0)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x05a8
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x05a8
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x05a8:
            egis.optical.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x05c0
            goto L_0x00dc
        L_0x05c0:
            goto L_0x0672
        L_0x05c2:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x0628, IOException -> 0x05fc }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x0628, IOException -> 0x05fc }
            r0 = r8
            if (r0 == 0) goto L_0x05dd
            egis.optical.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0628, IOException -> 0x05fc }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x0628, IOException -> 0x05fc }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x0628, IOException -> 0x05fc }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0628, IOException -> 0x05fc }
        L_0x05dd:
            if (r0 == 0) goto L_0x05e3
            r0.close()     // Catch:{ IOException -> 0x02c4 }
        L_0x05e3:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0672
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0672
            goto L_0x012a
        L_0x05fa:
            r3 = move-exception
            goto L_0x0654
        L_0x05fc:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x05fa }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r8)     // Catch:{ all -> 0x05fa }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x05fa }
            if (r0 == 0) goto L_0x0610
            r0.close()     // Catch:{ IOException -> 0x02fe }
        L_0x0610:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0627
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0627
            goto L_0x016a
        L_0x0627:
            return r1
        L_0x0628:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x05fa }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList retry to load DB fail"
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x05fa }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x05fa }
            if (r0 == 0) goto L_0x063c
            r0.close()     // Catch:{ IOException -> 0x0337 }
        L_0x063c:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0653
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0653
            goto L_0x01a1
        L_0x0653:
            return r1
        L_0x0654:
            if (r0 == 0) goto L_0x065a
            r0.close()     // Catch:{ IOException -> 0x02c4 }
        L_0x065a:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0671
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0671
            goto L_0x012a
        L_0x0671:
            throw r3
        L_0x0672:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x067a
            goto L_0x01ce
        L_0x067a:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            return r4
        L_0x0682:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList file not found, fail to recovertDB and dataSet"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x01de }
            if (r2 == 0) goto L_0x0694
            r2.close()     // Catch:{ IOException -> 0x068f }
            goto L_0x0694
        L_0x068f:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x0695
        L_0x0694:
        L_0x0695:
            java.lang.String r6 = "Enroll_Data"
            boolean r6 = r11.checkFileState(r6)
            if (r6 != 0) goto L_0x07da
            java.io.File r6 = new java.io.File
            java.lang.String r7 = "Enroll_Data"
            r6.<init>(r7)
            boolean r7 = r6.exists()
            if (r7 == 0) goto L_0x06ca
            boolean r7 = r6.delete()
            if (r7 == 0) goto L_0x06ca
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "refreshList delete file fail "
            r8.append(r9)
            java.lang.String r9 = r6.getPath()
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            android.util.Log.e(r7, r8)
        L_0x06ca:
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r7, r8)
            boolean r7 = r11.recoveryDB()
            if (r7 != 0) goto L_0x072a
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r0)
            java.lang.String r7 = "/"
            r5.append(r7)
            java.lang.String r7 = "Enroll_Data"
            r5.append(r7)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x0710
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x0710
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList delete file error"
            android.util.Log.e(r5, r7)
        L_0x0710:
            egis.optical.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "DB is cleared"
            android.util.Log.e(r5, r7)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r7 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r7)
            if (r5 != 0) goto L_0x0728
            goto L_0x00dc
        L_0x0728:
            goto L_0x07da
        L_0x072a:
            android.content.Context r7 = mContext     // Catch:{ FileNotFoundException -> 0x0790, IOException -> 0x0764 }
            java.lang.String r8 = "Enroll_Data"
            java.io.FileInputStream r7 = r7.openFileInput(r8)     // Catch:{ FileNotFoundException -> 0x0790, IOException -> 0x0764 }
            r0 = r7
            if (r0 == 0) goto L_0x0745
            egis.optical.finger.host.FileDB r7 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0790, IOException -> 0x0764 }
            r7.Load(r0)     // Catch:{ FileNotFoundException -> 0x0790, IOException -> 0x0764 }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r7, r8)     // Catch:{ FileNotFoundException -> 0x0790, IOException -> 0x0764 }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0790, IOException -> 0x0764 }
        L_0x0745:
            if (r0 == 0) goto L_0x074b
            r0.close()     // Catch:{ IOException -> 0x0107 }
        L_0x074b:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x07da
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x07da
            goto L_0x012a
        L_0x0762:
            r3 = move-exception
            goto L_0x07bc
        L_0x0764:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0762 }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r7 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r7)     // Catch:{ all -> 0x0762 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x0762 }
            if (r0 == 0) goto L_0x0778
            r0.close()     // Catch:{ IOException -> 0x0149 }
        L_0x0778:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x078f
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x078f
            goto L_0x016a
        L_0x078f:
            return r1
        L_0x0790:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x0762 }
            java.lang.String r7 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x0762 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x0762 }
            if (r0 == 0) goto L_0x07a4
            r0.close()     // Catch:{ IOException -> 0x0187 }
        L_0x07a4:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x07bb
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x07bb
            goto L_0x01a1
        L_0x07bb:
            return r1
        L_0x07bc:
            if (r0 == 0) goto L_0x07c2
            r0.close()     // Catch:{ IOException -> 0x0107 }
        L_0x07c2:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x07d9
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x07d9
            goto L_0x012a
        L_0x07d9:
            throw r3
        L_0x07da:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x07e2
            goto L_0x01ce
        L_0x07e2:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList fail"
            android.util.Log.e(r0, r3)
            return r1
        L_0x07f2:
            if (r2 == 0) goto L_0x07fe
            r2.close()     // Catch:{ IOException -> 0x07f9 }
            goto L_0x07fe
        L_0x07f9:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x07ff
        L_0x07fe:
        L_0x07ff:
            java.lang.String r7 = "Enroll_Data"
            boolean r7 = r11.checkFileState(r7)
            if (r7 != 0) goto L_0x0963
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "Enroll_Data"
            r7.<init>(r8)
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x0834
            boolean r8 = r7.delete()
            if (r8 == 0) goto L_0x0834
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "refreshList delete file fail "
            r9.append(r10)
            java.lang.String r10 = r7.getPath()
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
        L_0x0834:
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList checkDBState fail, delete db and try to recovery"
            android.util.Log.e(r8, r9)
            boolean r8 = r11.recoveryDB()
            if (r8 != 0) goto L_0x0894
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r3 = "refreshList recoveryDB fail"
            android.util.Log.e(r0, r3)
            java.lang.String r0 = r11.getDBPath()
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r0)
            java.lang.String r8 = "/"
            r5.append(r8)
            java.lang.String r8 = "Enroll_Data"
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            r3.<init>(r5)
            boolean r5 = r3.exists()
            if (r5 == 0) goto L_0x087a
            boolean r5 = r3.delete()
            if (r5 != 0) goto L_0x087a
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList delete file error"
            android.util.Log.e(r5, r8)
        L_0x087a:
            egis.optical.finger.host.FileDB r5 = r11.mFileDB
            r5.clear()
            java.lang.String r5 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "DB is cleared"
            android.util.Log.e(r5, r8)
            java.lang.String r5 = "Egistec_Company_Internal_Default_Data_1_id"
            java.lang.String r8 = "Egistec_Company_Internal_Default_Data_1_value"
            boolean r5 = r11.dataSet(r5, r8)
            if (r5 != 0) goto L_0x0892
            goto L_0x00dc
        L_0x0892:
            goto L_0x0963
        L_0x0894:
            android.content.Context r8 = mContext     // Catch:{ FileNotFoundException -> 0x0914, IOException -> 0x08db }
            java.lang.String r9 = "Enroll_Data"
            java.io.FileInputStream r8 = r8.openFileInput(r9)     // Catch:{ FileNotFoundException -> 0x0914, IOException -> 0x08db }
            r0 = r8
            if (r0 == 0) goto L_0x08af
            egis.optical.finger.host.FileDB r8 = r11.mFileDB     // Catch:{ FileNotFoundException -> 0x0914, IOException -> 0x08db }
            r8.Load(r0)     // Catch:{ FileNotFoundException -> 0x0914, IOException -> 0x08db }
            java.lang.String r8 = "FpCsaClientLib_FingerUtil"
            java.lang.String r9 = "refreshList mFileDB.Load(fis)"
            android.util.Log.d(r8, r9)     // Catch:{ FileNotFoundException -> 0x0914, IOException -> 0x08db }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0914, IOException -> 0x08db }
        L_0x08af:
            if (r0 == 0) goto L_0x08c2
            r0.close()     // Catch:{ IOException -> 0x08b5 }
            goto L_0x08c2
        L_0x08b5:
            r3 = move-exception
            r3.printStackTrace()
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r6 = "refreshList retry close DB fail"
            android.util.Log.e(r4, r6)
            goto L_0x0112
        L_0x08c2:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0963
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0963
            goto L_0x012a
        L_0x08d9:
            r3 = move-exception
            goto L_0x0945
        L_0x08db:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x08d9 }
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r6 = "refreshList retry to load DB fail"
            android.util.Log.e(r4, r6)     // Catch:{ all -> 0x08d9 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r5     // Catch:{ all -> 0x08d9 }
            if (r0 == 0) goto L_0x08fc
            r0.close()     // Catch:{ IOException -> 0x08ef }
            goto L_0x08fc
        L_0x08ef:
            r4 = move-exception
            r4.printStackTrace()
        L_0x08f3:
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry close DB fail"
            android.util.Log.e(r6, r8)
            goto L_0x0112
        L_0x08fc:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0913
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0913
            goto L_0x016a
        L_0x0913:
            return r1
        L_0x0914:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ all -> 0x08d9 }
            java.lang.String r6 = "FpCsaClientLib_FingerUtil"
            java.lang.String r8 = "refreshList retry to load DB fail"
            android.util.Log.e(r6, r8)     // Catch:{ all -> 0x08d9 }
            egis.optical.finger.host.FPNativeBase.lastErrCode = r3     // Catch:{ all -> 0x08d9 }
            if (r0 == 0) goto L_0x092d
            r0.close()     // Catch:{ IOException -> 0x0928 }
            goto L_0x092d
        L_0x0928:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x08f3
        L_0x092d:
            java.lang.String r3 = "Enroll_Data"
            boolean r3 = r11.checkFileState(r3)
            if (r3 != 0) goto L_0x0944
            java.lang.String r3 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r3, r5)
            boolean r3 = r11.reInitDB()
            if (r3 != 0) goto L_0x0944
            goto L_0x01a1
        L_0x0944:
            return r1
        L_0x0945:
            if (r0 == 0) goto L_0x094b
            r0.close()     // Catch:{ IOException -> 0x08b5 }
        L_0x094b:
            java.lang.String r4 = "Enroll_Data"
            boolean r4 = r11.checkFileState(r4)
            if (r4 != 0) goto L_0x0962
            java.lang.String r4 = "FpCsaClientLib_FingerUtil"
            java.lang.String r5 = "refresshList bak file does not work, init all data"
            android.util.Log.e(r4, r5)
            boolean r4 = r11.reInitDB()
            if (r4 != 0) goto L_0x0962
            goto L_0x012a
        L_0x0962:
            throw r3
        L_0x0963:
            boolean r0 = r11.saveFeatureToBakFile()
            if (r0 == 0) goto L_0x096b
            goto L_0x01ce
        L_0x096b:
            java.lang.String r0 = "FpCsaClientLib_FingerUtil"
            java.lang.String r1 = "refreshList saveFeatureToBakFile fail"
            android.util.Log.e(r0, r1)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: egis.optical.finger.host.FingerUtil.refreshList():boolean");
    }

    private boolean recoveryDB() {
        Log.d(TAG, "recoveryDB");
        StringBuilder sb = new StringBuilder();
        sb.append(getDBPath());
        sb.append("/");
        sb.append(FEATURE_FILE);
        File file = new File(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getDBPath());
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
            StringBuilder sb3 = new StringBuilder();
            sb3.append("rename file faile ");
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
        StringBuilder sb = new StringBuilder();
        sb.append(getDBPath());
        sb.append("/");
        sb.append(FEATURE_FILE_NEW);
        File file_new = new File(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(mContext.getFilesDir());
        sb2.append("/");
        sb2.append(FEATURE_FILE);
        File file = new File(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getDBPath());
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
        StringBuilder sb = new StringBuilder();
        sb.append(getDBPath());
        sb.append("/");
        sb.append(FEATURE_FILE_NEW);
        File file_new = new File(sb.toString());
        if (file_new.exists()) {
            Log.e(TAG, "replaceNewDB DB new file exists, replace current DB");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getDBPath());
            sb2.append("/");
            sb2.append(FEATURE_FILE);
            File file = new File(sb2.toString());
            if (file.exists() && !file.delete()) {
                String str = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("replaceNewDB delete file fail ");
                sb3.append(file.getPath());
                Log.e(str, sb3.toString());
            }
            if (!file_new.renameTo(file)) {
                String str2 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("replaceNewDB rename fail ");
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
        StringBuilder sb = new StringBuilder();
        sb.append("dataSet id=");
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
                StringBuilder sb = new StringBuilder();
                sb.append("getAllFeature() fid=");
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
        StringBuilder sb = new StringBuilder();
        sb.append("+++++ getMatchedID matchIdx = ");
        sb.append(matchIdx);
        sb.append(" +++++");
        Log.d(str, sb.toString());
        String data = this.mFileDB.readID(matchIdx).substring(1);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("data = ");
        sb2.append(data);
        Log.d(str2, sb2.toString());
        return data;
    }

    public String getEnrollListFromDB(String userID) {
        String fid;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getEnrollListFromDB=");
        sb.append(userID);
        Log.d(str, sb.toString());
        String enrollList = null;
        for (int i = 0; i < this.mFileDB.size(); i++) {
            String fid2 = this.mFileDB.readID(i);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("fid=");
            sb2.append(fid2);
            Log.d(str2, sb2.toString());
            if (!userID.equals(new String("*"))) {
                if (fid2.startsWith("*")) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("in startsWith() fid=");
                    sb3.append(fid2);
                    Log.d(str3, sb3.toString());
                    String dbUserId = fid2.substring(0, fid2.indexOf(";"));
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("dbUserId=");
                    sb4.append(dbUserId);
                    Log.d(str4, sb4.toString());
                    if (userID.equals(dbUserId)) {
                        fid = fid2.substring(fid2.length() - 2);
                    }
                }
            } else {
                String str5 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("userID=");
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
                StringBuilder sb6 = new StringBuilder();
                sb6.append(enrollList);
                sb6.append(";");
                sb6.append(fid);
                enrollList = sb6.toString();
            }
            Log.d(TAG, "-------------------------");
        }
        String str6 = TAG;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("enrollList=");
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
        StringBuilder sb = new StringBuilder();
        sb.append("s = ");
        sb.append(s);
        Log.d(str, sb.toString());
        return s;
    }

    public boolean deleteFromDB(String userID) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("+++++ deleteFromDB  userID = ");
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
