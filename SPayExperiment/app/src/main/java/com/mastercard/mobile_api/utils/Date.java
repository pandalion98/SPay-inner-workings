/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mobile_api.utils;

public class Date {
    int day;
    int month;
    int year;

    public Date(int n2, int n3, int n4) {
        this.year = n2;
        this.month = n3;
        this.day = n4;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public void setDay(int n2) {
        this.day = n2;
    }

    public void setMonth(int n2) {
        this.month = n2;
    }

    public void setYear(int n2) {
        this.year = n2;
    }

    public String toString() {
        return "Date{year=" + this.year + ", day=" + this.day + ", month=" + this.month + '}';
    }
}

