package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

public class AcctTransactionOperations {
    String action;
    String deepLink;
    String intentExtraText;
    String packageName;

    public String getDeepLink() {
        return this.deepLink;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getAction() {
        return this.action;
    }

    public String getIntentExtraText() {
        return this.intentExtraText;
    }
}
