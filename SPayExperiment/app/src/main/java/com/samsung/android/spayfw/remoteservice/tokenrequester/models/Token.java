/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Token {
    private Card card;
    private String id;

    public Token(String string, String string2, String string3) {
        this.id = string;
        this.card = new Card();
        this.card.brand = string2;
        this.card.merchantId = string3;
    }

    private static class Card {
        private String brand;
        private String merchantId;

        private Card() {
        }
    }

}

