package com.americanexpress.mobilepayments.hceclient.utils.common;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVException;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import java.util.ArrayList;
import java.util.List;

public class DOLParser {
    public static final String DOL_TOTAL_LEN = "DOL_TOTAL_LEN";

    public static List<DOLTag> parseDOL(String str) {
        List<DOLTag> arrayList = new ArrayList();
        byte[] fromHexString = Utility.fromHexString(str);
        short s = (short) 0;
        int i = 0;
        while (i < fromHexString.length) {
            DOLTag parseTLV = TLVParser.parseTLV(fromHexString, i, fromHexString.length, false);
            if (parseTLV == null) {
                throw new TLVException("::TLVHelper::parseTLV::Malformed Record Data");
            }
            parseTLV.setTagOffset(s);
            arrayList.add(parseTLV);
            i += parseTLV.getSkipLen();
            s = (short) (s + parseTLV.getTagLength());
        }
        DOLTag dOLTag = new DOLTag();
        dOLTag.setTagName(DOL_TOTAL_LEN);
        dOLTag.setTagLength(s);
        arrayList.add(dOLTag);
        return arrayList;
    }

    public static short computeDOL(String str, List<DOLTag> list) {
        int i = 0;
        while (i < list.size()) {
            try {
                DOLTag dOLTag = (DOLTag) list.get(i);
                short tagLength = dOLTag.getTagLength();
                short tagOffset = dOLTag.getTagOffset();
                if (dOLTag.getTagName().compareToIgnoreCase(DOL_TOTAL_LEN) != 0) {
                    ApplicationInfoManager.setApplcationInfoValue(dOLTag.getTagName(), HexUtils.nBytesFromHexString(str, tagOffset, tagLength));
                } else {
                    ApplicationInfoManager.setApplcationInfoValue(dOLTag.getTagName(), Short.valueOf(tagLength));
                }
                i++;
            } catch (Exception e) {
                Log.e(HCEClientConstants.TAG, "Failed to compute DOL" + e);
                return Constants.MAGIC_FALSE;
            }
        }
        return Constants.MAGIC_TRUE;
    }
}
