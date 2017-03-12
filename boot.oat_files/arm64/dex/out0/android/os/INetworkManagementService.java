package android.os;

import android.net.INetworkManagementEventObserver;
import android.net.InterfaceConfiguration;
import android.net.Network;
import android.net.NetworkStats;
import android.net.RouteInfo;
import android.net.UidRange;
import android.net.wifi.WifiConfiguration;
import java.util.List;

public interface INetworkManagementService extends IInterface {

    public static abstract class Stub extends Binder implements INetworkManagementService {
        private static final String DESCRIPTOR = "android.os.INetworkManagementService";
        static final int TRANSACTION_addChain = 140;
        static final int TRANSACTION_addEnterpriseUidRanges = 123;
        static final int TRANSACTION_addIdleTimer = 70;
        static final int TRANSACTION_addInterfaceToLocalNetwork = 116;
        static final int TRANSACTION_addInterfaceToNetwork = 102;
        static final int TRANSACTION_addIpAcceptRule = 152;
        static final int TRANSACTION_addLegacyRouteForNetId = 104;
        static final int TRANSACTION_addMptcpLink = 138;
        static final int TRANSACTION_addRoute = 16;
        static final int TRANSACTION_addRouteWithMetric = 119;
        static final int TRANSACTION_addSocksRule = 142;
        static final int TRANSACTION_addSocksSkipRule = 146;
        static final int TRANSACTION_addSocksSkipRuleProto = 148;
        static final int TRANSACTION_addSourcePortAcceptRule = 159;
        static final int TRANSACTION_addSourceRoute = 157;
        static final int TRANSACTION_addUidSocksRule = 144;
        static final int TRANSACTION_addUidToChain = 150;
        static final int TRANSACTION_addUpstreamV6Interface = 39;
        static final int TRANSACTION_addVpnUidRanges = 91;
        static final int TRANSACTION_allowProtect = 114;
        static final int TRANSACTION_appendInterfaceToLocalNetwork = 118;
        static final int TRANSACTION_attachPppd = 42;
        static final int TRANSACTION_blockEnterpriseUidRanges = 125;
        static final int TRANSACTION_buildFirewall = 84;
        static final int TRANSACTION_clearAllFirewallPolicy = 86;
        static final int TRANSACTION_clearDefaultNetId = 110;
        static final int TRANSACTION_clearInterfaceAddresses = 6;
        static final int TRANSACTION_clearPermission = 113;
        static final int TRANSACTION_controlPrivatePacket = 122;
        static final int TRANSACTION_createNetworkGuardChain = 130;
        static final int TRANSACTION_createPhysicalNetwork = 99;
        static final int TRANSACTION_createVirtualNetwork = 100;
        static final int TRANSACTION_delIpAcceptRule = 153;
        static final int TRANSACTION_delSourcePortAcceptRule = 160;
        static final int TRANSACTION_delSourceRoute = 158;
        static final int TRANSACTION_delSrcRoute = 121;
        static final int TRANSACTION_deleteNetworkGuardChain = 131;
        static final int TRANSACTION_deleteNetworkGuardWhiteListRule = 134;
        static final int TRANSACTION_denyProtect = 115;
        static final int TRANSACTION_detachPppd = 43;
        static final int TRANSACTION_disableEpdg = 128;
        static final int TRANSACTION_disableIpv6 = 11;
        static final int TRANSACTION_disableMptcp = 156;
        static final int TRANSACTION_disableNat = 38;
        static final int TRANSACTION_disableNetworkGuard = 133;
        static final int TRANSACTION_enableEpdg = 127;
        static final int TRANSACTION_enableIpv6 = 13;
        static final int TRANSACTION_enableMptcp = 155;
        static final int TRANSACTION_enableNat = 37;
        static final int TRANSACTION_enableNetworkGuard = 132;
        static final int TRANSACTION_flushNetworkDnsCache = 75;
        static final int TRANSACTION_getAccessPointNumConnectedSta = 44;
        static final int TRANSACTION_getAccessPointStaList = 45;
        static final int TRANSACTION_getDnsForwarders = 31;
        static final int TRANSACTION_getInterfaceConfig = 4;
        static final int TRANSACTION_getIpForwardingEnabled = 21;
        static final int TRANSACTION_getNetworkStatsDetail = 56;
        static final int TRANSACTION_getNetworkStatsSummaryDev = 54;
        static final int TRANSACTION_getNetworkStatsSummaryXt = 55;
        static final int TRANSACTION_getNetworkStatsTethering = 58;
        static final int TRANSACTION_getNetworkStatsUidDetail = 57;
        static final int TRANSACTION_getNetworkStatsVideoCall = 69;
        static final int TRANSACTION_getRoutes = 15;
        static final int TRANSACTION_getV6DnsForwarders = 34;
        static final int TRANSACTION_isBandwidthControlEnabled = 66;
        static final int TRANSACTION_isClatdStarted = 95;
        static final int TRANSACTION_isFirewallEnabled = 77;
        static final int TRANSACTION_isNetworkActive = 98;
        static final int TRANSACTION_isTetheringStarted = 25;
        static final int TRANSACTION_joinMulticastGroup = 36;
        static final int TRANSACTION_listInterfaces = 3;
        static final int TRANSACTION_listTetheredInterfaces = 28;
        static final int TRANSACTION_listTtys = 41;
        static final int TRANSACTION_readWhiteList = 47;
        static final int TRANSACTION_registerNetworkActivityListener = 96;
        static final int TRANSACTION_registerObserver = 1;
        static final int TRANSACTION_removeChain = 141;
        static final int TRANSACTION_removeEnterpriseUidRanges = 124;
        static final int TRANSACTION_removeIdleTimer = 71;
        static final int TRANSACTION_removeInterfaceAlert = 62;
        static final int TRANSACTION_removeInterfaceFromLocalNetwork = 117;
        static final int TRANSACTION_removeInterfaceFromNetwork = 103;
        static final int TRANSACTION_removeInterfaceQuota = 60;
        static final int TRANSACTION_removeLegacyRouteForNetId = 105;
        static final int TRANSACTION_removeMptcpLink = 139;
        static final int TRANSACTION_removeNetwork = 101;
        static final int TRANSACTION_removeRoute = 17;
        static final int TRANSACTION_removeSocksRule = 143;
        static final int TRANSACTION_removeSocksSkipRule = 147;
        static final int TRANSACTION_removeSocksSkipRuleProto = 149;
        static final int TRANSACTION_removeUidFromChain = 151;
        static final int TRANSACTION_removeUidSocksRule = 145;
        static final int TRANSACTION_removeUpstreamV6Interface = 40;
        static final int TRANSACTION_removeVpnUidRanges = 92;
        static final int TRANSACTION_replaceDefaultRoute = 20;
        static final int TRANSACTION_replaceSrcRoute = 120;
        static final int TRANSACTION_setAccessPoint = 53;
        static final int TRANSACTION_setAccessPointDisassocSta = 46;
        static final int TRANSACTION_setDefaultInterfaceForDns = 73;
        static final int TRANSACTION_setDefaultNetId = 109;
        static final int TRANSACTION_setDestinationBasedMarkRule = 163;
        static final int TRANSACTION_setDnsForwarders = 29;
        static final int TRANSACTION_setDnsServersForInterface = 74;
        static final int TRANSACTION_setDnsServersForNetwork = 72;
        static final int TRANSACTION_setEpdgInterfaceDropRule = 129;
        static final int TRANSACTION_setFirewallChainEnabled = 83;
        static final int TRANSACTION_setFirewallEgressDestRule = 80;
        static final int TRANSACTION_setFirewallEgressSourceRule = 79;
        static final int TRANSACTION_setFirewallEnabled = 76;
        static final int TRANSACTION_setFirewallInterfaceRule = 78;
        static final int TRANSACTION_setFirewallRuleMobileData = 87;
        static final int TRANSACTION_setFirewallRuleWifi = 88;
        static final int TRANSACTION_setFirewallUidRule = 81;
        static final int TRANSACTION_setFirewallUidRuleMobileData = 89;
        static final int TRANSACTION_setFirewallUidRuleWifi = 90;
        static final int TRANSACTION_setFirewallUidRules = 82;
        static final int TRANSACTION_setGlobalAlert = 63;
        static final int TRANSACTION_setInterfaceAlert = 61;
        static final int TRANSACTION_setInterfaceConfig = 5;
        static final int TRANSACTION_setInterfaceDown = 7;
        static final int TRANSACTION_setInterfaceIpv6NdOffload = 14;
        static final int TRANSACTION_setInterfaceIpv6PrivacyExtensions = 9;
        static final int TRANSACTION_setInterfaceQuota = 59;
        static final int TRANSACTION_setInterfaceUp = 8;
        static final int TRANSACTION_setIpForwardingEnabled = 22;
        static final int TRANSACTION_setMaxClient = 48;
        static final int TRANSACTION_setMtu = 18;
        static final int TRANSACTION_setNetworkGuardProtocolAcceptRule = 137;
        static final int TRANSACTION_setNetworkGuardUidRangeAcceptRule = 135;
        static final int TRANSACTION_setNetworkGuardUidRule = 136;
        static final int TRANSACTION_setNetworkPermission = 111;
        static final int TRANSACTION_setPermission = 112;
        static final int TRANSACTION_setPrivateIpRoute = 162;
        static final int TRANSACTION_setRoamingReductionRules = 108;
        static final int TRANSACTION_setTcpBufferSize = 154;
        static final int TRANSACTION_setTxPower = 49;
        static final int TRANSACTION_setUidCleartextNetworkPolicy = 65;
        static final int TRANSACTION_setUidNetworkRules = 64;
        static final int TRANSACTION_setWhiteListUidNetworkRules = 106;
        static final int TRANSACTION_setWhiteListUrlNetworkRules = 107;
        static final int TRANSACTION_setv6DnsForwarders = 30;
        static final int TRANSACTION_shutdown = 19;
        static final int TRANSACTION_startAccessPoint = 51;
        static final int TRANSACTION_startClatd = 93;
        static final int TRANSACTION_startInterfaceForwarding = 32;
        static final int TRANSACTION_startNetworkStatsOnPorts = 67;
        static final int TRANSACTION_startTethering = 23;
        static final int TRANSACTION_stopAccessPoint = 52;
        static final int TRANSACTION_stopClatd = 94;
        static final int TRANSACTION_stopInterfaceForwarding = 33;
        static final int TRANSACTION_stopNetworkStatsOnPorts = 68;
        static final int TRANSACTION_stopTethering = 24;
        static final int TRANSACTION_tagSocketNMS = 10;
        static final int TRANSACTION_tearDownFirewall = 85;
        static final int TRANSACTION_tetherInterface = 26;
        static final int TRANSACTION_unblockEnterpriseUidRanges = 126;
        static final int TRANSACTION_unregisterNetworkActivityListener = 97;
        static final int TRANSACTION_unregisterObserver = 2;
        static final int TRANSACTION_untetherInterface = 27;
        static final int TRANSACTION_updateRa = 35;
        static final int TRANSACTION_updateSourceRule = 161;
        static final int TRANSACTION_wifiFirmwareReload = 50;
        static final int TRANSACTION_wps_ap_method = 12;

