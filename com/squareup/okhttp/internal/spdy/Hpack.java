package com.squareup.okhttp.internal.spdy;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Source;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

final class Hpack {
    private static final Map<ByteString, Integer> NAME_TO_FIRST_INDEX;
    private static final int PREFIX_4_BITS = 15;
    private static final int PREFIX_5_BITS = 31;
    private static final int PREFIX_6_BITS = 63;
    private static final int PREFIX_7_BITS = 127;
    private static final Header[] STATIC_HEADER_TABLE;

    static final class Reader {
        Header[] dynamicTable;
        int dynamicTableByteCount;
        int headerCount;
        private final List<Header> headerList;
        private int headerTableSizeSetting;
        private int maxDynamicTableByteCount;
        int nextHeaderIndex;
        private final BufferedSource source;

        Reader(int i, Source source) {
            this.headerList = new ArrayList();
            this.dynamicTable = new Header[8];
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
            this.headerTableSizeSetting = i;
            this.maxDynamicTableByteCount = i;
            this.source = Okio.buffer(source);
        }

        int maxDynamicTableByteCount() {
            return this.maxDynamicTableByteCount;
        }

        void headerTableSizeSetting(int i) {
            this.headerTableSizeSetting = i;
            this.maxDynamicTableByteCount = i;
            adjustDynamicTableByteCount();
        }

        private void adjustDynamicTableByteCount() {
            if (this.maxDynamicTableByteCount >= this.dynamicTableByteCount) {
                return;
            }
            if (this.maxDynamicTableByteCount == 0) {
                clearDynamicTable();
            } else {
                evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
            }
        }

        private void clearDynamicTable() {
            this.headerList.clear();
            Arrays.fill(this.dynamicTable, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }

        private int evictToRecoverBytes(int i) {
            int i2 = 0;
            if (i > 0) {
                for (int length = this.dynamicTable.length - 1; length >= this.nextHeaderIndex && i > 0; length--) {
                    i -= this.dynamicTable[length].hpackSize;
                    this.dynamicTableByteCount -= this.dynamicTable[length].hpackSize;
                    this.headerCount--;
                    i2++;
                }
                System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, (this.nextHeaderIndex + 1) + i2, this.headerCount);
                this.nextHeaderIndex += i2;
            }
            return i2;
        }

        void readHeaders() {
            while (!this.source.exhausted()) {
                int readByte = this.source.readByte() & GF2Field.MASK;
                if (readByte == X509KeyUsage.digitalSignature) {
                    throw new IOException("index == 0");
                } else if ((readByte & X509KeyUsage.digitalSignature) == X509KeyUsage.digitalSignature) {
                    readIndexedHeader(readInt(readByte, Hpack.PREFIX_7_BITS) - 1);
                } else if (readByte == 64) {
                    readLiteralHeaderWithIncrementalIndexingNewName();
                } else if ((readByte & 64) == 64) {
                    readLiteralHeaderWithIncrementalIndexingIndexedName(readInt(readByte, Hpack.PREFIX_6_BITS) - 1);
                } else if ((readByte & 32) == 32) {
                    this.maxDynamicTableByteCount = readInt(readByte, Hpack.PREFIX_5_BITS);
                    if (this.maxDynamicTableByteCount < 0 || this.maxDynamicTableByteCount > this.headerTableSizeSetting) {
                        throw new IOException("Invalid dynamic table size update " + this.maxDynamicTableByteCount);
                    }
                    adjustDynamicTableByteCount();
                } else if (readByte == 16 || readByte == 0) {
                    readLiteralHeaderWithoutIndexingNewName();
                } else {
                    readLiteralHeaderWithoutIndexingIndexedName(readInt(readByte, Hpack.PREFIX_4_BITS) - 1);
                }
            }
        }

        public List<Header> getAndResetHeaderList() {
            List arrayList = new ArrayList(this.headerList);
            this.headerList.clear();
            return arrayList;
        }

        private void readIndexedHeader(int i) {
            if (isStaticHeader(i)) {
                this.headerList.add(Hpack.STATIC_HEADER_TABLE[i]);
                return;
            }
            int dynamicTableIndex = dynamicTableIndex(i - Hpack.STATIC_HEADER_TABLE.length);
            if (dynamicTableIndex < 0 || dynamicTableIndex > this.dynamicTable.length - 1) {
                throw new IOException("Header index too large " + (i + 1));
            }
            this.headerList.add(this.dynamicTable[dynamicTableIndex]);
        }

        private int dynamicTableIndex(int i) {
            return (this.nextHeaderIndex + 1) + i;
        }

