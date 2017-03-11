package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public interface CommitmentTypeIdentifier {
    public static final ASN1ObjectIdentifier proofOfApproval;
    public static final ASN1ObjectIdentifier proofOfCreation;
    public static final ASN1ObjectIdentifier proofOfDelivery;
    public static final ASN1ObjectIdentifier proofOfOrigin;
    public static final ASN1ObjectIdentifier proofOfReceipt;
    public static final ASN1ObjectIdentifier proofOfSender;

    static {
        proofOfOrigin = PKCSObjectIdentifiers.id_cti_ets_proofOfOrigin;
        proofOfReceipt = PKCSObjectIdentifiers.id_cti_ets_proofOfReceipt;
        proofOfDelivery = PKCSObjectIdentifiers.id_cti_ets_proofOfDelivery;
        proofOfSender = PKCSObjectIdentifiers.id_cti_ets_proofOfSender;
        proofOfApproval = PKCSObjectIdentifiers.id_cti_ets_proofOfApproval;
        proofOfCreation = PKCSObjectIdentifiers.id_cti_ets_proofOfCreation;
    }
}
