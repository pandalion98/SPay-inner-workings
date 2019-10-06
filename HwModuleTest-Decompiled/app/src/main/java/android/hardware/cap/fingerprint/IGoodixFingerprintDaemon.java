package android.hardware.cap.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGoodixFingerprintDaemon extends IInterface {

    public static abstract class Stub extends Binder implements IGoodixFingerprintDaemon {
        private static final String DESCRIPTOR = "android.hardware.cap.fingerprint.IGoodixFingerprintDaemon";
        static final int TRANSACTION_authenticateFido = 18;
        static final int TRANSACTION_cameraCapture = 5;
        static final int TRANSACTION_closeHal = 26;
        static final int TRANSACTION_dumpCmd = 17;
        static final int TRANSACTION_enableBioAssayFeature = 16;
        static final int TRANSACTION_enableFfFeature = 7;
        static final int TRANSACTION_enableFingerprintModule = 4;
        static final int TRANSACTION_enumerate = 14;
        static final int TRANSACTION_getIdList = 21;
        static final int TRANSACTION_invokeFidoCommand = 22;
        static final int TRANSACTION_isIdValid = 20;
        static final int TRANSACTION_navigate = 2;
        static final int TRANSACTION_openHal = 25;
        static final int TRANSACTION_pauseEnroll = 23;
        static final int TRANSACTION_reset_lockout = 15;
        static final int TRANSACTION_resumeEnroll = 24;
        static final int TRANSACTION_screenOff = 11;
        static final int TRANSACTION_screenOn = 10;
        static final int TRANSACTION_setSafeClass = 1;
        static final int TRANSACTION_startGetVersion = 32;
        static final int TRANSACTION_startHbd = 12;
        static final int TRANSACTION_startOtpCheckTest = 30;
        static final int TRANSACTION_startSensorInfoTest = 31;
        static final int TRANSACTION_startTouchPrepareTest = 28;
        static final int TRANSACTION_startTouchTest = 29;
        static final int TRANSACTION_startUntouchTest = 27;
        static final int TRANSACTION_stopAuthenticateFido = 19;
        static final int TRANSACTION_stopCameraCapture = 6;
        static final int TRANSACTION_stopHbd = 13;
        static final int TRANSACTION_stopNavigation = 3;
        static final int TRANSACTION_testCmd = 9;
        static final int TRANSACTION_testInit = 8;

        private static class Proxy implements IGoodixFingerprintDaemon {
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

            public int setSafeClass(int safeClass) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(safeClass);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int navigate(int navMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(navMode);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stopNavigation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enableFingerprintModule(byte enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(enable);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int cameraCapture() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stopCameraCapture() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enableFfFeature(byte enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(enable);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void testInit(IGoodixFingerprintDaemonCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int testCmd(int cmdId, byte[] param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmdId);
                    _data.writeByteArray(param);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int screenOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int screenOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startHbd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stopHbd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enumerate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int reset_lockout(byte[] token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(token);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enableBioAssayFeature(byte enableFlag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(enableFlag);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dumpCmd(int cmdId, byte[] param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmdId);
                    _data.writeByteArray(param);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int authenticateFido(int groupId, byte[] aaid, byte[] finalChallenge) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeByteArray(aaid);
                    _data.writeByteArray(finalChallenge);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stopAuthenticateFido() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isIdValid(int groupId, int fingerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeInt(fingerId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getIdList(int groupId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(groupId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createIntArray();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int invokeFidoCommand(byte[] inBuf, byte[] outBuf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(inBuf);
                    if (outBuf == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outBuf.length);
                    }
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(outBuf);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int pauseEnroll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int resumeEnroll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int openHal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int closeHal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startUntouchTest() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startTouchPrepareTest() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startTouchTest() throws RemoteException {
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

            public int startOtpCheckTest() throws RemoteException {
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

            public int startSensorInfoTest() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] startGetVersion() throws RemoteException {
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGoodixFingerprintDaemon asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGoodixFingerprintDaemon)) {
                return new Proxy(obj);
            }
            return (IGoodixFingerprintDaemon) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            byte[] _arg1;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        int _result = setSafeClass(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        int _result2 = navigate(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        int _result3 = stopNavigation();
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        int _result4 = enableFingerprintModule(data.readByte());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        int _result5 = cameraCapture();
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        int _result6 = stopCameraCapture();
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        int _result7 = enableFfFeature(data.readByte());
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        testInit(android.hardware.cap.fingerprint.IGoodixFingerprintDaemonCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        int _result8 = testCmd(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        int _result9 = screenOn();
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        int _result10 = screenOff();
                        reply.writeNoException();
                        reply.writeInt(_result10);
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        int _result11 = startHbd();
                        reply.writeNoException();
                        reply.writeInt(_result11);
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        int _result12 = stopHbd();
                        reply.writeNoException();
                        reply.writeInt(_result12);
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        int _result13 = enumerate();
                        reply.writeNoException();
                        reply.writeInt(_result13);
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        int _result14 = reset_lockout(data.createByteArray());
                        reply.writeNoException();
                        reply.writeInt(_result14);
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        int _result15 = enableBioAssayFeature(data.readByte());
                        reply.writeNoException();
                        reply.writeInt(_result15);
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        int _result16 = dumpCmd(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        reply.writeInt(_result16);
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        int _result17 = authenticateFido(data.readInt(), data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        reply.writeInt(_result17);
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        int _result18 = stopAuthenticateFido();
                        reply.writeNoException();
                        reply.writeInt(_result18);
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        int _result19 = isIdValid(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result19);
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        int[] _result20 = getIdList(data.readInt());
                        reply.writeNoException();
                        reply.writeIntArray(_result20);
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _arg0 = data.createByteArray();
                        int _arg1_length = data.readInt();
                        if (_arg1_length < 0) {
                            _arg1 = null;
                        } else {
                            _arg1 = new byte[_arg1_length];
                        }
                        int _result21 = invokeFidoCommand(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result21);
                        reply.writeByteArray(_arg1);
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        int _result22 = pauseEnroll();
                        reply.writeNoException();
                        reply.writeInt(_result22);
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        int _result23 = resumeEnroll();
                        reply.writeNoException();
                        reply.writeInt(_result23);
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        int _result24 = openHal();
                        reply.writeNoException();
                        reply.writeInt(_result24);
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        int _result25 = closeHal();
                        reply.writeNoException();
                        reply.writeInt(_result25);
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        int _result26 = startUntouchTest();
                        reply.writeNoException();
                        reply.writeInt(_result26);
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        int _result27 = startTouchPrepareTest();
                        reply.writeNoException();
                        reply.writeInt(_result27);
                        return true;
                    case 29:
                        data.enforceInterface(DESCRIPTOR);
                        int _result28 = startTouchTest();
                        reply.writeNoException();
                        reply.writeInt(_result28);
                        return true;
                    case 30:
                        data.enforceInterface(DESCRIPTOR);
                        int _result29 = startOtpCheckTest();
                        reply.writeNoException();
                        reply.writeInt(_result29);
                        return true;
                    case 31:
                        data.enforceInterface(DESCRIPTOR);
                        int _result30 = startSensorInfoTest();
                        reply.writeNoException();
                        reply.writeInt(_result30);
                        return true;
                    case 32:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _result31 = startGetVersion();
                        reply.writeNoException();
                        reply.writeByteArray(_result31);
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

    int authenticateFido(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    int cameraCapture() throws RemoteException;

    int closeHal() throws RemoteException;

    int dumpCmd(int i, byte[] bArr) throws RemoteException;

    int enableBioAssayFeature(byte b) throws RemoteException;

    int enableFfFeature(byte b) throws RemoteException;

    int enableFingerprintModule(byte b) throws RemoteException;

    int enumerate() throws RemoteException;

    int[] getIdList(int i) throws RemoteException;

    int invokeFidoCommand(byte[] bArr, byte[] bArr2) throws RemoteException;

    int isIdValid(int i, int i2) throws RemoteException;

    int navigate(int i) throws RemoteException;

    int openHal() throws RemoteException;

    int pauseEnroll() throws RemoteException;

    int reset_lockout(byte[] bArr) throws RemoteException;

    int resumeEnroll() throws RemoteException;

    int screenOff() throws RemoteException;

    int screenOn() throws RemoteException;

    int setSafeClass(int i) throws RemoteException;

    byte[] startGetVersion() throws RemoteException;

    int startHbd() throws RemoteException;

    int startOtpCheckTest() throws RemoteException;

    int startSensorInfoTest() throws RemoteException;

    int startTouchPrepareTest() throws RemoteException;

    int startTouchTest() throws RemoteException;

    int startUntouchTest() throws RemoteException;

    int stopAuthenticateFido() throws RemoteException;

    int stopCameraCapture() throws RemoteException;

    int stopHbd() throws RemoteException;

    int stopNavigation() throws RemoteException;

    int testCmd(int i, byte[] bArr) throws RemoteException;

    void testInit(IGoodixFingerprintDaemonCallback iGoodixFingerprintDaemonCallback) throws RemoteException;
}
