package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardProfileParser {
    public static final String MULTI_BYTE_LENGTH = "FF";

    public TokenDataHolder parseTokenData(String str) {
        TokenDataHolder tokenDataHolder = new TokenDataHolder();
        Matcher matcher = Pattern.compile("(#S[\\d]{4}#)").matcher(str);
        Matcher matcher2 = Pattern.compile("(#E[\\d]{4}#)").matcher(str);
        Map dgisMap = tokenDataHolder.getDgisMap();
        Map tagsMap = tokenDataHolder.getTagsMap();
        List eNC_DGIValues = CPDLConfig.getENC_DGIValues();
        while (matcher.find() && matcher2.find()) {
            String substring = str.substring(str.indexOf(matcher.group()) + 7, str.indexOf(matcher2.group()));
            String substring2 = substring.substring(0, 4);
            if (MULTI_BYTE_LENGTH.equalsIgnoreCase(substring.substring(4, 6))) {
                substring = substring.substring(10);
                dgisMap.put(substring2, substring);
            } else {
                substring = substring.substring(6);
                dgisMap.put(substring2, substring);
            }
            if (!eNC_DGIValues.contains(substring2)) {
                tagsMap.putAll(TLVParser.tagListToTagMap(TLVParser.parseTLV(HexUtils.hexStringToByteArray(substring), substring2, true), false));
            }
        }
        return tokenDataHolder;
    }
}
