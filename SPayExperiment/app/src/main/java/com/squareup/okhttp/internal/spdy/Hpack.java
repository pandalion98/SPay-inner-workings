/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.Huffman;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Source;

final class Hpack {
    private static final Map<ByteString, Integer> NAME_TO_FIRST_INDEX;
    private static final int PREFIX_4_BITS = 15;
    private static final int PREFIX_5_BITS = 31;
    private static final int PREFIX_6_BITS = 63;
    private static final int PREFIX_7_BITS = 127;
    private static final Header[] STATIC_HEADER_TABLE;

    static {
        Header[] arrheader = new Header[]{new Header(Header.TARGET_AUTHORITY, ""), new Header(Header.TARGET_METHOD, "GET"), new Header(Header.TARGET_METHOD, "POST"), new Header(Header.TARGET_PATH, "/"), new Header(Header.TARGET_PATH, "/index.html"), new Header(Header.TARGET_SCHEME, "http"), new Header(Header.TARGET_SCHEME, "https"), new Header(Header.RESPONSE_STATUS, "200"), new Header(Header.RESPONSE_STATUS, "204"), new Header(Header.RESPONSE_STATUS, "206"), new Header(Header.RESPONSE_STATUS, "304"), new Header(Header.RESPONSE_STATUS, "400"), new Header(Header.RESPONSE_STATUS, "404"), new Header(Header.RESPONSE_STATUS, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), new Header("www-authenticate", "")};
        STATIC_HEADER_TABLE = arrheader;
        NAME_TO_FIRST_INDEX = Hpack.nameToFirstIndex();
    }

    private Hpack() {
    }

