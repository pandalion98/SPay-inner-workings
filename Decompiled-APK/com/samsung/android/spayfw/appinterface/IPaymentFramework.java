package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import java.util.List;

public interface IPaymentFramework extends IInterface {

    public static abstract class Stub extends Binder implements IPaymentFramework {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IPaymentFramework";
        static final int TRANSACTION_acceptTnC = 2;
        static final int TRANSACTION_clearCard = 11;
        static final int TRANSACTION_clearEnrolledCard = 17;
        static final int TRANSACTION_deleteCard = 20;
        static final int TRANSACTION_enrollCard = 1;
        static final int TRANSACTION_extractGiftCardDetail = 27;
        static final int TRANSACTION_extractGlobalMembershipCardDetail = 45;
        static final int TRANSACTION_extractLoyaltyCardDetails = 38;
        static final int TRANSACTION_getAllCardState = 16;
        static final int TRANSACTION_getCardAttributes = 13;
        static final int TRANSACTION_getCardData = 19;
        static final int TRANSACTION_getConfig = 35;
        static final int TRANSACTION_getEnvironment = 29;
        static final int TRANSACTION_getGiftCardRegisterData = 24;
        static final int TRANSACTION_getGiftCardTzEncData = 25;
        static final int TRANSACTION_getGlobalMembershipCardRegisterData = 43;
        static final int TRANSACTION_getGlobalMembershipCardTzEncData = 44;
        static final int TRANSACTION_getLogs = 23;
        static final int TRANSACTION_getPaymentReadyState = 18;
        static final int TRANSACTION_getTokenStatus = 4;
        static final int TRANSACTION_getTransactionDetails = 40;
        static final int TRANSACTION_getUserSignature = 33;
        static final int TRANSACTION_isDsrpBlobMissing = 30;
        static final int TRANSACTION_isDsrpBlobMissingForTokenId = 31;
        static final int TRANSACTION_notifyDeviceReset = 12;
        static final int TRANSACTION_processPushMessage = 10;
        static final int TRANSACTION_processServerRequest = 36;
        static final int TRANSACTION_processSpayApdu = 48;
        static final int TRANSACTION_provisionToken = 3;
        static final int TRANSACTION_refreshIdv = 41;
        static final int TRANSACTION_reset = 15;
        static final int TRANSACTION_retryPay = 28;
        static final int TRANSACTION_selectCard = 7;
        static final int TRANSACTION_selectIdv = 5;
        static final int TRANSACTION_setConfig = 34;
        static final int TRANSACTION_setJwtToken = 14;
        static final int TRANSACTION_setPin = 42;
        static final int TRANSACTION_startGiftCardPay = 26;
        static final int TRANSACTION_startGlobalMembershipCardPay = 46;
        static final int TRANSACTION_startInAppPay = 22;
        static final int TRANSACTION_startLoyaltyCardPay = 39;
        static final int TRANSACTION_startPay = 8;
        static final int TRANSACTION_stopPay = 9;
        static final int TRANSACTION_storeUserSignature = 32;
        static final int TRANSACTION_synchronizeCards = 21;
        static final int TRANSACTION_updateBinAttribute = 47;
        static final int TRANSACTION_updateLoyaltyCard = 37;
        static final int TRANSACTION_verifyIdv = 6;

