/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package org.bouncycastle.i18n.filter;

import org.bouncycastle.i18n.filter.Filter;

public class HTMLFilter
implements Filter {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String doFilter(String string) {
        StringBuffer stringBuffer = new StringBuffer(string);
        int n2 = 0;
        while (n2 < stringBuffer.length()) {
            switch (stringBuffer.charAt(n2)) {
                default: {
                    n2 -= 3;
                    break;
                }
                case '<': {
                    stringBuffer.replace(n2, n2 + 1, "&#60");
                    break;
                }
                case '>': {
                    stringBuffer.replace(n2, n2 + 1, "&#62");
                    break;
                }
                case '(': {
                    stringBuffer.replace(n2, n2 + 1, "&#40");
                    break;
                }
                case ')': {
                    stringBuffer.replace(n2, n2 + 1, "&#41");
                    break;
                }
                case '#': {
                    stringBuffer.replace(n2, n2 + 1, "&#35");
                    break;
                }
                case '&': {
                    stringBuffer.replace(n2, n2 + 1, "&#38");
                    break;
                }
                case '\"': {
                    stringBuffer.replace(n2, n2 + 1, "&#34");
                    break;
                }
                case '\'': {
                    stringBuffer.replace(n2, n2 + 1, "&#39");
                    break;
                }
                case '%': {
                    stringBuffer.replace(n2, n2 + 1, "&#37");
                    break;
                }
                case ';': {
                    stringBuffer.replace(n2, n2 + 1, "&#59");
                    break;
                }
                case '+': {
                    stringBuffer.replace(n2, n2 + 1, "&#43");
                    break;
                }
                case '-': {
                    stringBuffer.replace(n2, n2 + 1, "&#45");
                }
            }
            n2 += 4;
        }
        return stringBuffer.toString();
    }

    @Override
    public String doFilterUrl(String string) {
        return this.doFilter(string);
    }
}

