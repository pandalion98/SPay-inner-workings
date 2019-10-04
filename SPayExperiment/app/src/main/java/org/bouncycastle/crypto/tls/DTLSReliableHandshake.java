/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.OutputStream
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.DTLSHandshakeRetransmit;
import org.bouncycastle.crypto.tls.DTLSReassembler;
import org.bouncycastle.crypto.tls.DTLSRecordLayer;
import org.bouncycastle.crypto.tls.DeferredHash;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Integers;

class DTLSReliableHandshake {
    private static final int MAX_RECEIVE_AHEAD = 10;
    private Hashtable currentInboundFlight = new Hashtable();
    private TlsHandshakeHash handshakeHash;
    private int message_seq = 0;
    private int next_receive_seq = 0;
    private Vector outboundFlight = new Vector();
    private Hashtable previousInboundFlight = null;
    private final DTLSRecordLayer recordLayer;
    private boolean sending = true;

    DTLSReliableHandshake(TlsContext tlsContext, DTLSRecordLayer dTLSRecordLayer) {
        this.recordLayer = dTLSRecordLayer;
        this.handshakeHash = new DeferredHash();
        this.handshakeHash.init(tlsContext);
    }

    private static boolean checkAll(Hashtable hashtable) {
        Enumeration enumeration = hashtable.elements();
        while (enumeration.hasMoreElements()) {
            if (((DTLSReassembler)enumeration.nextElement()).getBodyIfComplete() != null) continue;
            return false;
        }
        return true;
    }

    private void checkInboundFlight() {
        Enumeration enumeration = this.currentInboundFlight.keys();
        while (enumeration.hasMoreElements()) {
            if ((Integer)enumeration.nextElement() < this.next_receive_seq) continue;
        }
    }

    private void prepareInboundFlight() {
        DTLSReliableHandshake.resetAll(this.currentInboundFlight);
        this.previousInboundFlight = this.currentInboundFlight;
        this.currentInboundFlight = new Hashtable();
    }

    private void resendOutboundFlight() {
        this.recordLayer.resetWriteEpoch();
        for (int i2 = 0; i2 < this.outboundFlight.size(); ++i2) {
            this.writeMessage((Message)this.outboundFlight.elementAt(i2));
        }
    }

    private static void resetAll(Hashtable hashtable) {
        Enumeration enumeration = hashtable.elements();
        while (enumeration.hasMoreElements()) {
            ((DTLSReassembler)enumeration.nextElement()).reset();
        }
    }

    private Message updateHandshakeMessagesDigest(Message message) {
        if (message.getType() != 0) {
            byte[] arrby = message.getBody();
            byte[] arrby2 = new byte[12];
            TlsUtils.writeUint8(message.getType(), arrby2, 0);
            TlsUtils.writeUint24(arrby.length, arrby2, 1);
            TlsUtils.writeUint16(message.getSeq(), arrby2, 4);
            TlsUtils.writeUint24(0, arrby2, 6);
            TlsUtils.writeUint24(arrby.length, arrby2, 9);
            this.handshakeHash.update(arrby2, 0, arrby2.length);
            this.handshakeHash.update(arrby, 0, arrby.length);
        }
        return message;
    }

    private void writeHandshakeFragment(Message message, int n2, int n3) {
        RecordLayerBuffer recordLayerBuffer = new RecordLayerBuffer(n3 + 12);
        TlsUtils.writeUint8(message.getType(), (OutputStream)recordLayerBuffer);
        TlsUtils.writeUint24(message.getBody().length, (OutputStream)recordLayerBuffer);
        TlsUtils.writeUint16(message.getSeq(), (OutputStream)recordLayerBuffer);
        TlsUtils.writeUint24(n2, (OutputStream)recordLayerBuffer);
        TlsUtils.writeUint24(n3, (OutputStream)recordLayerBuffer);
        recordLayerBuffer.write(message.getBody(), n2, n3);
        recordLayerBuffer.sendToRecordLayer(this.recordLayer);
    }

    private void writeMessage(Message message) {
        int n2;
        int n3 = -12 + this.recordLayer.getSendLimit();
        if (n3 < 1) {
            throw new TlsFatalAlert(80);
        }
        int n4 = message.getBody().length;
        int n5 = 0;
        do {
            n2 = Math.min((int)(n4 - n5), (int)n3);
            this.writeHandshakeFragment(message, n5, n2);
        } while ((n5 += n2) < n4);
    }

