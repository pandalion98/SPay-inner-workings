package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.Tag.Class;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Arrays;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TagImpl implements Tag {
    String description;
    byte[] idBytes;
    String name;
    Class tagClass;
    TagValueType tagValueType;
    TagType type;

    public TagImpl(String str, TagValueType tagValueType, String str2, String str3) {
        build(Util.fromHexString(str), tagValueType, str2, str3);
    }

    public TagImpl(byte[] bArr, TagValueType tagValueType, String str, String str2) {
        build(bArr, tagValueType, str, str2);
    }

    private void build(byte[] bArr, TagValueType tagValueType, String str, String str2) {
        if (bArr == null) {
            throw new IllegalArgumentException("Param id cannot be null");
        } else if (bArr.length == 0) {
            throw new IllegalArgumentException("Param id cannot be empty");
        } else if (tagValueType == null) {
            throw new IllegalArgumentException("Param tagValueType cannot be null");
        } else {
            this.idBytes = bArr;
            this.name = str != null ? str : BuildConfig.FLAVOR;
            if (str2 == null) {
                str2 = BuildConfig.FLAVOR;
            }
            this.description = str2;
            this.tagValueType = tagValueType;
            if (Util.isBitSet(this.idBytes[0], 6)) {
                this.type = TagType.CONSTRUCTED;
            } else {
                this.type = TagType.PRIMITIVE;
            }
            byte b = (byte) ((this.idBytes[0] >>> 6) & 3);
            switch (b) {
                case ECCurve.COORD_AFFINE /*0*/:
                    this.tagClass = Class.UNIVERSAL;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.tagClass = Class.APPLICATION;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.tagClass = Class.CONTEXT_SPECIFIC;
                case F2m.PPB /*3*/:
                    this.tagClass = Class.PRIVATE;
                default:
                    throw new RuntimeException("UNEXPECTED TAG CLASS: " + Util.byte2BinaryLiteral(b) + " " + Util.byteArrayToHexString(this.idBytes) + " " + str);
            }
        }
    }

    public boolean isConstructed() {
        return this.type == TagType.CONSTRUCTED;
    }

    public byte[] getTagBytes() {
        return this.idBytes;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public TagValueType getTagValueType() {
        return this.tagValueType;
    }

    public TagType getType() {
        return this.type;
    }

    public Class getTagClass() {
        return this.tagClass;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) obj;
        if (getTagBytes().length == tag.getTagBytes().length) {
            return Arrays.equals(getTagBytes(), tag.getTagBytes());
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.idBytes) + CipherSuite.TLS_PSK_WITH_NULL_SHA384;
    }

    public int getNumTagBytes() {
        return this.idBytes.length;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tag[");
        stringBuilder.append(Util.byteArrayToHexString(getTagBytes()));
        stringBuilder.append("] Name=");
        stringBuilder.append(getName());
        stringBuilder.append(", TagType=");
        stringBuilder.append(getType());
        stringBuilder.append(", ValueType=");
        stringBuilder.append(getTagValueType());
        stringBuilder.append(", Class=");
        stringBuilder.append(this.tagClass);
        return stringBuilder.toString();
    }

    public static void main(String[] strArr) {
        System.out.println(new TagImpl("bf0c", TagValueType.BINARY, BuildConfig.FLAVOR, BuildConfig.FLAVOR));
    }
}
