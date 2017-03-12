package com.samsung.android.spayfw.payprovider.amex;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.model.EndTransactionResponse;
import com.americanexpress.sdkmodulelib.model.ProcessInAppPaymentResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.TokenStatusResponse;
import com.americanexpress.sdkmodulelib.payment.NFCPaymentProviderProxy;
import com.americanexpress.sdkmodulelib.payment.NFCPaymentProviderProxyImpl;
import com.americanexpress.sdkmodulelib.payment.TokenDataManager;
import com.americanexpress.sdkmodulelib.payment.TokenDataManagerImpl;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.PaymentFrameworkRequester;
import com.samsung.android.spayfw.fraud.FraudDataProvider;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.TokenReplenishAlarm;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.amex.AmexUtils.LupcMetaData;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.DevicePublicCerts;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.EphemeralKeyInfo;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.InAppTZTxnInfo;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.ProcessRequestDataResponse;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.ProcessTokenDataResponse;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardSchema;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;
import org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.payprovider.amex.a */
public class AmexPayProvider extends PaymentNetworkProvider {
    private EnrollCardInfo kQ;
    private BillingInfo mBillingInfo;
    private Context mContext;
    private AmexTAController pg;
    private NFCPaymentProviderProxy ph;
    private TokenDataManager pi;
    private ProviderTokenKey pj;
    private CertificateInfo pl;
    private CertificateInfo pm;
    private boolean pn;
    private boolean po;
    private SharedPreferences pp;
    private String pq;
    private boolean pr;
    private PaymentFrameworkRequester ps;

