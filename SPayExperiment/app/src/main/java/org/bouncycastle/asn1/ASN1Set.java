/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
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
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DLSet;

public abstract class ASN1Set
extends ASN1Primitive {
    private boolean isSorted = false;
    private Vector set = new Vector();

    protected ASN1Set() {
    }

    protected ASN1Set(ASN1Encodable aSN1Encodable) {
        this.set.addElement((Object)aSN1Encodable);
    }

    protected ASN1Set(ASN1EncodableVector aSN1EncodableVector, boolean bl) {
        for (int i2 = 0; i2 != aSN1EncodableVector.size(); ++i2) {
            this.set.addElement((Object)aSN1EncodableVector.get(i2));
        }
        if (bl) {
            this.sort();
        }
    }

    protected ASN1Set(ASN1Encodable[] arraSN1Encodable, boolean bl) {
        for (int i2 = 0; i2 != arraSN1Encodable.length; ++i2) {
            this.set.addElement((Object)arraSN1Encodable[i2]);
        }
        if (bl) {
            this.sort();
        }
    }

    private byte[] getDEREncoded(ASN1Encodable aSN1Encodable) {
        try {
            byte[] arrby = aSN1Encodable.toASN1Primitive().getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }
    }

    public static ASN1Set getInstance(Object object) {
        ASN1Primitive aSN1Primitive;
        if (object == null || object instanceof ASN1Set) {
            return (ASN1Set)object;
        }
        if (object instanceof ASN1SetParser) {
            return ASN1Set.getInstance(((ASN1SetParser)object).toASN1Primitive());
        }
        if (object instanceof byte[]) {
            try {
                ASN1Set aSN1Set = ASN1Set.getInstance(ASN1Primitive.fromByteArray((byte[])object));
                return aSN1Set;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct set from byte[]: " + iOException.getMessage());
            }
        }
        if (object instanceof ASN1Encodable && (aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive()) instanceof ASN1Set) {
            return (ASN1Set)aSN1Primitive;
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + object.getClass().getName());
    }

    public static ASN1Set getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        if (bl) {
            if (!aSN1TaggedObject.isExplicit()) {
                throw new IllegalArgumentException("object implicit - explicit expected.");
            }
            return (ASN1Set)aSN1TaggedObject.getObject();
        }
        if (aSN1TaggedObject.isExplicit()) {
            if (aSN1TaggedObject instanceof BERTaggedObject) {
                return new BERSet(aSN1TaggedObject.getObject());
            }
            return new DLSet(aSN1TaggedObject.getObject());
        }
        if (aSN1TaggedObject.getObject() instanceof ASN1Set) {
            return (ASN1Set)aSN1TaggedObject.getObject();
        }
        if (aSN1TaggedObject.getObject() instanceof ASN1Sequence) {
            ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1TaggedObject.getObject();
            if (aSN1TaggedObject instanceof BERTaggedObject) {
                return new BERSet(aSN1Sequence.toArray());
            }
            return new DLSet(aSN1Sequence.toArray());
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + aSN1TaggedObject.getClass().getName());
    }

    private ASN1Encodable getNext(Enumeration enumeration) {
        ASN1Encodable aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
        if (aSN1Encodable == null) {
            aSN1Encodable = DERNull.INSTANCE;
        }
        return aSN1Encodable;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean lessThanOrEqual(byte[] arrby, byte[] arrby2) {
        int n2 = Math.min((int)arrby.length, (int)arrby2.length);
        for (int i2 = 0; i2 != n2; ++i2) {
            if (arrby[i2] == arrby2[i2]) continue;
            if ((255 & arrby[i2]) < (255 & arrby2[i2])) return true;
            return false;
        }
        if (n2 != arrby.length) return false;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive instanceof ASN1Set) {
            ASN1Set aSN1Set = (ASN1Set)aSN1Primitive;
            if (this.size() == aSN1Set.size()) {
                ASN1Encodable aSN1Encodable;
                ASN1Primitive aSN1Primitive2;
                ASN1Encodable aSN1Encodable2;
                ASN1Primitive aSN1Primitive3;
                Enumeration enumeration = this.getObjects();
                Enumeration enumeration2 = aSN1Set.getObjects();
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
        return (ASN1Encodable)this.set.elementAt(n2);
    }

    public Enumeration getObjects() {
        return this.set.elements();
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

    public ASN1SetParser parser() {
        return new ASN1SetParser(){
            private int index;
            private final int max;
            {
                this.max = ASN1Set.this.size();
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
                ASN1Set aSN1Set = ASN1Set.this;
                int n2 = this.index;
                this.index = n2 + 1;
                ASN1Encodable aSN1Encodable = aSN1Set.getObjectAt(n2);
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
        return this.set.size();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void sort() {
        if (!this.isSorted) {
            this.isSorted = true;
            if (this.set.size() > 1) {
                int n2 = -1 + this.set.size();
                boolean bl = true;
                while (bl) {
                    byte[] arrby = this.getDEREncoded((ASN1Encodable)this.set.elementAt(0));
                    int n3 = 0;
                    bl = false;
                    for (int i2 = 0; i2 != n2; ++i2) {
                        int n4;
                        boolean bl2;
                        byte[] arrby2 = this.getDEREncoded((ASN1Encodable)this.set.elementAt(i2 + 1));
                        if (this.lessThanOrEqual(arrby, arrby2)) {
                            n4 = n3;
                            bl2 = bl;
                        } else {
                            Object object = this.set.elementAt(i2);
                            this.set.setElementAt(this.set.elementAt(i2 + 1), i2);
                            this.set.setElementAt(object, i2 + 1);
                            arrby2 = arrby;
                            bl2 = true;
                            n4 = i2;
                        }
                        bl = bl2;
                        n3 = n4;
                        arrby = arrby2;
                    }
                    n2 = n3;
                }
            }
        }
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
        if (this.isSorted) {
            DERSet dERSet = new DERSet();
            dERSet.set = this.set;
            return dERSet;
        }
        Vector vector = new Vector();
        for (int i2 = 0; i2 != this.set.size(); ++i2) {
            vector.addElement(this.set.elementAt(i2));
        }
        DERSet dERSet = new DERSet();
        dERSet.set = vector;
        dERSet.sort();
        return dERSet;
    }

    @Override
    ASN1Primitive toDLObject() {
        DLSet dLSet = new DLSet();
        dLSet.set = this.set;
        return dLSet;
    }

    public String toString() {
        return this.set.toString();
    }

}

