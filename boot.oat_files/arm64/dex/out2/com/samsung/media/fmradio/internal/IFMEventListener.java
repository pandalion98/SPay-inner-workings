package com.samsung.media.fmradio.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFMEventListener extends IInterface {

    public static abstract class Stub extends Binder implements IFMEventListener {
        private static final String DESCRIPTOR = "com.samsung.media.fmradio.internal.IFMEventListener";
        static final int TRANSACTION_earPhoneConnected = 8;
        static final int TRANSACTION_earPhoneDisconnected = 9;
        static final int TRANSACTION_onAFReceived = 15;
        static final int TRANSACTION_onAFStarted = 14;
        static final int TRANSACTION_onChannelFound = 3;
        static final int TRANSACTION_onOff = 2;
        static final int TRANSACTION_onOn = 1;
        static final int TRANSACTION_onPIECCReceived = 18;
        static final int TRANSACTION_onRDSDisabled = 13;
        static final int TRANSACTION_onRDSEnabled = 12;
        static final int TRANSACTION_onRDSReceived = 10;
        static final int TRANSACTION_onRTPlusReceived = 11;
        static final int TRANSACTION_onScanFinished = 6;
        static final int TRANSACTION_onScanStarted = 4;
        static final int TRANSACTION_onScanStopped = 5;
        static final int TRANSACTION_onTune = 7;
        static final int TRANSACTION_recFinish = 17;
        static final int TRANSACTION_volumeLock = 16;

        private static class Proxy implements IFMEventListener {
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

            public void onOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onOff(int reasonCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reasonCode);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onChannelFound(long freq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(freq);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScanStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScanStopped(long[] freqArray) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (freqArray == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(freqArray.length);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScanFinished(long[] freqArray) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (freqArray == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(freqArray.length);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onTune(long freq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(freq);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void earPhoneConnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void earPhoneDisconnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onRDSReceived(long freq, String channelName, String radioText) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(freq);
                    _data.writeString(channelName);
                    _data.writeString(radioText);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onRTPlusReceived(int contentType1, int startPos1, int additionalLen1, int contentType2, int startPos2, int additionalLen2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(contentType1);
                    _data.writeInt(startPos1);
                    _data.writeInt(additionalLen1);
                    _data.writeInt(contentType2);
                    _data.writeInt(startPos2);
                    _data.writeInt(additionalLen2);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onRDSEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onRDSDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onAFStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onAFReceived(long freq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(freq);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void volumeLock() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void recFinish() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onPIECCReceived(int pi, int ecc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pi);
                    _data.writeInt(ecc);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFMEventListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFMEventListener)) {
                return new Proxy(obj);
            }
            return (IFMEventListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _arg0_length;
            long[] _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onOn();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onOff(data.readInt());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onChannelFound(data.readLong());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onScanStarted();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0_length = data.readInt();
                    if (_arg0_length < 0) {
                        _arg0 = null;
                    } else {
                        _arg0 = new long[_arg0_length];
                    }
                    onScanStopped(_arg0);
                    reply.writeLongArray(_arg0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0_length = data.readInt();
                    if (_arg0_length < 0) {
                        _arg0 = null;
                    } else {
                        _arg0 = new long[_arg0_length];
                    }
                    onScanFinished(_arg0);
                    reply.writeLongArray(_arg0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    onTune(data.readLong());
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    earPhoneConnected();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    earPhoneDisconnected();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    onRDSReceived(data.readLong(), data.readString(), data.readString());
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    onRTPlusReceived(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    onRDSEnabled();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    onRDSDisabled();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    onAFStarted();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    onAFReceived(data.readLong());
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    volumeLock();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    recFinish();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    onPIECCReceived(data.readInt(), data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void earPhoneConnected() throws RemoteException;

    void earPhoneDisconnected() throws RemoteException;

    void onAFReceived(long j) throws RemoteException;

    void onAFStarted() throws RemoteException;

    void onChannelFound(long j) throws RemoteException;

    void onOff(int i) throws RemoteException;

    void onOn() throws RemoteException;

    void onPIECCReceived(int i, int i2) throws RemoteException;

    void onRDSDisabled() throws RemoteException;

    void onRDSEnabled() throws RemoteException;

    void onRDSReceived(long j, String str, String str2) throws RemoteException;

    void onRTPlusReceived(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void onScanFinished(long[] jArr) throws RemoteException;

    void onScanStarted() throws RemoteException;

    void onScanStopped(long[] jArr) throws RemoteException;

    void onTune(long j) throws RemoteException;

    void recFinish() throws RemoteException;

    void volumeLock() throws RemoteException;
}
