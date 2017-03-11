package com.samsung.android.spayfw.core.p005a;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardResult;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkMessage;
import com.samsung.android.spayfw.core.StatusCodeTranslator;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.cashcard.CashCardClient;
import com.samsung.android.spayfw.remoteservice.cashcard.ListCashCardRequest;
import com.samsung.android.spayfw.remoteservice.cashcard.QueryCashCardRequest;
import com.samsung.android.spayfw.remoteservice.cashcard.SetPinRequest;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.PatchData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.bouncycastle.asn1.x509.DisplayText;
import org.json.JSONObject;

/* renamed from: com.samsung.android.spayfw.core.a.e */
public class CashCardProcessor extends Processor {
    CashCardClient kT;
    private PushMessage kU;
    private IPushMessageCallback kV;
    private char[] kW;
    private ICommonCallback kX;
    private String mEnrollmentId;

    /* renamed from: com.samsung.android.spayfw.core.a.e.1 */
    class CashCardProcessor extends C0413a<Response<String>, SetPinRequest> {
        final /* synthetic */ TokenRecord kY;
        final /* synthetic */ CashCardProcessor kZ;

        CashCardProcessor(CashCardProcessor cashCardProcessor, TokenRecord tokenRecord) {
            this.kZ = cashCardProcessor;
            this.kY = tokenRecord;
        }

