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
    public TagInfo getFirstTag(String str) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Util.fromHexString(str));
        if (byteArrayInputStream.available() <= 0) {
            return null;
        }
        BERTLV nextTLV = TLVUtil.getNextTLV(byteArrayInputStream);
        String byteArrayToHexString = Util.byteArrayToHexString(nextTLV.getTagBytes());
        byte[] rawEncodedLengthBytes = nextTLV.getRawEncodedLengthBytes();
        int length = nextTLV.getLength();
        String byteArrayToHexString2 = Util.byteArrayToHexString(rawEncodedLengthBytes);
        String byteArrayToHexString3 = Util.byteArrayToHexString(nextTLV.getValueBytes());
        TagInfo tagInfo = new TagInfo();
        tagInfo.setTagName(byteArrayToHexString.toUpperCase());
        tagInfo.setTagValue(byteArrayToHexString3.toUpperCase());
        tagInfo.setTagLength(length);
        tagInfo.setRawEncodedLength(byteArrayToHexString2.toUpperCase());
        return tagInfo;
    }

    public List<TagInfo> parseTLV(byte[] bArr, List<TagInfo> list) {
        if (list == null) {
            list = new ArrayList();
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        while (byteArrayInputStream.available() > 0) {
            BERTLV nextTLV = TLVUtil.getNextTLV(byteArrayInputStream);
            String byteArrayToHexString = Util.byteArrayToHexString(nextTLV.getTagBytes());
            byte[] rawEncodedLengthBytes = nextTLV.getRawEncodedLengthBytes();
            int length = nextTLV.getLength();
            String byteArrayToHexString2 = Util.byteArrayToHexString(rawEncodedLengthBytes);
            byte[] valueBytes = nextTLV.getValueBytes();
            String byteArrayToHexString3 = Util.byteArrayToHexString(valueBytes);
            TagInfo tagInfo = new TagInfo();
            tagInfo.setTagName(byteArrayToHexString.toUpperCase());
            tagInfo.setTagValue(byteArrayToHexString3.toUpperCase());
            tagInfo.setTagLength(length);
            tagInfo.setRawEncodedLength(byteArrayToHexString2.toUpperCase());
            Tag tag = nextTLV.getTag();
            if (tag.isConstructed()) {
                tagInfo.setTagValue(null);
                list.add(tagInfo);
                parseTLV(valueBytes, list);
            } else if (tag.getTagValueType() == TagValueType.DOL) {
                list.add(tagInfo);
            } else {
                list.add(tagInfo);
            }
        }
        return list;
    }

    public List<TagInfo> parseTLV(String str) {
        return parseTLV(Util.fromHexString(str), null);
    }

    public static String buildTLVForValue(String str, String str2) {
        return str + Util.int2Hex(str2.length() / 2).toUpperCase() + str2;
    }
}
