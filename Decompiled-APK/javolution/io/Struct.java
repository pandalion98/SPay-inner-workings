package javolution.io;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

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
    protected int _alignment;
    private int _bitsUsed;
    private ByteBuffer _byteBuffer;
    private byte[] _bytes;
    private int _index;
    private int _length;
    Map<String, Class> _nameToClass;
    protected Struct _outer;
    protected int _outerOffset;
    private boolean _resetIndex;
    private int _wordSize;

    protected class Member {
        private final int _bitIndex;
        private final int _bitLength;
        private final int _offset;

        protected Member(int i, int i2) {
            this._bitLength = i;
            if (Struct.this._resetIndex) {
                Struct.this._index = 0;
            }
            if (i2 == 0 || (i != 0 && i2 == Struct.this._wordSize && Struct.this._bitsUsed + i <= (i2 << 3))) {
                this._offset = Struct.this._index - Struct.this._wordSize;
                this._bitIndex = Struct.this._bitsUsed;
                Struct.this._bitsUsed = Struct.this._bitsUsed + i;
                while (Struct.this._bitsUsed > (Struct.this._wordSize << 3)) {
                    Struct.this._index = Struct.this._index + 1;
                    Struct.this._wordSize = Struct.this._wordSize + 1;
                    Struct.this._length = Struct.max(Struct.this._length, Struct.this._index);
                }
                return;
            }
            if (!Struct.this.isPacked()) {
                if (Struct.this._alignment < i2) {
                    Struct.this._alignment = i2;
                }
                int access$100 = Struct.this._index % i2;
                if (access$100 != 0) {
                    Struct.this._index = (i2 - access$100) + Struct.this._index;
                }
            }
            this._offset = Struct.this._index;
            this._bitIndex = 0;
            Struct.this._index = Struct.this._index + Struct.max(i2, (i + 7) >> 3);
            Struct.this._wordSize = i2;
            Struct.this._bitsUsed = i;
            Struct.this._length = Struct.max(Struct.this._length, Struct.this._index);
        }

        public final Struct struct() {
            return Struct.this;
        }

        public final int offset() {
            return this._offset;
        }

        public final int bitIndex() {
            return this._bitIndex;
        }

        public final int bitLength() {
            return this._bitLength;
        }

        final int get(int i, int i2) {
            return (i2 >> (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN ? ((i << 3) - bitIndex()) - bitLength() : bitIndex())) & (-1 >>> (32 - bitLength()));
        }

        final int set(int i, int i2, int i3) {
            int bitIndex = Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN ? ((i2 << 3) - bitIndex()) - bitLength() : bitIndex();
            int bitLength = (-1 >>> (32 - bitLength())) << bitIndex;
            return ((i << bitIndex) & bitLength) | ((bitLength ^ -1) & i3);
        }

        final long get(int i, long j) {
            return (j >> (Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN ? ((i << 3) - bitIndex()) - bitLength() : bitIndex())) & (-1 >>> (64 - bitLength()));
        }

        final long set(long j, int i, long j2) {
            long bitIndex = Struct.this.byteOrder() == ByteOrder.BIG_ENDIAN ? ((i << 3) - bitIndex()) - bitLength() : bitIndex();
            long bitLength = (-1 >>> (64 - bitLength())) << bitIndex;
            return ((j << bitIndex) & bitLength) | ((-1 ^ bitLength) & j2);
        }
    }

    public class Bool extends Member {
        public Bool() {
            super(8, 1);
        }

        public Bool(int i) {
            super(i, 1);
        }

        public boolean get() {
            byte b = Struct.this.getByteBuffer().get(Struct.this.getByteBufferPosition() + offset());
            if (bitLength() != 8) {
                b = get(1, (int) b);
            }
            if (b != null) {
                return true;
            }
            return false;
        }

        public void set(boolean z) {
            int i = -1;
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 8) {
                ByteBuffer byteBuffer = Struct.this.getByteBuffer();
                if (!z) {
                    i = 0;
                }
                byteBuffer.put(byteBufferPosition, (byte) i);
                return;
            }
            byteBuffer = Struct.this.getByteBuffer();
            if (!z) {
                i = 0;
            }
            byteBuffer.put(byteBufferPosition, (byte) set(i, 1, (int) Struct.this.getByteBuffer().get(byteBufferPosition)));
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Enum16<T extends Enum> extends Member {
        private final Enum[] _values;

        public Enum16(Enum[] enumArr) {
            super(16, 2);
            this._values = enumArr;
        }

        public Enum16(Enum[] enumArr, int i) {
            super(i, 2);
            this._values = enumArr;
        }

        public Enum get() {
            return this._values[get(2, (int) Struct.this.getByteBuffer().getShort(Struct.this.getByteBufferPosition() + offset())) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE];
        }

        public void set(Enum enumR) {
            int ordinal = enumR.ordinal();
            if (this._values[ordinal] != enumR) {
                throw new IllegalArgumentException("enum: " + enumR + ", ordinal value does not reflect enum values position");
            }
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            Struct.this.getByteBuffer().putShort(byteBufferPosition, (short) set(ordinal, 2, (int) Struct.this.getByteBuffer().getShort(byteBufferPosition)));
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Enum32<T extends Enum> extends Member {
        private final Enum[] _values;

        public Enum32(Enum[] enumArr) {
            super(32, 4);
            this._values = enumArr;
        }

        public Enum32(Enum[] enumArr, int i) {
            super(i, 4);
            this._values = enumArr;
        }

        public Enum get() {
            return this._values[get(4, Struct.this.getByteBuffer().getInt(Struct.this.getByteBufferPosition() + offset()))];
        }

        public void set(Enum enumR) {
            int ordinal = enumR.ordinal();
            if (this._values[ordinal] != enumR) {
                throw new IllegalArgumentException("enum: " + enumR + ", ordinal value does not reflect enum values position");
            }
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            Struct.this.getByteBuffer().putInt(byteBufferPosition, set(ordinal, 4, Struct.this.getByteBuffer().getInt(byteBufferPosition)));
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Enum64<T extends Enum> extends Member {
        private final Enum[] _values;

        public Enum64(Enum[] enumArr) {
            super(64, 8);
            this._values = enumArr;
        }

        public Enum64(Enum[] enumArr, int i) {
            super(i, 8);
            this._values = enumArr;
        }

        public Enum get() {
            return this._values[(int) get(8, Struct.this.getByteBuffer().getLong(Struct.this.getByteBufferPosition() + offset()))];
        }

        public void set(Enum enumR) {
            long ordinal = (long) enumR.ordinal();
            if (this._values[(int) ordinal] != enumR) {
                throw new IllegalArgumentException("enum: " + enumR + ", ordinal value does not reflect enum values position");
            }
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            Struct.this.getByteBuffer().putLong(byteBufferPosition, set(ordinal, 8, Struct.this.getByteBuffer().getLong(byteBufferPosition)));
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Enum8<T extends Enum> extends Member {
        private final Enum[] _values;

        public Enum8(Enum[] enumArr) {
            super(8, 1);
            this._values = enumArr;
        }

        public Enum8(Enum[] enumArr, int i) {
            super(i, 1);
            this._values = enumArr;
        }

        public Enum get() {
            return this._values[get(1, (int) Struct.this.getByteBuffer().get(Struct.this.getByteBufferPosition() + offset())) & GF2Field.MASK];
        }

        public void set(Enum enumR) {
            int ordinal = enumR.ordinal();
            if (this._values[ordinal] != enumR) {
                throw new IllegalArgumentException("enum: " + enumR + ", ordinal value does not reflect enum values position");
            }
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            Struct.this.getByteBuffer().put(byteBufferPosition, (byte) set(ordinal, 1, (int) Struct.this.getByteBuffer().get(byteBufferPosition)));
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Float32 extends Member {
        public Float32() {
            super(32, 4);
        }

        public float get() {
            return Struct.this.getByteBuffer().getFloat(Struct.this.getByteBufferPosition() + offset());
        }

        public void set(float f) {
            Struct.this.getByteBuffer().putFloat(Struct.this.getByteBufferPosition() + offset(), f);
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Float64 extends Member {
        public Float64() {
            super(64, 8);
        }

        public double get() {
            return Struct.this.getByteBuffer().getDouble(Struct.this.getByteBufferPosition() + offset());
        }

        public void set(double d) {
            Struct.this.getByteBuffer().putDouble(Struct.this.getByteBufferPosition() + offset(), d);
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Signed16 extends Member {
        public Signed16() {
            super(16, 2);
        }

        public Signed16(int i) {
            super(i, 2);
        }

        public short get() {
            int i = Struct.this.getByteBuffer().getShort(Struct.this.getByteBufferPosition() + offset());
            if (bitLength() != 16) {
                i = get(2, i);
            }
            return (short) i;
        }

        public void set(short s) {
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 16) {
                Struct.this.getByteBuffer().putShort(byteBufferPosition, s);
            } else {
                Struct.this.getByteBuffer().putShort(byteBufferPosition, (short) set((int) s, 2, (int) Struct.this.getByteBuffer().getShort(byteBufferPosition)));
            }
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Signed32 extends Member {
        public Signed32() {
            super(32, 4);
        }

        public Signed32(int i) {
            super(i, 4);
        }

        public int get() {
            int i = Struct.this.getByteBuffer().getInt(Struct.this.getByteBufferPosition() + offset());
            return bitLength() == 32 ? i : get(4, i);
        }

        public void set(int i) {
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 32) {
                Struct.this.getByteBuffer().putInt(byteBufferPosition, i);
            } else {
                Struct.this.getByteBuffer().putInt(byteBufferPosition, set(i, 4, Struct.this.getByteBuffer().getInt(byteBufferPosition)));
            }
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Signed64 extends Member {
        public Signed64() {
            super(64, 8);
        }

        public Signed64(int i) {
            super(i, 8);
        }

        public long get() {
            long j = Struct.this.getByteBuffer().getLong(Struct.this.getByteBufferPosition() + offset());
            return bitLength() == 64 ? j : get(8, j);
        }

        public void set(long j) {
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 64) {
                Struct.this.getByteBuffer().putLong(byteBufferPosition, j);
                return;
            }
            Struct.this.getByteBuffer().putLong(byteBufferPosition, set(j, 8, Struct.this.getByteBuffer().getLong(byteBufferPosition)));
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Signed8 extends Member {
        public Signed8() {
            super(8, 1);
        }

        public Signed8(int i) {
            super(i, 1);
        }

        public byte get() {
            int i = Struct.this.getByteBuffer().get(Struct.this.getByteBufferPosition() + offset());
            if (bitLength() != 8) {
                i = get(1, i);
            }
            return (byte) i;
        }

        public void set(byte b) {
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 8) {
                Struct.this.getByteBuffer().put(byteBufferPosition, b);
            } else {
                Struct.this.getByteBuffer().put(byteBufferPosition, (byte) set((int) b, 1, (int) Struct.this.getByteBuffer().get(byteBufferPosition)));
            }
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class UTF8String extends Member {
        private final int _length;
        private final UTF8ByteBufferReader _reader;
        private final UTF8ByteBufferWriter _writer;

        public UTF8String(int i) {
            super(i << 3, 1);
            this._writer = new UTF8ByteBufferWriter();
            this._reader = new UTF8ByteBufferReader();
            this._length = i;
        }

        public void set(String str) {
            ByteBuffer byteBuffer = Struct.this.getByteBuffer();
            synchronized (byteBuffer) {
                try {
                    byteBuffer.position(Struct.this.getByteBufferPosition() + offset());
                    this._writer.setOutput(byteBuffer);
                    if (str.length() < this._length) {
                        this._writer.write(str);
                        this._writer.write(0);
                    } else if (str.length() > this._length) {
                        this._writer.write(str.substring(0, this._length));
                    } else {
                        this._writer.write(str);
                    }
                    this._writer.reset();
                } catch (IOException e) {
                    throw new Error(e.getMessage());
                } catch (Throwable th) {
                    this._writer.reset();
                }
            }
        }

        public String get() {
            String stringBuilder;
            ByteBuffer byteBuffer = Struct.this.getByteBuffer();
            synchronized (byteBuffer) {
                StringBuilder stringBuilder2 = new StringBuilder();
                try {
                    byteBuffer.position(Struct.this.getByteBufferPosition() + offset());
                    this._reader.setInput(byteBuffer);
                    int i = 0;
                    while (i < this._length) {
                        char read = (char) this._reader.read();
                        if (read == '\u0000') {
                            stringBuilder = stringBuilder2.toString();
                            this._reader.reset();
                        } else {
                            stringBuilder2.append(read);
                            i++;
                        }
                    }
                    stringBuilder = stringBuilder2.toString();
                    this._reader.reset();
                } catch (IOException e) {
                    throw new Error(e.getMessage());
                } catch (Throwable th) {
                    this._reader.reset();
                }
            }
            return stringBuilder;
        }

        public String toString() {
            return get();
        }
    }

    public class Unsigned16 extends Member {
        public Unsigned16() {
            super(16, 2);
        }

        public Unsigned16(int i) {
            super(i, 2);
        }

        public int get() {
            int i = Struct.this.getByteBuffer().getShort(Struct.this.getByteBufferPosition() + offset());
            if (bitLength() != 16) {
                i = get(2, i);
            }
            return i & HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
        }

        public void set(int i) {
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 16) {
                Struct.this.getByteBuffer().putShort(byteBufferPosition, (short) i);
            } else {
                Struct.this.getByteBuffer().putShort(byteBufferPosition, (short) set(i, 2, (int) Struct.this.getByteBuffer().getShort(byteBufferPosition)));
            }
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Unsigned32 extends Member {
        public Unsigned32() {
            super(32, 4);
        }

        public Unsigned32(int i) {
            super(i, 4);
        }

        public long get() {
            int i = Struct.this.getByteBuffer().getInt(Struct.this.getByteBufferPosition() + offset());
            if (bitLength() != 32) {
                i = get(4, i);
            }
            return ((long) i) & 4294967295L;
        }

        public void set(long j) {
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 32) {
                Struct.this.getByteBuffer().putInt(byteBufferPosition, (int) j);
            } else {
                Struct.this.getByteBuffer().putInt(byteBufferPosition, set((int) j, 4, Struct.this.getByteBuffer().getInt(byteBufferPosition)));
            }
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public class Unsigned8 extends Member {
        public Unsigned8() {
            super(8, 1);
        }

        public Unsigned8(int i) {
            super(i, 1);
        }

        public short get() {
            int i = Struct.this.getByteBuffer().get(Struct.this.getByteBufferPosition() + offset());
            if (bitLength() != 8) {
                i = get(1, i);
            }
            return (short) (i & GF2Field.MASK);
        }

        public void set(short s) {
            int byteBufferPosition = Struct.this.getByteBufferPosition() + offset();
            if (bitLength() == 8) {
                Struct.this.getByteBuffer().put(byteBufferPosition, (byte) s);
            } else {
                Struct.this.getByteBuffer().put(byteBufferPosition, (byte) set((int) s, 1, (int) Struct.this.getByteBuffer().get(byteBufferPosition)));
            }
        }

        public String toString() {
            return String.valueOf(get());
        }
    }

    public Struct() {
        this._alignment = 1;
        this._nameToClass = new HashMap();
        this._resetIndex = isUnion();
    }

    public final int size() {
        return this._alignment <= 1 ? this._length : (((this._length + this._alignment) - 1) / this._alignment) * this._alignment;
    }

    public Struct outer() {
        return this._outer;
    }

    public final ByteBuffer getByteBuffer() {
        if (this._outer != null) {
            return this._outer.getByteBuffer();
        }
        return this._byteBuffer != null ? this._byteBuffer : newBuffer();
    }

    private synchronized ByteBuffer newBuffer() {
        ByteBuffer byteBuffer;
        if (this._byteBuffer != null) {
            byteBuffer = this._byteBuffer;
        } else {
            byteBuffer = ByteBuffer.allocateDirect(size());
            byteBuffer.order(byteOrder());
            setByteBuffer(byteBuffer, 0);
            byteBuffer = this._byteBuffer;
        }
        return byteBuffer;
    }

    public final Struct setByteBuffer(ByteBuffer byteBuffer, int i) {
        if (byteBuffer.order() != byteOrder()) {
            throw new IllegalArgumentException("The byte order of the specified byte buffer is different from this struct byte order");
        } else if (this._outer != null) {
            throw new UnsupportedOperationException("Inner struct byte buffer is inherited from outer");
        } else {
            this._byteBuffer = byteBuffer;
            this._outerOffset = i;
            return this;
        }
    }

    public final Struct setByteBufferPosition(int i) {
        return setByteBuffer(getByteBuffer(), i);
    }

    public final int getByteBufferPosition() {
        return this._outer != null ? this._outer.getByteBufferPosition() + this._outerOffset : this._outerOffset;
    }

    public int read(InputStream inputStream) {
        ByteBuffer byteBuffer = getByteBuffer();
        if (byteBuffer.hasArray()) {
            return inputStream.read(byteBuffer.array(), byteBuffer.arrayOffset() + getByteBufferPosition(), size());
        }
        int read;
        synchronized (byteBuffer) {
            if (this._bytes == null) {
                this._bytes = new byte[size()];
            }
            read = inputStream.read(this._bytes);
            byteBuffer.position(getByteBufferPosition());
            byteBuffer.put(this._bytes);
        }
        return read;
    }

    public void write(OutputStream outputStream) {
        ByteBuffer byteBuffer = getByteBuffer();
        if (byteBuffer.hasArray()) {
            outputStream.write(byteBuffer.array(), byteBuffer.arrayOffset() + getByteBufferPosition(), size());
            return;
        }
        synchronized (byteBuffer) {
            if (this._bytes == null) {
                this._bytes = new byte[size()];
            }
            byteBuffer.position(getByteBufferPosition());
            byteBuffer.get(this._bytes);
            outputStream.write(this._bytes);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int size = size();
        ByteBuffer byteBuffer = getByteBuffer();
        int byteBufferPosition = getByteBufferPosition();
        for (int i = 0; i < size; i++) {
            int i2 = byteBuffer.get(byteBufferPosition + i) & GF2Field.MASK;
            stringBuilder.append(HEXA[i2 >> 4]);
            stringBuilder.append(HEXA[i2 & 15]);
            stringBuilder.append((i & 15) == 15 ? '\n' : ' ');
        }
        return stringBuilder.toString();
    }

    static {
        HEXA = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
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

    public boolean isUnion() {
        return false;
    }

    public ByteOrder byteOrder() {
        return this._outer != null ? this._outer.byteOrder() : ByteOrder.BIG_ENDIAN;
    }

    public boolean isPacked() {
        return false;
    }

    protected <S extends Struct> S inner(S s) {
        if (s._outer != null) {
            throw new IllegalArgumentException("struct: Already an inner struct");
        }
        Member member = new Member(s.size() << 3, s._alignment);
        s._outer = this;
        s._outerOffset = member.offset();
        return s;
    }

    protected <S extends Struct> S[] array(S[] sArr) {
        Class cls = null;
        boolean z = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        int i = 0;
        while (i < sArr.length) {
            Struct struct = sArr[i];
            if (struct == null) {
                if (cls == null) {
                    try {
                        String name = sArr.getClass().getName();
                        Object substring = name.substring(2, name.length() - 1);
                        cls = getClass(substring);
                        if (cls == null) {
                            throw new IllegalArgumentException("Struct class: " + substring + " not found");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
                struct = (Struct) cls.newInstance();
            }
            int i2 = i + 1;
            sArr[i] = inner(struct);
            i = i2;
        }
        this._resetIndex = z;
        return (Struct[]) sArr;
    }

    protected <S extends Struct> S[][] array(S[][] sArr) {
        int i = 0;
        boolean z = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (i < sArr.length) {
            array(sArr[i]);
            i++;
        }
        this._resetIndex = z;
        return (Struct[][]) sArr;
    }

    protected <S extends Struct> S[][][] array(S[][][] sArr) {
        int i = 0;
        boolean z = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (i < sArr.length) {
            array(sArr[i]);
            i++;
        }
        this._resetIndex = z;
        return (Struct[][][]) sArr;
    }

    protected <M extends Member> M[] array(M[] mArr) {
        int i = 0;
        boolean z = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        int i2;
        if (BOOL.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Bool();
                i = i2;
            }
        } else if (SIGNED_8.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Signed8();
                i = i2;
            }
        } else if (UNSIGNED_8.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Unsigned8();
                i = i2;
            }
        } else if (SIGNED_16.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Signed16();
                i = i2;
            }
        } else if (UNSIGNED_16.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Unsigned16();
                i = i2;
            }
        } else if (SIGNED_32.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Signed32();
                i = i2;
            }
        } else if (UNSIGNED_32.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Unsigned32();
                i = i2;
            }
        } else if (SIGNED_64.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Signed64();
                i = i2;
            }
        } else if (FLOAT_32.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Float32();
                i = i2;
            }
        } else if (FLOAT_64.isInstance(mArr)) {
            while (i < mArr.length) {
                i2 = i + 1;
                mArr[i] = new Float64();
                i = i2;
            }
        } else {
            throw new UnsupportedOperationException("Cannot create member elements, the arrayMember should contain the member instances instead of null");
        }
        this._resetIndex = z;
        return (Member[]) mArr;
    }

    protected <M extends Member> M[][] array(M[][] mArr) {
        int i = 0;
        boolean z = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (i < mArr.length) {
            array(mArr[i]);
            i++;
        }
        this._resetIndex = z;
        return (Member[][]) mArr;
    }

    protected <M extends Member> M[][][] array(M[][][] mArr) {
        int i = 0;
        boolean z = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (i < mArr.length) {
            array(mArr[i]);
            i++;
        }
        this._resetIndex = z;
        return (Member[][][]) mArr;
    }

    protected UTF8String[] array(UTF8String[] uTF8StringArr, int i) {
        int i2 = 0;
        boolean z = this._resetIndex;
        if (this._resetIndex) {
            this._index = 0;
            this._resetIndex = false;
        }
        while (i2 < uTF8StringArr.length) {
            uTF8StringArr[i2] = new UTF8String(i);
            i2++;
        }
        this._resetIndex = z;
        return uTF8StringArr;
    }

    private long readByteBufferLong(int i) {
        ByteBuffer byteBuffer = getByteBuffer();
        if (i + 8 < byteBuffer.limit()) {
            return byteBuffer.getLong(i);
        }
        if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
            int i2 = i + 1;
            i2++;
            i2++;
            i2++;
            i2++;
            i2++;
            return ((((long) readByte(i2 + 1, byteBuffer)) & 255) << 56) + (((((((((long) readByte(i, byteBuffer)) & 255) + ((((long) readByte(i2, byteBuffer)) & 255) << 8)) + ((((long) readByte(i2, byteBuffer)) & 255) << 16)) + ((((long) readByte(i2, byteBuffer)) & 255) << 24)) + ((((long) readByte(i2, byteBuffer)) & 255) << 32)) + ((((long) readByte(i2, byteBuffer)) & 255) << 40)) + ((((long) readByte(i2, byteBuffer)) & 255) << 48));
        }
        i2 = i + 1;
        i2++;
        i2++;
        i2++;
        i2++;
        i2++;
        return (((long) readByte(i2 + 1, byteBuffer)) & 255) + (((((((((long) readByte(i, byteBuffer)) << 56) + ((((long) readByte(i2, byteBuffer)) & 255) << 48)) + ((((long) readByte(i2, byteBuffer)) & 255) << 40)) + ((((long) readByte(i2, byteBuffer)) & 255) << 32)) + ((((long) readByte(i2, byteBuffer)) & 255) << 24)) + ((((long) readByte(i2, byteBuffer)) & 255) << 16)) + ((((long) readByte(i2, byteBuffer)) & 255) << 8));
    }

    private static byte readByte(int i, ByteBuffer byteBuffer) {
        return i < byteBuffer.limit() ? byteBuffer.get(i) : (byte) 0;
    }

    public static int max(int i, int i2) {
        return i >= i2 ? i : i2;
    }

    public Class getClass(CharSequence charSequence) {
        Class cls = (Class) this._nameToClass.get(charSequence);
        return cls != null ? cls : searchClass(charSequence.toString());
    }

    private Class searchClass(String str) {
        Class cls = null;
        try {
            cls = Class.forName(str);
        } catch (ClassNotFoundException e) {
        }
        if (cls != null) {
            this._nameToClass.put(str, cls);
        }
        return cls;
    }
}
