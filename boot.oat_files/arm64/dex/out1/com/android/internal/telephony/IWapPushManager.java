package com.android.internal.telephony;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWapPushManager extends IInterface {

    public static abstract class Stub extends Binder implements IWapPushManager {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IWapPushManager";
        static final int TRANSACTION_addPackage = 2;
        static final int TRANSACTION_deletePackage = 4;
        static final int TRANSACTION_processMessage = 1;
        static final int TRANSACTION_updatePackage = 3;

        private static class Proxy implements IWapPushManager {
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

            public int processMessage(String app_id, String content_type, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app_id);
                    _data.writeString(content_type);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addPackage(String x_app_id, String content_type, String package_name, String class_name, int app_type, boolean need_signature, boolean further_processing) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(x_app_id);
                    _data.writeString(content_type);
                    _data.writeString(package_name);
                    _data.writeString(class_name);
                    _data.writeInt(app_type);
                    if (need_signature) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (further_processing) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            public boolean updatePackage(String x_app_id, String content_type, String package_name, String class_name, int app_type, boolean need_signature, boolean further_processing) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(x_app_id);
                    _data.writeString(content_type);
                    _data.writeString(package_name);
                    _data.writeString(class_name);
                    _data.writeInt(app_type);
                    if (need_signature) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (further_processing) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public boolean deletePackage(String x_app_id, String content_type, String package_name, String class_name) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(x_app_id);
                    _data.writeString(content_type);
                    _data.writeString(package_name);
                    _data.writeString(class_name);
                    this.mRemote.transact(4, _data, _reply, 0);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWapPushManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWapPushManager)) {
                return new Proxy(obj);
            }
            return (IWapPushManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            String _arg0;
            String _arg1;
            String _arg2;
            String _arg3;
            int _arg4;
            boolean _arg5;
            boolean _arg6;
            boolean _result;
            switch (code) {
                case 1:
                    Intent _arg22;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    int _result2 = processMessage(_arg0, _arg1, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg5 = true;
                    } else {
                        _arg5 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = true;
                    } else {
                        _arg6 = false;
                    }
                    _result = addPackage(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg5 = true;
                    } else {
                        _arg5 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = true;
                    } else {
                        _arg6 = false;
                    }
                    _result = updatePackage(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = deletePackage(data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean addPackage(String str, String str2, String str3, String str4, int i, boolean z, boolean z2) throws RemoteException;

    boolean deletePackage(String str, String str2, String str3, String str4) throws RemoteException;

    int processMessage(String str, String str2, Intent intent) throws RemoteException;

    boolean updatePackage(String str, String str2, String str3, String str4, int i, boolean z, boolean z2) throws RemoteException;
}
