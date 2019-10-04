/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.tls;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.agreement.srp.SRP6VerifierGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.SRP6GroupParameters;
import org.bouncycastle.crypto.tls.TlsSRPIdentityManager;
import org.bouncycastle.crypto.tls.TlsSRPLoginParameters;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Strings;

public class SimulatedTlsSRPIdentityManager
implements TlsSRPIdentityManager {
    private static final byte[] PREFIX_PASSWORD = Strings.toByteArray((String)"password");
    private static final byte[] PREFIX_SALT = Strings.toByteArray((String)"salt");
    protected SRP6GroupParameters group;
    protected Mac mac;
    protected SRP6VerifierGenerator verifierGenerator;

    public SimulatedTlsSRPIdentityManager(SRP6GroupParameters sRP6GroupParameters, SRP6VerifierGenerator sRP6VerifierGenerator, Mac mac) {
        this.group = sRP6GroupParameters;
        this.verifierGenerator = sRP6VerifierGenerator;
        this.mac = mac;
    }

    public static SimulatedTlsSRPIdentityManager getRFC5054Default(SRP6GroupParameters sRP6GroupParameters, byte[] arrby) {
        SRP6VerifierGenerator sRP6VerifierGenerator = new SRP6VerifierGenerator();
        sRP6VerifierGenerator.init(sRP6GroupParameters, TlsUtils.createHash((short)2));
        HMac hMac = new HMac(TlsUtils.createHash((short)2));
        hMac.init(new KeyParameter(arrby));
        return new SimulatedTlsSRPIdentityManager(sRP6GroupParameters, sRP6VerifierGenerator, hMac);
    }

    @Override
    public TlsSRPLoginParameters getLoginParameters(byte[] arrby) {
        this.mac.update(PREFIX_SALT, 0, PREFIX_SALT.length);
        this.mac.update(arrby, 0, arrby.length);
        byte[] arrby2 = new byte[this.mac.getMacSize()];
        this.mac.doFinal(arrby2, 0);
        this.mac.update(PREFIX_PASSWORD, 0, PREFIX_PASSWORD.length);
        this.mac.update(arrby, 0, arrby.length);
        byte[] arrby3 = new byte[this.mac.getMacSize()];
        this.mac.doFinal(arrby3, 0);
        BigInteger bigInteger = this.verifierGenerator.generateVerifier(arrby2, arrby, arrby3);
        return new TlsSRPLoginParameters(this.group, bigInteger, arrby2);
    }
}

