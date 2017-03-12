package com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback;

import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient.AsyncNetworkHttpClient;

public class McTdsRegisterCallbck implements AsyncNetworkHttpClient {
    private static final String TAG = "McTdsRegisterCallbck";
    private static final String TDS_TAG_ERROR = "e_McTdsRegisterCallbck";
    private static final String TDS_TAG_INFO = "i_McTdsRegisterCallbck";
    private final long mCardMasterId;

    public McTdsRegisterCallbck(long j) {
        this.mCardMasterId = j;
    }

    public void onComplete(int r11, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r12, byte[] r13) {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback.McTdsRegisterCallbck.onComplete(int, java.util.Map, byte[]):void. bs: [B:2:0x0029, B:16:0x010a, B:39:0x029b]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:57)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r10 = this;
        r2 = 0;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.McProvider.getContext();
        r4 = new com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;
        r4.<init>(r0);
        r1 = 1;
        r3 = "i_McTdsRegisterCallbck";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "onComplete: entryTime:";
        r5 = r5.append(r6);
        r6 = java.lang.System.currentTimeMillis();
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r3, r5);
        if (r0 != 0) goto L_0x007d;
    L_0x0029:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McTdsRegisterCallbck: Err. Context missing. Cannot store authCode in db";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
    L_0x007c:
        return;
    L_0x007d:
        r0 = "i_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r3.<init>();	 Catch:{ all -> 0x0605 }
        r5 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r3 = r3.append(r5);	 Catch:{ all -> 0x0605 }
        r6 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r3 = r3.append(r6);	 Catch:{ all -> 0x0605 }
        r5 = " McTdsRegisterCallbck: statusCode : ";	 Catch:{ all -> 0x0605 }
        r3 = r3.append(r5);	 Catch:{ all -> 0x0605 }
        r3 = r3.append(r11);	 Catch:{ all -> 0x0605 }
        r3 = r3.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r3);	 Catch:{ all -> 0x0605 }
        r0 = -36;	 Catch:{ all -> 0x0605 }
        switch(r11) {
            case 200: goto L_0x00fd;
            default: goto L_0x00a6;
        };	 Catch:{ all -> 0x0605 }
    L_0x00a6:
        r3 = r0;	 Catch:{ all -> 0x0605 }
    L_0x00a7:
        if (r13 != 0) goto L_0x00ff;	 Catch:{ all -> 0x0605 }
    L_0x00a9:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McTdsRegisterCallbck: responseData empty received";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x00fd:
        r3 = r2;
        goto L_0x00a7;
    L_0x00ff:
        r0 = new java.lang.String;	 Catch:{ all -> 0x0605 }
        r0.<init>(r13);	 Catch:{ all -> 0x0605 }
        r5 = new com.google.gson.Gson;	 Catch:{ all -> 0x0605 }
        r5.<init>();	 Catch:{ all -> 0x0605 }
        r6 = 0;
        r7 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsRegisterResponse.class;	 Catch:{ JsonSyntaxException -> 0x029a }
        r0 = r5.fromJson(r0, r7);	 Catch:{ JsonSyntaxException -> 0x029a }
        r0 = (com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsRegisterResponse) r0;	 Catch:{ JsonSyntaxException -> 0x029a }
        if (r0 != 0) goto L_0x0169;
    L_0x0114:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck: Error:  Empty payload";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x0169:
        r5 = r0.getErrorCode();	 Catch:{ all -> 0x0605 }
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0605 }
        if (r5 == 0) goto L_0x0175;	 Catch:{ all -> 0x0605 }
    L_0x0173:
        if (r3 == 0) goto L_0x01dc;	 Catch:{ all -> 0x0605 }
    L_0x0175:
        r2 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r4.<init>();	 Catch:{ all -> 0x0605 }
        r5 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0605 }
        r6 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x0605 }
        r5 = " McRegisterCallbck: ErrorCode: ";	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0605 }
        r0 = r0.getErrorCode();	 Catch:{ all -> 0x0605 }
        r0 = r4.append(r0);	 Catch:{ all -> 0x0605 }
        r4 = " resultCode:";	 Catch:{ all -> 0x0605 }
        r0 = r0.append(r4);	 Catch:{ all -> 0x0605 }
        r0 = r0.append(r3);	 Catch:{ all -> 0x0605 }
        r0 = r0.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r0);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x01dc:
        r3 = r0.getAuthenticationCode();	 Catch:{ all -> 0x0605 }
        r3 = android.text.TextUtils.isEmpty(r3);	 Catch:{ all -> 0x0605 }
        if (r3 == 0) goto L_0x023b;	 Catch:{ all -> 0x0605 }
    L_0x01e6:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck:Auth Code missing missing in response ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x023b:
        r3 = r0.getTdsUrl();	 Catch:{ all -> 0x0605 }
        r3 = android.text.TextUtils.isEmpty(r3);	 Catch:{ all -> 0x0605 }
        if (r3 == 0) goto L_0x063c;	 Catch:{ all -> 0x0605 }
    L_0x0245:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck:TdsUrl missing missing in response ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x029a:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x047b }
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x047b }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x047b }
        r2.<init>();	 Catch:{ all -> 0x047b }
        r4 = "tokenId: ";	 Catch:{ all -> 0x047b }
        r2 = r2.append(r4);	 Catch:{ all -> 0x047b }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x047b }
        r2 = r2.append(r4);	 Catch:{ all -> 0x047b }
        r4 = " McTdsRegisterCallbck: JsonSyntaxException";	 Catch:{ all -> 0x047b }
        r2 = r2.append(r4);	 Catch:{ all -> 0x047b }
        r2 = r2.toString();	 Catch:{ all -> 0x047b }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x047b }
        if (r6 != 0) goto L_0x0315;
    L_0x02c0:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck: Error:  Empty payload";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x0315:
        r0 = r6.getErrorCode();	 Catch:{ all -> 0x0605 }
        r0 = android.text.TextUtils.isEmpty(r0);	 Catch:{ all -> 0x0605 }
        if (r0 == 0) goto L_0x0321;	 Catch:{ all -> 0x0605 }
    L_0x031f:
        if (r3 == 0) goto L_0x0388;	 Catch:{ all -> 0x0605 }
    L_0x0321:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r4 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = " McRegisterCallbck: ErrorCode: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = r6.getErrorCode();	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = " resultCode:";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x0388:
        r0 = r6.getAuthenticationCode();	 Catch:{ all -> 0x0605 }
        r0 = android.text.TextUtils.isEmpty(r0);	 Catch:{ all -> 0x0605 }
        if (r0 == 0) goto L_0x03e7;	 Catch:{ all -> 0x0605 }
    L_0x0392:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck:Auth Code missing missing in response ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x03e7:
        r0 = r6.getTdsUrl();	 Catch:{ all -> 0x0605 }
        r0 = android.text.TextUtils.isEmpty(r0);	 Catch:{ all -> 0x0605 }
        if (r0 == 0) goto L_0x0446;	 Catch:{ all -> 0x0605 }
    L_0x03f1:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck:TdsUrl missing missing in response ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x0446:
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x047b:
        r0 = move-exception;
        if (r6 != 0) goto L_0x04d3;
    L_0x047e:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck: Error:  Empty payload";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x04d3:
        r2 = r6.getErrorCode();	 Catch:{ all -> 0x0605 }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x0605 }
        if (r2 == 0) goto L_0x04df;	 Catch:{ all -> 0x0605 }
    L_0x04dd:
        if (r3 == 0) goto L_0x0546;	 Catch:{ all -> 0x0605 }
    L_0x04df:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r4 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = " McRegisterCallbck: ErrorCode: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = r6.getErrorCode();	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r4 = " resultCode:";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x0546:
        r2 = r6.getAuthenticationCode();	 Catch:{ all -> 0x0605 }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x0605 }
        if (r2 == 0) goto L_0x05a5;	 Catch:{ all -> 0x0605 }
    L_0x0550:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck:Auth Code missing missing in response ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x05a5:
        r2 = r6.getTdsUrl();	 Catch:{ all -> 0x0605 }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x0605 }
        if (r2 == 0) goto L_0x0604;	 Catch:{ all -> 0x0605 }
    L_0x05af:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r2.<init>();	 Catch:{ all -> 0x0605 }
        r3 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x0605 }
        r3 = " McRegisterCallbck:TdsUrl missing missing in response ";	 Catch:{ all -> 0x0605 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0605 }
        r2 = r2.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x0605 }
        r0 = "e_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: FAILED making re-try";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r10.mCardMasterId;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r0, r2);
        r0 = r10.mCardMasterId;
        r0 = java.lang.Long.valueOf(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r0);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
        goto L_0x007c;
    L_0x0604:
        throw r0;	 Catch:{ all -> 0x0605 }
    L_0x0605:
        r0 = move-exception;
    L_0x0606:
        if (r1 == 0) goto L_0x0769;
    L_0x0608:
        r1 = "e_McTdsRegisterCallbck";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r4 = r10.mCardMasterId;
        r2 = r2.append(r4);
        r3 = " registration: FAILED making re-try";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        r2 = r10.mCardMasterId;
        r1 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_NOT_REGISTERED;
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r2, r1);
        r2 = r10.mCardMasterId;
        r1 = java.lang.Long.valueOf(r2);
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.addTokenToRegPendingList(r1);
        com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.startTdsTimer();
    L_0x063b:
        throw r0;
    L_0x063c:
        r3 = r0.getAuthenticationCode();	 Catch:{ all -> 0x0605 }
        r5 = "McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r6.<init>();	 Catch:{ all -> 0x0605 }
        r7 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r6 = r6.append(r7);	 Catch:{ all -> 0x0605 }
        r8 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r6 = r6.append(r8);	 Catch:{ all -> 0x0605 }
        r7 = " McTdsRegisterCallbck:AuthenticationCode Received: ";	 Catch:{ all -> 0x0605 }
        r6 = r6.append(r7);	 Catch:{ all -> 0x0605 }
        r6 = r6.append(r3);	 Catch:{ all -> 0x0605 }
        r6 = r6.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m285d(r5, r6);	 Catch:{ all -> 0x0605 }
        r5 = r0.getTdsUrl();	 Catch:{ all -> 0x0605 }
        r6 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0605 }
        if (r6 != 0) goto L_0x0692;	 Catch:{ all -> 0x0605 }
    L_0x066e:
        r6 = "McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r7.<init>();	 Catch:{ all -> 0x0605 }
        r8 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r7 = r7.append(r8);	 Catch:{ all -> 0x0605 }
        r8 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r7 = r7.append(r8);	 Catch:{ all -> 0x0605 }
        r8 = " McRegisterCallbck: tdsUrl Received: ";	 Catch:{ all -> 0x0605 }
        r7 = r7.append(r8);	 Catch:{ all -> 0x0605 }
        r7 = r7.append(r5);	 Catch:{ all -> 0x0605 }
        r7 = r7.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m285d(r6, r7);	 Catch:{ all -> 0x0605 }
    L_0x0692:
        r6 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r3 = r4.storeAuthCodeWithUrl(r3, r6, r5);	 Catch:{ all -> 0x0605 }
        if (r3 == 0) goto L_0x071e;	 Catch:{ all -> 0x0605 }
    L_0x069a:
        r3 = "i_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r4.<init>();	 Catch:{ all -> 0x0605 }
        r5 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0605 }
        r6 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x0605 }
        r5 = " McTdsRegisterCallbck:AuthenticationCode Saved: ";	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0605 }
        r4 = r4.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m287i(r3, r4);	 Catch:{ all -> 0x0605 }
    L_0x06ba:
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r3 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.RegistrationState.TDS_REGISTERED;	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.setRegistrationState(r4, r3);	 Catch:{ all -> 0x0605 }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r3 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.deleteTokenFromRegPendingList(r3);	 Catch:{ all -> 0x0605 }
        r1 = r0.getResponseHost();	 Catch:{ all -> 0x078b }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ all -> 0x078b }
        if (r1 != 0) goto L_0x0747;	 Catch:{ all -> 0x078b }
    L_0x06d4:
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x078b }
        r1 = com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager.getInstance(r4);	 Catch:{ all -> 0x078b }
        if (r1 != 0) goto L_0x0740;	 Catch:{ all -> 0x078b }
    L_0x06dc:
        r0 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x078b }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x078b }
        r1.<init>();	 Catch:{ all -> 0x078b }
        r3 = "tokenId: ";	 Catch:{ all -> 0x078b }
        r1 = r1.append(r3);	 Catch:{ all -> 0x078b }
        r4 = r10.mCardMasterId;	 Catch:{ all -> 0x078b }
        r1 = r1.append(r4);	 Catch:{ all -> 0x078b }
        r3 = " McTdsRegisterCallbck: tdsManager is null. Cannot save responseHost :";	 Catch:{ all -> 0x078b }
        r1 = r1.append(r3);	 Catch:{ all -> 0x078b }
        r1 = r1.toString();	 Catch:{ all -> 0x078b }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x078b }
        r0 = "i_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: SUCCESS";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        goto L_0x007c;
    L_0x071e:
        r3 = "e_McTdsRegisterCallbck";	 Catch:{ all -> 0x0605 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0605 }
        r4.<init>();	 Catch:{ all -> 0x0605 }
        r5 = "tokenId: ";	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0605 }
        r6 = r10.mCardMasterId;	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x0605 }
        r5 = " McTdsRegisterCallbck:AuthenticationCode Not saved in db: ";	 Catch:{ all -> 0x0605 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0605 }
        r4 = r4.toString();	 Catch:{ all -> 0x0605 }
        com.samsung.android.spayfw.p002b.Log.m286e(r3, r4);	 Catch:{ all -> 0x0605 }
        goto L_0x06ba;
    L_0x0740:
        r0 = r0.getResponseHost();	 Catch:{ all -> 0x078b }
        r1.setResponseHostTds(r0);	 Catch:{ all -> 0x078b }
    L_0x0747:
        r0 = "i_McTdsRegisterCallbck";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "tokenId: ";
        r1 = r1.append(r2);
        r2 = r10.mCardMasterId;
        r1 = r1.append(r2);
        r2 = " registration: SUCCESS";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        goto L_0x007c;
    L_0x0769:
        r1 = "i_McTdsRegisterCallbck";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "tokenId: ";
        r2 = r2.append(r3);
        r4 = r10.mCardMasterId;
        r2 = r2.append(r4);
        r3 = " registration: SUCCESS";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
        goto L_0x063b;
    L_0x078b:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0606;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback.McTdsRegisterCallbck.onComplete(int, java.util.Map, byte[]):void");
    }
}
