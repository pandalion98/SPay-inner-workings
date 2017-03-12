package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import com.americanexpress.mobilepayments.hceclient.payments.nfc.EMVConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class McAidSelector {
    private static final String AID = "A0000000041010";
    private static final String AID_DELIMETER = "AA";
    private static String[] ALL_AIDS_COMMON_PIX_ARRAY = null;
    private static final String COMNA_DELIMETER = ",";
    private static final String DMC_MCC_AID = "A0000000041010";
    private static String[] DMC_MCC_PIX_ARRAY = null;
    private static String[] DMC_PLCC_COMMON_PIX_ARRAY = null;
    private static String[] KNOWN_PLCC_BIN = null;
    private static final String MSI_AID = "A0000000043060";
    private static String[] MSI_PIX_ARRAY = null;
    private static String[] MSI_PLCC_COMMON_PIX_ARRAY = null;
    private static final String PLCC_AID = "A0000000049100";
    private static String[] PLCC_PIX_ARRAY = null;
    private static final String PRIMARY_ACCEPTANCE_BRANCH = "1010";
    private static final String PRIMARY_ACCEPTANCE_BRANCH_DMC_MCC = "1010";
    private static final String PRIMARY_ACCEPTANCE_BRANCH_MSI = "3060";
    private static final String PRIMARY_ACCEPTANCE_BRANCH_PLV = "9100";
    private static final String RID = "A000000004";
    private static String TAG;
    private static List<String> mAllCommonFixedValues;
    private static List<String> mDmcPlccCommonFixedValues;
    private static List<String> mKnownPlccBinValues;
    private static List<String> mMcCreditCardPreFixedValues;
    private static List<String> mMsiFixedValues;
    private static List<String> mMsiPlccCommonFixedValues;
    private static List<String> mPlccFixedValues;
    private int PAN_INIT_INDEX;
    private int PAN_SECOND_INDEX;
    private int PAN_SIXTH_INDEX;
    private String mFirstSixDigitsofBin;
    private String mFirstTwoDigitsOfBin;
    private List<String> mSelectedAidLists;

    static {
        int i = 0;
        TAG = "McAidSelector";
        ALL_AIDS_COMMON_PIX_ARRAY = new String[]{"52", "53", "55"};
        DMC_MCC_PIX_ARRAY = new String[]{"51", "52", "53", "54", "55"};
        MSI_PIX_ARRAY = new String[]{HCEClientConstants.API_INDEX_TOKEN_UPDATE, "40", "43", Constants.TRACK2_EQUIVALENT_DATA, "64", "67", APDUConstants.READ_2_PREPEND_TAG, APDUConstants.SELECT_RESPONSE_DF_NAME, "92"};
        PLCC_PIX_ARRAY = new String[]{"71", APDUConstants.GPO_RESPONSE_TEMPLATE_WITH_CVM, "91", "97", "98"};
        MSI_PLCC_COMMON_PIX_ARRAY = new String[]{EMVConstants.APPLICATION_LABEL_TAG, "52", "53", "55", "56", "58", "60", EMVConstants.APPLICATION_TEMPLATE_TAG, "62", "63", APDUConstants.READ_2_PREPEND_TAG};
        DMC_PLCC_COMMON_PIX_ARRAY = new String[]{"51"};
        KNOWN_PLCC_BIN = new String[]{"639305"};
        mAllCommonFixedValues = new ArrayList();
        mMcCreditCardPreFixedValues = new ArrayList();
        mMsiFixedValues = new ArrayList();
        mPlccFixedValues = new ArrayList();
        mMsiPlccCommonFixedValues = new ArrayList();
        mDmcPlccCommonFixedValues = new ArrayList();
        mKnownPlccBinValues = new ArrayList();
        for (Object add : ALL_AIDS_COMMON_PIX_ARRAY) {
            mAllCommonFixedValues.add(add);
        }
        for (Object add2 : DMC_MCC_PIX_ARRAY) {
            mMcCreditCardPreFixedValues.add(add2);
        }
        for (Object add22 : MSI_PIX_ARRAY) {
            mMsiFixedValues.add(add22);
        }
        for (Object add222 : PLCC_PIX_ARRAY) {
            mPlccFixedValues.add(add222);
        }
        for (Object add2222 : MSI_PLCC_COMMON_PIX_ARRAY) {
            mMsiPlccCommonFixedValues.add(add2222);
        }
        for (Object add22222 : DMC_PLCC_COMMON_PIX_ARRAY) {
            mDmcPlccCommonFixedValues.add(add22222);
        }
        String[] strArr = KNOWN_PLCC_BIN;
        int length = strArr.length;
        while (i < length) {
            mKnownPlccBinValues.add(strArr[i]);
            i++;
        }
    }

    public McAidSelector(String str, boolean z) {
        this.PAN_INIT_INDEX = 0;
        this.PAN_SECOND_INDEX = 2;
        this.PAN_SIXTH_INDEX = 6;
        this.mSelectedAidLists = new ArrayList();
        if (z && str != null && str.length() > this.PAN_SIXTH_INDEX) {
            this.mFirstTwoDigitsOfBin = str.substring(this.PAN_INIT_INDEX, this.PAN_SECOND_INDEX);
            this.mFirstSixDigitsofBin = str.substring(this.PAN_INIT_INDEX, this.PAN_SIXTH_INDEX);
            if (mKnownPlccBinValues.contains(this.mFirstSixDigitsofBin) || mPlccFixedValues.contains(this.mFirstTwoDigitsOfBin)) {
                this.mSelectedAidLists.add(PLCC_AID);
                this.mSelectedAidLists.add(MSI_AID);
                this.mSelectedAidLists.add(DMC_MCC_AID);
            } else if (mMcCreditCardPreFixedValues.contains(this.mFirstTwoDigitsOfBin)) {
                this.mSelectedAidLists.add(DMC_MCC_AID);
                this.mSelectedAidLists.add(MSI_AID);
                this.mSelectedAidLists.add(PLCC_AID);
            } else {
                this.mSelectedAidLists.add(MSI_AID);
                this.mSelectedAidLists.add(PLCC_AID);
                this.mSelectedAidLists.add(DMC_MCC_AID);
            }
        } else if (z) {
            Log.m286e(TAG, "McAidSelector : Wrong PAN number");
        } else {
            this.mSelectedAidLists.add(DMC_MCC_AID);
            this.mSelectedAidLists.add(MSI_AID);
            this.mSelectedAidLists.add(PLCC_AID);
        }
    }

    public String generateAppletInstanceAids(long j) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.mSelectedAidLists.size(); i++) {
            stringBuilder.append(composeAppletInstanceAids((String) this.mSelectedAidLists.get(i), j));
            if (i != this.mSelectedAidLists.size() - 1) {
                stringBuilder.append(COMNA_DELIMETER);
            }
        }
        return stringBuilder.toString();
    }

    public String getAid() {
        if (this.mSelectedAidLists != null) {
            return (String) this.mSelectedAidLists.get(this.PAN_INIT_INDEX);
        }
        Log.m286e(TAG, "getAid : input PAN is null or empty");
        return null;
    }

    private String composeAppletInstanceAids(String str, long j) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.putLong(j);
        return str + AID_DELIMETER + CryptoUtils.convertbyteToHexString(allocate.array());
    }
}
