package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.bouncycastle.util.Arrays;

public class ServerSRPParams {
    protected BigInteger f264B;
    protected BigInteger f265N;
    protected BigInteger f266g;
    protected byte[] f267s;

    public ServerSRPParams(BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr, BigInteger bigInteger3) {
        this.f265N = bigInteger;
        this.f266g = bigInteger2;
        this.f267s = Arrays.clone(bArr);
        this.f264B = bigInteger3;
    }

    public static ServerSRPParams parse(InputStream inputStream) {
        return new ServerSRPParams(TlsSRPUtils.readSRPParameter(inputStream), TlsSRPUtils.readSRPParameter(inputStream), TlsUtils.readOpaque8(inputStream), TlsSRPUtils.readSRPParameter(inputStream));
    }

    public void encode(OutputStream outputStream) {
        TlsSRPUtils.writeSRPParameter(this.f265N, outputStream);
        TlsSRPUtils.writeSRPParameter(this.f266g, outputStream);
        TlsUtils.writeOpaque8(this.f267s, outputStream);
        TlsSRPUtils.writeSRPParameter(this.f264B, outputStream);
    }

    public BigInteger getB() {
        return this.f264B;
    }

    public BigInteger getG() {
        return this.f266g;
    }

    public BigInteger getN() {
        return this.f265N;
    }

    public byte[] getS() {
        return this.f267s;
    }
}
