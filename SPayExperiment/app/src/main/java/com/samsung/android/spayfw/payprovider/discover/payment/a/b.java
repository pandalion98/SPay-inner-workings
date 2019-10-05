/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment.a;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.text.ParseException;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class b
extends a {
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

    public b() {
        this.setCLA((byte)-128);
        this.setINS((byte)-88);
        this.setP1P2((short)0);
    }

    public b(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    public void d(ByteBuffer byteBuffer) {
        this.to = byteBuffer;
    }

    public ByteBuffer dm() {
        return this.to;
    }

    public ByteBuffer dn() {
        return this.tp;
    }

    public ByteBuffer do() {
        return this.tq;
    }

    public ByteBuffer dp() {
        return this.tr;
    }

    public ByteBuffer dq() {
        return this.ts;
    }

    public ByteBuffer dr() {
        return this.tt;
    }

    public ByteBuffer ds() {
        return this.tu;
    }

    public ByteBuffer dt() {
        return this.tv;
    }

    public ByteBuffer du() {
        return this.tw;
    }

    public ByteBuffer dv() {
        return this.tx;
    }

    public ByteBuffer dw() {
        return this.ty;
    }

    public ByteBuffer dx() {
        return this.tz;
    }

    public ByteBuffer dy() {
        return this.tA;
    }

    public ByteBuffer dz() {
        return this.tB;
    }

    public void e(ByteBuffer byteBuffer) {
        this.tp = byteBuffer;
    }

    public void f(ByteBuffer byteBuffer) {
        this.tq = byteBuffer;
    }

    public void g(ByteBuffer byteBuffer) {
        this.tr = byteBuffer;
    }

    public void h(ByteBuffer byteBuffer) {
        this.ts = byteBuffer;
    }

    public void i(ByteBuffer byteBuffer) {
        this.tt = byteBuffer;
    }

    public void j(ByteBuffer byteBuffer) {
        this.tu = byteBuffer;
    }

    public void k(ByteBuffer byteBuffer) {
        this.tv = byteBuffer;
    }

    public void l(ByteBuffer byteBuffer) {
        this.tw = byteBuffer;
    }

    public void m(ByteBuffer byteBuffer) {
        this.tx = byteBuffer;
    }

    public void n(ByteBuffer byteBuffer) {
        this.ty = byteBuffer;
    }

    public void o(ByteBuffer byteBuffer) {
        this.tz = byteBuffer;
    }

    public void p(ByteBuffer byteBuffer) {
        this.tA = byteBuffer;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void parse() {
        com.samsung.android.spayfw.payprovider.discover.payment.utils.b b2;
        int n2 = this.getData().getSize();
        if (n2 == 0) {
            Log.e("DCSDK_", "PDOL is empty...");
            throw new ParseException("PDOL is empty", 0);
        }
        if (n2 < 3) {
            Log.e("DCSDK_", "PDOL length is less than 3");
            throw new ParseException("PDOL length is less than 3", 0);
        }
        Log.d("DCSDK_", "PDOL length = " + n2);
        try {
            b2 = com.samsung.android.spayfw.payprovider.discover.payment.utils.a.c(this.getData().getBytes(), 0, n2);
            if (b2 == null) return;
            if (n2 == 3) {
                Log.d("DCSDK_", "Parse UN...");
                ByteBuffer byteBuffer = b2.O(131) != null ? (ByteBuffer)b2.O(131).get(0) : null;
                this.k(byteBuffer);
                Log.d("DCSDK_", "UN " + this.dt());
                if (this.dt() == null) return;
                {
                    Log.d("DCSDK_", "UN str" + this.dt().toHexString());
                    return;
                }
            }
            if (n2 < 28) {
                Log.e("DCSDK_", "Wrong PDOL lenth, less than mandatory value " + n2);
                throw new ParseException("Wrong PDOL lenth, less than mandatory value " + n2, 0);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new ParseException("Failed GPO APDU parsing", 0);
        }
        ByteBuffer byteBuffer = b2.O(131) != null ? (ByteBuffer)b2.O(131).get(0) : null;
        if (byteBuffer == null) {
            Log.e("DCSDK_", "Cannot parse PDOL data");
            throw new ParseException("Cannot parse PDOL data", 0);
        }
        this.q(byteBuffer);
        this.d(byteBuffer.copyBytes(0, 4));
        this.e(byteBuffer.copyBytes(4, 10));
        this.f(byteBuffer.copyBytes(10, 16));
        this.g(byteBuffer.copyBytes(16, 18));
        this.h(byteBuffer.copyBytes(18, 20));
        this.i(byteBuffer.copyBytes(20, 23));
        this.j(byteBuffer.copyBytes(23, 24));
        this.k(byteBuffer.copyBytes(24, 28));
        if (byteBuffer.getSize() > 29) {
            this.l(byteBuffer.copyBytes(28, 29));
        }
        if (byteBuffer.getSize() > 30) {
            this.m(byteBuffer.copyBytes(29, 30));
        }
        if (byteBuffer.getSize() > 32) {
            this.n(byteBuffer.copyBytes(30, 32));
        }
        if (byteBuffer.getSize() > 34) {
            this.o(byteBuffer.copyBytes(32, 34));
        }
        if (byteBuffer.getSize() > 36) {
            this.p(byteBuffer.copyBytes(34, 36));
        }
        this.d(byteBuffer.copyBytes(0, 4));
        this.e(byteBuffer.copyBytes(4, 10));
        this.f(byteBuffer.copyBytes(10, 16));
        this.g(byteBuffer.copyBytes(16, 18));
        this.h(byteBuffer.copyBytes(18, 20));
        this.i(byteBuffer.copyBytes(20, 23));
        this.j(byteBuffer.copyBytes(23, 24));
        this.k(byteBuffer.copyBytes(24, 28));
        if (29 < byteBuffer.getSize()) {
            this.l(byteBuffer.copyBytes(28, 29));
        }
        if (30 < byteBuffer.getSize()) {
            this.m(byteBuffer.copyBytes(29, 30));
        }
        if (32 < byteBuffer.getSize()) {
            this.n(byteBuffer.copyBytes(30, 32));
        }
        if (34 < byteBuffer.getSize()) {
            this.o(byteBuffer.copyBytes(32, 34));
        }
        if (36 >= byteBuffer.getSize()) return;
        {
            this.p(byteBuffer.copyBytes(34, 36));
        }
    }

    public void q(ByteBuffer byteBuffer) {
        this.tB = byteBuffer;
    }
}

