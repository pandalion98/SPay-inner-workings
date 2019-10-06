package egis.optical.finger.host;

import android.util.Log;
import com.sec.android.app.hwmoduletest.HwModuleTest;
import egis.optical.finger.host.CipherManager.CipherType;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class FileDB {
    private static final String TAG = "FpCsaClientLib_FileDB";

    /* renamed from: iv */
    private static final byte[] f43iv = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, HwModuleTest.ID_SLEEP, HwModuleTest.ID_SUB_KEY, HwModuleTest.ID_LED, HwModuleTest.ID_WACOM, HwModuleTest.ID_GRIP};
    private static final byte[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, HwModuleTest.ID_SLEEP, HwModuleTest.ID_SUB_KEY, HwModuleTest.ID_LED, HwModuleTest.ID_WACOM, HwModuleTest.ID_GRIP};

    /* renamed from: cm */
    CipherManager f44cm = new CipherManager(CipherType.AES);
    List<FingerData> mList = new ArrayList();

    public FileDB() {
        this.f44cm.init(key, f43iv);
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

    private Object bytetoObj(byte[] data) {
        if (data == null) {
            Log.e(TAG, "delete data == null");
            FPNativeBase.lastErrCode = 2017;
            return Boolean.valueOf(false);
        } else if (data.length <= 0) {
            Log.e(TAG, "delete data length invalid");
            FPNativeBase.lastErrCode = 2016;
            return Boolean.valueOf(false);
        } else {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            try {
                ObjectInput in = new ObjectInputStream(bis);
                Object o = in.readObject();
                try {
                    bis.close();
                } catch (IOException e) {
                }
                try {
                    in.close();
                } catch (IOException e2) {
                }
                return o;
            } catch (StreamCorruptedException e3) {
                e3.printStackTrace();
                try {
                    bis.close();
                } catch (IOException e4) {
                }
                if (0 != 0) {
                    null.close();
                }
                return null;
            } catch (IOException e5) {
                e5.printStackTrace();
                try {
                    bis.close();
                } catch (IOException e6) {
                }
                if (0 != 0) {
                    null.close();
                }
                return null;
            } catch (ClassNotFoundException e7) {
                e7.printStackTrace();
                try {
                    bis.close();
                } catch (IOException e8) {
                }
                if (0 != 0) {
                    try {
                        null.close();
                    } catch (IOException e9) {
                    }
                }
                return null;
            } catch (Throwable th) {
                try {
                    bis.close();
                } catch (IOException e10) {
                }
                if (0 != 0) {
                    try {
                        null.close();
                    } catch (IOException e11) {
                    }
                }
                throw th;
            }
        }
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
            StringBuilder sb = new StringBuilder();
            sb.append("mList.size = ");
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
            StringBuilder sb2 = new StringBuilder();
            sb2.append("save data.length = ");
            sb2.append(data.length);
            Log.d(str2, sb2.toString());
            byte[] outData = this.f44cm.encryption(data);
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
            StringBuilder sb3 = new StringBuilder();
            sb3.append("save outData.length = ");
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
            StringBuilder sb = new StringBuilder();
            sb.append("Load mFisSize = ");
            sb.append(mFisSize);
            Log.d(str, sb.toString());
            BufferedInputStream ois = null;
            if (mFisSize != 0) {
                try {
                    ois = new BufferedInputStream(fis);
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Load ois.available() = ");
                    sb2.append(ois.available());
                    Log.d(str2, sb2.toString());
                    byte[] data = new byte[ois.available()];
                    ois.read(data);
                    byte[] inData = this.f44cm.decryption(data);
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
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Load inData.length = ");
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
