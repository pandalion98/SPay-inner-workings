/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.ByteQueue;
import org.bouncycastle.crypto.tls.DTLSEpoch;
import org.bouncycastle.crypto.tls.DTLSHandshakeRetransmit;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsNullCipher;
import org.bouncycastle.crypto.tls.TlsPeer;
import org.bouncycastle.crypto.tls.TlsUtils;

class DTLSRecordLayer
implements DatagramTransport {
    private static final int MAX_FRAGMENT_LENGTH = 16384;
    private static final int RECORD_HEADER_LENGTH = 13;
    private static final long RETRANSMIT_TIMEOUT = 240000L;
    private static final long TCP_MSL = 120000L;
    private volatile boolean closed = false;
    private final TlsContext context;
    private DTLSEpoch currentEpoch;
    private volatile ProtocolVersion discoveredPeerVersion = null;
    private volatile boolean failed = false;
    private volatile boolean inHandshake;
    private final TlsPeer peer;
    private DTLSEpoch pendingEpoch;
    private volatile int plaintextLimit;
    private DTLSEpoch readEpoch;
    private final ByteQueue recordQueue = new ByteQueue();
    private DTLSHandshakeRetransmit retransmit = null;
    private DTLSEpoch retransmitEpoch = null;
    private long retransmitExpiry = 0L;
    private final DatagramTransport transport;
    private DTLSEpoch writeEpoch;

    DTLSRecordLayer(DatagramTransport datagramTransport, TlsContext tlsContext, TlsPeer tlsPeer, short s2) {
        this.transport = datagramTransport;
        this.context = tlsContext;
        this.peer = tlsPeer;
        this.inHandshake = true;
        this.currentEpoch = new DTLSEpoch(0, new TlsNullCipher(tlsContext));
        this.pendingEpoch = null;
        this.readEpoch = this.currentEpoch;
        this.writeEpoch = this.currentEpoch;
        this.setPlaintextLimit(16384);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void closeTransport() {
        if (!this.closed) {
            try {
                if (!this.failed) {
                    this.warn((short)0, null);
                }
                this.transport.close();
            }
            catch (Exception exception) {}
            this.closed = true;
        }
    }

    private static long getMacSequenceNumber(int n2, long l2) {
        return l2 | (0xFFFFFFFFL & (long)n2) << 48;
    }

    private void raiseAlert(short s2, short s3, String string, Throwable throwable) {
        this.peer.notifyAlertRaised(s2, s3, string, throwable);
        byte[] arrby = new byte[]{(byte)s2, (byte)s3};
        this.sendRecord((short)21, arrby, 0, 2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int receiveRecord(byte[] arrby, int n2, int n3, int n4) {
        int n5;
        if (this.recordQueue.available() > 0) {
            int n6;
            if (this.recordQueue.available() >= 13) {
                byte[] arrby2 = new byte[2];
                this.recordQueue.read(arrby2, 0, 2, 11);
                n6 = TlsUtils.readUint16(arrby2, 0);
            } else {
                n6 = 0;
            }
            int n7 = Math.min((int)this.recordQueue.available(), (int)(n6 + 13));
            this.recordQueue.removeData(arrby, n2, n7, 0);
            return n7;
        }
        int n8 = this.transport.receive(arrby, n2, n3, n4);
        if (n8 >= 13 && n8 > (n5 = 13 + TlsUtils.readUint16(arrby, n2 + 11))) {
            this.recordQueue.addData(arrby, n2 + n5, n8 - n5);
            return n5;
        }
        return n8;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void sendRecord(short s2, byte[] arrby, int n2, int n3) {
        if (n3 > this.plaintextLimit) {
            throw new TlsFatalAlert(80);
        }
        if (n3 < 1 && s2 != 23) {
            throw new TlsFatalAlert(80);
        }
        int n4 = this.writeEpoch.getEpoch();
        long l2 = this.writeEpoch.allocateSequenceNumber();
        byte[] arrby2 = this.writeEpoch.getCipher().encodePlaintext(DTLSRecordLayer.getMacSequenceNumber(n4, l2), s2, arrby, n2, n3);
        byte[] arrby3 = new byte[13 + arrby2.length];
        TlsUtils.writeUint8(s2, arrby3, 0);
        ProtocolVersion protocolVersion = this.discoveredPeerVersion != null ? this.discoveredPeerVersion : this.context.getClientVersion();
        TlsUtils.writeVersion(protocolVersion, arrby3, 1);
        TlsUtils.writeUint16(n4, arrby3, 3);
        TlsUtils.writeUint48(l2, arrby3, 5);
        TlsUtils.writeUint16(arrby2.length, arrby3, 11);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)13, (int)arrby2.length);
        this.transport.send(arrby3, 0, arrby3.length);
    }

    @Override
    public void close() {
        if (!this.closed) {
            if (this.inHandshake) {
                this.warn((short)90, "User canceled handshake");
            }
            this.closeTransport();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void fail(short s2) {
        if (!this.closed) {
            try {
                this.raiseAlert((short)2, s2, null, null);
            }
            catch (Exception exception) {}
            this.failed = true;
            this.closeTransport();
        }
    }

    ProtocolVersion getDiscoveredPeerVersion() {
        return this.discoveredPeerVersion;
    }

    @Override
    public int getReceiveLimit() {
        return Math.min((int)this.plaintextLimit, (int)this.readEpoch.getCipher().getPlaintextLimit(-13 + this.transport.getReceiveLimit()));
    }

    @Override
    public int getSendLimit() {
        return Math.min((int)this.plaintextLimit, (int)this.writeEpoch.getCipher().getPlaintextLimit(-13 + this.transport.getSendLimit()));
    }

    void handshakeSuccessful(DTLSHandshakeRetransmit dTLSHandshakeRetransmit) {
        if (this.readEpoch == this.currentEpoch || this.writeEpoch == this.currentEpoch) {
            throw new IllegalStateException();
        }
        if (dTLSHandshakeRetransmit != null) {
            this.retransmit = dTLSHandshakeRetransmit;
            this.retransmitEpoch = this.currentEpoch;
            this.retransmitExpiry = 240000L + System.currentTimeMillis();
        }
        this.inHandshake = false;
        this.currentEpoch = this.pendingEpoch;
        this.pendingEpoch = null;
    }

    void initPendingEpoch(TlsCipher tlsCipher) {
        if (this.pendingEpoch != null) {
            throw new IllegalStateException();
        }
        this.pendingEpoch = new DTLSEpoch(1 + this.writeEpoch.getEpoch(), tlsCipher);
    }

    /*
     * Exception decompiling
     */
    @Override
    public int receive(byte[] var1_1, int var2_2, int var3_3, int var4_4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
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

    ProtocolVersion resetDiscoveredPeerVersion() {
        ProtocolVersion protocolVersion = this.discoveredPeerVersion;
        this.discoveredPeerVersion = null;
        return protocolVersion;
    }

    void resetWriteEpoch() {
        if (this.retransmitEpoch != null) {
            this.writeEpoch = this.retransmitEpoch;
            return;
        }
        this.writeEpoch = this.currentEpoch;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void send(byte[] arrby, int n2, int n3) {
        short s2 = 23;
        if (this.inHandshake || this.writeEpoch == this.retransmitEpoch) {
            s2 = 22;
            if (TlsUtils.readUint8(arrby, n2) == 20) {
                DTLSEpoch dTLSEpoch;
                if (this.inHandshake) {
                    dTLSEpoch = this.pendingEpoch;
                } else {
                    DTLSEpoch dTLSEpoch2 = this.writeEpoch;
                    DTLSEpoch dTLSEpoch3 = this.retransmitEpoch;
                    dTLSEpoch = null;
                    if (dTLSEpoch2 == dTLSEpoch3) {
                        dTLSEpoch = this.currentEpoch;
                    }
                }
                if (dTLSEpoch == null) {
                    throw new IllegalStateException();
                }
                byte[] arrby2 = new byte[]{1};
                this.sendRecord((short)20, arrby2, 0, arrby2.length);
                this.writeEpoch = dTLSEpoch;
            }
        }
        this.sendRecord(s2, arrby, n2, n3);
    }

    void setPlaintextLimit(int n2) {
        this.plaintextLimit = n2;
    }

    void warn(short s2, String string) {
        this.raiseAlert((short)1, s2, string, null);
    }
}

