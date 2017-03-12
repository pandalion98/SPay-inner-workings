package android.net.wifi;

import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.hs20.WifiHs20OsuProvider;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;
import java.util.List;

public interface IWifiManager extends IInterface {

    public static abstract class Stub extends Binder implements IWifiManager {
        private static final String DESCRIPTOR = "android.net.wifi.IWifiManager";
        static final int TRANSACTION_acquireMulticastLock = 34;
        static final int TRANSACTION_acquireWifiLock = 29;
        static final int TRANSACTION_addOrUpdateNetwork = 6;
        static final int TRANSACTION_addToBlacklist = 43;
        static final int TRANSACTION_addToSBlacklist = 45;
        static final int TRANSACTION_addWifiApData = 89;
        static final int TRANSACTION_buildWifiConfig = 39;
        static final int TRANSACTION_callSECApi = 72;
        static final int TRANSACTION_callSECStringApi = 73;
        static final int TRANSACTION_checkWarningPopup = 107;
        static final int TRANSACTION_clearBlacklist = 44;
        static final int TRANSACTION_clearSBlacklist = 46;
        static final int TRANSACTION_disableEphemeralNetwork = 69;
        static final int TRANSACTION_disableNetwork = 9;
        static final int TRANSACTION_disconnect = 15;
        static final int TRANSACTION_enableAggressiveHandover = 60;
        static final int TRANSACTION_enableAutoJoinWhenAssociated = 66;
        static final int TRANSACTION_enableNetwork = 8;
        static final int TRANSACTION_enableTdls = 50;
        static final int TRANSACTION_enableTdlsWithMacAddress = 51;
        static final int TRANSACTION_enableVerboseLogging = 58;
        static final int TRANSACTION_enableWifiSharing = 118;
        static final int TRANSACTION_factoryReset = 70;
        static final int TRANSACTION_fetchHs20OsuProviders = 112;
        static final int TRANSACTION_getAggressiveHandover = 61;
        static final int TRANSACTION_getAllowScansWithTraffic = 63;
        static final int TRANSACTION_getBatchedScanResults = 54;
        static final int TRANSACTION_getChannelList = 11;
        static final int TRANSACTION_getConfigFile = 49;
        static final int TRANSACTION_getConfiguredNetworks = 3;
        static final int TRANSACTION_getConnectionInfo = 18;
        static final int TRANSACTION_getConnectionStatistics = 68;
        static final int TRANSACTION_getCountryCode = 22;
        static final int TRANSACTION_getCountryRev = 101;
        static final int TRANSACTION_getCurrentNetwork = 71;
        static final int TRANSACTION_getDhcpInfo = 27;
        static final int TRANSACTION_getEnableAutoJoinWhenAssociated = 67;
        static final int TRANSACTION_getFrequencyBand = 24;
        static final int TRANSACTION_getFullRoamScanPeriod = 97;
        static final int TRANSACTION_getHalBasedAutojoinOffload = 65;
        static final int TRANSACTION_getHs20OsuProviders = 110;
        static final int TRANSACTION_getLinkStatus = 102;
        static final int TRANSACTION_getMatchingWifiConfig = 5;
        static final int TRANSACTION_getNetworkInfo = 106;
        static final int TRANSACTION_getPPPOEInfo = 77;
        static final int TRANSACTION_getPrivilegedConfiguredNetworks = 4;
        static final int TRANSACTION_getProvisionSuccess = 120;
        static final int TRANSACTION_getRoamBand = 99;
        static final int TRANSACTION_getRoamDelta = 93;
        static final int TRANSACTION_getRoamScanPeriod = 95;
        static final int TRANSACTION_getRoamTrigger = 91;
        static final int TRANSACTION_getSBlacklist = 47;
        static final int TRANSACTION_getScanResults = 14;
        static final int TRANSACTION_getSpecificNetwork = 74;
        static final int TRANSACTION_getSupportedFeatures = 1;
        static final int TRANSACTION_getVerboseLoggingLevel = 59;
        static final int TRANSACTION_getWifiApChameleonSsid = 79;
        static final int TRANSACTION_getWifiApConfigTxPower = 80;
        static final int TRANSACTION_getWifiApConfiguration = 38;
        static final int TRANSACTION_getWifiApEnabledState = 37;
        static final int TRANSACTION_getWifiApLimitDataFromDb = 87;
        static final int TRANSACTION_getWifiApRemainDataFromDb = 88;
        static final int TRANSACTION_getWifiApStaList = 78;
        static final int TRANSACTION_getWifiApTimeOut = 84;
        static final int TRANSACTION_getWifiEnabledState = 20;
        static final int TRANSACTION_getWifiIBSSEnabledState = 105;
        static final int TRANSACTION_getWifiServiceMessenger = 48;
        static final int TRANSACTION_getWifiVerName = 121;
        static final int TRANSACTION_getWpsNfcConfigurationToken = 57;
        static final int TRANSACTION_initializeMulticastFiltering = 32;
        static final int TRANSACTION_isBatchedScanSupported = 55;
        static final int TRANSACTION_isCaptivePortalException = 116;
        static final int TRANSACTION_isDetectedAsMaliciousHotspot = 113;
        static final int TRANSACTION_isDualBandSupported = 25;
        static final int TRANSACTION_isHs20OsuProviderAvailable = 111;
        static final int TRANSACTION_isMulticastEnabled = 33;
        static final int TRANSACTION_isPasspointDefaultOn = 114;
        static final int TRANSACTION_isPasspointMenuVisible = 115;
        static final int TRANSACTION_isScanAlwaysAvailable = 28;
        static final int TRANSACTION_isWifiApDbContain = 85;
        static final int TRANSACTION_isWifiApListContain = 86;
        static final int TRANSACTION_isWifiSharingEnabled = 117;
        static final int TRANSACTION_pingSupplicant = 10;
        static final int TRANSACTION_pollBatchedScan = 56;
        static final int TRANSACTION_reassociate = 17;
        static final int TRANSACTION_reconnect = 16;
        static final int TRANSACTION_releaseMulticastLock = 35;
        static final int TRANSACTION_releaseWifiLock = 31;
        static final int TRANSACTION_removeNetwork = 7;
        static final int TRANSACTION_removeNetworkByMDM = 108;
        static final int TRANSACTION_reportActivityInfo = 2;
        static final int TRANSACTION_requestBatchedScan = 52;
        static final int TRANSACTION_saveConfiguration = 26;
        static final int TRANSACTION_saveNetworkByMDM = 109;
        static final int TRANSACTION_setAllowScansWithTraffic = 62;
        static final int TRANSACTION_setCountryCode = 21;
        static final int TRANSACTION_setCountryRev = 100;
        static final int TRANSACTION_setFrequencyBand = 23;
        static final int TRANSACTION_setFullRoamScanPeriod = 96;
        static final int TRANSACTION_setHalBasedAutojoinOffload = 64;
        static final int TRANSACTION_setIsFmcNetwork = 103;
        static final int TRANSACTION_setProvisionSuccess = 119;
        static final int TRANSACTION_setRoamBand = 98;
        static final int TRANSACTION_setRoamDelta = 92;
        static final int TRANSACTION_setRoamScanPeriod = 94;
        static final int TRANSACTION_setRoamTrigger = 90;
        static final int TRANSACTION_setWifiApConfigTxPower = 81;
        static final int TRANSACTION_setWifiApConfiguration = 40;
        static final int TRANSACTION_setWifiApConfigurationToDefault = 82;
        static final int TRANSACTION_setWifiApEnabled = 36;
        static final int TRANSACTION_setWifiApTimeOut = 83;
        static final int TRANSACTION_setWifiEnabled = 19;
        static final int TRANSACTION_setWifiIBSSEnabled = 104;
        static final int TRANSACTION_startLocationRestrictedScan = 13;
        static final int TRANSACTION_startPPPOE = 75;
        static final int TRANSACTION_startScan = 12;
        static final int TRANSACTION_startWifi = 41;
        static final int TRANSACTION_stopBatchedScan = 53;
        static final int TRANSACTION_stopPPPOE = 76;
        static final int TRANSACTION_stopWifi = 42;
        static final int TRANSACTION_updateWifiLockWorkSource = 30;