    protected com.samsung.android.spayfw.payprovider.ProviderResponseData createToken(java.lang.String r14, com.samsung.android.spayfw.payprovider.ProviderRequestData r15, int r16) {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0293 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:58)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r13 = this;
        r8 = new com.samsung.android.spayfw.payprovider.e;
        r8.<init>();
        r0 = 0;
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r10 = r15.ch();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        if (r10 != 0) goto L_0x001e;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x000f:
        r0 = -4;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x001c;
    L_0x0017:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x001c:
        r0 = r8;
    L_0x001d:
        return r0;
    L_0x001e:
        r0 = "secureTokenData";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r10.getAsJsonObject(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        if (r0 != 0) goto L_0x003c;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x0026:
        r0 = "AmexPayProvider";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "secureTokenData is null";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = -4;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x003a;
    L_0x0035:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x003a:
        r0 = r8;
        goto L_0x001d;
    L_0x003c:
        r1 = "initializationVector";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r0.get(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r1.getAsString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "encryptedTokenData";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r0.get(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = r1.getAsString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "encryptedTokenDataHMAC";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r0.get(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r4 = r1.getAsString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "cloudPublicKeyCert";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.get(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r0.getAsString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r9 = 0;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = "tokenStatus";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r10.get(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.getAsString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r5 = com.samsung.android.spayfw.payprovider.amex.AmexPayProvider.au(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        if (r5 != 0) goto L_0x008b;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x0075:
        r0 = "AmexPayProvider";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "Token Status is NULL";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = -2;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x0089;
    L_0x0084:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x0089:
        r0 = r8;
        goto L_0x001d;
    L_0x008b:
        r0 = r13.kQ;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        if (r0 == 0) goto L_0x00af;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x008f:
        r0 = r13.kQ;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.getWalletId();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r6 = r0;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x0096:
        if (r6 != 0) goto L_0x00bd;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x0098:
        r0 = "AmexPayProvider";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "Wallet Id is NULL";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = -2;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x00ac;
    L_0x00a7:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x00ac:
        r0 = r8;
        goto L_0x001d;
    L_0x00af:
        r0 = r13.mContext;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = com.samsung.android.spayfw.core.ConfigurationManager.m581h(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r6 = "CONFIG_WALLET_ID";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.getConfig(r6);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r6 = r0;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        goto L_0x0096;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x00bd:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0.<init>();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r0.append(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.pm;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        if (r0 != 0) goto L_0x012f;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x00ca:
        r0 = "";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x00cc:
        r0 = r1.append(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r0.toString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.pp;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r7.<init>();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r7 = r7.append(r14);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r11 = "_keys";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r7 = r7.append(r11);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r7 = r7.toString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r11 = 0;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r7 = r0.getString(r7, r11);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.pp;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.edit();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r11.<init>();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r11 = r11.append(r14);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r12 = "_keys";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r11 = r11.toString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.remove(r11);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0.apply();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.pg;	 Catch:{ AmexTAException -> 0x0136 }
        r6 = r6.getBytes();	 Catch:{ AmexTAException -> 0x0136 }
        r6 = com.samsung.android.spayfw.utils.Utils.encodeHex(r6);	 Catch:{ AmexTAException -> 0x0136 }
        r0 = r0.m782a(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ AmexTAException -> 0x0136 }
        r7 = r0;
    L_0x011d:
        if (r7 != 0) goto L_0x0146;
    L_0x011f:
        r0 = -2;
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x012c;
    L_0x0127:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x012c:
        r0 = r8;
        goto L_0x001d;
    L_0x012f:
        r0 = r13.pm;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.getContent();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        goto L_0x00cc;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x0136:
        r0 = move-exception;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = -2;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.setErrorCode(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "AmexPayProvider";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r0.getMessage();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r7 = r9;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        goto L_0x011d;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x0146:
        r0 = r13.pi;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r7.eAPDUBlob;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = r7.eNFCLUPCBlob;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r4 = r7.eOtherLUPCBlob;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r5 = r7.eMetadataBlob;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r6 = r7.lupcMetadataBlob;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r14;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.updateTokenData(r1, r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r0.getReasonCode();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = "00";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = r1.equals(r2);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        if (r1 != 0) goto L_0x01ab;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
    L_0x0163:
        r1 = "AmexPayProvider";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2.<init>();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = "mAmexNfcPaymentProviderProxy.updateTokenData failed Reason Code: ";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = r0.getReasonCode();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = "Detail Code = ";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = r0.getDetailCode();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = "Detail Message = ";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.getDetailMessage();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r2.append(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = -2;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x01a8;
    L_0x01a3:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x01a8:
        r0 = r8;
        goto L_0x001d;
    L_0x01ab:
        r1 = "TAG";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2.<init>();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = "AmexTokenDataManager status ";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = r0.getReasonCode();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = " : ";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.getDetailCode();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r2.append(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = new com.google.gson.JsonObject;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2.<init>();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.pg;	 Catch:{ AmexTAException -> 0x0252 }
        r0 = r0.cu();	 Catch:{ AmexTAException -> 0x0252 }
        r1 = r0.deviceCertificate;	 Catch:{ AmexTAException -> 0x0252 }
        if (r1 == 0) goto L_0x01eb;	 Catch:{ AmexTAException -> 0x0252 }
    L_0x01e4:
        r1 = "devicePublicKeyCert";	 Catch:{ AmexTAException -> 0x0252 }
        r3 = r0.deviceCertificate;	 Catch:{ AmexTAException -> 0x0252 }
        r2.addProperty(r1, r3);	 Catch:{ AmexTAException -> 0x0252 }
    L_0x01eb:
        r1 = r0.deviceSigningCertificate;	 Catch:{ AmexTAException -> 0x0252 }
        if (r1 == 0) goto L_0x01f6;	 Catch:{ AmexTAException -> 0x0252 }
    L_0x01ef:
        r1 = "deviceSigningPublicKeyCert";	 Catch:{ AmexTAException -> 0x0252 }
        r0 = r0.deviceSigningCertificate;	 Catch:{ AmexTAException -> 0x0252 }
        r2.addProperty(r1, r0);	 Catch:{ AmexTAException -> 0x0252 }
    L_0x01f6:
        r0 = "";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = "encryptionParameters";	 Catch:{ Exception -> 0x0275 }
        r1 = r10.get(r1);	 Catch:{ Exception -> 0x0275 }
        r1 = r1.getAsString();	 Catch:{ Exception -> 0x0275 }
        r3 = "encryptedData";	 Catch:{ Exception -> 0x0275 }
        r3 = r10.get(r3);	 Catch:{ Exception -> 0x0275 }
        r3 = r3.getAsString();	 Catch:{ Exception -> 0x0275 }
        if (r1 == 0) goto L_0x0222;	 Catch:{ Exception -> 0x0275 }
    L_0x020e:
        r4 = r1.isEmpty();	 Catch:{ Exception -> 0x0275 }
        if (r4 != 0) goto L_0x0222;	 Catch:{ Exception -> 0x0275 }
    L_0x0214:
        if (r3 == 0) goto L_0x0222;	 Catch:{ Exception -> 0x0275 }
    L_0x0216:
        r4 = r3.isEmpty();	 Catch:{ Exception -> 0x0275 }
        if (r4 != 0) goto L_0x0222;
    L_0x021c:
        r4 = r13.pg;	 Catch:{ AmexTAException -> 0x0263 }
        r0 = r4.m788o(r3, r1);	 Catch:{ AmexTAException -> 0x0263 }
    L_0x0222:
        r1 = new com.samsung.android.spayfw.payprovider.f;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1.<init>(r14);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.setProviderTokenKey(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1 = new com.google.gson.JsonObject;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1.<init>();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = "secureTokenDataSignature";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r4 = r7.responseDataSignature;	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1.addProperty(r3, r4);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r3 = "secureDeviceData";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1.add(r3, r2);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = "accountRefId";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r1.addProperty(r2, r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r8.m1057b(r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r13.av(r14);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x024f;
    L_0x024a:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x024f:
        r0 = r8;
        goto L_0x001d;
    L_0x0252:
        r0 = move-exception;
        r0 = -6;
        r8.setErrorCode(r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x0260;
    L_0x025b:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x0260:
        r0 = r8;
        goto L_0x001d;
    L_0x0263:
        r1 = move-exception;
        r3 = "AmexPayProvider";	 Catch:{ Exception -> 0x0275 }
        r4 = r1.getMessage();	 Catch:{ Exception -> 0x0275 }
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r1);	 Catch:{ Exception -> 0x0275 }
        r1 = "AmexPayProvider";	 Catch:{ Exception -> 0x0275 }
        r3 = "encryptedData is invalid";	 Catch:{ Exception -> 0x0275 }
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r3);	 Catch:{ Exception -> 0x0275 }
        goto L_0x0222;
    L_0x0275:
        r1 = move-exception;
        r3 = "AmexPayProvider";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r4 = r1.getMessage();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r1);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        goto L_0x0222;
    L_0x0280:
        r0 = move-exception;
        r1 = "AmexPayProvider";	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r2 = r0.getMessage();	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);	 Catch:{ Exception -> 0x0280, all -> 0x029a }
        r0 = r13.kQ;
        if (r0 == 0) goto L_0x0293;
    L_0x028e:
        r0 = r13.kQ;
        r0.decrementRefCount();
    L_0x0293:
        r0 = -2;
        r8.setErrorCode(r0);
        r0 = r8;
        goto L_0x001d;
    L_0x029a:
        r0 = move-exception;
        r1 = r13.kQ;
        if (r1 == 0) goto L_0x02a4;
    L_0x029f:
        r1 = r13.kQ;
        r1.decrementRefCount();
    L_0x02a4:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.a.createToken(java.lang.String, com.samsung.android.spayfw.payprovider.c, int):com.samsung.android.spayfw.payprovider.e");
    }

    public static final JsonObject m764c(JsonObject jsonObject) {
        TreeMap treeMap = new TreeMap();
        for (Entry entry : jsonObject.entrySet()) {
            Object obj = (JsonElement) entry.getValue();
            if (((JsonElement) entry.getValue()).isJsonObject()) {
                obj = AmexPayProvider.m764c(obj.getAsJsonObject());
            }
            treeMap.put(entry.getKey(), obj);
        }
        JsonObject jsonObject2 = new JsonObject();
        for (Entry entry2 : treeMap.entrySet()) {
            jsonObject2.add((String) entry2.getKey(), (JsonElement) entry2.getValue());
        }
        return jsonObject2;
    }

    private static final short m762a(byte[] bArr, short s) {
        return (short) ((((short) bArr[s]) << 8) + (((short) bArr[(short) (s + 1)]) & GF2Field.MASK));
    }

    private static String au(String str) {
        if (str == null) {
            Log.m286e("AmexPayProvider", "Token Status received is null");
            return null;
        } else if (HCEClientConstants.API_INDEX_TOKEN_OPEN.equals(str)) {
            return HCEClientConstants.API_INDEX_TOKEN_OPEN;
        } else {
            if (HCEClientConstants.API_INDEX_TOKEN_PERSO.equals(str)) {
                return Constants.SERVICE_CODE_LENGTH;
            }
            if (Constants.SERVICE_CODE_LENGTH.equals(str)) {
                return HCEClientConstants.API_INDEX_TOKEN_PERSO;
            }
            Log.m286e("AmexPayProvider", "Token Status translation failure");
            return null;
        }
    }

    public AmexPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.pj = null;
        this.pn = false;
        this.po = false;
        this.pr = false;
        if (context == null) {
            Log.m286e("AmexPayProvider", "context is null");
            return;
        }
        this.mContext = context;
        this.mTAController = AmexTAController.m780C(this.mContext);
        this.pg = (AmexTAController) this.mTAController;
        this.pi = new TokenDataManagerImpl();
        this.ph = new NFCPaymentProviderProxyImpl();
        this.pp = this.mContext.getSharedPreferences("AmexStorage", 0);
    }

    public void av(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.m286e("AmexPayProvider", "Error handleReplenishment invalid trTokenId ");
            return;
        }
        Log.m285d("AmexPayProvider", "replenishIfRequired id " + str);
        TokenStatusResponse tokenStatus = this.pi.getTokenStatus(str);
        if (tokenStatus == null) {
            Log.m286e("AmexPayProvider", "Error handleReplenishment TokenStatusResponse object is null! ");
            return;
        }
        Log.m285d("AmexPayProvider", "AmexTokenDataManager token status : " + tokenStatus.getTokenStatus());
        Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenStatus.getTokenDataStatus().getReasonCode() + " : " + tokenStatus.getTokenDataStatus().getDetailCode());
        if (tokenStatus.getTokenStatus() != null && (tokenStatus.getTokenStatus().equals(Constants.SERVICE_CODE_LENGTH) || tokenStatus.getTokenStatus().equals(HCEClientConstants.API_INDEX_TOKEN_PERSO))) {
            Log.m286e("AmexPayProvider", "Not Replenishing as Token is Suspended or Pending.");
        } else if (ax(str)) {
            Log.m287i("AmexPayProvider", "Replenish Retry Alarm Set");
        } else {
            TokenDataResponse tokenData = this.pi.getTokenData(str);
            if (tokenData == null || tokenData.getTokenDataStatus() == null) {
                Log.m286e("AmexPayProvider", "Error handleReplenishment TokenDataResponse object is null! ");
                return;
            }
            TokenDataStatus tokenDataStatus = tokenData.getTokenDataStatus();
            Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
            if (tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                String lupcMetadataBlob = tokenData.getLupcMetadataBlob();
                long am = Utils.am(this.mContext) / 1000;
                Log.m285d("AmexPayProvider", "currentTime : " + am);
                LupcMetaData aA = AmexUtils.aA(lupcMetadataBlob);
                if (aA == null) {
                    Log.m286e("AmexPayProvider", "Error in handleReplenishment lupcMetaData is null!");
                    return;
                }
                Log.m285d("AmexPayProvider", "nfcLupcCount : " + aA.nfcLupcCount);
                Log.m285d("AmexPayProvider", "nfcLupcExpiry : " + aA.nfcLupcExpiry);
                ProviderTokenKey providerTokenKey = new ProviderTokenKey(str);
                providerTokenKey.setTrTokenId(str);
                if (aA.nfcLupcCount < 3) {
                    Log.m287i("AmexPayProvider", "replenish token");
                    replenishAlarmExpired();
                } else if (aA.nfcLupcExpiry - am < 86400) {
                    Log.m287i("AmexPayProvider", "replenish token");
                    replenishAlarmExpired();
                } else {
                    TokenReplenishAlarm.m1070a(this.mContext, (aA.nfcLupcExpiry - 86400) * 1000, providerTokenKey);
                }
            }
        }
    }

    protected void init() {
    }

    public void delete() {
        if (this.kQ != null) {
            this.kQ.decrementRefCount();
        }
    }

    protected CertificateInfo[] getDeviceCertificates() {
        CertificateInfo[] certificateInfoArr = null;
        try {
            DevicePublicCerts cu = this.pg.cu();
            if (!(cu.deviceCertificate == null || cu.deviceSigningCertificate == null)) {
                certificateInfoArr = new CertificateInfo[2];
                certificateInfoArr[0] = new CertificateInfo();
                certificateInfoArr[0].setUsage(CertificateInfo.CERT_USAGE_CA);
                certificateInfoArr[0].setAlias("DeviceCert");
                certificateInfoArr[0].setContent(cu.deviceCertificate);
                certificateInfoArr[1] = new CertificateInfo();
                certificateInfoArr[1].setUsage(CertificateInfo.CERT_USAGE_VER);
                certificateInfoArr[1].setAlias("Amex-DeviceSigningCert");
                certificateInfoArr[1].setContent(cu.deviceSigningCertificate);
            }
        } catch (Throwable e) {
            Log.m284c("AmexPayProvider", e.getMessage(), e);
        }
        return certificateInfoArr;
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        String str = "tsp_rsa";
        String str2 = "tsp_ecc";
        for (int i = 0; i < certificateInfoArr.length; i++) {
            if (certificateInfoArr[i].getAlias().equals(str)) {
                this.pl = certificateInfoArr[i];
            } else if (certificateInfoArr[i].getAlias().equals(str2)) {
                this.pm = certificateInfoArr[i];
            }
        }
        return true;
    }

    protected ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        try {
            if (this.pl == null) {
                providerRequestData.setErrorCode(0);
                providerRequestData.m822a(new JsonObject());
                providerRequestData.m823e(m765b(enrollCardInfo));
                return providerRequestData;
            }
            providerRequestData.setErrorCode(0);
            if (enrollCardInfo == null) {
                providerRequestData.setErrorCode(-4);
                return providerRequestData;
            }
            ProcessRequestDataResponse b;
            JsonElement jsonObject = new JsonObject();
            if (enrollCardInfo instanceof EnrollCardPanInfo) {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) enrollCardInfo;
                if (enrollCardPanInfo.getPAN() == null) {
                    providerRequestData.setErrorCode(-4);
                    return providerRequestData;
                } else if (!(enrollCardPanInfo.getPAN() == null || enrollCardPanInfo.getPAN().isEmpty())) {
                    jsonObject.addProperty("accountNumber", enrollCardPanInfo.getPAN());
                }
            } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
                EnrollCardReferenceInfo enrollCardReferenceInfo = (EnrollCardReferenceInfo) enrollCardInfo;
                if (!(enrollCardReferenceInfo.getReferenceType() == null || !enrollCardReferenceInfo.getReferenceType().equals(EnrollCardReferenceInfo.CARD_REF_TYPE_ID) || enrollCardReferenceInfo.getExtraEnrollData() == null)) {
                    jsonObject.addProperty("accountRefId", enrollCardReferenceInfo.getExtraEnrollData().getString(EnrollCardReferenceInfo.CARD_REFERENCE_ID));
                }
            }
            if (enrollCardInfo.getCardEntryMode() != null) {
                if (enrollCardInfo.getCardEntryMode().equals(EnrollCardInfo.CARD_ENTRY_MODE_MANUAL)) {
                    jsonObject.addProperty("accountInputMethod", HCEClientConstants.API_INDEX_TOKEN_OPEN);
                } else if (enrollCardInfo.getCardEntryMode().equals(EnrollCardInfo.CARD_ENTRY_MODE_OCR)) {
                    jsonObject.addProperty("accountInputMethod", Constants.SERVICE_CODE_LENGTH);
                } else {
                    jsonObject.addProperty("accountInputMethod", HCEClientConstants.API_INDEX_TOKEN_PERSO);
                }
                if (enrollCardInfo.getCardEntryMode().equals(EnrollCardInfo.CARD_ENTRY_MODE_FILE)) {
                    jsonObject.addProperty("onFileIndicator", Boolean.valueOf(true));
                } else {
                    jsonObject.addProperty("onFileIndicator", Boolean.valueOf(false));
                }
            }
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.add("accountData", jsonObject);
            JsonElement jsonObject3 = new JsonObject();
            try {
                DevicePublicCerts cu = this.pg.cu();
                if (cu.deviceEncryptionCertificate != null) {
                    jsonObject3.addProperty("deviceEncryptionPublicKeyCert", cu.deviceEncryptionCertificate);
                }
                if (cu.deviceCertificate != null) {
                    jsonObject3.addProperty("devicePublicKeyCert", cu.deviceCertificate);
                }
                if (cu.deviceSigningCertificate != null) {
                    jsonObject3.addProperty("deviceSigningPublicKeyCert", cu.deviceSigningCertificate);
                }
            } catch (Exception e) {
                providerRequestData.setErrorCode(-2);
                e.printStackTrace();
            }
            try {
                b = this.pg.m787b(this.pl.getContent(), jsonObject2.toString(), AmexPayProvider.m764c(jsonObject).toString() + AmexPayProvider.m764c(jsonObject3).toString());
            } catch (Throwable e2) {
                providerRequestData.setErrorCode(-2);
                Log.m284c("AmexPayProvider", e2.getMessage(), e2);
                b = null;
            }
            if (b == null) {
                providerRequestData.setErrorCode(-2);
                return providerRequestData;
            }
            JsonObject jsonObject4 = new JsonObject();
            jsonObject4.addProperty("encryptedData", b.encryptedRequestData);
            jsonObject4.add("secureDeviceData", jsonObject3);
            jsonObject4.addProperty("encryptionParameters", b.encryptionParams);
            jsonObject4.addProperty("accountDataSignature", b.requestDataSignature);
            providerRequestData.m822a(jsonObject4);
            this.mBillingInfo = billingInfo;
            enrollCardInfo.incrementRefCount();
            this.kQ = enrollCardInfo;
            providerRequestData.m823e(m765b(enrollCardInfo));
            return providerRequestData;
        } catch (Throwable e22) {
            Log.m284c("AmexPayProvider", e22.getMessage(), e22);
            providerRequestData.setErrorCode(-2);
            return providerRequestData;
        }
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        EphemeralKeyInfo cv;
        Exception e;
        String str;
        JsonObject jsonObject;
        JsonElement jsonObject2;
        ProcessRequestDataResponse processRequestDataResponse = null;
        ProviderRequestData providerRequestData = new ProviderRequestData();
        try {
            if (this.pl == null) {
                providerRequestData.setErrorCode(0);
                providerRequestData.m822a(new JsonObject());
                return providerRequestData;
            }
            providerRequestData.setErrorCode(0);
            if (this.kQ == null || this.mBillingInfo == null) {
                providerRequestData.setErrorCode(-4);
                return providerRequestData;
            }
            JsonElement jsonObject3 = new JsonObject();
            JsonElement jsonObject4 = new JsonObject();
            if (this.mBillingInfo.getCity() != null) {
                jsonObject4.addProperty("city", this.mBillingInfo.getCity());
            }
            if (this.mBillingInfo.getCountry() != null) {
                jsonObject4.addProperty("country", this.mBillingInfo.getCountry());
            }
            if (this.mBillingInfo.getStreet1() != null) {
                jsonObject4.addProperty("addressLine1", this.mBillingInfo.getStreet1());
            }
            if (this.mBillingInfo.getStreet2() != null) {
                jsonObject4.addProperty("addressLine2", this.mBillingInfo.getStreet2());
            }
            if (this.mBillingInfo.getState() != null) {
                jsonObject4.addProperty("state", this.mBillingInfo.getState());
            }
            if (this.mBillingInfo.getZip() != null) {
                jsonObject4.addProperty("zipCode", this.mBillingInfo.getZip());
            }
            jsonObject3.add("billingAddress", jsonObject4);
            JsonElement jsonObject5 = new JsonObject();
            if (this.kQ instanceof EnrollCardPanInfo) {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) this.kQ;
                jsonObject5.addProperty("cardExpiry", enrollCardPanInfo.getExpMonth() + "/" + enrollCardPanInfo.getExpYear());
                jsonObject5.addProperty("cid", enrollCardPanInfo.getCVV());
                jsonObject3.add("cardVerificationValues", jsonObject5);
            }
            jsonObject3.addProperty("cardholderName", this.kQ.getName());
            JsonObject jsonObject6 = new JsonObject();
            jsonObject6.add("accountData", jsonObject3);
            JsonElement jsonObject7 = new JsonObject();
            try {
                DevicePublicCerts cu = this.pg.cu();
                cv = this.pg.cv();
                try {
                    jsonObject7.addProperty("clientAPIVersion", this.pi.getClientVersion());
                    if (cu.deviceCertificate != null) {
                        jsonObject7.addProperty("devicePublicKeyCert", cu.deviceCertificate);
                    }
                    if (cu.deviceSigningCertificate != null) {
                        jsonObject7.addProperty("deviceSigningPublicKeyCert", cu.deviceSigningCertificate);
                    }
                    if (cu.deviceEncryptionCertificate != null) {
                        jsonObject7.addProperty("deviceEncryptionPublicKeyCert", cu.deviceEncryptionCertificate);
                    }
                    if (cv != null) {
                        jsonObject7.addProperty("ephemeralPublicKey", cv.ephemeralPublicKey);
                    }
                } catch (Exception e2) {
                    e = e2;
                    providerRequestData.setErrorCode(-2);
                    e.printStackTrace();
                    Log.m288m("AmexPayProvider", "encryptedData.toString() " + jsonObject6.toString());
                    str = AmexPayProvider.m764c(jsonObject3).toString() + AmexPayProvider.m764c(jsonObject7).toString();
                    Log.m288m("AmexPayProvider", "dataToBeSigned " + str);
                    processRequestDataResponse = this.pg.m787b(this.pl.getContent(), jsonObject6.toString(), str);
                    if (processRequestDataResponse == null) {
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("encryptedData", processRequestDataResponse.encryptedRequestData);
                        jsonObject.add("secureDeviceData", jsonObject7);
                        jsonObject.addProperty("encryptionParameters", processRequestDataResponse.encryptionParams);
                        jsonObject.addProperty("accountDataSignature", processRequestDataResponse.requestDataSignature);
                        jsonObject2 = new JsonObject();
                        jsonObject2.addProperty("imei", DeviceInfo.getDeviceImei(this.mContext));
                        jsonObject2.addProperty("serial", DeviceInfo.getDeviceSerialNumber());
                        jsonObject2.addProperty("msisdn", DeviceInfo.getMsisdn(this.mContext));
                        jsonObject.add("deviceData", jsonObject2);
                        providerRequestData.m822a(jsonObject);
                        providerRequestData.m823e(m761a(provisionTokenInfo));
                        if (cv != null) {
                            this.pq = cv.encryptedEphemeralPrivateKey;
                        }
                        return providerRequestData;
                    }
                    providerRequestData.setErrorCode(-2);
                    return providerRequestData;
                }
            } catch (Exception e3) {
                e = e3;
                cv = processRequestDataResponse;
                providerRequestData.setErrorCode(-2);
                e.printStackTrace();
                Log.m288m("AmexPayProvider", "encryptedData.toString() " + jsonObject6.toString());
                str = AmexPayProvider.m764c(jsonObject3).toString() + AmexPayProvider.m764c(jsonObject7).toString();
                Log.m288m("AmexPayProvider", "dataToBeSigned " + str);
                processRequestDataResponse = this.pg.m787b(this.pl.getContent(), jsonObject6.toString(), str);
                if (processRequestDataResponse == null) {
                    providerRequestData.setErrorCode(-2);
                    return providerRequestData;
                }
                jsonObject = new JsonObject();
                jsonObject.addProperty("encryptedData", processRequestDataResponse.encryptedRequestData);
                jsonObject.add("secureDeviceData", jsonObject7);
                jsonObject.addProperty("encryptionParameters", processRequestDataResponse.encryptionParams);
                jsonObject.addProperty("accountDataSignature", processRequestDataResponse.requestDataSignature);
                jsonObject2 = new JsonObject();
                jsonObject2.addProperty("imei", DeviceInfo.getDeviceImei(this.mContext));
                jsonObject2.addProperty("serial", DeviceInfo.getDeviceSerialNumber());
                jsonObject2.addProperty("msisdn", DeviceInfo.getMsisdn(this.mContext));
                jsonObject.add("deviceData", jsonObject2);
                providerRequestData.m822a(jsonObject);
                providerRequestData.m823e(m761a(provisionTokenInfo));
                if (cv != null) {
                    this.pq = cv.encryptedEphemeralPrivateKey;
                }
                return providerRequestData;
            }
            try {
                Log.m288m("AmexPayProvider", "encryptedData.toString() " + jsonObject6.toString());
                str = AmexPayProvider.m764c(jsonObject3).toString() + AmexPayProvider.m764c(jsonObject7).toString();
                Log.m288m("AmexPayProvider", "dataToBeSigned " + str);
                processRequestDataResponse = this.pg.m787b(this.pl.getContent(), jsonObject6.toString(), str);
            } catch (Throwable e4) {
                providerRequestData.setErrorCode(-2);
                Log.m284c("AmexPayProvider", e4.getMessage(), e4);
            }
            if (processRequestDataResponse == null) {
                providerRequestData.setErrorCode(-2);
                return providerRequestData;
            }
            jsonObject = new JsonObject();
            jsonObject.addProperty("encryptedData", processRequestDataResponse.encryptedRequestData);
            jsonObject.add("secureDeviceData", jsonObject7);
            jsonObject.addProperty("encryptionParameters", processRequestDataResponse.encryptionParams);
            jsonObject.addProperty("accountDataSignature", processRequestDataResponse.requestDataSignature);
            jsonObject2 = new JsonObject();
            jsonObject2.addProperty("imei", DeviceInfo.getDeviceImei(this.mContext));
            jsonObject2.addProperty("serial", DeviceInfo.getDeviceSerialNumber());
            jsonObject2.addProperty("msisdn", DeviceInfo.getMsisdn(this.mContext));
            jsonObject.add("deviceData", jsonObject2);
            providerRequestData.m822a(jsonObject);
            providerRequestData.m823e(m761a(provisionTokenInfo));
            if (cv != null) {
                this.pq = cv.encryptedEphemeralPrivateKey;
            }
            return providerRequestData;
        } catch (Throwable e5) {
            Log.m284c("AmexPayProvider", e5.getMessage(), e5);
            providerRequestData.setErrorCode(-2);
            return providerRequestData;
        }
    }

    public SelectCardResult selectCard() {
        Log.m285d("AmexPayProvider", "selectCard");
        if (this.mProviderTokenKey == null) {
            return null;
        }
        if (aw(this.mProviderTokenKey.cn())) {
            byte[] nonce;
            if (this.pn) {
                Log.m286e("AmexPayProvider", "Error: Select Card called before previous Payment did not complete. This must never happen");
                cr();
            }
            try {
                this.pg.initializeSecuritySetup();
                nonce = this.pg.getNonce(32);
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                nonce = null;
            }
            if (nonce == null) {
                Log.m286e("AmexPayProvider", "Error: getNonce returned null");
                return null;
            }
            SelectCardResult selectCardResult = new SelectCardResult(AmexTAController.ct().getTAInfo().getTAId(), nonce);
            this.pj = this.mProviderTokenKey;
            return selectCardResult;
        }
        Log.m286e("AmexPayProvider", "Error: Can not pay since LUPC reached zero or token status not active");
        return null;
    }

    protected boolean authenticateTransaction(SecuredObject securedObject) {
        Log.m285d("AmexPayProvider", "authenticateTransaction");
        try {
            return this.pg.authenticateTransaction(securedObject.getSecureObjectData());
        } catch (Throwable e) {
            Log.m284c("AmexPayProvider", e.getMessage(), e);
            return false;
        }
    }

    protected void clearCard() {
    }

    protected boolean prepareMstPay() {
        Log.m285d("AmexPayProvider", "prepareMstPay");
        if (this.pn || cq()) {
            if (!this.po) {
                this.po = true;
                TokenDataStatus processOther = this.ph.processOther();
                if (!processOther.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                    Log.m286e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.processOther failed Reason Code: " + processOther.getReasonCode() + "Detail Code = " + processOther.getDetailCode() + "Detail Message = " + processOther.getDetailMessage());
                    cr();
                    return false;
                }
            }
            return true;
        }
        Log.m286e("AmexPayProvider", "Error: startPayment failed");
        return false;
    }

    protected void interruptMstPay() {
    }

    protected void stopMstPay(boolean z) {
        Log.m285d("AmexPayProvider", "stopMstPay: stop amex mst pay");
        cr();
        this.po = false;
    }

    public boolean prepareNfcPay() {
        Log.m285d("AmexPayProvider", "prepareNfcPay");
        if (this.pn || cq()) {
            return true;
        }
        Log.m286e("AmexPayProvider", "Start Payment Failed");
        return false;
    }

    public byte[] handleApdu(byte[] bArr, Bundle bundle) {
        byte[] bArr2 = null;
        long currentTimeMillis = System.currentTimeMillis();
        Log.m287i("AmexPayProvider", "handleApdu: start: " + currentTimeMillis);
        if (!this.pn) {
            Log.m286e("AmexPayProvider", "Error: prepareMstPay must never happen when there is already a pending MST");
        } else if (bArr == null) {
            Log.m286e("AmexPayProvider", "Error: apduBuffer received is NULL");
        } else {
            Log.m288m("AmexPayProvider", "HandlAPDU - Request = " + Utils.encodeHex(bArr));
            bArr2 = this.ph.generateAPDU(bArr).getApduBytes();
            if (AmexPayProvider.m762a(bArr, (short) 0) == ISO7816.GENERATE_AC && bArr2 != null) {
                byte[] bArr3 = new byte[]{bArr2[bArr2.length - 2], bArr2[bArr2.length - 1]};
                short s = (short) (((short) bArr3[1]) + (((short) bArr3[0]) * SkeinMac.SKEIN_256));
                if (s == ISO7816.SW_NO_ERROR) {
                    Log.m285d("AmexPayProvider", "set amex nfc payment to true");
                    this.pr = true;
                } else {
                    Log.m285d("AmexPayProvider", "amex nfc payment response code: " + s);
                }
                Log.m288m("AmexPayProvider", "HandlAPDU - Response = " + Utils.encodeHex(bArr2));
            }
            long currentTimeMillis2 = System.currentTimeMillis();
            Log.m287i("AmexPayProvider", "handleApdu: end: " + currentTimeMillis2 + "Time Taken = " + ((currentTimeMillis2 - currentTimeMillis) + 1));
        }
        return bArr2;
    }

    protected Bundle stopNfcPay(int i) {
        Log.m285d("AmexPayProvider", "stopNfcPay");
        if (this.pn) {
            short s;
            if (this.pr) {
                s = (short) 2;
            } else if (i == 4) {
                this.pr = false;
                Bundle bundle = new Bundle();
                bundle.putShort("nfcApduErrorCode", (short) 4);
                return bundle;
            } else {
                s = (short) 3;
            }
            Log.m285d("AmexPayProvider", "stopNfcPay: spayfw reason : " + i + " sdk iso ret = " + cr());
            this.pr = false;
            Bundle bundle2 = new Bundle();
            bundle2.putShort("nfcApduErrorCode", s);
            return bundle2;
        }
        Log.m285d("AmexPayProvider", "Stop NFC Pay called when payment is not in progress");
        this.pr = false;
        bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", (short) 1);
        return bundle;
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        byte[] bArr = null;
        Log.m287i("AmexPayProvider", "generateInAppPaymentPayload: start: " + System.currentTimeMillis());
        if (this.pn) {
            Log.m286e("AmexPayProvider", "Error: generateInAppPaymentPayload is called when payment is already in progress");
        } else if (cq()) {
            String valueOf = String.valueOf(Utils.am(this.mContext));
            InAppTZTxnInfo inAppTZTxnInfo = new InAppTZTxnInfo();
            inAppTZTxnInfo.txnAttributes = new HashMap();
            inAppTZTxnInfo.txnAttributes.put(IdvMethod.EXTRA_AMOUNT, inAppDetailedTransactionInfo.getAmount());
            inAppTZTxnInfo.txnAttributes.put("currency_code", inAppDetailedTransactionInfo.getCurrencyCode());
            inAppTZTxnInfo.txnAttributes.put("utc", valueOf);
            if (!(inAppDetailedTransactionInfo.getCardholderName() == null || inAppDetailedTransactionInfo.getCardholderName().isEmpty())) {
                inAppTZTxnInfo.txnAttributes.put("cardholder_name", inAppDetailedTransactionInfo.getCardholderName());
            }
            inAppTZTxnInfo.txnAttributes.put("eci_indicator", "5");
            inAppTZTxnInfo.nonce = inAppDetailedTransactionInfo.getNonce();
            try {
                ProcessInAppPaymentResponse processInAppPayment;
                byte[] generateRndBytes = generateRndBytes(4);
                if (inAppDetailedTransactionInfo.cd() == null) {
                    processInAppPayment = this.ph.processInAppPayment(inAppTZTxnInfo, Utils.encodeHex(generateRndBytes));
                } else {
                    inAppTZTxnInfo.merchantCertificate = inAppDetailedTransactionInfo.cd();
                    processInAppPayment = this.ph.processInAppPayment(inAppTZTxnInfo, Utils.encodeHex(generateRndBytes));
                }
                if (processInAppPayment == null) {
                    Log.m286e("AmexPayProvider", "Error: mAmexNfcPaymentProviderProxy.processInAppPayment returned null");
                } else {
                    TokenDataStatus tokenDataStatus = processInAppPayment.getTokenDataStatus();
                    if (tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                        bArr = Utils.fromBase64(processInAppPayment.getPaymentPayload());
                        Log.m285d("AmexPayProvider", "stopping In App Payment");
                        cr();
                        Log.m287i("AmexPayProvider", "generateInAppPaymentPayload: end: " + System.currentTimeMillis());
                        Log.m285d("AmexPayProvider", "InApp Payload " + Arrays.toString(bArr));
                        Log.m285d("AmexPayProvider", "InApp Payload " + bArr.length);
                    } else {
                        Log.m286e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.processInAppPayment failed Reason Code: " + tokenDataStatus.getReasonCode() + "Detail Code = " + tokenDataStatus.getDetailCode() + "Detail Message = " + tokenDataStatus.getDetailMessage());
                        Log.m285d("AmexPayProvider", "stopping In App Payment");
                        cr();
                    }
                }
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                Log.m286e("AmexPayProvider", "Exception occurred during generateInAppPaymentPayload ");
            } finally {
                Log.m285d("AmexPayProvider", "stopping In App Payment");
                cr();
            }
        } else {
            Log.m286e("AmexPayProvider", "Start Payment Failed");
        }
        return bArr;
    }

    protected ProviderRequestData getReplenishmentRequestData() {
        EphemeralKeyInfo cv;
        Exception e;
        TokenDataResponse tokenData;
        String str;
        String str2;
        String decryptTokenData;
        Throwable e2;
        ProcessRequestDataResponse processRequestDataResponse = null;
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (this.mProviderTokenKey == null) {
            providerRequestData.setErrorCode(-4);
            return providerRequestData;
        }
        TokenDataStatus tokenDataStatus;
        JsonObject jsonObject = new JsonObject();
        String cn = this.mProviderTokenKey.cn();
        JsonElement jsonObject2 = new JsonObject();
        try {
            DevicePublicCerts cu = this.pg.cu();
            cv = this.pg.cv();
            try {
                if (cu.deviceCertificate != null) {
                    jsonObject2.addProperty("devicePublicKeyCert", cu.deviceCertificate);
                }
                if (cu.deviceSigningCertificate != null) {
                    jsonObject2.addProperty("deviceSigningPublicKeyCert", cu.deviceSigningCertificate);
                }
                if (cv != null) {
                    jsonObject2.addProperty("ephemeralPublicKey", cv.ephemeralPublicKey);
                }
                jsonObject2.addProperty("clientAPIVersion", this.pi.getClientVersion());
                jsonObject2.addProperty("tokenDataVersion", this.pi.getTokenDataVersion(cn).getTokenDataVersion());
            } catch (Exception e3) {
                e = e3;
                providerRequestData.setErrorCode(-2);
                e.printStackTrace();
                tokenData = this.pi.getTokenData(cn);
                tokenDataStatus = tokenData.getTokenDataStatus();
                Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
                if (tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                    jsonObject.addProperty("responseCode", tokenDataStatus.getReasonCode());
                    jsonObject.addProperty("detailCode", tokenDataStatus.getDetailCode());
                    jsonObject.addProperty("detailMessage", tokenDataStatus.getDetailMessage());
                    str = BuildConfig.FLAVOR;
                    str2 = BuildConfig.FLAVOR;
                    try {
                        decryptTokenData = this.pg.decryptTokenData(tokenData.getApduBlob());
                        try {
                            str2 = this.pg.decryptTokenData(tokenData.getMetaDataBlob());
                        } catch (AmexTAException e4) {
                            e2 = e4;
                            providerRequestData.setErrorCode(-2);
                            Log.m284c("AmexPayProvider", e2.getMessage(), e2);
                            processRequestDataResponse = this.pg.m787b(this.pl.getContent(), null, decryptTokenData + str2);
                            if (processRequestDataResponse == null) {
                                providerRequestData.setErrorCode(-2);
                                return providerRequestData;
                            }
                            jsonObject.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
                            jsonObject.add("secureDeviceData", jsonObject2);
                            providerRequestData.m822a(jsonObject);
                            if (cv != null) {
                                this.pp.edit().putString(cn + "_keys", cv.encryptedEphemeralPrivateKey).apply();
                            }
                            return providerRequestData;
                        }
                    } catch (Throwable e5) {
                        Throwable th = e5;
                        decryptTokenData = str;
                        e2 = th;
                        providerRequestData.setErrorCode(-2);
                        Log.m284c("AmexPayProvider", e2.getMessage(), e2);
                        processRequestDataResponse = this.pg.m787b(this.pl.getContent(), null, decryptTokenData + str2);
                        if (processRequestDataResponse == null) {
                            providerRequestData.setErrorCode(-2);
                            return providerRequestData;
                        }
                        jsonObject.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
                        jsonObject.add("secureDeviceData", jsonObject2);
                        providerRequestData.m822a(jsonObject);
                        if (cv != null) {
                            this.pp.edit().putString(cn + "_keys", cv.encryptedEphemeralPrivateKey).apply();
                        }
                        return providerRequestData;
                    }
                    try {
                        processRequestDataResponse = this.pg.m787b(this.pl.getContent(), null, decryptTokenData + str2);
                    } catch (Throwable e6) {
                        providerRequestData.setErrorCode(-2);
                        Log.m284c("AmexPayProvider", e6.getMessage(), e6);
                    }
                    if (processRequestDataResponse == null) {
                        jsonObject.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
                        jsonObject.add("secureDeviceData", jsonObject2);
                        providerRequestData.m822a(jsonObject);
                        if (cv != null) {
                            this.pp.edit().putString(cn + "_keys", cv.encryptedEphemeralPrivateKey).apply();
                        }
                        return providerRequestData;
                    }
                    providerRequestData.setErrorCode(-2);
                    return providerRequestData;
                }
                providerRequestData.setErrorCode(-2);
                return providerRequestData;
            }
        } catch (Exception e7) {
            e = e7;
            cv = processRequestDataResponse;
            providerRequestData.setErrorCode(-2);
            e.printStackTrace();
            tokenData = this.pi.getTokenData(cn);
            tokenDataStatus = tokenData.getTokenDataStatus();
            Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
            if (tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                jsonObject.addProperty("responseCode", tokenDataStatus.getReasonCode());
                jsonObject.addProperty("detailCode", tokenDataStatus.getDetailCode());
                jsonObject.addProperty("detailMessage", tokenDataStatus.getDetailMessage());
                str = BuildConfig.FLAVOR;
                str2 = BuildConfig.FLAVOR;
                decryptTokenData = this.pg.decryptTokenData(tokenData.getApduBlob());
                str2 = this.pg.decryptTokenData(tokenData.getMetaDataBlob());
                processRequestDataResponse = this.pg.m787b(this.pl.getContent(), null, decryptTokenData + str2);
                if (processRequestDataResponse == null) {
                    jsonObject.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
                    jsonObject.add("secureDeviceData", jsonObject2);
                    providerRequestData.m822a(jsonObject);
                    if (cv != null) {
                        this.pp.edit().putString(cn + "_keys", cv.encryptedEphemeralPrivateKey).apply();
                    }
                    return providerRequestData;
                }
                providerRequestData.setErrorCode(-2);
                return providerRequestData;
            }
            providerRequestData.setErrorCode(-2);
            return providerRequestData;
        }
        tokenData = this.pi.getTokenData(cn);
        tokenDataStatus = tokenData.getTokenDataStatus();
        Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
        if (tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
            providerRequestData.setErrorCode(-2);
            return providerRequestData;
        }
        jsonObject.addProperty("responseCode", tokenDataStatus.getReasonCode());
        jsonObject.addProperty("detailCode", tokenDataStatus.getDetailCode());
        jsonObject.addProperty("detailMessage", tokenDataStatus.getDetailMessage());
        str = BuildConfig.FLAVOR;
        str2 = BuildConfig.FLAVOR;
        decryptTokenData = this.pg.decryptTokenData(tokenData.getApduBlob());
        str2 = this.pg.decryptTokenData(tokenData.getMetaDataBlob());
        processRequestDataResponse = this.pg.m787b(this.pl.getContent(), null, decryptTokenData + str2);
        if (processRequestDataResponse == null) {
            providerRequestData.setErrorCode(-2);
            return providerRequestData;
        }
        jsonObject.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
        jsonObject.add("secureDeviceData", jsonObject2);
        providerRequestData.m822a(jsonObject);
        if (cv != null) {
            this.pp.edit().putString(cn + "_keys", cv.encryptedEphemeralPrivateKey).apply();
        }
        return providerRequestData;
    }

    protected ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        Log.m287i("AmexPayProvider", "Replenish Token");
        ProviderResponseData providerResponseData = new ProviderResponseData();
        try {
            providerResponseData.setErrorCode(0);
            if (jsonObject == null || jsonObject.getAsJsonObject("secureTokenData") == null) {
                Log.m286e("AmexPayProvider", "Input Data is NULL");
                providerResponseData.setErrorCode(-4);
                return providerResponseData;
            }
            String walletId;
            JsonObject asJsonObject = jsonObject.getAsJsonObject("secureTokenData");
            String asString = asJsonObject.get("initializationVector").getAsString();
            String asString2 = asJsonObject.get("encryptedTokenData").getAsString();
            String asString3 = asJsonObject.get("encryptedTokenDataHMAC").getAsString();
            String asString4 = asJsonObject.get("cloudPublicKeyCert").getAsString();
            if (this.kQ != null) {
                walletId = this.kQ.getWalletId();
            } else {
                walletId = ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID);
            }
            if (walletId == null) {
                Log.m286e("AmexPayProvider", "Wallet Id is NULL");
                providerResponseData.setErrorCode(-2);
                return providerResponseData;
            }
            String au = AmexPayProvider.au(jsonObject.get(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS).getAsString());
            if (au == null) {
                Log.m286e("AmexPayProvider", "Token Status is NULL");
                providerResponseData.setErrorCode(-2);
                return providerResponseData;
            }
            String str;
            ProcessTokenDataResponse a;
            StringBuilder append = new StringBuilder().append(asString4);
            if (this.pm == null) {
                str = BuildConfig.FLAVOR;
            } else {
                str = this.pm.getContent();
            }
            asString4 = append.append(str).toString();
            String cn = this.mProviderTokenKey.cn();
            String string = this.pp.getString(cn + "_keys", null);
            this.pp.edit().remove(cn + "_keys").apply();
            try {
                a = this.pg.m782a(asString4, asString, asString2, asString3, au, Utils.encodeHex(walletId.getBytes()), string);
            } catch (Throwable e) {
                providerResponseData.setErrorCode(-2);
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                a = null;
            }
            if (a == null) {
                Log.m286e("AmexPayProvider", "Token Data Response is NULL");
                providerResponseData.setErrorCode(-2);
                return providerResponseData;
            }
            TokenDataStatus updateTokenData = this.pi.updateTokenData(cn, a.eAPDUBlob, a.eNFCLUPCBlob, a.eOtherLUPCBlob, a.eMetadataBlob, a.lupcMetadataBlob);
            if (updateTokenData.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + updateTokenData.getReasonCode() + " : " + updateTokenData.getDetailCode());
                JsonElement jsonObject2 = new JsonObject();
                try {
                    ProcessRequestDataResponse b;
                    DevicePublicCerts cu = this.pg.cu();
                    if (cu.deviceCertificate != null) {
                        jsonObject2.addProperty("devicePublicKeyCert", cu.deviceCertificate);
                    }
                    if (cu.deviceSigningCertificate != null) {
                        jsonObject2.addProperty("deviceSigningPublicKeyCert", cu.deviceSigningCertificate);
                    }
                    try {
                        b = this.pg.m787b(this.pl.getContent(), null, cn);
                    } catch (Throwable e2) {
                        providerResponseData.setErrorCode(-2);
                        Log.m284c("AmexPayProvider", e2.getMessage(), e2);
                        b = null;
                    }
                    if (b == null) {
                        providerResponseData.setErrorCode(-2);
                        return providerResponseData;
                    }
                    providerResponseData.setProviderTokenKey(new ProviderTokenKey(cn));
                    JsonObject jsonObject3 = new JsonObject();
                    jsonObject3.addProperty("secureTokenDataSignature", b.requestDataSignature);
                    jsonObject3.add("secureDeviceData", jsonObject2);
                    providerResponseData.m1057b(jsonObject3);
                    this.pp.edit().remove(cn + "_replenish_retry").apply();
                    this.ps.m312b(this.mProviderTokenKey);
                    av(cn);
                    return providerResponseData;
                } catch (AmexTAException e3) {
                    providerResponseData.setErrorCode(-2);
                    return providerResponseData;
                }
            }
            Log.m286e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.updateTokenData failed Reason Code: " + updateTokenData.getReasonCode() + "Detail Code = " + updateTokenData.getDetailCode() + "Detail Message = " + updateTokenData.getDetailMessage());
            providerResponseData.setErrorCode(-2);
            m763a(this.mProviderTokenKey, false);
            ax(cn);
            return providerResponseData;
        } catch (Throwable e22) {
            Log.m284c("AmexPayProvider", e22.getMessage(), e22);
            replenishAlarmExpired();
            providerResponseData.setErrorCode(-2);
            return providerResponseData;
        }
    }

    protected ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        if (tokenStatus == null) {
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        } else if (this.mProviderTokenKey != null) {
            String cn = this.mProviderTokenKey.cn();
            TokenStatusResponse tokenStatus2 = this.pi.getTokenStatus(cn);
            TokenDataStatus tokenDataStatus = null;
            JsonObject jsonObject2 = new JsonObject();
            if (tokenStatus2 != null && tokenStatus2.getTokenStatus() != null) {
                Log.m285d("AmexPayProvider", "AmexTokenDataManager token status : " + tokenStatus2.getTokenStatus());
                Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenStatus2.getTokenDataStatus().getReasonCode() + " : " + tokenStatus2.getTokenDataStatus().getDetailCode());
                if (tokenStatus.getCode().equals(TokenStatus.ACTIVE) && tokenStatus2.getTokenStatus().equals(HCEClientConstants.API_INDEX_TOKEN_PERSO)) {
                    Log.m285d("AmexPayProvider", "Activating Token");
                    tokenDataStatus = this.pi.activateToken(cn);
                    av(cn);
                } else if (tokenStatus.getCode().equals(TokenStatus.ACTIVE)) {
                    Log.m285d("AmexPayProvider", "Resuming Token");
                    try {
                        tokenDataStatus = this.pi.resumeToken(cn);
                    } catch (Exception e) {
                        Log.m286e("AmexPayProvider", e.getMessage());
                    }
                    av(cn);
                } else if (tokenStatus.getCode().equals(TokenStatus.SUSPENDED)) {
                    Log.m285d("AmexPayProvider", "Suspending Token");
                    tokenDataStatus = this.pi.suspendToken(cn);
                    TokenReplenishAlarm.m1071a(this.mContext, this.mProviderTokenKey);
                } else if (tokenStatus.getCode().equals(TokenStatus.DISPOSED)) {
                    Log.m285d("AmexPayProvider", "Deleting Token");
                    tokenDataStatus = this.pi.deleteToken(cn);
                    this.pp.edit().remove(cn + "_keys").remove(cn + "_transaction_json_data").remove(cn + "_replenish_retry").apply();
                    TokenReplenishAlarm.m1071a(this.mContext, this.mProviderTokenKey);
                } else {
                    Log.m286e("AmexPayProvider", "Unknown Token Status : " + tokenStatus.getCode());
                }
                if (tokenDataStatus != null) {
                    Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
                    jsonObject2.addProperty("responseCode", tokenDataStatus.getReasonCode());
                    if (!tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                        providerResponseData.setErrorCode(-2);
                    }
                }
            } else if (tokenStatus.getCode().equals(TokenStatus.ACTIVE) || tokenStatus.getCode().equals(TokenStatus.SUSPENDED)) {
                providerResponseData.setErrorCode(-5);
                Log.m286e("AmexPayProvider", "Unknown Token : " + cn);
            } else {
                jsonObject2.addProperty("responseCode", HCEClientConstants.HEX_ZERO_BYTE);
                Log.m287i("AmexPayProvider", "Token Already Deleted");
            }
            providerResponseData.m1057b(jsonObject2);
            return providerResponseData;
        } else if (tokenStatus.getCode().equals(TokenStatus.DISPOSED)) {
            return providerResponseData;
        } else {
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        }
    }

    public int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        return AmexTransactionManager.m775a(this.mContext, this.pg, this.pl, this.pp).m778a(this.mProviderTokenKey, bundle, transactionResponse);
    }

