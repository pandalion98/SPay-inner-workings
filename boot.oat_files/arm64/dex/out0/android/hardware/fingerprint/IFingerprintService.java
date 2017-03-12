package android.hardware.fingerprint;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IFingerprintService extends IInterface {

    public static abstract class Stub extends Binder implements IFingerprintService {
        private static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintService";
        static final int TRANSACTION_addLockoutResetCallback = 15;
        static final int TRANSACTION_authenticate = 1;
        static final int TRANSACTION_cancelAuthentication = 2;
        static final int TRANSACTION_cancelEnrollment = 4;
        static final int TRANSACTION_enroll = 3;
        static final int TRANSACTION_getAuthenticatorId = 12;
        static final int TRANSACTION_getEnrolledFingerprints = 7;
        static final int TRANSACTION_hasEnrolledFingerprints = 11;
        static final int TRANSACTION_isHardwareDetected = 8;
        static final int TRANSACTION_postEnroll = 10;
        static final int TRANSACTION_preEnroll = 9;
        static final int TRANSACTION_remove = 5;
        static final int TRANSACTION_rename = 6;
        static final int TRANSACTION_request = 13;
        static final int TRANSACTION_resetTimeout = 14;

        private static class Proxy implements IFingerprintService {
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

            public void authenticate(IBinder token, long sessionId, int userId, IFingerprintServiceReceiver receiver, int flags, String opPackageName, Bundle attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeLong(sessionId);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeString(opPackageName);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelAuthentication(IBinder token, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enroll(IBinder token, byte[] cryptoToken, int groupId, IFingerprintServiceReceiver receiver, int flags, Bundle attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeByteArray(cryptoToken);
                    _data.writeInt(groupId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(flags);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelEnrollment(IBinder token) throws RemoteException {
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

            public void remove(IBinder token, int fingerId, int groupId, IFingerprintServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(fingerId);
                    _data.writeInt(groupId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void rename(int fingerId, int groupId, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    _data.writeInt(groupId);
                    _data.writeString(name);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<Fingerprint> getEnrolledFingerprints(int groupId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<Fingerprint> _result = _reply.createTypedArrayList(Fingerprint.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isHardwareDetected(long deviceId, String opPackageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public long preEnroll(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int postEnroll(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasEnrolledFingerprints(int groupId, String opPackageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(11, _data, _reply, 0);
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

            public long getAuthenticatorId(String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int request(IBinder token, int cmd, byte[] inputBuf, byte[] outputBuf, int inParam, int groupId, IFingerprintServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(cmd);
                    _data.writeByteArray(inputBuf);
                    if (outputBuf == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputBuf.length);
                    }
                    _data.writeInt(inParam);
                    _data.writeInt(groupId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(outputBuf);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetTimeout(byte[] cryptoToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(cryptoToken);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addLockoutResetCallback(IFingerprintServiceLockoutResetCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFingerprintService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFingerprintService)) {
                return new Proxy(obj);
            }
            return (IFingerprintService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder _arg0;
            int _arg2;
            IFingerprintServiceReceiver _arg3;
            int _arg4;
            boolean _result;
            long _result2;
            int _result3;
            switch (code) {
                case 1:
                    Bundle _arg6;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    long _arg1 = data.readLong();
                    _arg2 = data.readInt();
                    _arg3 = android.hardware.fingerprint.IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                    _arg4 = data.readInt();
                    String _arg5 = data.readString();
                    if (data.readInt() != 0) {
                        _arg6 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    authenticate(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    cancelAuthentication(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    Bundle _arg52;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    byte[] _arg12 = data.createByteArray();
                    _arg2 = data.readInt();
                    _arg3 = android.hardware.fingerprint.IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                    _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg52 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    enroll(_arg0, _arg12, _arg2, _arg3, _arg4, _arg52);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    cancelEnrollment(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    remove(data.readStrongBinder(), data.readInt(), data.readInt(), android.hardware.fingerprint.IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    rename(data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    List<Fingerprint> _result4 = getEnrolledFingerprints(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isHardwareDetected(data.readLong(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = preEnroll(data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = postEnroll(data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result = hasEnrolledFingerprints(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAuthenticatorId(data.readString());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 13:
                    byte[] _arg32;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    int _arg13 = data.readInt();
                    byte[] _arg22 = data.createByteArray();
                    int _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg32 = null;
                    } else {
                        _arg32 = new byte[_arg3_length];
                    }
                    _result3 = request(_arg0, _arg13, _arg22, _arg32, data.readInt(), data.readInt(), android.hardware.fingerprint.IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    reply.writeByteArray(_arg32);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    resetTimeout(data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    addLockoutResetCallback(android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addLockoutResetCallback(IFingerprintServiceLockoutResetCallback iFingerprintServiceLockoutResetCallback) throws RemoteException;

    void authenticate(IBinder iBinder, long j, int i, IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, String str, Bundle bundle) throws RemoteException;

    void cancelAuthentication(IBinder iBinder, String str) throws RemoteException;

    void cancelEnrollment(IBinder iBinder) throws RemoteException;

    void enroll(IBinder iBinder, byte[] bArr, int i, IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, Bundle bundle) throws RemoteException;

    long getAuthenticatorId(String str) throws RemoteException;

    List<Fingerprint> getEnrolledFingerprints(int i, String str) throws RemoteException;

    boolean hasEnrolledFingerprints(int i, String str) throws RemoteException;

    boolean isHardwareDetected(long j, String str) throws RemoteException;

    int postEnroll(IBinder iBinder) throws RemoteException;

    long preEnroll(IBinder iBinder) throws RemoteException;

    void remove(IBinder iBinder, int i, int i2, IFingerprintServiceReceiver iFingerprintServiceReceiver) throws RemoteException;

    void rename(int i, int i2, String str) throws RemoteException;

    int request(IBinder iBinder, int i, byte[] bArr, byte[] bArr2, int i2, int i3, IFingerprintServiceReceiver iFingerprintServiceReceiver) throws RemoteException;

    void resetTimeout(byte[] bArr) throws RemoteException;
}
