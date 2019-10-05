/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.nio.ByteBuffer
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class McAidSelector {
    private static final String AID = "A0000000041010";
    private static final String AID_DELIMETER = "AA";
    private static String[] ALL_AIDS_COMMON_PIX_ARRAY;
    private static final String COMNA_DELIMETER = ",";
    private static final String DMC_MCC_AID = "A0000000041010";
    private static String[] DMC_MCC_PIX_ARRAY;
    private static String[] DMC_PLCC_COMMON_PIX_ARRAY;
    private static String[] KNOWN_PLCC_BIN;
    private static final String MSI_AID = "A0000000043060";
    private static String[] MSI_PIX_ARRAY;
    private static String[] MSI_PLCC_COMMON_PIX_ARRAY;
    private static final String PLCC_AID = "A0000000049100";
    private static String[] PLCC_PIX_ARRAY;
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
    private int PAN_INIT_INDEX = 0;
    private int PAN_SECOND_INDEX = 2;
    private int PAN_SIXTH_INDEX = 6;
    private String mFirstSixDigitsofBin;
    private String mFirstTwoDigitsOfBin;
    private List<String> mSelectedAidLists = new ArrayList();

    static {
        int n2 = 0;
        TAG = "McAidSelector";
        ALL_AIDS_COMMON_PIX_ARRAY = new String[]{"52", "53", "55"};
        DMC_MCC_PIX_ARRAY = new String[]{"51", "52", "53", "54", "55"};
        MSI_PIX_ARRAY = new String[]{"04", "40", "43", "57", "64", "67", "70", "84", "92"};
        PLCC_PIX_ARRAY = new String[]{"71", "77", "91", "97", "98"};
        MSI_PLCC_COMMON_PIX_ARRAY = new String[]{"50", "52", "53", "55", "56", "58", "60", "61", "62", "63", "70"};
        DMC_PLCC_COMMON_PIX_ARRAY = new String[]{"51"};
        KNOWN_PLCC_BIN = new String[]{"639305"};
        mAllCommonFixedValues = new ArrayList();
        mMcCreditCardPreFixedValues = new ArrayList();
        mMsiFixedValues = new ArrayList();
        mPlccFixedValues = new ArrayList();
        mMsiPlccCommonFixedValues = new ArrayList();
        mDmcPlccCommonFixedValues = new ArrayList();
        mKnownPlccBinValues = new ArrayList();
        for (String string : ALL_AIDS_COMMON_PIX_ARRAY) {
            mAllCommonFixedValues.add((Object)string);
        }
        for (String string : DMC_MCC_PIX_ARRAY) {
            mMcCreditCardPreFixedValues.add((Object)string);
        }
        for (String string : MSI_PIX_ARRAY) {
            mMsiFixedValues.add((Object)string);
        }
        for (String string : PLCC_PIX_ARRAY) {
            mPlccFixedValues.add((Object)string);
        }
        for (String string : MSI_PLCC_COMMON_PIX_ARRAY) {
            mMsiPlccCommonFixedValues.add((Object)string);
        }
        for (String string : DMC_PLCC_COMMON_PIX_ARRAY) {
            mDmcPlccCommonFixedValues.add((Object)string);
        }
        String[] arrstring = KNOWN_PLCC_BIN;
        int n3 = arrstring.length;
        while (n2 < n3) {
            String string = arrstring[n2];
            mKnownPlccBinValues.add((Object)string);
            ++n2;
        }
    }

    public McAidSelector(String string, boolean bl) {
        if (bl && string != null && string.length() > this.PAN_SIXTH_INDEX) {
            this.mFirstTwoDigitsOfBin = string.substring(this.PAN_INIT_INDEX, this.PAN_SECOND_INDEX);
            this.mFirstSixDigitsofBin = string.substring(this.PAN_INIT_INDEX, this.PAN_SIXTH_INDEX);
            if (mKnownPlccBinValues.contains((Object)this.mFirstSixDigitsofBin) || mPlccFixedValues.contains((Object)this.mFirstTwoDigitsOfBin)) {
                this.mSelectedAidLists.add((Object)PLCC_AID);
                this.mSelectedAidLists.add((Object)MSI_AID);
                this.mSelectedAidLists.add((Object)"A0000000041010");
                return;
            }
            if (mMcCreditCardPreFixedValues.contains((Object)this.mFirstTwoDigitsOfBin)) {
                this.mSelectedAidLists.add((Object)"A0000000041010");
                this.mSelectedAidLists.add((Object)MSI_AID);
                this.mSelectedAidLists.add((Object)PLCC_AID);
                return;
            }
            this.mSelectedAidLists.add((Object)MSI_AID);
            this.mSelectedAidLists.add((Object)PLCC_AID);
            this.mSelectedAidLists.add((Object)"A0000000041010");
            return;
        }
        if (!bl) {
            this.mSelectedAidLists.add((Object)"A0000000041010");
            this.mSelectedAidLists.add((Object)MSI_AID);
            this.mSelectedAidLists.add((Object)PLCC_AID);
            return;
        }
        Log.e(TAG, "McAidSelector : Wrong PAN number");
    }

    private String composeAppletInstanceAids(String string, long l2) {
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)8);
        byteBuffer.putLong(l2);
        byte[] arrby = byteBuffer.array();
        return string + AID_DELIMETER + CryptoUtils.convertbyteToHexString(arrby);
    }

    public String generateAppletInstanceAids(long l2) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < this.mSelectedAidLists.size(); ++i2) {
            stringBuilder.append(this.composeAppletInstanceAids((String)this.mSelectedAidLists.get(i2), l2));
            if (i2 == -1 + this.mSelectedAidLists.size()) continue;
            stringBuilder.append(COMNA_DELIMETER);
        }
        return stringBuilder.toString();
    }

    public String getAid() {
        if (this.mSelectedAidLists == null) {
            Log.e(TAG, "getAid : input PAN is null or empty");
            return null;
        }
        return (String)this.mSelectedAidLists.get(this.PAN_INIT_INDEX);
    }
}

