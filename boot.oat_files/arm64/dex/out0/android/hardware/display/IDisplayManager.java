package android.hardware.display;

import android.media.projection.IMediaProjection;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.DisplayInfo;
import android.view.MagnificationSpec;
import android.view.Surface;

public interface IDisplayManager extends IInterface {

    public static abstract class Stub extends Binder implements IDisplayManager {
        private static final String DESCRIPTOR = "android.hardware.display.IDisplayManager";
        static final int TRANSACTION_connectWifiDisplay = 6;
        static final int TRANSACTION_connectWifiDisplayWithMode = 23;
        static final int TRANSACTION_connectWifiDisplayWithModeEx = 24;
        static final int TRANSACTION_connectWifiDisplayWithPin = 22;
        static final int TRANSACTION_createVirtualDisplay = 14;
        static final int TRANSACTION_disableWifiDisplay = 28;
        static final int TRANSACTION_disconnectForMirroringSwitching = 44;
        static final int TRANSACTION_disconnectWifiDisplay = 7;
        static final int TRANSACTION_enableOverlayMagnifier = 52;
        static final int TRANSACTION_enableWifiDisplay = 25;
        static final int TRANSACTION_enableWifiDisplayEx = 26;
        static final int TRANSACTION_enableWifiDisplayEx2 = 27;
        static final int TRANSACTION_forgetWifiDisplay = 9;
        static final int TRANSACTION_getActiveDLNADevice = 49;
        static final int TRANSACTION_getActiveDLNAState = 50;
        static final int TRANSACTION_getDisplayIdOfDevice = 57;
        static final int TRANSACTION_getDisplayIds = 2;
        static final int TRANSACTION_getDisplayInfo = 1;
        static final int TRANSACTION_getDisplayInfoEx = 56;
        static final int TRANSACTION_getLastConnectedDLNADevice = 45;
        static final int TRANSACTION_getLastConnectedDisplay = 43;
        static final int TRANSACTION_getScreenSharingStatus = 47;
        static final int TRANSACTION_getWifiDisplayBridgeStatus = 31;
        static final int TRANSACTION_getWifiDisplayStatus = 12;
        static final int TRANSACTION_hasContent = 51;
        static final int TRANSACTION_isConnWithPinSupported = 38;
        static final int TRANSACTION_isDongleRenameAvailable = 39;
        static final int TRANSACTION_isKDDIServiceConnected = 33;
        static final int TRANSACTION_isSinkAvailable = 36;
        static final int TRANSACTION_isSourceAvailable = 37;
        static final int TRANSACTION_isWfdEngineRunning = 32;
        static final int TRANSACTION_isWifiDisplayBridgeAvailable = 30;
        static final int TRANSACTION_notifyEnterHomeSyncApp = 34;
        static final int TRANSACTION_notifyExitHomeSyncApp = 35;
        static final int TRANSACTION_pauseWifiDisplay = 10;
        static final int TRANSACTION_registerCallback = 3;
        static final int TRANSACTION_releaseVirtualDisplay = 19;
        static final int TRANSACTION_removeLastConnectedDLNADevice = 46;
        static final int TRANSACTION_renameDongle = 40;
        static final int TRANSACTION_renameWifiDisplay = 8;
        static final int TRANSACTION_requestColorTransform = 13;
        static final int TRANSACTION_resizeVirtualDisplay = 15;
        static final int TRANSACTION_restartWifiDisplay = 29;
        static final int TRANSACTION_resumeWifiDisplay = 11;
        static final int TRANSACTION_scanWifiDisplays = 20;
        static final int TRANSACTION_setCurrentDisplayIdMagnifier = 55;
        static final int TRANSACTION_setMagnificationSettings = 54;
        static final int TRANSACTION_setMagnificationSpec = 53;
        static final int TRANSACTION_setParameter = 42;
        static final int TRANSACTION_setScanningChannel = 41;
        static final int TRANSACTION_setScreenSharingStatus = 48;
        static final int TRANSACTION_setVirtualDisplayFixedOrientation = 17;
        static final int TRANSACTION_setVirtualDisplayMirroringDisplay = 16;
        static final int TRANSACTION_setVirtualDisplaySurface = 18;
        static final int TRANSACTION_startWifiDisplayScan = 4;
        static final int TRANSACTION_stopScanWifiDisplays = 21;
        static final int TRANSACTION_stopWifiDisplayScan = 5;