        public void m380a(int i, Response<String> response) {
            int i2;
            ErrorResponseData errorResponseData = null;
            Log.m285d("CashCardProcessor", "process : onRequestComplete: code: " + i);
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    i2 = 0;
                    break;
                case 400:
                    ErrorResponseData fa = response != null ? response.fa() : null;
                    i2 = -5;
                    if (!(fa == null || fa.getCode() == null)) {
                        if (ErrorResponseData.ERROR_CODE_UNKNOWN_DATA.equals(fa.getCode())) {
                            i2 = -4;
                            break;
                        }
                    }
                    break;
                default:
                    if (response != null) {
                        errorResponseData = response.fa();
                    }
                    i2 = StatusCodeTranslator.m660a(i, errorResponseData);
                    break;
            }
            Log.m286e("CashCardProcessor", "Error Code : " + i2);
            if (i2 != 0) {
                try {
                    this.kZ.kX.onFail(this.kY.fx(), i2);
                    return;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
            this.kZ.kX.onSuccess(this.kZ.mEnrollmentId);
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.e.2 */
    class CashCardProcessor extends C0413a<Response<CashCardInfo>, QueryCashCardRequest> {
        final /* synthetic */ CashCardProcessor kZ;

        CashCardProcessor(CashCardProcessor cashCardProcessor) {
            this.kZ = cashCardProcessor;
        }

        public void m382a(int i, Response<CashCardInfo> response) {
            int i2;
            CashCardInfo cashCardInfo = null;
            Log.m285d("CashCardProcessor", "process : onRequestComplete: code: " + i);
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    if (response != null) {
                        cashCardInfo = (CashCardInfo) response.getResult();
                    }
                    this.kZ.m392a(cashCardInfo);
                    i2 = 0;
                    break;
                default:
                    ErrorResponseData fa;
                    if (response != null) {
                        fa = response.fa();
                    }
                    i2 = StatusCodeTranslator.m660a(i, fa);
                    break;
            }
            if (i2 != 0) {
                Log.m286e("CashCardProcessor", "Error Code : " + i2);
                try {
                    this.kZ.kV.onFail(this.kZ.kU.getNotificationId(), i2);
                    if (i != PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID) {
                        this.kZ.m400h(this.kZ.kU.getCardNumber(), "Get from CC Server Failed : " + i2);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.e.3 */
    class CashCardProcessor extends C0413a<Response<Collection<CashCardInfo>>, ListCashCardRequest> {
        final /* synthetic */ CashCardProcessor kZ;

        CashCardProcessor(CashCardProcessor cashCardProcessor) {
            this.kZ = cashCardProcessor;
        }

        public void m384a(int i, Response<Collection<CashCardInfo>> response) {
            int i2;
            String str = null;
            Log.m285d("CashCardProcessor", "process : onRequestComplete: code: " + i);
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    Collection collection;
                    if (response != null) {
                        collection = (Collection) response.getResult();
                    } else {
                        collection = null;
                    }
                    if (collection != null && collection.getElements() != null && ((CashCardInfo[]) collection.getElements()).length != 0) {
                        str = ((CashCardInfo[]) collection.getElements())[0].getId();
                        if (!this.kZ.m386L(((CashCardInfo[]) collection.getElements())[0].getId())) {
                            this.kZ.m392a(((CashCardInfo[]) collection.getElements())[0]);
                            i2 = 0;
                            break;
                        }
                        i2 = -3;
                        break;
                    }
                    i2 = -6;
                    break;
                    break;
                default:
                    ErrorResponseData fa;
                    if (response != null) {
                        fa = response.fa();
                    } else {
                        fa = null;
                    }
                    i2 = StatusCodeTranslator.m660a(i, fa);
                    break;
            }
            if (i2 != 0) {
                Log.m286e("CashCardProcessor", "Error Code : " + i2);
                try {
                    this.kZ.kV.onFail(this.kZ.kU.getNotificationId(), i2);
                    if (i != PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID) {
                        this.kZ.m400h(str, "Get from CC Server Failed : " + i2);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.e.4 */
    class CashCardProcessor implements IEnrollCardCallback {
        final /* synthetic */ CashCardProcessor kZ;
        final /* synthetic */ CashCardInfo la;

        CashCardProcessor(CashCardProcessor cashCardProcessor, CashCardInfo cashCardInfo) {
            this.kZ = cashCardProcessor;
            this.la = cashCardInfo;
        }

        public void onSuccess(EnrollCardResult enrollCardResult) {
            Log.m286e("CashCardProcessor", "enrollCashCard.onSuccess : " + enrollCardResult);
            this.kZ.m393a(this.la, enrollCardResult);
        }

        public void onFail(int i, EnrollCardResult enrollCardResult) {
            Log.m286e("CashCardProcessor", "enrollCashCard.onFail : " + i);
            if (i == -3) {
                Log.m287i("CashCardProcessor", "enrollCashCard.onFail : already enrolled, proceed with provisioning");
                this.kZ.m393a(this.la, enrollCardResult);
                return;
            }
            try {
                this.kZ.kV.onFail(this.kZ.kU.getNotificationId(), i);
                this.kZ.m400h(this.la.getId(), "Enrollment to TR Server Failed : " + i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public IBinder asBinder() {
            return null;
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.e.5 */
    class CashCardProcessor implements IProvisionTokenCallback {
        final /* synthetic */ CashCardProcessor kZ;
        final /* synthetic */ CashCardInfo la;
        final /* synthetic */ EnrollCardResult lb;

        /* renamed from: com.samsung.android.spayfw.core.a.e.5.1 */
        class CashCardProcessor implements Runnable {
            final /* synthetic */ ProvisionTokenResult lc;
            final /* synthetic */ CashCardProcessor ld;

            CashCardProcessor(CashCardProcessor cashCardProcessor, ProvisionTokenResult provisionTokenResult) {
                this.ld = cashCardProcessor;
                this.lc = provisionTokenResult;
            }

            public void run() {
                Parcelable transactionDetails = new TransactionDetails();
                transactionDetails.setBalanceAmount(this.ld.la.getFunds().getAmount() + BuildConfig.FLAVOR);
                transactionDetails.setBalanceCurrencyCode(this.ld.la.getFunds().getCurrency());
                List arrayList = new ArrayList();
                this.ld.kZ.m394a(arrayList, (TransactionInfo[]) this.ld.la.getTransactions().getElements());
                transactionDetails.setTransactionData(arrayList);
                Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_TRANSACTION_DETAILS_RECEIVED);
                intent.putExtra(PaymentFramework.EXTRA_TOKEN_ID, this.lc.getToken().getTokenId());
                intent.putExtra(PaymentFramework.EXTRA_TRANSACTION_DETAILS, transactionDetails);
                PaymentFrameworkApp.m315a(intent);
            }
        }

        /* renamed from: com.samsung.android.spayfw.core.a.e.5.2 */
        class CashCardProcessor implements ITransactionDetailsCallback {
            final /* synthetic */ CashCardProcessor ld;

            CashCardProcessor(CashCardProcessor cashCardProcessor) {
                this.ld = cashCardProcessor;
            }

            public void onTransactionUpdate(String str, TransactionDetails transactionDetails) {
                Log.m285d("CashCardProcessor", "onTransactionUpdate : tokenId " + str);
                Log.m285d("CashCardProcessor", "onTransactionUpdate : transactionDetails " + transactionDetails);
                Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_TRANSACTION_DETAILS_RECEIVED);
                intent.putExtra(PaymentFramework.EXTRA_TOKEN_ID, str);
                intent.putExtra(PaymentFramework.EXTRA_TRANSACTION_DETAILS, transactionDetails);
                PaymentFrameworkApp.m315a(intent);
            }

            public void onFail(String str, int i) {
                Log.m286e("CashCardProcessor", "onFail : " + i);
            }

            public IBinder asBinder() {
                return null;
            }
        }

        CashCardProcessor(CashCardProcessor cashCardProcessor, CashCardInfo cashCardInfo, EnrollCardResult enrollCardResult) {
            this.kZ = cashCardProcessor;
            this.la = cashCardInfo;
            this.lb = enrollCardResult;
        }

        public void onSuccess(String str, ProvisionTokenResult provisionTokenResult) {
            Log.m287i("CashCardProcessor", "provisionCashCard.onSuccess : " + provisionTokenResult);
            if (provisionTokenResult != null && provisionTokenResult.getToken() != null && provisionTokenResult.getToken().getMetadata() != null) {
                provisionTokenResult.getToken().getMetadata().setCardType(PaymentFramework.CARD_TYPE_SAMSUNG_REWARD);
                provisionTokenResult.getToken().getMetadata().setCardExpiryMonth(this.la.getExpiry().getMonth());
                provisionTokenResult.getToken().getMetadata().setCardExpiryYear(this.la.getExpiry().getYear());
                if (provisionTokenResult.getToken().getMetadata().getCardIssuer() != null) {
                    provisionTokenResult.getToken().getMetadata().getCardIssuer().setAccountNumber(this.la.getNumber());
                }
                TokenRecord bq = this.kZ.jJ.bq(provisionTokenResult.getToken().getTokenId());
                if (bq == null || bq.fx() == null || bq.fx().isEmpty()) {
                    Log.m286e("CashCardProcessor", "Unable to find Token Id OR Cash Card Id from db " + provisionTokenResult.getToken().getTokenId());
                    try {
                        this.kZ.kV.onFail(this.kZ.kU.getNotificationId(), -6);
                        this.kZ.m400h(this.la.getId(), "Provisioning to TR Server Failed : -6");
                        return;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                bq.setCardType(PaymentFramework.CARD_TYPE_SAMSUNG_REWARD);
                this.kZ.jJ.m1230d(bq);
                try {
                    this.kZ.kV.onCreateToken(this.kZ.kU.getNotificationId(), this.lb.getEnrollmentId(), provisionTokenResult.getToken());
                    TokenReport tokenReport = new TokenReport(this.kZ.kU.getNotificationId(), provisionTokenResult.getToken().getTokenId(), provisionTokenResult.getToken().getTokenStatus().getCode());
                    tokenReport.setEvent(PushMessage.TYPE_PROVISION);
                    this.kZ.kT.m1175a(this.la.getId(), tokenReport).fe();
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                }
                if (this.kZ.kU.getCardNumber() != null) {
                    PaymentFrameworkApp.az().postDelayed(new CashCardProcessor(this, provisionTokenResult), 1000);
                    return;
                }
                Log.m285d("CashCardProcessor", "Get Transaction Details : " + provisionTokenResult.getToken().getTokenId());
                PaymentFrameworkApp.az().m618a(PaymentFrameworkMessage.m623a(37, provisionTokenResult.getToken().getTokenId(), Long.valueOf(-1), Long.valueOf(-1), Integer.valueOf(-1), new CashCardProcessor(this)));
            }
        }

        public void onFail(String str, int i, ProvisionTokenResult provisionTokenResult) {
            Log.m286e("CashCardProcessor", "provisionCashCard.onFail : " + i);
            try {
                this.kZ.kV.onFail(this.kZ.kU.getNotificationId(), i);
                this.kZ.m400h(this.la.getId(), "Provisioning to TR Server Failed : " + i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public CashCardProcessor(Context context, PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
        super(context);
        if (pushMessage == null || iPushMessageCallback == null) {
            throw new IllegalArgumentException("PushMessage or IPushMessageCallback is null");
        } else if (pushMessage.getCardNumber() == null || !pushMessage.getCardNumber().isEmpty()) {
            this.kU = pushMessage;
            this.kV = iPushMessageCallback;
        } else {
            throw new IllegalArgumentException("PushMessage Card Number is null or empty");
        }
    }

    public CashCardProcessor(Context context, String str, char[] cArr, ICommonCallback iCommonCallback) {
        super(context);
        if (str == null || str.isEmpty() || cArr == null || iCommonCallback == null) {
            throw new IllegalArgumentException("Enrollment Id or Pin or ICommonCallback is null");
        }
        this.mEnrollmentId = str;
        this.kW = cArr;
        this.kX = iCommonCallback;
    }

    public void process() {
        this.kT = CashCardClient.m1172I(this.mContext);
        if (this.kX != null) {
            aW();
        } else if (this.kV == null) {
        } else {
            if (this.kU.getCardNumber() != null) {
                aX();
                return;
            }
            try {
                m399d(new JSONObject(this.kU.getMessage()).getBoolean("forceRecover"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void aW() {
        TokenRecord bp = this.jJ.bp(this.mEnrollmentId);
        if (bp == null) {
            Log.m286e("CashCardProcessor", "Unable to find Enrollment Id from db");
            try {
                this.kX.onFail(this.mEnrollmentId, -6);
            } catch (Throwable e) {
                Log.m284c("CashCardProcessor", e.getMessage(), e);
            }
        } else if (bp.fx() == null || bp.fx().isEmpty()) {
            Log.m286e("CashCardProcessor", "Unable to find Cash Card Id from db");
            try {
                this.kX.onFail(this.mEnrollmentId, -5);
            } catch (Throwable e2) {
                Log.m284c("CashCardProcessor", e2.getMessage(), e2);
            }
        } else {
            PatchData patchData = new PatchData();
            patchData.setPath("/pin/value");
            patchData.setValue(Integer.valueOf(Integer.parseInt(new String(this.kW))));
            this.kT.m1173a(bp.fx(), new PatchData[]{patchData}).m836a(new CashCardProcessor(this, bp));
        }
    }

    private void aX() {
        this.kT.m1177b(this.kU.getCardNumber(), true).m836a(new CashCardProcessor(this));
    }

    private void m399d(boolean z) {
        ListCashCardRequest bi = this.kT.bi(ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_USER_ID));
        bi.m1179g(z);
        bi.m836a(new CashCardProcessor(this));
    }

    private void m392a(CashCardInfo cashCardInfo) {
        if (cashCardInfo == null || cashCardInfo.getEncryptedPAN() == null || cashCardInfo.getEncryptedPAN().isEmpty()) {
            Log.m286e("CashCardProcessor", "Cash Card Info is null");
            try {
                String id;
                this.kV.onFail(this.kU.getNotificationId(), -6);
                if (cashCardInfo != null) {
                    id = cashCardInfo.getId();
                } else {
                    id = "NoCardID";
                }
                m400h(id, "Get from CC Server has Empty/Null Data");
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }
        EnrollCardReferenceInfo enrollCardReferenceInfo = new EnrollCardReferenceInfo();
        enrollCardReferenceInfo.setCardReferenceType(EnrollCardReferenceInfo.CARD_REF_TYPE_APP2APP);
        enrollCardReferenceInfo.setName("SamsungPay User");
        enrollCardReferenceInfo.setSppId("SPP_ID");
        enrollCardReferenceInfo.setGcmId("GCM_ID");
        enrollCardReferenceInfo.setApplicationId("com.samsung.android.spay");
        enrollCardReferenceInfo.setCardEntryMode(IdvMethod.IDV_TYPE_APP);
        enrollCardReferenceInfo.setCardBrand(PaymentFramework.CARD_BRAND_VISA);
        enrollCardReferenceInfo.setCardType(PaymentFramework.CARD_TYPE_SAMSUNG_REWARD);
        enrollCardReferenceInfo.setCardPresentationMode(EnrollCardInfo.CARD_PRESENTATION_MODE_ALL);
        Bundle bundle = new Bundle();
        bundle.putByteArray(EnrollCardReferenceInfo.ENROLL_PAYLOAD, cashCardInfo.getEncryptedPAN().getBytes());
        bundle.putString(EnrollCardReferenceInfo.ENROLL_INITIATOR_ID_KEY, "com.samsung.android.spay");
        bundle.putString(EnrollCardReferenceInfo.ENROLL_FEATURE_KEY, EnrollCardReferenceInfo.ENROLL_FEATURE_SAMSUNG_REWARDS_VALUE);
        enrollCardReferenceInfo.setExtraEnrollData(bundle);
        enrollCardReferenceInfo.setUserId(ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_USER_ID));
        enrollCardReferenceInfo.setWalletId(ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID));
        AccountManager accountManager = (AccountManager) this.mContext.getSystemService("account");
        Log.m289v("CashCardProcessor", "enrollCashCard.Account : " + accountManager.getAccounts().length);
        for (Account account : accountManager.getAccounts()) {
            Log.m289v("CashCardProcessor", "enrollCashCar.Account : " + account.name);
            Log.m289v("CashCardProcessor", "enrollCashCard.Account : " + account.type);
        }
        Object accountsByType = accountManager.getAccountsByType("com.osp.app.signin");
        if (accountsByType == null || accountsByType.length <= 0) {
            enrollCardReferenceInfo.setUserEmail("SamsungPayUser@samsung.com");
        } else {
            Log.m289v("CashCardProcessor", "enrollCashCard.Account : " + accountsByType.toString());
            enrollCardReferenceInfo.setUserEmail(accountsByType[0].name);
        }
        PaymentFrameworkApp.az().m618a(PaymentFrameworkMessage.m621a(1, enrollCardReferenceInfo, new BillingInfo("SamsungPay", "User"), new CashCardProcessor(this, cashCardInfo)));
    }

    private void m393a(CashCardInfo cashCardInfo, EnrollCardResult enrollCardResult) {
        if (this.iJ == null) {
            this.iJ = com.samsung.android.spayfw.core.Account.m551a(this.mContext, null);
        }
        if (enrollCardResult == null || enrollCardResult.getEnrollmentId() == null || enrollCardResult.getEnrollmentId().isEmpty() || this.iJ == null) {
            Log.m286e("CashCardProcessor", "Enroll Card Result/Account is null");
            try {
                this.kV.onFail(this.kU.getNotificationId(), -6);
                m400h(cashCardInfo.getId(), "Enrollment to TR Server has Empty/Null Data");
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }
        Card q = this.iJ.m558q(enrollCardResult.getEnrollmentId());
        TokenRecord bp = this.jJ.bp(enrollCardResult.getEnrollmentId());
        if (q == null || bp == null) {
            if (q == null) {
                Log.m286e("CashCardProcessor", "acceptTnc Failed - Invalid Enrollment Id");
            } else {
                Log.m286e("CashCardProcessor", "acceptTnc Failed - unable to find Enrollment Id from db");
            }
            try {
                this.kV.onFail(this.kU.getNotificationId(), -6);
                return;
            } catch (Throwable e2) {
                Log.m284c("CashCardProcessor", e2.getMessage(), e2);
                return;
            }
        }
        bp.m1252b(System.currentTimeMillis());
        bp.bs(cashCardInfo.getId());
        this.jJ.m1230d(bp);
        PaymentFrameworkApp.az().m618a(PaymentFrameworkMessage.m621a(3, enrollCardResult.getEnrollmentId(), null, new CashCardProcessor(this, cashCardInfo, enrollCardResult)));
    }

    private void m400h(String str, String str2) {
        ErrorReport errorReport = new ErrorReport();
        errorReport.setSeverity(ErrorReport.ERROR_SEVERITY_ERROR);
        errorReport.setCode(TokenReport.ERROR_WALLET_APPLICATION);
        errorReport.setDescription(str2);
        this.kT.m1174a(str, errorReport).fe();
    }

    private void m394a(List<TransactionData> list, TransactionInfo[] transactionInfoArr) {
        for (TransactionInfo transactionInfo : transactionInfoArr) {
            Log.m285d("CashCardProcessor", transactionInfo.toString());
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(transactionInfo.getAmount() + BuildConfig.FLAVOR);
            transactionData.setCurrencyCode(transactionInfo.getCurrency());
            transactionData.setMechantName(transactionInfo.getDetails());
            try {
                Date date = new Date();
                date.setTime(transactionInfo.getTime());
                Log.m285d("CashCardProcessor", date.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String format = simpleDateFormat.format(date);
                Log.m285d("CashCardProcessor", format);
                transactionData.setTransactionDate(format);
            } catch (Exception e) {
                Log.m286e("CashCardProcessor", e.getMessage());
            }
            transactionData.setTransactionId(transactionInfo.getId());
            if (transactionInfo.getType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_PURCHASE)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_PURCHASE);
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_APPROVED);
            } else if (transactionInfo.getType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_REFUND)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_REFUND);
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_REFUNDED);
            } else if (transactionInfo.getType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_REWARD)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_REWARD);
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_APPROVED);
            }
            list.add(transactionData);
        }
    }

    private boolean m386L(String str) {
        Log.m285d("CashCardProcessor", "Card Id : " + str);
        List c = this.jJ.m1228c(TokenColumn.CASH_CARD_ID, str);
        if (c != null && c.size() > 0) {
            Log.m285d("CashCardProcessor", "TokenRecord Size : " + c.size());
            TokenRecord tokenRecord = (TokenRecord) c.get(0);
            Log.m285d("CashCardProcessor", "Token Id : " + tokenRecord.getTrTokenId());
            if (tokenRecord.getTrTokenId() != null) {
                return true;
            }
        }
        return false;
    }
}
