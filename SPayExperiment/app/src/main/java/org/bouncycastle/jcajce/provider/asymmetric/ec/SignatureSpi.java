/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil
 */
package org.bouncycastle.jcajce.provider.asymmetric.ec;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jcajce.provider.asymmetric.util.DSABase;
import org.bouncycastle.jcajce.provider.asymmetric.util.DSAEncoder;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;

public class SignatureSpi
extends DSABase {
    SignatureSpi(Digest digest, DSA dSA, DSAEncoder dSAEncoder) {
        super(digest, dSA, dSAEncoder);
    }

    protected void engineInitSign(PrivateKey privateKey) {
        AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePrivateKeyParameter((PrivateKey)privateKey);
        this.digest.reset();
        if (this.appRandom != null) {
            this.signer.init(true, new ParametersWithRandom(asymmetricKeyParameter, this.appRandom));
            return;
        }
        this.signer.init(true, asymmetricKeyParameter);
    }

    protected void engineInitVerify(PublicKey publicKey) {
        AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePublicKeyParameter((PublicKey)publicKey);
        this.digest.reset();
        this.signer.init(false, asymmetricKeyParameter);
    }

    private static class PlainDSAEncoder
    implements DSAEncoder {
        private PlainDSAEncoder() {
        }

        private byte[] makeUnsigned(BigInteger bigInteger) {
            byte[] arrby = bigInteger.toByteArray();
            if (arrby[0] == 0) {
                byte[] arrby2 = new byte[-1 + arrby.length];
                System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
                return arrby2;
            }
            return arrby;
        }

        @Override
        public BigInteger[] decode(byte[] arrby) {
            BigInteger[] arrbigInteger = new BigInteger[2];
            byte[] arrby2 = new byte[arrby.length / 2];
            byte[] arrby3 = new byte[arrby.length / 2];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
            System.arraycopy((Object)arrby, (int)arrby2.length, (Object)arrby3, (int)0, (int)arrby3.length);
            arrbigInteger[0] = new BigInteger(1, arrby2);
            arrbigInteger[1] = new BigInteger(1, arrby3);
            return arrbigInteger;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public byte[] encode(BigInteger bigInteger, BigInteger bigInteger2) {
            byte[] arrby;
            byte[] arrby2 = this.makeUnsigned(bigInteger);
            byte[] arrby3 = arrby2.length > (arrby = this.makeUnsigned(bigInteger2)).length ? new byte[2 * arrby2.length] : new byte[2 * arrby.length];
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)(arrby3.length / 2 - arrby2.length), (int)arrby2.length);
            System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)(arrby3.length - arrby.length), (int)arrby.length);
            return arrby3;
        }
    }

    private static class StdDSAEncoder
    implements DSAEncoder {
        private StdDSAEncoder() {
        }

        @Override
        public BigInteger[] decode(byte[] arrby) {
            ASN1Sequence aSN1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(arrby);
            BigInteger[] arrbigInteger = new BigInteger[]{ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0)).getValue(), ASN1Integer.getInstance(aSN1Sequence.getObjectAt(1)).getValue()};
            return arrbigInteger;
        }

        @Override
        public byte[] encode(BigInteger bigInteger, BigInteger bigInteger2) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            aSN1EncodableVector.add(new ASN1Integer(bigInteger));
            aSN1EncodableVector.add(new ASN1Integer(bigInteger2));
            return new DERSequence(aSN1EncodableVector).getEncoded("DER");
        }
    }

    public static class ecCVCDSA
    extends SignatureSpi {
        public ecCVCDSA() {
            super(new SHA1Digest(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }

    public static class ecCVCDSA224
    extends SignatureSpi {
        public ecCVCDSA224() {
            super(new SHA224Digest(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }

    public static class ecCVCDSA256
    extends SignatureSpi {
        public ecCVCDSA256() {
            super(new SHA256Digest(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }

    public static class ecCVCDSA384
    extends SignatureSpi {
        public ecCVCDSA384() {
            super(new SHA384Digest(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }

    public static class ecCVCDSA512
    extends SignatureSpi {
        public ecCVCDSA512() {
            super(new SHA512Digest(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }

    public static class ecDSA224
    extends SignatureSpi {
        public ecDSA224() {
            super(new SHA224Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

    public static class ecDSA256
    extends SignatureSpi {
        public ecDSA256() {
            super(new SHA256Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

    public static class ecDSA384
    extends SignatureSpi {
        public ecDSA384() {
            super(new SHA384Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

}