        private static class Proxy implements IPaymentFramework {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int enrollCard(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enrollCardInfo != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        enrollCardInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (billingInfo != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        billingInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iEnrollCardCallback != null ? iEnrollCardCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_enrollCard, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int acceptTnC(String str, boolean z, ICommonCallback iCommonCallback) {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (z) {
                        i = Stub.TRANSACTION_enrollCard;
                    }
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_acceptTnC, obtain, obtain2, 0);
                    obtain2.readException();
                    i = obtain2.readInt();
                    return i;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int provisionToken(String str, ProvisionTokenInfo provisionTokenInfo, IProvisionTokenCallback iProvisionTokenCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (provisionTokenInfo != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        provisionTokenInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iProvisionTokenCallback != null ? iProvisionTokenCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_provisionToken, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public TokenStatus getTokenStatus(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    TokenStatus tokenStatus;
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_getTokenStatus, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        tokenStatus = (TokenStatus) TokenStatus.CREATOR.createFromParcel(obtain2);
                    } else {
                        tokenStatus = null;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return tokenStatus;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int selectIdv(String str, IdvMethod idvMethod, ISelectIdvCallback iSelectIdvCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (idvMethod != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        idvMethod.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSelectIdvCallback != null ? iSelectIdvCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_selectIdv, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int verifyIdv(String str, VerifyIdvInfo verifyIdvInfo, IVerifyIdvCallback iVerifyIdvCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (verifyIdvInfo != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        verifyIdvInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iVerifyIdvCallback != null ? iVerifyIdvCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_verifyIdv, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int selectCard(String str, ISelectCardCallback iSelectCardCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iSelectCardCallback != null ? iSelectCardCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_selectCard, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startPay(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (securedObject != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        securedObject.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (payConfig != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        payConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iPayCallback != null ? iPayCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_startPay, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int stopPay(ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_stopPay, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int processPushMessage(PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pushMessage != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        pushMessage.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iPushMessageCallback != null ? iPushMessageCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_processPushMessage, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearCard() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_clearCard, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int notifyDeviceReset(ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_notifyDeviceReset, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCardAttributes(String str, boolean z, ICardAttributeCallback iCardAttributeCallback) {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (z) {
                        i = Stub.TRANSACTION_enrollCard;
                    }
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iCardAttributeCallback != null ? iCardAttributeCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getCardAttributes, obtain, obtain2, 0);
                    obtain2.readException();
                    i = obtain2.readInt();
                    return i;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setJwtToken(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_setJwtToken, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int reset(String str, ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_reset, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<CardState> getAllCardState(Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_getAllCardState, obtain, obtain2, 0);
                    obtain2.readException();
                    List<CardState> createTypedArrayList = obtain2.createTypedArrayList(CardState.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int clearEnrolledCard(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_clearEnrolledCard, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getPaymentReadyState(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_getPaymentReadyState, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCardData(String str, ICardDataCallback iCardDataCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iCardDataCallback != null ? iCardDataCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getCardData, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int deleteCard(String str, Bundle bundle, IDeleteCardCallback iDeleteCardCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iDeleteCardCallback != null ? iDeleteCardCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_deleteCard, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int synchronizeCards(String str, ISynchronizeCardsCallback iSynchronizeCardsCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iSynchronizeCardsCallback != null ? iSynchronizeCardsCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_synchronizeCards, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (securedObject != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        securedObject.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (inAppTransactionInfo != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        inAppTransactionInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iInAppPayCallback != null ? iInAppPayCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_startInAppPay, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getLogs(ParcelFileDescriptor parcelFileDescriptor, String str, ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parcelFileDescriptor != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        parcelFileDescriptor.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getLogs, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (giftCardRegisterRequestData != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        giftCardRegisterRequestData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGiftCardRegisterCallback != null ? iGiftCardRegisterCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getGiftCardRegisterData, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (giftCardRegisterRequestData != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        giftCardRegisterRequestData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGiftCardRegisterCallback != null ? iGiftCardRegisterCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getGiftCardTzEncData, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startGiftCardPay(byte[] bArr, byte[] bArr2, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    if (securedObject != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        securedObject.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (payConfig != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        payConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iPayCallback != null ? iPayCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_startGiftCardPay, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int extractGiftCardDetail(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, IGiftCardExtractDetailCallback iGiftCardExtractDetailCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extractGiftCardDetailRequest != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        extractGiftCardDetailRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (securedObject != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        securedObject.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGiftCardExtractDetailCallback != null ? iGiftCardExtractDetailCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_extractGiftCardDetail, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int retryPay(PayConfig payConfig) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (payConfig != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        payConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_retryPay, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getEnvironment(ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getEnvironment, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> isDsrpBlobMissing() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isDsrpBlobMissing, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int isDsrpBlobMissingForTokenId(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_isDsrpBlobMissingForTokenId, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int storeUserSignature(String str, byte[] bArr, ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeByteArray(bArr);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_storeUserSignature, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getUserSignature(String str, IUserSignatureCallback iUserSignatureCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iUserSignatureCallback != null ? iUserSignatureCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getUserSignature, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setConfig(String str, String str2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(Stub.TRANSACTION_setConfig, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getConfig(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_getConfig, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int processServerRequest(ServerRequest serverRequest, IServerResponseCallback iServerResponseCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serverRequest != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        serverRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iServerResponseCallback != null ? iServerResponseCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_processServerRequest, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int updateLoyaltyCard(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (updateLoyaltyCardInfo != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        updateLoyaltyCardInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iUpdateLoyaltyCardCallback != null ? iUpdateLoyaltyCardCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_updateLoyaltyCard, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, IExtractLoyaltyCardDetailResponseCallback iExtractLoyaltyCardDetailResponseCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extractLoyaltyCardDetailRequest != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        extractLoyaltyCardDetailRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iExtractLoyaltyCardDetailResponseCallback != null ? iExtractLoyaltyCardDetailResponseCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_extractLoyaltyCardDetails, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startLoyaltyCardPay(LoyaltyCardShowRequest loyaltyCardShowRequest, IPayCallback iPayCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (loyaltyCardShowRequest != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        loyaltyCardShowRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iPayCallback != null ? iPayCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_startLoyaltyCardPay, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getTransactionDetails(String str, long j, long j2, int i, ITransactionDetailsCallback iTransactionDetailsCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iTransactionDetailsCallback != null ? iTransactionDetailsCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getTransactionDetails, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int refreshIdv(String str, IRefreshIdvCallback iRefreshIdvCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iRefreshIdvCallback != null ? iRefreshIdvCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_refreshIdv, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setPin(String str, char[] cArr, ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeCharArray(cArr);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_setPin, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (globalMembershipCardRegisterRequestData != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        globalMembershipCardRegisterRequestData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGlobalMembershipCardRegisterCallback != null ? iGlobalMembershipCardRegisterCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getGlobalMembershipCardRegisterData, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (globalMembershipCardRegisterRequestData != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        globalMembershipCardRegisterRequestData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iGlobalMembershipCardRegisterCallback != null ? iGlobalMembershipCardRegisterCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getGlobalMembershipCardTzEncData, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> list, IGlobalMembershipCardExtractDetailCallback iGlobalMembershipCardExtractDetailCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    obtain.writeStrongBinder(iGlobalMembershipCardExtractDetailCallback != null ? iGlobalMembershipCardExtractDetailCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_extractGlobalMembershipCardDetail, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startGlobalMembershipCardPay(String str, byte[] bArr, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeByteArray(bArr);
                    if (securedObject != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        securedObject.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (payConfig != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        payConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iPayCallback != null ? iPayCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_startGlobalMembershipCardPay, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int updateBinAttribute(String str, String str2, ICommonCallback iCommonCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iCommonCallback != null ? iCommonCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_updateBinAttribute, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public CommonSpayResponse processSpayApdu(byte[] bArr, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    CommonSpayResponse commonSpayResponse;
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (bundle != null) {
                        obtain.writeInt(Stub.TRANSACTION_enrollCard);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_processSpayApdu, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        commonSpayResponse = (CommonSpayResponse) CommonSpayResponse.CREATOR.createFromParcel(obtain2);
                    } else {
                        commonSpayResponse = null;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return commonSpayResponse;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPaymentFramework asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPaymentFramework)) {
                return new Proxy(iBinder);
            }
            return (IPaymentFramework) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            boolean z = false;
            PayConfig payConfig = null;
            int enrollCard;
            String readString;
            SecuredObject securedObject;
            PayConfig payConfig2;
            String readString2;
            Bundle bundle;
            List allCardState;
            GiftCardRegisterRequestData giftCardRegisterRequestData;
            byte[] createByteArray;
            SecuredObject securedObject2;
            GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData;
            switch (i) {
                case TRANSACTION_enrollCard /*1*/:
                    EnrollCardInfo enrollCardInfo;
                    BillingInfo billingInfo;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        enrollCardInfo = (EnrollCardInfo) EnrollCardInfo.CREATOR.createFromParcel(parcel);
                    } else {
                        enrollCardInfo = null;
                    }
                    if (parcel.readInt() != 0) {
                        billingInfo = (BillingInfo) BillingInfo.CREATOR.createFromParcel(parcel);
                    } else {
                        billingInfo = null;
                    }
                    enrollCard = enrollCard(enrollCardInfo, billingInfo, com.samsung.android.spayfw.appinterface.IEnrollCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_acceptTnC /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = acceptTnC(parcel.readString(), parcel.readInt() != 0, com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_provisionToken /*3*/:
                    ProvisionTokenInfo provisionTokenInfo;
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        provisionTokenInfo = (ProvisionTokenInfo) ProvisionTokenInfo.CREATOR.createFromParcel(parcel);
                    } else {
                        provisionTokenInfo = null;
                    }
                    enrollCard = provisionToken(readString, provisionTokenInfo, com.samsung.android.spayfw.appinterface.IProvisionTokenCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getTokenStatus /*4*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    TokenStatus tokenStatus = getTokenStatus(parcel.readString());
                    parcel2.writeNoException();
                    if (tokenStatus != null) {
                        parcel2.writeInt(TRANSACTION_enrollCard);
                        tokenStatus.writeToParcel(parcel2, TRANSACTION_enrollCard);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case TRANSACTION_selectIdv /*5*/:
                    IdvMethod idvMethod;
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        idvMethod = (IdvMethod) IdvMethod.CREATOR.createFromParcel(parcel);
                    } else {
                        idvMethod = null;
                    }
                    enrollCard = selectIdv(readString, idvMethod, com.samsung.android.spayfw.appinterface.ISelectIdvCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_verifyIdv /*6*/:
                    VerifyIdvInfo verifyIdvInfo;
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        verifyIdvInfo = (VerifyIdvInfo) VerifyIdvInfo.CREATOR.createFromParcel(parcel);
                    } else {
                        verifyIdvInfo = null;
                    }
                    enrollCard = verifyIdv(readString, verifyIdvInfo, com.samsung.android.spayfw.appinterface.IVerifyIdvCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_selectCard /*7*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = selectCard(parcel.readString(), com.samsung.android.spayfw.appinterface.ISelectCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_startPay /*8*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        securedObject = (SecuredObject) SecuredObject.CREATOR.createFromParcel(parcel);
                    } else {
                        securedObject = null;
                    }
                    if (parcel.readInt() != 0) {
                        payConfig2 = (PayConfig) PayConfig.CREATOR.createFromParcel(parcel);
                    } else {
                        payConfig2 = null;
                    }
                    enrollCard = startPay(securedObject, payConfig2, com.samsung.android.spayfw.appinterface.IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_stopPay /*9*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = stopPay(com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_processPushMessage /*10*/:
                    PushMessage pushMessage;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        pushMessage = (PushMessage) PushMessage.CREATOR.createFromParcel(parcel);
                    } else {
                        pushMessage = null;
                    }
                    enrollCard = processPushMessage(pushMessage, com.samsung.android.spayfw.appinterface.IPushMessageCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_clearCard /*11*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    clearCard();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_notifyDeviceReset /*12*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = notifyDeviceReset(com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getCardAttributes /*13*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString2 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    enrollCard = getCardAttributes(readString2, z, com.samsung.android.spayfw.appinterface.ICardAttributeCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_setJwtToken /*14*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = setJwtToken(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_reset /*15*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = reset(parcel.readString(), com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getAllCardState /*16*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    } else {
                        bundle = null;
                    }
                    allCardState = getAllCardState(bundle);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(allCardState);
                    return true;
                case TRANSACTION_clearEnrolledCard /*17*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = clearEnrolledCard(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getPaymentReadyState /*18*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    allCardState = getPaymentReadyState(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeStringList(allCardState);
                    return true;
                case TRANSACTION_getCardData /*19*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = getCardData(parcel.readString(), com.samsung.android.spayfw.appinterface.ICardDataCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_deleteCard /*20*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    } else {
                        bundle = null;
                    }
                    enrollCard = deleteCard(readString, bundle, com.samsung.android.spayfw.appinterface.IDeleteCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_synchronizeCards /*21*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = synchronizeCards(parcel.readString(), com.samsung.android.spayfw.appinterface.ISynchronizeCardsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_startInAppPay /*22*/:
                    InAppTransactionInfo inAppTransactionInfo;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        securedObject = (SecuredObject) SecuredObject.CREATOR.createFromParcel(parcel);
                    } else {
                        securedObject = null;
                    }
                    if (parcel.readInt() != 0) {
                        inAppTransactionInfo = (InAppTransactionInfo) InAppTransactionInfo.CREATOR.createFromParcel(parcel);
                    } else {
                        inAppTransactionInfo = null;
                    }
                    enrollCard = startInAppPay(securedObject, inAppTransactionInfo, com.samsung.android.spayfw.appinterface.IInAppPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getLogs /*23*/:
                    ParcelFileDescriptor parcelFileDescriptor;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    enrollCard = getLogs(parcelFileDescriptor, parcel.readString(), com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getGiftCardRegisterData /*24*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        giftCardRegisterRequestData = (GiftCardRegisterRequestData) GiftCardRegisterRequestData.CREATOR.createFromParcel(parcel);
                    } else {
                        giftCardRegisterRequestData = null;
                    }
                    enrollCard = getGiftCardRegisterData(giftCardRegisterRequestData, com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getGiftCardTzEncData /*25*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        giftCardRegisterRequestData = (GiftCardRegisterRequestData) GiftCardRegisterRequestData.CREATOR.createFromParcel(parcel);
                    } else {
                        giftCardRegisterRequestData = null;
                    }
                    enrollCard = getGiftCardTzEncData(giftCardRegisterRequestData, com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_startGiftCardPay /*26*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    byte[] createByteArray2 = parcel.createByteArray();
                    createByteArray = parcel.createByteArray();
                    if (parcel.readInt() != 0) {
                        securedObject2 = (SecuredObject) SecuredObject.CREATOR.createFromParcel(parcel);
                    } else {
                        securedObject2 = null;
                    }
                    if (parcel.readInt() != 0) {
                        payConfig = (PayConfig) PayConfig.CREATOR.createFromParcel(parcel);
                    }
                    enrollCard = startGiftCardPay(createByteArray2, createByteArray, securedObject2, payConfig, com.samsung.android.spayfw.appinterface.IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_extractGiftCardDetail /*27*/:
                    ExtractGiftCardDetailRequest extractGiftCardDetailRequest;
                    SecuredObject securedObject3;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        extractGiftCardDetailRequest = (ExtractGiftCardDetailRequest) ExtractGiftCardDetailRequest.CREATOR.createFromParcel(parcel);
                    } else {
                        extractGiftCardDetailRequest = null;
                    }
                    if (parcel.readInt() != 0) {
                        securedObject3 = (SecuredObject) SecuredObject.CREATOR.createFromParcel(parcel);
                    } else {
                        securedObject3 = null;
                    }
                    enrollCard = extractGiftCardDetail(extractGiftCardDetailRequest, securedObject3, com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_retryPay /*28*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        payConfig2 = (PayConfig) PayConfig.CREATOR.createFromParcel(parcel);
                    } else {
                        payConfig2 = null;
                    }
                    enrollCard = retryPay(payConfig2);
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getEnvironment /*29*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = getEnvironment(com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_isDsrpBlobMissing /*30*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    allCardState = isDsrpBlobMissing();
                    parcel2.writeNoException();
                    parcel2.writeStringList(allCardState);
                    return true;
                case TRANSACTION_isDsrpBlobMissingForTokenId /*31*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = isDsrpBlobMissingForTokenId(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_storeUserSignature /*32*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = storeUserSignature(parcel.readString(), parcel.createByteArray(), com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getUserSignature /*33*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = getUserSignature(parcel.readString(), com.samsung.android.spayfw.appinterface.IUserSignatureCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_setConfig /*34*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = setConfig(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getConfig /*35*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString2 = getConfig(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(readString2);
                    return true;
                case TRANSACTION_processServerRequest /*36*/:
                    ServerRequest serverRequest;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        serverRequest = (ServerRequest) ServerRequest.CREATOR.createFromParcel(parcel);
                    } else {
                        serverRequest = null;
                    }
                    enrollCard = processServerRequest(serverRequest, com.samsung.android.spayfw.appinterface.IServerResponseCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_updateLoyaltyCard /*37*/:
                    UpdateLoyaltyCardInfo updateLoyaltyCardInfo;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        updateLoyaltyCardInfo = (UpdateLoyaltyCardInfo) UpdateLoyaltyCardInfo.CREATOR.createFromParcel(parcel);
                    } else {
                        updateLoyaltyCardInfo = null;
                    }
                    enrollCard = updateLoyaltyCard(updateLoyaltyCardInfo, com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_extractLoyaltyCardDetails /*38*/:
                    ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        extractLoyaltyCardDetailRequest = (ExtractLoyaltyCardDetailRequest) ExtractLoyaltyCardDetailRequest.CREATOR.createFromParcel(parcel);
                    } else {
                        extractLoyaltyCardDetailRequest = null;
                    }
                    enrollCard = extractLoyaltyCardDetails(extractLoyaltyCardDetailRequest, com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_startLoyaltyCardPay /*39*/:
                    LoyaltyCardShowRequest loyaltyCardShowRequest;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        loyaltyCardShowRequest = (LoyaltyCardShowRequest) LoyaltyCardShowRequest.CREATOR.createFromParcel(parcel);
                    } else {
                        loyaltyCardShowRequest = null;
                    }
                    enrollCard = startLoyaltyCardPay(loyaltyCardShowRequest, com.samsung.android.spayfw.appinterface.IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getTransactionDetails /*40*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = getTransactionDetails(parcel.readString(), parcel.readLong(), parcel.readLong(), parcel.readInt(), com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_refreshIdv /*41*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = refreshIdv(parcel.readString(), com.samsung.android.spayfw.appinterface.IRefreshIdvCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_setPin /*42*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = setPin(parcel.readString(), parcel.createCharArray(), com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getGlobalMembershipCardRegisterData /*43*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        globalMembershipCardRegisterRequestData = (GlobalMembershipCardRegisterRequestData) GlobalMembershipCardRegisterRequestData.CREATOR.createFromParcel(parcel);
                    } else {
                        globalMembershipCardRegisterRequestData = null;
                    }
                    enrollCard = getGlobalMembershipCardRegisterData(globalMembershipCardRegisterRequestData, com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_getGlobalMembershipCardTzEncData /*44*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        globalMembershipCardRegisterRequestData = (GlobalMembershipCardRegisterRequestData) GlobalMembershipCardRegisterRequestData.CREATOR.createFromParcel(parcel);
                    } else {
                        globalMembershipCardRegisterRequestData = null;
                    }
                    enrollCard = getGlobalMembershipCardTzEncData(globalMembershipCardRegisterRequestData, com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_extractGlobalMembershipCardDetail /*45*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = extractGlobalMembershipCardDetail(parcel.createTypedArrayList(ExtractGlobalMembershipCardDetailRequest.CREATOR), com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_startGlobalMembershipCardPay /*46*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    createByteArray = parcel.createByteArray();
                    if (parcel.readInt() != 0) {
                        securedObject2 = (SecuredObject) SecuredObject.CREATOR.createFromParcel(parcel);
                    } else {
                        securedObject2 = null;
                    }
                    if (parcel.readInt() != 0) {
                        payConfig = (PayConfig) PayConfig.CREATOR.createFromParcel(parcel);
                    }
                    enrollCard = startGlobalMembershipCardPay(readString, createByteArray, securedObject2, payConfig, com.samsung.android.spayfw.appinterface.IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_updateBinAttribute /*47*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    enrollCard = updateBinAttribute(parcel.readString(), parcel.readString(), com.samsung.android.spayfw.appinterface.ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(enrollCard);
                    return true;
                case TRANSACTION_processSpayApdu /*48*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    createByteArray = parcel.createByteArray();
                    if (parcel.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    } else {
                        bundle = null;
                    }
                    CommonSpayResponse processSpayApdu = processSpayApdu(createByteArray, bundle);
                    parcel2.writeNoException();
                    if (processSpayApdu != null) {
                        parcel2.writeInt(TRANSACTION_enrollCard);
                        processSpayApdu.writeToParcel(parcel2, TRANSACTION_enrollCard);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int acceptTnC(String str, boolean z, ICommonCallback iCommonCallback);

    void clearCard();

    int clearEnrolledCard(String str);

    int deleteCard(String str, Bundle bundle, IDeleteCardCallback iDeleteCardCallback);

    int enrollCard(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback);

    int extractGiftCardDetail(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, IGiftCardExtractDetailCallback iGiftCardExtractDetailCallback);

    int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> list, IGlobalMembershipCardExtractDetailCallback iGlobalMembershipCardExtractDetailCallback);

    int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, IExtractLoyaltyCardDetailResponseCallback iExtractLoyaltyCardDetailResponseCallback);

    List<CardState> getAllCardState(Bundle bundle);

    int getCardAttributes(String str, boolean z, ICardAttributeCallback iCardAttributeCallback);

    int getCardData(String str, ICardDataCallback iCardDataCallback);

    String getConfig(String str);

    int getEnvironment(ICommonCallback iCommonCallback);

    int getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback);

    int getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback);

    int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback);

    int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback);

    int getLogs(ParcelFileDescriptor parcelFileDescriptor, String str, ICommonCallback iCommonCallback);

    List<String> getPaymentReadyState(String str);

    TokenStatus getTokenStatus(String str);

    int getTransactionDetails(String str, long j, long j2, int i, ITransactionDetailsCallback iTransactionDetailsCallback);

    int getUserSignature(String str, IUserSignatureCallback iUserSignatureCallback);

    List<String> isDsrpBlobMissing();

    int isDsrpBlobMissingForTokenId(String str);

    int notifyDeviceReset(ICommonCallback iCommonCallback);

    int processPushMessage(PushMessage pushMessage, IPushMessageCallback iPushMessageCallback);

    int processServerRequest(ServerRequest serverRequest, IServerResponseCallback iServerResponseCallback);

    CommonSpayResponse processSpayApdu(byte[] bArr, Bundle bundle);

    int provisionToken(String str, ProvisionTokenInfo provisionTokenInfo, IProvisionTokenCallback iProvisionTokenCallback);

    int refreshIdv(String str, IRefreshIdvCallback iRefreshIdvCallback);

    int reset(String str, ICommonCallback iCommonCallback);

    int retryPay(PayConfig payConfig);

    int selectCard(String str, ISelectCardCallback iSelectCardCallback);

    int selectIdv(String str, IdvMethod idvMethod, ISelectIdvCallback iSelectIdvCallback);

    int setConfig(String str, String str2);

    int setJwtToken(String str);

    int setPin(String str, char[] cArr, ICommonCallback iCommonCallback);

    int startGiftCardPay(byte[] bArr, byte[] bArr2, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback);

    int startGlobalMembershipCardPay(String str, byte[] bArr, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback);

    int startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback);

    int startLoyaltyCardPay(LoyaltyCardShowRequest loyaltyCardShowRequest, IPayCallback iPayCallback);

    int startPay(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback);

    int stopPay(ICommonCallback iCommonCallback);

    int storeUserSignature(String str, byte[] bArr, ICommonCallback iCommonCallback);

    int synchronizeCards(String str, ISynchronizeCardsCallback iSynchronizeCardsCallback);

    int updateBinAttribute(String str, String str2, ICommonCallback iCommonCallback);

    int updateLoyaltyCard(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback);

    int verifyIdv(String str, VerifyIdvInfo verifyIdvInfo, IVerifyIdvCallback iVerifyIdvCallback);
}
