/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.text.ParseException
 *  java.util.HashMap
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.a.b;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry;
import com.samsung.android.spayfw.payprovider.discover.payment.data.d;
import com.samsung.android.spayfw.payprovider.discover.payment.data.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.f;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import java.text.ParseException;

public class c
extends com.samsung.android.spayfw.payprovider.discover.payment.a {
    private b te;

    public c(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, e2, discoverPaymentCard);
        this.te = new b(byteBuffer);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private long a(long var1_1, ByteBuffer var3_2) {
        if (var3_2 == null || var3_2.getSize() != 5) {
            var4_3 = new StringBuilder().append("convertAmount error, currency convertion code wrong size: ");
            var5_4 = var3_2 == null ? "null" : var3_2.toHexString();
            Log.e("DCSDK_DiscoverGpoApduHandler", var4_3.append(var5_4).toString());
            return var1_1;
        }
        var10_5 = Long.getLong((String)var3_2.copyBytes(3, 4).toHexString());
        Log.i("DCSDK_DiscoverGpoApduHandler", "convertAmount convertion rate: " + var10_5);
        var12_6 = var3_2.getByte(5);
        Log.i("DCSDK_DiscoverGpoApduHandler", "convertAmount convertion exponent: " + var12_6);
        if ((1L & var12_6 >> 7) == 1L) {
            var20_7 = var10_5 * var1_1;
            var22_8 = Math.pow((double)10.0, (double)(var12_6 & 127L));
            var8_9 = (long)(var20_7 / var22_8);
        } else {
            var14_12 = var10_5 * var1_1;
            var16_13 = var12_6 & 127L;
            var18_14 = Math.pow((double)10.0, (double)var16_13);
            var8_9 = (long)(var14_12 * var18_14);
        }
        try {
            Log.i("DCSDK_DiscoverGpoApduHandler", "convertAmount, value: " + var8_9);
            return var8_9;
        }
        catch (Exception var7_10) {}
        ** GOTO lbl-1000
        catch (Exception var6_15) {
            var7_11 = var6_15;
            var8_9 = var1_1;
        }
lbl-1000: // 2 sources:
        {
            Log.e("DCSDK_DiscoverGpoApduHandler", "convertAmount, unexpected exception during currency conversion: " + var7_11.getMessage());
            return var8_9;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private a a(PDOLCheckEntry[] arrpDOLCheckEntry, boolean bl) {
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  Process PDOL entries...");
        a a2 = new a();
        if (arrpDOLCheckEntry == null) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolDecline,  PDOL Decline entries is null.");
            return a2;
        }
        int n2 = 0;
        block13 : while (n2 < arrpDOLCheckEntry.length) {
            PDOLCheckEntry pDOLCheckEntry = arrpDOLCheckEntry[n2];
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry " + n2);
            if (pDOLCheckEntry == null) continue;
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry data type:" + pDOLCheckEntry.getDataType());
            byte by = pDOLCheckEntry.getDataType();
            ByteBuffer byteBuffer = null;
            switch (by) {
                case 1: {
                    ByteBuffer byteBuffer2 = this.te.dz();
                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry data type pdol:" + byteBuffer2);
                    if (byteBuffer2 == null) {
                        Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolDecline,  PDOL value is null, continue.");
                        ++n2;
                        continue block13;
                    }
                    int n3 = new ByteBuffer(pDOLCheckEntry.getDataOffset()).getInt();
                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry offset:" + n3);
                    if (byteBuffer2.getSize() <= n3 + pDOLCheckEntry.getDataSize()) {
                        Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolDecline,  PDOL size is less or equal offset + length:  pdol size: " + byteBuffer2.getSize() + ", offset: " + n3 + " data length: " + pDOLCheckEntry.getDataSize());
                        ++n2;
                        continue block13;
                    }
                    StringBuilder stringBuilder = new StringBuilder().append("processApdu, C-APDU GPO, performPdolProfile,  PDOL entry data type pdol hex:");
                    String string = byteBuffer2 != null ? byteBuffer2.toHexString() : null;
                    Log.i("DCSDK_DiscoverGpoApduHandler", stringBuilder.append(string).toString());
                    byteBuffer = byteBuffer2.copyBytes(n3, n3 + pDOLCheckEntry.getDataSize());
                    break;
                }
                case 2: {
                    byteBuffer = com.samsung.android.spayfw.payprovider.discover.payment.data.c.c(new ByteBuffer(pDOLCheckEntry.getDataOffset()), this.cM());
                    break;
                }
                case 8: {
                    byteBuffer = new ByteBuffer(new byte[]{-128});
                    break;
                }
                case 4: {
                    int n4 = this.cN().dS();
                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  aliasId:" + n4);
                    if (n4 == -1) {
                        Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolDecline,  aliasId is not defined");
                        return a2;
                    }
                    byte[] arrby = new byte[]{(byte)(n4 & 255)};
                    byteBuffer = new ByteBuffer(arrby);
                }
            }
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  value to compare: " + byteBuffer);
            if (byteBuffer == null) {
                Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolDecline,  parse error, PDOL value is null.");
                ++n2;
                continue;
            }
            Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  value to compare: " + byteBuffer.toHexString());
            ByteBuffer byteBuffer3 = byteBuffer.bitAnd(pDOLCheckEntry.getBitMask());
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  compareResult: " + byteBuffer3);
            if (byteBuffer3 == null) {
                Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolDecline,  comparison result is null.");
                ++n2;
                continue;
            }
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  compareResult hex: " + byteBuffer3.toHexString());
            int n5 = 0;
            byte by2 = pDOLCheckEntry.getMatchNotFound();
            Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  entry.getTestType() " + pDOLCheckEntry.getTestType());
            Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  entry.getNumberMatchValues() " + pDOLCheckEntry.getNumberMatchValues());
            for (int i2 = 0; i2 < pDOLCheckEntry.getValues().length; ++i2) {
                Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, value: " + pDOLCheckEntry.getValues()[i2]);
            }
            boolean bl2 = false;
            while (by2 == pDOLCheckEntry.getMatchNotFound() && n5 < pDOLCheckEntry.getNumberMatchValues() && !bl2) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  entry.getTestType() inside: " + pDOLCheckEntry.getTestType());
                switch (pDOLCheckEntry.getTestType()) {
                    case 2: {
                        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  check compareResult, no match");
                        Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) " + pDOLCheckEntry.getValues()[n5]);
                        if (pDOLCheckEntry.getValues()[n5] != null) {
                            Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) " + pDOLCheckEntry.getValues()[n5].toHexString());
                        }
                        if (byteBuffer3.equals(pDOLCheckEntry.getValues()[n5])) break;
                        by2 = pDOLCheckEntry.getMatchFound();
                        Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, found action code match found.");
                        bl2 = true;
                        break;
                    }
                    case 4: {
                        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  check compareResult, exact match");
                        Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) " + pDOLCheckEntry.getValues()[n5]);
                        if (pDOLCheckEntry.getValues()[n5] != null) {
                            Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) " + pDOLCheckEntry.getValues()[n5].toHexString());
                        }
                        if (!byteBuffer3.equals(pDOLCheckEntry.getValues()[n5])) break;
                        by2 = pDOLCheckEntry.getMatchFound();
                        Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, found action code match found.");
                        bl2 = true;
                        break;
                    }
                    case 1: {
                        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, test type equal or greater.");
                        if (byteBuffer3.getLong() < pDOLCheckEntry.getValues()[n5].getLong()) break;
                        by2 = pDOLCheckEntry.getMatchFound();
                        bl2 = true;
                        break;
                    }
                    case 0: {
                        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, test type always.");
                        by2 = pDOLCheckEntry.getMatchFound();
                        break;
                    }
                    case 8: {
                        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, test type never.");
                        if (n5 == -1 + pDOLCheckEntry.getNumberMatchValues()) {
                            by2 = pDOLCheckEntry.getMatchNotFound();
                            break;
                        }
                        by2 = 0;
                        break;
                    }
                }
                ++n5;
            }
            byte by3 = (byte)(15 & by2 >> 4);
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, action code: " + by3);
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, result: " + pDOLCheckEntry.getResult());
            if ((by3 & 8) == 8) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, set value mask.");
                a2.tf = true;
                a2.tg = pDOLCheckEntry.getResult();
            }
            if ((by3 & 1) == 1) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, jump to line mask.");
                n2 = pDOLCheckEntry.getResult();
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, jump to entry: " + n2);
                continue;
            }
            if (bl && (by3 & 2) == 2) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile, exit mask.");
                return a2;
            }
            ++n2;
        }
        return a2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private com.samsung.android.spayfw.payprovider.discover.payment.data.a a(byte by) {
        com.samsung.android.spayfw.payprovider.discover.payment.a.c c2;
        boolean bl;
        com.samsung.android.spayfw.payprovider.discover.payment.data.b b2;
        block8 : {
            block7 : {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process start, cid = " + by);
                this.setCid(by);
                this.dg();
                b2 = this.cN().dM();
                try {
                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process, compute application cryptogram, cid =" + by);
                    this.cY();
                    if (!this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 8)) break block7;
                }
                catch (Exception exception) {
                    Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process, unexpected exception: " + exception.getMessage());
                    exception.printStackTrace();
                    com.samsung.android.spayfw.payprovider.discover.payment.data.a a2 = new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
                    return a2;
                }
                finally {
                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process, log transaction, cid = " + by);
                    this.cZ();
                }
                this.cN().getPth().setBit(1, 7);
                this.cN().L(this.df());
                bl = true;
                break block8;
            }
            this.cN().getPth().clearBit(1, 7);
            bl = false;
        }
        this.cN().b((byte)0);
        this.cN().c((byte)1);
        ByteBuffer byteBuffer = this.cN().getPaymentProfile().getAfl();
        if (bl) {
            ByteBuffer byteBuffer2 = this.cN().getPaymentProfile().getAip();
            ByteBuffer byteBuffer3 = b2.dU();
            ByteBuffer byteBuffer4 = b2.dT();
            ByteBuffer byteBuffer5 = this.cN().dM().getIssuerApplicationData();
            byte[] arrby = new byte[]{b2.getCid()};
            c2 = new com.samsung.android.spayfw.payprovider.discover.payment.a.c(new d.a(byteBuffer2, byteBuffer, byteBuffer3, byteBuffer4, byteBuffer5, new ByteBuffer(arrby), this.cN().dI()));
        } else {
            ByteBuffer byteBuffer6 = this.cN().getPaymentProfile().getAip();
            ByteBuffer byteBuffer7 = b2.dU();
            ByteBuffer byteBuffer8 = b2.dT();
            ByteBuffer byteBuffer9 = this.cN().dM().getIssuerApplicationData();
            byte[] arrby = new byte[]{b2.getCid()};
            c2 = new com.samsung.android.spayfw.payprovider.discover.payment.a.c(new d.a(byteBuffer6, byteBuffer7, byteBuffer8, byteBuffer9, new ByteBuffer(arrby), this.cM().getTrack2EquivalentData(), this.cN().getPaymentProfile().getApplicationUsageControl(), this.cM().getCountryCode(), this.cM().getDiscoverApplicationData().getPanSn(), this.cN().dI(), this.cM().getDiscoverApplicationData().getApplicationEffectiveDate(), this.cM().getDiscoverApplicationData().getApplicationVersionNumber()));
        }
        com.samsung.android.spayfw.payprovider.discover.payment.data.c.a(this.cL(), this.cM());
        com.samsung.android.spayfw.payprovider.discover.payment.data.a a3 = new com.samsung.android.spayfw.payprovider.discover.payment.data.a(c2.dj(), DiscoverApduHandlerState.sW);
        a3.dD();
        this.cL().a(a3);
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process end, cid = " + by);
        return a3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean a(PDOLCheckEntry[] arrpDOLCheckEntry) {
        DiscoverPaymentProfile discoverPaymentProfile;
        a a2 = this.d(arrpDOLCheckEntry);
        Log.i("DCSDK_DiscoverGpoApduHandler", "Result: " + a2.tg);
        if (a2.tf) {
            byte by = this.cN().dH().getByte(5);
            Log.d("DCSDK_DiscoverGpoApduHandler", "cvrB6 1: " + by);
            byte by2 = (byte)(by | a2.tg);
            Log.d("DCSDK_DiscoverGpoApduHandler", "cvrB6 2: " + by2);
            this.cN().dH().setByte(5, by2);
            Log.d("DCSDK_DiscoverGpoApduHandler", "cvrB6: " + this.cN().dH().toHexString());
        }
        if ((discoverPaymentProfile = (DiscoverPaymentProfile)this.cM().getPaymentProfiles().get((Object)a2.tg)) != null) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolProfile,  set payment profile, id = " + (-1 + a2.tg));
            this.cN().setSelectedPaymentProfile(discoverPaymentProfile);
            do {
                return a2.tf;
                break;
            } while (true);
        }
        Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolProfile,  profile id = " + (-1 + a2.tg) + " not found, use default profile.");
        return a2.tf;
    }

    private boolean b(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return false;
        }
        try {
            Long.parseLong((String)byteBuffer.toHexString());
            return true;
        }
        catch (Exception exception) {
            Log.d("DCSDK_DiscoverGpoApduHandler", "isNumericValue, value " + byteBuffer.toHexString() + " not numeric.");
            return false;
        }
    }

    private boolean b(PDOLCheckEntry[] arrpDOLCheckEntry) {
        a a2 = this.c(arrpDOLCheckEntry);
        if (a2.tf) {
            byte by = (byte)(this.cN().dH().getByte(6) | a2.tg << 4);
            this.cN().dH().setByte(6, by);
        }
        return a2.tf;
    }

    private a c(PDOLCheckEntry[] arrpDOLCheckEntry) {
        return this.a(arrpDOLCheckEntry, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean cQ() {
        StringBuilder stringBuilder = new StringBuilder().append("TTQ: ");
        String string = this.te.dm() != null ? this.te.dm().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder.append(string).toString());
        StringBuilder stringBuilder2 = new StringBuilder().append("getAuthAmount: ");
        String string2 = this.te.dn() != null ? this.te.dn().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder2.append(string2).toString());
        StringBuilder stringBuilder3 = new StringBuilder().append("getOtherAmount: ");
        String string3 = this.te.do() != null ? this.te.do().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder3.append(string3).toString());
        StringBuilder stringBuilder4 = new StringBuilder().append("getTerminalCountryCode: ");
        String string4 = this.te.dp() != null ? this.te.dp().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder4.append(string4).toString());
        StringBuilder stringBuilder5 = new StringBuilder().append("getTerminalCurrencyCode: ");
        String string5 = this.te.dq() != null ? this.te.dq().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder5.append(string5).toString());
        StringBuilder stringBuilder6 = new StringBuilder().append("getTransactionDate: ");
        String string6 = this.te.dr() != null ? this.te.dr().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder6.append(string6).toString());
        StringBuilder stringBuilder7 = new StringBuilder().append("getTransactionType: ");
        String string7 = this.te.ds() != null ? this.te.ds().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder7.append(string7).toString());
        StringBuilder stringBuilder8 = new StringBuilder().append("getUnpredictableNumber: ");
        String string8 = this.te.dt() != null ? this.te.dt().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder8.append(string8).toString());
        StringBuilder stringBuilder9 = new StringBuilder().append("getTerminalType: ");
        String string9 = this.te.du() != null ? this.te.du().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder9.append(string9).toString());
        StringBuilder stringBuilder10 = new StringBuilder().append("getLoyalityProgram: ");
        String string10 = this.te.dv() != null ? this.te.dv().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder10.append(string10).toString());
        StringBuilder stringBuilder11 = new StringBuilder().append("getMerchantCategoryCode: ");
        String string11 = this.te.dw() != null ? this.te.dw().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder11.append(string11).toString());
        StringBuilder stringBuilder12 = new StringBuilder().append("getVAT1: ");
        String string12 = this.te.dx() != null ? this.te.dx().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder12.append(string12).toString());
        StringBuilder stringBuilder13 = new StringBuilder().append("getVAT2: ");
        String string13 = this.te.dy() != null ? this.te.dy().toHexString() : "null";
        Log.d("DCSDK_DiscoverGpoApduHandler", stringBuilder13.append(string13).toString());
        return this.te.dm() != null && this.te.dm().getSize() >= 4 && this.te.dn() != null && this.te.dn().getSize() >= 6 && this.b(this.te.dn()) && this.te.do() != null && this.te.do().getSize() >= 6 && this.te.dp() != null && this.te.dp().getSize() >= 2 && this.te.dq() != null && this.te.dq().getSize() >= 2 && this.te.dr() != null && this.te.dr().getSize() >= 3 && this.te.ds() != null && this.te.ds().getSize() >= 1 && this.te.dt() != null && this.te.dt().getSize() >= 4;
    }

    /*
     * Exception decompiling
     */
    private com.samsung.android.spayfw.payprovider.discover.payment.data.a cR() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 3[CATCHBLOCK]
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

    /*
     * Enabled aggressive block sorting
     */
    private com.samsung.android.spayfw.payprovider.discover.payment.data.a cS() {
        ByteBuffer byteBuffer;
        block28 : {
            long l2;
            long l3;
            block32 : {
                long l4;
                block31 : {
                    long l5;
                    block30 : {
                        long l6;
                        block29 : {
                            block27 : {
                                byte by;
                                ByteBuffer byteBuffer2;
                                l2 = 999999999999L;
                                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm starting...");
                                byteBuffer = this.cN().dH();
                                ByteBuffer byteBuffer3 = this.cN().getPth();
                                if (byteBuffer3.getByte(1) != 0) {
                                    byteBuffer.setByte(0, (byte)(-16 & byteBuffer.getByte(0) | 15 & byteBuffer3.getByte(1)));
                                }
                                if (byteBuffer3.checkBit(1, 8)) {
                                    byteBuffer.setBit(4, 8);
                                }
                                if (byteBuffer3.checkBit(1, 7)) {
                                    byteBuffer.setBit(4, 7);
                                }
                                if (byteBuffer3.checkBit(1, 5)) {
                                    byteBuffer.setBit(4, 6);
                                }
                                if (byteBuffer3.checkBit(1, 3)) {
                                    byteBuffer.setBit(4, 5);
                                }
                                if (byteBuffer3.checkBit(1, 1)) {
                                    byteBuffer.setBit(4, 4);
                                }
                                if ((byteBuffer2 = this.te.dm()).checkBit(2, 8)) {
                                    byteBuffer.setBit(2, 8);
                                }
                                if (byteBuffer2.checkBit(2, 7)) {
                                    byteBuffer.setBit(3, 8);
                                }
                                if (byteBuffer2.checkBit(1, 5)) {
                                    byteBuffer.setBit(7, 8);
                                }
                                if (byteBuffer2.checkBit(1, 4)) {
                                    byteBuffer.setBit(7, 7);
                                }
                                if (byteBuffer2.checkBit(1, 3)) {
                                    byteBuffer.setBit(7, 6);
                                }
                                if (byteBuffer2.checkBit(1, 2)) {
                                    byteBuffer.setBit(7, 5);
                                }
                                if (byteBuffer2.checkBit(3, 8)) {
                                    byteBuffer.setBit(7, 4);
                                }
                                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, after pth, ttq check, cvr " + byteBuffer.toHexString());
                                ByteBuffer byteBuffer4 = this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions();
                                if (byteBuffer4.checkBit(1, 1) && this.cM().getCountryCode().equals(this.te.dp())) {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, domestic transaction,  domestic transaction CL ACO B1b1 = 1 & country code equals");
                                    byteBuffer.setBit(2, 5);
                                } else {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, international transaction , country code check.");
                                    byteBuffer.setBit(2, 4);
                                }
                                if (!byteBuffer4.checkBit(1, 1) && this.cM().getCurrencyCodeCode().equals(this.te.dq())) {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, domestic transaction CL ACO B1b1 = 0 & currency code equals");
                                    byteBuffer.setBit(2, 5);
                                } else {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, international transaction, currency code check.");
                                    byteBuffer.setBit(2, 4);
                                }
                                if ((by = this.te.ds().getByte(0)) == 1 || by == 9) {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, cash advance/goods service");
                                    byteBuffer.setBit(2, 7);
                                } else if (by == 32) {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, refund");
                                    byteBuffer.setBit(2, 6);
                                }
                                if (this.cN().getPaymentProfile().getAip().checkBit(1, 6) || !this.cN().getPaymentProfile().getAip().checkBit(1, 1)) {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, offline, perform cvm or");
                                    return this.cU();
                                }
                                if (by == 32) {
                                    Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, process refund transaction.");
                                    if (!this.cM().getCaco().checkBit(1, 4)) {
                                        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, do not count/accumulate refund.");
                                        return this.cU();
                                    }
                                }
                                l5 = Long.parseLong((String)this.te.dn().toHexString());
                                l6 = Long.parseLong((String)this.te.do().toHexString());
                                Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, authAmount: " + l5);
                                Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, otherAmount: " + l6);
                                if (l5 != 0L || l6 != 0L) break block27;
                                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, auth amount is 0, and other amount is 0.");
                                break block28;
                            }
                            if (!this.cM().getCurrencyCodeCode().equals(this.te.dq())) break block29;
                            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, increment accumulator, currency code is ok.");
                            this.d(l5, l6);
                            break block28;
                        }
                        if (!this.cM().getSecondaryCurrency1().copyBytes(1, 2).equals(this.te.dq())) break block30;
                        l3 = this.a(l5, this.cM().getSecondaryCurrency1());
                        l4 = this.a(l6, this.cM().getSecondaryCurrency1());
                        if (l3 > l2) {
                            l3 = l2;
                        }
                        if (l4 <= l2) break block31;
                        break block32;
                    }
                    if (this.cM().getSecondaryCurrency2().copyBytes(1, 2).equals(this.te.dq())) {
                        long l7;
                        long l8 = this.a(l5, this.cM().getSecondaryCurrency2());
                        if (l8 > l2) {
                            l8 = l2;
                        }
                        if ((l7 = this.a(l5, this.cM().getSecondaryCurrency2())) <= l2) {
                            l2 = l7;
                        }
                        this.te.e(ByteBuffer.getFromLong(l8));
                        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, increment accumulator, convert amount to sec currency 2.");
                        this.d(l8, l2);
                    }
                    break block28;
                }
                l2 = l4;
            }
            this.te.e(ByteBuffer.getFromLong(l3));
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, increment accumulator, convert amount to sec currency 1.");
            this.d(l3, l2);
        }
        Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, before processCrmCvmCounters, cvr " + byteBuffer.toHexString());
        this.cT();
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, after processCrmCvmCounters, cvr " + byteBuffer.toHexString());
        return this.cU();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void cT() {
        ByteBuffer byteBuffer = this.cM().getCaco();
        long l2 = Long.parseLong((String)this.te.dn().toHexString());
        ByteBuffer byteBuffer2 = this.te.dp();
        ByteBuffer byteBuffer3 = this.cM().getCountryCode();
        ByteBuffer byteBuffer4 = this.te.dq();
        ByteBuffer byteBuffer5 = this.cM().getCurrencyCodeCode();
        DiscoverPaymentProfile.CRM cRM = this.cN().getPaymentProfile().getCRM();
        DiscoverPaymentProfile.CL cL = this.cN().getPaymentProfile().getCl();
        DiscoverPaymentProfile.CVM cVM = this.cN().getPaymentProfile().getCVM();
        int n2 = 7 & byteBuffer.getByte(2);
        if (l2 != 0L || l2 == 0L && byteBuffer.checkBit(1, 3)) {
            if (byteBuffer2.equals(byteBuffer3)) {
                if (n2 == 0) {
                    cRM.incrementCrmCounter();
                }
            } else if (n2 == 1) {
                cRM.incrementCrmCounter();
            }
            if (byteBuffer4.equals(byteBuffer5)) {
                if (n2 == 2) {
                    cRM.incrementCrmCounter();
                }
            } else if (n2 == 3) {
                cRM.incrementCrmCounter();
            }
        } else if (n2 == 4) {
            cRM.incrementCrmCounter();
        }
        if (byteBuffer.checkBit(1, 7)) {
            this.cN().getPaymentProfile().getCl().incrementClCounter();
        }
        Log.d("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, before increment cvm counter: " + this.cN().getPaymentProfile().getCVM().getCvmCounter());
        if (byteBuffer.checkBit(1, 5) && !this.te.dm().checkBit(2, 7)) {
            Log.d("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, increment cvm counter, " + this.cN().getPaymentProfile().getCVM().getCvmCounter());
            this.cN().getPaymentProfile().getCVM().incrementCvmCounter();
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, after increment cvm counter, " + this.cN().getPaymentProfile().getCVM().getCvmCounter());
        }
        ByteBuffer byteBuffer6 = this.cN().dH();
        if (cRM.getCrmAccumulator() > cRM.getLCOA()) {
            byteBuffer6.setBit(5, 3);
        }
        if (cRM.getCrmAccumulator() > cRM.getUCOA()) {
            byteBuffer6.setBit(5, 2);
        }
        if (cL.getClAccumulator() > cL.getCL_Cum_Limit()) {
            byteBuffer6.setBit(2, 7);
        }
        if (cVM.getCvmAccumulator() > cVM.getCVM_Cum_Limit_1()) {
            byteBuffer6.setBit(3, 4);
        }
        if (cVM.getCvmAccumulator() > cVM.getCVM_Cum_Limit_2()) {
            byteBuffer6.setBit(3, 3);
        }
        if (cRM.getCrmCounter() > cRM.getLCOL()) {
            byteBuffer6.setBit(5, 5);
        }
        if (cRM.getCrmCounter() > cRM.getUCOL()) {
            byteBuffer6.setBit(5, 4);
        }
        if (cL.getClCounter() > cL.getCL_Cons_Limit()) {
            byteBuffer6.setBit(5, 8);
        }
        if (cVM.getCvmCounter() > cVM.getCVM_Cons_Limit_1()) {
            byteBuffer6.setBit(3, 6);
        }
        if (cVM.getCvmCounter() > cVM.getCVM_Cons_Limit_2()) {
            byteBuffer6.setBit(3, 5);
        }
        if (byteBuffer4.equals(byteBuffer5) || byteBuffer4.equals(this.cM().getSecondaryCurrency1()) || byteBuffer4.equals(this.cM().getSecondaryCurrency2())) {
            if (l2 > this.cN().getPaymentProfile().getCRM().getSTA()) {
                byteBuffer6.setBit(5, 1);
            }
            if (l2 > cL.getCL_STA_Limit()) {
                byteBuffer6.setBit(5, 6);
            }
            if (l2 > cVM.getCVM_Sta_Limit_1()) {
                byteBuffer6.setBit(3, 2);
            }
            if (l2 > cVM.getCVM_Sta_Limit_2()) {
                byteBuffer6.setBit(3, 1);
            }
            if (l2 > cVM.getCVM_Sta_Limit_2()) {
                byteBuffer6.setBit(3, 1);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private com.samsung.android.spayfw.payprovider.discover.payment.data.a cU() {
        ByteBuffer byteBuffer = this.te.dm();
        if (byteBuffer == null) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, empty ttq.");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
        }
        if (!byteBuffer.checkBit(3, 7)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, terminal doesn't support CDCVM.");
            if (!byteBuffer.checkBit(1, 3)) return this.cV();
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, online PIN supported.");
            ByteBuffer byteBuffer2 = new ByteBuffer(2);
            byteBuffer2.setByte(0, this.cN().dH().getByte(1));
            byteBuffer2.setByte(1, this.cN().dH().getByte(2));
            if (!byteBuffer2.checkBitAndMatch(this.cN().getPaymentProfile().getCVM().getCVM_CAC_Online_PIN())) return this.cV();
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, match found, set online PIN required.");
            this.cN().dI().setBit(1, 8);
            return this.cV();
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, terminal supports CDCVM.");
        if (this.cN().dN() == 1) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCF = 1.");
            if (this.cN().dO() != 1) return this.cV();
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCI = 1.");
            this.cN().dH().setBit(2, 1);
            return this.cV();
        }
        if (byteBuffer.checkBit(3, 4)) {
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27014);
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCF = 0.");
        this.cN().dH().setBit(2, 2);
        if (this.cN().dO() != 1) return this.cV();
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCI = 1.");
        this.cN().dH().setBit(2, 1);
        return this.cV();
    }

    private com.samsung.android.spayfw.payprovider.discover.payment.data.a cV() {
        DiscoverPaymentProfile.CRM cRM = this.cN().getPaymentProfile().getCRM();
        ByteBuffer byteBuffer = this.cN().dH();
        ByteBuffer byteBuffer2 = new ByteBuffer(3);
        byteBuffer2.setByte(0, byteBuffer.getByte(1));
        byteBuffer2.setByte(1, byteBuffer.getByte(3));
        byteBuffer2.setByte(2, byteBuffer.getByte(4));
        if (byteBuffer2.checkBitAndMatch(cRM.getCRM_CAC_Denial())) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra, CM-CAC-Decline match found, aac. CVR data: " + byteBuffer2.toHexString() + ", mask: " + cRM.getCRM_CAC_Denial().toHexString());
            return this.cX();
        }
        ByteBuffer byteBuffer3 = this.te.dm();
        if (byteBuffer3.checkBit(2, 8)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  online cryptogram required.");
            return this.dc();
        }
        if (byteBuffer3.checkBit(1, 4)) {
            Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  offline only reader.");
            if (byteBuffer2.checkBitAndMatch(cRM.getCRM_CAC_Default())) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra, CM-CAC-Default match found, aac. CVR data: " + byteBuffer2.toHexString() + ", mask: " + cRM.getCRM_CAC_Default().toHexString());
                return this.cX();
            }
            if (this.cN().getPaymentProfile().getAip().checkBit(1, 1)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda supported, tc.");
                return this.dd();
            }
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda not supported, aac.");
            return this.cX();
        }
        if (byteBuffer2.checkBitAndMatch(cRM.getCRM_CAC_Online())) {
            Log.d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra, CM-CAC-Online match found, arqc. CVR data: " + byteBuffer2.toHexString() + ", mask: " + cRM.getCRM_CAC_Online().toHexString());
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  arqc.");
            return this.dc();
        }
        if (this.cN().getPaymentProfile().getAip().checkBit(1, 1)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda supported, tc.");
            return this.dd();
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda not supported, aac.");
        return this.cX();
    }

    /*
     * Enabled aggressive block sorting
     */
    private com.samsung.android.spayfw.payprovider.discover.payment.data.a cW() {
        PDOLCheckEntry[] arrpDOLCheckEntry;
        PDOLCheckEntry[] arrpDOLCheckEntry2 = this.cM().getPdolDeclineEntries();
        ByteBuffer byteBuffer = this.cN().dH();
        if (arrpDOLCheckEntry2 != null && this.b(arrpDOLCheckEntry2)) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolCheck,  PDOL Decline match found, aac.");
            byteBuffer.setBit(4, 3);
            return this.cX();
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  get profile entries...");
        PDOLCheckEntry[] arrpDOLCheckEntry3 = this.cM().getPdolProfileEntries();
        if (arrpDOLCheckEntry3 != null) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  Process PDOL profile...");
            this.a(arrpDOLCheckEntry3);
        } else if (this.cM().getPDOLProfileCheckTable() != null) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL Profile check table cannot be parsed.");
            byteBuffer.setBit(4, 3);
        }
        if ((arrpDOLCheckEntry = this.cM().getPdolOnlineEntries()) != null && this.b(arrpDOLCheckEntry)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL Online match found.");
            byteBuffer.setBit(4, 2);
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  perform CRM.");
        return this.cS();
    }

    private com.samsung.android.spayfw.payprovider.discover.payment.data.a cX() {
        return this.a((byte)0);
    }

    private void cY() {
        byte[] arrby;
        com.samsung.android.spayfw.payprovider.discover.payment.data.b b2 = this.cN().dM();
        if (b2 == null) {
            b2 = new com.samsung.android.spayfw.payprovider.discover.payment.data.b();
        }
        ByteBuffer byteBuffer = this.da();
        ByteBuffer byteBuffer2 = this.db();
        if (byteBuffer == null) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, input data 1 is empty.");
            throw new Exception("Conditions not satisfied, input data is empty");
        }
        if (byteBuffer2 == null) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, input data 2 is empty.");
            throw new Exception("Conditions not satisfied, input data is empty");
        }
        com.samsung.android.spayfw.payprovider.discover.tzsvc.b b3 = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.E(com.samsung.android.spayfw.payprovider.discover.a.cC());
        try {
            arrby = b3.c(byteBuffer.getBytes(), byteBuffer2.getBytes());
        }
        catch (DcTAException dcTAException) {
            Log.c("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, computeAppCryptogram response is null. " + dcTAException.getMessage(), (Throwable)((Object)dcTAException));
            throw new Exception("Conditions not satisfied, crypto data is empty");
        }
        if (arrby.length != 8) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, computeAppCryptogram length is wrong, length " + arrby.length);
            throw new Exception("Conditions not satisfied, crypto length is wrong, length " + arrby.length);
        }
        b2.u(this.cL().dT());
        b2.v(new ByteBuffer(arrby));
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, generated cryptogram, exit.");
    }

    private void cZ() {
        f f2 = new f(this.te.dn(), this.te.dq(), this.te.dr(), this.cN().dM().dT(), this.te.dp(), null, this.te.ds(), this.te.dt(), this.te.do(), this.cN().dH(), this.te.dm(), this.cN().dI(), this.cN().getPaymentProfile().getCtq(), this.cN().dM().dU());
        this.cN().a(f2);
    }

    private a d(PDOLCheckEntry[] arrpDOLCheckEntry) {
        return this.a(arrpDOLCheckEntry, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void d(long l2, long l3) {
        long l4;
        Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, START");
        ByteBuffer byteBuffer = this.cM().getCaco();
        Log.d("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, caco: " + byteBuffer.toHexString());
        long l5 = 0L;
        if (!byteBuffer.checkBit(2, 8) && !byteBuffer.checkBit(2, 7)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added auth amount: " + l2);
            l5 = l2;
        } else if (!byteBuffer.checkBit(2, 8) && byteBuffer.checkBit(2, 7)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added other amount: " + l3);
            l5 = l3;
        } else if (byteBuffer.checkBit(2, 8) && !byteBuffer.checkBit(2, 7)) {
            l5 = l2 + l3;
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added both amounts: " + l5);
        }
        if ((l4 = l5 + this.cN().getPaymentProfile().getCRM().getCrmAccumulator()) > 999999999999L) {
            l4 = 999999999999L;
        }
        this.cN().getPaymentProfile().getCRM().setCrmAccumulator(l4);
        if (byteBuffer.checkBit(1, 8)) {
            long l6;
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, cl accumulator");
            long l7 = 0L;
            if (!byteBuffer.checkBit(4, 8) && !byteBuffer.checkBit(4, 7)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added auth amount: " + l2);
                l7 = l2;
            } else if (!byteBuffer.checkBit(4, 8) && byteBuffer.checkBit(4, 7)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added other amount: " + l3);
                l7 = l3;
            } else if (byteBuffer.checkBit(4, 8) && !byteBuffer.checkBit(4, 7)) {
                l7 = l2 + l3;
                Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added both amount: " + l7);
            }
            if ((l6 = l7 + this.cN().getPaymentProfile().getCl().getClAccumulator()) > 999999999999L) {
                l6 = 999999999999L;
            }
            this.cN().getPaymentProfile().getCl().setClAccumulator(l6);
        }
        if (byteBuffer.checkBit(1, 6)) {
            long l8;
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, cvm accumulator");
            if (!byteBuffer.checkBit(5, 8) && !byteBuffer.checkBit(5, 7)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added auth amount: " + l2);
            } else if (!byteBuffer.checkBit(5, 8) && byteBuffer.checkBit(5, 7)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added other amount: " + l3);
                l2 = l3;
            } else if (byteBuffer.checkBit(5, 8) && !byteBuffer.checkBit(5, 7)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added both amount: " + (l2 += l3));
            } else {
                l2 = 0L;
            }
            if ((l8 = l2 + this.cN().getPaymentProfile().getCVM().getCvmAccumulator()) > 999999999999L) {
                l8 = 999999999999L;
            }
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, cvmAddedAmount: " + l8);
            this.cN().getPaymentProfile().getCVM().setCvmAccumulator(l8);
            Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, set amount.");
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, END");
    }

    private ByteBuffer da() {
        if (this.cN().dM() == null) {
            this.cN().a(new com.samsung.android.spayfw.payprovider.discover.payment.data.b());
        }
        ByteBuffer byteBuffer = this.te.dn();
        byteBuffer.append(this.te.dq());
        byteBuffer.append(this.te.dt());
        return byteBuffer;
    }

    private ByteBuffer db() {
        if (this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 7)) {
            return this.cN().dH();
        }
        return this.cM().getIssuerApplicationData().getIssuerApplicationData();
    }

    private com.samsung.android.spayfw.payprovider.discover.payment.data.a dc() {
        this.cN().dH().setBit(1, 6);
        this.cN().dH().clearBit(1, 5);
        com.samsung.android.spayfw.payprovider.discover.payment.data.a a2 = this.a((byte)-128);
        this.cN().getPth().clearBit(1, 5);
        ByteBuffer byteBuffer = this.cM().getCaco();
        if (byteBuffer.checkBit(2, 4) && byteBuffer.checkBit(3, 4) && byteBuffer.checkBit(4, 4) && byteBuffer.checkBit(5, 4)) {
            this.cN().getPaymentProfile().getCRM().resetCrm();
        }
        return a2;
    }

    private com.samsung.android.spayfw.payprovider.discover.payment.data.a dd() {
        ByteBuffer byteBuffer;
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, tc");
        this.setCid((byte)64);
        this.dg();
        try {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, tc, compute application cryptogram");
            this.cY();
            byteBuffer = this.cM().getCaco();
        }
        catch (Exception exception) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, tc, unexpected exception: " + exception.getMessage());
            exception.printStackTrace();
            com.samsung.android.spayfw.payprovider.discover.payment.data.a a2 = new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27010);
            return a2;
        }
        finally {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, tc, log transaction");
            this.cZ();
        }
        if (byteBuffer != null && byteBuffer.getSize() >= 6 && byteBuffer.checkBit(6, 2)) {
            DiscoverPaymentProfile.CRM cRM = this.cN().getPaymentProfile().getCRM();
            long l2 = cRM.getUCOA() - cRM.getCrmAccumulator();
            this.cN().t(l2);
        }
        this.de();
        this.cN().L(this.df());
        this.cN().b((byte)0);
        this.cN().c((byte)1);
        com.samsung.android.spayfw.payprovider.discover.payment.data.b b2 = this.cN().dM();
        ByteBuffer byteBuffer2 = this.cN().getPaymentProfile().getAip();
        ByteBuffer byteBuffer3 = this.cN().getPaymentProfile().getAfl();
        ByteBuffer byteBuffer4 = b2.dV();
        ByteBuffer byteBuffer5 = b2.dT();
        ByteBuffer byteBuffer6 = b2.getIssuerApplicationData();
        byte[] arrby = new byte[]{b2.getCid()};
        com.samsung.android.spayfw.payprovider.discover.payment.a.c c2 = new com.samsung.android.spayfw.payprovider.discover.payment.a.c(new d.b(byteBuffer2, byteBuffer3, byteBuffer4, byteBuffer5, byteBuffer6, new ByteBuffer(arrby), this.cN().dI()));
        this.cN().getPth().setBit(1, 7);
        com.samsung.android.spayfw.payprovider.discover.payment.data.c.a(this.cL(), this.cM());
        com.samsung.android.spayfw.payprovider.discover.payment.data.a a3 = new com.samsung.android.spayfw.payprovider.discover.payment.data.a(c2.dj(), DiscoverApduHandlerState.sW);
        a3.dD();
        this.cL().a(a3);
        return a3;
    }

    private void de() {
    }

    private int df() {
        ByteBuffer byteBuffer = this.cN().getPaymentProfile().getAfl();
        if (byteBuffer != null) {
            return byteBuffer.getSize() / 4;
        }
        return 0;
    }

    private void dg() {
        ByteBuffer byteBuffer = this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions();
        ByteBuffer byteBuffer2 = this.cM().getIssuerApplicationData().getIssuerApplicationData();
        Log.i("DCSDK_DiscoverGpoApduHandler", "computeIssuerApplicationData, computeIssuerApplicationData, copy cvr: " + this.cN().dH().toHexString());
        ByteBuffer byteBuffer3 = this.cN().dH();
        for (int i2 = 0; i2 < 8; ++i2) {
            byteBuffer2.setByte(i2 + 2, byteBuffer3.getByte(i2));
        }
        if (byteBuffer.checkBit(2, 6)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "computeIssuerApplicationData, computeIssuerApplicationData, compose IDD");
            com.samsung.android.spayfw.payprovider.discover.payment.data.c.a(this.cM().getIssuerApplicationData().getIADOL(), this.cN().getPaymentProfile(), this.cL(), this.cM().getIssuerApplicationData().getIDDTags(), byteBuffer2);
        }
        Log.d("DCSDK_DiscoverGpoApduHandler", "computeIssuerApplicationData, iad: " + byteBuffer2.toHexString());
        this.cN().dM().setIssuerApplicationData(byteBuffer2);
    }

    private void setCid(byte by) {
        ByteBuffer byteBuffer = this.cN().dH();
        byteBuffer.setBit(5, (by & 2) >> 1);
        byteBuffer.setBit(6, by & 1);
        com.samsung.android.spayfw.payprovider.discover.payment.data.b b2 = this.cN().dM();
        if (b2 == null) {
            this.cN().a(new com.samsung.android.spayfw.payprovider.discover.payment.data.b());
            b2 = this.cN().dM();
        }
        b2.setCid(by);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public com.samsung.android.spayfw.payprovider.discover.payment.data.a cK() {
        if (this.te.dk() != 128) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, cla is not supported, cla = " + this.te.dk() + ", expected " + 128);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(28160);
        }
        if ((255 & this.te.getINS()) != 168) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, ins is not supported, ins = " + (255 & this.te.getINS()) + ", expected " + 168);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27904);
        }
        if (this.te.getP1() != 0 || this.te.getP2() != 0) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, wrong p1 and/or p2, p1 = " + this.te.getP1() + ", p2 = " + this.te.getP2());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27270);
        }
        if (this.te.getData() == null || this.te.getLc() != this.te.getData().getSize()) {
            StringBuilder stringBuilder = new StringBuilder().append("processApdu, C-APDU GPO, wrong Lc = ").append(this.te.getLc()).append(", actual data length  = ");
            String string = this.te.getData() != null ? Integer.valueOf((int)this.te.getData().getSize()) : "null";
            Log.e("DCSDK_DiscoverGpoApduHandler", stringBuilder.append((Object)string).toString());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(26368);
        }
        if (6 + this.te.getLc() != this.te.getLength()) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, wrong length Lc= " + this.te.getLc() + ", actual data length  = " + (-6 + this.te.getLength()));
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(26368);
        }
        if (this.te.getData().getByte(0) != -125) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, wrong data field format, B1 " + (255 & this.te.getData().getByte(0)) + ", expected  = " + -125);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27264);
        }
        try {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, parse GPO data...");
            this.te.parse();
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, parsed GPO data.");
        }
        catch (ParseException parseException) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, parsing exception: " + parseException.getMessage());
            parseException.printStackTrace();
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27264);
        }
        catch (Exception exception) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, unknown exception while parsing: " + exception.getMessage());
            exception.printStackTrace();
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27264);
        }
        if (DiscoverCLTransactionContext.DiscoverClTransactionType.up.equals((Object)this.cN().dK())) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, ZIP AID, start ZIP transaction...");
            return this.cR();
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start EMV transaction...");
        if (!this.cQ()) {
            Log.e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, wrong mandatory tags.");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27264);
        }
        Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, check terminal mode support...");
        ByteBuffer byteBuffer = this.te.dm();
        if (byteBuffer.checkBit(1, 8)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, terminal mode: MS/EMV mode");
            if (!byteBuffer.checkBit(1, 6)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, terminal mode: MS only mode");
                return this.cR();
            }
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, terminal mode: EMV capable");
            if (this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(1, 8)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, MS is preferred mode.");
                return this.cR();
            }
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, EMV is preferred mode.");
            if (this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 4)) {
                Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start PDOL check...");
                return this.cW();
            }
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start CRM...");
            return this.cS();
        }
        if (!byteBuffer.checkBit(1, 8) && byteBuffer.checkBit(1, 6)) {
            Log.i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start CRM in EMV only mode...");
            return this.cS();
        }
        return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
    }

    private static class a {
        public boolean tf = false;
        public int tg = 0;
    }

}

