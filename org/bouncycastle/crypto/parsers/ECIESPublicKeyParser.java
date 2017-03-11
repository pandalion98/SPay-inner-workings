package org.bouncycastle.crypto.parsers;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public class ECIESPublicKeyParser implements KeyParser {
    private ECDomainParameters ecParams;

    public ECIESPublicKeyParser(ECDomainParameters eCDomainParameters) {
        this.ecParams = eCDomainParameters;
    }

    public AsymmetricKeyParameter readKey(InputStream inputStream) {
        byte[] bArr;
        int read = inputStream.read();
        switch (read) {
            case ECCurve.COORD_AFFINE /*0*/:
                throw new IOException("Sender's public key invalid.");
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case F2m.PPB /*3*/:
                bArr = new byte[(((this.ecParams.getCurve().getFieldSize() + 7) / 8) + 1)];
                break;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
            case ECCurve.COORD_SKEWED /*7*/:
                bArr = new byte[((((this.ecParams.getCurve().getFieldSize() + 7) / 8) * 2) + 1)];
                break;
            default:
                throw new IOException("Sender's public key has invalid point encoding 0x" + Integer.toString(read, 16));
        }
        bArr[0] = (byte) read;
        inputStream.read(bArr, 1, bArr.length - 1);
        return new ECPublicKeyParameters(this.ecParams.getCurve().decodePoint(bArr), this.ecParams);
    }
}
