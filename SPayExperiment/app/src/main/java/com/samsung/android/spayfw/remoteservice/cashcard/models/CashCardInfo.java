/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.cashcard.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Expiry;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;

public class CashCardInfo {
    private Data data;
    private Expiry expiry;
    private Funds funds;
    private String href;
    private String id;
    private String number;
    private int suffix;
    private Collection<TransactionInfo> transactions;
    private Id user;

    public String getEncryptedPAN() {
        if (this.data != null) {
            return this.data.encryptedPAN;
        }
        return null;
    }

    public Expiry getExpiry() {
        return this.expiry;
    }

    public Funds getFunds() {
        return this.funds;
    }

    public String getHref() {
        return this.href;
    }

    public String getId() {
        return this.id;
    }

    public String getNumber() {
        return this.number;
    }

    public int getSuffix() {
        return this.suffix;
    }

    public Collection<TransactionInfo> getTransactions() {
        return this.transactions;
    }

    public Id getUser() {
        return this.user;
    }

    private static class Data {
        private String encryptedPAN;

        private Data() {
        }
    }

    public static class Funds {
        private double amount;
        private String currency;

        public double getAmount() {
            return this.amount;
        }

        public String getCurrency() {
            return this.currency;
        }
    }

    public static class Pin {
        private double createdAt;
        private double updatedAt;

        public double getCreatedTime() {
            return this.createdAt;
        }

        public double getUpdatedTime() {
            return this.updatedAt;
        }
    }

    public static class TransactionInfo {
        private double amount;
        private String currency;
        private String details;
        private String href;
        private String id;
        private long time;
        private String type;

        public double getAmount() {
            return this.amount;
        }

        public String getCurrency() {
            return this.currency;
        }

        public String getDetails() {
            return this.details;
        }

        public String getHref() {
            return this.href;
        }

        public String getId() {
            return this.id;
        }

        public long getTime() {
            return this.time;
        }

        public String getType() {
            return this.type;
        }

        public void setId(String string) {
            this.id = string;
        }

        public String toString() {
            return "TransactionInfo{id='" + this.id + '\'' + ", href='" + this.href + '\'' + ", time=" + this.time + ", details='" + this.details + '\'' + ", type='" + this.type + '\'' + ", amount=" + this.amount + ", currency='" + this.currency + '\'' + '}';
        }
    }

}

