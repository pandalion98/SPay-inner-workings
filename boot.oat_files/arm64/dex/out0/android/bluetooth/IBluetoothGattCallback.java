package android.bluetooth;

import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanResult;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import java.util.List;

public interface IBluetoothGattCallback extends IInterface {

    public static abstract class Stub extends Binder implements IBluetoothGattCallback {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGattCallback";
        static final int TRANSACTION_onAutoConnectionStatusCb = 22;
        static final int TRANSACTION_onBatchScanResults = 4;
        static final int TRANSACTION_onCharacteristicRead = 10;
        static final int TRANSACTION_onCharacteristicWrite = 11;
        static final int TRANSACTION_onClientConnParamsChanged = 23;
        static final int TRANSACTION_onClientConnectionState = 2;
        static final int TRANSACTION_onClientRegistered = 1;
        static final int TRANSACTION_onConfigureMTU = 20;
        static final int TRANSACTION_onDescriptorRead = 13;
        static final int TRANSACTION_onDescriptorWrite = 14;
        static final int TRANSACTION_onExecuteWrite = 12;
        static final int TRANSACTION_onFoundOrLost = 21;
        static final int TRANSACTION_onGetCharacteristic = 7;
        static final int TRANSACTION_onGetDescriptor = 8;
        static final int TRANSACTION_onGetIncludedService = 6;
        static final int TRANSACTION_onGetService = 5;
        static final int TRANSACTION_onMonitorRssi = 19;
        static final int TRANSACTION_onMultiAdvertiseCallback = 17;
        static final int TRANSACTION_onNotify = 15;
        static final int TRANSACTION_onReadRemoteRssi = 16;
        static final int TRANSACTION_onScanManagerErrorCallback = 18;
        static final int TRANSACTION_onScanResult = 3;
        static final int TRANSACTION_onSearchComplete = 9;

        private static class Proxy implements IBluetoothGattCallback {
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

