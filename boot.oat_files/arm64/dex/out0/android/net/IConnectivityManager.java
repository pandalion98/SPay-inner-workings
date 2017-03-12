package android.net;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.telephony.SignalStrength;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnInfo;
import com.android.internal.net.VpnProfile;

public interface IConnectivityManager extends IInterface {

    public static abstract class Stub extends Binder implements IConnectivityManager {
        private static final String DESCRIPTOR = "android.net.IConnectivityManager";
        static final int TRANSACTION_addVpnAddress = 90;
        static final int TRANSACTION_checkIfLocalProxyPortExists = 89;
        static final int TRANSACTION_checkMobileProvisioning = 56;
        static final int TRANSACTION_createEnterpriseVpn = 74;
        static final int TRANSACTION_disconnect = 76;
        static final int TRANSACTION_disconnectPerAppVpn = 83;
        static final int TRANSACTION_disconnectSystemVpn = 80;
        static final int TRANSACTION_enforceVzw800ApnPermission = 96;
        static final int TRANSACTION_establishEnterpriseVpn = 79;
        static final int TRANSACTION_establishVpn = 50;
        static final int TRANSACTION_factoryReset = 93;
        static final int TRANSACTION_getActiveEnterpriseNetworkType = 31;
        static final int TRANSACTION_getActiveLinkProperties = 12;
        static final int TRANSACTION_getActiveNetwork = 1;
        static final int TRANSACTION_getActiveNetworkInfo = 2;
        static final int TRANSACTION_getActiveNetworkInfoForUid = 3;
        static final int TRANSACTION_getActiveNetworkQuotaInfo = 18;
        static final int TRANSACTION_getAllNetworkInfo = 6;
        static final int TRANSACTION_getAllNetworkInfoEx = 7;
        static final int TRANSACTION_getAllNetworkState = 17;
        static final int TRANSACTION_getAllNetworks = 9;
        static final int TRANSACTION_getAllVpnInfo = 54;
        static final int TRANSACTION_getChainingEnabledForProfile = 86;
        static final int TRANSACTION_getDefaultNetworkCapabilitiesForUser = 10;
        static final int TRANSACTION_getDhcpServerConfiguration = 60;
        static final int TRANSACTION_getGlobalProxy = 45;
        static final int TRANSACTION_getInterfaceName = 75;
        static final int TRANSACTION_getKdiLinkQualityInfo = 98;
        static final int TRANSACTION_getKdiSignalStrength = 97;
        static final int TRANSACTION_getLastTetherError = 26;
        static final int TRANSACTION_getLegacyVpnInfo = 53;
        static final int TRANSACTION_getLinkProperties = 14;
        static final int TRANSACTION_getLinkPropertiesForType = 13;
        static final int TRANSACTION_getMobileProvisioningUrl = 57;
        static final int TRANSACTION_getNetIdForInterface = 85;
        static final int TRANSACTION_getNetIdForNetworkInfo = 84;
        static final int TRANSACTION_getNetworkCapabilities = 15;
        static final int TRANSACTION_getNetworkForType = 8;
        static final int TRANSACTION_getNetworkInfo = 4;
        static final int TRANSACTION_getNetworkInfoForNetwork = 5;
        static final int TRANSACTION_getProxyForNetwork = 47;
        static final int TRANSACTION_getProxyInfoForUid = 88;
        static final int TRANSACTION_getRestoreDefaultNetworkDelay = 73;
        static final int TRANSACTION_getRoamingReduction = 100;
        static final int TRANSACTION_getRoutingInfo = 87;
        static final int TRANSACTION_getTetherableBluetoothRegexs = 40;
        static final int TRANSACTION_getTetherableIfaces = 34;
        static final int TRANSACTION_getTetherableUsbRegexs = 38;
        static final int TRANSACTION_getTetherableWifiRegexs = 39;
        static final int TRANSACTION_getTetheredDhcpRanges = 37;
        static final int TRANSACTION_getTetheredIfaces = 35;
        static final int TRANSACTION_getTetheringErroredIfaces = 36;
        static final int TRANSACTION_getUidsForApnType = 30;
        static final int TRANSACTION_getUsersForEnterpriseNetwork = 32;
        static final int TRANSACTION_getVpnConfig = 51;
        static final int TRANSACTION_isActiveNetworkMetered = 19;
        static final int TRANSACTION_isEntApnEnabled = 29;
        static final int TRANSACTION_isEnterpriseApn = 33;
        static final int TRANSACTION_isMobilePolicyDataEnable = 23;
        static final int TRANSACTION_isNetworkSupported = 11;
        static final int TRANSACTION_isSplitBillingEnabled = 28;
        static final int TRANSACTION_isTetheringSupported = 27;
        static final int TRANSACTION_knoxVpnProfileType = 82;
        static final int TRANSACTION_listenForNetwork = 69;
        static final int TRANSACTION_pendingListenForNetwork = 70;
        static final int TRANSACTION_pendingRequestForNetwork = 67;
        static final int TRANSACTION_prepareEnterpriseVpn = 77;
        static final int TRANSACTION_prepareEnterpriseVpnExt = 78;
        static final int TRANSACTION_prepareVpn = 48;
        static final int TRANSACTION_registerNetworkAgent = 65;
        static final int TRANSACTION_registerNetworkFactory = 62;
        static final int TRANSACTION_releaseNetworkRequest = 71;
        static final int TRANSACTION_releasePendingNetworkRequest = 68;
        static final int TRANSACTION_removeRouteToHostAddress = 21;
        static final int TRANSACTION_removeVpnAddress = 91;
        static final int TRANSACTION_reportInetCondition = 43;
        static final int TRANSACTION_reportNetworkConnectivity = 44;
        static final int TRANSACTION_requestBandwidthUpdate = 63;
        static final int TRANSACTION_requestNetwork = 66;
        static final int TRANSACTION_requestRouteToHostAddress = 20;
        static final int TRANSACTION_saveDhcpServerConfiguration = 61;
        static final int TRANSACTION_setAcceptUnvalidated = 72;
        static final int TRANSACTION_setAirplaneMode = 59;
        static final int TRANSACTION_setGlobalProxy = 46;
        static final int TRANSACTION_setNcmTethering = 42;
        static final int TRANSACTION_setPolicyDataEnable = 22;
        static final int TRANSACTION_setProvisioningNotificationVisible = 58;
        static final int TRANSACTION_setRoamingReduction = 99;
        static final int TRANSACTION_setUnderlyingNetworksForVpn = 92;
        static final int TRANSACTION_setUsbTethering = 41;
        static final int TRANSACTION_setVpnPackageAuthorization = 49;
        static final int TRANSACTION_startLegacyVpn = 52;
        static final int TRANSACTION_startNattKeepalive = 94;
        static final int TRANSACTION_stopKeepalive = 95;
        static final int TRANSACTION_tagSocket = 16;
        static final int TRANSACTION_tether = 24;
        static final int TRANSACTION_unregisterNetworkFactory = 64;
        static final int TRANSACTION_untether = 25;
        static final int TRANSACTION_updateEnterpriseVpn = 81;
        static final int TRANSACTION_updateLockdownVpn = 55;

