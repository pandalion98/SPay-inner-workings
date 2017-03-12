package com.android.internal.telephony;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccount;
import android.telephony.CellInfo;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.ModemActivityInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import java.util.List;

public interface ITelephony extends IInterface {

    public static abstract class Stub extends Binder implements ITelephony {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephony";
        static final int TRANSACTION_IsDomesticRoaming = 160;
        static final int TRANSACTION_IsInternationalRoaming = 159;
        static final int TRANSACTION_NSRI_requestProc = 203;
        static final int TRANSACTION_SimSlotActivation = 180;
        static final int TRANSACTION_SimSlotOnOff = 179;
        static final int TRANSACTION_answerRingingCall = 8;
        static final int TRANSACTION_answerRingingCallForSubscriber = 9;
        static final int TRANSACTION_calculateAkaResponse = 185;
        static final int TRANSACTION_calculateGbaBootstrappingResponse = 186;
        static final int TRANSACTION_calculateNafExternalKey = 187;
        static final int TRANSACTION_call = 4;
        static final int TRANSACTION_callForSubscriber = 5;
        static final int TRANSACTION_canChangeDtmfToneLength = 120;
        static final int TRANSACTION_checkCarrierPrivilegesForPackage = 104;
        static final int TRANSACTION_checkCarrierPrivilegesForPackageAnyPhone = 105;
        static final int TRANSACTION_checkNSRIUSIMstate_int = 202;
        static final int TRANSACTION_dial = 2;
        static final int TRANSACTION_dialForSubscriber = 3;
        static final int TRANSACTION_disableDataConnectivity = 42;
        static final int TRANSACTION_disableLocationUpdates = 39;
        static final int TRANSACTION_disableLocationUpdatesForSubscriber = 40;
        static final int TRANSACTION_enableDataConnectivity = 41;
        static final int TRANSACTION_enableLocationUpdates = 37;
        static final int TRANSACTION_enableLocationUpdatesForSubscriber = 38;
        static final int TRANSACTION_enableVideoCalling = 118;
        static final int TRANSACTION_endCall = 6;
        static final int TRANSACTION_endCallForSubscriber = 7;
        static final int TRANSACTION_factoryReset = 131;
        static final int TRANSACTION_getActiveFgCallState = 210;
        static final int TRANSACTION_getActivePhoneType = 51;
        static final int TRANSACTION_getActivePhoneTypeForSubscriber = 52;
        static final int TRANSACTION_getAllCellInfo = 72;
        static final int TRANSACTION_getAtr = 148;
        static final int TRANSACTION_getAtrUsingSlotId = 149;
        static final int TRANSACTION_getCalculatedPreferredNetworkType = 90;
        static final int TRANSACTION_getCallState = 46;
        static final int TRANSACTION_getCallStateForSubscriber = 47;
        static final int TRANSACTION_getCarrierPackageNamesForIntentAndPhone = 106;
        static final int TRANSACTION_getCarrierPrivilegeStatus = 103;
        static final int TRANSACTION_getCdmaEriIconIndex = 53;
        static final int TRANSACTION_getCdmaEriIconIndexForSubscriber = 54;
        static final int TRANSACTION_getCdmaEriIconMode = 55;
        static final int TRANSACTION_getCdmaEriIconModeForSubscriber = 56;
        static final int TRANSACTION_getCdmaEriText = 57;
        static final int TRANSACTION_getCdmaEriTextForSubscriber = 58;
        static final int TRANSACTION_getCdmaMdn = 101;
        static final int TRANSACTION_getCdmaMin = 102;
        static final int TRANSACTION_getCellLocation = 44;
        static final int TRANSACTION_getCellNetworkScanResults = 94;
        static final int TRANSACTION_getCurrentUATI = 169;
        static final int TRANSACTION_getDataActivity = 48;
        static final int TRANSACTION_getDataEnabled = 98;
        static final int TRANSACTION_getDataNetworkType = 65;
        static final int TRANSACTION_getDataNetworkTypeForSubscriber = 66;
        static final int TRANSACTION_getDataRoamingEnabled = 137;
        static final int TRANSACTION_getDataRoamingEnabledUsingSubID = 138;
        static final int TRANSACTION_getDataServiceState = 146;
        static final int TRANSACTION_getDataServiceStateUsingSubId = 147;
        static final int TRANSACTION_getDataState = 49;
        static final int TRANSACTION_getDataStateSimSlot = 50;
        static final int TRANSACTION_getDefaultSim = 74;
        static final int TRANSACTION_getDeviceId = 129;
        static final int TRANSACTION_getDisable2g = 212;
        static final int TRANSACTION_getEuimid = 206;
        static final int TRANSACTION_getFeliCaUimLockStatus = 155;
        static final int TRANSACTION_getHandsetInfo = 171;
        static final int TRANSACTION_getImei = 152;
        static final int TRANSACTION_getIpAddressFromLinkProp = 163;
        static final int TRANSACTION_getLgt3GDataStatus = 173;
        static final int TRANSACTION_getLgtOzStartPage = 174;
        static final int TRANSACTION_getLine1AlphaTagForDisplay = 109;
        static final int TRANSACTION_getLine1NumberForDisplay = 108;
        static final int TRANSACTION_getLocaleFromDefaultSim = 132;
        static final int TRANSACTION_getLteCellInfo = 168;
        static final int TRANSACTION_getLteOnCdmaMode = 70;
        static final int TRANSACTION_getLteOnCdmaModeForSubscriber = 71;
        static final int TRANSACTION_getMeid = 151;
        static final int TRANSACTION_getMergedSubscriberIds = 110;
        static final int TRANSACTION_getMobileQualityInformation = 162;
        static final int TRANSACTION_getModemActivityInfo = 133;
        static final int TRANSACTION_getMultiSimForegroundPhoneId = 193;
        static final int TRANSACTION_getMultiSimLastRejectIncomingCallPhoneId = 191;
        static final int TRANSACTION_getNeighboringCellInfo = 45;
        static final int TRANSACTION_getNetworkType = 63;
        static final int TRANSACTION_getNetworkTypeForSubscriber = 64;
        static final int TRANSACTION_getPcscfAddress = 99;
        static final int TRANSACTION_getPhoneServiceState = 172;
        static final int TRANSACTION_getPreferredNetworkType = 91;
        static final int TRANSACTION_getRadioAccessFamily = 117;
        static final int TRANSACTION_getSdnAvailable = 158;
        static final int TRANSACTION_getSelectedApn = 140;
        static final int TRANSACTION_getServiceState = 144;
        static final int TRANSACTION_getServiceStateUsingSubId = 145;
        static final int TRANSACTION_getSimPinRetry = 153;
        static final int TRANSACTION_getSimPinRetryForSubscriber = 176;
        static final int TRANSACTION_getSimPukRetry = 157;
        static final int TRANSACTION_getSimPukRetryForSubscriber = 177;
        static final int TRANSACTION_getSubIdForPhoneAccount = 130;
        static final int TRANSACTION_getTetherApnRequired = 92;
        static final int TRANSACTION_getTimeInfo = 166;
        static final int TRANSACTION_getVoiceMessageCount = 61;
        static final int TRANSACTION_getVoiceMessageCountForSubscriber = 62;
        static final int TRANSACTION_getVoiceNetworkTypeForSubscriber = 67;
        static final int TRANSACTION_getVoicePhoneServiceState = 142;
        static final int TRANSACTION_getVoicePhoneServiceStateUsingSubId = 143;
        static final int TRANSACTION_getWipiSysInfo = 167;
        static final int TRANSACTION_handlePinMmi = 28;
        static final int TRANSACTION_handlePinMmiForSubscriber = 29;
        static final int TRANSACTION_hasIccCard = 68;
        static final int TRANSACTION_hasIccCardUsingSlotId = 69;
        static final int TRANSACTION_iccCloseLogicalChannel = 77;
        static final int TRANSACTION_iccCloseLogicalChannelUsingSlotId = 78;
        static final int TRANSACTION_iccExchangeSimIO = 83;
        static final int TRANSACTION_iccExchangeSimIOUsingSlotId = 84;
        static final int TRANSACTION_iccOpenLogicalChannel = 75;
        static final int TRANSACTION_iccOpenLogicalChannelUsingSlotId = 76;
        static final int TRANSACTION_iccTransmitApduBasicChannel = 81;
        static final int TRANSACTION_iccTransmitApduBasicChannelUsingSlotId = 82;
        static final int TRANSACTION_iccTransmitApduLogicalChannel = 79;
        static final int TRANSACTION_iccTransmitApduLogicalChannelUsingSlotId = 80;
        static final int TRANSACTION_invokeOemRilRequestRaw = 113;
        static final int TRANSACTION_invokeOemRilRequestRawForSubscriber = 211;
        static final int TRANSACTION_isApnTypeAvailable = 204;
        static final int TRANSACTION_isApnTypeAvailableUsingSubId = 205;
        static final int TRANSACTION_isDataConnectivityPossible = 43;
        static final int TRANSACTION_isDualBTConnection = 198;
        static final int TRANSACTION_isHearingAidCompatibilitySupported = 123;
        static final int TRANSACTION_isIdle = 15;
        static final int TRANSACTION_isIdleForSubscriber = 16;
        static final int TRANSACTION_isImsCall = 194;
        static final int TRANSACTION_isImsRegistered = 124;
        static final int TRANSACTION_isOffhook = 11;
        static final int TRANSACTION_isOffhookForSubscriber = 12;
        static final int TRANSACTION_isRadioOn = 17;
        static final int TRANSACTION_isRadioOnForSubscriber = 18;
        static final int TRANSACTION_isRinging = 14;
        static final int TRANSACTION_isRingingForSubscriber = 13;
        static final int TRANSACTION_isSimFDNEnabled = 135;
        static final int TRANSACTION_isSimFDNEnabledForSubscriber = 136;
        static final int TRANSACTION_isSimPinEnabled = 19;
        static final int TRANSACTION_isTtyModeSupported = 122;
        static final int TRANSACTION_isVideoCall = 134;
        static final int TRANSACTION_isVideoCallingEnabled = 119;
        static final int TRANSACTION_isVideoTelephonyAvailable = 127;
        static final int TRANSACTION_isVolteAvailable = 126;
        static final int TRANSACTION_isWifiCallingAvailable = 125;
        static final int TRANSACTION_isWorldPhone = 121;
        static final int TRANSACTION_needMobileRadioShutdown = 114;
        static final int TRANSACTION_needsOtaServiceProvisioning = 59;
        static final int TRANSACTION_notifyMissedCall = 1;
        static final int TRANSACTION_notifyVoIPCallStateChangeIntoBT = 197;
        static final int TRANSACTION_nvReadItem = 86;
        static final int TRANSACTION_nvResetConfig = 89;
        static final int TRANSACTION_nvWriteCdmaPrl = 88;
        static final int TRANSACTION_nvWriteItem = 87;
        static final int TRANSACTION_sendEnvelopeWithStatus = 85;
        static final int TRANSACTION_sendRequestRawToRIL = 170;
        static final int TRANSACTION_sendRequestToRIL = 161;
        static final int TRANSACTION_setAirplaneMode = 208;
        static final int TRANSACTION_setBTUserWantsAudioOn = 195;
        static final int TRANSACTION_setBTUserWantsSwitchAudio = 196;
        static final int TRANSACTION_setCellInfoListRate = 73;
        static final int TRANSACTION_setDataEnabled = 97;
        static final int TRANSACTION_setDataRoamingEnabled = 139;
        static final int TRANSACTION_setDisable2g = 213;
        static final int TRANSACTION_setDmCmd = 175;
        static final int TRANSACTION_setEPSLOCI = 189;
        static final int TRANSACTION_setGbaBootstrappingParams = 188;
        static final int TRANSACTION_setImsRegistrationState = 100;
        static final int TRANSACTION_setLine1NumberForDisplayForSubscriber = 107;
        static final int TRANSACTION_setMultiSimForegroundPhoneId = 192;
        static final int TRANSACTION_setMultiSimLastRejectIncomingCallPhoneId = 190;
        static final int TRANSACTION_setNetworkBand = 207;
        static final int TRANSACTION_setNetworkSelectionModeAutomatic = 93;
        static final int TRANSACTION_setNetworkSelectionModeManual = 95;
        static final int TRANSACTION_setOperatorBrandOverride = 111;
        static final int TRANSACTION_setPreferredNetworkType = 96;
        static final int TRANSACTION_setRadio = 32;
        static final int TRANSACTION_setRadioCapability = 116;
        static final int TRANSACTION_setRadioForSubscriber = 33;
        static final int TRANSACTION_setRadioPower = 34;
        static final int TRANSACTION_setRoamingOverride = 112;
        static final int TRANSACTION_setSelectedApn = 141;
        static final int TRANSACTION_setTransmitPower = 150;
        static final int TRANSACTION_setUimRemoteLockStatus = 156;
        static final int TRANSACTION_setVoiceMailNumber = 60;
        static final int TRANSACTION_shutdownMobileRadios = 115;
        static final int TRANSACTION_silenceRinger = 10;
        static final int TRANSACTION_sms_NSRI_decryptsms = 200;
        static final int TRANSACTION_sms_NSRI_decryptsmsintxside = 201;
        static final int TRANSACTION_sms_NSRI_encryptsms = 199;
        static final int TRANSACTION_startGlobalNetworkSearchTimer = 181;
        static final int TRANSACTION_startGlobalNoSvcChkTimer = 183;
        static final int TRANSACTION_startMobileQualityInformation = 164;
        static final int TRANSACTION_startVoiceLessOtaProvisioning = 128;
        static final int TRANSACTION_stopGlobalNetworkSearchTimer = 182;
        static final int TRANSACTION_stopGlobalNoSvcChkTimer = 184;
        static final int TRANSACTION_stopMobileQualityInformation = 165;
        static final int TRANSACTION_supplyPerso = 178;
        static final int TRANSACTION_supplyPin = 20;
        static final int TRANSACTION_supplyPinForSubscriber = 21;
        static final int TRANSACTION_supplyPinReportResult = 24;
        static final int TRANSACTION_supplyPinReportResultForSubscriber = 25;
        static final int TRANSACTION_supplyPuk = 22;
        static final int TRANSACTION_supplyPukForSubscriber = 23;
        static final int TRANSACTION_supplyPukReportResult = 26;
        static final int TRANSACTION_supplyPukReportResultForSubscriber = 27;
        static final int TRANSACTION_toggleRadioOnOff = 30;
        static final int TRANSACTION_toggleRadioOnOffForSubscriber = 31;
        static final int TRANSACTION_transmitIccAPDU = 154;
        static final int TRANSACTION_updateServiceLocation = 35;
        static final int TRANSACTION_updateServiceLocationForSubscriber = 36;
        static final int TRANSACTION_validateMsisdn = 209;