    private static ByteString checkLowercase(ByteString byteString) {
        int n2 = byteString.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = byteString.getByte(i2);
            if (by < 65 || by > 90) continue;
            throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + byteString.utf8());
        }
        return byteString;
    }

    private static Map<ByteString, Integer> nameToFirstIndex() {
        LinkedHashMap linkedHashMap = new LinkedHashMap(STATIC_HEADER_TABLE.length);
        for (int i2 = 0; i2 < STATIC_HEADER_TABLE.length; ++i2) {
            if (linkedHashMap.containsKey((Object)Hpack.STATIC_HEADER_TABLE[i2].name)) continue;
            linkedHashMap.put((Object)Hpack.STATIC_HEADER_TABLE[i2].name, (Object)i2);
        }
        return Collections.unmodifiableMap((Map)linkedHashMap);
    }

    static final class Reader {
        Header[] dynamicTable = new Header[8];
        int dynamicTableByteCount = 0;
        int headerCount = 0;
        private final List<Header> headerList = new ArrayList();
        private int headerTableSizeSetting;
        private int maxDynamicTableByteCount;
        int nextHeaderIndex = -1 + this.dynamicTable.length;
        private final BufferedSource source;

        Reader(int n2, Source source) {
            this.headerTableSizeSetting = n2;
            this.maxDynamicTableByteCount = n2;
            this.source = Okio.buffer(source);
        }

        private void adjustDynamicTableByteCount() {
            block3 : {
                block2 : {
                    if (this.maxDynamicTableByteCount >= this.dynamicTableByteCount) break block2;
                    if (this.maxDynamicTableByteCount != 0) break block3;
                    this.clearDynamicTable();
                }
                return;
            }
            this.evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
        }

        private void clearDynamicTable() {
            this.headerList.clear();
            Arrays.fill((Object[])this.dynamicTable, null);
            this.nextHeaderIndex = -1 + this.dynamicTable.length;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }

        private int dynamicTableIndex(int n2) {
            return n2 + (1 + this.nextHeaderIndex);
        }

        private int evictToRecoverBytes(int n2) {
            int n3 = 0;
            if (n2 > 0) {
                for (int i2 = -1 + this.dynamicTable.length; i2 >= this.nextHeaderIndex && n2 > 0; --i2) {
                    n2 -= this.dynamicTable[i2].hpackSize;
                    this.dynamicTableByteCount -= this.dynamicTable[i2].hpackSize;
                    this.headerCount = -1 + this.headerCount;
                    ++n3;
                }
                System.arraycopy((Object)this.dynamicTable, (int)(1 + this.nextHeaderIndex), (Object)this.dynamicTable, (int)(n3 + (1 + this.nextHeaderIndex)), (int)this.headerCount);
                this.nextHeaderIndex = n3 + this.nextHeaderIndex;
            }
            return n3;
        }

        private ByteString getName(int n2) {
            if (this.isStaticHeader(n2)) {
                return Hpack.access$000()[n2].name;
            }
            return this.dynamicTable[this.dynamicTableIndex((int)(n2 - Hpack.access$000().length))].name;
        }

        /*
         * Enabled aggressive block sorting
         */
        private void insertIntoDynamicTable(int n2, Header header) {
            this.headerList.add((Object)header);
            int n3 = header.hpackSize;
            if (n2 != -1) {
                n3 -= this.dynamicTable[this.dynamicTableIndex((int)n2)].hpackSize;
            }
            if (n3 > this.maxDynamicTableByteCount) {
                this.clearDynamicTable();
                return;
            }
            int n4 = this.evictToRecoverBytes(n3 + this.dynamicTableByteCount - this.maxDynamicTableByteCount);
            if (n2 == -1) {
                if (1 + this.headerCount > this.dynamicTable.length) {
                    Header[] arrheader = new Header[2 * this.dynamicTable.length];
                    System.arraycopy((Object)this.dynamicTable, (int)0, (Object)arrheader, (int)this.dynamicTable.length, (int)this.dynamicTable.length);
                    this.nextHeaderIndex = -1 + this.dynamicTable.length;
                    this.dynamicTable = arrheader;
                }
                int n5 = this.nextHeaderIndex;
                this.nextHeaderIndex = n5 - 1;
                this.dynamicTable[n5] = header;
                this.headerCount = 1 + this.headerCount;
            } else {
                int n6 = n2 + (n4 + this.dynamicTableIndex(n2));
                this.dynamicTable[n6] = header;
            }
            this.dynamicTableByteCount = n3 + this.dynamicTableByteCount;
        }

        private boolean isStaticHeader(int n2) {
            return n2 >= 0 && n2 <= -1 + STATIC_HEADER_TABLE.length;
        }

        private int readByte() {
            return 255 & this.source.readByte();
        }

        private void readIndexedHeader(int n2) {
            if (this.isStaticHeader(n2)) {
                Header header = STATIC_HEADER_TABLE[n2];
                this.headerList.add((Object)header);
                return;
            }
            int n3 = this.dynamicTableIndex(n2 - STATIC_HEADER_TABLE.length);
            if (n3 < 0 || n3 > -1 + this.dynamicTable.length) {
                throw new IOException("Header index too large " + (n2 + 1));
            }
            this.headerList.add((Object)this.dynamicTable[n3]);
        }

        private void readLiteralHeaderWithIncrementalIndexingIndexedName(int n2) {
            this.insertIntoDynamicTable(-1, new Header(this.getName(n2), this.readByteString()));
        }

        private void readLiteralHeaderWithIncrementalIndexingNewName() {
            this.insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(this.readByteString()), this.readByteString()));
        }

        private void readLiteralHeaderWithoutIndexingIndexedName(int n2) {
            ByteString byteString = this.getName(n2);
            ByteString byteString2 = this.readByteString();
            this.headerList.add((Object)new Header(byteString, byteString2));
        }

        private void readLiteralHeaderWithoutIndexingNewName() {
            ByteString byteString = Hpack.checkLowercase(this.readByteString());
            ByteString byteString2 = this.readByteString();
            this.headerList.add((Object)new Header(byteString, byteString2));
        }

        public List<Header> getAndResetHeaderList() {
            ArrayList arrayList = new ArrayList(this.headerList);
            this.headerList.clear();
            return arrayList;
        }

        void headerTableSizeSetting(int n2) {
            this.headerTableSizeSetting = n2;
            this.maxDynamicTableByteCount = n2;
            this.adjustDynamicTableByteCount();
        }

        int maxDynamicTableByteCount() {
            return this.maxDynamicTableByteCount;
        }

        /*
         * Enabled aggressive block sorting
         */
        ByteString readByteString() {
            int n2 = this.readByte();
            boolean bl = (n2 & 128) == 128;
            int n3 = this.readInt(n2, 127);
            if (bl) {
                return ByteString.of(Huffman.get().decode(this.source.readByteArray(n3)));
            }
            return this.source.readByteString(n3);
        }

        void readHeaders() {
            while (!this.source.exhausted()) {
                int n2 = 255 & this.source.readByte();
                if (n2 == 128) {
                    throw new IOException("index == 0");
                }
                if ((n2 & 128) == 128) {
                    this.readIndexedHeader(-1 + this.readInt(n2, 127));
                    continue;
                }
                if (n2 == 64) {
                    this.readLiteralHeaderWithIncrementalIndexingNewName();
                    continue;
                }
                if ((n2 & 64) == 64) {
                    this.readLiteralHeaderWithIncrementalIndexingIndexedName(-1 + this.readInt(n2, 63));
                    continue;
                }
                if ((n2 & 32) == 32) {
                    this.maxDynamicTableByteCount = this.readInt(n2, 31);
                    if (this.maxDynamicTableByteCount < 0 || this.maxDynamicTableByteCount > this.headerTableSizeSetting) {
                        throw new IOException("Invalid dynamic table size update " + this.maxDynamicTableByteCount);
                    }
                    this.adjustDynamicTableByteCount();
                    continue;
                }
                if (n2 == 16 || n2 == 0) {
                    this.readLiteralHeaderWithoutIndexingNewName();
                    continue;
                }
                this.readLiteralHeaderWithoutIndexingIndexedName(-1 + this.readInt(n2, 15));
            }
        }

        int readInt(int n2, int n3) {
            int n4;
            int n5 = n2 & n3;
            if (n5 < n3) {
                return n5;
            }
            int n6 = 0;
            while (((n4 = this.readByte()) & 128) != 0) {
                n3 += (n4 & 127) << n6;
                n6 += 7;
            }
            return n3 + (n4 << n6);
        }
    }

    static final class Writer {
        private final Buffer out;

        Writer(Buffer buffer) {
            this.out = buffer;
        }

        void writeByteString(ByteString byteString) {
            this.writeInt(byteString.size(), 127, 0);
            this.out.write(byteString);
        }

        /*
         * Enabled aggressive block sorting
         */
        void writeHeaders(List<Header> list) {
            int n2 = list.size();
            int n3 = 0;
            while (n3 < n2) {
                ByteString byteString = ((Header)list.get((int)n3)).name.toAsciiLowercase();
                Integer n4 = (Integer)NAME_TO_FIRST_INDEX.get((Object)byteString);
                if (n4 != null) {
                    this.writeInt(1 + n4, 15, 0);
                    this.writeByteString(((Header)list.get((int)n3)).value);
                } else {
                    this.out.writeByte(0);
                    this.writeByteString(byteString);
                    this.writeByteString(((Header)list.get((int)n3)).value);
                }
                ++n3;
            }
            return;
        }

        void writeInt(int n2, int n3, int n4) {
            int n5;
            if (n2 < n3) {
                this.out.writeByte(n4 | n2);
                return;
            }
            this.out.writeByte(n4 | n3);
            for (n5 = n2 - n3; n5 >= 128; n5 >>>= 7) {
                int n6 = n5 & 127;
                this.out.writeByte(n6 | 128);
            }
            this.out.writeByte(n5);
        }
    }

}