        private static class Proxy implements INetworkManagementService {
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

            public void registerObserver(INetworkManagementEventObserver obs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(obs != null ? obs.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterObserver(INetworkManagementEventObserver obs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(obs != null ? obs.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] listInterfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public InterfaceConfiguration getInterfaceConfig(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    InterfaceConfiguration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (InterfaceConfiguration) InterfaceConfiguration.CREATOR.createFromParcel(_reply);
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

            public void setInterfaceConfig(String iface, InterfaceConfiguration cfg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (cfg != null) {
                        _data.writeInt(1);
                        cfg.writeToParcel(_data, 0);
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

            public void clearInterfaceAddresses(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceDown(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceUp(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceIpv6PrivacyExtensions(String iface, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tagSocketNMS(ParcelFileDescriptor pfd, int tag, int uid) throws RemoteException {
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
                    _data.writeInt(tag);
                    _data.writeInt(uid);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableIpv6(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String wps_ap_method(String type, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeString(pin);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableIpv6(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceIpv6NdOffload(String iface, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RouteInfo[] getRoutes(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    RouteInfo[] _result = (RouteInfo[]) _reply.createTypedArray(RouteInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addRoute(int netId, RouteInfo route) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (route != null) {
                        _data.writeInt(1);
                        route.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeRoute(int netId, RouteInfo route) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (route != null) {
                        _data.writeInt(1);
                        route.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMtu(String iface, int mtu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(mtu);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean replaceDefaultRoute(String iface, byte[] gateway) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeByteArray(gateway);
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

            public boolean getIpForwardingEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public void setIpForwardingEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public void startTethering(String[] dhcpRanges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(dhcpRanges);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopTethering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTetheringStarted() throws RemoteException {
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

            public void tetherInterface(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void untetherInterface(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] listTetheredInterfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDnsForwarders(Network network, String[] dns) throws RemoteException {
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
                    _data.writeStringArray(dns);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setv6DnsForwarders(String[] dns) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(dns);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getDnsForwarders() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startInterfaceForwarding(String fromIface, String toIface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fromIface);
                    _data.writeString(toIface);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopInterfaceForwarding(String fromIface, String toIface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fromIface);
                    _data.writeString(toIface);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getV6DnsForwarders() throws RemoteException {
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

            public void updateRa(String enable, String internalInterface, String externalInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(enable);
                    _data.writeString(internalInterface);
                    _data.writeString(externalInterface);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void joinMulticastGroup(String join, String internalInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(join);
                    _data.writeString(internalInterface);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableNat(String internalInterface, String externalInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(internalInterface);
                    _data.writeString(externalInterface);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableNat(String internalInterface, String externalInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(internalInterface);
                    _data.writeString(externalInterface);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addUpstreamV6Interface(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeUpstreamV6Interface(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] listTtys() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void attachPppd(String tty, String localAddr, String remoteAddr, String dns1Addr, String dns2Addr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tty);
                    _data.writeString(localAddr);
                    _data.writeString(remoteAddr);
                    _data.writeString(dns1Addr);
                    _data.writeString(dns2Addr);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void detachPppd(String tty) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tty);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAccessPointNumConnectedSta() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAccessPointStaList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setAccessPointDisassocSta(String mac) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mac);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int readWhiteList() throws RemoteException {
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

            public int setMaxClient(int num) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(num);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setTxPower(int txPower) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(txPower);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void wifiFirmwareReload(String wlanIface, String mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(wlanIface);
                    _data.writeString(mode);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startAccessPoint(WifiConfiguration wifiConfig, String iface) throws RemoteException {
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
                    _data.writeString(iface);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopAccessPoint(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAccessPoint(WifiConfiguration wifiConfig, String iface) throws RemoteException {
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
                    _data.writeString(iface);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getNetworkStatsSummaryDev() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkStats _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
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

            public NetworkStats getNetworkStatsSummaryXt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkStats _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
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

            public NetworkStats getNetworkStatsDetail() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkStats _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
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

            public NetworkStats getNetworkStatsUidDetail(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkStats _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
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

            public NetworkStats getNetworkStatsTethering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkStats _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
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

            public void setInterfaceQuota(String iface, long quotaBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeLong(quotaBytes);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceQuota(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterfaceAlert(String iface, long alertBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeLong(alertBytes);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceAlert(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGlobalAlert(long alertBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(alertBytes);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUidNetworkRules(int uid, boolean rejectOnQuotaInterfaces) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (rejectOnQuotaInterfaces) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUidCleartextNetworkPolicy(int uid, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(policy);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBandwidthControlEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(66, _data, _reply, 0);
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

            public void startNetworkStatsOnPorts(String iface, int inport, int outport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(inport);
                    _data.writeInt(outport);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopNetworkStatsOnPorts(String iface, int inport, int outport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(inport);
                    _data.writeInt(outport);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getNetworkStatsVideoCall(String iface, int sport, int dport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    NetworkStats _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(sport);
                    _data.writeInt(dport);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
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

            public void addIdleTimer(String iface, int timeout, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(timeout);
                    _data.writeInt(type);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeIdleTimer(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDnsServersForNetwork(int netId, String[] servers, String domains) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeStringArray(servers);
                    _data.writeString(domains);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDefaultInterfaceForDns(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDnsServersForInterface(String iface, String[] servers, String domains) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeStringArray(servers);
                    _data.writeString(domains);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void flushNetworkDnsCache(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFirewallEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(77, _data, _reply, 0);
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

            public void setFirewallInterfaceRule(String iface, boolean allow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (allow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallEgressSourceRule(String addr, boolean allow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(addr);
                    if (allow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallEgressDestRule(String addr, int port, boolean allow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(addr);
                    _data.writeInt(port);
                    if (allow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallUidRule(int chain, int uid, int rule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chain);
                    _data.writeInt(uid);
                    _data.writeInt(rule);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallUidRules(int chain, int[] uids, int[] rules) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chain);
                    _data.writeIntArray(uids);
                    _data.writeIntArray(rules);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallChainEnabled(int chain, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chain);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void buildFirewall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tearDownFirewall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearAllFirewallPolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallRuleMobileData(int uid, boolean allow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (allow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallRuleWifi(int uid, boolean allow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (allow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallUidRuleMobileData(int uid, boolean allow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (allow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFirewallUidRuleWifi(int uid, boolean allow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (allow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addVpnUidRanges(int netId, UidRange[] ranges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeTypedArray(ranges, 0);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeVpnUidRanges(int netId, UidRange[] ranges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeTypedArray(ranges, 0);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startClatd(String interfaceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(interfaceName);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopClatd(String interfaceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(interfaceName);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isClatdStarted(String interfaceName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(interfaceName);
                    this.mRemote.transact(95, _data, _reply, 0);
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

            public void registerNetworkActivityListener(INetworkActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterNetworkActivityListener(INetworkActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNetworkActive() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public void createPhysicalNetwork(int netId, String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(permission);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createVirtualNetwork(int netId, boolean hasDNS, boolean secure) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(hasDNS ? 1 : 0);
                    if (!secure) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeNetwork(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addInterfaceToNetwork(String iface, int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(netId);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceFromNetwork(String iface, int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(netId);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addLegacyRouteForNetId(int netId, RouteInfo routeInfo, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (routeInfo != null) {
                        _data.writeInt(1);
                        routeInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeLegacyRouteForNetId(int netId, RouteInfo routeInfo, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    if (routeInfo != null) {
                        _data.writeInt(1);
                        routeInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWhiteListUidNetworkRules(int uid, boolean allowed) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (allowed) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWhiteListUrlNetworkRules(String ip, String port, int protocol, boolean allowed) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ip);
                    _data.writeString(port);
                    _data.writeInt(protocol);
                    if (allowed) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRoamingReductionRules(boolean allowed) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (allowed) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDefaultNetId(int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearDefaultNetId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkPermission(int netId, String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(permission);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPermission(String permission, int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeIntArray(uids);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearPermission(int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uids);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void allowProtect(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void denyProtect(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addInterfaceToLocalNetwork(String iface, List<RouteInfo> routes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeTypedList(routes);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInterfaceFromLocalNetwork(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void appendInterfaceToLocalNetwork(String iface, List<RouteInfo> routes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeTypedList(routes);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addRouteWithMetric(String iface, int metric, RouteInfo route) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(metric);
                    if (route != null) {
                        _data.writeInt(1);
                        route.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
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

            public boolean replaceSrcRoute(String iface, byte[] ip, byte[] gateway, int routeId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeByteArray(ip);
                    _data.writeByteArray(gateway);
                    _data.writeInt(routeId);
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

            public boolean delSrcRoute(byte[] ip, int routeId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(ip);
                    _data.writeInt(routeId);
                    this.mRemote.transact(121, _data, _reply, 0);
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

            public void controlPrivatePacket(String targetInterface, String address, String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetInterface);
                    _data.writeString(address);
                    _data.writeString(action);
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addEnterpriseUidRanges(UidRange[] ranges, String iface, int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(ranges, 0);
                    _data.writeString(iface);
                    _data.writeInt(netId);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeEnterpriseUidRanges(UidRange[] ranges, String iface, int netId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(ranges, 0);
                    _data.writeString(iface);
                    _data.writeInt(netId);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void blockEnterpriseUidRanges(UidRange[] ranges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(ranges, 0);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unblockEnterpriseUidRanges(UidRange[] ranges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(ranges, 0);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableEpdg(String mobileInterface, String tunnelingInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mobileInterface);
                    _data.writeString(tunnelingInterface);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableEpdg(String mobileInterface, String tunnelingInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mobileInterface);
                    _data.writeString(tunnelingInterface);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setEpdgInterfaceDropRule(String iface, boolean add) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (add) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createNetworkGuardChain() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteNetworkGuardChain() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableNetworkGuard(boolean isBlack) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isBlack) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableNetworkGuard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteNetworkGuardWhiteListRule() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkGuardUidRangeAcceptRule(int uidStart, int uidEnd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uidStart);
                    _data.writeInt(uidEnd);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkGuardUidRule(int uid, boolean mode, boolean isDrop) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mode ? 1 : 0);
                    if (!isDrop) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkGuardProtocolAcceptRule(int protocol) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(protocol);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addMptcpLink(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeMptcpLink(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addChain(String chain, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(ip_type);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeChain(String chain, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(ip_type);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSocksRule(String iface, String chain, String proto, int port) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSocksRule(String iface, String chain, String proto, int port) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addUidSocksRule(String iface, String chain, String proto, int port, int uid, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    _data.writeInt(uid);
                    _data.writeString(ip_type);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeUidSocksRule(String iface, String chain, String proto, int port, int uid, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    _data.writeInt(uid);
                    _data.writeString(ip_type);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSocksSkipRule(String chain, String addr, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(addr);
                    _data.writeString(ip_type);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSocksSkipRule(String chain, String addr, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(addr);
                    _data.writeString(ip_type);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSocksSkipRuleProto(String chain, String addr, String proto, int port, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(addr);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    _data.writeString(ip_type);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSocksSkipRuleProto(String chain, String addr, String proto, int port, String ip_type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(addr);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    _data.writeString(ip_type);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addUidToChain(String chain, String proto, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(uid);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeUidFromChain(String chain, String proto, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(uid);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addIpAcceptRule(String chain, String dest, String proto) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(dest);
                    _data.writeString(proto);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void delIpAcceptRule(String chain, String dest, String proto) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(dest);
                    _data.writeString(proto);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTcpBufferSize(String rmem, String wmem) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rmem);
                    _data.writeString(wmem);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableMptcp(String value, String mobileIface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(value);
                    _data.writeString(mobileIface);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableMptcp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(156, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSourceRoute(String iface, String addr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(addr);
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void delSourceRoute(String iface, String addr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(addr);
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSourcePortAcceptRule(String chain, String proto, int port) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    this.mRemote.transact(159, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void delSourcePortAcceptRule(String chain, String proto, int port) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(chain);
                    _data.writeString(proto);
                    _data.writeInt(port);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSourceRule(boolean add, String ipAddr, String ifaceName) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (add) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(ipAddr);
                    _data.writeString(ifaceName);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPrivateIpRoute(boolean add, String ifaceName, int mark) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (add) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(ifaceName);
                    _data.writeInt(mark);
                    this.mRemote.transact(162, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDestinationBasedMarkRule(boolean add, String addr, String outInterface, int mark) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (add) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(addr);
                    _data.writeString(outInterface);
                    _data.writeInt(mark);
                    this.mRemote.transact(163, _data, _reply, 0);
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

        public static INetworkManagementService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkManagementService)) {
                return new Proxy(obj);
            }
            return (INetworkManagementService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String[] _result;
            String _arg0;
            String _result2;
            int _arg02;
            RouteInfo _arg1;
            boolean _result3;
            int _result4;
            WifiConfiguration _arg03;
            NetworkStats _result5;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    registerObserver(android.net.INetworkManagementEventObserver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterObserver(android.net.INetworkManagementEventObserver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = listInterfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    InterfaceConfiguration _result6 = getInterfaceConfig(data.readString());
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    InterfaceConfiguration _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (InterfaceConfiguration) InterfaceConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    setInterfaceConfig(_arg0, _arg12);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    clearInterfaceAddresses(data.readString());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    setInterfaceDown(data.readString());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setInterfaceUp(data.readString());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setInterfaceIpv6PrivacyExtensions(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 10:
                    ParcelFileDescriptor _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    tagSocketNMS(_arg04, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    disableIpv6(data.readString());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = wps_ap_method(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    enableIpv6(data.readString());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    setInterfaceIpv6NdOffload(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    RouteInfo[] _result7 = getRoutes(data.readString());
                    reply.writeNoException();
                    reply.writeTypedArray(_result7, 1);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    addRoute(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    removeRoute(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    setMtu(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    shutdown();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = replaceDefaultRoute(data.readString(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getIpForwardingEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    setIpForwardingEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    startTethering(data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    stopTethering();
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isTetheringStarted();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    tetherInterface(data.readString());
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    untetherInterface(data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result = listTetheredInterfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                case 29:
                    Network _arg05;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Network) Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    setDnsForwarders(_arg05, data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    setv6DnsForwarders(data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDnsForwarders();
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    startInterfaceForwarding(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    stopInterfaceForwarding(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getV6DnsForwarders();
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    updateRa(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    joinMulticastGroup(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    enableNat(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    disableNat(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    addUpstreamV6Interface(data.readString());
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    removeUpstreamV6Interface(data.readString());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _result = listTtys();
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    attachPppd(data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    detachPppd(data.readString());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getAccessPointNumConnectedSta();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAccessPointStaList();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setAccessPointDisassocSta(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = readWhiteList();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setMaxClient(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setTxPower(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    wifiFirmwareReload(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    startAccessPoint(_arg03, data.readString());
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    stopAccessPoint(data.readString());
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (WifiConfiguration) WifiConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    setAccessPoint(_arg03, data.readString());
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getNetworkStatsSummaryDev();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getNetworkStatsSummaryXt();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getNetworkStatsDetail();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getNetworkStatsUidDetail(data.readInt());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getNetworkStatsTethering();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    setInterfaceQuota(data.readString(), data.readLong());
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    removeInterfaceQuota(data.readString());
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    setInterfaceAlert(data.readString(), data.readLong());
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    removeInterfaceAlert(data.readString());
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    setGlobalAlert(data.readLong());
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    setUidNetworkRules(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    setUidCleartextNetworkPolicy(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isBandwidthControlEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    startNetworkStatsOnPorts(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    stopNetworkStatsOnPorts(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getNetworkStatsVideoCall(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    addIdleTimer(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    removeIdleTimer(data.readString());
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    setDnsServersForNetwork(data.readInt(), data.createStringArray(), data.readString());
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    setDefaultInterfaceForDns(data.readString());
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    setDnsServersForInterface(data.readString(), data.createStringArray(), data.readString());
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    flushNetworkDnsCache(data.readInt());
                    reply.writeNoException();
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isFirewallEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallInterfaceRule(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallEgressSourceRule(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallEgressDestRule(data.readString(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallUidRule(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallUidRules(data.readInt(), data.createIntArray(), data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallChainEnabled(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    buildFirewall();
                    reply.writeNoException();
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    tearDownFirewall();
                    reply.writeNoException();
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    clearAllFirewallPolicy();
                    reply.writeNoException();
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallRuleMobileData(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallRuleWifi(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallUidRuleMobileData(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    setFirewallUidRuleWifi(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    addVpnUidRanges(data.readInt(), (UidRange[]) data.createTypedArray(UidRange.CREATOR));
                    reply.writeNoException();
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    removeVpnUidRanges(data.readInt(), (UidRange[]) data.createTypedArray(UidRange.CREATOR));
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    startClatd(data.readString());
                    reply.writeNoException();
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    stopClatd(data.readString());
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isClatdStarted(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    registerNetworkActivityListener(android.os.INetworkActivityListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterNetworkActivityListener(android.os.INetworkActivityListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isNetworkActive();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    createPhysicalNetwork(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    createVirtualNetwork(data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    removeNetwork(data.readInt());
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    addInterfaceToNetwork(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    removeInterfaceFromNetwork(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    addLegacyRouteForNetId(_arg02, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    removeLegacyRouteForNetId(_arg02, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    setWhiteListUidNetworkRules(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    setWhiteListUrlNetworkRules(data.readString(), data.readString(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    setRoamingReductionRules(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    setDefaultNetId(data.readInt());
                    reply.writeNoException();
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    clearDefaultNetId();
                    reply.writeNoException();
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    setNetworkPermission(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    setPermission(data.readString(), data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    clearPermission(data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    allowProtect(data.readInt());
                    reply.writeNoException();
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    denyProtect(data.readInt());
                    reply.writeNoException();
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    addInterfaceToLocalNetwork(data.readString(), data.createTypedArrayList(RouteInfo.CREATOR));
                    reply.writeNoException();
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    removeInterfaceFromLocalNetwork(data.readString());
                    reply.writeNoException();
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    appendInterfaceToLocalNetwork(data.readString(), data.createTypedArrayList(RouteInfo.CREATOR));
                    reply.writeNoException();
                    return true;
                case 119:
                    RouteInfo _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    int _arg13 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    _result3 = addRouteWithMetric(_arg0, _arg13, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = replaceSrcRoute(data.readString(), data.createByteArray(), data.createByteArray(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = delSrcRoute(data.createByteArray(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    controlPrivatePacket(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    addEnterpriseUidRanges((UidRange[]) data.createTypedArray(UidRange.CREATOR), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    removeEnterpriseUidRanges((UidRange[]) data.createTypedArray(UidRange.CREATOR), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    blockEnterpriseUidRanges((UidRange[]) data.createTypedArray(UidRange.CREATOR));
                    reply.writeNoException();
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    unblockEnterpriseUidRanges((UidRange[]) data.createTypedArray(UidRange.CREATOR));
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    enableEpdg(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    disableEpdg(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    setEpdgInterfaceDropRule(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    createNetworkGuardChain();
                    reply.writeNoException();
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    deleteNetworkGuardChain();
                    reply.writeNoException();
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    enableNetworkGuard(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    disableNetworkGuard();
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    deleteNetworkGuardWhiteListRule();
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    setNetworkGuardUidRangeAcceptRule(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    setNetworkGuardUidRule(data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    setNetworkGuardProtocolAcceptRule(data.readInt());
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    addMptcpLink(data.readString());
                    reply.writeNoException();
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    removeMptcpLink(data.readString());
                    reply.writeNoException();
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    addChain(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    removeChain(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    addSocksRule(data.readString(), data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    removeSocksRule(data.readString(), data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    addUidSocksRule(data.readString(), data.readString(), data.readString(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    removeUidSocksRule(data.readString(), data.readString(), data.readString(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    addSocksSkipRule(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    removeSocksSkipRule(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    addSocksSkipRuleProto(data.readString(), data.readString(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    removeSocksSkipRuleProto(data.readString(), data.readString(), data.readString(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    addUidToChain(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    removeUidFromChain(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    addIpAcceptRule(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    delIpAcceptRule(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    setTcpBufferSize(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    enableMptcp(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    disableMptcp();
                    reply.writeNoException();
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    addSourceRoute(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    delSourceRoute(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    addSourcePortAcceptRule(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    delSourcePortAcceptRule(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    updateSourceRule(data.readInt() != 0, data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    setPrivateIpRoute(data.readInt() != 0, data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    setDestinationBasedMarkRule(data.readInt() != 0, data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addChain(String str, String str2) throws RemoteException;

    void addEnterpriseUidRanges(UidRange[] uidRangeArr, String str, int i) throws RemoteException;

    void addIdleTimer(String str, int i, int i2) throws RemoteException;

    void addInterfaceToLocalNetwork(String str, List<RouteInfo> list) throws RemoteException;

    void addInterfaceToNetwork(String str, int i) throws RemoteException;

    void addIpAcceptRule(String str, String str2, String str3) throws RemoteException;

    void addLegacyRouteForNetId(int i, RouteInfo routeInfo, int i2) throws RemoteException;

    void addMptcpLink(String str) throws RemoteException;

    void addRoute(int i, RouteInfo routeInfo) throws RemoteException;

    boolean addRouteWithMetric(String str, int i, RouteInfo routeInfo) throws RemoteException;

    void addSocksRule(String str, String str2, String str3, int i) throws RemoteException;

    void addSocksSkipRule(String str, String str2, String str3) throws RemoteException;

    void addSocksSkipRuleProto(String str, String str2, String str3, int i, String str4) throws RemoteException;

    void addSourcePortAcceptRule(String str, String str2, int i) throws RemoteException;

    void addSourceRoute(String str, String str2) throws RemoteException;

    void addUidSocksRule(String str, String str2, String str3, int i, int i2, String str4) throws RemoteException;

    void addUidToChain(String str, String str2, int i) throws RemoteException;

    void addUpstreamV6Interface(String str) throws RemoteException;

    void addVpnUidRanges(int i, UidRange[] uidRangeArr) throws RemoteException;

    void allowProtect(int i) throws RemoteException;

    void appendInterfaceToLocalNetwork(String str, List<RouteInfo> list) throws RemoteException;

    void attachPppd(String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void blockEnterpriseUidRanges(UidRange[] uidRangeArr) throws RemoteException;

    void buildFirewall() throws RemoteException;

    void clearAllFirewallPolicy() throws RemoteException;

    void clearDefaultNetId() throws RemoteException;

    void clearInterfaceAddresses(String str) throws RemoteException;

    void clearPermission(int[] iArr) throws RemoteException;

    void controlPrivatePacket(String str, String str2, String str3) throws RemoteException;

    void createNetworkGuardChain() throws RemoteException;

    void createPhysicalNetwork(int i, String str) throws RemoteException;

    void createVirtualNetwork(int i, boolean z, boolean z2) throws RemoteException;

    void delIpAcceptRule(String str, String str2, String str3) throws RemoteException;

    void delSourcePortAcceptRule(String str, String str2, int i) throws RemoteException;

    void delSourceRoute(String str, String str2) throws RemoteException;

    boolean delSrcRoute(byte[] bArr, int i) throws RemoteException;

    void deleteNetworkGuardChain() throws RemoteException;

    void deleteNetworkGuardWhiteListRule() throws RemoteException;

    void denyProtect(int i) throws RemoteException;

    void detachPppd(String str) throws RemoteException;

    void disableEpdg(String str, String str2) throws RemoteException;

    void disableIpv6(String str) throws RemoteException;

    void disableMptcp() throws RemoteException;

    void disableNat(String str, String str2) throws RemoteException;

    void disableNetworkGuard() throws RemoteException;

    void enableEpdg(String str, String str2) throws RemoteException;

    void enableIpv6(String str) throws RemoteException;

    void enableMptcp(String str, String str2) throws RemoteException;

    void enableNat(String str, String str2) throws RemoteException;

    void enableNetworkGuard(boolean z) throws RemoteException;

    void flushNetworkDnsCache(int i) throws RemoteException;

    int getAccessPointNumConnectedSta() throws RemoteException;

    String getAccessPointStaList() throws RemoteException;

    String[] getDnsForwarders() throws RemoteException;

    InterfaceConfiguration getInterfaceConfig(String str) throws RemoteException;

    boolean getIpForwardingEnabled() throws RemoteException;

    NetworkStats getNetworkStatsDetail() throws RemoteException;

    NetworkStats getNetworkStatsSummaryDev() throws RemoteException;

    NetworkStats getNetworkStatsSummaryXt() throws RemoteException;

    NetworkStats getNetworkStatsTethering() throws RemoteException;

    NetworkStats getNetworkStatsUidDetail(int i) throws RemoteException;

    NetworkStats getNetworkStatsVideoCall(String str, int i, int i2) throws RemoteException;

    RouteInfo[] getRoutes(String str) throws RemoteException;

    String[] getV6DnsForwarders() throws RemoteException;

    boolean isBandwidthControlEnabled() throws RemoteException;

    boolean isClatdStarted(String str) throws RemoteException;

    boolean isFirewallEnabled() throws RemoteException;

    boolean isNetworkActive() throws RemoteException;

    boolean isTetheringStarted() throws RemoteException;

    void joinMulticastGroup(String str, String str2) throws RemoteException;

    String[] listInterfaces() throws RemoteException;

    String[] listTetheredInterfaces() throws RemoteException;

    String[] listTtys() throws RemoteException;

    int readWhiteList() throws RemoteException;

    void registerNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException;

    void registerObserver(INetworkManagementEventObserver iNetworkManagementEventObserver) throws RemoteException;

    void removeChain(String str, String str2) throws RemoteException;

    void removeEnterpriseUidRanges(UidRange[] uidRangeArr, String str, int i) throws RemoteException;

    void removeIdleTimer(String str) throws RemoteException;

    void removeInterfaceAlert(String str) throws RemoteException;

    void removeInterfaceFromLocalNetwork(String str) throws RemoteException;

    void removeInterfaceFromNetwork(String str, int i) throws RemoteException;

    void removeInterfaceQuota(String str) throws RemoteException;

    void removeLegacyRouteForNetId(int i, RouteInfo routeInfo, int i2) throws RemoteException;

    void removeMptcpLink(String str) throws RemoteException;

    void removeNetwork(int i) throws RemoteException;

    void removeRoute(int i, RouteInfo routeInfo) throws RemoteException;

    void removeSocksRule(String str, String str2, String str3, int i) throws RemoteException;

    void removeSocksSkipRule(String str, String str2, String str3) throws RemoteException;

    void removeSocksSkipRuleProto(String str, String str2, String str3, int i, String str4) throws RemoteException;

    void removeUidFromChain(String str, String str2, int i) throws RemoteException;

    void removeUidSocksRule(String str, String str2, String str3, int i, int i2, String str4) throws RemoteException;

    void removeUpstreamV6Interface(String str) throws RemoteException;

    void removeVpnUidRanges(int i, UidRange[] uidRangeArr) throws RemoteException;

    boolean replaceDefaultRoute(String str, byte[] bArr) throws RemoteException;

    boolean replaceSrcRoute(String str, byte[] bArr, byte[] bArr2, int i) throws RemoteException;

    void setAccessPoint(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    int setAccessPointDisassocSta(String str) throws RemoteException;

    void setDefaultInterfaceForDns(String str) throws RemoteException;

    void setDefaultNetId(int i) throws RemoteException;

    void setDestinationBasedMarkRule(boolean z, String str, String str2, int i) throws RemoteException;

    void setDnsForwarders(Network network, String[] strArr) throws RemoteException;

    void setDnsServersForInterface(String str, String[] strArr, String str2) throws RemoteException;

    void setDnsServersForNetwork(int i, String[] strArr, String str) throws RemoteException;

    void setEpdgInterfaceDropRule(String str, boolean z) throws RemoteException;

    void setFirewallChainEnabled(int i, boolean z) throws RemoteException;

    void setFirewallEgressDestRule(String str, int i, boolean z) throws RemoteException;

    void setFirewallEgressSourceRule(String str, boolean z) throws RemoteException;

    void setFirewallEnabled(boolean z) throws RemoteException;

    void setFirewallInterfaceRule(String str, boolean z) throws RemoteException;

    void setFirewallRuleMobileData(int i, boolean z) throws RemoteException;

    void setFirewallRuleWifi(int i, boolean z) throws RemoteException;

    void setFirewallUidRule(int i, int i2, int i3) throws RemoteException;

    void setFirewallUidRuleMobileData(int i, boolean z) throws RemoteException;

    void setFirewallUidRuleWifi(int i, boolean z) throws RemoteException;

    void setFirewallUidRules(int i, int[] iArr, int[] iArr2) throws RemoteException;

    void setGlobalAlert(long j) throws RemoteException;

    void setInterfaceAlert(String str, long j) throws RemoteException;

    void setInterfaceConfig(String str, InterfaceConfiguration interfaceConfiguration) throws RemoteException;

    void setInterfaceDown(String str) throws RemoteException;

    void setInterfaceIpv6NdOffload(String str, boolean z) throws RemoteException;

    void setInterfaceIpv6PrivacyExtensions(String str, boolean z) throws RemoteException;

    void setInterfaceQuota(String str, long j) throws RemoteException;

    void setInterfaceUp(String str) throws RemoteException;

    void setIpForwardingEnabled(boolean z) throws RemoteException;

    int setMaxClient(int i) throws RemoteException;

    void setMtu(String str, int i) throws RemoteException;

    void setNetworkGuardProtocolAcceptRule(int i) throws RemoteException;

    void setNetworkGuardUidRangeAcceptRule(int i, int i2) throws RemoteException;

    void setNetworkGuardUidRule(int i, boolean z, boolean z2) throws RemoteException;

    void setNetworkPermission(int i, String str) throws RemoteException;

    void setPermission(String str, int[] iArr) throws RemoteException;

    void setPrivateIpRoute(boolean z, String str, int i) throws RemoteException;

    void setRoamingReductionRules(boolean z) throws RemoteException;

    void setTcpBufferSize(String str, String str2) throws RemoteException;

    int setTxPower(int i) throws RemoteException;

    void setUidCleartextNetworkPolicy(int i, int i2) throws RemoteException;

    void setUidNetworkRules(int i, boolean z) throws RemoteException;

    void setWhiteListUidNetworkRules(int i, boolean z) throws RemoteException;

    void setWhiteListUrlNetworkRules(String str, String str2, int i, boolean z) throws RemoteException;

    void setv6DnsForwarders(String[] strArr) throws RemoteException;

    void shutdown() throws RemoteException;

    void startAccessPoint(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    void startClatd(String str) throws RemoteException;

    void startInterfaceForwarding(String str, String str2) throws RemoteException;

    void startNetworkStatsOnPorts(String str, int i, int i2) throws RemoteException;

    void startTethering(String[] strArr) throws RemoteException;

    void stopAccessPoint(String str) throws RemoteException;

    void stopClatd(String str) throws RemoteException;

    void stopInterfaceForwarding(String str, String str2) throws RemoteException;

    void stopNetworkStatsOnPorts(String str, int i, int i2) throws RemoteException;

    void stopTethering() throws RemoteException;

    void tagSocketNMS(ParcelFileDescriptor parcelFileDescriptor, int i, int i2) throws RemoteException;

    void tearDownFirewall() throws RemoteException;

    void tetherInterface(String str) throws RemoteException;

    void unblockEnterpriseUidRanges(UidRange[] uidRangeArr) throws RemoteException;

    void unregisterNetworkActivityListener(INetworkActivityListener iNetworkActivityListener) throws RemoteException;

    void unregisterObserver(INetworkManagementEventObserver iNetworkManagementEventObserver) throws RemoteException;

    void untetherInterface(String str) throws RemoteException;

    void updateRa(String str, String str2, String str3) throws RemoteException;

    void updateSourceRule(boolean z, String str, String str2) throws RemoteException;

    void wifiFirmwareReload(String str, String str2) throws RemoteException;

    String wps_ap_method(String str, String str2) throws RemoteException;
}