        private void readLiteralHeaderWithoutIndexingIndexedName(int i) {
            this.headerList.add(new Header(getName(i), readByteString()));
        }

        private void readLiteralHeaderWithoutIndexingNewName() {
            this.headerList.add(new Header(Hpack.checkLowercase(readByteString()), readByteString()));
        }

        private void readLiteralHeaderWithIncrementalIndexingIndexedName(int i) {
            insertIntoDynamicTable(-1, new Header(getName(i), readByteString()));
        }

        private void readLiteralHeaderWithIncrementalIndexingNewName() {
            insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(readByteString()), readByteString()));
        }

        private ByteString getName(int i) {
            if (isStaticHeader(i)) {
                return Hpack.STATIC_HEADER_TABLE[i].name;
            }
            return this.dynamicTable[dynamicTableIndex(i - Hpack.STATIC_HEADER_TABLE.length)].name;
        }

        private boolean isStaticHeader(int i) {
            return i >= 0 && i <= Hpack.STATIC_HEADER_TABLE.length - 1;
        }

        private void insertIntoDynamicTable(int i, Header header) {
            this.headerList.add(header);
            int i2 = header.hpackSize;
            if (i != -1) {
                i2 -= this.dynamicTable[dynamicTableIndex(i)].hpackSize;
            }
            if (i2 > this.maxDynamicTableByteCount) {
                clearDynamicTable();
                return;
            }
            int evictToRecoverBytes = evictToRecoverBytes((this.dynamicTableByteCount + i2) - this.maxDynamicTableByteCount);
            if (i == -1) {
                if (this.headerCount + 1 > this.dynamicTable.length) {
                    Object obj = new Header[(this.dynamicTable.length * 2)];
                    System.arraycopy(this.dynamicTable, 0, obj, this.dynamicTable.length, this.dynamicTable.length);
                    this.nextHeaderIndex = this.dynamicTable.length - 1;
                    this.dynamicTable = obj;
                }
                evictToRecoverBytes = this.nextHeaderIndex;
                this.nextHeaderIndex = evictToRecoverBytes - 1;
                this.dynamicTable[evictToRecoverBytes] = header;
                this.headerCount++;
            } else {
                this.dynamicTable[(evictToRecoverBytes + dynamicTableIndex(i)) + i] = header;
            }
            this.dynamicTableByteCount = i2 + this.dynamicTableByteCount;
        }

        private int readByte() {
            return this.source.readByte() & GF2Field.MASK;
        }

        int readInt(int i, int i2) {
            int i3 = i & i2;
            if (i3 < i2) {
                return i3;
            }
            i3 = 0;
            while (true) {
                int readByte = readByte();
                if ((readByte & X509KeyUsage.digitalSignature) == 0) {
                    return (readByte << i3) + i2;
                }
                i2 += (readByte & Hpack.PREFIX_7_BITS) << i3;
                i3 += 7;
            }
        }

        ByteString readByteString() {
            int readByte = readByte();
            Object obj = (readByte & X509KeyUsage.digitalSignature) == X509KeyUsage.digitalSignature ? 1 : null;
            readByte = readInt(readByte, Hpack.PREFIX_7_BITS);
            if (obj != null) {
                return ByteString.of(Huffman.get().decode(this.source.readByteArray((long) readByte)));
            }
            return this.source.readByteString((long) readByte);
        }
    }

    static final class Writer {
        private final Buffer out;

        Writer(Buffer buffer) {
            this.out = buffer;
        }

        void writeHeaders(List<Header> list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ByteString toAsciiLowercase = ((Header) list.get(i)).name.toAsciiLowercase();
                Integer num = (Integer) Hpack.NAME_TO_FIRST_INDEX.get(toAsciiLowercase);
                if (num != null) {
                    writeInt(num.intValue() + 1, Hpack.PREFIX_4_BITS, 0);
                    writeByteString(((Header) list.get(i)).value);
                } else {
                    this.out.writeByte(0);
                    writeByteString(toAsciiLowercase);
                    writeByteString(((Header) list.get(i)).value);
                }
            }
        }

        void writeInt(int i, int i2, int i3) {
            if (i < i2) {
                this.out.writeByte(i3 | i);
                return;
            }
            this.out.writeByte(i3 | i2);
            int i4 = i - i2;
            while (i4 >= X509KeyUsage.digitalSignature) {
                this.out.writeByte((i4 & Hpack.PREFIX_7_BITS) | X509KeyUsage.digitalSignature);
                i4 >>>= 7;
            }
            this.out.writeByte(i4);
        }

        void writeByteString(ByteString byteString) {
            writeInt(byteString.size(), Hpack.PREFIX_7_BITS, 0);
            this.out.write(byteString);
        }
    }

    static {
        STATIC_HEADER_TABLE = new Header[]{new Header(Header.TARGET_AUTHORITY, BuildConfig.FLAVOR), new Header(Header.TARGET_METHOD, "GET"), new Header(Header.TARGET_METHOD, "POST"), new Header(Header.TARGET_PATH, "/"), new Header(Header.TARGET_PATH, "/index.html"), new Header(Header.TARGET_SCHEME, "http"), new Header(Header.TARGET_SCHEME, "https"), new Header(Header.RESPONSE_STATUS, "200"), new Header(Header.RESPONSE_STATUS, "204"), new Header(Header.RESPONSE_STATUS, "206"), new Header(Header.RESPONSE_STATUS, "304"), new Header(Header.RESPONSE_STATUS, "400"), new Header(Header.RESPONSE_STATUS, "404"), new Header(Header.RESPONSE_STATUS, "500"), new Header("accept-charset", BuildConfig.FLAVOR), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", BuildConfig.FLAVOR), new Header("accept-ranges", BuildConfig.FLAVOR), new Header("accept", BuildConfig.FLAVOR), new Header("access-control-allow-origin", BuildConfig.FLAVOR), new Header("age", BuildConfig.FLAVOR), new Header("allow", BuildConfig.FLAVOR), new Header("authorization", BuildConfig.FLAVOR), new Header("cache-control", BuildConfig.FLAVOR), new Header("content-disposition", BuildConfig.FLAVOR), new Header("content-encoding", BuildConfig.FLAVOR), new Header("content-language", BuildConfig.FLAVOR), new Header("content-length", BuildConfig.FLAVOR), new Header("content-location", BuildConfig.FLAVOR), new Header("content-range", BuildConfig.FLAVOR), new Header("content-type", BuildConfig.FLAVOR), new Header("cookie", BuildConfig.FLAVOR), new Header("date", BuildConfig.FLAVOR), new Header("etag", BuildConfig.FLAVOR), new Header("expect", BuildConfig.FLAVOR), new Header("expires", BuildConfig.FLAVOR), new Header("from", BuildConfig.FLAVOR), new Header("host", BuildConfig.FLAVOR), new Header("if-match", BuildConfig.FLAVOR), new Header("if-modified-since", BuildConfig.FLAVOR), new Header("if-none-match", BuildConfig.FLAVOR), new Header("if-range", BuildConfig.FLAVOR), new Header("if-unmodified-since", BuildConfig.FLAVOR), new Header("last-modified", BuildConfig.FLAVOR), new Header("link", BuildConfig.FLAVOR), new Header("location", BuildConfig.FLAVOR), new Header("max-forwards", BuildConfig.FLAVOR), new Header("proxy-authenticate", BuildConfig.FLAVOR), new Header("proxy-authorization", BuildConfig.FLAVOR), new Header("range", BuildConfig.FLAVOR), new Header("referer", BuildConfig.FLAVOR), new Header("refresh", BuildConfig.FLAVOR), new Header("retry-after", BuildConfig.FLAVOR), new Header("server", BuildConfig.FLAVOR), new Header("set-cookie", BuildConfig.FLAVOR), new Header("strict-transport-security", BuildConfig.FLAVOR), new Header("transfer-encoding", BuildConfig.FLAVOR), new Header("user-agent", BuildConfig.FLAVOR), new Header("vary", BuildConfig.FLAVOR), new Header("via", BuildConfig.FLAVOR), new Header("www-authenticate", BuildConfig.FLAVOR)};
        NAME_TO_FIRST_INDEX = nameToFirstIndex();
    }

    private Hpack() {
    }

    private static Map<ByteString, Integer> nameToFirstIndex() {
        Map linkedHashMap = new LinkedHashMap(STATIC_HEADER_TABLE.length);
        for (int i = 0; i < STATIC_HEADER_TABLE.length; i++) {
            if (!linkedHashMap.containsKey(STATIC_HEADER_TABLE[i].name)) {
                linkedHashMap.put(STATIC_HEADER_TABLE[i].name, Integer.valueOf(i));
            }
        }
        return Collections.unmodifiableMap(linkedHashMap);
    }

    private static ByteString checkLowercase(ByteString byteString) {
        int i = 0;
        int size = byteString.size();
        while (i < size) {
            byte b = byteString.getByte(i);
            if (b < 65 || b > 90) {
                i++;
            } else {
                throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + byteString.utf8());
            }
        }
        return byteString;
    }
}
