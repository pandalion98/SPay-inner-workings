package org.bouncycastle.jcajce.spec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class SkeinParameterSpec implements AlgorithmParameterSpec {
    public static final int PARAM_TYPE_CONFIG = 4;
    public static final int PARAM_TYPE_KEY = 0;
    public static final int PARAM_TYPE_KEY_IDENTIFIER = 16;
    public static final int PARAM_TYPE_MESSAGE = 48;
    public static final int PARAM_TYPE_NONCE = 20;
    public static final int PARAM_TYPE_OUTPUT = 63;
    public static final int PARAM_TYPE_PERSONALISATION = 8;
    public static final int PARAM_TYPE_PUBLIC_KEY = 12;
    private Map parameters;

    public static class Builder {
        private Map parameters;

        public Builder() {
            this.parameters = new HashMap();
        }

        public Builder(SkeinParameterSpec skeinParameterSpec) {
            this.parameters = new HashMap();
            for (Integer num : skeinParameterSpec.parameters.keySet()) {
                this.parameters.put(num, skeinParameterSpec.parameters.get(num));
            }
        }

        public SkeinParameterSpec build() {
            return new SkeinParameterSpec(null);
        }

        public Builder set(int i, byte[] bArr) {
            if (bArr == null) {
                throw new IllegalArgumentException("Parameter value must not be null.");
            } else if (i != 0 && (i <= SkeinParameterSpec.PARAM_TYPE_CONFIG || i >= SkeinParameterSpec.PARAM_TYPE_OUTPUT || i == SkeinParameterSpec.PARAM_TYPE_MESSAGE)) {
                throw new IllegalArgumentException("Parameter types must be in the range 0,5..47,49..62.");
            } else if (i == SkeinParameterSpec.PARAM_TYPE_CONFIG) {
                throw new IllegalArgumentException("Parameter type 4 is reserved for internal use.");
            } else {
                this.parameters.put(Integers.valueOf(i), bArr);
                return this;
            }
        }

        public Builder setKey(byte[] bArr) {
            return set(SkeinParameterSpec.PARAM_TYPE_KEY, bArr);
        }

        public Builder setKeyIdentifier(byte[] bArr) {
            return set(SkeinParameterSpec.PARAM_TYPE_KEY_IDENTIFIER, bArr);
        }

        public Builder setNonce(byte[] bArr) {
            return set(SkeinParameterSpec.PARAM_TYPE_NONCE, bArr);
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
                return set(SkeinParameterSpec.PARAM_TYPE_PERSONALISATION, byteArrayOutputStream.toByteArray());
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
                return set(SkeinParameterSpec.PARAM_TYPE_PERSONALISATION, byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException("Byte I/O failed: " + e);
            }
        }

        public Builder setPersonalisation(byte[] bArr) {
            return set(SkeinParameterSpec.PARAM_TYPE_PERSONALISATION, bArr);
        }

        public Builder setPublicKey(byte[] bArr) {
            return set(SkeinParameterSpec.PARAM_TYPE_PUBLIC_KEY, bArr);
        }
    }

    public SkeinParameterSpec() {
        this(new HashMap());
    }

    private SkeinParameterSpec(Map map) {
        this.parameters = Collections.unmodifiableMap(map);
    }

    public byte[] getKey() {
        return Arrays.clone((byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_KEY)));
    }

    public byte[] getKeyIdentifier() {
        return Arrays.clone((byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_KEY_IDENTIFIER)));
    }

    public byte[] getNonce() {
        return Arrays.clone((byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_NONCE)));
    }

    public Map getParameters() {
        return this.parameters;
    }

    public byte[] getPersonalisation() {
        return Arrays.clone((byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_PERSONALISATION)));
    }

    public byte[] getPublicKey() {
        return Arrays.clone((byte[]) this.parameters.get(Integers.valueOf(PARAM_TYPE_PUBLIC_KEY)));
    }
}
