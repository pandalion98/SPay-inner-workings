package android.bluetooth;

import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import java.util.List;

public interface IBluetoothGatt extends IInterface {

    public static abstract class Stub extends Binder implements IBluetoothGatt {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGatt";
        static final int TRANSACTION_addCharacteristic = 30;
        static final int TRANSACTION_addDescriptor = 31;
        static final int TRANSACTION_addIncludedService = 29;
        static final int TRANSACTION_beginReliableWrite = 19;
        static final int TRANSACTION_beginServiceDeclaration = 28;
        static final int TRANSACTION_clearServices = 34;
        static final int TRANSACTION_clientConnect = 10;
        static final int TRANSACTION_clientConnectionParameterUpdate = 39;
        static final int TRANSACTION_clientDataRateUpdate = 42;
        static final int TRANSACTION_clientDisconnect = 11;
        static final int TRANSACTION_configureMTU = 22;
        static final int TRANSACTION_connectionParameterUpdate = 23;
        static final int TRANSACTION_disconnectAll = 43;
        static final int TRANSACTION_discoverServices = 13;
        static final int TRANSACTION_endReliableWrite = 20;
        static final int TRANSACTION_endServiceDeclaration = 32;
        static final int TRANSACTION_flushPendingBatchResults = 4;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 1;
        static final int TRANSACTION_monitorRssi = 37;
        static final int TRANSACTION_numHwTrackFiltersAvailable = 45;
        static final int TRANSACTION_readCharacteristic = 14;
        static final int TRANSACTION_readDescriptor = 16;
        static final int TRANSACTION_readRemoteRssi = 21;
        static final int TRANSACTION_refreshDevice = 12;
        static final int TRANSACTION_registerClient = 7;
        static final int TRANSACTION_registerForNotification = 18;
        static final int TRANSACTION_registerServer = 24;
        static final int TRANSACTION_removeService = 33;
        static final int TRANSACTION_sendNotification = 36;
        static final int TRANSACTION_sendResponse = 35;
        static final int TRANSACTION_serverConnect = 26;
        static final int TRANSACTION_serverConnectionParameterUpdate = 38;
        static final int TRANSACTION_serverDataRateUpdate = 41;
        static final int TRANSACTION_serverDisconnect = 27;
        static final int TRANSACTION_serverDisconnectIncomingConnClients = 40;
        static final int TRANSACTION_startMultiAdvertising = 5;
        static final int TRANSACTION_startScan = 2;
        static final int TRANSACTION_stopMultiAdvertising = 6;
        static final int TRANSACTION_stopScan = 3;
        static final int TRANSACTION_unregAll = 44;
        static final int TRANSACTION_unregisterClient = 8;
        static final int TRANSACTION_unregisterClientbyUuid = 9;
        static final int TRANSACTION_unregisterServer = 25;
        static final int TRANSACTION_writeCharacteristic = 15;
        static final int TRANSACTION_writeDescriptor = 17;

        private static class Proxy implements IBluetoothGatt {
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

