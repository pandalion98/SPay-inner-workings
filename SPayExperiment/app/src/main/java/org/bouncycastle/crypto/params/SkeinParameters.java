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
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Locale
 *  org.bouncycastle.util.Integers
 */
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

public class SkeinParameters
implements CipherParameters {
    public static final int PARAM_TYPE_CONFIG = 4;
    public static final int PARAM_TYPE_KEY = 0;
    public static final int PARAM_TYPE_KEY_IDENTIFIER = 16;
    public static final int PARAM_TYPE_MESSAGE = 48;
    public static final int PARAM_TYPE_NONCE = 20;
    public static final int PARAM_TYPE_OUTPUT = 63;
    public static final int PARAM_TYPE_PERSONALISATION = 8;
    public static final int PARAM_TYPE_PUBLIC_KEY = 12;
    private Hashtable parameters;

    public SkeinParameters() {
        this(new Hashtable());
    }

    private SkeinParameters(Hashtable hashtable) {
        this.parameters = hashtable;
    }

    public byte[] getKey() {
        return (byte[])this.parameters.get((Object)Integers.valueOf((int)0));
    }

    public byte[] getKeyIdentifier() {
        return (byte[])this.parameters.get((Object)Integers.valueOf((int)16));
    }

    public byte[] getNonce() {
        return (byte[])this.parameters.get((Object)Integers.valueOf((int)20));
    }

    public Hashtable getParameters() {
        return this.parameters;
    }

    public byte[] getPersonalisation() {
        return (byte[])this.parameters.get((Object)Integers.valueOf((int)8));
    }

    public byte[] getPublicKey() {
        return (byte[])this.parameters.get((Object)Integers.valueOf((int)12));
    }

    public static class Builder {
        private Hashtable parameters = new Hashtable();

        public Builder() {
        }

        public Builder(Hashtable hashtable) {
            Enumeration enumeration = hashtable.keys();
            while (enumeration.hasMoreElements()) {
                Integer n2 = (Integer)enumeration.nextElement();
                this.parameters.put((Object)n2, hashtable.get((Object)n2));
            }
        }

        public Builder(SkeinParameters skeinParameters) {
            Enumeration enumeration = skeinParameters.parameters.keys();
            while (enumeration.hasMoreElements()) {
                Integer n2 = (Integer)enumeration.nextElement();
                this.parameters.put((Object)n2, skeinParameters.parameters.get((Object)n2));
            }
        }

        public SkeinParameters build() {
            return new SkeinParameters(this.parameters);
        }

        public Builder set(int n2, byte[] arrby) {
            if (arrby == null) {
                throw new IllegalArgumentException("Parameter value must not be null.");
            }
            if (n2 != 0 && (n2 <= 4 || n2 >= 63 || n2 == 48)) {
                throw new IllegalArgumentException("Parameter types must be in the range 0,5..47,49..62.");
            }
            if (n2 == 4) {
                throw new IllegalArgumentException("Parameter type 4 is reserved for internal use.");
            }
            this.parameters.put((Object)Integers.valueOf((int)n2), (Object)arrby);
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

