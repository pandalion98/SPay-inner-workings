package org.bouncycastle.crypto.tls;

import com.google.android.gms.location.LocationStatusCodes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.util.Integers;

class DTLSReliableHandshake {
    private static final int MAX_RECEIVE_AHEAD = 10;
    private Hashtable currentInboundFlight;
    private TlsHandshakeHash handshakeHash;
    private int message_seq;
    private int next_receive_seq;
    private Vector outboundFlight;
    private Hashtable previousInboundFlight;
    private final DTLSRecordLayer recordLayer;
    private boolean sending;

    /* renamed from: org.bouncycastle.crypto.tls.DTLSReliableHandshake.1 */
    class C07491 implements DTLSHandshakeRetransmit {
        C07491() {
        }

        public void receivedHandshakeRecord(int i, byte[] bArr, int i2, int i3) {
            if (i3 >= 12) {
                int readUint24 = TlsUtils.readUint24(bArr, i2 + 9);
                if (i3 == readUint24 + 12) {
                    int readUint16 = TlsUtils.readUint16(bArr, i2 + 4);
                    if (readUint16 < DTLSReliableHandshake.this.next_receive_seq) {
                        short readUint8 = TlsUtils.readUint8(bArr, i2);
                        if (i == (readUint8 == (short) 20 ? 1 : 0)) {
                            int readUint242 = TlsUtils.readUint24(bArr, i2 + 1);
                            int readUint243 = TlsUtils.readUint24(bArr, i2 + 6);
                            if (readUint243 + readUint24 <= readUint242) {
                                DTLSReassembler dTLSReassembler = (DTLSReassembler) DTLSReliableHandshake.this.currentInboundFlight.get(Integers.valueOf(readUint16));
                                if (dTLSReassembler != null) {
                                    dTLSReassembler.contributeFragment(readUint8, readUint242, bArr, i2 + 12, readUint243, readUint24);
                                    if (DTLSReliableHandshake.checkAll(DTLSReliableHandshake.this.currentInboundFlight)) {
                                        DTLSReliableHandshake.this.resendOutboundFlight();
                                        DTLSReliableHandshake.resetAll(DTLSReliableHandshake.this.currentInboundFlight);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static class Message {
        private final byte[] body;
        private final int message_seq;
        private final short msg_type;

        private Message(int i, short s, byte[] bArr) {
            this.message_seq = i;
            this.msg_type = s;
            this.body = bArr;
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

    static class RecordLayerBuffer extends ByteArrayOutputStream {
        RecordLayerBuffer(int i) {
            super(i);
        }

        void sendToRecordLayer(DTLSRecordLayer dTLSRecordLayer) {
            dTLSRecordLayer.send(this.buf, 0, this.count);
            this.buf = null;
        }
    }

    DTLSReliableHandshake(TlsContext tlsContext, DTLSRecordLayer dTLSRecordLayer) {
        this.currentInboundFlight = new Hashtable();
        this.previousInboundFlight = null;
        this.outboundFlight = new Vector();
        this.sending = true;
        this.message_seq = 0;
        this.next_receive_seq = 0;
        this.recordLayer = dTLSRecordLayer;
        this.handshakeHash = new DeferredHash();
        this.handshakeHash.init(tlsContext);
    }

    private static boolean checkAll(Hashtable hashtable) {
        Enumeration elements = hashtable.elements();
        while (elements.hasMoreElements()) {
            if (((DTLSReassembler) elements.nextElement()).getBodyIfComplete() == null) {
                return false;
            }
        }
        return true;
    }

    private void checkInboundFlight() {
        Enumeration keys = this.currentInboundFlight.keys();
        while (keys.hasMoreElements()) {
            if (((Integer) keys.nextElement()).intValue() >= this.next_receive_seq) {
            }
        }
    }

    private void prepareInboundFlight() {
        resetAll(this.currentInboundFlight);
        this.previousInboundFlight = this.currentInboundFlight;
        this.currentInboundFlight = new Hashtable();
    }

    private void resendOutboundFlight() {
        this.recordLayer.resetWriteEpoch();
        for (int i = 0; i < this.outboundFlight.size(); i++) {
            writeMessage((Message) this.outboundFlight.elementAt(i));
        }
    }

    private static void resetAll(Hashtable hashtable) {
        Enumeration elements = hashtable.elements();
        while (elements.hasMoreElements()) {
            ((DTLSReassembler) elements.nextElement()).reset();
        }
    }

    private Message updateHandshakeMessagesDigest(Message message) {
        if (message.getType() != (short) 0) {
            byte[] body = message.getBody();
            byte[] bArr = new byte[12];
            TlsUtils.writeUint8(message.getType(), bArr, 0);
            TlsUtils.writeUint24(body.length, bArr, 1);
            TlsUtils.writeUint16(message.getSeq(), bArr, 4);
            TlsUtils.writeUint24(0, bArr, 6);
            TlsUtils.writeUint24(body.length, bArr, 9);
            this.handshakeHash.update(bArr, 0, bArr.length);
            this.handshakeHash.update(body, 0, body.length);
        }
        return message;
    }

    private void writeHandshakeFragment(Message message, int i, int i2) {
        OutputStream recordLayerBuffer = new RecordLayerBuffer(i2 + 12);
        TlsUtils.writeUint8(message.getType(), recordLayerBuffer);
        TlsUtils.writeUint24(message.getBody().length, recordLayerBuffer);
        TlsUtils.writeUint16(message.getSeq(), recordLayerBuffer);
        TlsUtils.writeUint24(i, recordLayerBuffer);
        TlsUtils.writeUint24(i2, recordLayerBuffer);
        recordLayerBuffer.write(message.getBody(), i, i2);
        recordLayerBuffer.sendToRecordLayer(this.recordLayer);
    }

    private void writeMessage(Message message) {
        int sendLimit = this.recordLayer.getSendLimit() - 12;
        if (sendLimit < 1) {
            throw new TlsFatalAlert((short) 80);
        }
        int length = message.getBody().length;
        int i = 0;
        do {
            int min = Math.min(length - i, sendLimit);
            writeHandshakeFragment(message, i, min);
            i += min;
        } while (i < length);
    }

    void finish() {
        DTLSHandshakeRetransmit dTLSHandshakeRetransmit = null;
        if (!this.sending) {
            checkInboundFlight();
        } else if (this.currentInboundFlight != null) {
            dTLSHandshakeRetransmit = new C07491();
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

    Message receiveMessage() {
        byte[] bodyIfComplete;
        Object obj = null;
        if (this.sending) {
            this.sending = false;
            prepareInboundFlight();
        }
        DTLSReassembler dTLSReassembler = (DTLSReassembler) this.currentInboundFlight.get(Integers.valueOf(this.next_receive_seq));
        if (dTLSReassembler != null) {
            bodyIfComplete = dTLSReassembler.getBodyIfComplete();
            if (bodyIfComplete != null) {
                this.previousInboundFlight = null;
                int i = this.next_receive_seq;
                this.next_receive_seq = i + 1;
                return updateHandshakeMessagesDigest(new Message(dTLSReassembler.getMsgType(), bodyIfComplete, null));
            }
        }
        int i2 = LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
        while (true) {
            int i3;
            int receiveLimit = this.recordLayer.getReceiveLimit();
            if (obj == null || obj.length < receiveLimit) {
                obj = new byte[receiveLimit];
                i3 = i2;
            } else {
                i3 = i2;
            }
            while (true) {
                i2 = this.recordLayer.receive(obj, 0, receiveLimit, i3);
                if (i2 < 0) {
                    break;
                    continue;
                } else if (i2 >= 12) {
                    try {
                        int readUint24 = TlsUtils.readUint24(obj, 9);
                        if (i2 == readUint24 + 12) {
                            int readUint16 = TlsUtils.readUint16(obj, 4);
                            if (readUint16 <= this.next_receive_seq + MAX_RECEIVE_AHEAD) {
                                short readUint8 = TlsUtils.readUint8(obj, 0);
                                int readUint242 = TlsUtils.readUint24(obj, 1);
                                int readUint243 = TlsUtils.readUint24(obj, 6);
                                if (readUint243 + readUint24 > readUint242) {
                                    continue;
                                } else if (readUint16 >= this.next_receive_seq) {
                                    dTLSReassembler = (DTLSReassembler) this.currentInboundFlight.get(Integers.valueOf(readUint16));
                                    if (dTLSReassembler == null) {
                                        dTLSReassembler = new DTLSReassembler(readUint8, readUint242);
                                        this.currentInboundFlight.put(Integers.valueOf(readUint16), dTLSReassembler);
                                    }
                                    dTLSReassembler.contributeFragment(readUint8, readUint242, obj, 12, readUint243, readUint24);
                                    if (readUint16 == this.next_receive_seq) {
                                        bodyIfComplete = dTLSReassembler.getBodyIfComplete();
                                        if (bodyIfComplete != null) {
                                            this.previousInboundFlight = null;
                                            i = this.next_receive_seq;
                                            this.next_receive_seq = i + 1;
                                            return updateHandshakeMessagesDigest(new Message(dTLSReassembler.getMsgType(), bodyIfComplete, null));
                                        }
                                    } else {
                                        continue;
                                    }
                                } else if (this.previousInboundFlight != null) {
                                    dTLSReassembler = (DTLSReassembler) this.previousInboundFlight.get(Integers.valueOf(readUint16));
                                    if (dTLSReassembler != null) {
                                        dTLSReassembler.contributeFragment(readUint8, readUint242, obj, 12, readUint243, readUint24);
                                        if (checkAll(this.previousInboundFlight)) {
                                            resendOutboundFlight();
                                            i2 = Math.min(i3 * 2, 60000);
                                            try {
                                                resetAll(this.previousInboundFlight);
                                                i3 = i2;
                                            } catch (IOException e) {
                                                i3 = i2;
                                            }
                                        }
                                    }
                                    i2 = i3;
                                    i3 = i2;
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    } catch (IOException e2) {
                    }
                }
            }
            resendOutboundFlight();
            i2 = Math.min(i3 * 2, 60000);
        }
    }

    byte[] receiveMessageBody(short s) {
        Message receiveMessage = receiveMessage();
        if (receiveMessage.getType() == s) {
            return receiveMessage.getBody();
        }
        throw new TlsFatalAlert((short) 10);
    }

    void resetHandshakeMessagesDigest() {
        this.handshakeHash.reset();
    }

    void sendMessage(short s, byte[] bArr) {
        TlsUtils.checkUint24(bArr.length);
        if (!this.sending) {
            checkInboundFlight();
            this.sending = true;
            this.outboundFlight.removeAllElements();
        }
        int i = this.message_seq;
        this.message_seq = i + 1;
        Message message = new Message(s, bArr, null);
        this.outboundFlight.addElement(message);
        writeMessage(message);
        updateHandshakeMessagesDigest(message);
    }
}
