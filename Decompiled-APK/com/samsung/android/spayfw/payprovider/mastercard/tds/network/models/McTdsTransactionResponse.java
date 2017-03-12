package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.utils.DateUtils;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData.Card;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.http.impl.cookie.DateParseException;

public class McTdsTransactionResponse extends McTdsCommonResponse {
    private static final String TAG = "McTdsTransactionResponse";
    private static final String TDS_TAG_ERROR = "e_McTdsTransactionResponse";
    private static final String TDS_TAG_INFO = "i_McTdsTransactionResponse";
    public static Comparator<TransactionData> tdsHistoryCompartor;
    private String authenticationCode;
    private String lastUpdatedTag;
    private Transaction[] transactions;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsTransactionResponse.1 */
    static class C05701 implements Comparator<TransactionData> {
        C05701() {
        }

        public int compare(TransactionData transactionData, TransactionData transactionData2) {
            Date iso8601ToDate;
            DateParseException e;
            Date date;
            Throwable th;
            Date date2 = null;
            if (transactionData == null || transactionData2 == null || TextUtils.isEmpty(transactionData.getTransactionDate()) || TextUtils.isEmpty(transactionData2.getTransactionDate())) {
                return 0;
            }
            try {
                iso8601ToDate = DateUtils.iso8601ToDate(transactionData.getTransactionDate());
                try {
                    date2 = DateUtils.iso8601ToDate(transactionData2.getTransactionDate());
                    if (iso8601ToDate == null || date2 == null) {
                        return 0;
                    }
                } catch (DateParseException e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        Log.m286e(McTdsTransactionResponse.TDS_TAG_ERROR, "tdsHistoryCompartor: Error : " + e.getMessage());
                        return 0;
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        date = iso8601ToDate;
                        th = th3;
                        if (date != null) {
                        }
                        return 0;
                    }
                }
            } catch (DateParseException e3) {
                e = e3;
                iso8601ToDate = date2;
                e.printStackTrace();
                Log.m286e(McTdsTransactionResponse.TDS_TAG_ERROR, "tdsHistoryCompartor: Error : " + e.getMessage());
                if (iso8601ToDate == null || date2 == null) {
                    return 0;
                }
                return date2.compareTo(iso8601ToDate);
            } catch (Throwable th4) {
                th = th4;
                date = date2;
                if (date != null || date2 == null) {
                    return 0;
                }
                throw th;
            }
            return date2.compareTo(iso8601ToDate);
        }
    }

    public static class Transaction {
        public static final String AUTHORIZED = "AUTHORIZED";
        public static final String CLEARED = "CLEARED";
        public static final String DECLINED = "DECLINED";
        public static final String PURCHASE = "PURCHASE";
        public static final String REFUND = "REFUND";
        public static final String REVERSED = "REVERSED";
        private String amount;
        private String authorizationStatus;
        private String currencyCode;
        private String merchantName;
        private String merchantType;
        private String recordId;
        private String tokenUniqueReference;
        private String transactionIdentifier;
        private String transactionTimestamp;
        private String transactionType;

        public String getTokenUniqueReference() {
            return this.tokenUniqueReference;
        }

        public void setTokenUniqueReference(String str) {
            this.tokenUniqueReference = str;
        }

        public String getRecordId() {
            return this.recordId;
        }

        public void setRecordId(String str) {
            this.recordId = str;
        }

        public String getTransactionIdentifier() {
            return this.transactionIdentifier;
        }

        public void setTransactionIdentifier(String str) {
            this.transactionIdentifier = str;
        }

        public String getTransactionType() {
            return this.transactionType;
        }

        public void setTransactionType(String str) {
            this.transactionType = str;
        }

        public String getAmount() {
            return this.amount;
        }

        public void setAmount(String str) {
            this.amount = str;
        }

        public String getCurrencyCode() {
            return this.currencyCode;
        }

        public void setCurrencyCode(String str) {
            this.currencyCode = str;
        }

        public String getAuthorizationStatus() {
            return this.authorizationStatus;
        }

        public void setAuthorizationStatus(String str) {
            this.authorizationStatus = str;
        }

        public String getTransactionTimeStamp() {
            return this.transactionTimestamp;
        }

        public void setTransactionTimeStamp(String str) {
            this.transactionTimestamp = str;
        }

        public String getMerchantName() {
            return this.merchantName;
        }

        public void setMerchantName(String str) {
            this.merchantName = str;
        }

        public String getMerchantType() {
            return this.merchantType;
        }

        public void setMerchantType(String str) {
            this.merchantType = str;
        }

        public String toString() {
            return "Transaction [tokenUniqueReference=" + this.tokenUniqueReference + ", recordId=" + this.recordId + ", transactionIdentifier=" + this.transactionIdentifier + ", transactionType=" + this.transactionType + ", amount=" + this.amount + ", currencyCode=" + this.currencyCode + ", authorizationStatus=" + this.authorizationStatus + ", transactionTimeStamp=" + this.transactionTimestamp + ", merchantName=" + this.merchantName + ", merchantType=" + this.merchantType + "]";
        }
    }

    public String getAuthenticationCode() {
        return this.authenticationCode;
    }

    public void setAuthenticationCode(String str) {
        this.authenticationCode = str;
    }

    public String getLastUpdatedTag() {
        return this.lastUpdatedTag;
    }

    public void setLastUpdatedTag(String str) {
        this.lastUpdatedTag = str;
    }

    public Transaction[] getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Transaction[] transactionArr) {
        this.transactions = transactionArr;
    }

    public TransactionDetails getTransactionDetailsForApp() {
        boolean z = true;
        if (this.transactions == null || this.transactions.length < 1) {
            return null;
        }
        TransactionDetails transactionDetails = new TransactionDetails();
        List arrayList = new ArrayList();
        for (Transaction transaction : this.transactions) {
            if (transaction != null) {
                TransactionData transactionData = new TransactionData();
                transactionData.setAmount(transaction.getAmount());
                transactionData.setCurrencyCode(transaction.getCurrencyCode());
                transactionData.setMechantName(transaction.getMerchantName());
                transactionData.setTransactionId(transaction.getRecordId());
                transactionData.setTransactionStatus(convertAuthorizationStatusForPayApp(transaction.getAuthorizationStatus()));
                transactionData.setTransactionType(convertTransactionTypeForPayApp(transaction.getTransactionType()));
                transactionData.setTransactionDate(convertDateForPayApp(transaction.getTransactionTimeStamp()));
                arrayList.add(transactionData);
            }
        }
        transactionDetails.setTransactionData(arrayList);
        if (arrayList.size() <= 0) {
            z = false;
        }
        Log.m287i(TDS_TAG_INFO, "getTransactionDetailsForApp: " + z);
        return transactionDetails;
    }

    static {
        tdsHistoryCompartor = new C05701();
    }

    public String convertDateForPayApp(String str) {
        try {
            String convertToIso8601Basic = DateUtils.convertToIso8601Basic(DateUtils.iso8601ToDate(str), false);
            Log.m285d(TAG, "convertDateForPayApp: converted timestamp for app: " + convertToIso8601Basic);
            return convertToIso8601Basic;
        } catch (DateParseException e) {
            e.printStackTrace();
            Log.m286e(TDS_TAG_ERROR, "convertDateForPayApp: Error parsing timestampData: " + e.getMessage());
            return null;
        }
    }

    public String convertAuthorizationStatusForPayApp(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.m286e(TDS_TAG_ERROR, "convertAuthorizationStatusForPayApp: invalid type: " + str);
            return null;
        } else if (str.equalsIgnoreCase(Card.STATUS_AUTHORIZED)) {
            return TransactionData.TRANSACTION_STATUS_PENDING;
        } else {
            if (str.equalsIgnoreCase(Transaction.DECLINED)) {
                return TransactionData.TRANSACTION_STATUS_DECLINED;
            }
            if (str.equalsIgnoreCase(Transaction.CLEARED)) {
                return TransactionData.TRANSACTION_STATUS_APPROVED;
            }
            if (str.equalsIgnoreCase(Transaction.REVERSED)) {
                return TransactionData.TRANSACTION_STATUS_REFUNDED;
            }
            Log.m286e(TDS_TAG_ERROR, "convertAuthorizationStatusForPayApp: invalid type: " + str);
            return null;
        }
    }

    public String convertTransactionTypeForPayApp(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.m286e(TDS_TAG_ERROR, "convertTransactionTypeForPayApp: invalid type: " + str);
            return null;
        } else if (str.equalsIgnoreCase(Transaction.PURCHASE)) {
            return TransactionData.TRANSACTION_TYPE_PURCHASE;
        } else {
            if (str.equalsIgnoreCase(Transaction.REFUND)) {
                return TransactionData.TRANSACTION_TYPE_REFUND;
            }
            Log.m286e(TDS_TAG_ERROR, "convertTransactionTypeForPayApp: invalid type: " + str);
            return null;
        }
    }
}
