/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Exception;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.BERApplicationSpecificParser;
import org.bouncycastle.asn1.BERFactory;
import org.bouncycastle.asn1.BEROctetStringParser;
import org.bouncycastle.asn1.BERSequenceParser;
import org.bouncycastle.asn1.BERSetParser;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.BERTaggedObjectParser;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DERExternalParser;
import org.bouncycastle.asn1.DERFactory;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROctetStringParser;
import org.bouncycastle.asn1.DERSequenceParser;
import org.bouncycastle.asn1.DERSetParser;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DefiniteLengthInputStream;
import org.bouncycastle.asn1.InMemoryRepresentable;
import org.bouncycastle.asn1.IndefiniteLengthInputStream;
import org.bouncycastle.asn1.StreamUtil;

public class ASN1StreamParser {
    private final InputStream _in;
    private final int _limit;
    private final byte[][] tmpBuffers;

    public ASN1StreamParser(InputStream inputStream) {
        this(inputStream, StreamUtil.findLimit(inputStream));
    }

    public ASN1StreamParser(InputStream inputStream, int n2) {
        this._in = inputStream;
        this._limit = n2;
        this.tmpBuffers = new byte[11][];
    }

    public ASN1StreamParser(byte[] arrby) {
        this((InputStream)new ByteArrayInputStream(arrby), arrby.length);
    }

    private void set00Check(boolean bl) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream)this._in).setEofOn00(bl);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    ASN1Encodable readImplicit(boolean bl, int n2) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            if (bl) return this.readIndef(n2);
            throw new IOException("indefinite length primitive encoding encountered");
        }
        if (bl) {
            switch (n2) {
                default: {
                    do {
                        throw new RuntimeException("implicit tagging not implemented");
                        break;
                    } while (true);
                }
                case 17: {
                    return new DERSetParser(this);
                }
                case 16: {
                    return new DERSequenceParser(this);
                }
                case 4: 
            }
            return new BEROctetStringParser(this);
        }
        switch (n2) {
            default: {
                throw new RuntimeException("implicit tagging not implemented");
            }
            case 4: {
                return new DEROctetStringParser((DefiniteLengthInputStream)this._in);
            }
            case 17: {
                throw new ASN1Exception("sequences must use constructed encoding (see X.690 8.9.1/8.10.1)");
            }
            case 16: 
        }
        throw new ASN1Exception("sets must use constructed encoding (see X.690 8.11.1/8.12.1)");
    }

    ASN1Encodable readIndef(int n2) {
        switch (n2) {
            default: {
                throw new ASN1Exception("unknown BER object encountered: 0x" + Integer.toHexString((int)n2));
            }
            case 8: {
                return new DERExternalParser(this);
            }
            case 4: {
                return new BEROctetStringParser(this);
            }
            case 16: {
                return new BERSequenceParser(this);
            }
            case 17: 
        }
        return new BERSetParser(this);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public ASN1Encodable readObject() {
        int n2;
        int n3 = this._in.read();
        if (n3 == -1) {
            return null;
        }
        this.set00Check(false);
        int n4 = ASN1InputStream.readTagNumber(this._in, n3);
        int n5 = n3 & 32;
        boolean bl = false;
        if (n5 != 0) {
            bl = true;
        }
        if ((n2 = ASN1InputStream.readLength(this._in, this._limit)) < 0) {
            if (!bl) {
                throw new IOException("indefinite length primitive encoding encountered");
            }
            ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this._in, this._limit), this._limit);
            if ((n3 & 64) != 0) {
                return new BERApplicationSpecificParser(n4, aSN1StreamParser);
            }
            if ((n3 & 128) == 0) return aSN1StreamParser.readIndef(n4);
            return new BERTaggedObjectParser(true, n4, aSN1StreamParser);
        }
        DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, n2);
        if ((n3 & 64) != 0) {
            return new DERApplicationSpecific(bl, n4, definiteLengthInputStream.toByteArray());
        }
        if ((n3 & 128) != 0) {
            return new BERTaggedObjectParser(bl, n4, new ASN1StreamParser(definiteLengthInputStream));
        }
        if (bl) {
            switch (n4) {
                default: {
                    throw new IOException("unknown tag " + n4 + " encountered");
                }
                case 4: {
                    return new BEROctetStringParser(new ASN1StreamParser(definiteLengthInputStream));
                }
                case 16: {
                    return new DERSequenceParser(new ASN1StreamParser(definiteLengthInputStream));
                }
                case 17: {
                    return new DERSetParser(new ASN1StreamParser(definiteLengthInputStream));
                }
                case 8: 
            }
            return new DERExternalParser(new ASN1StreamParser(definiteLengthInputStream));
        }
        switch (n4) {
            default: {
                return ASN1InputStream.createPrimitiveDERObject(n4, definiteLengthInputStream, this.tmpBuffers);
            }
            case 4: 
        }
        return new DEROctetStringParser(definiteLengthInputStream);
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ASN1Exception("corrupted stream detected", illegalArgumentException);
        }
    }

    ASN1Primitive readTaggedObject(boolean bl, int n2) {
        if (!bl) {
            return new DERTaggedObject(false, n2, new DEROctetString(((DefiniteLengthInputStream)this._in).toByteArray()));
        }
        ASN1EncodableVector aSN1EncodableVector = this.readVector();
        if (this._in instanceof IndefiniteLengthInputStream) {
            if (aSN1EncodableVector.size() == 1) {
                return new BERTaggedObject(true, n2, aSN1EncodableVector.get(0));
            }
            return new BERTaggedObject(false, n2, BERFactory.createSequence(aSN1EncodableVector));
        }
        if (aSN1EncodableVector.size() == 1) {
            return new DERTaggedObject(true, n2, aSN1EncodableVector.get(0));
        }
        return new DERTaggedObject(false, n2, DERFactory.createSequence(aSN1EncodableVector));
    }

    ASN1EncodableVector readVector() {
        ASN1Encodable aSN1Encodable;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while ((aSN1Encodable = this.readObject()) != null) {
            if (aSN1Encodable instanceof InMemoryRepresentable) {
                aSN1EncodableVector.add(((InMemoryRepresentable)((Object)aSN1Encodable)).getLoadedObject());
                continue;
            }
            aSN1EncodableVector.add(aSN1Encodable.toASN1Primitive());
        }
        return aSN1EncodableVector;
    }
}

