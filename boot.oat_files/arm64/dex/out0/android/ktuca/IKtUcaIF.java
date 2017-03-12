package android.ktuca;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IKtUcaIF extends IInterface {

    public static abstract class Stub extends Binder implements IKtUcaIF {
        private static final String DESCRIPTOR = "android.ktuca.IKtUcaIF";
        static final int TRANSACTION_KUCA_CHInit = 19;
        static final int TRANSACTION_KUCA_Close = 16;
        static final int TRANSACTION_KUCA_CloseT = 25;
        static final int TRANSACTION_KUCA_KUH_Establish = 21;
        static final int TRANSACTION_KUCA_KUH_Release = 22;
        static final int TRANSACTION_KUCA_KUH_Transmit = 23;
        static final int TRANSACTION_KUCA_Open = 14;
        static final int TRANSACTION_KUCA_OpenT = 24;
        static final int TRANSACTION_KUCA_Transmit = 15;
        static final int TRANSACTION_KUCA_UCAVersion = 18;
        static final int TRANSACTION_KUCA_getHandle = 3;
        static final int TRANSACTION_KUCA_getICCID = 6;
        static final int TRANSACTION_KUCA_getIMSI = 5;
        static final int TRANSACTION_KUCA_getMDN = 8;
        static final int TRANSACTION_KUCA_getMODEL = 9;
        static final int TRANSACTION_KUCA_getMSISDN = 4;
        static final int TRANSACTION_KUCA_getPUID = 7;
        static final int TRANSACTION_KUCA_getPinStatus = 12;
        static final int TRANSACTION_KUCA_getSIMInfo = 10;
        static final int TRANSACTION_KUCA_getSimStatus = 17;
        static final int TRANSACTION_KUCA_printCHInfo = 20;
        static final int TRANSACTION_KUCA_usimAUTH = 11;
        static final int TRANSACTION_KUCA_verifyPin = 13;
        static final int TRANSACTION_getResource = 1;
        static final int TRANSACTION_releaseResource = 2;

        private static class Proxy implements IKtUcaIF {
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

            public int getResource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int releaseResource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getHandle(byte[] callerId, byte[] preKey, byte[] appId, byte[] handle, int[] handleLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(callerId);
                    _data.writeByteArray(preKey);
                    _data.writeByteArray(appId);
                    _data.writeByteArray(handle);
                    _data.writeIntArray(handleLen);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(handle);
                    _reply.readIntArray(handleLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getMSISDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    _data.writeInt(encryptType);
                    _data.writeByteArray(deviceIp);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getIMSI(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    _data.writeInt(encryptType);
                    _data.writeByteArray(deviceIp);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getICCID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    _data.writeInt(encryptType);
                    _data.writeByteArray(deviceIp);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getPUID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    _data.writeInt(encryptType);
                    _data.writeByteArray(deviceIp);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getMDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    _data.writeInt(encryptType);
                    _data.writeByteArray(deviceIp);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getMODEL(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    _data.writeInt(encryptType);
                    _data.writeByteArray(deviceIp);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getSIMInfo(byte[] handle, byte[] output, int[] outputLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_usimAUTH(byte[] handle, byte[] rand, byte[] autn, byte[] output, int[] outputLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    _data.writeByteArray(rand);
                    _data.writeByteArray(autn);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getPinStatus(byte[] handle, int pinId, byte[] output, int[] outputLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    _data.writeInt(pinId);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_verifyPin(byte[] handle, int pinId, String pinCode, byte[] output, int[] outputLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    _data.writeInt(pinId);
                    _data.writeString(pinCode);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_Open(byte[] handle, byte[] channel, int[] channelLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (channel == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(channel.length);
                    }
                    if (channelLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(channelLen.length);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(channel);
                    _reply.readIntArray(channelLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_Transmit(byte[] handle, byte[] input, int inputLen, byte[] output, int[] outputLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    _data.writeByteArray(input);
                    _data.writeInt(inputLen);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_Close(byte[] handle, byte channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    _data.writeByte(channel);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_getSimStatus(byte[] handle, byte[] output) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_UCAVersion(byte[] handle, byte[] output, int[] outputLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(handle);
                    if (output == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(output.length);
                    }
                    if (outputLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(outputLen.length);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(output);
                    _reply.readIntArray(outputLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_CHInit(byte ucatag, byte[] channel, int[] channelLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(ucatag);
                    if (channel == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(channel.length);
                    }
                    if (channelLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(channelLen.length);
                    }
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(channel);
                    _reply.readIntArray(channelLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_printCHInfo(byte ucatag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(ucatag);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_KUH_Establish(byte ucatag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(ucatag);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_KUH_Release(byte ucatag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(ucatag);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_KUH_Transmit(byte ucatag, byte[] pbSendBuffer, int cbSendLength, byte[] pbRecvBuffer, int[] pcbRecvLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(ucatag);
                    _data.writeByteArray(pbSendBuffer);
                    _data.writeInt(cbSendLength);
                    if (pbRecvBuffer == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(pbRecvBuffer.length);
                    }
                    if (pcbRecvLength == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(pcbRecvLength.length);
                    }
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(pbRecvBuffer);
                    _reply.readIntArray(pcbRecvLength);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_OpenT(byte[] appId, byte[] channel, int[] channelLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(appId);
                    if (channel == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(channel.length);
                    }
                    if (channelLen == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(channelLen.length);
                    }
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    _reply.readByteArray(channel);
                    _reply.readIntArray(channelLen);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long KUCA_CloseT(byte[] appId, byte channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(appId);
                    _data.writeByte(channel);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
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

        public static IKtUcaIF asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IKtUcaIF)) {
                return new Proxy(obj);
            }
            return (IKtUcaIF) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            byte[] _arg0;
            byte[] _arg1;
            byte[] _arg2;
            byte[] _arg3;
            int[] _arg4;
            long _result2;
            int _arg1_length;
            int _arg2_length;
            int[] _arg22;
            int _arg3_length;
            int _arg4_length;
            int _arg12;
            int _arg23;
            byte _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getResource();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = releaseResource();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1 = data.createByteArray();
                    _arg2 = data.createByteArray();
                    _arg3 = data.createByteArray();
                    _arg4 = data.createIntArray();
                    _result2 = KUCA_getHandle(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg3);
                    reply.writeIntArray(_arg4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_getMSISDN(_arg0, _arg1, _arg22, data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_getIMSI(_arg0, _arg1, _arg22, data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_getICCID(_arg0, _arg1, _arg22, data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_getPUID(_arg0, _arg1, _arg22, data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_getMDN(_arg0, _arg1, _arg22, data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_getMODEL(_arg0, _arg1, _arg22, data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_getSIMInfo(_arg0, _arg1, _arg22);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1 = data.createByteArray();
                    _arg2 = data.createByteArray();
                    _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg3 = null;
                    } else {
                        _arg3 = new byte[_arg3_length];
                    }
                    _arg4_length = data.readInt();
                    if (_arg4_length < 0) {
                        _arg4 = null;
                    } else {
                        _arg4 = new int[_arg4_length];
                    }
                    _result2 = KUCA_usimAUTH(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg3);
                    reply.writeIntArray(_arg4);
                    return true;
                case 12:
                    int[] _arg32;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg12 = data.readInt();
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        _arg2 = new byte[_arg2_length];
                    }
                    _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg32 = null;
                    } else {
                        _arg32 = new int[_arg3_length];
                    }
                    _result2 = KUCA_getPinStatus(_arg0, _arg12, _arg2, _arg32);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg2);
                    reply.writeIntArray(_arg32);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg12 = data.readInt();
                    String _arg24 = data.readString();
                    _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg3 = null;
                    } else {
                        _arg3 = new byte[_arg3_length];
                    }
                    _arg4_length = data.readInt();
                    if (_arg4_length < 0) {
                        _arg4 = null;
                    } else {
                        _arg4 = new int[_arg4_length];
                    }
                    _result2 = KUCA_verifyPin(_arg0, _arg12, _arg24, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg3);
                    reply.writeIntArray(_arg4);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_Open(_arg0, _arg1, _arg22);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1 = data.createByteArray();
                    _arg23 = data.readInt();
                    _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg3 = null;
                    } else {
                        _arg3 = new byte[_arg3_length];
                    }
                    _arg4_length = data.readInt();
                    if (_arg4_length < 0) {
                        _arg4 = null;
                    } else {
                        _arg4 = new int[_arg4_length];
                    }
                    _result2 = KUCA_Transmit(_arg0, _arg1, _arg23, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg3);
                    reply.writeIntArray(_arg4);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = KUCA_Close(data.createByteArray(), data.readByte());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _result2 = KUCA_getSimStatus(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_UCAVersion(_arg0, _arg1, _arg22);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readByte();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_CHInit(_arg02, _arg1, _arg22);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = KUCA_printCHInfo(data.readByte());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = KUCA_KUH_Establish(data.readByte());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = KUCA_KUH_Release(data.readByte());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readByte();
                    _arg1 = data.createByteArray();
                    _arg23 = data.readInt();
                    _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg3 = null;
                    } else {
                        _arg3 = new byte[_arg3_length];
                    }
                    _arg4_length = data.readInt();
                    if (_arg4_length < 0) {
                        _arg4 = null;
                    } else {
                        _arg4 = new int[_arg4_length];
                    }
                    _result2 = KUCA_KUH_Transmit(_arg02, _arg1, _arg23, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg3);
                    reply.writeIntArray(_arg4);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.createByteArray();
                    _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new int[_arg2_length];
                    }
                    _result2 = KUCA_OpenT(_arg0, _arg1, _arg22);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    reply.writeByteArray(_arg1);
                    reply.writeIntArray(_arg22);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = KUCA_CloseT(data.createByteArray(), data.readByte());
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    long KUCA_CHInit(byte b, byte[] bArr, int[] iArr) throws RemoteException;

    long KUCA_Close(byte[] bArr, byte b) throws RemoteException;

    long KUCA_CloseT(byte[] bArr, byte b) throws RemoteException;

    long KUCA_KUH_Establish(byte b) throws RemoteException;

    long KUCA_KUH_Release(byte b) throws RemoteException;

    long KUCA_KUH_Transmit(byte b, byte[] bArr, int i, byte[] bArr2, int[] iArr) throws RemoteException;

    long KUCA_Open(byte[] bArr, byte[] bArr2, int[] iArr) throws RemoteException;

    long KUCA_OpenT(byte[] bArr, byte[] bArr2, int[] iArr) throws RemoteException;

    long KUCA_Transmit(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int[] iArr) throws RemoteException;

    long KUCA_UCAVersion(byte[] bArr, byte[] bArr2, int[] iArr) throws RemoteException;

    long KUCA_getHandle(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int[] iArr) throws RemoteException;

    long KUCA_getICCID(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3) throws RemoteException;

    long KUCA_getIMSI(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3) throws RemoteException;

    long KUCA_getMDN(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3) throws RemoteException;

    long KUCA_getMODEL(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3) throws RemoteException;

    long KUCA_getMSISDN(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3) throws RemoteException;

    long KUCA_getPUID(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3) throws RemoteException;

    long KUCA_getPinStatus(byte[] bArr, int i, byte[] bArr2, int[] iArr) throws RemoteException;

    long KUCA_getSIMInfo(byte[] bArr, byte[] bArr2, int[] iArr) throws RemoteException;

    long KUCA_getSimStatus(byte[] bArr, byte[] bArr2) throws RemoteException;

    long KUCA_printCHInfo(byte b) throws RemoteException;

    long KUCA_usimAUTH(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int[] iArr) throws RemoteException;

    long KUCA_verifyPin(byte[] bArr, int i, String str, byte[] bArr2, int[] iArr) throws RemoteException;

    int getResource() throws RemoteException;

    int releaseResource() throws RemoteException;
}
