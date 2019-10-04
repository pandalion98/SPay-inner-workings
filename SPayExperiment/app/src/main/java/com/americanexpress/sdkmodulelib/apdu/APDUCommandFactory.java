/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Character
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.HashMap
 */
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
import java.util.Arrays;
import java.util.HashMap;

public class APDUCommandFactory
implements APDUConstants {
    public static final HashMap<String, Class> commandParserMapper = new HashMap();

    static {
        commandParserMapper.put((Object)"00A4", SelectApduInfo.class);
        commandParserMapper.put((Object)"80A8", GetProcessingOptionsApduInfo.class);
        commandParserMapper.put((Object)"00B2", ReadApduInfo.class);
        commandParserMapper.put((Object)"80CA", GetDataApduInfo.class);
        commandParserMapper.put((Object)"80AE", GenerateAppCryptoApduInfo.class);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public static APDU createAPDU(String var0) {
        var1_1 = var0.getBytes();
        if (var1_1.length < 8) {
            throw new APDUParseException("Failed to Parse: invalid command: " + var0);
        }
        var2_2 = Character.toString((char)((char)var1_1[0])) + Character.toString((char)((char)var1_1[1]));
        var3_3 = Character.toString((char)((char)var1_1[2])) + Character.toString((char)((char)var1_1[3]));
        var4_4 = var2_2 + var3_3;
        var5_5 = Character.toString((char)((char)var1_1[4])) + Character.toString((char)((char)var1_1[5]));
        var6_6 = Character.toString((char)((char)var1_1[6])) + Character.toString((char)((char)var1_1[7]));
        if (APDUCommandFactory.commandParserMapper.containsKey((Object)var4_4)) {
            block8 : {
                if ("00a404000e325041592e5359532e444446303100".equalsIgnoreCase(var0)) {
                    var7_7 = new SelectPPSEApduInfo();
                    var7_7.setStatusWord(var7_7.validate());
                    return var7_7;
                }
                var8_8 = (APDU)((Class)APDUCommandFactory.commandParserMapper.get((Object)var4_4)).newInstance();
                var8_8.setApduClass(var2_2);
                var8_8.setInstruction(var3_3);
                var8_8.setApduClassAndInstruction(var4_4);
                var8_8.setParameter1(var5_5);
                var8_8.setParameter2(var6_6);
                var8_8.setLengthExpectedData(var0.substring(-2 + var0.length(), var0.length()));
                if (-8 + var1_1.length <= 2) break block8;
                var8_8.setLengthCommandData(Character.toString((char)((char)var1_1[8])) + Character.toString((char)((char)var1_1[9])));
                var10_9 = 2 * var8_8.getLengthCommandDataDecimal();
                var8_8.setDataLength(var10_9);
                if (var10_9 == 0) break block8;
                var11_10 = var10_9 + 10;
                var8_8.setData(var0.substring(10, var11_10));
            }
lbl31: // 2 sources:
            do {
                var8_8.setStatusWord(var8_8.validate());
                return var8_8;
                break;
            } while (true);
            catch (Exception var9_11) {
                throw new APDUParseException("Failed to Parse: invalid command: " + var4_4);
            }
        }
        return APDUCommandFactory.generateInvalidClassInsApdu(var2_2, var3_3);
        catch (Exception var12_12) {
            ** continue;
        }
    }

    private static APDU generateInvalidClassInsApdu(final String string, final String string2) {
        APDU aPDU = new APDU(){

            @Override
            public APDUResponse buildResponse() {
                return null;
            }

            @Override
            public String validate() {
                if (!Arrays.asList((Object[])Constants.APDU_CLASSES).contains((Object)string)) {
                    return "6E00";
                }
                if (!Arrays.asList((Object[])Constants.APDU_INSTRUCTIONS).contains((Object)string2)) {
                    return "6D00";
                }
                if (!Arrays.asList((Object[])Constants.APDU_CLASSES_INSTRUCTIONS).contains((Object)(string + "" + string2))) {
                    return "6E00";
                }
                return "6985";
            }
        };
        aPDU.setStatusWord(aPDU.validate());
        aPDU.setValid(false);
        return aPDU;
    }

}

