/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.net.InetAddress
 *  java.net.UnknownHostException
 */
package com.squareup.okhttp.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface Network {
    public static final Network DEFAULT = new Network(){

        @Override
        public InetAddress[] resolveInetAddresses(String string) {
            if (string == null) {
                throw new UnknownHostException("host == null");
            }
            return InetAddress.getAllByName((String)string);
        }
    };

    public InetAddress[] resolveInetAddresses(String var1);

}

