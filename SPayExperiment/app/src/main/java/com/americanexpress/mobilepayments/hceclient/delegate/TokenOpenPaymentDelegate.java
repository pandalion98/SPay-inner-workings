/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PPSEResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLParser;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;
import java.util.Map;

public class TokenOpenPaymentDelegate
extends OperationDelegate {
    /*
     * Enabled aggressive block sorting
     */
    private void buildAppInfo(DataContext dataContext) {
        List<TagKey> list = TagsMapUtil.getTagList(dataContext.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
        if (list == null || list.isEmpty()) {
            PaymentUtils.setTokenStatus(StateMode.BLOCKED);
        }
        ApplicationInfoManager.initTransient();
        this.createDOLList(dataContext);
        PPSEResponse pPSEResponse = new PPSEResponse();
        if (Operation.OPERATION.getMode().equals((Object)OperationMode.TAP_PAYMENT) && PaymentUtils.checkXPMConfig((byte)4, (byte)8) == Constants.MAGIC_TRUE) {
            pPSEResponse.buildPPSEResponse(false);
        } else {
            pPSEResponse.buildPPSEResponse(true);
        }
        ApplicationInfoManager.setApplcationInfoValue("TR_CUR_PPSE_RES_OBJ", pPSEResponse);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void createDOLList(DataContext dataContext) {
        TagValue tagValue = TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("CDOL_TAG"), true);
        TagValue tagValue2 = TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("CDOL_TAG"), true);
        List<DOLTag> list = tagValue != null ? DOLParser.parseDOL(tagValue.getValue()) : null;
        List<DOLTag> list2 = null;
        if (tagValue2 != null) {
            list2 = DOLParser.parseDOL(tagValue2.getValue());
        }
        ApplicationInfoManager.setApplcationInfoValue("MS_CDOL_LIST", list);
        ApplicationInfoManager.setApplcationInfoValue("EMV_CDOL_LIST", list2);
    }

    /*
     * Exception decompiling
     */
    @Override
    public void doOperation() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 5[CATCHBLOCK]
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

    protected void invokeOpen() {
        byte[] arrby = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), "HCECLIENTSC_DATA_CONTEXT").getBytes();
        this.checkSCStatus(new SecureComponentImpl().open(arrby));
    }

    protected byte[] unwrapSDKContext() {
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        String string = Operation.OPERATION.getTokenRefId();
        String string2 = new DataStashImpl().getDataFromStorage(string, "HCECLIENT_DATA_CONTEXT");
        Utility.largeLog("core-hceclient", new String(string2));
        byte[] arrby = string2.getBytes();
        byte[] arrby2 = new byte[1024 + arrby.length];
        this.checkSCStatus(secureComponentImpl.unwrap(1, arrby, arrby.length, arrby2, arrby2.length));
        if (secureComponentImpl.isRetryExecuted()) {
            arrby2 = secureComponentImpl.getDestBuffer();
        }
        return arrby2;
    }
}

