package com.americanexpress.mobilepayments.hceclient.payments.nfc.process;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.ExtensionType;

public class ReadApplicationDataProcess extends CommandProcess {
    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        Object obj = null;
        TokenAPDUResponse tokenAPDUResponse = new TokenAPDUResponse();
        try {
            byte b = (byte) (commandAPDU.getbP2() >> 3);
            byte b2 = commandAPDU.getbP1();
            String str = BuildConfig.FLAVOR;
            if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException((short) ISO7816.SW_COMMAND_NOT_ALLOWED);
            } else if (StateMode.TERMINATE == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() != ApplicationInfoManager.STATE_APP_SELECTED && ((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() != ApplicationInfoManager.STATE_APP_INITIATED && ((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() != ApplicationInfoManager.STATE_APP_COMPLETED) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if ((commandAPDU.getbP2() & 7) != 4 || commandAPDU.getbP1() == null || ((short) (((short) (commandAPDU.getbP2() & 248)) >> 3)) > (short) 30) {
                throw new HCEClientException((short) ISO7816.SW_INCORRECT_P1P2);
            } else if (commandAPDU.getbCommandCase() != CommandAPDU.CASE_2) {
                throw new HCEClientException((short) ISO7816.SW_WRONG_LENGTH);
            } else {
                Object dgi_tag;
                if (b == (byte) 1 && b2 == (byte) 1) {
                    if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE)).byteValue() == 83) {
                        dgi_tag = CPDLConfig.getDGI_TAG(CPDLConfig.SFI1_REC1_DGI_EMV);
                    } else {
                        dgi_tag = CPDLConfig.getDGI_TAG(CPDLConfig.SFI1_REC1_DGI_MS);
                        int i = 1;
                    }
                } else if (Constants.MAGIC_FALSE == checkAFL(b, b2, tokenAPDUResponse)) {
                    throw new HCEClientException(tokenAPDUResponse.getsSW());
                } else {
                    dgi_tag = Utility.byte2Hex(b) + Utility.byte2Hex(b2);
                }
                str = (String) this.dataContext.getDgiMap().get(dgi_tag);
                if (obj != null && PaymentUtils.checkXPMConfig((byte) 4, (byte) 1) == Constants.MAGIC_TRUE && -102 == ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue()) {
                    int parseInt = Integer.parseInt(TagsMapUtil.getTagValue(HCEClientConstants.SERVICE_CODE_MS_OFFSET), 10);
                    String str2 = "721";
                    switch (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE)).byteValue()) {
                        case ExtensionType.session_ticket /*35*/:
                        case EACTags.FILE_REFERENCE /*81*/:
                            str2 = com.americanexpress.sdkmodulelib.util.Constants.SERVICE_CODE_MAG_NON_BIO;
                            break;
                        case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA /*70*/:
                            str2 = com.americanexpress.sdkmodulelib.util.Constants.SERVICE_CODE_MAG_BIO;
                            break;
                    }
                    str = str.substring(0, parseInt) + str2 + str.substring(parseInt + 3);
                }
                tokenAPDUResponse.setBaApduResponse(HexUtils.hexStringToByteArray(str));
                return tokenAPDUResponse;
            }
        } catch (HCEClientException e) {
            if (e.getIsoSW() == (short) 0) {
                tokenAPDUResponse.setsSW(ISO7816.SW_UNKNOWN);
            } else {
                tokenAPDUResponse.setsSW(e.getIsoSW());
            }
            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_IDLE));
        }
    }

    private short checkAFL(byte b, byte b2, TokenAPDUResponse tokenAPDUResponse) {
        byte[] bArr;
        tokenAPDUResponse.setsSW(ISO7816.SW_FILE_NOT_FOUND);
        byte[] bArr2 = (byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_AFL);
        if (bArr2 != null) {
            bArr = bArr2;
        } else if (PaymentUtils.checkXPMConfig((byte) 2, (byte) 1) == Constants.MAGIC_TRUE) {
            bArr = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AFL_TAG), true).getValue());
        } else {
            bArr = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AFL_TAG), true).getValue());
        }
        if (bArr != null) {
            int i = 0;
            while (i < bArr.length) {
                short s = (short) (i + 1);
                byte b3 = (byte) (bArr[i] >> 3);
                short s2 = (short) (s + 1);
                byte b4 = bArr[s];
                short s3 = (short) (s2 + 1);
                byte b5 = bArr[s2];
                if (b3 == b) {
                    tokenAPDUResponse.setsSW(ISO7816.SW_RECORD_NOT_FOUND);
                    if (b2 < b4 || b2 > b5) {
                        tokenAPDUResponse.setsSW(ISO7816.SW_RECORD_NOT_FOUND);
                    } else {
                        tokenAPDUResponse.setsSW(ISO7816.SW_NO_ERROR);
                        return Constants.MAGIC_TRUE;
                    }
                }
                i = (short) (s3 + 1);
            }
        }
        return Constants.MAGIC_FALSE;
    }
}
