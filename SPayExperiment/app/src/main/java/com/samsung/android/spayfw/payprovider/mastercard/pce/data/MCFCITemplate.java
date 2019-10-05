/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.b.Log;

import java.util.ArrayList;
import java.util.List;

public class MCFCITemplate {
    public static final short AID_PRIMARY_INDICATOR = 1;
    public static final short AID_SECONDARY_INDICATOR = 2;
    public static final byte ALT_PRIORITY = 2;
    public static final String TAG = "mcpce_MCFCITemplate";
    public static final short TAG_ADF_NAME = 79;
    public static final short TAG_APP_LABEL = 80;
    public static final short TAG_APP_PRIORITY_INDICATOR = 135;
    public static final short TAG_APP_TEMPLATE = 97;
    public static final short TAG_DF_NAME = 132;
    public static final int TAG_FCI_ISSUER_DISCRETIONARY_DATA = 48908;
    public static final int TAG_FCI_ISSUER_ICC = 24405;
    public static final byte TAG_FCI_ISSUER_IIN = 66;
    public static final int TAG_FCI_ISSUER_LNG_PREF = 24365;
    public static final int TAG_FCI_PDOL = 40760;
    public static final short TAG_FCI_PROPRIETARY_TEMPLATE = 165;
    public static final byte TAG_FCI_TEMPLATE = 111;
    public static final byte TAG_FILE_CONTROL_INFORMATION = 95;
    private static final ByteArrayFactory baf = ByteArrayFactory.getInstance();
    private List<DirectoryEntry> mEntries = new ArrayList();
    private ByteArray mFci;
    private DirectoryEntry mPrimaryEntry;
    private DirectoryEntry mSecondaryEntry;

    public MCFCITemplate(ByteArray byteArray) {
        this.mFci = byteArray;
    }

    static /* synthetic */ DirectoryEntry access$502(MCFCITemplate mCFCITemplate, DirectoryEntry directoryEntry) {
        mCFCITemplate.mPrimaryEntry = directoryEntry;
        return directoryEntry;
    }

    static /* synthetic */ DirectoryEntry access$602(MCFCITemplate mCFCITemplate, DirectoryEntry directoryEntry) {
        mCFCITemplate.mSecondaryEntry = directoryEntry;
        return directoryEntry;
    }

