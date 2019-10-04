/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.UnsupportedEncodingException
 *  java.lang.ClassLoader
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.Throwable
 *  java.nio.charset.Charset
 *  java.text.DateFormat
 *  java.text.Format
 *  java.text.MessageFormat
 *  java.util.Locale
 *  java.util.MissingResourceException
 *  java.util.ResourceBundle
 *  java.util.TimeZone
 */
package org.bouncycastle.i18n;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;
import org.bouncycastle.i18n.LocaleString;
import org.bouncycastle.i18n.MissingEntryException;
import org.bouncycastle.i18n.filter.Filter;
import org.bouncycastle.i18n.filter.TrustedInput;
import org.bouncycastle.i18n.filter.UntrustedInput;
import org.bouncycastle.i18n.filter.UntrustedUrlInput;

public class LocalizedMessage {
    public static final String DEFAULT_ENCODING = "ISO-8859-1";
    protected FilteredArguments arguments;
    protected String encoding = "ISO-8859-1";
    protected FilteredArguments extraArgs = null;
    protected Filter filter = null;
    protected final String id;
    protected ClassLoader loader = null;
    protected final String resource;

    public LocalizedMessage(String string, String string2) {
        if (string == null || string2 == null) {
            throw new NullPointerException();
        }
        this.id = string2;
        this.resource = string;
        this.arguments = new FilteredArguments();
    }

    public LocalizedMessage(String string, String string2, String string3) {
        if (string == null || string2 == null) {
            throw new NullPointerException();
        }
        this.id = string2;
        this.resource = string;
        this.arguments = new FilteredArguments();
        if (!Charset.isSupported((String)string3)) {
            throw new UnsupportedEncodingException("The encoding \"" + string3 + "\" is not supported.");
        }
        this.encoding = string3;
    }

    public LocalizedMessage(String string, String string2, String string3, Object[] arrobject) {
        if (string == null || string2 == null || arrobject == null) {
            throw new NullPointerException();
        }
        this.id = string2;
        this.resource = string;
        this.arguments = new FilteredArguments(arrobject);
        if (!Charset.isSupported((String)string3)) {
            throw new UnsupportedEncodingException("The encoding \"" + string3 + "\" is not supported.");
        }
        this.encoding = string3;
    }

    public LocalizedMessage(String string, String string2, Object[] arrobject) {
        if (string == null || string2 == null || arrobject == null) {
            throw new NullPointerException();
        }
        this.id = string2;
        this.resource = string;
        this.arguments = new FilteredArguments(arrobject);
    }

    protected String addExtraArgs(String string, Locale locale) {
        if (this.extraArgs != null) {
            StringBuffer stringBuffer = new StringBuffer(string);
            Object[] arrobject = this.extraArgs.getFilteredArgs(locale);
            for (int i2 = 0; i2 < arrobject.length; ++i2) {
                stringBuffer.append(arrobject[i2]);
            }
            string = stringBuffer.toString();
        }
        return string;
    }

    protected String formatWithTimeZone(String string, Object[] arrobject, Locale locale, TimeZone timeZone) {
        MessageFormat messageFormat = new MessageFormat(" ");
        messageFormat.setLocale(locale);
        messageFormat.applyPattern(string);
        if (!timeZone.equals((Object)TimeZone.getDefault())) {
            Format[] arrformat = messageFormat.getFormats();
            for (int i2 = 0; i2 < arrformat.length; ++i2) {
                if (!(arrformat[i2] instanceof DateFormat)) continue;
                DateFormat dateFormat = (DateFormat)arrformat[i2];
                dateFormat.setTimeZone(timeZone);
                messageFormat.setFormat(i2, (Format)dateFormat);
            }
        }
        return messageFormat.format((Object)arrobject);
    }

    public Object[] getArguments() {
        return this.arguments.getArguments();
    }

