/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Vector
 */
package org.bouncycastle.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;
import org.bouncycastle.util.StringList;

public final class Strings {
    public static char[] asCharArray(byte[] arrby) {
        char[] arrc = new char[arrby.length];
        for (int i = 0; i != arrc.length; ++i) {
            arrc[i] = (char)(255 & arrby[i]);
        }
        return arrc;
    }

    public static String fromByteArray(byte[] arrby) {
        return new String(Strings.asCharArray(arrby));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String fromUTF8ByteArray(byte[] arrby) {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (n3 < arrby.length) {
            ++n2;
            if ((240 & arrby[n3]) == 240) {
                ++n2;
                n3 += 4;
                continue;
            }
            if ((224 & arrby[n3]) == 224) {
                n3 += 3;
                continue;
            }
            if ((192 & arrby[n3]) == 192) {
                n3 += 2;
                continue;
            }
            ++n3;
        }
        char[] arrc = new char[n2];
        int n4 = 0;
        while (n4 < arrby.length) {
            int n5;
            char c;
            if ((240 & arrby[n4]) == 240) {
                int n6 = ((3 & arrby[n4]) << 18 | (63 & arrby[n4 + 1]) << 12 | (63 & arrby[n4 + 2]) << 6 | 63 & arrby[n4 + 3]) - 65536;
                char c2 = (char)(55296 | n6 >> 10);
                c = (char)(56320 | n6 & 1023);
                n5 = n + 1;
                arrc[n] = c2;
                n4 += 4;
            } else if ((224 & arrby[n4]) == 224) {
                c = (char)((15 & arrby[n4]) << 12 | (63 & arrby[n4 + 1]) << 6 | 63 & arrby[n4 + 2]);
                n4 += 3;
                n5 = n;
            } else if ((208 & arrby[n4]) == 208) {
                c = (char)((31 & arrby[n4]) << 6 | 63 & arrby[n4 + 1]);
                n4 += 2;
                n5 = n;
            } else if ((192 & arrby[n4]) == 192) {
                c = (char)((31 & arrby[n4]) << 6 | 63 & arrby[n4 + 1]);
                n4 += 2;
                n5 = n;
            } else {
                c = (char)(255 & arrby[n4]);
                ++n4;
                n5 = n;
            }
            n = n5 + 1;
            arrc[n5] = c;
        }
        return new String(arrc);
    }

    public static StringList newList() {
        return new StringListImpl();
    }

    public static String[] split(String string, char c) {
        int n = 0;
        Vector vector = new Vector();
        boolean bl = true;
        String string2 = string;
        while (bl) {
            int n2 = string2.indexOf((int)c);
            if (n2 > 0) {
                vector.addElement((Object)string2.substring(0, n2));
                string2 = string2.substring(n2 + 1);
                continue;
            }
            vector.addElement((Object)string2);
            bl = false;
        }
        String[] arrstring = new String[vector.size()];
        while (n != arrstring.length) {
            arrstring[n] = (String)vector.elementAt(n);
            ++n;
        }
        return arrstring;
    }

    public static int toByteArray(String string, byte[] arrby, int n) {
        int n2 = string.length();
        for (int i = 0; i < n2; ++i) {
            char c = string.charAt(i);
            arrby[n + i] = (byte)c;
        }
        return n2;
    }

    public static byte[] toByteArray(String string) {
        byte[] arrby = new byte[string.length()];
        for (int i = 0; i != arrby.length; ++i) {
            arrby[i] = (byte)string.charAt(i);
        }
        return arrby;
    }

    public static byte[] toByteArray(char[] arrc) {
        byte[] arrby = new byte[arrc.length];
        for (int i = 0; i != arrby.length; ++i) {
            arrby[i] = (byte)arrc[i];
        }
        return arrby;
    }

    public static String toLowerCase(String string) {
        char[] arrc = string.toCharArray();
        boolean bl = false;
        for (int i = 0; i != arrc.length; ++i) {
            char c = arrc[i];
            if ('A' > c || 'Z' < c) continue;
            bl = true;
            arrc[i] = (char)(97 + (c - 65));
        }
        if (bl) {
            string = new String(arrc);
        }
        return string;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void toUTF8ByteArray(char[] arrc, OutputStream outputStream) {
        int n = 0;
        while (n < arrc.length) {
            char c = arrc[n];
            if (c < '') {
                outputStream.write((int)c);
            } else if (c < '\u0800') {
                outputStream.write(192 | c >> 6);
                outputStream.write(128 | c & 63);
            } else if (c >= '\ud800' && c <= '\udfff') {
                if (n + 1 >= arrc.length) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                char c2 = arrc[++n];
                if (c > '\udbff') {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                int n2 = 65536 + ((c & 1023) << 10 | c2 & 1023);
                outputStream.write(240 | n2 >> 18);
                outputStream.write(128 | 63 & n2 >> 12);
                outputStream.write(128 | 63 & n2 >> 6);
                outputStream.write(128 | n2 & 63);
            } else {
                outputStream.write(224 | c >> 12);
                outputStream.write(128 | 63 & c >> 6);
                outputStream.write(128 | c & 63);
            }
            ++n;
        }
        return;
    }

    public static byte[] toUTF8ByteArray(String string) {
        return Strings.toUTF8ByteArray(string.toCharArray());
    }

    public static byte[] toUTF8ByteArray(char[] arrc) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Strings.toUTF8ByteArray(arrc, (OutputStream)byteArrayOutputStream);
        }
        catch (IOException iOException) {
            throw new IllegalStateException("cannot encode string to byte array!");
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String toUpperCase(String string) {
        char[] arrc = string.toCharArray();
        boolean bl = false;
        for (int i = 0; i != arrc.length; ++i) {
            char c = arrc[i];
            if ('a' > c || 'z' < c) continue;
            bl = true;
            arrc[i] = (char)(65 + (c - 97));
        }
        if (bl) {
            string = new String(arrc);
        }
        return string;
    }

    private static class StringListImpl
    extends ArrayList<String>
    implements StringList {
        private StringListImpl() {
        }

        public void add(int n, String string) {
            super.add(n, (Object)string);
        }

        @Override
        public boolean add(String string) {
            return super.add((Object)string);
        }

        public String set(int n, String string) {
            return (String)super.set(n, (Object)string);
        }

        @Override
        public String[] toStringArray() {
            String[] arrstring = new String[this.size()];
            for (int i = 0; i != arrstring.length; ++i) {
                arrstring[i] = (String)this.get(i);
            }
            return arrstring;
        }

        @Override
        public String[] toStringArray(int n, int n2) {
            String[] arrstring = new String[n2 - n];
            for (int i = n; i != this.size() && i != n2; ++i) {
                arrstring[i - n] = (String)this.get(i);
            }
            return arrstring;
        }
    }

}

