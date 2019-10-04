/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.io.OutputStreamWriter
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.AlgorithmParameterSpec
 *  java.text.SimpleDateFormat
 *  java.util.Collections
 *  java.util.Date
 *  java.util.HashMap
 *  java.util.Locale
 *  java.util.Map
 *  java.util.Set
 */
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
import java.util.Set;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class SkeinParameterSpec
implements AlgorithmParameterSpec {
    public static final int PARAM_TYPE_CONFIG = 4;
    public static final int PARAM_TYPE_KEY = 0;
    public static final int PARAM_TYPE_KEY_IDENTIFIER = 16;
    public static final int PARAM_TYPE_MESSAGE = 48;
    public static final int PARAM_TYPE_NONCE = 20;
    public static final int PARAM_TYPE_OUTPUT = 63;
    public static final int PARAM_TYPE_PERSONALISATION = 8;
    public static final int PARAM_TYPE_PUBLIC_KEY = 12;
    private Map parameters;

    public SkeinParameterSpec() {
        this((Map)new HashMap());
    }

    private SkeinParameterSpec(Map map) {
        this.parameters = Collections.unmodifiableMap((Map)map);
    }

    public byte[] getKey() {
        return Arrays.clone((byte[])this.parameters.get((Object)Integers.valueOf(0)));
    }

    public byte[] getKeyIdentifier() {
        return Arrays.clone((byte[])this.parameters.get((Object)Integers.valueOf(16)));
    }

    public byte[] getNonce() {
        return Arrays.clone((byte[])this.parameters.get((Object)Integers.valueOf(20)));
    }

    public Map getParameters() {
        return this.parameters;
    }

    public byte[] getPersonalisation() {
        return Arrays.clone((byte[])this.parameters.get((Object)Integers.valueOf(8)));
    }

    public byte[] getPublicKey() {
        return Arrays.clone((byte[])this.parameters.get((Object)Integers.valueOf(12)));
    }

    public static class Builder {
        private Map parameters = new HashMap();

        public Builder() {
        }

        public Builder(SkeinParameterSpec skeinParameterSpec) {
            for (Integer n : skeinParameterSpec.parameters.keySet()) {
                this.parameters.put((Object)n, skeinParameterSpec.parameters.get((Object)n));
            }
        }

        public SkeinParameterSpec build() {
            return new SkeinParameterSpec(this.parameters);
        }

        public Builder set(int n, byte[] arrby) {
            if (arrby == null) {
                throw new IllegalArgumentException("Parameter value must not be null.");
            }
            if (n != 0 && (n <= 4 || n >= 63 || n == 48)) {
                throw new IllegalArgumentException("Parameter types must be in the range 0,5..47,49..62.");
            }
            if (n == 4) {
                throw new IllegalArgumentException("Parameter type 4 is reserved for internal use.");
            }
            this.parameters.put((Object)Integers.valueOf(n), (Object)arrby);
            return this;
        }

        public Builder setKey(byte[] arrby) {
            return this.set(0, arrby);
        }

        public Builder setKeyIdentifier(byte[] arrby) {
            return this.set(16, arrby);
        }

        public Builder setNonce(byte[] arrby) {
            return this.set(20, arrby);
        }

        public Builder setPersonalisation(Date date, String string, String string2) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter((OutputStream)byteArrayOutputStream, "UTF-8");
                outputStreamWriter.write(new SimpleDateFormat("YYYYMMDD").format(date));
                outputStreamWriter.write(" ");
                outputStreamWriter.write(string);
                outputStreamWriter.write(" ");
                outputStreamWriter.write(string2);
                outputStreamWriter.close();
                Builder builder = this.set(8, byteArrayOutputStream.toByteArray());
                return builder;
            }
            catch (IOException iOException) {
                throw new IllegalStateException("Byte I/O failed: " + (Object)((Object)iOException));
            }
        }

        public Builder setPersonalisation(Date date, Locale locale, String string, String string2) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter((OutputStream)byteArrayOutputStream, "UTF-8");
                outputStreamWriter.write(new SimpleDateFormat("YYYYMMDD", locale).format(date));
                outputStreamWriter.write(" ");
                outputStreamWriter.write(string);
                outputStreamWriter.write(" ");
                outputStreamWriter.write(string2);
                outputStreamWriter.close();
                Builder builder = this.set(8, byteArrayOutputStream.toByteArray());
                return builder;
            }
            catch (IOException iOException) {
                throw new IllegalStateException("Byte I/O failed: " + (Object)((Object)iOException));
            }
        }

        public Builder setPersonalisation(byte[] arrby) {
            return this.set(8, arrby);
        }

        public Builder setPublicKey(byte[] arrby) {
            return this.set(12, arrby);
        }
    }

}