        private static class Proxy implements IConnectivityManager {
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

            public Network getActiveNetwork() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Network _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
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

            public NetworkInfo getActiveNetworkInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            public NetworkInfo getActiveNetworkInfoForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(4, _data, _reply, 0);
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

            public NetworkInfo getNetworkInfoForNetwork(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
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

            public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    NetworkInfo[] _result = (NetworkInfo[]) _reply.createTypedArray(NetworkInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkInfo[] getAllNetworkInfoEx() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    NetworkInfo[] _result = (NetworkInfo[]) _reply.createTypedArray(NetworkInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Network getNetworkForType(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Network _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public Network[] getAllNetworks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    Network[] _result = (Network[]) _reply.createTypedArray(Network.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    NetworkCapabilities[] _result = (NetworkCapabilities[]) _reply.createTypedArray(NetworkCapabilities.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNetworkSupported(int networkType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
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

            public LinkProperties getActiveLinkProperties() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    LinkProperties _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (LinkProperties) LinkProperties.CREATOR.createFromParcel(_reply);
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

            public LinkProperties getLinkPropertiesForType(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    LinkProperties _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (LinkProperties) LinkProperties.CREATOR.createFromParcel(_reply);
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

            public LinkProperties getLinkProperties(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    LinkProperties _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (LinkProperties) LinkProperties.CREATOR.createFromParcel(_reply);
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

            public NetworkCapabilities getNetworkCapabilities(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkCapabilities _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(_reply);
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

            public void tagSocket(ParcelFileDescriptor pfd, int uid, int tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pfd != null) {
                        _data.writeInt(1);
                        pfd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    _data.writeInt(tag);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkState[] getAllNetworkState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    NetworkState[] _result = (NetworkState[]) _reply.createTypedArray(NetworkState.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkQuotaInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkQuotaInfo) NetworkQuotaInfo.CREATOR.createFromParcel(_reply);
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

            public boolean isActiveNetworkMetered() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
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

            public boolean requestRouteToHostAddress(int networkType, byte[] hostAddress) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeByteArray(hostAddress);
                    this.mRemote.transact(20, _data, _reply, 0);
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

            public boolean removeRouteToHostAddress(int networkType, byte[] hostAddress) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeByteArray(hostAddress);
                    this.mRemote.transact(21, _data, _reply, 0);
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

            public void setPolicyDataEnable(int networkType, boolean enabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    if (enabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMobilePolicyDataEnable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
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

            public int tether(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int untether(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLastTetherError(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTetheringSupported() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
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

            public boolean isSplitBillingEnabled() throws RemoteException {
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

            public boolean isEntApnEnabled(int connectionType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(connectionType);
                    this.mRemote.transact(29, _data, _reply, 0);
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

            public int[] getUidsForApnType(String apn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apn);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActiveEnterpriseNetworkType(String apn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apn);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getUsersForEnterpriseNetwork(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnterpriseApn(String apn, String mcc, String mnc) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apn);
                    _data.writeString(mcc);
                    _data.writeString(mnc);
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

            public String[] getTetherableIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTetheredIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTetheringErroredIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTetheredDhcpRanges() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTetherableUsbRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTetherableWifiRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getTetherableBluetoothRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setUsbTethering(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setNcmTethering(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportInetCondition(int networkType, int percentage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeInt(percentage);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportNetworkConnectivity(Network network, boolean hasConnectivity) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!hasConnectivity) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ProxyInfo getGlobalProxy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ProxyInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ProxyInfo) ProxyInfo.CREATOR.createFromParcel(_reply);
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

            public void setGlobalProxy(ProxyInfo p) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ProxyInfo getProxyForNetwork(Network nework) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ProxyInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (nework != null) {
                        _data.writeInt(1);
                        nework.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ProxyInfo) ProxyInfo.CREATOR.createFromParcel(_reply);
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

            public boolean prepareVpn(String oldPackage, String newPackage, int userId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(oldPackage);
                    _data.writeString(newPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(48, _data, _reply, 0);
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

            public void setVpnPackageAuthorization(String packageName, int userId, boolean authorized) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (authorized) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor establishVpn(VpnConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ParcelFileDescriptor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
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

            public VpnConfig getVpnConfig(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    VpnConfig _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (VpnConfig) VpnConfig.CREATOR.createFromParcel(_reply);
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

            public void startLegacyVpn(VpnProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public LegacyVpnInfo getLegacyVpnInfo(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    LegacyVpnInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (LegacyVpnInfo) LegacyVpnInfo.CREATOR.createFromParcel(_reply);
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

            public VpnInfo[] getAllVpnInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    VpnInfo[] _result = (VpnInfo[]) _reply.createTypedArray(VpnInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateLockdownVpn() throws RemoteException {
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

            public int checkMobileProvisioning(int suggestedTimeOutMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suggestedTimeOutMs);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMobileProvisioningUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setProvisioningNotificationVisible(boolean visible, int networkType, String action) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (visible) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(networkType);
                    _data.writeString(action);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAirplaneMode(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public DhcpServerConfiguration getDhcpServerConfiguration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    DhcpServerConfiguration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (DhcpServerConfiguration) DhcpServerConfiguration.CREATOR.createFromParcel(_reply);
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

            public boolean saveDhcpServerConfiguration(DhcpServerConfiguration serverConfig) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serverConfig != null) {
                        _data.writeInt(1);
                        serverConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(61, _data, _reply, 0);
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

            public void registerNetworkFactory(Messenger messenger, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(name);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestBandwidthUpdate(Network network) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(63, _data, _reply, 0);
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

            public void unregisterNetworkFactory(Messenger messenger) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int registerNetworkAgent(Messenger messenger, NetworkInfo ni, LinkProperties lp, NetworkCapabilities nc, int score, NetworkMisc misc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (ni != null) {
                        _data.writeInt(1);
                        ni.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (lp != null) {
                        _data.writeInt(1);
                        lp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (nc != null) {
                        _data.writeInt(1);
                        nc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(score);
                    if (misc != null) {
                        _data.writeInt(1);
                        misc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkRequest requestNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, int timeoutSec, IBinder binder, int legacy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkRequest _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(timeoutSec);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(legacy);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(_reply);
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

            public NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkRequest _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(_reply);
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

            public void releasePendingNetworkRequest(PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkRequest _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(_reply);
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

            public void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkRequest != null) {
                        _data.writeInt(1);
                        networkRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAcceptUnvalidated(Network network, boolean accept, boolean always) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    if (!always) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRestoreDefaultNetworkDelay(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean createEnterpriseVpn(String packageName, String vpnProfileName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(vpnProfileName);
                    this.mRemote.transact(74, _data, _reply, 0);
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

            public String getInterfaceName(String vpnProfileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(vpnProfileName);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disconnect(String vpnProfileName, boolean isConnectedProfile) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(vpnProfileName);
                    if (isConnectedProfile) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(76, _data, _reply, 0);
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

            public boolean prepareEnterpriseVpn(String oldPackage, String newPackage, String profile, boolean type, boolean isConnecting) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(oldPackage);
                    _data.writeString(newPackage);
                    _data.writeString(profile);
                    if (type) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (isConnecting) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(77, _data, _reply, 0);
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

            public boolean prepareEnterpriseVpnExt(String oldPackage, String profile, boolean isConnecting, boolean isMetaEnabled) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(oldPackage);
                    _data.writeString(profile);
                    if (isConnecting) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (isMetaEnabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(78, _data, _reply, 0);
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

            public ParcelFileDescriptor establishEnterpriseVpn(VpnConfig config, String vpnProfileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ParcelFileDescriptor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(vpnProfileName);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
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

            public void disconnectSystemVpn(int user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(user);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateEnterpriseVpn(String profileName, int domain, boolean flag) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(profileName);
                    _data.writeInt(domain);
                    if (flag) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int knoxVpnProfileType(String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(profileName);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disconnectPerAppVpn(String vpnProfileName, int callingUid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(vpnProfileName);
                    _data.writeInt(callingUid);
                    this.mRemote.transact(83, _data, _reply, 0);
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

            public int getNetIdForNetworkInfo(NetworkInfo ni) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ni != null) {
                        _data.writeInt(1);
                        ni.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNetIdForInterface(String interfaceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(interfaceName);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getChainingEnabledForProfile(int callingUid) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUid);
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

            public String getRoutingInfo(int uid, String host, String url) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(host);
                    _data.writeString(url);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getProxyInfoForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkIfLocalProxyPortExists(int port) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(port);
                    this.mRemote.transact(89, _data, _reply, 0);
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

            public boolean addVpnAddress(String address, int prefixLength) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prefixLength);
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

            public boolean removeVpnAddress(String address, int prefixLength) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prefixLength);
                    this.mRemote.transact(91, _data, _reply, 0);
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

            public boolean setUnderlyingNetworksForVpn(Network[] networks) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(networks, 0);
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

            public void factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startNattKeepalive(Network network, int intervalSeconds, Messenger messenger, IBinder binder, String srcAddr, int srcPort, String dstAddr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(intervalSeconds);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(srcAddr);
                    _data.writeInt(srcPort);
                    _data.writeString(dstAddr);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopKeepalive(Network network, int slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(slot);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enforceVzw800ApnPermission(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SignalStrength getKdiSignalStrength() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    SignalStrength _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (SignalStrength) SignalStrength.CREATOR.createFromParcel(_reply);
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

            public LinkQualityInfo getKdiLinkQualityInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    LinkQualityInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (LinkQualityInfo) LinkQualityInfo.CREATOR.createFromParcel(_reply);
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

            public void setRoamingReduction(int uid, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(policy);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRoamingReduction(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(100, _data, _reply, 0);
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

        public static IConnectivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IConnectivityManager)) {
                return new Proxy(obj);
            }
            return (IConnectivityManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            Network _result;
            NetworkInfo _result2;
            Network _arg0;
            NetworkInfo[] _result3;
            boolean _result4;
            LinkProperties _result5;
            boolean _arg1;
            int _result6;
            int[] _result7;
            String[] _result8;
            boolean _arg02;
            ProxyInfo _result9;
            String _arg03;
            int _arg12;
            boolean _arg2;
            VpnConfig _arg04;
            ParcelFileDescriptor _result10;
            String _result11;
            Messenger _arg05;
            NetworkCapabilities _arg06;
            Messenger _arg13;
            NetworkRequest _result12;
            PendingIntent _arg14;
            String _arg15;
            boolean _arg3;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getActiveNetwork();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getActiveNetworkInfo();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getActiveNetworkInfoForUid(data.readInt());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getNetworkInfo(data.readInt());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result2 = getNetworkInfoForNetwork(_arg0);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAllNetworkInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result3, 1);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAllNetworkInfoEx();
                    reply.writeNoException();
                    reply.writeTypedArray(_result3, 1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getNetworkForType(data.readInt());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    Network[] _result13 = getAllNetworks();
                    reply.writeNoException();
                    reply.writeTypedArray(_result13, 1);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkCapabilities[] _result14 = getDefaultNetworkCapabilitiesForUser(data.readInt());
                    reply.writeNoException();
                    reply.writeTypedArray(_result14, 1);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isNetworkSupported(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getActiveLinkProperties();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getLinkPropertiesForType(data.readInt());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result5 = getLinkProperties(_arg0);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    NetworkCapabilities _result15 = getNetworkCapabilities(_arg0);
                    reply.writeNoException();
                    if (_result15 != null) {
                        reply.writeInt(1);
                        _result15.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 16:
                    ParcelFileDescriptor _arg07;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    tagSocket(_arg07, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkState[] _result16 = getAllNetworkState();
                    reply.writeNoException();
                    reply.writeTypedArray(_result16, 1);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkQuotaInfo _result17 = getActiveNetworkQuotaInfo();
                    reply.writeNoException();
                    if (_result17 != null) {
                        reply.writeInt(1);
                        _result17.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isActiveNetworkMetered();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = requestRouteToHostAddress(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = removeRouteToHostAddress(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    setPolicyDataEnable(_arg08, _arg1);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isMobilePolicyDataEnable();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = tether(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = untether(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getLastTetherError(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isTetheringSupported();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isSplitBillingEnabled();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isEntApnEnabled(data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getUidsForApnType(data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result7);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getActiveEnterpriseNetworkType(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getUsersForEnterpriseNetwork(data.readInt());
                    reply.writeNoException();
                    reply.writeIntArray(_result7);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isEnterpriseApn(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getTetherableIfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getTetheredIfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getTetheringErroredIfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getTetheredDhcpRanges();
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getTetherableUsbRegexs();
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getTetherableWifiRegexs();
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getTetherableBluetoothRegexs();
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    _result6 = setUsbTethering(_arg02);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    _result6 = setNcmTethering(_arg02);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    reportInetCondition(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    reportNetworkConnectivity(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getGlobalProxy();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 46:
                    ProxyInfo _arg09;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = (ProxyInfo) ProxyInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    setGlobalProxy(_arg09);
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result9 = getProxyForNetwork(_arg0);
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = prepareVpn(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readString();
                    _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    setVpnPackageAuthorization(_arg03, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (VpnConfig) VpnConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result10 = establishVpn(_arg04);
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    VpnConfig _result18 = getVpnConfig(data.readInt());
                    reply.writeNoException();
                    if (_result18 != null) {
                        reply.writeInt(1);
                        _result18.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 52:
                    VpnProfile _arg010;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = (VpnProfile) VpnProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    startLegacyVpn(_arg010);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    LegacyVpnInfo _result19 = getLegacyVpnInfo(data.readInt());
                    reply.writeNoException();
                    if (_result19 != null) {
                        reply.writeInt(1);
                        _result19.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    VpnInfo[] _result20 = getAllVpnInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result20, 1);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = updateLockdownVpn();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = checkMobileProvisioning(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _result11 = getMobileProvisioningUrl();
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    setProvisioningNotificationVisible(_arg02, data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    setAirplaneMode(_arg02);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    DhcpServerConfiguration _result21 = getDhcpServerConfiguration();
                    reply.writeNoException();
                    if (_result21 != null) {
                        reply.writeInt(1);
                        _result21.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 61:
                    DhcpServerConfiguration _arg011;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = (DhcpServerConfiguration) DhcpServerConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    _result4 = saveDhcpServerConfiguration(_arg011);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Messenger) Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    registerNetworkFactory(_arg05, data.readString());
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result4 = requestBandwidthUpdate(_arg0);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Messenger) Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    unregisterNetworkFactory(_arg05);
                    reply.writeNoException();
                    return true;
                case 65:
                    NetworkInfo _arg16;
                    LinkProperties _arg22;
                    NetworkCapabilities _arg32;
                    NetworkMisc _arg5;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Messenger) Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg16 = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg22 = (LinkProperties) LinkProperties.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg32 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    int _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg5 = (NetworkMisc) NetworkMisc.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    _result6 = registerNetworkAgent(_arg05, _arg16, _arg22, _arg32, _arg4, _arg5);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = (Messenger) Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    _result12 = requestNetwork(_arg06, _arg13, data.readInt(), data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg14 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    _result12 = pendingRequestForNetwork(_arg06, _arg14);
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 68:
                    PendingIntent _arg012;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    releasePendingNetworkRequest(_arg012);
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = (Messenger) Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    _result12 = listenForNetwork(_arg06, _arg13, data.readStrongBinder());
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg14 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    pendingListenForNetwork(_arg06, _arg14);
                    reply.writeNoException();
                    return true;
                case 71:
                    NetworkRequest _arg013;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = (NetworkRequest) NetworkRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    releaseNetworkRequest(_arg013);
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    setAcceptUnvalidated(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getRestoreDefaultNetworkDelay(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = createEnterpriseVpn(data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    _result11 = getInterfaceName(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _result4 = disconnect(_arg03, _arg1);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 77:
                    boolean _arg42;
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readString();
                    _arg15 = data.readString();
                    String _arg23 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    } else {
                        _arg3 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg42 = true;
                    } else {
                        _arg42 = false;
                    }
                    _result4 = prepareEnterpriseVpn(_arg03, _arg15, _arg23, _arg3, _arg42);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readString();
                    _arg15 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    } else {
                        _arg3 = false;
                    }
                    _result4 = prepareEnterpriseVpnExt(_arg03, _arg15, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (VpnConfig) VpnConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result10 = establishEnterpriseVpn(_arg04, data.readString());
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    disconnectSystemVpn(data.readInt());
                    reply.writeNoException();
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readString();
                    _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    updateEnterpriseVpn(_arg03, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = knoxVpnProfileType(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = disconnectPerAppVpn(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 84:
                    NetworkInfo _arg014;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = (NetworkInfo) NetworkInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    _result6 = getNetIdForNetworkInfo(_arg014);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getNetIdForInterface(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getChainingEnabledForProfile(data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    _result11 = getRoutingInfo(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getProxyInfoForUid(data.readInt());
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = checkIfLocalProxyPortExists(data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = addVpnAddress(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = removeVpnAddress(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setUnderlyingNetworksForVpn((Network[]) data.createTypedArray(Network.CREATOR));
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    factoryReset();
                    reply.writeNoException();
                    return true;
                case 94:
                    Messenger _arg24;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg24 = (Messenger) Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg24 = null;
                    }
                    startNattKeepalive(_arg0, _arg12, _arg24, data.readStrongBinder(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    stopKeepalive(_arg0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    enforceVzw800ApnPermission(data.readInt());
                    reply.writeNoException();
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    SignalStrength _result22 = getKdiSignalStrength();
                    reply.writeNoException();
                    if (_result22 != null) {
                        reply.writeInt(1);
                        _result22.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    LinkQualityInfo _result23 = getKdiLinkQualityInfo();
                    reply.writeNoException();
                    if (_result23 != null) {
                        reply.writeInt(1);
                        _result23.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    setRoamingReduction(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = getRoamingReduction(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean addVpnAddress(String str, int i) throws RemoteException;

    boolean checkIfLocalProxyPortExists(int i) throws RemoteException;

    int checkMobileProvisioning(int i) throws RemoteException;

    boolean createEnterpriseVpn(String str, String str2) throws RemoteException;

    boolean disconnect(String str, boolean z) throws RemoteException;

    boolean disconnectPerAppVpn(String str, int i) throws RemoteException;

    void disconnectSystemVpn(int i) throws RemoteException;

    void enforceVzw800ApnPermission(int i) throws RemoteException;

    ParcelFileDescriptor establishEnterpriseVpn(VpnConfig vpnConfig, String str) throws RemoteException;

    ParcelFileDescriptor establishVpn(VpnConfig vpnConfig) throws RemoteException;

    void factoryReset() throws RemoteException;

    int getActiveEnterpriseNetworkType(String str) throws RemoteException;

    LinkProperties getActiveLinkProperties() throws RemoteException;

    Network getActiveNetwork() throws RemoteException;

    NetworkInfo getActiveNetworkInfo() throws RemoteException;

    NetworkInfo getActiveNetworkInfoForUid(int i) throws RemoteException;

    NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException;

    NetworkInfo[] getAllNetworkInfo() throws RemoteException;

    NetworkInfo[] getAllNetworkInfoEx() throws RemoteException;

    NetworkState[] getAllNetworkState() throws RemoteException;

    Network[] getAllNetworks() throws RemoteException;

    VpnInfo[] getAllVpnInfo() throws RemoteException;

    boolean getChainingEnabledForProfile(int i) throws RemoteException;

    NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int i) throws RemoteException;

    DhcpServerConfiguration getDhcpServerConfiguration() throws RemoteException;

    ProxyInfo getGlobalProxy() throws RemoteException;

    String getInterfaceName(String str) throws RemoteException;

    LinkQualityInfo getKdiLinkQualityInfo() throws RemoteException;

    SignalStrength getKdiSignalStrength() throws RemoteException;

    int getLastTetherError(String str) throws RemoteException;

    LegacyVpnInfo getLegacyVpnInfo(int i) throws RemoteException;

    LinkProperties getLinkProperties(Network network) throws RemoteException;

    LinkProperties getLinkPropertiesForType(int i) throws RemoteException;

    String getMobileProvisioningUrl() throws RemoteException;

    int getNetIdForInterface(String str) throws RemoteException;

    int getNetIdForNetworkInfo(NetworkInfo networkInfo) throws RemoteException;

    NetworkCapabilities getNetworkCapabilities(Network network) throws RemoteException;

    Network getNetworkForType(int i) throws RemoteException;

    NetworkInfo getNetworkInfo(int i) throws RemoteException;

    NetworkInfo getNetworkInfoForNetwork(Network network) throws RemoteException;

    ProxyInfo getProxyForNetwork(Network network) throws RemoteException;

    String[] getProxyInfoForUid(int i) throws RemoteException;

    int getRestoreDefaultNetworkDelay(int i) throws RemoteException;

    int getRoamingReduction(int i) throws RemoteException;

    String getRoutingInfo(int i, String str, String str2) throws RemoteException;

    String[] getTetherableBluetoothRegexs() throws RemoteException;

    String[] getTetherableIfaces() throws RemoteException;

    String[] getTetherableUsbRegexs() throws RemoteException;

    String[] getTetherableWifiRegexs() throws RemoteException;

    String[] getTetheredDhcpRanges() throws RemoteException;

    String[] getTetheredIfaces() throws RemoteException;

    String[] getTetheringErroredIfaces() throws RemoteException;

    int[] getUidsForApnType(String str) throws RemoteException;

    int[] getUsersForEnterpriseNetwork(int i) throws RemoteException;

    VpnConfig getVpnConfig(int i) throws RemoteException;

    boolean isActiveNetworkMetered() throws RemoteException;

    boolean isEntApnEnabled(int i) throws RemoteException;

    boolean isEnterpriseApn(String str, String str2, String str3) throws RemoteException;

    boolean isMobilePolicyDataEnable() throws RemoteException;

    boolean isNetworkSupported(int i) throws RemoteException;

    boolean isSplitBillingEnabled() throws RemoteException;

    boolean isTetheringSupported() throws RemoteException;

    int knoxVpnProfileType(String str) throws RemoteException;

    NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder iBinder) throws RemoteException;

    void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent) throws RemoteException;

    NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent) throws RemoteException;

    boolean prepareEnterpriseVpn(String str, String str2, String str3, boolean z, boolean z2) throws RemoteException;

    boolean prepareEnterpriseVpnExt(String str, String str2, boolean z, boolean z2) throws RemoteException;

    boolean prepareVpn(String str, String str2, int i) throws RemoteException;

    int registerNetworkAgent(Messenger messenger, NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int i, NetworkMisc networkMisc) throws RemoteException;

    void registerNetworkFactory(Messenger messenger, String str) throws RemoteException;

    void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException;

    void releasePendingNetworkRequest(PendingIntent pendingIntent) throws RemoteException;

    boolean removeRouteToHostAddress(int i, byte[] bArr) throws RemoteException;

    boolean removeVpnAddress(String str, int i) throws RemoteException;

    void reportInetCondition(int i, int i2) throws RemoteException;

    void reportNetworkConnectivity(Network network, boolean z) throws RemoteException;

    boolean requestBandwidthUpdate(Network network) throws RemoteException;

    NetworkRequest requestNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, int i, IBinder iBinder, int i2) throws RemoteException;

    boolean requestRouteToHostAddress(int i, byte[] bArr) throws RemoteException;

    boolean saveDhcpServerConfiguration(DhcpServerConfiguration dhcpServerConfiguration) throws RemoteException;

    void setAcceptUnvalidated(Network network, boolean z, boolean z2) throws RemoteException;

    void setAirplaneMode(boolean z) throws RemoteException;

    void setGlobalProxy(ProxyInfo proxyInfo) throws RemoteException;

    int setNcmTethering(boolean z) throws RemoteException;

    void setPolicyDataEnable(int i, boolean z) throws RemoteException;

    void setProvisioningNotificationVisible(boolean z, int i, String str) throws RemoteException;

    void setRoamingReduction(int i, int i2) throws RemoteException;

    boolean setUnderlyingNetworksForVpn(Network[] networkArr) throws RemoteException;

    int setUsbTethering(boolean z) throws RemoteException;

    void setVpnPackageAuthorization(String str, int i, boolean z) throws RemoteException;

    void startLegacyVpn(VpnProfile vpnProfile) throws RemoteException;

    void startNattKeepalive(Network network, int i, Messenger messenger, IBinder iBinder, String str, int i2, String str2) throws RemoteException;

    void stopKeepalive(Network network, int i) throws RemoteException;

    void tagSocket(ParcelFileDescriptor parcelFileDescriptor, int i, int i2) throws RemoteException;

    int tether(String str) throws RemoteException;

    void unregisterNetworkFactory(Messenger messenger) throws RemoteException;

    int untether(String str) throws RemoteException;

    void updateEnterpriseVpn(String str, int i, boolean z) throws RemoteException;

    boolean updateLockdownVpn() throws RemoteException;
}
