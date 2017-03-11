package com.samsung.sensorframework.sdm.datahandler.except;

import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;

public class DataHandlerException extends Exception {
    private static final long serialVersionUID = 8240175615135197888L;
    private final int errorCode;

    public String getMessage() {
        switch (this.errorCode) {
            case NamedCurve.sect283r1 /*10*/:
                return "Unknown config key.";
            case CertStatus.UNREVOKED /*11*/:
                return "Missing URL target.";
            case CertStatus.UNDETERMINED /*12*/:
                return "I/O Error!";
            case NamedCurve.sect571k1 /*13*/:
                return "This feature is unimplemented";
            case NamedCurve.sect571r1 /*14*/:
                return "Error: attempting to write to default directory.";
            case NamedCurve.secp160k1 /*15*/:
                return "Conflict in config values!";
            case X509KeyUsage.dataEncipherment /*16*/:
                return "Failure posting data to server.";
            case NamedCurve.secp160r2 /*17*/:
                return "Permission denied, check for required permissions.";
            default:
                return super.getMessage();
        }
    }
}
