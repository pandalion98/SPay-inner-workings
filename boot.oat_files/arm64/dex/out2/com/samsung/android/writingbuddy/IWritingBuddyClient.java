package com.samsung.android.writingbuddy;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

public interface IWritingBuddyClient extends IInterface {

    public static abstract class Stub extends Binder implements IWritingBuddyClient {
        private static final String DESCRIPTOR = "com.samsung.android.writingbuddy.IWritingBuddyClient";
        static final int TRANSACTION_onImageInserted = 1;
        static final int TRANSACTION_onResultReceived = 4;
        static final int TRANSACTION_onTextDeleted = 3;
        static final int TRANSACTION_onTextInserted = 2;
        static final int TRANSACTION_onUpdateDialog = 5;

        private static class Proxy implements IWritingBuddyClient {
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

            public void onImageInserted(int clientId, Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onTextInserted(int clientId, int where, CharSequence text, int nextCursor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(where);
                    if (text != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(text, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(nextCursor);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onTextDeleted(int clientId, int start, int end) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(start);
                    _data.writeInt(end);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onResultReceived(int clientId, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onUpdateDialog(int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWritingBuddyClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWritingBuddyClient)) {
                return new Proxy(obj);
            }
            return (IWritingBuddyClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _arg0;
            switch (code) {
                case 1:
                    Uri _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    onImageInserted(_arg0, _arg1);
                    return true;
                case 2:
                    CharSequence _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    int _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    onTextInserted(_arg0, _arg12, _arg2, data.readInt());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onTextDeleted(data.readInt(), data.readInt(), data.readInt());
                    return true;
                case 4:
                    Bundle _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    onResultReceived(_arg0, _arg13);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onUpdateDialog(data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onImageInserted(int i, Uri uri) throws RemoteException;

    void onResultReceived(int i, Bundle bundle) throws RemoteException;

    void onTextDeleted(int i, int i2, int i3) throws RemoteException;

    void onTextInserted(int i, int i2, CharSequence charSequence, int i3) throws RemoteException;

    void onUpdateDialog(int i) throws RemoteException;
}
