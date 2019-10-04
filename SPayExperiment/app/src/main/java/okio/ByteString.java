/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.InputStream
 *  java.io.ObjectInputStream
 *  java.io.ObjectOutputStream
 *  java.io.OutputStream
 *  java.io.Serializable
 *  java.lang.AssertionError
 *  java.lang.IllegalAccessException
 *  java.lang.IllegalArgumentException
 *  java.lang.NoSuchFieldException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Field
 *  java.nio.charset.Charset
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.util.Arrays
 */
package okio;

import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import okio.Base64;
import okio.Buffer;
import okio.Util;

public class ByteString
implements Serializable {
    public static final ByteString EMPTY;
    static final char[] HEX_DIGITS;
    private static final long serialVersionUID = 1L;
    final byte[] data;
    transient int hashCode;
    transient String utf8;

    static {
        HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        EMPTY = ByteString.of(new byte[0]);
    }

    ByteString(byte[] arrby) {
        this.data = arrby;
    }

    public static ByteString decodeBase64(String string) {
        if (string == null) {
            throw new IllegalArgumentException("base64 == null");
        }
        byte[] arrby = Base64.decode(string);
        if (arrby != null) {
            return new ByteString(arrby);
        }
        return null;
    }

    public static ByteString decodeHex(String string) {
        if (string == null) {
            throw new IllegalArgumentException("hex == null");
        }
        if (string.length() % 2 != 0) {
            throw new IllegalArgumentException("Unexpected hex string: " + string);
        }
        byte[] arrby = new byte[string.length() / 2];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)((ByteString.decodeHexDigit(string.charAt(i2 * 2)) << 4) + ByteString.decodeHexDigit(string.charAt(1 + i2 * 2)));
        }
        return ByteString.of(arrby);
    }

    private static int decodeHexDigit(char c2) {
        if (c2 >= '0' && c2 <= '9') {
            return c2 - 48;
        }
        if (c2 >= 'a' && c2 <= 'f') {
            return 10 + (c2 - 97);
        }
        if (c2 >= 'A' && c2 <= 'F') {
            return 10 + (c2 - 65);
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c2);
    }

    private ByteString digest(String string) {
        try {
            ByteString byteString = ByteString.of(MessageDigest.getInstance((String)string).digest(this.data));
            return byteString;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
    }

    public static ByteString encodeUtf8(String string) {
        if (string == null) {
            throw new IllegalArgumentException("s == null");
        }
        ByteString byteString = new ByteString(string.getBytes(Util.UTF_8));
        byteString.utf8 = string;
        return byteString;
    }

    public static /* varargs */ ByteString of(byte ... arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("data == null");
        }
        return new ByteString((byte[])arrby.clone());
    }

    public static ByteString of(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            throw new IllegalArgumentException("data == null");
        }
        Util.checkOffsetAndCount(arrby.length, n2, n3);
        byte[] arrby2 = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
        return new ByteString(arrby2);
    }

    public static ByteString read(InputStream inputStream, int n2) {
        int n3;
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + n2);
        }
        byte[] arrby = new byte[n2];
        for (int i2 = 0; i2 < n2; i2 += n3) {
            n3 = inputStream.read(arrby, i2, n2 - i2);
            if (n3 != -1) continue;
            throw new EOFException();
        }
        return new ByteString(arrby);
    }

    private void readObject(ObjectInputStream objectInputStream) {
        ByteString byteString = ByteString.read((InputStream)objectInputStream, objectInputStream.readInt());
        try {
            Field field = ByteString.class.getDeclaredField("data");
            field.setAccessible(true);
            field.set((Object)this, (Object)byteString.data);
            return;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            throw new AssertionError();
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeInt(this.data.length);
        objectOutputStream.write(this.data);
    }

    public String base64() {
        return Base64.encode(this.data);
    }

    public String base64Url() {
        return Base64.encodeUrl(this.data);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ByteString)) return false;
        if (((ByteString)object).size() != this.data.length) return false;
        if (!((ByteString)object).rangeEquals(0, this.data, 0, this.data.length)) return false;
        return true;
    }

    public byte getByte(int n2) {
        return this.data[n2];
    }

    public int hashCode() {
        int n2;
        int n3 = this.hashCode;
        if (n3 != 0) {
            return n3;
        }
        this.hashCode = n2 = Arrays.hashCode((byte[])this.data);
        return n2;
    }

    public String hex() {
        char[] arrc = new char[2 * this.data.length];
        byte[] arrby = this.data;
        int n2 = arrby.length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = arrby[i2];
            int n4 = n3 + 1;
            arrc[n3] = HEX_DIGITS[15 & by >> 4];
            n3 = n4 + 1;
            arrc[n4] = HEX_DIGITS[by & 15];
        }
        return new String(arrc);
    }

    public ByteString md5() {
        return this.digest("MD5");
    }

    public boolean rangeEquals(int n2, ByteString byteString, int n3, int n4) {
        return byteString.rangeEquals(n3, this.data, n2, n4);
    }

    public boolean rangeEquals(int n2, byte[] arrby, int n3, int n4) {
        return n2 <= this.data.length - n4 && n3 <= arrby.length - n4 && Util.arrayRangeEquals(this.data, n2, arrby, n3, n4);
    }

    public ByteString sha256() {
        return this.digest("SHA-256");
    }

    public int size() {
        return this.data.length;
    }

    public ByteString substring(int n2) {
        return this.substring(n2, this.data.length);
    }

    public ByteString substring(int n2, int n3) {
        if (n2 < 0) {
            throw new IllegalArgumentException("beginIndex < 0");
        }
        if (n3 > this.data.length) {
            throw new IllegalArgumentException("endIndex > length(" + this.data.length + ")");
        }
        int n4 = n3 - n2;
        if (n4 < 0) {
            throw new IllegalArgumentException("endIndex < beginIndex");
        }
        if (n2 == 0 && n3 == this.data.length) {
            return this;
        }
        byte[] arrby = new byte[n4];
        System.arraycopy((Object)this.data, (int)n2, (Object)arrby, (int)0, (int)n4);
        return new ByteString(arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ByteString toAsciiLowercase() {
        int n2;
        byte by;
        block4 : {
            n2 = 0;
            while (n2 < this.data.length) {
                by = this.data[n2];
                if (by < 65 || by > 90) {
                    ++n2;
                    continue;
                }
                break block4;
            }
            return this;
        }
        byte[] arrby = (byte[])this.data.clone();
        int n3 = n2 + 1;
        arrby[n2] = (byte)(by + 32);
        int n4 = n3;
        while (n4 < arrby.length) {
            byte by2 = arrby[n4];
            if (by2 >= 65 && by2 <= 90) {
                arrby[n4] = (byte)(by2 + 32);
            }
            ++n4;
        }
        return new ByteString(arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ByteString toAsciiUppercase() {
        int n2;
        byte by;
        block4 : {
            n2 = 0;
            while (n2 < this.data.length) {
                by = this.data[n2];
                if (by < 97 || by > 122) {
                    ++n2;
                    continue;
                }
                break block4;
            }
            return this;
        }
        byte[] arrby = (byte[])this.data.clone();
        int n3 = n2 + 1;
        arrby[n2] = (byte)(by - 32);
        int n4 = n3;
        while (n4 < arrby.length) {
            byte by2 = arrby[n4];
            if (by2 >= 97 && by2 <= 122) {
                arrby[n4] = (byte)(by2 - 32);
            }
            ++n4;
        }
        return new ByteString(arrby);
    }

    public byte[] toByteArray() {
        return (byte[])this.data.clone();
    }

    public String toString() {
        if (this.data.length == 0) {
            return "ByteString[size=0]";
        }
        if (this.data.length <= 16) {
            Object[] arrobject = new Object[]{this.data.length, this.hex()};
            return String.format((String)"ByteString[size=%s data=%s]", (Object[])arrobject);
        }
        Object[] arrobject = new Object[]{this.data.length, this.md5().hex()};
        return String.format((String)"ByteString[size=%s md5=%s]", (Object[])arrobject);
    }

    public String utf8() {
        String string;
        String string2 = this.utf8;
        if (string2 != null) {
            return string2;
        }
        this.utf8 = string = new String(this.data, Util.UTF_8);
        return string;
    }

    public void write(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        outputStream.write(this.data);
    }

    void write(Buffer buffer) {
        buffer.write(this.data, 0, this.data.length);
    }
}

