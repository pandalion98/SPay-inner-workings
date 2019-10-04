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

public class SQLFilter
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
                case '\'': {
                    stringBuffer.replace(n2, n2 + 1, "\\'");
                    ++n2;
                    break;
                }
                case '\"': {
                    stringBuffer.replace(n2, n2 + 1, "\\\"");
                    ++n2;
                    break;
                }
                case '=': {
                    stringBuffer.replace(n2, n2 + 1, "\\=");
                    ++n2;
                    break;
                }
                case '-': {
                    stringBuffer.replace(n2, n2 + 1, "\\-");
                    ++n2;
                    break;
                }
                case '/': {
                    stringBuffer.replace(n2, n2 + 1, "\\/");
                    ++n2;
                    break;
                }
                case '\\': {
                    stringBuffer.replace(n2, n2 + 1, "\\\\");
                    ++n2;
                    break;
                }
                case ';': {
                    stringBuffer.replace(n2, n2 + 1, "\\;");
                    ++n2;
                    break;
                }
                case '\r': {
                    stringBuffer.replace(n2, n2 + 1, "\\r");
                    ++n2;
                    break;
                }
                case '\n': {
                    stringBuffer.replace(n2, n2 + 1, "\\n");
                    ++n2;
                    break;
                }
            }
            ++n2;
        }
        return stringBuffer.toString();
    }

    @Override
    public String doFilterUrl(String string) {
        return this.doFilter(string);
    }
}

