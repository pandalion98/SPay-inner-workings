package com.samsung.android.spayfw.payprovider.discover.payment.p017a;

import com.mastercard.mobile_api.utils.apdu.emv.GetProcessingOptions;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.TLVData;
import java.text.ParseException;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.a.b */
public class GPOCApdu extends CommandApdu {
    private ByteBuffer tA;
    private ByteBuffer tB;
    private ByteBuffer to;
    private ByteBuffer tp;
    private ByteBuffer tq;
    private ByteBuffer tr;
    private ByteBuffer ts;
    private ByteBuffer tt;
    private ByteBuffer tu;
    private ByteBuffer tv;
    private ByteBuffer tw;
    private ByteBuffer tx;
    private ByteBuffer ty;
    private ByteBuffer tz;

    public GPOCApdu() {
        setCLA(VerifyPINApdu.P2_PLAINTEXT);
        setINS(GetProcessingOptions.INS);
        setP1P2((short) 0);
    }

    public GPOCApdu(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    public void parse() {
        int size = getData().getSize();
        if (size == 0) {
            Log.m286e("DCSDK_", "PDOL is empty...");
            throw new ParseException("PDOL is empty", 0);
        } else if (size < 3) {
            Log.m286e("DCSDK_", "PDOL length is less than 3");
            throw new ParseException("PDOL length is less than 3", 0);
        } else {
            Log.m285d("DCSDK_", "PDOL length = " + size);
            try {
                TLVData c = BERTLV.m1005c(getData().getBytes(), 0, size);
                if (c == null) {
                    return;
                }
                if (size == 3) {
                    Log.m285d("DCSDK_", "Parse UN...");
                    m919k(c.m1006O(131) != null ? (ByteBuffer) c.m1006O(131).get(0) : null);
                    Log.m285d("DCSDK_", "UN " + dt());
                    if (dt() != null) {
                        Log.m285d("DCSDK_", "UN str" + dt().toHexString());
                    }
                } else if (size < 28) {
                    Log.m286e("DCSDK_", "Wrong PDOL lenth, less than mandatory value " + size);
                    throw new ParseException("Wrong PDOL lenth, less than mandatory value " + size, 0);
                } else {
                    ByteBuffer byteBuffer = c.m1006O(131) != null ? (ByteBuffer) c.m1006O(131).get(0) : null;
                    if (byteBuffer == null) {
                        Log.m286e("DCSDK_", "Cannot parse PDOL data");
                        throw new ParseException("Cannot parse PDOL data", 0);
                    }
                    m925q(byteBuffer);
                    m911d(byteBuffer.copyBytes(0, 4));
                    m913e(byteBuffer.copyBytes(4, 10));
                    m914f(byteBuffer.copyBytes(10, 16));
                    m915g(byteBuffer.copyBytes(16, 18));
                    m916h(byteBuffer.copyBytes(18, 20));
                    m917i(byteBuffer.copyBytes(20, 23));
                    m918j(byteBuffer.copyBytes(23, 24));
                    m919k(byteBuffer.copyBytes(24, 28));
                    if (byteBuffer.getSize() > 29) {
                        m920l(byteBuffer.copyBytes(28, 29));
                    }
                    if (byteBuffer.getSize() > 30) {
                        m921m(byteBuffer.copyBytes(29, 30));
                    }
                    if (byteBuffer.getSize() > 32) {
                        m922n(byteBuffer.copyBytes(30, 32));
                    }
                    if (byteBuffer.getSize() > 34) {
                        m923o(byteBuffer.copyBytes(32, 34));
                    }
                    if (byteBuffer.getSize() > 36) {
                        m924p(byteBuffer.copyBytes(34, 36));
                    }
                    m911d(byteBuffer.copyBytes(0, 4));
                    m913e(byteBuffer.copyBytes(4, 10));
                    m914f(byteBuffer.copyBytes(10, 16));
                    m915g(byteBuffer.copyBytes(16, 18));
                    m916h(byteBuffer.copyBytes(18, 20));
                    m917i(byteBuffer.copyBytes(20, 23));
                    m918j(byteBuffer.copyBytes(23, 24));
                    m919k(byteBuffer.copyBytes(24, 28));
                    if (29 < byteBuffer.getSize()) {
                        m920l(byteBuffer.copyBytes(28, 29));
                    }
                    if (30 < byteBuffer.getSize()) {
                        m921m(byteBuffer.copyBytes(29, 30));
                    }
                    if (32 < byteBuffer.getSize()) {
                        m922n(byteBuffer.copyBytes(30, 32));
                    }
                    if (34 < byteBuffer.getSize()) {
                        m923o(byteBuffer.copyBytes(32, 34));
                    }
                    if (36 < byteBuffer.getSize()) {
                        m924p(byteBuffer.copyBytes(34, 36));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ParseException("Failed GPO APDU parsing", 0);
            }
        }
    }

    public void m911d(ByteBuffer byteBuffer) {
        this.to = byteBuffer;
    }

    public ByteBuffer dm() {
        return this.to;
    }

    public void m913e(ByteBuffer byteBuffer) {
        this.tp = byteBuffer;
    }

    public ByteBuffer dn() {
        return this.tp;
    }

    public void m914f(ByteBuffer byteBuffer) {
        this.tq = byteBuffer;
    }

    public ByteBuffer m912do() {
        return this.tq;
    }

    public void m915g(ByteBuffer byteBuffer) {
        this.tr = byteBuffer;
    }

    public ByteBuffer dp() {
        return this.tr;
    }

    public void m916h(ByteBuffer byteBuffer) {
        this.ts = byteBuffer;
    }

    public ByteBuffer dq() {
        return this.ts;
    }

    public void m917i(ByteBuffer byteBuffer) {
        this.tt = byteBuffer;
    }

    public ByteBuffer dr() {
        return this.tt;
    }

    public void m918j(ByteBuffer byteBuffer) {
        this.tu = byteBuffer;
    }

    public ByteBuffer ds() {
        return this.tu;
    }

    public void m919k(ByteBuffer byteBuffer) {
        this.tv = byteBuffer;
    }

    public ByteBuffer dt() {
        return this.tv;
    }

    public void m920l(ByteBuffer byteBuffer) {
        this.tw = byteBuffer;
    }

    public ByteBuffer du() {
        return this.tw;
    }

    public void m921m(ByteBuffer byteBuffer) {
        this.tx = byteBuffer;
    }

    public ByteBuffer dv() {
        return this.tx;
    }

    public void m922n(ByteBuffer byteBuffer) {
        this.ty = byteBuffer;
    }

    public ByteBuffer dw() {
        return this.ty;
    }

    public void m923o(ByteBuffer byteBuffer) {
        this.tz = byteBuffer;
    }

    public ByteBuffer dx() {
        return this.tz;
    }

    public void m924p(ByteBuffer byteBuffer) {
        this.tA = byteBuffer;
    }

    public ByteBuffer dy() {
        return this.tA;
    }

    public ByteBuffer dz() {
        return this.tB;
    }

    public void m925q(ByteBuffer byteBuffer) {
        this.tB = byteBuffer;
    }
}
