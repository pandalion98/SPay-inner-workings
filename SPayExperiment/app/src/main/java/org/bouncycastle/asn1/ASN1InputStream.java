/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.EOFException
 *  java.io.FilterInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.lang.Throwable
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Exception;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.BERApplicationSpecificParser;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.BEROctetStringParser;
import org.bouncycastle.asn1.BERSequenceParser;
import org.bouncycastle.asn1.BERSetParser;
import org.bouncycastle.asn1.BERTaggedObjectParser;
import org.bouncycastle.asn1.BERTags;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERExternal;
import org.bouncycastle.asn1.DERExternalParser;
import org.bouncycastle.asn1.DERFactory;
import org.bouncycastle.asn1.DERGeneralString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERNumericString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERUniversalString;
import org.bouncycastle.asn1.DERVisibleString;
import org.bouncycastle.asn1.DefiniteLengthInputStream;
import org.bouncycastle.asn1.IndefiniteLengthInputStream;
import org.bouncycastle.asn1.LazyEncodedSequence;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.io.Streams;

public class ASN1InputStream
extends FilterInputStream
implements BERTags {
    private final boolean lazyEvaluate;
    private final int limit;
    private final byte[][] tmpBuffers;

    public ASN1InputStream(InputStream inputStream) {
        this(inputStream, StreamUtil.findLimit(inputStream));
    }

    public ASN1InputStream(InputStream inputStream, int n2) {
        this(inputStream, n2, false);
    }

    public ASN1InputStream(InputStream inputStream, int n2, boolean bl) {
        super(inputStream);
        this.limit = n2;
        this.lazyEvaluate = bl;
        this.tmpBuffers = new byte[11][];
    }

    public ASN1InputStream(InputStream inputStream, boolean bl) {
        this(inputStream, StreamUtil.findLimit(inputStream), bl);
    }

    public ASN1InputStream(byte[] arrby) {
        this((InputStream)new ByteArrayInputStream(arrby), arrby.length);
    }

    public ASN1InputStream(byte[] arrby, boolean bl) {
        this((InputStream)new ByteArrayInputStream(arrby), arrby.length, bl);
    }

    static ASN1Primitive createPrimitiveDERObject(int n2, DefiniteLengthInputStream definiteLengthInputStream, byte[][] arrby) {
        switch (n2) {
            default: {
                throw new IOException("unknown tag " + n2 + " encountered");
            }
            case 3: {
                return DERBitString.fromInputStream(definiteLengthInputStream.getRemaining(), definiteLengthInputStream);
            }
            case 30: {
                return new DERBMPString(ASN1InputStream.getBMPCharBuffer(definiteLengthInputStream));
            }
            case 1: {
                return ASN1Boolean.fromOctetString(ASN1InputStream.getBuffer(definiteLengthInputStream, arrby));
            }
            case 10: {
                return ASN1Enumerated.fromOctetString(ASN1InputStream.getBuffer(definiteLengthInputStream, arrby));
            }
            case 24: {
                return new ASN1GeneralizedTime(definiteLengthInputStream.toByteArray());
            }
            case 27: {
                return new DERGeneralString(definiteLengthInputStream.toByteArray());
            }
            case 22: {
                return new DERIA5String(definiteLengthInputStream.toByteArray());
            }
            case 2: {
                return new ASN1Integer(definiteLengthInputStream.toByteArray(), false);
            }
            case 5: {
                return DERNull.INSTANCE;
            }
            case 18: {
                return new DERNumericString(definiteLengthInputStream.toByteArray());
            }
            case 6: {
                return ASN1ObjectIdentifier.fromOctetString(ASN1InputStream.getBuffer(definiteLengthInputStream, arrby));
            }
            case 4: {
                return new DEROctetString(definiteLengthInputStream.toByteArray());
            }
            case 19: {
                return new DERPrintableString(definiteLengthInputStream.toByteArray());
            }
            case 20: {
                return new DERT61String(definiteLengthInputStream.toByteArray());
            }
            case 28: {
                return new DERUniversalString(definiteLengthInputStream.toByteArray());
            }
            case 23: {
                return new ASN1UTCTime(definiteLengthInputStream.toByteArray());
            }
            case 12: {
                return new DERUTF8String(definiteLengthInputStream.toByteArray());
            }
            case 26: 
        }
        return new DERVisibleString(definiteLengthInputStream.toByteArray());
    }

    /*
     * Enabled aggressive block sorting
     */
    private static char[] getBMPCharBuffer(DefiniteLengthInputStream definiteLengthInputStream) {
        int n2 = definiteLengthInputStream.getRemaining() / 2;
        char[] arrc = new char[n2];
        int n3 = 0;
        int n4;
        int n5;
        while (n3 < n2 && (n5 = definiteLengthInputStream.read()) >= 0 && (n4 = definiteLengthInputStream.read()) >= 0) {
            int n6 = n3 + 1;
            arrc[n3] = (char)(n5 << 8 | n4 & 255);
            n3 = n6;
        }
        return arrc;
    }

    private static byte[] getBuffer(DefiniteLengthInputStream definiteLengthInputStream, byte[][] arrby) {
        int n2 = definiteLengthInputStream.getRemaining();
        if (definiteLengthInputStream.getRemaining() < arrby.length) {
            byte[] arrby2 = arrby[n2];
            if (arrby2 == null) {
                arrby2 = new byte[n2];
                arrby[n2] = arrby2;
            }
            Streams.readFully((InputStream)definiteLengthInputStream, (byte[])arrby2);
            return arrby2;
        }
        return definiteLengthInputStream.toByteArray();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static int readLength(InputStream inputStream, int n2) {
        int n3 = inputStream.read();
        if (n3 < 0) {
            throw new EOFException("EOF found when length expected");
        }
        if (n3 == 128) {
            return -1;
        }
        if (n3 <= 127) return n3;
        int n4 = n3 & 127;
        if (n4 > 4) {
            throw new IOException("DER length more than 4 bytes: " + n4);
        }
        n3 = 0;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5 = inputStream.read();
            if (n5 < 0) {
                throw new EOFException("EOF found reading length");
            }
            int n6 = n5 + (n3 << 8);
            n3 = n6;
        }
        if (n3 < 0) {
            throw new IOException("corrupted stream - negative length found");
        }
        if (n3 < n2) return n3;
        throw new IOException("corrupted stream - out of bounds length found");
    }

    static int readTagNumber(InputStream inputStream, int n2) {
        int n3 = n2 & 31;
        if (n3 == 31) {
            int n4 = inputStream.read();
            int n5 = n4 & 127;
            int n6 = 0;
            if (n5 == 0) {
                throw new IOException("corrupted stream - invalid high tag number found");
            }
            while (n4 >= 0 && (n4 & 128) != 0) {
                n6 = (n6 | n4 & 127) << 7;
                n4 = inputStream.read();
            }
            if (n4 < 0) {
                throw new EOFException("EOF found inside tag value.");
            }
            n3 = n6 | n4 & 127;
        }
        return n3;
    }

    ASN1EncodableVector buildDEREncodableVector(DefiniteLengthInputStream definiteLengthInputStream) {
        return new ASN1InputStream(definiteLengthInputStream).buildEncodableVector();
    }

    ASN1EncodableVector buildEncodableVector() {
        ASN1Primitive aSN1Primitive;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while ((aSN1Primitive = this.readObject()) != null) {
            aSN1EncodableVector.add(aSN1Primitive);
        }
        return aSN1EncodableVector;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected ASN1Primitive buildObject(int n2, int n3, int n4) {
        int n5 = 0;
        boolean bl = (n2 & 32) != 0;
        DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream((InputStream)this, n4);
        if ((n2 & 64) != 0) {
            return new DERApplicationSpecific(bl, n3, definiteLengthInputStream.toByteArray());
        }
        if ((n2 & 128) != 0) {
            return new ASN1StreamParser(definiteLengthInputStream).readTaggedObject(bl, n3);
        }
        if (!bl) {
            return ASN1InputStream.createPrimitiveDERObject(n3, definiteLengthInputStream, this.tmpBuffers);
        }
        switch (n3) {
            default: {
                throw new IOException("unknown tag " + n3 + " encountered");
            }
            case 4: {
                ASN1EncodableVector aSN1EncodableVector = this.buildDEREncodableVector(definiteLengthInputStream);
                ASN1OctetString[] arraSN1OctetString = new ASN1OctetString[aSN1EncodableVector.size()];
                do {
                    if (n5 == arraSN1OctetString.length) {
                        return new BEROctetString(arraSN1OctetString);
                    }
                    arraSN1OctetString[n5] = (ASN1OctetString)aSN1EncodableVector.get(n5);
                    ++n5;
                } while (true);
            }
            case 16: {
                if (this.lazyEvaluate) {
                    return new LazyEncodedSequence(definiteLengthInputStream.toByteArray());
                }
                return DERFactory.createSequence(this.buildDEREncodableVector(definiteLengthInputStream));
            }
            case 17: {
                return DERFactory.createSet(this.buildDEREncodableVector(definiteLengthInputStream));
            }
            case 8: 
        }
        return new DERExternal(this.buildDEREncodableVector(definiteLengthInputStream));
    }

    int getLimit() {
        return this.limit;
    }

    protected void readFully(byte[] arrby) {
        if (Streams.readFully((InputStream)this, (byte[])arrby) != arrby.length) {
            throw new EOFException("EOF encountered in middle of object");
        }
    }

    protected int readLength() {
        return ASN1InputStream.readLength((InputStream)this, this.limit);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ASN1Primitive readObject() {
        int n2 = this.read();
        if (n2 <= 0) {
            if (n2 != 0) return null;
            throw new IOException("unexpected end-of-contents marker");
        }
        int n3 = ASN1InputStream.readTagNumber((InputStream)this, n2);
        boolean bl = (n2 & 32) != 0;
        int n4 = this.readLength();
        if (n4 < 0) {
            if (!bl) {
                throw new IOException("indefinite length primitive encoding encountered");
            }
            ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream((InputStream)this, this.limit), this.limit);
            if ((n2 & 64) != 0) {
                return new BERApplicationSpecificParser(n3, aSN1StreamParser).getLoadedObject();
            }
            if ((n2 & 128) != 0) {
                return new BERTaggedObjectParser(true, n3, aSN1StreamParser).getLoadedObject();
            }
            switch (n3) {
                default: {
                    throw new IOException("unknown BER object encountered");
                }
                case 4: {
                    return new BEROctetStringParser(aSN1StreamParser).getLoadedObject();
                }
                case 16: {
                    return new BERSequenceParser(aSN1StreamParser).getLoadedObject();
                }
                case 17: {
                    return new BERSetParser(aSN1StreamParser).getLoadedObject();
                }
                case 8: 
            }
            return new DERExternalParser(aSN1StreamParser).getLoadedObject();
        }
        try {
            return this.buildObject(n2, n3, n4);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ASN1Exception("corrupted stream detected", illegalArgumentException);
        }
    }
}

