package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.agreement.jpake.JPAKEParticipant;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.util.io.Streams;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class ASN1InputStream extends FilterInputStream implements BERTags {
    private final boolean lazyEvaluate;
    private final int limit;
    private final byte[][] tmpBuffers;

    public ASN1InputStream(InputStream inputStream) {
        this(inputStream, StreamUtil.findLimit(inputStream));
    }

    public ASN1InputStream(InputStream inputStream, int i) {
        this(inputStream, i, false);
    }

    public ASN1InputStream(InputStream inputStream, int i, boolean z) {
        super(inputStream);
        this.limit = i;
        this.lazyEvaluate = z;
        this.tmpBuffers = new byte[11][];
    }

    public ASN1InputStream(InputStream inputStream, boolean z) {
        this(inputStream, StreamUtil.findLimit(inputStream), z);
    }

    public ASN1InputStream(byte[] bArr) {
        this(new ByteArrayInputStream(bArr), bArr.length);
    }

    public ASN1InputStream(byte[] bArr, boolean z) {
        this(new ByteArrayInputStream(bArr), bArr.length, z);
    }

    static ASN1Primitive createPrimitiveDERObject(int i, DefiniteLengthInputStream definiteLengthInputStream, byte[][] bArr) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return ASN1Boolean.fromOctetString(getBuffer(definiteLengthInputStream, bArr));
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return new ASN1Integer(definiteLengthInputStream.toByteArray(), false);
            case F2m.PPB /*3*/:
                return DERBitString.fromInputStream(definiteLengthInputStream.getRemaining(), definiteLengthInputStream);
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new DEROctetString(definiteLengthInputStream.toByteArray());
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return DERNull.INSTANCE;
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return ASN1ObjectIdentifier.fromOctetString(getBuffer(definiteLengthInputStream, bArr));
            case NamedCurve.sect283r1 /*10*/:
                return ASN1Enumerated.fromOctetString(getBuffer(definiteLengthInputStream, bArr));
            case CertStatus.UNDETERMINED /*12*/:
                return new DERUTF8String(definiteLengthInputStream.toByteArray());
            case NamedCurve.secp192k1 /*18*/:
                return new DERNumericString(definiteLengthInputStream.toByteArray());
            case NamedCurve.secp192r1 /*19*/:
                return new DERPrintableString(definiteLengthInputStream.toByteArray());
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                return new DERT61String(definiteLengthInputStream.toByteArray());
            case NamedCurve.secp256k1 /*22*/:
                return new DERIA5String(definiteLengthInputStream.toByteArray());
            case NamedCurve.secp256r1 /*23*/:
                return new ASN1UTCTime(definiteLengthInputStream.toByteArray());
            case NamedCurve.secp384r1 /*24*/:
                return new ASN1GeneralizedTime(definiteLengthInputStream.toByteArray());
            case NamedCurve.brainpoolP256r1 /*26*/:
                return new DERVisibleString(definiteLengthInputStream.toByteArray());
            case NamedCurve.brainpoolP384r1 /*27*/:
                return new DERGeneralString(definiteLengthInputStream.toByteArray());
            case NamedCurve.brainpoolP512r1 /*28*/:
                return new DERUniversalString(definiteLengthInputStream.toByteArray());
            case JPAKEParticipant.STATE_ROUND_2_CREATED /*30*/:
                return new DERBMPString(getBMPCharBuffer(definiteLengthInputStream));
            default:
                throw new IOException("unknown tag " + i + " encountered");
        }
    }

    private static char[] getBMPCharBuffer(DefiniteLengthInputStream definiteLengthInputStream) {
        int remaining = definiteLengthInputStream.getRemaining() / 2;
        char[] cArr = new char[remaining];
        int i = 0;
        while (i < remaining) {
            int read = definiteLengthInputStream.read();
            if (read >= 0) {
                int read2 = definiteLengthInputStream.read();
                if (read2 < 0) {
                    break;
                }
                int i2 = i + 1;
                cArr[i] = (char) ((read << 8) | (read2 & GF2Field.MASK));
                i = i2;
            } else {
                break;
            }
        }
        return cArr;
    }

    private static byte[] getBuffer(DefiniteLengthInputStream definiteLengthInputStream, byte[][] bArr) {
        int remaining = definiteLengthInputStream.getRemaining();
        if (definiteLengthInputStream.getRemaining() >= bArr.length) {
            return definiteLengthInputStream.toByteArray();
        }
        byte[] bArr2 = bArr[remaining];
        if (bArr2 == null) {
            bArr2 = new byte[remaining];
            bArr[remaining] = bArr2;
        }
        Streams.readFully(definiteLengthInputStream, bArr2);
        return bArr2;
    }

    static int readLength(InputStream inputStream, int i) {
        int i2 = 0;
        int read = inputStream.read();
        if (read < 0) {
            throw new EOFException("EOF found when length expected");
        } else if (read == X509KeyUsage.digitalSignature) {
            return -1;
        } else {
            if (read <= CertificateBody.profileType) {
                return read;
            }
            int i3 = read & CertificateBody.profileType;
            if (i3 > 4) {
                throw new IOException("DER length more than 4 bytes: " + i3);
            }
            read = 0;
            while (i2 < i3) {
                int read2 = inputStream.read();
                if (read2 < 0) {
                    throw new EOFException("EOF found reading length");
                }
                i2++;
                read = read2 + (read << 8);
            }
            if (read < 0) {
                throw new IOException("corrupted stream - negative length found");
            } else if (read < i) {
                return read;
            } else {
                throw new IOException("corrupted stream - out of bounds length found");
            }
        }
    }

    static int readTagNumber(InputStream inputStream, int i) {
        int i2 = i & 31;
        if (i2 != 31) {
            return i2;
        }
        int i3 = 0;
        i2 = inputStream.read();
        if ((i2 & CertificateBody.profileType) == 0) {
            throw new IOException("corrupted stream - invalid high tag number found");
        }
        while (i2 >= 0 && (i2 & X509KeyUsage.digitalSignature) != 0) {
            i3 = ((i2 & CertificateBody.profileType) | i3) << 7;
            i2 = inputStream.read();
        }
        if (i2 >= 0) {
            return (i2 & CertificateBody.profileType) | i3;
        }
        throw new EOFException("EOF found inside tag value.");
    }

    ASN1EncodableVector buildDEREncodableVector(DefiniteLengthInputStream definiteLengthInputStream) {
        return new ASN1InputStream((InputStream) definiteLengthInputStream).buildEncodableVector();
    }

    ASN1EncodableVector buildEncodableVector() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (true) {
            ASN1Encodable readObject = readObject();
            if (readObject == null) {
                return aSN1EncodableVector;
            }
            aSN1EncodableVector.add(readObject);
        }
    }

    protected ASN1Primitive buildObject(int i, int i2, int i3) {
        int i4 = 0;
        boolean z = (i & 32) != 0;
        InputStream definiteLengthInputStream = new DefiniteLengthInputStream(this, i3);
        if ((i & 64) != 0) {
            return new DERApplicationSpecific(z, i2, definiteLengthInputStream.toByteArray());
        }
        if ((i & X509KeyUsage.digitalSignature) != 0) {
            return new ASN1StreamParser(definiteLengthInputStream).readTaggedObject(z, i2);
        }
        if (!z) {
            return createPrimitiveDERObject(i2, definiteLengthInputStream, this.tmpBuffers);
        }
        switch (i2) {
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                ASN1EncodableVector buildDEREncodableVector = buildDEREncodableVector(definiteLengthInputStream);
                ASN1OctetString[] aSN1OctetStringArr = new ASN1OctetString[buildDEREncodableVector.size()];
                while (i4 != aSN1OctetStringArr.length) {
                    aSN1OctetStringArr[i4] = (ASN1OctetString) buildDEREncodableVector.get(i4);
                    i4++;
                }
                return new BEROctetString(aSN1OctetStringArr);
            case X509KeyUsage.keyAgreement /*8*/:
                return new DERExternal(buildDEREncodableVector(definiteLengthInputStream));
            case X509KeyUsage.dataEncipherment /*16*/:
                return this.lazyEvaluate ? new LazyEncodedSequence(definiteLengthInputStream.toByteArray()) : DERFactory.createSequence(buildDEREncodableVector(definiteLengthInputStream));
            case NamedCurve.secp160r2 /*17*/:
                return DERFactory.createSet(buildDEREncodableVector(definiteLengthInputStream));
            default:
                throw new IOException("unknown tag " + i2 + " encountered");
        }
    }

    int getLimit() {
        return this.limit;
    }

    protected void readFully(byte[] bArr) {
        if (Streams.readFully(this, bArr) != bArr.length) {
            throw new EOFException("EOF encountered in middle of object");
        }
    }

    protected int readLength() {
        return readLength(this, this.limit);
    }

    public ASN1Primitive readObject() {
        int read = read();
        if (read > 0) {
            int readTagNumber = readTagNumber(this, read);
            boolean z = (read & 32) != 0;
            int readLength = readLength();
            if (readLength >= 0) {
                try {
                    return buildObject(read, readTagNumber, readLength);
                } catch (Throwable e) {
                    throw new ASN1Exception("corrupted stream detected", e);
                }
            } else if (z) {
                ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this, this.limit), this.limit);
                if ((read & 64) != 0) {
                    return new BERApplicationSpecificParser(readTagNumber, aSN1StreamParser).getLoadedObject();
                }
                if ((read & X509KeyUsage.digitalSignature) != 0) {
                    return new BERTaggedObjectParser(true, readTagNumber, aSN1StreamParser).getLoadedObject();
                }
                switch (readTagNumber) {
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        return new BEROctetStringParser(aSN1StreamParser).getLoadedObject();
                    case X509KeyUsage.keyAgreement /*8*/:
                        return new DERExternalParser(aSN1StreamParser).getLoadedObject();
                    case X509KeyUsage.dataEncipherment /*16*/:
                        return new BERSequenceParser(aSN1StreamParser).getLoadedObject();
                    case NamedCurve.secp160r2 /*17*/:
                        return new BERSetParser(aSN1StreamParser).getLoadedObject();
                    default:
                        throw new IOException("unknown BER object encountered");
                }
            } else {
                throw new IOException("indefinite length primitive encoding encountered");
            }
        } else if (read != 0) {
            return null;
        } else {
            throw new IOException("unexpected end-of-contents marker");
        }
    }
}
