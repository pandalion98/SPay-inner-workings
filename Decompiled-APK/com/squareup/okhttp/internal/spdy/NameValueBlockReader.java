package com.squareup.okhttp.internal.spdy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSource;
import okio.InflaterSource;
import okio.Okio;
import okio.Source;
import org.bouncycastle.crypto.macs.SkeinMac;

class NameValueBlockReader {
    private int compressedLimit;
    private final InflaterSource inflaterSource;
    private final BufferedSource source;

    /* renamed from: com.squareup.okhttp.internal.spdy.NameValueBlockReader.1 */
    class C06541 extends ForwardingSource {
        C06541(Source source) {
            super(source);
        }

        public long read(Buffer buffer, long j) {
            if (NameValueBlockReader.this.compressedLimit == 0) {
                return -1;
            }
            long read = super.read(buffer, Math.min(j, (long) NameValueBlockReader.this.compressedLimit));
            if (read == -1) {
                return -1;
            }
            NameValueBlockReader.this.compressedLimit = (int) (((long) NameValueBlockReader.this.compressedLimit) - read);
            return read;
        }
    }

    /* renamed from: com.squareup.okhttp.internal.spdy.NameValueBlockReader.2 */
    class C06552 extends Inflater {
        C06552() {
        }

        public int inflate(byte[] bArr, int i, int i2) {
            int inflate = super.inflate(bArr, i, i2);
            if (inflate != 0 || !needsDictionary()) {
                return inflate;
            }
            setDictionary(Spdy3.DICTIONARY);
            return super.inflate(bArr, i, i2);
        }
    }

    public NameValueBlockReader(BufferedSource bufferedSource) {
        this.inflaterSource = new InflaterSource(new C06541(bufferedSource), new C06552());
        this.source = Okio.buffer(this.inflaterSource);
    }

    public List<Header> readNameValueBlock(int i) {
        this.compressedLimit += i;
        int readInt = this.source.readInt();
        if (readInt < 0) {
            throw new IOException("numberOfPairs < 0: " + readInt);
        } else if (readInt > SkeinMac.SKEIN_1024) {
            throw new IOException("numberOfPairs > 1024: " + readInt);
        } else {
            List<Header> arrayList = new ArrayList(readInt);
            for (int i2 = 0; i2 < readInt; i2++) {
                ByteString toAsciiLowercase = readByteString().toAsciiLowercase();
                ByteString readByteString = readByteString();
                if (toAsciiLowercase.size() == 0) {
                    throw new IOException("name.size == 0");
                }
                arrayList.add(new Header(toAsciiLowercase, readByteString));
            }
            doneReading();
            return arrayList;
        }
    }

    private ByteString readByteString() {
        return this.source.readByteString((long) this.source.readInt());
    }

    private void doneReading() {
        if (this.compressedLimit > 0) {
            this.inflaterSource.refill();
            if (this.compressedLimit != 0) {
                throw new IOException("compressedLimit > 0: " + this.compressedLimit);
            }
        }
    }

    public void close() {
        this.source.close();
    }
}
