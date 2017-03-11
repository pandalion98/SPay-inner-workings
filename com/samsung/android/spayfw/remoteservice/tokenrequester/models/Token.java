package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Token {
    private Card card;
    private String id;

    private static class Card {
        private String brand;
        private String merchantId;

        private Card() {
        }
    }

    public Token(String str, String str2, String str3) {
        this.id = str;
        this.card = new Card();
        this.card.brand = str2;
        this.card.merchantId = str3;
    }
}