            public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(states);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<BluetoothDevice> _result = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startScan(int appIf, boolean isServer, ScanSettings settings, List<ScanFilter> filters, List scanStorages, String callingPackage) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appIf);
                    if (!isServer) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(filters);
                    _data.writeList(scanStorages);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopScan(int appIf, boolean isServer) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appIf);
                    if (isServer) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void flushPendingBatchResults(int appIf, boolean isServer) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appIf);
                    if (isServer) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startMultiAdvertising(int appIf, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseSettings settings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appIf);
                    if (advertiseData != null) {
                        _data.writeInt(1);
                        advertiseData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (scanResponse != null) {
                        _data.writeInt(1);
                        scanResponse.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopMultiAdvertising(int appIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appIf);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerClient(ParcelUuid appId, IBluetoothGattCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appId != null) {
                        _data.writeInt(1);
                        appId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterClient(int clientIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterClientbyUuid(ParcelUuid appId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appId != null) {
                        _data.writeInt(1);
                        appId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clientConnect(int clientIf, String address, boolean isDirect, int transport, boolean set_own_addr_type, int own_addr_type) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(isDirect ? 1 : 0);
                    _data.writeInt(transport);
                    if (!set_own_addr_type) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(own_addr_type);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clientDisconnect(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void refreshDevice(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void discoverServices(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void readCharacteristic(int clientIf, String address, int srvcType, int srvcInstanceId, ParcelUuid srvcId, int charInstanceId, ParcelUuid charId, int authReq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstanceId);
                    if (charId != null) {
                        _data.writeInt(1);
                        charId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(authReq);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void writeCharacteristic(int clientIf, String address, int srvcType, int srvcInstanceId, ParcelUuid srvcId, int charInstanceId, ParcelUuid charId, int writeType, int authReq, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstanceId);
                    if (charId != null) {
                        _data.writeInt(1);
                        charId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(writeType);
                    _data.writeInt(authReq);
                    _data.writeByteArray(value);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void readDescriptor(int clientIf, String address, int srvcType, int srvcInstanceId, ParcelUuid srvcId, int charInstanceId, ParcelUuid charId, int descrInstanceId, ParcelUuid descrUuid, int authReq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstanceId);
                    if (charId != null) {
                        _data.writeInt(1);
                        charId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(descrInstanceId);
                    if (descrUuid != null) {
                        _data.writeInt(1);
                        descrUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(authReq);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void writeDescriptor(int clientIf, String address, int srvcType, int srvcInstanceId, ParcelUuid srvcId, int charInstanceId, ParcelUuid charId, int descrInstanceId, ParcelUuid descrId, int writeType, int authReq, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstanceId);
                    if (charId != null) {
                        _data.writeInt(1);
                        charId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(descrInstanceId);
                    if (descrId != null) {
                        _data.writeInt(1);
                        descrId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(writeType);
                    _data.writeInt(authReq);
                    _data.writeByteArray(value);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerForNotification(int clientIf, String address, int srvcType, int srvcInstanceId, ParcelUuid srvcId, int charInstanceId, ParcelUuid charId, boolean enable) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstanceId);
                    if (charId != null) {
                        _data.writeInt(1);
                        charId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enable) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void beginReliableWrite(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void endReliableWrite(int clientIf, String address, boolean execute) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (execute) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void readRemoteRssi(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void configureMTU(int clientIf, String address, int mtu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(mtu);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectionParameterUpdate(int clientIf, String address, int connectionPriority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(connectionPriority);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerServer(ParcelUuid appId, IBluetoothGattServerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appId != null) {
                        _data.writeInt(1);
                        appId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterServer(int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverConnect(int servertIf, String address, boolean isDirect, int transport) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(servertIf);
                    _data.writeString(address);
                    if (isDirect) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(transport);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverDisconnect(int serverIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void beginServiceDeclaration(int serverIf, int srvcType, int srvcInstanceId, int minHandles, ParcelUuid srvcId, boolean advertisePreferred) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    _data.writeInt(minHandles);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!advertisePreferred) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addIncludedService(int serverIf, int srvcType, int srvcInstanceId, ParcelUuid srvcId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addCharacteristic(int serverIf, ParcelUuid charId, int properties, int permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    if (charId != null) {
                        _data.writeInt(1);
                        charId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(properties);
                    _data.writeInt(permissions);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addDescriptor(int serverIf, ParcelUuid descId, int permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    if (descId != null) {
                        _data.writeInt(1);
                        descId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(permissions);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void endServiceDeclaration(int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeService(int serverIf, int srvcType, int srvcInstanceId, ParcelUuid srvcId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearServices(int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendResponse(int serverIf, String address, int requestId, int status, int offset, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(requestId);
                    _data.writeInt(status);
                    _data.writeInt(offset);
                    _data.writeByteArray(value);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendNotification(int serverIf, String address, int srvcType, int srvcInstanceId, ParcelUuid srvcId, int charInstanceId, ParcelUuid charId, boolean confirm, byte[] value) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    _data.writeInt(srvcInstanceId);
                    if (srvcId != null) {
                        _data.writeInt(1);
                        srvcId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(charInstanceId);
                    if (charId != null) {
                        _data.writeInt(1);
                        charId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!confirm) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeByteArray(value);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void monitorRssi(int clientIf, String address, int rssi_low, int rssi_range, int rssi_high) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(rssi_low);
                    _data.writeInt(rssi_range);
                    _data.writeInt(rssi_high);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverConnectionParameterUpdate(int serverIf, String address, int minInterval, int maxInterval, int latency, int timeout, int minCE, int maxCE) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(minInterval);
                    _data.writeInt(maxInterval);
                    _data.writeInt(latency);
                    _data.writeInt(timeout);
                    _data.writeInt(minCE);
                    _data.writeInt(maxCE);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clientConnectionParameterUpdate(int clientIf, String address, int minInterval, int maxInterval, int latency, int timeout, int minCE, int maxCE) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(minInterval);
                    _data.writeInt(maxInterval);
                    _data.writeInt(latency);
                    _data.writeInt(timeout);
                    _data.writeInt(minCE);
                    _data.writeInt(maxCE);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverDisconnectIncomingConnClients() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverDataRateUpdate(int serverIf, String address, int interval, int duty) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(interval);
                    _data.writeInt(duty);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clientDataRateUpdate(int clientIf, String address, int interval, int duty) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(interval);
                    _data.writeInt(duty);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnectAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregAll() throws RemoteException {
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

            public int numHwTrackFiltersAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
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

        public static IBluetoothGatt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothGatt)) {
                return new Proxy(obj);
            }
            return (IBluetoothGatt) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _arg0;
            ParcelUuid _arg02;
            String _arg1;
            int _arg2;
            int _arg3;
            ParcelUuid _arg4;
            int _arg5;
            ParcelUuid _arg6;
            int _arg7;
            ParcelUuid _arg8;
            int _arg12;
            ParcelUuid _arg32;
            ParcelUuid _arg13;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    List<BluetoothDevice> _result = getDevicesMatchingConnectionStates(data.createIntArray());
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 2:
                    ScanSettings _arg22;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    boolean _arg14 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg22 = (ScanSettings) ScanSettings.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    startScan(_arg0, _arg14, _arg22, data.createTypedArrayList(ScanFilter.CREATOR), data.readArrayList(getClass().getClassLoader()), data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    stopScan(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    flushPendingBatchResults(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 5:
                    AdvertiseData _arg15;
                    AdvertiseData _arg23;
                    AdvertiseSettings _arg33;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg15 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg23 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg33 = (AdvertiseSettings) AdvertiseSettings.CREATOR.createFromParcel(data);
                    } else {
                        _arg33 = null;
                    }
                    startMultiAdvertising(_arg0, _arg15, _arg23, _arg33);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    stopMultiAdvertising(data.readInt());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    registerClient(_arg02, android.bluetooth.IBluetoothGattCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterClient(data.readInt());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    unregisterClientbyUuid(_arg02);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    clientConnect(data.readInt(), data.readString(), data.readInt() != 0, data.readInt(), data.readInt() != 0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    clientDisconnect(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    refreshDevice(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    discoverServices(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readInt();
                    _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    readCharacteristic(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, data.readInt());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readInt();
                    _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    writeCharacteristic(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, data.readInt(), data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readInt();
                    _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
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
                    readDescriptor(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, data.readInt());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readInt();
                    _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
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
                    writeDescriptor(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, data.readInt(), data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readInt();
                    _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    registerForNotification(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    beginReliableWrite(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    endReliableWrite(data.readInt(), data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    readRemoteRssi(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    configureMTU(data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    connectionParameterUpdate(data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    registerServer(_arg02, android.bluetooth.IBluetoothGattServerCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterServer(data.readInt());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    serverConnect(data.readInt(), data.readString(), data.readInt() != 0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    serverDisconnect(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg12 = data.readInt();
                    _arg2 = data.readInt();
                    _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    beginServiceDeclaration(_arg0, _arg12, _arg2, _arg3, _arg4, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg12 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg32 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    addIncludedService(_arg0, _arg12, _arg2, _arg32);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    addCharacteristic(_arg0, _arg13, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    addDescriptor(_arg0, _arg13, data.readInt());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    endServiceDeclaration(data.readInt());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg12 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg32 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    removeService(_arg0, _arg12, _arg2, _arg32);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    clearServices(data.readInt());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    sendResponse(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readInt();
                    _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg6 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendNotification(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, data.readInt() != 0, data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    monitorRssi(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    serverConnectionParameterUpdate(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    clientConnectionParameterUpdate(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    serverDisconnectIncomingConnClients();
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    serverDataRateUpdate(data.readInt(), data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    clientDataRateUpdate(data.readInt(), data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    disconnectAll();
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    unregAll();
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = numHwTrackFiltersAvailable();
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

    void addCharacteristic(int i, ParcelUuid parcelUuid, int i2, int i3) throws RemoteException;

    void addDescriptor(int i, ParcelUuid parcelUuid, int i2) throws RemoteException;

    void addIncludedService(int i, int i2, int i3, ParcelUuid parcelUuid) throws RemoteException;

    void beginReliableWrite(int i, String str) throws RemoteException;

    void beginServiceDeclaration(int i, int i2, int i3, int i4, ParcelUuid parcelUuid, boolean z) throws RemoteException;

    void clearServices(int i) throws RemoteException;

    void clientConnect(int i, String str, boolean z, int i2, boolean z2, int i3) throws RemoteException;

    void clientConnectionParameterUpdate(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7) throws RemoteException;

    void clientDataRateUpdate(int i, String str, int i2, int i3) throws RemoteException;

    void clientDisconnect(int i, String str) throws RemoteException;

    void configureMTU(int i, String str, int i2) throws RemoteException;

    void connectionParameterUpdate(int i, String str, int i2) throws RemoteException;

    void disconnectAll() throws RemoteException;

    void discoverServices(int i, String str) throws RemoteException;

    void endReliableWrite(int i, String str, boolean z) throws RemoteException;

    void endServiceDeclaration(int i) throws RemoteException;

    void flushPendingBatchResults(int i, boolean z) throws RemoteException;

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;

    void monitorRssi(int i, String str, int i2, int i3, int i4) throws RemoteException;

    int numHwTrackFiltersAvailable() throws RemoteException;

    void readCharacteristic(int i, String str, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, int i5) throws RemoteException;

    void readDescriptor(int i, String str, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, int i5, ParcelUuid parcelUuid3, int i6) throws RemoteException;

    void readRemoteRssi(int i, String str) throws RemoteException;

    void refreshDevice(int i, String str) throws RemoteException;

    void registerClient(ParcelUuid parcelUuid, IBluetoothGattCallback iBluetoothGattCallback) throws RemoteException;

    void registerForNotification(int i, String str, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, boolean z) throws RemoteException;

    void registerServer(ParcelUuid parcelUuid, IBluetoothGattServerCallback iBluetoothGattServerCallback) throws RemoteException;

    void removeService(int i, int i2, int i3, ParcelUuid parcelUuid) throws RemoteException;

    void sendNotification(int i, String str, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, boolean z, byte[] bArr) throws RemoteException;

    void sendResponse(int i, String str, int i2, int i3, int i4, byte[] bArr) throws RemoteException;

    void serverConnect(int i, String str, boolean z, int i2) throws RemoteException;

    void serverConnectionParameterUpdate(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7) throws RemoteException;

    void serverDataRateUpdate(int i, String str, int i2, int i3) throws RemoteException;

    void serverDisconnect(int i, String str) throws RemoteException;

    void serverDisconnectIncomingConnClients() throws RemoteException;

    void startMultiAdvertising(int i, AdvertiseData advertiseData, AdvertiseData advertiseData2, AdvertiseSettings advertiseSettings) throws RemoteException;

    void startScan(int i, boolean z, ScanSettings scanSettings, List<ScanFilter> list, List list2, String str) throws RemoteException;

    void stopMultiAdvertising(int i) throws RemoteException;

    void stopScan(int i, boolean z) throws RemoteException;

    void unregAll() throws RemoteException;

    void unregisterClient(int i) throws RemoteException;

    void unregisterClientbyUuid(ParcelUuid parcelUuid) throws RemoteException;

    void unregisterServer(int i) throws RemoteException;

    void writeCharacteristic(int i, String str, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, int i5, int i6, byte[] bArr) throws RemoteException;

    void writeDescriptor(int i, String str, int i2, int i3, ParcelUuid parcelUuid, int i4, ParcelUuid parcelUuid2, int i5, ParcelUuid parcelUuid3, int i6, int i7, byte[] bArr) throws RemoteException;
}