    private static ByteArray getByteArrayFromTag(int n2) {
        ByteArrayFactory byteArrayFactory = baf;
        byte[] arrby = new byte[]{(byte)(255 & n2 >> 8), (byte)(n2 & 255)};
        return byteArrayFactory.getByteArray(arrby, 2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ByteArray restoreAlternateFCI(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4) {
        MCFCITemplate mCFCITemplate = new MCFCITemplate(byteArray);
        mCFCITemplate.parseFCI_AID();
        if (mCFCITemplate.getEntries() == null || mCFCITemplate.getEntries().isEmpty()) {
            Log.e(TAG, "restoreAlternateFCI, cannot parse main fci.");
            return null;
        }
        DirectoryEntry directoryEntry = (DirectoryEntry)mCFCITemplate.getEntries().get(0);
        if (directoryEntry == null) {
            Log.e(TAG, "restoreAlternateFCI, main DF is null.");
            return null;
        }
        ByteArray byteArray5 = TLV.create((byte)-124, byteArray2);
        ByteArray byteArray6 = TLV.create((byte)80, byteArray3);
        byteArray6.append(TLV.create((byte)-121, baf.getByteArray(new byte[]{2}, 1)));
        if (directoryEntry.getICC() != null) {
            byteArray6.append(TLV.create(MCFCITemplate.getByteArrayFromTag(24405), directoryEntry.getICC()));
        } else {
            Log.e(TAG, "restoreAlternateFCI, main FCI error: ICC is null");
        }
        if (directoryEntry.getIIN() != null) {
            byteArray6.append(TLV.create((byte)66, directoryEntry.getIIN()));
        } else {
            Log.e(TAG, "restoreAlternateFCI, main FCI error: IIN is null");
        }
        if (directoryEntry.getLangPref() != null) {
            byteArray6.append(TLV.create(MCFCITemplate.getByteArrayFromTag(24365), directoryEntry.getLangPref()));
        } else {
            Log.e(TAG, "restoreAlternateFCI, main FCI error: language preferences is null");
        }
        byteArray6.append(TLV.create(MCFCITemplate.getByteArrayFromTag(40760), byteArray4));
        if (directoryEntry.getIDD() != null) {
            byteArray6.append(TLV.create(MCFCITemplate.getByteArrayFromTag(48908), directoryEntry.getIDD()));
        } else {
            Log.e(TAG, "restoreAlternateFCI, main FCI error: idd is null");
        }
        byteArray5.append(TLV.create((byte)-91, byteArray6));
        return TLV.create((byte)111, byteArray5);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ByteArray getAID() {
        StringBuilder stringBuilder = new StringBuilder().append("getAID: ");
        String string = !this.getEntries().isEmpty() ? "not empty" : "empty";
        Log.d(TAG, stringBuilder.append(string).toString());
        if (!this.getEntries().isEmpty()) {
            return ((DirectoryEntry)this.getEntries().get(0)).getAid();
        }
        return null;
    }

    public List<DirectoryEntry> getEntries() {
        return this.mEntries;
    }

    public ByteArray getPrimaryAid() {
        if (this.mPrimaryEntry != null) {
            return this.mPrimaryEntry.getAid();
        }
        return null;
    }

    public ByteArray getSecondaryAid() {
        if (this.mSecondaryEntry != null) {
            return this.mSecondaryEntry.getAid();
        }
        return null;
    }

    public void parseFCI_AID() {
        if (this.mFci == null) {
            Log.e(TAG, "parseFCI_AID, passed AID is null...");
            return;
        }
        this.mEntries.clear();
        try {
            byte[] arrby = this.mFci.getBytes();
            TLVParser.parseTLV(arrby, 0, arrby.length, new FCITLVHandlerOnlyForTemplate());
            Log.d(TAG, "parseFCI_AID, No parsing exception");
            return;
        }
        catch (ParsingException parsingException) {
            Log.e(TAG, "ParsingException: " + parsingException.getMessage());
            parsingException.printStackTrace();
            return;
        }
    }

    /*
     * Exception decompiling
     */
    public void parseFCI_PPSE() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 4[FORLOOP]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    private static class DirectoryEntry {
        private ByteArray mAid;
        private ByteArray mApplicationLabel;
        private ByteArray mICC;
        private ByteArray mIDD;
        private ByteArray mIIN;
        private ByteArray mLangPref;
        private ByteArray mPDOL;
        private byte mPriorityIndicator;

        public ByteArray getAid() {
            return this.mAid;
        }

        public ByteArray getApplicationLabel() {
            return this.mApplicationLabel;
        }

        public ByteArray getICC() {
            return this.mICC;
        }

        public ByteArray getIDD() {
            return this.mIDD;
        }

        public ByteArray getIIN() {
            return this.mIIN;
        }

        public ByteArray getLangPref() {
            return this.mLangPref;
        }

        public ByteArray getPDOL() {
            return this.mPDOL;
        }

        public byte getPriorityIndicator() {
            return this.mPriorityIndicator;
        }

        public void setAid(ByteArray byteArray) {
            this.mAid = byteArray;
        }

        public void setApplicationLabel(ByteArray byteArray) {
            this.mApplicationLabel = byteArray;
        }

        public void setICC(ByteArray byteArray) {
            this.mICC = byteArray;
        }

        public void setIDD(ByteArray byteArray) {
            this.mIDD = byteArray;
        }

        public void setIIN(ByteArray byteArray) {
            this.mIIN = byteArray;
        }

        public void setLangPref(ByteArray byteArray) {
            this.mLangPref = byteArray;
        }

        public void setPDOL(ByteArray byteArray) {
            this.mPDOL = byteArray;
        }

        public void setPriorityIndicator(byte by) {
            this.mPriorityIndicator = by;
        }
    }

    private static class EntryTLVHandler
    extends TLVHandler {
        private DirectoryEntry entry = new DirectoryEntry();

        private EntryTLVHandler() {
        }

        private boolean checkTagLength(int n2, byte[] arrby, int n3) {
            if (n2 == 0) {
                Log.e(MCFCITemplate.TAG, "checkTagLength Parse tag: tag length is 0");
                return false;
            }
            if (arrby == null) {
                Log.e(MCFCITemplate.TAG, "checkTagLength Parse tag: data is null");
                return false;
            }
            if (arrby.length <= n3) {
                Log.e(MCFCITemplate.TAG, "checkTagLength: Wrong offset value: offset = " + n3 + ", data length = " + arrby.length);
                return false;
            }
            if (arrby.length < n3 + n2) {
                Log.e(MCFCITemplate.TAG, "checkTagLength: Tag offset and length < data length value: offset = " + n3 + ", length = " + n2 + ", data length = " + arrby.length);
                return false;
            }
            return true;
        }

        public DirectoryEntry getDirectoryEntry() {
            return this.entry;
        }

        @Override
        public void parseTag(byte by, int n2, byte[] arrby, int n3) {
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler Parse tag: " + by + ", length: " + n2);
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler Parse tag: " + by + ", length: " + n2 + ", offset: " + n3);
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler Parse tag: " + (by & 255) + ", length: " + n2 + ", offset: " + n3);
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler: checkLength start.");
            if (!this.checkTagLength(n2, arrby, n3)) {
                Log.e(MCFCITemplate.TAG, "EntryTLVHandler: checkLength failed.");
                return;
            }
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler: array.");
            byte[] arrby2 = new byte[n2];
            System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)0, (int)n2);
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler: data: " + baf.getByteArray(arrby, arrby.length).getHexString());
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler: array: " + baf.getByteArray(arrby2, arrby2.length).getHexString());
            switch (by & 255) {
                default: {
                    return;
                }
                case 66: {
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: IIN");
                    this.entry.setIIN(baf.getByteArray(arrby2, arrby2.length));
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: issuer iin: " + this.entry.getIIN().getHexString());
                    return;
                }
                case 79: {
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: Aid");
                    this.entry.setAid(baf.getByteArray(arrby2, arrby2.length));
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: Aid: " + this.entry.getAid().getHexString());
                    return;
                }
                case 80: {
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: Label");
                    this.entry.setApplicationLabel(baf.getByteArray(arrby2, arrby2.length));
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: label: " + this.entry.getApplicationLabel().getHexString());
                    return;
                }
                case 135: 
            }
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler: Priority");
            if (n2 == 1) {
                this.entry.setPriorityIndicator(arrby[n3]);
            }
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler: priority: " + this.entry.getPriorityIndicator());
        }

        @Override
        public void parseTag(short s2, int n2, byte[] arrby, int n3) {
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler Parse long tag: " + s2 + ", length: " + n2);
            if (!this.checkTagLength(n2, arrby, n3)) {
                Log.e(MCFCITemplate.TAG, "EntryTLVHandler: checkLength failed.");
                return;
            }
            byte[] arrby2 = new byte[n2];
            System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)0, (int)n2);
            switch (s2) {
                default: {
                    return;
                }
                case -24776: {
                    this.entry.setPDOL(baf.getByteArray(arrby2, arrby2.length));
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler:  PDOL:" + this.entry.getPDOL().getHexString());
                    return;
                }
                case -16628: {
                    this.entry.setIDD(baf.getByteArray(arrby2, arrby2.length));
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: issuer IDD: " + this.entry.getIDD().getHexString());
                    return;
                }
                case 24405: {
                    this.entry.setICC(baf.getByteArray(arrby2, arrby2.length));
                    Log.d(MCFCITemplate.TAG, "EntryTLVHandler: issuer ICC: " + this.entry.getICC().getHexString());
                    return;
                }
                case 24365: 
            }
            this.entry.setLangPref(baf.getByteArray(arrby2, arrby2.length));
            Log.d(MCFCITemplate.TAG, "EntryTLVHandler: lang pref: " + this.entry.getLangPref().getHexString());
        }

