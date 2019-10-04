/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.CardState;
import com.samsung.android.spayfw.appinterface.CommonSpayResponse;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractGlobalMembershipCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.ICardAttributeCallback;
import com.samsung.android.spayfw.appinterface.ICardDataCallback;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IDeleteCardCallback;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.ISelectIdvCallback;
import com.samsung.android.spayfw.appinterface.IServerResponseCallback;
import com.samsung.android.spayfw.appinterface.ISynchronizeCardsCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.IUserSignatureCallback;
import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.ServerRequest;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import java.util.ArrayList;
import java.util.List;

public interface IPaymentFramework
extends IInterface {
    public int acceptTnC(String var1, boolean var2, ICommonCallback var3);

    public void clearCard();

    public int clearEnrolledCard(String var1);

    public int deleteCard(String var1, Bundle var2, IDeleteCardCallback var3);

    public int enrollCard(EnrollCardInfo var1, BillingInfo var2, IEnrollCardCallback var3);

    public int extractGiftCardDetail(ExtractGiftCardDetailRequest var1, SecuredObject var2, IGiftCardExtractDetailCallback var3);

    public int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> var1, IGlobalMembershipCardExtractDetailCallback var2);

    public int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest var1, IExtractLoyaltyCardDetailResponseCallback var2);

    public List<CardState> getAllCardState(Bundle var1);

    public int getCardAttributes(String var1, boolean var2, ICardAttributeCallback var3);

    public int getCardData(String var1, ICardDataCallback var2);

    public String getConfig(String var1);

    public int getEnvironment(ICommonCallback var1);

    public int getGiftCardRegisterData(GiftCardRegisterRequestData var1, IGiftCardRegisterCallback var2);

    public int getGiftCardTzEncData(GiftCardRegisterRequestData var1, IGiftCardRegisterCallback var2);

    public int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData var1, IGlobalMembershipCardRegisterCallback var2);

    public int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData var1, IGlobalMembershipCardRegisterCallback var2);

    public int getLogs(ParcelFileDescriptor var1, String var2, ICommonCallback var3);

    public List<String> getPaymentReadyState(String var1);

    public TokenStatus getTokenStatus(String var1);

    public int getTransactionDetails(String var1, long var2, long var4, int var6, ITransactionDetailsCallback var7);

    public int getUserSignature(String var1, IUserSignatureCallback var2);

    public List<String> isDsrpBlobMissing();

    public int isDsrpBlobMissingForTokenId(String var1);

    public int notifyDeviceReset(ICommonCallback var1);

    public int processPushMessage(PushMessage var1, IPushMessageCallback var2);

    public int processServerRequest(ServerRequest var1, IServerResponseCallback var2);

    public CommonSpayResponse processSpayApdu(byte[] var1, Bundle var2);

    public int provisionToken(String var1, ProvisionTokenInfo var2, IProvisionTokenCallback var3);

    public int refreshIdv(String var1, IRefreshIdvCallback var2);

    public int reset(String var1, ICommonCallback var2);

    public int retryPay(PayConfig var1);

    public int selectCard(String var1, ISelectCardCallback var2);

    public int selectIdv(String var1, IdvMethod var2, ISelectIdvCallback var3);

    public int setConfig(String var1, String var2);

    public int setJwtToken(String var1);

    public int setPin(String var1, char[] var2, ICommonCallback var3);

    public int startGiftCardPay(byte[] var1, byte[] var2, SecuredObject var3, PayConfig var4, IPayCallback var5);

    public int startGlobalMembershipCardPay(String var1, byte[] var2, SecuredObject var3, PayConfig var4, IPayCallback var5);

    public int startInAppPay(SecuredObject var1, InAppTransactionInfo var2, IInAppPayCallback var3);

    public int startLoyaltyCardPay(LoyaltyCardShowRequest var1, IPayCallback var2);

    public int startPay(SecuredObject var1, PayConfig var2, IPayCallback var3);

    public int stopPay(ICommonCallback var1);

    public int storeUserSignature(String var1, byte[] var2, ICommonCallback var3);

    public int synchronizeCards(String var1, ISynchronizeCardsCallback var2);

    public int updateBinAttribute(String var1, String var2, ICommonCallback var3);

    public int updateLoyaltyCard(UpdateLoyaltyCardInfo var1, IUpdateLoyaltyCardCallback var2);

    public int verifyIdv(String var1, VerifyIdvInfo var2, IVerifyIdvCallback var3);

    public static abstract class Stub
    extends Binder
    implements IPaymentFramework {
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

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IPaymentFramework asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IPaymentFramework) {
                return (IPaymentFramework)iInterface;
            }
            return new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onTransact(int n2, Parcel parcel, Parcel parcel2, int n3) {
            switch (n2) {
                default: {
                    return super.onTransact(n2, parcel, parcel2, n3);
                }
                case 1598968902: {
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                }
                case 1: {
                    parcel.enforceInterface(DESCRIPTOR);
                    EnrollCardInfo enrollCardInfo = parcel.readInt() != 0 ? (EnrollCardInfo)EnrollCardInfo.CREATOR.createFromParcel(parcel) : null;
                    BillingInfo billingInfo = parcel.readInt() != 0 ? (BillingInfo)BillingInfo.CREATOR.createFromParcel(parcel) : null;
                    int n4 = this.enrollCard(enrollCardInfo, billingInfo, IEnrollCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n4);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    boolean bl = parcel.readInt() != 0;
                    int n5 = this.acceptTnC(string, bl, ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n5);
                    return true;
                }
                case 3: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    ProvisionTokenInfo provisionTokenInfo = parcel.readInt() != 0 ? (ProvisionTokenInfo)ProvisionTokenInfo.CREATOR.createFromParcel(parcel) : null;
                    int n6 = this.provisionToken(string, provisionTokenInfo, IProvisionTokenCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n6);
                    return true;
                }
                case 4: {
                    parcel.enforceInterface(DESCRIPTOR);
                    TokenStatus tokenStatus = this.getTokenStatus(parcel.readString());
                    parcel2.writeNoException();
                    if (tokenStatus != null) {
                        parcel2.writeInt(1);
                        tokenStatus.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                }
                case 5: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    IdvMethod idvMethod = parcel.readInt() != 0 ? (IdvMethod)IdvMethod.CREATOR.createFromParcel(parcel) : null;
                    int n7 = this.selectIdv(string, idvMethod, ISelectIdvCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n7);
                    return true;
                }
                case 6: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    VerifyIdvInfo verifyIdvInfo = parcel.readInt() != 0 ? (VerifyIdvInfo)VerifyIdvInfo.CREATOR.createFromParcel(parcel) : null;
                    int n8 = this.verifyIdv(string, verifyIdvInfo, IVerifyIdvCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n8);
                    return true;
                }
                case 7: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n9 = this.selectCard(parcel.readString(), ISelectCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n9);
                    return true;
                }
                case 8: {
                    parcel.enforceInterface(DESCRIPTOR);
                    SecuredObject securedObject = parcel.readInt() != 0 ? (SecuredObject)SecuredObject.CREATOR.createFromParcel(parcel) : null;
                    PayConfig payConfig = parcel.readInt() != 0 ? (PayConfig)PayConfig.CREATOR.createFromParcel(parcel) : null;
                    int n10 = this.startPay(securedObject, payConfig, IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n10);
                    return true;
                }
                case 9: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n11 = this.stopPay(ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n11);
                    return true;
                }
                case 10: {
                    parcel.enforceInterface(DESCRIPTOR);
                    PushMessage pushMessage = parcel.readInt() != 0 ? (PushMessage)PushMessage.CREATOR.createFromParcel(parcel) : null;
                    int n12 = this.processPushMessage(pushMessage, IPushMessageCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n12);
                    return true;
                }
                case 11: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.clearCard();
                    parcel2.writeNoException();
                    return true;
                }
                case 12: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n13 = this.notifyDeviceReset(ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n13);
                    return true;
                }
                case 13: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    int n14 = parcel.readInt();
                    boolean bl = false;
                    if (n14 != 0) {
                        bl = true;
                    }
                    int n15 = this.getCardAttributes(string, bl, ICardAttributeCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n15);
                    return true;
                }
                case 14: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n16 = this.setJwtToken(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(n16);
                    return true;
                }
                case 15: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n17 = this.reset(parcel.readString(), ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n17);
                    return true;
                }
                case 16: {
                    parcel.enforceInterface(DESCRIPTOR);
                    Bundle bundle = parcel.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel(parcel) : null;
                    List list = this.getAllCardState(bundle);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(list);
                    return true;
                }
                case 17: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n18 = this.clearEnrolledCard(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(n18);
                    return true;
                }
                case 18: {
                    parcel.enforceInterface(DESCRIPTOR);
                    List list = this.getPaymentReadyState(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeStringList(list);
                    return true;
                }
                case 19: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n19 = this.getCardData(parcel.readString(), ICardDataCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n19);
                    return true;
                }
                case 20: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    Bundle bundle = parcel.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel(parcel) : null;
                    int n20 = this.deleteCard(string, bundle, IDeleteCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n20);
                    return true;
                }
                case 21: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n21 = this.synchronizeCards(parcel.readString(), ISynchronizeCardsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n21);
                    return true;
                }
                case 22: {
                    parcel.enforceInterface(DESCRIPTOR);
                    SecuredObject securedObject = parcel.readInt() != 0 ? (SecuredObject)SecuredObject.CREATOR.createFromParcel(parcel) : null;
                    InAppTransactionInfo inAppTransactionInfo = parcel.readInt() != 0 ? (InAppTransactionInfo)InAppTransactionInfo.CREATOR.createFromParcel(parcel) : null;
                    int n22 = this.startInAppPay(securedObject, inAppTransactionInfo, IInAppPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n22);
                    return true;
                }
                case 23: {
                    parcel.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor parcelFileDescriptor = parcel.readInt() != 0 ? (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(parcel) : null;
                    int n23 = this.getLogs(parcelFileDescriptor, parcel.readString(), ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n23);
                    return true;
                }
                case 24: {
                    parcel.enforceInterface(DESCRIPTOR);
                    GiftCardRegisterRequestData giftCardRegisterRequestData = parcel.readInt() != 0 ? (GiftCardRegisterRequestData)GiftCardRegisterRequestData.CREATOR.createFromParcel(parcel) : null;
                    int n24 = this.getGiftCardRegisterData(giftCardRegisterRequestData, IGiftCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n24);
                    return true;
                }
                case 25: {
                    parcel.enforceInterface(DESCRIPTOR);
                    GiftCardRegisterRequestData giftCardRegisterRequestData = parcel.readInt() != 0 ? (GiftCardRegisterRequestData)GiftCardRegisterRequestData.CREATOR.createFromParcel(parcel) : null;
                    int n25 = this.getGiftCardTzEncData(giftCardRegisterRequestData, IGiftCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n25);
                    return true;
                }
                case 26: {
                    parcel.enforceInterface(DESCRIPTOR);
                    byte[] arrby = parcel.createByteArray();
                    byte[] arrby2 = parcel.createByteArray();
                    SecuredObject securedObject = parcel.readInt() != 0 ? (SecuredObject)SecuredObject.CREATOR.createFromParcel(parcel) : null;
                    int n26 = parcel.readInt();
                    PayConfig payConfig = null;
                    if (n26 != 0) {
                        payConfig = (PayConfig)PayConfig.CREATOR.createFromParcel(parcel);
                    }
                    int n27 = this.startGiftCardPay(arrby, arrby2, securedObject, payConfig, IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n27);
                    return true;
                }
                case 27: {
                    parcel.enforceInterface(DESCRIPTOR);
                    ExtractGiftCardDetailRequest extractGiftCardDetailRequest = parcel.readInt() != 0 ? (ExtractGiftCardDetailRequest)ExtractGiftCardDetailRequest.CREATOR.createFromParcel(parcel) : null;
                    SecuredObject securedObject = parcel.readInt() != 0 ? (SecuredObject)SecuredObject.CREATOR.createFromParcel(parcel) : null;
                    int n28 = this.extractGiftCardDetail(extractGiftCardDetailRequest, securedObject, IGiftCardExtractDetailCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n28);
                    return true;
                }
                case 28: {
                    parcel.enforceInterface(DESCRIPTOR);
                    PayConfig payConfig = parcel.readInt() != 0 ? (PayConfig)PayConfig.CREATOR.createFromParcel(parcel) : null;
                    int n29 = this.retryPay(payConfig);
                    parcel2.writeNoException();
                    parcel2.writeInt(n29);
                    return true;
                }
                case 29: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n30 = this.getEnvironment(ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n30);
                    return true;
                }
                case 30: {
                    parcel.enforceInterface(DESCRIPTOR);
                    List list = this.isDsrpBlobMissing();
                    parcel2.writeNoException();
                    parcel2.writeStringList(list);
                    return true;
                }
                case 31: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n31 = this.isDsrpBlobMissingForTokenId(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(n31);
                    return true;
                }
                case 32: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n32 = this.storeUserSignature(parcel.readString(), parcel.createByteArray(), ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n32);
                    return true;
                }
                case 33: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n33 = this.getUserSignature(parcel.readString(), IUserSignatureCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n33);
                    return true;
                }
                case 34: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n34 = this.setConfig(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(n34);
                    return true;
                }
                case 35: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = this.getConfig(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(string);
                    return true;
                }
                case 36: {
                    parcel.enforceInterface(DESCRIPTOR);
                    ServerRequest serverRequest = parcel.readInt() != 0 ? (ServerRequest)ServerRequest.CREATOR.createFromParcel(parcel) : null;
                    int n35 = this.processServerRequest(serverRequest, IServerResponseCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n35);
                    return true;
                }
                case 37: {
                    parcel.enforceInterface(DESCRIPTOR);
                    UpdateLoyaltyCardInfo updateLoyaltyCardInfo = parcel.readInt() != 0 ? (UpdateLoyaltyCardInfo)UpdateLoyaltyCardInfo.CREATOR.createFromParcel(parcel) : null;
                    int n36 = this.updateLoyaltyCard(updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n36);
                    return true;
                }
                case 38: {
                    parcel.enforceInterface(DESCRIPTOR);
                    ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest = parcel.readInt() != 0 ? (ExtractLoyaltyCardDetailRequest)ExtractLoyaltyCardDetailRequest.CREATOR.createFromParcel(parcel) : null;
                    int n37 = this.extractLoyaltyCardDetails(extractLoyaltyCardDetailRequest, IExtractLoyaltyCardDetailResponseCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n37);
                    return true;
                }
                case 39: {
                    parcel.enforceInterface(DESCRIPTOR);
                    LoyaltyCardShowRequest loyaltyCardShowRequest = parcel.readInt() != 0 ? (LoyaltyCardShowRequest)LoyaltyCardShowRequest.CREATOR.createFromParcel(parcel) : null;
                    int n38 = this.startLoyaltyCardPay(loyaltyCardShowRequest, IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n38);
                    return true;
                }
                case 40: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n39 = this.getTransactionDetails(parcel.readString(), parcel.readLong(), parcel.readLong(), parcel.readInt(), ITransactionDetailsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n39);
                    return true;
                }
                case 41: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n40 = this.refreshIdv(parcel.readString(), IRefreshIdvCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n40);
                    return true;
                }
                case 42: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n41 = this.setPin(parcel.readString(), parcel.createCharArray(), ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n41);
                    return true;
                }
                case 43: {
                    parcel.enforceInterface(DESCRIPTOR);
                    GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData = parcel.readInt() != 0 ? (GlobalMembershipCardRegisterRequestData)GlobalMembershipCardRegisterRequestData.CREATOR.createFromParcel(parcel) : null;
                    int n42 = this.getGlobalMembershipCardRegisterData(globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n42);
                    return true;
                }
                case 44: {
                    parcel.enforceInterface(DESCRIPTOR);
                    GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData = parcel.readInt() != 0 ? (GlobalMembershipCardRegisterRequestData)GlobalMembershipCardRegisterRequestData.CREATOR.createFromParcel(parcel) : null;
                    int n43 = this.getGlobalMembershipCardTzEncData(globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n43);
                    return true;
                }
                case 45: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n44 = this.extractGlobalMembershipCardDetail((List)parcel.createTypedArrayList(ExtractGlobalMembershipCardDetailRequest.CREATOR), IGlobalMembershipCardExtractDetailCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n44);
                    return true;
                }
                case 46: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    byte[] arrby = parcel.createByteArray();
                    SecuredObject securedObject = parcel.readInt() != 0 ? (SecuredObject)SecuredObject.CREATOR.createFromParcel(parcel) : null;
                    int n45 = parcel.readInt();
                    PayConfig payConfig = null;
                    if (n45 != 0) {
                        payConfig = (PayConfig)PayConfig.CREATOR.createFromParcel(parcel);
                    }
                    int n46 = this.startGlobalMembershipCardPay(string, arrby, securedObject, payConfig, IPayCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n46);
                    return true;
                }
                case 47: {
                    parcel.enforceInterface(DESCRIPTOR);
                    int n47 = this.updateBinAttribute(parcel.readString(), parcel.readString(), ICommonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(n47);
                    return true;
                }
                case 48: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            byte[] arrby = parcel.createByteArray();
            Bundle bundle = parcel.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel(parcel) : null;
            CommonSpayResponse commonSpayResponse = this.processSpayApdu(arrby, bundle);
            parcel2.writeNoException();
            if (commonSpayResponse != null) {
                parcel2.writeInt(1);
                commonSpayResponse.writeToParcel(parcel2, 1);
                return true;
            }
            parcel2.writeInt(0);
            return true;
        }

        private static class Proxy
        implements IPaymentFramework {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int acceptTnC(String string, boolean bl, ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    int n2 = 0;
                    if (bl) {
                        n2 = 1;
                    }
                    parcel.writeInt(n2);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(2, parcel, parcel2, 0);
                    parcel2.readException();
                    int n3 = parcel2.readInt();
                    return n3;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override
            public void clearCard() {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public int clearEnrolledCard(String string) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    this.mRemote.transact(17, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int deleteCard(String string, Bundle bundle, IDeleteCardCallback iDeleteCardCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    if (bundle != null) {
                        parcel.writeInt(1);
                        bundle.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iDeleteCardCallback != null ? iDeleteCardCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(20, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int enrollCard(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enrollCardInfo != null) {
                        parcel.writeInt(1);
                        enrollCardInfo.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (billingInfo != null) {
                        parcel.writeInt(1);
                        billingInfo.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iEnrollCardCallback != null ? iEnrollCardCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(1, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int extractGiftCardDetail(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, IGiftCardExtractDetailCallback iGiftCardExtractDetailCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extractGiftCardDetailRequest != null) {
                        parcel.writeInt(1);
                        extractGiftCardDetailRequest.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (securedObject != null) {
                        parcel.writeInt(1);
                        securedObject.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iGiftCardExtractDetailCallback != null ? iGiftCardExtractDetailCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(27, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> list, IGlobalMembershipCardExtractDetailCallback iGlobalMembershipCardExtractDetailCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeTypedList(list);
                    IBinder iBinder = iGlobalMembershipCardExtractDetailCallback != null ? iGlobalMembershipCardExtractDetailCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(45, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, IExtractLoyaltyCardDetailResponseCallback iExtractLoyaltyCardDetailResponseCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extractLoyaltyCardDetailRequest != null) {
                        parcel.writeInt(1);
                        extractLoyaltyCardDetailRequest.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iExtractLoyaltyCardDetailResponseCallback != null ? iExtractLoyaltyCardDetailResponseCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(38, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public List<CardState> getAllCardState(Bundle bundle) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        parcel.writeInt(1);
                        bundle.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(16, parcel, parcel2, 0);
                    parcel2.readException();
                    ArrayList arrayList = parcel2.createTypedArrayList(CardState.CREATOR);
                    return arrayList;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getCardAttributes(String string, boolean bl, ICardAttributeCallback iCardAttributeCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    int n2 = 0;
                    if (bl) {
                        n2 = 1;
                    }
                    parcel.writeInt(n2);
                    IBinder iBinder = iCardAttributeCallback != null ? iCardAttributeCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(13, parcel, parcel2, 0);
                    parcel2.readException();
                    int n3 = parcel2.readInt();
                    return n3;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getCardData(String string, ICardDataCallback iCardDataCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    IBinder iBinder = iCardDataCallback != null ? iCardDataCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(19, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public String getConfig(String string) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    this.mRemote.transact(35, parcel, parcel2, 0);
                    parcel2.readException();
                    String string2 = parcel2.readString();
                    return string2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getEnvironment(ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(29, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (giftCardRegisterRequestData != null) {
                        parcel.writeInt(1);
                        giftCardRegisterRequestData.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iGiftCardRegisterCallback != null ? iGiftCardRegisterCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(24, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (giftCardRegisterRequestData != null) {
                        parcel.writeInt(1);
                        giftCardRegisterRequestData.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iGiftCardRegisterCallback != null ? iGiftCardRegisterCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(25, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (globalMembershipCardRegisterRequestData != null) {
                        parcel.writeInt(1);
                        globalMembershipCardRegisterRequestData.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iGlobalMembershipCardRegisterCallback != null ? iGlobalMembershipCardRegisterCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(43, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (globalMembershipCardRegisterRequestData != null) {
                        parcel.writeInt(1);
                        globalMembershipCardRegisterRequestData.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iGlobalMembershipCardRegisterCallback != null ? iGlobalMembershipCardRegisterCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(44, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getLogs(ParcelFileDescriptor parcelFileDescriptor, String string, ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parcelFileDescriptor != null) {
                        parcel.writeInt(1);
                        parcelFileDescriptor.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    parcel.writeString(string);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(23, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public List<String> getPaymentReadyState(String string) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    this.mRemote.transact(18, parcel, parcel2, 0);
                    parcel2.readException();
                    ArrayList arrayList = parcel2.createStringArrayList();
                    return arrayList;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public TokenStatus getTokenStatus(String string) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    this.mRemote.transact(4, parcel, parcel2, 0);
                    parcel2.readException();
                    TokenStatus tokenStatus = parcel2.readInt() != 0 ? (TokenStatus)TokenStatus.CREATOR.createFromParcel(parcel2) : null;
                    return tokenStatus;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getTransactionDetails(String string, long l2, long l3, int n2, ITransactionDetailsCallback iTransactionDetailsCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeLong(l2);
                    parcel.writeLong(l3);
                    parcel.writeInt(n2);
                    IBinder iBinder = iTransactionDetailsCallback != null ? iTransactionDetailsCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(40, parcel, parcel2, 0);
                    parcel2.readException();
                    int n3 = parcel2.readInt();
                    return n3;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int getUserSignature(String string, IUserSignatureCallback iUserSignatureCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    IBinder iBinder = iUserSignatureCallback != null ? iUserSignatureCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(33, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public List<String> isDsrpBlobMissing() {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, parcel, parcel2, 0);
                    parcel2.readException();
                    ArrayList arrayList = parcel2.createStringArrayList();
                    return arrayList;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public int isDsrpBlobMissingForTokenId(String string) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    this.mRemote.transact(31, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int notifyDeviceReset(ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(12, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int processPushMessage(PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pushMessage != null) {
                        parcel.writeInt(1);
                        pushMessage.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iPushMessageCallback != null ? iPushMessageCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(10, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int processServerRequest(ServerRequest serverRequest, IServerResponseCallback iServerResponseCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serverRequest != null) {
                        parcel.writeInt(1);
                        serverRequest.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iServerResponseCallback != null ? iServerResponseCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(36, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public CommonSpayResponse processSpayApdu(byte[] arrby, Bundle bundle) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeByteArray(arrby);
                    if (bundle != null) {
                        parcel.writeInt(1);
                        bundle.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(48, parcel, parcel2, 0);
                    parcel2.readException();
                    CommonSpayResponse commonSpayResponse = parcel2.readInt() != 0 ? (CommonSpayResponse)CommonSpayResponse.CREATOR.createFromParcel(parcel2) : null;
                    return commonSpayResponse;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int provisionToken(String string, ProvisionTokenInfo provisionTokenInfo, IProvisionTokenCallback iProvisionTokenCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    if (provisionTokenInfo != null) {
                        parcel.writeInt(1);
                        provisionTokenInfo.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iProvisionTokenCallback != null ? iProvisionTokenCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int refreshIdv(String string, IRefreshIdvCallback iRefreshIdvCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    IBinder iBinder = iRefreshIdvCallback != null ? iRefreshIdvCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(41, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int reset(String string, ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(15, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int retryPay(PayConfig payConfig) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (payConfig != null) {
                        parcel.writeInt(1);
                        payConfig.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(28, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int selectCard(String string, ISelectCardCallback iSelectCardCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    IBinder iBinder = iSelectCardCallback != null ? iSelectCardCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(7, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int selectIdv(String string, IdvMethod idvMethod, ISelectIdvCallback iSelectIdvCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    if (idvMethod != null) {
                        parcel.writeInt(1);
                        idvMethod.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iSelectIdvCallback != null ? iSelectIdvCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(5, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public int setConfig(String string, String string2) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    this.mRemote.transact(34, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public int setJwtToken(String string) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    this.mRemote.transact(14, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int setPin(String string, char[] arrc, ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeCharArray(arrc);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(42, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int startGiftCardPay(byte[] arrby, byte[] arrby2, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeByteArray(arrby);
                    parcel.writeByteArray(arrby2);
                    if (securedObject != null) {
                        parcel.writeInt(1);
                        securedObject.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (payConfig != null) {
                        parcel.writeInt(1);
                        payConfig.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iPayCallback != null ? iPayCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(26, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int startGlobalMembershipCardPay(String string, byte[] arrby, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeByteArray(arrby);
                    if (securedObject != null) {
                        parcel.writeInt(1);
                        securedObject.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (payConfig != null) {
                        parcel.writeInt(1);
                        payConfig.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iPayCallback != null ? iPayCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(46, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (securedObject != null) {
                        parcel.writeInt(1);
                        securedObject.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (inAppTransactionInfo != null) {
                        parcel.writeInt(1);
                        inAppTransactionInfo.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iInAppPayCallback != null ? iInAppPayCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(22, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int startLoyaltyCardPay(LoyaltyCardShowRequest loyaltyCardShowRequest, IPayCallback iPayCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (loyaltyCardShowRequest != null) {
                        parcel.writeInt(1);
                        loyaltyCardShowRequest.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iPayCallback != null ? iPayCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(39, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int startPay(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (securedObject != null) {
                        parcel.writeInt(1);
                        securedObject.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (payConfig != null) {
                        parcel.writeInt(1);
                        payConfig.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iPayCallback != null ? iPayCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(8, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int stopPay(ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(9, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int storeUserSignature(String string, byte[] arrby, ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeByteArray(arrby);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(32, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int synchronizeCards(String string, ISynchronizeCardsCallback iSynchronizeCardsCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    IBinder iBinder = iSynchronizeCardsCallback != null ? iSynchronizeCardsCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(21, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int updateBinAttribute(String string, String string2, ICommonCallback iCommonCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    IBinder iBinder = iCommonCallback != null ? iCommonCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(47, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int updateLoyaltyCard(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (updateLoyaltyCardInfo != null) {
                        parcel.writeInt(1);
                        updateLoyaltyCardInfo.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iUpdateLoyaltyCardCallback != null ? iUpdateLoyaltyCardCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(37, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public int verifyIdv(String string, VerifyIdvInfo verifyIdvInfo, IVerifyIdvCallback iVerifyIdvCallback) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    if (verifyIdvInfo != null) {
                        parcel.writeInt(1);
                        verifyIdvInfo.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    IBinder iBinder = iVerifyIdvCallback != null ? iVerifyIdvCallback.asBinder() : null;
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(6, parcel, parcel2, 0);
                    parcel2.readException();
                    int n2 = parcel2.readInt();
                    return n2;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }
        }

    }

}

