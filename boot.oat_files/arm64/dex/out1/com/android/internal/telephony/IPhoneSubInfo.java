package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPhoneSubInfo extends IInterface {

    public static abstract class Stub extends Binder implements IPhoneSubInfo {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneSubInfo";
        static final int TRANSACTION_getBtid = 41;
        static final int TRANSACTION_getCompleteVoiceMailNumber = 23;
        static final int TRANSACTION_getCompleteVoiceMailNumberForSubscriber = 24;
        static final int TRANSACTION_getDeviceId = 1;
        static final int TRANSACTION_getDeviceIdForPhone = 3;
        static final int TRANSACTION_getDeviceSvn = 6;
        static final int TRANSACTION_getDeviceSvnUsingSubId = 7;
        static final int TRANSACTION_getDrxValue = 51;
        static final int TRANSACTION_getGroupIdLevel1 = 11;
        static final int TRANSACTION_getGroupIdLevel1ForSubscriber = 12;
        static final int TRANSACTION_getIccSerialNumber = 13;
        static final int TRANSACTION_getIccSerialNumberForSubscriber = 14;
        static final int TRANSACTION_getIccSimChallengeResponse = 37;
        static final int TRANSACTION_getImeiForSubscriber = 4;
        static final int TRANSACTION_getImsRegisteredFeature = 48;
        static final int TRANSACTION_getIsimAid = 39;
        static final int TRANSACTION_getIsimChallengeResponse = 35;
        static final int TRANSACTION_getIsimChallengeResponseForSubscriber = 36;
        static final int TRANSACTION_getIsimDomain = 29;
        static final int TRANSACTION_getIsimDomainForSubscriber = 30;
        static final int TRANSACTION_getIsimImpi = 27;
        static final int TRANSACTION_getIsimImpiForSubscriber = 28;
        static final int TRANSACTION_getIsimImpu = 31;
        static final int TRANSACTION_getIsimImpuForSubscriber = 32;
        static final int TRANSACTION_getIsimIst = 33;
        static final int TRANSACTION_getIsimPcscf = 34;
        static final int TRANSACTION_getKeyLifetime = 42;
        static final int TRANSACTION_getLine1AlphaTag = 17;
        static final int TRANSACTION_getLine1AlphaTagForSubscriber = 18;
        static final int TRANSACTION_getLine1Number = 15;
        static final int TRANSACTION_getLine1NumberForSubscriber = 16;
        static final int TRANSACTION_getLine1NumberType = 56;
        static final int TRANSACTION_getMeidForSubscriber = 5;
        static final int TRANSACTION_getMsisdn = 19;
        static final int TRANSACTION_getMsisdnForSubscriber = 20;
        static final int TRANSACTION_getNaiForSubscriber = 2;
        static final int TRANSACTION_getPsismsc = 45;
        static final int TRANSACTION_getRand = 40;
        static final int TRANSACTION_getReducedCycleTime = 53;
        static final int TRANSACTION_getSktImsiM = 54;
        static final int TRANSACTION_getSktIrm = 55;
        static final int TRANSACTION_getSponImsi = 58;
        static final int TRANSACTION_getSubscriberId = 8;
        static final int TRANSACTION_getSubscriberIdDm = 10;
        static final int TRANSACTION_getSubscriberIdForSubscriber = 9;
        static final int TRANSACTION_getSubscriberIdType = 57;
        static final int TRANSACTION_getVoiceMailAlphaTag = 25;
        static final int TRANSACTION_getVoiceMailAlphaTagForSubscriber = 26;
        static final int TRANSACTION_getVoiceMailNumber = 21;
        static final int TRANSACTION_getVoiceMailNumberForSubscriber = 22;
        static final int TRANSACTION_hasCall = 49;
        static final int TRANSACTION_hasIsim = 44;
        static final int TRANSACTION_isGbaSupported = 38;
        static final int TRANSACTION_isSmoveripSupported = 43;
        static final int TRANSACTION_isVolteRegistered = 46;
        static final int TRANSACTION_isWfcRegistered = 47;
        static final int TRANSACTION_setDrxMode = 50;
        static final int TRANSACTION_setPcoValue = 59;
        static final int TRANSACTION_setReducedCycleTime = 52;

        private static class Proxy implements IPhoneSubInfo {
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

            public String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getNaiForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceIdForPhone(int phoneId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getImeiForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMeidForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceSvn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceSvnUsingSubId(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSubscriberId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSubscriberIdForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSubscriberIdDm(int subId, int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(networkType);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getGroupIdLevel1(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getGroupIdLevel1ForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIccSerialNumber(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIccSerialNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1Number(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1NumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1AlphaTag(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1AlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMsisdn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMsisdnForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVoiceMailNumber(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVoiceMailNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCompleteVoiceMailNumber() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCompleteVoiceMailNumberForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVoiceMailAlphaTag(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVoiceMailAlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIsimImpi() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIsimImpiForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIsimDomain() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIsimDomainForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getIsimImpu() throws RemoteException {
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

            public String[] getIsimImpuForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIsimIst() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getIsimPcscf() throws RemoteException {
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

            public String getIsimChallengeResponse(String nonce) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(nonce);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIsimChallengeResponseForSubscriber(int subId, String nonce) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(nonce);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIccSimChallengeResponse(int subId, int appType, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    _data.writeString(data);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isGbaSupported() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public String getIsimAid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getRand() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getBtid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getKeyLifetime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSmoveripSupported() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
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

            public boolean hasIsim() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
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

            public byte[] getPsismsc() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVolteRegistered() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
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

            public boolean isWfcRegistered() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
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

            public int getImsRegisteredFeature() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasCall(String callType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callType);
                    this.mRemote.transact(49, _data, _reply, 0);
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

            public boolean setDrxMode(int drxMode) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(drxMode);
                    this.mRemote.transact(50, _data, _reply, 0);
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

            public int getDrxValue() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setReducedCycleTime(int cycleTime) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cycleTime);
                    this.mRemote.transact(52, _data, _reply, 0);
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

            public int getReducedCycleTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSktImsiM() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSktIrm() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1NumberType(int SimType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(SimType);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSubscriberIdType(int SimType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(SimType);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getSponImsi() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPcoValue(int newPco) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newPco);
                    this.mRemote.transact(59, _data, _reply, 0);
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

        public static IPhoneSubInfo asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPhoneSubInfo)) {
                return new Proxy(obj);
            }
            return (IPhoneSubInfo) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            String _result;
            String[] _result2;
            boolean _result3;
            byte[] _result4;
            int _result5;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDeviceId(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getNaiForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDeviceIdForPhone(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getImeiForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getMeidForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDeviceSvn(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDeviceSvnUsingSubId(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSubscriberId(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSubscriberIdForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSubscriberIdDm(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getGroupIdLevel1(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getGroupIdLevel1ForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIccSerialNumber(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIccSerialNumberForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLine1Number(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLine1NumberForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLine1AlphaTag(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLine1AlphaTagForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getMsisdn(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getMsisdnForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getVoiceMailNumber(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getVoiceMailNumberForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCompleteVoiceMailNumber();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCompleteVoiceMailNumberForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getVoiceMailAlphaTag(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getVoiceMailAlphaTagForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimImpi();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimImpiForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimDomain();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimDomainForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsimImpu();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsimImpuForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimIst();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getIsimPcscf();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimChallengeResponse(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimChallengeResponseForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIccSimChallengeResponse(data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isGbaSupported();
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIsimAid();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getRand();
                    reply.writeNoException();
                    reply.writeByteArray(_result4);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getBtid();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getKeyLifetime();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isSmoveripSupported();
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = hasIsim();
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPsismsc();
                    reply.writeNoException();
                    reply.writeByteArray(_result4);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isVolteRegistered();
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isWfcRegistered();
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getImsRegisteredFeature();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = hasCall(data.readString());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = setDrxMode(data.readInt());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getDrxValue();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = setReducedCycleTime(data.readInt());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getReducedCycleTime();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSktImsiM();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSktIrm();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getLine1NumberType(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSubscriberIdType(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getSponImsi();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    setPcoValue(data.readInt());
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

    String getBtid() throws RemoteException;

    String getCompleteVoiceMailNumber() throws RemoteException;

    String getCompleteVoiceMailNumberForSubscriber(int i) throws RemoteException;

    String getDeviceId(String str) throws RemoteException;

    String getDeviceIdForPhone(int i, String str) throws RemoteException;

    String getDeviceSvn(String str) throws RemoteException;

    String getDeviceSvnUsingSubId(int i, String str) throws RemoteException;

    int getDrxValue() throws RemoteException;

    String getGroupIdLevel1(String str) throws RemoteException;

    String getGroupIdLevel1ForSubscriber(int i, String str) throws RemoteException;

    String getIccSerialNumber(String str) throws RemoteException;

    String getIccSerialNumberForSubscriber(int i, String str) throws RemoteException;

    String getIccSimChallengeResponse(int i, int i2, String str) throws RemoteException;

    String getImeiForSubscriber(int i, String str) throws RemoteException;

    int getImsRegisteredFeature() throws RemoteException;

    String getIsimAid() throws RemoteException;

    String getIsimChallengeResponse(String str) throws RemoteException;

    String getIsimChallengeResponseForSubscriber(int i, String str) throws RemoteException;

    String getIsimDomain() throws RemoteException;

    String getIsimDomainForSubscriber(int i) throws RemoteException;

    String getIsimImpi() throws RemoteException;

    String getIsimImpiForSubscriber(int i) throws RemoteException;

    String[] getIsimImpu() throws RemoteException;

    String[] getIsimImpuForSubscriber(int i) throws RemoteException;

    String getIsimIst() throws RemoteException;

    String[] getIsimPcscf() throws RemoteException;

    String getKeyLifetime() throws RemoteException;

    String getLine1AlphaTag(String str) throws RemoteException;

    String getLine1AlphaTagForSubscriber(int i, String str) throws RemoteException;

    String getLine1Number(String str) throws RemoteException;

    String getLine1NumberForSubscriber(int i, String str) throws RemoteException;

    String getLine1NumberType(int i) throws RemoteException;

    String getMeidForSubscriber(int i) throws RemoteException;

    String getMsisdn(String str) throws RemoteException;

    String getMsisdnForSubscriber(int i, String str) throws RemoteException;

    String getNaiForSubscriber(int i, String str) throws RemoteException;

    byte[] getPsismsc() throws RemoteException;

    byte[] getRand() throws RemoteException;

    int getReducedCycleTime() throws RemoteException;

    String getSktImsiM() throws RemoteException;

    String getSktIrm() throws RemoteException;

    String[] getSponImsi() throws RemoteException;

    String getSubscriberId(String str) throws RemoteException;

    String getSubscriberIdDm(int i, int i2) throws RemoteException;

    String getSubscriberIdForSubscriber(int i, String str) throws RemoteException;

    String getSubscriberIdType(int i) throws RemoteException;

    String getVoiceMailAlphaTag(String str) throws RemoteException;

    String getVoiceMailAlphaTagForSubscriber(int i, String str) throws RemoteException;

    String getVoiceMailNumber(String str) throws RemoteException;

    String getVoiceMailNumberForSubscriber(int i, String str) throws RemoteException;

    boolean hasCall(String str) throws RemoteException;

    boolean hasIsim() throws RemoteException;

    boolean isGbaSupported() throws RemoteException;

    boolean isSmoveripSupported() throws RemoteException;

    boolean isVolteRegistered() throws RemoteException;

    boolean isWfcRegistered() throws RemoteException;

    boolean setDrxMode(int i) throws RemoteException;

    void setPcoValue(int i) throws RemoteException;

    boolean setReducedCycleTime(int i) throws RemoteException;
}
