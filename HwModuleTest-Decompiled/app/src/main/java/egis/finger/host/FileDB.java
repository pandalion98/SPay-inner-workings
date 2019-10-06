package egis.finger.host;

import android.util.Log;
import com.sec.android.app.hwmoduletest.HwModuleTest;
import egis.finger.host.CipherManager.CipherType;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileDB {
    private static final String TAG = "FpCsaClientLib_FileDB";

    /* renamed from: iv */
    private static final byte[] f38iv;
    private static final byte[] key;

    /* renamed from: cm */
    CipherManager f39cm = new CipherManager(CipherType.AES);
    List<FingerData> mList = new ArrayList();

    static {
        byte[] bArr = new byte[16];
        bArr[1] = 1;
        bArr[2] = 2;
        bArr[3] = 3;
        bArr[4] = 4;
        bArr[5] = 5;
        bArr[6] = 6;
        bArr[7] = 7;
        bArr[8] = 8;
        bArr[9] = 9;
        bArr[10] = 10;
        bArr[11] = HwModuleTest.ID_SLEEP;
        bArr[12] = HwModuleTest.ID_SUB_KEY;
        bArr[13] = HwModuleTest.ID_LED;
        bArr[14] = HwModuleTest.ID_WACOM;
        bArr[15] = HwModuleTest.ID_GRIP;
        key = bArr;
        byte[] bArr2 = new byte[16];
        bArr2[1] = 1;
        bArr2[2] = 2;
        bArr2[3] = 3;
        bArr2[4] = 4;
        bArr2[5] = 5;
        bArr2[6] = 6;
        bArr2[7] = 7;
        bArr2[8] = 8;
        bArr2[9] = 9;
        bArr2[10] = 10;
        bArr2[11] = HwModuleTest.ID_SLEEP;
        bArr2[12] = HwModuleTest.ID_SUB_KEY;
        bArr2[13] = HwModuleTest.ID_LED;
        bArr2[14] = HwModuleTest.ID_WACOM;
        bArr2[15] = HwModuleTest.ID_GRIP;
        f38iv = bArr2;
    }

    public FileDB() {
        this.f39cm.init(key, f38iv);
    }

    public boolean add(String id, String feature) {
        if (id == null) {
            Log.e(TAG, "id == null");
            FPNativeBase.lastErrCode = 2013;
            return false;
        } else if (feature == null) {
            Log.e(TAG, "feature == null");
            FPNativeBase.lastErrCode = 2011;
            return false;
        } else if (id.length() <= 0) {
            Log.e(TAG, "id.length <= 0");
            FPNativeBase.lastErrCode = 2012;
            return false;
        } else if (feature.length() > 0) {
            return add(id, feature.getBytes());
        } else {
            Log.e(TAG, "feature.length <= 0");
            FPNativeBase.lastErrCode = 2010;
            return false;
        }
    }

    public boolean add(String id, byte[] feature) {
        if (feature == null) {
            Log.e(TAG, "feature == null");
            FPNativeBase.lastErrCode = 2011;
            return false;
        } else if (feature.length <= 0) {
            Log.e(TAG, "feature.length <= 0");
            FPNativeBase.lastErrCode = 2010;
            return false;
        } else {
            FingerData mFD = new FingerData(id, feature);
            int indexOf = this.mList.indexOf(mFD);
            int mRepIndex = indexOf;
            if (indexOf == -1) {
                this.mList.add(mFD);
            } else {
                ((FingerData) this.mList.get(mRepIndex)).setfeature(feature);
            }
            return true;
        }
    }

    public boolean delete(String id) {
        if (id == null) {
            Log.e(TAG, "delete id == null");
            FPNativeBase.lastErrCode = 2013;
            return false;
        } else if (id.length() <= 0) {
            Log.e(TAG, "delete id length invalid");
            FPNativeBase.lastErrCode = 2012;
            return false;
        } else if (id.equals(new String("*"))) {
            this.mList.clear();
            return true;
        } else {
            int indexOf = this.mList.indexOf(new FingerData(id, null));
            int mDelIndex = indexOf;
            if (indexOf == -1) {
                FPNativeBase.lastErrCode = 2014;
                return false;
            }
            this.mList.remove(mDelIndex);
            return true;
        }
    }

    public boolean update(String id, byte[] feature) {
        if (id == null) {
            Log.e(TAG, "id == null");
            FPNativeBase.lastErrCode = 2013;
            return false;
        } else if (feature == null) {
            Log.e(TAG, "feature == null");
            FPNativeBase.lastErrCode = 2011;
            return false;
        } else if (id.length() <= 0) {
            Log.e(TAG, "id.length <= 0");
            FPNativeBase.lastErrCode = 2012;
            return false;
        } else if (feature.length <= 0) {
            Log.e(TAG, "feature.length <= 0");
            FPNativeBase.lastErrCode = 2010;
            return false;
        } else {
            int indexOf = this.mList.indexOf(new FingerData(id, null));
            int mUpdateIndex = indexOf;
            if (indexOf == -1) {
                FPNativeBase.lastErrCode = 2014;
                return false;
            }
            ((FingerData) this.mList.get(mUpdateIndex)).setfeature(feature);
            return true;
        }
    }

    private byte[] objtoByte(Object obj) {
        if (obj == null) {
            Log.e(TAG, "objtoByte obj == null");
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            Log.e(TAG, "+++ before out.writeObject(obj) +++");
            out.writeObject(obj);
            Log.e(TAG, "+++ after out.writeObject(obj) +++");
            Log.e(TAG, "objtoByte obj == null");
            byte[] bytes = bos.toByteArray();
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                bos.close();
            } catch (IOException e2) {
            }
            return bytes;
        } catch (IOException e3) {
            e3.printStackTrace();
            if (0 != 0) {
                try {
                    null.close();
                } catch (IOException e4) {
                }
            }
            try {
                bos.close();
            } catch (IOException e5) {
            }
            return null;
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    null.close();
                } catch (IOException e6) {
                }
            }
            try {
                bos.close();
            } catch (IOException e7) {
            }
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0050, code lost:
        if (0 == 0) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        null.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0061, code lost:
        if (0 == 0) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x006d, code lost:
        if (0 == 0) goto L_0x0070;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Object bytetoObj(byte[] r5) {
        /*
            r4 = this;
            r0 = 0
            if (r5 != 0) goto L_0x0013
            java.lang.String r1 = "FpCsaClientLib_FileDB"
            java.lang.String r2 = "delete data == null"
            android.util.Log.e(r1, r2)
            r1 = 2017(0x7e1, float:2.826E-42)
            egis.finger.host.FPNativeBase.lastErrCode = r1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L_0x0013:
            int r1 = r5.length
            if (r1 > 0) goto L_0x0026
            java.lang.String r1 = "FpCsaClientLib_FileDB"
            java.lang.String r2 = "delete data length invalid"
            android.util.Log.e(r1, r2)
            r1 = 2016(0x7e0, float:2.825E-42)
            egis.finger.host.FPNativeBase.lastErrCode = r1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L_0x0026:
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream
            r0.<init>(r5)
            r1 = 0
            r2 = r1
            java.io.ObjectInputStream r3 = new java.io.ObjectInputStream     // Catch:{ StreamCorruptedException -> 0x0064, IOException -> 0x0058, ClassNotFoundException -> 0x0047 }
            r3.<init>(r0)     // Catch:{ StreamCorruptedException -> 0x0064, IOException -> 0x0058, ClassNotFoundException -> 0x0047 }
            r2 = r3
            java.lang.Object r3 = r2.readObject()     // Catch:{ StreamCorruptedException -> 0x0064, IOException -> 0x0058, ClassNotFoundException -> 0x0047 }
            r1 = r3
            r0.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x003e
        L_0x003d:
            r3 = move-exception
        L_0x003e:
            r2.close()     // Catch:{ IOException -> 0x0043 }
            goto L_0x0044
        L_0x0043:
            r3 = move-exception
        L_0x0044:
            return r1
        L_0x0045:
            r1 = move-exception
            goto L_0x0071
        L_0x0047:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0045 }
            r0.close()     // Catch:{ IOException -> 0x004f }
            goto L_0x0050
        L_0x004f:
            r3 = move-exception
        L_0x0050:
            if (r2 == 0) goto L_0x0070
        L_0x0052:
            r2.close()     // Catch:{ IOException -> 0x0056 }
            goto L_0x0070
        L_0x0056:
            r3 = move-exception
            goto L_0x0070
        L_0x0058:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0045 }
            r0.close()     // Catch:{ IOException -> 0x0060 }
            goto L_0x0061
        L_0x0060:
            r3 = move-exception
        L_0x0061:
            if (r2 == 0) goto L_0x0070
            goto L_0x0052
        L_0x0064:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0045 }
            r0.close()     // Catch:{ IOException -> 0x006c }
            goto L_0x006d
        L_0x006c:
            r3 = move-exception
        L_0x006d:
            if (r2 == 0) goto L_0x0070
            goto L_0x0052
        L_0x0070:
            return r1
        L_0x0071:
            r0.close()     // Catch:{ IOException -> 0x0076 }
            goto L_0x0077
        L_0x0076:
            r3 = move-exception
        L_0x0077:
            if (r2 == 0) goto L_0x007e
            r2.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x007e
        L_0x007d:
            r3 = move-exception
        L_0x007e:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: egis.finger.host.FileDB.bytetoObj(byte[]):java.lang.Object");
    }

    public boolean save(FileOutputStream fos) {
        Log.d(TAG, "save start");
        if (fos == null) {
            Log.e(TAG, "save file stream == null");
            FPNativeBase.lastErrCode = 2021;
            return false;
        }
        BufferedOutputStream oos = null;
        try {
            oos = new BufferedOutputStream(fos);
            String str = TAG;
            StringBuilder sb = new StringBuilder("mList.size = ");
            sb.append(this.mList.size());
            Log.d(str, sb.toString());
            byte[] data = objtoByte(this.mList);
            if (data == null) {
                Log.e(TAG, "save data == null");
                FPNativeBase.lastErrCode = 2017;
                try {
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("save data.length = ");
            sb2.append(data.length);
            Log.d(str2, sb2.toString());
            byte[] outData = this.f39cm.encryption(data);
            if (outData == null) {
                Log.e(TAG, "save outData ==  null");
                FPNativeBase.lastErrCode = 2019;
                try {
                    oos.close();
                    fos.close();
                } catch (IOException e12) {
                    e12.printStackTrace();
                }
                return false;
            }
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder("save outData.length = ");
            sb3.append(outData.length);
            Log.d(str3, sb3.toString());
            oos.write(outData);
            oos.flush();
            fos.getFD().sync();
            oos.close();
            fos.close();
            Log.d(TAG, "save success");
            try {
                oos.close();
                fos.close();
            } catch (IOException e13) {
                e13.printStackTrace();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
            } catch (IOException e14) {
                e14.printStackTrace();
            }
            Log.d(TAG, "save fail");
            return false;
        } finally {
            try {
                oos.close();
                fos.close();
            } catch (IOException e15) {
                e15.printStackTrace();
            }
        }
    }

    public boolean Load(FileInputStream fis) {
        Log.d(TAG, "Load start");
        if (fis == null) {
            Log.e(TAG, "Load file stream == null");
            FPNativeBase.lastErrCode = 2022;
            return false;
        }
        try {
            int mFisSize = fis.available();
            String str = TAG;
            StringBuilder sb = new StringBuilder("Load mFisSize = ");
            sb.append(mFisSize);
            Log.d(str, sb.toString());
            BufferedInputStream ois = null;
            if (mFisSize != 0) {
                try {
                    ois = new BufferedInputStream(fis);
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder("Load ois.available() = ");
                    sb2.append(ois.available());
                    Log.d(str2, sb2.toString());
                    byte[] data = new byte[ois.available()];
                    ois.read(data);
                    byte[] inData = this.f39cm.decryption(data);
                    if (inData == null) {
                        Log.e(TAG, "Load inData == null");
                        FPNativeBase.lastErrCode = 2020;
                        try {
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    }
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder("Load inData.length = ");
                    sb3.append(inData.length);
                    Log.d(str3, sb3.toString());
                    this.mList = (ArrayList) bytetoObj(inData);
                    ois.close();
                    fis.close();
                    Log.d(TAG, "Load succuess");
                    try {
                        ois.close();
                        fis.close();
                    } catch (IOException e12) {
                        e12.printStackTrace();
                    }
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                    } catch (IOException e13) {
                        e13.printStackTrace();
                    }
                } finally {
                    try {
                        ois.close();
                        fis.close();
                    } catch (IOException e14) {
                        e14.printStackTrace();
                    }
                }
            }
            Log.d(TAG, "Load fail");
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            FPNativeBase.lastErrCode = 2015;
            return false;
        }
    }

    public int size() {
        Log.d(TAG, "mFileDB.mList.size()");
        return this.mList.size();
    }

    public byte[] readFeature(int index) {
        return ((FingerData) this.mList.get(index)).mFeature;
    }

    public byte[] readFeature(String fid) {
        for (FingerData data : this.mList) {
            if (data.mID.equals(fid)) {
                return data.mFeature;
            }
        }
        return null;
    }

    public String readID(int index) {
        return ((FingerData) this.mList.get(index)).mID;
    }

    public int readID(String fid) {
        int idx = -1;
        for (FingerData f : this.mList) {
            idx++;
            if (f.getid().equals(fid)) {
                return idx;
            }
        }
        return idx;
    }

    public void clear() {
        this.mList.clear();
    }
}
