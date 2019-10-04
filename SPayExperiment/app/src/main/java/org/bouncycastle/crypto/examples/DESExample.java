/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.BufferedInputStream
 *  java.io.BufferedOutputStream
 *  java.io.BufferedReader
 *  java.io.FileInputStream
 *  java.io.FileNotFoundException
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.OutputStream
 *  java.io.PrintStream
 *  java.io.Reader
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 *  org.bouncycastle.util.encoders.Hex
 */
package org.bouncycastle.crypto.examples;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.generators.DESedeKeyGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

public class DESExample {
    private PaddedBufferedBlockCipher cipher = null;
    private boolean encrypt = true;
    private BufferedInputStream in = null;
    private byte[] key = null;
    private BufferedOutputStream out = null;

    public DESExample() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public DESExample(String var1_1, String var2_2, String var3_3, boolean var4_4) {
        super();
        this.encrypt = var4_4;
        try {
            this.in = new BufferedInputStream((InputStream)new FileInputStream(var1_1));
        }
        catch (FileNotFoundException var5_10) {
            System.err.println("Input file not found [" + var1_1 + "]");
            System.exit((int)1);
            ** continue;
        }
lbl10: // 2 sources:
        do {
            this.out = new BufferedOutputStream((OutputStream)new FileOutputStream(var2_2));
lbl13: // 2 sources:
            ** while (var4_4)
lbl-1000: // 1 sources:
            {
                var12_5 = new SecureRandom();
                var12_5.setSeed("www.bouncycastle.org".getBytes());
lbl19: // 2 sources:
                do {
                    var15_6 = new KeyGenerationParameters(var12_5, 192);
                    var16_7 = new DESedeKeyGenerator();
                    var16_7.init(var15_6);
                    this.key = var16_7.generateKey();
                    var17_8 = new BufferedOutputStream((OutputStream)new FileOutputStream(var3_3));
                    var18_9 = Hex.encode((byte[])this.key);
                    var17_8.write(var18_9, 0, var18_9.length);
                    var17_8.flush();
                    var17_8.close();
                    return;
                    break;
                } while (true);
lbl30: // 1 sources:
            }
            break;
        } while (true);
        catch (IOException var6_11) {
            System.err.println("Output file not created [" + var2_2 + "]");
            System.exit((int)1);
            ** GOTO lbl13
        }
        {
            catch (Exception var19_12) {
                var12_5 = null;
lbl41: // 2 sources:
                do {
                    try {
                        System.err.println("Hmmm, no SHA1PRNG, you need the Sun implementation");
                        System.exit((int)1);
                        ** continue;
                    }
                    catch (IOException var14_13) {
                        System.err.println("Could not decryption create key file [" + var3_3 + "]");
                        System.exit((int)1);
                        return;
                    }
                    break;
                } while (true);
            }
        }
        try {
            var7_14 = new BufferedInputStream((InputStream)new FileInputStream(var3_3));
            var9_15 = var7_14.available();
            var10_16 = new byte[var9_15];
            var7_14.read(var10_16, 0, var9_15);
            this.key = Hex.decode((byte[])var10_16);
            return;
        }
        catch (IOException var8_17) {
            System.err.println("Decryption key file not found, or not valid [" + var3_3 + "]");
            System.exit((int)1);
            return;
        }
        {
            catch (Exception var13_18) {
                ** continue;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void main(String[] arrstring) {
        boolean bl = false;
        if (arrstring.length < 2) {
            DESExample dESExample = new DESExample();
            System.err.println("Usage: java " + dESExample.getClass().getName() + " infile outfile [keyfile]");
            System.exit((int)1);
        }
        String string = "deskey.dat";
        String string2 = arrstring[0];
        String string3 = arrstring[1];
        if (arrstring.length > 2) {
            string = arrstring[2];
        } else {
            bl = true;
        }
        new DESExample(string2, string3, string, bl).process();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void performDecrypt(byte[] arrby) {
        this.cipher.init(false, new KeyParameter(arrby));
        BufferedReader bufferedReader = new BufferedReader((Reader)new InputStreamReader((InputStream)this.in));
        byte[] arrby2 = null;
        try {
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                byte[] arrby3 = Hex.decode((String)string);
                int n2 = this.cipher.processBytes(arrby3, 0, arrby3.length, arrby2 = new byte[this.cipher.getOutputSize(arrby3.length)], 0);
                if (n2 <= 0) continue;
                this.out.write(arrby2, 0, n2);
            }
            int n3 = this.cipher.doFinal(arrby2, 0);
            if (n3 <= 0) return;
            {
                this.out.write(arrby2, 0, n3);
                return;
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void performEncrypt(byte[] arrby) {
        this.cipher.init(true, new KeyParameter(arrby));
        int n2 = this.cipher.getOutputSize(47);
        byte[] arrby2 = new byte[47];
        byte[] arrby3 = new byte[n2];
        try {
            int n3;
            while ((n3 = this.in.read(arrby2, 0, 47)) > 0) {
                int n4 = this.cipher.processBytes(arrby2, 0, n3, arrby3, 0);
                if (n4 <= 0) continue;
                byte[] arrby4 = Hex.encode((byte[])arrby3, (int)0, (int)n4);
                this.out.write(arrby4, 0, arrby4.length);
                this.out.write(10);
            }
            int n5 = this.cipher.doFinal(arrby3, 0);
            if (n5 <= 0) return;
            {
                byte[] arrby5 = Hex.encode((byte[])arrby3, (int)0, (int)n5);
                this.out.write(arrby5, 0, arrby5.length);
                this.out.write(10);
                return;
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void process() {
        this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()));
        if (this.encrypt) {
            this.performEncrypt(this.key);
        } else {
            this.performDecrypt(this.key);
        }
        try {
            this.in.close();
            this.out.flush();
            this.out.close();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }
}