            public void onClientRegistered(int status, int clientIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(clientIf);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onClientConnectionState(int status, int clientIf, boolean connected, String address) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(clientIf);
                    if (!connected) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(address);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScanResult(ScanResult scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onBatchScanResults(List<ScanResult> batchResults) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(batchResults);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onGetService(String address, int srvcType, int srvcInstId, ParcelUuid srvcUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onGetIncludedService(String address, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int inclSrvcType, int inclSrvcInstId, ParcelUuid inclSrvcUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(inclSrvcType);
                    _data.writeInt(inclSrvcInstId);
                    if (inclSrvcUuid != null) {
                        _data.writeInt(1);
                        inclSrvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onGetCharacteristic(String address, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int charInstId, ParcelUuid charUuid, int charProps) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstId);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charProps);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onGetDescriptor(String address, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int charInstId, ParcelUuid charUuid, int descrInstId, ParcelUuid descrUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstId);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(descrInstId);
                    if (descrUuid != null) {
                        _data.writeInt(1);
                        descrUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onSearchComplete(String address, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onCharacteristicRead(String address, int status, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int charInstId, ParcelUuid charUuid, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstId);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(value);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onCharacteristicWrite(String address, int status, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int charInstId, ParcelUuid charUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstId);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onExecuteWrite(String address, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onDescriptorRead(String address, int status, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int charInstId, ParcelUuid charUuid, int descrInstId, ParcelUuid descrUuid, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstId);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(descrInstId);
                    if (descrUuid != null) {
                        _data.writeInt(1);
                        descrUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(value);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onDescriptorWrite(String address, int status, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int charInstId, ParcelUuid charUuid, int descrInstId, ParcelUuid descrUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstId);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(descrInstId);
                    if (descrUuid != null) {
                        _data.writeInt(1);
                        descrUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onNotify(String address, int srvcType, int srvcInstId, ParcelUuid srvcUuid, int charInstId, ParcelUuid charUuid, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstId);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstId);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(value);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onReadRemoteRssi(String address, int rssi, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(rssi);
                    _data.writeInt(status);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onMultiAdvertiseCallback(int status, boolean isStart, AdvertiseSettings advertiseSettings) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (!isStart) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (advertiseSettings != null) {
                        _data.writeInt(1);
                        advertiseSettings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScanManagerErrorCallback(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onMonitorRssi(String address, int rssi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(rssi);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onConfigureMTU(String address, int mtu, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(mtu);
                    _data.writeInt(status);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onFoundOrLost(boolean onFound, ScanResult scanResult) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!onFound) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onAutoConnectionStatusCb(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onClientConnParamsChanged(String address, int interval, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(interval);
                    _data.writeInt(status);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothGattCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothGattCallback)) {
                return new Proxy(obj);
            }
            return (IBluetoothGattCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String _arg0;
            int _arg1;
            int _arg2;
            ParcelUuid _arg3;
            int _arg4;
            int _arg5;
            ParcelUuid _arg6;
            ParcelUuid _arg52;
            int _arg32;
            ParcelUuid _arg42;
            int _arg7;
            ParcelUuid _arg8;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onClientRegistered(data.readInt(), data.readInt());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onClientConnectionState(data.readInt(), data.readInt(), data.readInt() != 0, data.readString());
                    return true;
                case 3:
                    ScanResult _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (ScanResult) ScanResult.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onScanResult(_arg02);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onBatchScanResults(data.createTypedArrayList(ScanResult.CREATOR));
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    onGetService(_arg0, _arg1, _arg2, _arg3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _arg4 = data.readInt();
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    onGetIncludedService(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg52 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    onGetCharacteristic(_arg0, _arg1, _arg2, _arg3, _arg4, _arg52, data.readInt());
                    return true;
                case 8:
                    ParcelUuid _arg72;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg52 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    int _arg62 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg72 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg72 = null;
                    }
                    onGetDescriptor(_arg0, _arg1, _arg2, _arg3, _arg4, _arg52, _arg62, _arg72);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    onSearchComplete(data.readString(), data.readInt());
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    _arg32 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg42 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    onCharacteristicRead(_arg0, _arg1, _arg2, _arg32, _arg42, _arg5, _arg6, data.createByteArray());
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    _arg32 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg42 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    onCharacteristicWrite(_arg0, _arg1, _arg2, _arg32, _arg42, _arg5, _arg6);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    onExecuteWrite(data.readString(), data.readInt());
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    _arg32 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg42 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    _arg7 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg8 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg8 = null;
                    }
                    onDescriptorRead(_arg0, _arg1, _arg2, _arg32, _arg42, _arg5, _arg6, _arg7, _arg8, data.createByteArray());
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    _arg32 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg42 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    _arg7 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg8 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg8 = null;
                    }
                    onDescriptorWrite(_arg0, _arg1, _arg2, _arg32, _arg42, _arg5, _arg6, _arg7, _arg8);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg52 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    onNotify(_arg0, _arg1, _arg2, _arg3, _arg4, _arg52, data.createByteArray());
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    onReadRemoteRssi(data.readString(), data.readInt(), data.readInt());
                    return true;
                case 17:
                    AdvertiseSettings _arg22;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    boolean _arg12 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg22 = (AdvertiseSettings) AdvertiseSettings.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    onMultiAdvertiseCallback(_arg03, _arg12, _arg22);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    onScanManagerErrorCallback(data.readInt());
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    onMonitorRssi(data.readString(), data.readInt());
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    onConfigureMTU(data.readString(), data.readInt(), data.readInt());
                    return true;
                case 21:
                    ScanResult _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg04 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg13 = (ScanResult) ScanResult.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    onFoundOrLost(_arg04, _arg13);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    onAutoConnectionStatusCb(data.readInt());
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    onClientConnParamsChanged(data.readString(), data.readInt(), data.readInt());
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onAutoConnectionStatusCb(int i) throws RemoteException;

    void onBatchScanResults(List<ScanResult> list) throws RemoteException;

    void onCharacteristicRead(String str, int i, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, byte[] bArr) throws RemoteException;

    void onCharacteristicWrite(String str, int i, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2) throws RemoteException;

    void onClientConnParamsChanged(String str, int i, int i2) throws RemoteException;

    void onClientConnectionState(int i, int i2, boolean z, String str) throws RemoteException;

    void onClientRegistered(int i, int i2) throws RemoteException;

    void onConfigureMTU(String str, int i, int i2) throws RemoteException;

    void onDescriptorRead(String str, int i, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, int i5, ParcelUuid parcelUuid3, byte[] bArr) throws RemoteException;

    void onDescriptorWrite(String str, int i, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, int i5, ParcelUuid parcelUuid3) throws RemoteException;

    void onExecuteWrite(String str, int i) throws RemoteException;

    void onFoundOrLost(boolean z, ScanResult scanResult) throws RemoteException;

    void onGetCharacteristic(String str, int i, int i2, ParcelUuid parcelUuid, int i3, ParcelUuid parcelUuid2, int i4) throws RemoteException;

    void onGetDescriptor(String str, int i, int i2, ParcelUuid parcelUuid, int i3, ParcelUuid parcelUuid2, int i4, ParcelUuid parcelUuid3) throws RemoteException;

    void onGetIncludedService(String str, int i, int i2, ParcelUuid parcelUuid, int i3, int i4, ParcelUuid parcelUuid2) throws RemoteException;

    void onGetService(String str, int i, int i2, ParcelUuid parcelUuid) throws RemoteException;

    void onMonitorRssi(String str, int i) throws RemoteException;

    void onMultiAdvertiseCallback(int i, boolean z, AdvertiseSettings advertiseSettings) throws RemoteException;

    void onNotify(String str, int i, int i2, ParcelUuid parcelUuid, int i3, ParcelUuid parcelUuid2, byte[] bArr) throws RemoteException;

    void onReadRemoteRssi(String str, int i, int i2) throws RemoteException;

    void onScanManagerErrorCallback(int i) throws RemoteException;

    void onScanResult(ScanResult scanResult) throws RemoteException;

    void onSearchComplete(String str, int i) throws RemoteException;
}
