/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.math.BigInteger
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.OIDTokenizer;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;

public class ASN1ObjectIdentifier
extends ASN1Primitive {
    private static final long LONG_LIMIT = 72057594037927808L;
    private static ASN1ObjectIdentifier[][] cache = new ASN1ObjectIdentifier[256][];
    private byte[] body;
    String identifier;

    public ASN1ObjectIdentifier(String string) {
        if (string == null) {
            throw new IllegalArgumentException("'identifier' cannot be null");
        }
        if (!ASN1ObjectIdentifier.isValidIdentifier(string)) {
            throw new IllegalArgumentException("string " + string + " not an OID");
        }
        this.identifier = string;
    }

    ASN1ObjectIdentifier(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        if (!ASN1ObjectIdentifier.isValidBranchID(string, 0)) {
            throw new IllegalArgumentException("string " + string + " not a valid OID branch");
        }
        this.identifier = aSN1ObjectIdentifier.getId() + "." + string;
    }

    /*
     * Enabled aggressive block sorting
     */
    ASN1ObjectIdentifier(byte[] arrby) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = true;
        BigInteger bigInteger = null;
        long l2 = 0L;
        int n2 = 0;
        do {
            if (n2 == arrby.length) {
                this.identifier = stringBuffer.toString();
                this.body = Arrays.clone((byte[])arrby);
                return;
            }
            int n3 = 255 & arrby[n2];
            if (l2 <= 72057594037927808L) {
                long l3 = l2 + (long)(n3 & 127);
                if ((n3 & 128) == 0) {
                    if (bl) {
                        if (l3 < 40L) {
                            stringBuffer.append('0');
                        } else if (l3 < 80L) {
                            stringBuffer.append('1');
                            l3 -= 40L;
                        } else {
                            stringBuffer.append('2');
                            l3 -= 80L;
                        }
                        bl = false;
                    }
                    stringBuffer.append('.');
                    stringBuffer.append(l3);
                    l2 = 0L;
                } else {
                    l2 = l3 << 7;
                }
            } else {
                if (bigInteger == null) {
                    bigInteger = BigInteger.valueOf((long)l2);
                }
                BigInteger bigInteger2 = bigInteger.or(BigInteger.valueOf((long)(n3 & 127)));
                if ((n3 & 128) == 0) {
                    BigInteger bigInteger3;
                    boolean bl2;
                    if (bl) {
                        stringBuffer.append('2');
                        bigInteger3 = bigInteger2.subtract(BigInteger.valueOf((long)80L));
                        bl2 = false;
                    } else {
                        boolean bl3 = bl;
                        bigInteger3 = bigInteger2;
                        bl2 = bl3;
                    }
                    stringBuffer.append('.');
                    stringBuffer.append((Object)bigInteger3);
                    l2 = 0L;
                    bl = bl2;
                    bigInteger = null;
                } else {
                    bigInteger = bigInteger2.shiftLeft(7);
                }
            }
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void doOutput(ByteArrayOutputStream byteArrayOutputStream) {
        OIDTokenizer oIDTokenizer = new OIDTokenizer(this.identifier);
        int n2 = 40 * Integer.parseInt((String)oIDTokenizer.nextToken());
        String string = oIDTokenizer.nextToken();
        if (string.length() <= 18) {
            this.writeField(byteArrayOutputStream, (long)n2 + Long.parseLong((String)string));
        } else {
            this.writeField(byteArrayOutputStream, new BigInteger(string).add(BigInteger.valueOf((long)n2)));
        }
        while (oIDTokenizer.hasMoreTokens()) {
            String string2 = oIDTokenizer.nextToken();
            if (string2.length() <= 18) {
                this.writeField(byteArrayOutputStream, Long.parseLong((String)string2));
                continue;
            }
            this.writeField(byteArrayOutputStream, new BigInteger(string2));
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static ASN1ObjectIdentifier fromOctetString(byte[] arrby) {
        ASN1ObjectIdentifier[][] arraSN1ObjectIdentifier;
        ASN1ObjectIdentifier aSN1ObjectIdentifier;
        if (arrby.length < 3) {
            return new ASN1ObjectIdentifier(arrby);
        }
        int n2 = 255 & arrby[-2 + arrby.length];
        int n3 = 127 & arrby[-1 + arrby.length];
        ASN1ObjectIdentifier[][] arraSN1ObjectIdentifier2 = arraSN1ObjectIdentifier = cache;
        synchronized (arraSN1ObjectIdentifier2) {
            ASN1ObjectIdentifier[] arraSN1ObjectIdentifier3;
            ASN1ObjectIdentifier[] arraSN1ObjectIdentifier4;
            ASN1ObjectIdentifier aSN1ObjectIdentifier2;
            ASN1ObjectIdentifier aSN1ObjectIdentifier3;
            ASN1ObjectIdentifier[] arraSN1ObjectIdentifier5 = cache[n2];
            if (arraSN1ObjectIdentifier5 == null) {
                ASN1ObjectIdentifier[][] arraSN1ObjectIdentifier6 = cache;
                ASN1ObjectIdentifier[] arraSN1ObjectIdentifier7 = new ASN1ObjectIdentifier[128];
                arraSN1ObjectIdentifier6[n2] = arraSN1ObjectIdentifier7;
                arraSN1ObjectIdentifier4 = arraSN1ObjectIdentifier7;
            } else {
                arraSN1ObjectIdentifier4 = arraSN1ObjectIdentifier5;
            }
            if ((aSN1ObjectIdentifier3 = arraSN1ObjectIdentifier4[n3]) == null) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier4;
                arraSN1ObjectIdentifier4[n3] = aSN1ObjectIdentifier4 = new ASN1ObjectIdentifier(arrby);
                return aSN1ObjectIdentifier4;
            }
            if (Arrays.areEqual((byte[])arrby, (byte[])aSN1ObjectIdentifier3.getBody())) {
                return aSN1ObjectIdentifier3;
            }
            int n4 = 255 & n2 + 1;
            ASN1ObjectIdentifier[] arraSN1ObjectIdentifier8 = cache[n4];
            if (arraSN1ObjectIdentifier8 == null) {
                ASN1ObjectIdentifier[][] arraSN1ObjectIdentifier9 = cache;
                ASN1ObjectIdentifier[] arraSN1ObjectIdentifier10 = new ASN1ObjectIdentifier[128];
                arraSN1ObjectIdentifier9[n4] = arraSN1ObjectIdentifier10;
                arraSN1ObjectIdentifier3 = arraSN1ObjectIdentifier10;
            } else {
                arraSN1ObjectIdentifier3 = arraSN1ObjectIdentifier8;
            }
            if ((aSN1ObjectIdentifier2 = arraSN1ObjectIdentifier3[n3]) == null) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier5;
                arraSN1ObjectIdentifier3[n3] = aSN1ObjectIdentifier5 = new ASN1ObjectIdentifier(arrby);
                return aSN1ObjectIdentifier5;
            }
            if (Arrays.areEqual((byte[])arrby, (byte[])aSN1ObjectIdentifier2.getBody())) {
                return aSN1ObjectIdentifier2;
            }
            int n5 = 127 & n3 + 1;
            aSN1ObjectIdentifier = arraSN1ObjectIdentifier3[n5];
            if (aSN1ObjectIdentifier == null) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier6;
                arraSN1ObjectIdentifier3[n5] = aSN1ObjectIdentifier6 = new ASN1ObjectIdentifier(arrby);
                return aSN1ObjectIdentifier6;
            }
        }
        if (Arrays.areEqual((byte[])arrby, (byte[])aSN1ObjectIdentifier.getBody())) return aSN1ObjectIdentifier;
        return new ASN1ObjectIdentifier(arrby);
    }

    public static ASN1ObjectIdentifier getInstance(Object object) {
        if (object == null || object instanceof ASN1ObjectIdentifier) {
            return (ASN1ObjectIdentifier)object;
        }
        if (object instanceof ASN1Encodable && ((ASN1Encodable)object).toASN1Primitive() instanceof ASN1ObjectIdentifier) {
            return (ASN1ObjectIdentifier)((ASN1Encodable)object).toASN1Primitive();
        }
        if (object instanceof byte[]) {
            byte[] arrby = (byte[])object;
            try {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)ASN1ObjectIdentifier.fromByteArray(arrby);
                return aSN1ObjectIdentifier;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct object identifier from byte[]: " + iOException.getMessage());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1ObjectIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof ASN1ObjectIdentifier) {
            return ASN1ObjectIdentifier.getInstance(aSN1Primitive);
        }
        return ASN1ObjectIdentifier.fromOctetString(ASN1OctetString.getInstance(aSN1TaggedObject.getObject()).getOctets());
    }

    private static boolean isValidBranchID(String string, int n2) {
        int n3 = string.length();
        boolean bl = false;
        while (--n3 >= n2) {
            char c2 = string.charAt(n3);
            if ('0' <= c2 && c2 <= '9') {
                bl = true;
                continue;
            }
            if (c2 != '.' || !bl) {
                return false;
            }
            bl = false;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean isValidIdentifier(String string) {
        char c2;
        if (string.length() < 3 || string.charAt(1) != '.' || (c2 = string.charAt(0)) < '0' || c2 > '2') {
            return false;
        }
        return ASN1ObjectIdentifier.isValidBranchID(string, 2);
    }

    private void writeField(ByteArrayOutputStream byteArrayOutputStream, long l2) {
        byte[] arrby = new byte[9];
        int n2 = 8;
        arrby[n2] = (byte)(127 & (int)l2);
        while (l2 >= 128L) {
            arrby[--n2] = (byte)(128 | 127 & (int)(l2 >>= 7));
        }
        byteArrayOutputStream.write(arrby, n2, 9 - n2);
    }

    private void writeField(ByteArrayOutputStream byteArrayOutputStream, BigInteger bigInteger) {
        int n2 = (6 + bigInteger.bitLength()) / 7;
        if (n2 == 0) {
            byteArrayOutputStream.write(0);
            return;
        }
        byte[] arrby = new byte[n2];
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            arrby[i2] = (byte)(128 | 127 & bigInteger.intValue());
            bigInteger = bigInteger.shiftRight(7);
        }
        int n3 = n2 - 1;
        arrby[n3] = (byte)(127 & arrby[n3]);
        byteArrayOutputStream.write(arrby, 0, arrby.length);
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1ObjectIdentifier)) {
            return false;
        }
        return this.identifier.equals((Object)((ASN1ObjectIdentifier)aSN1Primitive).identifier);
    }

    public ASN1ObjectIdentifier branch(String string) {
        return new ASN1ObjectIdentifier(this, string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        byte[] arrby = this.getBody();
        aSN1OutputStream.write(6);
        aSN1OutputStream.writeLength(arrby.length);
        aSN1OutputStream.write(arrby);
    }

    @Override
    int encodedLength() {
        int n2 = this.getBody().length;
        return n2 + (1 + StreamUtil.calculateBodyLength(n2));
    }

    protected byte[] getBody() {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = this;
        synchronized (aSN1ObjectIdentifier) {
            if (this.body == null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                this.doOutput(byteArrayOutputStream);
                this.body = byteArrayOutputStream.toByteArray();
            }
            byte[] arrby = this.body;
            return arrby;
        }
    }

    public String getId() {
        return this.identifier;
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override
    boolean isConstructed() {
        return false;
    }

    public boolean on(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        String string = this.getId();
        String string2 = aSN1ObjectIdentifier.getId();
        return string.length() > string2.length() && string.charAt(string2.length()) == '.' && string.startsWith(string2);
    }

    public String toString() {
        return this.getId();
    }
}