    /*
     * Enabled aggressive block sorting
     */
    void finish() {
        DTLSHandshakeRetransmit dTLSHandshakeRetransmit = null;
        if (!this.sending) {
            this.checkInboundFlight();
        } else {
            Hashtable hashtable = this.currentInboundFlight;
            dTLSHandshakeRetransmit = null;
            if (hashtable != null) {
                dTLSHandshakeRetransmit = new DTLSHandshakeRetransmit(){

                    /*
                     * Enabled aggressive block sorting
                     */
                    @Override
                    public void receivedHandshakeRecord(int n2, byte[] arrby, int n3, int n4) {
                        block3 : {
                            block2 : {
                                DTLSReassembler dTLSReassembler;
                                int n5;
                                int n6;
                                short s2;
                                int n7;
                                if (n4 < 12 || n4 != (n5 = TlsUtils.readUint24(arrby, n3 + 9)) + 12 || (n7 = TlsUtils.readUint16(arrby, n3 + 4)) >= DTLSReliableHandshake.this.next_receive_seq || n2 != (n6 = (s2 = TlsUtils.readUint8(arrby, n3)) == 20 ? 1 : 0)) break block2;
                                int n8 = TlsUtils.readUint24(arrby, n3 + 1);
                                int n9 = TlsUtils.readUint24(arrby, n3 + 6);
                                if (n9 + n5 > n8 || (dTLSReassembler = (DTLSReassembler)DTLSReliableHandshake.this.currentInboundFlight.get((Object)Integers.valueOf((int)n7))) == null) break block2;
                                dTLSReassembler.contributeFragment(s2, n8, arrby, n3 + 12, n9, n5);
                                if (DTLSReliableHandshake.checkAll(DTLSReliableHandshake.this.currentInboundFlight)) break block3;
                            }
                            return;
                        }
                        DTLSReliableHandshake.this.resendOutboundFlight();
                        DTLSReliableHandshake.resetAll(DTLSReliableHandshake.this.currentInboundFlight);
                    }
                };
            }
        }
        this.recordLayer.handshakeSuccessful(dTLSHandshakeRetransmit);
    }

    TlsHandshakeHash getHandshakeHash() {
        return this.handshakeHash;
    }

    void notifyHelloComplete() {
        this.handshakeHash = this.handshakeHash.notifyPRFDetermined();
    }

    TlsHandshakeHash prepareToFinish() {
        TlsHandshakeHash tlsHandshakeHash = this.handshakeHash;
        this.handshakeHash = this.handshakeHash.stopTracking();
        return tlsHandshakeHash;
    }

    /*
     * Exception decompiling
     */
    Message receiveMessage() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 8[WHILELOOP]
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

    byte[] receiveMessageBody(short s2) {
        Message message = this.receiveMessage();
        if (message.getType() != s2) {
            throw new TlsFatalAlert(10);
        }
        return message.getBody();
    }

    void resetHandshakeMessagesDigest() {
        this.handshakeHash.reset();
    }

    void sendMessage(short s2, byte[] arrby) {
        TlsUtils.checkUint24(arrby.length);
        if (!this.sending) {
            this.checkInboundFlight();
            this.sending = true;
            this.outboundFlight.removeAllElements();
        }
        int n2 = this.message_seq;
        this.message_seq = n2 + 1;
        Message message = new Message(n2, s2, arrby);
        this.outboundFlight.addElement((Object)message);
        this.writeMessage(message);
        this.updateHandshakeMessagesDigest(message);
    }

    static class Message {
        private final byte[] body;
        private final int message_seq;
        private final short msg_type;

        private Message(int n2, short s2, byte[] arrby) {
            this.message_seq = n2;
            this.msg_type = s2;
            this.body = arrby;
        }

        public byte[] getBody() {
            return this.body;
        }

        public int getSeq() {
            return this.message_seq;
        }

        public short getType() {
            return this.msg_type;
        }
    }

    static class RecordLayerBuffer
    extends ByteArrayOutputStream {
        RecordLayerBuffer(int n2) {
            super(n2);
        }

        void sendToRecordLayer(DTLSRecordLayer dTLSRecordLayer) {
            dTLSRecordLayer.send(this.buf, 0, this.count);
            this.buf = null;
        }
    }

}

