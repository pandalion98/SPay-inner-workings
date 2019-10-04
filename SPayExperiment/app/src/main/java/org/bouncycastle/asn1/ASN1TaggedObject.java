/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DLTaggedObject;

public abstract class ASN1TaggedObject
extends ASN1Primitive
implements ASN1TaggedObjectParser {
    boolean empty = false;
    boolean explicit = true;
    ASN1Encodable obj = null;
    int tagNo;

    /*
     * Enabled aggressive block sorting
     */
    public ASN1TaggedObject(boolean bl, int n2, ASN1Encodable aSN1Encodable) {
        this.explicit = aSN1Encodable instanceof ASN1Choice ? true : bl;
        this.tagNo = n2;
        if (this.explicit) {
            this.obj = aSN1Encodable;
            return;
        }
        if (aSN1Encodable.toASN1Primitive() instanceof ASN1Set) {
            // empty if block
        }
        this.obj = aSN1Encodable;
    }

    public static ASN1TaggedObject getInstance(Object object) {
        if (object == null || object instanceof ASN1TaggedObject) {
            return (ASN1TaggedObject)object;
        }
        if (object instanceof byte[]) {
            try {
                ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(ASN1TaggedObject.fromByteArray((byte[])object));
                return aSN1TaggedObject;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct tagged object from byte[]: " + iOException.getMessage());
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + object.getClass().getName());
    }

    public static ASN1TaggedObject getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        if (bl) {
            return (ASN1TaggedObject)aSN1TaggedObject.getObject();
        }
        throw new IllegalArgumentException("implicitly tagged tagged object");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1TaggedObject)) return false;
        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)aSN1Primitive;
        if (this.tagNo != aSN1TaggedObject.tagNo || this.empty != aSN1TaggedObject.empty || this.explicit != aSN1TaggedObject.explicit) return false;
        if (this.obj == null) {
            if (aSN1TaggedObject.obj == null) return true;
            return false;
        }
        if (!this.obj.toASN1Primitive().equals(aSN1TaggedObject.obj.toASN1Primitive())) return false;
        return true;
    }

    @Override
    abstract void encode(ASN1OutputStream var1);

    @Override
    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }

    public ASN1Primitive getObject() {
        if (this.obj != null) {
            return this.obj.toASN1Primitive();
        }
        return null;
    }

    @Override
    public ASN1Encodable getObjectParser(int n2, boolean bl) {
        switch (n2) {
            default: {
                if (!bl) break;
                return this.getObject();
            }
            case 17: {
                return ASN1Set.getInstance(this, bl).parser();
            }
            case 16: {
                return ASN1Sequence.getInstance(this, bl).parser();
            }
            case 4: {
                return ASN1OctetString.getInstance(this, bl).parser();
            }
        }
        throw new RuntimeException("implicit tagging not implemented for tag: " + n2);
    }

    @Override
    public int getTagNo() {
        return this.tagNo;
    }

    @Override
    public int hashCode() {
        int n2 = this.tagNo;
        if (this.obj != null) {
            n2 ^= this.obj.hashCode();
        }
        return n2;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isExplicit() {
        return this.explicit;
    }

    @Override
    ASN1Primitive toDERObject() {
        return new DERTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    @Override
    ASN1Primitive toDLObject() {
        return new DLTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    public String toString() {
        return "[" + this.tagNo + "]" + this.obj;
    }
}

