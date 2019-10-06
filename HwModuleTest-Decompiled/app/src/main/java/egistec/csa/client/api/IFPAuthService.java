package egistec.csa.client.api;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFPAuthService extends IInterface {

    public static abstract class Stub extends Binder implements IFPAuthService {
        private static final String DESCRIPTOR = "egistec.csa.client.api.IFPAuthService";
        static final int TRANSACTION_DataDelete = 22;
        static final int TRANSACTION_DataRead = 21;
        static final int TRANSACTION_DataSet = 20;
        static final int TRANSACTION_IsSensorWorking = 40;
        static final int TRANSACTION_abort = 15;
        static final int TRANSACTION_captureFrame = 26;
        static final int TRANSACTION_captureRawData = 25;
        static final int TRANSACTION_checkServiceException = 44;
        static final int TRANSACTION_connectDevice = 11;
        static final int TRANSACTION_deleteFeature = 2;
        static final int TRANSACTION_deletePwd = 5;
        static final int TRANSACTION_disconnectDevice = 12;
        static final int TRANSACTION_enroll = 1;
        static final int TRANSACTION_getDeviceType = 16;
        static final int TRANSACTION_getEnrollList = 3;
        static final int TRANSACTION_getEnrollProgress = 34;
        static final int TRANSACTION_getEnrollStatus = 38;
        static final int TRANSACTION_getLastErrCode = 29;
        static final int TRANSACTION_getMatchedImg = 42;
        static final int TRANSACTION_getMatchedImgInfo = 43;
        static final int TRANSACTION_getMatchedUserID = 23;
        static final int TRANSACTION_getOperationType = 41;
        static final int TRANSACTION_getRawData = 27;
        static final int TRANSACTION_getRawDataInfo = 28;
        static final int TRANSACTION_getSensorInfo = 36;
        static final int TRANSACTION_getThreadImg = 32;
        static final int TRANSACTION_getThreadImgInfo = 33;
        static final int TRANSACTION_getTinyMap = 7;
        static final int TRANSACTION_getTinyMapInfo = 8;
        static final int TRANSACTION_getUserIdList = 37;
        static final int TRANSACTION_getVerifyLearningScore = 30;
        static final int TRANSACTION_getVersion = 35;
        static final int TRANSACTION_identify = 13;
        static final int TRANSACTION_isEnrolled = 17;
        static final int TRANSACTION_isRequestVerify = 6;
        static final int TRANSACTION_isSimplePwd = 18;
        static final int TRANSACTION_learning = 31;
        static final int TRANSACTION_learningIdentify = 14;
        static final int TRANSACTION_registerCallback = 9;
        static final int TRANSACTION_sensorControl = 45;
        static final int TRANSACTION_setAccuracyLevel = 39;
        static final int TRANSACTION_setOnGetRawData = 24;
        static final int TRANSACTION_setPwd = 4;
        static final int TRANSACTION_unregisterCallback = 10;
        static final int TRANSACTION_verifyPwd = 19;

        private static class Proxy implements IFPAuthService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public boolean enroll(String userID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(userID);
                    boolean z = false;
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteFeature(String userID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(userID);
                    boolean z = false;
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getEnrollList(String userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPwd(String pwd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pwd);
                    boolean z = false;
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deletePwd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRequestVerify() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getTinyMap() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createByteArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getTinyMapInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createIntArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCallback(IFPAuthServiceCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCallback(IFPAuthServiceCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean connectDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disconnectDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean identify(String userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(userId);
                    boolean z = false;
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean learningIdentify(String fid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fid);
                    boolean z = false;
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean abort() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDeviceType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnrolled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSimplePwd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean verifyPwd(String pwd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pwd);
                    boolean z = false;
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean DataSet(String key, String value, String pwd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeString(pwd);
                    boolean z = false;
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String DataRead(String key, String pwd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(pwd);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean DataDelete(String key, String pwd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(pwd);
                    boolean z = false;
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMatchedUserID() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOnGetRawData(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean captureRawData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean captureFrame() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getRawData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createByteArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getRawDataInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createIntArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLastErrCode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVerifyLearningScore() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean learning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getThreadImg() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createByteArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getThreadImgInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createIntArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getEnrollProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSensorInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getUserIdList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createStringArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getEnrollStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createIntArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setAccuracyLevel(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean IsSensorWorking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getOperationType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getMatchedImg() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createByteArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getMatchedImgInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createIntArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void checkServiceException() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int sensorControl(int request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(request);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFPAuthService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFPAuthService)) {
                return new Proxy(obj);
            }
            return (IFPAuthService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result = enroll(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result2 = deleteFeature(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        String _result3 = getEnrollList(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result4 = setPwd(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result5 = deletePwd();
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result6 = isRequestVerify();
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _result7 = getTinyMap();
                        reply.writeNoException();
                        reply.writeByteArray(_result7);
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        int[] _result8 = getTinyMapInfo();
                        reply.writeNoException();
                        reply.writeIntArray(_result8);
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        registerCallback(egistec.csa.client.api.IFPAuthServiceCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        unregisterCallback(egistec.csa.client.api.IFPAuthServiceCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result9 = connectDevice();
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result10 = disconnectDevice();
                        reply.writeNoException();
                        reply.writeInt(_result10);
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result11 = identify(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result11);
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result12 = learningIdentify(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result12);
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result13 = abort();
                        reply.writeNoException();
                        reply.writeInt(_result13);
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        int _result14 = getDeviceType();
                        reply.writeNoException();
                        reply.writeInt(_result14);
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result15 = isEnrolled();
                        reply.writeNoException();
                        reply.writeInt(_result15);
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result16 = isSimplePwd();
                        reply.writeNoException();
                        reply.writeInt(_result16);
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result17 = verifyPwd(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result17);
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result18 = DataSet(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result18);
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        String _result19 = DataRead(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result19);
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result20 = DataDelete(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result20);
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        String _result21 = getMatchedUserID();
                        reply.writeNoException();
                        reply.writeString(_result21);
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        setOnGetRawData(data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result22 = captureRawData();
                        reply.writeNoException();
                        reply.writeInt(_result22);
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result23 = captureFrame();
                        reply.writeNoException();
                        reply.writeInt(_result23);
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _result24 = getRawData();
                        reply.writeNoException();
                        reply.writeByteArray(_result24);
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        int[] _result25 = getRawDataInfo();
                        reply.writeNoException();
                        reply.writeIntArray(_result25);
                        return true;
                    case 29:
                        data.enforceInterface(DESCRIPTOR);
                        int _result26 = getLastErrCode();
                        reply.writeNoException();
                        reply.writeInt(_result26);
                        return true;
                    case 30:
                        data.enforceInterface(DESCRIPTOR);
                        int _result27 = getVerifyLearningScore();
                        reply.writeNoException();
                        reply.writeInt(_result27);
                        return true;
                    case 31:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result28 = learning();
                        reply.writeNoException();
                        reply.writeInt(_result28);
                        return true;
                    case 32:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _result29 = getThreadImg();
                        reply.writeNoException();
                        reply.writeByteArray(_result29);
                        return true;
                    case 33:
                        data.enforceInterface(DESCRIPTOR);
                        int[] _result30 = getThreadImgInfo();
                        reply.writeNoException();
                        reply.writeIntArray(_result30);
                        return true;
                    case 34:
                        data.enforceInterface(DESCRIPTOR);
                        int _result31 = getEnrollProgress();
                        reply.writeNoException();
                        reply.writeInt(_result31);
                        return true;
                    case 35:
                        data.enforceInterface(DESCRIPTOR);
                        String _result32 = getVersion();
                        reply.writeNoException();
                        reply.writeString(_result32);
                        return true;
                    case 36:
                        data.enforceInterface(DESCRIPTOR);
                        String _result33 = getSensorInfo();
                        reply.writeNoException();
                        reply.writeString(_result33);
                        return true;
                    case 37:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _result34 = getUserIdList();
                        reply.writeNoException();
                        reply.writeStringArray(_result34);
                        return true;
                    case 38:
                        data.enforceInterface(DESCRIPTOR);
                        int[] _result35 = getEnrollStatus();
                        reply.writeNoException();
                        reply.writeIntArray(_result35);
                        return true;
                    case 39:
                        data.enforceInterface(DESCRIPTOR);
                        int _result36 = setAccuracyLevel(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result36);
                        return true;
                    case 40:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _result37 = IsSensorWorking();
                        reply.writeNoException();
                        reply.writeInt(_result37);
                        return true;
                    case 41:
                        data.enforceInterface(DESCRIPTOR);
                        int _result38 = getOperationType();
                        reply.writeNoException();
                        reply.writeInt(_result38);
                        return true;
                    case 42:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _result39 = getMatchedImg();
                        reply.writeNoException();
                        reply.writeByteArray(_result39);
                        return true;
                    case 43:
                        data.enforceInterface(DESCRIPTOR);
                        int[] _result40 = getMatchedImgInfo();
                        reply.writeNoException();
                        reply.writeIntArray(_result40);
                        return true;
                    case 44:
                        data.enforceInterface(DESCRIPTOR);
                        checkServiceException();
                        reply.writeNoException();
                        return true;
                    case 45:
                        data.enforceInterface(DESCRIPTOR);
                        int _result41 = sensorControl(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result41);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }
    }

    boolean DataDelete(String str, String str2) throws RemoteException;

    String DataRead(String str, String str2) throws RemoteException;

    boolean DataSet(String str, String str2, String str3) throws RemoteException;

    boolean IsSensorWorking() throws RemoteException;

    boolean abort() throws RemoteException;

    boolean captureFrame() throws RemoteException;

    boolean captureRawData() throws RemoteException;

    void checkServiceException() throws RemoteException;

    boolean connectDevice() throws RemoteException;

    boolean deleteFeature(String str) throws RemoteException;

    boolean deletePwd() throws RemoteException;

    boolean disconnectDevice() throws RemoteException;

    boolean enroll(String str) throws RemoteException;

    int getDeviceType() throws RemoteException;

    String getEnrollList(String str) throws RemoteException;

    int getEnrollProgress() throws RemoteException;

    int[] getEnrollStatus() throws RemoteException;

    int getLastErrCode() throws RemoteException;

    byte[] getMatchedImg() throws RemoteException;

    int[] getMatchedImgInfo() throws RemoteException;

    String getMatchedUserID() throws RemoteException;

    int getOperationType() throws RemoteException;

    byte[] getRawData() throws RemoteException;

    int[] getRawDataInfo() throws RemoteException;

    String getSensorInfo() throws RemoteException;

    byte[] getThreadImg() throws RemoteException;

    int[] getThreadImgInfo() throws RemoteException;

    byte[] getTinyMap() throws RemoteException;

    int[] getTinyMapInfo() throws RemoteException;

    String[] getUserIdList() throws RemoteException;

    int getVerifyLearningScore() throws RemoteException;

    String getVersion() throws RemoteException;

    boolean identify(String str) throws RemoteException;

    boolean isEnrolled() throws RemoteException;

    boolean isRequestVerify() throws RemoteException;

    boolean isSimplePwd() throws RemoteException;

    boolean learning() throws RemoteException;

    boolean learningIdentify(String str) throws RemoteException;

    void registerCallback(IFPAuthServiceCallback iFPAuthServiceCallback) throws RemoteException;

    int sensorControl(int i) throws RemoteException;

    int setAccuracyLevel(int i) throws RemoteException;

    void setOnGetRawData(boolean z) throws RemoteException;

    boolean setPwd(String str) throws RemoteException;

    void unregisterCallback(IFPAuthServiceCallback iFPAuthServiceCallback) throws RemoteException;

    boolean verifyPwd(String str) throws RemoteException;
}