    protected TransactionDetails processTransactionData(Object obj) {
        return AmexTransactionManager.m775a(this.mContext, this.pg, this.pl, this.pp).m779a(this.mProviderTokenKey, obj);
    }

    public boolean getPayReadyState() {
        return aw(this.mProviderTokenKey.cn());
    }

    protected void loadTA() {
        this.pg.loadTA();
        Log.m287i("AmexPayProvider", "load real TA");
    }

    protected void unloadTA() {
        this.pg.unloadTA();
        Log.m287i("AmexPayProvider", "unload real TA");
    }

    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
        av(this.mProviderTokenKey.cn());
    }

    public void setupReplenishAlarm() {
        Log.m285d("AmexPayProvider", "Entered setup Replenish Alarm");
        String cn = this.mProviderTokenKey.cn();
        if (cn == null) {
            Log.m286e("AmexPayProvider", "TrTokenId is null");
        } else {
            av(cn);
        }
    }

    public void setPaymentFrameworkRequester(PaymentFrameworkRequester paymentFrameworkRequester) {
        this.ps = paymentFrameworkRequester;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        boolean z = false;
        Log.m285d("AmexPayProvider", "startMstPay(for transmit)");
        if (this.pn) {
            Log.m285d("AmexPayProvider", "startMstPay: input config " + Arrays.toString(bArr));
            try {
                z = this.pg.m786a(i, bArr);
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", e.getMessage(), e);
            }
            if (!z) {
                Log.m286e("AmexPayProvider", "failure to do startMstPay");
            }
            return true;
        }
        Log.m286e("AmexPayProvider", "Error: prepareMstPay must never happen when there is already a pending MST");
        return z;
    }

    protected void onPaySwitch(int i, int i2) {
        super.onPaySwitch(i, i2);
        if (i == 1 && i2 == 2) {
            Log.m286e("AmexPayProvider", "Error: Payment mode switching from NFC to MST. Must never happen");
        } else {
            Log.m285d("AmexPayProvider", "onPaySwitch");
        }
    }

    public void updateRequestStatus(ProviderRequestStatus providerRequestStatus) {
        Log.m285d("AmexPayProvider", "updateRequestStatus : " + providerRequestStatus.getRequestType() + " " + providerRequestStatus.ci());
        if (providerRequestStatus.ck() != null) {
            Log.m285d("AmexPayProvider", "updateRequestStatus : " + providerRequestStatus.ck().cn());
        }
        if (providerRequestStatus.getRequestType() != 23) {
            switch (providerRequestStatus.ci()) {
                case RequestedCertificate.certificate /*-1*/:
                    if (providerRequestStatus.ck() != null) {
                        this.pp.edit().remove(providerRequestStatus.ck().cn() + "_keys").apply();
                    }
                    if (providerRequestStatus.getRequestType() == 11 && providerRequestStatus.cj().equals(ErrorResponseData.ERROR_CODE_REPLENISH_EXCEEDED)) {
                        m763a(providerRequestStatus.ck(), true);
                        ax(providerRequestStatus.ck().cn());
                    }
                case ECCurve.COORD_AFFINE /*0*/:
                    if (providerRequestStatus.getRequestType() == 3) {
                        this.pp.edit().putString(providerRequestStatus.ck().cn() + "_keys", this.pq).apply();
                    }
                default:
                    Log.m286e("AmexPayProvider", "Error in updating status");
            }
        }
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        return jsonObject.getAsJsonObject("secureTokenData") != null;
    }

    protected ProviderRequestData getVerifyIdvRequestData(VerifyIdvInfo verifyIdvInfo) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (verifyIdvInfo == null) {
            try {
                providerRequestData.setErrorCode(-4);
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                providerRequestData.setErrorCode(-2);
            }
        } else {
            ProcessRequestDataResponse b;
            JsonElement jsonObject = new JsonObject();
            try {
                DevicePublicCerts cu = this.pg.cu();
                if (cu.deviceCertificate != null) {
                    jsonObject.addProperty("devicePublicKeyCert", cu.deviceCertificate);
                }
                if (cu.deviceSigningCertificate != null) {
                    jsonObject.addProperty("deviceSigningPublicKeyCert", cu.deviceSigningCertificate);
                }
            } catch (Exception e2) {
                providerRequestData.setErrorCode(-2);
                e2.printStackTrace();
            }
            try {
                b = this.pg.m787b(this.pl.getContent(), null, verifyIdvInfo.getValue());
            } catch (Throwable e3) {
                providerRequestData.setErrorCode(-2);
                Log.m284c("AmexPayProvider", e3.getMessage(), e3);
                b = null;
            }
            if (b == null) {
                providerRequestData.setErrorCode(-2);
            } else {
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("authenticationCodeSignature", b.requestDataSignature);
                jsonObject2.add("secureDeviceData", jsonObject);
                providerRequestData.m822a(jsonObject2);
            }
        }
        return providerRequestData;
    }

    protected ProviderRequestData getDeleteRequestData(Bundle bundle) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (this.mProviderTokenKey == null) {
            providerRequestData.setErrorCode(-4);
            return providerRequestData;
        }
        ProcessRequestDataResponse b;
        JsonObject jsonObject = new JsonObject();
        String cn = this.mProviderTokenKey.cn();
        if (cn == null) {
            cn = this.mProviderTokenKey.getTrTokenId();
        }
        JsonElement jsonObject2 = new JsonObject();
        try {
            DevicePublicCerts cu = this.pg.cu();
            EphemeralKeyInfo cv = this.pg.cv();
            if (cu.deviceCertificate != null) {
                jsonObject2.addProperty("devicePublicKeyCert", cu.deviceCertificate);
            }
            if (cu.deviceSigningCertificate != null) {
                jsonObject2.addProperty("deviceSigningPublicKeyCert", cu.deviceSigningCertificate);
            }
            if (cv != null) {
                jsonObject2.addProperty("ephemeralPublicKey", cv.ephemeralPublicKey);
            }
            jsonObject2.addProperty("clientAPIVersion", this.pi.getClientVersion());
            jsonObject2.addProperty("tokenDataVersion", this.pi.getTokenDataVersion(cn).getTokenDataVersion());
        } catch (Exception e) {
            providerRequestData.setErrorCode(-2);
            e.printStackTrace();
        }
        TokenDataStatus tokenDataStatus = this.pi.getTokenData(cn).getTokenDataStatus();
        Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
        jsonObject.addProperty("tokenRefId", cn);
        jsonObject.addProperty("responseCode", tokenDataStatus.getReasonCode());
        jsonObject.addProperty("detailCode", tokenDataStatus.getDetailCode());
        String jsonObject3 = AmexPayProvider.m764c(jsonObject).toString();
        Log.m288m("AmexPayProvider", "Sorted Data" + jsonObject3);
        try {
            b = this.pg.m787b(this.pl.getContent(), null, jsonObject3);
        } catch (Throwable e2) {
            providerRequestData.setErrorCode(-2);
            Log.m284c("AmexPayProvider", e2.getMessage(), e2);
            b = null;
        }
        if (b == null) {
            providerRequestData.setErrorCode(-2);
            return providerRequestData;
        }
        jsonObject.addProperty("secureTokenDataSignature", b.requestDataSignature);
        jsonObject.add("secureDeviceData", jsonObject2);
        jsonObject3 = this.pp.getString(cn + "_transaction_json_data", null);
        cn = this.pp.getString(cn + "_transaction_param", null);
        if (jsonObject3 == null || cn == null) {
            Log.m286e("AmexPayProvider", "No Access Key.");
        } else {
            try {
                cn = this.pg.m788o(jsonObject3, cn);
            } catch (Throwable e3) {
                Log.m284c("AmexPayProvider", e3.getMessage(), e3);
                Log.m286e("AmexPayProvider", "encryptedData is invalid");
                cn = null;
            }
            if (cn != null) {
                ProcessRequestDataResponse b2;
                try {
                    b2 = this.pg.m787b(this.pl.getContent(), new String(Base64.decode(cn, 2)), null);
                } catch (Throwable e32) {
                    Log.m284c("AmexPayProvider", e32.getMessage(), e32);
                    b2 = null;
                }
                if (b2 != null) {
                    jsonObject.addProperty("encryptedData", b2.encryptedRequestData);
                    jsonObject.addProperty("encryptionParameters", b2.encryptionParams);
                }
            } else {
                Log.m286e("AmexPayProvider", "Cannot Decrypt Access Key");
            }
        }
        providerRequestData.m822a(jsonObject);
        return providerRequestData;
    }

    public boolean isPayAllowedForPresentationMode(int i) {
        if (this.mProviderTokenKey == null) {
            Log.m286e("AmexPayProvider", "ProviderTokenKey is null");
            return false;
        }
        if (i == 2) {
            Log.m285d("AmexPayProvider", "isPayAllowedForPresentationMode " + this.mProviderTokenKey.getTrTokenId());
            TokenDataResponse tokenData = this.pi.getTokenData(this.mProviderTokenKey.getTrTokenId());
            TokenDataStatus tokenDataStatus = tokenData.getTokenDataStatus();
            Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
            if (!tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                return false;
            }
            LupcMetaData aA = AmexUtils.aA(tokenData.getLupcMetadataBlob());
            if (aA != null) {
                Log.m285d("AmexPayProvider", "otherLupcCount : " + aA.otherLupcCount);
                if (aA.otherLupcCount > 0) {
                    return true;
                }
                return false;
            }
            Log.m285d("AmexPayProvider", "lupcMetaData null");
        }
        return true;
    }

    public byte[] decryptUserSignature(String str) {
        byte[] bArr = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                Object a = this.pg.m785a(str, false);
                if (!TextUtils.isEmpty(a)) {
                    bArr = com.samsung.android.spayfw.payprovider.amex.tzsvc.Utils.fromBase64(a);
                }
            } catch (Throwable e) {
                Log.m286e("AmexPayProvider", "decryptUserSignature Error occured while gettting decrypted data from TA");
                Log.m284c("AmexPayProvider", e.getMessage(), e);
            }
        }
        return bArr;
    }

    public String encryptUserSignature(byte[] bArr) {
        String str = null;
        if (bArr != null) {
            try {
                str = this.pg.m785a(com.samsung.android.spayfw.payprovider.amex.tzsvc.Utils.toBase64(bArr), true);
            } catch (Throwable e) {
                Log.m286e("AmexPayProvider", "encryptUserSignature Error occured while gettting encypted data from TA");
                Log.m284c("AmexPayProvider", e.getMessage(), e);
            }
        }
        return str;
    }

    protected void replenishAlarmExpired() {
        if (this.mProviderTokenKey == null) {
            Log.m286e("AmexPayProvider", "cannot fire replenishment, providerTokenKey is null");
        } else if (this.ps != null) {
            this.ps.m311a(this.mProviderTokenKey);
        } else {
            Log.m286e("AmexPayProvider", "CMN FW REQUESTER IS NOT INITIALIZED");
        }
    }

    boolean cq() {
        Log.m285d("AmexPayProvider", "startPayment");
        if (this.pn) {
            Log.m286e("AmexPayProvider", "Error: Previous Payment is not yet stopped");
            return false;
        }
        try {
            int i;
            if (getAuthType().equalsIgnoreCase(PaymentNetworkProvider.AUTHTYPE_TRUSTED_PIN)) {
                i = 2;
            } else if (getAuthType().equalsIgnoreCase(PaymentNetworkProvider.AUTHTYPE_FP) || getAuthType().equalsIgnoreCase(PaymentNetworkProvider.AUTHTYPE_IRIS)) {
                i = 5;
            } else if (getAuthType().equalsIgnoreCase(PaymentNetworkProvider.AUTHTYPE_BACKUPPASSWORD)) {
                i = 4;
            } else {
                i = 0;
            }
            TokenDataStatus startTransaction = this.ph.startTransaction(this.pj.cn(), 1, i, String.valueOf(Utils.am(this.mContext) / 1000));
            if (startTransaction.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                this.po = false;
                this.pn = true;
                return true;
            }
            Log.m286e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.startTransaction failed Reason Code: " + startTransaction.getReasonCode() + "Detail Code = " + startTransaction.getDetailCode() + "Detail Message = " + startTransaction.getDetailMessage());
            return false;
        } catch (Throwable e) {
            Log.m284c("AmexPayProvider", e.getMessage(), e);
            return false;
        }
    }

    boolean cr() {
        Log.m285d("AmexPayProvider", "stopPayment");
        if (this.pn) {
            try {
                EndTransactionResponse endTransaction = this.ph.endTransaction();
                if (endTransaction == null) {
                    Log.m286e("AmexPayProvider", "FATAL Error: mAmexNfcPaymentProviderProxy.endTransaction failed");
                    return false;
                }
                TokenDataStatus tokenDataStatus = endTransaction.getTokenDataStatus();
                if (!tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                    Log.m286e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.endTransaction failed Reason Code: " + tokenDataStatus.getReasonCode() + "Detail Code = " + tokenDataStatus.getDetailCode() + "Detail Message = " + tokenDataStatus.getDetailMessage());
                    Log.m285d("AmexPayProvider", "stopPayment : cleanup state and check replenishment");
                    if (this.pj != null) {
                        av(this.pj.cn());
                    }
                    this.pn = false;
                    this.pj = null;
                    this.po = false;
                    return false;
                } else if (endTransaction.getLupcMetaDataBlob() == null) {
                    Log.m286e("AmexPayProvider", "LUPC MetaDataBlob (returned by endTransaction is null");
                    Log.m285d("AmexPayProvider", "stopPayment : cleanup state and check replenishment");
                    if (this.pj != null) {
                        av(this.pj.cn());
                    }
                    this.pn = false;
                    this.pj = null;
                    this.po = false;
                    return false;
                } else {
                    LupcMetaData aA = AmexUtils.aA(endTransaction.getLupcMetaDataBlob());
                    if (aA != null) {
                        Log.m285d("AmexPayProvider", "Remaining LUPC Count (after endTransaction): " + aA.nfcLupcCount);
                    } else {
                        Log.m285d("AmexPayProvider", "lupcMetaData is null");
                    }
                    Log.m285d("AmexPayProvider", "stopPayment : cleanup state and check replenishment");
                    if (this.pj != null) {
                        av(this.pj.cn());
                    }
                    this.pn = false;
                    this.pj = null;
                    this.po = false;
                    return true;
                }
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                return false;
            } finally {
                Log.m285d("AmexPayProvider", "stopPayment : cleanup state and check replenishment");
                if (this.pj != null) {
                    av(this.pj.cn());
                }
                this.pn = false;
                this.pj = null;
                this.po = false;
            }
        } else {
            Log.m286e("AmexPayProvider", "Error: Stop Payment is called when there is no Payment in Progress");
            return false;
        }
    }

    Bundle m765b(EnrollCardInfo enrollCardInfo) {
        Bundle bundle;
        Throwable e;
        try {
            bundle = new Bundle();
            try {
                if (enrollCardInfo.getUserEmail() != null) {
                    bundle.putString("emailHash", AmexUtils.az(enrollCardInfo.getUserEmail()));
                }
                bundle.putString("appId", this.mContext.getPackageName());
            } catch (Exception e2) {
                e = e2;
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                return bundle;
            }
        } catch (Throwable e3) {
            Throwable th = e3;
            bundle = null;
            e = th;
            Log.m284c("AmexPayProvider", e.getMessage(), e);
            return bundle;
        }
        return bundle;
    }

    private Bundle m761a(ProvisionTokenInfo provisionTokenInfo) {
        int parseInt;
        ArrayList arrayList = new ArrayList();
        Bundle bundle = new Bundle();
        List arrayList2 = new ArrayList();
        bundle.putSerializable("riskData", arrayList);
        Map activationParams = provisionTokenInfo.getActivationParams();
        arrayList.add(new RiskDataParam("networkOperator", DeviceInfo.getNetworkOperatorName(this.mContext)));
        arrayList.add(new RiskDataParam("networkType", Utils.aj(this.mContext) ? "wifi" : "cellular"));
        arrayList.add(new RiskDataParam("ipAddress", DeviceInfo.getLocalIpAddress()));
        String id = TimeZone.getDefault().getID();
        if (!(id == null || id.isEmpty())) {
            arrayList.add(new RiskDataParam("deviceTimezone", id));
        }
        arrayList.add(new RiskDataParam("timezoneSetByCarrier", Boolean.valueOf(DeviceInfo.getAutoTimeZone(this.mContext))));
        Location lastKnownLocation = DeviceInfo.getLastKnownLocation(this.mContext);
        if (lastKnownLocation != null) {
            arrayList.add(new RiskDataParam("deviceLatitude", lastKnownLocation.getLatitude() + BuildConfig.FLAVOR));
            arrayList.add(new RiskDataParam("deviceLongitude", lastKnownLocation.getLongitude() + BuildConfig.FLAVOR));
        }
        arrayList.add(new RiskDataParam("locale", Locale.getDefault().getLanguage() + HCEClientConstants.TAG_KEY_SEPARATOR + Locale.getDefault().getCountry()));
        FraudDataProvider fraudDataProvider = new FraudDataProvider(this.mContext);
        arrayList.add(new RiskDataParam(ActivationData.DEVICE_SCORE, Integer.valueOf(fraudDataProvider.m742E(CNCCCommands.CMD_CNCC_CMD_UNKNOWN).nk)));
        arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_SCORE, activationParams.get(ActivationData.WALLET_ACCOUNT_SCORE)));
        try {
            id = (String) activationParams.get(ActivationData.USER_ACCOUNT_FIRST_CREATED_IN_DAYS);
            if (!(id == null || id.isEmpty())) {
                parseInt = Integer.parseInt(id);
                String str = (parseInt / 7) + BuildConfig.FLAVOR;
                arrayList.add(new RiskDataParam("accountTenureOnFile", str));
                arrayList.add(new RiskDataParam("accountIdTenure", str));
                if (parseInt < 30) {
                    arrayList2.add("LT");
                }
            }
        } catch (Throwable e) {
            Log.m284c("AmexPayProvider", e.getMessage(), e);
        }
        try {
            id = (String) activationParams.get(ActivationData.WALLET_ACCOUNT_FIRST_CREATED_IN_DAYS);
            if (!(id == null || id.isEmpty())) {
                arrayList.add(new RiskDataParam("walletAccountTenure", (Integer.parseInt(id) / 7) + BuildConfig.FLAVOR));
            }
        } catch (Throwable e2) {
            Log.m284c("AmexPayProvider", e2.getMessage(), e2);
        }
        arrayList.add(new RiskDataParam("countryOnDevice", DeviceInfo.getDeviceCountry()));
        arrayList.add(new RiskDataParam("countryonAccountId", activationParams.get(ActivationData.WALLET_ACCOUNT_COUNTRY_CODE)));
        arrayList.add(new RiskDataParam(ActivationData.TOTAL_REGISTERED_DEVICES_FOR_ACCOUNT, activationParams.get(ActivationData.TOTAL_REGISTERED_DEVICES_FOR_ACCOUNT)));
        arrayList.add(new RiskDataParam(ActivationData.TOTAL_DEVICES_WITH_TOKEN_FOR_ACCOUNT, activationParams.get(ActivationData.TOTAL_DEVICES_WITH_TOKEN_FOR_ACCOUNT)));
        arrayList.add(new RiskDataParam("activeTokensCountForUser", activationParams.get(ActivationData.WALLET_ACC_ACTIVE_TOKENS_GIVEN_DEVICE)));
        arrayList.add(new RiskDataParam("daysSinceLastWalletActivity", activationParams.get(ActivationData.LAST_ACCOUNT_ACTIVITY_IN_DAYS)));
        arrayList.add(new RiskDataParam("walletTransactionsCount", activationParams.get(ActivationData.LAST_12_MONTHS_TRANSACTION_COUNT)));
        arrayList.add(new RiskDataParam(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS, activationParams.get(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS)));
        arrayList.add(new RiskDataParam(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS, activationParams.get(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS)));
        try {
            id = (String) activationParams.get(ActivationData.WALLET_ACCOUNT_DEVICE_BINDING_AGE_IN_DAYS);
            if (!(id == null || id.isEmpty())) {
                parseInt = Integer.parseInt(id);
                arrayList.add(new RiskDataParam("ageOfAcctOnDevice", (parseInt / 7) + BuildConfig.FLAVOR));
                if (parseInt < 30) {
                    arrayList2.add("GD");
                }
            }
        } catch (Throwable e22) {
            Log.m284c("AmexPayProvider", e22.getMessage(), e22);
        }
        try {
            arrayList.add(new RiskDataParam("noOfProvisioningAttempts", Integer.valueOf(fraudDataProvider.m743x(1))));
            arrayList.add(new RiskDataParam("tokensOnDeviceScore", Integer.valueOf(fraudDataProvider.m744y(CNCCCommands.CMD_CNCC_CMD_UNKNOWN))));
            parseInt = fraudDataProvider.m743x(30) + 1;
            Log.m285d("AmexPayProvider", "provAttempts : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XC");
            }
            if (parseInt >= 3) {
                arrayList2.add(PaymentFramework.CARD_BRAND_MASTERCARD);
            }
            parseInt = fraudDataProvider.m739B(30);
            Log.m285d("AmexPayProvider", "billingAddress : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XZ");
            }
            if (parseInt >= 3) {
                arrayList2.add("MZ");
            }
            parseInt = fraudDataProvider.m745z(30);
            Log.m285d("AmexPayProvider", "lastNames : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XN");
            }
            if (parseInt >= 3) {
                arrayList2.add("MN");
            }
            parseInt = fraudDataProvider.m740C(30);
            Log.m285d("AmexPayProvider", "resetCount : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XR");
            }
            try {
                id = (String) activationParams.get(ActivationData.WALLET_ACCOUNT_CARD_BINDING_AGE_IN_DAYS);
                if (!(id == null || id.isEmpty() || Integer.parseInt(id) >= 30)) {
                    arrayList2.add("LP");
                }
            } catch (Throwable e222) {
                Log.m284c("AmexPayProvider", e222.getMessage(), e222);
            }
            if (DeviceInfo.isVpnConnected(this.mContext) || DeviceInfo.isProxyEnabled(this.mContext)) {
                arrayList2.add("GV");
            }
            arrayList.add(new RiskDataParam("reasonCodes", arrayList2));
        } catch (Throwable e2222) {
            Log.m284c("AmexPayProvider", e2222.getMessage(), e2222);
        }
        return bundle;
    }

    private boolean aw(String str) {
        Log.m285d("AmexPayProvider", "canPay id " + str);
        TokenDataResponse tokenData = this.pi.getTokenData(str);
        TokenDataStatus tokenDataStatus = tokenData.getTokenDataStatus();
        Log.m285d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
        if (!tokenDataStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
            return false;
        }
        LupcMetaData aA = AmexUtils.aA(tokenData.getLupcMetadataBlob());
        if (aA != null) {
            Log.m285d("AmexPayProvider", "nfcLupcCount : " + aA.nfcLupcCount);
            long am = Utils.am(this.mContext) / 1000;
            Log.m285d("AmexPayProvider", "currentTime : " + am);
            Log.m285d("AmexPayProvider", "nfcLupcExpiry : " + aA.nfcLupcExpiry);
            if (aA.nfcLupcCount <= 0 || aA.nfcLupcExpiry <= am) {
                return false;
            }
            return true;
        }
        Log.m285d("AmexPayProvider", "lupcMetaData is null");
        return false;
    }

    private void m763a(ProviderTokenKey providerTokenKey, boolean z) {
        String str = null;
        String string = this.pp.getString(providerTokenKey.cn() + "_replenish_retry", null);
        Log.m286e("AmexPayProvider", "incrementReplenishRetryCount : " + z);
        if (Utils.al(this.mContext)) {
            str = string;
        } else {
            z = false;
        }
        if (z) {
            str = "4|" + ((Utils.am(this.mContext) + 86400000) + 600000);
        } else {
            int i;
            long j;
            long am = Utils.am(this.mContext);
            if (str != null) {
                int parseInt = Integer.parseInt(str.split("|")[0]);
                switch (parseInt) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        i = parseInt + 1;
                        j = am + 600000;
                        break;
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        i = parseInt + 1;
                        j = 5400000 + am;
                        break;
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        i = parseInt + 1;
                        j = 10800000 + am;
                        break;
                    default:
                        i = 4;
                        j = am + 86400000;
                        break;
                }
            }
            Log.m290w("AmexPayProvider", "Retry Data is Empty");
            i = 1;
            j = am + 600000;
            str = i + "|" + j;
            Log.m287i("AmexPayProvider", "Retry Count :" + i);
        }
        this.pp.edit().putString(providerTokenKey.cn() + "_replenish_retry", str).apply();
    }

    private boolean ax(String str) {
        String string = this.pp.getString(str + "_replenish_retry", null);
        if (string != null) {
            try {
                String[] split = string.split("\\|");
                for (String str2 : split) {
                    Log.m285d("AmexPayProvider", "retryParts : " + str2);
                }
                long parseLong = Long.parseLong(split[1]);
                Log.m285d("AmexPayProvider", "retryTime : " + parseLong);
                ProviderTokenKey providerTokenKey = new ProviderTokenKey(str);
                providerTokenKey.setTrTokenId(str);
                TokenReplenishAlarm.m1070a(this.mContext, parseLong, providerTokenKey);
                return true;
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                Log.m286e("AmexPayProvider", "Exception when trying to set replenish retry alarm");
                return false;
            }
        }
        Log.m290w("AmexPayProvider", "Trying to set alarm but retry data is empty.");
        return false;
    }

    private byte[] generateRndBytes(int i) {
        if (i < 1) {
            Log.m285d("AmexPayProvider", "Invalid input length");
            return null;
        }
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }
}
