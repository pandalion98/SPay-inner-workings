package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData.ClearDataDGI;
import com.samsung.android.spayfw.payprovider.mastercard.payload.McPayloadBuilder;

public class MCUnusedDGIElements {
    private String a002Accumulator1ControlContactless;
    private String a002Accumulator1ControlManagement;
    private String a002Accumulator1CurrencyCode;
    private String a002Accumulator1CurrencyConversionTable;
    private String a002Accumulator1LowerLimit;
    private String a002Accumulator1UpperLimit;
    private String a002Accumulator2ControlContactless;
    private String a002Accumulator2ControlManagement;
    private String a002Accumulator2CurrencyCode;
    private String a002Accumulator2CurrencyConversionTable;
    private String a002Accumulator2LowerLimit;
    private String a002Accumulator2UpperLimit;
    private String a002AckResetTimeout;
    private String a002ActivateManagementModeInformation;
    private String a002ApplicationControlContactless;
    private String a002ApplicationCotrol;
    private String a002CiacDeclineOfflineContactless;
    private String a002CiacDeclineOfflineManagement;
    private String a002CiacDeclineOnlineManagement;
    private String a002CiacDeclineonlineContactless;
    private String a002CiacGoOnlineContactless;
    private String a002CiacGoOnlineManagement;
    private String a002Counter1Control;
    private String a002Counter1ControlContactless;
    private String a002Counter1LowerLimit;
    private String a002Counter1UpperLimit;
    private String a002Counter2Control;
    private String a002Counter2ControlContactless;
    private String a002Counter2LowerLimit;
    private String a002Counter2UpperLimit;
    private String a002MchipCvmCardHolderContactless;
    private String a002MchipCvmIssuerOptionsContactless;
    private String a002OfflineChangePinRequired;
    private String a002SecurityWorld;
    private String a002Wcota;
    private String a002Wcotn;
    private String a003MagstripeCvmCardHolderOptions;
    private String a003MagstripeCvmIssuerOptions;
    private String a404Accumulator1ControlAlternateCl;
    private String a404Accumulator2ControlAlternateCl;
    private String a404ApplicationControlAlternateCl;
    private String a404Cdol1RelatedDataLengthAlternateCl;
    private String a404CiacDeclineOfflineAlternateCl;
    private String a404CiacDeclineOnlineAlternateCl;
    private String a404CiacGoOnlineAlternateCl;
    private String a404Counter1ControlAlternateCl;
    private String a404Counter2ControlAlternateCl;
    private String a404CvrIssuerDiscretionaryDataAlternateCl;
    private String a404ReadRecordFilterAlternateCl;
    private String a502AcSessionKeyCounterLimitRemote;
    private String a502SmiSessionKeyCounterLimitRemote;
    private String a504AcSessionKeyCounterLimitAlternateCl;
    private String a504KeyDerivationIndexAlternateCl;
    private String a504SmiSessoinKeyCounterLimitAlternateCl;
    private String b007AflRemote;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements.1 */
    static /* synthetic */ class C05641 {
        static final /* synthetic */ int[] f12x236d376b;

        static {
            f12x236d376b = new int[ClearDataDGI.values().length];
            try {
                f12x236d376b[ClearDataDGI.DGIA002.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f12x236d376b[ClearDataDGI.DGIA003.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f12x236d376b[ClearDataDGI.DGIA404.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f12x236d376b[ClearDataDGI.DGIA502.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f12x236d376b[ClearDataDGI.DGIA504.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f12x236d376b[ClearDataDGI.DGIB007.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MCUnusedDGIElements(java.util.Map<com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData.ClearDataDGI, byte[]> r10) {
        /*
        r9 = this;
        r8 = 2;
        r9.<init>();
        r0 = "mcpce_MCUnusedDGIElements";
        r0 = r10.entrySet();
        r2 = r0.iterator();
    L_0x000e:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x02be;
    L_0x0014:
        r0 = r2.next();
        r0 = (java.util.Map.Entry) r0;
        if (r0 == 0) goto L_0x000e;
    L_0x001c:
        r1 = r0.getValue();
        r1 = (byte[]) r1;
        r3 = r1.length;
        if (r3 <= r8) goto L_0x000e;
    L_0x0025:
        r3 = r1[r8];
        r3 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.unsignedByteToInt(r3);
        r4 = 3;
        r5 = "mcpce_MCUnusedDGIElements";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Unused Dgi element : inpute dgi type : ";
        r6 = r6.append(r7);
        r7 = r0.getKey();
        r6 = r6.append(r7);
        r7 = " , size :";
        r6 = r6.append(r7);
        r7 = " ";
        r6 = r6.append(r7);
        r6 = r6.append(r3);
        r7 = ", contents :";
        r6 = r6.append(r7);
        r7 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteArrayToHex(r1);
        r6 = r6.append(r7);
        r6 = r6.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r5, r6);
        r5 = com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements.C05641.f12x236d376b;	 Catch:{ Exception -> 0x01bf }
        r0 = r0.getKey();	 Catch:{ Exception -> 0x01bf }
        r0 = (com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData.ClearDataDGI) r0;	 Catch:{ Exception -> 0x01bf }
        r0 = r0.ordinal();	 Catch:{ Exception -> 0x01bf }
        r0 = r5[r0];	 Catch:{ Exception -> 0x01bf }
        switch(r0) {
            case 1: goto L_0x0078;
            case 2: goto L_0x01c5;
            case 3: goto L_0x01d6;
            case 4: goto L_0x0239;
            case 5: goto L_0x027f;
            case 6: goto L_0x0299;
            default: goto L_0x0077;
        };	 Catch:{ Exception -> 0x01bf }
    L_0x0077:
        goto L_0x000e;
    L_0x0078:
        r0 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r0, r1, r4);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator1CurrencyCode = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 5;
        r3 = 25;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator1CurrencyConversionTable = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 30;
        r3 = 6;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator1LowerLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 36;
        r3 = 6;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator1UpperLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 42;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator2CurrencyCode = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 44;
        r3 = 25;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator2CurrencyConversionTable = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 69;
        r3 = 6;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator2LowerLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 75;
        r3 = 6;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator2UpperLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 100;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter1LowerLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter1UpperLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter2LowerLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter2UpperLimit = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 106; // 0x6a float:1.49E-43 double:5.24E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator1ControlManagement = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator2ControlManagement = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r3 = 4;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002ApplicationCotrol = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter1Control = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter2Control = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator1ControlContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Accumulator2ControlContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        r3 = 4;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002ApplicationControlContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 126; // 0x7e float:1.77E-43 double:6.23E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter1ControlContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Counter2ControlContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 130; // 0x82 float:1.82E-43 double:6.4E-322;
        r3 = 16;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002SecurityWorld = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 146; // 0x92 float:2.05E-43 double:7.2E-322;
        r3 = 20;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002ActivateManagementModeInformation = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 166; // 0xa6 float:2.33E-43 double:8.2E-322;
        r3 = 6;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Wcota = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 172; // 0xac float:2.41E-43 double:8.5E-322;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002Wcotn = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 179; // 0xb3 float:2.51E-43 double:8.84E-322;
        r3 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002CiacDeclineOfflineManagement = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 182; // 0xb6 float:2.55E-43 double:9.0E-322;
        r3 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002CiacDeclineOfflineContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 185; // 0xb9 float:2.59E-43 double:9.14E-322;
        r3 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002CiacDeclineOnlineManagement = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 188; // 0xbc float:2.63E-43 double:9.3E-322;
        r3 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002CiacDeclineonlineContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 191; // 0xbf float:2.68E-43 double:9.44E-322;
        r3 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002CiacGoOnlineManagement = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 194; // 0xc2 float:2.72E-43 double:9.6E-322;
        r3 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002CiacGoOnlineContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 198; // 0xc6 float:2.77E-43 double:9.8E-322;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002MchipCvmCardHolderContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 199; // 0xc7 float:2.79E-43 double:9.83E-322;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002MchipCvmIssuerOptionsContactless = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002AckResetTimeout = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 202; // 0xca float:2.83E-43 double:1.0E-321;
        r3 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a002OfflineChangePinRequired = r0;	 Catch:{ Exception -> 0x01bf }
        goto L_0x000e;
    L_0x01bf:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x000e;
    L_0x01c5:
        r0 = 1;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r0, r1, r4);	 Catch:{ Exception -> 0x01bf }
        r9.a003MagstripeCvmCardHolderOptions = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 1;
        r3 = 4;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r0, r1, r3);	 Catch:{ Exception -> 0x01bf }
        r9.a003MagstripeCvmIssuerOptions = r0;	 Catch:{ Exception -> 0x01bf }
        goto L_0x000e;
    L_0x01d6:
        r0 = r1[r4];	 Catch:{ Exception -> 0x01bf }
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteToHex(r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404Cdol1RelatedDataLengthAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 4;
        r0 = r1[r0];	 Catch:{ Exception -> 0x01bf }
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteToHex(r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404CvrIssuerDiscretionaryDataAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 5;
        r4 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404Accumulator1ControlAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 7;
        r4 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404Accumulator2ControlAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 9;
        r4 = 6;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404ApplicationControlAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 15;
        r4 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404Counter1ControlAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 17;
        r4 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404Counter2ControlAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 22;
        r4 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404CiacDeclineOfflineAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 25;
        r4 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404CiacDeclineOnlineAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 28;
        r4 = 3;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r4, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404CiacGoOnlineAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 31;
        r3 = r3 + -28;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a404ReadRecordFilterAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        goto L_0x000e;
    L_0x0239:
        r0 = 4;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a502AcSessionKeyCounterLimitRemote = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 6;
        r3 = "mcpce_MCUnusedDGIElements";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01bf }
        r4.<init>();	 Catch:{ Exception -> 0x01bf }
        r5 = "a502RemotePaymentAcSessionKeyCounterLimit : ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01bf }
        r5 = r9.a502AcSessionKeyCounterLimitRemote;	 Catch:{ Exception -> 0x01bf }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x01bf }
        r4 = r4.toString();	 Catch:{ Exception -> 0x01bf }
        com.samsung.android.spayfw.p002b.Log.m285d(r3, r4);	 Catch:{ Exception -> 0x01bf }
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a502SmiSessionKeyCounterLimitRemote = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = "mcpce_MCUnusedDGIElements";
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01bf }
        r1.<init>();	 Catch:{ Exception -> 0x01bf }
        r3 = "a502RemotePaymentSmiSessionKeyCounterLimit : ";
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x01bf }
        r3 = r9.a502SmiSessionKeyCounterLimitRemote;	 Catch:{ Exception -> 0x01bf }
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x01bf }
        r1 = r1.toString();	 Catch:{ Exception -> 0x01bf }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ Exception -> 0x01bf }
        goto L_0x000e;
    L_0x027f:
        r0 = r1[r4];	 Catch:{ Exception -> 0x01bf }
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteToHex(r0);	 Catch:{ Exception -> 0x01bf }
        r9.a504KeyDerivationIndexAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 4;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a504AcSessionKeyCounterLimitAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = 6;
        r3 = 2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.a504SmiSessoinKeyCounterLimitAlternateCl = r0;	 Catch:{ Exception -> 0x01bf }
        goto L_0x000e;
    L_0x0299:
        r0 = 5;
        r3 = r3 + -2;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.copySrcToDes(r3, r1, r0);	 Catch:{ Exception -> 0x01bf }
        r9.b007AflRemote = r0;	 Catch:{ Exception -> 0x01bf }
        r0 = "mcpce_MCUnusedDGIElements";
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01bf }
        r1.<init>();	 Catch:{ Exception -> 0x01bf }
        r3 = "b007RemotePaymentAfl : ";
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x01bf }
        r3 = r9.b007AflRemote;	 Catch:{ Exception -> 0x01bf }
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x01bf }
        r1 = r1.toString();	 Catch:{ Exception -> 0x01bf }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ Exception -> 0x01bf }
        goto L_0x000e;
    L_0x02be:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements.<init>(java.util.Map):void");
    }

    public void toCopy(MCUnusedDGIElements mCUnusedDGIElements) {
        if (mCUnusedDGIElements == null) {
            Log.m286e("MCUnusedDGIElements", "source is null");
            return;
        }
        this.a002Accumulator1CurrencyCode = mCUnusedDGIElements.getA002Accumulator1CurrencyCode();
        this.a002Accumulator1CurrencyConversionTable = mCUnusedDGIElements.getA002Accumulator1CurrencyConversionTable();
        this.a002Accumulator1LowerLimit = mCUnusedDGIElements.getA002Accumulator1LowerLimit();
        this.a002Accumulator1UpperLimit = mCUnusedDGIElements.getA002Accumulator1UpperLimit();
        this.a002Accumulator2CurrencyCode = mCUnusedDGIElements.getA002Accumulator2CurrencyCode();
        this.a002Accumulator2CurrencyConversionTable = mCUnusedDGIElements.getA002Accumulator2CurrencyConversionTable();
        this.a002Accumulator2LowerLimit = mCUnusedDGIElements.getA002Accumulator2LowerLimit();
        this.a002Accumulator2UpperLimit = mCUnusedDGIElements.getA002Accumulator2UpperLimit();
        this.a002Counter1LowerLimit = mCUnusedDGIElements.getA002Counter1LowerLimit();
        this.a002Counter1UpperLimit = mCUnusedDGIElements.getA002Counter1UpperLimit();
        this.a002Counter2LowerLimit = mCUnusedDGIElements.getA002Counter2LowerLimit();
        this.a002Counter2UpperLimit = mCUnusedDGIElements.getA002Counter2UpperLimit();
        this.a002Accumulator1ControlManagement = mCUnusedDGIElements.getA002Accumulator1ControlManagement();
        this.a002Accumulator2ControlManagement = mCUnusedDGIElements.getA002Accumulator2ControlManagement();
        this.a002ApplicationCotrol = mCUnusedDGIElements.getA002ApplicationCotrol();
        this.a002Counter1Control = mCUnusedDGIElements.getA002Counter1Control();
        this.a002Counter2Control = mCUnusedDGIElements.getA002Counter2Control();
        this.a002Accumulator1ControlContactless = mCUnusedDGIElements.getA002Accumulator1ControlContactless();
        this.a002Accumulator2ControlContactless = mCUnusedDGIElements.getA002Accumulator2ControlContactless();
        this.a002ApplicationControlContactless = mCUnusedDGIElements.getA002ApplicationControlContactless();
        this.a002Counter1ControlContactless = mCUnusedDGIElements.getA002Counter1ControlContactless();
        this.a002Counter2ControlContactless = mCUnusedDGIElements.getA002Counter2ControlContactless();
        this.a002SecurityWorld = mCUnusedDGIElements.getA002SecurityWorld();
        this.a002ActivateManagementModeInformation = mCUnusedDGIElements.getA002ActivateManagementModeInformation();
        this.a002Wcota = mCUnusedDGIElements.getA002Wcota();
        this.a002Wcotn = mCUnusedDGIElements.getA002Wcotn();
        this.a002CiacDeclineOfflineManagement = mCUnusedDGIElements.getA002CiacDeclineOfflineManagement();
        this.a002CiacDeclineOfflineContactless = mCUnusedDGIElements.getA002CiacDeclineOfflineContactless();
        this.a002CiacDeclineOnlineManagement = mCUnusedDGIElements.getA002CiacDeclineOnlineManagement();
        this.a002CiacDeclineonlineContactless = mCUnusedDGIElements.getA002CiacDeclineonlineContactless();
        this.a002CiacGoOnlineManagement = mCUnusedDGIElements.getA002CiacGoOnlineManagement();
        this.a002CiacGoOnlineContactless = mCUnusedDGIElements.getA002CiacGoOnlineContactless();
        this.a002MchipCvmCardHolderContactless = mCUnusedDGIElements.getA002MchipCvmCardHolderContactless();
        this.a002MchipCvmIssuerOptionsContactless = mCUnusedDGIElements.getA002MchipCvmIssuerOptionsContactless();
        this.a002AckResetTimeout = mCUnusedDGIElements.getA002AckResetTimeout();
        this.a002OfflineChangePinRequired = mCUnusedDGIElements.getA002OfflineChangePinRequired();
        this.a003MagstripeCvmCardHolderOptions = mCUnusedDGIElements.getA003MagstripeCvmCardHolderOptions();
        this.a003MagstripeCvmIssuerOptions = mCUnusedDGIElements.getA003MagstripeCvmIssuerOptions();
        this.a404Cdol1RelatedDataLengthAlternateCl = mCUnusedDGIElements.getA404Cdol1RelatedDataLengthAlternateCl();
        this.a404CvrIssuerDiscretionaryDataAlternateCl = mCUnusedDGIElements.getA404CvrIssuerDiscretionaryDataAlternateCl();
        this.a404Accumulator1ControlAlternateCl = mCUnusedDGIElements.getA404Accumulator1ControlAlternateCl();
        this.a404Accumulator2ControlAlternateCl = mCUnusedDGIElements.getA404Accumulator2ControlAlternateCl();
        this.a404ApplicationControlAlternateCl = mCUnusedDGIElements.getA404ApplicationControlAlternateCl();
        this.a404Counter1ControlAlternateCl = mCUnusedDGIElements.getA404Counter1ControlAlternateCl();
        this.a404Counter2ControlAlternateCl = mCUnusedDGIElements.getA404Counter2ControlAlternateCl();
        this.a404CiacDeclineOfflineAlternateCl = mCUnusedDGIElements.getA404CiacDeclineOfflineAlternateCl();
        this.a404CiacDeclineOnlineAlternateCl = mCUnusedDGIElements.getA404CiacDeclineOnlineAlternateCl();
        this.a404CiacGoOnlineAlternateCl = mCUnusedDGIElements.getA404CiacGoOnlineAlternateCl();
        this.a404ReadRecordFilterAlternateCl = mCUnusedDGIElements.getA404ReadRecordFilterAlternateCl();
        this.a502AcSessionKeyCounterLimitRemote = mCUnusedDGIElements.getA502AcSessionKeyCounterLimitRemote();
        this.a502SmiSessionKeyCounterLimitRemote = mCUnusedDGIElements.getA502SmiSessionKeyCounterLimitRemote();
        this.a504KeyDerivationIndexAlternateCl = mCUnusedDGIElements.getA504KeyDerivationIndexAlternateCl();
        this.a504AcSessionKeyCounterLimitAlternateCl = mCUnusedDGIElements.getA504AcSessionKeyCounterLimitAlternateCl();
        this.a504SmiSessoinKeyCounterLimitAlternateCl = mCUnusedDGIElements.getA504SmiSessoinKeyCounterLimitAlternateCl();
        this.b007AflRemote = mCUnusedDGIElements.getB007AflRemote();
    }

    public String toString() {
        return McPayloadBuilder.toString(this);
    }

    public String getA002Accumulator1CurrencyCode() {
        return this.a002Accumulator1CurrencyCode;
    }

    public String getA002Accumulator1CurrencyConversionTable() {
        return this.a002Accumulator1CurrencyConversionTable;
    }

    public String getA002Accumulator1LowerLimit() {
        return this.a002Accumulator1LowerLimit;
    }

    public String getA002Accumulator1UpperLimit() {
        return this.a002Accumulator1UpperLimit;
    }

    public String getA002Accumulator2CurrencyCode() {
        return this.a002Accumulator2CurrencyCode;
    }

    public String getA002Accumulator2CurrencyConversionTable() {
        return this.a002Accumulator2CurrencyConversionTable;
    }

    public String getA002Accumulator2LowerLimit() {
        return this.a002Accumulator2LowerLimit;
    }

    public String getA002Accumulator2UpperLimit() {
        return this.a002Accumulator2UpperLimit;
    }

    public String getA002Counter1LowerLimit() {
        return this.a002Counter1LowerLimit;
    }

    public String getA002Counter1UpperLimit() {
        return this.a002Counter1UpperLimit;
    }

    public String getA002Counter2LowerLimit() {
        return this.a002Counter2LowerLimit;
    }

    public String getA002Counter2UpperLimit() {
        return this.a002Counter2UpperLimit;
    }

    public String getA002Accumulator1ControlManagement() {
        return this.a002Accumulator1ControlManagement;
    }

    public String getA002Accumulator2ControlManagement() {
        return this.a002Accumulator2ControlManagement;
    }

    public String getA002ApplicationCotrol() {
        return this.a002ApplicationCotrol;
    }

    public String getA002Counter1Control() {
        return this.a002Counter1Control;
    }

    public String getA002Counter2Control() {
        return this.a002Counter2Control;
    }

    public String getA002Accumulator1ControlContactless() {
        return this.a002Accumulator1ControlContactless;
    }

    public String getA002Accumulator2ControlContactless() {
        return this.a002Accumulator2ControlContactless;
    }

    public String getA002ApplicationControlContactless() {
        return this.a002ApplicationControlContactless;
    }

    public String getA002Counter1ControlContactless() {
        return this.a002Counter1ControlContactless;
    }

    public String getA002Counter2ControlContactless() {
        return this.a002Counter2ControlContactless;
    }

    public String getA002SecurityWorld() {
        return this.a002SecurityWorld;
    }

    public String getA002ActivateManagementModeInformation() {
        return this.a002ActivateManagementModeInformation;
    }

    public String getA002Wcota() {
        return this.a002Wcota;
    }

    public String getA002Wcotn() {
        return this.a002Wcotn;
    }

    public String getA002CiacDeclineOfflineManagement() {
        return this.a002CiacDeclineOfflineManagement;
    }

    public String getA002CiacDeclineOfflineContactless() {
        return this.a002CiacDeclineOfflineContactless;
    }

    public String getA002CiacDeclineOnlineManagement() {
        return this.a002CiacDeclineOnlineManagement;
    }

    public String getA002CiacDeclineonlineContactless() {
        return this.a002CiacDeclineonlineContactless;
    }

    public String getA002CiacGoOnlineManagement() {
        return this.a002CiacGoOnlineManagement;
    }

    public String getA002CiacGoOnlineContactless() {
        return this.a002CiacGoOnlineContactless;
    }

    public String getA002MchipCvmCardHolderContactless() {
        return this.a002MchipCvmCardHolderContactless;
    }

    public String getA002MchipCvmIssuerOptionsContactless() {
        return this.a002MchipCvmIssuerOptionsContactless;
    }

    public String getA002AckResetTimeout() {
        return this.a002AckResetTimeout;
    }

    public String getA002OfflineChangePinRequired() {
        return this.a002OfflineChangePinRequired;
    }

    public String getA003MagstripeCvmCardHolderOptions() {
        return this.a003MagstripeCvmCardHolderOptions;
    }

    public String getA003MagstripeCvmIssuerOptions() {
        return this.a003MagstripeCvmIssuerOptions;
    }

    public String getA404Cdol1RelatedDataLengthAlternateCl() {
        return this.a404Cdol1RelatedDataLengthAlternateCl;
    }

    public String getA404CvrIssuerDiscretionaryDataAlternateCl() {
        return this.a404CvrIssuerDiscretionaryDataAlternateCl;
    }

    public String getA404Accumulator1ControlAlternateCl() {
        return this.a404Accumulator1ControlAlternateCl;
    }

    public String getA404Accumulator2ControlAlternateCl() {
        return this.a404Accumulator2ControlAlternateCl;
    }

    public String getA404ApplicationControlAlternateCl() {
        return this.a404ApplicationControlAlternateCl;
    }

    public String getA404Counter1ControlAlternateCl() {
        return this.a404Counter1ControlAlternateCl;
    }

    public String getA404Counter2ControlAlternateCl() {
        return this.a404Counter2ControlAlternateCl;
    }

    public String getA404CiacDeclineOfflineAlternateCl() {
        return this.a404CiacDeclineOfflineAlternateCl;
    }

    public String getA404CiacDeclineOnlineAlternateCl() {
        return this.a404CiacDeclineOnlineAlternateCl;
    }

    public String getA404CiacGoOnlineAlternateCl() {
        return this.a404CiacGoOnlineAlternateCl;
    }

    public String getA404ReadRecordFilterAlternateCl() {
        return this.a404ReadRecordFilterAlternateCl;
    }

    public String getA502AcSessionKeyCounterLimitRemote() {
        return this.a502AcSessionKeyCounterLimitRemote;
    }

    public String getA502SmiSessionKeyCounterLimitRemote() {
        return this.a502SmiSessionKeyCounterLimitRemote;
    }

    public String getA504KeyDerivationIndexAlternateCl() {
        return this.a504KeyDerivationIndexAlternateCl;
    }

    public String getA504AcSessionKeyCounterLimitAlternateCl() {
        return this.a504AcSessionKeyCounterLimitAlternateCl;
    }

    public String getA504SmiSessoinKeyCounterLimitAlternateCl() {
        return this.a504SmiSessoinKeyCounterLimitAlternateCl;
    }

    public String getB007AflRemote() {
        return this.b007AflRemote;
    }

    public void setA002Accumulator1CurrencyCode(String str) {
        this.a002Accumulator1CurrencyCode = str;
    }

    public void setA002Accumulator1CurrencyConversionTable(String str) {
        this.a002Accumulator1CurrencyConversionTable = str;
    }

    public void setA002Accumulator1LowerLimit(String str) {
        this.a002Accumulator1LowerLimit = str;
    }

    public void setA002Accumulator1UpperLimit(String str) {
        this.a002Accumulator1UpperLimit = str;
    }

    public void setA002Accumulator2CurrencyCode(String str) {
        this.a002Accumulator2CurrencyCode = str;
    }

    public void setA002Accumulator2CurrencyConversionTable(String str) {
        this.a002Accumulator2CurrencyConversionTable = str;
    }

    public void setA002Accumulator2LowerLimit(String str) {
        this.a002Accumulator2LowerLimit = str;
    }

    public void setA002Accumulator2UpperLimit(String str) {
        this.a002Accumulator2UpperLimit = str;
    }

    public void setA002Counter1LowerLimit(String str) {
        this.a002Counter1LowerLimit = str;
    }

    public void setA002Counter1UpperLimit(String str) {
        this.a002Counter1UpperLimit = str;
    }

    public void setA002Counter2LowerLimit(String str) {
        this.a002Counter2LowerLimit = str;
    }

    public void setA002Counter2UpperLimit(String str) {
        this.a002Counter2UpperLimit = str;
    }

    public void setA002Accumulator1ControlManagement(String str) {
        this.a002Accumulator1ControlManagement = str;
    }

    public void setA002Accumulator2ControlManagement(String str) {
        this.a002Accumulator2ControlManagement = str;
    }

    public void setA002ApplicationCotrol(String str) {
        this.a002ApplicationCotrol = str;
    }

    public void setA002Counter1Control(String str) {
        this.a002Counter1Control = str;
    }

    public void setA002Counter2Control(String str) {
        this.a002Counter2Control = str;
    }

    public void setA002Accumulator1ControlContactless(String str) {
        this.a002Accumulator1ControlContactless = str;
    }

    public void setA002Accumulator2ControlContactless(String str) {
        this.a002Accumulator2ControlContactless = str;
    }

    public void setA002ApplicationControlContactless(String str) {
        this.a002ApplicationControlContactless = str;
    }

    public void setA002Counter1ControlContactless(String str) {
        this.a002Counter1ControlContactless = str;
    }

    public void setA002Counter2ControlContactless(String str) {
        this.a002Counter2ControlContactless = str;
    }

    public void setA002SecurityWorld(String str) {
        this.a002SecurityWorld = str;
    }

    public void setA002ActivateManagementModeInformation(String str) {
        this.a002ActivateManagementModeInformation = str;
    }

    public void setA002Wcota(String str) {
        this.a002Wcota = str;
    }

    public void setA002Wcotn(String str) {
        this.a002Wcotn = str;
    }

    public void setA002CiacDeclineOfflineManagement(String str) {
        this.a002CiacDeclineOfflineManagement = str;
    }

    public void setA002CiacDeclineOfflineContactless(String str) {
        this.a002CiacDeclineOfflineContactless = str;
    }

    public void setA002CiacDeclineOnlineManagement(String str) {
        this.a002CiacDeclineOnlineManagement = str;
    }

    public void setA002CiacDeclineonlineContactless(String str) {
        this.a002CiacDeclineonlineContactless = str;
    }

    public void setA002CiacGoOnlineManagement(String str) {
        this.a002CiacGoOnlineManagement = str;
    }

    public void setA002CiacGoOnlineContactless(String str) {
        this.a002CiacGoOnlineContactless = str;
    }

    public void setA002MchipCvmCardHolderContactless(String str) {
        this.a002MchipCvmCardHolderContactless = str;
    }

    public void setA002MchipCvmIssuerOptionsContactless(String str) {
        this.a002MchipCvmIssuerOptionsContactless = str;
    }

    public void setA002AckResetTimeout(String str) {
        this.a002AckResetTimeout = str;
    }

    public void setA002OfflineChangePinRequired(String str) {
        this.a002OfflineChangePinRequired = str;
    }

    public void setA003MagstripeCvmCardHolderOptions(String str) {
        this.a003MagstripeCvmCardHolderOptions = str;
    }

    public void setA003MagstripeCvmIssuerOptions(String str) {
        this.a003MagstripeCvmIssuerOptions = str;
    }

    public void setA404Cdol1RelatedDataLengthAlternateCl(String str) {
        this.a404Cdol1RelatedDataLengthAlternateCl = str;
    }

    public void setA404CvrIssuerDiscretionaryDataAlternateCl(String str) {
        this.a404CvrIssuerDiscretionaryDataAlternateCl = str;
    }

    public void setA404Accumulator1ControlAlternateCl(String str) {
        this.a404Accumulator1ControlAlternateCl = str;
    }

    public void setA404Accumulator2ControlAlternateCl(String str) {
        this.a404Accumulator2ControlAlternateCl = str;
    }

    public void setA404ApplicationControlAlternateCl(String str) {
        this.a404ApplicationControlAlternateCl = str;
    }

    public void setA404Counter1ControlAlternateCl(String str) {
        this.a404Counter1ControlAlternateCl = str;
    }

    public void setA404Counter2ControlAlternateCl(String str) {
        this.a404Counter2ControlAlternateCl = str;
    }

    public void setA404CiacDeclineOfflineAlternateCl(String str) {
        this.a404CiacDeclineOfflineAlternateCl = str;
    }

    public void setA404CiacDeclineOnlineAlternateCl(String str) {
        this.a404CiacDeclineOnlineAlternateCl = str;
    }

    public void setA404CiacGoOnlineAlternateCl(String str) {
        this.a404CiacGoOnlineAlternateCl = str;
    }

    public void setA404ReadRecordFilterAlternateCl(String str) {
        this.a404ReadRecordFilterAlternateCl = str;
    }

    public void setA502AcSessionKeyCounterLimitRemote(String str) {
        this.a502AcSessionKeyCounterLimitRemote = str;
    }

    public void setA502SmiSessionKeyCounterLimitRemote(String str) {
        this.a502SmiSessionKeyCounterLimitRemote = str;
    }

    public void setA504KeyDerivationIndexAlternateCl(String str) {
        this.a504KeyDerivationIndexAlternateCl = str;
    }

    public void setA504AcSessionKeyCounterLimitAlternateCl(String str) {
        this.a504AcSessionKeyCounterLimitAlternateCl = str;
    }

    public void setA504SmiSessoinKeyCounterLimitAlternateCl(String str) {
        this.a504SmiSessoinKeyCounterLimitAlternateCl = str;
    }

    public void setB007AflRemote(String str) {
        this.b007AflRemote = str;
    }
}
