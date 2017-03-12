package org.bouncycastle.crypto.params;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.util.Integers;

public class SkeinParameters implements CipherParameters {
    public static final int PARAM_TYPE_CONFIG = 4;
    public static final int PARAM_TYPE_KEY = 0;
    public static final int PARAM_TYPE_KEY_IDENTIFIER = 16;
    public static final int PARAM_TYPE_MESSAGE = 48;
    public static final int PARAM_TYPE_NONCE = 20;
    public static final int PARAM_TYPE_OUTPUT = 63;
    public static final int PARAM_TYPE_PERSONALISATION = 8;
    public static final int PARAM_TYPE_PUBLIC_KEY = 12;
    private Hashtable parameters;

    public static class Builder {
        private Hashtable parameters;

        public Builder() {
            this.parameters = new Hashtable();
        }

        public Builder(Hashtable hashtable) {
            this.parameters = new Hashtable();
            Enumeration keys = hashtable.keys();
            while (keys.hasMoreElements()) {
                Integer num = (Integer) keys.nextElement();
                this.parameters.put(num, hashtable.get(num));
            }
        }

        public Builder(SkeinParameters skeinParameters) {
            this.parameters = new Hashtable();
            Enumeration keys = skeinParameters.parameters.keys();
            while (keys.hasMoreElements()) {
                Integer num = (Integer) keys.nextElement();
                this.parameters.put(num, skeinParameters.parameters.get(num));
            }
        }

        public SkeinParameters build() {
            return new SkeinParameters(null);
        }

        public Builder set(int i, byte[] bArr) {
            if (bArr == null) {
                throw new IllegalArgumentException("Parameter value must not be null.");
            } else if (i != 0 && (i <= SkeinParameters.PARAM_TYPE_CONFIG || i >= SkeinParameters.PARAM_TYPE_OUTPUT || i == SkeinParameters.PARAM_TYPE_MESSAGE)) {
                throw new IllegalArgumentException("Parameter types must be in the range 0,5..47,49..62.");
            } else if (i == SkeinParameters.PARAM_TYPE_CONFIG) {
                throw new IllegalArgumentException("Parameter type 4 is reserved for internal use.");
            } else {
                this.parameters.put(Integers.valueOf(i), bArr);
                return this;
            }
        }

        public Builder setKey(byte[] bArr) {
            return set(SkeinParameters.PARAM_TYPE_KEY, bArr);
        }

        public Builder setKeyIdentifier(byte[] bArr) {
            return set(SkeinParameters.PARAM_TYPE_KEY_IDENTIFIER, bArr);
        }

        public Builder setNonce(byte[] bArr) {
            return set(SkeinParameters.PARAM_TYPE_NONCE, bArr);
        }

        public Builder setPersonalisation(Date date, String str, String str2) {
            try {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
                outputStreamWriter.write(new SimpleDateFormat("YYYYMMDD").format(date));
                outputStreamWriter.write(" ");
                outputStreamWriter.write(str);
                outputStreamWriter.write(" ");
                outputStreamWriter.write(str2);
                outputStreamWriter.close();
                return set(SkeinParameters.PARAM_TYPE_PERSONALISATION, byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException("Byte I/O failed: " + e);
            }
        }

        public Builder setPersonalisation(Date date, Locale locale, String str, String str2) {
            try {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
                outputStreamWriter.write(new SimpleDateFormat("YYYYMMDD", locale).format(date));
                outputStreamWriter.write(" ");
                outputStreamWriter.write(str);
                outputStreamWriter.write(" ");
                outputStreamWriter.write(str2);
                outputStreamWriter.close();
                return set(SkeinParameters.PARAM_TYPE_PERSONALISATION, byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException("Byte I/O failed: " + e);
            }
        }

        public Builder setPersonalisation(byte[] bArr) {
            return set(SkeinParameters.PARAM_TYPE_PERSONALISATION, bArr);
        }

        public Builder setPublicKey(byte[] bArr) {
            return set(SkeinParameters.PARAM_TYPE_PUBLIC_KEY, bArr);
        }
    }

    public SkeinParameters() {
        this(new Hashtable());
    }

    private SkeinParameters(Hashtable hashtable) {
        this.parameters = hashtable;
    }

    public byte[] getKey() {
        return (byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_KEY));
    }

    public byte[] getKeyIdentifier() {
        return (byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_KEY_IDENTIFIER));
    }

    public byte[] getNonce() {
        return (byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_NONCE));
    }

    public Hashtable getParameters() {
        return this.parameters;
    }

    public byte[] getPersonalisation() {
        return (byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_PERSONALISATION));
    }

    public byte[] getPublicKey() {
        return (byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_PUBLIC_KEY));
    }
}
