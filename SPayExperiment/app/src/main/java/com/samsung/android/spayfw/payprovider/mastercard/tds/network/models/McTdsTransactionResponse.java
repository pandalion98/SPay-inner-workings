/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Comparator
 *  java.util.Date
 *  java.util.List
 *  org.apache.http.impl.cookie.DateParseException
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsCommonResponse;
import com.samsung.android.spayfw.payprovider.mastercard.utils.DateUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.http.impl.cookie.DateParseException;

public class McTdsTransactionResponse
extends McTdsCommonResponse {
    private static final String TAG = "McTdsTransactionResponse";
    private static final String TDS_TAG_ERROR = "e_McTdsTransactionResponse";
    private static final String TDS_TAG_INFO = "i_McTdsTransactionResponse";
    public static Comparator<TransactionData> tdsHistoryCompartor = new Comparator<TransactionData>(){

        /*
         * Unable to fully structure code
         * Enabled aggressive exception aggregation
         */
        public int compare(TransactionData var1_1, TransactionData var2_2) {
            block15 : {
                if (var1_1 == null || var2_2 == null || TextUtils.isEmpty((CharSequence)var1_1.getTransactionDate()) || TextUtils.isEmpty((CharSequence)var2_2.getTransactionDate())) {
                    return 0;
                }
                var7_4 = var10_3 = DateUtils.iso8601ToDate(var1_1.getTransactionDate());
                var9_6 = var11_5 = DateUtils.iso8601ToDate(var2_2.getTransactionDate());
                if (var7_4 == null || var9_6 == null) {
                    return 0;
                }
                break block15;
                catch (DateParseException var5_7) {
                    var6_8 = var5_7;
                    var7_4 = null;
lbl14: // 2 sources:
                    do {
                        var6_8.printStackTrace();
                        c.e("e_McTdsTransactionResponse", "tdsHistoryCompartor: Error : " + var6_8.getMessage());
                        if (var7_4 != null) {
                            var9_6 = null;
                            if (false) break block15;
                        }
                        return 0;
                        break;
                    } while (true);
                }
                catch (Throwable var3_10) {
                    var4_12 = null;
lbl23: // 2 sources:
                    do {
                        if (var4_12 == null || !false) {
                            return 0;
                        }
                        throw var3_11;
                        break;
                    } while (true);
                }
            }
            return var9_6.compareTo(var7_4);
            catch (Throwable var8_13) {
                var4_12 = var7_4;
                var3_11 = var8_13;
                ** continue;
            }
            {
                catch (DateParseException var6_9) {
                    ** continue;
                }
            }
        }
    };
    private String authenticationCode;
    private String lastUpdatedTag;
    private Transaction[] transactions;

    public String convertAuthorizationStatusForPayApp(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            c.e(TDS_TAG_ERROR, "convertAuthorizationStatusForPayApp: invalid type: " + string);
            return null;
        }
        if (string.equalsIgnoreCase("AUTHORIZED")) {
            return "Pending";
        }
        if (string.equalsIgnoreCase("DECLINED")) {
            return "Declined";
        }
        if (string.equalsIgnoreCase("CLEARED")) {
            return "Approved";
        }
        if (string.equalsIgnoreCase("REVERSED")) {
            return "Refunded";
        }
        c.e(TDS_TAG_ERROR, "convertAuthorizationStatusForPayApp: invalid type: " + string);
        return null;
    }

    public String convertDateForPayApp(String string) {
        Date date;
        try {
            date = DateUtils.iso8601ToDate(string);
        }
        catch (DateParseException dateParseException) {
            dateParseException.printStackTrace();
            c.e(TDS_TAG_ERROR, "convertDateForPayApp: Error parsing timestampData: " + dateParseException.getMessage());
            return null;
        }
        String string2 = DateUtils.convertToIso8601Basic(date, false);
        c.d(TAG, "convertDateForPayApp: converted timestamp for app: " + string2);
        return string2;
    }

    public String convertTransactionTypeForPayApp(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            c.e(TDS_TAG_ERROR, "convertTransactionTypeForPayApp: invalid type: " + string);
            return null;
        }
        if (string.equalsIgnoreCase("PURCHASE")) {
            return "Purchase";
        }
        if (string.equalsIgnoreCase("REFUND")) {
            return "Refund";
        }
        c.e(TDS_TAG_ERROR, "convertTransactionTypeForPayApp: invalid type: " + string);
        return null;
    }

    public String getAuthenticationCode() {
        return this.authenticationCode;
    }

    public String getLastUpdatedTag() {
        return this.lastUpdatedTag;
    }

    /*
     * Enabled aggressive block sorting
     */
    public TransactionDetails getTransactionDetailsForApp() {
        boolean bl = true;
        if (this.transactions == null) return null;
        if (this.transactions.length < bl) {
            return null;
        }
        TransactionDetails transactionDetails = new TransactionDetails();
        ArrayList arrayList = new ArrayList();
        for (Transaction transaction : this.transactions) {
            if (transaction == null) continue;
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(transaction.getAmount());
            transactionData.setCurrencyCode(transaction.getCurrencyCode());
            transactionData.setMechantName(transaction.getMerchantName());
            transactionData.setTransactionId(transaction.getRecordId());
            transactionData.setTransactionStatus(this.convertAuthorizationStatusForPayApp(transaction.getAuthorizationStatus()));
            transactionData.setTransactionType(this.convertTransactionTypeForPayApp(transaction.getTransactionType()));
            transactionData.setTransactionDate(this.convertDateForPayApp(transaction.getTransactionTimeStamp()));
            arrayList.add((Object)transactionData);
        }
        transactionDetails.setTransactionData((List<TransactionData>)arrayList);
        if (arrayList.size() <= 0) {
            bl = false;
        }
        c.i(TDS_TAG_INFO, "getTransactionDetailsForApp: " + bl);
        return transactionDetails;
    }

    public Transaction[] getTransactions() {
        return this.transactions;
    }

    public void setAuthenticationCode(String string) {
        this.authenticationCode = string;
    }

    public void setLastUpdatedTag(String string) {
        this.lastUpdatedTag = string;
    }

    public void setTransactions(Transaction[] arrtransaction) {
        this.transactions = arrtransaction;
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

        public String getAmount() {
            return this.amount;
        }

        public String getAuthorizationStatus() {
            return this.authorizationStatus;
        }

        public String getCurrencyCode() {
            return this.currencyCode;
        }

        public String getMerchantName() {
            return this.merchantName;
        }

        public String getMerchantType() {
            return this.merchantType;
        }

        public String getRecordId() {
            return this.recordId;
        }

        public String getTokenUniqueReference() {
            return this.tokenUniqueReference;
        }

        public String getTransactionIdentifier() {
            return this.transactionIdentifier;
        }

        public String getTransactionTimeStamp() {
            return this.transactionTimestamp;
        }

        public String getTransactionType() {
            return this.transactionType;
        }

        public void setAmount(String string) {
            this.amount = string;
        }

        public void setAuthorizationStatus(String string) {
            this.authorizationStatus = string;
        }

        public void setCurrencyCode(String string) {
            this.currencyCode = string;
        }

        public void setMerchantName(String string) {
            this.merchantName = string;
        }

        public void setMerchantType(String string) {
            this.merchantType = string;
        }

        public void setRecordId(String string) {
            this.recordId = string;
        }

        public void setTokenUniqueReference(String string) {
            this.tokenUniqueReference = string;
        }

        public void setTransactionIdentifier(String string) {
            this.transactionIdentifier = string;
        }

        public void setTransactionTimeStamp(String string) {
            this.transactionTimestamp = string;
        }

        public void setTransactionType(String string) {
            this.transactionType = string;
        }

        public String toString() {
            return "Transaction [tokenUniqueReference=" + this.tokenUniqueReference + ", recordId=" + this.recordId + ", transactionIdentifier=" + this.transactionIdentifier + ", transactionType=" + this.transactionType + ", amount=" + this.amount + ", currencyCode=" + this.currencyCode + ", authorizationStatus=" + this.authorizationStatus + ", transactionTimeStamp=" + this.transactionTimestamp + ", merchantName=" + this.merchantName + ", merchantType=" + this.merchantType + "]";
        }
    }

}

