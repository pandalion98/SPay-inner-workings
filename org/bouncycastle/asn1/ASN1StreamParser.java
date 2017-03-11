package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;

public class ASN1StreamParser {
    private final InputStream _in;
    private final int _limit;
    private final byte[][] tmpBuffers;

    public ASN1StreamParser(InputStream inputStream) {
        this(inputStream, StreamUtil.findLimit(inputStream));
    }

    public ASN1StreamParser(InputStream inputStream, int i) {
        this._in = inputStream;
        this._limit = i;
        this.tmpBuffers = new byte[11][];
    }

    public ASN1StreamParser(byte[] bArr) {
        this(new ByteArrayInputStream(bArr), bArr.length);
    }

    private void set00Check(boolean z) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream) this._in).setEofOn00(z);
        }
    }

    ASN1Encodable readImplicit(boolean z, int i) {
        if (!(this._in instanceof IndefiniteLengthInputStream)) {
            if (!z) {
                switch (i) {
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        return new DEROctetStringParser((DefiniteLengthInputStream) this._in);
                    case X509KeyUsage.dataEncipherment /*16*/:
                        throw new ASN1Exception("sets must use constructed encoding (see X.690 8.11.1/8.12.1)");
                    case NamedCurve.secp160r2 /*17*/:
                        throw new ASN1Exception("sequences must use constructed encoding (see X.690 8.9.1/8.10.1)");
                    default:
                        break;
                }
            }
            switch (i) {
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    return new BEROctetStringParser(this);
                case X509KeyUsage.dataEncipherment /*16*/:
                    return new DERSequenceParser(this);
                case NamedCurve.secp160r2 /*17*/:
                    return new DERSetParser(this);
            }
            throw new RuntimeException("implicit tagging not implemented");
        } else if (z) {
            return readIndef(i);
        } else {
            throw new IOException("indefinite length primitive encoding encountered");
        }
    }

    ASN1Encodable readIndef(int i) {
        switch (i) {
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new BEROctetStringParser(this);
            case X509KeyUsage.keyAgreement /*8*/:
                return new DERExternalParser(this);
            case X509KeyUsage.dataEncipherment /*16*/:
                return new BERSequenceParser(this);
            case NamedCurve.secp160r2 /*17*/:
                return new BERSetParser(this);
            default:
                throw new ASN1Exception("unknown BER object encountered: 0x" + Integer.toHexString(i));
        }
    }

    public ASN1Encodable readObject() {
        boolean z = false;
        int read = this._in.read();
        if (read == -1) {
            return null;
        }
        set00Check(false);
        int readTagNumber = ASN1InputStream.readTagNumber(this._in, read);
        if ((read & 32) != 0) {
            z = true;
        }
        int readLength = ASN1InputStream.readLength(this._in, this._limit);
        if (readLength >= 0) {
            InputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, readLength);
            if ((read & 64) != 0) {
                return new DERApplicationSpecific(z, readTagNumber, definiteLengthInputStream.toByteArray());
            }
            if ((read & X509KeyUsage.digitalSignature) != 0) {
                return new BERTaggedObjectParser(z, readTagNumber, new ASN1StreamParser(definiteLengthInputStream));
            }
            if (z) {
                switch (readTagNumber) {
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        return new BEROctetStringParser(new ASN1StreamParser(definiteLengthInputStream));
                    case X509KeyUsage.keyAgreement /*8*/:
                        return new DERExternalParser(new ASN1StreamParser(definiteLengthInputStream));
                    case X509KeyUsage.dataEncipherment /*16*/:
                        return new DERSequenceParser(new ASN1StreamParser(definiteLengthInputStream));
                    case NamedCurve.secp160r2 /*17*/:
                        return new DERSetParser(new ASN1StreamParser(definiteLengthInputStream));
                    default:
                        throw new IOException("unknown tag " + readTagNumber + " encountered");
                }
            }
            switch (readTagNumber) {
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    return new DEROctetStringParser(definiteLengthInputStream);
                default:
                    try {
                        return ASN1InputStream.createPrimitiveDERObject(readTagNumber, definiteLengthInputStream, this.tmpBuffers);
                    } catch (Throwable e) {
                        throw new ASN1Exception("corrupted stream detected", e);
                    }
            }
        } else if (z) {
            ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this._in, this._limit), this._limit);
            return (read & 64) != 0 ? new BERApplicationSpecificParser(readTagNumber, aSN1StreamParser) : (read & X509KeyUsage.digitalSignature) != 0 ? new BERTaggedObjectParser(true, readTagNumber, aSN1StreamParser) : aSN1StreamParser.readIndef(readTagNumber);
        } else {
            throw new IOException("indefinite length primitive encoding encountered");
        }
    }

    ASN1Primitive readTaggedObject(boolean z, int i) {
        if (!z) {
            return new DERTaggedObject(false, i, new DEROctetString(((DefiniteLengthInputStream) this._in).toByteArray()));
        }
        ASN1EncodableVector readVector = readVector();
        return this._in instanceof IndefiniteLengthInputStream ? readVector.size() == 1 ? new BERTaggedObject(true, i, readVector.get(0)) : new BERTaggedObject(false, i, BERFactory.createSequence(readVector)) : readVector.size() == 1 ? new DERTaggedObject(true, i, readVector.get(0)) : new DERTaggedObject(false, i, DERFactory.createSequence(readVector));
    }

    ASN1EncodableVector readVector() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (true) {
            ASN1Encodable readObject = readObject();
            if (readObject == null) {
                return aSN1EncodableVector;
            }
            if (readObject instanceof InMemoryRepresentable) {
                aSN1EncodableVector.add(((InMemoryRepresentable) readObject).getLoadedObject());
            } else {
                aSN1EncodableVector.add(readObject.toASN1Primitive());
            }
        }
    }
}
