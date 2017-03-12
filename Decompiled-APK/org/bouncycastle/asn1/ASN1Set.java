package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public abstract class ASN1Set extends ASN1Primitive {
    private boolean isSorted;
    private Vector set;

    /* renamed from: org.bouncycastle.asn1.ASN1Set.1 */
    class C07061 implements ASN1SetParser {
        private int index;
        private final int max;
        final /* synthetic */ ASN1Set val$outer;

        C07061(ASN1Set aSN1Set) {
            this.val$outer = aSN1Set;
            this.max = ASN1Set.this.size();
        }

        public ASN1Primitive getLoadedObject() {
            return this.val$outer;
        }

        public ASN1Encodable readObject() {
            if (this.index == this.max) {
                return null;
            }
            ASN1Set aSN1Set = ASN1Set.this;
            int i = this.index;
            this.index = i + 1;
            ASN1Encodable objectAt = aSN1Set.getObjectAt(i);
            return objectAt instanceof ASN1Sequence ? ((ASN1Sequence) objectAt).parser() : objectAt instanceof ASN1Set ? ((ASN1Set) objectAt).parser() : objectAt;
        }

        public ASN1Primitive toASN1Primitive() {
            return this.val$outer;
        }
    }

    protected ASN1Set() {
        this.set = new Vector();
        this.isSorted = false;
    }

    protected ASN1Set(ASN1Encodable aSN1Encodable) {
        this.set = new Vector();
        this.isSorted = false;
        this.set.addElement(aSN1Encodable);
    }

    protected ASN1Set(ASN1EncodableVector aSN1EncodableVector, boolean z) {
        int i = 0;
        this.set = new Vector();
        this.isSorted = false;
        while (i != aSN1EncodableVector.size()) {
            this.set.addElement(aSN1EncodableVector.get(i));
            i++;
        }
        if (z) {
            sort();
        }
    }

    protected ASN1Set(ASN1Encodable[] aSN1EncodableArr, boolean z) {
        int i = 0;
        this.set = new Vector();
        this.isSorted = false;
        while (i != aSN1EncodableArr.length) {
            this.set.addElement(aSN1EncodableArr[i]);
            i++;
        }
        if (z) {
            sort();
        }
    }

    private byte[] getDEREncoded(ASN1Encodable aSN1Encodable) {
        try {
            return aSN1Encodable.toASN1Primitive().getEncoded(ASN1Encoding.DER);
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }
    }

    public static ASN1Set getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Set)) {
            return (ASN1Set) obj;
        }
        if (obj instanceof ASN1SetParser) {
            return getInstance(((ASN1SetParser) obj).toASN1Primitive());
        }
        if (obj instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[]) obj));
            } catch (IOException e) {
                throw new IllegalArgumentException("failed to construct set from byte[]: " + e.getMessage());
            }
        }
        if (obj instanceof ASN1Encodable) {
            ASN1Primitive toASN1Primitive = ((ASN1Encodable) obj).toASN1Primitive();
            if (toASN1Primitive instanceof ASN1Set) {
                return (ASN1Set) toASN1Primitive;
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1Set getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            if (aSN1TaggedObject.isExplicit()) {
                return (ASN1Set) aSN1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        } else if (aSN1TaggedObject.isExplicit()) {
            return aSN1TaggedObject instanceof BERTaggedObject ? new BERSet(aSN1TaggedObject.getObject()) : new DLSet(aSN1TaggedObject.getObject());
        } else {
            if (aSN1TaggedObject.getObject() instanceof ASN1Set) {
                return (ASN1Set) aSN1TaggedObject.getObject();
            }
            if (aSN1TaggedObject.getObject() instanceof ASN1Sequence) {
                ASN1Sequence aSN1Sequence = (ASN1Sequence) aSN1TaggedObject.getObject();
                return aSN1TaggedObject instanceof BERTaggedObject ? new BERSet(aSN1Sequence.toArray()) : new DLSet(aSN1Sequence.toArray());
            } else {
                throw new IllegalArgumentException("unknown object in getInstance: " + aSN1TaggedObject.getClass().getName());
            }
        }
    }

    private ASN1Encodable getNext(Enumeration enumeration) {
        ASN1Encodable aSN1Encodable = (ASN1Encodable) enumeration.nextElement();
        return aSN1Encodable == null ? DERNull.INSTANCE : aSN1Encodable;
    }

    private boolean lessThanOrEqual(byte[] bArr, byte[] bArr2) {
        int min = Math.min(bArr.length, bArr2.length);
        int i = 0;
        while (i != min) {
            if (bArr[i] != bArr2[i]) {
                return (bArr[i] & GF2Field.MASK) < (bArr2[i] & GF2Field.MASK);
            } else {
                i++;
            }
        }
        return min == bArr.length;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1Set)) {
            return false;
        }
        ASN1Set aSN1Set = (ASN1Set) aSN1Primitive;
        if (size() != aSN1Set.size()) {
            return false;
        }
        Enumeration objects = getObjects();
        Enumeration objects2 = aSN1Set.getObjects();
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
        return (ASN1Encodable) this.set.elementAt(i);
    }

    public Enumeration getObjects() {
        return this.set.elements();
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

    public ASN1SetParser parser() {
        return new C07061(this);
    }

    public int size() {
        return this.set.size();
    }

    protected void sort() {
        if (!this.isSorted) {
            this.isSorted = true;
            if (this.set.size() > 1) {
                int size = this.set.size() - 1;
                boolean z = true;
                while (z) {
                    byte[] dEREncoded = getDEREncoded((ASN1Encodable) this.set.elementAt(0));
                    int i = 0;
                    int i2 = 0;
                    z = false;
                    while (i2 != size) {
                        int i3;
                        boolean z2;
                        byte[] dEREncoded2 = getDEREncoded((ASN1Encodable) this.set.elementAt(i2 + 1));
                        if (lessThanOrEqual(dEREncoded, dEREncoded2)) {
                            i3 = i;
                            z2 = z;
                        } else {
                            Object elementAt = this.set.elementAt(i2);
                            this.set.setElementAt(this.set.elementAt(i2 + 1), i2);
                            this.set.setElementAt(elementAt, i2 + 1);
                            dEREncoded2 = dEREncoded;
                            z2 = true;
                            i3 = i2;
                        }
                        i2++;
                        z = z2;
                        i = i3;
                        dEREncoded = dEREncoded2;
                    }
                    size = i;
                }
            }
        }
    }

    public ASN1Encodable[] toArray() {
        ASN1Encodable[] aSN1EncodableArr = new ASN1Encodable[size()];
        for (int i = 0; i != size(); i++) {
            aSN1EncodableArr[i] = getObjectAt(i);
        }
        return aSN1EncodableArr;
    }

    ASN1Primitive toDERObject() {
        if (this.isSorted) {
            ASN1Primitive dERSet = new DERSet();
            dERSet.set = this.set;
            return dERSet;
        }
        Vector vector = new Vector();
        for (int i = 0; i != this.set.size(); i++) {
            vector.addElement(this.set.elementAt(i));
        }
        dERSet = new DERSet();
        dERSet.set = vector;
        dERSet.sort();
        return dERSet;
    }

    ASN1Primitive toDLObject() {
        ASN1Primitive dLSet = new DLSet();
        dLSet.set = this.set;
        return dLSet;
    }

    public String toString() {
        return this.set.toString();
    }
}
