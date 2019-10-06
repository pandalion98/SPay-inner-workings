package com.qualcomm.qti.biometrics.fingerprint;

import android.graphics.Bitmap;
import android.util.Log;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QFSManager {
    private static final String DEFAULT_JAR_PATH = "/vendor/app/QFSManager/";
    private static final String TAG = "StaticQFSManager";
    private static final String VERSION = "11152018";
    /* access modifiers changed from: private */
    public static QFSEventListener mQFSEventListener;
    private static Object mQFSEventListenerProxy;
    /* access modifiers changed from: private */
    public static QFSLivePreviewListener mQFSLivePreviewListener;
    private static Object mQFSLivePreviewListenerProxy;
    /* access modifiers changed from: private */
    public static QFSScanListener mQFSScanListener;
    private static Object mQFSScanListenerProxy;
    private static Object mQfsManagerObject;
    /* access modifiers changed from: private */
    public HashMap<String, Class<?>> map = new HashMap<>();

    private class QFSInvocationHandler implements InvocationHandler {
        private QFSInvocationHandler() {
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args != null) {
                try {
                    if (!method.getName().equals("onFingerprintEvent")) {
                        if (method.getName().equals("onLivePreview")) {
                            QFSManager.mQFSLivePreviewListener.onLivePreview(args[0]);
                        }
                    } else if (args[1] == null || (args[1] instanceof Integer)) {
                        if (QFSManager.mQFSEventListener != null) {
                            QFSManager.mQFSEventListener.onFingerprintEvent(args[0].intValue(), args[1]);
                        }
                        if (QFSManager.mQFSScanListener != null) {
                            QFSManager.mQFSScanListener.onFingerprintEvent(args[0].intValue(), args[1]);
                        }
                    } else {
                        Object customObject = args[1];
                        try {
                            ((Class) QFSManager.this.map.get(args[1].getClass().getName())).cast(customObject);
                            if (args[1].getClass().getName().contains("QFSCaptureInfo")) {
                                QFSCaptureInfo qFSCaptureInfo = new QFSCaptureInfo((Bitmap) customObject.getClass().getDeclaredMethod("getBitmap", null).invoke(customObject, null), ((Integer) customObject.getClass().getDeclaredMethod("getHeight", null).invoke(customObject, null)).intValue(), ((Integer) customObject.getClass().getDeclaredMethod("getQuality", null).invoke(customObject, null)).intValue(), (Bitmap) customObject.getClass().getDeclaredMethod("getRawBitmap", null).invoke(customObject, null), ((Integer) customObject.getClass().getDeclaredMethod("getWidth", null).invoke(customObject, null)).intValue());
                                QFSCaptureInfo newCaptureInfo = qFSCaptureInfo;
                                if (QFSManager.mQFSEventListener != null) {
                                    QFSManager.mQFSEventListener.onFingerprintEvent(args[0].intValue(), newCaptureInfo);
                                }
                            } else if (args[1].getClass().getName().contains("QFSEnrollStatus")) {
                                QFSEnrollStatus newEnrollStatus = new QFSEnrollStatus(((Integer) customObject.getClass().getDeclaredMethod("getBadTrial", null).invoke(customObject, null)).intValue(), ((Integer) customObject.getClass().getDeclaredMethod("getProgress", null).invoke(customObject, null)).intValue(), ((Integer) customObject.getClass().getDeclaredMethod("getSuccessTrial", null).invoke(customObject, null)).intValue(), ((Integer) customObject.getClass().getDeclaredMethod("getTotalTrial", null).invoke(customObject, null)).intValue());
                                if (QFSManager.mQFSEventListener != null) {
                                    QFSManager.mQFSEventListener.onFingerprintEvent(args[0].intValue(), newEnrollStatus);
                                }
                            } else if (args[1].getClass().getName().contains("QFSIdentifyResult")) {
                                QFSIdentifyResult identifyResult = new QFSIdentifyResult(((Integer) customObject.getClass().getDeclaredMethod("getIndex", null).invoke(customObject, null)).intValue(), (int[]) customObject.getClass().getDeclaredMethod("getIndexes", null).invoke(customObject, null), ((Integer) customObject.getClass().getDeclaredMethod("getResult", null).invoke(customObject, null)).intValue(), (String) customObject.getClass().getDeclaredMethod("getUserId", null).invoke(customObject, null));
                                if (QFSManager.mQFSEventListener != null) {
                                    QFSManager.mQFSEventListener.onFingerprintEvent(args[0].intValue(), identifyResult);
                                }
                            } else {
                                Log.e(QFSManager.TAG, "Unknown class on onFingerprintEvent");
                            }
                        } catch (Throwable th) {
                            e = th;
                            String str = QFSManager.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onFingerprintEvent invoke exception ");
                            sb.append(e.getMessage());
                            Log.e(str, sb.toString());
                            e.printStackTrace();
                            return null;
                        }
                    }
                } catch (Throwable th2) {
                    e = th2;
                    String str2 = QFSManager.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onFingerprintEvent invoke exception ");
                    sb2.append(e.getMessage());
                    Log.e(str2, sb2.toString());
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }
    }

    public QFSManager() {
        Log.i(TAG, "Static QFSManager version: 11152018");
        createInstances(DEFAULT_JAR_PATH);
    }

    public void createInstances(String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append("QFSManager.jar");
        File file = new File(sb.toString());
        File tmpDir = new File(path);
        tmpDir.mkdir();
        try {
            Iterator it = ((ArrayList) new DexClassLoader(file.getAbsolutePath(), tmpDir.getAbsolutePath(), path, ClassLoader.getSystemClassLoader()).loadClass("com.qualcomm.qti.biometrics.fingerprint.Registry").getDeclaredField("_classes").get(null)).iterator();
            while (it.hasNext()) {
                Class<?> cls = (Class) it.next();
                String str = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Class loaded ");
                sb2.append(cls.getName());
                Log.v(str, sb2.toString());
                if (cls.getName().equals("com.qualcomm.qti.biometrics.fingerprint.QFSManager")) {
                    mQfsManagerObject = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                } else if (cls.getName().equals("com.qualcomm.qti.biometrics.fingerprint.QFSEventListener")) {
                    mQFSEventListenerProxy = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new QFSInvocationHandler());
                } else if (cls.getName().equals("com.qualcomm.qti.biometrics.fingerprint.QFSScanListener")) {
                    mQFSScanListenerProxy = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new QFSInvocationHandler());
                } else if (cls.getName().equals("com.qualcomm.qti.biometrics.fingerprint.QFSLivePreviewListener")) {
                    mQFSLivePreviewListenerProxy = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new QFSInvocationHandler());
                } else {
                    String str2 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Putting [");
                    sb3.append(cls.getName());
                    sb3.append("]");
                    Log.v(str2, sb3.toString());
                    this.map.put(cls.getName(), cls);
                }
            }
        } catch (ClassNotFoundException e) {
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("ClassNotFoundException ");
            sb4.append(e.getMessage());
            Log.e(str3, sb4.toString());
            e.printStackTrace();
        } catch (InstantiationException e2) {
            String str4 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("InstantiationException ");
            sb5.append(e2.getMessage());
            Log.e(str4, sb5.toString());
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            String str5 = TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("InvocationTargetException ");
            sb6.append(e3.getMessage());
            Log.e(str5, sb6.toString());
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            String str6 = TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("NoSuchMethodException ");
            sb7.append(e4.getMessage());
            Log.e(str6, sb7.toString());
            e4.printStackTrace();
        } catch (IllegalAccessException e5) {
            String str7 = TAG;
            StringBuilder sb8 = new StringBuilder();
            sb8.append("IllegalAccessException ");
            sb8.append(e5.getMessage());
            Log.e(str7, sb8.toString());
            e5.printStackTrace();
        } catch (NoSuchFieldException e6) {
            String str8 = TAG;
            StringBuilder sb9 = new StringBuilder();
            sb9.append("NoSuchFieldException ");
            sb9.append(e6.getMessage());
            Log.e(str8, sb9.toString());
            e6.printStackTrace();
        }
    }

    private Object executeAPI(String methodName, Object... params) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("executeAPI [");
        sb.append(methodName);
        sb.append("]");
        Log.i(str, sb.toString());
        if (mQfsManagerObject == null) {
            Log.e(TAG, "QfsManagerObject NULL");
            return null;
        }
        Class[] clsArr = null;
        try {
            Method[] qfsManagerMethods = mQfsManagerObject.getClass().getDeclaredMethods();
            for (int i = 0; i < qfsManagerMethods.length; i++) {
                if (qfsManagerMethods[i].getName().equals(methodName) && params != null && qfsManagerMethods[i].getParameterCount() == params.length) {
                    clsArr = qfsManagerMethods[i].getParameterTypes();
                }
            }
            Method method = mQfsManagerObject.getClass().getDeclaredMethod(methodName, clsArr);
            if (method != null) {
                return method.invoke(mQfsManagerObject, params);
            }
            Log.e(TAG, "Failed to get method");
            return null;
        } catch (NoSuchMethodException e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("NoSuchMethodException ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("IllegalAccessException ");
            sb3.append(e2.getMessage());
            Log.e(str3, sb3.toString());
            e2.printStackTrace();
            return null;
        } catch (InvocationTargetException e3) {
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("InvocationTargetException ");
            sb4.append(e3.getMessage());
            Log.e(str4, sb4.toString());
            e3.printStackTrace();
            return null;
        }
    }

    public void startLivePreview(QFSLivePreviewListener listener) {
        mQFSLivePreviewListener = listener;
        executeAPI("startLivePreview", mQFSLivePreviewListenerProxy);
    }

    public void stopLivePreview() {
        executeAPI("stopLivePreview", null);
    }

    public void setEnrollmentTimeout(int timeout) {
        executeAPI("setEnrollmentTimeout", Integer.valueOf(timeout));
    }

    public void setTrackingFilePath(String path) {
        executeAPI("setTrackingFilePath", path);
    }

    public void runQFSTest(int testId) {
        executeAPI("runQFSTest", Integer.valueOf(testId));
    }

    public int setActiveStorage(String userId, String path) {
        return ((Integer) executeAPI("setActiveStorage", userId, path)).intValue();
    }

    public int authenticate(String userId) {
        return ((Integer) executeAPI("authenticate", userId)).intValue();
    }

    public int cancel() {
        return ((Integer) executeAPI("cancel", null)).intValue();
    }

    public int dispatch(int eventId, Object object) {
        return ((Integer) executeAPI("dispatch", Integer.valueOf(eventId), object)).intValue();
    }

    public int enroll(String userId, int index) {
        return ((Integer) executeAPI("enroll", userId, Integer.valueOf(index))).intValue();
    }

    public byte[] getFingerprintId(String userId, int index) {
        return (byte[]) executeAPI("getFingerprintId", userId, Integer.valueOf(index));
    }

    public int[] getFingerprintIndexList(String userId) {
        return (int[]) executeAPI("getFingerprintIndexList", userId);
    }

    public String getSensorInfo() {
        return (String) executeAPI("getSensorInfo", null);
    }

    public void getSensorInfo(QFSScanListener scanListener) {
        mQFSScanListener = scanListener;
        String str = "getSensorInfo";
        Object[] objArr = new Object[1];
        objArr[0] = scanListener == null ? null : mQFSScanListenerProxy;
        executeAPI(str, objArr);
    }

    public int getSensorStatus() {
        return ((Integer) executeAPI("getSensorStatus", null)).intValue();
    }

    public String[] getUserIdList() {
        return (String[]) executeAPI("getUserIdList", null);
    }

    public boolean delBGE(String path) {
        return ((Boolean) executeAPI("delBGE", path)).booleanValue();
    }

    public BGEInfo getBGEInfo() {
        Object customObject = executeAPI("getBGEInfo", null);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Map get:[");
        sb.append(((Class) this.map.get(customObject.getClass().getName())).getName());
        sb.append("]");
        Log.d(str, sb.toString());
        ((Class) this.map.get(customObject.getClass().getName())).cast(customObject);
        try {
            List<Object> binsObjs = (List) customObject.getClass().getDeclaredMethod("getCurrentBins", null).invoke(customObject, null);
            ArrayList<BinData> binDataList = new ArrayList<>();
            for (Object binObj : binsObjs) {
                ((Class) this.map.get(binObj.getClass().getName())).cast(binObj);
                binDataList.add(new BinData(((Integer) binObj.getClass().getDeclaredMethod("getCalibTime", null).invoke(binObj, null)).intValue(), ((Integer) binObj.getClass().getDeclaredMethod("getBinTemperature", null).invoke(binObj, null)).intValue(), ((Integer) binObj.getClass().getDeclaredMethod("getBinIndex", null).invoke(binObj, null)).intValue()));
            }
            return new BGEInfo(binDataList, ((Integer) customObject.getClass().getDeclaredMethod("getCurrentTemp", null).invoke(customObject, null)).intValue(), ((Integer) customObject.getClass().getDeclaredMethod("getBinIndexToReplace", null).invoke(customObject, null)).intValue());
        } catch (NoSuchMethodException e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("BGEInfo exception ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("BGEInfo exception ");
            sb3.append(e2.getMessage());
            Log.e(str3, sb3.toString());
            e2.printStackTrace();
            return null;
        } catch (InvocationTargetException e3) {
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("BGEInfo exception ");
            sb4.append(e3.getMessage());
            Log.e(str4, sb4.toString());
            e3.printStackTrace();
            return null;
        }
    }

    public void runBGECalibration(String path) {
        executeAPI("runBGECalibration", path);
    }

    public boolean checkBGE() {
        return ((Boolean) executeAPI("checkBGE", null)).booleanValue();
    }

    public String getVersion() {
        return (String) executeAPI("getVersion", null);
    }

    public String getFpVersion() {
        return (String) executeAPI("getFpVersion", null);
    }

    public void startNormalScan(QFSScanListener scanListener) {
        mQFSScanListener = scanListener;
        String str = "startNormalScan";
        Object[] objArr = new Object[1];
        objArr[0] = scanListener == null ? null : mQFSScanListenerProxy;
        executeAPI(str, objArr);
    }

    public void cancelTest() {
        executeAPI("cancelTest", null);
    }

    public void endTest() {
        executeAPI("endTest", null);
    }

    public void startSNRPrepare(QFSScanListener scanListener) {
        mQFSScanListener = scanListener;
        String str = "startSNRPrepare";
        Object[] objArr = new Object[1];
        objArr[0] = scanListener == null ? null : mQFSScanListenerProxy;
        executeAPI(str, objArr);
    }

    public void startSNR() {
        executeAPI("startSNR", null);
    }

    public void remove(String userId, int index) {
        executeAPI("remove", userId, Integer.valueOf(index));
    }

    public int request(int code, Object data) {
        return ((Integer) executeAPI("request", Integer.valueOf(code), data)).intValue();
    }

    public void setEventListener(QFSEventListener listener) {
        mQFSEventListener = listener;
        String str = "setEventListener";
        Object[] objArr = new Object[1];
        objArr[0] = listener == null ? null : mQFSEventListenerProxy;
        executeAPI(str, objArr);
    }

    public void setProtectionFilmChoice(boolean bUserChoice) {
        executeAPI("setProtectionFilmChoice", Boolean.valueOf(bUserChoice));
    }
}
