package com.mastercard.mobile_api.utils;

public class Date {
    int day;
    int month;
    int year;

    public Date(int i, int i2, int i3) {
        this.year = i;
        this.month = i2;
        this.day = i3;
    }

    public String toString() {
        return "Date{year=" + this.year + ", day=" + this.day + ", month=" + this.month + '}';
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int i) {
        this.year = i;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int i) {
        this.day = i;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int i) {
        this.month = i;
    }
}
