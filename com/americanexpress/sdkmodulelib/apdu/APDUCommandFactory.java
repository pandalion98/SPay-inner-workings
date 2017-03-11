package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.exception.APDUParseException;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.GenerateAppCryptoApduInfo;
import com.americanexpress.sdkmodulelib.model.apdu.GetDataApduInfo;
import com.americanexpress.sdkmodulelib.model.apdu.GetProcessingOptionsApduInfo;
import com.americanexpress.sdkmodulelib.model.apdu.ReadApduInfo;
import com.americanexpress.sdkmodulelib.model.apdu.SelectApduInfo;
import com.americanexpress.sdkmodulelib.model.apdu.SelectPPSEApduInfo;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Arrays;
import java.util.HashMap;

public class APDUCommandFactory implements APDUConstants {
    public static final HashMap<String, Class> commandParserMapper;

    /* renamed from: com.americanexpress.sdkmodulelib.apdu.APDUCommandFactory.1 */
    static class C00721 extends APDU {
        final /* synthetic */ String val$apduClass;
        final /* synthetic */ String val$instruction;

        C00721(String str, String str2) {
            this.val$apduClass = str;
            this.val$instruction = str2;
        }

        public String validate() {
            if (!Arrays.asList(Constants.APDU_CLASSES).contains(this.val$apduClass)) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_CLA_NOT_SUPPORTED;
            }
            if (!Arrays.asList(Constants.APDU_INSTRUCTIONS).contains(this.val$instruction)) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_INS_NOT_SUPPORTED;
            }
            if (Arrays.asList(Constants.APDU_CLASSES_INSTRUCTIONS).contains(this.val$apduClass + BuildConfig.FLAVOR + this.val$instruction)) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE;
            }
            return APDUConstants.APDU_COMMAND_STATUS_WORD_CLA_NOT_SUPPORTED;
        }

        public APDUResponse buildResponse() {
            return null;
        }
    }

    static {
        commandParserMapper = new HashMap();
        commandParserMapper.put(APDUConstants.COMMAND_INSTRUCTION_SELECT_ADPU_INFO, SelectApduInfo.class);
        commandParserMapper.put(APDUConstants.COMMAND_INSTRUCTION_GET_PROCESSING_OPTIONS, GetProcessingOptionsApduInfo.class);
        commandParserMapper.put(APDUConstants.COMMAND_INSTRUCTION_READ_ADPU_INFO, ReadApduInfo.class);
        commandParserMapper.put(APDUConstants.COMMAND_INSTRUCTION_GET_DATA_ADPU_INFO, GetDataApduInfo.class);
        commandParserMapper.put(APDUConstants.COMMAND_INSTRUCTION_GENERATE_APP_CRYPTO_ADPU_INFO, GenerateAppCryptoApduInfo.class);
    }

    public static APDU createAPDU(String str) {
        byte[] bytes = str.getBytes();
        if (bytes.length < 8) {
            throw new APDUParseException("Failed to Parse: invalid command: " + str);
        }
        String str2 = Character.toString((char) bytes[0]) + Character.toString((char) bytes[1]);
        String str3 = Character.toString((char) bytes[2]) + Character.toString((char) bytes[3]);
        String str4 = str2 + str3;
        String str5 = Character.toString((char) bytes[4]) + Character.toString((char) bytes[5]);
        String str6 = Character.toString((char) bytes[6]) + Character.toString((char) bytes[7]);
        if (!commandParserMapper.containsKey(str4)) {
            return generateInvalidClassInsApdu(str2, str3);
        }
        if (APDUConstants.COMMAND_INSTRUCTION_SELECT_PPSE_ADPU_INFO.equalsIgnoreCase(str)) {
            APDU selectPPSEApduInfo = new SelectPPSEApduInfo();
            selectPPSEApduInfo.setStatusWord(selectPPSEApduInfo.validate());
            return selectPPSEApduInfo;
        }
        selectPPSEApduInfo = (APDU) ((Class) commandParserMapper.get(str4)).newInstance();
        selectPPSEApduInfo.setApduClass(str2);
        selectPPSEApduInfo.setInstruction(str3);
        selectPPSEApduInfo.setApduClassAndInstruction(str4);
        selectPPSEApduInfo.setParameter1(str5);
        selectPPSEApduInfo.setParameter2(str6);
        selectPPSEApduInfo.setLengthExpectedData(str.substring(str.length() - 2, str.length()));
        try {
            if (bytes.length - 8 > 2) {
                selectPPSEApduInfo.setLengthCommandData(Character.toString((char) bytes[8]) + Character.toString((char) bytes[9]));
                int lengthCommandDataDecimal = selectPPSEApduInfo.getLengthCommandDataDecimal() * 2;
                selectPPSEApduInfo.setDataLength(lengthCommandDataDecimal);
                if (lengthCommandDataDecimal != 0) {
                    try {
                        selectPPSEApduInfo.setData(str.substring(10, lengthCommandDataDecimal + 10));
                    } catch (Exception e) {
                    }
                }
            }
            selectPPSEApduInfo.setStatusWord(selectPPSEApduInfo.validate());
            return selectPPSEApduInfo;
        } catch (Exception e2) {
            throw new APDUParseException("Failed to Parse: invalid command: " + str4);
        }
    }

    private static APDU generateInvalidClassInsApdu(String str, String str2) {
        APDU c00721 = new C00721(str, str2);
        c00721.setStatusWord(c00721.validate());
        c00721.setValid(false);
        return c00721;
    }
}
