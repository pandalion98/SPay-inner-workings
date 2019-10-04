/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.Date
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  java.util.TreeSet
 */
package com.squareup.okhttp;

import com.squareup.okhttp.internal.http.HttpDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class Headers {
    private final String[] namesAndValues;

    private Headers(Builder builder) {
        this.namesAndValues = (String[])builder.namesAndValues.toArray((Object[])new String[builder.namesAndValues.size()]);
    }

    private Headers(String[] arrstring) {
        this.namesAndValues = arrstring;
    }

    private static String get(String[] arrstring, String string) {
        for (int i2 = -2 + arrstring.length; i2 >= 0; i2 -= 2) {
            if (!string.equalsIgnoreCase(arrstring[i2])) continue;
            return arrstring[i2 + 1];
        }
        return null;
    }

    public static Headers of(Map<String, String> map) {
        if (map == null) {
            throw new IllegalArgumentException("Expected map with header names and values");
        }
        String[] arrstring = new String[2 * map.size()];
        Iterator iterator = map.entrySet().iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            String string = ((String)entry.getKey()).trim();
            String string2 = ((String)entry.getValue()).trim();
            if (string.length() == 0 || string.indexOf(0) != -1 || string2.indexOf(0) != -1) {
                throw new IllegalArgumentException("Unexpected header: " + string + ": " + string2);
            }
            arrstring[n2] = string;
            arrstring[n2 + 1] = string2;
            n2 += 2;
        }
        return new Headers(arrstring);
    }

    public static /* varargs */ Headers of(String ... arrstring) {
        if (arrstring == null || arrstring.length % 2 != 0) {
            throw new IllegalArgumentException("Expected alternating header names and values");
        }
        String[] arrstring2 = (String[])arrstring.clone();
        for (int i2 = 0; i2 < arrstring2.length; ++i2) {
            if (arrstring2[i2] == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            arrstring2[i2] = arrstring2[i2].trim();
        }
        for (int i3 = 0; i3 < arrstring2.length; i3 += 2) {
            String string = arrstring2[i3];
            String string2 = arrstring2[i3 + 1];
            if (string.length() != 0 && string.indexOf(0) == -1 && string2.indexOf(0) == -1) continue;
            throw new IllegalArgumentException("Unexpected header: " + string + ": " + string2);
        }
        return new Headers(arrstring2);
    }

    public String get(String string) {
        return Headers.get(this.namesAndValues, string);
    }

    public Date getDate(String string) {
        String string2 = this.get(string);
        if (string2 != null) {
            return HttpDate.parse(string2);
        }
        return null;
    }

    public String name(int n2) {
        int n3 = n2 * 2;
        if (n3 < 0 || n3 >= this.namesAndValues.length) {
            return null;
        }
        return this.namesAndValues[n3];
    }

    public Set<String> names() {
        TreeSet treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        int n2 = this.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            treeSet.add((Object)this.name(i2));
        }
        return Collections.unmodifiableSet((Set)treeSet);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        Collections.addAll((Collection)builder.namesAndValues, (Object[])this.namesAndValues);
        return builder;
    }

    public int size() {
        return this.namesAndValues.length / 2;
    }

    public Map<String, List<String>> toMultimap() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int n2 = this.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = this.name(i2);
            List list = (List)linkedHashMap.get((Object)string);
            if (list == null) {
                list = new ArrayList(2);
                linkedHashMap.put((Object)string, (Object)list);
            }
            list.add((Object)this.value(i2));
        }
        return linkedHashMap;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = this.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append(this.name(i2)).append(": ").append(this.value(i2)).append("\n");
        }
        return stringBuilder.toString();
    }

    public String value(int n2) {
        int n3 = 1 + n2 * 2;
        if (n3 < 0 || n3 >= this.namesAndValues.length) {
            return null;
        }
        return this.namesAndValues[n3];
    }

    public List<String> values(String string) {
        int n2 = this.size();
        ArrayList arrayList = null;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!string.equalsIgnoreCase(this.name(i2))) continue;
            if (arrayList == null) {
                arrayList = new ArrayList(2);
            }
            arrayList.add((Object)this.value(i2));
        }
        if (arrayList != null) {
            return Collections.unmodifiableList(arrayList);
        }
        return Collections.emptyList();
    }

    public static final class Builder {
        private final List<String> namesAndValues = new ArrayList(20);

        public Builder add(String string) {
            int n2 = string.indexOf(":");
            if (n2 == -1) {
                throw new IllegalArgumentException("Unexpected header: " + string);
            }
            return this.add(string.substring(0, n2).trim(), string.substring(n2 + 1));
        }

        public Builder add(String string, String string2) {
            if (string == null) {
                throw new IllegalArgumentException("name == null");
            }
            if (string2 == null) {
                throw new IllegalArgumentException("value == null");
            }
            if (string.length() == 0 || string.indexOf(0) != -1 || string2.indexOf(0) != -1) {
                throw new IllegalArgumentException("Unexpected header: " + string + ": " + string2);
            }
            return this.addLenient(string, string2);
        }

        Builder addLenient(String string) {
            int n2 = string.indexOf(":", 1);
            if (n2 != -1) {
                return this.addLenient(string.substring(0, n2), string.substring(n2 + 1));
            }
            if (string.startsWith(":")) {
                return this.addLenient("", string.substring(1));
            }
            return this.addLenient("", string);
        }

        Builder addLenient(String string, String string2) {
            this.namesAndValues.add((Object)string);
            this.namesAndValues.add((Object)string2.trim());
            return this;
        }

        public Headers build() {
            return new Headers(this);
        }

        public String get(String string) {
            for (int i2 = -2 + this.namesAndValues.size(); i2 >= 0; i2 -= 2) {
                if (!string.equalsIgnoreCase((String)this.namesAndValues.get(i2))) continue;
                return (String)this.namesAndValues.get(i2 + 1);
            }
            return null;
        }

        public Builder removeAll(String string) {
            for (int i2 = 0; i2 < this.namesAndValues.size(); i2 += 2) {
                if (!string.equalsIgnoreCase((String)this.namesAndValues.get(i2))) continue;
                this.namesAndValues.remove(i2);
                this.namesAndValues.remove(i2);
                i2 -= 2;
            }
            return this;
        }

        public Builder set(String string, String string2) {
            this.removeAll(string);
            this.add(string, string2);
            return this;
        }
    }

}

