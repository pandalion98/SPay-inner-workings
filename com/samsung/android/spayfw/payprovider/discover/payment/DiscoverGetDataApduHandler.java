package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverApduProcessingResult;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverDataTags;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIDDTag;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.CommandApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.p017a.ResponseApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.b */
public class DiscoverGetDataApduHandler extends DiscoverApduHandler {
    private CommandApdu td;

    public DiscoverGetDataApduHandler(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, discoverTransactionContext, discoverPaymentCard);
        this.td = new CommandApdu(byteBuffer);
    }

    public DiscoverApduProcessingResult cK() {
        if (this.td.dk() != X509KeyUsage.digitalSignature) {
            Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, cla is not supported, cla = " + this.td.dk() + ", expected " + X509KeyUsage.digitalSignature);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_CLA_NOT_SUPPORTED);
        } else if ((this.td.getINS() & GF2Field.MASK) != 202) {
            Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, ins is not supported, ins = " + (this.td.getINS() & GF2Field.MASK) + ", expected " + 202);
            return new DiscoverApduProcessingResult((short) ISO7816.SW_INS_NOT_SUPPORTED);
        } else {
            ByteBuffer byteBuffer;
            if (this.td.getP1() == null) {
                byteBuffer = new ByteBuffer(new byte[]{this.td.getP2()});
            } else {
                byteBuffer = new ByteBuffer(new byte[]{this.td.getP1(), this.td.getP2()});
            }
            if (!DiscoverDataTags.m979w(byteBuffer)) {
                Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is not supported, tag = " + byteBuffer.toHexString());
                return new DiscoverApduProcessingResult((short) ISO7816.SW_DATA_NOT_FOUND);
            } else if (this.td.dj().getByte(this.td.dj().getSize() - 1) != null) {
                Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, Lc != 0, Lc value " + this.td.dj().getByte(this.td.dj().getSize() - 1));
                byteBuffer = DiscoverDataTags.m980x(byteBuffer);
                if (byteBuffer == null) {
                    Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag not found, Le = 0.");
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_CORRECT_LENGTH_00);
                }
                short size = (short) byteBuffer.getSize();
                Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, return tag size, Le = " + size);
                return new DiscoverApduProcessingResult((short) (size + 27648));
            } else if (cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 2) && cN().dM() == null) {
                Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, GPO is not executed, required by CL ACO B2b2, 27013");
                return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (DiscoverDataTags.m981y(byteBuffer)) {
                Log.m285d("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, counter/limit, tag = " + byteBuffer.toHexString());
                if (cN().dM() == null) {
                    Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, GPO is not executed, return 27013");
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                } else if (cM().getCaco().checkBit(1, 1)) {
                    Log.m287i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is retrievable, return tag = " + byteBuffer.toHexString());
                    return m931a(byteBuffer);
                } else {
                    Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is not retrievable, tag = " + byteBuffer.toHexString());
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                }
            } else {
                Log.m285d("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, check IDDT tag...");
                if (!DiscoverDataTags.m982z(byteBuffer)) {
                    Log.m287i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, not IDD tag, return tag = " + byteBuffer.toHexString());
                    return m931a(byteBuffer);
                } else if (cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 5)) {
                    Log.m287i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDD tag & IDDT enabled, return tag = " + byteBuffer.toHexString());
                    return m931a(byteBuffer);
                } else {
                    Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDD tag & IDDT disabled, tag = " + byteBuffer.toHexString());
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                }
            }
        }
    }

    private DiscoverApduProcessingResult m931a(ByteBuffer byteBuffer) {
        ByteBuffer byteBuffer2 = null;
        Log.m285d("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag " + (byteBuffer != null ? byteBuffer.toHexString() : null));
        if (byteBuffer == null) {
            Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is null, tag.");
            return new DiscoverApduProcessingResult((short) ISO7816.SW_DATA_NOT_FOUND);
        }
        if (DiscoverDataTags.m967A(byteBuffer)) {
            Log.m287i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, profile tag found." + byteBuffer.toHexString());
            byteBuffer2 = DiscoverDataTags.m969a(byteBuffer, cM());
        } else if (DiscoverDataTags.m968B(byteBuffer)) {
            Log.m287i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDDT tag found." + byteBuffer.toHexString());
            DiscoverIDDTag b = DiscoverDataTags.m974b(byteBuffer, cM());
            if (b != null) {
                if ((b.getAccess() & 64) == 64 && cN().dM() == null) {
                    Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDDT tag requested, GPO access required, but not executed");
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
                }
                byteBuffer2 = b.getData();
            }
        } else {
            byteBuffer2 = DiscoverDataTags.m980x(byteBuffer);
        }
        if (byteBuffer2 == null) {
            Log.m286e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag data is null, tag = " + byteBuffer.toHexString());
            return new DiscoverApduProcessingResult((short) ISO7816.SW_DATA_NOT_FOUND);
        }
        Log.m287i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, return data for tag = " + byteBuffer.toHexString());
        ResponseApdu responseApdu = new ResponseApdu();
        responseApdu.m926r(BERTLV.m1004c(byteBuffer, byteBuffer2));
        return new DiscoverApduProcessingResult(responseApdu.dj());
    }
}
