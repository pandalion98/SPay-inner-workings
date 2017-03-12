package android.os;

public interface IVibratorService extends IInterface {

    public static abstract class Stub extends Binder implements IVibratorService {
        private static final String DESCRIPTOR = "android.os.IVibratorService";
        static final int TRANSACTION_cancelVibrate = 4;
        static final int TRANSACTION_getMagnitude = 12;
        static final int TRANSACTION_getMaxMagnitude = 11;
        static final int TRANSACTION_hasVibrator = 1;
        static final int TRANSACTION_isEnableIntensity = 5;
        static final int TRANSACTION_readFromFile = 14;
        static final int TRANSACTION_resetMagnitude = 10;
        static final int TRANSACTION_setMagnitude = 9;
        static final int TRANSACTION_vibrate = 2;
        static final int TRANSACTION_vibrateCommonPatternMagnitude = 8;
        static final int TRANSACTION_vibrateMagnitude = 6;
        static final int TRANSACTION_vibratePattern = 3;
        static final int TRANSACTION_vibratePatternMagnitude = 7;
        static final int TRANSACTION_writeToFile = 13;

        private static class Proxy implements IVibratorService {
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

            public boolean hasVibrator() throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void vibrate(int uid, String opPkg, long milliseconds, int usageHint, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(opPkg);
                    _data.writeLong(milliseconds);
                    _data.writeInt(usageHint);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void vibratePattern(int uid, String opPkg, long[] pattern, int repeat, int usageHint, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(opPkg);
                    _data.writeLongArray(pattern);
                    _data.writeInt(repeat);
                    _data.writeInt(usageHint);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelVibrate(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnableIntensity() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void vibrateMagnitude(int uid, String opPkg, long milliseconds, int usageHint, IBinder token, int magnitude) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(opPkg);
                    _data.writeLong(milliseconds);
                    _data.writeInt(usageHint);
                    _data.writeStrongBinder(token);
                    _data.writeInt(magnitude);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void vibratePatternMagnitude(int uid, String opPkg, long[] pattern, int repeat, int usageHint, IBinder token, int magnitude) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(opPkg);
                    _data.writeLongArray(pattern);
                    _data.writeInt(repeat);
                    _data.writeInt(usageHint);
                    _data.writeStrongBinder(token);
                    _data.writeInt(magnitude);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void vibrateCommonPatternMagnitude(int uid, String packageName, int type, int repeat, IBinder token, int magnitude, int usageHint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(type);
                    _data.writeInt(repeat);
                    _data.writeStrongBinder(token);
                    _data.writeInt(magnitude);
                    _data.writeInt(usageHint);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMagnitude(int magnitude) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(magnitude);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetMagnitude() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMaxMagnitude() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMagnitude(String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean writeToFile(long offset, int value) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(offset);
                    _data.writeInt(value);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int readFromFile(long offset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(offset);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVibratorService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVibratorService)) {
                return new Proxy(obj);
            }
            return (IVibratorService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _result;
            int _result2;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = hasVibrator();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    vibrate(data.readInt(), data.readString(), data.readLong(), data.readInt(), data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    vibratePattern(data.readInt(), data.readString(), data.createLongArray(), data.readInt(), data.readInt(), data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    cancelVibrate(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isEnableIntensity();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    vibrateMagnitude(data.readInt(), data.readString(), data.readLong(), data.readInt(), data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    vibratePatternMagnitude(data.readInt(), data.readString(), data.createLongArray(), data.readInt(), data.readInt(), data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    vibrateCommonPatternMagnitude(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readStrongBinder(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setMagnitude(data.readInt());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    resetMagnitude();
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getMaxMagnitude();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getMagnitude(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = writeToFile(data.readLong(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = readFromFile(data.readLong());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void cancelVibrate(IBinder iBinder) throws RemoteException;

    int getMagnitude(String str) throws RemoteException;

    int getMaxMagnitude() throws RemoteException;

    boolean hasVibrator() throws RemoteException;

    boolean isEnableIntensity() throws RemoteException;

    int readFromFile(long j) throws RemoteException;

    void resetMagnitude() throws RemoteException;

    void setMagnitude(int i) throws RemoteException;

    void vibrate(int i, String str, long j, int i2, IBinder iBinder) throws RemoteException;

    void vibrateCommonPatternMagnitude(int i, String str, int i2, int i3, IBinder iBinder, int i4, int i5) throws RemoteException;

    void vibrateMagnitude(int i, String str, long j, int i2, IBinder iBinder, int i3) throws RemoteException;

    void vibratePattern(int i, String str, long[] jArr, int i2, int i3, IBinder iBinder) throws RemoteException;

    void vibratePatternMagnitude(int i, String str, long[] jArr, int i2, int i3, IBinder iBinder, int i4) throws RemoteException;

    boolean writeToFile(long j, int i) throws RemoteException;
}
