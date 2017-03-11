package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.exception.APDUCommandProcessorException;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class APDUCommandProcessorFactory {

    /* renamed from: com.americanexpress.sdkmodulelib.apdu.APDUCommandProcessorFactory.1 */
    static /* synthetic */ class C00731 {
        static final /* synthetic */ int[] f4xb31c09f7;

        static {
            f4xb31c09f7 = new int[APDURequestCommand.values().length];
            try {
                f4xb31c09f7[APDURequestCommand.SELECT_PPSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f4xb31c09f7[APDURequestCommand.SELECT_AID.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f4xb31c09f7[APDURequestCommand.GPO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f4xb31c09f7[APDURequestCommand.READ.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f4xb31c09f7[APDURequestCommand.GETDATA.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f4xb31c09f7[APDURequestCommand.GENAC.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public static CommandProcessor getCommandProcessor(CommandInfo commandInfo) {
        if (commandInfo == null) {
            throw new APDUCommandProcessorException("CommandInfo is null or cannot be recognized");
        }
        switch (C00731.f4xb31c09f7[commandInfo.getApdu().getCommand().ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new ApplicationSelection();
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return new ApplicationSelection();
            case F2m.PPB /*3*/:
                return new InitialApplicationProcessing();
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new ReadApplicationData();
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return new TerminalRiskManagementGetData();
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return new FirstCardActionAnalysisGenAc();
            default:
                throw new APDUCommandProcessorException("Command not recognized: " + commandInfo.getApdu().getCommand());
        }
    }
}
