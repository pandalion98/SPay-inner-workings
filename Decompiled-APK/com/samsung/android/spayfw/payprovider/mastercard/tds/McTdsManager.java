package com.samsung.android.spayfw.payprovider.mastercard.tds;

import android.os.Bundle;
import android.text.TextUtils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.RequestType;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.TdsRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestor;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsError;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils.ShaConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class McTdsManager {
    private static final String DEFAULT_TOKEN_REF = "ErrTokenRef";
    private static final String TAG = "McTdsManager";
    private static final String TDS_TAG_ERROR = "e__McTdsManager";
    private static final String TDS_TAG_INFO = "i__McTdsManager";
    private static final int TOKEN_SUFFIX_DISP_LEN = 8;
    private static final Map<Long, McTdsManager> mInstanceMap;
    private static final List<Long> sRegistrationPendingList;
    private static final Map<Long, RegistrationState> sRegistrationStatusMap;
    private String mRegCode1;
    private String mRegCode2;
    private String mResponseHostTds;
    private String mResponseHostTdsReg;
    private final AtomicLong mTokenId;
    private final String mTokenRefSuffix;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.1 */
    static /* synthetic */ class C05661 {
        static final /* synthetic */ int[] f13x8cd57be0;

        static {
            f13x8cd57be0 = new int[RegistrationState.values().length];
            try {
                f13x8cd57be0[RegistrationState.DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f13x8cd57be0[RegistrationState.NO_TDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f13x8cd57be0[RegistrationState.TDS_REGISTERED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f13x8cd57be0[RegistrationState.TDS_NOT_REGISTERED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum RegistrationState {
        DEFAULT(1),
        NO_TDS(2),
        TDS_NOT_REGISTERED(3),
        TDS_REGISTERED(4);
        
        private final int mValue;

        private RegistrationState(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    private int getTransactions(com.samsung.android.spayfw.payprovider.TransactionResponse r9, java.lang.String r10) {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x022f in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:58)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r8 = this;
        r1 = -2;
        r2 = 0;
        r0 = r8.mTokenId;
        if (r0 != 0) goto L_0x000f;
    L_0x0006:
        r0 = "e__McTdsManager";
        r2 = "getTransactions: Empty tokenId..Cant register tds";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
    L_0x000e:
        return r0;
    L_0x000f:
        r0 = new com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = com.samsung.android.spayfw.payprovider.mastercard.McProvider.getContext();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r8.mTokenId;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r3.get();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = r0.getData(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = (com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster) r0;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r0.getTokenUniqueReference();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = android.text.TextUtils.isEmpty(r3);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        if (r0 == 0) goto L_0x00a1;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
    L_0x002e:
        r0 = "e__McTdsManager";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3.<init>();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = "tokenId: ";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r8.mTokenId;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r4.get();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = " getTransactions: Err...TokenUniqueReference missing in DB. Cannot complete TDS fetch";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r3);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        if (r2 == 0) goto L_0x009e;
    L_0x0054:
        r0 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestor.sendRequest(r2);
        if (r0 != 0) goto L_0x007a;
    L_0x005a:
        r0 = "e__McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
        goto L_0x000e;
    L_0x007a:
        r0 = "McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r3 = " getTransactions: making fetchTransactionRequest";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r2);
    L_0x009e:
        r0 = r1;
        goto L_0x000e;
    L_0x00a1:
        r0 = new com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = com.samsung.android.spayfw.payprovider.mastercard.McProvider.getContext();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0.<init>(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r8.mTokenId;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r4.get();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = r0.getData(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = (com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData) r0;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r0.getAuthCode();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = android.text.TextUtils.isEmpty(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        if (r4 == 0) goto L_0x0134;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
    L_0x00c0:
        r0 = "e__McTdsManager";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3.<init>();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = "tokenId: ";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r8.mTokenId;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r4.get();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = " getTransactions: Auth Code missing in db. Cannot fetch transactions";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r3);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        if (r2 == 0) goto L_0x0131;
    L_0x00e6:
        r0 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestor.sendRequest(r2);
        if (r0 != 0) goto L_0x010d;
    L_0x00ec:
        r0 = "e__McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
        goto L_0x000e;
    L_0x010d:
        r0 = "McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r3 = " getTransactions: making fetchTransactionRequest";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r2);
    L_0x0131:
        r0 = r1;
        goto L_0x000e;
    L_0x0134:
        r4 = "McTdsManager";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5.<init>();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r6 = "tokenId: ";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r6 = r8.mTokenId;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r6 = r6.get();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r6 = " getTransactions: Using tdsUrl: ";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = r5.append(r10);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.RequestType.GET_TRANSACTIONS;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.build(r4, r0, r3, r9, r10);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        if (r0 == 0) goto L_0x01af;
    L_0x0164:
        r0 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestor.sendRequest(r0);
        if (r0 != 0) goto L_0x018b;
    L_0x016a:
        r0 = "e__McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
        goto L_0x000e;
    L_0x018b:
        r0 = "McTdsManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r8.mTokenId;
        r2 = r2.get();
        r1 = r1.append(r2);
        r2 = " getTransactions: making fetchTransactionRequest";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
    L_0x01af:
        r0 = 0;
        goto L_0x000e;
    L_0x01b2:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r3 = "e__McTdsManager";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4.<init>();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = "tokenId: ";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = r8.mTokenId;	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r6 = r5.get();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r4.append(r6);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r5 = " getTransactions: Err retriving TDS data from DB. Cannot register for TDS: ";	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = r0.getMessage();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = r4.append(r0);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        com.samsung.android.spayfw.p002b.Log.m286e(r3, r0);	 Catch:{ Exception -> 0x01b2, all -> 0x0232 }
        if (r2 == 0) goto L_0x022f;
    L_0x01e4:
        r0 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestor.sendRequest(r2);
        if (r0 != 0) goto L_0x020b;
    L_0x01ea:
        r0 = "e__McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
        goto L_0x000e;
    L_0x020b:
        r0 = "McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r3 = " getTransactions: making fetchTransactionRequest";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r2);
    L_0x022f:
        r0 = r1;
        goto L_0x000e;
    L_0x0232:
        r0 = move-exception;
        if (r2 == 0) goto L_0x0280;
    L_0x0235:
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestor.sendRequest(r2);
        if (r2 != 0) goto L_0x025c;
    L_0x023b:
        r0 = "e__McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
        goto L_0x000e;
    L_0x025c:
        r1 = "McTdsManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r3 = r8.mTokenId;
        r4 = r3.get();
        r2 = r2.append(r4);
        r3 = " getTransactions: making fetchTransactionRequest";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
    L_0x0280:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.getTransactions(com.samsung.android.spayfw.payprovider.i, java.lang.String):int");
    }

    static {
        mInstanceMap = new HashMap();
        sRegistrationStatusMap = new HashMap();
        sRegistrationPendingList = new ArrayList();
    }

    private McTdsManager(long j) {
        this.mTokenId = new AtomicLong(j);
        this.mTokenRefSuffix = getTokenSuffix();
        RegistrationState currentRegistrationState = getCurrentRegistrationState(j, true);
        if (currentRegistrationState == null) {
            currentRegistrationState = RegistrationState.DEFAULT;
        }
        Log.m286e(TDS_TAG_INFO, "tokenId: " + j + " init: " + currentRegistrationState.getValue());
        setRegistrationState(j, currentRegistrationState);
        if (currentRegistrationState == RegistrationState.TDS_NOT_REGISTERED) {
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + j + " add to List" + currentRegistrationState.getValue());
            addTokenToRegPendingList(Long.valueOf(j));
            McTdsTimerUtil.startTdsTimer();
        }
    }

    private String getTokenSuffix() {
        String tokenUniqueReference = ((McCardMaster) new McCardMasterDaoImpl(McProvider.getContext()).getData(this.mTokenId.get())).getTokenUniqueReference();
        if (!TextUtils.isEmpty(tokenUniqueReference)) {
            return tokenUniqueReference.substring(Math.max(tokenUniqueReference.length() - 8, 0));
        }
        Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " register: Err...TokenUniqueReference missing in DB. Cannot complete TDS registration");
        return DEFAULT_TOKEN_REF;
    }

    public static synchronized McTdsManager getInstance(long j) {
        McTdsManager mcTdsManager;
        synchronized (McTdsManager.class) {
            if (mInstanceMap.containsKey(Long.valueOf(j))) {
                mcTdsManager = (McTdsManager) mInstanceMap.get(Long.valueOf(j));
            } else {
                mcTdsManager = new McTdsManager(j);
                mInstanceMap.put(Long.valueOf(j), mcTdsManager);
            }
            Log.m287i(TDS_TAG_INFO, "tokenId: " + j + " Ref: " + mcTdsManager.mTokenRefSuffix);
        }
        return mcTdsManager;
    }

    private synchronized void setRegCode1(String str) {
        this.mRegCode1 = str;
    }

    private synchronized String getRegCode1() {
        return this.mRegCode1;
    }

    private synchronized void setRegCode2(String str) {
        this.mRegCode2 = str;
    }

    private synchronized String getRegCode2() {
        return this.mRegCode2;
    }

    private synchronized void cleanupRegCodes() {
        this.mRegCode1 = null;
        this.mRegCode2 = null;
    }

    public int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        if (this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "getTransactionData: Empty tokenId..Cant fetch tds");
            return -9;
        }
        String responseHostTds;
        Log.m287i(TDS_TAG_INFO, "getTransactionData: entryTime:" + System.currentTimeMillis());
        if (bundle == null || !bundle.containsKey("pushtransactionUrl") || TextUtils.isEmpty(bundle.getString("pushtransactionUrl"))) {
            responseHostTds = getResponseHostTds();
            if (!TextUtils.isEmpty(responseHostTds)) {
                setResponseHostTds(null);
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: responseHost from before ignored: ");
                Log.m285d(TAG, "getResponseHostTds: " + getResponseHostTds());
            }
            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: using responseHost : ");
            Log.m285d(TAG, "url: " + responseHostTds);
        } else {
            responseHostTds = bundle.getString("pushtransactionUrl");
            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: new tds URL obtained : ");
            Log.m285d(TAG, "url: " + responseHostTds);
        }
        if (bundle != null && bundle.containsKey("authorizationCode") && !TextUtils.isEmpty(bundle.getString("authorizationCode"))) {
            responseHostTds = bundle.getString("authorizationCode");
            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " getTransactionData: RegistrationCode2 received: ");
            Log.m285d(TDS_TAG_INFO, "RegistrationCode2: " + responseHostTds);
            return onRegistrationCode2(responseHostTds);
        } else if (transactionResponse != null) {
            return getTransactions(transactionResponse, responseHostTds);
        } else {
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " getTransactionData: callBack missing");
            return -4;
        }
    }

    public synchronized void register() {
        if (this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "register: Empty tokenId..Cant register tds");
        } else {
            try {
                McTdsMetaData mcTdsMetaData = (McTdsMetaData) new McTdsMetaDataDaoImpl(McProvider.getContext()).getData(this.mTokenId.get());
                String tokenUniqueReference = ((McCardMaster) new McCardMasterDaoImpl(McProvider.getContext()).getData(this.mTokenId.get())).getTokenUniqueReference();
                if (TextUtils.isEmpty(tokenUniqueReference)) {
                    Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " register: Err...TokenUniqueReference missing in DB. Cannot complete TDS registration");
                    if (null != null) {
                        if (McTdsRequestor.sendRequest(null)) {
                            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                        } else {
                            Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                        }
                    }
                } else if (TextUtils.isEmpty(getRegCode1()) && TextUtils.isEmpty(getRegCode2())) {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: Triggering getRegistrationCode");
                    r0 = McTdsRequestBuilder.build(RequestType.REGISTRATION_CODE, mcTdsMetaData, tokenUniqueReference, null, null);
                    if (r0 != null) {
                        if (McTdsRequestor.sendRequest(r0)) {
                            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                        } else {
                            Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                        }
                    }
                } else if (TextUtils.isEmpty(getRegCode1())) {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: Missing regCode1");
                    if (null != null) {
                        if (McTdsRequestor.sendRequest(null)) {
                            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                        } else {
                            Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                        }
                    }
                } else if (TextUtils.isEmpty(getRegCode2())) {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: Missing regCode2..");
                    if (null != null) {
                        if (McTdsRequestor.sendRequest(null)) {
                            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                        } else {
                            Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                        }
                    }
                } else {
                    Object generateHash = generateHash(getRegCode1(), getRegCode2());
                    if (TextUtils.isEmpty(generateHash)) {
                        Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + "register: Err...Could not generate Hash! Cannot complete TDS registration");
                        if (null != null) {
                            if (McTdsRequestor.sendRequest(null)) {
                                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                            } else {
                                Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                            }
                        }
                    } else {
                        mcTdsMetaData.setHash(generateHash);
                        cleanupRegCodes();
                        r0 = McTdsRequestBuilder.build(RequestType.REGISTER, mcTdsMetaData, tokenUniqueReference, null, getResponseHostTdsReg());
                        if (r0 != null) {
                            if (McTdsRequestor.sendRequest(r0)) {
                                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                            } else {
                                Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.m286e(TDS_TAG_ERROR, "register: Err retriving TDS data from DB. Cannot register for TDS: " + e.getMessage());
                if (null != null) {
                    if (McTdsRequestor.sendRequest(null)) {
                        Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                    } else {
                        Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                    }
                }
            } catch (Throwable th) {
                if (null != null) {
                    if (McTdsRequestor.sendRequest(null)) {
                        Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " register: making registration request");
                    } else {
                        Log.m286e(TDS_TAG_ERROR, "registration Err tokenId: " + this.mTokenId.get());
                    }
                }
            }
        }
    }

    private String generateHash(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Log.m286e(TDS_TAG_ERROR, "generateHash: Invalid params passed");
            return null;
        }
        try {
            String str3 = str + str2;
            Log.m285d(TAG, "generateHash: Combined code : " + str3);
            str3 = CryptoUtils.convertbyteToHexString(CryptoUtils.getShaDigest(str3, ShaConstants.SHA256));
            Log.m285d(TAG, "generateHash: Hash generated : " + str3);
            return str3;
        } catch (Exception e) {
            e.printStackTrace();
            Log.m286e(TDS_TAG_ERROR, "generateHash: Exception during hashing " + e.getMessage());
            return null;
        }
    }

    public void unRegister() {
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(McProvider.getContext());
        if (mcTdsMetaDataDaoImpl == null || this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "unRegister: Empty tokenId..Cant unRegister tds : " + mcTdsMetaDataDaoImpl);
            return;
        }
        try {
            Object tokenUniqueReference = ((McCardMaster) new McCardMasterDaoImpl(McProvider.getContext()).getData(this.mTokenId.get())).getTokenUniqueReference();
            if (TextUtils.isEmpty(tokenUniqueReference)) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + "unRegister: Err...TokenUniqueReference missing in DB. Cannot complete TDS unRegistration");
                mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
                if (mInstanceMap != null) {
                    mInstanceMap.remove(Long.valueOf(this.mTokenId.get()));
                }
                synchronized (McTdsManager.class) {
                    if (sRegistrationStatusMap != null) {
                        sRegistrationStatusMap.remove(Long.valueOf(this.mTokenId.get()));
                    }
                }
                if (null == null) {
                    return;
                }
                if (McTdsRequestor.sendRequest(null)) {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
                    return;
                } else {
                    Log.m286e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                    return;
                }
            }
            McTdsMetaData mcTdsMetaData = (McTdsMetaData) mcTdsMetaDataDaoImpl.getData(this.mTokenId.get());
            if (mcTdsMetaData == null || TextUtils.isEmpty(tokenUniqueReference) || TextUtils.isEmpty(mcTdsMetaData.getAuthCode())) {
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + "unRegister: TDS not registered..Nothing to do");
                mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
                if (mInstanceMap != null) {
                    mInstanceMap.remove(Long.valueOf(this.mTokenId.get()));
                }
                synchronized (McTdsManager.class) {
                    if (sRegistrationStatusMap != null) {
                        sRegistrationStatusMap.remove(Long.valueOf(this.mTokenId.get()));
                    }
                }
                if (null == null) {
                    return;
                }
                if (McTdsRequestor.sendRequest(null)) {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
                    return;
                } else {
                    Log.m286e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                    return;
                }
            }
            TdsRequest build = McTdsRequestBuilder.build(RequestType.UNREGISTER, mcTdsMetaData, tokenUniqueReference, null, getResponseHostTds());
            mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
            if (mInstanceMap != null) {
                mInstanceMap.remove(Long.valueOf(this.mTokenId.get()));
            }
            synchronized (McTdsManager.class) {
                if (sRegistrationStatusMap != null) {
                    sRegistrationStatusMap.remove(Long.valueOf(this.mTokenId.get()));
                }
            }
            if (build == null) {
                return;
            }
            if (McTdsRequestor.sendRequest(build)) {
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
            } else {
                Log.m286e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + "unRegister: Err retriving TDS data from DB. Cannot register for TDS: " + e.getMessage());
            mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
            if (mInstanceMap != null) {
                mInstanceMap.remove(Long.valueOf(this.mTokenId.get()));
            }
            synchronized (McTdsManager.class) {
            }
            if (sRegistrationStatusMap != null) {
                sRegistrationStatusMap.remove(Long.valueOf(this.mTokenId.get()));
            }
            if (null == null) {
                return;
            }
            if (McTdsRequestor.sendRequest(null)) {
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
            } else {
                Log.m286e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
            }
        } catch (Throwable th) {
            mcTdsMetaDataDaoImpl.deleteData(this.mTokenId.get());
            if (mInstanceMap != null) {
                mInstanceMap.remove(Long.valueOf(this.mTokenId.get()));
            }
            synchronized (McTdsManager.class) {
            }
            if (sRegistrationStatusMap != null) {
                sRegistrationStatusMap.remove(Long.valueOf(this.mTokenId.get()));
            }
            if (null != null) {
                if (McTdsRequestor.sendRequest(null)) {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " unRegister: making Unregistration request");
                } else {
                    Log.m286e(TDS_TAG_ERROR, "unregistration Err tokenId: " + this.mTokenId.get());
                }
            }
        }
    }

    private int onRegistrationCode2(String str) {
        if (this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "onRegistrationCode2: Empty tokenId..Cant process onRegistrationCode2");
            return -2;
        } else if (TextUtils.isEmpty(str)) {
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onAuthorizationCode2: Err...Input validation failed");
            return -4;
        } else {
            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " onRegistrationCode2: " + System.currentTimeMillis());
            setRegCode2(str);
            register();
            return 0;
        }
    }

    public void onRegisterCode1(String str, String str2) {
        if (this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "onRegisterCode1: Empty tokenId..Cant process onRegisterCode1");
            return;
        }
        try {
            if (TextUtils.isEmpty(str2)) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onRegisterCode1: Invalid RegistrationCode1 received");
                return;
            }
            if (!TextUtils.isEmpty(str)) {
                setResponseHostTdsReg(str);
            }
            setRegCode1(str2);
            register();
        } catch (Exception e) {
            e.printStackTrace();
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onRegisterCode1: Exception occured: " + e.getMessage());
        }
    }

    public void reRegisterIfNeeded(String str) {
        Object obj = null;
        if (this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "reRegisterIfNeeded: Empty tokenId..Cant process reRegisterIfNeeded");
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            McTdsError tdsError = McTdsError.getTdsError(str);
            if (tdsError != null && tdsError.getErrorCode().equalsIgnoreCase(McTdsError.INVALID_AUTHENTICATION_CODE.getErrorCode())) {
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " reRegisterIfNeeded: Auth expired");
                obj = 1;
            }
        }
        if (obj != null || registerReTry()) {
            cleanupRegCodes();
            Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " reRegisterIfNeeded: triggering re-register flow :");
            register();
        }
    }

    private static synchronized RegistrationState getCurrentRegistrationState(long j, boolean z) {
        RegistrationState tdsState;
        synchronized (McTdsManager.class) {
            if (z) {
                try {
                    tdsState = getTdsState((McTdsMetaData) new McTdsMetaDataDaoImpl(McProvider.getContext()).getData(j));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.m286e(TDS_TAG_ERROR, "checkForRegistrationReTry: Err : " + e.getMessage());
                    tdsState = null;
                }
            } else if (sRegistrationStatusMap != null) {
                tdsState = (RegistrationState) sRegistrationStatusMap.get(Long.valueOf(j));
            } else {
                tdsState = null;
            }
        }
        return tdsState;
    }

    public boolean registerReTry() {
        try {
            RegistrationState currentRegistrationState = getCurrentRegistrationState(this.mTokenId.get(), false);
            Log.m285d(TAG, "registerReTry:" + currentRegistrationState.name());
            if (currentRegistrationState == RegistrationState.TDS_NOT_REGISTERED) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.m286e(TDS_TAG_ERROR, "registerReTry: NPE" + e.getMessage());
            return false;
        }
    }

    public int onTokenStateUpdate(String str, String str2) {
        int revokeCheck = revokeCheck(str);
        if (revokeCheck != 0) {
            return revokeCheck;
        }
        revokeCheck = storeOrUpdateTdsMetaData(str, str2, true);
        if (revokeCheck != 0 || this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "onTokenStateUpdate: dbOperation failed: ");
            return revokeCheck;
        }
        RegistrationState currentRegistrationState = getCurrentRegistrationState(this.mTokenId.get(), false);
        if (currentRegistrationState == null) {
            return -2;
        }
        switch (C05661.f13x8cd57be0[currentRegistrationState.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case F2m.PPB /*3*/:
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " onTokenStateUpdate: tdsState: " + currentRegistrationState.name());
                return revokeCheck;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " onTokenStateUpdate: Triggering tds flow first time");
                reRegisterIfNeeded(null);
                return revokeCheck;
            default:
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " onTokenStateUpdate: invalid tdsState: " + currentRegistrationState.name());
                return revokeCheck;
        }
    }

    private int revokeCheck(String str) {
        if (!TextUtils.isEmpty(str)) {
            return 0;
        }
        if (this.mTokenId == null) {
            Log.m286e(TDS_TAG_ERROR, "revokeCheck: tokenId cannot be null");
            return -2;
        }
        RegistrationState currentRegistrationState = getCurrentRegistrationState(this.mTokenId.get(), true);
        if (currentRegistrationState == null) {
            Log.m286e(TDS_TAG_ERROR, "revokeCheck: Failed to get current state from DB");
            return -2;
        }
        switch (C05661.f13x8cd57be0[currentRegistrationState.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: No tds support. Nothing to do here" + currentRegistrationState.name());
                return 0;
            case F2m.PPB /*3*/:
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: revoking tdsState: " + currentRegistrationState.name());
                unRegister();
                return 0;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: tds not registered before. so delete entry");
                new McTdsMetaDataDaoImpl(McTokenManager.getContext()).deleteData(this.mTokenId.get());
                return 0;
            default:
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " revokeRevokeCheck: invalid tdsState: " + currentRegistrationState.name());
                return 0;
        }
    }

    private int storeOrUpdateTdsMetaData(String str, String str2, boolean z) {
        int i = 0;
        int i2 = -2;
        if (this.mTokenId == null || this.mTokenId.get() < 1 || TextUtils.isEmpty(str)) {
            Log.m285d(TAG, "Invalid TDS data received in token call");
            if (this.mTokenId != null) {
                Log.m285d(TAG, "Invalid TDS data received in token call: cardId : " + this.mTokenId.get());
            }
            Log.m285d(TAG, "Invalid TDS data received in token call: tdsRegisterUrl : " + str);
        } else {
            RegistrationState registrationState;
            McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(McTokenManager.getContext());
            McTdsMetaData mcTdsMetaData = (McTdsMetaData) mcTdsMetaDataDaoImpl.getData(this.mTokenId.get());
            if (mcTdsMetaData != null) {
                if (TextUtils.isEmpty(mcTdsMetaData.getAuthCode())) {
                    registrationState = RegistrationState.TDS_NOT_REGISTERED;
                } else {
                    registrationState = RegistrationState.TDS_REGISTERED;
                }
                if (!mcTdsMetaDataDaoImpl.updateUrl(str, this.mTokenId.get(), true)) {
                    i = -2;
                }
                i2 = i;
            } else if (TextUtils.isEmpty(str2)) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mTokenId.get() + " Invalid TDS data received in token call: paymentAppInstanceId : " + str2);
            } else {
                mcTdsMetaData = new McTdsMetaData();
                mcTdsMetaData.setCardMasterId(this.mTokenId.get());
                mcTdsMetaData.setTdsRegisterUrl(str);
                mcTdsMetaData.setPaymentAppInstanceId(str2);
                i2 = mcTdsMetaDataDaoImpl.saveData(mcTdsMetaData) != -1 ? 0 : -2;
                registrationState = RegistrationState.TDS_NOT_REGISTERED;
            }
            if (z) {
                setRegistrationState(this.mTokenId.get(), registrationState);
            }
        }
        return i2;
    }

    private static RegistrationState getTdsState(McTdsMetaData mcTdsMetaData) {
        if (mcTdsMetaData == null || TextUtils.isEmpty(mcTdsMetaData.getTdsRegisterUrl())) {
            return RegistrationState.NO_TDS;
        }
        if (TextUtils.isEmpty(mcTdsMetaData.getAuthCode())) {
            return RegistrationState.TDS_NOT_REGISTERED;
        }
        return RegistrationState.TDS_REGISTERED;
    }

    public synchronized String getResponseHostTdsReg() {
        return this.mResponseHostTdsReg;
    }

    public synchronized void setResponseHostTdsReg(String str) {
        this.mResponseHostTdsReg = str;
    }

    public synchronized String getResponseHostTds() {
        return this.mResponseHostTds;
    }

    public synchronized void setResponseHostTds(String str) {
        this.mResponseHostTds = str;
    }

    public static synchronized void setRegistrationState(long j, RegistrationState registrationState) {
        synchronized (McTdsManager.class) {
            if (registrationState != null) {
                if (sRegistrationStatusMap != null) {
                    sRegistrationStatusMap.put(Long.valueOf(j), registrationState);
                }
            }
        }
    }

    public static synchronized void addTokenToRegPendingList(Long l) {
        synchronized (McTdsManager.class) {
            if (l != null) {
                if (!sRegistrationPendingList.contains(l)) {
                    sRegistrationPendingList.add(l);
                }
            }
        }
    }

    public static synchronized List<Long> getRegList() {
        List<Long> list;
        synchronized (McTdsManager.class) {
            list = sRegistrationPendingList;
        }
        return list;
    }

    public static synchronized void deleteTokenFromRegPendingList(Long l) {
        synchronized (McTdsManager.class) {
            if (l != null) {
                sRegistrationPendingList.remove(l);
            }
        }
    }
}
