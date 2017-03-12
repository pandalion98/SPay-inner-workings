package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.HashMap;
import java.util.Map;

public class ClearCardInfo {
    private static final int MAXLEN_BILLING_ADDR = 128;
    private static final int MAXLEN_BILLING_ZIP = 24;
    private static final int MAXLEN_USER_NAME = 64;
    private static final String TAG = "DCSDK_ClearCardInfo";
    private String billingAddr;
    private String billingZip;
    private String captureMethod;
    private String cardHolderName;
    private String cid;
    private String expDate;
    private String pan;
    private String seqNbr;
    private String source;

    public enum CaptureMethod {
        CAMERA(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND),
        MANUAL(CAPTUREMETHOD_DEFAULT_VALUE);
        
        private static final String CAPTUREMETHOD_DEFAULT_VALUE = "2";
        private static final Map<String, String> mCaptureMethodMapper;
        private String mCaptureMethod;

        static {
            mCaptureMethodMapper = new HashMap();
            mCaptureMethodMapper.put(EnrollCardInfo.CARD_ENTRY_MODE_MANUAL, CAPTUREMETHOD_DEFAULT_VALUE);
            mCaptureMethodMapper.put(EnrollCardInfo.CARD_ENTRY_MODE_OCR, TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        }

        public String getCaptureMethodString() {
            return this.mCaptureMethod;
        }

        public static String getDiscoverCaptureMethodValue(String str) {
            String str2 = (String) mCaptureMethodMapper.get(str);
            return str2 != null ? str2 : CAPTUREMETHOD_DEFAULT_VALUE;
        }

        private CaptureMethod(String str) {
            this.mCaptureMethod = str;
        }
    }

    public enum Source {
        ADD_DEVICE("add-device"),
        IN_APP("in-app"),
        ON_FILE("on-file"),
        RESTORE(PaymentFramework.OPERATION_TYPE_RESTORE),
        USER_INPUT("user-input");
        
        private static String SOURCE_DEFAULT_VALUE;
        private static final Map<String, String> mSourceMapper;
        private String mSource;

        static {
            mSourceMapper = new HashMap();
            SOURCE_DEFAULT_VALUE = "user-input";
            mSourceMapper.put(EnrollCardInfo.CARD_ENTRY_MODE_FILE, "on-file");
            mSourceMapper.put(IdvMethod.IDV_TYPE_APP, "in-app");
            mSourceMapper.put(EnrollCardInfo.CARD_ENTRY_MODE_MANUAL, "user-input");
            mSourceMapper.put(EnrollCardInfo.CARD_ENTRY_MODE_OCR, "user-input");
        }

        public String getSourceString() {
            return this.mSource;
        }

        public static String getDiscoverSourceValue(String str) {
            String str2 = (String) mSourceMapper.get(str);
            return str2 != null ? str2 : SOURCE_DEFAULT_VALUE;
        }

        private Source(String str) {
            this.mSource = str;
        }
    }

    public String getSequenceNum() {
        return this.seqNbr;
    }

    public void setSequenceNum(String str) {
        this.seqNbr = str;
    }

    public String getBillingAddr() {
        return this.billingAddr;
    }

    public void setBillingAddr(BillingInfo billingInfo) {
        String street1 = billingInfo.getStreet1();
        String street2 = billingInfo.getStreet2();
        String city = billingInfo.getCity();
        String state = billingInfo.getState();
        this.billingAddr = street1 != null ? street1 + " " : BuildConfig.FLAVOR;
        this.billingAddr += (street2 != null ? street2 + " " : BuildConfig.FLAVOR);
        this.billingAddr += (city != null ? city + " " : BuildConfig.FLAVOR);
        this.billingAddr += (state != null ? state + " " : BuildConfig.FLAVOR);
        if (this.billingAddr.isEmpty()) {
            this.billingAddr = null;
        }
    }

    public String getBillingZip() {
        return this.billingZip;
    }

    public void setBillingZip(String str) {
        this.billingZip = str;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(Source source) {
        this.source = source.getSourceString();
    }

    public String getPanNumber() {
        return this.pan;
    }

    public void setPanNumber(String str) {
        this.pan = str;
    }

    public String getCvvValue() {
        return this.cid;
    }

    public void setCvvValue(String str) {
        this.cid = str;
    }

    public String getName() {
        return this.cardHolderName;
    }

    public void setName(String str) {
        this.cardHolderName = str;
    }

    public String getCaptureMethod() {
        return this.captureMethod;
    }

    public void setCaptureMethod(String str) {
        this.captureMethod = str;
    }

    public static String generateBillingAddr(BillingInfo billingInfo) {
        String street1 = billingInfo.getStreet1();
        String street2 = billingInfo.getStreet2();
        String city = billingInfo.getCity();
        String state = billingInfo.getState();
        street1 = (((street1 != null ? street1 + " " : BuildConfig.FLAVOR) + (street2 != null ? street2 + " " : BuildConfig.FLAVOR)) + (city != null ? city + " " : BuildConfig.FLAVOR)) + (state != null ? state + " " : BuildConfig.FLAVOR);
        if (street1 == null || street1.isEmpty()) {
            return null;
        }
        if (street1.length() > MAXLEN_BILLING_ADDR) {
            return street1.substring(0, MAXLEN_BILLING_ADDR);
        }
        return street1;
    }

    public static String generateUserName(String str) {
        String str2 = null;
        if (str != null) {
            str2 = str.trim();
        }
        if (str2.length() > MAXLEN_USER_NAME) {
            return str2.substring(0, MAXLEN_USER_NAME);
        }
        return str2;
    }

    public static ClearCardInfo getEnrollmentPayload(EnrollCardPanInfo enrollCardPanInfo, BillingInfo billingInfo) {
        ClearCardInfo clearCardInfo = new ClearCardInfo();
        clearCardInfo.cid = null;
        clearCardInfo.pan = enrollCardPanInfo.getPAN();
        clearCardInfo.expDate = enrollCardPanInfo.getExpMonth() + enrollCardPanInfo.getExpYear();
        clearCardInfo.seqNbr = null;
        clearCardInfo.cardHolderName = generateUserName(enrollCardPanInfo.getName());
        clearCardInfo.billingAddr = generateBillingAddr(billingInfo);
        clearCardInfo.billingZip = billingInfo.getZip();
        clearCardInfo.source = Source.getDiscoverSourceValue(enrollCardPanInfo.getCardEntryMode());
        clearCardInfo.captureMethod = CaptureMethod.getDiscoverCaptureMethodValue(enrollCardPanInfo.getCardEntryMode());
        return clearCardInfo;
    }

    public static ClearCardInfo getProvisionPayload(EnrollCardPanInfo enrollCardPanInfo) {
        ClearCardInfo clearCardInfo = new ClearCardInfo();
        clearCardInfo.pan = null;
        clearCardInfo.expDate = null;
        clearCardInfo.seqNbr = null;
        clearCardInfo.cardHolderName = null;
        clearCardInfo.billingAddr = null;
        clearCardInfo.billingZip = null;
        clearCardInfo.cid = enrollCardPanInfo.getCVV();
        clearCardInfo.source = Source.getDiscoverSourceValue(enrollCardPanInfo.getCardEntryMode());
        clearCardInfo.captureMethod = CaptureMethod.getDiscoverCaptureMethodValue(enrollCardPanInfo.getCardEntryMode());
        return clearCardInfo;
    }
}