        private static class Proxy implements IWifiManager {
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

            public int getSupportedFeatures() throws RemoteException {
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

            public WifiActivityEnergyInfo reportActivityInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiActivityEnergyInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiActivityEnergyInfo) WifiActivityEnergyInfo.CREATOR.createFromParcel(_reply);
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

            public List<WifiConfiguration> getConfiguredNetworks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    List<WifiConfiguration> _result = _reply.createTypedArrayList(WifiConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<WifiConfiguration> getPrivilegedConfiguredNetworks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<WifiConfiguration> _result = _reply.createTypedArrayList(WifiConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiConfiguration getMatchingWifiConfig(ScanResult scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiConfiguration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(_reply);
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

            public int addOrUpdateNetwork(WifiConfiguration config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeNetwork(int netId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(7, _data, _reply, 0);
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

            public boolean enableNetwork(int netId, boolean disableOthers) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (disableOthers) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public boolean disableNetwork(int netId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(9, _data, _reply, 0);
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

            public boolean pingSupplicant() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
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

            public List<WifiChannel> getChannelList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<WifiChannel> _result = _reply.createTypedArrayList(WifiChannel.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startScan(ScanSettings requested, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (requested != null) {
                        _data.writeInt(1);
                        requested.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startLocationRestrictedScan(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ScanResult> getScanResults(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    List<ScanResult> _result = _reply.createTypedArrayList(ScanResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reconnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reassociate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiInfo getConnectionInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiInfo) WifiInfo.CREATOR.createFromParcel(_reply);
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

            public boolean setWifiEnabled(boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(19, _data, _reply, 0);
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

            public int getWifiEnabledState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCountryCode(String country, boolean persist) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(country);
                    if (persist) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCountryCode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFrequencyBand(int band, boolean persist) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(band);
                    if (persist) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFrequencyBand() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDualBandSupported() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
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

            public boolean saveConfiguration() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
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

            public DhcpInfo getDhcpInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DhcpInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DhcpInfo) DhcpInfo.CREATOR.createFromParcel(_reply);
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

            public boolean isScanAlwaysAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
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

            public boolean acquireWifiLock(IBinder lock, int lockType, String tag, WorkSource ws) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    _data.writeInt(lockType);
                    _data.writeString(tag);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
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

            public void updateWifiLockWorkSource(IBinder lock, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean releaseWifiLock(IBinder lock) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    this.mRemote.transact(31, _data, _reply, 0);
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

            public void initializeMulticastFiltering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMulticastEnabled() throws RemoteException {
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

            public void acquireMulticastLock(IBinder binder, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(tag);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseMulticastLock() throws RemoteException {
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

            public void setWifiApEnabled(WifiConfiguration wifiConfig, boolean enable) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (wifiConfig != null) {
                        _data.writeInt(1);
                        wifiConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enable) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWifiApEnabledState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiConfiguration getWifiApConfiguration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiConfiguration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(_reply);
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

            public WifiConfiguration buildWifiConfig(String uriString, String mimeType, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiConfiguration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriString);
                    _data.writeString(mimeType);
                    _data.writeByteArray(data);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(_reply);
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

            public void setWifiApConfiguration(WifiConfiguration wifiConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (wifiConfig != null) {
                        _data.writeInt(1);
                        wifiConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startWifi() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWifi() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addToBlacklist(String bssid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(bssid);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearBlacklist() throws RemoteException {
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

            public void addToSBlacklist(String bssid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(bssid);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearSBlacklist() throws RemoteException {
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

            public String getSBlacklist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Messenger getWifiServiceMessenger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Messenger _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Messenger) Messenger.CREATOR.createFromParcel(_reply);
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

            public String getConfigFile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableTdls(String remoteIPAddress, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(remoteIPAddress);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableTdlsWithMacAddress(String remoteMacAddress, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(remoteMacAddress);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestBatchedScan(BatchedScanSettings requested, IBinder binder, WorkSource ws) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (requested != null) {
                        _data.writeInt(1);
                        requested.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(52, _data, _reply, 0);
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

            public void stopBatchedScan(BatchedScanSettings requested) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (requested != null) {
                        _data.writeInt(1);
                        requested.writeToParcel(_data, 0);
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

            public List<BatchedScanResult> getBatchedScanResults(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    List<BatchedScanResult> _result = _reply.createTypedArrayList(BatchedScanResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBatchedScanSupported() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(55, _data, _reply, 0);
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

            public void pollBatchedScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getWpsNfcConfigurationToken(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableVerboseLogging(int verbose) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(verbose);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVerboseLoggingLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableAggressiveHandover(int enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAggressiveHandover() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAllowScansWithTraffic(int enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAllowScansWithTraffic() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHalBasedAutojoinOffload(int enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getHalBasedAutojoinOffload() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableAutoJoinWhenAssociated(boolean enabled) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(66, _data, _reply, 0);
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

            public boolean getEnableAutoJoinWhenAssociated() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(67, _data, _reply, 0);
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

            public WifiConnectionStatistics getConnectionStatistics() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiConnectionStatistics _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiConnectionStatistics) WifiConnectionStatistics.CREATOR.createFromParcel(_reply);
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

            public void disableEphemeralNetwork(String SSID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(SSID);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Network getCurrentNetwork() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Network _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Network) Network.CREATOR.createFromParcel(_reply);
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

            public int callSECApi(Message msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (msg != null) {
                        _data.writeInt(1);
                        msg.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String callSECStringApi(Message msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (msg != null) {
                        _data.writeInt(1);
                        msg.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiConfiguration getSpecificNetwork(int netID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WifiConfiguration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netID);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(_reply);
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

            public void startPPPOE(PPPOEConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopPPPOE() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PPPOEInfo getPPPOEInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PPPOEInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PPPOEInfo) PPPOEInfo.CREATOR.createFromParcel(_reply);
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

            public String getWifiApStaList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getWifiApChameleonSsid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWifiApConfigTxPower() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiApConfigTxPower(int txPowerMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(txPowerMode);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiApConfigurationToDefault() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiApTimeOut(int sec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sec);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWifiApTimeOut() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWifiApDbContain(String mac) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mac);
                    this.mRemote.transact(85, _data, _reply, 0);
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

            public boolean isWifiApListContain(String mac) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mac);
                    this.mRemote.transact(86, _data, _reply, 0);
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

            public String getWifiApLimitDataFromDb(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mac);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getWifiApRemainDataFromDb(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mac);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addWifiApData(String mac, String ip, String limit) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mac);
                    _data.writeString(ip);
                    _data.writeString(limit);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRoamTrigger(int roamTrigger) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(roamTrigger);
                    this.mRemote.transact(90, _data, _reply, 0);
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

            public int getRoamTrigger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRoamDelta(int roamDelta) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(roamDelta);
                    this.mRemote.transact(92, _data, _reply, 0);
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

            public int getRoamDelta() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRoamScanPeriod(int roamScanPeriod) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(roamScanPeriod);
                    this.mRemote.transact(94, _data, _reply, 0);
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

            public int getRoamScanPeriod() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setFullRoamScanPeriod(int fullRoamScanPeriod) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fullRoamScanPeriod);
                    this.mRemote.transact(96, _data, _reply, 0);
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

            public int getFullRoamScanPeriod() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRoamBand(int roamBand) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(roamBand);
                    this.mRemote.transact(98, _data, _reply, 0);
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

            public int getRoamBand() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setCountryRev(String countryRev) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(countryRev);
                    this.mRemote.transact(100, _data, _reply, 0);
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

            public String getCountryRev() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLinkStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setIsFmcNetwork(boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(103, _data, _reply, 0);
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

            public boolean setWifiIBSSEnabled(boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(104, _data, _reply, 0);
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

            public int getWifiIBSSEnabledState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkInfo getNetworkInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(_reply);
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

            public boolean checkWarningPopup() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(107, _data, _reply, 0);
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

            public boolean removeNetworkByMDM(int netId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(108, _data, _reply, 0);
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

            public boolean saveNetworkByMDM(WifiConfiguration config) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(109, _data, _reply, 0);
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

            public List<WifiHs20OsuProvider> getHs20OsuProviders() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    List<WifiHs20OsuProvider> _result = _reply.createTypedArrayList(WifiHs20OsuProvider.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isHs20OsuProviderAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(111, _data, _reply, 0);
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

            public boolean fetchHs20OsuProviders() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(112, _data, _reply, 0);
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

            public boolean isDetectedAsMaliciousHotspot(String bssid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(bssid);
                    this.mRemote.transact(113, _data, _reply, 0);
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

            public boolean isPasspointDefaultOn() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(114, _data, _reply, 0);
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

            public boolean isPasspointMenuVisible() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(115, _data, _reply, 0);
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

            public boolean isCaptivePortalException() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(116, _data, _reply, 0);
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

            public boolean isWifiSharingEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(117, _data, _reply, 0);
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

            public boolean enableWifiSharing(boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(118, _data, _reply, 0);
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

            public boolean setProvisionSuccess(boolean set) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (set) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(119, _data, _reply, 0);
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

            public boolean getProvisionSuccess() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(120, _data, _reply, 0);
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

            public String getWifiVerName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
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

        public static IWifiManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWifiManager)) {
                return new Proxy(obj);
            }
            return (IWifiManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            List<WifiConfiguration> _result2;
            WifiConfiguration _result3;
            WifiConfiguration _arg0;
            boolean _result4;
            WorkSource _arg1;
            String _result5;
            IBinder _arg02;
            BatchedScanSettings _arg03;
            Message _arg04;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSupportedFeatures();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    WifiActivityEnergyInfo _result6 = reportActivityInfo();
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getConfiguredNetworks();
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPrivilegedConfiguredNetworks();
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 5:
                    ScanResult _arg05;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (ScanResult) ScanResult.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    _result3 = getMatchingWifiConfig(_arg05);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = addOrUpdateNetwork(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = removeNetwork(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = enableNetwork(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = disableNetwork(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = pingSupplicant();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    List<WifiChannel> _result7 = getChannelList();
                    reply.writeNoException();
                    reply.writeTypedList(_result7);
                    return true;
                case 12:
                    ScanSettings _arg06;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (ScanSettings) ScanSettings.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = (WorkSource) WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    startScan(_arg06, _arg1);
                    reply.writeNoException();
                    return true;
                case 13:
                    WorkSource _arg07;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = (WorkSource) WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    startLocationRestrictedScan(_arg07);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    List<ScanResult> _result8 = getScanResults(data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result8);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    disconnect();
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    reconnect();
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    reassociate();
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    WifiInfo _result9 = getConnectionInfo();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setWifiEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getWifiEnabledState();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    setCountryCode(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getCountryCode();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    setFrequencyBand(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getFrequencyBand();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isDualBandSupported();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = saveConfiguration();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    DhcpInfo _result10 = getDhcpInfo();
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isScanAlwaysAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 29:
                    WorkSource _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    int _arg12 = data.readInt();
                    String _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = (WorkSource) WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _result4 = acquireWifiLock(_arg02, _arg12, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (WorkSource) WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    updateWifiLockWorkSource(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = releaseWifiLock(data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    initializeMulticastFiltering();
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isMulticastEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    acquireMulticastLock(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    releaseMulticastLock();
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    setWifiApEnabled(_arg0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getWifiApEnabledState();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getWifiApConfiguration();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = buildWifiConfig(data.readString(), data.readString(), data.createByteArray());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    setWifiApConfiguration(_arg0);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    startWifi();
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    stopWifi();
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    addToBlacklist(data.readString());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    clearBlacklist();
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    addToSBlacklist(data.readString());
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    clearSBlacklist();
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getSBlacklist();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    Messenger _result11 = getWifiServiceMessenger();
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getConfigFile();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    enableTdls(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    enableTdlsWithMacAddress(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 52:
                    WorkSource _arg22;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (BatchedScanSettings) BatchedScanSettings.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    IBinder _arg13 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg22 = (WorkSource) WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    _result4 = requestBatchedScan(_arg03, _arg13, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (BatchedScanSettings) BatchedScanSettings.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    stopBatchedScan(_arg03);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    List<BatchedScanResult> _result12 = getBatchedScanResults(data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result12);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isBatchedScanSupported();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    pollBatchedScan();
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getWpsNfcConfigurationToken(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    enableVerboseLogging(data.readInt());
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getVerboseLoggingLevel();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    enableAggressiveHandover(data.readInt());
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getAggressiveHandover();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    setAllowScansWithTraffic(data.readInt());
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getAllowScansWithTraffic();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    setHalBasedAutojoinOffload(data.readInt());
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getHalBasedAutojoinOffload();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = enableAutoJoinWhenAssociated(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getEnableAutoJoinWhenAssociated();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    WifiConnectionStatistics _result13 = getConnectionStatistics();
                    reply.writeNoException();
                    if (_result13 != null) {
                        reply.writeInt(1);
                        _result13.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    disableEphemeralNetwork(data.readString());
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    factoryReset();
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    Network _result14 = getCurrentNetwork();
                    reply.writeNoException();
                    if (_result14 != null) {
                        reply.writeInt(1);
                        _result14.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (Message) Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result = callSECApi(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (Message) Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result5 = callSECStringApi(_arg04);
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSpecificNetwork(data.readInt());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 75:
                    PPPOEConfig _arg08;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = (PPPOEConfig) PPPOEConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    startPPPOE(_arg08);
                    reply.writeNoException();
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    stopPPPOE();
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    PPPOEInfo _result15 = getPPPOEInfo();
                    reply.writeNoException();
                    if (_result15 != null) {
                        reply.writeInt(1);
                        _result15.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getWifiApStaList();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getWifiApChameleonSsid();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getWifiApConfigTxPower();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    setWifiApConfigTxPower(data.readInt());
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    setWifiApConfigurationToDefault();
                    reply.writeNoException();
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    setWifiApTimeOut(data.readInt());
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getWifiApTimeOut();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isWifiApDbContain(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isWifiApListContain(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getWifiApLimitDataFromDb(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getWifiApRemainDataFromDb(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    addWifiApData(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setRoamTrigger(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getRoamTrigger();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setRoamDelta(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getRoamDelta();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setRoamScanPeriod(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getRoamScanPeriod();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setFullRoamScanPeriod(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getFullRoamScanPeriod();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setRoamBand(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getRoamBand();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setCountryRev(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getCountryRev();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLinkStatus();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setIsFmcNetwork(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setWifiIBSSEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getWifiIBSSEnabledState();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkInfo _result16 = getNetworkInfo();
                    reply.writeNoException();
                    if (_result16 != null) {
                        reply.writeInt(1);
                        _result16.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = checkWarningPopup();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = removeNetworkByMDM(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result4 = saveNetworkByMDM(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    List<WifiHs20OsuProvider> _result17 = getHs20OsuProviders();
                    reply.writeNoException();
                    reply.writeTypedList(_result17);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isHs20OsuProviderAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = fetchHs20OsuProviders();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isDetectedAsMaliciousHotspot(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isPasspointDefaultOn();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isPasspointMenuVisible();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isCaptivePortalException();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isWifiSharingEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = enableWifiSharing(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setProvisionSuccess(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getProvisionSuccess();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getWifiVerName();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void acquireMulticastLock(IBinder iBinder, String str) throws RemoteException;

    boolean acquireWifiLock(IBinder iBinder, int i, String str, WorkSource workSource) throws RemoteException;

    int addOrUpdateNetwork(WifiConfiguration wifiConfiguration) throws RemoteException;

    void addToBlacklist(String str) throws RemoteException;

    void addToSBlacklist(String str) throws RemoteException;

    void addWifiApData(String str, String str2, String str3) throws RemoteException;

    WifiConfiguration buildWifiConfig(String str, String str2, byte[] bArr) throws RemoteException;

    int callSECApi(Message message) throws RemoteException;

    String callSECStringApi(Message message) throws RemoteException;

    boolean checkWarningPopup() throws RemoteException;

    void clearBlacklist() throws RemoteException;

    void clearSBlacklist() throws RemoteException;

    void disableEphemeralNetwork(String str) throws RemoteException;

    boolean disableNetwork(int i) throws RemoteException;

    void disconnect() throws RemoteException;

    void enableAggressiveHandover(int i) throws RemoteException;

    boolean enableAutoJoinWhenAssociated(boolean z) throws RemoteException;

    boolean enableNetwork(int i, boolean z) throws RemoteException;

    void enableTdls(String str, boolean z) throws RemoteException;

    void enableTdlsWithMacAddress(String str, boolean z) throws RemoteException;

    void enableVerboseLogging(int i) throws RemoteException;

    boolean enableWifiSharing(boolean z) throws RemoteException;

    void factoryReset() throws RemoteException;

    boolean fetchHs20OsuProviders() throws RemoteException;

    int getAggressiveHandover() throws RemoteException;

    int getAllowScansWithTraffic() throws RemoteException;

    List<BatchedScanResult> getBatchedScanResults(String str) throws RemoteException;

    List<WifiChannel> getChannelList() throws RemoteException;

    String getConfigFile() throws RemoteException;

    List<WifiConfiguration> getConfiguredNetworks() throws RemoteException;

    WifiInfo getConnectionInfo() throws RemoteException;

    WifiConnectionStatistics getConnectionStatistics() throws RemoteException;

    String getCountryCode() throws RemoteException;

    String getCountryRev() throws RemoteException;

    Network getCurrentNetwork() throws RemoteException;

    DhcpInfo getDhcpInfo() throws RemoteException;

    boolean getEnableAutoJoinWhenAssociated() throws RemoteException;

    int getFrequencyBand() throws RemoteException;

    int getFullRoamScanPeriod() throws RemoteException;

    int getHalBasedAutojoinOffload() throws RemoteException;

    List<WifiHs20OsuProvider> getHs20OsuProviders() throws RemoteException;

    int getLinkStatus() throws RemoteException;

    WifiConfiguration getMatchingWifiConfig(ScanResult scanResult) throws RemoteException;

    NetworkInfo getNetworkInfo() throws RemoteException;

    PPPOEInfo getPPPOEInfo() throws RemoteException;

    List<WifiConfiguration> getPrivilegedConfiguredNetworks() throws RemoteException;

    boolean getProvisionSuccess() throws RemoteException;

    int getRoamBand() throws RemoteException;

    int getRoamDelta() throws RemoteException;

    int getRoamScanPeriod() throws RemoteException;

    int getRoamTrigger() throws RemoteException;

    String getSBlacklist() throws RemoteException;

    List<ScanResult> getScanResults(String str) throws RemoteException;

    WifiConfiguration getSpecificNetwork(int i) throws RemoteException;

    int getSupportedFeatures() throws RemoteException;

    int getVerboseLoggingLevel() throws RemoteException;

    String getWifiApChameleonSsid() throws RemoteException;

    int getWifiApConfigTxPower() throws RemoteException;

    WifiConfiguration getWifiApConfiguration() throws RemoteException;

    int getWifiApEnabledState() throws RemoteException;

    String getWifiApLimitDataFromDb(String str) throws RemoteException;

    String getWifiApRemainDataFromDb(String str) throws RemoteException;

    String getWifiApStaList() throws RemoteException;

    int getWifiApTimeOut() throws RemoteException;

    int getWifiEnabledState() throws RemoteException;

    int getWifiIBSSEnabledState() throws RemoteException;

    Messenger getWifiServiceMessenger() throws RemoteException;

    String getWifiVerName() throws RemoteException;

    String getWpsNfcConfigurationToken(int i) throws RemoteException;

    void initializeMulticastFiltering() throws RemoteException;

    boolean isBatchedScanSupported() throws RemoteException;

    boolean isCaptivePortalException() throws RemoteException;

    boolean isDetectedAsMaliciousHotspot(String str) throws RemoteException;

    boolean isDualBandSupported() throws RemoteException;

    boolean isHs20OsuProviderAvailable() throws RemoteException;

    boolean isMulticastEnabled() throws RemoteException;

    boolean isPasspointDefaultOn() throws RemoteException;

    boolean isPasspointMenuVisible() throws RemoteException;

    boolean isScanAlwaysAvailable() throws RemoteException;

    boolean isWifiApDbContain(String str) throws RemoteException;

    boolean isWifiApListContain(String str) throws RemoteException;

    boolean isWifiSharingEnabled() throws RemoteException;

    boolean pingSupplicant() throws RemoteException;

    void pollBatchedScan() throws RemoteException;

    void reassociate() throws RemoteException;

    void reconnect() throws RemoteException;

    void releaseMulticastLock() throws RemoteException;

    boolean releaseWifiLock(IBinder iBinder) throws RemoteException;

    boolean removeNetwork(int i) throws RemoteException;

    boolean removeNetworkByMDM(int i) throws RemoteException;

    WifiActivityEnergyInfo reportActivityInfo() throws RemoteException;

    boolean requestBatchedScan(BatchedScanSettings batchedScanSettings, IBinder iBinder, WorkSource workSource) throws RemoteException;

    boolean saveConfiguration() throws RemoteException;

    boolean saveNetworkByMDM(WifiConfiguration wifiConfiguration) throws RemoteException;

    void setAllowScansWithTraffic(int i) throws RemoteException;

    void setCountryCode(String str, boolean z) throws RemoteException;

    boolean setCountryRev(String str) throws RemoteException;

    void setFrequencyBand(int i, boolean z) throws RemoteException;

    boolean setFullRoamScanPeriod(int i) throws RemoteException;

    void setHalBasedAutojoinOffload(int i) throws RemoteException;

    boolean setIsFmcNetwork(boolean z) throws RemoteException;

    boolean setProvisionSuccess(boolean z) throws RemoteException;

    boolean setRoamBand(int i) throws RemoteException;

    boolean setRoamDelta(int i) throws RemoteException;

    boolean setRoamScanPeriod(int i) throws RemoteException;

    boolean setRoamTrigger(int i) throws RemoteException;

    void setWifiApConfigTxPower(int i) throws RemoteException;

    void setWifiApConfiguration(WifiConfiguration wifiConfiguration) throws RemoteException;

    void setWifiApConfigurationToDefault() throws RemoteException;

    void setWifiApEnabled(WifiConfiguration wifiConfiguration, boolean z) throws RemoteException;

    void setWifiApTimeOut(int i) throws RemoteException;

    boolean setWifiEnabled(boolean z) throws RemoteException;

    boolean setWifiIBSSEnabled(boolean z) throws RemoteException;

    void startLocationRestrictedScan(WorkSource workSource) throws RemoteException;

    void startPPPOE(PPPOEConfig pPPOEConfig) throws RemoteException;

    void startScan(ScanSettings scanSettings, WorkSource workSource) throws RemoteException;

    void startWifi() throws RemoteException;

    void stopBatchedScan(BatchedScanSettings batchedScanSettings) throws RemoteException;

    void stopPPPOE() throws RemoteException;

    void stopWifi() throws RemoteException;

    void updateWifiLockWorkSource(IBinder iBinder, WorkSource workSource) throws RemoteException;
}
