package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.p002b.Log;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class MCFCITemplate {
    public static final short AID_PRIMARY_INDICATOR = (short) 1;
    public static final short AID_SECONDARY_INDICATOR = (short) 2;
    public static final byte ALT_PRIORITY = (byte) 2;
    public static final String TAG = "mcpce_MCFCITemplate";
    public static final short TAG_ADF_NAME = (short) 79;
    public static final short TAG_APP_LABEL = (short) 80;
    public static final short TAG_APP_PRIORITY_INDICATOR = (short) 135;
    public static final short TAG_APP_TEMPLATE = (short) 97;
    public static final short TAG_DF_NAME = (short) 132;
    public static final int TAG_FCI_ISSUER_DISCRETIONARY_DATA = 48908;
    public static final int TAG_FCI_ISSUER_ICC = 24405;
    public static final byte TAG_FCI_ISSUER_IIN = (byte) 66;
    public static final int TAG_FCI_ISSUER_LNG_PREF = 24365;
    public static final int TAG_FCI_PDOL = 40760;
    public static final short TAG_FCI_PROPRIETARY_TEMPLATE = (short) 165;
    public static final byte TAG_FCI_TEMPLATE = (byte) 111;
    public static final byte TAG_FILE_CONTROL_INFORMATION = (byte) 95;
    private static final ByteArrayFactory baf;
    private List<DirectoryEntry> mEntries;
    private ByteArray mFci;
    private DirectoryEntry mPrimaryEntry;
    private DirectoryEntry mSecondaryEntry;

    private static class DirectoryEntry {
        private ByteArray mAid;
        private ByteArray mApplicationLabel;
        private ByteArray mICC;
        private ByteArray mIDD;
        private ByteArray mIIN;
        private ByteArray mLangPref;
        private ByteArray mPDOL;
        private byte mPriorityIndicator;

        public void setAid(ByteArray byteArray) {
            this.mAid = byteArray;
        }

        public ByteArray getAid() {
            return this.mAid;
        }

        public void setPriorityIndicator(byte b) {
            this.mPriorityIndicator = b;
        }

        public byte getPriorityIndicator() {
            return this.mPriorityIndicator;
        }

        public void setApplicationLabel(ByteArray byteArray) {
            this.mApplicationLabel = byteArray;
        }

        public ByteArray getApplicationLabel() {
            return this.mApplicationLabel;
        }

        public ByteArray getIIN() {
            return this.mIIN;
        }

        public ByteArray getIDD() {
            return this.mIDD;
        }

        public ByteArray getICC() {
            return this.mICC;
        }

        public ByteArray getLangPref() {
            return this.mLangPref;
        }

        public ByteArray getPDOL() {
            return this.mPDOL;
        }

        public void setIIN(ByteArray byteArray) {
            this.mIIN = byteArray;
        }

        public void setIDD(ByteArray byteArray) {
            this.mIDD = byteArray;
        }

        public void setICC(ByteArray byteArray) {
            this.mICC = byteArray;
        }

        public void setLangPref(ByteArray byteArray) {
            this.mLangPref = byteArray;
        }

        public void setPDOL(ByteArray byteArray) {
            this.mPDOL = byteArray;
        }
    }

    private static class EntryTLVHandler extends TLVHandler {
        private DirectoryEntry entry;

        private EntryTLVHandler() {
            this.entry = new DirectoryEntry();
        }

        public void parseTag(byte b, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler Parse tag: " + b + ", length: " + i);
            Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler Parse tag: " + b + ", length: " + i + ", offset: " + i2);
            Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler Parse tag: " + (b & GF2Field.MASK) + ", length: " + i + ", offset: " + i2);
            Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: checkLength start.");
            if (checkTagLength(i, bArr, i2)) {
                Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: array.");
                Object obj = new byte[i];
                System.arraycopy(bArr, i2, obj, 0, i);
                Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: data: " + MCFCITemplate.baf.getByteArray(bArr, bArr.length).getHexString());
                Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: array: " + MCFCITemplate.baf.getByteArray(obj, obj.length).getHexString());
                switch (b & GF2Field.MASK) {
                    case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA /*66*/:
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: IIN");
                        this.entry.setIIN(MCFCITemplate.baf.getByteArray(obj, obj.length));
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: issuer iin: " + this.entry.getIIN().getHexString());
                        return;
                    case EACTags.APPLICATION_IDENTIFIER /*79*/:
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: Aid");
                        this.entry.setAid(MCFCITemplate.baf.getByteArray(obj, obj.length));
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: Aid: " + this.entry.getAid().getHexString());
                        return;
                    case EACTags.APPLICATION_LABEL /*80*/:
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: Label");
                        this.entry.setApplicationLabel(MCFCITemplate.baf.getByteArray(obj, obj.length));
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: label: " + this.entry.getApplicationLabel().getHexString());
                        return;
                    case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA /*135*/:
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: Priority");
                        if (i == 1) {
                            this.entry.setPriorityIndicator(bArr[i2]);
                        }
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: priority: " + this.entry.getPriorityIndicator());
                        return;
                    default:
                        return;
                }
            }
            Log.m286e(MCFCITemplate.TAG, "EntryTLVHandler: checkLength failed.");
        }

        public DirectoryEntry getDirectoryEntry() {
            return this.entry;
        }

        public void setEntry(DirectoryEntry directoryEntry) {
            this.entry = directoryEntry;
        }

        public void parseTag(short s, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler Parse long tag: " + s + ", length: " + i);
            if (checkTagLength(i, bArr, i2)) {
                Object obj = new byte[i];
                System.arraycopy(bArr, i2, obj, 0, i);
                switch (s) {
                    case (short) -24776:
                        this.entry.setPDOL(MCFCITemplate.baf.getByteArray(obj, obj.length));
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler:  PDOL:" + this.entry.getPDOL().getHexString());
                        return;
                    case (short) -16628:
                        this.entry.setIDD(MCFCITemplate.baf.getByteArray(obj, obj.length));
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: issuer IDD: " + this.entry.getIDD().getHexString());
                        return;
                    case MCFCITemplate.TAG_FCI_ISSUER_LNG_PREF /*24365*/:
                        this.entry.setLangPref(MCFCITemplate.baf.getByteArray(obj, obj.length));
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: lang pref: " + this.entry.getLangPref().getHexString());
                        return;
                    case MCFCITemplate.TAG_FCI_ISSUER_ICC /*24405*/:
                        this.entry.setICC(MCFCITemplate.baf.getByteArray(obj, obj.length));
                        Log.m285d(MCFCITemplate.TAG, "EntryTLVHandler: issuer ICC: " + this.entry.getICC().getHexString());
                        return;
                    default:
                        return;
                }
            }
            Log.m286e(MCFCITemplate.TAG, "EntryTLVHandler: checkLength failed.");
        }

        private boolean checkTagLength(int i, byte[] bArr, int i2) {
            if (i == 0) {
                Log.m286e(MCFCITemplate.TAG, "checkTagLength Parse tag: tag length is 0");
                return false;
            } else if (bArr == null) {
                Log.m286e(MCFCITemplate.TAG, "checkTagLength Parse tag: data is null");
                return false;
            } else if (bArr.length <= i2) {
                Log.m286e(MCFCITemplate.TAG, "checkTagLength: Wrong offset value: offset = " + i2 + ", data length = " + bArr.length);
                return false;
            } else if (bArr.length >= i2 + i) {
                return true;
            } else {
                Log.m286e(MCFCITemplate.TAG, "checkTagLength: Tag offset and length < data length value: offset = " + i2 + ", length = " + i + ", data length = " + bArr.length);
                return false;
            }
        }
    }

    private class FCIAIDTLVHandler extends TLVHandler {
        ByteArray aid;

        private FCIAIDTLVHandler() {
            this.aid = null;
        }

        public void parseTag(byte b, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler Parse tag: " + (b & GF2Field.MASK) + ", length: " + i + ", offset: " + i2);
            switch (b & GF2Field.MASK) {
                case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA /*132*/:
                    Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler Parse tag: " + (b & GF2Field.MASK) + ", length: " + i);
                    Object obj = new byte[i];
                    System.arraycopy(bArr, i2, obj, 0, i);
                    this.aid = MCFCITemplate.baf.getByteArray(obj, obj.length);
                    Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler AID: " + this.aid.getHexString());
                case CipherSuite.TLS_DH_DSS_WITH_AES_256_GCM_SHA384 /*165*/:
                    try {
                        Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler Parse: Template tag");
                        TLVHandler entryTLVHandler = new EntryTLVHandler();
                        TLVParser.parseTLV(bArr, i2, i, entryTLVHandler);
                        Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler Entry parsed...");
                        DirectoryEntry directoryEntry = entryTLVHandler.getDirectoryEntry();
                        if (directoryEntry != null) {
                            Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler entry is not null.");
                            if (this.aid == null) {
                                Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler aid is null");
                            } else {
                                Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler aid is not null: " + this.aid.getHexString());
                            }
                            directoryEntry.setAid(this.aid);
                            MCFCITemplate.this.mEntries.add(directoryEntry);
                            return;
                        }
                        Log.m285d(MCFCITemplate.TAG, "FCIAIDTLVHandler entry is null.");
                    } catch (ParsingException e) {
                        Log.m286e(MCFCITemplate.TAG, "ParsingException parsing directory entry: " + e.getMessage());
                        e.printStackTrace();
                    }
                default:
            }
        }

        public void parseTag(short s, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "Parse long tag: " + s + ", length: " + i);
        }
    }

    private class FCITLVHandler extends TLVHandler {
        private FCITLVHandler() {
        }

        public void parseTag(byte b, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "Parse tag: " + b + ", length: " + i + ", offset: " + i2);
            switch (b & GF2Field.MASK) {
                case EACTags.APPLICATION_TEMPLATE /*97*/:
                    try {
                        Log.m285d(MCFCITemplate.TAG, "Parse: Template tag");
                        TLVHandler entryTLVHandler = new EntryTLVHandler();
                        TLVParser.parseTLV(bArr, i2, i, entryTLVHandler);
                        DirectoryEntry directoryEntry = entryTLVHandler.getDirectoryEntry();
                        MCFCITemplate.this.mEntries.add(directoryEntry);
                        switch ((short) directoryEntry.getPriorityIndicator()) {
                            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                                Log.m285d(MCFCITemplate.TAG, "Parse: primary indicator");
                                MCFCITemplate.this.mPrimaryEntry = directoryEntry;
                                return;
                            case CipherSpiExt.DECRYPT_MODE /*2*/:
                                Log.m285d(MCFCITemplate.TAG, "Parse: secondary indicator");
                                MCFCITemplate.this.mSecondaryEntry = directoryEntry;
                                return;
                            default:
                                return;
                        }
                    } catch (ParsingException e) {
                        Log.m286e(MCFCITemplate.TAG, "ParsingException parsing directory entry: " + e.getMessage());
                        e.printStackTrace();
                    }
                    Log.m286e(MCFCITemplate.TAG, "ParsingException parsing directory entry: " + e.getMessage());
                    e.printStackTrace();
                default:
            }
        }

        public void parseTag(short s, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "Parse long tag: " + s + ", length: " + i);
        }
    }

    private class FCITLVHandlerOnlyForTemplate extends TLVHandler {
        private FCITLVHandlerOnlyForTemplate() {
        }

        public void parseTag(byte b, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "Parse tag: " + b + ", length: " + i + ", offset: " + i2);
            switch (b & GF2Field.MASK) {
                case EACTags.FCI_TEMPLATE /*111*/:
                    try {
                        Log.m285d(MCFCITemplate.TAG, "Parse: Template tag : Invoke FCIAIDTLVHandler now");
                        TLVParser.parseTLV(bArr, i2, i, new FCIAIDTLVHandler(null));
                    } catch (ParsingException e) {
                        Log.m286e(MCFCITemplate.TAG, "ParsingException parsing directory entry: " + e.getMessage());
                        e.printStackTrace();
                    }
                default:
                    Log.m285d(MCFCITemplate.TAG, "FCITLVHandlerOnlyForTemplate Parse invalid tag: " + b + ", length: " + i);
            }
        }

        public void parseTag(short s, int i, byte[] bArr, int i2) {
            Log.m285d(MCFCITemplate.TAG, "Parse long tag: " + s + ", length: " + i);
        }
    }

    static {
        baf = ByteArrayFactory.getInstance();
    }

    public MCFCITemplate(ByteArray byteArray) {
        this.mEntries = new ArrayList();
        this.mFci = byteArray;
    }

    public List<DirectoryEntry> getEntries() {
        return this.mEntries;
    }

    public ByteArray getAID() {
        Log.m285d(TAG, "getAID: " + (!getEntries().isEmpty() ? "not empty" : "empty"));
        return !getEntries().isEmpty() ? ((DirectoryEntry) getEntries().get(0)).getAid() : null;
    }

    public ByteArray getPrimaryAid() {
        return this.mPrimaryEntry != null ? this.mPrimaryEntry.getAid() : null;
    }

    public ByteArray getSecondaryAid() {
        return this.mSecondaryEntry != null ? this.mSecondaryEntry.getAid() : null;
    }

    public void parseFCI_PPSE() {
        int i = 0;
        if (this.mFci == null) {
            Log.m286e(TAG, "parseFCI_PPSE, passed FCI is null...");
            return;
        }
        this.mEntries.clear();
        try {
            Object bytes = this.mFci.getBytes();
            int i2 = 0;
            while (i2 < bytes.length) {
                if ((bytes[i2] & GF2Field.MASK) == CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256) {
                    Log.m285d(TAG, "Found 0xBF, index: " + i2);
                    i2++;
                    if (i2 < bytes.length && (bytes[i2] & GF2Field.MASK) == 12) {
                        Log.m285d(TAG, "Found 0xOC, index: " + i2);
                        i2++;
                        if (i2 < bytes.length) {
                            i = i2 + 1;
                            i2 = bytes[i2];
                            Log.m285d(TAG, "FCI length: " + i2 + ", index: " + i);
                            break;
                        }
                    }
                }
                i2++;
            }
            i2 = 0;
            Object obj = new byte[i2];
            System.arraycopy(bytes, i, obj, 0, i2);
            Log.m285d(TAG, "FCI to parse: " + baf.getByteArray(obj, obj.length).getHexString());
            TLVParser.parseTLV(obj, 0, obj.length, new FCITLVHandler());
            Log.m285d(TAG, "No parsing exception");
        } catch (ParsingException e) {
            Log.m286e(TAG, "ParsingException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void parseFCI_AID() {
        if (this.mFci == null) {
            Log.m286e(TAG, "parseFCI_AID, passed AID is null...");
            return;
        }
        this.mEntries.clear();
        try {
            byte[] bytes = this.mFci.getBytes();
            TLVParser.parseTLV(bytes, 0, bytes.length, new FCITLVHandlerOnlyForTemplate());
            Log.m285d(TAG, "parseFCI_AID, No parsing exception");
        } catch (ParsingException e) {
            Log.m286e(TAG, "ParsingException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ByteArray restoreAlternateFCI(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4) {
        MCFCITemplate mCFCITemplate = new MCFCITemplate(byteArray);
        mCFCITemplate.parseFCI_AID();
        if (mCFCITemplate.getEntries() == null || mCFCITemplate.getEntries().isEmpty()) {
            Log.m286e(TAG, "restoreAlternateFCI, cannot parse main fci.");
            return null;
        }
        DirectoryEntry directoryEntry = (DirectoryEntry) mCFCITemplate.getEntries().get(0);
        if (directoryEntry == null) {
            Log.m286e(TAG, "restoreAlternateFCI, main DF is null.");
            return null;
        }
        ByteArray create = TLV.create((byte) PinChangeUnblockApdu.CLA, byteArray2);
        ByteArray create2 = TLV.create((byte) GetTemplateApdu.TAG_APPLICATION_LABEL_50, byteArray3);
        create2.append(TLV.create((byte) GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, baf.getByteArray(new byte[]{ALT_PRIORITY}, 1)));
        if (directoryEntry.getICC() != null) {
            create2.append(TLV.create(getByteArrayFromTag(TAG_FCI_ISSUER_ICC), directoryEntry.getICC()));
        } else {
            Log.m286e(TAG, "restoreAlternateFCI, main FCI error: ICC is null");
        }
        if (directoryEntry.getIIN() != null) {
            create2.append(TLV.create((byte) TAG_FCI_ISSUER_IIN, directoryEntry.getIIN()));
        } else {
            Log.m286e(TAG, "restoreAlternateFCI, main FCI error: IIN is null");
        }
        if (directoryEntry.getLangPref() != null) {
            create2.append(TLV.create(getByteArrayFromTag(TAG_FCI_ISSUER_LNG_PREF), directoryEntry.getLangPref()));
        } else {
            Log.m286e(TAG, "restoreAlternateFCI, main FCI error: language preferences is null");
        }
        create2.append(TLV.create(getByteArrayFromTag(TAG_FCI_PDOL), byteArray4));
        if (directoryEntry.getIDD() != null) {
            create2.append(TLV.create(getByteArrayFromTag(TAG_FCI_ISSUER_DISCRETIONARY_DATA), directoryEntry.getIDD()));
        } else {
            Log.m286e(TAG, "restoreAlternateFCI, main FCI error: idd is null");
        }
        create.append(TLV.create((byte) PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, create2));
        return TLV.create((byte) TAG_FCI_TEMPLATE, create);
    }

    private static ByteArray getByteArrayFromTag(int i) {
        return baf.getByteArray(new byte[]{(byte) ((i >> 8) & GF2Field.MASK), (byte) (i & GF2Field.MASK)}, 2);
    }
}
