package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Sequence extends ASN1Primitive {
    protected Vector seq;

    /* renamed from: org.bouncycastle.asn1.ASN1Sequence.1 */
    class C07051 implements ASN1SequenceParser {
        private int index;
        private final int max;
        final /* synthetic */ ASN1Sequence val$outer;

        C07051(ASN1Sequence aSN1Sequence) {
            this.val$outer = aSN1Sequence;
            this.max = ASN1Sequence.this.size();
        }

        public ASN1Primitive getLoadedObject() {
            return this.val$outer;
        }

        public ASN1Encodable readObject() {
            if (this.index == this.max) {
                return null;
            }
            ASN1Sequence aSN1Sequence = ASN1Sequence.this;
            int i = this.index;
            this.index = i + 1;
            ASN1Encodable objectAt = aSN1Sequence.getObjectAt(i);
            return objectAt instanceof ASN1Sequence ? ((ASN1Sequence) objectAt).parser() : objectAt instanceof ASN1Set ? ((ASN1Set) objectAt).parser() : objectAt;
        }

        public ASN1Primitive toASN1Primitive() {
            return this.val$outer;
        }
    }

    protected ASN1Sequence() {
        this.seq = new Vector();
    }

    protected ASN1Sequence(ASN1Encodable aSN1Encodable) {
        this.seq = new Vector();
        this.seq.addElement(aSN1Encodable);
    }

    protected ASN1Sequence(ASN1EncodableVector aSN1EncodableVector) {
        this.seq = new Vector();
        for (int i = 0; i != aSN1EncodableVector.size(); i++) {
            this.seq.addElement(aSN1EncodableVector.get(i));
        }
    }

    protected ASN1Sequence(ASN1Encodable[] aSN1EncodableArr) {
        this.seq = new Vector();
        for (int i = 0; i != aSN1EncodableArr.length; i++) {
            this.seq.addElement(aSN1EncodableArr[i]);
        }
    }

    public static ASN1Sequence getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Sequence)) {
            return (ASN1Sequence) obj;
        }
        if (obj instanceof ASN1SequenceParser) {
            return getInstance(((ASN1SequenceParser) obj).toASN1Primitive());
        }
        if (obj instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[]) obj));
            } catch (IOException e) {
                throw new IllegalArgumentException("failed to construct sequence from byte[]: " + e.getMessage());
            }
        }
        if (obj instanceof ASN1Encodable) {
            ASN1Primitive toASN1Primitive = ((ASN1Encodable) obj).toASN1Primitive();
            if (toASN1Primitive instanceof ASN1Sequence) {
                return (ASN1Sequence) toASN1Primitive;
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1Sequence getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            if (aSN1TaggedObject.isExplicit()) {
                return getInstance(aSN1TaggedObject.getObject().toASN1Primitive());
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        } else if (aSN1TaggedObject.isExplicit()) {
            return aSN1TaggedObject instanceof BERTaggedObject ? new BERSequence(aSN1TaggedObject.getObject()) : new DLSequence(aSN1TaggedObject.getObject());
        } else {
            if (aSN1TaggedObject.getObject() instanceof ASN1Sequence) {
                return (ASN1Sequence) aSN1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("unknown object in getInstance: " + aSN1TaggedObject.getClass().getName());
        }
    }

    private ASN1Encodable getNext(Enumeration enumeration) {
        return (ASN1Encodable) enumeration.nextElement();
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1Sequence)) {
            return false;
        }
        ASN1Sequence aSN1Sequence = (ASN1Sequence) aSN1Primitive;
        if (size() != aSN1Sequence.size()) {
            return false;
        }
        Enumeration objects = getObjects();
        Enumeration objects2 = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Encodable next = getNext(objects);
            ASN1Encodable next2 = getNext(objects2);
            ASN1Primitive toASN1Primitive = next.toASN1Primitive();
            ASN1Primitive toASN1Primitive2 = next2.toASN1Primitive();
            if (toASN1Primitive != toASN1Primitive2) {
                if (!toASN1Primitive.equals(toASN1Primitive2)) {
                    return false;
                }
            }
        }
        return true;
    }

    abstract void encode(ASN1OutputStream aSN1OutputStream);

    public ASN1Encodable getObjectAt(int i) {
        return (ASN1Encodable) this.seq.elementAt(i);
    }

    public Enumeration getObjects() {
        return this.seq.elements();
    }

    public int hashCode() {
        Enumeration objects = getObjects();
        int size = size();
        while (objects.hasMoreElements()) {
            size = (size * 17) ^ getNext(objects).hashCode();
        }
        return size;
    }

    boolean isConstructed() {
        return true;
    }

    public ASN1SequenceParser parser() {
        return new C07051(this);
    }

    public int size() {
        return this.seq.size();
    }

    public ASN1Encodable[] toArray() {
        ASN1Encodable[] aSN1EncodableArr = new ASN1Encodable[size()];
        for (int i = 0; i != size(); i++) {
            aSN1EncodableArr[i] = getObjectAt(i);
        }
        return aSN1EncodableArr;
    }

    ASN1Primitive toDERObject() {
        ASN1Primitive dERSequence = new DERSequence();
        dERSequence.seq = this.seq;
        return dERSequence;
    }

    ASN1Primitive toDLObject() {
        ASN1Primitive dLSequence = new DLSequence();
        dLSequence.seq = this.seq;
        return dLSequence;
    }

    public String toString() {
        return this.seq.toString();
    }
}
