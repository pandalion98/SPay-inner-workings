/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
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

    /*
     * Enabled aggressive block sorting
     */
    public static String generateBillingAddr(BillingInfo billingInfo) {
        String string = billingInfo.getStreet1();
        String string2 = billingInfo.getStreet2();
        String string3 = billingInfo.getCity();
        String string4 = billingInfo.getState();
        String string5 = string != null ? string + " " : "";
        StringBuilder stringBuilder = new StringBuilder().append(string5);
        String string6 = string2 != null ? string2 + " " : "";
        String string7 = stringBuilder.append(string6).toString();
        StringBuilder stringBuilder2 = new StringBuilder().append(string7);
        String string8 = string3 != null ? string3 + " " : "";
        String string9 = stringBuilder2.append(string8).toString();
        StringBuilder stringBuilder3 = new StringBuilder().append(string9);
        String string10 = string4 != null ? string4 + " " : "";
        String string11 = stringBuilder3.append(string10).toString();
        if (string11 == null) return null;
        if (string11.isEmpty()) {
            return null;
        }
        if (string11.length() <= 128) return string11;
        return string11.substring(0, 128);
    }

    public static String generateUserName(String string) {
        String string2 = null;
        if (string != null) {
            string2 = string.trim();
        }
        if (string2.length() > 64) {
            string2 = string2.substring(0, 64);
        }
        return string2;
    }

    public static ClearCardInfo getEnrollmentPayload(EnrollCardPanInfo enrollCardPanInfo, BillingInfo billingInfo) {
        ClearCardInfo clearCardInfo = new ClearCardInfo();
        clearCardInfo.cid = null;
        clearCardInfo.pan = enrollCardPanInfo.getPAN();
        clearCardInfo.expDate = enrollCardPanInfo.getExpMonth() + enrollCardPanInfo.getExpYear();
        clearCardInfo.seqNbr = null;
        clearCardInfo.cardHolderName = ClearCardInfo.generateUserName(enrollCardPanInfo.getName());
        clearCardInfo.billingAddr = ClearCardInfo.generateBillingAddr(billingInfo);
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

    public String getBillingAddr() {
        return this.billingAddr;
    }

    public String getBillingZip() {
        return this.billingZip;
    }

    public String getCaptureMethod() {
        return this.captureMethod;
    }

    public String getCvvValue() {
        return this.cid;
    }

    public String getName() {
        return this.cardHolderName;
    }

    public String getPanNumber() {
        return this.pan;
    }

    public String getSequenceNum() {
        return this.seqNbr;
    }

    public String getSource() {
        return this.source;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setBillingAddr(BillingInfo billingInfo) {
        String string = billingInfo.getStreet1();
        String string2 = billingInfo.getStreet2();
        String string3 = billingInfo.getCity();
        String string4 = billingInfo.getState();
        String string5 = string != null ? string + " " : "";
        this.billingAddr = string5;
        StringBuilder stringBuilder = new StringBuilder().append(this.billingAddr);
        String string6 = string2 != null ? string2 + " " : "";
        this.billingAddr = stringBuilder.append(string6).toString();
        StringBuilder stringBuilder2 = new StringBuilder().append(this.billingAddr);
        String string7 = string3 != null ? string3 + " " : "";
        this.billingAddr = stringBuilder2.append(string7).toString();
        StringBuilder stringBuilder3 = new StringBuilder().append(this.billingAddr);
        String string8 = string4 != null ? string4 + " " : "";
        this.billingAddr = stringBuilder3.append(string8).toString();
        if (this.billingAddr.isEmpty()) {
            this.billingAddr = null;
        }
    }

    public void setBillingZip(String string) {
        this.billingZip = string;
    }

    public void setCaptureMethod(String string) {
        this.captureMethod = string;
    }

    public void setCvvValue(String string) {
        this.cid = string;
    }

    public void setName(String string) {
        this.cardHolderName = string;
    }

    public void setPanNumber(String string) {
        this.pan = string;
    }

    public void setSequenceNum(String string) {
        this.seqNbr = string;
    }

    public void setSource(Source source) {
        this.source = source.getSourceString();
    }

    public static final class CaptureMethod
    extends Enum<CaptureMethod> {
        private static final /* synthetic */ CaptureMethod[] $VALUES;
        public static final /* enum */ CaptureMethod CAMERA = new CaptureMethod("1");
        private static final String CAPTUREMETHOD_DEFAULT_VALUE = "2";
        public static final /* enum */ CaptureMethod MANUAL = new CaptureMethod("2");
        private static final Map<String, String> mCaptureMethodMapper;
        private String mCaptureMethod;

        static {
            CaptureMethod[] arrcaptureMethod = new CaptureMethod[]{CAMERA, MANUAL};
            $VALUES = arrcaptureMethod;
            mCaptureMethodMapper = new HashMap();
            mCaptureMethodMapper.put((Object)"MAN", (Object)CAPTUREMETHOD_DEFAULT_VALUE);
            mCaptureMethodMapper.put((Object)"OCR", (Object)"1");
        }

        private CaptureMethod(String string2) {
            this.mCaptureMethod = string2;
        }

        public static String getDiscoverCaptureMethodValue(String string) {
            String string2 = (String)mCaptureMethodMapper.get((Object)string);
            if (string2 != null) {
                return string2;
            }
            return CAPTUREMETHOD_DEFAULT_VALUE;
        }

        public static CaptureMethod valueOf(String string) {
            return (CaptureMethod)Enum.valueOf(CaptureMethod.class, (String)string);
        }

        public static CaptureMethod[] values() {
            return (CaptureMethod[])$VALUES.clone();
        }

        public String getCaptureMethodString() {
            return this.mCaptureMethod;
        }
    }

    public static final class Source
    extends Enum<Source> {
        private static final /* synthetic */ Source[] $VALUES;
        public static final /* enum */ Source ADD_DEVICE = new Source("add-device");
        public static final /* enum */ Source IN_APP = new Source("in-app");
        public static final /* enum */ Source ON_FILE = new Source("on-file");
        public static final /* enum */ Source RESTORE = new Source("restore");
        private static String SOURCE_DEFAULT_VALUE;
        public static final /* enum */ Source USER_INPUT;
        private static final Map<String, String> mSourceMapper;
        private String mSource;

        static {
            USER_INPUT = new Source("user-input");
            Source[] arrsource = new Source[]{ADD_DEVICE, IN_APP, ON_FILE, RESTORE, USER_INPUT};
            $VALUES = arrsource;
            mSourceMapper = new HashMap();
            SOURCE_DEFAULT_VALUE = "user-input";
            mSourceMapper.put((Object)"FILE", (Object)"on-file");
            mSourceMapper.put((Object)"APP", (Object)"in-app");
            mSourceMapper.put((Object)"MAN", (Object)"user-input");
            mSourceMapper.put((Object)"OCR", (Object)"user-input");
        }

        private Source(String string2) {
            this.mSource = string2;
        }

        public static String getDiscoverSourceValue(String string) {
            String string2 = (String)mSourceMapper.get((Object)string);
            if (string2 != null) {
                return string2;
            }
            return SOURCE_DEFAULT_VALUE;
        }

        public static Source valueOf(String string) {
            return (Source)Enum.valueOf(Source.class, (String)string);
        }

        public static Source[] values() {
            return (Source[])$VALUES.clone();
        }

        public String getSourceString() {
            return this.mSource;
        }
    }

}

