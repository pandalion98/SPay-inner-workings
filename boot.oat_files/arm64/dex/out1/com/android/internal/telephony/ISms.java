package com.android.internal.telephony;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ISms extends IInterface {

    public static abstract class Stub extends Binder implements ISms {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ISms";
        static final int TRANSACTION_copyMessageToIccEfForSubscriber = 3;
        static final int TRANSACTION_disableCdmaBroadcast = 42;
        static final int TRANSACTION_disableCdmaBroadcastRange = 44;
        static final int TRANSACTION_disableCellBroadcastForSubscriber = 11;
        static final int TRANSACTION_disableCellBroadcastRangeForSubscriber = 13;
        static final int TRANSACTION_enableCdmaBroadcast = 41;
        static final int TRANSACTION_enableCdmaBroadcastRange = 43;
        static final int TRANSACTION_enableCellBroadcastForSubscriber = 10;
        static final int TRANSACTION_enableCellBroadcastRangeForSubscriber = 12;
        static final int TRANSACTION_getAllMessagesFromIccEfForSubscriber = 1;
        static final int TRANSACTION_getCbSettings = 55;
        static final int TRANSACTION_getCbSettingsForSubscriber = 56;
        static final int TRANSACTION_getImsSmsFormatForSubscriber = 21;
        static final int TRANSACTION_getPreferredSmsSubscription = 20;
        static final int TRANSACTION_getPremiumSmsPermission = 14;
        static final int TRANSACTION_getPremiumSmsPermissionForSubscriber = 15;
        static final int TRANSACTION_getSMSAvailable = 50;
        static final int TRANSACTION_getSMSAvailableForSubscriber = 51;
        static final int TRANSACTION_getSMSPAvailable = 52;
        static final int TRANSACTION_getSimFullStatus = 53;
        static final int TRANSACTION_getSimFullStatusForSubscriber = 54;
        static final int TRANSACTION_getSmsc = 63;
        static final int TRANSACTION_getSmscForSubscriber = 64;
        static final int TRANSACTION_getToddlerMode = 61;
        static final int TRANSACTION_getToddlerModeForSubscriber = 62;
        static final int TRANSACTION_injectSmsPduForSubscriber = 8;
        static final int TRANSACTION_isImsSmsSupportedForSubscriber = 18;
        static final int TRANSACTION_isSMSPromptEnabled = 22;
        static final int TRANSACTION_isSmsSimPickActivityNeeded = 19;
        static final int TRANSACTION_resetSimFullStatus = 57;
        static final int TRANSACTION_resetSimFullStatusForSubscriber = 58;
        static final int TRANSACTION_sendDataForSubscriber = 4;
        static final int TRANSACTION_sendDataForSubscriberWithSelfPermissions = 5;
        static final int TRANSACTION_sendDatawithOrigPort = 27;
        static final int TRANSACTION_sendDatawithOrigPortForSubscriber = 28;
        static final int TRANSACTION_sendMultipartTextForSubscriber = 9;
        static final int TRANSACTION_sendMultipartTextwithCBP = 39;
        static final int TRANSACTION_sendMultipartTextwithCBPForSubscriber = 40;
        static final int TRANSACTION_sendMultipartTextwithOptions = 45;
        static final int TRANSACTION_sendMultipartTextwithOptionsForSubscriber = 46;
        static final int TRANSACTION_sendOTADomestic = 37;
        static final int TRANSACTION_sendOTADomesticForSubscriber = 38;
        static final int TRANSACTION_sendRawPduSat = 49;
        static final int TRANSACTION_sendStoredMultipartText = 24;
        static final int TRANSACTION_sendStoredText = 23;
        static final int TRANSACTION_sendTextForSubscriber = 6;
        static final int TRANSACTION_sendTextForSubscriberWithSelfPermissions = 7;
        static final int TRANSACTION_sendTextKdi = 30;
        static final int TRANSACTION_sendTextNSRI = 47;
        static final int TRANSACTION_sendTextNSRIForSubscriber = 48;
        static final int TRANSACTION_sendTextWithPriority = 29;
        static final int TRANSACTION_sendTextwithCBP = 31;
        static final int TRANSACTION_sendTextwithCBPForSubscriber = 32;
        static final int TRANSACTION_sendTextwithOptions = 33;
        static final int TRANSACTION_sendTextwithOptionsForSubscriber = 34;
        static final int TRANSACTION_sendTextwithOptionsReadconfirm = 35;
        static final int TRANSACTION_sendTextwithOptionsReadconfirmForSubscriber = 36;
        static final int TRANSACTION_sendscptResult = 59;
        static final int TRANSACTION_setCDMASmsReassembly = 60;
        static final int TRANSACTION_setPremiumSmsPermission = 16;
        static final int TRANSACTION_setPremiumSmsPermissionForSubscriber = 17;
        static final int TRANSACTION_updateMessageOnIccEfForSubscriber = 2;
        static final int TRANSACTION_updateSmsServiceCenterOnSimEf = 25;
        static final int TRANSACTION_updateSmsServiceCenterOnSimEfForSubscriber = 26;
        static final int TRANSACTION_useLte3GPPSms = 65;

        private static class Proxy implements ISms {
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

            public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int subId, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<SmsRawData> _result = _reply.createTypedArrayList(SmsRawData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateMessageOnIccEfForSubscriber(int subId, String callingPkg, int messageIndex, int newStatus, byte[] pdu) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeInt(messageIndex);
                    _data.writeInt(newStatus);
                    _data.writeByteArray(pdu);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            public boolean copyMessageToIccEfForSubscriber(int subId, String callingPkg, int status, byte[] pdu, byte[] smsc) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeInt(status);
                    _data.writeByteArray(pdu);
                    _data.writeByteArray(smsc);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public void sendDataForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeInt(destPort);
                    _data.writeByteArray(data);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendDataForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeInt(destPort);
                    _data.writeByteArray(data);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
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

            public void sendTextForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!persistMessageForNonDefaultSmsApp) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void injectSmsPduForSubscriber(int subId, byte[] pdu, String format, PendingIntent receivedIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeByteArray(pdu);
                    _data.writeString(format);
                    if (receivedIntent != null) {
                        _data.writeInt(1);
                        receivedIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendMultipartTextForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeStringList(parts);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    if (persistMessageForNonDefaultSmsApp) {
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

            public boolean enableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(messageIdentifier);
                    _data.writeInt(ranType);
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

            public boolean disableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(messageIdentifier);
                    _data.writeInt(ranType);
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

            public boolean enableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
                    _data.writeInt(ranType);
                    this.mRemote.transact(12, _data, _reply, 0);
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

            public boolean disableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
                    _data.writeInt(ranType);
                    this.mRemote.transact(13, _data, _reply, 0);
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

            public int getPremiumSmsPermission(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPremiumSmsPermissionForSubscriber(int subId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(packageName);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPremiumSmsPermission(String packageName, int permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(permission);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPremiumSmsPermissionForSubscriber(int subId, String packageName, int permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(packageName);
                    _data.writeInt(permission);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isImsSmsSupportedForSubscriber(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(18, _data, _reply, 0);
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

            public boolean isSmsSimPickActivityNeeded(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
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

            public int getPreferredSmsSubscription() throws RemoteException {
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

            public String getImsSmsFormatForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSMSPromptEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
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

            public void sendStoredText(int subId, String callingPkg, Uri messageUri, String scAddress, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(scAddress);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendStoredMultipartText(int subId, String callingPkg, Uri messageUri, String scAddress, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(scAddress);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateSmsServiceCenterOnSimEf(byte[] smsc) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(smsc);
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

            public boolean updateSmsServiceCenterOnSimEfForSubscriber(int subId, byte[] smsc) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeByteArray(smsc);
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

            public void sendDatawithOrigPort(String callingPkg, String destAddr, String scAddr, int destPort, int origPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeInt(destPort);
                    _data.writeInt(origPort);
                    _data.writeByteArray(data);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendDatawithOrigPortForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, int origPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeInt(destPort);
                    _data.writeInt(origPort);
                    _data.writeByteArray(data);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextWithPriority(String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(priority);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextKdi(String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean is7bitAlphabet) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!is7bitAlphabet) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextwithCBP(String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, String callbackNumber, int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callbackNumber);
                    _data.writeInt(priority);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextwithCBPForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, String callbackNumber, int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callbackNumber);
                    _data.writeInt(priority);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextwithOptions(String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean replyPath, int expiry, int serviceType, int encodingType) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!replyPath) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(expiry);
                    _data.writeInt(serviceType);
                    _data.writeInt(encodingType);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextwithOptionsForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean replyPath, int expiry, int serviceType, int encodingType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(replyPath ? 1 : 0);
                    _data.writeInt(expiry);
                    _data.writeInt(serviceType);
                    _data.writeInt(encodingType);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextwithOptionsReadconfirm(String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean replyPath, int expiry, int serviceType, int encodingType, int confirmID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(replyPath ? 1 : 0);
                    _data.writeInt(expiry);
                    _data.writeInt(serviceType);
                    _data.writeInt(encodingType);
                    _data.writeInt(confirmID);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextwithOptionsReadconfirmForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean replyPath, int expiry, int serviceType, int encodingType, int confirmID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(replyPath ? 1 : 0);
                    _data.writeInt(expiry);
                    _data.writeInt(serviceType);
                    _data.writeInt(encodingType);
                    _data.writeInt(confirmID);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendOTADomestic(String callingPkg, String destinationAddress, String scAddress, String text) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeString(text);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendOTADomesticForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, String text) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeString(text);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendMultipartTextwithCBP(String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, String callbackNumber, int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeStringList(parts);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    _data.writeString(callbackNumber);
                    _data.writeInt(priority);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendMultipartTextwithCBPForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, String callbackNumber, int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeStringList(parts);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    _data.writeString(callbackNumber);
                    _data.writeInt(priority);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableCdmaBroadcast(int messageIdentifier) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageIdentifier);
                    this.mRemote.transact(41, _data, _reply, 0);
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

            public boolean disableCdmaBroadcast(int messageIdentifier) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageIdentifier);
                    this.mRemote.transact(42, _data, _reply, 0);
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

            public boolean enableCdmaBroadcastRange(int startMessageId, int endMessageId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
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

            public boolean disableCdmaBroadcastRange(int startMessageId, int endMessageId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
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

            public void sendMultipartTextwithOptions(String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean replyPath, int expiry, int serviceType, int encodingType) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeStringList(parts);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    if (replyPath) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(expiry);
                    _data.writeInt(serviceType);
                    _data.writeInt(encodingType);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendMultipartTextwithOptionsForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean replyPath, int expiry, int serviceType, int encodingType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeStringList(parts);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    _data.writeInt(replyPath ? 1 : 0);
                    _data.writeInt(expiry);
                    _data.writeInt(serviceType);
                    _data.writeInt(encodingType);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextNSRI(String callingPkg, String destAddr, String scAddr, byte[] text, PendingIntent sentIntent, PendingIntent deliveryIntent, int msgCount, int msgTotal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeByteArray(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(msgCount);
                    _data.writeInt(msgTotal);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendTextNSRIForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, byte[] text, PendingIntent sentIntent, PendingIntent deliveryIntent, int msgCount, int msgTotal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeByteArray(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(msgCount);
                    _data.writeInt(msgTotal);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendRawPduSat(byte[] smsc, byte[] pdu, String format, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(smsc);
                    _data.writeByteArray(pdu);
                    _data.writeString(format);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getSMSAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean getSMSAvailableForSubscriber(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
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

            public boolean getSMSPAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean getSimFullStatus() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(53, _data, _reply, 0);
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

            public boolean getSimFullStatusForSubscriber(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(54, _data, _reply, 0);
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

            public byte[] getCbSettings() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getCbSettingsForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetSimFullStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetSimFullStatusForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendscptResult(String destinationAddress, int NoOfOccur, int Category, int Language, int categoryResult, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(destinationAddress);
                    _data.writeInt(NoOfOccur);
                    _data.writeInt(Category);
                    _data.writeInt(Language);
                    _data.writeInt(categoryResult);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCDMASmsReassembly(boolean onOff) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (onOff) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getToddlerMode() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(61, _data, _reply, 0);
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

            public boolean getToddlerModeForSubscriber(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(62, _data, _reply, 0);
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

            public String getSmsc() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSmscForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean useLte3GPPSms() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(65, _data, _reply, 0);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISms)) {
                return new Proxy(obj);
            }
            return (ISms) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _result;
            int _arg0;
            String _arg1;
            String _arg2;
            String _arg3;
            int _arg4;
            byte[] _arg5;
            PendingIntent _arg6;
            PendingIntent _arg7;
            String _arg42;
            PendingIntent _arg52;
            byte[] _arg12;
            PendingIntent _arg32;
            int _result2;
            String _result3;
            Uri _arg22;
            PendingIntent _arg43;
            String _arg02;
            int _arg33;
            byte[] _result4;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    List<SmsRawData> _result5 = getAllMessagesFromIccEfForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = updateMessageOnIccEfForSubscriber(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = copyMessageToIccEfForSubscriber(data.readInt(), data.readString(), data.readInt(), data.createByteArray(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readInt();
                    _arg5 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg7 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    sendDataForSubscriber(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readInt();
                    _arg5 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg7 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    sendDataForSubscriberWithSelfPermissions(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg42 = data.readString();
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendTextForSubscriber(_arg0, _arg1, _arg2, _arg3, _arg42, _arg52, _arg6, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg42 = data.readString();
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendTextForSubscriberWithSelfPermissions(_arg0, _arg1, _arg2, _arg3, _arg42, _arg52, _arg6);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg12 = data.createByteArray();
                    _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg32 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    injectSmsPduForSubscriber(_arg0, _arg12, _arg2, _arg32);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    sendMultipartTextForSubscriber(data.readInt(), data.readString(), data.readString(), data.readString(), data.createStringArrayList(), data.createTypedArrayList(PendingIntent.CREATOR), data.createTypedArrayList(PendingIntent.CREATOR), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enableCellBroadcastForSubscriber(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result = disableCellBroadcastForSubscriber(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enableCellBroadcastRangeForSubscriber(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = disableCellBroadcastRangeForSubscriber(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPremiumSmsPermission(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPremiumSmsPermissionForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    setPremiumSmsPermission(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    setPremiumSmsPermissionForSubscriber(data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isImsSmsSupportedForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isSmsSimPickActivityNeeded(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPreferredSmsSubscription();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getImsSmsFormatForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isSMSPromptEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    _arg3 = data.readString();
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    sendStoredText(_arg0, _arg1, _arg22, _arg3, _arg43, _arg52);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    sendStoredMultipartText(_arg0, _arg1, _arg22, data.readString(), data.createTypedArrayList(PendingIntent.CREATOR), data.createTypedArrayList(PendingIntent.CREATOR));
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result = updateSmsServiceCenterOnSimEf(data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result = updateSmsServiceCenterOnSimEfForSubscriber(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg33 = data.readInt();
                    _arg4 = data.readInt();
                    _arg5 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg7 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    sendDatawithOrigPort(_arg02, _arg1, _arg2, _arg33, _arg4, _arg5, _arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 28:
                    PendingIntent _arg8;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readInt();
                    int _arg53 = data.readInt();
                    byte[] _arg62 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg7 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg8 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg8 = null;
                    }
                    sendDatawithOrigPortForSubscriber(_arg0, _arg1, _arg2, _arg3, _arg4, _arg53, _arg62, _arg7, _arg8);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg32 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    sendTextWithPriority(_arg02, _arg1, _arg2, _arg32, _arg43, data.readInt());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg32 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    sendTextKdi(_arg02, _arg1, _arg2, _arg32, _arg43, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    sendTextwithCBP(_arg02, _arg1, _arg2, _arg3, _arg43, _arg52, data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg42 = data.readString();
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendTextwithCBPForSubscriber(_arg0, _arg1, _arg2, _arg3, _arg42, _arg52, _arg6, data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    sendTextwithOptions(_arg02, _arg1, _arg2, _arg3, _arg43, _arg52, data.readInt() != 0, data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg42 = data.readString();
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendTextwithOptionsForSubscriber(_arg0, _arg1, _arg2, _arg3, _arg42, _arg52, _arg6, data.readInt() != 0, data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    sendTextwithOptionsReadconfirm(_arg02, _arg1, _arg2, _arg3, _arg43, _arg52, data.readInt() != 0, data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg42 = data.readString();
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendTextwithOptionsReadconfirmForSubscriber(_arg0, _arg1, _arg2, _arg3, _arg42, _arg52, _arg6, data.readInt() != 0, data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    sendOTADomestic(data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    sendOTADomesticForSubscriber(data.readInt(), data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    sendMultipartTextwithCBP(data.readString(), data.readString(), data.readString(), data.createStringArrayList(), data.createTypedArrayList(PendingIntent.CREATOR), data.createTypedArrayList(PendingIntent.CREATOR), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    sendMultipartTextwithCBPForSubscriber(data.readInt(), data.readString(), data.readString(), data.readString(), data.createStringArrayList(), data.createTypedArrayList(PendingIntent.CREATOR), data.createTypedArrayList(PendingIntent.CREATOR), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enableCdmaBroadcast(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result = disableCdmaBroadcast(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enableCdmaBroadcastRange(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result = disableCdmaBroadcastRange(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    sendMultipartTextwithOptions(data.readString(), data.readString(), data.readString(), data.createStringArrayList(), data.createTypedArrayList(PendingIntent.CREATOR), data.createTypedArrayList(PendingIntent.CREATOR), data.readInt() != 0, data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    sendMultipartTextwithOptionsForSubscriber(data.readInt(), data.readString(), data.readString(), data.readString(), data.createStringArrayList(), data.createTypedArrayList(PendingIntent.CREATOR), data.createTypedArrayList(PendingIntent.CREATOR), data.readInt() != 0, data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    byte[] _arg34 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    sendTextNSRI(_arg02, _arg1, _arg2, _arg34, _arg43, _arg52, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    byte[] _arg44 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendTextNSRIForSubscriber(_arg0, _arg1, _arg2, _arg3, _arg44, _arg52, _arg6, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg03 = data.createByteArray();
                    _arg12 = data.createByteArray();
                    _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg32 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg43 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    sendRawPduSat(_arg03, _arg12, _arg2, _arg32, _arg43);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSMSAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSMSAvailableForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSMSPAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSimFullStatus();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSimFullStatusForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCbSettings();
                    reply.writeNoException();
                    reply.writeByteArray(_result4);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCbSettingsForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result4);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    resetSimFullStatus();
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    resetSimFullStatusForSubscriber(data.readInt());
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    int _arg13 = data.readInt();
                    int _arg23 = data.readInt();
                    _arg33 = data.readInt();
                    _arg4 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    sendscptResult(_arg02, _arg13, _arg23, _arg33, _arg4, _arg52, _arg6);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    setCDMASmsReassembly(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getToddlerMode();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getToddlerModeForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSmsc();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSmscForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    _result = useLte3GPPSms();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean copyMessageToIccEfForSubscriber(int i, String str, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    boolean disableCdmaBroadcast(int i) throws RemoteException;

    boolean disableCdmaBroadcastRange(int i, int i2) throws RemoteException;

    boolean disableCellBroadcastForSubscriber(int i, int i2, int i3) throws RemoteException;

    boolean disableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) throws RemoteException;

    boolean enableCdmaBroadcast(int i) throws RemoteException;

    boolean enableCdmaBroadcastRange(int i, int i2) throws RemoteException;

    boolean enableCellBroadcastForSubscriber(int i, int i2, int i3) throws RemoteException;

    boolean enableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) throws RemoteException;

    List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int i, String str) throws RemoteException;

    byte[] getCbSettings() throws RemoteException;

    byte[] getCbSettingsForSubscriber(int i) throws RemoteException;

    String getImsSmsFormatForSubscriber(int i) throws RemoteException;

    int getPreferredSmsSubscription() throws RemoteException;

    int getPremiumSmsPermission(String str) throws RemoteException;

    int getPremiumSmsPermissionForSubscriber(int i, String str) throws RemoteException;

    boolean getSMSAvailable() throws RemoteException;

    boolean getSMSAvailableForSubscriber(int i) throws RemoteException;

    boolean getSMSPAvailable() throws RemoteException;

    boolean getSimFullStatus() throws RemoteException;

    boolean getSimFullStatusForSubscriber(int i) throws RemoteException;

    String getSmsc() throws RemoteException;

    String getSmscForSubscriber(int i) throws RemoteException;

    boolean getToddlerMode() throws RemoteException;

    boolean getToddlerModeForSubscriber(int i) throws RemoteException;

    void injectSmsPduForSubscriber(int i, byte[] bArr, String str, PendingIntent pendingIntent) throws RemoteException;

    boolean isImsSmsSupportedForSubscriber(int i) throws RemoteException;

    boolean isSMSPromptEnabled() throws RemoteException;

    boolean isSmsSimPickActivityNeeded(int i) throws RemoteException;

    void resetSimFullStatus() throws RemoteException;

    void resetSimFullStatusForSubscriber(int i) throws RemoteException;

    void sendDataForSubscriber(int i, String str, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendDataForSubscriberWithSelfPermissions(int i, String str, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendDatawithOrigPort(String str, String str2, String str3, int i, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendDatawithOrigPortForSubscriber(int i, String str, String str2, String str3, int i2, int i3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendMultipartTextForSubscriber(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z) throws RemoteException;

    void sendMultipartTextwithCBP(String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, String str4, int i) throws RemoteException;

    void sendMultipartTextwithCBPForSubscriber(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, String str4, int i2) throws RemoteException;

    void sendMultipartTextwithOptions(String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, int i, int i2, int i3) throws RemoteException;

    void sendMultipartTextwithOptionsForSubscriber(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, int i2, int i3, int i4) throws RemoteException;

    void sendOTADomestic(String str, String str2, String str3, String str4) throws RemoteException;

    void sendOTADomesticForSubscriber(int i, String str, String str2, String str3, String str4) throws RemoteException;

    void sendRawPduSat(byte[] bArr, byte[] bArr2, String str, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendStoredMultipartText(int i, String str, Uri uri, String str2, List<PendingIntent> list, List<PendingIntent> list2) throws RemoteException;

    void sendStoredText(int i, String str, Uri uri, String str2, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendTextForSubscriber(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    void sendTextForSubscriberWithSelfPermissions(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendTextKdi(String str, String str2, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    void sendTextNSRI(String str, String str2, String str3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i, int i2) throws RemoteException;

    void sendTextNSRIForSubscriber(int i, String str, String str2, String str3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i2, int i3) throws RemoteException;

    void sendTextWithPriority(String str, String str2, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i) throws RemoteException;

    void sendTextwithCBP(String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str5, int i) throws RemoteException;

    void sendTextwithCBPForSubscriber(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str5, int i2) throws RemoteException;

    void sendTextwithOptions(String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i, int i2, int i3) throws RemoteException;

    void sendTextwithOptionsForSubscriber(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, int i3, int i4) throws RemoteException;

    void sendTextwithOptionsReadconfirm(String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i, int i2, int i3, int i4) throws RemoteException;

    void sendTextwithOptionsReadconfirmForSubscriber(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, int i3, int i4, int i5) throws RemoteException;

    void sendscptResult(String str, int i, int i2, int i3, int i4, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void setCDMASmsReassembly(boolean z) throws RemoteException;

    void setPremiumSmsPermission(String str, int i) throws RemoteException;

    void setPremiumSmsPermissionForSubscriber(int i, String str, int i2) throws RemoteException;

    boolean updateMessageOnIccEfForSubscriber(int i, String str, int i2, int i3, byte[] bArr) throws RemoteException;

    boolean updateSmsServiceCenterOnSimEf(byte[] bArr) throws RemoteException;

    boolean updateSmsServiceCenterOnSimEfForSubscriber(int i, byte[] bArr) throws RemoteException;

    boolean useLte3GPPSms() throws RemoteException;
}
