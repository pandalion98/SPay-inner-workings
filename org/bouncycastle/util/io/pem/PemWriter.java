package org.bouncycastle.util.io.pem;

import java.io.BufferedWriter;
import java.io.Writer;
import org.bouncycastle.util.encoders.Base64;

public class PemWriter extends BufferedWriter {
    private static final int LINE_LENGTH = 64;
    private char[] buf;
    private final int nlLength;

    public PemWriter(Writer writer) {
        super(writer);
        this.buf = new char[LINE_LENGTH];
        String property = System.getProperty("line.separator");
        if (property != null) {
            this.nlLength = property.length();
        } else {
            this.nlLength = 2;
        }
    }

    private void writeEncoded(byte[] bArr) {
        byte[] encode = Base64.encode(bArr);
        int i = 0;
        while (i < encode.length) {
            int i2 = 0;
            while (i2 != this.buf.length && i + i2 < encode.length) {
                this.buf[i2] = (char) encode[i + i2];
                i2++;
            }
            write(this.buf, 0, i2);
            newLine();
            i += this.buf.length;
        }
    }

    private void writePostEncapsulationBoundary(String str) {
        write("-----END " + str + "-----");
        newLine();
    }

    private void writePreEncapsulationBoundary(String str) {
        write("-----BEGIN " + str + "-----");
        newLine();
    }

    public int getOutputSize(PemObject pemObject) {
        int i;
        int length = ((((pemObject.getType().length() + 10) + this.nlLength) * 2) + 6) + 4;
        if (!pemObject.getHeaders().isEmpty()) {
            i = length;
            for (PemHeader pemHeader : pemObject.getHeaders()) {
                i = ((pemHeader.getValue().length() + (pemHeader.getName().length() + ": ".length())) + this.nlLength) + i;
            }
            length = this.nlLength + i;
        }
        i = ((pemObject.getContent().length + 2) / 3) * 4;
        return length + (i + ((((i + LINE_LENGTH) - 1) / LINE_LENGTH) * this.nlLength));
    }

    public void writeObject(PemObjectGenerator pemObjectGenerator) {
        PemObject generate = pemObjectGenerator.generate();
        writePreEncapsulationBoundary(generate.getType());
        if (!generate.getHeaders().isEmpty()) {
            for (PemHeader pemHeader : generate.getHeaders()) {
                write(pemHeader.getName());
                write(": ");
                write(pemHeader.getValue());
                newLine();
            }
            newLine();
        }
        writeEncoded(generate.getContent());
        writePostEncapsulationBoundary(generate.getType());
    }
}
