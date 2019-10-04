/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVException;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import java.util.ArrayList;
import java.util.List;

public class DOLParser {
    public static final String DOL_TOTAL_LEN = "DOL_TOTAL_LEN";

    public static short computeDOL(String string, List<DOLTag> list) {
        int n2 = 0;
        do {
            block6 : {
                block5 : {
                    try {
                        if (n2 >= list.size()) break block5;
                        DOLTag dOLTag = (DOLTag)list.get(n2);
                        short s2 = dOLTag.getTagLength();
                        short s3 = dOLTag.getTagOffset();
                        if (dOLTag.getTagName().compareToIgnoreCase(DOL_TOTAL_LEN) != 0) {
                            String string2 = HexUtils.nBytesFromHexString(string, s3, s2);
                            ApplicationInfoManager.setApplcationInfoValue(dOLTag.getTagName(), string2);
                        } else {
                            ApplicationInfoManager.setApplcationInfoValue(dOLTag.getTagName(), s2);
                        }
                        break block6;
                    }
                    catch (Exception exception) {
                        Log.e((String)"core-hceclient", (String)("Failed to compute DOL" + (Object)((Object)exception)));
                        return Constants.MAGIC_FALSE;
                    }
                }
                return Constants.MAGIC_TRUE;
            }
            ++n2;
        } while (true);
    }

    public static List<DOLTag> parseDOL(String string) {
        DOLTag dOLTag;
        ArrayList arrayList = new ArrayList();
        byte[] arrby = Utility.fromHexString(string);
        short s2 = 0;
        for (int i2 = 0; i2 < arrby.length; i2 += dOLTag.getSkipLen()) {
            dOLTag = TLVParser.parseTLV(arrby, i2, arrby.length, false);
            if (dOLTag == null) {
                throw new TLVException("::TLVHelper::parseTLV::Malformed Record Data");
            }
            dOLTag.setTagOffset(s2);
            arrayList.add((Object)dOLTag);
            s2 = (short)(s2 + dOLTag.getTagLength());
        }
        DOLTag dOLTag2 = new DOLTag();
        dOLTag2.setTagName(DOL_TOTAL_LEN);
        dOLTag2.setTagLength(s2);
        arrayList.add((Object)dOLTag2);
        return arrayList;
    }
}

