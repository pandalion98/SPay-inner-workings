/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McProductConfig {
    private UrlInfo[] brandLogo_urls;
    private UrlInfo[] cardBackgroundCombined_urls;
    private UrlInfo[] cardBackground_urls;
    private UrlInfo[] coBrandLogo_urls;
    private String customerServiceEmail;
    private String customerServiceUrl;
    private UrlInfo[] foregroundColor_urls;
    private UrlInfo[] icon_urls;
    private String isserProductConfigCode;
    private UrlInfo[] issuerLogo_urls;
    private String issuerName;
    private String longDescription;
    private String onlineBankingLoginUrl;
    private String privacyPolicyUrl;
    private String shortDescription;
    private String termsAndConditionsUrl;

    public UrlInfo[] getBrandLogo_urls() {
        return this.brandLogo_urls;
    }

    public UrlInfo[] getCardBackgroundCombined_urls() {
        return this.cardBackgroundCombined_urls;
    }

    public UrlInfo[] getCardBackground_urls() {
        return this.cardBackground_urls;
    }

    public UrlInfo[] getCoBrandLogo_urls() {
        return this.coBrandLogo_urls;
    }

    public String getCustomerServiceEmail() {
        return this.customerServiceEmail;
    }

    public String getCustomerServiceUrl() {
        return this.customerServiceUrl;
    }

    public UrlInfo[] getForegroundColor_urls() {
        return this.foregroundColor_urls;
    }

    public UrlInfo[] getIcon_urls() {
        return this.icon_urls;
    }

    public String getIsserProductConfigCode() {
        return this.isserProductConfigCode;
    }

    public UrlInfo[] getIssuerLogo_urls() {
        return this.issuerLogo_urls;
    }

    public String getIssuerName() {
        return this.issuerName;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public String getOnlineBankingLoginUrl() {
        return this.onlineBankingLoginUrl;
    }

    public String getPrivacyPolicyUrl() {
        return this.privacyPolicyUrl;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getTermsAndConditionsUrl() {
        return this.termsAndConditionsUrl;
    }

    public void setBrandLogo_urls(UrlInfo[] arrurlInfo) {
        if (arrurlInfo == null) {
            return;
        }
        this.brandLogo_urls = arrurlInfo;
    }

    public void setCardBackgroundCombined_urls(UrlInfo[] arrurlInfo) {
        if (this.cardBackgroundCombined_urls == null) {
            return;
        }
        this.cardBackgroundCombined_urls = arrurlInfo;
    }

    public void setCardBackground_urls(UrlInfo[] arrurlInfo) {
        if (this.cardBackground_urls == null) {
            return;
        }
        this.cardBackground_urls = arrurlInfo;
    }

    public void setCoBrandLogo_urls(UrlInfo[] arrurlInfo) {
        if (this.coBrandLogo_urls == null) {
            return;
        }
        this.coBrandLogo_urls = arrurlInfo;
    }

    public void setCustomerServiceEmail(String string) {
        this.customerServiceEmail = string;
    }

    public void setCustomerServiceUrl(String string) {
        this.customerServiceUrl = string;
    }

    public void setForegroundColor_urls(UrlInfo[] arrurlInfo) {
        if (arrurlInfo == null) {
            return;
        }
        this.foregroundColor_urls = arrurlInfo;
    }

    public void setIcon_urls(UrlInfo[] arrurlInfo) {
        if (this.icon_urls == null) {
            return;
        }
        this.icon_urls = arrurlInfo;
    }

    public void setIsserProductConfigCode(String string) {
        this.isserProductConfigCode = string;
    }

    public void setIssuerLogo_urls(UrlInfo[] arrurlInfo) {
        if (this.issuerLogo_urls == null) {
            return;
        }
        this.issuerLogo_urls = arrurlInfo;
    }

    public void setIssuerName(String string) {
        this.issuerName = string;
    }

    public void setLongDescription(String string) {
        this.longDescription = string;
    }

    public void setOnlineBankingLoginUrl(String string) {
        this.onlineBankingLoginUrl = string;
    }

    public void setPrivacyPolicyUrl(String string) {
        this.privacyPolicyUrl = string;
    }

    public void setShortDescription(String string) {
        this.shortDescription = string;
    }

    public void setTermsAndConditionsUrl(String string) {
        this.termsAndConditionsUrl = string;
    }

    static class UrlInfo {
        private int height;
        private String url;
        private int width;

        UrlInfo() {
        }

        public int getHeight() {
            return this.height;
        }

        public String getUrl() {
            return this.url;
        }

        public int getWidth() {
            return this.width;
        }

        public void setHeight(int n2) {
            this.height = n2;
        }

        public void setUrl(String string) {
            this.url = string;
        }

        public void setWidth(int n2) {
            this.width = n2;
        }
    }

}

