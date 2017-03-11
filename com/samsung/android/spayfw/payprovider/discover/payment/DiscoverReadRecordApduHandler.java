package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverApduProcessingResult;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext.DiscoverClTransactionType;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.ReadRecordCApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.ResponseApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.List;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.e */
public class DiscoverReadRecordApduHandler extends DiscoverApduHandler {
    private ReadRecordCApdu th;

    public DiscoverReadRecordApduHandler(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, discoverTransactionContext, discoverPaymentCard);
        this.th = new ReadRecordCApdu(byteBuffer);
    }

    public DiscoverApduProcessingResult cK() {
        if (this.th.dk() != 0) {
            Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, cla is not supported, cla = " + this.th.dk() + ", expected " + 0);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_CLA_NOT_SUPPORTED);
        } else if ((this.th.getINS() & GF2Field.MASK) != CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256) {
            Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, ins is not supported, ins = " + (this.th.getINS() & GF2Field.MASK) + ", expected " + CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INS_NOT_SUPPORTED);
        } else if (this.th.getP1() == null || (this.th.getP2() & 7) != 4) {
            Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, wrong p1 and/or p2, p1 = " + this.th.getP1() + ", p2 = " + this.th.getP2());
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INCORRECT_P1P2);
        } else if (this.th.dl() != null) {
            Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, wrong Le = " + this.th.dl() + ", expected Le  = " + 0);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_LENGTH);
        } else {
            DiscoverRecord dL;
            int i;
            byte sfiNumber = this.th.getSfiNumber();
            byte recordNumber = this.th.getRecordNumber();
            Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "Requested SFI: " + (sfiNumber & GF2Field.MASK));
            Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "Requested record: " + (recordNumber & GF2Field.MASK));
            if (sfiNumber == (byte) 1 && recordNumber == (byte) 1) {
                Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "Read record processApdu: zip record requested...");
                if (cN().dL() != null) {
                    dL = cN().dL();
                    Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "Read record processApdu: zip record found.");
                    i = 1;
                } else {
                    dL = null;
                    i = 1;
                }
            } else {
                Log.m285d("DCSDK_DiscoverReadRecordApduHandler", "Read record processApdu: EMV transaction");
                if (sfiNumber < (byte) 1 || sfiNumber > (byte) 10) {
                    Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, record not supported, SFI <  EMV_MIN or SFI > EMV_MAX.");
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
                } else if (sfiNumber > (byte) 1) {
                    Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, SFI is not supported.");
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
                } else {
                    List<DiscoverRecord> records = cM().getRecords();
                    if (records == null) {
                        Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, cannot find records in the profile.");
                        return new DiscoverApduProcessingResult((short) ISO7816.SW_RECORD_NOT_FOUND);
                    }
                    Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, requested record, sfi = " + sfiNumber + ", record number = " + recordNumber);
                    i = 0;
                    for (DiscoverRecord dL2 : records) {
                        int i2;
                        if (dL2.getSFI().getByte(0) != sfiNumber) {
                            i2 = i;
                        } else if (dL2.getRecordNumber().getByte(0) == recordNumber) {
                            i = 1;
                            break;
                        } else {
                            i2 = 1;
                        }
                        i = i2;
                    }
                    dL2 = null;
                }
            }
            if (i == 0) {
                Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, sfi not found, sfi = " + sfiNumber);
                return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
            } else if (dL2 == null) {
                Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, record not found, sfi = " + sfiNumber + ", record = " + recordNumber);
                return new DiscoverApduProcessingResult((short) ISO7816.SW_RECORD_NOT_FOUND);
            } else if (sfiNumber < (byte) 1 || sfiNumber > (byte) 10 || m988a(dL2)) {
                i = cL().ed().dJ();
                int i3 = i - 1;
                cL().ed().m944L(i);
                if (cL().ed().dJ() == 0) {
                    if ((cL().ed().dH().getByte(1) & 48) == 1) {
                        Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, indicate CDA successful.");
                        cN().getPth().clearBit(1, 8);
                    }
                    Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, indicate transaction completed.");
                    cN().getPth().clearBit(1, 7);
                    if (cN().getPaymentProfile().getCpr().checkBit(1, 6)) {
                        Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, loyality program indicated, reset PID counter.");
                        cN().m945M(0);
                    }
                }
                ResponseApdu responseApdu = new ResponseApdu();
                responseApdu.m926r(dL2.getRecordData());
                DiscoverApduProcessingResult discoverApduProcessingResult = new DiscoverApduProcessingResult(responseApdu.dj());
                discoverApduProcessingResult.dD();
                cL().m984a(discoverApduProcessingResult);
                return discoverApduProcessingResult;
            } else {
                Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, record not found in AFL, sfi = " + sfiNumber + ", record = " + recordNumber);
                return new DiscoverApduProcessingResult((short) ISO7816.SW_FILE_NOT_FOUND);
            }
        }
    }

    private boolean m988a(DiscoverRecord discoverRecord) {
        ByteBuffer zipAfl;
        if (DiscoverClTransactionType.DISCOVER_CL_ZIP.equals(cN().dK())) {
            Log.m287i("DCSDK_DiscoverReadRecordApduHandler", "checkRecordInAFL: initialize ZIP afl...");
            zipAfl = cM().getZipAfl();
        } else {
            zipAfl = cN().getPaymentProfile().getAfl();
        }
        if (zipAfl == null) {
            Log.m286e("DCSDK_DiscoverReadRecordApduHandler", "checkRecordInAFL: record not found in afl, afl is null.");
            return false;
        }
        boolean z = false;
        for (int i = 1; zipAfl.getSize() >= i * 4; i++) {
            ByteBuffer copyBytes = zipAfl.copyBytes((i * 4) - 4, i * 4);
            if ((copyBytes.getByte(0) >> 3) == discoverRecord.getSFI().getByte(0) && discoverRecord.getRecordNumber().getByte(0) >= copyBytes.getByte(1) && discoverRecord.getRecordNumber().getByte(0) <= copyBytes.getByte(2)) {
                z = true;
            }
        }
        return z;
    }
}
