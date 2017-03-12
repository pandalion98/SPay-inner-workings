package com.android.internal.midi;

import android.media.midi.MidiReceiver;
import android.media.midi.MidiSender;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MidiDispatcher extends MidiReceiver {
    private final CopyOnWriteArrayList<MidiReceiver> mReceivers = new CopyOnWriteArrayList();
    private final MidiSender mSender = new MidiSender() {
        public void onConnect(MidiReceiver receiver) {
            MidiDispatcher.this.mReceivers.add(receiver);
        }

        public void onDisconnect(MidiReceiver receiver) {
            MidiDispatcher.this.mReceivers.remove(receiver);
        }
    };

    public int getReceiverCount() {
        return this.mReceivers.size();
    }

    public MidiSender getSender() {
        return this.mSender;
    }

    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        Iterator i$ = this.mReceivers.iterator();
        while (i$.hasNext()) {
            MidiReceiver receiver = (MidiReceiver) i$.next();
            try {
                receiver.send(msg, offset, count, timestamp);
            } catch (IOException e) {
                this.mReceivers.remove(receiver);
            }
        }
    }

    public void onFlush() throws IOException {
        Iterator i$ = this.mReceivers.iterator();
        while (i$.hasNext()) {
            ((MidiReceiver) i$.next()).flush();
        }
    }
}