        public void setEntry(DirectoryEntry directoryEntry) {
            this.entry = directoryEntry;
        }
    }

    private class FCIAIDTLVHandler
    extends TLVHandler {
        ByteArray aid = null;

        private FCIAIDTLVHandler() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void parseTag(byte by, int n2, byte[] arrby, int n3) {
            Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler Parse tag: " + (by & 255) + ", length: " + n2 + ", offset: " + n3);
            switch (by & 255) {
                default: {
                    return;
                }
                case 132: {
                    Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler Parse tag: " + (by & 255) + ", length: " + n2);
                    byte[] arrby2 = new byte[n2];
                    System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)0, (int)n2);
                    this.aid = baf.getByteArray(arrby2, arrby2.length);
                    Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler AID: " + this.aid.getHexString());
                    return;
                }
                case 165: 
            }
            try {
                Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler Parse: Template tag");
                EntryTLVHandler entryTLVHandler = new EntryTLVHandler();
                TLVParser.parseTLV(arrby, n3, n2, entryTLVHandler);
                Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler Entry parsed...");
                DirectoryEntry directoryEntry = entryTLVHandler.getDirectoryEntry();
                if (directoryEntry != null) {
                    Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler entry is not null.");
                    if (this.aid == null) {
                        Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler aid is null");
                    } else {
                        Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler aid is not null: " + this.aid.getHexString());
                    }
                    directoryEntry.setAid(this.aid);
                    MCFCITemplate.this.mEntries.add((Object)directoryEntry);
                    return;
                }
            }
            catch (ParsingException parsingException) {
                Log.e(MCFCITemplate.TAG, "ParsingException parsing directory entry: " + parsingException.getMessage());
                parsingException.printStackTrace();
                return;
            }
            Log.d(MCFCITemplate.TAG, "FCIAIDTLVHandler entry is null.");
        }

        @Override
        public void parseTag(short s2, int n2, byte[] arrby, int n3) {
            Log.d(MCFCITemplate.TAG, "Parse long tag: " + s2 + ", length: " + n2);
        }
    }

    private class FCITLVHandler
    extends TLVHandler {
        private FCITLVHandler() {
        }

        /*
         * Exception decompiling
         */
        @Override
        public void parseTag(byte var1_1, int var2_2, byte[] var3_3, int var4_4) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 7[CASE]
            // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
            // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
            // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
            // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
            // org.benf.cfr.reader.entities.g.p(Method.java:396)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
            // org.benf.cfr.reader.entities.d.c(ClassFile.java:773)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:870)
            // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
            // org.benf.cfr.reader.b.a(Driver.java:128)
            // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
            // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
            // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
            // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
            // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
            // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
            // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
            // java.lang.Thread.run(Thread.java:764)
            throw new IllegalStateException("Decompilation failed");
        }

        @Override
        public void parseTag(short s2, int n2, byte[] arrby, int n3) {
            Log.d(MCFCITemplate.TAG, "Parse long tag: " + s2 + ", length: " + n2);
        }
    }

    private class FCITLVHandlerOnlyForTemplate
    extends TLVHandler {
        private FCITLVHandlerOnlyForTemplate() {
        }

        @Override
        public void parseTag(byte by, int n2, byte[] arrby, int n3) {
            Log.d(MCFCITemplate.TAG, "Parse tag: " + by + ", length: " + n2 + ", offset: " + n3);
            switch (by & 255) {
                default: {
                    Log.d(MCFCITemplate.TAG, "FCITLVHandlerOnlyForTemplate Parse invalid tag: " + by + ", length: " + n2);
                    return;
                }
                case 111: 
            }
            try {
                Log.d(MCFCITemplate.TAG, "Parse: Template tag : Invoke FCIAIDTLVHandler now");
                TLVParser.parseTLV(arrby, n3, n2, new FCIAIDTLVHandler());
                return;
            }
            catch (ParsingException parsingException) {
                Log.e(MCFCITemplate.TAG, "ParsingException parsing directory entry: " + parsingException.getMessage());
                parsingException.printStackTrace();
                return;
            }
        }

        @Override
        public void parseTag(short s2, int n2, byte[] arrby, int n3) {
            Log.d(MCFCITemplate.TAG, "Parse long tag: " + s2 + ", length: " + n2);
        }
    }

}

