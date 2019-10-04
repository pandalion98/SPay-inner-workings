/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;

public abstract class ASN1Sequence
extends ASN1Primitive {
    protected Vector seq = new Vector();

    protected ASN1Sequence() {
    }

    protected ASN1Sequence(ASN1Encodable aSN1Encodable) {
        this.seq.addElement((Object)aSN1Encodable);
    }

    protected ASN1Sequence(ASN1EncodableVector aSN1EncodableVector) {
        for (int i2 = 0; i2 != aSN1EncodableVector.size(); ++i2) {
            this.seq.addElement((Object)aSN1EncodableVector.get(i2));
        }
    }

    protected ASN1Sequence(ASN1Encodable[] arraSN1Encodable) {
        for (int i2 = 0; i2 != arraSN1Encodable.length; ++i2) {
            this.seq.addElement((Object)arraSN1Encodable[i2]);
        }
    }

    public static ASN1Sequence getInstance(Object object) {
        ASN1Primitive aSN1Primitive;
        if (object == null || object instanceof ASN1Sequence) {
            return (ASN1Sequence)object;
        }
        if (object instanceof ASN1SequenceParser) {
            return ASN1Sequence.getInstance(((ASN1SequenceParser)object).toASN1Primitive());
        }
        if (object instanceof byte[]) {
            try {
                ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(ASN1Sequence.fromByteArray((byte[])object));
                return aSN1Sequence;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct sequence from byte[]: " + iOException.getMessage());
            }
        }
        if (object instanceof ASN1Encodable && (aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive()) instanceof ASN1Sequence) {
            return (ASN1Sequence)aSN1Primitive;
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + object.getClass().getName());
    }

    public static ASN1Sequence getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        if (bl) {
            if (!aSN1TaggedObject.isExplicit()) {
                throw new IllegalArgumentException("object implicit - explicit expected.");
            }
            return ASN1Sequence.getInstance(aSN1TaggedObject.getObject().toASN1Primitive());
        }
        if (aSN1TaggedObject.isExplicit()) {
            if (aSN1TaggedObject instanceof BERTaggedObject) {
                return new BERSequence(aSN1TaggedObject.getObject());
            }
            return new DLSequence(aSN1TaggedObject.getObject());
        }
        if (aSN1TaggedObject.getObject() instanceof ASN1Sequence) {
            return (ASN1Sequence)aSN1TaggedObject.getObject();
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + aSN1TaggedObject.getClass().getName());
    }

    private ASN1Encodable getNext(Enumeration enumeration) {
        return (ASN1Encodable)enumeration.nextElement();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive instanceof ASN1Sequence) {
            ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1Primitive;
            if (this.size() == aSN1Sequence.size()) {
                ASN1Encodable aSN1Encodable;
                ASN1Primitive aSN1Primitive2;
                ASN1Encodable aSN1Encodable2;
                ASN1Primitive aSN1Primitive3;
                Enumeration enumeration = this.getObjects();
                Enumeration enumeration2 = aSN1Sequence.getObjects();
                do {
                    if (!enumeration.hasMoreElements()) {
                        return true;
                    }
                    aSN1Encodable2 = this.getNext(enumeration);
                    aSN1Encodable = this.getNext(enumeration2);
                } while ((aSN1Primitive2 = aSN1Encodable2.toASN1Primitive()) == (aSN1Primitive3 = aSN1Encodable.toASN1Primitive()) || aSN1Primitive2.equals(aSN1Primitive3));
            }
        }
        return false;
    }

    @Override
    abstract void encode(ASN1OutputStream var1);

    public ASN1Encodable getObjectAt(int n2) {
        return (ASN1Encodable)this.seq.elementAt(n2);
    }

    public Enumeration getObjects() {
        return this.seq.elements();
    }

    @Override
    public int hashCode() {
        Enumeration enumeration = this.getObjects();
        int n2 = this.size();
        while (enumeration.hasMoreElements()) {
            ASN1Encodable aSN1Encodable = this.getNext(enumeration);
            n2 = n2 * 17 ^ aSN1Encodable.hashCode();
        }
        return n2;
    }

    @Override
    boolean isConstructed() {
        return true;
    }

    public ASN1SequenceParser parser() {
        return new ASN1SequenceParser(){
            private int index;
            private final int max;
            {
                this.max = ASN1Sequence.this.size();
            }

            @Override
            public ASN1Primitive getLoadedObject() {
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public ASN1Encodable readObject() {
                if (this.index == this.max) {
                    return null;
                }
                ASN1Sequence aSN1Sequence = ASN1Sequence.this;
                int n2 = this.index;
                this.index = n2 + 1;
                ASN1Encodable aSN1Encodable = aSN1Sequence.getObjectAt(n2);
                if (aSN1Encodable instanceof ASN1Sequence) {
                    return ((ASN1Sequence)aSN1Encodable).parser();
                }
                if (!(aSN1Encodable instanceof ASN1Set)) return aSN1Encodable;
                return ((ASN1Set)aSN1Encodable).parser();
            }

            @Override
            public ASN1Primitive toASN1Primitive() {
                return this;
            }
        };
    }

    public int size() {
        return this.seq.size();
    }

    public ASN1Encodable[] toArray() {
        ASN1Encodable[] arraSN1Encodable = new ASN1Encodable[this.size()];
        for (int i2 = 0; i2 != this.size(); ++i2) {
            arraSN1Encodable[i2] = this.getObjectAt(i2);
        }
        return arraSN1Encodable;
    }

    @Override
    ASN1Primitive toDERObject() {
        DERSequence dERSequence = new DERSequence();
        dERSequence.seq = this.seq;
        return dERSequence;
    }

    @Override
    ASN1Primitive toDLObject() {
        DLSequence dLSequence = new DLSequence();
        dLSequence.seq = this.seq;
        return dLSequence;
    }

    public String toString() {
        return this.seq.toString();
    }

}

