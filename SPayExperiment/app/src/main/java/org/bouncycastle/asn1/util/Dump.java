/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.FileInputStream
 *  java.io.InputStream
 *  java.io.PrintStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.asn1.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.util.ASN1Dump;

public class Dump {
    public static void main(String[] arrstring) {
        ASN1Primitive aSN1Primitive;
        ASN1InputStream aSN1InputStream = new ASN1InputStream((InputStream)new FileInputStream(arrstring[0]));
        while ((aSN1Primitive = aSN1InputStream.readObject()) != null) {
            System.out.println(ASN1Dump.dumpAsString(aSN1Primitive));
        }
    }
}