        private static class Proxy implements ITelephony {
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

            public void notifyMissedCall(String name, String number, String label, long date) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(number);
                    _data.writeString(label);
                    _data.writeLong(date);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dial(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dialForSubscriber(int subId, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(number);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void call(String callingPackage, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(number);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void callForSubscriber(int subId, String callingPackage, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    _data.writeString(number);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean endCall() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
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

            public boolean endCallForSubscriber(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
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

            public void answerRingingCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void answerRingingCallForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void silenceRinger() throws RemoteException {
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

            public boolean isOffhook(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
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

            public boolean isOffhookForSubscriber(int subId, String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
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

            public boolean isRingingForSubscriber(int subId, String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
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

            public boolean isRinging(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(14, _data, _reply, 0);
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

            public boolean isIdle(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(15, _data, _reply, 0);
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

            public boolean isIdleForSubscriber(int subId, String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(16, _data, _reply, 0);
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

            public boolean isRadioOn(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(17, _data, _reply, 0);
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

            public boolean isRadioOnForSubscriber(int subId, String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
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

            public boolean isSimPinEnabled(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
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

            public boolean supplyPin(String pin) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
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

            public boolean supplyPinForSubscriber(int subId, String pin) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
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

            public boolean supplyPuk(String puk, String pin) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
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

            public boolean supplyPukForSubscriber(int subId, String puk, String pin) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
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

            public int[] supplyPinReportResult(String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] supplyPinReportResultForSubscriber(int subId, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] supplyPukReportResult(String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] supplyPukReportResultForSubscriber(int subId, String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handlePinMmi(String dialString) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
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

            public boolean handlePinMmiForSubscriber(int subId, String dialString) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(dialString);
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

            public void toggleRadioOnOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void toggleRadioOnOffForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRadio(boolean turnOn) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (turnOn) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(32, _data, _reply, 0);
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

            public boolean setRadioForSubscriber(int subId, boolean turnOn) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (turnOn) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(33, _data, _reply, 0);
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

            public boolean setRadioPower(boolean turnOn) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (turnOn) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(34, _data, _reply, 0);
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

            public void updateServiceLocation() throws RemoteException {
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

            public void updateServiceLocationForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableDataConnectivity() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean disableDataConnectivity() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean isDataConnectivityPossible() throws RemoteException {
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

            public Bundle getCellLocation(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
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

            public List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    List<NeighboringCellInfo> _result = _reply.createTypedArrayList(NeighboringCellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCallStateForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataActivity() throws RemoteException {
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

            public int getDataState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataStateSimSlot(int lSubId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(lSubId);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActivePhoneType() throws RemoteException {
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

            public int getActivePhoneTypeForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconIndex(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconIndexForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaEriText(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaEriTextForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needsOtaServiceProvisioning() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(59, _data, _reply, 0);
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

            public boolean setVoiceMailNumber(int subId, String alphaTag, String number) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
                    this.mRemote.transact(60, _data, _reply, 0);
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

            public int getVoiceMessageCount() throws RemoteException {
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

            public int getVoiceMessageCountForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNetworkType() throws RemoteException {
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

            public int getNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVoiceNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasIccCard() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(68, _data, _reply, 0);
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

            public boolean hasIccCardUsingSlotId(int slotId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(69, _data, _reply, 0);
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

            public int getLteOnCdmaMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLteOnCdmaModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<CellInfo> getAllCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    List<CellInfo> _result = _reply.createTypedArrayList(CellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCellInfoListRate(int rateInMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rateInMillis);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDefaultSim() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IccOpenLogicalChannelResponse iccOpenLogicalChannel(String AID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    IccOpenLogicalChannelResponse _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(AID);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (IccOpenLogicalChannelResponse) IccOpenLogicalChannelResponse.CREATOR.createFromParcel(_reply);
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

            public IccOpenLogicalChannelResponse iccOpenLogicalChannelUsingSlotId(String AID, int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    IccOpenLogicalChannelResponse _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(AID);
                    _data.writeInt(slotId);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (IccOpenLogicalChannelResponse) IccOpenLogicalChannelResponse.CREATOR.createFromParcel(_reply);
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

            public boolean iccCloseLogicalChannel(int channel) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(channel);
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

            public boolean iccCloseLogicalChannelUsingSlotId(int channel, int slotId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(channel);
                    _data.writeInt(slotId);
                    this.mRemote.transact(78, _data, _reply, 0);
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

            public String iccTransmitApduLogicalChannel(int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(channel);
                    _data.writeInt(cla);
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String iccTransmitApduLogicalChannelUsingSlotId(int channel, int cla, int instruction, int p1, int p2, int p3, String data, int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(channel);
                    _data.writeInt(cla);
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    _data.writeInt(slotId);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String iccTransmitApduBasicChannel(int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cla);
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String iccTransmitApduBasicChannelUsingSlotId(int cla, int instruction, int p1, int p2, int p3, String data, int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cla);
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    _data.writeInt(slotId);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] iccExchangeSimIO(int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fileID);
                    _data.writeInt(command);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(filePath);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] iccExchangeSimIOUsingSlotId(int fileID, int command, int p1, int p2, int p3, String filePath, int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fileID);
                    _data.writeInt(command);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(filePath);
                    _data.writeInt(slotId);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String sendEnvelopeWithStatus(String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(content);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String nvReadItem(int itemID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean nvWriteItem(int itemID, String itemValue) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    _data.writeString(itemValue);
                    this.mRemote.transact(87, _data, _reply, 0);
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

            public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(preferredRoamingList);
                    this.mRemote.transact(88, _data, _reply, 0);
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

            public boolean nvResetConfig(int resetType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resetType);
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

            public int getCalculatedPreferredNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredNetworkType(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getTetherApnRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkSelectionModeAutomatic(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CellNetworkScanResult getCellNetworkScanResults(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CellNetworkScanResult _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CellNetworkScanResult) CellNetworkScanResult.CREATOR.createFromParcel(_reply);
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

            public boolean setNetworkSelectionModeManual(int subId, OperatorInfo operator, boolean persistSelection) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (operator != null) {
                        _data.writeInt(1);
                        operator.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(persistSelection ? 1 : 0);
                    this.mRemote.transact(95, _data, _reply, 0);
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

            public boolean setPreferredNetworkType(int subId, int networkType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(networkType);
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

            public void setDataEnabled(int subId, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getDataEnabled(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
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

            public String[] getPcscfAddress(String apnType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setImsRegistrationState(boolean registered) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (registered) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaMdn(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaMin(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCarrierPrivilegeStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkCarrierPrivilegesForPackage(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(phoneId);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
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

            public String getLine1NumberForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1AlphaTagForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getMergedSubscriberIds(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setOperatorBrandOverride(String brand) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(brand);
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

            public boolean setRoamingOverride(List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(gsmRoamingList);
                    _data.writeStringList(gsmNonRoamingList);
                    _data.writeStringList(cdmaRoamingList);
                    _data.writeStringList(cdmaNonRoamingList);
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

            public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(oemReq);
                    if (oemResp == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(oemResp.length);
                    }
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(oemResp);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needMobileRadioShutdown() throws RemoteException {
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

            public void shutdownMobileRadios() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(rafs, 0);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRadioAccessFamily(int phoneId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableVideoCalling(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVideoCallingEnabled(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(119, _data, _reply, 0);
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

            public boolean canChangeDtmfToneLength() throws RemoteException {
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

            public boolean isWorldPhone() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean isTtyModeSupported() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(122, _data, _reply, 0);
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

            public boolean isHearingAidCompatibilitySupported() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(123, _data, _reply, 0);
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

            public boolean isImsRegistered() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(124, _data, _reply, 0);
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

            public boolean isWifiCallingAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(125, _data, _reply, 0);
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

            public boolean isVolteAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(126, _data, _reply, 0);
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

            public boolean isVideoTelephonyAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(127, _data, _reply, 0);
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

            public void startVoiceLessOtaProvisioning(String number, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void factoryReset(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLocaleFromDefaultSim() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ModemActivityInfo getModemActivityInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ModemActivityInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ModemActivityInfo) ModemActivityInfo.CREATOR.createFromParcel(_reply);
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

            public boolean isVideoCall() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(134, _data, _reply, 0);
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

            public boolean isSimFDNEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(135, _data, _reply, 0);
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

            public boolean isSimFDNEnabledForSubscriber(int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(136, _data, _reply, 0);
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

            public boolean getDataRoamingEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(137, _data, _reply, 0);
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

            public boolean getDataRoamingEnabledUsingSubID(int subID) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subID);
                    this.mRemote.transact(138, _data, _reply, 0);
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

            public void setDataRoamingEnabled(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSelectedApn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSelectedApn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ServiceState getVoicePhoneServiceState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ServiceState _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ServiceState) ServiceState.CREATOR.createFromParcel(_reply);
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

            public ServiceState getVoicePhoneServiceStateUsingSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ServiceState _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ServiceState) ServiceState.CREATOR.createFromParcel(_reply);
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

            public int getServiceState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getServiceStateUsingSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataServiceState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataServiceStateUsingSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getAtr() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getAtrUsingSlotId(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setTransmitPower(int powerLevel) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(powerLevel);
                    this.mRemote.transact(150, _data, _reply, 0);
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

            public String getMeid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getImei() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSimPinRetry() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String transmitIccAPDU(int cla, int command, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cla);
                    _data.writeInt(command);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFeliCaUimLockStatus(int option) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(option);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setUimRemoteLockStatus(int option) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(option);
                    this.mRemote.transact(156, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSimPukRetry() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getSdnAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(158, _data, _reply, 0);
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

            public boolean IsInternationalRoaming() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(159, _data, _reply, 0);
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

            public boolean IsDomesticRoaming() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(160, _data, _reply, 0);
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

            public int sendRequestToRIL(byte[] data, byte[] response, int what) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(data);
                    _data.writeByteArray(response);
                    _data.writeInt(what);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(response);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMobileQualityInformation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(162, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIpAddressFromLinkProp(String nwkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(nwkType);
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startMobileQualityInformation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopMobileQualityInformation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getTimeInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getWipiSysInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLteCellInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getCurrentUATI() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(169, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int sendRequestRawToRIL(byte[] data, Message msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(data);
                    if (msg != null) {
                        _data.writeInt(1);
                        msg.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(170, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getHandsetInfo(String ID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ID);
                    this.mRemote.transact(171, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPhoneServiceState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(172, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLgt3GDataStatus(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(173, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLgtOzStartPage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(174, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] setDmCmd(int cmdCode, byte[] cmdData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmdCode);
                    _data.writeByteArray(cmdData);
                    this.mRemote.transact(175, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSimPinRetryForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(176, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSimPukRetryForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(177, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supplyPerso(String passwd) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(passwd);
                    this.mRemote.transact(178, _data, _reply, 0);
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

            public void SimSlotOnOff(int on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on);
                    this.mRemote.transact(179, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void SimSlotActivation(boolean activation) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (activation) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(180, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startGlobalNetworkSearchTimer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(181, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopGlobalNetworkSearchTimer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(182, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startGlobalNoSvcChkTimer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(183, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopGlobalNoSvcChkTimer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(184, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String calculateAkaResponse(byte[] rand, byte[] autn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(rand);
                    _data.writeByteArray(autn);
                    this.mRemote.transact(185, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle calculateGbaBootstrappingResponse(byte[] rand, byte[] autn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(rand);
                    _data.writeByteArray(autn);
                    this.mRemote.transact(186, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
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

            public byte[] calculateNafExternalKey(byte[] nafId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(nafId);
                    this.mRemote.transact(187, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGbaBootstrappingParams(byte[] rand, String btid, String keyLifetime) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(rand);
                    _data.writeString(btid);
                    _data.writeString(keyLifetime);
                    this.mRemote.transact(188, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setEPSLOCI(byte[] newEpsloci) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(newEpsloci);
                    this.mRemote.transact(189, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMultiSimLastRejectIncomingCallPhoneId(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(190, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMultiSimLastRejectIncomingCallPhoneId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(191, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMultiSimForegroundPhoneId(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(192, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMultiSimForegroundPhoneId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(193, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isImsCall() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(194, _data, _reply, 0);
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

            public void setBTUserWantsAudioOn(boolean flag) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (flag) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(195, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBTUserWantsSwitchAudio() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(196, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyVoIPCallStateChangeIntoBT() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(197, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDualBTConnection() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(198, _data, _reply, 0);
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

            public byte[] sms_NSRI_encryptsms(int in_len, String potherphonenumber, byte[] message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(in_len);
                    _data.writeString(potherphonenumber);
                    _data.writeByteArray(message);
                    this.mRemote.transact(199, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] sms_NSRI_decryptsms(int in_len, byte[] message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(in_len);
                    _data.writeByteArray(message);
                    this.mRemote.transact(200, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] sms_NSRI_decryptsmsintxside(int in_len, String potherphonenumber, byte[] message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(in_len);
                    _data.writeString(potherphonenumber);
                    _data.writeByteArray(message);
                    this.mRemote.transact(201, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkNSRIUSIMstate_int() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(202, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] NSRI_requestProc(int datalen, byte[] adata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(datalen);
                    _data.writeByteArray(adata);
                    this.mRemote.transact(203, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isApnTypeAvailable(String apnType) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    this.mRemote.transact(204, _data, _reply, 0);
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

            public boolean isApnTypeAvailableUsingSubId(String apnType, int subId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeInt(subId);
                    this.mRemote.transact(205, _data, _reply, 0);
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

            public String getEuimid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(206, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setNetworkBand(String passcode, String mode, int band) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(passcode);
                    _data.writeString(mode);
                    _data.writeInt(band);
                    this.mRemote.transact(207, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setAirplaneMode(String passcode, boolean mode) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(passcode);
                    if (mode) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(208, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int validateMsisdn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(209, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getActiveFgCallState() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(210, _data, _reply, 0);
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

            public int invokeOemRilRequestRawForSubscriber(int subId, byte[] oemReq, byte[] oemResp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeByteArray(oemReq);
                    if (oemResp == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(oemResp.length);
                    }
                    this.mRemote.transact(211, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(oemResp);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDisable2g() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(212, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDisable2g(int state) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(213, _data, _reply, 0);
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

        public static ITelephony asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITelephony)) {
                return new Proxy(obj);
            }
            return (ITelephony) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _result;
            int[] _result2;
            Bundle _result3;
            int _result4;
            String _result5;
            IccOpenLogicalChannelResponse _result6;
            byte[] _result7;
            int _arg0;
            String[] _result8;
            byte[] _arg02;
            byte[] _arg1;
            ServiceState _result9;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    notifyMissedCall(data.readString(), data.readString(), data.readString(), data.readLong());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    dial(data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    dialForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    call(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    callForSubscriber(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = endCall();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = endCallForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    answerRingingCall();
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    answerRingingCallForSubscriber(data.readInt());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    silenceRinger();
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isOffhook(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isOffhookForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isRingingForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isRinging(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isIdle(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isIdleForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isRadioOn(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isRadioOnForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isSimPinEnabled(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result = supplyPin(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result = supplyPinForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result = supplyPuk(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result = supplyPukForSubscriber(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = supplyPinReportResult(data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = supplyPinReportResultForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = supplyPukReportResult(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = supplyPukReportResultForSubscriber(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result = handlePinMmi(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _result = handlePinMmiForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    toggleRadioOnOff();
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    toggleRadioOnOffForSubscriber(data.readInt());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setRadio(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setRadioForSubscriber(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setRadioPower(data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    updateServiceLocation();
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    updateServiceLocationForSubscriber(data.readInt());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    enableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    enableLocationUpdatesForSubscriber(data.readInt());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    disableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    disableLocationUpdatesForSubscriber(data.readInt());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enableDataConnectivity();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result = disableDataConnectivity();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isDataConnectivityPossible();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCellLocation(data.readString());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    List<NeighboringCellInfo> _result10 = getNeighboringCellInfo(data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result10);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCallState();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCallStateForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDataActivity();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDataState();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDataStateSimSlot(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getActivePhoneType();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getActivePhoneTypeForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCdmaEriIconIndex(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCdmaEriIconIndexForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCdmaEriIconMode(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCdmaEriIconModeForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getCdmaEriText(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getCdmaEriTextForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    _result = needsOtaServiceProvisioning();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setVoiceMailNumber(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getVoiceMessageCount();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getVoiceMessageCountForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getNetworkType();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getNetworkTypeForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDataNetworkType(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDataNetworkTypeForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getVoiceNetworkTypeForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    _result = hasIccCard();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    _result = hasIccCardUsingSlotId(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getLteOnCdmaMode(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getLteOnCdmaModeForSubscriber(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    List<CellInfo> _result11 = getAllCellInfo(data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result11);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    setCellInfoListRate(data.readInt());
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDefaultSim();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = iccOpenLogicalChannel(data.readString());
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    _result6 = iccOpenLogicalChannelUsingSlotId(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    _result = iccCloseLogicalChannel(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    _result = iccCloseLogicalChannelUsingSlotId(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = iccTransmitApduLogicalChannel(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = iccTransmitApduLogicalChannelUsingSlotId(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = iccTransmitApduBasicChannel(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = iccTransmitApduBasicChannelUsingSlotId(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = iccExchangeSimIO(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = iccExchangeSimIOUsingSlotId(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = sendEnvelopeWithStatus(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = nvReadItem(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    _result = nvWriteItem(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    _result = nvWriteCdmaPrl(data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    _result = nvResetConfig(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCalculatedPreferredNetworkType(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPreferredNetworkType(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getTetherApnRequired();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    setNetworkSelectionModeAutomatic(data.readInt());
                    reply.writeNoException();
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    CellNetworkScanResult _result12 = getCellNetworkScanResults(data.readInt());
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 95:
                    OperatorInfo _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = (OperatorInfo) OperatorInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    _result = setNetworkSelectionModeManual(_arg0, _arg12, data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setPreferredNetworkType(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    setDataEnabled(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDataEnabled(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getPcscfAddress(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    setImsRegistrationState(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getCdmaMdn(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getCdmaMin(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getCarrierPrivilegeStatus();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = checkCarrierPrivilegesForPackage(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = checkCarrierPrivilegesForPackageAnyPhone(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 106:
                    Intent _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    List<String> _result13 = getCarrierPackageNamesForIntentAndPhone(_arg03, data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result13);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setLine1NumberForDisplayForSubscriber(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getLine1NumberForDisplay(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getLine1AlphaTagForDisplay(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    _result8 = getMergedSubscriberIds(data.readString());
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setOperatorBrandOverride(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setRoamingOverride(data.createStringArrayList(), data.createStringArrayList(), data.createStringArrayList(), data.createStringArrayList());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.createByteArray();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    _result4 = invokeOemRilRequestRaw(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    reply.writeByteArray(_arg1);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    _result = needMobileRadioShutdown();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    shutdownMobileRadios();
                    reply.writeNoException();
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    setRadioCapability((RadioAccessFamily[]) data.createTypedArray(RadioAccessFamily.CREATOR));
                    reply.writeNoException();
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getRadioAccessFamily(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    enableVideoCalling(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isVideoCallingEnabled(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    _result = canChangeDtmfToneLength();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isWorldPhone();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isTtyModeSupported();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isHearingAidCompatibilitySupported();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isImsRegistered();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isWifiCallingAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isVolteAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isVideoTelephonyAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    startVoiceLessOtaProvisioning(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getDeviceId(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 130:
                    PhoneAccount _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (PhoneAccount) PhoneAccount.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result4 = getSubIdForPhoneAccount(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    factoryReset(data.readInt());
                    reply.writeNoException();
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getLocaleFromDefaultSim();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    ModemActivityInfo _result14 = getModemActivityInfo();
                    reply.writeNoException();
                    if (_result14 != null) {
                        reply.writeInt(1);
                        _result14.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isVideoCall();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isSimFDNEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isSimFDNEnabledForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDataRoamingEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDataRoamingEnabledUsingSubID(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    setDataRoamingEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getSelectedApn();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    setSelectedApn();
                    reply.writeNoException();
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getVoicePhoneServiceState();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    _result9 = getVoicePhoneServiceStateUsingSubId(data.readInt());
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getServiceState();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getServiceStateUsingSubId(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDataServiceState();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDataServiceStateUsingSubId(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getAtr();
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getAtrUsingSlotId(data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setTransmitPower(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getMeid();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getImei();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getSimPinRetry();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = transmitIccAPDU(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getFeliCaUimLockStatus(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setUimRemoteLockStatus(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getSimPukRetry();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSdnAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    _result = IsInternationalRoaming();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    _result = IsDomesticRoaming();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.createByteArray();
                    _arg1 = data.createByteArray();
                    _result4 = sendRequestToRIL(_arg02, _arg1, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    reply.writeByteArray(_arg1);
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getMobileQualityInformation();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getIpAddressFromLinkProp(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    startMobileQualityInformation();
                    reply.writeNoException();
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    stopMobileQualityInformation();
                    reply.writeNoException();
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getTimeInfo();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getWipiSysInfo();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getLteCellInfo();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = getCurrentUATI();
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 170:
                    Message _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.createByteArray();
                    if (data.readInt() != 0) {
                        _arg13 = (Message) Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    _result4 = sendRequestRawToRIL(_arg02, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getHandsetInfo(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPhoneServiceState();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getLgt3GDataStatus(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getLgtOzStartPage();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = setDmCmd(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getSimPinRetryForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getSimPukRetryForSubscriber(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    _result = supplyPerso(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    SimSlotOnOff(data.readInt());
                    reply.writeNoException();
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    SimSlotActivation(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    startGlobalNetworkSearchTimer();
                    reply.writeNoException();
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    stopGlobalNetworkSearchTimer();
                    reply.writeNoException();
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    startGlobalNoSvcChkTimer();
                    reply.writeNoException();
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    stopGlobalNoSvcChkTimer();
                    reply.writeNoException();
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = calculateAkaResponse(data.createByteArray(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = calculateGbaBootstrappingResponse(data.createByteArray(), data.createByteArray());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = calculateNafExternalKey(data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    setGbaBootstrappingParams(data.createByteArray(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    setEPSLOCI(data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    setMultiSimLastRejectIncomingCallPhoneId(data.readInt());
                    reply.writeNoException();
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getMultiSimLastRejectIncomingCallPhoneId();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    setMultiSimForegroundPhoneId(data.readInt());
                    reply.writeNoException();
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getMultiSimForegroundPhoneId();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isImsCall();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    setBTUserWantsAudioOn(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    setBTUserWantsSwitchAudio();
                    reply.writeNoException();
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    notifyVoIPCallStateChangeIntoBT();
                    reply.writeNoException();
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isDualBTConnection();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = sms_NSRI_encryptsms(data.readInt(), data.readString(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = sms_NSRI_decryptsms(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = sms_NSRI_decryptsmsintxside(data.readInt(), data.readString(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = checkNSRIUSIMstate_int();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    _result7 = NSRI_requestProc(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isApnTypeAvailable(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isApnTypeAvailableUsingSubId(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getEuimid();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setNetworkBand(data.readString(), data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 208:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setAirplaneMode(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 209:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = validateMsisdn();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 210:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getActiveFgCallState();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 211:
                    byte[] _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.createByteArray();
                    int _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        _arg2 = new byte[_arg2_length];
                    }
                    _result4 = invokeOemRilRequestRawForSubscriber(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    reply.writeByteArray(_arg2);
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getDisable2g();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 213:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setDisable2g(data.readInt());
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

    boolean IsDomesticRoaming() throws RemoteException;

    boolean IsInternationalRoaming() throws RemoteException;

    byte[] NSRI_requestProc(int i, byte[] bArr) throws RemoteException;

    void SimSlotActivation(boolean z) throws RemoteException;

    void SimSlotOnOff(int i) throws RemoteException;

    void answerRingingCall() throws RemoteException;

    void answerRingingCallForSubscriber(int i) throws RemoteException;

    String calculateAkaResponse(byte[] bArr, byte[] bArr2) throws RemoteException;

    Bundle calculateGbaBootstrappingResponse(byte[] bArr, byte[] bArr2) throws RemoteException;

    byte[] calculateNafExternalKey(byte[] bArr) throws RemoteException;

    void call(String str, String str2) throws RemoteException;

    void callForSubscriber(int i, String str, String str2) throws RemoteException;

    boolean canChangeDtmfToneLength() throws RemoteException;

    int checkCarrierPrivilegesForPackage(String str) throws RemoteException;

    int checkCarrierPrivilegesForPackageAnyPhone(String str) throws RemoteException;

    int checkNSRIUSIMstate_int() throws RemoteException;

    void dial(String str) throws RemoteException;

    void dialForSubscriber(int i, String str) throws RemoteException;

    boolean disableDataConnectivity() throws RemoteException;

    void disableLocationUpdates() throws RemoteException;

    void disableLocationUpdatesForSubscriber(int i) throws RemoteException;

    boolean enableDataConnectivity() throws RemoteException;

    void enableLocationUpdates() throws RemoteException;

    void enableLocationUpdatesForSubscriber(int i) throws RemoteException;

    void enableVideoCalling(boolean z) throws RemoteException;

    boolean endCall() throws RemoteException;

    boolean endCallForSubscriber(int i) throws RemoteException;

    void factoryReset(int i) throws RemoteException;

    boolean getActiveFgCallState() throws RemoteException;

    int getActivePhoneType() throws RemoteException;

    int getActivePhoneTypeForSubscriber(int i) throws RemoteException;

    List<CellInfo> getAllCellInfo(String str) throws RemoteException;

    byte[] getAtr() throws RemoteException;

    byte[] getAtrUsingSlotId(int i) throws RemoteException;

    int getCalculatedPreferredNetworkType(String str) throws RemoteException;

    int getCallState() throws RemoteException;

    int getCallStateForSubscriber(int i) throws RemoteException;

    List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int i) throws RemoteException;

    int getCarrierPrivilegeStatus() throws RemoteException;

    int getCdmaEriIconIndex(String str) throws RemoteException;

    int getCdmaEriIconIndexForSubscriber(int i, String str) throws RemoteException;

    int getCdmaEriIconMode(String str) throws RemoteException;

    int getCdmaEriIconModeForSubscriber(int i, String str) throws RemoteException;

    String getCdmaEriText(String str) throws RemoteException;

    String getCdmaEriTextForSubscriber(int i, String str) throws RemoteException;

    String getCdmaMdn(int i) throws RemoteException;

    String getCdmaMin(int i) throws RemoteException;

    Bundle getCellLocation(String str) throws RemoteException;

    CellNetworkScanResult getCellNetworkScanResults(int i) throws RemoteException;

    byte[] getCurrentUATI() throws RemoteException;

    int getDataActivity() throws RemoteException;

    boolean getDataEnabled(int i) throws RemoteException;

    int getDataNetworkType(String str) throws RemoteException;

    int getDataNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    boolean getDataRoamingEnabled() throws RemoteException;

    boolean getDataRoamingEnabledUsingSubID(int i) throws RemoteException;

    int getDataServiceState() throws RemoteException;

    int getDataServiceStateUsingSubId(int i) throws RemoteException;

    int getDataState() throws RemoteException;

    int getDataStateSimSlot(int i) throws RemoteException;

    int getDefaultSim() throws RemoteException;

    String getDeviceId(String str) throws RemoteException;

    int getDisable2g() throws RemoteException;

    String getEuimid() throws RemoteException;

    int getFeliCaUimLockStatus(int i) throws RemoteException;

    String getHandsetInfo(String str) throws RemoteException;

    String getImei() throws RemoteException;

    String getIpAddressFromLinkProp(String str) throws RemoteException;

    String getLgt3GDataStatus(int i) throws RemoteException;

    String getLgtOzStartPage() throws RemoteException;

    String getLine1AlphaTagForDisplay(int i, String str) throws RemoteException;

    String getLine1NumberForDisplay(int i, String str) throws RemoteException;

    String getLocaleFromDefaultSim() throws RemoteException;

    String getLteCellInfo() throws RemoteException;

    int getLteOnCdmaMode(String str) throws RemoteException;

    int getLteOnCdmaModeForSubscriber(int i, String str) throws RemoteException;

    String getMeid() throws RemoteException;

    String[] getMergedSubscriberIds(String str) throws RemoteException;

    String getMobileQualityInformation() throws RemoteException;

    ModemActivityInfo getModemActivityInfo() throws RemoteException;

    int getMultiSimForegroundPhoneId() throws RemoteException;

    int getMultiSimLastRejectIncomingCallPhoneId() throws RemoteException;

    List<NeighboringCellInfo> getNeighboringCellInfo(String str) throws RemoteException;

    int getNetworkType() throws RemoteException;

    int getNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    String[] getPcscfAddress(String str, String str2) throws RemoteException;

    int getPhoneServiceState() throws RemoteException;

    int getPreferredNetworkType(int i) throws RemoteException;

    int getRadioAccessFamily(int i, String str) throws RemoteException;

    boolean getSdnAvailable() throws RemoteException;

    String getSelectedApn() throws RemoteException;

    int getServiceState() throws RemoteException;

    int getServiceStateUsingSubId(int i) throws RemoteException;

    int getSimPinRetry() throws RemoteException;

    int getSimPinRetryForSubscriber(int i) throws RemoteException;

    int getSimPukRetry() throws RemoteException;

    int getSimPukRetryForSubscriber(int i) throws RemoteException;

    int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException;

    int getTetherApnRequired() throws RemoteException;

    String getTimeInfo() throws RemoteException;

    int getVoiceMessageCount() throws RemoteException;

    int getVoiceMessageCountForSubscriber(int i) throws RemoteException;

    int getVoiceNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    ServiceState getVoicePhoneServiceState() throws RemoteException;

    ServiceState getVoicePhoneServiceStateUsingSubId(int i) throws RemoteException;

    String getWipiSysInfo() throws RemoteException;

    boolean handlePinMmi(String str) throws RemoteException;

    boolean handlePinMmiForSubscriber(int i, String str) throws RemoteException;

    boolean hasIccCard() throws RemoteException;

    boolean hasIccCardUsingSlotId(int i) throws RemoteException;

    boolean iccCloseLogicalChannel(int i) throws RemoteException;

    boolean iccCloseLogicalChannelUsingSlotId(int i, int i2) throws RemoteException;

    byte[] iccExchangeSimIO(int i, int i2, int i3, int i4, int i5, String str) throws RemoteException;

    byte[] iccExchangeSimIOUsingSlotId(int i, int i2, int i3, int i4, int i5, String str, int i6) throws RemoteException;

    IccOpenLogicalChannelResponse iccOpenLogicalChannel(String str) throws RemoteException;

    IccOpenLogicalChannelResponse iccOpenLogicalChannelUsingSlotId(String str, int i) throws RemoteException;

    String iccTransmitApduBasicChannel(int i, int i2, int i3, int i4, int i5, String str) throws RemoteException;

    String iccTransmitApduBasicChannelUsingSlotId(int i, int i2, int i3, int i4, int i5, String str, int i6) throws RemoteException;

    String iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, String str) throws RemoteException;

    String iccTransmitApduLogicalChannelUsingSlotId(int i, int i2, int i3, int i4, int i5, int i6, String str, int i7) throws RemoteException;

    int invokeOemRilRequestRaw(byte[] bArr, byte[] bArr2) throws RemoteException;

    int invokeOemRilRequestRawForSubscriber(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    boolean isApnTypeAvailable(String str) throws RemoteException;

    boolean isApnTypeAvailableUsingSubId(String str, int i) throws RemoteException;

    boolean isDataConnectivityPossible() throws RemoteException;

    boolean isDualBTConnection() throws RemoteException;

    boolean isHearingAidCompatibilitySupported() throws RemoteException;

    boolean isIdle(String str) throws RemoteException;

    boolean isIdleForSubscriber(int i, String str) throws RemoteException;

    boolean isImsCall() throws RemoteException;

    boolean isImsRegistered() throws RemoteException;

    boolean isOffhook(String str) throws RemoteException;

    boolean isOffhookForSubscriber(int i, String str) throws RemoteException;

    boolean isRadioOn(String str) throws RemoteException;

    boolean isRadioOnForSubscriber(int i, String str) throws RemoteException;

    boolean isRinging(String str) throws RemoteException;

    boolean isRingingForSubscriber(int i, String str) throws RemoteException;

    boolean isSimFDNEnabled() throws RemoteException;

    boolean isSimFDNEnabledForSubscriber(int i) throws RemoteException;

    boolean isSimPinEnabled(String str) throws RemoteException;

    boolean isTtyModeSupported() throws RemoteException;

    boolean isVideoCall() throws RemoteException;

    boolean isVideoCallingEnabled(String str) throws RemoteException;

    boolean isVideoTelephonyAvailable() throws RemoteException;

    boolean isVolteAvailable() throws RemoteException;

    boolean isWifiCallingAvailable() throws RemoteException;

    boolean isWorldPhone() throws RemoteException;

    boolean needMobileRadioShutdown() throws RemoteException;

    boolean needsOtaServiceProvisioning() throws RemoteException;

    void notifyMissedCall(String str, String str2, String str3, long j) throws RemoteException;

    void notifyVoIPCallStateChangeIntoBT() throws RemoteException;

    String nvReadItem(int i) throws RemoteException;

    boolean nvResetConfig(int i) throws RemoteException;

    boolean nvWriteCdmaPrl(byte[] bArr) throws RemoteException;

    boolean nvWriteItem(int i, String str) throws RemoteException;

    String sendEnvelopeWithStatus(String str) throws RemoteException;

    int sendRequestRawToRIL(byte[] bArr, Message message) throws RemoteException;

    int sendRequestToRIL(byte[] bArr, byte[] bArr2, int i) throws RemoteException;

    int setAirplaneMode(String str, boolean z) throws RemoteException;

    void setBTUserWantsAudioOn(boolean z) throws RemoteException;

    void setBTUserWantsSwitchAudio() throws RemoteException;

    void setCellInfoListRate(int i) throws RemoteException;

    void setDataEnabled(int i, boolean z) throws RemoteException;

    void setDataRoamingEnabled(boolean z) throws RemoteException;

    boolean setDisable2g(int i) throws RemoteException;

    int[] setDmCmd(int i, byte[] bArr) throws RemoteException;

    void setEPSLOCI(byte[] bArr) throws RemoteException;

    void setGbaBootstrappingParams(byte[] bArr, String str, String str2) throws RemoteException;

    void setImsRegistrationState(boolean z) throws RemoteException;

    boolean setLine1NumberForDisplayForSubscriber(int i, String str, String str2) throws RemoteException;

    void setMultiSimForegroundPhoneId(int i) throws RemoteException;

    void setMultiSimLastRejectIncomingCallPhoneId(int i) throws RemoteException;

    int setNetworkBand(String str, String str2, int i) throws RemoteException;

    void setNetworkSelectionModeAutomatic(int i) throws RemoteException;

    boolean setNetworkSelectionModeManual(int i, OperatorInfo operatorInfo, boolean z) throws RemoteException;

    boolean setOperatorBrandOverride(String str) throws RemoteException;

    boolean setPreferredNetworkType(int i, int i2) throws RemoteException;

    boolean setRadio(boolean z) throws RemoteException;

    void setRadioCapability(RadioAccessFamily[] radioAccessFamilyArr) throws RemoteException;

    boolean setRadioForSubscriber(int i, boolean z) throws RemoteException;

    boolean setRadioPower(boolean z) throws RemoteException;

    boolean setRoamingOverride(List<String> list, List<String> list2, List<String> list3, List<String> list4) throws RemoteException;

    void setSelectedApn() throws RemoteException;

    boolean setTransmitPower(int i) throws RemoteException;

    int setUimRemoteLockStatus(int i) throws RemoteException;

    boolean setVoiceMailNumber(int i, String str, String str2) throws RemoteException;

    void shutdownMobileRadios() throws RemoteException;

    void silenceRinger() throws RemoteException;

    byte[] sms_NSRI_decryptsms(int i, byte[] bArr) throws RemoteException;

    byte[] sms_NSRI_decryptsmsintxside(int i, String str, byte[] bArr) throws RemoteException;

    byte[] sms_NSRI_encryptsms(int i, String str, byte[] bArr) throws RemoteException;

    void startGlobalNetworkSearchTimer() throws RemoteException;

    void startGlobalNoSvcChkTimer() throws RemoteException;

    void startMobileQualityInformation() throws RemoteException;

    void startVoiceLessOtaProvisioning(String str, String str2) throws RemoteException;

    void stopGlobalNetworkSearchTimer() throws RemoteException;

    void stopGlobalNoSvcChkTimer() throws RemoteException;

    void stopMobileQualityInformation() throws RemoteException;

    boolean supplyPerso(String str) throws RemoteException;

    boolean supplyPin(String str) throws RemoteException;

    boolean supplyPinForSubscriber(int i, String str) throws RemoteException;

    int[] supplyPinReportResult(String str) throws RemoteException;

    int[] supplyPinReportResultForSubscriber(int i, String str) throws RemoteException;

    boolean supplyPuk(String str, String str2) throws RemoteException;

    boolean supplyPukForSubscriber(int i, String str, String str2) throws RemoteException;

    int[] supplyPukReportResult(String str, String str2) throws RemoteException;

    int[] supplyPukReportResultForSubscriber(int i, String str, String str2) throws RemoteException;

    void toggleRadioOnOff() throws RemoteException;

    void toggleRadioOnOffForSubscriber(int i) throws RemoteException;

    String transmitIccAPDU(int i, int i2, int i3, int i4, int i5, String str) throws RemoteException;

    void updateServiceLocation() throws RemoteException;

    void updateServiceLocationForSubscriber(int i) throws RemoteException;

    int validateMsisdn() throws RemoteException;
}
