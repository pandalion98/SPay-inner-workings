/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayauth.sdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface a
extends IInterface {
    public int N();

    public int O();

    public int P();

    public int Q();

    public byte[] R();

    public int S();

    public int[] T();

    public void V();

    public int a(int var1, int[] var2);

    public int a(String var1, String var2, String var3, String var4, String var5, String var6, byte[] var7);

    public int a(boolean var1);

    public int a(boolean var1, boolean var2);

    public int a(byte[] var1, int var2, int var3);

    public int a(byte[] var1, int var2, int var3, int var4, int var5);

    public int a(byte[] var1, int var2, int var3, int[] var4);

    public int a(byte[] var1, byte[] var2, int var3, int var4);

    public int a(byte[] var1, byte[] var2, int var3, int var4, int var5, int var6, int var7);

    public int a(String[] var1);

    public byte[] a(String var1, byte[] var2);

    public int b(byte[] var1, int var2, int var3);

    public int b(byte[] var1, byte[] var2);

    public int b(String[] var1);

    public int c(byte[] var1);

    public int d(byte[] var1);

    public int e(byte[] var1);

    public int f(byte[] var1);

    public static abstract class a
    extends Binder
    implements a {
        public a() {
            this.attachInterface((IInterface)this, "com.samsung.android.spayauth.sdk.IAuthFramework");
        }

        public IBinder asBinder() {
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onTransact(int n2, Parcel parcel, Parcel parcel2, int n3) {
            switch (n2) {
                default: {
                    return super.onTransact(n2, parcel, parcel2, n3);
                }
                case 1598968902: {
                    parcel2.writeString("com.samsung.android.spayauth.sdk.IAuthFramework");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n4 = this.N();
                    parcel2.writeNoException();
                    parcel2.writeInt(n4);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n5 = this.O();
                    parcel2.writeNoException();
                    parcel2.writeInt(n5);
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n6 = this.a(parcel.createStringArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n6);
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n7 = this.a(parcel.createByteArray(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(n7);
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n8 = this.a(parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(n8);
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n9 = this.b(parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(n9);
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n10 = this.a(parcel.createByteArray(), parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(n10);
                    return true;
                }
                case 8: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n11 = this.b(parcel.createByteArray(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n11);
                    return true;
                }
                case 9: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n12 = this.c(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n12);
                    return true;
                }
                case 10: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    boolean bl = parcel.readInt() != 0;
                    int n13 = parcel.readInt();
                    boolean bl2 = false;
                    if (n13 != 0) {
                        bl2 = true;
                    }
                    int n14 = this.a(bl, bl2);
                    parcel2.writeNoException();
                    parcel2.writeInt(n14);
                    return true;
                }
                case 11: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n15 = parcel.readInt();
                    boolean bl = false;
                    if (n15 != 0) {
                        bl = true;
                    }
                    int n16 = this.a(bl);
                    parcel2.writeNoException();
                    parcel2.writeInt(n16);
                    return true;
                }
                case 12: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    byte[] arrby = this.a(parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(arrby);
                    return true;
                }
                case 13: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n17 = this.d(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n17);
                    return true;
                }
                case 14: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n18 = this.P();
                    parcel2.writeNoException();
                    parcel2.writeInt(n18);
                    return true;
                }
                case 15: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n19 = this.Q();
                    parcel2.writeNoException();
                    parcel2.writeInt(n19);
                    return true;
                }
                case 16: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n20 = this.b(parcel.createStringArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n20);
                    return true;
                }
                case 17: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    byte[] arrby = this.R();
                    parcel2.writeNoException();
                    parcel2.writeByteArray(arrby);
                    return true;
                }
                case 18: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n21 = this.S();
                    parcel2.writeNoException();
                    parcel2.writeInt(n21);
                    return true;
                }
                case 19: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n22 = this.e(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n22);
                    return true;
                }
                case 20: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n23 = this.f(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n23);
                    return true;
                }
                case 21: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n24 = this.a(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n24);
                    return true;
                }
                case 22: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n25 = this.a(parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.createIntArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n25);
                    return true;
                }
                case 23: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int[] arrn = this.T();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(arrn);
                    return true;
                }
                case 24: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n26 = this.a(parcel.readInt(), parcel.createIntArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(n26);
                    return true;
                }
                case 25: {
                    parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
                    int n27 = this.a(parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(n27);
                    return true;
                }
                case 26: 
            }
            parcel.enforceInterface("com.samsung.android.spayauth.sdk.IAuthFramework");
            this.V();
            parcel2.writeNoException();
            return true;
        }
    }

}

