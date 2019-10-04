/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.americanexpress.sdkmodulelib.util;

import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.tlv.BERTLV;
import com.americanexpress.sdkmodulelib.tlv.TLVUtil;
import com.americanexpress.sdkmodulelib.tlv.Tag;
import com.americanexpress.sdkmodulelib.tlv.TagValueType;
import com.americanexpress.sdkmodulelib.tlv.Util;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class TLVHelper {
    public static String buildTLVForValue(String string, String string2) {
        String string3 = Util.int2Hex(string2.length() / 2).toUpperCase();
        return string + string3 + string2;
    }

    public TagInfo getFirstTag(String string) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Util.fromHexString(string));
        int n2 = byteArrayInputStream.available();
        TagInfo tagInfo = null;
        if (n2 > 0) {
            BERTLV bERTLV = TLVUtil.getNextTLV(byteArrayInputStream);
            String string2 = Util.byteArrayToHexString(bERTLV.getTagBytes());
            byte[] arrby = bERTLV.getRawEncodedLengthBytes();
            int n3 = bERTLV.getLength();
            String string3 = Util.byteArrayToHexString(arrby);
            String string4 = Util.byteArrayToHexString(bERTLV.getValueBytes());
            tagInfo = new TagInfo();
            tagInfo.setTagName(string2.toUpperCase());
            tagInfo.setTagValue(string4.toUpperCase());
            tagInfo.setTagLength(n3);
            tagInfo.setRawEncodedLength(string3.toUpperCase());
        }
        return tagInfo;
    }

    public List<TagInfo> parseTLV(String string) {
        return this.parseTLV(Util.fromHexString(string), null);
    }

    public List<TagInfo> parseTLV(byte[] arrby, List<TagInfo> arrayList) {
        if (arrayList == null) {
            arrayList = new ArrayList();
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        while (byteArrayInputStream.available() > 0) {
            BERTLV bERTLV = TLVUtil.getNextTLV(byteArrayInputStream);
            String string = Util.byteArrayToHexString(bERTLV.getTagBytes());
            byte[] arrby2 = bERTLV.getRawEncodedLengthBytes();
            int n2 = bERTLV.getLength();
            String string2 = Util.byteArrayToHexString(arrby2);
            byte[] arrby3 = bERTLV.getValueBytes();
            String string3 = Util.byteArrayToHexString(arrby3);
            TagInfo tagInfo = new TagInfo();
            tagInfo.setTagName(string.toUpperCase());
            tagInfo.setTagValue(string3.toUpperCase());
            tagInfo.setTagLength(n2);
            tagInfo.setRawEncodedLength(string2.toUpperCase());
            Tag tag = bERTLV.getTag();
            if (tag.isConstructed()) {
                tagInfo.setTagValue(null);
                arrayList.add((Object)tagInfo);
                this.parseTLV(arrby3, (List<TagInfo>)arrayList);
                continue;
            }
            if (tag.getTagValueType() == TagValueType.DOL) {
                arrayList.add((Object)tagInfo);
                continue;
            }
            arrayList.add((Object)tagInfo);
        }
        return arrayList;
    }
}

