package com.samsung.android.spayfw.payprovider.discover.payment;

import com.mastercard.mobile_api.utils.apdu.emv.EMVGetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.DiscoverPayProvider;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverApduProcessingResult;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext.DiscoverClTransactionType;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCryptoData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverDataTags;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverGPOResponse.DiscoverGPOResponse;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTrackData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionLogData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CL;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CRM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CVM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.GPOCApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.GPORApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAController;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPConstants;
import com.samsung.android.spaytzsvc.api.TAException;
import java.text.ParseException;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.c */
public class DiscoverGpoApduHandler extends DiscoverApduHandler {
    private GPOCApdu te;

    /* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.c.a */
    private static class DiscoverGpoApduHandler {
        public boolean tf;
        public int tg;

        public DiscoverGpoApduHandler() {
            this.tf = false;
            this.tg = 0;
        }
    }

    public DiscoverGpoApduHandler(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, discoverTransactionContext, discoverPaymentCard);
        this.te = new GPOCApdu(byteBuffer);
    }

    public DiscoverApduProcessingResult cK() {
        if (this.te.dk() != X509KeyUsage.digitalSignature) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, cla is not supported, cla = " + this.te.dk() + ", expected " + X509KeyUsage.digitalSignature);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_CLA_NOT_SUPPORTED);
        } else if ((this.te.getINS() & GF2Field.MASK) != CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, ins is not supported, ins = " + (this.te.getINS() & GF2Field.MASK) + ", expected " + CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INS_NOT_SUPPORTED);
        } else if (this.te.getP1() != null || this.te.getP2() != null) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, wrong p1 and/or p2, p1 = " + this.te.getP1() + ", p2 = " + this.te.getP2());
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INCORRECT_P1P2);
        } else if (this.te.getData() == null || this.te.getLc() != this.te.getData().getSize()) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, wrong Lc = " + this.te.getLc() + ", actual data length  = " + (this.te.getData() != null ? Integer.valueOf(this.te.getData().getSize()) : "null"));
            return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_LENGTH);
        } else if (this.te.getLc() + 6 != this.te.getLength()) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, wrong length Lc= " + this.te.getLc() + ", actual data length  = " + (this.te.getLength() - 6));
            return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_LENGTH);
        } else if (this.te.getData().getByte(0) != (byte) -125) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, wrong data field format, B1 " + (this.te.getData().getByte(0) & GF2Field.MASK) + ", expected  = " + -125);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
        } else {
            try {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, parse GPO data...");
                this.te.parse();
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, parsed GPO data.");
                if (DiscoverClTransactionType.DISCOVER_CL_ZIP.equals(cN().dK())) {
                    Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, ZIP AID, start ZIP transaction...");
                    return cR();
                }
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start EMV transaction...");
                if (cQ()) {
                    Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, check terminal mode support...");
                    ByteBuffer dm = this.te.dm();
                    if (dm.checkBit(1, 8)) {
                        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, terminal mode: MS/EMV mode");
                        if (dm.checkBit(1, 6)) {
                            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, terminal mode: EMV capable");
                            if (cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(1, 8)) {
                                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, MS is preferred mode.");
                                return cR();
                            }
                            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, EMV is preferred mode.");
                            if (cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 4)) {
                                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start PDOL check...");
                                return cW();
                            }
                            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start CRM...");
                            return cS();
                        }
                        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, terminal mode: MS only mode");
                        return cR();
                    } else if (dm.checkBit(1, 8) || !dm.checkBit(1, 6)) {
                        return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                    } else {
                        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, start CRM in EMV only mode...");
                        return cS();
                    }
                }
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, EMV AID, wrong mandatory tags.");
                return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
            } catch (ParseException e) {
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, parsing exception: " + e.getMessage());
                e.printStackTrace();
                return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
            } catch (Exception e2) {
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, unknown exception while parsing: " + e2.getMessage());
                e2.printStackTrace();
                return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
            }
        }
    }

    private boolean cQ() {
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "TTQ: " + (this.te.dm() != null ? this.te.dm().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getAuthAmount: " + (this.te.dn() != null ? this.te.dn().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getOtherAmount: " + (this.te.m912do() != null ? this.te.m912do().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getTerminalCountryCode: " + (this.te.dp() != null ? this.te.dp().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getTerminalCurrencyCode: " + (this.te.dq() != null ? this.te.dq().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getTransactionDate: " + (this.te.dr() != null ? this.te.dr().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getTransactionType: " + (this.te.ds() != null ? this.te.ds().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getUnpredictableNumber: " + (this.te.dt() != null ? this.te.dt().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getTerminalType: " + (this.te.du() != null ? this.te.du().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getLoyalityProgram: " + (this.te.dv() != null ? this.te.dv().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getMerchantCategoryCode: " + (this.te.dw() != null ? this.te.dw().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getVAT1: " + (this.te.dx() != null ? this.te.dx().toHexString() : "null"));
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "getVAT2: " + (this.te.dy() != null ? this.te.dy().toHexString() : "null"));
        if (this.te.dm() == null || this.te.dm().getSize() < 4 || this.te.dn() == null || this.te.dn().getSize() < 6 || !m936b(this.te.dn()) || this.te.m912do() == null || this.te.m912do().getSize() < 6 || this.te.dp() == null || this.te.dp().getSize() < 2 || this.te.dq() == null || this.te.dq().getSize() < 2 || this.te.dr() == null || this.te.dr().getSize() < 3 || this.te.ds() == null || this.te.ds().getSize() < 1 || this.te.dt() == null || this.te.dt().getSize() < 4) {
            return false;
        }
        return true;
    }

    private boolean m936b(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return false;
        }
        try {
            Long.parseLong(byteBuffer.toHexString());
            return true;
        } catch (Exception e) {
            Log.m285d("DCSDK_DiscoverGpoApduHandler", "isNumericValue, value " + byteBuffer.toHexString() + " not numeric.");
            return false;
        }
    }

    private DiscoverApduProcessingResult cR() {
        String str = null;
        cN().m947a(DiscoverClTransactionType.DISCOVER_CL_ZIP);
        if (cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(1, 7)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processClTransactionInZipMode, dcvv supported.");
            ByteBuffer dt = this.te.dt();
            if (dt == null || !(dt.getSize() == 1 || dt.getSize() == 4)) {
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processClTransactionInZipMode, wrong un length, un length: " + (dt != null ? Integer.valueOf(dt.getSize()) : "null"));
                return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
            }
            try {
                DcTAController E = DcTAController.m1039E(DiscoverPayProvider.cC());
                if (dt.getSize() > 1) {
                    dt = dt.copyBytes(0, 1);
                }
                Log.m285d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, un to TA: " + dt.toHexString());
                try {
                    String toHexString;
                    long j = E.m1054l(dt.getBytes()).wO.get();
                    Log.m285d("DCSDK_DiscoverGpoApduHandler", "DCVV: " + j);
                    DiscoverCryptoData discoverCryptoData = new DiscoverCryptoData();
                    discoverCryptoData.m964u(j);
                    DiscoverTrackData discoverTrackData = new DiscoverTrackData(cL().dT(), dt, j, cL().ea());
                    dt = discoverTrackData.m959C(cM().getTrack1DataZipMsMode());
                    ByteBuffer D = discoverTrackData.m960D(cM().getTrack2DataZipMsMode());
                    ByteBuffer dZ = discoverTrackData.dZ();
                    Log.m285d("DCSDK_DiscoverGpoApduHandler", "track1: " + (dt != null ? dt.toHexString() : null));
                    String str2 = "DCSDK_DiscoverGpoApduHandler";
                    StringBuilder append = new StringBuilder().append("track2: ");
                    if (D != null) {
                        toHexString = D.toHexString();
                    } else {
                        toHexString = null;
                    }
                    Log.m285d(str2, append.append(toHexString).toString());
                    toHexString = "DCSDK_DiscoverGpoApduHandler";
                    StringBuilder append2 = new StringBuilder().append("dcvvData: ");
                    if (dZ != null) {
                        str = dZ.toHexString();
                    }
                    Log.m285d(toHexString, append2.append(str).toString());
                    discoverCryptoData.m963a(discoverTrackData);
                    cN().m948a(discoverCryptoData);
                    ByteBuffer a = BERTLV.m1001a((byte) 86, dt);
                    a.append(BERTLV.m1001a((byte) 87, D));
                    if (dZ != null) {
                        a.append(BERTLV.m1004c(DiscoverTrackData.wg, dZ));
                    }
                    a = BERTLV.m1001a((byte) 112, a);
                    DiscoverRecord discoverRecord = new DiscoverRecord();
                    discoverRecord.setRecordData(a);
                    discoverRecord.setSFI(new ByteBuffer(new byte[]{(byte) 1}));
                    discoverRecord.setRecordNumber(new ByteBuffer(new byte[]{(byte) 1}));
                    cN().m952b(discoverRecord);
                } catch (Throwable e) {
                    Log.m284c("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processClTransactionInZipMode Error: " + e.getMessage(), e);
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                }
            } catch (TAException e2) {
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processClTransactionInZipMode, unexpected TA exception: " + e2.getMessage());
                e2.printStackTrace();
                return new DiscoverApduProcessingResult((short) ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
            } catch (Exception e3) {
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processClTransactionInZipMode, unexpected exception: " + e3.getMessage());
                e3.printStackTrace();
                return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
            }
        }
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processClTransactionInZipMode, dcvv not supported.");
        GPORApdu gPORApdu = new GPORApdu(cM().getZipAip(), cM().getZipAfl());
        DiscoverDataTags.m977b(cL(), cM());
        DiscoverDataTags.m972a(cL(), cM());
        return new DiscoverApduProcessingResult(gPORApdu.dj(), DiscoverApduHandlerState.DiscoverAPDUStateInitiated);
    }

    private DiscoverApduProcessingResult cS() {
        long j = DSRPConstants.DSRP_INPUT_AMOUNT_MAX;
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm starting...");
        ByteBuffer dH = cN().dH();
        ByteBuffer pth = cN().getPth();
        if (pth.getByte(1) != null) {
            dH.setByte(0, (byte) ((dH.getByte(0) & -16) | (pth.getByte(1) & 15)));
        }
        if (pth.checkBit(1, 8)) {
            dH.setBit(4, 8);
        }
        if (pth.checkBit(1, 7)) {
            dH.setBit(4, 7);
        }
        if (pth.checkBit(1, 5)) {
            dH.setBit(4, 6);
        }
        if (pth.checkBit(1, 3)) {
            dH.setBit(4, 5);
        }
        if (pth.checkBit(1, 1)) {
            dH.setBit(4, 4);
        }
        pth = this.te.dm();
        if (pth.checkBit(2, 8)) {
            dH.setBit(2, 8);
        }
        if (pth.checkBit(2, 7)) {
            dH.setBit(3, 8);
        }
        if (pth.checkBit(1, 5)) {
            dH.setBit(7, 8);
        }
        if (pth.checkBit(1, 4)) {
            dH.setBit(7, 7);
        }
        if (pth.checkBit(1, 3)) {
            dH.setBit(7, 6);
        }
        if (pth.checkBit(1, 2)) {
            dH.setBit(7, 5);
        }
        if (pth.checkBit(3, 8)) {
            dH.setBit(7, 4);
        }
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, after pth, ttq check, cvr " + dH.toHexString());
        pth = cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions();
        if (pth.checkBit(1, 1) && cM().getCountryCode().equals(this.te.dp())) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, domestic transaction,  domestic transaction CL ACO B1b1 = 1 & country code equals");
            dH.setBit(2, 5);
        } else {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, international transaction , country code check.");
            dH.setBit(2, 4);
        }
        if (pth.checkBit(1, 1) || !cM().getCurrencyCodeCode().equals(this.te.dq())) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, international transaction, currency code check.");
            dH.setBit(2, 4);
        } else {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, domestic transaction CL ACO B1b1 = 0 & currency code equals");
            dH.setBit(2, 5);
        }
        byte b = this.te.ds().getByte(0);
        if (b == (byte) 1 || b == 9) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, cash advance/goods service");
            dH.setBit(2, 7);
        } else if (b == 32) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, refund");
            dH.setBit(2, 6);
        }
        if (cN().getPaymentProfile().getAip().checkBit(1, 6) || !cN().getPaymentProfile().getAip().checkBit(1, 1)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, offline, perform cvm or");
            return cU();
        }
        if (b == 32) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, process refund transaction.");
            if (!cM().getCaco().checkBit(1, 4)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, do not count/accumulate refund.");
                return cU();
            }
        }
        long parseLong = Long.parseLong(this.te.dn().toHexString());
        long parseLong2 = Long.parseLong(this.te.m912do().toHexString());
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, authAmount: " + parseLong);
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, otherAmount: " + parseLong2);
        if (parseLong == 0 && parseLong2 == 0) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCrm, auth amount is 0, and other amount is 0.");
        } else if (cM().getCurrencyCodeCode().equals(this.te.dq())) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, increment accumulator, currency code is ok.");
            m940d(parseLong, parseLong2);
        } else if (cM().getSecondaryCurrency1().copyBytes(1, 2).equals(this.te.dq())) {
            parseLong = m932a(parseLong, cM().getSecondaryCurrency1());
            parseLong2 = m932a(parseLong2, cM().getSecondaryCurrency1());
            if (parseLong > DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
                parseLong = DSRPConstants.DSRP_INPUT_AMOUNT_MAX;
            }
            if (parseLong2 <= DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
                j = parseLong2;
            }
            this.te.m913e(ByteBuffer.getFromLong(parseLong));
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, increment accumulator, convert amount to sec currency 1.");
            m940d(parseLong, j);
        } else if (cM().getSecondaryCurrency2().copyBytes(1, 2).equals(this.te.dq())) {
            parseLong2 = m932a(parseLong, cM().getSecondaryCurrency2());
            if (parseLong2 > DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
                parseLong2 = DSRPConstants.DSRP_INPUT_AMOUNT_MAX;
            }
            parseLong = m932a(parseLong, cM().getSecondaryCurrency2());
            if (parseLong <= DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
                j = parseLong;
            }
            this.te.m913e(ByteBuffer.getFromLong(parseLong2));
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, increment accumulator, convert amount to sec currency 2.");
            m940d(parseLong2, j);
        }
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, before processCrmCvmCounters, cvr " + dH.toHexString());
        cT();
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, after processCrmCvmCounters, cvr " + dH.toHexString());
        return cU();
    }

    private long m932a(long j, ByteBuffer byteBuffer) {
        Exception e;
        if (byteBuffer == null || byteBuffer.getSize() != 5) {
            String str;
            String str2 = "DCSDK_DiscoverGpoApduHandler";
            StringBuilder append = new StringBuilder().append("convertAmount error, currency convertion code wrong size: ");
            if (byteBuffer == null) {
                str = "null";
            } else {
                str = byteBuffer.toHexString();
            }
            Log.m286e(str2, append.append(str).toString());
            return j;
        }
        long longValue;
        try {
            longValue = Long.getLong(byteBuffer.copyBytes(3, 4).toHexString()).longValue();
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "convertAmount convertion rate: " + longValue);
            long j2 = (long) byteBuffer.getByte(5);
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "convertAmount convertion exponent: " + j2);
            if (((j2 >> 7) & 1) == 1) {
                longValue = (long) (((double) (longValue * j)) / Math.pow(10.0d, (double) (j2 & 127)));
            } else {
                longValue = (long) (((double) (longValue * j)) * Math.pow(10.0d, (double) (j2 & 127)));
            }
            try {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "convertAmount, value: " + longValue);
                return longValue;
            } catch (Exception e2) {
                e = e2;
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "convertAmount, unexpected exception during currency conversion: " + e.getMessage());
                return longValue;
            }
        } catch (Exception e3) {
            e = e3;
            longValue = j;
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "convertAmount, unexpected exception during currency conversion: " + e.getMessage());
            return longValue;
        }
    }

    private void m940d(long j, long j2) {
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, START");
        ByteBuffer caco = cM().getCaco();
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, caco: " + caco.toHexString());
        long j3 = 0;
        if (!caco.checkBit(2, 8) && !caco.checkBit(2, 7)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added auth amount: " + j);
            j3 = j;
        } else if (!caco.checkBit(2, 8) && caco.checkBit(2, 7)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added other amount: " + j2);
            j3 = j2;
        } else if (caco.checkBit(2, 8) && !caco.checkBit(2, 7)) {
            j3 = j + j2;
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added both amounts: " + j3);
        }
        j3 += cN().getPaymentProfile().getCRM().getCrmAccumulator();
        if (j3 > DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
            j3 = DSRPConstants.DSRP_INPUT_AMOUNT_MAX;
        }
        cN().getPaymentProfile().getCRM().setCrmAccumulator(j3);
        if (caco.checkBit(1, 8)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, cl accumulator");
            j3 = 0;
            if (!caco.checkBit(4, 8) && !caco.checkBit(4, 7)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added auth amount: " + j);
                j3 = j;
            } else if (!caco.checkBit(4, 8) && caco.checkBit(4, 7)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added other amount: " + j2);
                j3 = j2;
            } else if (caco.checkBit(4, 8) && !caco.checkBit(4, 7)) {
                j3 = j + j2;
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added both amount: " + j3);
            }
            j3 += cN().getPaymentProfile().getCl().getClAccumulator();
            if (j3 > DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
                j3 = DSRPConstants.DSRP_INPUT_AMOUNT_MAX;
            }
            cN().getPaymentProfile().getCl().setClAccumulator(j3);
        }
        if (caco.checkBit(1, 6)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, cvm accumulator");
            if (!caco.checkBit(5, 8) && !caco.checkBit(5, 7)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added auth amount: " + j);
            } else if (!caco.checkBit(5, 8) && caco.checkBit(5, 7)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added other amount: " + j2);
                j = j2;
            } else if (!caco.checkBit(5, 8) || caco.checkBit(5, 7)) {
                j = 0;
            } else {
                j += j2;
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, added both amount: " + j);
            }
            j3 = cN().getPaymentProfile().getCVM().getCvmAccumulator() + j;
            if (j3 > DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
                j3 = DSRPConstants.DSRP_INPUT_AMOUNT_MAX;
            }
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, cvmAddedAmount: " + j3);
            cN().getPaymentProfile().getCVM().setCvmAccumulator(j3);
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, set amount.");
        }
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, END");
    }

    private void cT() {
        ByteBuffer caco = cM().getCaco();
        long parseLong = Long.parseLong(this.te.dn().toHexString());
        ByteBuffer dp = this.te.dp();
        ByteBuffer countryCode = cM().getCountryCode();
        ByteBuffer dq = this.te.dq();
        ByteBuffer currencyCodeCode = cM().getCurrencyCodeCode();
        CRM crm = cN().getPaymentProfile().getCRM();
        CL cl = cN().getPaymentProfile().getCl();
        CVM cvm = cN().getPaymentProfile().getCVM();
        int i = caco.getByte(2) & 7;
        if (parseLong != 0 || (parseLong == 0 && caco.checkBit(1, 3))) {
            if (dp.equals(countryCode)) {
                if (i == 0) {
                    crm.incrementCrmCounter();
                }
            } else if (i == 1) {
                crm.incrementCrmCounter();
            }
            if (dq.equals(currencyCodeCode)) {
                if (i == 2) {
                    crm.incrementCrmCounter();
                }
            } else if (i == 3) {
                crm.incrementCrmCounter();
            }
        } else if (i == 4) {
            crm.incrementCrmCounter();
        }
        if (caco.checkBit(1, 7)) {
            cN().getPaymentProfile().getCl().incrementClCounter();
        }
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, before increment cvm counter: " + cN().getPaymentProfile().getCVM().getCvmCounter());
        if (caco.checkBit(1, 5) && !this.te.dm().checkBit(2, 7)) {
            Log.m285d("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, increment cvm counter, " + cN().getPaymentProfile().getCVM().getCvmCounter());
            cN().getPaymentProfile().getCVM().incrementCvmCounter();
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "incrementCrmAccumulators, after increment cvm counter, " + cN().getPaymentProfile().getCVM().getCvmCounter());
        }
        caco = cN().dH();
        if (crm.getCrmAccumulator() > crm.getLCOA()) {
            caco.setBit(5, 3);
        }
        if (crm.getCrmAccumulator() > crm.getUCOA()) {
            caco.setBit(5, 2);
        }
        if (cl.getClAccumulator() > cl.getCL_Cum_Limit()) {
            caco.setBit(2, 7);
        }
        if (cvm.getCvmAccumulator() > cvm.getCVM_Cum_Limit_1()) {
            caco.setBit(3, 4);
        }
        if (cvm.getCvmAccumulator() > cvm.getCVM_Cum_Limit_2()) {
            caco.setBit(3, 3);
        }
        if (crm.getCrmCounter() > crm.getLCOL()) {
            caco.setBit(5, 5);
        }
        if (crm.getCrmCounter() > crm.getUCOL()) {
            caco.setBit(5, 4);
        }
        if (cl.getClCounter() > cl.getCL_Cons_Limit()) {
            caco.setBit(5, 8);
        }
        if (cvm.getCvmCounter() > cvm.getCVM_Cons_Limit_1()) {
            caco.setBit(3, 6);
        }
        if (cvm.getCvmCounter() > cvm.getCVM_Cons_Limit_2()) {
            caco.setBit(3, 5);
        }
        if (dq.equals(currencyCodeCode) || dq.equals(cM().getSecondaryCurrency1()) || dq.equals(cM().getSecondaryCurrency2())) {
            if (parseLong > cN().getPaymentProfile().getCRM().getSTA()) {
                caco.setBit(5, 1);
            }
            if (parseLong > cl.getCL_STA_Limit()) {
                caco.setBit(5, 6);
            }
            if (parseLong > cvm.getCVM_Sta_Limit_1()) {
                caco.setBit(3, 2);
            }
            if (parseLong > cvm.getCVM_Sta_Limit_2()) {
                caco.setBit(3, 1);
            }
            if (parseLong > cvm.getCVM_Sta_Limit_2()) {
                caco.setBit(3, 1);
            }
        }
    }

    private DiscoverApduProcessingResult cU() {
        ByteBuffer dm = this.te.dm();
        if (dm == null) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, empty ttq.");
            return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
        }
        if (dm.checkBit(3, 7)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, terminal supports CDCVM.");
            if (cN().dN() == (byte) 1) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCF = 1.");
                if (cN().dO() == (byte) 1) {
                    Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCI = 1.");
                    cN().dH().setBit(2, 1);
                }
            } else if (dm.checkBit(3, 4)) {
                return new DiscoverApduProcessingResult((short) ISO7816.SW_COMMAND_NOT_ALLOWED);
            } else {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCF = 0.");
                cN().dH().setBit(2, 2);
                if (cN().dO() == (byte) 1) {
                    Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, CCI = 1.");
                    cN().dH().setBit(2, 1);
                }
            }
        } else {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, terminal doesn't support CDCVM.");
            if (dm.checkBit(1, 3)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, online PIN supported.");
                dm = new ByteBuffer(2);
                dm.setByte(0, cN().dH().getByte(1));
                dm.setByte(1, cN().dH().getByte(2));
                if (dm.checkBitAndMatch(cN().getPaymentProfile().getCVM().getCVM_CAC_Online_PIN())) {
                    Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCvm, match found, set online PIN required.");
                    cN().dI().setBit(1, 8);
                }
            }
        }
        return cV();
    }

    private DiscoverApduProcessingResult cV() {
        CRM crm = cN().getPaymentProfile().getCRM();
        ByteBuffer dH = cN().dH();
        ByteBuffer byteBuffer = new ByteBuffer(3);
        byteBuffer.setByte(0, dH.getByte(1));
        byteBuffer.setByte(1, dH.getByte(3));
        byteBuffer.setByte(2, dH.getByte(4));
        if (byteBuffer.checkBitAndMatch(crm.getCRM_CAC_Denial())) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra, CM-CAC-Decline match found, aac. CVR data: " + byteBuffer.toHexString() + ", mask: " + crm.getCRM_CAC_Denial().toHexString());
            return cX();
        }
        dH = this.te.dm();
        if (dH.checkBit(2, 8)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  online cryptogram required.");
            return dc();
        } else if (dH.checkBit(1, 4)) {
            Log.m285d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  offline only reader.");
            if (byteBuffer.checkBitAndMatch(crm.getCRM_CAC_Default())) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra, CM-CAC-Default match found, aac. CVR data: " + byteBuffer.toHexString() + ", mask: " + crm.getCRM_CAC_Default().toHexString());
                return cX();
            } else if (cN().getPaymentProfile().getAip().checkBit(1, 1)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda supported, tc.");
                return dd();
            } else {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda not supported, aac.");
                return cX();
            }
        } else if (byteBuffer.checkBitAndMatch(crm.getCRM_CAC_Online())) {
            Log.m285d("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra, CM-CAC-Online match found, arqc. CVR data: " + byteBuffer.toHexString() + ", mask: " + crm.getCRM_CAC_Online().toHexString());
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  arqc.");
            return dc();
        } else if (cN().getPaymentProfile().getAip().checkBit(1, 1)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda supported, tc.");
            return dd();
        } else {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performCra,  cda not supported, aac.");
            return cX();
        }
    }

    private DiscoverApduProcessingResult cW() {
        PDOLCheckEntry[] pdolDeclineEntries = cM().getPdolDeclineEntries();
        ByteBuffer dH = cN().dH();
        if (pdolDeclineEntries == null || !m937b(pdolDeclineEntries)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  get profile entries...");
            pdolDeclineEntries = cM().getPdolProfileEntries();
            if (pdolDeclineEntries != null) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  Process PDOL profile...");
                m935a(pdolDeclineEntries);
            } else if (cM().getPDOLProfileCheckTable() != null) {
                Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL Profile check table cannot be parsed.");
                dH.setBit(4, 3);
            }
            pdolDeclineEntries = cM().getPdolOnlineEntries();
            if (pdolDeclineEntries != null && m937b(pdolDeclineEntries)) {
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  PDOL Online match found.");
                dH.setBit(4, 2);
            }
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolProfile,  perform CRM.");
            return cS();
        }
        Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, performPdolCheck,  PDOL Decline match found, aac.");
        dH.setBit(4, 3);
        return cX();
    }

    private boolean m935a(PDOLCheckEntry[] pDOLCheckEntryArr) {
        DiscoverGpoApduHandler d = m939d(pDOLCheckEntryArr);
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "Result: " + d.tg);
        if (d.tf) {
            byte b = cN().dH().getByte(5);
            Log.m285d("DCSDK_DiscoverGpoApduHandler", "cvrB6 1: " + b);
            b = (byte) (b | d.tg);
            Log.m285d("DCSDK_DiscoverGpoApduHandler", "cvrB6 2: " + b);
            cN().dH().setByte(5, b);
            Log.m285d("DCSDK_DiscoverGpoApduHandler", "cvrB6: " + cN().dH().toHexString());
        }
        DiscoverPaymentProfile discoverPaymentProfile = (DiscoverPaymentProfile) cM().getPaymentProfiles().get(Integer.valueOf(d.tg));
        if (discoverPaymentProfile != null) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolProfile,  set payment profile, id = " + (d.tg - 1));
            cN().setSelectedPaymentProfile(discoverPaymentProfile);
        } else {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, processPdolProfile,  profile id = " + (d.tg - 1) + " not found, use default profile.");
        }
        return d.tf;
    }

    private boolean m937b(PDOLCheckEntry[] pDOLCheckEntryArr) {
        DiscoverGpoApduHandler c = m938c(pDOLCheckEntryArr);
        if (c.tf) {
            cN().dH().setByte(6, (byte) (cN().dH().getByte(6) | (c.tg << 4)));
        }
        return c.tf;
    }

    private DiscoverGpoApduHandler m938c(PDOLCheckEntry[] pDOLCheckEntryArr) {
        return m933a(pDOLCheckEntryArr, false);
    }

    private DiscoverGpoApduHandler m939d(PDOLCheckEntry[] pDOLCheckEntryArr) {
        return m933a(pDOLCheckEntryArr, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.samsung.android.spayfw.payprovider.discover.payment.DiscoverGpoApduHandler.DiscoverGpoApduHandler m933a(com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry[] r13, boolean r14) {
        /*
        r12 = this;
        r0 = "DCSDK_DiscoverGpoApduHandler";
        r1 = "processApdu, C-APDU GPO, performPdolProfile,  Process PDOL entries...";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        r4 = new com.samsung.android.spayfw.payprovider.discover.payment.c$a;
        r4.<init>();
        if (r13 != 0) goto L_0x0017;
    L_0x000e:
        r0 = "DCSDK_DiscoverGpoApduHandler";
        r1 = "processApdu, C-APDU GPO, processPdolDecline,  PDOL Decline entries is null.";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r4;
    L_0x0016:
        return r0;
    L_0x0017:
        r0 = 0;
    L_0x0018:
        r1 = r13.length;
        if (r0 >= r1) goto L_0x0439;
    L_0x001b:
        r5 = r13[r0];
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry ";
        r2 = r2.append(r3);
        r2 = r2.append(r0);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
        if (r5 == 0) goto L_0x0018;
    L_0x0037:
        r1 = 0;
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry data type:";
        r3 = r3.append(r6);
        r6 = r5.getDataType();
        r3 = r3.append(r6);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r3);
        r2 = r5.getDataType();
        switch(r2) {
            case 1: goto L_0x007f;
            case 2: goto L_0x013d;
            case 3: goto L_0x005b;
            case 4: goto L_0x015f;
            case 5: goto L_0x005b;
            case 6: goto L_0x005b;
            case 7: goto L_0x005b;
            case 8: goto L_0x0150;
            default: goto L_0x005b;
        };
    L_0x005b:
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "processApdu, C-APDU GPO, performPdolProfile,  value to compare: ";
        r3 = r3.append(r6);
        r3 = r3.append(r1);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r3);
        if (r1 != 0) goto L_0x019c;
    L_0x0075:
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r2 = "processApdu, C-APDU GPO, processPdolDecline,  parse error, PDOL value is null.";
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        r0 = r0 + 1;
        goto L_0x0018;
    L_0x007f:
        r1 = r12.te;
        r2 = r1.dz();
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry data type pdol:";
        r3 = r3.append(r6);
        r3 = r3.append(r2);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r3);
        if (r2 != 0) goto L_0x00aa;
    L_0x009f:
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r2 = "processApdu, C-APDU GPO, processPdolDecline,  PDOL value is null, continue.";
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        r0 = r0 + 1;
        goto L_0x0018;
    L_0x00aa:
        r1 = new com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
        r3 = r5.getDataOffset();
        r1.<init>(r3);
        r3 = r1.getInt();
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry offset:";
        r6 = r6.append(r7);
        r6 = r6.append(r3);
        r6 = r6.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r6);
        r1 = r2.getSize();
        r6 = r5.getDataSize();
        r6 = r6 + r3;
        if (r1 > r6) goto L_0x0112;
    L_0x00da:
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "processApdu, C-APDU GPO, processPdolDecline,  PDOL size is less or equal offset + length:  pdol size: ";
        r6 = r6.append(r7);
        r2 = r2.getSize();
        r2 = r6.append(r2);
        r6 = ", offset: ";
        r2 = r2.append(r6);
        r2 = r2.append(r3);
        r3 = " data length: ";
        r2 = r2.append(r3);
        r3 = r5.getDataSize();
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        r0 = r0 + 1;
        goto L_0x0018;
    L_0x0112:
        r6 = "DCSDK_DiscoverGpoApduHandler";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r7 = "processApdu, C-APDU GPO, performPdolProfile,  PDOL entry data type pdol hex:";
        r7 = r1.append(r7);
        if (r2 == 0) goto L_0x013b;
    L_0x0121:
        r1 = r2.toHexString();
    L_0x0125:
        r1 = r7.append(r1);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r6, r1);
        r1 = r5.getDataSize();
        r1 = r1 + r3;
        r1 = r2.copyBytes(r3, r1);
        goto L_0x005b;
    L_0x013b:
        r1 = 0;
        goto L_0x0125;
    L_0x013d:
        r1 = new com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
        r2 = r5.getDataOffset();
        r1.<init>(r2);
        r2 = r12.cM();
        r1 = com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverDataTags.m978c(r1, r2);
        goto L_0x005b;
    L_0x0150:
        r1 = new com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
        r2 = 1;
        r2 = new byte[r2];
        r3 = 0;
        r6 = -128; // 0xffffffffffffff80 float:NaN double:NaN;
        r2[r3] = r6;
        r1.<init>(r2);
        goto L_0x005b;
    L_0x015f:
        r1 = r12.cN();
        r2 = r1.dS();
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "processApdu, C-APDU GPO, performPdolProfile,  aliasId:";
        r3 = r3.append(r6);
        r3 = r3.append(r2);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r3);
        r1 = -1;
        if (r2 != r1) goto L_0x018c;
    L_0x0182:
        r0 = "DCSDK_DiscoverGpoApduHandler";
        r1 = "processApdu, C-APDU GPO, processPdolDecline,  aliasId is not defined";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r4;
        goto L_0x0016;
    L_0x018c:
        r1 = new com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
        r3 = 1;
        r3 = new byte[r3];
        r6 = 0;
        r2 = r2 & 255;
        r2 = (byte) r2;
        r3[r6] = r2;
        r1.<init>(r3);
        goto L_0x005b;
    L_0x019c:
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "processApdu, C-APDU GPO, performPdolProfile,  value to compare: ";
        r3 = r3.append(r6);
        r6 = r1.toHexString();
        r3 = r3.append(r6);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);
        r2 = r5.getBitMask();
        r6 = r1.bitAnd(r2);
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "processApdu, C-APDU GPO, performPdolProfile,  compareResult: ";
        r2 = r2.append(r3);
        r2 = r2.append(r6);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
        if (r6 != 0) goto L_0x01e5;
    L_0x01da:
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r2 = "processApdu, C-APDU GPO, processPdolDecline,  comparison result is null.";
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        r0 = r0 + 1;
        goto L_0x0018;
    L_0x01e5:
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "processApdu, C-APDU GPO, performPdolProfile,  compareResult hex: ";
        r2 = r2.append(r3);
        r3 = r6.toHexString();
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
        r3 = 0;
        r2 = r5.getMatchNotFound();
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "processApdu, C-APDU GPO, performPdolProfile,  entry.getTestType() ";
        r7 = r7.append(r8);
        r8 = r5.getTestType();
        r7 = r7.append(r8);
        r7 = r7.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r7);
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "processApdu, C-APDU GPO, performPdolProfile,  entry.getNumberMatchValues() ";
        r7 = r7.append(r8);
        r8 = r5.getNumberMatchValues();
        r7 = r7.append(r8);
        r7 = r7.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r7);
        r1 = 0;
    L_0x023f:
        r7 = r5.getValues();
        r7 = r7.length;
        if (r1 >= r7) goto L_0x0267;
    L_0x0246:
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "processApdu, C-APDU GPO, performPdolProfile, value: ";
        r8 = r8.append(r9);
        r9 = r5.getValues();
        r9 = r9[r1];
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r7, r8);
        r1 = r1 + 1;
        goto L_0x023f;
    L_0x0267:
        r1 = 0;
    L_0x0268:
        r7 = r5.getMatchNotFound();
        if (r2 != r7) goto L_0x03b2;
    L_0x026e:
        r7 = r5.getNumberMatchValues();
        if (r3 >= r7) goto L_0x03b2;
    L_0x0274:
        if (r1 != 0) goto L_0x03b2;
    L_0x0276:
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "processApdu, C-APDU GPO, performPdolProfile,  entry.getTestType() inside: ";
        r8 = r8.append(r9);
        r9 = r5.getTestType();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r7, r8);
        r7 = r5.getTestType();
        switch(r7) {
            case 0: goto L_0x038d;
            case 1: goto L_0x036d;
            case 2: goto L_0x029c;
            case 3: goto L_0x0299;
            case 4: goto L_0x0304;
            case 5: goto L_0x0299;
            case 6: goto L_0x0299;
            case 7: goto L_0x0299;
            case 8: goto L_0x039a;
            default: goto L_0x0299;
        };
    L_0x0299:
        r3 = r3 + 1;
        goto L_0x0268;
    L_0x029c:
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = "processApdu, C-APDU GPO, performPdolProfile,  check compareResult, no match";
        com.samsung.android.spayfw.p002b.Log.m287i(r7, r8);
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) ";
        r8 = r8.append(r9);
        r9 = r5.getValues();
        r9 = r9[r3];
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r7, r8);
        r7 = r5.getValues();
        r7 = r7[r3];
        if (r7 == 0) goto L_0x02eb;
    L_0x02c9:
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) ";
        r8 = r8.append(r9);
        r9 = r5.getValues();
        r9 = r9[r3];
        r9 = r9.toHexString();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r7, r8);
    L_0x02eb:
        r7 = r5.getValues();
        r7 = r7[r3];
        r7 = r6.equals(r7);
        if (r7 != 0) goto L_0x0299;
    L_0x02f7:
        r2 = r5.getMatchFound();
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r7 = "processApdu, C-APDU GPO, performPdolProfile, found action code match found.";
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r7);
        r1 = 1;
        goto L_0x0299;
    L_0x0304:
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = "processApdu, C-APDU GPO, performPdolProfile,  check compareResult, exact match";
        com.samsung.android.spayfw.p002b.Log.m287i(r7, r8);
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) ";
        r8 = r8.append(r9);
        r9 = r5.getValues();
        r9 = r9[r3];
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r7, r8);
        r7 = r5.getValues();
        r7 = r7[r3];
        if (r7 == 0) goto L_0x0353;
    L_0x0331:
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "processApdu, C-APDU GPO, performPdolProfile,  entry.getValues()[valuesCounter]) ";
        r8 = r8.append(r9);
        r9 = r5.getValues();
        r9 = r9[r3];
        r9 = r9.toHexString();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r7, r8);
    L_0x0353:
        r7 = r5.getValues();
        r7 = r7[r3];
        r7 = r6.equals(r7);
        if (r7 == 0) goto L_0x0299;
    L_0x035f:
        r2 = r5.getMatchFound();
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r7 = "processApdu, C-APDU GPO, performPdolProfile, found action code match found.";
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r7);
        r1 = 1;
        goto L_0x0299;
    L_0x036d:
        r7 = "DCSDK_DiscoverGpoApduHandler";
        r8 = "processApdu, C-APDU GPO, performPdolProfile, test type equal or greater.";
        com.samsung.android.spayfw.p002b.Log.m287i(r7, r8);
        r8 = r6.getLong();
        r7 = r5.getValues();
        r7 = r7[r3];
        r10 = r7.getLong();
        r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r7 < 0) goto L_0x0299;
    L_0x0386:
        r2 = r5.getMatchFound();
        r1 = 1;
        goto L_0x0299;
    L_0x038d:
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r7 = "processApdu, C-APDU GPO, performPdolProfile, test type always.";
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r7);
        r2 = r5.getMatchFound();
        goto L_0x0299;
    L_0x039a:
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r7 = "processApdu, C-APDU GPO, performPdolProfile, test type never.";
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r7);
        r2 = r5.getNumberMatchValues();
        r2 = r2 + -1;
        if (r3 != r2) goto L_0x03af;
    L_0x03a9:
        r2 = r5.getMatchNotFound();
        goto L_0x0299;
    L_0x03af:
        r2 = 0;
        goto L_0x0299;
    L_0x03b2:
        r1 = r2 >> 4;
        r1 = r1 & 15;
        r1 = (byte) r1;
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "processApdu, C-APDU GPO, performPdolProfile, action code: ";
        r3 = r3.append(r6);
        r3 = r3.append(r1);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r3);
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "processApdu, C-APDU GPO, performPdolProfile, result: ";
        r3 = r3.append(r6);
        r6 = r5.getResult();
        r3 = r3.append(r6);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r3);
        r2 = r1 & 8;
        r3 = 8;
        if (r2 != r3) goto L_0x0401;
    L_0x03f1:
        r2 = "DCSDK_DiscoverGpoApduHandler";
        r3 = "processApdu, C-APDU GPO, performPdolProfile, set value mask.";
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r3);
        r2 = 1;
        r4.tf = r2;
        r2 = r5.getResult();
        r4.tg = r2;
    L_0x0401:
        r2 = r1 & 1;
        r3 = 1;
        if (r2 != r3) goto L_0x042b;
    L_0x0406:
        r0 = "DCSDK_DiscoverGpoApduHandler";
        r1 = "processApdu, C-APDU GPO, performPdolProfile, jump to line mask.";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        r0 = r5.getResult();
        r1 = "DCSDK_DiscoverGpoApduHandler";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "processApdu, C-APDU GPO, performPdolProfile, jump to entry: ";
        r2 = r2.append(r3);
        r2 = r2.append(r0);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
        goto L_0x0018;
    L_0x042b:
        if (r14 == 0) goto L_0x043c;
    L_0x042d:
        r1 = r1 & 2;
        r2 = 2;
        if (r1 != r2) goto L_0x043c;
    L_0x0432:
        r0 = "DCSDK_DiscoverGpoApduHandler";
        r1 = "processApdu, C-APDU GPO, performPdolProfile, exit mask.";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
    L_0x0439:
        r0 = r4;
        goto L_0x0016;
    L_0x043c:
        r0 = r0 + 1;
        goto L_0x0018;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.discover.payment.c.a(com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry[], boolean):com.samsung.android.spayfw.payprovider.discover.payment.c$a");
    }

    private DiscoverApduProcessingResult cX() {
        return m934a((byte) 0);
    }

    private void cY() {
        DiscoverCryptoData dM = cN().dM();
        if (dM == null) {
            dM = new DiscoverCryptoData();
        }
        ByteBuffer da = da();
        ByteBuffer db = db();
        if (da == null) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, input data 1 is empty.");
            throw new Exception("Conditions not satisfied, input data is empty");
        } else if (db == null) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, input data 2 is empty.");
            throw new Exception("Conditions not satisfied, input data is empty");
        } else {
            try {
                byte[] c = DcTAController.m1039E(DiscoverPayProvider.cC()).m1050c(da.getBytes(), db.getBytes());
                if (c.length != 8) {
                    Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, computeAppCryptogram length is wrong, length " + c.length);
                    throw new Exception("Conditions not satisfied, crypto length is wrong, length " + c.length);
                }
                dM.m965u(cL().dT());
                dM.m966v(new ByteBuffer(c));
                Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, generated cryptogram, exit.");
            } catch (Throwable e) {
                Log.m284c("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, computeApplicationCryptogram, computeAppCryptogram response is null. " + e.getMessage(), e);
                throw new Exception("Conditions not satisfied, crypto data is empty");
            }
        }
    }

    private void cZ() {
        cN().m949a(new DiscoverTransactionLogData(this.te.dn(), this.te.dq(), this.te.dr(), cN().dM().dT(), this.te.dp(), null, this.te.ds(), this.te.dt(), this.te.m912do(), cN().dH(), this.te.dm(), cN().dI(), cN().getPaymentProfile().getCtq(), cN().dM().dU()));
    }

    private ByteBuffer da() {
        if (cN().dM() == null) {
            cN().m948a(new DiscoverCryptoData());
        }
        ByteBuffer dn = this.te.dn();
        dn.append(this.te.dq());
        dn.append(this.te.dt());
        return dn;
    }

    private ByteBuffer db() {
        if (cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 7)) {
            return cN().dH();
        }
        return cM().getIssuerApplicationData().getIssuerApplicationData();
    }

    private DiscoverApduProcessingResult dc() {
        cN().dH().setBit(1, 6);
        cN().dH().clearBit(1, 5);
        DiscoverApduProcessingResult a = m934a((byte) VerifyPINApdu.P2_PLAINTEXT);
        cN().getPth().clearBit(1, 5);
        ByteBuffer caco = cM().getCaco();
        if (caco.checkBit(2, 4) && caco.checkBit(3, 4) && caco.checkBit(4, 4) && caco.checkBit(5, 4)) {
            cN().getPaymentProfile().getCRM().resetCrm();
        }
        return a;
    }

    private DiscoverApduProcessingResult dd() {
        int i = 6;
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, tc");
        setCid(EMVGetStatusApdu.P1);
        dg();
        DiscoverApduProcessingResult discoverApduProcessingResult;
        try {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, tc, compute application cryptogram");
            cY();
            ByteBuffer caco = cM().getCaco();
            if (caco != null && caco.getSize() >= r2 && caco.checkBit(r2, 2)) {
                CRM crm = cN().getPaymentProfile().getCRM();
                cN().m955t(crm.getUCOA() - crm.getCrmAccumulator());
            }
            de();
            cN().m944L(df());
            cN().m951b((byte) 0);
            cN().m953c((byte) 1);
            DiscoverCryptoData dM = cN().dM();
            GPORApdu gPORApdu = new GPORApdu(new DiscoverGPOResponse(cN().getPaymentProfile().getAip(), cN().getPaymentProfile().getAfl(), dM.dV(), dM.dT(), dM.getIssuerApplicationData(), new ByteBuffer(new byte[]{dM.getCid()}), cN().dI()));
            cN().getPth().setBit(1, 7);
            DiscoverDataTags.m972a(cL(), cM());
            discoverApduProcessingResult = new DiscoverApduProcessingResult(gPORApdu.dj(), DiscoverApduHandlerState.DiscoverAPDUStateInitiated);
            discoverApduProcessingResult.dD();
            cL().m984a(discoverApduProcessingResult);
            return discoverApduProcessingResult;
        } catch (Exception e) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, tc, unexpected exception: " + e.getMessage());
            e.printStackTrace();
            discoverApduProcessingResult = new DiscoverApduProcessingResult((short) ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
            return discoverApduProcessingResult;
        } finally {
            i = "processApdu, C-APDU GPO, tc, log transaction";
            Log.m287i("DCSDK_DiscoverGpoApduHandler", i);
            cZ();
        }
    }

    private void de() {
    }

    private int df() {
        ByteBuffer afl = cN().getPaymentProfile().getAfl();
        return afl != null ? afl.getSize() / 4 : 0;
    }

    private DiscoverApduProcessingResult m934a(byte b) {
        int i = 7;
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process start, cid = " + b);
        setCid(b);
        dg();
        DiscoverCryptoData dM = cN().dM();
        try {
            Object obj;
            GPORApdu gPORApdu;
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process, compute application cryptogram, cid =" + b);
            cY();
            if (cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 8)) {
                cN().getPth().setBit(1, i);
                cN().m944L(df());
                obj = (byte) 1;
            } else {
                cN().getPth().clearBit(1, i);
                obj = (byte) 0;
            }
            cN().m951b((byte) 0);
            cN().m953c((byte) 1);
            ByteBuffer afl = cN().getPaymentProfile().getAfl();
            if (obj != null) {
                gPORApdu = new GPORApdu(new DiscoverGPOResponse(cN().getPaymentProfile().getAip(), afl, dM.dU(), dM.dT(), cN().dM().getIssuerApplicationData(), new ByteBuffer(new byte[]{dM.getCid()}), cN().dI()));
            } else {
                gPORApdu = new GPORApdu(new DiscoverGPOResponse(cN().getPaymentProfile().getAip(), dM.dU(), dM.dT(), cN().dM().getIssuerApplicationData(), new ByteBuffer(new byte[]{dM.getCid()}), cM().getTrack2EquivalentData(), cN().getPaymentProfile().getApplicationUsageControl(), cM().getCountryCode(), cM().getDiscoverApplicationData().getPanSn(), cN().dI(), cM().getDiscoverApplicationData().getApplicationEffectiveDate(), cM().getDiscoverApplicationData().getApplicationVersionNumber()));
            }
            DiscoverDataTags.m972a(cL(), cM());
            DiscoverApduProcessingResult discoverApduProcessingResult = new DiscoverApduProcessingResult(gPORApdu.dj(), DiscoverApduHandlerState.DiscoverAPDUStateInitiated);
            discoverApduProcessingResult.dD();
            cL().m984a(discoverApduProcessingResult);
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process end, cid = " + b);
            return discoverApduProcessingResult;
        } catch (Exception e) {
            Log.m286e("DCSDK_DiscoverGpoApduHandler", "processApdu, C-APDU GPO, process, unexpected exception: " + e.getMessage());
            e.printStackTrace();
            DiscoverApduProcessingResult discoverApduProcessingResult2 = new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            return discoverApduProcessingResult2;
        } finally {
            i = "processApdu, C-APDU GPO, process, log transaction, cid = ";
            Log.m287i("DCSDK_DiscoverGpoApduHandler", i + b);
            cZ();
        }
    }

    private void setCid(byte b) {
        ByteBuffer dH = cN().dH();
        dH.setBit(5, (b & 2) >> 1);
        dH.setBit(6, b & 1);
        DiscoverCryptoData dM = cN().dM();
        if (dM == null) {
            cN().m948a(new DiscoverCryptoData());
            dM = cN().dM();
        }
        dM.setCid(b);
    }

    private void dg() {
        ByteBuffer cLApplicationConfigurationOptions = cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions();
        ByteBuffer issuerApplicationData = cM().getIssuerApplicationData().getIssuerApplicationData();
        Log.m287i("DCSDK_DiscoverGpoApduHandler", "computeIssuerApplicationData, computeIssuerApplicationData, copy cvr: " + cN().dH().toHexString());
        ByteBuffer dH = cN().dH();
        for (int i = 0; i < 8; i++) {
            issuerApplicationData.setByte(i + 2, dH.getByte(i));
        }
        if (cLApplicationConfigurationOptions.checkBit(2, 6)) {
            Log.m287i("DCSDK_DiscoverGpoApduHandler", "computeIssuerApplicationData, computeIssuerApplicationData, compose IDD");
            DiscoverDataTags.m973a(cM().getIssuerApplicationData().getIADOL(), cN().getPaymentProfile(), cL(), cM().getIssuerApplicationData().getIDDTags(), issuerApplicationData);
        }
        Log.m285d("DCSDK_DiscoverGpoApduHandler", "computeIssuerApplicationData, iad: " + issuerApplicationData.toHexString());
        cN().dM().setIssuerApplicationData(issuerApplicationData);
    }
}
