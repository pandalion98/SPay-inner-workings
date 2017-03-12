package org.bouncycastle.asn1.dvcs;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;

public class ServiceType extends ASN1Object {
    public static final ServiceType CCPD;
    public static final ServiceType CPD;
    public static final ServiceType VPKC;
    public static final ServiceType VSD;
    private ASN1Enumerated value;

    static {
        CPD = new ServiceType(1);
        VSD = new ServiceType(2);
        VPKC = new ServiceType(3);
        CCPD = new ServiceType(4);
    }

    public ServiceType(int i) {
        this.value = new ASN1Enumerated(i);
    }

    private ServiceType(ASN1Enumerated aSN1Enumerated) {
        this.value = aSN1Enumerated;
    }

    public static ServiceType getInstance(Object obj) {
        return obj instanceof ServiceType ? (ServiceType) obj : obj != null ? new ServiceType(ASN1Enumerated.getInstance(obj)) : null;
    }

    public static ServiceType getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Enumerated.getInstance(aSN1TaggedObject, z));
    }

    public BigInteger getValue() {
        return this.value.getValue();
    }

    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }

    public String toString() {
        int intValue = this.value.getValue().intValue();
        StringBuilder append = new StringBuilder().append(BuildConfig.FLAVOR).append(intValue);
        String str = intValue == CPD.getValue().intValue() ? "(CPD)" : intValue == VSD.getValue().intValue() ? "(VSD)" : intValue == VPKC.getValue().intValue() ? "(VPKC)" : intValue == CCPD.getValue().intValue() ? "(CCPD)" : "?";
        return append.append(str).toString();
    }
}
