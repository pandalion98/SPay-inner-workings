package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;

public abstract class ASN1TaggedObject extends ASN1Primitive implements ASN1TaggedObjectParser {
    boolean empty;
    boolean explicit;
    ASN1Encodable obj;
    int tagNo;

    public ASN1TaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        this.empty = false;
        this.explicit = true;
        this.obj = null;
        if (aSN1Encodable instanceof ASN1Choice) {
            this.explicit = true;
        } else {
            this.explicit = z;
        }
        this.tagNo = i;
        if (this.explicit) {
            this.obj = aSN1Encodable;
            return;
        }
        if (aSN1Encodable.toASN1Primitive() instanceof ASN1Set) {
            this.obj = aSN1Encodable;
        } else {
            this.obj = aSN1Encodable;
        }
    }

    public static ASN1TaggedObject getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1TaggedObject)) {
            return (ASN1TaggedObject) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[]) obj));
            } catch (IOException e) {
                throw new IllegalArgumentException("failed to construct tagged object from byte[]: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1TaggedObject getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            return (ASN1TaggedObject) aSN1TaggedObject.getObject();
        }
        throw new IllegalArgumentException("implicitly tagged tagged object");
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1TaggedObject)) {
            return false;
        }
        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Primitive;
        if (this.tagNo != aSN1TaggedObject.tagNo || this.empty != aSN1TaggedObject.empty || this.explicit != aSN1TaggedObject.explicit) {
            return false;
        }
        if (this.obj == null) {
            if (aSN1TaggedObject.obj != null) {
                return false;
            }
        } else if (!this.obj.toASN1Primitive().equals(aSN1TaggedObject.obj.toASN1Primitive())) {
            return false;
        }
        return true;
    }

    abstract void encode(ASN1OutputStream aSN1OutputStream);

    public ASN1Primitive getLoadedObject() {
        return toASN1Primitive();
    }

    public ASN1Primitive getObject() {
        return this.obj != null ? this.obj.toASN1Primitive() : null;
    }

    public ASN1Encodable getObjectParser(int i, boolean z) {
        switch (i) {
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return ASN1OctetString.getInstance(this, z).parser();
            case X509KeyUsage.dataEncipherment /*16*/:
                return ASN1Sequence.getInstance(this, z).parser();
            case NamedCurve.secp160r2 /*17*/:
                return ASN1Set.getInstance(this, z).parser();
            default:
                if (z) {
                    return getObject();
                }
                throw new RuntimeException("implicit tagging not implemented for tag: " + i);
        }
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public int hashCode() {
        int i = this.tagNo;
        return this.obj != null ? i ^ this.obj.hashCode() : i;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isExplicit() {
        return this.explicit;
    }

    ASN1Primitive toDERObject() {
        return new DERTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    ASN1Primitive toDLObject() {
        return new DLTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    public String toString() {
        return "[" + this.tagNo + "]" + this.obj;
    }
}
