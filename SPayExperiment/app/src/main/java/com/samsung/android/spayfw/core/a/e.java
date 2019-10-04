/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.accounts.AccountManager
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Message
 *  android.os.Parcelable
 *  android.os.RemoteException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.text.SimpleDateFormat
 *  java.util.ArrayList
 *  java.util.Date
 *  java.util.List
 *  java.util.Locale
 *  java.util.TimeZone
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.core.a;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.CardIssuer;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardResult;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.core.p;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.cashcard.b;
import com.samsung.android.spayfw.remoteservice.cashcard.d;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Expiry;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.PatchData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONObject;

public class e
extends o {
    com.samsung.android.spayfw.remoteservice.cashcard.a kT;
    private PushMessage kU;
    private IPushMessageCallback kV;
    private char[] kW;
    private ICommonCallback kX;
    private String mEnrollmentId;

    public e(Context context, PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
        super(context);
        if (pushMessage == null || iPushMessageCallback == null) {
            throw new IllegalArgumentException("PushMessage or IPushMessageCallback is null");
        }
        if (pushMessage.getCardNumber() != null && pushMessage.getCardNumber().isEmpty()) {
            throw new IllegalArgumentException("PushMessage Card Number is null or empty");
        }
        this.kU = pushMessage;
        this.kV = iPushMessageCallback;
    }

    public e(Context context, String string, char[] arrc, ICommonCallback iCommonCallback) {
        super(context);
        if (string == null || string.isEmpty() || arrc == null || iCommonCallback == null) {
            throw new IllegalArgumentException("Enrollment Id or Pin or ICommonCallback is null");
        }
        this.mEnrollmentId = string;
        this.kW = arrc;
        this.kX = iCommonCallback;
    }

    private boolean L(String string) {
        com.samsung.android.spayfw.b.c.d("CashCardProcessor", "Card Id : " + string);
        List<com.samsung.android.spayfw.storage.models.a> list = this.jJ.c(TokenRecordStorage.TokenGroup.TokenColumn.CD, string);
        if (list != null && list.size() > 0) {
            com.samsung.android.spayfw.b.c.d("CashCardProcessor", "TokenRecord Size : " + list.size());
            com.samsung.android.spayfw.storage.models.a a2 = (com.samsung.android.spayfw.storage.models.a)list.get(0);
            com.samsung.android.spayfw.b.c.d("CashCardProcessor", "Token Id : " + a2.getTrTokenId());
            if (a2.getTrTokenId() != null) {
                return true;
            }
        }
        return false;
    }

    static /* synthetic */ ICommonCallback a(e e2) {
        return e2.kX;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(final CashCardInfo cashCardInfo) {
        if (cashCardInfo == null || cashCardInfo.getEncryptedPAN() == null || cashCardInfo.getEncryptedPAN().isEmpty()) {
            com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Cash Card Info is null");
            try {
                this.kV.onFail(this.kU.getNotificationId(), -6);
                String string = cashCardInfo != null ? cashCardInfo.getId() : "NoCardID";
                this.h(string, "Get from CC Server has Empty/Null Data");
                return;
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
                return;
            }
        }
        EnrollCardReferenceInfo enrollCardReferenceInfo = new EnrollCardReferenceInfo();
        enrollCardReferenceInfo.setCardReferenceType("app2app");
        enrollCardReferenceInfo.setName("SamsungPay User");
        enrollCardReferenceInfo.setSppId("SPP_ID");
        enrollCardReferenceInfo.setGcmId("GCM_ID");
        enrollCardReferenceInfo.setApplicationId("com.samsung.android.spay");
        enrollCardReferenceInfo.setCardEntryMode("APP");
        enrollCardReferenceInfo.setCardBrand("VI");
        enrollCardReferenceInfo.setCardType("SAMSUNG_REWARD");
        enrollCardReferenceInfo.setCardPresentationMode("ALL");
        Bundle bundle = new Bundle();
        bundle.putByteArray("enrollPayload", cashCardInfo.getEncryptedPAN().getBytes());
        bundle.putString("enrollCardInitiatorId", "com.samsung.android.spay");
        bundle.putString("enrollCardFeature", "SAMSUNG_REWARDS_CARD");
        enrollCardReferenceInfo.setExtraEnrollData(bundle);
        enrollCardReferenceInfo.setUserId(com.samsung.android.spayfw.core.e.h(this.mContext).getConfig("CONFIG_USER_ID"));
        enrollCardReferenceInfo.setWalletId(com.samsung.android.spayfw.core.e.h(this.mContext).getConfig("CONFIG_WALLET_ID"));
        AccountManager accountManager = (AccountManager)this.mContext.getSystemService("account");
        com.samsung.android.spayfw.b.c.v("CashCardProcessor", "enrollCashCard.Account : " + accountManager.getAccounts().length);
        for (Account account : accountManager.getAccounts()) {
            com.samsung.android.spayfw.b.c.v("CashCardProcessor", "enrollCashCar.Account : " + account.name);
            com.samsung.android.spayfw.b.c.v("CashCardProcessor", "enrollCashCard.Account : " + account.type);
        }
        Account[] arraccount = accountManager.getAccountsByType("com.osp.app.signin");
        if (arraccount != null && arraccount.length > 0) {
            com.samsung.android.spayfw.b.c.v("CashCardProcessor", "enrollCashCard.Account : " + arraccount.toString());
            enrollCardReferenceInfo.setUserEmail(arraccount[0].name);
        } else {
            enrollCardReferenceInfo.setUserEmail("SamsungPayUser@samsung.com");
        }
        Message message = j.a(1, enrollCardReferenceInfo, new BillingInfo("SamsungPay", "User"), new IEnrollCardCallback(){

            public IBinder asBinder() {
                return null;
            }

            @Override
            public void onFail(int n2, EnrollCardResult enrollCardResult) {
                com.samsung.android.spayfw.b.c.e("CashCardProcessor", "enrollCashCard.onFail : " + n2);
                if (n2 == -3) {
                    com.samsung.android.spayfw.b.c.i("CashCardProcessor", "enrollCashCard.onFail : already enrolled, proceed with provisioning");
                    e.this.a(cashCardInfo, enrollCardResult);
                    return;
                }
                try {
                    e.this.kV.onFail(e.this.kU.getNotificationId(), n2);
                    e.this.h(cashCardInfo.getId(), "Enrollment to TR Server Failed : " + n2);
                    return;
                }
                catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                    return;
                }
            }

            @Override
            public void onSuccess(EnrollCardResult enrollCardResult) {
                com.samsung.android.spayfw.b.c.e("CashCardProcessor", "enrollCashCard.onSuccess : " + enrollCardResult);
                e.this.a(cashCardInfo, enrollCardResult);
            }
        });
        PaymentFrameworkApp.az().a(message);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(final CashCardInfo cashCardInfo, final EnrollCardResult enrollCardResult) {
        if (this.iJ == null) {
            this.iJ = a.a(this.mContext, null);
        }
        if (enrollCardResult == null || enrollCardResult.getEnrollmentId() == null || enrollCardResult.getEnrollmentId().isEmpty() || this.iJ == null) {
            com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Enroll Card Result/Account is null");
            try {
                this.kV.onFail(this.kU.getNotificationId(), -6);
                this.h(cashCardInfo.getId(), "Enrollment to TR Server has Empty/Null Data");
                return;
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
                return;
            }
        }
        c c2 = this.iJ.q(enrollCardResult.getEnrollmentId());
        com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bp(enrollCardResult.getEnrollmentId());
        if (c2 != null && a2 != null) {
            a2.b(System.currentTimeMillis());
            a2.bs(cashCardInfo.getId());
            this.jJ.d(a2);
            IProvisionTokenCallback iProvisionTokenCallback = new IProvisionTokenCallback(){

                public IBinder asBinder() {
                    return null;
                }

                @Override
                public void onFail(String string, int n2, ProvisionTokenResult provisionTokenResult) {
                    com.samsung.android.spayfw.b.c.e("CashCardProcessor", "provisionCashCard.onFail : " + n2);
                    try {
                        e.this.kV.onFail(e.this.kU.getNotificationId(), n2);
                        e.this.h(cashCardInfo.getId(), "Provisioning to TR Server Failed : " + n2);
                        return;
                    }
                    catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                        return;
                    }
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Lifted jumps to return sites
                 */
                @Override
                public void onSuccess(String string, final ProvisionTokenResult provisionTokenResult) {
                    com.samsung.android.spayfw.storage.models.a a2;
                    com.samsung.android.spayfw.b.c.i("CashCardProcessor", "provisionCashCard.onSuccess : " + provisionTokenResult);
                    if (provisionTokenResult == null) return;
                    if (provisionTokenResult.getToken() == null) return;
                    if (provisionTokenResult.getToken().getMetadata() == null) return;
                    provisionTokenResult.getToken().getMetadata().setCardType("SAMSUNG_REWARD");
                    provisionTokenResult.getToken().getMetadata().setCardExpiryMonth(cashCardInfo.getExpiry().getMonth());
                    provisionTokenResult.getToken().getMetadata().setCardExpiryYear(cashCardInfo.getExpiry().getYear());
                    if (provisionTokenResult.getToken().getMetadata().getCardIssuer() != null) {
                        provisionTokenResult.getToken().getMetadata().getCardIssuer().setAccountNumber(cashCardInfo.getNumber());
                    }
                    if ((a2 = e.this.jJ.bq(provisionTokenResult.getToken().getTokenId())) == null || a2.fx() == null || a2.fx().isEmpty()) {
                        com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Unable to find Token Id OR Cash Card Id from db " + provisionTokenResult.getToken().getTokenId());
                        try {
                            e.this.kV.onFail(e.this.kU.getNotificationId(), -6);
                            e.this.h(cashCardInfo.getId(), "Provisioning to TR Server Failed : -6");
                            return;
                        }
                        catch (RemoteException remoteException) {
                            remoteException.printStackTrace();
                            return;
                        }
                    }
                    a2.setCardType("SAMSUNG_REWARD");
                    e.this.jJ.d(a2);
                    try {
                        e.this.kV.onCreateToken(e.this.kU.getNotificationId(), enrollCardResult.getEnrollmentId(), provisionTokenResult.getToken());
                        TokenReport tokenReport = new TokenReport(e.this.kU.getNotificationId(), provisionTokenResult.getToken().getTokenId(), provisionTokenResult.getToken().getTokenStatus().getCode());
                        tokenReport.setEvent("PROVISION");
                        e.this.kT.a(cashCardInfo.getId(), tokenReport).fe();
                    }
                    catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                    if (e.this.kU.getCardNumber() != null) {
                        PaymentFrameworkApp.az().postDelayed(new Runnable(){

                            public void run() {
                                TransactionDetails transactionDetails = new TransactionDetails();
                                transactionDetails.setBalanceAmount(cashCardInfo.getFunds().getAmount() + "");
                                transactionDetails.setBalanceCurrencyCode(cashCardInfo.getFunds().getCurrency());
                                ArrayList arrayList = new ArrayList();
                                e.this.a((List<TransactionData>)((List)arrayList), cashCardInfo.getTransactions().getElements());
                                transactionDetails.setTransactionData((List<TransactionData>)arrayList);
                                Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
                                intent.putExtra("notiType", "transactionDetailsReceived");
                                intent.putExtra("tokenId", provisionTokenResult.getToken().getTokenId());
                                intent.putExtra("transactionDetails", (Parcelable)transactionDetails);
                                PaymentFrameworkApp.a(intent);
                            }
                        }, 1000L);
                        return;
                    }
                    com.samsung.android.spayfw.b.c.d("CashCardProcessor", "Get Transaction Details : " + provisionTokenResult.getToken().getTokenId());
                    Message message = j.a(37, provisionTokenResult.getToken().getTokenId(), -1L, -1L, -1, new ITransactionDetailsCallback(){

                        public IBinder asBinder() {
                            return null;
                        }

                        @Override
                        public void onFail(String string, int n2) {
                            com.samsung.android.spayfw.b.c.e("CashCardProcessor", "onFail : " + n2);
                        }

                        @Override
                        public void onTransactionUpdate(String string, TransactionDetails transactionDetails) {
                            com.samsung.android.spayfw.b.c.d("CashCardProcessor", "onTransactionUpdate : tokenId " + string);
                            com.samsung.android.spayfw.b.c.d("CashCardProcessor", "onTransactionUpdate : transactionDetails " + transactionDetails);
                            Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
                            intent.putExtra("notiType", "transactionDetailsReceived");
                            intent.putExtra("tokenId", string);
                            intent.putExtra("transactionDetails", (Parcelable)transactionDetails);
                            PaymentFrameworkApp.a(intent);
                        }
                    });
                    PaymentFrameworkApp.az().a(message);
                }

            };
            Message message = j.a(3, enrollCardResult.getEnrollmentId(), null, iProvisionTokenCallback);
            PaymentFrameworkApp.az().a(message);
            return;
        }
        if (c2 == null) {
            com.samsung.android.spayfw.b.c.e("CashCardProcessor", "acceptTnc Failed - Invalid Enrollment Id");
        } else {
            com.samsung.android.spayfw.b.c.e("CashCardProcessor", "acceptTnc Failed - unable to find Enrollment Id from db");
        }
        try {
            this.kV.onFail(this.kU.getNotificationId(), -6);
            return;
        }
        catch (RemoteException remoteException) {
            com.samsung.android.spayfw.b.c.c("CashCardProcessor", remoteException.getMessage(), remoteException);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(List<TransactionData> list, CashCardInfo.TransactionInfo[] arrtransactionInfo) {
        int n2 = arrtransactionInfo.length;
        int n3 = 0;
        while (n3 < n2) {
            CashCardInfo.TransactionInfo transactionInfo = arrtransactionInfo[n3];
            com.samsung.android.spayfw.b.c.d("CashCardProcessor", transactionInfo.toString());
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(transactionInfo.getAmount() + "");
            transactionData.setCurrencyCode(transactionInfo.getCurrency());
            transactionData.setMechantName(transactionInfo.getDetails());
            try {
                Date date = new Date();
                date.setTime(transactionInfo.getTime());
                com.samsung.android.spayfw.b.c.d("CashCardProcessor", date.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone((String)"GMT"));
                String string = simpleDateFormat.format(date);
                com.samsung.android.spayfw.b.c.d("CashCardProcessor", string);
                transactionData.setTransactionDate(string);
            }
            catch (Exception exception) {
                com.samsung.android.spayfw.b.c.e("CashCardProcessor", exception.getMessage());
            }
            transactionData.setTransactionId(transactionInfo.getId());
            if (transactionInfo.getType().equalsIgnoreCase("Purchase")) {
                transactionData.setTransactionType("Purchase");
                transactionData.setTransactionStatus("Approved");
            } else if (transactionInfo.getType().equalsIgnoreCase("Refund")) {
                transactionData.setTransactionType("Refund");
                transactionData.setTransactionStatus("Refunded");
            } else if (transactionInfo.getType().equalsIgnoreCase("Reward")) {
                transactionData.setTransactionType("Reward");
                transactionData.setTransactionStatus("Approved");
            }
            list.add((Object)transactionData);
            ++n3;
        }
        return;
    }

    private void aW() {
        final com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bp(this.mEnrollmentId);
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Unable to find Enrollment Id from db");
            try {
                this.kX.onFail(this.mEnrollmentId, -6);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("CashCardProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        if (a2.fx() == null || a2.fx().isEmpty()) {
            com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Unable to find Cash Card Id from db");
            try {
                this.kX.onFail(this.mEnrollmentId, -5);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("CashCardProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        PatchData patchData = new PatchData();
        patchData.setPath("/pin/value");
        patchData.setValue(Integer.parseInt((String)new String(this.kW)));
        PatchData[] arrpatchData = new PatchData[]{patchData};
        this.kT.a(a2.fx(), arrpatchData).a(new Request.a<com.samsung.android.spayfw.remoteservice.c<String>, d>(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<String> var2_2) {
                com.samsung.android.spayfw.b.c.d("CashCardProcessor", "process : onRequestComplete: code: " + var1_1);
                switch (var1_1) {
                    default: {
                        var6_3 = null;
                        if (var2_2 != null) {
                            var6_3 = var2_2.fa();
                        }
                        var4_4 = p.a(var1_1, var6_3);
                        break;
                    }
                    case 200: {
                        var4_4 = 0;
                        break;
                    }
                    case 400: {
                        var3_5 = var2_2 != null ? var2_2.fa() : null;
                        var4_4 = -5;
                        if (var3_5 == null || var3_5.getCode() == null || !"400.2".equals((Object)var3_5.getCode())) break;
                        var4_4 = -4;
                    }
                }
                com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Error Code : " + var4_4);
                if (var4_4 == 0) ** GOTO lbl22
                try {
                    e.a(e.this).onFail(a2.fx(), var4_4);
                    return;
lbl22: // 1 sources:
                    e.a(e.this).onSuccess(e.b(e.this));
                    return;
                }
                catch (RemoteException var5_6) {
                    var5_6.printStackTrace();
                    return;
                }
            }
        });
    }

    private void aX() {
        this.kT.b(this.kU.getCardNumber(), true).a(new Request.a<com.samsung.android.spayfw.remoteservice.c<CashCardInfo>, com.samsung.android.spayfw.remoteservice.cashcard.c>(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void a(int n2, com.samsung.android.spayfw.remoteservice.c<CashCardInfo> c2) {
                int n3;
                com.samsung.android.spayfw.b.c.d("CashCardProcessor", "process : onRequestComplete: code: " + n2);
                switch (n2) {
                    default: {
                        ErrorResponseData errorResponseData = null;
                        if (c2 != null) {
                            errorResponseData = c2.fa();
                        }
                        n3 = p.a(n2, errorResponseData);
                        break;
                    }
                    case 200: {
                        CashCardInfo cashCardInfo = null;
                        if (c2 != null) {
                            cashCardInfo = c2.getResult();
                        }
                        e.this.a(cashCardInfo);
                        return;
                    }
                }
                if (n3 == 0) return;
                com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Error Code : " + n3);
                try {
                    e.this.kV.onFail(e.this.kU.getNotificationId(), n3);
                    if (n2 == -206) return;
                    e.this.h(e.this.kU.getCardNumber(), "Get from CC Server Failed : " + n3);
                    return;
                }
                catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                    return;
                }
            }
        });
    }

    static /* synthetic */ String b(e e2) {
        return e2.mEnrollmentId;
    }

    private void d(boolean bl) {
        b b2 = this.kT.bi(com.samsung.android.spayfw.core.e.h(this.mContext).getConfig("CONFIG_USER_ID"));
        b2.g(bl);
        b2.a(new Request.a<com.samsung.android.spayfw.remoteservice.c<Collection<CashCardInfo>>, b>(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void a(int n2, com.samsung.android.spayfw.remoteservice.c<Collection<CashCardInfo>> c2) {
                String string;
                int n3;
                block9 : {
                    ErrorResponseData errorResponseData;
                    string = null;
                    com.samsung.android.spayfw.b.c.d("CashCardProcessor", "process : onRequestComplete: code: " + n2);
                    switch (n2) {
                        default: {
                            errorResponseData = c2 != null ? c2.fa() : null;
                        }
                        case 200: {
                            Collection<CashCardInfo> collection = c2 != null ? c2.getResult() : null;
                            if (collection == null || collection.getElements() == null || collection.getElements().length == 0) {
                                n3 = -6;
                                string = null;
                            } else {
                                string = collection.getElements()[0].getId();
                                if (e.this.L(collection.getElements()[0].getId())) {
                                    n3 = -3;
                                } else {
                                    e.this.a(collection.getElements()[0]);
                                    n3 = 0;
                                }
                            }
                            break block9;
                        }
                    }
                    n3 = p.a(n2, errorResponseData);
                }
                if (n3 == 0) return;
                com.samsung.android.spayfw.b.c.e("CashCardProcessor", "Error Code : " + n3);
                try {
                    e.this.kV.onFail(e.this.kU.getNotificationId(), n3);
                    if (n2 == -206) return;
                    e.this.h(string, "Get from CC Server Failed : " + n3);
                    return;
                }
                catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                    return;
                }
            }
        });
    }

    private void h(String string, String string2) {
        ErrorReport errorReport = new ErrorReport();
        errorReport.setSeverity("ERROR");
        errorReport.setCode("ERROR-10000");
        errorReport.setDescription(string2);
        this.kT.a(string, errorReport).fe();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void process() {
        this.kT = com.samsung.android.spayfw.remoteservice.cashcard.a.I(this.mContext);
        if (this.kX != null) {
            this.aW();
            return;
        }
        if (this.kV == null) return;
        {
            if (this.kU.getCardNumber() != null) {
                this.aX();
                return;
            }
        }
        try {
            this.d(new JSONObject(this.kU.getMessage()).getBoolean("forceRecover"));
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

}