    public ClassLoader getClassLoader() {
        return this.loader;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getEntry(String string, Locale locale, TimeZone timeZone) {
        String string2 = this.id;
        if (string != null) {
            string2 = string2 + "." + string;
        }
        try {
            ResourceBundle resourceBundle;
            ResourceBundle resourceBundle2 = this.loader == null ? ResourceBundle.getBundle((String)this.resource, (Locale)locale) : (resourceBundle = ResourceBundle.getBundle((String)this.resource, (Locale)locale, (ClassLoader)this.loader));
            String string3 = resourceBundle2.getString(string2);
            String string4 = !this.encoding.equals((Object)DEFAULT_ENCODING) ? new String(string3.getBytes(DEFAULT_ENCODING), this.encoding) : string3;
            if (this.arguments.isEmpty()) return this.addExtraArgs(string4, locale);
            string4 = this.formatWithTimeZone(string4, this.arguments.getFilteredArgs(locale), locale, timeZone);
            return this.addExtraArgs(string4, locale);
        }
        catch (MissingResourceException missingResourceException) {
            ClassLoader classLoader;
            String string5 = "Can't find entry " + string2 + " in resource file " + this.resource + ".";
            String string6 = this.resource;
            if (this.loader != null) {
                classLoader = this.loader;
                throw new MissingEntryException(string5, string6, string2, locale, classLoader);
            }
            classLoader = this.getClassLoader();
            throw new MissingEntryException(string5, string6, string2, locale, classLoader);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException((Throwable)unsupportedEncodingException);
        }
    }

    public Object[] getExtraArgs() {
        if (this.extraArgs == null) {
            return null;
        }
        return this.extraArgs.getArguments();
    }

    public Filter getFilter() {
        return this.filter;
    }

    public String getId() {
        return this.id;
    }

    public String getResource() {
        return this.resource;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }

    public void setExtraArgument(Object object) {
        this.setExtraArguments(new Object[]{object});
    }

    public void setExtraArguments(Object[] arrobject) {
        if (arrobject != null) {
            this.extraArgs = new FilteredArguments(arrobject);
            this.extraArgs.setFilter(this.filter);
            return;
        }
        this.extraArgs = null;
    }

    public void setFilter(Filter filter) {
        this.arguments.setFilter(filter);
        if (this.extraArgs != null) {
            this.extraArgs.setFilter(filter);
        }
        this.filter = filter;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Resource: \"").append(this.resource);
        stringBuffer.append("\" Id: \"").append(this.id).append("\"");
        stringBuffer.append(" Arguments: ").append(this.arguments.getArguments().length).append(" normal");
        if (this.extraArgs != null && this.extraArgs.getArguments().length > 0) {
            stringBuffer.append(", ").append(this.extraArgs.getArguments().length).append(" extra");
        }
        stringBuffer.append(" Encoding: ").append(this.encoding);
        stringBuffer.append(" ClassLoader: ").append((Object)this.loader);
        return stringBuffer.toString();
    }

    protected class FilteredArguments {
        protected static final int FILTER = 1;
        protected static final int FILTER_URL = 2;
        protected static final int NO_FILTER;
        protected int[] argFilterType;
        protected Object[] arguments;
        protected Filter filter = null;
        protected Object[] filteredArgs;
        protected boolean[] isLocaleSpecific;
        protected Object[] unpackedArgs;

        FilteredArguments() {
            this(new Object[0]);
        }

        /*
         * Enabled aggressive block sorting
         */
        FilteredArguments(Object[] arrobject) {
            this.arguments = arrobject;
            this.unpackedArgs = new Object[arrobject.length];
            this.filteredArgs = new Object[arrobject.length];
            this.isLocaleSpecific = new boolean[arrobject.length];
            this.argFilterType = new int[arrobject.length];
            int n2 = 0;
            while (n2 < arrobject.length) {
                if (arrobject[n2] instanceof TrustedInput) {
                    this.unpackedArgs[n2] = ((TrustedInput)arrobject[n2]).getInput();
                    this.argFilterType[n2] = 0;
                } else if (arrobject[n2] instanceof UntrustedInput) {
                    this.unpackedArgs[n2] = ((UntrustedInput)arrobject[n2]).getInput();
                    this.argFilterType[n2] = arrobject[n2] instanceof UntrustedUrlInput ? 2 : 1;
                } else {
                    this.unpackedArgs[n2] = arrobject[n2];
                    this.argFilterType[n2] = 1;
                }
                this.isLocaleSpecific[n2] = this.unpackedArgs[n2] instanceof LocaleString;
                ++n2;
            }
            return;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private Object filter(int n2, Object object) {
            if (this.filter == null) return object;
            if (object == null) {
                object = "null";
            }
            switch (n2) {
                default: {
                    object = null;
                }
                case 0: {
                    return object;
                }
                case 1: {
                    return this.filter.doFilter(object.toString());
                }
                case 2: 
            }
            return this.filter.doFilterUrl(object.toString());
        }

        public Object[] getArguments() {
            return this.arguments;
        }

        public Filter getFilter() {
            return this.filter;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Object[] getFilteredArgs(Locale locale) {
            Object[] arrobject = new Object[this.unpackedArgs.length];
            int n2 = 0;
            while (n2 < this.unpackedArgs.length) {
                Object object;
                if (this.filteredArgs[n2] != null) {
                    object = this.filteredArgs[n2];
                } else {
                    Object object2 = this.unpackedArgs[n2];
                    if (this.isLocaleSpecific[n2]) {
                        String string = ((LocaleString)object2).getLocaleString(locale);
                        object = this.filter(this.argFilterType[n2], string);
                    } else {
                        this.filteredArgs[n2] = object = this.filter(this.argFilterType[n2], object2);
                    }
                }
                arrobject[n2] = object;
                ++n2;
            }
            return arrobject;
        }

        public boolean isEmpty() {
            return this.unpackedArgs.length == 0;
        }

        public void setFilter(Filter filter) {
            if (filter != this.filter) {
                for (int i2 = 0; i2 < this.unpackedArgs.length; ++i2) {
                    this.filteredArgs[i2] = null;
                }
            }
            this.filter = filter;
        }
    }

}

