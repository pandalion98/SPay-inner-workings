package org.bouncycastle.crypto.engines;

import com.google.android.gms.location.places.Place;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.util.Pack;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class ISAACEngine implements StreamCipher {
    private int f159a;
    private int f160b;
    private int f161c;
    private int[] engineState;
    private int index;
    private boolean initialised;
    private byte[] keyStream;
    private int[] results;
    private final int sizeL;
    private final int stateArraySize;
    private byte[] workingKey;

    public ISAACEngine() {
        this.sizeL = 8;
        this.stateArraySize = SkeinMac.SKEIN_256;
        this.engineState = null;
        this.results = null;
        this.f159a = 0;
        this.f160b = 0;
        this.f161c = 0;
        this.index = 0;
        this.keyStream = new byte[SkeinMac.SKEIN_1024];
        this.workingKey = null;
        this.initialised = false;
    }

    private void isaac() {
        int i = this.f160b;
        int i2 = this.f161c + 1;
        this.f161c = i2;
        this.f160b = i + i2;
        for (i = 0; i < SkeinMac.SKEIN_256; i++) {
            i2 = this.engineState[i];
            switch (i & 3) {
                case ECCurve.COORD_AFFINE /*0*/:
                    this.f159a ^= this.f159a << 13;
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.f159a ^= this.f159a >>> 6;
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.f159a ^= this.f159a << 2;
                    break;
                case F2m.PPB /*3*/:
                    this.f159a ^= this.f159a >>> 16;
                    break;
                default:
                    break;
            }
            this.f159a += this.engineState[(i + X509KeyUsage.digitalSignature) & GF2Field.MASK];
            int i3 = (this.engineState[(i2 >>> 2) & GF2Field.MASK] + this.f159a) + this.f160b;
            this.engineState[i] = i3;
            int[] iArr = this.results;
            i2 += this.engineState[(i3 >>> 10) & GF2Field.MASK];
            this.f160b = i2;
            iArr[i] = i2;
        }
    }

    private void mix(int[] iArr) {
        iArr[0] = iArr[0] ^ (iArr[1] << 11);
        iArr[3] = iArr[3] + iArr[0];
        iArr[1] = iArr[1] + iArr[2];
        iArr[1] = iArr[1] ^ (iArr[2] >>> 2);
        iArr[4] = iArr[4] + iArr[1];
        iArr[2] = iArr[2] + iArr[3];
        iArr[2] = iArr[2] ^ (iArr[3] << 8);
        iArr[5] = iArr[5] + iArr[2];
        iArr[3] = iArr[3] + iArr[4];
        iArr[3] = iArr[3] ^ (iArr[4] >>> 16);
        iArr[6] = iArr[6] + iArr[3];
        iArr[4] = iArr[4] + iArr[5];
        iArr[4] = iArr[4] ^ (iArr[5] << 10);
        iArr[7] = iArr[7] + iArr[4];
        iArr[5] = iArr[5] + iArr[6];
        iArr[5] = iArr[5] ^ (iArr[6] >>> 4);
        iArr[0] = iArr[0] + iArr[5];
        iArr[6] = iArr[6] + iArr[7];
        iArr[6] = iArr[6] ^ (iArr[7] << 8);
        iArr[1] = iArr[1] + iArr[6];
        iArr[7] = iArr[7] + iArr[0];
        iArr[7] = iArr[7] ^ (iArr[0] >>> 9);
        iArr[2] = iArr[2] + iArr[7];
        iArr[0] = iArr[0] + iArr[1];
    }

    private void setKey(byte[] bArr) {
        int i;
        this.workingKey = bArr;
        if (this.engineState == null) {
            this.engineState = new int[SkeinMac.SKEIN_256];
        }
        if (this.results == null) {
            this.results = new int[SkeinMac.SKEIN_256];
        }
        for (i = 0; i < SkeinMac.SKEIN_256; i++) {
            int[] iArr = this.engineState;
            this.results[i] = 0;
            iArr[i] = 0;
        }
        this.f161c = 0;
        this.f160b = 0;
        this.f159a = 0;
        this.index = 0;
        Object obj = new byte[(bArr.length + (bArr.length & 3))];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        for (i = 0; i < obj.length; i += 4) {
            this.results[i >>> 2] = Pack.littleEndianToInt(obj, i);
        }
        int[] iArr2 = new int[8];
        for (i = 0; i < 8; i++) {
            iArr2[i] = -1640531527;
        }
        for (i = 0; i < 4; i++) {
            mix(iArr2);
        }
        int i2 = 0;
        while (i2 < 2) {
            for (int i3 = 0; i3 < SkeinMac.SKEIN_256; i3 += 8) {
                for (int i4 = 0; i4 < 8; i4++) {
                    iArr2[i4] = (i2 < 1 ? this.results[i3 + i4] : this.engineState[i3 + i4]) + iArr2[i4];
                }
                mix(iArr2);
                for (i = 0; i < 8; i++) {
                    this.engineState[i3 + i] = iArr2[i];
                }
            }
            i2++;
        }
        isaac();
        this.initialised = true;
    }

    public String getAlgorithmName() {
        return "ISAAC";
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            setKey(((KeyParameter) cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to ISAAC init - " + cipherParameters.getClass().getName());
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (!this.initialised) {
            throw new IllegalStateException(getAlgorithmName() + " not initialised");
        } else if (i + i2 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i3 + i2 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            for (int i4 = 0; i4 < i2; i4++) {
                if (this.index == 0) {
                    isaac();
                    this.keyStream = Pack.intToBigEndian(this.results);
                }
                bArr2[i4 + i3] = (byte) (this.keyStream[this.index] ^ bArr[i4 + i]);
                this.index = (this.index + 1) & Place.TYPE_SUBLOCALITY_LEVEL_1;
            }
            return i2;
        }
    }

    public void reset() {
        setKey(this.workingKey);
    }

    public byte returnByte(byte b) {
        if (this.index == 0) {
            isaac();
            this.keyStream = Pack.intToBigEndian(this.results);
        }
        byte b2 = (byte) (this.keyStream[this.index] ^ b);
        this.index = (this.index + 1) & Place.TYPE_SUBLOCALITY_LEVEL_1;
        return b2;
    }
}
