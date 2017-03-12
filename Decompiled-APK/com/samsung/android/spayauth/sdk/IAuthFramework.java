package com.samsung.android.spayauth.sdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayauth.sdk.a */
public interface IAuthFramework extends IInterface {

    /* renamed from: com.samsung.android.spayauth.sdk.a.a */
    public static abstract class IAuthFramework extends Binder implements IAuthFramework {
        public IAuthFramework() {
            attachInterface(this, "com.samsung.android.spayauth.sdk.IAuthFramework");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            boolean z = false;
            int N;
            byte[] a;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m169N();
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m170O();
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m186a(parcel.createStringArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m185a(parcel.createByteArray(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m181a(parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m188b(parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m184a(parcel.createByteArray(), parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m189b(parcel.createByteArray(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.sect283k1 /*9*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m191c(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.sect283r1 /*10*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    boolean z2 = parcel.readInt() != 0;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    N = m180a(z2, z);
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case CertStatus.UNREVOKED /*11*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    N = m179a(z);
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case CertStatus.UNDETERMINED /*12*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    a = m187a(parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(a);
                    return true;
                case NamedCurve.sect571k1 /*13*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m192d(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.sect571r1 /*14*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m171P();
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.secp160k1 /*15*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m172Q();
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case X509KeyUsage.dataEncipherment /*16*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m190b(parcel.createStringArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.secp160r2 /*17*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    a = m173R();
                    parcel2.writeNoException();
                    parcel2.writeByteArray(a);
                    return true;
                case NamedCurve.secp192k1 /*18*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m174S();
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.secp192r1 /*19*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m193e(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m194f(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.secp224r1 /*21*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m178a(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.secp256k1 /*22*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m183a(parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.createIntArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.secp256r1 /*23*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int[] T = m175T();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(T);
                    return true;
                case NamedCurve.secp384r1 /*24*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m177a(parcel.readInt(), parcel.createIntArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.secp521r1 /*25*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    N = m182a(parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(N);
                    return true;
                case NamedCurve.brainpoolP256r1 /*26*/:
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    m176V();
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.samsung.android.spayauth.sdk.IAuthFramework");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int m169N();

    int m170O();

    int m171P();

    int m172Q();

    byte[] m173R();

    int m174S();

    int[] m175T();

    void m176V();

    int m177a(int i, int[] iArr);

    int m178a(String str, String str2, String str3, String str4, String str5, String str6, byte[] bArr);

    int m179a(boolean z);

    int m180a(boolean z, boolean z2);

    int m181a(byte[] bArr, int i, int i2);

    int m182a(byte[] bArr, int i, int i2, int i3, int i4);

    int m183a(byte[] bArr, int i, int i2, int[] iArr);

    int m184a(byte[] bArr, byte[] bArr2, int i, int i2);

    int m185a(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, int i5);

    int m186a(String[] strArr);

    byte[] m187a(String str, byte[] bArr);

    int m188b(byte[] bArr, int i, int i2);

    int m189b(byte[] bArr, byte[] bArr2);

    int m190b(String[] strArr);

    int m191c(byte[] bArr);

    int m192d(byte[] bArr);

    int m193e(byte[] bArr);

    int m194f(byte[] bArr);
}
