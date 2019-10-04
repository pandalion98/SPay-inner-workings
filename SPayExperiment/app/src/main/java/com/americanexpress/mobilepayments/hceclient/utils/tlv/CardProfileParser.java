/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardProfileParser {
    public static final String MULTI_BYTE_LENGTH = "FF";

    /*
     * Enabled aggressive block sorting
     */
    public TokenDataHolder parseTokenData(String string) {
        TokenDataHolder tokenDataHolder = new TokenDataHolder();
        Matcher matcher = Pattern.compile((String)"(#S[\\d]{4}#)").matcher((CharSequence)string);
        Matcher matcher2 = Pattern.compile((String)"(#E[\\d]{4}#)").matcher((CharSequence)string);
        Map<String, String> map = tokenDataHolder.getDgisMap();
        Map<TagKey, TagValue> map2 = tokenDataHolder.getTagsMap();
        List<String> list = CPDLConfig.getENC_DGIValues();
        while (matcher.find() && matcher2.find()) {
            String string2;
            String string3 = string.substring(7 + string.indexOf(matcher.group()), string.indexOf(matcher2.group()));
            String string4 = string3.substring(0, 4);
            if (!MULTI_BYTE_LENGTH.equalsIgnoreCase(string3.substring(4, 6))) {
                string2 = string3.substring(6);
                map.put((Object)string4, (Object)string2);
            } else {
                string2 = string3.substring(10);
                map.put((Object)string4, (Object)string2);
            }
            if (list.contains((Object)string4)) continue;
            map2.putAll(TLVParser.tagListToTagMap(TLVParser.parseTLV(HexUtils.hexStringToByteArray(string2), string4, true), false));
        }
        return tokenDataHolder;
    }
}

