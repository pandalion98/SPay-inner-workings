package com.broadcom.fm.fmreceiver;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFmReceiverCallback extends IInterface {

    public static abstract class Stub extends Binder implements IFmReceiverCallback {
        private static final String DESCRIPTOR = "com.broadcom.fm.fmreceiver.IFmReceiverCallback";
        static final int TRANSACTION_onRdsAFEvent = 4;
        static final int TRANSACTION_onRdsDataEvent = 2;
        static final int TRANSACTION_onRdsPIECCEvent = 5;
        static final int TRANSACTION_onRdsRTPlusEvent = 3;
        static final int TRANSACTION_onStatusEvent = 1;

        private static class Proxy implements IFmReceiverCallback {
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

            public void onStatusEvent(int freq, int rssi, int snr, boolean radioIsOn, int rdsProgramType, String rdsProgramService, String rdsRadioText, String rdsProgramTypeName, boolean isMute) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(freq);
                    _data.writeInt(rssi);
                    _data.writeInt(snr);
                    _data.writeInt(radioIsOn ? 1 : 0);
                    _data.writeInt(rdsProgramType);
                    _data.writeString(rdsProgramService);
                    _data.writeString(rdsRadioText);
                    _data.writeString(rdsProgramTypeName);
                    if (!isMute) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRdsDataEvent(int rdsDataType, int rdsIndex, String rdsText) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rdsDataType);
                    _data.writeInt(rdsIndex);
                    _data.writeString(rdsText);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRdsRTPlusEvent(int contentType1, int startPos1, int additionalLen1, int contentType2, int startPos2, int additionalLen2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(contentType1);
                    _data.writeInt(startPos1);
                    _data.writeInt(additionalLen1);
                    _data.writeInt(contentType2);
                    _data.writeInt(startPos2);
                    _data.writeInt(additionalLen2);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRdsAFEvent(int afreq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(afreq);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRdsPIECCEvent(int pi, int ecc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pi);
                    _data.writeInt(ecc);
                    this.mRemote.transact(5, _data, _reply, 0);
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

        public static IFmReceiverCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFmReceiverCallback)) {
                return new Proxy(obj);
            }
            return (IFmReceiverCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onStatusEvent(data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt(), data.readString(), data.readString(), data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onRdsDataEvent(data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onRdsRTPlusEvent(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onRdsAFEvent(data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onRdsPIECCEvent(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onRdsAFEvent(int i) throws RemoteException;

    void onRdsDataEvent(int i, int i2, String str) throws RemoteException;

    void onRdsPIECCEvent(int i, int i2) throws RemoteException;

    void onRdsRTPlusEvent(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void onStatusEvent(int i, int i2, int i3, boolean z, int i4, String str, String str2, String str3, boolean z2) throws RemoteException;
}
