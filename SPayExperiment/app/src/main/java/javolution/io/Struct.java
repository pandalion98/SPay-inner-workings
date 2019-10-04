/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Enum
 *  java.lang.Error
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.UnsupportedOperationException
 *  java.nio.Buffer
 *  java.nio.ByteBuffer
 *  java.nio.ByteOrder
 *  java.util.HashMap
 *  java.util.Map
 */
package javolution.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import javolution.io.UTF8ByteBufferReader;
import javolution.io.UTF8ByteBufferWriter;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class Struct {
    private static final Class BOOL;
    private static final Class FLOAT_32;
    private static final Class FLOAT_64;
    private static final char[] HEXA;
    private static final Class SIGNED_16;
    private static final Class SIGNED_32;
    private static final Class SIGNED_64;
    private static final Class SIGNED_8;
    private static final Class UNSIGNED_16;
    private static final Class UNSIGNED_32;
    private static final Class UNSIGNED_8;
    protected int _alignment = 1;
    private int _bitsUsed;
    private ByteBuffer _byteBuffer;
    private byte[] _bytes;
    private int _index;
    private int _length;
    Map<String, Class> _nameToClass = new HashMap();
    protected Struct _outer;
    protected int _outerOffset;
    private boolean _resetIndex = this.isUnion();
    private int _wordSize;

    static {
        HEXA = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        BOOL = new Bool[0].getClass();
        SIGNED_8 = new Signed8[0].getClass();
        UNSIGNED_8 = new Unsigned8[0].getClass();
        SIGNED_16 = new Signed16[0].getClass();
        UNSIGNED_16 = new Unsigned16[0].getClass();
        SIGNED_32 = new Signed32[0].getClass();
        UNSIGNED_32 = new Unsigned32[0].getClass();
        SIGNED_64 = new Signed64[0].getClass();
        FLOAT_32 = new Float32[0].getClass();
        FLOAT_64 = new Float64[0].getClass();
    }

    static /* synthetic */ int access$108(Struct struct) {
        int n2 = struct._index;
        struct._index = n2 + 1;
        return n2;
    }

    static /* synthetic */ int access$208(Struct struct) {
        int n2 = struct._wordSize;
        struct._wordSize = n2 + 1;
        return n2;
    }

    public static int max(int n2, int n3) {
        if (n2 >= n3) {
            return n2;
        }
        return n3;
    }

    private ByteBuffer newBuffer() {
        Struct struct = this;
        synchronized (struct) {
            ByteBuffer byteBuffer;
            if (this._byteBuffer != null) {
                byteBuffer = this._byteBuffer;
                return byteBuffer;
            }
            ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect((int)this.size());
            byteBuffer2.order(this.byteOrder());
            this.setByteBuffer(byteBuffer2, 0);
            byteBuffer = this._byteBuffer;
        }
    }

    private static byte readByte(int n2, ByteBuffer byteBuffer) {
        if (n2 < byteBuffer.limit()) {
            return byteBuffer.get(n2);
        }
        return 0;
    }

    private long readByteBufferLong(int n2) {
        ByteBuffer byteBuffer = this.getByteBuffer();
        if (n2 + 8 < byteBuffer.limit()) {
            return byteBuffer.getLong(n2);
        }
        if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
            long l2 = 255L & (long)Struct.readByte(n2, byteBuffer);
            int n3 = n2 + 1;
            long l3 = l2 + ((255L & (long)Struct.readByte(n3, byteBuffer)) << 8);
            int n4 = n3 + 1;
            long l4 = l3 + ((255L & (long)Struct.readByte(n4, byteBuffer)) << 16);
            int n5 = n4 + 1;
            long l5 = l4 + ((255L & (long)Struct.readByte(n5, byteBuffer)) << 24);
            int n6 = n5 + 1;
            long l6 = l5 + ((255L & (long)Struct.readByte(n6, byteBuffer)) << 32);
            int n7 = n6 + 1;
            long l7 = l6 + ((255L & (long)Struct.readByte(n7, byteBuffer)) << 40);
            int n8 = n7 + 1;
            return l7 + ((255L & (long)Struct.readByte(n8, byteBuffer)) << 48) + ((255L & (long)Struct.readByte(n8 + 1, byteBuffer)) << 56);
        }
        long l8 = (long)Struct.readByte(n2, byteBuffer) << 56;
        int n9 = n2 + 1;
        long l9 = l8 + ((255L & (long)Struct.readByte(n9, byteBuffer)) << 48);
        int n10 = n9 + 1;
        long l10 = l9 + ((255L & (long)Struct.readByte(n10, byteBuffer)) << 40);
        int n11 = n10 + 1;
        long l11 = l10 + ((255L & (long)Struct.readByte(n11, byteBuffer)) << 32);
        int n12 = n11 + 1;
        long l12 = l11 + ((255L & (long)Struct.readByte(n12, byteBuffer)) << 24);
        int n13 = n12 + 1;
        long l13 = l12 + ((255L & (long)Struct.readByte(n13, byteBuffer)) << 16);
        int n14 = n13 + 1;
        return l13 + ((255L & (long)Struct.readByte(n14, byteBuffer)) << 8) + (255L & (long)Struct.readByte(n14 + 1, byteBuffer));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Class searchClass(String string) {
        Class class_;
        try {
            Class class_2;
            class_ = class_2 = Class.forName((String)string);
            if (class_ == null) return class_;
        }
        catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
        this._nameToClass.put((Object)string, (Object)class_);
        return class_;
    }

    protected <M extends Member> M[] array(M[] arrM) {
        boolean bl = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        boolean bl2 = BOOL.isInstance(arrM);
        int n2 = 0;
        if (bl2) {
            while (n2 < arrM.length) {
                int n3 = n2 + 1;
                arrM[n2] = new Bool();
                n2 = n3;
            }
        } else {
            boolean bl3 = SIGNED_8.isInstance(arrM);
            int n4 = 0;
            if (bl3) {
                while (n4 < arrM.length) {
                    int n5 = n4 + 1;
                    arrM[n4] = new Signed8();
                    n4 = n5;
                }
            } else {
                boolean bl4 = UNSIGNED_8.isInstance(arrM);
                int n6 = 0;
                if (bl4) {
                    while (n6 < arrM.length) {
                        int n7 = n6 + 1;
                        arrM[n6] = new Unsigned8();
                        n6 = n7;
                    }
                } else {
                    boolean bl5 = SIGNED_16.isInstance(arrM);
                    int n8 = 0;
                    if (bl5) {
                        while (n8 < arrM.length) {
                            int n9 = n8 + 1;
                            arrM[n8] = new Signed16();
                            n8 = n9;
                        }
                    } else {
                        boolean bl6 = UNSIGNED_16.isInstance(arrM);
                        int n10 = 0;
                        if (bl6) {
                            while (n10 < arrM.length) {
                                int n11 = n10 + 1;
                                arrM[n10] = new Unsigned16();
                                n10 = n11;
                            }
                        } else {
                            boolean bl7 = SIGNED_32.isInstance(arrM);
                            int n12 = 0;
                            if (bl7) {
                                while (n12 < arrM.length) {
                                    int n13 = n12 + 1;
                                    arrM[n12] = new Signed32();
                                    n12 = n13;
                                }
                            } else {
                                boolean bl8 = UNSIGNED_32.isInstance(arrM);
                                int n14 = 0;
                                if (bl8) {
                                    while (n14 < arrM.length) {
                                        int n15 = n14 + 1;
                                        arrM[n14] = new Unsigned32();
                                        n14 = n15;
                                    }
                                } else {
                                    boolean bl9 = SIGNED_64.isInstance(arrM);
                                    int n16 = 0;
                                    if (bl9) {
                                        while (n16 < arrM.length) {
                                            int n17 = n16 + 1;
                                            arrM[n16] = new Signed64();
                                            n16 = n17;
                                        }
                                    } else {
                                        boolean bl10 = FLOAT_32.isInstance(arrM);
                                        int n18 = 0;
                                        if (bl10) {
                                            while (n18 < arrM.length) {
                                                int n19 = n18 + 1;
                                                arrM[n18] = new Float32();
                                                n18 = n19;
                                            }
                                        } else {
                                            boolean bl11 = FLOAT_64.isInstance(arrM);
                                            int n20 = 0;
                                            if (bl11) {
                                                while (n20 < arrM.length) {
                                                    int n21 = n20 + 1;
                                                    arrM[n20] = new Float64();
                                                    n20 = n21;
                                                }
                                            } else {
                                                throw new UnsupportedOperationException("Cannot create member elements, the arrayMember should contain the member instances instead of null");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this._resetIndex = bl;
        return (Member[])arrM;
    }

    protected UTF8String[] array(UTF8String[] arruTF8String, int n2) {
        boolean bl = this._resetIndex;
        boolean bl2 = this._resetIndex;
        int n3 = 0;
        if (bl2) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (n3 < arruTF8String.length) {
            arruTF8String[n3] = new UTF8String(n2);
            ++n3;
        }
        this._resetIndex = bl;
        return arruTF8String;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected <S extends Struct> S[] array(S[] var1_1) {
        var2_2 = null;
        var3_3 = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        var4_4 = 0;
        do {
            block7 : {
                if (var4_4 >= var1_1.length) {
                    this._resetIndex = var3_3;
                    return (Struct[])var1_1;
                }
                var5_5 /* !! */  = var1_1[var4_4];
                if (var5_5 /* !! */  != null) break block7;
                if (var2_2 != null) ** GOTO lbl20
                try {
                    var8_7 = var1_1.getClass().getName();
                    var9_8 = var8_7.substring(2, -1 + var8_7.length());
                    var2_2 = this.getClass(var9_8);
                    if (var2_2 == null) {
                        throw new IllegalArgumentException("Struct class: " + var9_8 + " not found");
                    }
lbl20: // 3 sources:
                    var5_5 /* !! */  = (Struct)var2_2.newInstance();
                }
                catch (Exception var7_9) {
                    throw new RuntimeException(var7_9.getMessage());
                }
            }
            var6_6 = var4_4 + 1;
            var1_1[var4_4] = this.inner(var5_5 /* !! */ );
            var4_4 = var6_6;
        } while (true);
    }

    protected <M extends Member> M[][] array(M[][] arrM) {
        boolean bl = this._resetIndex;
        boolean bl2 = this._resetIndex;
        int n2 = 0;
        if (bl2) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (n2 < arrM.length) {
            this.array(arrM[n2]);
            ++n2;
        }
        this._resetIndex = bl;
        return (Member[][])arrM;
    }

    protected <S extends Struct> S[][] array(S[][] arrS) {
        boolean bl = this._resetIndex;
        boolean bl2 = this._resetIndex;
        int n2 = 0;
        if (bl2) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (n2 < arrS.length) {
            this.array(arrS[n2]);
            ++n2;
        }
        this._resetIndex = bl;
        return (Struct[][])arrS;
    }

    protected <M extends Member> M[][][] array(M[][][] arrM) {
        boolean bl = this._resetIndex;
        boolean bl2 = this._resetIndex;
        int n2 = 0;
        if (bl2) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (n2 < arrM.length) {
            this.array(arrM[n2]);
            ++n2;
        }
        this._resetIndex = bl;
        return (Member[][][])arrM;
    }

    protected <S extends Struct> S[][][] array(S[][][] arrS) {
        boolean bl = this._resetIndex;
        boolean bl2 = this._resetIndex;
        int n2 = 0;
        if (bl2) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (n2 < arrS.length) {
            this.array(arrS[n2]);
            ++n2;
        }
        this._resetIndex = bl;
        return (Struct[][][])arrS;
    }

    public ByteOrder byteOrder() {
        if (this._outer != null) {
            return this._outer.byteOrder();
        }
        return ByteOrder.BIG_ENDIAN;
    }

    public final ByteBuffer getByteBuffer() {
        if (this._outer != null) {
            return this._outer.getByteBuffer();
        }
        if (this._byteBuffer != null) {
            return this._byteBuffer;
        }
        return this.newBuffer();
    }

    public final int getByteBufferPosition() {
        if (this._outer != null) {
            return this._outer.getByteBufferPosition() + this._outerOffset;
        }
        return this._outerOffset;
    }

    public Class getClass(CharSequence charSequence) {
        Class class_ = (Class)this._nameToClass.get((Object)charSequence);
        if (class_ != null) {
            return class_;
        }
        return this.searchClass(charSequence.toString());
    }

    protected <S extends Struct> S inner(S s2) {
        if (((Struct)s2)._outer != null) {
            throw new IllegalArgumentException("struct: Already an inner struct");
        }
        Member member = new Member(((Struct)s2).size() << 3, ((Struct)s2)._alignment);
        ((Struct)s2)._outer = this;
        ((Struct)s2)._outerOffset = member.offset();
        return s2;
    }

    public boolean isPacked() {
        return false;
    }

    public boolean isUnion() {
        return false;
    }

    public Struct outer() {
        return this._outer;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int read(InputStream inputStream) {
        ByteBuffer byteBuffer = this.getByteBuffer();
        if (byteBuffer.hasArray()) {
            int n2 = byteBuffer.arrayOffset() + this.getByteBufferPosition();
            return inputStream.read(byteBuffer.array(), n2, this.size());
        }
        ByteBuffer byteBuffer2 = byteBuffer;
        synchronized (byteBuffer2) {
            if (this._bytes == null) {
                this._bytes = new byte[this.size()];
            }
            int n3 = inputStream.read(this._bytes);
            byteBuffer.position(this.getByteBufferPosition());
            byteBuffer.put(this._bytes);
            return n3;
        }
    }

    public final Struct setByteBuffer(ByteBuffer byteBuffer, int n2) {
        if (byteBuffer.order() != this.byteOrder()) {
            throw new IllegalArgumentException("The byte order of the specified byte buffer is different from this struct byte order");
        }
        if (this._outer != null) {
            throw new UnsupportedOperationException("Inner struct byte buffer is inherited from outer");
        }
        this._byteBuffer = byteBuffer;
        this._outerOffset = n2;
        return this;
    }

    public final Struct setByteBufferPosition(int n2) {
        return this.setByteBuffer(this.getByteBuffer(), n2);
    }

    public final int size() {
        if (this._alignment <= 1) {
            return this._length;
        }
        return (-1 + (this._length + this._alignment)) / this._alignment * this._alignment;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = this.size();
        ByteBuffer byteBuffer = this.getByteBuffer();
        int n3 = this.getByteBufferPosition();
        int n4 = 0;
        while (n4 < n2) {
            int n5 = 255 & byteBuffer.get(n3 + n4);
            stringBuilder.append(HEXA[n5 >> 4]);
            stringBuilder.append(HEXA[n5 & 15]);
            char c2 = (n4 & 15) == 15 ? (char)'\n' : ' ';
            stringBuilder.append(c2);
            ++n4;
        }
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void write(OutputStream outputStream) {
        ByteBuffer byteBuffer = this.getByteBuffer();
        if (byteBuffer.hasArray()) {
            int n2 = byteBuffer.arrayOffset() + this.getByteBufferPosition();
            outputStream.write(byteBuffer.array(), n2, this.size());
            return;
        }
        ByteBuffer byteBuffer2 = byteBuffer;
        synchronized (byteBuffer2) {
            if (this._bytes == null) {
                this._bytes = new byte[this.size()];
            }
            byteBuffer.position(this.getByteBufferPosition());
            byteBuffer.get(this._bytes);
            outputStream.write(this._bytes);
            return;
        }
    }

    public class Bool
    extends Member {
        public Bool() {
            super(8, 1);
        }

        public Bool(int n2) {
            super(n2, 1);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public boolean get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().get(n2);
            if (this.bitLength() == 8) {
                do {
                    return n3 != 0;
                    break;
                } while (true);
            }
            n3 = this.get(1, n3);
            return n3 != 0;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void set(boolean bl) {
            byte by = -1;
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 8) {
                ByteBuffer byteBuffer = Struct.this.getByteBuffer();
                if (!bl) {
                    by = 0;
                }
                byteBuffer.put(n2, by);
                return;
            }
            ByteBuffer byteBuffer = Struct.this.getByteBuffer();
            if (!bl) {
                by = 0;
            }
            byteBuffer.put(n2, (byte)this.set(by, 1, Struct.this.getByteBuffer().get(n2)));
        }

        public String toString() {
            return String.valueOf((boolean)this.get());
        }
    }

    public class Enum16<T extends Enum>
    extends Member {
        private final Enum[] _values;

        public Enum16(Enum[] arrenum) {
            super(16, 2);
            this._values = arrenum;
        }

        public Enum16(Enum[] arrenum, int n2) {
            super(n2, 2);
            this._values = arrenum;
        }

        public Enum get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            short s2 = Struct.this.getByteBuffer().getShort(n2);
            return this._values[65535 & this.get(2, s2)];
        }

        public void set(Enum enum_) {
            int n2 = enum_.ordinal();
            if (this._values[n2] != enum_) {
                throw new IllegalArgumentException("enum: " + enum_ + ", ordinal value does not reflect enum values position");
            }
            int n3 = Struct.this.getByteBufferPosition() + this.offset();
            short s2 = Struct.this.getByteBuffer().getShort(n3);
            Struct.this.getByteBuffer().putShort(n3, (short)this.set(n2, 2, s2));
        }

        public String toString() {
            return String.valueOf((Object)this.get());
        }
    }

    public class Enum32<T extends Enum>
    extends Member {
        private final Enum[] _values;

        public Enum32(Enum[] arrenum) {
            super(32, 4);
            this._values = arrenum;
        }

        public Enum32(Enum[] arrenum, int n2) {
            super(n2, 4);
            this._values = arrenum;
        }

        public Enum get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().getInt(n2);
            return this._values[this.get(4, n3)];
        }

        public void set(Enum enum_) {
            int n2 = enum_.ordinal();
            if (this._values[n2] != enum_) {
                throw new IllegalArgumentException("enum: " + enum_ + ", ordinal value does not reflect enum values position");
            }
            int n3 = Struct.this.getByteBufferPosition() + this.offset();
            int n4 = Struct.this.getByteBuffer().getInt(n3);
            Struct.this.getByteBuffer().putInt(n3, this.set(n2, 4, n4));
        }

        public String toString() {
            return String.valueOf((Object)this.get());
        }
    }

    public class Enum64<T extends Enum>
    extends Member {
        private final Enum[] _values;

        public Enum64(Enum[] arrenum) {
            super(64, 8);
            this._values = arrenum;
        }

        public Enum64(Enum[] arrenum, int n2) {
            super(n2, 8);
            this._values = arrenum;
        }

        public Enum get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            long l2 = Struct.this.getByteBuffer().getLong(n2);
            return this._values[(int)this.get(8, l2)];
        }

        public void set(Enum enum_) {
            long l2 = enum_.ordinal();
            if (this._values[(int)l2] != enum_) {
                throw new IllegalArgumentException("enum: " + enum_ + ", ordinal value does not reflect enum values position");
            }
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            long l3 = Struct.this.getByteBuffer().getLong(n2);
            Struct.this.getByteBuffer().putLong(n2, this.set(l2, 8, l3));
        }

        public String toString() {
            return String.valueOf((Object)this.get());
        }
    }

    public class Enum8<T extends Enum>
    extends Member {
        private final Enum[] _values;

        public Enum8(Enum[] arrenum) {
            super(8, 1);
            this._values = arrenum;
        }

        public Enum8(Enum[] arrenum, int n2) {
            super(n2, 1);
            this._values = arrenum;
        }

        public Enum get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            byte by = Struct.this.getByteBuffer().get(n2);
            return this._values[255 & this.get(1, by)];
        }

        public void set(Enum enum_) {
            int n2 = enum_.ordinal();
            if (this._values[n2] != enum_) {
                throw new IllegalArgumentException("enum: " + enum_ + ", ordinal value does not reflect enum values position");
            }
            int n3 = Struct.this.getByteBufferPosition() + this.offset();
            byte by = Struct.this.getByteBuffer().get(n3);
            Struct.this.getByteBuffer().put(n3, (byte)this.set(n2, 1, by));
        }

        public String toString() {
            return String.valueOf((Object)this.get());
        }
    }

    public class Float32
    extends Member {
        public Float32() {
            super(32, 4);
        }

        public float get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            return Struct.this.getByteBuffer().getFloat(n2);
        }

        public void set(float f2) {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            Struct.this.getByteBuffer().putFloat(n2, f2);
        }

        public String toString() {
            return String.valueOf((float)this.get());
        }
    }

    public class Float64
    extends Member {
        public Float64() {
            super(64, 8);
        }

        public double get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            return Struct.this.getByteBuffer().getDouble(n2);
        }

        public void set(double d2) {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            Struct.this.getByteBuffer().putDouble(n2, d2);
        }

        public String toString() {
            return String.valueOf((double)this.get());
        }
    }

    protected class Member {
        private final int _bitIndex;
        private final int _bitLength;
        private final int _offset;

        protected Member(int n2, int n3) {
            this._bitLength = n2;
            if (Struct.this._resetIndex) {
                Struct.this._index = 0;
            }
            if (n3 == 0 || n2 != 0 && n3 == Struct.this._wordSize && n2 + Struct.this._bitsUsed <= n3 << 3) {
                this._offset = Struct.this._index - Struct.this._wordSize;
                this._bitIndex = Struct.this._bitsUsed;
                Struct.this._bitsUsed = n2 + Struct.this._bitsUsed;
                while (Struct.this._bitsUsed > Struct.this._wordSize << 3) {
                    Struct.access$108(Struct.this);
                    Struct.access$208(Struct.this);
                    Struct.this._length = Struct.max(Struct.this._length, Struct.this._index);
                }
            } else {
                if (!Struct.this.isPacked()) {
                    int n4;
                    if (Struct.this._alignment < n3) {
                        Struct.this._alignment = n3;
                    }
                    if ((n4 = Struct.this._index % n3) != 0) {
                        Struct.this._index = Struct.this._index + (n3 - n4);
                    }
                }
                this._offset = Struct.this._index;
                this._bitIndex = 0;
                Struct.this._index = Struct.this._index + Struct.max(n3, n2 + 7 >> 3);
                Struct.this._wordSize = n3;
                Struct.this._bitsUsed = n2;
                Struct.this._length = Struct.max(Struct.this._length, Struct.this._index);
            }
        }

        public final int bitIndex() {
            return this._bitIndex;
        }

        public final int bitLength() {
            return this._bitLength;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        final int get(int n2, int n3) {
            int n4;
            if (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) {
                n4 = (n2 << 3) - this.bitIndex() - this.bitLength();
                do {
                    return n3 >> n4 & -1 >>> 32 - this.bitLength();
                    break;
                } while (true);
            }
            n4 = this.bitIndex();
            return n3 >> n4 & -1 >>> 32 - this.bitLength();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        final long get(int n2, long l2) {
            int n3;
            if (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN) {
                n3 = (n2 << 3) - this.bitIndex() - this.bitLength();
                do {
                    return l2 >> n3 & -1L >>> 64 - this.bitLength();
                    break;
                } while (true);
            }
            n3 = this.bitIndex();
            return l2 >> n3 & -1L >>> 64 - this.bitLength();
        }

        public final int offset() {
            return this._offset;
        }

        /*
         * Enabled aggressive block sorting
         */
        final int set(int n2, int n3, int n4) {
            int n5 = Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN ? (n3 << 3) - this.bitIndex() - this.bitLength() : this.bitIndex();
            int n6 = -1 >>> 32 - this.bitLength() << n5;
            int n7 = n2 << n5;
            return n4 & ~n6 | n7 & n6;
        }

        /*
         * Enabled aggressive block sorting
         */
        final long set(long l2, int n2, long l3) {
            int n3 = Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN ? (n2 << 3) - this.bitIndex() - this.bitLength() : this.bitIndex();
            long l4 = -1L >>> 64 - this.bitLength() << n3;
            long l5 = l2 << n3;
            return l3 & (-1L ^ l4) | l5 & l4;
        }

        public final Struct struct() {
            return Struct.this;
        }
    }

    public class Signed16
    extends Member {
        public Signed16() {
            super(16, 2);
        }

        public Signed16(int n2) {
            super(n2, 2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public short get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().getShort(n2);
            if (this.bitLength() == 16) {
                do {
                    return (short)n3;
                    break;
                } while (true);
            }
            n3 = this.get(2, n3);
            return (short)n3;
        }

        public void set(short s2) {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 16) {
                Struct.this.getByteBuffer().putShort(n2, s2);
                return;
            }
            Struct.this.getByteBuffer().putShort(n2, (short)this.set(s2, 2, Struct.this.getByteBuffer().getShort(n2)));
        }

        public String toString() {
            return String.valueOf((int)this.get());
        }
    }

    public class Signed32
    extends Member {
        public Signed32() {
            super(32, 4);
        }

        public Signed32(int n2) {
            super(n2, 4);
        }

        public int get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().getInt(n2);
            if (this.bitLength() == 32) {
                return n3;
            }
            return this.get(4, n3);
        }

        public void set(int n2) {
            int n3 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 32) {
                Struct.this.getByteBuffer().putInt(n3, n2);
                return;
            }
            Struct.this.getByteBuffer().putInt(n3, this.set(n2, 4, Struct.this.getByteBuffer().getInt(n3)));
        }

        public String toString() {
            return String.valueOf((int)this.get());
        }
    }

    public class Signed64
    extends Member {
        public Signed64() {
            super(64, 8);
        }

        public Signed64(int n2) {
            super(n2, 8);
        }

        public long get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            long l2 = Struct.this.getByteBuffer().getLong(n2);
            if (this.bitLength() == 64) {
                return l2;
            }
            return this.get(8, l2);
        }

        public void set(long l2) {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 64) {
                Struct.this.getByteBuffer().putLong(n2, l2);
                return;
            }
            Struct.this.getByteBuffer().putLong(n2, this.set(l2, 8, Struct.this.getByteBuffer().getLong(n2)));
        }

        public String toString() {
            return String.valueOf((long)this.get());
        }
    }

    public class Signed8
    extends Member {
        public Signed8() {
            super(8, 1);
        }

        public Signed8(int n2) {
            super(n2, 1);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public byte get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().get(n2);
            if (this.bitLength() == 8) {
                do {
                    return (byte)n3;
                    break;
                } while (true);
            }
            n3 = this.get(1, n3);
            return (byte)n3;
        }

        public void set(byte by) {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 8) {
                Struct.this.getByteBuffer().put(n2, by);
                return;
            }
            Struct.this.getByteBuffer().put(n2, (byte)this.set(by, 1, Struct.this.getByteBuffer().get(n2)));
        }

        public String toString() {
            return String.valueOf((int)this.get());
        }
    }

    public class UTF8String
    extends Member {
        private final int _length;
        private final UTF8ByteBufferReader _reader;
        private final UTF8ByteBufferWriter _writer;

        public UTF8String(int n2) {
            super(n2 << 3, 1);
            this._writer = new UTF8ByteBufferWriter();
            this._reader = new UTF8ByteBufferReader();
            this._length = n2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public String get() {
            ByteBuffer byteBuffer;
            ByteBuffer byteBuffer2 = byteBuffer = Struct.this.getByteBuffer();
            synchronized (byteBuffer2) {
                int n2;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    byteBuffer.position(Struct.this.getByteBufferPosition() + this.offset());
                    this._reader.setInput(byteBuffer);
                    n2 = 0;
                }
                catch (IOException iOException) {
                    throw new Error(iOException.getMessage());
                }
                while (n2 < this._length) {
                    char c2 = (char)this._reader.read();
                    if (c2 == '\u0000') {
                        return stringBuilder.toString();
                    }
                    stringBuilder.append(c2);
                    ++n2;
                }
                return stringBuilder.toString();
                finally {
                    this._reader.reset();
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void set(String string) {
            ByteBuffer byteBuffer;
            ByteBuffer byteBuffer2 = byteBuffer = Struct.this.getByteBuffer();
            synchronized (byteBuffer2) {
                block11 : {
                    block12 : {
                        try {
                            byteBuffer.position(Struct.this.getByteBufferPosition() + this.offset());
                            this._writer.setOutput(byteBuffer);
                            if (string.length() < this._length) {
                                this._writer.write(string);
                                this._writer.write(0);
                                break block11;
                            }
                            if (string.length() <= this._length) break block12;
                            this._writer.write(string.substring(0, this._length));
                            break block11;
                        }
                        catch (IOException iOException) {
                            throw new Error(iOException.getMessage());
                        }
                    }
                    this._writer.write(string);
                }
                return;
                finally {
                    this._writer.reset();
                }
            }
        }

        public String toString() {
            return this.get();
        }
    }

    public class Unsigned16
    extends Member {
        public Unsigned16() {
            super(16, 2);
        }

        public Unsigned16(int n2) {
            super(n2, 2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public int get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().getShort(n2);
            if (this.bitLength() == 16) {
                do {
                    return n3 & 65535;
                    break;
                } while (true);
            }
            n3 = this.get(2, n3);
            return n3 & 65535;
        }

        public void set(int n2) {
            int n3 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 16) {
                Struct.this.getByteBuffer().putShort(n3, (short)n2);
                return;
            }
            Struct.this.getByteBuffer().putShort(n3, (short)this.set(n2, 2, Struct.this.getByteBuffer().getShort(n3)));
        }

        public String toString() {
            return String.valueOf((int)this.get());
        }
    }

    public class Unsigned32
    extends Member {
        public Unsigned32() {
            super(32, 4);
        }

        public Unsigned32(int n2) {
            super(n2, 4);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public long get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().getInt(n2);
            if (this.bitLength() == 32) {
                do {
                    return 0xFFFFFFFFL & (long)n3;
                    break;
                } while (true);
            }
            n3 = this.get(4, n3);
            return 0xFFFFFFFFL & (long)n3;
        }

        public void set(long l2) {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 32) {
                Struct.this.getByteBuffer().putInt(n2, (int)l2);
                return;
            }
            Struct.this.getByteBuffer().putInt(n2, this.set((int)l2, 4, Struct.this.getByteBuffer().getInt(n2)));
        }

        public String toString() {
            return String.valueOf((long)this.get());
        }
    }

    public class Unsigned8
    extends Member {
        public Unsigned8() {
            super(8, 1);
        }

        public Unsigned8(int n2) {
            super(n2, 1);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public short get() {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            int n3 = Struct.this.getByteBuffer().get(n2);
            if (this.bitLength() == 8) {
                do {
                    return (short)(n3 & 255);
                    break;
                } while (true);
            }
            n3 = this.get(1, n3);
            return (short)(n3 & 255);
        }

        public void set(short s2) {
            int n2 = Struct.this.getByteBufferPosition() + this.offset();
            if (this.bitLength() == 8) {
                Struct.this.getByteBuffer().put(n2, (byte)s2);
                return;
            }
            Struct.this.getByteBuffer().put(n2, (byte)this.set(s2, 1, Struct.this.getByteBuffer().get(n2)));
        }

        public String toString() {
            return String.valueOf((int)this.get());
        }
    }

}

