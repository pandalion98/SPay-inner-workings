package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HashUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.RandomNumberGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.PPSETagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class TokenPersoDelegate extends OperationDelegate {
    private static final int NO_OF_DATA_ELEMENTS_WITHOUT_MST = 4;
    private static final int NO_OF_DATA_ELEMENTS_WITH_MST = 5;

    public void doOperation() {
        Session session = SessionManager.getSession();
        try {
            String str;
            String str2;
            byte[] objectsToLLVar;
            int parseInt;
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            TokenDataHolder tokenDataHolder = (TokenDataHolder) session.getValue(SessionConstants.TOKEN_DATA_HOLDER, false);
            invokeInit(tokenDataHolder);
            if (isPersoReqd(tokenDataHolder)) {
                str = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dP1));
                String str3 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dQ1));
                String str4 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_P));
                String str5 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_Q));
                String str6 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_PQ));
                str2 = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
                if (tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_MOD))) {
                    str2 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_MOD));
                }
                String str7 = LLVARUtil.HEX_STRING + tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.ICC_DYNAMIC_AUTHENTICATION_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_PUBLIC_KEY_EXPONENT_TAG), true).getValue();
                objectsToLLVar = LLVARUtil.objectsToLLVar(str, str3, str4, str5, str6, str2, str7);
                parseInt = Integer.parseInt(tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_KEY_LENGTH_TAG), true).getValue(), 16);
                byte[] random = RandomNumberGenerator.random(8);
                str4 = Utility.byteArrayToHexString(random);
                TagValue tagValue = new TagValue();
                tagValue.setValue(str4);
                tokenDataHolder.getTagsMap().put(TagsMapUtil.getTagKey(HCEClientConstants.LOCK_CODE), tagValue);
                checkSCStatus(secureComponentImpl.perso(parseInt, objectsToLLVar, random));
            }
            boolean mstDataAvailable = mstDataAvailable(tokenDataHolder);
            TagValue tagValue2 = new TagValue();
            tagValue2.setValue(String.valueOf(mstDataAvailable));
            tokenDataHolder.getTagsMap().put(TagsMapUtil.getTagKey(HCEClientConstants.MST_SUPPORTED), tagValue2);
            List tagList = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
            List tagList2 = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG));
            Object obj = tagList != null ? 1 : null;
            int i = NO_OF_DATA_ELEMENTS_WITHOUT_MST;
            if (mstDataAvailable) {
                Object obj2 = (tagList == null || tagList2 == null || tagList.size() != tagList2.size()) ? null : 1;
                obj = obj2;
                i = NO_OF_DATA_ELEMENTS_WITH_MST;
            }
            if (obj != null) {
                String[] strArr = new String[(i * tagList.size())];
                MSTLUPCTagValue mSTLUPCTagValue = null;
                int i2 = 0;
                int i3 = 0;
                while (i3 < tagList.size()) {
                    NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) tokenDataHolder.getTagValue((TagKey) tagList.get(i3));
                    if (mstDataAvailable) {
                        mSTLUPCTagValue = (MSTLUPCTagValue) tokenDataHolder.getTagValue((TagKey) tagList2.get(i3));
                        if (!nFCLUPCTagValue.getAtc().equals(mSTLUPCTagValue.getAtc())) {
                            throw new HCEClientException(OperationStatus.NFC_ATC_AND_MST_ATC_MISMATCH);
                        }
                    }
                    MSTLUPCTagValue mSTLUPCTagValue2 = mSTLUPCTagValue;
                    str = nFCLUPCTagValue.getLupc();
                    int i4 = i2 + 1;
                    strArr[i2] = LLVARUtil.HEX_STRING + Integer.toHexString(str.length() / 2);
                    i2 = i4 + 1;
                    strArr[i4] = LLVARUtil.HEX_STRING + str;
                    i4 = i2 + 1;
                    strArr[i2] = LLVARUtil.HEX_STRING + nFCLUPCTagValue.getKcv();
                    parseInt = i4 + 1;
                    strArr[i4] = LLVARUtil.HEX_STRING + nFCLUPCTagValue.getAtc();
                    if (mstDataAvailable) {
                        i = parseInt + 1;
                        strArr[parseInt] = LLVARUtil.HEX_STRING + mSTLUPCTagValue2.getMstDynamicData();
                    } else {
                        i = parseInt;
                    }
                    i3++;
                    i2 = i;
                    mSTLUPCTagValue = mSTLUPCTagValue2;
                }
                checkSCStatus(secureComponentImpl.update(LLVARUtil.objectsToLLVar(strArr)));
                str2 = HashUtils.computeSHA256(tokenDataHolder.getTlsClearTokenData());
                byte[] objectsToLLVar2 = LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + str2);
                objectsToLLVar = new byte[computeDestBufferSize(objectsToLLVar2.length)];
                checkSCStatus(secureComponentImpl.getSignatureData(objectsToLLVar2, objectsToLLVar));
                if (secureComponentImpl.isRetryExecuted()) {
                    objectsToLLVar = secureComponentImpl.getDestBuffer();
                }
                session.setValue(SessionConstants.TOKEN_DATA_SIGNATURE, LLVARUtil.llVarToObjects(objectsToLLVar)[0].toString());
                buildMetaData(tokenDataHolder);
                cleanDGIMap(tokenDataHolder);
                setDataContext(tokenDataHolder);
                setTokenConfiguration();
                setServiceCdMSOffset();
                setCVN();
                prepareFCIResponse();
                tokenActivation();
                return;
            }
            Log.e(HCEClientConstants.TAG, "::TokenPersoDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
            throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenPersoDelegate::catch::" + e.getMessage());
            throw e;
        }
    }

    protected void setDataContext(TokenDataHolder tokenDataHolder) {
        DataContext dataContext = (DataContext) SessionManager.getSession().getValue(SessionConstants.DATA_CONTEXT, false);
        dataContext.getDgiMap().putAll(tokenDataHolder.getDgisMap());
        dataContext.getTagMap().putAll(tokenDataHolder.getTagsMap());
        cleanTokenDataHolder(tokenDataHolder);
    }

    protected void buildMetaData(TokenDataHolder tokenDataHolder) {
        int i = 0;
        Map tagsMap = tokenDataHolder.getTagsMap();
        boolean mstDataAvailable = mstDataAvailable(tokenDataHolder);
        List tagList = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
        List tagList2 = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG));
        MetaDataManager.setMetaDataValue(MetaDataManager.MAX_ATC, TagValue.fromString(((NFCLUPCTagValue) tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), false)).getAtc()));
        MetaDataManager.setMetaDataValue(MetaDataManager.LUPC_COUNT, TagValue.fromString(String.valueOf(tagList.size())));
        int i2 = tagList != null ? 1 : 0;
        if (mstDataAvailable) {
            i2 = (tagList == null || tagList2 == null || tagList.size() != tagList2.size()) ? 0 : 1;
        }
        if (i2 != 0) {
            while (i < tagList.size()) {
                TagKey tagKey = (TagKey) tagList.get(i);
                MetaDataManager.getMetaDataMap().put(tagKey, tagsMap.remove(tagKey));
                if (mstDataAvailable) {
                    tagKey = (TagKey) tagList2.get(i);
                    MetaDataManager.getMetaDataMap().put(tagKey, tagsMap.remove(tagKey));
                }
                i++;
            }
            updateMetaDataMap(tokenDataHolder);
            executeLUPCThreshold(tokenDataHolder);
            return;
        }
        Log.e(HCEClientConstants.TAG, "::TokenPersoDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
        throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
    }

    protected void updateMetaDataMap(TokenDataHolder tokenDataHolder) {
        MetaDataManager.setMetaDataValue(MetaDataManager.RUNNING_ATC, tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.STARTING_ATC_TAG), false));
        MetaDataManager.setMetaDataValue(MetaDataManager.TOKEN_DATA_VERSION, tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_DATA_VERSION_TAG), false));
        MetaDataManager.setMetaDataValue(MetaDataManager.LUPC_THRESHOLD, tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.LUPC_THRESHOLD_TAG), false));
        MetaDataManager.setMetaDataValue(MetaDataManager.TOKEN_STATUS, tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_STATUS_TAG), true));
    }

    protected void executeLUPCThreshold(TokenDataHolder tokenDataHolder) {
        List tagList = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
        List tagList2 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG));
        int parseInt = Integer.parseInt(HexUtils.nBytesFromHexString(MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_THRESHOLD), 1, 1), 16);
        boolean mstDataAvailable = mstDataAvailable(tokenDataHolder);
        boolean z = tagList != null;
        if (mstDataAvailable) {
            z = (tagList == null || tagList2 == null || tagList.size() != tagList2.size()) ? false : true;
        }
        if (z) {
            int i;
            int size = tagList.size();
            if (size > parseInt) {
                i = size - parseInt;
                for (size = 0; size < i; size++) {
                    MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), true));
                }
            }
            if (mstDataAvailable) {
                size = tagList2.size();
                if (size > parseInt) {
                    i = size - parseInt;
                    for (size = 0; size < i; size++) {
                        MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG), true));
                    }
                }
            }
            MetaDataManager.setMetaDataValue(MetaDataManager.RUNNING_ATC, TagValue.fromString(((NFCLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), true)).getAtc()));
            MetaDataManager.setMetaDataValue(MetaDataManager.MAX_ATC, TagValue.fromString(((NFCLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), false)).getAtc()));
            return;
        }
        Log.e(HCEClientConstants.TAG, "::TokenPersoDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
        throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
    }

    protected boolean isPersoReqd(TokenDataHolder tokenDataHolder) {
        TagValue tagValue = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_KEY_LENGTH_TAG), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_KEY_LENGTH_TAG), true);
        }
        return tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dP1)) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dQ1)) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_P)) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_Q)) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_PQ)) && tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.ICC_DYNAMIC_AUTHENTICATION_DGI)) && tagValue != null && tagValue.getValue() != null;
    }

    protected boolean mstDataAvailable(TokenDataHolder tokenDataHolder) {
        if (((byte) (getXPMConfigBytes(tokenDataHolder)[NO_OF_DATA_ELEMENTS_WITHOUT_MST] & 16)) == 16 && tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI))) {
            return true;
        }
        return false;
    }

    protected byte[] getXPMConfigBytes(TokenDataHolder tokenDataHolder) {
        return HexUtils.hexStringToByteArray(tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_XPM_CONFIG), true).getValue());
    }

    protected void cleanDGIMap(TokenDataHolder tokenDataHolder) {
        try {
            tokenDataHolder.getDgisMap().remove(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI));
            if (isPersoReqd(tokenDataHolder)) {
                tokenDataHolder.getDgisMap().remove(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dP1));
                tokenDataHolder.getDgisMap().remove(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dQ1));
                tokenDataHolder.getDgisMap().remove(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_P));
                tokenDataHolder.getDgisMap().remove(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_Q));
                tokenDataHolder.getDgisMap().remove(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_PQ));
            }
            if (mstDataAvailable(tokenDataHolder)) {
                tokenDataHolder.getDgisMap().remove(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI));
            }
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "cleanDGIMap::error::" + e.getMessage());
        }
    }

    private void setServiceCdMSOffset() {
        String dGIValue = TagsMapUtil.getDGIValue(CPDLConfig.getDGI_TAG(CPDLConfig.SFI1_REC1_DGI_MS));
        if (dGIValue != null) {
            byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(dGIValue);
            int i = 0;
            while (i < hexStringToByteArray.length) {
                int i2;
                Object obj;
                if ((hexStringToByteArray[i] & 32) == 32) {
                    i2 = i;
                    obj = 1;
                } else {
                    i2 = i;
                    obj = null;
                }
                while (true) {
                    if (hexStringToByteArray[i2] != null && hexStringToByteArray[i2] != -1) {
                        short s;
                        int i3;
                        short s2;
                        int i4;
                        if (((byte) (hexStringToByteArray[i2] & 31)) == 31) {
                            s = HexUtils.getShort(hexStringToByteArray, i2);
                            i3 = i2 + 2;
                            s2 = s;
                        } else {
                            i3 = i2 + 1;
                            s2 = (short) (hexStringToByteArray[i2] & GF2Field.MASK);
                        }
                        if (hexStringToByteArray[i3] == -127) {
                            i4 = i3 + 1;
                            i3 = i4 + 1;
                            i4 = (short) (hexStringToByteArray[i4] & GF2Field.MASK);
                        } else if (hexStringToByteArray[i3] == -126) {
                            i4 = i3 + 1;
                            s = HexUtils.getShort(hexStringToByteArray, i4);
                            i3 = i4 + 2;
                            r4 = s;
                        } else {
                            s = (short) (hexStringToByteArray[i3] & GF2Field.MASK);
                            i3++;
                            r4 = s;
                        }
                        switch (s2) {
                            case EACTags.TRACK2_APPLICATION /*87*/:
                                TagsMapUtil.setTagValue(HCEClientConstants.SERVICE_CODE_MS_OFFSET, TagValue.fromString(String.valueOf((HexUtils.byteArrayToHexString(hexStringToByteArray, i3, i4).toUpperCase().indexOf(68) + 1) + ((i3 * 2) + NO_OF_DATA_ELEMENTS_WITHOUT_MST))));
                                return;
                            default:
                                if (obj == null) {
                                    i = i3 + i4;
                                } else {
                                    i = i3;
                                }
                        }
                    } else if (i2 >= hexStringToByteArray.length - 2) {
                        throw new HCEClientException(HCEClientConstants.TAG, "::TokenPersoDelegate::setServiceCdMSOffset::Malformed Record Data");
                    } else {
                        i2++;
                    }
                }
            }
        }
    }

    private void prepareFCIResponse() {
        String str;
        String str2;
        byte length;
        DataContext sessionInstance = DataContext.getSessionInstance();
        PPSETagValue pPSETagValue = (PPSETagValue) TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V1), true);
        PPSETagValue pPSETagValue2 = (PPSETagValue) TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V2), false);
        String str3 = BuildConfig.FLAVOR;
        if (pPSETagValue != null) {
            str3 = pPSETagValue.getsAID();
        }
        String str4 = BuildConfig.FLAVOR;
        if (pPSETagValue2 != null) {
            str = pPSETagValue2.getsAID();
        } else {
            str = str4;
        }
        str4 = (String) sessionInstance.getDgiMap().get(CPDLConfig.getDGI_TAG(CPDLConfig.SELECT_AID_DGI));
        TagValue tagValue = TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PDOL_TAG), false);
        String str5 = BuildConfig.FLAVOR;
        if (tagValue != null) {
            str2 = "9F38" + Utility.constructLV(tagValue.getValue());
            length = (byte) (str2.length() / 2);
        } else {
            str2 = str5;
            length = (byte) 0;
        }
        byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(str4);
        String str6 = BuildConfig.FLAVOR;
        byte b = length;
        int i = false;
        while (i < hexStringToByteArray.length) {
            DOLTag parseTLV = TLVParser.parseTLV(hexStringToByteArray, i, hexStringToByteArray.length, true);
            if (parseTLV == null) {
                throw new HCEClientException(OperationStatus.TLV_PARSING_FAILURE);
            }
            byte b2;
            String str7;
            int skipLen = parseTLV.getSkipLen() + i;
            int i2;
            if (parseTLV.isConstructed()) {
                i2 = skipLen;
                b2 = b;
                str7 = (str6 + parseTLV.getTagName().toUpperCase()) + HexUtils.short2Hex(parseTLV.getTagLength()).toUpperCase();
                i = i2;
            } else {
                if (parseTLV.getTagName().compareToIgnoreCase("9F38") != 0 || str2.compareToIgnoreCase(BuildConfig.FLAVOR) == 0) {
                    str5 = ((str6 + parseTLV.getTagName().toUpperCase()) + HexUtils.short2Hex(parseTLV.getTagLength()).toUpperCase()) + parseTLV.getTagValue().toUpperCase();
                } else {
                    str5 = str6 + str2;
                    b = (byte) (b - (parseTLV.getTagLength() + 3));
                }
                i2 = skipLen + parseTLV.getTagLength();
                b2 = b;
                str7 = str5;
                i = i2;
            }
            str6 = str7;
            b = b2;
        }
        byte[] hexStringToByteArray2 = HexUtils.hexStringToByteArray(str6);
        hexStringToByteArray2[1] = (byte) (b + hexStringToByteArray2[1]);
        str5 = HexUtils.byteArrayToHexString(hexStringToByteArray2);
        str4 = APDUConstants.SELECT_RESPONSE_FCI_TEMPLATE + Utility.constructLV(APDUConstants.SELECT_RESPONSE_DF_NAME + Utility.constructLV(str3) + str4);
        str = APDUConstants.SELECT_RESPONSE_FCI_TEMPLATE + Utility.constructLV(APDUConstants.SELECT_RESPONSE_DF_NAME + Utility.constructLV(str) + str5);
        TagsMapUtil.setTagValue(HCEClientConstants.PRIMARY_AID_FCI, TagValue.fromString(str4));
        TagsMapUtil.setTagValue(HCEClientConstants.ALIAS_AID_FCI, TagValue.fromString(str));
    }

    private void setCVN() {
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_TAG), true);
        TagValue tagValue2 = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_TAG), true);
        if (tagValue != null) {
            TagsMapUtil.setTagValue(HCEClientConstants.MS_CVN_VALUE, TagValue.fromString(HexUtils.nBytesFromHexString(tagValue.getValue(), 2, 1)));
        }
        if (tagValue2 != null) {
            TagsMapUtil.setTagValue(HCEClientConstants.EMV_CVN_VALUE, TagValue.fromString(HexUtils.nBytesFromHexString(tagValue2.getValue(), 2, 1)));
        }
    }

    private void cleanTokenDataHolder(TokenDataHolder tokenDataHolder) {
        tokenDataHolder.getDgisMap().clear();
        tokenDataHolder.getTagsMap().clear();
    }

    protected void invokeInit(TokenDataHolder tokenDataHolder) {
        String str = LLVARUtil.HEX_STRING + tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.STARTING_ATC_TAG), true).getValue();
        TagValue tagValue = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_TAG), true);
        String str2 = "20000";
        if (tagValue != null) {
            str2 = LLVARUtil.HEX_STRING + tagValue.getValue();
        }
        String str3 = "201";
        String str4 = LLVARUtil.HEX_STRING + tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.CRYPTOGRAM_INFORMATION_DATA_TAG), true).getValue();
        String str5 = LLVARUtil.HEX_STRING + HexUtils.nBytesFromHexString(tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.LUPC_THRESHOLD_TAG), true).getValue(), 1, 1);
        String str6 = "200";
        if (mstDataAvailable(tokenDataHolder)) {
            str6 = LLVARUtil.HEX_STRING + HexUtils.nBytesFromHexString(tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.LUPC_THRESHOLD_TAG), true).getValue(), 1, 1);
        }
        checkSCStatus(new SecureComponentImpl().init(LLVARUtil.objectsToLLVar(str, str2, str3, str4, str5, str6)));
    }

    protected void setTokenConfiguration() {
        boolean z;
        boolean z2 = true;
        Session session = SessionManager.getSession();
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(HCEClientConstants.ISSUER_COUNTRY_CODE), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(HCEClientConstants.ISSUER_COUNTRY_CODE), true);
        }
        session.setValue(HCEClientConstants.ISSUER_COUNTRY_CODE, tagValue.getValue());
        byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(DataContext.getSessionInstance().getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_XPM_CONFIG), true).getValue());
        if (((byte) (hexStringToByteArray[3] & 16)) == 16) {
            z = true;
        } else {
            z = false;
        }
        session.setValue(HCEClientConstants.INAPP_SUPPORTED, String.valueOf(z));
        if (((byte) (hexStringToByteArray[NO_OF_DATA_ELEMENTS_WITHOUT_MST] & 8)) != 8) {
            z2 = false;
        }
        session.setValue(HCEClientConstants.TAP_PAYMENT_SUPPORTED, String.valueOf(z2));
    }

    private void tokenActivation() {
        PaymentUtils.setTokenStatus(StateMode.ACTIVE);
    }
}
