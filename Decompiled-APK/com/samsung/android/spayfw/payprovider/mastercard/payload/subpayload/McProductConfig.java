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

    static class UrlInfo {
        private int height;
        private String url;
        private int width;

        UrlInfo() {
        }

        public void setUrl(String str) {
            this.url = str;
        }

        public void setWidth(int i) {
            this.width = i;
        }

        public void setHeight(int i) {
            this.height = i;
        }

        public String getUrl() {
            return this.url;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }

    public void setBrandLogo_urls(UrlInfo[] urlInfoArr) {
        if (urlInfoArr != null) {
            this.brandLogo_urls = urlInfoArr;
        }
    }

    public void setIssuerLogo_urls(UrlInfo[] urlInfoArr) {
        if (this.issuerLogo_urls != null) {
            this.issuerLogo_urls = urlInfoArr;
        }
    }

    public void setCoBrandLogo_urls(UrlInfo[] urlInfoArr) {
        if (this.coBrandLogo_urls != null) {
            this.coBrandLogo_urls = urlInfoArr;
        }
    }

    public void setCardBackgroundCombined_urls(UrlInfo[] urlInfoArr) {
        if (this.cardBackgroundCombined_urls != null) {
            this.cardBackgroundCombined_urls = urlInfoArr;
        }
    }

    public void setCardBackground_urls(UrlInfo[] urlInfoArr) {
        if (this.cardBackground_urls != null) {
            this.cardBackground_urls = urlInfoArr;
        }
    }

    public void setIcon_urls(UrlInfo[] urlInfoArr) {
        if (this.icon_urls != null) {
            this.icon_urls = urlInfoArr;
        }
    }

    public void setForegroundColor_urls(UrlInfo[] urlInfoArr) {
        if (urlInfoArr != null) {
            this.foregroundColor_urls = urlInfoArr;
        }
    }

    public void setIssuerName(String str) {
        this.issuerName = str;
    }

    public void setShortDescription(String str) {
        this.shortDescription = str;
    }

    public void setLongDescription(String str) {
        this.longDescription = str;
    }

    public void setCustomerServiceUrl(String str) {
        this.customerServiceUrl = str;
    }

    public void setCustomerServiceEmail(String str) {
        this.customerServiceEmail = str;
    }

    public void setOnlineBankingLoginUrl(String str) {
        this.onlineBankingLoginUrl = str;
    }

    public void setTermsAndConditionsUrl(String str) {
        this.termsAndConditionsUrl = str;
    }

    public void setPrivacyPolicyUrl(String str) {
        this.privacyPolicyUrl = str;
    }

    public void setIsserProductConfigCode(String str) {
        this.isserProductConfigCode = str;
    }

    public UrlInfo[] getBrandLogo_urls() {
        return this.brandLogo_urls;
    }

    public UrlInfo[] getIssuerLogo_urls() {
        return this.issuerLogo_urls;
    }

    public UrlInfo[] getCoBrandLogo_urls() {
        return this.coBrandLogo_urls;
    }

    public UrlInfo[] getCardBackgroundCombined_urls() {
        return this.cardBackgroundCombined_urls;
    }

    public UrlInfo[] getCardBackground_urls() {
        return this.cardBackground_urls;
    }

    public UrlInfo[] getIcon_urls() {
        return this.icon_urls;
    }

    public UrlInfo[] getForegroundColor_urls() {
        return this.foregroundColor_urls;
    }

    public String getIssuerName() {
        return this.issuerName;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public String getCustomerServiceUrl() {
        return this.customerServiceUrl;
    }

    public String getCustomerServiceEmail() {
        return this.customerServiceEmail;
    }

    public String getOnlineBankingLoginUrl() {
        return this.onlineBankingLoginUrl;
    }

    public String getTermsAndConditionsUrl() {
        return this.termsAndConditionsUrl;
    }

    public String getPrivacyPolicyUrl() {
        return this.privacyPolicyUrl;
    }

    public String getIsserProductConfigCode() {
        return this.isserProductConfigCode;
    }
}