        private static class Proxy implements IDisplayManager {
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

            public DisplayInfo getDisplayInfo(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DisplayInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DisplayInfo) DisplayInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getDisplayIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCallback(IDisplayManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startWifiDisplayScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWifiDisplayScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectWifiDisplay(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnectWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void renameWifiDisplay(String address, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(alias);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forgetWifiDisplay(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pauseWifiDisplay() throws RemoteException {
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

            public void resumeWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiDisplayStatus getWifiDisplayStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiDisplayStatus _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiDisplayStatus) WifiDisplayStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestColorTransform(int displayId, int colorTransformId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(colorTransformId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createVirtualDisplay(IVirtualDisplayCallback callback, IMediaProjection projectionToken, String packageName, String name, int width, int height, int densityDpi, Surface surface, int flags) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    IBinder asBinder;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        asBinder = callback.asBinder();
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    if (projectionToken != null) {
                        iBinder = projectionToken.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeString(packageName);
                    _data.writeString(name);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(densityDpi);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resizeVirtualDisplay(IVirtualDisplayCallback token, int width, int height, int densityDpi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(densityDpi);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVirtualDisplayMirroringDisplay(IVirtualDisplayCallback token, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(displayId);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVirtualDisplayFixedOrientation(IVirtualDisplayCallback token, int orientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(orientation);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVirtualDisplaySurface(IVirtualDisplayCallback token, Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseVirtualDisplay(IVirtualDisplayCallback token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void scanWifiDisplays() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopScanWifiDisplays() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectWifiDisplayWithPin(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectWifiDisplayWithMode(int connectingMode, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(connectingMode);
                    _data.writeString(address);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectWifiDisplayWithModeEx(int connectingMode, String deviceAddress, boolean isPendingRequest) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(connectingMode);
                    _data.writeString(deviceAddress);
                    if (isPendingRequest) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableWifiDisplay(WifiP2pDevice sinkDevice, int deviceType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sinkDevice != null) {
                        _data.writeInt(1);
                        sinkDevice.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(deviceType);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableWifiDisplayEx(String ipAddr, String port, int deviceType, String options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ipAddr);
                    _data.writeString(port);
                    _data.writeInt(deviceType);
                    _data.writeString(options);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableWifiDisplayEx2(String ipAddr, String port, int deviceType, String options, String deviceName, String remoteP2pMacAddr, boolean isPendingRequest) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ipAddr);
                    _data.writeString(port);
                    _data.writeInt(deviceType);
                    _data.writeString(options);
                    _data.writeString(deviceName);
                    _data.writeString(remoteP2pMacAddr);
                    if (isPendingRequest) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restartWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWifiDisplayBridgeAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
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

            public int getWifiDisplayBridgeStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWfdEngineRunning() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
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

            public boolean isKDDIServiceConnected() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
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

            public void notifyEnterHomeSyncApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyExitHomeSyncApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSinkAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
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

            public boolean isSourceAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
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

            public boolean isConnWithPinSupported(String address) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(38, _data, _reply, 0);
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

            public boolean isDongleRenameAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
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

            public void renameDongle(String deviceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(deviceName);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setScanningChannel(int requestedChannel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestedChannel);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setParameter(int type, int param1, String param2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(param1);
                    _data.writeString(param2);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiDisplay getLastConnectedDisplay(boolean cancel) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiDisplay _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cancel) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiDisplay) WifiDisplay.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnectForMirroringSwitching() throws RemoteException {
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

            public DLNADevice getLastConnectedDLNADevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DLNADevice _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DLNADevice) DLNADevice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeLastConnectedDLNADevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getScreenSharingStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setScreenSharingStatus(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public DLNADevice getActiveDLNADevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DLNADevice _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DLNADevice) DLNADevice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActiveDLNAState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasContent(int displayId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(51, _data, _reply, 0);
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

            public void enableOverlayMagnifier(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMagnificationSpec(MagnificationSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (spec != null) {
                        _data.writeInt(1);
                        spec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMagnificationSettings(int width, int height, float scale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeFloat(scale);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCurrentDisplayIdMagnifier(int displayid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayid);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public DisplayInfo getDisplayInfoEx(int displayId, IBinder appToken, boolean isSelectiveOrientationState) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DisplayInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(appToken);
                    if (isSelectiveOrientationState) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DisplayInfo) DisplayInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDisplayIdOfDevice(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    this.mRemote.transact(57, _data, _reply, 0);
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

        public static IDisplayManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDisplayManager)) {
                return new Proxy(obj);
            }
            return (IDisplayManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DisplayInfo _result;
            IVirtualDisplayCallback _arg0;
            int _result2;
            boolean _result3;
            DLNADevice _result4;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDisplayInfo(data.readInt());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result5 = getDisplayIds();
                    reply.writeNoException();
                    reply.writeIntArray(_result5);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    registerCallback(android.hardware.display.IDisplayManagerCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    startWifiDisplayScan();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    stopWifiDisplayScan();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    connectWifiDisplay(data.readString());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    disconnectWifiDisplay();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    renameWifiDisplay(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    forgetWifiDisplay(data.readString());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    pauseWifiDisplay();
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    resumeWifiDisplay();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    WifiDisplayStatus _result6 = getWifiDisplayStatus();
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    requestColorTransform(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 14:
                    Surface _arg7;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                    IMediaProjection _arg1 = android.media.projection.IMediaProjection.Stub.asInterface(data.readStrongBinder());
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg7 = (Surface) Surface.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    _result2 = createVirtualDisplay(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    resizeVirtualDisplay(android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    setVirtualDisplayMirroringDisplay(android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    setVirtualDisplayFixedOrientation(android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 18:
                    Surface _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg12 = (Surface) Surface.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    setVirtualDisplaySurface(_arg0, _arg12);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    releaseVirtualDisplay(android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    scanWifiDisplays();
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    stopScanWifiDisplays();
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    connectWifiDisplayWithPin(data.readString());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    connectWifiDisplayWithMode(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    connectWifiDisplayWithModeEx(data.readInt(), data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 25:
                    WifiP2pDevice _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (WifiP2pDevice) WifiP2pDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    enableWifiDisplay(_arg02, data.readInt());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    enableWifiDisplayEx(data.readString(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    enableWifiDisplayEx2(data.readString(), data.readString(), data.readInt(), data.readString(), data.readString(), data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    disableWifiDisplay();
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    restartWifiDisplay();
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isWifiDisplayBridgeAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getWifiDisplayBridgeStatus();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isWfdEngineRunning();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isKDDIServiceConnected();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    notifyEnterHomeSyncApp();
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    notifyExitHomeSyncApp();
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isSinkAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isSourceAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isConnWithPinSupported(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isDongleRenameAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    renameDongle(data.readString());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    setScanningChannel(data.readInt());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setParameter(data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    WifiDisplay _result7 = getLastConnectedDisplay(data.readInt() != 0);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    disconnectForMirroringSwitching();
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getLastConnectedDLNADevice();
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    removeLastConnectedDLNADevice();
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getScreenSharingStatus();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    setScreenSharingStatus(data.readInt());
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getActiveDLNADevice();
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getActiveDLNAState();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = hasContent(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    enableOverlayMagnifier(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 53:
                    MagnificationSpec _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (MagnificationSpec) MagnificationSpec.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    setMagnificationSpec(_arg03);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    setMagnificationSettings(data.readInt(), data.readInt(), data.readFloat());
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    setCurrentDisplayIdMagnifier(data.readInt());
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDisplayInfoEx(data.readInt(), data.readStrongBinder(), data.readInt() != 0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getDisplayIdOfDevice(data.readInt());
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

    void connectWifiDisplay(String str) throws RemoteException;

    void connectWifiDisplayWithMode(int i, String str) throws RemoteException;

    void connectWifiDisplayWithModeEx(int i, String str, boolean z) throws RemoteException;

    void connectWifiDisplayWithPin(String str) throws RemoteException;

    int createVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, IMediaProjection iMediaProjection, String str, String str2, int i, int i2, int i3, Surface surface, int i4) throws RemoteException;

    void disableWifiDisplay() throws RemoteException;

    void disconnectForMirroringSwitching() throws RemoteException;

    void disconnectWifiDisplay() throws RemoteException;

    void enableOverlayMagnifier(boolean z) throws RemoteException;

    void enableWifiDisplay(WifiP2pDevice wifiP2pDevice, int i) throws RemoteException;

    void enableWifiDisplayEx(String str, String str2, int i, String str3) throws RemoteException;

    void enableWifiDisplayEx2(String str, String str2, int i, String str3, String str4, String str5, boolean z) throws RemoteException;

    void forgetWifiDisplay(String str) throws RemoteException;

    DLNADevice getActiveDLNADevice() throws RemoteException;

    int getActiveDLNAState() throws RemoteException;

    int getDisplayIdOfDevice(int i) throws RemoteException;

    int[] getDisplayIds() throws RemoteException;

    DisplayInfo getDisplayInfo(int i) throws RemoteException;

    DisplayInfo getDisplayInfoEx(int i, IBinder iBinder, boolean z) throws RemoteException;

    DLNADevice getLastConnectedDLNADevice() throws RemoteException;

    WifiDisplay getLastConnectedDisplay(boolean z) throws RemoteException;

    int getScreenSharingStatus() throws RemoteException;

    int getWifiDisplayBridgeStatus() throws RemoteException;

    WifiDisplayStatus getWifiDisplayStatus() throws RemoteException;

    boolean hasContent(int i) throws RemoteException;

    boolean isConnWithPinSupported(String str) throws RemoteException;

    boolean isDongleRenameAvailable() throws RemoteException;

    boolean isKDDIServiceConnected() throws RemoteException;

    boolean isSinkAvailable() throws RemoteException;

    boolean isSourceAvailable() throws RemoteException;

    boolean isWfdEngineRunning() throws RemoteException;

    boolean isWifiDisplayBridgeAvailable() throws RemoteException;

    void notifyEnterHomeSyncApp() throws RemoteException;

    void notifyExitHomeSyncApp() throws RemoteException;

    void pauseWifiDisplay() throws RemoteException;

    void registerCallback(IDisplayManagerCallback iDisplayManagerCallback) throws RemoteException;

    void releaseVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback) throws RemoteException;

    void removeLastConnectedDLNADevice() throws RemoteException;

    void renameDongle(String str) throws RemoteException;

    void renameWifiDisplay(String str, String str2) throws RemoteException;

    void requestColorTransform(int i, int i2) throws RemoteException;

    void resizeVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, int i, int i2, int i3) throws RemoteException;

    void restartWifiDisplay() throws RemoteException;

    void resumeWifiDisplay() throws RemoteException;

    void scanWifiDisplays() throws RemoteException;

    void setCurrentDisplayIdMagnifier(int i) throws RemoteException;

    void setMagnificationSettings(int i, int i2, float f) throws RemoteException;

    void setMagnificationSpec(MagnificationSpec magnificationSpec) throws RemoteException;

    int setParameter(int i, int i2, String str) throws RemoteException;

    void setScanningChannel(int i) throws RemoteException;

    void setScreenSharingStatus(int i) throws RemoteException;

    void setVirtualDisplayFixedOrientation(IVirtualDisplayCallback iVirtualDisplayCallback, int i) throws RemoteException;

    void setVirtualDisplayMirroringDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, int i) throws RemoteException;

    void setVirtualDisplaySurface(IVirtualDisplayCallback iVirtualDisplayCallback, Surface surface) throws RemoteException;

    void startWifiDisplayScan() throws RemoteException;

    void stopScanWifiDisplays() throws RemoteException;

    void stopWifiDisplayScan() throws RemoteException;
}
